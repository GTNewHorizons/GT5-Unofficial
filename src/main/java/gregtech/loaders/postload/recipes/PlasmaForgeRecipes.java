package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPlasmaForgeRecipes;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import gregtech.api.enums.*;

public class PlasmaForgeRecipes implements Runnable {

    @Override
    public void run() {
        // Giga chad trophy.
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            ItemList.Field_Generator_UEV.get(64),
                            ItemList.Field_Generator_UIV.get(64),
                            ItemList.Field_Generator_UMV.get(64))
                    .fluidInputs(
                            Materials.ExcitedDTEC.getFluid(100_000_000),
                            Materials.SpaceTime.getMolten(64 * 2 * 9 * 144))
                    .itemOutputs(ItemList.GigaChad.get(1))
                    .noFluidOutputs()
                    .duration(86400 * 20 * 2)
                    .eut(2_000_000_000)
                    .metadata(COIL_HEAT, 13500)
                    .addTo(sPlasmaForgeRecipes);

        // Quantum anomaly recipe bypass for UXV. Avoids RNG.
        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 1),
                            getModItem(GoodGenerator.ID, "huiCircuit", 1, 4))
                    .fluidInputs(
                            Materials.WhiteDwarfMatter.getMolten(144),
                            getFluidStack("molten.shirabon", 72),
                            Materials.BlackDwarfMatter.getMolten(144))
                    .itemOutputs(getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1, 32105))
                    .noFluidOutputs()
                    .duration(50 * 20)
                    .eut((int) TierEU.RECIPE_UXV)
                    .metadata(COIL_HEAT, 13500)
                    .addTo(sPlasmaForgeRecipes);
    }
}
