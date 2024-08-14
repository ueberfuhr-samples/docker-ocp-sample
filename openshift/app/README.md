# Deploying the application to OpenShift

To get the app deployed, we need the following steps:

1. Build the container images and store them into a container registry.\
   _(This is already done by GitHub actions.)_
2. Setup the database.\
   _(We can find instructions [here](../db/README.md).)_
3. Create a _Pod_, a _Service_ and a _Route_.\
   _(See the instructions below.)_

### Configuration Objects (_Secret_ and _ConfigMap) to configure the database

We here use a [_Template_](https://docs.openshift.com/container-platform/4.16/openshift_images/using-templates.html)
to create the secret. This allows to specify the concrete secret's values by passing command line arguments instead of storing them in the SCM.

So we need these commands:

```bash
# test the template
oc process -f ConfigObjects.yaml
# show the parameters of the template
oc process --parameters -f ConfigObjects.yaml
# all parameters have default values, except the database service IP address
oc process -f ConfigObjects.yaml --param=DB_SERVICE_IP=192.168.0.0
```

The database service IP address can be found using this command:

```bash
oc get service petclinic-db-service -o jsonpath='{.spec.clusterIP}'
```

So, all together, we simply apply the configuration by the following command:

```bash
oc process -f ConfigObjects.yaml --param=DB_SERVICE_IP=$(oc get service petclinic-db-service -o jsonpath='{.spec.clusterIP}') | oc apply -f -
```

## Create a _Pod_ (_Deployment_)



- **in the same _Pod_ as the application (same container or another one):**
  This is not recommended because application and database then share the same lifecycle. Each instance of the application then has its own database.
  This scenario is the default, when we deploy Spring Boot apps with default properties to a container.
- **in another _Pod_ in the same project:**
  - **create a deployment manually:** 
    This is low-level, i.e. it needs a lot of configuration.
  - **using an OpenShift _Operator_ (e.g. [Crunchy Data](https://github.com/CrunchyData/postgres-operator)):**
    This is hgh-level, i.e. it provides a default solution for a default problem
- **run the database externally (not managed by OpenShift):**
  This might be the way to go on the road to production, because production databases should not be replaced automatically (_pets-vs-cattle_).
  But for testing purposes, it is not always possible to get an external database provided.

When the database is managed by OpenShift, we need a [_Persistent Volume_ (PV)](https://kubernetes.io/docs/concepts/storage/persistent-volumes/),
which we can get by creating a [_PersistentVolumeClaim_ (PVC)](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#persistentvolumeclaims). 

For details, we could also read [this article](https://zesty.co/blog/deploy-databases-kubernetes/).

## Manual Deployment

So here we go, we now deploy the given configs in the given order.

### _PersistentVolumeClaim_ to get persistent storage

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

### _Secret_ to configure the database

We here use a [_Template_](https://docs.openshift.com/container-platform/4.16/openshift_images/using-templates.html)
to create the secret. This allows to specify the concrete secret's values by passing command line arguments instead of storing them in the SCM.

So we need these commands:

```bash
# test the template
oc process -f ConfigObjects.yaml
# show the parameters of the template
oc process --parameters -f ConfigObjects.yaml
# all parameters have default values, so only override those values if needed
oc process -f ConfigObjects.yaml --param=PW=test
```

So, all together, we simply apply the secret by the following command:

```bash
oc process -f ConfigObjects.yaml | oc apply -f -
```

### _Pod_ running the database container

As simple as possible:

```bash
oc apply -f Deployment.yaml
```

**Note:** When running for the first time, this might take a little longer because of the _PersistentVolume_
being provisioned lazily.

We can connect to the pod with the following commands:

```bash
# Connect to shell (SSH)
oc rsh $(oc get pods -o name --selector='app=db')
# Port forwarding to connection to the database
# URL: jdbc:postgresql://localhost:5432/petclinic
oc port-forward $(oc get pods -o name --selector='app=db') 5432
```



### _Service_ and _Route_

To access the database using database tools, we simply create a _Service_ and a _Route_:

```bash
oc apply -f Service.yaml
oc process -f Route.yaml --param=HOST=<your-host-here> | oc apply -f -
```
