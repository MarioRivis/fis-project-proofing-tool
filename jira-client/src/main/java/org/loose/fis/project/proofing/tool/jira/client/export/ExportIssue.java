package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ExportIssue {
    /**
     * The Issue Key, composed of the project id and the number of the issue
     * Should be unique!
     */
    @Key
    private String key;
    /**
     * The internal Jira id of this issue. Should be unique!
     */
    @Key
    private String id;
    /**
     * A link to the rest api of this issue, for further use by other tools, if necessary.
     * Note: The access to this link could require authentication.
     */
    @Key
    private String self;
    /**
     * The issue Title
     */
    @Key
    private String summary;
    /**
     * The issue description in markup language
     */
    @Key
    private String description;
    /**
     * The current status of the Issue
     */
    @Key
    private String status;
    /**
     * The id of the issue type from ExportReport
     */
    @Key
    private String typeId;
    /**
     * The name of the issue's type
     */
    @Key
    private String type;
    /**
     * ZonedDateTime when the issue was created
     * Example: 2020-04-24T12:03:20.759+0300
     */
    @Key
    private String created;
    /**
     * ZonedDateTime when the issue was last updated
     * Example: 2020-04-24T12:03:20.759+0300
     */
    @Key
    private String updated;

	/**
	 * The name of the issues priority
	 */
    @Key
	private String priority;
    @Key
	private String creatorId;
    @Key
	private String assigneeId;
    @Key
	private String reporterId;
    /**
     * A list with all the Keys of the issue's sub-tasks
     */
    @Key
    private List<String> subTasks;
    @Key
    private List<ExportChange> changes;
    @Key
    private List<ExportComment> comments;
    @Key
    private Map<String, Object> customFields;
}
