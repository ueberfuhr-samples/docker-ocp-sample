# Deploying the application (access database in pod)

## _ConfigMap_ to configure the database

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

**Note:** It seems that OpenShift adds some container environment variables for the service automatically, but we do not use
them in our `application-cloud.properties` file directly, because this makes the application code dependent from the OpenShift resources.
The variables for the service named `petclinic-db-service` are:

```bash
PETCLINIC_DB_SERVICE_PORT=tcp://172.30.11.36:5432
PETCLINIC_DB_SERVICE_PORT_5432_TCP=tcp://172.30.11.36:5432
PETCLINIC_DB_SERVICE_PORT_5432_TCP_ADDR=172.30.11.36
PETCLINIC_DB_SERVICE_PORT_5432_TCP_PORT=5432
PETCLINIC_DB_SERVICE_PORT_5432_TCP_PROTO=tcp
PETCLINIC_DB_SERVICE_SERVICE_HOST=172.30.11.36
PETCLINIC_DB_SERVICE_SERVICE_PORT=5432
```

## _Pod_ running the application container

As simple as possible:

```bash
oc apply -f Deployment.yaml
```

**Note:** If accessing the database fails, we could look into the logs where all application properties and environment variables are printed out on startup.
