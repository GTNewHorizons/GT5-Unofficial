package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.util.GTUtility;

/**
 * Unit tests covering the logic changed in the Auto Workbench (LV) fix PR:
 * <ul>
 * <li>Fix 1 — {@link MTEElectricAutoWorkbench#buildCircleRecipe} (Circle mode positional grid read)</li>
 * <li>Fix 2 — {@link MTEElectricAutoWorkbench#updateRecipePreview} (preview always reflects the output)</li>
 * <li>Fix 3 — {@link MTEElectricAutoWorkbench#benchContent(ItemStack[])} (material aggregation across gaps)</li>
 * <li>Fix 4 — {@link MTEElectricAutoWorkbench#buildModeZeroRecipe} (mode 0 preview vs stray buffer items)</li>
 * </ul>
 */
class MTEElectricAutoWorkbenchTest {

    private static final int TEMPLATE_OFFSET = 19; // phantom template occupies slots 19-27

    // ---------------------------------------------------------------------------------------------
    // Fix 4 — mode 0 (Normal Crafting Table) preview vs stray buffer items
    // ---------------------------------------------------------------------------------------------

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
        // output slot (18) left empty

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

    // ---------------------------------------------------------------------------------------------
    // Fix 1 — Circle mode (mode 9) reads the live grid positionally
    // ---------------------------------------------------------------------------------------------

    /**
     * Circle mode must copy live grid slots 0-8 position-for-position (preserving empty cells), not
     * compact non-null items toward the front. An item placed in slot 3 must land in grid cell 3 so that
     * shaped recipes can match; the stack size is normalised to 1.
     */
    @Test
    void circleMode_readsGridPositionally_notCompacted() {
        ItemStack item = mock(ItemStack.class);
        item.stackSize = 64;

        ItemStack[] inventory = new ItemStack[29];
        inventory[3] = item; // single item in cell 3; cells 0-2 and 4-8 empty

        try (MockedStatic<GTUtility> gtUtil = mockStatic(GTUtility.class)) {
            gtUtil.when(() -> GTUtility.copy(any(ItemStack.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

            ItemStack[] grid = MTEElectricAutoWorkbench.buildCircleRecipe(inventory);

            assertSame(item, grid[3], "Circle mode must keep the item in its original grid position");
            assertEquals(1, grid[3].stackSize, "recipe grid stack size must be normalised to 1");
            for (int i = 0; i < 9; i++) {
                if (i != 3) {
                    assertNull(grid[i], "empty cells must remain empty (no compaction toward the front)");
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Fix 2 — recipe preview (slot 28) always reflects the current output
    // ---------------------------------------------------------------------------------------------

    @Test
    void updatePreview_clearsWhenNoRecipe() {
        ItemStack[] inventory = new ItemStack[29];
        inventory[28] = mock(ItemStack.class); // stale preview from a previous tick

        MTEElectricAutoWorkbench.updateRecipePreview(inventory, null);

        assertNull(inventory[28], "preview must be cleared when the current grid yields no recipe");
    }

    @Test
    void updatePreview_showsCurrentOutput() {
        ItemStack[] inventory = new ItemStack[29];
        ItemStack output = mock(ItemStack.class);

        MTEElectricAutoWorkbench.updateRecipePreview(inventory, output);

        assertSame(output, inventory[28], "preview must reflect the current recipe output");
    }

    // ---------------------------------------------------------------------------------------------
    // Fix 3 — benchContent aggregates available materials by type, even across empty slots
    // ---------------------------------------------------------------------------------------------

    /**
     * Two stacks of the same item separated by an empty slot must be summed under a single entry. This
     * exercises the index bug that credited counts to the wrong accumulator entry when a null slot made
     * the inventory index and the list index diverge.
     */
    @Test
    void benchContent_sumsSameItemAcrossGaps() {
        Item coalItem = mock(Item.class);
        Item stickItem = mock(Item.class);

        ItemStack coalA = stackOf(coalItem, 3);
        ItemStack stick = stackOf(stickItem, 2);
        ItemStack coalB = stackOf(coalItem, 5);

        ItemStack[] inventory = new ItemStack[29];
        inventory[0] = null; // leading gap: makes inventory index != accumulator index
        inventory[1] = coalA;
        inventory[2] = stick;
        inventory[3] = coalB;

        try (MockedStatic<GTUtility> gtUtil = mockStatic(GTUtility.class)) {
            gtUtil.when(() -> GTUtility.areStacksEqual(any(), any()))
                .thenAnswer(invocation -> {
                    ItemStack a = invocation.getArgument(0);
                    ItemStack b = invocation.getArgument(1);
                    return a != null && b != null && a.getItem() == b.getItem();
                });
            gtUtil.when(() -> GTUtility.copy(any(ItemStack.class)))
                .thenAnswer(invocation -> {
                    ItemStack src = invocation.getArgument(0);
                    return stackOf(src.getItem(), src.stackSize); // independent copy
                });

            ArrayList<ItemStack> content = MTEElectricAutoWorkbench.benchContent(inventory);

            assertEquals(2, content.size(), "distinct item types must produce one entry each");
            assertSame(
                coalItem,
                content.get(0)
                    .getItem());
            assertEquals(8, content.get(0).stackSize, "coal (3 + 5) must aggregate to 8");
            assertSame(
                stickItem,
                content.get(1)
                    .getItem());
            assertEquals(2, content.get(1).stackSize, "stick must remain 2, not absorb coal's count");
        }
    }

    private static ItemStack stackOf(Item item, int size) {
        ItemStack stack = mock(ItemStack.class);
        when(stack.getItem()).thenReturn(item);
        stack.stackSize = size;
        return stack;
    }
}
