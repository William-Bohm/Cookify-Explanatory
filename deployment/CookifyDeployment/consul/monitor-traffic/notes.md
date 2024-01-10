- .sh files

  - run traffic_monitoring.sh first
  - then run install_observability_suite.sh

- grafana

  - kubectl port-forward svc/grafana --namespace default 3000:3000
  - http://localhost:3000/d/hashicups/hashicups

- consul Ui
  - echo $CONSUL_HTTP_ADDR
  - echo $CONSUL_HTTP_TOKEN
  - Then take the two values and google the address then sign in with token
