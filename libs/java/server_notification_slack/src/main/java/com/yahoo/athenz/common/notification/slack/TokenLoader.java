package com.yahoo.athenz.common.notification.slack;

public interface TokenLoader {
    /**
     * Loads the Slack access token.
     *
     * @return The Slack access token as a String.
     */
    String loadToken();
}