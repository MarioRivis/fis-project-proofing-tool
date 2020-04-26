package org.loose.fis.project.proofing.tool.actions;

import com.google.api.client.util.Data;
import org.apache.commons.lang3.StringUtils;
import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.Issue;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.issues.IssueField;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportIssue;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportIssueType;
import org.loose.fis.project.proofing.tool.jira.client.export.ExportResult;
import org.loose.fis.project.proofing.tool.jira.client.export.ResultExporter;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssueFieldsService;
import org.loose.fis.project.proofing.tool.jira.client.issues.IssuesService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JiraActions {

    public static final int SUMMARY_LENGTH = 50;
    public static final String LINE_FORMAT = " | %-20s | %-" + SUMMARY_LENGTH + "s | %-9s | %-9s | %-9s | %-9s |\n";
    public static final String SPACES = "    ";
    public static final String STORY_POINTS = "Story Points";
    public static final Path FOLDER_PATH = Paths.get(System.getProperty("user.home"), "fppt");
    public static PrintStream OUT = System.out;

    public static void getJiraSummary() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();
        studentResponses.forEach(studentResponse -> {
            try {
                boolean access = CheckAccessActions.checkJiraAccessForStudentResponse(studentResponse);
                if (!access)
                    throw new RuntimeException("Could not access " + studentResponse.getJiraUrl());

                System.out.printf("Getting issues for %s-[%d] [%s]......\n", studentResponse.getName(), studentResponse.getId(), studentResponse.getRepoUrl());

                IssuesService issuesService = new IssuesService(studentResponse.getJiraUrl(), Config.getJiraCredentials());
                IssueFieldsService issueFieldsService = new IssueFieldsService(studentResponse.getJiraUrl(), Config.getJiraCredentials());
                ResultExporter resultExporter = new ResultExporter();

                List<Issue> issues = issuesService.getAllIssuesForProjects(studentResponse.getJiraProjectId());
                List<IssueField> issueFields = issueFieldsService.getFields();

                Path studentFilePath = Paths.get(".", studentResponse.getName(), "jiraSummary.txt");


                ExportResult exportResult = resultExporter.getExportResult(issues, issueFields);
                List<ExportIssue> exportIssues = exportResult.getIssues();
                Map<String, ExportIssue> keyToIssuesMap = exportIssues.stream().collect(Collectors.toMap(ExportIssue::getKey, Function.identity()));
                Map<String, ExportIssueType> issueTypeMap = exportResult.getIssueTypes().stream().collect(Collectors.toMap(ExportIssueType::getId, Function.identity()));


                File studentFile = FOLDER_PATH.resolve(studentFilePath).toFile();
                studentFile.getParentFile().mkdirs();
                try (PrintStream ps = new PrintStream(studentFile)) {
                    OUT = ps;
                    printHeader();
                    exportIssues.stream()
                            .filter(exportIssue -> !issueTypeMap.get(exportIssue.getTypeId()).isSubTask())
                            .sorted(Comparator.comparingInt(exportIssue -> getIssueTypeSortValue(exportIssue.getType())))
                            .forEach(exportIssue -> {
                                OUT.printf(LINE_FORMAT,
                                        exportIssue.getKey() + "[" + exportIssue.getType().toUpperCase().charAt(0) + "]",
                                        formatSummary(exportIssue.getSummary()),
                                        formatBoolean(hasAssignee(exportIssue)),
                                        formatBoolean(isEstimated(exportIssue)),
                                        formatInteger(getDescriptionLinesNumber(exportIssue)),
                                        formatInteger(exportIssue.getSubTasks().size())
                                );
                                exportIssue.getSubTasks().stream()
                                        .map(keyToIssuesMap::get)
                                        .filter(Objects::nonNull)
                                        .forEach(subTask -> OUT.printf(LINE_FORMAT,
                                                "  \\--" + subTask.getKey(),
                                                formatSummary(subTask.getSummary()),
                                                formatBoolean(hasAssignee(subTask)),
                                                formatBoolean(isEstimated(subTask)),
                                                formatInteger(getDescriptionLinesNumber(subTask)),
                                                ""
                                        ));
                                printLineSeparators();
                            });
                    printFooter(exportIssues);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("SUCCESSFUL");
            } catch (Exception e) {
                System.err.println("FAILED");
                e.printStackTrace();
            }
        });
    }

    private static void printLineSeparators() {
        OUT.println(" |" + createNCharacters(123, "-") + "|");
    }

    private static String formatInteger(Integer number) {
        int numberOfSpaces = (9 - number.toString().length()) / 2;
        String spaces = createNCharacters(numberOfSpaces, " ");
        return spaces + number + spaces;
    }

    private static String createNCharacters(int numberOfSpaces, String s) {
        return IntStream.range(0, numberOfSpaces).mapToObj(operand -> s).collect(Collectors.joining());
    }

    private static int getDescriptionLinesNumber(ExportIssue exportIssue) {
        return (int) (Arrays.stream(exportIssue.getDescription().split("\n"))
                .filter(s -> !s.trim().isEmpty())
                .count());
    }

    private static boolean isEstimated(ExportIssue exportIssue) {
        return hasStoryPoints(exportIssue)
                || !Data.isNull(exportIssue.getTimeEstimate());
    }

    private static boolean hasStoryPoints(ExportIssue exportIssue) {
        Object o = exportIssue.getCustomFields().get(STORY_POINTS);
        if (o == null || Data.isNull(o))
            return false;

        return true;
    }

    private static String formatBoolean(boolean b) {
        return SPACES + (b ? "X" : "-") + SPACES;
    }

    private static String formatSummary(String summary) {
        if (summary.length() > SUMMARY_LENGTH) {
            return summary.substring(0, SUMMARY_LENGTH - 4) + "...";
        }
        return summary;
    }

    private static void printHeader() {
        printLineSeparators();
        OUT.printf(LINE_FORMAT, "ID", "Summary", "Assigned", "Estimated", "Desc Len", "Subtasks");
        printLineSeparators();
    }

    private static void printFooter(List<ExportIssue> exportIssues) {
        long assignedIssues = exportIssues.stream()
                .filter(JiraActions::hasAssignee)
                .count();
        long estimatedIssues = exportIssues.stream()
                .filter(JiraActions::isEstimated)
                .count();
        int descLinesAvg = (int) exportIssues.stream()
                .mapToInt(JiraActions::getDescriptionLinesNumber)
                .average().orElse(0);
        int numberOfSubtasksAvg = (int) exportIssues.stream()
                .filter(exportIssue -> exportIssue.getParent() != null)
                .mapToInt(exportIssue -> exportIssue.getSubTasks().size())
                .average().orElse(0);
        OUT.printf(LINE_FORMAT, "Total",
                formatInteger(exportIssues.size()),
                formatInteger((int) assignedIssues),
                formatInteger((int) estimatedIssues),
                formatInteger(descLinesAvg),
                formatInteger(numberOfSubtasksAvg));
        printLineSeparators();
    }

    private static boolean hasAssignee(ExportIssue exportIssue) {
        return StringUtils.isNotBlank(exportIssue.getAssigneeId());
    }

    private static int getIssueTypeSortValue(String issueType) {
        if (issueType == null)
            return Integer.MAX_VALUE;

        if (issueType.equalsIgnoreCase("story"))
            return 1;
        if (issueType.equalsIgnoreCase("task"))
            return 2;
        if (issueType.equalsIgnoreCase("bug"))
            return 3;

        return Integer.MAX_VALUE;
    }
}
