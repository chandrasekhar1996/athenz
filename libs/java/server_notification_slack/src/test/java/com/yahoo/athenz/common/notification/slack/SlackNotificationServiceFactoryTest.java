package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.server.notification.NotificationService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.yahoo.athenz.common.notification.slack.SlackNotificationConsts.NOTIFICATION_SLACK_CLIENT_CLASS;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SlackNotificationServiceFactoryTest {

    private SlackNotificationServiceFactory factory;
    private PrivateKeyStore mockPrivateKeyStore;

    @BeforeMethod
    public void setUp() {
        factory = new SlackNotificationServiceFactory();
        mockPrivateKeyStore = mock(PrivateKeyStore.class);
    }

    @Test
    public void testCreateNotificationServiceSuccess() throws Exception {
        System.setProperty(NOTIFICATION_SLACK_CLIENT_CLASS, "com.yahoo.athenz.common.notification.slack.client.NoOpSlackClient");

        NotificationService notificationService = factory.create(mockPrivateKeyStore);
        assertNotNull(notificationService);
        assertTrue(notificationService instanceof SlackNotificationService);

        System.clearProperty(NOTIFICATION_SLACK_CLIENT_CLASS);
    }

    @Test
    public void testCreateNotificationServiceFailure() {
        try {
            NotificationService notificationService = factory.create(mockPrivateKeyStore);
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testCreateNotificationServiceInvalidClassFailure() {
        System.setProperty(NOTIFICATION_SLACK_CLIENT_CLASS, "com.yahoo.athenz.common.notification.slack.client.InvalidSlackClient");

        try {
            NotificationService notificationService = factory.create(mockPrivateKeyStore);
            fail();
        } catch (Exception ignored) {
        }
        System.clearProperty(NOTIFICATION_SLACK_CLIENT_CLASS);
    }
}