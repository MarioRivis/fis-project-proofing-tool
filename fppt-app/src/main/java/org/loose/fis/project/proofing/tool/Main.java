package org.loose.fis.project.proofing.tool;

import com.google.common.collect.ImmutableMap;
import org.loose.fis.project.proofing.tool.actions.Action;
import org.loose.fis.project.proofing.tool.actions.CheckAccessActions;
import org.loose.fis.project.proofing.tool.actions.Command;
import org.loose.fis.project.proofing.tool.actions.InvitationActions;
import org.loose.fis.project.proofing.tool.exceptions.FeatureUnavailableException;
import org.loose.fis.project.proofing.tool.exceptions.IllegalCommandException;

import java.util.Map;

public class Main {

    private static Map<Command, Action> actionsMap = ImmutableMap.of(
            Command.LIST_INVITATIONS, InvitationActions::listInvitations,
            Command.ACCEPT_INVITATIONS, InvitationActions::acceptInvitations,
            Command.CHECK_GIT_ACCESS, CheckAccessActions::checkGithubAccessAction,
            Command.CHECK_JIRA_ACCESS, CheckAccessActions::checkJiraAccessAction,
            Command.CHECK_ACCESS, CheckAccessActions::checkAccessAction
    );


    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new IllegalCommandException();
            }

            String arg = args[0];

            Command command = Command.fromString(arg).orElseThrow(() -> new IllegalCommandException(arg));
            Action action = actionsMap.get(command);

            if (action == null) throw new FeatureUnavailableException();

            action.perform(args);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
