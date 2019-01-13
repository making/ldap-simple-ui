#!/bin/bash

cf local stage ldap-simple-ui -p ./backend/target/backend-0.0.1-SNAPSHOT.jar
cf local export ldap-simple-ui -r making/ldap-simple-ui
docker push making/ldap-simple-ui