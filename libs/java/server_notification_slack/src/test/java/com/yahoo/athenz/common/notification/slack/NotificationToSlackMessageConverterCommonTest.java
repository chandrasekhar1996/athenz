package com.yahoo.athenz.common.notification.slack;

public class NotificationToSlackMessageConverterCommonTest {
//    @Test
//    public void testGenerateSlackMessageFromTemplateEmptyNotes() {
//        System.setProperty("athenz.notification_athenz_ui_url", "https://athenz.io");
//        PrivateKeyStore mockPrivateKeyStore = mock(PrivateKeyStore.class);
//        when(mockPrivateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("test".toCharArray());
//        NotificationToSlackMessageConverterCommon1 converter = new NotificationToSlackMessageConverterCommon1(null);
//        Map<String, Object> rootDataModel = new HashMap<>();
//
//        List<Map<String, String>> dataModel = new ArrayList<>();
//        Map<String, String> role1 = new HashMap<>();
//        role1.put("domain", "athenz");
//        role1.put("role", "admin");
//        role1.put("member", "user.joe");
//        role1.put("expiration", "2023-01-01T000000Z");
//        role1.put("notes", "");
//        role1.put("roleLink", converter.getRoleLink("athenz", "admin"));
//        role1.put("domainLink", converter.getDomainLink("athenz"));
//
//        dataModel.add(role1);
//        rootDataModel.put("roleData", dataModel);
//        String slackTemplate = NotificationConverterCommon1.readContentFromFile(getClass().getClassLoader(),"messages/slack-role-member-expiry.ftl");
//        String entryNames = "athenz;admin;user.joe;2023-01-01T000000Z|athenz;readers;user.jane;2023-01-01T000000Z|athenz;writers;user.bad";
//        Map<String, String> metaDetails = new HashMap<>();
//        metaDetails.put(NOTIFICATION_DETAILS_ROLES_LIST, entryNames);
//        String slackMessage = converter.generateSlackMessageFromTemplate(rootDataModel, slackTemplate);
//        assertEquals(slackMessage, NotificationConverterCommon1.readContentFromFile(getClass().getClassLoader(),
//                "messages/role-member-expiry-slack.txt"));
//
//        System.clearProperty("notification_athenz_ui_url");
//    }
//
//    @Test
//    public void testGenerateSlackMessageFromTemplateMultipleRoles() {
//        System.setProperty("athenz.notification_athenz_ui_url", "https://athenz.io");
//        PrivateKeyStore mockPrivateKeyStore = mock(PrivateKeyStore.class);
//        when(mockPrivateKeyStore.getSecret(anyString(), anyString(), anyString())).thenReturn("test".toCharArray());
//        NotificationToSlackMessageConverterCommon1 converter = new NotificationToSlackMessageConverterCommon1(null);
//        Map<String, Object> rootDataModel = new HashMap<>();
//
//        List<Map<String, String>> dataModel = new ArrayList<>();
//        Map<String, String> role1 = new HashMap<>();
//        role1.put("domain", "athenz");
//        role1.put("role", "admin");
//        role1.put("member", "user.joe");
//        role1.put("expiration", "2023-01-01T000000Z");
//        role1.put("notes", "");
//        role1.put("roleLink", converter.getRoleLink("athenz", "admin"));
//        role1.put("domainLink", converter.getDomainLink("athenz"));
//
//        dataModel.add(role1);
//
//        Map<String, String> role2 = new HashMap<>();
//        role2.put("domain", "athenz.dev");
//        role2.put("role", "admin-dev");
//        role2.put("member", "user.john");
//        role2.put("expiration", "2024-01-01T000000Z");
//        role2.put("notes", "Lorem Ipsum");
//        role2.put("roleLink", converter.getRoleLink("athenz", "admin-dev"));
//        role2.put("domainLink", converter.getDomainLink("athenz"));
//
//        dataModel.add(role2);
//        rootDataModel.put("roleData", dataModel);
//
//        String slackTemplate = NotificationConverterCommon1.readContentFromFile(getClass().getClassLoader(),"messages/slack-role-member-expiry.ftl");
//        String entryNames = "athenz;admin;user.joe;2023-01-01T000000Z|athenz;readers;user.jane;2023-01-01T000000Z|athenz;writers;user.bad";
//        Map<String, String> metaDetails = new HashMap<>();
//        metaDetails.put(NOTIFICATION_DETAILS_ROLES_LIST, entryNames);
//        String slackMessage = converter.generateSlackMessageFromTemplate(rootDataModel, slackTemplate);
//        assertEquals(slackMessage, NotificationConverterCommon1.readContentFromFile(getClass().getClassLoader(),
//                "messages/role-member-expiry-multiple-roles-slack.txt"));
//
//        System.clearProperty("notification_athenz_ui_url");
//    }
}
