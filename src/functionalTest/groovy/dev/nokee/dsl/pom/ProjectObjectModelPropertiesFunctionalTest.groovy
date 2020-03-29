package dev.nokee.dsl.pom

class ProjectObjectModelPropertiesFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	def "maps pom.xml properties to extra properties"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>

				<properties>
					<myKey>myValue</myKey>
					<my.key>my.value</my.key>
				</properties>
			</project>
		"""
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert project.property('myKey') == 'myValue'
					assert project.property('my.key') == 'my.value'
				}
			}
		"""

		expect:
		succeeds('verify')
	}

	def "maps groupId and artifactId of pom.xml to extra properties"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
			</project>
		"""
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert project.property('groupId') == 'com.mycompany.app'
					assert project.property('artifactId') == 'my-app'
				}
			}
		"""

		expect:
		succeeds('verify')
	}

	def "interpolate artifactId"() {
		pomFile << """<?xml version="1.0" encoding="US-ASCII"?>
			<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
				<modelVersion>4.0.0</modelVersion>
				<packaging>pom</packaging>
				<groupId>com.in28minutes.example.layering</groupId>
				<artifactId>parent</artifactId>
				<version>0.0.1-SNAPSHOT</version>
				<description>This pom acts as the parent pom for the entire project (name: \${project.artifactId}).</description>
			</project>
		"""
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert project.description == 'This pom acts as the parent pom for the entire project (name: parent).'
				}
			}
		"""

		expect:
		succeeds('verify', '-s')
	}

	def "interpolate properties"() {
		pomFile << """<?xml version="1.0" encoding="US-ASCII"?>
			<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
				<modelVersion>4.0.0</modelVersion>
				<packaging>pom</packaging>
				<groupId>com.in28minutes.example.layering</groupId>
				<artifactId>parent</artifactId>
				<version>0.0.1-SNAPSHOT</version>
				<description>Value is \${spring-core-version}</description>

				<properties>
					<spring-core-version>4.1.6.RELEASE</spring-core-version>
					<spring-aop-version>4.1.6.RELEASE</spring-aop-version>
				</properties>
			</project>
		"""
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert project.description == 'Value is 4.1.6.RELEASE'
				}
			}
		"""

		expect:
		succeeds('verify', '-s')
	}

}
