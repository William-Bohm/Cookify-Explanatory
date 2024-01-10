# apply new values
kubectl apply -f proxy/proxy-defaults.yaml

# restart sidecars/containers
kubectl delete --filename hashicups/v1/
kubectl apply --filename hashicups/v1/

# confirm
kubectl port-forward deploy/frontend 19000:19000
http://localhost:19000/config_dump
