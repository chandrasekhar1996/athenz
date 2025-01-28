package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;
import com.yahoo.athenz.common.server.notification.NotificationService;
import com.yahoo.athenz.common.server.notification.NotificationServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackNotificationServiceFactory implements NotificationServiceFactory {
    public static final String SLACK_NOTIFICATION_PROP_TOKEN_LOADER_CLASS = "athenz.zms.slack_notification_token_loader_class";
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationServiceFactory.class);


    @Override
    public NotificationService create(PrivateKeyStore privateKeyStore) {
        AthenzSlackConfig slackConfig = new AthenzSlackConfig(privateKeyStore);
        SlackClient slackClient = new SlackClientFactory().createSlackClient(slackConfig);
        return new SlackNotificationService(slackClient);
    }
}

