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
"text": "*${notificationDate}*"
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
<#-- Now loop through each member to create the repeated blocks -->
<#list members as member>
    ,{
    "type": "section",
    "text": {
    "type": "mrkdwn",
    "text": "*<${member.domain}:${member.role}>*"
    }
    },
    {
    "type": "section",
    "fields": [
    {
    "type": "mrkdwn",
    "text": "*Domain:*\n<${member.domain}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Expiration:*\n${member.expiration}"
    },
    {
    "type": "mrkdwn",
    "text": "*Role:*\n<${member.role}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Member:*\n${member.member}"
    },
    <#if member.notes?? && (member.notes?length > 0)>
        ,{
        "type": "mrkdwn",
        "text": "*Notes:*\n${member.notes}"
        }
    </#if>
    ]
    },
    {
    "type": "divider"
    }
</#list>
]