/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yahoo.athenz.common.notification.slack.client;

import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.yahoo.athenz.auth.PrivateKeyStore;
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

public class AthenzSlackSdkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AthenzSlackSdkClient.class);
    private Slack slackClient;
    private volatile String accessToken;
    private PrivateKeyStore privateKeyStore;

    public AthenzSlackSdkClient(PrivateKeyStore privateKeyStore) {
        this.privateKeyStore = privateKeyStore;
        this.slackClient = Slack.getInstance();
        refreshToken();
        refreshTokenTimerTask();
    }

    public AthenzSlackSdkClient(PrivateKeyStore privateKeyStore, Slack slackClient) {
        this.slackClient = slackClient;
        this.privateKeyStore = privateKeyStore;
        refreshToken();
        refreshTokenTimerTask();
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

    public boolean sendMessage(Collection<String> recipients, String message) {

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#random")
                .blocksAsString(message)
                .build();

        ChatPostMessageResponse response;
        try {
            response = slackClient.methods(accessToken).chatPostMessage(request);
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
            response = slackClient.methods(accessToken)
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

