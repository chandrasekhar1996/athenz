package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;
import com.yahoo.athenz.common.server.notification.NotificationService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.mockito.Mockito.*;

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
        System.setProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_FACTORY_CLASS, "com.yahoo.athenz.common.notification.slack.SlackTokenLoaderFactory");

        when(mockSlackTokenLoaderFactory.createTokenLoader()).thenReturn(mockTokenLoader);
        when(mockSlackClientFactory.createSlackClient(any(AthenzSlackConfig.class))).thenReturn(mockSlackClient);

        NotificationService notificationService = factory.create(mockPrivateKeyStore);
        assertNotNull(notificationService);
        assertTrue(notificationService instanceof SlackNotificationService);
    }

    @Test
    public void testCreateNotificationServiceFailure() {
        System.setProperty(SlackNotificationServiceFactory.SLACK_NOTIFICATION_PROP_TOKEN_LOADER_FACTORY_CLASS, "InvalidClassName");

        NotificationService notificationService = factory.create(mockPrivateKeyStore);
        assertNull(notificationService);
    }
}