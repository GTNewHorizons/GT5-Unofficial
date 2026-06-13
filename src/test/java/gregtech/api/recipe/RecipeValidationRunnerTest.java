package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RecipeValidationRunnerTest {

    @Test
    void validationRunnerCompletesWhenNoValidatorsAreEnabled() {
        assertDoesNotThrow(() -> RecipeValidationRunner.run(false, false, () -> {}, () -> {}));
    }

    @Test
    void validationRunnerRunsBothValidatorsBeforeThrowingCombinedFailure() {
        boolean[] ranLookup = { false };
        boolean[] ranDuplicates = { false };

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeValidationRunner.run(true, true, () -> {
                ranLookup[0] = true;
                throw new IllegalStateException("lookup failure");
            }, () -> {
                ranDuplicates[0] = true;
                throw new IllegalStateException("duplicate failure");
            }));

        assertTrue(ranLookup[0]);
        assertTrue(ranDuplicates[0]);
        assertTrue(
            error.getMessage()
                .contains("lookup failure"));
        assertTrue(
            error.getMessage()
                .contains("duplicate failure"));
    }

    @Test
    void validationRunnerCanRunOnlyDuplicateValidator() {
        boolean[] ranDuplicates = { false };

        assertDoesNotThrow(
            () -> RecipeValidationRunner.run(
                false,
                true,
                () -> { throw new AssertionError("lookup should not run"); },
                () -> ranDuplicates[0] = true));

        assertTrue(ranDuplicates[0]);
    }

    @Test
    void validationRunnerRunsOnlyLookupValidatorWhenOnlyLookupFlagIsEnabled() {
        boolean[] ranLookup = { false };

        assertDoesNotThrow(
            () -> RecipeValidationRunner.run(
                true,
                false,
                () -> ranLookup[0] = true,
                () -> { throw new AssertionError("duplicate should not run"); }));

        assertTrue(ranLookup[0]);
    }

    @Test
    void validationRunnerCompletesWhenBothValidatorsPass() {
        assertDoesNotThrow(() -> RecipeValidationRunner.run(true, true, () -> {}, () -> {}));
    }
}
