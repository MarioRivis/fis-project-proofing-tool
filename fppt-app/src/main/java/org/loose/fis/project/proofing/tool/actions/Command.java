package org.loose.fis.project.proofing.tool.actions;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Command {
    LIST_INVITATIONS("listInvitations"),
    ACCEPT_INVITATIONS("acceptInvitations"),
    CHECK_ACCESS("checkAccess"), // checks for both jira and git access
    CHECK_GIT_ACCESS("checkGitAccess"),
    CHECK_JIRA_ACCESS("checkJiraAccess"),
    UPLOAD_WORKFLOW("uploadWorkflow"),
    CHECK_WORKFLOW("checkWorkflow"),
    GET_JIRA_SUMMARY("getGitSummary"),
    GET_GIT_SUMMARY("getJiraSummary");

    private final String name;

    Command(String value) {
        this.name = value;
    }

    public static String names() {
        return Arrays.asList(values()).stream()
                .map(Command::getName)
                .collect(Collectors.toList())
                .toString();
    }

    public static Optional<Command> fromString(String name) {
        if (name != null) {
            for (Command b : Command.values()) {
                if (name.equalsIgnoreCase(b.name)) {
                    return Optional.of(b);
                }
            }
        }
        return Optional.empty();
    }

    public String getName() {
        return name;
    }
}