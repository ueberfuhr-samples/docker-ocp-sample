# Helm Charts

## Install Helm locally

**Note:** For Helm, we need extended permissions on the Kubernetes cluster. If we do not have them, we could 
also install a local Kubernetes-Cluster, e.g.:
 - [Minikube](https://minikube.sigs.k8s.io/docs/start/)
 - [OpenShift Local Container (CRC)](https://github.com/crc-org/crc)

On Mac, we can install the Helm CLI and a Dashboard using these commands:

```bash
# install CLI
brew install helm
# install dashbard
helm plugin install https://github.com/komodorio/helm-dashboard.git
# login to the openshift cluster
# e.g. when using the OpenShift Local Container, after "crc start":
eval $(crc oc-env) && oc login -u kubeadmin https://api.crc.testing:6443
# run dashboard
helm dashboard
```

## First sample

We might want to install the CrunchyData Postgres Operator (PGO). We could do this simply with this command:

```bash
helm install pgo oci://registry.developers.crunchydata.com/crunchydata/pgo -n openshift-operators
```

## Steps to install the application

We first need to create a Helm Chart. For this, we need
 - a `Chart.yaml` file providing general chart settings
 - a `values.yaml` file providing parameters that can be replaced in the templates
 - a `templates` folder containing the Kubernetes YAMLs that may contain placeholders
   that are replaced by the values in the `values.yaml` file
   (those files are treated as [Go templates](https://pkg.go.dev/text/template))

We can find details in the [Helm Documentation](https://helm.sh/docs/topics/charts/).

The Kubernetes YAML templates need to be transferred to Go templates.
We can find a good tutorial in the [Red Hat Blog](https://www.redhat.com/en/blog/from-templates-to-openshift-helm-charts). 

The database service must be a `NodePort`, i.e. it is not available via a static IP, but a static port number (between 30000 and 32767).
(for details, see this [sample solution](https://stackoverflow.com/questions/67926772/how-to-connect-to-ibm-mq-deployed-to-openshift/67927780#67927780).)

Then, we can install the helm chart using
```bash
# --values overwrites default values (helpful for staging)
helm install petclinic . --values=ENV_LOCAL_CRC.yaml -n "<openshift-project>"
```
