package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class IssuePriority extends GenericJson {
	@Key
	private String name;
}
