package dev.nokee.dsl.pom

import dev.gradleplugins.test.fixtures.sources.SourceElement
import dev.nokee.dsl.pom.fixtures.JavaHelloWorldApp

class MultiModuleJarPackagingProjectFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	protected void makeSingleProject() {
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>app</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    <dependencies>
    	<dependency>
    		<groupId>com.example</groupId>
    		<artifactId>library</artifactId>
    		<version>0.1.0</version>
    	</dependency>
    </dependencies>

    <modules>
    	<module>library</module>
    </modules>
</project>
		"""

		file('library/pom.xml') << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>library</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>
</project>
		"""
	}

	SourceElement getComponentUnderTest() {
		return new JavaHelloWorldApp().withLibraryAsModule('library')
	}

	def "can declare multiple module"() {
		makeSingleProject()

		expect:
		succeeds('projects')
		outputContains("""
> Task :projects

------------------------------------------------------------
Root project
------------------------------------------------------------

Root project 'app'
\\--- Project ':library'
""")
	}

	def "can build multiple module"() {
		makeSingleProject()
		componentUnderTest.writeToProject(testDirectory)

		expect:
		succeeds('assemble')
	}

	def "sdfdsgfdg"() {
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>app</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    <dependencies>
    	<dependency>
    		<groupId>com.example</groupId>
    		<artifactId>library</artifactId>
    		<version>0.1.0</version>
    	</dependency>
    </dependencies>

    <dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>junit</groupId>
				<artifactId>junit</artifactId>
				<version>4.12</version>
				<scope>test</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

    <modules>
    	<module>library</module>
    </modules>
</project>
		"""

			file('library/pom.xml') << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

	<parent>
    	<groupId>com.example</groupId>
    	<artifactId>app</artifactId>
    	<version>0.1.0</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>library</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
		</dependency>
	</dependencies>
</project>
		"""
		componentUnderTest.writeToProject(testDirectory)

		expect:
		succeeds('assemble')
	}

	def "dfsd"() {
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>parent</artifactId>
    <packaging>pom</packaging>
    <version>0.1.0</version>

    <modules>
    	<module>library</module>
    </modules>
</project>
		"""

		file('library/pom.xml') << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
    	<groupId>com.example</groupId>
    	<artifactId>parent</artifactId>
    	<version>0.1.0</version>
    </parent>

    <artifactId>library</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>
</project>
		"""
		file('library/build.gradle') << '''
			tasks.register('verify') {
				doLast {
					assert project.groupId == 'com.example'
				}
			}
		'''

		expect:
		succeeds('verify')
	}
}
