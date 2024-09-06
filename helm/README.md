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
helm dashboard --port 8765
```

## First sample

We might want to install the CrunchyData Postgres Operator (PGO). We could do this simply with this command:

```bash
helm install pgo oci://registry.developers.crunchydata.com/crunchydata/pgo -n openshift-operators
```

## Steps to install the application

### Create Helm Chart

We first need to create a Helm Chart. This can easily be done by invoking

```bash
helm create <chart-name>
```

A Helm chart contains at least the following artifacts:
 - a [`Chart.yaml`](chart/Chart.yaml) file providing general chart settings
 - a [`values.yaml`](chart/values.yaml) file providing parameters that can be replaced in the templates
 - a [`templates`](chart/templates) folder containing the Kubernetes YAMLs that may contain placeholders
   that are replaced by the values in the `values.yaml` file
   (those files are treated as [Go templates](https://pkg.go.dev/text/template))
Optionally, it can also contain
  - a [README](chart/README.md)
  - some [notes](chart/templates/NOTES.txt) that are displayed after installation
  - a [JSON schema](chart/values.schema.json) to describe the `values.yaml`
  - [staging-specific YAMLs](chart/ENV_LOCAL_CRC.yaml) to overwrite values from `values.yaml`

We can find details in the [Helm Documentation](https://helm.sh/docs/topics/charts/).

### Migrate Kubernetes YAMLs to Go Templates

The Kubernetes YAML templates need to be transferred to Go templates. This means esp.
 - replacing [_Kubernetes templates_](https://docs.openshift.com/container-platform/4.16/openshift_images/using-templates.html)
   by [Go templates](https://pkg.go.dev/text/template)[Go templates](https://pkg.go.dev/text/template).
   We can find a good tutorial in the [Red Hat Blog](https://www.redhat.com/en/blog/from-templates-to-openshift-helm-charts). 
 - splitting YAMLs that contain multiple resources - each template must contain only a single Kubernetes resource
 - using service type `NodePort` for the database service, i.e. it is not available via a static IP,
   but a static port number (in range `30000..32767`).
   (for details, see this [sample solution](https://stackoverflow.com/questions/67926772/how-to-connect-to-ibm-mq-deployed-to-openshift/67927780#67927780).)

### Install the Helm Chart directly from source

We can install the helm chart with this command:
```bash
# --values overwrites default values (helpful for staging)
helm install petclinic . --values=ENV_LOCAL_CRC.yaml -n "<openshift-project>"
```

### Using an OCI image

We can create OCI image and push them to the container image registry. This allows to deliver and re-use Helm Charts.

```bash
helm package .
# Successfully packaged chart and saved it to: <...>/spring-petclinic-oci-0.0.1.tgz

# Login and push to DockerHub (https://docs.docker.com/docker-hub/oci-artifacts/)
echo "<password>" | helm registry login registry-1.docker.io -u <user> --password-stdin
helm push spring-petclinic-oci-0.0.1.tgz oci://registry-1.docker.io/<your-namespace>
```

We can then install the Kubernetes resources with this command:

```bash
helm install petclinic oci://registry-1.docker.io/<your-namespace>/spring-petclinic-oci --values=ENV_LOCAL_CRC.yaml -n "<openshift-project>"
```
