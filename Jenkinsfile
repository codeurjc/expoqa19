logstash {
	node {
	    try {
	       stage("Preparation") { 
		    git(
		        url: 'https://github.com/codeurjc/expoqa19.git',
		        branch: "demo3"
		        )
	       }
	       stage("Create jar") {
		   sh "docker-compose build"
	       }
	       stage("Start app") {
		   sh "docker-compose up -d"
	       }
	       stage("Test") {
		   sh "mvn test -Dsel.jup.recording=true -Dsel.jup.screenshot.at.the.end.of.tests=true -Dsel.jup.screenshot.format=png -Dsel.jup.output.folder=surefire-reports"
	       } 
	    } finally {
	    step([$class: 'JUnitResultArchiver', testDataPublishers: [[$class: 'AttachmentPublisher']], testResults: '**/target/surefire-reports/TEST-*.xml'])
		sh "docker-compose down"
		junit "target/*-reports/TEST-*.xml"
	    }
	}
}
