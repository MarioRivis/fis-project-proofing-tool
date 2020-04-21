package org.loose.fis.project.proofing.tool.github.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GithubClientTest {

    private GithubClient githubClient;

    @Test
    void getApiPath() {


        assertEquals("https://api.github.com/repos/MarioRivis/SimpleRegistrationExample", GithubClient.getApiPath("repos", ":owner", ":repo"));
    }
}