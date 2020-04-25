package org.loose.fis.project.proofing.tool.jira.client.dto.response.issues;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class IssueField {
	@Key
	private String id;
	@Key
	private String name;
	@Key
	private boolean custom;
}
