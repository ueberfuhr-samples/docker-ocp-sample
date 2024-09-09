# Terraform

[Terraform](https://www.terraform.io/) is an infrastructure as code tool that lets you build, change,
and version cloud and on-prem resources safely and efficiently. The main advantage
is that we get a unified way to manage infrastructure accross multiple cloud providers.

We can find a good
tutorial [here](https://dev.to/chefgs/deploy-kubernetes-resources-in-minikube-cluster-using-terraform-1p8o).

In this repository, we

- create a minikube profile (_config context_) named `terraform` (see file `~/.kube/config`)
- create a kube namespace named `petclinic`
- a deployment, service and ingress (compare with [ArgoCD sample](../argocd-minikube/resources))

## Install and run Terraform

We need the following commands:

```bash
# configure minikube to use the latest kubernetes version
minikube config set kubernetes-version v1.30.0
# run minikube in the 'terraform' profile
minikube start -p terraform
# might be necessary for terraform to detect the service
minikube tunnel -p terraform --cleanup
# install Terraform CLI
brew install terraform
# Initialize current directory as working directory
terraform init
# show the changes required by the configuration
terraform plan
# do the changes
terraform apply
# clear down resources
terraform destroy
```

