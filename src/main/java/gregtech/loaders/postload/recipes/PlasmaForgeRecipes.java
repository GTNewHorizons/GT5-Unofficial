package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.material.ELEMENT;

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
                getModItem(GTPlusPlus.ID, "particleBase", 1, 24),
                getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 0),
                getModItem(GoodGenerator.ID, "huiCircuit", 0, 4))
            .fluidInputs(MaterialsUEVplus.ExcitedDTRC.getFluid(92), Materials.Duranium.getMolten(144))
            .itemOutputs(getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1, 32105))
            .fluidOutputs(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(46))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 10800)
            .addTo(plasmaForgeRecipes);

        if (Avaritia.isModLoaded()) {
            // Six-Phased Copper
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(Avaritia.ID, "Singularity", 8, 5))
                .fluidInputs(
                    ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(72 * 144),
                    ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(4 * 72 * 144),
                    ELEMENT.STANDALONE.HYPOGEN.getFluidStack(36 * 144),
                    ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(8 * 72 * 144),
                    ELEMENT.STANDALONE.RHUGNOR.getFluidStack(18 * 144),
                    MaterialsUEVplus.Mellion.getMolten(72 * 144))
                .fluidOutputs(MaterialsUEVplus.SixPhasedCopper.getMolten(72 * 144L))
                .duration(60 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);
        }
    }
}
