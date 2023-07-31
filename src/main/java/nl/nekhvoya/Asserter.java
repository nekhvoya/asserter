package nl.nekhvoya;

/**
 * Provides methods allowing to wrap an assertion call to prevent it from throwing an exception right away.
 * Allows proceeding with the execution and collect o=all the encountered errors.
 */
public class Asserter {

    private static final ThreadLocal<ExceptionManager> exceptionManager = ThreadLocal.withInitial(ExceptionManager::new);

    private Asserter() {
        // There is no reason to create an instance of this class
    }

    /**
     * Wraps test steps and stores all the exceptions by means of the ExceptionManager.
     * Wraps all the assertion errors.
     * Fails right away if an exception other than AssertionError was encountered.
     * @param runnable an assertion that needs to be wrapped.
     */
    public static void collect(Runnable runnable) {
        try {
            runnable.run();
        } catch (AssertionError assertionError) {
            exceptionManager.get().addSoftAssertions(assertionError);
        } catch (Throwable anotherError){
            exceptionManager.get().setTestException(anotherError);
            throwCollected();
        }
    }

    /**
     * Throws an exception containing all the collected errors.
     */
    public static void throwCollected() {
        exceptionManager.get().throwOnFailure();
        exceptionManager.get().flush();
    }
}
