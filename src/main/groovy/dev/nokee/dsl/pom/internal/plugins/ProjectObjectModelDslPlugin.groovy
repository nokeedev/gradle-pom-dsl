package dev.nokee.dsl.pom.internal.plugins

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class ProjectObjectModelDslPlugin implements Plugin<Settings> {
    @Override
    void apply(Settings settings) {
		def pom = new XmlSlurper().parse(new File(settings.settingsDir, 'pom.xml'))
		settings.rootProject.name = pom.artifactId
		settings.gradle.rootProject {
			it.group = pom.groupId
			it.version = pom.version
			it.description = pom.name
		}
    }
}
