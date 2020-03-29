package dev.nokee.dsl.pom


import dev.gradleplugins.test.fixtures.file.TestFile

class MinimalProjectObjectModelFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
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

	def "can configure the description from the pom.xml"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
				<name>Some description</name>
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
			</project>
		"""

		expect:
		fails('verify')
		failure.assertHasDescription("An exception occurred applying plugin request [id: 'dev.nokee.pom-dsl']")
		failure.assertHasCause("Failed to apply plugin [id 'dev.nokee.pom-dsl']")
		failure.assertHasCause("pom.xml file doesn't have an artifactId tag.")
	}
}
