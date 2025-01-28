package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;
import com.yahoo.athenz.common.server.notification.NotificationService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.yahoo.athenz.common.notification.slack.SlackClientFactory.NOTIFICATION_SLACK_CLIENT_CLASS;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SlackNotificationServiceFactoryTest {

    private SlackNotificationServiceFactory factory;
    private PrivateKeyStore mockPrivateKeyStore;
    private SlackTokenLoaderFactory mockSlackTokenLoaderFactory;
    private TokenLoader mockTokenLoader;
    private AthenzSlackConfig mockSlackConfig;
    private SlackClientFactory mockSlackClientFactory;
    private SlackClient mockSlackClient;

    @BeforeMethod
    public void setUp() {
        factory = new SlackNotificationServiceFactory();
        mockPrivateKeyStore = mock(PrivateKeyStore.class);
        mockSlackTokenLoaderFactory = mock(SlackTokenLoaderFactory.class);
        mockTokenLoader = mock(TokenLoader.class);
        mockSlackConfig = mock(AthenzSlackConfig.class);
        mockSlackClientFactory = mock(SlackClientFactory.class);
        mockSlackClient = mock(SlackClient.class);
    }

    @Test
    public void testCreateNotificationServiceSuccess() throws Exception {
        System.setProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_CLASS, "com.yahoo.athenz.common.notification.slack.impl.NoOpTokenLoader");
        System.setProperty(NOTIFICATION_SLACK_CLIENT_CLASS, "com.yahoo.athenz.common.notification.slack.client.NoOpSlackClient");

        NotificationService notificationService = factory.create(mockPrivateKeyStore);
        assertNotNull(notificationService);
        assertTrue(notificationService instanceof SlackNotificationService);

        System.clearProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_CLASS);
        System.clearProperty(NOTIFICATION_SLACK_CLIENT_CLASS);

    }

    @Test
    public void testCreateNotificationServiceFailure() {
        System.setProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_CLASS, "InvalidClassName");
        try {
            NotificationService notificationService = factory.create(mockPrivateKeyStore);
            fail();
        } catch (Exception ignored) {
        }
        System.clearProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_CLASS);
    }
}