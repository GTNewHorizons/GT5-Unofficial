package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.util.GTUtility;

/**
 * Tests for {@link MTEElectricAutoWorkbench} mode 0 (Normal Crafting Table) recipe-grid construction,
 * which drives the recipe preview shown in slot 28.
 */
class MTEElectricAutoWorkbenchTest {

    private static final int OUTPUT_SLOT = 18;
    private static final int TEMPLATE_OFFSET = 19; // phantom template occupies slots 19-27

    /**
     * Regression test: a stray (non-template) item in the buffer that cannot be ejected this tick - e.g.
     * a container item returned to slots 9-17 after a craft - must NOT blank the preview. The recipe grid
     * (and thus slot 28) must still reflect the phantom template.
     */
    @Test
    void modeZero_strayItemThatCannotBeEjected_stillBuildsTemplateRecipe() {
        ItemStack template = mock(ItemStack.class);
        ItemStack stray = mock(ItemStack.class);

        ItemStack[] inventory = new ItemStack[29];
        inventory[TEMPLATE_OFFSET] = template; // single-ingredient template, top-left cell
        inventory[9] = stray; // stray in a return slot (index 9 -> outside the 0-7 input grid)
        // OUTPUT_SLOT (18) left empty

        try (MockedStatic<GTUtility> gtUtil = mockStatic(GTUtility.class)) {
            gtUtil.when(() -> GTUtility.copy(any(ItemStack.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
            gtUtil.when(() -> GTUtility.areStacksEqual(any(), any()))
                .thenReturn(false); // the stray never matches the template

            ItemStack[] grid = MTEElectricAutoWorkbench.buildModeZeroRecipe(inventory, 9, 0);

            assertSame(
                template,
                grid[0],
                "mode 0 preview grid must mirror the phantom template even with a stray item present");
            for (int i = 1; i < 9; i++) {
                assertNull(grid[i], "empty template cells must stay empty");
            }
        }
    }

    /**
     * The eject path is preserved: a stray item in the input grid (slots 0-7) with a free output and
     * throughput headroom is being ejected this tick, so the grid is intentionally left empty (no craft)
     * to avoid crafting over the item just moved to the output slot.
     */
    @Test
    void modeZero_strayItemBeingEjected_leavesGridEmpty() {
        ItemStack template = mock(ItemStack.class);
        ItemStack stray = mock(ItemStack.class);

        ItemStack[] inventory = new ItemStack[29];
        inventory[TEMPLATE_OFFSET] = template;
        inventory[0] = stray; // stray in the input grid; eligible for ejection (output free, throughput 0)

        try (MockedStatic<GTUtility> gtUtil = mockStatic(GTUtility.class)) {
            gtUtil.when(() -> GTUtility.copy(any(ItemStack.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
            gtUtil.when(() -> GTUtility.areStacksEqual(any(), any()))
                .thenReturn(false);

            ItemStack[] grid = MTEElectricAutoWorkbench.buildModeZeroRecipe(inventory, 0, 0);

            for (int i = 0; i < 9; i++) {
                assertNull(grid[i], "grid must be empty on the tick a stray item is ejected");
            }
        }
    }
}
