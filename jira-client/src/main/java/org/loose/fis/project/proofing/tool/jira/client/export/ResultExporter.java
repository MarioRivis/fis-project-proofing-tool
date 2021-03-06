package org.loose.fis.project.proofing.tool.jira.client.export;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.*;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.comments.IssueComment;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.users.User;
import org.loose.fis.project.proofing.tool.utils.JsonMapper;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class ResultExporter {

	public static final String AVATAR_RESOLUTION = "32x32";
	public static final String DESCRIPTION = "description";

	public void export(List<Issue> issues, Path toPath) {
		export(issues, toPath, emptyList());
	}

	@SneakyThrows
	public void export(List<Issue> issues, Path toPath, List<IssueField> customFields) {
		ExportResult exportResult = getExportResult(issues, customFields);

		new JsonMapper().writeJSON(new FileWriter(toPath.toFile()), exportResult);
	}

	public ExportResult getExportResult(List<Issue> issues, List<IssueField> customFields) {
		List<ExportUser> exportUsers = issues.stream()
				.flatMap(issue -> Stream.of(issue.getCreator(), issue.getReporter(), issue.getAssignee()))
				.filter(user -> user.getAccountId() != null).distinct()
				.map(user -> ExportUser.builder().id(user.getAccountId()).name(user.getDisplayName())
						.avatarUrl((String) user.getAvatarUrls().get(AVATAR_RESOLUTION)).build()).collect(Collectors.toList());

		List<ExportIssueType> exportIssueTypes = issues.stream().map(Issue::getIssuetype).distinct()
				.map(type -> ExportIssueType.builder().id(type.getId()).name(type.getName())
						.description(type.getDescription()).isSubTask(type.isSubTask()).build())
				.collect(Collectors.toList());

		List<ExportIssue> exportIssues = getExportIssues(issues, customFields);

		return ExportResult.builder().users(exportUsers).issueTypes(exportIssueTypes)
				.issues(exportIssues).build();
	}

	private List<ExportIssue> getExportIssues(List<Issue> issues, List<IssueField> customFields) {
		return issues.stream()
				.map(issue -> ExportIssue.builder().key(issue.getKey()).id(issue.getId()).self(issue.getSelf())
						.summary(issue.getSummary()).description(getDescription(issue))
						.status(issue.getStatus().getName()).typeId(issue.getIssuetype().getId())
						.type(issue.getIssuetype().getName()).created(issue.getCreated()).updated(issue.getUpdated())
						.creatorId(getUserId(issue.getCreator())).reporterId(getUserId(issue.getReporter()))
						.assigneeId(getUserId(issue.getAssignee())).priority(issue.getPriority().getName())
						.parent(getParent(issue)).subTasks(getSubtasks(issue)).changes(getChanges(issue))
						.comments(getComments(issue)).timeEstimate(issue.getTimeestimate())
						.customFields(getCustomFields(issue, customFields)).build()).collect(Collectors.toList());
	}

	private List<String> getSubtasks(Issue issue) {
		return issue.getSubtasks().stream().map(Issue::getKey).collect(Collectors.toList());
	}

	private String getParent(Issue issue) {
		return Optional.ofNullable(issue.getParent()).map(Issue::getKey).orElse(null);
	}

	private Map<String, Object> getCustomFields(Issue issue, List<IssueField> customFields) {
		return customFields.stream().map(field -> new ImmutablePair<>(field.getName(), issue.get(field.getId())))
				.filter(pair -> pair.getRight() != null)
				.collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
	}

	private String getDescription(Issue issue) {
		return issue.getChangelog().getChanges().stream()
				.filter(change -> change.getItems().stream().anyMatch(this::isDescription))
				.max(Comparator.comparing(IssueChange::getCreated))
				.flatMap(change -> change.getItems().stream().filter(this::isDescription).findFirst())
				.map(ChangeItem::getToString).orElse("");
	}

	private boolean isDescription(ChangeItem item) {
		return item.getField().equals(DESCRIPTION);
	}

	private List<ExportComment> getComments(Issue issue) {
		List<IssueComment> comments = issue.getComments();
		if (comments == null)
			return emptyList();
		return comments.stream().map(comment -> ExportComment.builder().userId(comment.getAuthor().getAccountId())
				.created(comment.getCreated()).updateUserId(comment.getUpdateAuthor().getAccountId())
				.updated(comment.getUpdated()).body(comment.getBody()).build()).collect(Collectors.toList());
	}

	private List<ExportChange> getChanges(Issue issue) {
		ChangeLog changelog = issue.getChangelog();
		if (changelog == null)
			return emptyList();
		return changelog.getChanges().stream()
				.map(change -> ExportChange.builder().id(change.getId()).userId(change.getAuthor().getAccountId())
						.created(change.getCreated()).changedFields(
								change.getItems().stream().map(ChangeItem::getField).collect(Collectors.toList()))
						.items(change.getItems().stream().map(item -> ExportChangeItem.builder().field(item.getField())
								.from(item.getFromString()).to(item.getToString()).build())
								.collect(Collectors.toList())).build()).collect(Collectors.toList());
	}

	private String getUserId(User user) {
		if (user != null)
			return user.getAccountId();
		return "";
	}
}
