resource "kubernetes_namespace" "petclinic-namespace" {
  metadata {
    name = "petclinic"
  }
}

resource "kubernetes_deployment" "deployment_petclinic" {
  metadata {
    labels = {
      app = "petclinic"
    }
    name      = "petclinic-deployment"
    namespace = "petclinic"
  }
  spec = {
    replicas = 1
    selector = {
      matchLabels = {
        app = "petclinic"
      }
    }
    template = {
      metadata = {
        labels = {
          app = "petclinic"
        }
      }
      spec = {
        containers = [
          {
            env = [
              {
                name  = "CONTEXT_ROOT"
                value = "/"
              },
            ]
            image           = "ralfueberfuhr/spring-petclinic-rest:latest"
            imagePullPolicy = "Always"
            name            = "petclinic"
            ports = [
              {
                containerPort = 8080
              },
            ]
          },
        ]
      }
    }
  }
}


resource "kubernetes_service" "petclinic_service" {
  metadata = {
    labels = {
      app = "petclinic"
    }
    name      = "petclinic-service"
    namespace = "petclinic"
  }
  spec = {
    type = "LoadBalancer"
    ports = [
      {
        port       = 8888
        protocol   = "TCP"
        targetPort = 8080
      },
    ]
    selector = {
      app = "petclinic"
    }
  }
}


resource "kubernetes_ingress" "petclinic_ingress" {
  metadata = {
    name      = "petclinic-ingress"
    namespace = "petclinic"
  }
  spec = {
    rules = [
      {
        host = "petclinic.devnation"
        http = {
          paths = [
            {
              backend = {
                service = {
                  name = "petclinic-service"
                  port = {
                    "number" = 8888
                  }
                }
              }
              path     = "/api"
              pathType = "Prefix"
            },
          ]
        }
      },
    ]
  }
}

