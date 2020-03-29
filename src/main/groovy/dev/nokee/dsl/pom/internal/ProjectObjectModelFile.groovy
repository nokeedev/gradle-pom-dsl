package dev.nokee.dsl.pom.internal

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
}
