package org.loose.fis.project.proofing.tool;

import com.google.api.client.util.Key;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class StudentResponse {
    @Key("ID")
    private int id;
    @Key("Name")
    private String name;
    @Key("Teaching Assistant")
    private String teacher;
    @Key("Project Title")
    private String projectTitle;
    @Key("First Member (First and Last Name)")
    private String firstMember;
    @Key("Second Member (First and Last Name)")
    private String secondMember;
    @Key("Third Member (First and Last Name)")
    private String thirdMember;
    @Key("Repository URL")
    private String repoUrl;
    @Key("Jira URL")
    private String jiraUrl;
    @Key("Jira Project ID")
    private String jiraProjectId;
    @Key("Build Tool")
    private String buildTool;

    public String getOwner() {
        String[] remainingParts = repoUrl.replace("https://github.com/", "").split("/");
        if (remainingParts.length > 0)
            return remainingParts[0];

        return null;
    }

    public String getRepo() {
        String[] remainingParts = repoUrl.replace("https://github.com/", "").split("/");
        if (remainingParts.length >= 2)
            return remainingParts[1];

        return null;
    }

    public String getAllNames() {
        return String.join("-", firstMember, secondMember, StringUtils.isEmpty(thirdMember) ? "" : thirdMember)
                .replace(" ", "_");
    }
}
