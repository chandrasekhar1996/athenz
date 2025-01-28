package com.yahoo.athenz.common.notification.slack.config;

import com.yahoo.athenz.auth.PrivateKeyStore;
import com.yahoo.athenz.common.notification.slack.TokenLoader;

import static com.yahoo.athenz.common.notification.slack.SlackNotificationConsts.*;

public class AthenzSlackConfig {

    private volatile char[] token;
    private PrivateKeyStore keyStore;

    /**
     * Constructor for AthenzSlackConfig.
     *
     * @param privateKeyStore The TokenLoader instance.
     * @throws Exception if the token cannot be loaded.
     */
    public AthenzSlackConfig(PrivateKeyStore privateKeyStore) {
        this.keyStore = privateKeyStore;
        reloadToken(); // Load token initially
    }

    /**
     * Reload the Slack token using the provided loader.
     *
     * @throws Exception if the token cannot be loaded.
     */
    public synchronized void reloadToken() {
        final String appName = System.getProperty(SLACK_BOT_TOKEN_APP_NAME, "");
        final String keygroupName = System.getProperty(SLACK_BOT_TOKEN_KEYGROUP_NAME, "");
        final String keyName = System.getProperty(SLACK_BOT_TOKEN_KEY_NAME, "");

        char[] newToken = keyStore.getSecret(appName, keygroupName, keyName);
        if (newToken == null ) {
            throw new IllegalArgumentException("Slack token cannot be null");
        }
        this.token = newToken;
    }

    /**
     * Get the current Slack token.
     *
     * @return The Slack token.
     */
    public String getToken() {
        return String.valueOf(token);
    }
}
