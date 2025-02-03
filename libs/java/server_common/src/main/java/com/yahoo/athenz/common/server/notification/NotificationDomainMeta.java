package com.yahoo.athenz.common.server.notification;

public class NotificationDomainMeta {

    private String domainName;
    private String slackChannel;

    public NotificationDomainMeta(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public NotificationDomainMeta setSlackChannel(String slackChannel) {
        this.slackChannel = slackChannel;
        return this;
    }

    public String getSlackChannel() {
        return slackChannel;
    }
}
