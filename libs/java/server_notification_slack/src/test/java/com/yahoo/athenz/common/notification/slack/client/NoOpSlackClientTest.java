package com.yahoo.athenz.common.notification.slack.client;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.SlackClient;
import com.yahoo.athenz.common.notification.slack.SlackClientFactory;
import com.yahoo.athenz.common.notification.slack.SlackNotificationService;
import com.yahoo.athenz.common.notification.slack.SlackNotificationServiceFactory;
import com.yahoo.athenz.common.server.notification.NotificationService;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static com.yahoo.athenz.common.notification.slack.SlackNotificationConsts.NOTIFICATION_SLACK_CLIENT_CLASS;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class NoOpSlackClientTest {

    @Test
    public void testNoOpDomainChangePublisher() {
        System.setProperty(NOTIFICATION_SLACK_CLIENT_CLASS, "com.yahoo.athenz.common.notification.slack.client.NoOpSlackClient");
        PrivateKeyStore mockPrivateKeyStore = mock(PrivateKeyStore.class);
        SlackClientFactory factory = new SlackClientFactory();
        SlackClient slackClient = factory.createSlackClient(mockPrivateKeyStore);
        assertNotNull(slackClient);
        assertTrue(slackClient instanceof NoOpSlackClient);

        assertTrue(slackClient.sendMessage(Collections.emptyList(), "test"));
        System.clearProperty(NOTIFICATION_SLACK_CLIENT_CLASS);
    }
}
