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
        String slackTokenLoaderFactoryClass = System.getProperty(SLACK_NOTIFICATION_PROP_TOKEN_LOADER_FACTORY_CLASS);
        TokenLoader tokenLoader = null;
        try {
            TokenLoaderFactory tokenLoaderFactory = (TokenLoaderFactory) Class.forName(
                    slackTokenLoaderFactoryClass.trim()).getDeclaredConstructor().newInstance();
            tokenLoader = tokenLoaderFactory.createTokenLoader();
            AthenzSlackConfig slackConfig = new AthenzSlackConfig(tokenLoader);
            SlackClient slackClient = new SlackClientFactory().createSlackClient(slackConfig);
            return new SlackNotificationService(slackClient);
        } catch (Exception ex) {
            LOGGER.error("Invalid NotificationServiceFactory class: {}", slackTokenLoaderFactoryClass, ex);
        }

        return null;
    }
}
