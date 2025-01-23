package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;

public class SlackClientFactory {
    public static SlackClient createSlackClient(AthenzSlackConfig slackConfig) {
        String clientClassName = System.getProperty("slack.client.class");
        if (clientClassName == null || clientClassName.isEmpty()) {
            throw new RuntimeException("System property 'slack.client.class' is not set.");
        }

        try {
            Class<?> clientClass = Class.forName(clientClassName);
            if (!SlackClient.class.isAssignableFrom(clientClass)) {
                throw new RuntimeException(clientClassName + " does not implement SlackClient interface.");
            }

            return (SlackClient) clientClass.getConstructor(AthenzSlackConfig.class).newInstance(slackConfig);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
