#!/bin/bash

# Use Helm to install the chart
helm install userservice ./UserService


# Wait for the deployment to complete
while [[ $(kubectl get pods -l app=userservice -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do 
  echo "waiting for pod" && sleep 5; 
done

echo "Helm deployment has been successfully deployed"