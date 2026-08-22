package gregtech.api.util;

/**
 * Walks a straight line of chained pass-through hulls looking for a network endpoint. Pure logic so it can be unit
 * tested; callers supply the block classification.
 */
public final class PassthroughChainWalker {

    public enum StepKind {
        /** Another pass-through hull: keep walking. */
        HULL,
        /** A valid network endpoint (AE2 cable bus / routed LP pipe). */
        ENDPOINT,
        /** Anything else: the chain is broken here. */
        OTHER
    }

    public interface Stepper {

        /** @param step 1-based distance from the origin hull along one direction. */
        StepKind kindAt(int step);
    }

    private PassthroughChainWalker() {}

    /**
     * @return 1-based step index of the endpoint, or -1 if there is none within {@code limit} steps.
     */
    public static int walk(Stepper stepper, int limit) {
        for (int step = 1; step <= limit; step++) {
            switch (stepper.kindAt(step)) {
                case HULL -> {}
                case ENDPOINT -> {
                    return step;
                }
                default -> {
                    return -1;
                }
            }
        }
        return -1;
    }
}
