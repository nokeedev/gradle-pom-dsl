package dev.nokee.dsl.pom.internal.plugins

import dev.nokee.dsl.pom.internal.EffectiveProjectObjectModel
import dev.nokee.dsl.pom.internal.MavenModuleLocator
import dev.nokee.dsl.pom.internal.ProjectObjectModelRegistry
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class ProjectObjectModelDslPlugin implements Plugin<Settings> {
    @Override
    void apply(Settings settings) {
		def registry = new ProjectObjectModelRegistry()
		def allModules = new MavenModuleLocator().locateAllModules(settings.settingsDir)
			.collect { k, v -> new EffectiveProjectObjectModel(k, v, registry) }

		if (!allModules.empty) {
			// Register all projects
			allModules.each {
				registry.register(it)
			}

			// Configure the structure
			settings.rootProject.name = registry.root.artifactId
			registry.allSubprojects.each { pom ->
				settings.include(pom.path)
			}
			settings.gradle.beforeProject { project ->
				configureProject(project, registry.forPath(project.path), registry)
			}
		}
    }

	private void configureProject(Project project, EffectiveProjectObjectModel pom, ProjectObjectModelRegistry registry) {
			try {
				// Configure basic information
				// FIXME: Setting the group cause Gradle to create loops inside the same project... completely awful... especially after 8h+ of debugging
//				project.group = pom.groupId
				project.version = pom.version
				pom.description.ifPresent { project.description = it }

				// Always apply maven publishing plugin
				project.pluginManager.apply('maven-publish')
				project.extensions.configure(PublishingExtension) { publishing ->
					publishing.publications.withType(MavenPublication).all { MavenPublication publication ->
						publication.pom.withXml { pom.copyTo(it) }
						publication.artifactId = pom.artifactId

						// Configuring the groupId here because of the issue above
						publication.groupId = pom.groupId
					}
				}

				// Apply plugins based on packaging
				if (pom.packaging == 'war') {
					project.pluginManager.apply('java-library') // for the api configuration
					project.pluginManager.apply('war')
				} else if (pom.packaging == 'jar') {
					project.pluginManager.apply('java-library') // for the api configuration instead of using the java plugin
					project.extensions.configure(PublishingExtension) { publishing ->
						publishing.publications.create('jar', MavenPublication) {
							it.from(project.components.java)
						}
					}
				} else if (pom.packaging == 'ear') {
					project.pluginManager.apply('java-library') // for the api configuration
					project.pluginManager.apply('ear')
				} else if (pom.packaging == 'pom') {
					// ignore pom packaging
					project.pluginManager.apply('base')
					project.extensions.configure(PublishingExtension) { publishing ->
						publishing.publications.create('pom', MavenPublication)
					}
				} else {
					project.logger.lifecycle("Project '${project.path}' use an unsupported packaging (i.e. ${pom.packaging}), future version may support it.")
				}

				// For java project, add the maven Central repository
				if (project.pluginManager.hasPlugin('java-base')) {
					project.repositories.mavenCentral()
				}

				// Map properties to extra properties
				project.ext.groupId = pom.groupId
				project.ext.artifactId = pom.artifactId
				pom.properties.each { k, v ->
					project.ext."$k" = v
				}

				// Map dependencies
				pom.dependencies.each { dep ->
					def configurationName = null
					def scope = Optional.ofNullable(dep.scope).orElse('compile')
					if (scope == 'compile') {
						configurationName = 'api'
					} else if (scope == 'runtime') {
						configurationName = 'runtimeOnly'
					} else if (scope == 'provided') {
						configurationName = 'compileOnly'
					} else if (scope == 'test') {
						configurationName = 'testImplementation'
					} else {
						project.logger.lifecycle("Project '${project.path}' use an unsupported dependency scope (i.e. ${dep.groupId}:${dep.artifactId}:${dep.version} for ${scope})")
						return
					}

					def projectNotation = String.format('%s:%s:%s', dep.groupId, dep.artifactId, dep.version)
					if (registry.has(projectNotation)) {
						def proj = registry.get(projectNotation)
						project.dependencies.add(configurationName, project.project(proj.path))
					} else {
						def notation = [group: dep.groupId, name: dep.artifactId, version: dep.version, classifier: dep.classifier]
						project.dependencies.add(configurationName, notation) { d ->
							dep.exclusions.each { exclusion ->
								d.exclude([group: exclusion.groupId, module: exclusion.artifactId])
							}
						}
					}
				}
			} catch (Throwable e) {
				throw new GradleException("Exception while configuring project '${project.path}'", e)
			}
	}
}
