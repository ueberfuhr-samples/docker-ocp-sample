# Provisioning Postgres Database in OpenShift

Typically, applications need a (relational) database to store their values.

## Provisioning Hints

### Architecture

To provide a database, we could install the database

- **in the same _Pod_ as the application (same container or another one):**\
  This is not recommended because application and database then share the same lifecycle. Each instance of the application then has its own database.
  This scenario is the default, when we deploy Spring Boot apps with default properties to a container.
- **in another _Pod_ in the same project:**
  - **create a deployment manually:**\
    This is low-level, i.e. it needs a lot of configuration.
  - **using an OpenShift _Operator_ (e.g. [Crunchy Data](https://github.com/CrunchyData/postgres-operator)):**\
    This is hgh-level, i.e. it provides a default solution for a default problem
- **externally (not managed by OpenShift):**\
  This might be the way to go on the road to production, because production databases should not be replaced automatically (_pets-vs-cattle_).
  But for testing purposes, it is not always possible to get an external database provided.

We could also install the database as a Stateful Set. We could find details about that in [this article](https://zesty.co/blog/deploy-databases-kubernetes/).

### Dependencies

When the database is managed by OpenShift, we need a [_Persistent Volume_ (PV)](https://kubernetes.io/docs/concepts/storage/persistent-volumes/),
which we can get by creating a [_PersistentVolumeClaim_ (PVC)](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#persistentvolumeclaims). 

And we typically need a _Secret_ to store credentials and a _ConfigMap_ to store connection properties.

## Provisioning instructions

### Install Dependencies

We need to follow [these instructions](0-shared/README.md).

### Install Database

- To install the database container as a _Pod_, we follow [these instructions](1-install-pod-service/README.md).
- To use the [Crunchy Data OpenShift Operator](https://github.com/CrunchyData/postgres-operator), we follow [these instructions](2-install-operator/README.md).

**Note:** When running for the first time, this might take a little longer because of the _PersistentVolume_
being provisioned lazily.

## Access the database

We can then connect to the database using port forwarding:

```bash
oc port-forward $(oc get pods -o name --selector='app=db') 5432
# URL: jdbc:postgresql://localhost:5432/petclinic
# Credentials: see secret "db-credentials"
```
