Okay so If I am going to use the bolt protocol I should change my services/ports in my helm chart. What should I change?
Here is my values.yaml:
service:
  type: ClusterIP
  port: 7474

here is my deployment.yaml:
ports:
  - name: http
    containerPort: {{ .Values.service.port }}
    protocol: TCP
# probes
livenessProbe:
  exec:
    command:
    - bash
    - -c
    - "echo 'RETURN true' | cypher-shell -u neo4j -p WinterthusDoesntThinkTwice8374ChoiceButterSlinky --fail-at-end"
  initialDelaySeconds: 60
  periodSeconds: 10
readinessProbe:
  exec:
    command:
    - bash
    - -c
    - "echo 'RETURN true' | cypher-shell -u neo4j -p WinterthusDoesntThinkTwice8374ChoiceButterSlinky --fail-at-end"
  initialDelaySeconds: 30
  periodSeconds: 10


services.yaml:
ports:
  - port: {{ .Values.service.port }}
    targetPort: http
    protocol: TCP
    name: http