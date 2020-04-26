package org.loose.fis.project.proofing.tool.actions;

import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizeActions {

    public static void normalizeJiraUrl() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getAllForTeacher();

        studentResponses.forEach(studentResponse -> {
            String jiraUrl = studentResponse.getJiraUrl();

            String suffix = ".atlassian.net";
            String atlassianRegex = "\\.atlassian\\.net";
            String prefix = "https://";
            if (jiraUrl.startsWith(prefix) && jiraUrl.endsWith(suffix))
                return;

            String[] split = jiraUrl.split(atlassianRegex);
            if (split.length != 2 || !jiraUrl.startsWith(prefix)) {
                System.err.printf("Url [%s] from %s does not match the expected format!\n", jiraUrl, studentResponse.getName());
                return;
            }
            String normalizedUrl = split[0] + suffix;

            if (!jiraUrl.equalsIgnoreCase(normalizedUrl)) {
                System.out.printf("Modified for %s from [%s] to [%s]\n", studentResponse.getName(), jiraUrl, normalizedUrl);
                studentResponse.setJiraUrl(normalizedUrl);
            }
        });

        StudentsRegistry.getInstance().persist();
    }

    public static void normalizeGithubUrl() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getAllForTeacher();

        studentResponses.forEach(studentResponse -> {
            String repoUrl = studentResponse.getRepoUrl();
            String githubUrlPrefix = "https://github.com/";
            if (!repoUrl.startsWith(githubUrlPrefix)) {
                System.err.printf("Url [%s] from %s does not match the expected format!\n", repoUrl, studentResponse.getName());
                return;
            }
            String afterPrefix = repoUrl.replace(githubUrlPrefix, "");
            boolean modified = false;
            if (afterPrefix.endsWith(".git")) {
                afterPrefix = afterPrefix.substring(0, afterPrefix.length() - 4);
                modified = true;
            }

            String[] pathVariables = afterPrefix.split("/");
            if (pathVariables.length < 2) {
                System.err.printf("Url [%s] from %s does not match the expected format!\n", repoUrl, studentResponse.getName());
                return;
            } else if (pathVariables.length > 2) {
                modified = true;
                afterPrefix = Arrays.asList(pathVariables[0], pathVariables[1]).stream().collect(Collectors.joining("/"));
            }

            if (modified) {
                String normalizedUrl = githubUrlPrefix + afterPrefix;
                System.out.printf("Modified for %s from [%s] to [%s]\n", studentResponse.getName(), repoUrl, normalizedUrl);
                studentResponse.setRepoUrl(normalizedUrl);
            }
        });

        StudentsRegistry.getInstance().persist();
    }

}
