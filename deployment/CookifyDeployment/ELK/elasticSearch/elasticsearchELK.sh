#!/bin/bash


# uplaod configmap
release_name="elasticsearchELK"
namespace="default"
interval_seconds=5
timeout_seconds=300

# install Helm chart
echo "Installing elasticsearch ELK Helm chart..."
helm install "$release_name" ./elasticsearchelk


# wait for it to be offically deployed
echo "Waiting for elasticsearch ELK to be officially deployed..."

elapsed_seconds=0
while true; do
  status=$(kubectl get pods -n "$namespace" -l "app.kubernetes.io/instance=$release_name" -o jsonpath='{.items[*].status.conditions[?(@.type=="Ready")].status}')

  if [[ $status == "True" ]]; then
    echo "elasticsearch ELK is officially deployed and ready!"
    exit 0
  fi

  sleep "$interval_seconds"
  elapsed_seconds=$((elapsed_seconds + interval_seconds))

  if [[ elapsed_seconds -ge timeout_seconds ]]; then
    echo "Timeout: elasticsearch ELK did not become ready within $timeout_seconds seconds."
    exit 1
  fi
done