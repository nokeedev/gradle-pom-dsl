package dev.nokee.dsl.pom

import dev.gradleplugins.integtests.fixtures.AbstractFunctionalSpec
import dev.gradleplugins.test.fixtures.file.TestFile

class MinimalProjectObjectModelFunctionalTest extends AbstractFunctionalSpec {
	TestFile getPomFile() {
		return file('pom.xml')
	}

	def setup() {
		settingsFile << """
			plugins {
				id 'dev.nokee.pom-dsl'
			}
		"""
	}

	def "can configure the group, name and version from the pom.xml"() {
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
					assert project.group == 'com.mycompany.app'
					assert project.name == 'my-app'
					assert project.version == '1'
				}
			}
		"""

		expect:
		succeeds('verify')
	}
}
