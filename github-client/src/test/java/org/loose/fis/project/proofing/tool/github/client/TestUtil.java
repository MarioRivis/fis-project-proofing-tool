package org.loose.fis.project.proofing.tool.github.client;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.github.client.model.BasicAuthCredentials;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class TestUtil {

    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        properties = new Properties();
        properties.load(new FileInputStream(Paths.get(".", "/config/github-client.properties").toFile()));
    }

    public static BasicAuthCredentials getCredentials() {
        return new BasicAuthCredentials(getProperty("github.username"), getProperty("github.token"));
    }
}
