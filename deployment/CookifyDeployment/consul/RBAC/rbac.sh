kubectl apply --filename hashicups/v2/

# validate intentions
consul catalog services | grep api-gateway
consul intention list