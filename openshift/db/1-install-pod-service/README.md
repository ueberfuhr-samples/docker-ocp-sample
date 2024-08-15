# Installing the database as a _Pod_

We need a _Deployment_ and a _Service. Installation is as simple as possible:

```bash
oc apply -f Deployment.yaml && oc apply -f Service.yaml
```

We can then connect to the pod using SSH:

```bash
oc rsh $(oc get pods -o name --selector='app=db')
```
