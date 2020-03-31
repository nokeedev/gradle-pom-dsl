package dev.nokee.dsl.pom

import dev.gradleplugins.test.fixtures.scan.GradleEnterpriseBuildScan

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

	def "sdfsd"() {
		pomFile << '''<?xml version="1.0" encoding="US-ASCII"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<packaging>pom</packaging>
	<groupId>com.example.maven.layering</groupId>
	<artifactId>parent</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>${project.artifactId}</name>
	<description>This pom acts as the parent pom for the entire project.</description>

	<properties>
		<spring-core-version>4.1.6.RELEASE</spring-core-version>
		<spring-aop-version>4.1.6.RELEASE</spring-aop-version>
	</properties>

	<modules>
		<module>data</module>
		<module>business</module>
	</modules>

	<dependencyManagement>
		<dependencies>
			<!-- A number of dependencies here are unnecessary for our example but
				leaving them here to act as an example for typical project with all layers -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-aop</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-asm</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-aspects</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-beans</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context-support</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-core</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-jdbc</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-jms</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-oxm</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-orm</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-test</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-tx</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-web</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-webmvc</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-webmvc-portlet</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework.security</groupId>
				<artifactId>spring-security-core</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>log4j</groupId>
				<artifactId>log4j</artifactId>
				<version>1.2.16</version>
			</dependency>
			<dependency>
				<groupId>junit</groupId>
				<artifactId>junit</artifactId>
				<version>4.10</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>org.mockito</groupId>
				<artifactId>mockito-core</artifactId>
				<version>1.9.0-rc1</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>org.easymock</groupId>
				<artifactId>easymock</artifactId>
				<version>3.0</version>
				<scope>test</scope>
			</dependency>

			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-model</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-data-api</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-data-impl</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-business-api</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-business-impl</artifactId>
				<version>${project.version}</version>
			</dependency>

		</dependencies>
	</dependencyManagement>

	<distributionManagement>
		<repository>
			<id>releases</id>
			<name>Corporate Releases</name>
			<url>Repository Name for releasing</url>
		</repository>
		<snapshotRepository>
			<id>snapshot</id>
			<name>Corporate Snapshots</name>
			<url>Repository Name for releasing</url>
		</snapshotRepository>
	</distributionManagement>
</project>
		'''
		file('business/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>parent</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>pom</packaging>
    <artifactId>in28minutes-multi-module-business</artifactId>
    <name>${project.artifactId}</name>
    <description>Business Layer Project.</description>

    <modules>
        <module>api</module>
    </modules>
</project>
'''
		file('business/api/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>in28minutes-multi-module-business</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>jar</packaging>
    <artifactId>in28minutes-multi-module-business-api</artifactId>
    <name>${project.artifactId}</name>
    <description>Business Layer API.</description>

    <dependencies>
		<dependency>
            <groupId>com.example.maven.layering</groupId>
            <artifactId>in28minutes-multi-module-data-api</artifactId>
		</dependency>
    </dependencies>
</project>
'''
		file('data/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>parent</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>pom</packaging>
    <artifactId>in28minutes-multi-module-data</artifactId>
    <name>${project.artifactId}</name>
    <description>Sample Implementation of the Business Layer.</description>

    <modules>
        <module>api</module>
    </modules>
</project>'''
		file('data/api/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>in28minutes-multi-module-data</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>jar</packaging>
    <artifactId>in28minutes-multi-module-data-api</artifactId>
    <name>${project.artifactId}</name>
    <description>Data Access API.</description>
</project>'''

		expect:
		succeeds(':business:api:build')
	}














	def "dfdfdfdsfdsfdsfdsfdsfdsfds"() {
		pomFile << '''<?xml version="1.0" encoding="US-ASCII"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<packaging>pom</packaging>
	<groupId>com.example.maven.layering</groupId>
	<artifactId>parent</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>${project.artifactId}</name>
	<description>This pom acts as the parent pom for the entire project.</description>

	<properties>
		<spring-core-version>4.1.6.RELEASE</spring-core-version>
		<spring-aop-version>4.1.6.RELEASE</spring-aop-version>
	</properties>

	<modules>
		<module>data</module>
		<module>business</module>
	</modules>

	<dependencyManagement>
		<dependencies>
			<!-- A number of dependencies here are unnecessary for our example but
				leaving them here to act as an example for typical project with all layers -->
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-aop</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-asm</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-aspects</artifactId>
				<version>${spring-aop-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-beans</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-context-support</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-core</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-jdbc</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-jms</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-oxm</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-orm</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-test</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-tx</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-web</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-webmvc</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring-webmvc-portlet</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>org.springframework.security</groupId>
				<artifactId>spring-security-core</artifactId>
				<version>${spring-core-version}</version>
			</dependency>
			<dependency>
				<groupId>log4j</groupId>
				<artifactId>log4j</artifactId>
				<version>1.2.16</version>
			</dependency>
			<dependency>
				<groupId>junit</groupId>
				<artifactId>junit</artifactId>
				<version>4.10</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>org.mockito</groupId>
				<artifactId>mockito-core</artifactId>
				<version>1.9.0-rc1</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>org.easymock</groupId>
				<artifactId>easymock</artifactId>
				<version>3.0</version>
				<scope>test</scope>
			</dependency>

			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-model</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-data-api</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-data-impl</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-business-api</artifactId>
				<version>${project.version}</version>
			</dependency>
			<dependency>
				<groupId>com.example.maven.layering</groupId>
				<artifactId>in28minutes-multi-module-business-impl</artifactId>
				<version>${project.version}</version>
			</dependency>

		</dependencies>
	</dependencyManagement>

	<distributionManagement>
		<repository>
			<id>releases</id>
			<name>Corporate Releases</name>
			<url>Repository Name for releasing</url>
		</repository>
		<snapshotRepository>
			<id>snapshot</id>
			<name>Corporate Snapshots</name>
			<url>Repository Name for releasing</url>
		</snapshotRepository>
	</distributionManagement>
</project>
		'''
		file('business/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>parent</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>jar</packaging>
    <artifactId>in28minutes-multi-module-business-api</artifactId>
    <name>${project.artifactId}</name>
    <description>Business Layer API.</description>

    <dependencies>
		<dependency>
            <groupId>com.example.maven.layering</groupId>
            <artifactId>in28minutes-multi-module-data-api</artifactId>
		</dependency>
    </dependencies>
</project>
'''
		file('data/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
	    <groupId>com.example.maven.layering</groupId>
    	<artifactId>parent</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

    <packaging>jar</packaging>
    <artifactId>in28minutes-multi-module-data-api</artifactId>
    <name>${project.artifactId}</name>
    <description>Sample Implementation of the Business Layer.</description>
</project>'''
//		file('data/api/pom.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
//<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
//    <modelVersion>4.0.0</modelVersion>
//
//    <parent>
//	    <groupId>com.example.maven.layering</groupId>
//    	<artifactId>in28minutes-multi-module-data</artifactId>
//    	<version>0.0.1-SNAPSHOT</version>
//    </parent>
//
//    <packaging>jar</packaging>
//    <artifactId>in28minutes-multi-module-data-api</artifactId>
//    <name>${project.artifactId}</name>
//    <description>Data Access API.</description>
//</project>'''

		expect:
//		succeeds('tasks')
		new GradleEnterpriseBuildScan().apply(executer.withDebuggerAttached().withTasks('build', '-s')).run()
	}
}
