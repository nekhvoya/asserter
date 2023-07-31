package nl.nekhvoya;

/**
 * Exception that will be thrown after the test execution has completed.
 */
public class TestExecutionError extends AssertionError {

    public TestExecutionError(String message) {
        super(message);
    }
}
