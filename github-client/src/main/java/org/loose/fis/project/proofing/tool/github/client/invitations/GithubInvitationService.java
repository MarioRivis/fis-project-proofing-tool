package org.loose.fis.project.proofing.tool.github.client.invitations;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.github.client.GithubClient;
import org.loose.fis.project.proofing.tool.github.client.GithubService;
import org.loose.fis.project.proofing.tool.github.client.dto.response.invitations.Invitation;
import org.loose.fis.project.proofing.tool.http.BasicAuthCredentials;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class GithubInvitationService extends GithubService {
    private static String GET_INVITATIONS_URL = GithubClient.getApiPath("user", "repository_invitations");

    public GithubInvitationService() {
        super();
    }

    public GithubInvitationService(BasicAuthCredentials credentials) {
        super(credentials);
    }

    @SneakyThrows
    public List<Invitation> listInvitationsForUser() {

        HttpResponse httpResponse = httpClient.get(new GenericUrl(GET_INVITATIONS_URL), credentials);

        Type type = new TypeToken<List<Invitation>>() {
        }.getType();
        return (List<Invitation>) httpResponse.parseAs(type);
    }

    public boolean acceptInvitation(Invitation invitation) {
        return acceptInvitation(invitation.getId());
    }

    public boolean acceptInvitation(BigDecimal invitationId) {
        ImmutableMap<String, String> map = ImmutableMap.of("invitation_id", invitationId.toString());
        String apiPath = GithubClient.getApiPath(map, "user", "repository_invitations", ":invitation_id");

        HttpResponse httpResponse = httpClient.patch(new GenericUrl(apiPath), credentials, null);

        return httpResponse.getStatusCode() == 204;
    }
}
