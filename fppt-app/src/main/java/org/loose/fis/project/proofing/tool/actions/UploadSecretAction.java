package org.loose.fis.project.proofing.tool.actions;

import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.github.client.repository.actions.secrets.GithubSecretsService;

public class UploadSecretAction {

    private static final String JIRA_SECRET_NAME = "JIRA_TOKEN";

    public static boolean createJiraTokenSecretFor(StudentResponse studentResponse) {
        GithubSecretsService githubSecretsService = new GithubSecretsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Creating Jira Secret for %s............", studentResponse.getRepoUrl());
        try {
            githubSecretsService.createSecret(JIRA_SECRET_NAME, Config.getJiraToken());
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }
}
