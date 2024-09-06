# ArgoCD

ArgoCD is a tool that is used for GitOps. It reads Kubernetes resource YAMLs or
Helm Charts from a (Git) source repository and deploys it to Kubernetes - and keeps the
deployment up-to-date with the sources.

To install ArgoCD into minikube, we follow 
[this tutorial](https://redhat-scholars.github.io/argocd-tutorial/argocd-tutorial/01-setup.html).

## Open ArgoCD WebUI

We use the following commands (using a profile named ``gitops`):

```bash
# start minikube
minikube start -p "gitops"
# find out the local port where ArgoCD can be opened in the browser
# on Mac, we might get a message that we should keep the process running, otherwise, we'll get other ports
minikube -n "argocd" -p "gitops" service "argocd-server" --url
# we copy the URL and open it in the browser
# we can login using username "admin", the password is available with this command:
kubectl -n "argocd" get secret "argocd-initial-admin-secret" -o jsonpath="{.data.password}" | base64 -d
# then, we can create a project in the Web UI
```

## Install the Petclinic sample with the CLI

```bash
# to login in the CLI, we can use
argocd login --insecure --grpc-web "localhost:<port>"  --username "admin" --password "<password>"
# we can then create the project
kubectl apply -f ArgoCD-Application.yaml
```

## Use the Petclinic App

We could open the Swagger UI in the browser by

```bash
# find out the URL
minikube -n petclinic -p gitops service "petclinic-service" --url
```

Alternatively, we could use `curl` and go through the ingress: 

```bash
# Non-Mac
curl --resolve "petclinic.devnation:80:$( minikube ip -p gitops )" -i http://petclinic.devnation/api/pettypes
# MacOS
minikube tunnel -p gitops # keep it running
curl --resolve "petclinic.devnation:443:127.0.0.1" -i https://petclinic.devnation/api/pettypes --insecure
```

> [!IMPORTANT]
> If we followed the instructions in the tutorial, we patched the `argocd-server` service to be of type
> `LoadBalancer`, which means that it is published on default HTTP(S) ports too. This might shadow our
> ingress, so we should patch it back to `ClusterIP`:
> 
> ```bash
> kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "ClusterIP"}}'
> ```
