package org.loose.fis.project.proofing.tool.github.client.secrets;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubSecretsServiceTest {

    private GithubSecretsService secretsService = new GithubSecretsService("gogmaster", "test", TestUtil.getCredentials());

    @Test
    void listAllSecretsForRepo() {
        List<Secret> secrets = secretsService.listAllSecretsForRepo();
        assertNotNull(secrets);
    }

    @Test
    void createTestSecret() {
        assertTrue(secretsService.createSecret("JIRA_TOKEN", TestUtil.getProperty("jira.token")));
    }
}