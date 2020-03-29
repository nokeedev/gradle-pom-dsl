package dev.nokee.dsl.pom.internal

import dev.gradleplugins.spock.lang.CleanupTestDirectory
import dev.gradleplugins.spock.lang.TestNameTestDirectoryProvider
import dev.gradleplugins.test.fixtures.file.TestFile
import org.junit.Rule
import spock.lang.Specification

@CleanupTestDirectory
class ProjectObjectModelTest extends Specification {
	@Rule
	final TestNameTestDirectoryProvider temporaryFolder = new TestNameTestDirectoryProvider(getClass())

	def "throws exception when pom.xml file does not exists"() {
		when:
		ProjectObjectModel.of(new ProjectObjectModelFile(temporaryFolder.testDirectory, 'pom.xml'))
		then:
		def exception = thrown(IllegalArgumentException)
		exception.message == "pom.xml file doesn't exists."
	}
}
