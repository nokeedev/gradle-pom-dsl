package dev.nokee.dsl.pom

import dev.gradleplugins.integtests.fixtures.AbstractFunctionalSpec
import dev.gradleplugins.test.fixtures.file.TestFile

class AbstractProjectObjectModelFunctionalSpec extends AbstractFunctionalSpec {
	TestFile getPomFile() {
		return file('pom.xml')
	}

	def setup() {
		settingsFile << """
			plugins {
				id 'dev.nokee.pom-dsl'
			}
		"""
	}
}
