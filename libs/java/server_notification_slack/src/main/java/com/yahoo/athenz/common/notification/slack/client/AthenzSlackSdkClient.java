package com.yahoo.athenz.common.notification.slack.client;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.yahoo.athenz.common.notification.slack.SlackClient;
import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

public class AthenzSlackSdkClient implements SlackClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AthenzSlackSdkClient.class);
    private MethodsClient slackClient;
    private final AthenzSlackConfig athenzSlackConfig;

    private void initSlackClient() {
        // chandu TODO: initialize with access token
        // also need to handle loading of refreshed tokens
        // question is if lambda is updating it then the token can be revoked when lambda updates it so it is best to reload when we get auth failure?
        // or should we do it on a schedule + auth error?

        // add code to retrieve user id from email
        // which means now the receipent should be detailed as to if it is a channel or user
        this.slackClient = Slack.getInstance().methods(athenzSlackConfig.getToken());
    }

    public AthenzSlackSdkClient(AthenzSlackConfig athenzSlackConfig) {
        this.athenzSlackConfig = athenzSlackConfig;
        initSlackClient();
    }

    @Override
    public boolean sendMessage(Collection<String> recipients, String message) {

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#random") // Use a channel ID `C1234567` is preferable
                .blocksAsString(message)
                .build();

        ChatPostMessageResponse response;
        try {
            response = slackClient.chatPostMessage(request);
        } catch (Exception e) {
            LOGGER.error("Failed to send message to slack: {}", e.getMessage());
            return false;
        }

        if (response.isOk()) {
            return true;
        } else {
            LOGGER.error("Failed to send message to slack: {}", response.getError());
            return false;
        }
    }

    /**
     * Fetches the Slack user ID associated with a given email.
     *
     * @param email The email address of the user
     * @return The Slack user ID or null if the user is not found
     * @throws IOException        If there is an error during the HTTP request
     * @throws SlackApiException  If there is an error from the Slack API
     */
    public String fetchUserIdFromEmail(String email)  {
        // Use the `users.lookupByEmail` method
        UsersLookupByEmailResponse response = null;
        try {
            response = slackClient
                    .usersLookupByEmail(req -> req.email(email));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }

        if (response.isOk()) {
            // If the response is successful, return the user ID
            return response.getUser().getId();
        } else {
            // Log or handle error based on the response's error code
            System.err.println("Error: " + response.getError());
            return null;
        }
    }
}

