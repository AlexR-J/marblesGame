--REQUIREMENTS--

- The folder that contains the jar file must also contain 3 text files containing the weights of the pebbles.
	+This must be a list of integers separated by only a comma.
	+The number of pebbles must be at least 11 times the number of players you intend to use.
	Eg: 1,2,3,4,5,6,7,8,9,10 

- A working version of Java SE Development kit (javaSE-13)

NOTE: Running this program will create new files so placing it in a place where they are contained would be advised.

--RUNNING-- 

To run the program you can use the command "java -jar PebblesGame.jar".
to do so the console must have first been opened in the same directory as the jar file.
to do this you can use the command "cd <your directory>"

--TESTING SUITE--

Using Eclipse:
- All of the java bytecode (.class) and source (.java) files must be in the same directory
- These will then need to be imported into a project within your software of choice. (in our case it was eclipse)
- Ensure that all of the source files have been added to a named package called pebblesGame
- Ensure that Junit4 has been added to the class path of the project. Else the testing will not function as intended.

You should not be able to run the testCases idividually, or you can run the testSuite which will run all of the tests together.
