They are not starting but I see no error:
PS C:\Users\wsboh\documents\Programming\projects\mealBud\deployment\CookifyDeployment\ELK\elasticSearch> kubectl get pods
NAME                       READY   STATUS     RESTARTS   AGE
docker-cluster-master-0    0/2     Init:1/2   0          4m41s
docker-cluster-master-1    0/2     Init:1/2   0          4m41s
docker-cluster-master-2    0/2     Init:1/2   0          4m41s
mongodb-6f67878f55-hnlvx   2/2     Running    0          36m

here is the kubectl events from the pod:
Events:
  Type    Reason                  Age    From                     Message
  ----    ------                  ----   ----                     -------
  Normal  Scheduled               3m53s  default-scheduler        Successfully assigned default/docker-cluster-master-0 to ip-10-0-1-244.us-west-2.compute.internal
  Normal  SuccessfulAttachVolume  3m51s  attachdetach-controller  AttachVolume.Attach succeeded for volume "pvc-c5faacdf-798f-4902-87cd-0be44751a59c"
  Normal  Pulled                  3m51s  kubelet                  Container image "docker.elastic.co/elasticsearch/elasticsearch:8.6.1" already present on machine
  Normal  Created                 3m51s  kubelet                  Created container configure-sysctl
  Normal  Started                 3m50s  kubelet                  Started container configure-sysctl
  Normal  Pulled                  3m50s  kubelet                  Container image "hashicorp/consul-k8s-control-plane:1.0.2" already present on machine
  Normal  Created                 3m50s  kubelet                  Created container consul-connect-inject-init
  Normal  Started                 3m50s  kubelet                  Started container consul-connect-inject-init