# upload configmap
$release_name = "mysql"
$configmap_name = "mysql-config"
$namespace = "default"
$interval_seconds = 5
$timeout_seconds = 300

# upload config file
kubectl apply -f ./app/database/MYSQL/MySQLConfigMap.yaml

# make sure it has been uploaded
$elapsed_seconds = 0
while ($true) {
    if (kubectl get configmap $configmap_name -n $namespace 2>$null) {
        Write-Host "ConfigMap '$configmap_name' found!"
        break  # Exit the loop, but continue the script
    }
    
    Start-Sleep -Seconds $interval_seconds
    $elapsed_seconds = $elapsed_seconds + $interval_seconds
    
    if ($elapsed_seconds -ge $timeout_seconds) {
        Write-Host "Timeout: ConfigMap '$configmap_name' not found within $timeout_seconds seconds."
        exit 1  # Exit the entire script since this is an error condition
    }
}

# install Helm chart
Write-Host "Installing mysql-config Helm chart..."
helm install $release_name .\app\database\MySQL\mysql\

# wait for it to be officially deployed
Write-Host "Waiting for mysql to be officially deployed..."

$elapsed_seconds = 0
while ($true) {
    # Using an updated JSONPath expression to check status.phase field of the pods
    $podStatuses = kubectl get pods -n $namespace -l "app.kubernetes.io/instance=$release_name" -o jsonpath='{.items[*].status.phase}'
    
    # Checking if all pods are in 'Running' phase
    $allRunning = $podStatuses.Split(' ') -notcontains 'Pending'
    
    if ($allRunning) {
        Write-Host "mysql is officially deployed and ready!"
        break  # Exit the loop, but continue the script
    }
    
    Start-Sleep -Seconds $interval_seconds
    $elapsed_seconds = $elapsed_seconds + $interval_seconds
    
    if ($elapsed_seconds -ge $timeout_seconds) {
        Write-Host "Timeout: mysql did not become ready within $timeout_seconds seconds."
        exit 1  # Exit the entire script since this is an error condition
    }
}
