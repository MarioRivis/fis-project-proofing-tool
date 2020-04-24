package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import org.loose.fis.project.proofing.tool.jira.client.dto.response.users.User;

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
    private GenericJson description;
    @Key
    private String created;
    @Key
    private String updated;
    @Key
    private List<Issue> subtasks;
    @Key
    private IssuePriority priority;
    @Key
    private User creator;
    @Key
    private User reporter;
    @Key
    private User assignee;
}
