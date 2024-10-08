# Installing the database as a _Pod_

## Provisioning Database Dependencies

First, we need a storage and external configuration that is used by the database container and could also be shared with others.

### _PersistentVolumeClaim_ to get persistent storage

To request a persistent storage for the database, we need to run:

```bash
# create the pvc (only once, it's not updatable)
oc apply -f Storage.yaml

# to check the pending state, we can call
oc describe pvc postgres-pvc

# This might be part of the output
  Type    Reason                Age                From                         Message
  ----    ------                ----               ----                         -------
  Normal  WaitForFirstConsumer  12s (x3 over 36s)  persistentvolume-controller  waiting for first consumer to be created before binding
```

The pending state is okay, because storage will only be bound when the PVC is used for the first time.

### Configuration Objects (_Secret_ and _ConfigMap_) to configure the database

We here use a [_Template_](https://docs.openshift.com/container-platform/4.16/openshift_images/using-templates.html)
to create the configuration. This allows to specify the concrete secret's values by passing command line arguments instead of storing them in the SCM.

The configuration is needed when the database is initialized, so this has to be done ~before~ the database gets installed.

We run these commands:

```bash
# test the template
oc process -f ConfigObjects.yaml
# show the parameters of the template
oc process --parameters -f ConfigObjects.yaml
# all parameters have default values (password is generated each time we apply)
# set a custom password
oc process -f ConfigObjects.yaml --param=PW=<your-password>
```

So, all together, we simply apply the configuration by the following command:

```bash
oc process -f ConfigObjects.yaml | oc apply -f -
```

## Install the Database

We need a _Deployment_ and a _Service. Installation is as simple as possible:

```bash
oc apply -f Deployment.yaml && oc apply -f Service.yaml
```

**Please note:** The service is of default type `ClusterIP`, so we'll get a static IP address that we can
find out with this command:

```bash
oc get service petclinic-db-service -o jsonpath='{.spec.clusterIP}'
```

If this is not possible (e.g. when using [Helm](../../../helm/README.md)), we need to use the service type
`NodePort`, which will bind to a specific port (in range `32000..32767`), that will be available for every
subdomain in the cluster. Using a route is not possible because we do not have HTTP traffic here.

## Access the Database

We can then connect to the database using port forwarding:

```bash
oc port-forward $(oc get pods -o name --selector='app=db') 5432
# URL: jdbc:postgresql://localhost:5432/petclinic
# get username
oc get secret db-credentials -o jsonpath='{.data}' | jq -r '.["username"]' | base64 --decode
# get password
oc get secret db-credentials -o jsonpath='{.data}' | jq -r '.["password"]' | base64 --decode

```

We can also connect to the pod using SSH:

```bash
oc rsh $(oc get pods -o name --selector='app=db')
```
