package org.loose.fis.project.proofing.tool.github.client;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GithubClientTest {

    @Test
    void getApiPath() {
        Map<String, String> map = ImmutableMap.of("owner", "MarioRivis", "repo", "SimpleRegistrationExample");

        assertEquals("https://api.github.com/repos/MarioRivis/SimpleRegistrationExample", GithubClient.getApiPath(map, "repos", ":owner", ":repo"));
    }
}