[
{
"type": "header",
"text": {
"type": "plain_text",
"text": ":clock1: Athenz Domain Role Member Review Notification :clock1:",
"emoji": true
}
},
{
"type": "section",
"text": {
"type": "mrkdwn",
"text": "You have one or more principals in your Athenz roles whose review date will pass soon. Please review this list and, if necessary, login to *<${uiUrl}|Athenz UI>* to extend their review dates.",
"verbatim": false
}
},
{
"type": "divider"
}
<#list memberData as item>
    ,{
    "type": "section",
    "text": {
    "type": "mrkdwn",
    "text": "*<${item["roleLink"]}|${item["domain"]}:role.${item["role"]}>*"
    }
    },
    {
    "type": "section",
    "fields": [
    {
    "type": "mrkdwn",
    "text": "*Domain:*\n<${item["domainLink"]}|${item["domain"]}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Review:*\n${item["expiration"]}"
    },
    {
    "type": "mrkdwn",
    "text": "*Role:*\n<${item["roleLink"]}|${item["role"]}>"
    },
    {
    "type": "mrkdwn",
    "text": "*Member:*\n${item["member"]}"
    }
    <#if item["notes"]?has_content>
        ,{
        "type": "mrkdwn",
        "text": "*Notes:*\n${item["notes"]}"
        }
    </#if>
    ]
    },
    {
    "type": "divider"
    }
</#list>
]