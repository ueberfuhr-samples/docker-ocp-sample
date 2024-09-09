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
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "petclinic"
      }
    }
    template {
      metadata {
        labels = {
          app = "petclinic"
        }
      }
      spec {
        container {
          name              = "petclinic"
          image             = "ralfueberfuhr/spring-petclinic-rest:latest"
          image_pull_policy = "Always"
          env {
            name  = "CONTEXT_ROOT"
            value = "/"
          }
          port {
            container_port = 8080
          }
        }
      }
    }
  }
}


resource "kubernetes_service" "petclinic_service" {
  metadata {
    labels = {
      app = "petclinic"
    }
    name      = "petclinic-service"
    namespace = "petclinic"
  }
  spec {
    type = "ClusterIP"
    port {
      port        = 8888
      protocol    = "TCP"
      target_port = 8080
    }
    selector = {
      app = "petclinic"
    }
  }
}


resource "kubernetes_ingress_v1" "petclinic_ingress" {
  metadata {
    name      = "petclinic-ingress"
    namespace = "petclinic"
  }
  spec {
    rule {
      host = "petclinic.devnation"
      http {
        path {
          path      = "/api"
          path_type = "Prefix"
          backend {
            service {
              name = "petclinic-service"
              port {
                number = 8888
              }
            }
          }
        }
      }
    }
  }
}

