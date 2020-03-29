package dev.nokee

import dev.nokee.dsl.pom.internal.plugins.ProjectObjectModelDslPlugin
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class PomDslPlugin implements Plugin<Settings> {
	@Override
	void apply(Settings settings) {
		settings.pluginManager.apply(ProjectObjectModelDslPlugin)
	}
}
