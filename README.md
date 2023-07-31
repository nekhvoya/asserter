# Asserter 
This is a tiny library wrapping assertions.

Sometimes test scenarios (especially end-to-end) could become quite complex containing a lot of steps and assertion steps.
If we encounter an assertion error while running a test we would sometimes like to proceed with the test to see if there are more errors on the way.

The asserter collects all the assertions along the test execution and proceeds execution of the test.
The test will fail when it completes the execution or if an error occurs that doesn't allow to proceed the execution.
