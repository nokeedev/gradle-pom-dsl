package dev.nokee.dsl.pom

import spock.lang.Ignore

class MinimalProjectObjectModelFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	def "can configure the name and version from the pom.xml"() {
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
					assert project.name == 'my-app'
					assert project.version == '1'
				}
			}
		"""

		expect:
		succeeds('verify')
	}

	@Ignore
	def "can configure the group from the pom.xml"() {
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
				}
			}
		"""

		expect:
		succeeds('verify')
	}

	def "can configure the description from the pom.xml"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
				<description>Some description</description>
			</project>
		"""
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert project.description == 'Some description'
				}
			}
		"""

		expect:
		succeeds('verify')
	}

	def "throws an exception when artifactId tag"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<version>1</version>
			</project>
		"""

		expect:
		fails('verify')
		failure.assertHasDescription("An exception occurred applying plugin request [id: 'dev.nokee.pom-dsl']")
		failure.assertHasCause("Failed to apply plugin [id 'dev.nokee.pom-dsl']")
		failure.assertHasCause("pom.xml file doesn't have an artifactId tag")
	}
}
