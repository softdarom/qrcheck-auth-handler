export DOCKER_VERSION=minikube
export PROJECT_NAME=qrcheck
export APPLICATION_NAME=auth-handler

start:
	@echo "Gradle Build"
	@./gradlew build

	@echo "Switch context to Minikube"
	@kubectl config use-context minikube

	@echo "Docker Build"
	@eval $$(minikube docker-env) && docker build -t ${PROJECT_NAME}/${APPLICATION_NAME}:${DOCKER_VERSION} .

	@echo "Generating configs and Starting Service"
	@ansible-playbook -i .deploy/ansible/inventories/local --vault-password-file=.deploy/ansible/ansible_vault_local.txt -e env=local .deploy/ansible/main.yaml
stop:
	@echo "Removing and Stopping Service"
	@kubectl delete all -l app=${APPLICATION_NAME}
	@kubectl delete cm -l app=${APPLICATION_NAME}
	@kubectl delete secret -l app=${APPLICATION_NAME}
	@eval $$(minikube docker-env) && docker rmi ${PROJECT_NAME}/${APPLICATION_NAME}:${DOCKER_VERSION} -f