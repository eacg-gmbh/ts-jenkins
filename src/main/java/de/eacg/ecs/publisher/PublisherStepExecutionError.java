package de.eacg.ecs.publisher;

/**
 * Step execution error
 *
 * @author Varanytsia Anatolii
 */
public class PublisherStepExecutionError extends Exception {
    /**
     * Constructor
     *
     * @param message message
     */
    public PublisherStepExecutionError(String message) {
        super(message);
    }
}
