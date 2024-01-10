#!/bin/bash


# uplaod configmap
release_name="mysql"
configmap_name="mysql-config"
namespace="default"
interval_seconds=5
timeout_seconds=300

# upload config file
kubectl apply -f MySQLConfigMap.yaml


# make sure it has been uploaded
elapsed_seconds=0
while true; do
  if [[ $(kubectl get configmap "$configmap_name" -n "$namespace" 2>/dev/null) ]]; then
    echo "ConfigMap '$configmap_name' found!"
    exit 0
  fi

  sleep "$interval_seconds"
  elapsed_seconds=$((elapsed_seconds + interval_seconds))

  if [[ elapsed_seconds -ge timeout_seconds ]]; then
    echo "Timeout: ConfigMap '$configmap_name' not found within $timeout_seconds seconds."
    exit 1
  fi
done


# install Helm chart
echo "Installing mysql Helm chart..."
helm install "$release_name" ./mysql


# wait for it to be offically deployed
echo "Waiting for mysql to be officially deployed..."

elapsed_seconds=0
while true; do
  status=$(kubectl get pods -n "$namespace" -l "app.kubernetes.io/instance=$release_name" -o jsonpath='{.items[*].status.conditions[?(@.type=="Ready")].status}')

  if [[ $status == "True" ]]; then
    echo "mysql is officially deployed and ready!"
    exit 0
  fi

  sleep "$interval_seconds"
  elapsed_seconds=$((elapsed_seconds + interval_seconds))

  if [[ elapsed_seconds -ge timeout_seconds ]]; then
    echo "Timeout: MonmysqlgoDB did not become ready within $timeout_seconds seconds."
    exit 1
  fi
done