# Testing cloud and kubernetes applications

Code used in the masterclass "Testing cloud and kubernetes applications" in [ExpoQA event](http://www.expoqa.com) held in Madrid in June 18-19 2019.

[Download slides](https://github.com/codeurjc/expoqa19/blob/master/Testing%20cloud%20and%20Kubernetes%20applications.pdf)

There are 6 git branches with different ways to configure and improve the same basic web application. The application is containerized and has two services, web app container and database container. I has two E2E tests using a web browser with Selenium.

* [Demo 0](https://github.com/codeurjc/expoqa19/tree/demo0): Docker-compose managed app. Tests using browsers installed on Jenkins machine.
* [Demo 1](https://github.com/codeurjc/expoqa19/tree/demo1): Logs archived in the Jenkins build for further inspection.
* [Demo 2](https://github.com/codeurjc/expoqa19/tree/demo2): Logs archived in ElasticSearch and analyzed with Kibana
* [Demo 3](https://github.com/codeurjc/expoqa19/tree/demo3): Dockerized browsers with recording for furhter inspection using Selenium Jupiter.
* [Demo 4](https://github.com/codeurjc/expoqa19/tree/demo4): ElasTest integration
* [Demo 5](https://github.com/codeurjc/expoqa19/tree/demo5): Kubernetes managed app without ElasTest.
* [Demo 6](https://github.com/codeurjc/expoqa19/tree/demo5): Kubernetes managed app with ElasTest integration.
* [Demo 6 Fixed Test](https://github.com/codeurjc/expoqa19/tree/demo5): Kubernetes managed app with ElasTest integration and success Test.
