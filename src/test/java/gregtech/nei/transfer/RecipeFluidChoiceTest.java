package gregtech.nei.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class RecipeFluidChoiceTest {

    /** The cutting machine fluids, in the order NEI shows them. */
    private static final List<String> CUTTING_FLUIDS = List.of("water", "distilled water", "lubricant", "superfluid");

    @Test
    void takesTheFluidInTheTank() {
        assertEquals(2, RecipeFluidChoice.choose(CUTTING_FLUIDS, "lubricant"::equals, "water"::equals));
    }

    @Test
    void otherwiseTakesOneThePlayerHas() {
        assertEquals(1, RecipeFluidChoice.choose(CUTTING_FLUIDS, fluid -> false, "distilled water"::equals));
    }

    @Test
    void otherwiseTakesTheFirst() {
        assertEquals(0, RecipeFluidChoice.choose(CUTTING_FLUIDS, fluid -> false, fluid -> false));
    }
}
