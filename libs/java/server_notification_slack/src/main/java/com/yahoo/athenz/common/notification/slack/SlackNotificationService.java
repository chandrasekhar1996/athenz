/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yahoo.athenz.common.notification.slack;

import com.yahoo.athenz.common.server.notification.Notification;
import com.yahoo.athenz.common.server.notification.NotificationService;
import com.yahoo.athenz.common.server.notification.NotificationSlackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/*
 * Slack based notification service.
 */
public class SlackNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotificationService.class);

    private static final String AT = "@";

    private final SlackClient slackClient;

        public SlackNotificationService(SlackClient slackClient) {
        this.slackClient = slackClient;
    }


    @Override
    public boolean notify(Notification notification) {
        if (notification == null) {
            return false;
        }

        NotificationSlackMessage notificationSlackMessage = notification.getNotificationAsSlackMessage();
        if (notificationSlackMessage == null) {
            return false;
        }

        Set<String> recipients = notificationSlackMessage.getRecepients();
        final String message = notificationSlackMessage.getMessage();

        // if our list of recipients is empty then we have nothing to do,
        // but we want to log it for debugging purposes

        if (recipients.isEmpty()) {
            LOGGER.error("No recipients specified in the notification. Subject={}", message);
            return false;
        }

        if (sendSlackMessage(recipients, message)) {
            LOGGER.info("Successfully sent email notification. Subject={}, Recipients={}", message, recipients);
            return true;
        } else {
            LOGGER.error("Failed sending email notification. Subject={}, Recipients={}", message, recipients);
            return false;
        }
    }

//    public boolean sendEmail(Set<String> recipients, String subject, String body) {
//        final AtomicInteger counter = new AtomicInteger();
//        // SES imposes a limit of 50 recipients. So we convert the recipients into batches
//        if (recipients.size() > SES_RECIPIENTS_LIMIT_PER_MESSAGE) {
//            final Collection<List<String>> recipientsBatch = recipients.stream()
//                    .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / SES_RECIPIENTS_LIMIT_PER_MESSAGE))
//                    .values();
//            boolean status = true;
//            for (List<String> recipientsSegment : recipientsBatch) {
//                if (!sendEmailMIME(subject, body, recipientsSegment)) {
//                    status = false;
//                }
//            }
//            return status;
//        } else {
//            return sendEmailMIME(subject, body, new ArrayList<>(recipients));
//        }
//    }

    public boolean sendSlackMessage(Set<String> recipients, String message) {
        return slackClient.sendMessage(recipients, message);
    }


//    private boolean sendEmailMIME(String subject, String body, Collection<String> recipients) {
//        MimeMessage mimeMessage;
//        try {
//            mimeMessage = getMimeMessage(subject, body, recipients, from + AT + emailDomainFrom, logoImage);
//        } catch (MessagingException ex) {
//            LOGGER.error("The email could not be sent. Error message: {}", ex.getMessage());
//            return false;
//        }
//
//        return emailProvider.sendEmail(recipients, from + AT + emailDomainFrom, mimeMessage);
//    }


}
