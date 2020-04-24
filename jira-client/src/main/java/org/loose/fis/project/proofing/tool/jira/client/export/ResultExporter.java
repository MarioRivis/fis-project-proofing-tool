package org.loose.fis.project.proofing.tool.jira.client.export;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.ChangeItem;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.users.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultExporter {
	@SneakyThrows
	public void export(List<Issue> issues, Path toPath) {
		List<ExportUser> exportUsers = issues.stream()
				.flatMap(issue -> Stream.of(issue.getCreator(), issue.getReporter(), issue.getAssignee()))
				.filter(Objects::nonNull).distinct()
				.map(user -> ExportUser.builder().id(user.getAccountId()).name(user.getDisplayName())
						.avatarUrl((String) user.getAvatarUrls().get("32x32")).build()).collect(Collectors.toList());

		List<ExportIssueType> exportIssueTypes = issues.stream().map(Issue::getIssuetype).distinct()
				.map(type -> ExportIssueType.builder().id(type.getId()).name(type.getName())
						.description(type.getDescription()).isSubTask(type.isSubTask()).build())
				.collect(Collectors.toList());

		List<ExportIssue> exportIssues = issues.stream()
				.map(issue -> ExportIssue.builder().key(issue.getKey()).id(issue.getId()).self(issue.getSelf())
						.summary(issue.getSummary()).description(issue.getDescription())
						.status(issue.getStatus().getName()).typeId(issue.getIssuetype().getId())
						.type(issue.getIssuetype().getName()).created(issue.getCreated()).updated(issue.getUpdated())
						.creatorId(getUserId(issue.getCreator())).reporterId(getUserId(issue.getReporter()))
						.assigneeId(getUserId(issue.getAssignee())).priority(issue.getPriority().getName())
						.subTasks(issue.getSubtasks().stream().map(Issue::getId).collect(Collectors.toList()))
						.changes(getChanges(issue)).comments(getComments(issue)).build()).collect(Collectors.toList());

		ExportResult exportResult = ExportResult.builder().users(exportUsers).issueTypes(exportIssueTypes)
				.issues(exportIssues).build();

		Files.write(toPath, Collections.singleton(exportResult.toString()));
	}

	private List<ExportComment> getComments(Issue issue) {
		return Collections.emptyList();
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
