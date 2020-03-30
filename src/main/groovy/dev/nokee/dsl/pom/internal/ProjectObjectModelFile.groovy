package dev.nokee.dsl.pom.internal

import java.nio.file.Path

class ProjectObjectModelFile {
	final File rootDirectory
	final String path

	ProjectObjectModelFile(File rootDirectory, String path) {
		this.rootDirectory = rootDirectory
		this.path = path
	}

	File getAsFile() {
		return new File(rootDirectory, path)
	}

	boolean exists() {
		return asFile.exists()
	}

	ProjectObjectModelFile module(String module) {
		Path p = new File(path).toPath()
		if (p.parent == null) {
			return new ProjectObjectModelFile(rootDirectory, "${module}/pom.xml")
		}
		return new ProjectObjectModelFile(rootDirectory, p.getParent().resolve("${module}/pom.xml").toString())
	}

	String getModulePath() {
		Path p = new File(path).toPath()
		if (p.parent == null) {
			return ''
		}
		return p.parent.toString().replace('/', ':')
	}
}
