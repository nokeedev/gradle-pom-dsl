package dev.nokee.dsl.pom.internal.plugins

import dev.nokee.dsl.pom.internal.ProjectObjectModel
import dev.nokee.dsl.pom.internal.ProjectObjectModelFile
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class ProjectObjectModelDslPlugin implements Plugin<Settings> {
    @Override
    void apply(Settings settings) {
		def pom = ProjectObjectModel.of(new ProjectObjectModelFile(settings.settingsDir, 'pom.xml'))
		settings.rootProject.name = pom.artifactId
		settings.gradle.rootProject {
			it.group = pom.groupId
			it.version = pom.version
			it.description = pom.name

			if (pom.packaging == 'war') {
				it.pluginManager.apply('war')
			} else if (pom.packaging == 'jar') {
				it.pluginManager.apply('java')
			} else if (pom.packaging == 'ear') {
				it.pluginManager.apply('ear')
			} else if (pom.packaging == 'pom') {
				// ignore pom packaging
			} else {
				it.logger.lifecycle("Project '${it.path}' use an unsupported packaging (i.e. ${pom.packaging}), future version may support it.")
			}

			if (it.pluginManager.hasPlugin('java-base')) {
				it.repositories.mavenCentral()
			}

			pom.dependencies.each { dep ->
				def configurationName = null
				if (dep.scope == 'compile') {
					configurationName = 'compile'
				} else if (dep.scope == 'runtime') {
					configurationName = 'runtimeOnly'
				} else if (dep.scope == 'provided') {
					configurationName = 'compileOnly'
				} else if (dep.scope == 'test') {
					configurationName = 'testImplementation'
				} else {
					it.logger.lifecycle("Project '${it.path}' use an unsupported dependency scope (i.e. ${dep.groupId}:${dep.artifactId}:${dep.version} for ${dep.scope})")
					return
				}

				def notation = [group: dep.groupId, name: dep.artifactId, version: dep.version, ext: dep.type, classifier: dep.classifier]
				it.dependencies.add(configurationName, notation) { d ->
					dep.exclusions.each { exclusion ->
						d.exclude([group: exclusion.groupId, module: exclusion.artifactId])
					}
				}
			}

			pom.getUnsupportedTags().each { tag ->
				it.logger.lifecycle("Project '${it.path}' use an unsupported tag (i.e. ${tag}), future version may support it.")
			}
		}
    }
}
