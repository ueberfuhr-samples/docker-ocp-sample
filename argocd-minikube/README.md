# ArgoCD

ArgoCD is a tool that is used for GitOps. It reads Kubernetes resource YAMLs or
Helm Charts from a (Git) source repository and deploys it to Kubernetes - and keeps the
deployment up-to-date with the sources.

To install ArgoCD into minikube, we follow 
[this tutorial](https://redhat-scholars.github.io/argocd-tutorial/argocd-tutorial/01-setup.html).

Then, we run the following commands (using a profile named ``gitops`):

```bash
# start minikube
minikube start -p "gitops"
# find out the local port where ArgoCD can be opened in the browser
# on Mac, we might get a message that we should keep the process running, otherwise, we'll get other ports
minikube -n "argocd" -p "gitops" service "argocd-server" --url
# we copy the URL and open it in the browser
# we can login using username "admin", the password is available with this command:
kubectl -n "argocd" get secret "argocd-initial-admin-secret" -o jsonpath="{.data.password}" | base64 -d
# to login in the CLI, we can use
argocd login --insecure --grpc-web "localhost:<port>"  --username "admin" --password "<password>"
# we can then create a project
kubectl apply -f ArgoCD-Application.yam
```
