package gregtech.loaders.postload.recipes;

import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class PlasmaForgeRecipes implements Runnable {

    @Override
    public void run() {
        // Giga chad trophy.
        GTValues.RA.stdBuilder()
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
        GTValues.RA.stdBuilder()
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

        if (Avaritia.isModLoaded()) {
            // Six-Phased Copper
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Avaritia.ID, "Singularity", 8, 5))
                .fluidInputs(
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(72 * 144),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(4 * 72 * 144),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(36 * 144),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(8 * 72 * 144),
                    MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(18 * 144),
                    MaterialsUEVplus.Mellion.getMolten(72 * 144))
                .fluidOutputs(MaterialsUEVplus.SixPhasedCopper.getMolten(72 * 144L))
                .duration(60 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);
        }

        Fluid celestialTungstenPlasma = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma();

        // Dimensionally shifted superfluid

        // First recipe using AwDr coil and super heavy radox
        GTValues.RA.stdBuilder()
            .itemInputs()
            .fluidInputs(
                Materials.StableBaryonicMatter.getFluid(250),
                GGMaterial.metastableOganesson.getMolten(144),
                Materials.Grade8PurifiedWater.getFluid(400),
                new FluidStack(celestialTungstenPlasma, 24 * 144),
                Materials.RadoxSuperHeavy.getFluid(2000),
                MaterialsUEVplus.ExcitedDTPC.getFluid(1000))
            .fluidOutputs(
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(7500),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(250))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .metadata(COIL_HEAT, 10800)
            .addTo(plasmaForgeRecipes);

        // Better recipe, unlocks with Hypogen coil.
        // This recipe takes UMV power but processes 4x input and output as the original recipe, making it a free POC
        // over the original recipe. Only increases the cost of baryonic and water, making the ratio much cheaper.
        GTValues.RA.stdBuilder()
            .itemInputs()
            .fluidInputs(
                Materials.StableBaryonicMatter.getFluid(1000),
                GGMaterial.metastableOganesson.getMolten(144),
                Materials.Grade8PurifiedWater.getFluid(1600),
                new FluidStack(celestialTungstenPlasma, 24 * 144),
                Materials.RadoxSuperHeavy.getFluid(2000),
                MaterialsUEVplus.ExcitedDTRC.getFluid(2000))
            .fluidOutputs(
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(30000),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1000))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .metadata(COIL_HEAT, 12600)
            .addTo(plasmaForgeRecipes);

        // Better recipe, unlocks with Eternal coil.
        // Doubles the cost across the board, but outputs 3x more.
        // Switches to Heavy Radox, which can be mass-produced with the QFT.
        GTValues.RA.stdBuilder()
            .itemInputs()
            .fluidInputs(
                Materials.StableBaryonicMatter.getFluid(2000),
                GGMaterial.metastableOganesson.getMolten(144 * 2),
                Materials.Grade8PurifiedWater.getFluid(3200),
                new FluidStack(celestialTungstenPlasma, 48 * 144),
                Materials.RadoxHeavy.getFluid(4000),
                MaterialsUEVplus.ExcitedDTEC.getFluid(2000))
            .fluidOutputs(
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(90000),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2000))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);

        // Best recipe, unlocks with Stellar Catalyst.
        // Quadruples the cost of everything except for Metastable and Celestial, which are only doubled,
        // but gives 4x the output.
        GTValues.RA.stdBuilder()
            .itemInputs()
            .fluidInputs(
                Materials.StableBaryonicMatter.getFluid(8000),
                GGMaterial.metastableOganesson.getMolten(144 * 4),
                Materials.Grade8PurifiedWater.getFluid(12800),
                new FluidStack(celestialTungstenPlasma, 96 * 144),
                Materials.RadoxHeavy.getFluid(32000),
                MaterialsUEVplus.ExcitedDTSC.getFluid(2000))
            .fluidOutputs(
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(360000),
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(4000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut((int) TierEU.RECIPE_UXV)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);
    }
}
