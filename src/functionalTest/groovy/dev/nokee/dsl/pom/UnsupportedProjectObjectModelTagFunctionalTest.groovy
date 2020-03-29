package dev.nokee.dsl.pom

import spock.lang.Unroll

class UnsupportedProjectObjectModelTagFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	protected static List<String> getUnsupportedTags() {
		return [
			// The basic
			'parent',
			'dependencyManagement',
			'modules',
			'properties',

			// Build Settings
			'build',
			'reporting',

			// More Project Information
			'description',
			'url',
			'inceptionYear',
			'licenses',
			'organization',
			'developers',
			'contributors',

			// Environment Settings
			'issueManagement',
			'ciManagement',
			'mailingLists',
			'scm',
			'prerequisites',
			'repositories',
			'pluginRepositories',
			'distributionManagement',
			'profiles'
		]
	}

	@Unroll
	def "shows warning for unsupported tag [#unsupportedTag]"(unsupportedTag) {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<artifactId>my-app</artifactId>
				<${unsupportedTag}>...</${unsupportedTag}>
			</project>
		"""

		expect:
		succeeds('help')
		outputContains("Project ':' use an unsupported tag (i.e. ${unsupportedTag}), future version may support it.")

		where:
		unsupportedTag << unsupportedTags
	}

	def "does not show warning when unsupported tags aren't present"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
			</project>
		"""

		expect:
		succeeds('help')
		!result.output.contains("Project ':' use an unsupported tag")
	}

	// TODO: Show warning when parent pom
}
