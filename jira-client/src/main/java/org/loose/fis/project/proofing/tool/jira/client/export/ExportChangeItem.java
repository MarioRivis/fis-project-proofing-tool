package org.loose.fis.project.proofing.tool.jira.client.export;

import com.google.api.client.util.Key;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExportChangeItem {
	@Key
	private String field;
	@Key
	private String from;
	@Key
	private String to;
}
