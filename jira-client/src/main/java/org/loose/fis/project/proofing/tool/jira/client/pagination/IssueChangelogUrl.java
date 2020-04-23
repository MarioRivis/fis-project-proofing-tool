package org.loose.fis.project.proofing.tool.jira.client.pagination;

import com.google.api.client.util.Key;

public class IssueChangelogUrl extends PaginatedUrl {

    @Key
    private String expand = "changelog";
    @Key
    private String fields = "none";

    public IssueChangelogUrl(String encodedUrl, int startIndex, int maxResults) {
        super(encodedUrl, startIndex, maxResults);
    }
}
