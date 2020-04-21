package org.loose.fis.project.proofing.tool.github.client.invitations;

import lombok.extern.slf4j.Slf4j;
import org.loose.fis.project.proofing.tool.github.client.GithubClientAction;
import org.loose.fis.project.proofing.tool.github.client.model.BasicAuthCredentials;

import java.util.List;

@Slf4j
public class AcceptInvitationAction implements GithubClientAction<Void> {

    private GithubInvitationService githubInvitationService;

    @Override
    public Void perform(String... args) {
        BasicAuthCredentials credentials = new BasicAuthCredentials();
        githubInvitationService = new GithubInvitationService(credentials);
        List<Invitation> invitations = githubInvitationService.listInvitationsForUser();
        invitations.forEach(invitation -> log.info(invitation.getId().toEngineeringString()));
        return null;
    }
}
