package dev.nokee.dsl.pom.internal

import javax.annotation.Nullable

import static java.util.Collections.emptyList
import static java.util.Collections.emptyMap

class ProjectObjectModel {
	private final ProjectObjectModelFile pomFile
	private final XmlOptionalWrapper pom

	private ProjectObjectModel(ProjectObjectModelFile pomFile, Node pom) {
		this.pomFile = pomFile
		this.pom = new XmlOptionalWrapper(pom)
	}

	String getArtifactId() {
		return pom.getAsString('artifactId').orElseThrow { new IllegalStateException("${pomFile.path} file doesn't have an artifactId tag.") }
	}

	String getPackaging() {
		return pom.getAsString('packaging').orElse('jar')
	}

	Optional<String> getGroupId() {
		return pom.getAsString('groupId')
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

	List<Dependency> getDependencies() {
		return pom.get('dependencies').map { it.children().collect { n -> new Dependency(n) } }.orElse(emptyList())
	}

	private static final List<String> UNSUPPORTED_TAGS = [
		// The basic
		'parent',
		'dependencyManagement',
		'modules',

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

	static ProjectObjectModel of(ProjectObjectModelFile pomFile) {
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
		return new ProjectObjectModel(pomFile, pom)
	}


	static class XmlOptionalWrapper {
		private final Node delegate

		XmlOptionalWrapper(Node delegate) {
			this.delegate = delegate
		}

		Optional<Node> get(String name) {
			return Optional.of(delegate.getByName(name)).filter { it.size() == 1 }.map { it.first() }
		}

		Optional<String> getAsString(String name) {
			return get(name).map { it.text() }
		}

		Map<String, String> getAsMap(String name) {
			return get(name).map { it.children().collectEntries { ["${it.name()}": it.text()] }}.orElse(emptyMap())
		}

		List<String> getAsList(String name) {
			return get(name).map { it.children().collect { it.text() }}.orElse(emptyList())
		}

		Optional<Node> getAsNode(String name) {
			return get(name).map { it }
		}
	}

	class Dependency {
		private final XmlOptionalWrapper delegate

		Dependency(Node delegate) {
			this.delegate = new XmlOptionalWrapper(delegate)
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
			return delegate.getAsString('version').get()
		}

		@Nullable
		String getClassifier() {
			return delegate.getAsString('classifier').orElse(null)
		}

		List<Exclusion> getExclusions() {
			return delegate.get('exclusions').map { it.children().collect { new Exclusion(it) } }.orElse(emptyList())
		}

		static class Exclusion {
			private final XmlOptionalWrapper delegate

			Exclusion(XmlOptionalWrapper delegate) {
				this.delegate = new XmlOptionalWrapper(delegate)
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
