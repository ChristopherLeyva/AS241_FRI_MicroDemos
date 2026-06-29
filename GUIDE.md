# Entrega Final - Microservicios con Kubernetes

## Estructura del Proyecto

```
ms-pedidos/                         ms-productos/
├── Dockerfile                      ├── Dockerfile
├── k8s/                            ├── pom.xml
│   ├── namespace.yaml              └── src/
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── ms-productos.yaml
│   └── ms-pedidos.yaml
├── pom.xml
└── src/
```

---

## 1. Prerrequisitos

```bash
# Verificar herramientas
java -version                          # 17
docker --version
kubectl version --client
mvn --version                          # o ./mvnw --version
```

---

## 2. Construir JARs

```bash
# MicroProductos (puerto 8081)
cd /Users/Christopher/AS241_FRI_MicroProductos
./mvnw clean package -DskipTests

# MicroPedidos (puerto 8082)
cd /Users/Christopher/AS241_FRI_MicroPedidos
./mvnw clean package -DskipTests
```

---

## 3. Construir Imágenes Docker

```bash
# Construir
docker build -t ms-productos:latest /Users/Christopher/AS241_FRI_MicroProductos
docker build -t ms-pedidos:latest /Users/Christopher/AS241_FRI_MicroPedidos

# Verificar
docker images | grep ms-
```

### Opcional: Subir a Docker Hub

```bash
docker tag ms-pedidos:latest tu-usuario/ms-pedidos:latest
docker tag ms-productos:latest tu-usuario/ms-productos:latest
docker push tu-usuario/ms-pedidos:latest
docker push tu-usuario/ms-productos:latest
```

---

## 4. Prueba Local (sin Docker)

### Terminal 1 - MicroProductos
```bash
cd /Users/Christopher/AS241_FRI_MicroProductos
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Terminal 2 - MicroPedidos
```bash
cd /Users/Christopher/AS241_FRI_MicroPedidos
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

### Probar comunicación (Terminal 3)

```bash
# 1. Crear un producto
curl -s -X POST http://localhost:8081/api/productos \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Gamer","price":2500.00,"stock":10,"active":true}' | json_pp

# 2. Listar productos
curl -s http://localhost:8081/api/productos | json_pp

# 3. Crear un pedido (consume a productos vía WebClient)
curl -s -X POST http://localhost:8082/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}' | json_pp

# 4. Listar pedidos
curl -s http://localhost:8082/api/pedidos | json_pp
```

**Captura para el informe:** mostrar la terminal con ambos servicios corriendo y las respuestas JSON.

---

## 5. Despliegue en Kubernetes

### 5.1 Aplicar recursos

```bash
# Namespace
kubectl apply -f k8s/namespace.yaml

# ConfigMap y Secret
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/configmap.yaml

# Desplegar microservicios
kubectl apply -f k8s/ms-productos.yaml
kubectl apply -f k8s/ms-pedidos.yaml
```

### 5.2 Verificar despliegue

```bash
# Pods
kubectl get pods -n ms-pedidos-ns

# Services
kubectl get svc -n ms-pedidos-ns

# Logs
kubectl logs -n ms-pedidos-ns deployment/ms-productos
kubectl logs -n ms-pedidos-ns deployment/ms-pedidos
```

### 5.3 Probar comunicación interna

```bash
# Pod temporal para pruebas
kubectl run curl-test --image=curlimages/curl:latest -n ms-pedidos-ns --rm -it --restart=Never -- sh

# Dentro del pod:
# Probar productos
curl -s -X POST http://ms-productos:8081/api/productos \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Gamer","price":2500.00,"stock":10,"active":true}'

curl -s http://ms-productos:8081/api/productos

# Probar pedidos (consume a productos internamente)
curl -s -X POST http://ms-pedidos:8082/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}'

curl -s http://ms-pedidos:8082/api/pedidos

exit
```

### 5.4 Acceso externo (LoadBalancer)

```bash
# Obtener IP externa
kubectl get svc ms-pedidos-lb -n ms-pedidos-ns

# Probar desde fuera
curl http://localhost:8082/api/pedidos

# Si usas minikube:
minikube service ms-pedidos-lb -n ms-pedidos-ns
```

### 5.5 Probar puertos diferentes

```bash
# Ver puertos configurados
kubectl get svc -n ms-pedidos-ns

# ms-productos: 8081 (ClusterIP)
# ms-pedidos:   8082 (ClusterIP + LoadBalancer)
```

### 5.6 Limpiar

```bash
kubectl delete namespace ms-pedidos-ns
```

---

## 6. Para el Informe

### Apartados requeridos:

| Sección | Contenido |
|---------|-----------|
| **Conceptos** | Hexagonal Architecture, R2DBC, WebClient, Kubernetes, Docker, LoadBalancer |
| **Test local** | Capturas de terminal con ambos servicios y curl probando comunicación |
| **Test K8s** | Capturas de `kubectl get pods`, `kubectl get svc`, curl dentro del cluster |
| **Manifiestos** | namespace.yaml, configmap.yaml, secret.yaml, ms-productos.yaml, ms-pedidos.yaml |
| **Comandos** | Todos los comandos de las secciones 2-5 |
| **Conclusiones** | Ventajas de Kubernetes, escalabilidad, comunicación entre microservicios |

### Para el Video (7-10 min)

1. **0:00-1:00** — Explicar estructura del proyecto (hexagonal)
2. **1:00-2:30** — Build de JARs + Docker
3. **2:30-4:00** — Prueba local con curl (mostrar POST y GET)
4. **4:00-6:30** — Despliegue en Kubernetes (pods, services, curl interno)
5. **6:30-7:30** — LoadBalancer y prueba externa
6. **7:30-8:30** — Demostrar puertos diferentes (8081 vs 8082)
7. **8:30-10:00** — Conclusiones

---

## 7. Docker Hub (opcional)

Si no quieres exportar imágenes, publícalas:

```bash
docker login
docker tag ms-pedidos:latest tu-usuario/ms-pedidos:latest
docker tag ms-productos:latest tu-usuario/ms-productos:latest
docker push tu-usuario/ms-pedidos:latest
docker push tu-usuario/ms-productos:latest
```

Luego actualiza `image:` en los manifests YAML con `tu-usuario/ms-pedidos:latest` y `imagePullPolicy: Always`.
