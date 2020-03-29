package dev.nokee.dsl.pom.internal

import groovy.util.slurpersupport.GPathResult

import static java.util.Collections.emptyList

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

	Map<String, String> getProperties() {
		return pom.properties.children().collectEntries { ["${it.name()}": it] }
	}

	List<ProjectObjectModelDependency> getDependencies() {
		return pom.dependencies.children().collect { new ProjectObjectModelDependency(it) }
	}

	private static final List<String> UNSUPPORTED_TAGS = [
		// The basic
		'parent',
		'dependencyManagement',
		'modules',

		// Build Settings
		'build',
		'reporting',

		// More Project Information
		'name',
		'url',
		'inceptionYear',
		'licenses',
		'organization',
		'developers',
		'contributors',

		// Environment Settings
		'issueManagement',
		'ciManagement',
		'mailingLists',
		'scm',
		'prerequisites',
		'repositories',
		'pluginRepositories',
		'distributionManagement',
		'profiles'
	]
	List<String> getUnsupportedTags() {
		return pom.children().findAll { UNSUPPORTED_TAGS.contains(it.name()) }*.name()
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
