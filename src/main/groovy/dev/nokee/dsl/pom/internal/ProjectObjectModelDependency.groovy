package dev.nokee.dsl.pom.internal

import groovy.transform.ToString;
import groovy.util.slurpersupport.GPathResult

import javax.annotation.Nullable

import static java.util.Collections.emptyList;

class ProjectObjectModelDependency {
	private final GPathResult delegate

	ProjectObjectModelDependency(GPathResult delegate) {
		this.delegate = delegate
	}

	def propertyMissing(String name) {
		return delegate."$name"
	}

	def propertyMissing(String name, def arg) {
		throw new UnsupportedOperationException()
	}

	String getScope() {
		if (delegate.scope.size() == 0) {
			return 'compile'
		}
		return delegate.scope
	}

	String getType() {
		if (delegate.type.size() == 0) {
			return 'jar'
		}
		return delegate.type
	}

	@Nullable
	String getClassifier() {
		if (delegate.classifier.size() == 0) {
			return null
		}
		return delegate.classifier
	}

	List<Exclusion> getExclusions() {
		return delegate.exclusions.children().collect { new Exclusion(it) }
	}

	static class Exclusion {
		private final GPathResult delegate

		Exclusion(GPathResult delegate) {
			this.delegate = delegate
		}

		@Nullable
		String getGroupId() {
			if (delegate.groupId.size() == 0) {
				return null
			}
			return delegate.groupId
		}

		@Nullable
		String getArtifactId() {
			if (delegate.artifactId.size() == 0) {
				return null
			}
			return delegate.artifactId
		}
	}
}
