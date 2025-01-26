package com.yahoo.athenz.common.notification.slack;

import com.slack.api.RequestConfigurator;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.slack.api.model.User;
import com.yahoo.athenz.common.notification.slack.impl.NoOpTokenLoader;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

public class NoOpTokenLoaderTest {

    @Test
    public void testNoOpTokenLoader() {
        NoOpTokenLoader noOpTokenLoader = new NoOpTokenLoader();
        assertNotNull(noOpTokenLoader);
    }

    @Test
    public void testLoadToken() {
        NoOpTokenLoader noOpTokenLoader = new NoOpTokenLoader();
        assertNotNull(noOpTokenLoader.loadToken());
    }

}
