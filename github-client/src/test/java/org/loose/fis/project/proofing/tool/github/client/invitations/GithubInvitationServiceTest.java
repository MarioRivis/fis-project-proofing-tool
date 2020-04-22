package org.loose.fis.project.proofing.tool.github.client.invitations;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.dto.response.invitations.Invitation;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubInvitationServiceTest {

    GithubInvitationService invitationService = new GithubInvitationService(TestUtils.getGithubCredentials());

    @Test
    void listInvitationsForUser() {
        List<Invitation> invitations = invitationService.listInvitationsForUser();

        assertNotNull(invitations);
    }

    @Test
    void acceptInvitation() {
        assertTrue(invitationService.acceptInvitation(invitationService.listInvitationsForUser().get(0)));
    }
}