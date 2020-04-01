package dev.nokee.dsl.pom

import spock.lang.Unroll

class ProjectObjectModelPackagingFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	protected void makeSingleProject(String packaging) {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
				<packaging>${packaging}</packaging>
			</project>
		"""
	}

	def "applies the core war plugin for packaging war"() {
		makeSingleProject('war')
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert pluginManager.hasPlugin('war')
					assert !pluginManager.hasPlugin('ear')
				}
			}
		"""

		expect:
		succeeds('verify')
		!result.output.contains("Project ':' use an unsupported packaging")
	}

	def "applies the core java plugin for packaging jar"() {
		makeSingleProject('jar')
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert pluginManager.hasPlugin('java-library')
					assert !pluginManager.hasPlugin('war')
					assert !pluginManager.hasPlugin('ear')
				}
			}
		"""

		expect:
		succeeds('verify')
		!result.output.contains("Project ':' use an unsupported packaging")
	}

	def "applies the core ear plugin for packaging ear"() {
		makeSingleProject('ear')
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert pluginManager.hasPlugin('ear')
					assert !pluginManager.hasPlugin('war')
				}
			}
		"""

		expect:
		succeeds('verify')
		!result.output.contains("Project ':' use an unsupported packaging")
	}

	def "applies the core base plugin for packaging pom"() {
		makeSingleProject('pom')
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert pluginManager.hasPlugin('base')
					assert !pluginManager.hasPlugin('ear')
					assert !pluginManager.hasPlugin('java')
					assert !pluginManager.hasPlugin('war')
				}
			}
		"""

		expect:
		succeeds('verify')
		!result.output.contains("Project ':' use an unsupported packaging")
	}

	def "applies the core java plugin when no packaging"() {
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
					assert pluginManager.hasPlugin('java-library')
					assert !pluginManager.hasPlugin('war')
					assert !pluginManager.hasPlugin('ear')
				}
			}
		"""

		expect:
		succeeds('verify')
		!result.output.contains("Project ':' use an unsupported packaging")
	}

	@Unroll
	def "shows warning for unsupported packaging [#unsupportedPackaging]"(unsupportedPackaging) {
		makeSingleProject(unsupportedPackaging)
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert !pluginManager.hasPlugin('java')
					assert !pluginManager.hasPlugin('war')
				}
			}
		"""

		expect:
		succeeds('verify')
		result.assertOutputContains("Project ':' use an unsupported packaging (i.e. ${unsupportedPackaging}), future version may support it.")

		where:
		unsupportedPackaging << ['maven-plugin', 'ejb', 'rar']
	}

	@Unroll
	def "always apply maven-publish plugin [#packaging]"(packaging) {
		makeSingleProject(packaging)
		buildFile << """
			tasks.register('verify') {
				doLast {
					assert pluginManager.hasPlugin('maven-publish')
				}
			}
		"""

		expect:
		succeeds('verify')

		where:
		packaging << ['maven-plugin', 'ejb', 'rar', 'ear', 'pom', 'jar', 'war']
	}
}
