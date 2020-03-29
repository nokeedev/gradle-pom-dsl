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
	JavaSourceElement greeterTest

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
		greeterTest = new JavaSourceElement() {

			@Override
			String getSourceSetName() {
				return 'test'
			}

			@Override
			SourceElement getSources() {
				return ofFile(sourceFile('java/hello', 'GreeterTest.java', '''
package hello;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

public class GreeterTest {

  private Greeter greeter = new Greeter();

  @Test
  public void greeterSaysHello() {
    assertThat(greeter.sayHello(), containsString("Hello"));
  }

}
'''))
			}
		}
	}

	JavaSourceElement withExternalDependency() {
		return new JavaSourceElement() {
			@Override
			SourceElement getSources() {
				return ofElements(helloWorldWithDependency, greeter)
			}
		}
	}

	SourceElement withJUnitTest() {
		return ofElements(helloWorld, greeter, greeterTest)
	}
}
