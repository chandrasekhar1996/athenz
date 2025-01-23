package com.yahoo.athenz.common.notification.slack.impl;

import com.yahoo.athenz.common.notification.slack.TokenLoader;
import io.athenz.server.aws.common.store.impl.S3ChangeLogStore;
import io.athenz.server.aws.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

public class ParameterStoreTokenLoader implements TokenLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterStoreTokenLoader.class);
    private final String parameterName;
    private final SsmClient ssmClient;

    public ParameterStoreTokenLoader(String parameterName, SsmClient ssmClient) {
        this.parameterName = parameterName;
        this.ssmClient = initClient();
    }

    SsmClient initClient() {
        try {
            Region region = Utils.getAwsRegion(Region.US_EAST_1);
            return SsmClient.builder().region(region).build();
        } catch (Exception ex) {
            LOGGER.error("Failed to init aws ssm client", ex);
        }
        return null;
    }

    @Override
    public String loadToken() {
        // Implement actual AWS Parameter Store fetching logic here.
        // This is a mock implementation.
        try {
            System.out.println("Fetching token from AWS Parameter Store: " + parameterName);
            // Replace with real AWS SDK code to fetch the parameter
            return "mock-token-from-parameter-store";
        } catch (Exception e) {
            throw new TokenLoadingException("Failed to load token from Parameter Store: " + parameterName, e);
        }
    }


}
