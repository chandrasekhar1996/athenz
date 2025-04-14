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

package com.yahoo.athenz.common.server.notification;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NotificationSlackMessage {

    private final Set<String> recipients;
    private final List<String> messageList;

    public NotificationSlackMessage(List<String> messageList, Set<String> recipients) {
        this.recipients = recipients;
        this.messageList = messageList;
    }

    public List<String> getMessageList() {
        return messageList;
    }


    public Set<String> getRecipients() {
        return recipients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationSlackMessage that = (NotificationSlackMessage) o;
        return  Objects.equals(getMessageList(), that.getMessageList()) &&
                Objects.equals(getRecipients(), that.getRecipients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessageList(), getRecipients());
    }
}
