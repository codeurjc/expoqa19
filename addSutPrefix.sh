#/!bin/bash
# Add sut prefix for the current execution 
cd ${PWD}/k8s
sed -i -e "s/name: web/name: ${ET_SUT_CONTAINER_NAME}-web/g" -e 's/sut_/sut-/g' -e "s/io.elastest.service: web/io.elastest.service: ${ET_SUT_CONTAINER_NAME}-web/g" web-deployment.yaml
sed -i -e "s/name: db/name: ${ET_SUT_CONTAINER_NAME}-db/g" -e 's/sut_/sut-/g' -e "s/io.elastest.service: db/io.elastest.service: ${ET_SUT_CONTAINER_NAME}-db/g" db-deployment.yaml



