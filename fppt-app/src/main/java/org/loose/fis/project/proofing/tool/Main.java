package org.loose.fis.project.proofing.tool;

import org.loose.fis.project.proofing.tool.actions.*;
import org.loose.fis.project.proofing.tool.exceptions.FeatureUnavailableException;
import org.loose.fis.project.proofing.tool.exceptions.IllegalCommandException;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<Command, Runnable> actionsMap = new HashMap<>();

    static {
        actionsMap.put(Command.LIST_INVITATIONS, InvitationActions::listInvitations);
        actionsMap.put(Command.ACCEPT_INVITATIONS, InvitationActions::acceptInvitations);
        actionsMap.put(Command.CHECK_GIT_ACCESS, CheckAccessActions::checkGithubAccessAction);
        actionsMap.put(Command.CHECK_JIRA_ACCESS, CheckAccessActions::checkJiraAccessAction);
        actionsMap.put(Command.CHECK_ACCESS, CheckAccessActions::checkAccessAction);
        actionsMap.put(Command.UPLOAD_WORKFLOW, WorkflowActions::createWorkflowFile);
        actionsMap.put(Command.GET_JIRA_SUMMARY, JiraActions::getJiraSummary);
        actionsMap.put(Command.NORMALIZE_JIRA_URLS, NormalizeActions::normalizeJiraUrl);
        actionsMap.put(Command.NORMALIZE_GITHUB_URLS, NormalizeActions::normalizeGithubUrl);
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new IllegalCommandException();
            }

            String arg = args[0];

            Command command = Command.fromString(arg).orElseThrow(() -> new IllegalCommandException(arg));
            Runnable action = actionsMap.get(command);

            if (action == null) throw new FeatureUnavailableException();

            action.run();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
