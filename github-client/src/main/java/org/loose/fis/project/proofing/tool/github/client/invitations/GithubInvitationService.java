package org.loose.fis.project.proofing.tool.github.client.invitations;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.github.client.GithubClient;
import org.loose.fis.project.proofing.tool.github.client.http.GithubHttpClient;
import org.loose.fis.project.proofing.tool.github.client.model.BasicAuthCredentials;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GithubInvitationService {

    private static String GET_INVITATIONS_URL = GithubClient.getApiPath("user", "repository_invitations");

    private GithubHttpClient githubHttpClient = new GithubHttpClient();
    private BasicAuthCredentials credentials;


    public GithubInvitationService(BasicAuthCredentials credentials) {
        this.credentials = credentials;
    }

    @SneakyThrows
    public List<Invitation> listInvitationsForUser() {

        HttpResponse httpResponse = githubHttpClient.get(new GenericUrl(GET_INVITATIONS_URL), credentials);

        Type type = new TypeToken<List<Invitation>>() {
        }.getType();
        return (List<Invitation>) httpResponse.parseAs(type);
    }

    public boolean acceptInvitation(Invitation invitation) {
        return acceptInvitation(invitation.getId());
    }

    public boolean acceptInvitation(BigDecimal invitationId) {
        Map map = new HashMap<String, String>();
        map.put("invitation_id", invitationId.toString());

        String apiPath = GithubClient.getApiPath(map, "user", "repository_invitations", ":invitation_id");

        HttpResponse httpResponse = githubHttpClient.patch(new GenericUrl(apiPath), credentials, null);

        return httpResponse.getStatusCode() == 204;
    }
}
