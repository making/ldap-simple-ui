# LDAP Simple UI

A web UI for updating a LDAP user.
This UI is intended to work as an OAuth2 Client for a OIDC provider which support LDAP integration such as Cloud Foundry UAA.

![image](https://user-images.githubusercontent.com/106908/51087916-b7f2f200-179c-11e9-908e-d9dda988f694.png)

## Create an UAA Client 

```
uaac client add ldap-simple-ui \
  --name ユーザー情報変更 \
  --access_token_validity 604800 \
  --refresh_token_validity 1209600 \
  --authorized_grant_types authorization_code,refresh_token \
  --redirect-uri https://<hostname of LDAP Simple UI> \
  --scope openid,email,profile,roles \
  --secret ......... \
  --show_on_homepage true \
  --app_launch_url https://<hostname of LDAP Simple UI>
```

## License

Apache License 2.0
