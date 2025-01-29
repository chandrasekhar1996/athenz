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

import com.slack.api.RequestConfigurator;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.slack.api.model.User;
import com.yahoo.athenz.auth.PrivateKeyStore;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertNotNull;

public class AthenzSlackSdkClientTest {

    @Test
    public void testAthenzSlackSdkClientNotNull() {
        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("access-token-1".toCharArray());
        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore);
        assertNotNull(athenzSlackClient);
    }

    @Test
    public void testSendMessageNullToken() throws SlackApiException, IOException {
        Set<String> recipients = new HashSet<>(Arrays.asList("user.user1", "user.user2", "user.user3"));
        String message = "test slack message";

        ChatPostMessageResponse chatPostMessageResponse = mock(ChatPostMessageResponse.class);
        Mockito.when(chatPostMessageResponse.isOk()).thenReturn(false);

        Slack slackClient = mock(Slack.class);
        MethodsClient slackMethodClient = mock(MethodsClient.class);
        when(slackMethodClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(chatPostMessageResponse);
        when(slackClient.methods(null)).thenReturn(slackMethodClient);

        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn(null);
        ArgumentCaptor<ChatPostMessageRequest> captor = ArgumentCaptor.forClass(ChatPostMessageRequest.class);

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertFalse(athenzSlackClient.sendMessage(recipients, message));
        Mockito.verify(slackMethodClient, atLeastOnce()).chatPostMessage(captor.capture());
    }

    @Test
    public void testSendMessageSuccess() throws SlackApiException, IOException {
        Set<String> recipients = new HashSet<>(Arrays.asList("user.user1", "user.user2", "user.user3"));
        String message = "test slack message";

        ChatPostMessageResponse chatPostMessageResponse = mock(ChatPostMessageResponse.class);
        Mockito.when(chatPostMessageResponse.isOk()).thenReturn(true);

        Slack slackClient = mock(Slack.class);
        MethodsClient slackMethodClient = mock(MethodsClient.class);
        when(slackMethodClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(chatPostMessageResponse);
        when(slackClient.methods(anyString())).thenReturn(slackMethodClient);

        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());
        ArgumentCaptor<ChatPostMessageRequest> captor = ArgumentCaptor.forClass(ChatPostMessageRequest.class);

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertTrue(athenzSlackClient.sendMessage(recipients, message));
        Mockito.verify(slackMethodClient, atLeastOnce()).chatPostMessage(captor.capture());
    }

    @Test
    public void testSendMessageFailure() throws SlackApiException, IOException {
        Set<String> recipients = new HashSet<>(Arrays.asList("user.user1", "user.user2", "user.user3"));
        String message = "test slack message";

        ChatPostMessageResponse chatPostMessageResponse = mock(ChatPostMessageResponse.class);
        Mockito.when(chatPostMessageResponse.isOk()).thenReturn(false);

        Slack slackClient = mock(Slack.class);
        MethodsClient slackMethodClient = mock(MethodsClient.class);
        when(slackMethodClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenReturn(chatPostMessageResponse);
        when(slackClient.methods(anyString())).thenReturn(slackMethodClient);

        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());
        ArgumentCaptor<ChatPostMessageRequest> captor = ArgumentCaptor.forClass(ChatPostMessageRequest.class);

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertFalse(athenzSlackClient.sendMessage(recipients, message));
        Mockito.verify(slackMethodClient, atLeastOnce()).chatPostMessage(captor.capture());
    }

    @Test
    public void testSendMessageException() throws SlackApiException, IOException {
        Set<String> recipients = new HashSet<>(Arrays.asList("user.user1", "user.user2", "user.user3"));
        String message = "test slack message";

        MethodsClient slackMethodsClient = mock(MethodsClient.class);
        when(slackMethodsClient.chatPostMessage(any(ChatPostMessageRequest.class))).thenThrow(new IOException());

        Slack slackClient = mock(Slack.class);
        when(slackClient.methods(anyString())).thenReturn(slackMethodsClient);

        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());
        ArgumentCaptor<ChatPostMessageRequest> captor = ArgumentCaptor.forClass(ChatPostMessageRequest.class);

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertFalse(athenzSlackClient.sendMessage(recipients, message));
        Mockito.verify(slackMethodsClient, atLeastOnce()).chatPostMessage(captor.capture());
    }

    @Test
    public void testFetchUserIdFromEmail() throws SlackApiException, IOException {
        String message = "test slack message";

        UsersLookupByEmailResponse usersLookupByEmailResponse = mock(UsersLookupByEmailResponse.class);
        Mockito.when(usersLookupByEmailResponse.isOk()).thenReturn(true);
        User slackUser = new User();
        slackUser.setId("slackId");

        Mockito.when(usersLookupByEmailResponse.getUser()).thenReturn(slackUser);

        MethodsClient slackMethodsClient = mock(MethodsClient.class);

        when(slackMethodsClient.usersLookupByEmail(any(RequestConfigurator.class))).thenReturn(usersLookupByEmailResponse);
        Slack slackClient = mock(Slack.class);
        when(slackClient.methods(anyString())).thenReturn(slackMethodsClient);
        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());

        ArgumentCaptor<RequestConfigurator> captor = ArgumentCaptor.forClass(RequestConfigurator.class);

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertEquals(athenzSlackClient.fetchUserIdFromEmail(message), "slackId");
        Mockito.verify(slackMethodsClient, atLeastOnce()).usersLookupByEmail(captor.capture());
    }

    @Test
    public void testFetchUserIdFromEmailNotOk() throws SlackApiException, IOException {
        String message = "test slack message";

        UsersLookupByEmailResponse usersLookupByEmailResponse = mock(UsersLookupByEmailResponse.class);
        Mockito.when(usersLookupByEmailResponse.isOk()).thenReturn(false);

        MethodsClient slackMethodsClient = mock(MethodsClient.class);

        when(slackMethodsClient.usersLookupByEmail(any(RequestConfigurator.class))).thenReturn(usersLookupByEmailResponse);
        Slack slackClient = mock(Slack.class);
        when(slackClient.methods(anyString())).thenReturn(slackMethodsClient);

        ArgumentCaptor<RequestConfigurator> captor = ArgumentCaptor.forClass(RequestConfigurator.class);

        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());
        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertNull(athenzSlackClient.fetchUserIdFromEmail(message));
        Mockito.verify(slackMethodsClient, atLeastOnce()).usersLookupByEmail(captor.capture());
    }

    @Test
    public void testFetchUserIdFromEmailException() throws SlackApiException, IOException {
        String message = "test slack message";

        UsersLookupByEmailResponse usersLookupByEmailResponse = mock(UsersLookupByEmailResponse.class);
        Mockito.when(usersLookupByEmailResponse.isOk()).thenReturn(false);

        MethodsClient slackMethodsClient = mock(MethodsClient.class);

        when(slackMethodsClient.usersLookupByEmail(any(RequestConfigurator.class))).thenThrow(new IOException());
        Slack slackClient = mock(Slack.class);
        when(slackClient.methods(anyString())).thenReturn(slackMethodsClient);

        ArgumentCaptor<RequestConfigurator> captor = ArgumentCaptor.forClass(RequestConfigurator.class);
        PrivateKeyStore privateKeyStore = mock(PrivateKeyStore.class);
        when(privateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("token-1".toCharArray());

        AthenzSlackSdkClient athenzSlackClient = new AthenzSlackSdkClient(privateKeyStore, slackClient);
        assertNull(athenzSlackClient.fetchUserIdFromEmail(message));
        Mockito.verify(slackMethodsClient, atLeastOnce()).usersLookupByEmail(captor.capture());
    }
}


