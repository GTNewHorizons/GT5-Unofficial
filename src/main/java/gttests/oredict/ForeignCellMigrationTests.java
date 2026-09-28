package gttests.oredict;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;

/// Pins that IC2's cells and Railcraft's creosote cell leave play: saved stacks load as gregtech cells holding the
/// same fluid, and no canner recipe makes them anew.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class ForeignCellMigrationTests {

    private static final String IC2_CELL = "IC2:itemCellEmpty";
    private static final String CREOSOTE_CELL = "Railcraft:fluid.creosote.cell";
    /// IC2 cell metas whose gregtech cell holds IC2's own fluid.
    private static final int[] SAME_FLUID_METAS = { 1, 2, 3, 4, 6, 7, 9, 10, 11, 12, 13 };

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void savedForeignCellsLoadAsGregtechCells(GameTestHelper helper) {
        List<String> mismatches = new ArrayList<>();
        Item ic2Cell = (Item) Item.itemRegistry.getObject(IC2_CELL);
        helper.assertTrue(ic2Cell != null, "IC2's cell item is not registered");
        ItemStack emptyLoaded = load(new ItemStack(ic2Cell, 1, 0));
        if (!ItemList.Cell_Empty.isStackEqual(emptyLoaded)) {
            mismatches.add(IC2_CELL + "@0 -> " + describe(emptyLoaded));
        }
        for (int meta : SAME_FLUID_METAS) {
            expectSameFluid(mismatches, new ItemStack(ic2Cell, 1, meta));
        }
        Item creosoteCell = (Item) Item.itemRegistry.getObject(CREOSOTE_CELL);
        if (creosoteCell != null) expectSameFluid(mismatches, new ItemStack(creosoteCell, 1, 0));
        helper.assertTrue(mismatches.isEmpty(), "Saved foreign cells did not migrate: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void noCannerRecipeMakesForeignCells(GameTestHelper helper) {
        List<String> makers = new ArrayList<>();
        for (GTRecipe recipe : RecipeMaps.cannerRecipes.getAllRecipes()) {
            for (ItemStack output : recipe.mOutputs) {
                if (output == null || output.getItem() == null) continue;
                String name = Item.itemRegistry.getNameForObject(output.getItem());
                if (IC2_CELL.equals(name) || CREOSOTE_CELL.equals(name)) makers.add(describe(output));
            }
        }
        helper.assertTrue(makers.isEmpty(), "Canner recipes still make foreign cells: " + makers);
        helper.succeed();
    }

    private static void expectSameFluid(List<String> mismatches, ItemStack saved) {
        FluidStack held = FluidContainerRegistry.getFluidForFilledItem(saved);
        ItemStack loaded = load(saved);
        FluidStack loadedFluid = loaded == null ? null : FluidContainerRegistry.getFluidForFilledItem(loaded);
        String name = loaded == null ? null : Item.itemRegistry.getNameForObject(loaded.getItem());
        if (held == null || loadedFluid == null
            || !held.getFluid()
                .getName()
                .equals(
                    loadedFluid.getFluid()
                        .getName())
            || IC2_CELL.equals(name)
            || CREOSOTE_CELL.equals(name)) {
            mismatches.add(describe(saved) + " -> " + describe(loaded));
        }
    }

    private static ItemStack load(ItemStack saved) {
        return ItemStack.loadItemStackFromNBT(saved.writeToNBT(new NBTTagCompound()));
    }

    private static String describe(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "null";
        return Item.itemRegistry.getNameForObject(stack.getItem()) + "@" + stack.getItemDamage();
    }
}
