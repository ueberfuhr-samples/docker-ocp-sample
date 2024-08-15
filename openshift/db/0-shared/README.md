# Provisioning Database Dependencies

First, we need a storage and external configuration that is used by the database container and could also be shared with others.

## _PersistentVolumeClaim_ to get persistent storage

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
