package dev.nokee.dsl.pom.internal

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.apache.maven.model.Parent

@ToString
@CompileStatic
class ProjectObjectModelRegistry {
	private final Map<String, EffectiveProjectObjectModel> registry = [:]

	void register(EffectiveProjectObjectModel model) {
		registry.put("${model.groupId}:${model.artifactId}:${model.version}".toString(), model)
	}

	EffectiveProjectObjectModel get(String id) {
		assert registry.containsKey(id)
		return Optional.ofNullable(registry.get(id)).orElseThrow { new IllegalStateException('The registry should always have the requested project') }
	}

	EffectiveProjectObjectModel get(Parent parentId) {
		return get("${parentId.groupId}:${parentId.artifactId}:${parentId.version}")
	}

	boolean has(String id) {
		return registry.containsKey(id)
	}

	EffectiveProjectObjectModel getRoot() {
		return Optional.ofNullable(registry.values().find { it.path == ':' }).orElseThrow { new IllegalStateException('There should always be a root project ') }
	}

	Set<EffectiveProjectObjectModel> getAllSubprojects() {
		return (registry.values() as Set) - [getRoot()]
	}

	EffectiveProjectObjectModel forPath(String path) {
		return Optional.ofNullable(registry.values().find { it.path == path }).orElseThrow { new IllegalStateException('There should be one project for the path') }
	}
}
