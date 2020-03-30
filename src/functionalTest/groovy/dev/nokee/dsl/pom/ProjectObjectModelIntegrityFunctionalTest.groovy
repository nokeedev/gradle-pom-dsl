package dev.nokee.dsl.pom

class ProjectObjectModelIntegrityFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	def "asserts the pom.xml modelVersion is 4.0.0"() {
		pomFile << """
			<project>
				<modelVersion>3.0.0</modelVersion>
				<artifactId>my-app</artifactId>
			</project>
		"""

		expect:
		fails('help')
		failure.assertHasDescription("An exception occurred applying plugin request [id: 'dev.nokee.pom-dsl']")
		failure.assertHasCause("Failed to apply plugin [id 'dev.nokee.pom-dsl']")
		failure.assertHasCause("pom.xml file contains an unsupported version for 'modelVersion' (i.e. 3.0.0)")
	}

	def "asserts the pom.xml has modelVersion tag"() {
		pomFile << """
			<project>
				<artifactId>my-app</artifactId>
			</project>
		"""

		expect:
		fails('help')
		failure.assertHasDescription("An exception occurred applying plugin request [id: 'dev.nokee.pom-dsl']")
		failure.assertHasCause("Failed to apply plugin [id 'dev.nokee.pom-dsl']")
		failure.assertHasCause("pom.xml file doesn't have a 'modelVersion' tag")
	}
}
