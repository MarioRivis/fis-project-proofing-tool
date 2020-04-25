package org.loose.fis.project.proofing.tool.actions;

import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportIssue;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportResult;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportUser;
import org.loose.fis.project.proofing.tool.jira.client.export.ResultExporter;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssueFieldsService;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssuesService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JiraActions {

    public static final String LINE_FORMAT = "%-5s | %-30s | %-9s | %-9s | %-9s | %-9s\n";
    public static final String SPACES = "    ";
    public static final String STORY_POINTS = "Story Points";

    public static void getJiraSummary() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();
        studentResponses.forEach(studentResponse -> {
            IssuesService issuesService = new IssuesService(studentResponse.getJiraUrl(), Config.getJiraCredentials());
            IssueFieldsService issueFieldsService = new IssueFieldsService(studentResponse.getJiraUrl(), Config.getJiraCredentials());
            ResultExporter resultExporter = new ResultExporter();

            List<Issue> issues = issuesService.getAllIssuesForProjects(studentResponse.getJiraProjectId());
            List<IssueField> issueFields = issueFieldsService.getFields();

            ExportResult exportResult = resultExporter.getExportResult(issues, issueFields);

            Map<String, ExportUser> usersMap = exportResult.getUsers().stream().collect(Collectors.toMap(ExportUser::getId, Function.identity()));


            printHeader();
            exportResult.getIssues().stream()
                    .filter(exportIssue -> exportIssue.getType().equalsIgnoreCase("story"))
                    .forEach(exportIssue -> {
                        System.out.printf(LINE_FORMAT, exportIssue.getId(),
                                formatSummary(exportIssue.getSummary()),
                                formatBoolean(exportIssue.getAssigneeId() != null),
                                formatBoolean(isEstimated(exportIssue)),
                                formatInteger(getDescriptionLinesNumber(exportIssue)),
                                formatInteger(exportIssue.getSubTasks().size())
                        );
                    });

        });
    }

    private static String formatInteger(Integer number) {
        int numberOfSpaces = (9 - number.toString().length()) / 2;
        String spaces = IntStream.range(0, numberOfSpaces).mapToObj(operand -> " ").collect(Collectors.joining());
        return spaces + number + spaces;
    }

    private static int getDescriptionLinesNumber(ExportIssue exportIssue) {
        return (int) (Arrays.stream(exportIssue.getDescription().split("\n"))
                .filter(s -> !s.trim().isEmpty())
                .count() - 1);
    }

    private static boolean isEstimated(ExportIssue exportIssue) {
        return exportIssue.getCustomFields().get(STORY_POINTS) != null || exportIssue.getTimeEstimate() != null;
    }

    private static String formatBoolean(boolean b) {
        return SPACES + (b ? "X" : "-") + SPACES;
    }

    private static String formatSummary(String summary) {
        if (summary.length() > 30) {
            return summary.substring(0, 26) + "...";
        }
        return summary;
    }

    private static void printHeader() {
        System.out.printf(LINE_FORMAT, "ID", "Summary", "Assigned", "Estimated", "DescLen", "Subtasks");
    }
}
