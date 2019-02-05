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
  --redirect_uri https://<hostname of LDAP Simple UI> \
  --scope openid,email,profile,roles \
  --secret ......... 
```

## Deploy to Kubernetes


```yaml
cat <<EOF > ldap-namespace.yml
apiVersion: v1
kind: Namespace
metadata:
  name: ldap
EOF

kubectl apply -f ldap-namespace.yml

kubectl create -n ldap secret generic ldap-secret \
  --from-literal=uaa-url=https://<UAA URL> \
  --from-literal=uaa-client-secret=<client secret created above> \
  --from-file=ldap-ca=./ldap_ca.pem \
  --from-literal=ldap-url=ldaps://<LDAP IP>:636 \
  --from-literal=ldap-base=ou=people,dc=example,dc=com \
  --from-literal=ldap-username=cn=admin,dc=example,dc=com \
  --from-literal=ldap-password=<admin password> \
  --dry-run -o yaml > ldap-secret.yml
  
kubectl apply -f ldap-secret.yml

cat <<EOF > ldap-simple-ui.yml
kind: Service
apiVersion: v1
metadata:
  name: ldap-simple-ui
  namespace: ldap
  labels:
    app: ldap-simple-ui
spec:
  selector:
    app: ldap-simple-ui
  ports:
  - protocol: TCP
    port: 8080
  type: NodePort
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: ldap-simple-ui
  namespace: ldap
  labels:
    app: ldap-simple-ui
spec:
  replicas: 1
  revisionHistoryLimit: 4
  strategy:
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
    type: RollingUpdate
  template:
    metadata:
      labels:
        app: ldap-simple-ui
      annotations:
        kubernetes.io/change-cause: "FIXME"
    spec:
      containers:
      - name: ldap-simple-ui
        image: making/ldap-simple-ui
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
        env:
        - name: SERVER_PORT
          value: "8080"
        - name: SPRING_PROFILES_ACTIVE
          value: kubernetes
        - name: SPRING_APPLICATION_NAME
          value: "${NAMESPACE}:ldap-simple-ui"
        - name: NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        - name: UAA_URL
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: uaa-url
        - name: SPRING_LDAP_URLS
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: ldap-url
        - name: SPRING_LDAP_BASE
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: ldap-base
        - name: SPRING_LDAP_USERNAME
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: ldap-username
        - name: SPRING_LDAP_PASSWORD
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: ldap-password
        - name: SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_UAA_CLIENT_SECRET
          valueFrom:
            secretKeyRef:
              name: ldap-secret
              key: uaa-client-secret
        volumeMounts:
        - name: tmp
          mountPath: /home/vcap/tmp
        - name: ldap-ca
          mountPath: /etc/ssl/certs
        resources:
          limits:
            memory: "386Mi"
          requests:
            memory: "386Mi"
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 15
          timeoutSeconds: 3
          periodSeconds: 10
          failureThreshold: 3
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 180
          timeoutSeconds: 3
          periodSeconds: 10
          failureThreshold: 3
      volumes:
      - name: tmp
        emptyDir: {}
      - name: ldap-ca
        secret:
          secretName: ldap-secret
          items:
          - key: ldap-ca
            path: ca-certificates.crt
EOF

kubectl apply -f ldap-simple-ui.yml
```

## License

Apache License 2.0
