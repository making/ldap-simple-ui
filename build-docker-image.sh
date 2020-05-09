#!/bin/bash
set -e
./mvnw clean package -DskipTests
# VERSION=$(grep '<version>' pom.xml | head -n 2 | tail -n 1 | sed -e 's|<version>||g' -e 's|</version>||g' -e 's| ||g')
VERSION=latest
pack build making/ldap-simple-ui:${VERSION} \
  -p backend/target/backend-*.jar  \
  --builder gcr.io/paketo-buildpacks/builder:base
docker push making/ldap-simple-ui:${VERSION}