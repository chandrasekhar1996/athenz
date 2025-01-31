package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.auth.Authority;
import com.yahoo.athenz.auth.util.StringUtils;
import com.yahoo.athenz.common.server.notification.NotificationDomainMeta;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class NotificationToSlackMessageConverterCommon1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationToSlackMessageConverterCommon1.class);

    private static final String AT = "@";
    private static final String SLACK_CHANNEL_PREFIX = "#";
    private static final String PROP_NOTIFICATION_USER_AUTHORITY = "athenz.notification_user_authority";
    private static final String PROP_NOTIFICATION_WORKFLOW_URL = "athenz.notification_workflow_url";
    private static final String PROP_NOTIFICATION_ATHENZ_UI_URL = "athenz.notification_athenz_ui_url";
    private static final String USER_DOMAIN_DEFAULT = "user";
    private static final String PROP_USER_DOMAIN = "athenz.user_domain";
    private static final String PROP_NOTIFICATION_EMAIL_DOMAIN_TO = "athenz.notification_email_domain_to";

    private final String userDomainPrefix;
    private final String workflowUrl;
    private final String athenzUIUrl;
    private final String emailDomainTo;
    private final Authority notificationUserAuthority;

    public NotificationToSlackMessageConverterCommon1(Authority notificationUserAuthority) {
        emailDomainTo = System.getProperty(PROP_NOTIFICATION_EMAIL_DOMAIN_TO);
        userDomainPrefix = System.getProperty(PROP_USER_DOMAIN, USER_DOMAIN_DEFAULT);
        workflowUrl = System.getProperty(PROP_NOTIFICATION_WORKFLOW_URL);
        athenzUIUrl = System.getProperty(PROP_NOTIFICATION_ATHENZ_UI_URL);

        final String configuredNotificationAuthority = System.getProperty(PROP_NOTIFICATION_USER_AUTHORITY);
        if (configuredNotificationAuthority != null) {
            this.notificationUserAuthority = loadNotificationUserAuthority(configuredNotificationAuthority);
        } else {
            this.notificationUserAuthority = notificationUserAuthority;
        }
    }

    private Authority loadNotificationUserAuthority(String className) {
        LOGGER.debug("Loading Notification user authority {}...", className);

        Authority authority;
        try {
            authority = (Authority) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            LOGGER.error("Invalid Notification user Authority class: {}", className, ex);
            return null;
        }
        return authority;
    }

    public String getFullyQualifiedEmailAddress(String userName) {
        if (notificationUserAuthority != null) {
            String email = notificationUserAuthority.getUserEmail(userName);
            if (!StringUtil.isEmpty(email)) {
                return email;
            }
        }
        return userName.replaceAll(userDomainPrefix, "") + AT + emailDomainTo;
    }

    public String getWorkflowUrl() {
        return workflowUrl;
    }

    public String getAthenzUIUrl() {
        return athenzUIUrl;
    }

    public String generateSlackMessageFromTemplate(Map<String, Object> dataModel, String templateContent) {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        try {
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);

            StringTemplateLoader stringLoader = new StringTemplateLoader();

            String templateName = "slackBlocksTemplate";
            stringLoader.putTemplate(templateName, templateContent);
            cfg.setTemplateLoader(stringLoader);

            Template template = cfg.getTemplate("slackBlocksTemplate");
            StringWriter out = new StringWriter();
            template.process(dataModel, out);

            return out.toString();
        } catch (Exception e) {
            LOGGER.error("Error processing Slack template: {}", e.getMessage());
            return null;
        }
    }

    public String getDomainLink(String domainName) {
        if (StringUtils.isEmpty(athenzUIUrl)) {
            return "";
        }
        return athenzUIUrl + "/domain/" + domainName;
    }

    public String getRoleLink(String domainName, String roleName) {
        if (StringUtils.isEmpty(athenzUIUrl)) {
            return "";
        }
        return athenzUIUrl + "/domain/" + domainName + "/role/" + roleName + "/members";
    }

    public String getGroupLink(String domainName, String groupName) {
        if (StringUtils.isEmpty(athenzUIUrl)) {
            return "";
        }
        return athenzUIUrl + "/domain/" + domainName + "/group/" + groupName + "/members";
    }

    public Set<String> getSlackRecipients(Set<String> notificationRecipients, Map<String, NotificationDomainMeta> domainMetaMap) {
        Set<String> slackRecipients = new HashSet<>();

        for (String recipient: notificationRecipients) {
            if (recipient.startsWith(userDomainPrefix)) {
                // if user then convert to email id
                String emailId = getFullyQualifiedEmailAddress(recipient);
                slackRecipients.add(emailId);
            } else {
                // for domain name, use domainMetaMap to get slack channel
                NotificationDomainMeta domainMeta = domainMetaMap.get(recipient);
                if (domainMeta != null && !StringUtils.isEmpty(domainMeta.getSlackChannel())) {
                    slackRecipients.add(domainMeta.getSlackChannel());
                }
            }
        }

        return slackRecipients;
    }


}
