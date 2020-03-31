package dev.nokee.dsl.pom

import dev.nokee.dsl.pom.fixtures.JavaHelloWorldApp
import groovy.xml.MarkupBuilder

class ProjectObjectModelPublishingFunctionalTest extends AbstractProjectObjectModelFunctionalSpec {
	protected void makeSingleProject(Map<String, Object> tags) {
		pomFile.delete()
		pomFile << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-maven</artifactId>
    <packaging>jar</packaging>
    <version>0.1.0</version>

    ${toXml(tags)}
</project>
		"""
	}

	String toXml(Map tags) {
		new StringWriter().with { sw ->
			new MarkupBuilder(sw).with {
				tags.collect { k, v ->
					"$k" { v instanceof Map ? v.collect(owner) : mkp.yield(v) }
				}
			}
			return sw.toString()
		}
	}

	JavaHelloWorldApp getComponentUnderTest() {
		return new JavaHelloWorldApp()
	}

	def "handles the name tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([name: 'Some Name'])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<name>Some Name</name>')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<name>')
	}

	def "handles the url tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([url: 'Some Name'])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<url>Some Name</url>')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<url>')
	}

	def "handles the inceptionYear tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([inceptionYear: 'Some Name'])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<inceptionYear>Some Name</inceptionYear>')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<inceptionYear>')
	}

	def "handles the ciManagement tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([ciManagement: [url: 'https://continuum.example.com/']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<ciManagement><url>https://continuum.example.com/</url></ciManagement>')

		when:
		makeSingleProject([ciManagement: [system: 'continuum']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<ciManagement><system>continuum</system></ciManagement>')

		when:
		makeSingleProject([ciManagement: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<ciManagement>')
		!pomContent.contains('<ciManagement />')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<ciManagement>')
		!pomContent.contains('<ciManagement />')
	}

	def "handles the organization tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([organization: [url: 'https://example.com/']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<organization><url>https://example.com/</url></organization>')

		when:
		makeSingleProject([organization: [name: 'Example LLC']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<organization><name>Example LLC</name></organization>')

		when:
		makeSingleProject([organization: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<organization>')
		!pomContent.contains('<organization />')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<organization>')
		!pomContent.contains('<organization />')
	}

	def "handles the contributors tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when: 'has name'
		makeSingleProject([contributors: [contributor: [name: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><name>Foo bar</name></contributor></contributors>')

		when: 'has timezone'
		makeSingleProject([contributors: [contributor: [timezone: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><timezone>Foo bar</timezone></contributor></contributors>')

		when: 'has organization url'
		makeSingleProject([contributors: [contributor: [organizationUrl: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><organizationUrl>Foo bar</organizationUrl></contributor></contributors>')

		when: 'has organization'
		makeSingleProject([contributors: [contributor: [organization: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><organization>Foo bar</organization></contributor></contributors>')

		when: 'has email'
		makeSingleProject([contributors: [contributor: [email: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><email>Foo bar</email></contributor></contributors>')

		when: 'has url'
		makeSingleProject([contributors: [contributor: [url: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><url>Foo bar</url></contributor></contributors>')

		when: 'has properties'
		makeSingleProject([contributors: [contributor: [properties: [key: 'Foo bar']]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><properties><key>Foo bar</key></properties></contributor></contributors>')

		when: 'has roles'
		makeSingleProject([contributors: [contributor: [roles: [role: 'Foo bar']]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><roles><role>Foo bar</role></roles></contributor></contributors>')

		when: 'ignores empty properties'
		makeSingleProject([contributors: [contributor: [properties: [:]]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><properties/></contributor></contributors>')

		when: 'ignores empty roles'
		makeSingleProject([contributors: [contributor: [roles: '']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors><contributor><roles/></contributor></contributors>')

		when: 'empty contributors'
		makeSingleProject([contributors: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<contributors/>')

		when: 'no contributors'
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<contributors>')
		!pomContent.contains('<contributors/>')
		!pomContent.contains('<contributors />')
	}

	def "handles the scm tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when: 'has url'
		makeSingleProject([scm: [url: 'Foo bar']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<scm><url>Foo bar</url></scm>')

		when: 'has connection'
		makeSingleProject([scm: [connection: 'Foo bar']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<scm><connection>Foo bar</connection></scm>')

		when: 'has developer connection'
		makeSingleProject([scm: [developerConnection: 'Foo bar']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<scm><developerConnection>Foo bar</developerConnection></scm>')

		when: 'has tag'
		makeSingleProject([scm: [tag: 'Foo bar']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<scm><tag>Foo bar</tag></scm>')

		when: 'no scm'
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<scm>')
		!pomContent.contains('<scm/>')
		!pomContent.contains('<scm />')
	}

	def "handles the mailingLists tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when: 'has name'
		makeSingleProject([mailingLists: [mailingList: [name: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><name>Foo bar</name></mailingList></mailingLists>')

		when: 'has archive'
		makeSingleProject([mailingLists: [mailingList: [archive: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><archive>Foo bar</archive></mailingList></mailingLists>')

		when: 'has post'
		makeSingleProject([mailingLists: [mailingList: [post: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><post>Foo bar</post></mailingList></mailingLists>')

		when: 'has subscribe'
		makeSingleProject([mailingLists: [mailingList: [subscribe: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><subscribe>Foo bar</subscribe></mailingList></mailingLists>')

		when: 'has unsubscribe'
		makeSingleProject([mailingLists: [mailingList: [unsubscribe: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><unsubscribe>Foo bar</unsubscribe></mailingList></mailingLists>')

		when: 'has otherArchives'
		makeSingleProject([mailingLists: [mailingList: [otherArchives: [otherArchive: 'Foo bar']]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><otherArchives><otherArchive>Foo bar</otherArchive></otherArchives></mailingList></mailingLists>')

		when: 'ignores empty properties'
		makeSingleProject([mailingLists: [mailingList: [otherArchives: '']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists><mailingList><otherArchives/></mailingList></mailingLists>')

		when: 'empty mailingLists'
		makeSingleProject([mailingLists: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<mailingLists/>')

		when: 'no mailingLists'
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<mailingLists>')
		!pomContent.contains('<mailingLists/>')
		!pomContent.contains('<mailingLists />')
	}

	def "handles the developers tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when: 'has name'
		makeSingleProject([developers: [developer: [name: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><name>Foo bar</name></developer></developers>')

		when: 'has timezone'
		makeSingleProject([developers: [developer: [timezone: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><timezone>Foo bar</timezone></developer></developers>')

		when: 'has organization url'
		makeSingleProject([developers: [developer: [organizationUrl: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><organizationUrl>Foo bar</organizationUrl></developer></developers>')

		when: 'has organization'
		makeSingleProject([developers: [developer: [organization: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><organization>Foo bar</organization></developer></developers>')

		when: 'has email'
		makeSingleProject([developers: [developer: [email: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><email>Foo bar</email></developer></developers>')

		when: 'has url'
		makeSingleProject([developers: [developer: [url: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><url>Foo bar</url></developer></developers>')

		when: 'has properties'
		makeSingleProject([developers: [developer: [properties: [key: 'Foo bar']]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><properties><key>Foo bar</key></properties></developer></developers>')

		when: 'has roles'
		makeSingleProject([developers: [developer: [roles: [role: 'Foo bar']]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><roles><role>Foo bar</role></roles></developer></developers>')

		when: 'ignores empty properties'
		makeSingleProject([developers: [developer: [properties: [:]]]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><properties/></developer></developers>')

		when: 'ignores empty roles'
		makeSingleProject([developers: [developer: [roles: '']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers><developer><roles/></developer></developers>')

		when: 'empty developers'
		makeSingleProject([developers: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<developers/>')

		when: 'no developers'
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<developers>')
		!pomContent.contains('<developers/>')
		!pomContent.contains('<developers />')
	}

	def "handles the licenses tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when: 'has name'
		makeSingleProject([licenses: [license: [name: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<licenses><license><name>Foo bar</name></license></licenses>')

		when: 'has timezone'
		makeSingleProject([licenses: [license: [comments: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<licenses><license><comments>Foo bar</comments></license></licenses>')

		when: 'has organization url'
		makeSingleProject([licenses: [license: [distribution: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<licenses><license><distribution>Foo bar</distribution></license></licenses>')

		when: 'has url'
		makeSingleProject([licenses: [license: [url: 'Foo bar']]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<licenses><license><url>Foo bar</url></license></licenses>')

		when: 'empty licenses'
		makeSingleProject([licenses: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<licenses/>')

		when: 'no licenses'
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<licenses>')
		!pomContent.contains('<licenses/>')
		!pomContent.contains('<licenses />')
	}

	def "handles the issueManagement tag when publishing"() {
		componentUnderTest.writeToProject(testDirectory)

		when:
		makeSingleProject([issueManagement: [url: 'https://example.com/']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<issueManagement><url>https://example.com/</url></issueManagement>')

		when:
		makeSingleProject([issueManagement: [system: 'Foo bar']])
		succeeds('generatePomFileForJarPublication')
		then:
		pomContent.contains('<issueManagement><system>Foo bar</system></issueManagement>')

		when:
		makeSingleProject([issueManagement: [:]])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<issueManagement>')
		!pomContent.contains('<issueManagement />')

		when:
		makeSingleProject([:])
		succeeds('generatePomFileForJarPublication')
		then:
		!pomContent.contains('<issueManagement>')
		!pomContent.contains('<issueManagement />')
	}

	protected String getPomContent() {
		return file('build/publications/jar/pom-default.xml').assertExists().readLines()*.trim().join('')
	}
}
