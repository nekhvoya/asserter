package nl.nekhvoya;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Exception Manager allows to store all the exceptions occurred during the executions of a test and throw them all at once at the end of the test.
 * It allows to wrap all the exceptions and errors and throw a custom TestExecutionError instead with the message containing the main exception caused test failure and its stacktrace
 * as well as the list of all soft assertions that failed during the test execution.
 */
class ExceptionManager {

    private Throwable testException;
    private List<Throwable> softAssertions = new LinkedList<>();

    public ExceptionManager() {
    }

    public Throwable getTestException() {
        return testException;
    }

    public void setTestException(Throwable testException) {
        this.testException = testException;
    }

    public List<Throwable> getSoftAssertions() {
        return softAssertions;
    }

    public void setSoftAssertions(List<Throwable> softAssertions) {
        this.softAssertions = softAssertions;
    }

    public void addSoftAssertions(Throwable softAssertion) {
        this.softAssertions.add(softAssertion);
    }

    /**
     * Clears all collected errors.
     */
    public void flush() {
        setTestException(null);
        softAssertions.clear();
    }

    /**
     * Throws custom TestExecutionError if there were any failures during the test execution.
     */
    public void throwOnFailure() {
        if (testException != null) {
            throw new TestExecutionError(createMessage(testException, softAssertions));
        } else if (!softAssertions.isEmpty()) {
            throw new TestExecutionError(createMessage(softAssertions));
        }
    }

    private static String createMessage(Throwable testException, List<Throwable> softAssertions) {
        StringBuilder builder = new StringBuilder();
        builder.append("Exception caused test failure: ").append(testException.getClass().getSimpleName()).append(": ").append(testException.getMessage())
                .append(System.lineSeparator());
        Stream.of(testException.getStackTrace()).forEach(s -> builder.append("\t").append(s).append(System.lineSeparator()));
        builder.append(System.lineSeparator());

        if (!softAssertions.isEmpty()) {
            builder.append(createMessage(softAssertions));
        }
        return builder.toString();
    }

    private static String createMessage(List<Throwable> softAssertions) {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator());
        builder.append("Failed Soft Assertions: ").append(System.lineSeparator());
        AtomicInteger i = new AtomicInteger(1);
        softAssertions.forEach(a -> {
            builder.append(i.get()).append(") ");
            builder.append(a.getMessage()).append(System.lineSeparator());
            i.getAndIncrement();
        });
        return builder.toString();
    }
}
