package org.loose.fis.project.proofing.tool.actions;

import org.apache.commons.io.FileUtils;
import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents.CreateFileRequestBody;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.pullrequests.CreatePullRequestBody;
import org.loose.fis.project.proofing.tool.github.client.repository.contents.GithubContentsService;
import org.loose.fis.project.proofing.tool.github.client.repository.pullrequests.GithubPullRequestsService;
import org.loose.fis.project.proofing.tool.github.client.repository.refs.GithubRefsService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class WorkflowActions {

    private static final String MAVEN = "maven";
    private static final String GRADLE = "gradle";
    private static final String MAVEN_RESOURCE = "BuildResultMaven.yml";
    private static final String GRADLE_RESOURCE = "BuildResultGradle.yml";
    private static final String GITHUB_WORKFLOWS_BUILD_YML = ".github/workflows/Build.yml";

    private static final String MASTER = "master";
    private static final String BUILD_ACTIONS_BRANCH = "build-actions";

    private static final String BUILD_ACTION_MESSAGE = "Added Build.yml workflow to build the application on every push or pull request to Master branch";
    private static final String PULL_REQUEST_BODY = "After you merge this Pull Request, a job will be run by Github every time you create a Pull Request" +
            "for the 'master' branch, of every time commits get pushed to the master branch.\n" +
            "This way you will know if your application builds everytime you want to merge some changes to the holy 'master' branch.\n" +
            "If you look just know, you should have a job running, or already finished. Hopefully it didn't fail ;)\n" +
            "Anytime you want to see the result of these runs, you can find them under the Actions tab just on top of the repo page.\n" +
            "Now go ahead and merge this PR! Then make sure your build succeeded on the master branch. If it didn't, fix it, ASAP!!!";

    public static Void createWorkflowFile(String... args) {

        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();

        studentResponses.forEach(studentResponse -> {
            boolean gitAccess = CheckAccessActions.checkGithubAccessForStudentResponse(studentResponse);
            boolean jiraAccess = CheckAccessActions.checkJiraAccessForStudentResponse(studentResponse);
            if (!gitAccess || !jiraAccess) {
                System.err.println("Aborting workflow creation...");
                return;
            }

            boolean createdJiraSecret = UploadSecretAction.createJiraTokenSecretFor(studentResponse);
            if (!createdJiraSecret) {
                System.err.println("Could not upload Jira Secret!!!");
                System.err.println("Aborting workflow creation...");
                return;
            }

            CreateFileRequestBody contentBody = createContentBodyFor(studentResponse);
            if (contentBody == null) {
                System.err.println("Aborting workflow creation...");
                return;
            }

            boolean createdBuildActionsBranch = createBuildActionsBranchFor(studentResponse);
            if (!createdBuildActionsBranch) {
                System.err.println("Aborting workflow creation...");
                return;
            }

            boolean uploadedFileToBranch = uploadBuildFileFor(studentResponse, contentBody);
            if (!uploadedFileToBranch) {
                System.err.println("Aborting workflow creation...");
                return;
            }

            boolean createdPR = createPullRequestForBuildActionsBranch(studentResponse);
            if (!createdPR) {
                System.err.println("Aborting workflow creation...");
                return;
            }

            System.out.printf("SUCCESSFULLY uploaded workflow to repo %s, for %s", studentResponse.getRepoUrl(), studentResponse.getName());

        });

        return null;
    }

    private static boolean createBuildActionsBranchFor(StudentResponse studentResponse) {
        GithubRefsService githubRefsService = new GithubRefsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Creating branch %s for repo %s............", BUILD_ACTIONS_BRANCH, studentResponse.getJiraUrl());
        try {
            githubRefsService.createBranch(BUILD_ACTIONS_BRANCH);
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }

    private static CreateFileRequestBody createContentBodyFor(StudentResponse studentResponse) {
        String resourceToLoad;
        if (studentResponse.getBuildTool().equalsIgnoreCase(MAVEN))
            resourceToLoad = MAVEN_RESOURCE;
        else if (studentResponse.getBuildTool().equalsIgnoreCase(GRADLE))
            resourceToLoad = GRADLE_RESOURCE;
        else {
            System.err.println("[WARNING]: Build tool is not Maven or Gradle!");
            return null;
        }

        String fileContent;
        try {
            fileContent = FileUtils.readFileToString(new File(
                            WorkflowActions.class.getClassLoader().getResource(resourceToLoad).getFile()),
                    StandardCharsets.UTF_8.toString());
        } catch (IOException e) {
            System.err.println("Failed to read the resource file " + resourceToLoad);
            return null;
        }

        return CreateFileRequestBody.builder()
                .branch(BUILD_ACTIONS_BRANCH)
                .content(Base64.getEncoder().encodeToString(fileContent.getBytes()))
                .message(BUILD_ACTION_MESSAGE)
                .build();
    }

    private static boolean uploadBuildFileFor(StudentResponse studentResponse, CreateFileRequestBody body) {
        GithubContentsService githubContentsService = new GithubContentsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Uploading %s file on branch %s for repo %s........", BUILD_ACTIONS_BRANCH, studentResponse.getJiraUrl());

        try {
            githubContentsService.addFileToRepo(GITHUB_WORKFLOWS_BUILD_YML, body);
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }

    private static boolean createPullRequestForBuildActionsBranch(StudentResponse studentResponse) {
        GithubPullRequestsService githubPullRequestsService = new GithubPullRequestsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Uploading %s file on branch %s for repo %s........", BUILD_ACTIONS_BRANCH, studentResponse.getJiraUrl());

        try {
            githubPullRequestsService.createPullRequest(CreatePullRequestBody.builder()
                    .title(BUILD_ACTION_MESSAGE)
                    .body(PULL_REQUEST_BODY)
                    .base(MASTER)
                    .head(BUILD_ACTIONS_BRANCH)
                    .draft(false)
                    .build());
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }

}
