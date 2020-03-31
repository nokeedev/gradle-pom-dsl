package dev.nokee.dsl.pom

import dev.gradleplugins.spock.lang.CleanupTestDirectory
import dev.gradleplugins.spock.lang.TestNameTestDirectoryProvider
import dev.gradleplugins.test.fixtures.file.TestFile
import dev.gradleplugins.test.fixtures.gradle.executer.GradleExecuter
import dev.gradleplugins.test.fixtures.gradle.executer.GradleRunnerExecuter
import dev.gradleplugins.test.fixtures.scan.GradleEnterpriseBuildScan
import org.junit.Rule
import spock.lang.Specification

@CleanupTestDirectory
abstract class AbstractSampleTest extends Specification {
	@Rule
	final TestNameTestDirectoryProvider temporaryFolder = new TestNameTestDirectoryProvider(getClass())
	GradleExecuter executer = new GradleRunnerExecuter(TestFile.of(temporaryFolder.testDirectory))
	TestFile m2

	def setup() {
		sampleDirectory.file(sampleName).copyTo(testDirectory)
		m2 = getTestDirectory().file('m2')
		m2.mkdirs()
		executer = executer.usingInitScript(initScriptFile).withArgument('-Dmaven.repo.local=' + m2.absolutePath)
	}

	def "can assemble"() {
		expect:
		executer.withTasks('assemble').run()
	}

	def "can build"() {
		expect:
		executer.withTasks('build').run()
	}

	def "can create build scans"() {
		expect:
		new GradleEnterpriseBuildScan().apply(executer.withTasks('build')).run()
	}

	def "can execute projects"() {
		expect:
		executer.withTasks('projects').run()
	}

	def "can execute tasks"() {
		expect:
		executer.withTasks('tasks').run()
	}

	def "can publish"() {
		expect:
		executer.withTasks('publishToMavenLocal').run()
		def p = ['tree', m2].execute()
		p.waitFor() == 0
		println p.text
	}

	protected TestFile getSampleDirectory() {
		return TestFile.of(new File("${System.getProperty("user.dir")}/src/docs/samples"))
	}

	protected TestFile getInitScriptFile() {
		return TestFile.of(new File("${System.getProperty('user.dir')}/pom.init.gradle"))
	}

	protected TestFile getTestDirectory() {
		return TestFile.of(temporaryFolder.testDirectory)
	}

	protected abstract String getSampleName();
}
