# Provisioning Postgres Database in OpenShift

Typically, applications need a (relational) database to store their values.

## Architectural Hints

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

## Install the Database

- To install the database container as a _Pod_, we follow [these instructions](1-pod-service/README.md).
- To use the [Crunchy Data OpenShift Operator](https://github.com/CrunchyData/postgres-operator), we follow [these instructions](2-crunchy-operator/README.md).

**Note:** When running for the first time, this might take a little longer because of the _PersistentVolume_
being provisioned lazily.
