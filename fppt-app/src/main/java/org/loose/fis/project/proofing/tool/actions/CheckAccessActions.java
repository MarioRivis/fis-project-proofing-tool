package org.loose.fis.project.proofing.tool.actions;

import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.github.client.repository.actions.secrets.GithubSecretsService;
import org.loose.fis.project.proofing.tool.jira.client.projects.ProjectsService;

import java.util.List;

public class CheckAccessActions {

    public static void checkGithubAccessAction() {

        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();

        studentResponses.forEach(CheckAccessActions::checkGithubAccessForStudentResponse);
    }

    public static void checkJiraAccessAction() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();

        studentResponses.forEach(CheckAccessActions::checkJiraAccessForStudentResponse);
    }

    public static void checkAccessAction() {
        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();

        studentResponses.forEach(studentResponse -> {
            checkGithubAccessForStudentResponse(studentResponse);
            checkJiraAccessForStudentResponse(studentResponse);
            System.out.println();
        });
    }

    static boolean checkGithubAccessForStudentResponse(StudentResponse studentResponse) {
        GithubSecretsService githubSecretsService = new GithubSecretsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Checking Github access to %s [%s]............", studentResponse.getRepoUrl(), studentResponse.getName());
        try {
            githubSecretsService.listAllSecretsForRepo();
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }

    static boolean checkJiraAccessForStudentResponse(StudentResponse studentResponse) {
        ProjectsService projectsService = new ProjectsService(studentResponse.getJiraUrl(), Config.getJiraCredentials());
        System.out.printf("Checking Jira access to %s [%s]............", studentResponse.getJiraUrl(), studentResponse.getName());
        try {
            projectsService.getProject(studentResponse.getJiraProjectId());
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }
}
