#/!bin/bash
# Add sut prefix for the current execution 
echo ${PWD}
cd ${PWD}/k8s; sed -i -e "s/name: web/name: ${ET_SUT_CONTAINER_NAME}-web/g" -e 's/sut_/sut-/g' web-deployment.yaml
cd ${PWD}/k8s; sed -i -e "s/name: db/name: ${ET_SUT_CONTAINER_NAME}-db/g" -e 's/sut_/sut-/g' db-deployment.yaml