# Provisioning Postgres Database in OpenShift

Typically, applications need a (relational) database to store their values.
Such a database could run

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


