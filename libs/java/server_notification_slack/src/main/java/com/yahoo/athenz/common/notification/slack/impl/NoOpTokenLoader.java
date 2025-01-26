package com.yahoo.athenz.common.notification.slack.impl;

import com.yahoo.athenz.common.notification.slack.TokenLoader;

public class NoOpTokenLoader implements TokenLoader {

    @Override
    public String loadToken() {
        return "sample-token";
    }
}
