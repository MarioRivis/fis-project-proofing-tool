package org.loose.fis.project.proofing.tool.github.client.repository.secrets;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.dto.response.repository.secrets.Secret;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubSecretsServiceTest {

    private GithubSecretsService secretsService = new GithubSecretsService("gogmaster", "test", TestUtils.getGithubCredentials());

    @Test
    void listAllSecretsForRepo() {
        List<Secret> secrets = secretsService.listAllSecretsForRepo();
        assertNotNull(secrets);
    }

    @Test
    void createTestSecret() {
        assertTrue(secretsService.createSecret("JIRA_TOKEN", TestUtils.getProperty("jira.token")));
    }
}