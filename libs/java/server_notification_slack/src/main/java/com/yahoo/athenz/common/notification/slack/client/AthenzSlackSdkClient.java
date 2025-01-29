package com.yahoo.athenz.common.notification.slack.client;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.SlackClient;
import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.yahoo.athenz.common.notification.slack.SlackNotificationConsts.*;

public class AthenzSlackSdkClient implements SlackClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AthenzSlackSdkClient.class);
    private MethodsClient slackClient;
    private volatile String accessToken;
    private PrivateKeyStore privateKeyStore;

    private void initSlackClient() {
        // chandu TODO: initialize with access token
        // also need to handle loading of refreshed tokens
        // question is if lambda is updating it then the token can be revoked when lambda updates it so it is best to reload when we get auth failure?
        // or should we do it on a schedule + auth error?

        // add code to retrieve user id from email
        // which means now the receipent should be detailed as to if it is a channel or user
        this.slackClient = Slack.getInstance().methods(accessToken);
    }

    public AthenzSlackSdkClient(PrivateKeyStore privateKeyStore) {
        this.privateKeyStore = privateKeyStore;
        refreshToken();
        refreshTokenTimerTask();
    }

    public AthenzSlackSdkClient(MethodsClient slackClient) {
        this.slackClient = slackClient;
    }

    void refreshToken() {
        final String appName = System.getProperty(SLACK_BOT_TOKEN_APP_NAME, "");
        final String keygroupName = System.getProperty(SLACK_BOT_TOKEN_KEYGROUP_NAME, "");
        final String keyName = System.getProperty(SLACK_BOT_TOKEN_KEY_NAME, "");

        char[] newToken = privateKeyStore.getSecret(appName, keygroupName, keyName);
        if (newToken == null) {
            LOGGER.error("Error while refreshing slack token, token is null");
            return;
        }

        this.accessToken = String.valueOf(newToken);
    }

    private void refreshTokenTimerTask() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        long periodBetweenExecutions = Long.parseLong(
                System.getProperty(PROP_SLACK_FETCH_TOKEN_PERIOD_BETWEEN_EXECUTIONS, DEFAULT_SLACK_FETCH_TOKEN_PERIOD_BETWEEN_EXECUTIONS));

        executor.scheduleAtFixedRate(this::refreshToken,
                periodBetweenExecutions, periodBetweenExecutions, TimeUnit.SECONDS);
    }

    @Override
    public boolean sendMessage(Collection<String> recipients, String message) {

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#random")
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


    public String fetchUserIdFromEmail(String email)  {
        UsersLookupByEmailResponse response = null;
        try {
            response = slackClient
                    .usersLookupByEmail(req -> req.email(email));
        } catch (Exception e) {
            LOGGER.error("Unable to lookup user by email: {}", e.getMessage());
            return null;
        }

        if (response.isOk()) {
            return response.getUser().getId();
        } else {
            LOGGER.error("Unable to lookup slack user ID by email: {}", response.getError());
            return null;
        }
    }

}

