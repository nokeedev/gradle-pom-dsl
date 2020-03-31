package dev.nokee.dsl.pom.internal

import groovy.text.SimpleTemplateEngine
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.gradle.api.XmlProvider

import java.util.function.Function

@CompileStatic
class EffectiveProjectObjectModel {
	private final String path
	private final Model pom
	private final ProjectObjectModelRegistry registry
	private final SimpleTemplateEngine engine = new SimpleTemplateEngine()

	EffectiveProjectObjectModel(String path, Model pom, ProjectObjectModelRegistry registry) {
		assertModelVersion(pom.modelVersion)
		this.path = path
		this.pom = pom
		this.registry = registry
	}

	private static void assertModelVersion(String modelVersion) {
		if (modelVersion == null) {
			throw new RuntimeException("pom.xml file doesn't have a 'modelVersion' tag")
		}
		if (modelVersion != '4.0.0') {
			throw new RuntimeException("pom.xml file contains an unsupported version for 'modelVersion' (i.e. ${modelVersion})")
		}
	}

	String getPath() {
		return path
	}

	private Optional<EffectiveProjectObjectModel> getParent() {
		if (pom.getParent()) {
			return Optional.of(registry.get(pom.getParent()))
		}
		return Optional.<EffectiveProjectObjectModel>empty()
	}

	String getGroupId() {
		return Optional.ofNullable(pom.groupId).orElseGet { getParent().map { it.groupId }.orElse(null) }
	}

	String getArtifactId() {
		return Optional.ofNullable(pom.artifactId).orElseThrow { new IllegalStateException("pom.xml file doesn't have an artifactId tag") }
	}

	String getVersion() {
		return Optional.ofNullable(pom.version).orElseGet { getParent().map{ it.version }.orElse(null) }
	}

	String getPackaging() {
		return Optional.ofNullable(pom.packaging).orElse('jar')
	}

	Map<String, String> getProperties() {
		def result = new HashMap<String, String>()
		getParent().ifPresent {
			result.putAll(it.properties)
		}
		pom.properties.each {k, v ->
			result.put(k.toString(), v.toString())
		}
		return result
	}

	private List<Dependency> getDependencyManagement() {
		List<Dependency> result = getParent().map { it.dependencyManagement }.orElse([])
		List<Dependency> dependencies = Optional.ofNullable(pom.dependencyManagement).map { it.dependencies }.orElse(new ArrayList<Dependency>())
		result.addAll(dependencies.collect { convert(it) })
		return result
	}

	List<Dependency> getDependencies() {
		def depManagement = dependencyManagement

		return pom.dependencies.collect { dep ->
			def result = new Dependency()
			depManagement.findAll { it.groupId == dep.groupId && it.artifactId == dep.artifactId }.each { mergeTo(it, result) }
			mergeTo(dep, result)

			return convert(result)
		}
	}

	@CompileDynamic
	void copyTo(XmlProvider xmlProvider) {
		Node xmlPom = new XmlParser().parse(pom.pomFile)
		maybeNode(xmlPom.name).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.url).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.inceptionYear).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.ciManagement).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.contributors).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.organization).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.scm).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.mailingLists).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.developers).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.licenses).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.issueManagement).ifPresent { xmlProvider.asNode().append(it) }
		maybeNode(xmlPom.properties).ifPresent {xmlProvider.asNode().append(it) }
	}

	private Optional<Node> maybeNode(NodeList node) {
		return Optional.of(node).filter { it.size() == 1 }.map { (Node)it.first() }
	}

	private void mergeTo(Dependency src, Dependency dst) {
		Optional.ofNullable(src.artifactId).ifPresent { dst.artifactId = it }
		Optional.ofNullable(src.groupId).ifPresent { dst.groupId = it }
		Optional.ofNullable(src.version).ifPresent { dst.version = it }
		Optional.ofNullable(src.scope).ifPresent { dst.scope = it }
		Optional.ofNullable(src.classifier).ifPresent { dst.classifier = it }
		Optional.ofNullable(src.type).ifPresent { dst.type = it }

		if (!src.exclusions.empty && !dst.exclusions.empty) {
			throw new UnsupportedOperationException("Not sure what to do in this case")
		} else if (!src.exclusions.empty && dst.exclusions.empty) {
			dst.exclusions = src.exclusions
		}
	}

	private Dependency convert(Dependency src) {
		def dst = new Dependency()
		Optional.ofNullable(src.artifactId).map(interpolate()).ifPresent { dst.artifactId = it }
		Optional.ofNullable(src.groupId).map(interpolate()).ifPresent { dst.groupId = it }
		Optional.ofNullable(src.version).map(interpolate()).ifPresent { dst.version = it }
		Optional.ofNullable(src.scope).map(interpolate()).ifPresent { dst.scope = it }
		Optional.ofNullable(src.classifier).map(interpolate()).ifPresent { dst.classifier = it }
		Optional.ofNullable(src.type).map(interpolate()).ifPresent { dst.type = it }

		// Let's not interpolate this value
		dst.exclusions = src.exclusions

		return dst
	}

	Optional<String> getDescription() {
		return maybeGet('description').<String>map(interpolate())
	}

	@CompileDynamic
	private <T> Optional<T> maybeGet(String name) {
		Optional<T> result = Optional.<T>ofNullable(pom."$name")
		if (!result.present) {
			result = parent.flatMap { it."$name" }
		}
		return result
	}

	private Function<String, String> interpolate() {
		return new Function<String, String>() {
			@Override
			String apply(String value) {
				EffectiveProjectObjectModel.this.getProperties().each { k, v ->
					value = value.replace("\${$k}".toString(), v)
				}
				return EffectiveProjectObjectModel.this.engine.createTemplate(value).make([project: [artifactId: EffectiveProjectObjectModel.this.artifactId, version: EffectiveProjectObjectModel.this.version, groupId: EffectiveProjectObjectModel.this.groupId]])
			}
		}
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (!(o instanceof EffectiveProjectObjectModel)) return false

		EffectiveProjectObjectModel that = (EffectiveProjectObjectModel) o

		if (path != that.path) return false

		return true
	}

	int hashCode() {
		return path.hashCode()
	}
}
