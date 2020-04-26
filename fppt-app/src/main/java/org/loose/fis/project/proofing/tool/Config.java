package org.loose.fis.project.proofing.tool;

import lombok.SneakyThrows;
import org.loose.fis.project.proofing.tool.http.BasicAuthenticationProvider;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

    private static final String JIRA_TOKEN = "jira.token";
    private static final String JIRA_USERNAME = "jira.username";
    private static final String GITHUB_USERNAME = "github.username";
    private static final String GITHUB_TOKEN = "github.token";
    private static final String FPPT_PROPERTIES = "fppt.properties";
    private static final String CURRENT_STUDENTS = "currentStudents.txt";
    private static final String STUDENT_RESPONSES = "studentResponses";
    private static final String TEACHER_FIELD = "teacher";

    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (properties == null)
            loadProperties();

        return properties.getProperty(key, defaultValue);
    }

    @SneakyThrows
    private static void loadProperties() {
        properties = new Properties();
        properties.load(new FileReader(Paths.get(".", "config", FPPT_PROPERTIES).toFile()));
    }

    public static BasicAuthenticationProvider getGithubCredentials() {
        return new BasicAuthenticationProvider(getProperty(GITHUB_USERNAME), getProperty(GITHUB_TOKEN));
    }

    public static BasicAuthenticationProvider getJiraCredentials() {
        return new BasicAuthenticationProvider(getProperty(JIRA_USERNAME), getProperty(JIRA_TOKEN));
    }

    public static String getStudentResponsesFilePath() {
        return getProperty(STUDENT_RESPONSES, String.format("config/%s.json", STUDENT_RESPONSES));
    }

    public static String getTeacherName() {
        return getProperty(TEACHER_FIELD, "");
    }

    public static String getJiraToken() {
        return getProperty(JIRA_TOKEN);
    }

    public static Path getCurrentStudentPath() {
        return Paths.get(".", "config", CURRENT_STUDENTS);
    }
}
