# Terraform

[Terraform](https://www.terraform.io/) is an infrastructure as code tool that lets you build, change,
and version cloud and on-prem resources safely and efficiently.

We can find a good tutorial [here](https://dev.to/chefgs/deploy-kubernetes-resources-in-minikube-cluster-using-terraform-1p8o).

In this repository, we
- create a minikube profile (_config context_) named `terraform` (see file `~/.kube/config`)
- create a kube namespace named `petclinic`
- a deployment, service and ingress (compare with [ArgoCD sample](../argocd-minikube/resources))

```bash
minikube start -p terraform
```
