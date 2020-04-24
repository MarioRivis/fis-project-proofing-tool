package org.loose.fis.project.proofing.tool.actions;

import lombok.extern.slf4j.Slf4j;
import org.loose.fis.project.proofing.tool.Config;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.StudentsRegistry;
import org.loose.fis.project.proofing.tool.github.client.dto.response.invitations.Invitation;
import org.loose.fis.project.proofing.tool.github.client.invitations.GithubInvitationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class InvitationActions {

    private static GithubInvitationService githubInvitationService = new GithubInvitationService(Config.getGithubCredentials());

    public static Void listInvitations(String... args) {
        Map<Boolean, List<Invitation>> existenceToInvitations = getBooleanListMap();

        List<Invitation> existingInvitations = existenceToInvitations.get(Boolean.TRUE);
        List<Invitation> failedInvitations = existenceToInvitations.get(Boolean.FALSE);

        existingInvitations.forEach(invitation -> System.out.println(formatInvitation(invitation)));

        writeFailedInvitations(failedInvitations);
        return null;
    }

    public static Void acceptInvitations(String... args) {
        Map<Boolean, List<Invitation>> existenceToInvitations = getBooleanListMap();

        List<Invitation> existingInvitations = existenceToInvitations.get(Boolean.TRUE);
        List<Invitation> failedInvitations = existenceToInvitations.get(Boolean.FALSE);

        existingInvitations.forEach(invitation -> {
            String repoUrl = invitation.getRepoUrl();
            StudentResponse studentResponse = StudentsRegistry.getInstance().getByGitRepo(repoUrl).get();
            System.out.printf("Accepting invite for %s [%s]...", repoUrl, studentResponse.getName());
            try {
                githubInvitationService.acceptInvitation(invitation);
                System.out.println("SUCCESSFUL");
            } catch (Exception e) {
                System.out.println("FAILED");
            }
        });

        writeFailedInvitations(failedInvitations);

        System.out.println("\n\nChecking access to the repositories:\n\n");
        CheckAccessActions.checkGithubAccessAction(args);

        return null;
    }

    private static void writeFailedInvitations(List<Invitation> failedInvitations) {
        if (failedInvitations == null)
            return;

        System.err.println("\n\n");
        System.err.println("The following repos do not match eny entry in the response:\n");
        failedInvitations.forEach(invitation -> System.err.println(invitation.getRepoUrl()));
        StudentsRegistry.getInstance().persist();
    }

    private static Map<Boolean, List<Invitation>> getBooleanListMap() {
        List<Invitation> invitations = githubInvitationService.listInvitationsForUser();

        return invitations.stream().collect(Collectors.groupingBy(InvitationActions::invitationRepoExistsInStudentResponses));
    }

    private static Boolean invitationRepoExistsInStudentResponses(Invitation invitation) {
        return StudentsRegistry.getInstance().getByGitRepo(invitation.getRepoUrl()).isPresent();
    }

    private static String formatInvitation(Invitation invitation) {
        String repoUrl = invitation.getRepoUrl();
        StudentResponse studentResponse = StudentsRegistry.getInstance().getByGitRepo(repoUrl).get();

        return String.format("[%s] -> %s -> %s -> %s", invitation.getPermissions(), studentResponse.getProjectTitle(), studentResponse.getName(), repoUrl);
    }
}
