#/!bin/bash
# Add sut prefix for the current execution 
cd /var/lib/jenkins/workspace/expoQA19Demo/dev/k8s; sed -i -e 's/name: web/name: ${env.ET_SUT_CONTAINER_NAME}-web/g' -e 's/sut_/sut-/g' web-deployment.yaml
cd /var/lib/jenkins/workspace/expoQA19Demo/dev/k8s; sed -i -e 's/name: db/name: ${env.ET_SUT_CONTAINER_NAME}-db/g' -e 's/sut_/sut-/g' db-deployment.yaml