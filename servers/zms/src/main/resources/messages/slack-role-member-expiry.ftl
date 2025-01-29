[
{
"type": "header",
"text": {
"type": "plain_text",
"text": ":athenz: Role Member Expiration Notification :clock1:",
"emoji": true
}
},
{
"type": "context",
"elements": [
{
"type": "mrkdwn",
"text": "*${notificationDate?string("MMMM d, yyyy")}*"
}
]
},
{
"type": "section",
"text": {
"type": "mrkdwn",
"text": "Please review this list and, if necessary, follow up with the respective domain administrators to extend those expiration dates.",
"verbatim": false
}
},
{
"type": "divider"
}
<#list roles as role>
    ,
    {
    "type": "section",
    "text": {
    "type": "mrkdwn",
    "text": "*<${role.roleLink}|${role.roleName}>*"
    }
    },
    {
    "type": "section",
    "fields": [
    {
    "type": "mrkdwn",
    "text": "*Domain:*\n<${role.domainLink}|${role.domain}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Expiration:*\n${role.expiration}"
    },
    {
    "type": "mrkdwn",
    "text": "*Role:*\n<${role.roleLink}|${role.roleShortName}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Member:*\n${role.member}"
    }
    <#if role.notes?has_content>
        ,
        {
        "type": "mrkdwn",
        "text": "*Notes:*\n${role.notes}"
        }
    </#if>
    ]
    },
    {
    "type": "divider"
    }
</#list>
]