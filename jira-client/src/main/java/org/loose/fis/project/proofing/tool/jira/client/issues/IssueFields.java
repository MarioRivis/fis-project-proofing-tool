package org.loose.fis.project.proofing.tool.jira.client.issues;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class IssueFields {
    @Key
    private Issue parent;
    @Key
    private IssueType issuetype;
    @Key
    private IssueStatus status;
    @Key
    private List<JiraComponent> components;
    @Key
    private String summary;
    @Key
    private String description;
    @Key
    private String created;
    @Key
    private String updated;
    @Key
    private List<Issue> subtasks;
}
