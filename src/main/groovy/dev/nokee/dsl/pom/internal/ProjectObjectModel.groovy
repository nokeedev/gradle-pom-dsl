package dev.nokee.dsl.pom.internal

import groovy.text.SimpleTemplateEngine
import groovy.xml.QName
import org.gradle.api.Project

import javax.annotation.Nullable

import static java.util.Collections.emptyList
import static java.util.Collections.emptyMap

class ProjectObjectModel {
	private final SimpleTemplateEngine engine = new SimpleTemplateEngine()
	private final ProjectObjectModelFile pomFile
	private final XmlOptionalWrapper pom
	private final Project project
	private final Map<String, ProjectObjectModel> projectRegistry

	private ProjectObjectModel(ProjectObjectModelFile pomFile, Node pom, Map<String, ProjectObjectModel> projectRegistry) {
		this(pomFile, pom, projectRegistry, null)
	}

	private ProjectObjectModel(ProjectObjectModelFile pomFile, Node pom, Map<String, ProjectObjectModel> projectRegistry, Project project) {
		this.pomFile = pomFile
		this.pom = new XmlOptionalWrapper(pom)
		this.projectRegistry = projectRegistry
		this.project = project
	}

	ProjectObjectModel attach(Project project) {
		return new ProjectObjectModel(pomFile, pom.delegate, projectRegistry, project)
	}

	ProjectObjectModelFile getFile() {
		return pomFile
	}

	String getArtifactId() {
		return pom.getAsString('artifactId').orElseThrow { new IllegalStateException("${pomFile.path} file doesn't have an artifactId tag.") }
	}

	String getPackaging() {
		return pom.getAsString('packaging').orElse('jar')
	}

	Optional<ProjectObjectModel> getParent() {
		return pom.getAsNode('parent').map { new Dependency(new XmlOptionalWrapper(it, false)) }.map {
			projectRegistry.get("${it.groupId}:${it.artifactId}".toString())
		}
	}

	Optional<String> getGroupId() {
		return Optional.ofNullable(pom.getAsString('groupId').orElseGet { getParent().flatMap { it.groupId }.orElse(null) })
	}

	Optional<String> getVersion() {
		return pom.getAsString('version')
	}

	Optional<String> getDescription() {
		return pom.getAsString('description')
	}

	Map<String, String> getProperties() {
		return pom.getAsMap('properties')
	}

	Optional<Node> getNode(String name) {
		return pom.getAsNode(name)
	}

	List<ProjectObjectModel> getModules() {
		return pom.getAsNode('modules').map { it.children().collect { of(pomFile.module(it.text()), projectRegistry) } }.orElse(emptyList())
	}

	List<Dependency> getDependencyManagement() {
		def result = pom.get('dependencyManagement').map {
			return it.get('dependencies').first().children().collect { new Dependency(new XmlOptionalWrapper(it)) }
		}.orElse(emptyList())

		if (result.empty) {
			return getParent().map { it.dependencyManagement }.orElse(emptyList())
		}
		return result
	}

	List<Dependency> getDependencies() {
		return pom.get('dependencies').map { it.children().collect { new Dependency(new XmlOptionalWrapper(it)) } }.orElse(emptyList())
	}

	private static final List<String> UNSUPPORTED_TAGS = [
		// The basic
		'parent',
		'dependencyManagement',

		// Build Settings
		'build',
		'reporting',

		// Environment Settings
		'prerequisites',
		'repositories',
		'pluginRepositories',
		'distributionManagement',
		'profiles'
	]
	List<String> getUnsupportedTags() {
		return pom.delegate.children().findAll { UNSUPPORTED_TAGS.contains(it.name()) }*.name()
	}

	def propertyMissing(String name) {
		return pom.get(name).get()
	}

	def propertyMissing(String name, def arg) {
		throw new UnsupportedOperationException()
	}

