package dev.nokee.dsl.pom

import dev.gradleplugins.test.fixtures.archive.JarTestFixture
import dev.nokee.dsl.pom.fixtures.JavaHelloWorldApp

class SimpleJarPackagingProjectFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	protected void makeSingleProject() {
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-maven</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>
</project>
		"""
	}

	protected void makeSingleProjectWithDependency() {
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-maven</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    <dependencies>
		<dependency>
			<groupId>joda-time</groupId>
			<artifactId>joda-time</artifactId>
			<version>2.9.2</version>
		</dependency>
	</dependencies>
</project>
		"""
	}

	JavaHelloWorldApp getComponentUnderTest() {
		return new JavaHelloWorldApp()
	}

	def "can assemble jar packaging project using pom.xml DSL"() {
		makeSingleProject()
		componentUnderTest.writeToProject(testDirectory)

		when:
		file('build/libs/gs-maven.jar').assertDoesNotExist()
		succeeds('assemble')
		then:
		new JarTestFixture(file('build/libs/gs-maven-0.1.0.jar')).hasDescendants('hello/HelloWorld.class', 'hello/Greeter.class')
	}

	def "can assemble jar packaging project using pom.xml DSL with external dependency"() {
		makeSingleProjectWithDependency()
		componentUnderTest.withExternalDependency().writeToProject(testDirectory)

		when:
		file('build/libs/gs-maven.jar').assertDoesNotExist()
		succeeds('assemble')
		then:
		new JarTestFixture(file('build/libs/gs-maven-0.1.0.jar')).hasDescendants('hello/HelloWorld.class', 'hello/Greeter.class')
	}
}
