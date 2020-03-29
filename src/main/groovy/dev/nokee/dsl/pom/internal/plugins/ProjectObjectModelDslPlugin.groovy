package dev.nokee.dsl.pom.internal.plugins

import dev.nokee.dsl.pom.internal.ProjectObjectModel
import dev.nokee.dsl.pom.internal.ProjectObjectModelFile
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.XmlProvider
import org.gradle.api.initialization.Settings
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class ProjectObjectModelDslPlugin implements Plugin<Settings> {
    @Override
    void apply(Settings settings) {
		def pom = ProjectObjectModel.of(new ProjectObjectModelFile(settings.settingsDir, 'pom.xml'))
		settings.rootProject.name = pom.artifactId
		settings.gradle.rootProject { project ->
			// Configure basic information
			pom.groupId.ifPresent {
				project.ext.groupId = it
				project.group = it
			}
			project.ext.artifactId = project.name
			pom.version.ifPresent { project.version = it }
			pom.description.ifPresent { project.description = it }

			// Always apply maven publishing plugin
			project.pluginManager.apply('maven-publish')
			project.extensions.configure(PublishingExtension) { publishing ->
				publishing.publications.withType(MavenPublication).all { MavenPublication publication ->
					publication.pom.withXml(copy(pom))
				}
			}

			// Apply plugins based on packaging
			if (pom.packaging == 'war') {
				project.pluginManager.apply('war')
			} else if (pom.packaging == 'jar') {
				project.pluginManager.apply('java')
				project.extensions.configure(PublishingExtension) { publishing ->
					publishing.publications.create('maven', MavenPublication) {
						it.from(project.components.java)
					}
				}
			} else if (pom.packaging == 'ear') {
				project.pluginManager.apply('ear')
			} else if (pom.packaging == 'pom') {
				// ignore pom packaging
			} else {
				project.logger.lifecycle("Project '${project.path}' use an unsupported packaging (i.e. ${pom.packaging}), future version may support it.")
			}

			// For java project, add the maven Central repository
			if (project.pluginManager.hasPlugin('java-base')) {
				project.repositories.mavenCentral()
			}

			// Map properties to extra properties
			pom.properties.each { k, v ->
				project.ext."$k" = v
			}

			// Map dependencies
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
					project.logger.lifecycle("Project '${project.path}' use an unsupported dependency scope (i.e. ${dep.groupId}:${dep.artifactId}:${dep.version} for ${dep.scope})")
					return
				}

				def notation = [group: dep.groupId, name: dep.artifactId, version: dep.version, classifier: dep.classifier]
				project.dependencies.add(configurationName, notation) { d ->
					dep.exclusions.each { exclusion ->
						d.exclude([group: exclusion.groupId, module: exclusion.artifactId])
					}
				}
			}

			pom.getUnsupportedTags().each { tag ->
				project.logger.lifecycle("Project '${project.path}' use an unsupported tag (i.e. ${tag}), future version may support it.")
			}
		}
    }

	private Action<XmlProvider> copy(ProjectObjectModel pom) {
		return new Action<XmlProvider>() {
			@Override
			void execute(XmlProvider xmlProvider) {
				pom.getNode('name').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('url').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('inceptionYear').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('ciManagement').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('contributors').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('organization').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('scm').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('mailingLists').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('developers').ifPresent { xmlProvider.asNode().append(it) }
				pom.getNode('licenses').ifPresent { xmlProvider.asNode().append(it) }
			}
		}
	}
}
