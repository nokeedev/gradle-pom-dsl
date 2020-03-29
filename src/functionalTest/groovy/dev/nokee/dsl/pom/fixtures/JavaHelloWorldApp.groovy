package dev.nokee.dsl.pom.fixtures

import dev.gradleplugins.test.fixtures.sources.SourceElement
import dev.gradleplugins.test.fixtures.sources.SourceFile
import dev.gradleplugins.test.fixtures.sources.SourceFileElement
import dev.gradleplugins.test.fixtures.sources.java.JavaSourceElement
import dev.gradleplugins.test.fixtures.sources.java.JavaSourceFileElement

import static dev.gradleplugins.test.fixtures.sources.SourceFileElement.ofFile

class JavaHelloWorldApp extends JavaSourceElement {
	SourceFileElement helloWorld
	SourceFileElement helloWorldWithDependency
	SourceFileElement greeter

	@Override
	SourceElement getSources() {
		return ofElements(helloWorld, greeter)
	}

	JavaHelloWorldApp() {
		helloWorld = ofFile(sourceFile('java/hello', 'HelloWorld.java', '''
package hello;

public class HelloWorld {
  public static void main(String[] args) {
    Greeter greeter = new Greeter();
    System.out.println(greeter.sayHello());
  }
}
'''))
		greeter = ofFile(sourceFile('java/hello', 'Greeter.java', '''
package hello;

public class Greeter {
  public String sayHello() {
    return "Hello world!";
  }
}
'''))
		helloWorldWithDependency = ofFile(sourceFile('java/hello', 'HelloWorld.java', '''
package hello;

import org.joda.time.LocalTime;

public class HelloWorld {
  public static void main(String[] args) {
    LocalTime currentTime = new LocalTime();
    System.out.println("The current local time is: " + currentTime);
    Greeter greeter = new Greeter();
    System.out.println(greeter.sayHello());
  }
}
'''))
	}

	JavaSourceElement withExternalDependency() {
		return new JavaSourceElement() {
			@Override
			SourceElement getSources() {
				return ofElements(helloWorldWithDependency, greeter)
			}
		}
	}
}
