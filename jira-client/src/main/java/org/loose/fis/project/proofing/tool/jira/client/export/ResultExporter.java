package org.loose.fis.project.proofing.tool.jira.client.export;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.ChangeItem;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueChange;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.users.User;
import org.loose.fis.project.proofing.tool.utils.JsonMapper;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class ResultExporter {
	public void export(List<Issue> issues, Path toPath) {
		export(issues, toPath, emptyList());
	}

	@SneakyThrows
	public void export(List<Issue> issues, Path toPath, List<IssueField> customFields) {
		List<ExportUser> exportUsers = issues.stream()
				.flatMap(issue -> Stream.of(issue.getCreator(), issue.getReporter(), issue.getAssignee()))
				.filter(user -> user.getAccountId() != null).distinct()
				.map(user -> ExportUser.builder().id(user.getAccountId()).name(user.getDisplayName())
						.avatarUrl((String) user.getAvatarUrls().get("32x32")).build()).collect(Collectors.toList());

		List<ExportIssueType> exportIssueTypes = issues.stream().map(Issue::getIssuetype).distinct()
				.map(type -> ExportIssueType.builder().id(type.getId()).name(type.getName())
						.description(type.getDescription()).isSubTask(type.isSubTask()).build())
				.collect(Collectors.toList());

		List<ExportIssue> exportIssues = issues.stream()
				.map(issue -> ExportIssue.builder().key(issue.getKey()).id(issue.getId()).self(issue.getSelf())
						.summary(issue.getSummary()).description(getDescription(issue))
						.status(issue.getStatus().getName()).typeId(issue.getIssuetype().getId())
						.type(issue.getIssuetype().getName()).created(issue.getCreated()).updated(issue.getUpdated())
						.creatorId(getUserId(issue.getCreator())).reporterId(getUserId(issue.getReporter()))
						.assigneeId(getUserId(issue.getAssignee())).priority(issue.getPriority().getName())
						.subTasks(issue.getSubtasks().stream().map(Issue::getId).collect(Collectors.toList()))
						.changes(getChanges(issue)).comments(getComments(issue))
						.customFields(getCustomFields(issue, customFields)).build()).collect(Collectors.toList());

		ExportResult exportResult = ExportResult.builder().users(exportUsers).issueTypes(exportIssueTypes)
				.issues(exportIssues).build();

		new JsonMapper().writeJSON(new FileWriter(toPath.toFile()), exportResult);
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
		return item.getField().equals("description");
	}

	private List<ExportComment> getComments(Issue issue) {
		return emptyList();
	}

	private List<ExportChange> getChanges(Issue issue) {
		return issue.getChangelog().getChanges().stream()
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
