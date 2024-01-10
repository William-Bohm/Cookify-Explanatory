#!/bin/bash

# Use Helm to install the chart
helm install photoservice ./PhotoService


# Wait for the deployment to complete
while [[ $(kubectl get pods -l app=photoservice -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do 
  echo "waiting for pod" && sleep 5; 
done

echo "Helm deployment has been successfully deployed"