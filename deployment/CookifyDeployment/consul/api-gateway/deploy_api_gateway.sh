kubectl apply --kustomize "github.com/hashicorp/consul-api-gateway/config/crd?ref=v0.5.1"

# upload files
kubectl apply --filename api-gw/consul-api-gateway.yaml --namespace consul && \
kubectl wait --for=condition=ready gateway/api-gateway --namespace consul --timeout=90s && \
kubectl apply --filename api-gw/routes.yaml --namespace consul

# verify
kubectl get pods --namespace consul | grep "api-gateway"
