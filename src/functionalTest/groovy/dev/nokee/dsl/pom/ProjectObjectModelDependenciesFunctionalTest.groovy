package dev.nokee.dsl.pom

class ProjectObjectModelDependenciesFunctionalTest extends AbstractProjectObjectModelFunctionalSpec{
	def "can use exclusion inside dependency declaration"() {
		pomFile << """
			<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.in28minutes.maven</groupId>
				<artifactId>intermediate-maven-example</artifactId>
				<packaging>jar</packaging>
				<version>1.0-SNAPSHOT</version>
				<name>maven-example-2</name>
				<url>http://maven.apache.org</url>

				<dependencies>
					<dependency>
						<groupId>junit</groupId>
						<artifactId>junit</artifactId>
						<version>[4.1,4.20]</version>
						<scope>test</scope>
					</dependency>

					<dependency>
						<groupId>org.hibernate</groupId>
						<artifactId>hibernate</artifactId>
						<version>3.2.5.ga</version>
						<exclusions>
							<exclusion>
								<groupId>javax.transaction</groupId>
								<artifactId>jta</artifactId>
							</exclusion>
						</exclusions>
					</dependency>
				</dependencies>
			</project>
		"""

		expect:
		succeeds('help')
	}
}
