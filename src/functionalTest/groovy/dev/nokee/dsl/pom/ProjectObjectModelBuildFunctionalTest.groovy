package dev.nokee.dsl.pom

class ProjectObjectModelBuildFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	def "shows warning for unsupported build tag"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
				<build>
					<plugins>
						<plugin>
							<groupId>org.apache.maven.plugins</groupId>
							<artifactId>maven-shade-plugin</artifactId>
							<version>2.1</version>
							<executions>
								<execution>
									<phase>package</phase>
									<goals>
										<goal>shade</goal>
									</goals>
									<configuration>
										<transformers>
											<transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
												<mainClass>hello.HelloWorld</mainClass>
											</transformer>
										</transformers>
									</configuration>
								</execution>
							</executions>
						</plugin>
					</plugins>
				</build>
			</project>
		"""

		expect:
		succeeds('help')
		outputContains("Project ':' use an unsupported tag (i.e. build), future version may support it.")
	}

	def "does not show warning when build tag isn't present"() {
		pomFile << """
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>com.mycompany.app</groupId>
				<artifactId>my-app</artifactId>
				<version>1</version>
			</project>
		"""

		expect:
		succeeds('help')
		!result.output.contains("Project ':' use an unsupported tag (i.e. build), future version may support it.")
	}

	// TODO: Show warning when parent pom
}
