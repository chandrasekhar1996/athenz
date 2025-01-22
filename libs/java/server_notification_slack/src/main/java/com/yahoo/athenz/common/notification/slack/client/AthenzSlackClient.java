package com.yahoo.athenz.common.notification.slack.client;

import com.slack.api.bolt.App;
import com.slack.api.model.block.LayoutBlock;

import java.util.List;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.bolt.service.InstallationService;
import com.slack.api.bolt.service.OAuthStateService;
import com.slack.api.bolt.service.builtin.AmazonS3InstallationService;
import com.slack.api.bolt.service.builtin.AmazonS3OAuthStateService;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.*;
public class AthenzSlackClient {

    public AthenzSlackClient() throws Exception {
        // The standard AWS env variables are expected
        // export AWS_REGION=us-east-1
        // export AWS_ACCESS_KEY_ID=AAAA*************
        // export AWS_SECRET_ACCESS_KEY=4o7***********************

        // Please be careful about the security policies on this bucket.
        String awsS3BucketName = "YOUR_OWN_BUCKET_NAME_HERE";

        InstallationService installationService = new AmazonS3InstallationService(awsS3BucketName);
        // Set true if you'd like to store every single installation as a different record
        installationService.setHistoricalDataEnabled(false);

        // apiApp uses only InstallationService to access stored tokens
        App apiApp = new App();
        apiApp.command("/hi", (req, ctx) -> {
            return ctx.ack("Hi there!");
        });
        apiApp.service(installationService);

        // Needless to say, oauthApp uses InstallationService
        // In addition, it uses OAuthStateService to create/read/delete state parameters
        App oauthApp = new App().asOAuthApp(true);
        oauthApp.service(installationService);

        // Store valid state parameter values in Amazon S3 storage
        OAuthStateService stateService = new AmazonS3OAuthStateService(awsS3BucketName);
        // This service is necessary only for OAuth flow apps
        oauthApp.service(stateService);

        // Mount the two apps with their root path
        SlackAppServer server = new SlackAppServer(new HashMap<>(Map.ofEntries(
                entry("/slack/events", apiApp), // POST /slack/events (incoming API requests from the Slack Platform)
                entry("/slack/oauth", oauthApp) // GET  /slack/oauth/start, /slack/oauth/callback (user access)
        )));

        server.start(); // http://localhost:3000

    }

}

