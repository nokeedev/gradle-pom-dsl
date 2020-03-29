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

	protected TestFile getTestDirectory() {
		return TestFile.of(temporaryFolder.testDirectory)
	}

	protected TestFile file(Object... path) {
		return testDirectory.file(path)
	}

	protected TestFile getPomFile() {
		return file('pom.xml')
	}

	def "throws exception when pom.xml file does not exists"() {
		when:
		ProjectObjectModel.of(pomFile)
		then:
		def exception = thrown(IllegalArgumentException)
		exception.message == ''
	}
}
