package org.loose.fis.project.proofing.tool.actions;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.IOUtils;
import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents.CreateFileRequestBody;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.pullrequests.CreatePullRequestBody;
import org.loose.fis.project.proofing.tool.github.client.dto.response.repository.refs.Ref;
import org.loose.fis.project.proofing.tool.github.client.repository.contents.GithubContentsService;
import org.loose.fis.project.proofing.tool.github.client.repository.pullrequests.GithubPullRequestsService;
import org.loose.fis.project.proofing.tool.github.client.repository.refs.GithubRefsService;

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

    public static void createWorkflowFile() {

        List<StudentResponse> studentResponses = StudentsRegistry.getInstance().getForTeacher();

        studentResponses.forEach(studentResponse -> {
            boolean gitAccess = CheckAccessActions.checkGithubAccessForStudentResponse(studentResponse);
            boolean jiraAccess = CheckAccessActions.checkJiraAccessForStudentResponse(studentResponse);
            if (!gitAccess || !jiraAccess) {
                printAbortWorkflow();
                return;
            }

            boolean createdJiraSecret = UploadSecretAction.createJiraTokenSecretFor(studentResponse);
            if (!createdJiraSecret) {
                System.err.println("Could not upload Jira Secret!!!");
                printAbortWorkflow();
                return;
            }

            CreateFileRequestBody contentBody = createContentBodyFor(studentResponse);
            if (contentBody == null) {
                printAbortWorkflow();
                return;
            }

            boolean createdBuildActionsBranch = createBuildActionsBranchFromMasterFor(studentResponse);
            if (!createdBuildActionsBranch) {
                printAbortWorkflow();
                return;
            }

            boolean uploadedFileToBranch = uploadBuildFileFor(studentResponse, contentBody);
            if (!uploadedFileToBranch) {
                printAbortWorkflow();
                return;
            }

            boolean createdPR = createPullRequestForBuildActionsBranch(studentResponse);
            if (!createdPR) {
                printAbortWorkflow();
                return;
            }
            System.out.printf("SUCCESSFULLY uploaded workflow to repo %s, for %s [%d]\n\n", studentResponse.getRepoUrl(), studentResponse.getName(), studentResponse.getId());
        });
    }

    private static void printAbortWorkflow() {
        System.err.println("Aborting workflow creation...\n");
    }

    private static boolean createBuildActionsBranchFromMasterFor(StudentResponse studentResponse) {
        GithubRefsService githubRefsService = new GithubRefsService(studentResponse.getOwner(), studentResponse.getRepo(), Config.getGithubCredentials());
        System.out.printf("Creating branch %s for repo %s............", BUILD_ACTIONS_BRANCH, studentResponse.getRepoUrl());
        try {
            try {
                Ref buildActionsRef = githubRefsService.getBranch(BUILD_ACTIONS_BRANCH);
                if (buildActionsRef != null) {
                    System.out.printf("\nBranch %s already exists!\n", BUILD_ACTIONS_BRANCH);
                    return false;
                }
            } catch (Exception e) {
            }
            Ref master = githubRefsService.getBranch(MASTER);
            githubRefsService.createBranch(BUILD_ACTIONS_BRANCH, master.getObject().getSha());
            System.out.println("SUCCESSFUL");
            return true;
        } catch (Exception e) {
            System.err.println("FAILED");
            return false;
        }
    }

    @VisibleForTesting
    static CreateFileRequestBody createContentBodyFor(StudentResponse studentResponse) {
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
            fileContent = IOUtils.toString(WorkflowActions.class.getClassLoader().getResourceAsStream(resourceToLoad), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            System.err.println("Failed to read the resource file " + resourceToLoad);
            e.printStackTrace();
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
        System.out.printf("Uploading %s file on branch %s for repo %s........", GITHUB_WORKFLOWS_BUILD_YML, BUILD_ACTIONS_BRANCH, studentResponse.getRepoUrl());

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
        System.out.printf("Creating Pull Request branch %s for repo %s........", BUILD_ACTIONS_BRANCH, studentResponse.getRepoUrl());

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