	static ProjectObjectModel of(ProjectObjectModelFile pomFile, Map<String, ProjectObjectModel> projectRegistry) {
		if (!pomFile.exists()) {
			throw new IllegalArgumentException("${pomFile.path} file doesn't exists.")
		}
		Node pom = new XmlParser().parse(pomFile.asFile)
		if (!pom.name().matches('project')) {
			throw new IllegalArgumentException("${pomFile.path} file doesn't have a 'project' root tag (i.e. ${pom.name()}).")
		}
		if (pom.modelVersion.size() == 0) {
			throw new IllegalArgumentException("${pomFile.path} file doesn't have a 'modelVersion' tag.")
		}
		if (pom.modelVersion.text() != '4.0.0') {
			throw new IllegalArgumentException("${pomFile.path} file contains an unsupported version for 'modelVersion' (i.e. ${pom.modelVersion.text()}).")
		}
		return new ProjectObjectModel(pomFile, pom, projectRegistry)
	}

	@Override
	String toString() {
		return "ProjectObjectModel{modulePath=${file.modulePath},artifactId=${artifactId}}"
	}


	class XmlOptionalWrapper {
		private final Node delegate
		private final boolean interpolate

		XmlOptionalWrapper(Node delegate, boolean interpolate = true) {
			this.interpolate = interpolate
			this.delegate = delegate
		}

		Optional<Node> get(String name) {
			return Optional.of(delegate.getByName(name)).filter { it.size() == 1 }.map { it.first() }
		}

		Optional<String> getAsString(String name) {
			return get(name).map { toString(it.text()) }
		}

		Map<String, String> getAsMap(String name) {
			return get(name).map { it.children().collectEntries { ["${getName(it)}": it.text()] }}.orElse(emptyMap())
		}

		private String getName(Node node) {
			if (node.name() instanceof QName) {
				return node.name().localPart
			}
			return node.name()
		}

		List<String> getAsList(String name) {
			return get(name).map { it.children().collect { it.text() }}.orElse(emptyList())
		}

		Optional<Node> getAsNode(String name) {
			return get(name).map { it }
		}

		private String toString(String value) {
			if (interpolate) {
				ProjectObjectModel.this.getProperties().each { k, v ->
					value = value.replace("\${$k}", v)
				}
				return ProjectObjectModel.this.engine.createTemplate(value).make([project: ProjectObjectModel.this.project])
			}
			return value
		}
	}

	class Dependency {
		private final XmlOptionalWrapper delegate

		Dependency(XmlOptionalWrapper delegate) {
			this.delegate = delegate
		}

		String getScope() {
			return delegate.getAsString('scope').orElse('compile')
		}

		String getType() {
			return delegate.getAsString('type').orElse('jar')
		}

		String getGroupId() {
			return delegate.getAsString('groupId').get()
		}

		String getArtifactId() {
			return delegate.getAsString('artifactId').get()
		}

		String getVersion() {
			return delegate.getAsString('version').orElseGet {
				ProjectObjectModel.this.dependencyManagement.find { it.groupId == Dependency.this.groupId && it.artifactId == Dependency.this.artifactId }.version
			}
		}

		@Nullable
		String getClassifier() {
			return delegate.getAsString('classifier').orElse(null)
		}

		List<Exclusion> getExclusions() {
			return delegate.get('exclusions').map { it.children().collect { new Exclusion(new XmlOptionalWrapper(it)) } }.orElse(emptyList())
		}

		@Override
		String toString() {
			return "Dependency{groupId=${delegate.getAsString('groupId')},artifactId=${delegate.getAsString('artifactId')}}";
		}


		static class Exclusion {
			private final XmlOptionalWrapper delegate

			Exclusion(XmlOptionalWrapper delegate) {
				this.delegate = delegate
			}

			@Nullable
			String getGroupId() {
				return delegate.getAsString('groupId').orElse(null)
			}

			@Nullable
			String getArtifactId() {
				return delegate.getAsString('artifactId').orElse(null)
			}
		}
	}
}
