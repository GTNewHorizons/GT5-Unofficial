package gregtech.loaders.postload.recipes;

import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

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
                MaterialsUEVplus.ExcitedDTEC.getFluid(100_000_000),
                MaterialsUEVplus.SpaceTime.getMolten(64 * 2 * 9 * 144))
            .itemOutputs(ItemList.GigaChad.get(1))
            .duration(86400 * 20 * 2)
            .eut(2_000_000_000)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);

        // Quantum anomaly recipe bypass for UEV+. Avoids RNG.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ModItems.itemStandarParticleBase, 1, 24),
                getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 0),
                new ItemStack(huiCircuit, 0, 4))
            .fluidInputs(MaterialsUEVplus.ExcitedDTRC.getFluid(92), Materials.Duranium.getMolten(144))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(46))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 10800)
            .addTo(plasmaForgeRecipes);
    }
}
