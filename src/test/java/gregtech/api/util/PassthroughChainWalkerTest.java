package gregtech.api.util;

import static gregtech.api.util.PassthroughChainWalker.StepKind.ENDPOINT;
import static gregtech.api.util.PassthroughChainWalker.StepKind.HULL;
import static gregtech.api.util.PassthroughChainWalker.StepKind.OTHER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gregtech.api.util.PassthroughChainWalker.StepKind;

class PassthroughChainWalkerTest {

    private static PassthroughChainWalker.Stepper of(StepKind... kinds) {
        // step is 1-based; anything past the array is OTHER (air, stone, whatever)
        return step -> step <= kinds.length ? kinds[step - 1] : OTHER;
    }

    @Test
    void endpointDirectlyAdjacentIsStepOne() {
        assertEquals(1, PassthroughChainWalker.walk(of(ENDPOINT), 16));
    }

    @Test
    void walksThroughChainedHullsToEndpoint() {
        assertEquals(4, PassthroughChainWalker.walk(of(HULL, HULL, HULL, ENDPOINT), 16));
    }

    @Test
    void unrelatedBlockEndsTheWalk() {
        assertEquals(-1, PassthroughChainWalker.walk(of(HULL, OTHER, ENDPOINT), 16));
    }

    @Test
    void airEndsTheWalk() {
        assertEquals(-1, PassthroughChainWalker.walk(of(HULL, HULL), 16));
    }

    @Test
    void limitIsInclusive() {
        assertEquals(3, PassthroughChainWalker.walk(of(HULL, HULL, ENDPOINT), 3));
    }

    @Test
    void endpointBeyondLimitIsNotFound() {
        assertEquals(-1, PassthroughChainWalker.walk(of(HULL, HULL, HULL, ENDPOINT), 3));
    }

    @Test
    void zeroLimitFindsNothing() {
        assertEquals(-1, PassthroughChainWalker.walk(of(ENDPOINT), 0));
    }
}
