package dev.nokee.dsl.pom.internal

import groovy.util.slurpersupport.GPathResult

class ProjectObjectModel {
	private final ProjectObjectModelFile pomFile
	private final GPathResult pom

	private ProjectObjectModel(ProjectObjectModelFile pomFile, GPathResult pom) {
		this.pomFile = pomFile
		this.pom = pom
	}

	String getArtifactId() {
		if (pom.artifactId.size() == 0) {
			throw new IllegalStateException("${pomFile.path} file doesn't have an artifactId tag.")
		}
		return pom.artifactId
	}

	String getPackaging() {
		if (pom.packaging.size() == 0) {
			return 'jar'
		}
		return pom.packaging
	}

	boolean hasBuildTag() {
		if (pom.build.size() == 0) {
			return false
		}
		return true
	}

	def propertyMissing(String name) {
		return pom."$name"
	}

	def propertyMissing(String name, def arg) {
		throw new UnsupportedOperationException()
	}

	static ProjectObjectModel of(ProjectObjectModelFile pomFile) {
		if (!pomFile.exists()) {
			throw new IllegalArgumentException("${pomFile.path} file doesn't exists.")
		}
		GPathResult pom = new XmlSlurper().parse(pomFile.asFile)
		if (pom.name() != 'project') {
			throw new IllegalArgumentException("${pomFile.path} file doesn't have a 'project' root tag (i.e. ${pom.name()}).")
		}
		if (pom.modelVersion.size() == 0) {
			throw new IllegalArgumentException("${pomFile.path} file doesn't have a 'modelVersion' tag.")
		}
		if (pom.modelVersion != '4.0.0') {
			throw new IllegalArgumentException("${pomFile.path} file contains an unsupported version for 'modelVersion' (i.e. ${pom.modelVersion}).")
		}
		return new ProjectObjectModel(pomFile, pom)
	}
}
