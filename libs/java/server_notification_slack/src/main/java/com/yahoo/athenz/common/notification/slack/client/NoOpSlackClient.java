package com.yahoo.athenz.common.notification.slack.client;

import com.yahoo.athenz.common.notification.slack.SlackClient;
import com.yahoo.athenz.common.notification.slack.config.AthenzSlackConfig;

import java.util.Collection;

public class NoOpSlackClient implements SlackClient {

    AthenzSlackConfig athenzSlackConfig;

    @Override
    public boolean sendMessage(Collection<String> recipients, String message) {
        return true;
    }

    public NoOpSlackClient(AthenzSlackConfig config) {
        this.athenzSlackConfig = config;
    }


}

