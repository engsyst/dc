package ua.nure.trspo.omp;

public class Simple {
	public static void main(String[] args) {
		String helpMsg = """
                omp4j - the OpenMP-like preprocessor
                http://www.omp4j.org
                Author: Petr Belohlavek <omp4j@petrbel.cz>
                ------------------------------------------
                Execution: omp4j [params] [files]
                Supported options are the same as the options supported by javac
                Additionally, the option listed below are supported:
                -d <dir>    Directory where preprocessed/compiled classes are stored.
                -h    Print help
                -n    Do not compile preprocessed sources. Store only .java files.
                -v    Provide progress information
                Please refer to www.omp4j.org/tutorial for more information
                ------------------------------------------
                Example executions:
                $ omp4j -d classes MyClass1.java MyClass2.java # javac-like behavior
                $ omp4j -d sources -v -n MyClass1.java MyClass2.java # preprocess only""";
		System.out.println(helpMsg);
		
		int foo = 5;
		// omp parallel
		{
			System.out.println("hello");
			System.out.println("world");
			System.out.println(foo);
		}

		System.out.println("last line");
	}
}
