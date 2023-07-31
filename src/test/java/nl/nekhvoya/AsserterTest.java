package nl.nekhvoya;

import org.junit.Test;

import static nl.nekhvoya.Asserter.collect;
import static nl.nekhvoya.Asserter.throwCollected;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AsserterTest {

    @Test(expected = TestExecutionError.class)
    public void testCollectingAssertions() {
        collect(() -> assertThat(true).as("Test error message 1").isFalse());
        collect(() -> assertThat("Test text").as("Test error message 2").isEqualTo("Not matching text"));
        collect(() -> assertThat(new Object()).as("Object was not null").isNull());
        throwCollected();
    }

    @Test(expected = TestExecutionError.class)
    public void testCollectingOtherErrors() {
        collect(() -> assertThat(true).as("Test assertion message").isFalse());
        collect(() -> { throw new NullPointerException(); });
        collect(() -> assertThat(new Object()).as("Object was not null").isNull());
        throwCollected();
    }

    @Test(expected = AssertionError.class)
    public void testFailOnNotCollectedAssertion() {
        collect(() -> assertThat(true).as("Test error message 1").isFalse());
        collect(() -> assertThat("Test text").as("Test error message 2").isEqualTo("Not matching text"));
        assertThat(true).as("Not collected error").isFalse();
        collect(() -> assertThat(new Object()).as("Object was not null").isNull());
        throwCollected();
    }
}
