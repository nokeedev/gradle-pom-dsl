package dev.nokee.dsl.pom

class ProjectObjectModelPropertiesFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	def "can configure the group, name and version from the pom.xml"() {
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
}
