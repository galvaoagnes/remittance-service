# Remittance service

The primary function of the remittance service is to execute cross-currency financial remittances and orchestrate the entities involved in the process. It enables the creation of multi-currency accounts and the execution of transfers converted based on exchange rates.

## Technologies

* Java 21+
* Spring Boot
* PostgreSQL
* Flyway
* Docker
* Kubernetes (Rancher Desktop / k3s)
* Maven

---

## API Documentation

* [Local](http://localhost:8080/swagger-ui/index.html)

---

## Run tests

```bash
mvn test
```
with verification

```bash
mvn test verify
```
---

# Running the Project Locally

## Run without Kubernetes


## Prerequisites

* Java 21+
* Maven
* Docker
---

```bash
cd docker && docker compose up -d && cd .. && mvn spring-boot:run -Dspring-boot.run.profiles=api
```
---


## Run with kubernetes 

Make sure you have installed:

* Java 21+
* Maven
* Docker
* Rancher Desktop (or another local Kubernetes environment)
* kubectl

# 1. Clone the Repository

```bash
git clone <repository-url>

cd remittance-service
```

---

# 2. Start Kubernetes Locally

Start Rancher Desktop and wait until Kubernetes is available.

Check the cluster status:

```bash
kubectl get nodes
```

Expected result:

```text
NAME                   STATUS
lima-rancher-desktop   Ready
```

---

# 3. Build the Docker Image

From the project root directory:

```bash
docker build -t remittance-api:latest .
```

Verify the image:

```bash
docker images | grep remittance-api
```

Expected:

```text
remittance-api   latest
```

---

# 4. Kubernetes Configuration

The Kubernetes manifests are located in:

```text
K8s/
```
structure:

```text
K8s/
├── api-deployment.yml
├── api-service.yml
├── postgres.yml
├── cron-job.yml
└── postgres-service.yml
```

---

# 5. Deploy Application on Kubernetes

Apply all Kubernetes resources:

```bash
kubectl apply -f K8s/
```

Check running pods:

```bash
kubectl get pods
```

Expected result:

```text
NAME                         READY   STATUS
postgres-xxxxx               1/1     Running
remittance-api-xxxxx         1/1     Running
```

---

# 6. Check Application Logs

List pods:

```bash
kubectl get pods
```

View application logs:

```bash
kubectl logs -f <pod-name>
```

Example:

```bash
kubectl logs -f remittance-api-xxxxx
```

---

# 7. Access the API Locally

Create a port forward:

```bash
kubectl port-forward service/remittance-api 8080:80
```

The API will be available at:

```
http://localhost:8080
```

---

# 8. Scheduler Execution

The scheduler runs as a Kubernetes CronJob using the `scheduler` Spring profile.

Check CronJobs:

```bash
kubectl get cronjobs
```

Check executed jobs:

```bash
kubectl get jobs
```

Run the scheduler manually:

```bash
kubectl create job \
--from=cronjob/remittance-scheduler \
scheduler-manual
```

View scheduler logs:

```bash
kubectl logs job/scheduler-manual
```

---

# 9. Remove Kubernetes Resources

To remove all deployed resources:

```bash
kubectl delete -f K8s/
```

---

# Troubleshooting

## ErrImageNeverPull

This means Kubernetes cannot find the local Docker image.

Check the image:

```bash
docker images | grep remittance-api
```

The expected image name:

```text
remittance-api:latest
```

The Kubernetes Deployment should contain:

```yaml
image: remittance-api:latest
imagePullPolicy: Never
```

---

## Database Connection Refused

Inside Kubernetes, do not use:

```properties
jdbc:postgresql://localhost:5432/remittance
```

`localhost` points to the application container itself.

Use the PostgreSQL Kubernetes Service name:

```properties
jdbc:postgresql://postgres:5432/remittance
```

Example environment configuration:

```yaml
env:
  - name: SPRING_DATASOURCE_URL
    value: jdbc:postgresql://postgres:5432/remittance

  - name: SPRING_DATASOURCE_USERNAME
    value: user

  - name: SPRING_DATASOURCE_PASSWORD
    value: password
```

---

# Restart Application

Restart the API deployment:

```bash
kubectl rollout restart deployment remittance-api
```

Watch pod status:

```bash
kubectl get pods -w
```

---


# Project Architecture

The application is deployed using separated workloads:

* **API Deployment**

    * Handles HTTP requests
    * Runs with the `api` Spring profile

* **Scheduler CronJob**

    * Executes background tasks
    * Runs with the `scheduler` Spring profile

* **PostgreSQL Deployment**

    * Persistent application database

* **Flyway**

* Executes database migrations automatically during application startup

