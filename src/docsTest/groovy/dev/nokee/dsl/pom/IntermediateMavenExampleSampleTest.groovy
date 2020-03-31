package dev.nokee.dsl.pom

class IntermediateMavenExampleSampleTest extends AbstractSampleTest {
	@Override
	protected String getSampleName() {
		return '2.intermediate-maven-example'
	}

	def "can report dependencyInsight"() {
		expect:
		executer.withTasks('dependencyInsight', '--configuration', 'compileClasspath', '--dependency', 'org.hibernate:hibernate:3.2.5.ga').run()
	}
}
