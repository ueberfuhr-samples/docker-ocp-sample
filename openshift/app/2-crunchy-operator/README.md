# Deploying the application (access database from Crunchy Operator)

## _ConfigMap_ to configure the database

Crunchy provides the database connection properties in a credential named
`<cluster>-pguser-<cluster>` (here: `petclinic-db-pguser-petclinic-db`).
We can use this credential directly, so we only have to provide the application-specific
driver properties to the application:

```bash
oc apply -f ConfigObjects.yaml
```

## _Pod_ running the application container

As simple as possible:

```bash
oc apply -f Deployment.yaml
```

**Note:** If accessing the database fails, we could look into the logs where all application properties and environment variables are printed out on startup.
