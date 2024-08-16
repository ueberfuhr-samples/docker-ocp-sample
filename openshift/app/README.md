# Deploying the application to OpenShift

To get the app deployed, we need the following steps:

1. Build the container images and store them into a container registry.\
   _(This is already done by GitHub actions.)_
2. Setup the database.\
   _(We can find instructions [here](../db/README.md).)_
3. Create a _ConfigMap_ and a _Pod_.
   - If we created the database _Pod_ and _Service_ manually, we can find the files and instructions [here](1-pod-service).
   - If we used the Crunchy Operator, we can find the files and instructions [here](2-crunchy-operator).
4. Create a _Service_ and a _Route_.\
   _(See the instructions below.)_

### _Service_ and _Route_

To make the application public, we need to create a _Service_ and a _Route_.

```bash
oc apply -f Service.yaml
oc process -f Route.yaml --param=HOST=<your-host-here> | oc apply -f -
```
