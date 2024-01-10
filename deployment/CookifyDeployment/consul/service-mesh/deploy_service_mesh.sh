kubectl apply --filename hashicups/intentions/allow.yaml

# validate
consul catalog services
consul intention list