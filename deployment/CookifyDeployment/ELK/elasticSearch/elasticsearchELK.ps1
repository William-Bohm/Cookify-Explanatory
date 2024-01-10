# upload configmap
helm repo add elastic https://helm.elastic.co
helm repo update


$release_name = "elk-elastic-search"
$namespace = "default"
$interval_seconds = 5
$timeout_seconds = 300

# install Helm chart
Write-Host "Installing elasticsearchELK Helm chart..."
helm install $release_name -f .\elasticsearch.yml elastic/elasticsearch


# wait for it to be officially deployed
Write-Host "Waiting for elasticsearchELK to be officially deployed..."

$elapsed_seconds = 0
while ($true) {
    # Using an updated JSONPath expression to check status.phase field of the pods
    $podStatuses = kubectl get pods -n $namespace -l "app.kubernetes.io/instance=$release_name" -o jsonpath='{.items[*].status.phase}'
    
    # Checking if all pods are in 'Running' phase
    $allRunning = $podStatuses.Split(' ') -notcontains 'Pending'
    
    if ($allRunning) {
        Write-Host "elasticsearchELK is officially deployed and ready!"
        break  # Exit the loop, but continue the script
    }
    
    Start-Sleep -Seconds $interval_seconds
    $elapsed_seconds = $elapsed_seconds + $interval_seconds
    
    if ($elapsed_seconds -ge $timeout_seconds) {
        Write-Host "Timeout: elasticsearchELK did not become ready within $timeout_seconds seconds."
        exit 1  # Exit the entire script since this is an error condition
    }
}



