package com.yahoo.athenz.common.notification.slack.config;

import com.yahoo.athenz.common.notification.slack.TokenLoader;

public class AthenzSlackConfig {

    private volatile String token;
    private final TokenLoader tokenLoader;

    /**
     * Constructor for AthenzSlackConfig.
     *
     * @param tokenLoader The TokenLoader instance.
     * @throws Exception if the token cannot be loaded.
     */
    public AthenzSlackConfig(TokenLoader tokenLoader) throws Exception {
        this.tokenLoader = tokenLoader;
        reloadToken(); // Load token initially
    }

    /**
     * Reload the Slack token using the provided loader.
     *
     * @throws Exception if the token cannot be loaded.
     */
    public synchronized void reloadToken() throws Exception {
        String newToken = tokenLoader.loadToken();
        if (newToken == null || newToken.isEmpty()) {
            throw new Exception("Slack access token cannot be null or empty.");
        }
        this.token = newToken;
    }

    /**
     * Get the current Slack token.
     *
     * @return The Slack token.
     */
    public String getToken() {
        return token;
    }
}
