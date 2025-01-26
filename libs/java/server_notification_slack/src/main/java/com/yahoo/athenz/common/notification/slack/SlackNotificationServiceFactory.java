package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;
import com.yahoo.athenz.common.server.notification.NotificationService;
import com.yahoo.athenz.common.server.notification.NotificationServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackNotificationServiceFactory implements NotificationServiceFactory {
    public static final String SLACK_NOTIFICATION_PROP_TOKEN_LOADER_FACTORY_CLASS = "athenz.zms.slack_notification_token_loader_factory_class";
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationServiceFactory.class);


    @Override
    public NotificationService create(PrivateKeyStore privateKeyStore) {
        final String slackTokenLoaderFactoryClass = System.getProperty(SLACK_NOTIFICATION_PROP_TOKEN_LOADER_FACTORY_CLASS);
        if (slackTokenLoaderFactoryClass == null) {
            LOGGER.error("Slack token loader factory class is not defined");
            throw new IllegalArgumentException("Slack token loader factory class is not defined");
        }

        try {
            SlackTokenLoaderFactory slackTokenLoaderFactory = (SlackTokenLoaderFactory) Class.forName(
                    slackTokenLoaderFactoryClass.trim()).getDeclaredConstructor().newInstance();

            TokenLoader tokenLoader = slackTokenLoaderFactory.createTokenLoader();
            AthenzSlackConfig slackConfig = new AthenzSlackConfig(tokenLoader);

            SlackClient slackClient = new SlackClientFactory().createSlackClient(slackConfig);
            return new SlackNotificationService(slackClient);
        } catch (Exception ex) {
            LOGGER.error("Invalid NotificationServiceFactory class: {}", slackTokenLoaderFactoryClass, ex);
            throw new IllegalArgumentException("Invalid Slack token loader class", ex);
        }

    }

}

