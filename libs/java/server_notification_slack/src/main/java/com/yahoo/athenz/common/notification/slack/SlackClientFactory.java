package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.auth.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.yahoo.athenz.common.notification.slack.SlackNotificationConsts.NOTIFICATION_SLACK_CLIENT_CLASS;

public class SlackClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackClientFactory.class);

    public static SlackClient createSlackClient(PrivateKeyStore privateKeyStore) {
        String clientClassName = System.getProperty(NOTIFICATION_SLACK_CLIENT_CLASS);
        if (StringUtils.isEmpty(clientClassName)) {
            LOGGER.error("SlackClient class is not initialized");
            throw new IllegalArgumentException("SlackClient class is not initialized");
        }

        try {
            Class<?> clientClass = Class.forName(clientClassName);
            return (SlackClient) clientClass.getDeclaredConstructor(PrivateKeyStore.class).newInstance(privateKeyStore);
        } catch (Exception e) {
            LOGGER.error("Invalid SlackClient class: {}", clientClassName, e);
            throw new IllegalArgumentException("Invalid SlackClient class");
        }
    }
}
