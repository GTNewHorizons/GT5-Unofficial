package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RecipeValidationGateTest {

    @Test
    void intentionalShutdownMessageMarksGateAndReason() {
        IllegalStateException error = RecipeValidationGate
            .intentionalShutdown("snapshot export completed to baseline.json");

        assertTrue(
            error.getMessage()
                .contains(RecipeValidationGate.GATE_MARKER));
        assertTrue(
            error.getMessage()
                .contains("snapshot export completed to baseline.json"));
        assertTrue(
            error.getMessage()
                .contains("(intentional shutdown)"));
        assertEquals(
            "GT recipe validation gate: snapshot export completed to baseline.json (intentional shutdown).",
            error.getMessage());
    }
}
