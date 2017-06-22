package de.eacg.ecs.publisher;

/**
 * Step execution error
 *
 * @author Varanytsia Anatolii
 */
public class StepExecutionError extends Exception {
    /**
     * Constructor
     *
     * @param message message
     */
    public StepExecutionError(String message) {
        super(message);
    }
}
