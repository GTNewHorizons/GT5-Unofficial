package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;

public class PlasmaForgeRecipes implements Runnable {

    @Override
    public void run() {
        // Giga chad trophy.
        GT_Values.RA.addPlasmaForgeRecipe(
                new ItemStack[] { ItemList.Field_Generator_UEV.get(64), ItemList.Field_Generator_UIV.get(64),
                        ItemList.Field_Generator_UMV.get(64) },
                new FluidStack[] { Materials.ExcitedDTEC.getFluid(100_000_000),
                        Materials.SpaceTime.getMolten(64 * 2 * 9 * 144) },
                new ItemStack[] { ItemList.GigaChad.get(1) },
                new FluidStack[] { GT_Values.NF },
                86400 * 20 * 2,
                2_000_000_000,
                13500);

        // Quantum anomaly recipe bypass for UXV. Avoids RNG.
        GT_Values.RA.addPlasmaForgeRecipe(
                new ItemStack[] { getModItem(MOD_ID_DC, "item.ChromaticLens", 1),
                        getModItem("GoodGenerator", "huiCircuit", 1, 4) },
                new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(144), getFluidStack("molten.shirabon", 72),
                        Materials.BlackDwarfMatter.getMolten(144) },
                new ItemStack[] { getModItem(MOD_ID_GTPP, "MU-metaitem.01", 1, 32105) },
                new FluidStack[] { NF },
                50 * 20,
                (int) TierEU.RECIPE_UXV,
                13_500);
    }
}
