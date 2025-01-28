package com.yahoo.athenz.common.notification.slack.client;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.SlackClient;

import java.util.Collection;

public class NoOpSlackClient implements SlackClient {

    PrivateKeyStore privateKeyStore;

    @Override
    public boolean sendMessage(Collection<String> recipients, String message) {
        return true;
    }

    public NoOpSlackClient(PrivateKeyStore privateKeyStore) {
        this.privateKeyStore = privateKeyStore;
    }

}

