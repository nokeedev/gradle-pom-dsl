package dev.nokee.dsl.pom.internal

import groovy.transform.Immutable
import lombok.Value
import lombok.With
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.gradle.maven.MavenModule

import java.nio.file.Path

class MavenModuleLocator {
	Map<String, Model> locateAllModules(File rootDirectory) {
		return processModules(locateRootModule(rootDirectory), '')
	}

	private File locateRootModule(File rootDirectory) {
		return new File(rootDirectory, 'pom.xml')
	}

	Map<String, Model> processModules(File pomFile, String path) {
		def result = [:]
		MavenXpp3Reader reader = new MavenXpp3Reader()
		def pom = reader.read(pomFile.newInputStream())
		pom.pomFile = pomFile

		if (path.empty) {
			result.put(':', pom)
		} else {
			result.put(path, pom)
		}

		pom.modules.each { modulePath ->
			result.putAll(processModules(new File(pomFile.parentFile, "${modulePath}/pom.xml"), "$path:$modulePath"))
		}

		return result
	}
}
