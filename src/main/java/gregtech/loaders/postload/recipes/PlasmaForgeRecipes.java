package gregtech.loaders.postload.recipes;

import static goodgenerator.loader.Loaders.huiCircuit;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.plasmaForgeRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class PlasmaForgeRecipes implements Runnable {

    @Override
    public void run() {
        // Dimensionally transcendent plasma forge recipes.
        // Ordered so that recipes using higher tier catalysts are prioritized.

        {
            // Dimensionally Shifted Superfluid

            Fluid celestialTungstenPlasma = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma();

            // Tier 5
            // Best recipe, unlocks with Stellar Catalyst.
            // Quadruples the cost of everything except for Metastable and Celestial, which are only doubled,
            // but gives 4x the output.
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.StableBaryonicMatter.getFluid(8_000),
                    GGMaterial.metastableOganesson.getMolten(4 * INGOTS),
                    Materials.Grade8PurifiedWater.getFluid(12_800),
                    new FluidStack(celestialTungstenPlasma, 1 * STACKS + 32 * INGOTS),
                    Materials.RadoxHeavy.getFluid(32_000),
                    Materials.ExcitedDTSC.getFluid(2_000))
                .fluidOutputs(Materials.DimensionallyShiftedSuperfluid.getFluid(360_000), Materials.DTR.getFluid(4_000))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut((int) TierEU.RECIPE_UXV)
                .metadata(COIL_HEAT, 13500)
                .addTo(plasmaForgeRecipes);

            // Tier 4
            // Better recipe, unlocks with Eternal coil.
            // Doubles the cost across the board, but outputs 3x more.
            // Switches to Heavy Radox, which can be mass-produced with the QFT.
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.StableBaryonicMatter.getFluid(2_000),
                    GGMaterial.metastableOganesson.getMolten(2 * INGOTS),
                    Materials.Grade8PurifiedWater.getFluid(3_200),
                    new FluidStack(celestialTungstenPlasma, 48 * INGOTS),
                    Materials.RadoxHeavy.getFluid(4_000),
                    Materials.ExcitedDTEC.getFluid(2_000))
                .fluidOutputs(Materials.DimensionallyShiftedSuperfluid.getFluid(90_000), Materials.DTR.getFluid(2_000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 13500)
                .addTo(plasmaForgeRecipes);

            // Tier 3
            // Better recipe, unlocks with Hypogen coil.
            // This recipe takes UMV power but processes 4x input and output as the original recipe, making it a free
            // POC
            // over the original recipe. Only increases the cost of baryonic and water, making the ratio much cheaper.
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.StableBaryonicMatter.getFluid(1_000),
                    GGMaterial.metastableOganesson.getMolten(1 * INGOTS),
                    Materials.Grade8PurifiedWater.getFluid(1_600),
                    new FluidStack(celestialTungstenPlasma, 24 * INGOTS),
                    Materials.RadoxSuperHeavy.getFluid(2_000),
                    Materials.ExcitedDTRC.getFluid(2_000))
                .fluidOutputs(Materials.DimensionallyShiftedSuperfluid.getFluid(30_000), Materials.DTR.getFluid(1_000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);

            // Tier 2
            // First recipe using AwDr coil and super heavy radox
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    Materials.StableBaryonicMatter.getFluid(250),
                    GGMaterial.metastableOganesson.getMolten(1 * INGOTS),
                    Materials.Grade8PurifiedWater.getFluid(400),
                    new FluidStack(celestialTungstenPlasma, 24 * INGOTS),
                    Materials.RadoxSuperHeavy.getFluid(2_000),
                    Materials.ExcitedDTPC.getFluid(1_000))
                .fluidOutputs(Materials.DimensionallyShiftedSuperfluid.getFluid(7_500), Materials.DTR.getFluid(250))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_UIV)
                .metadata(COIL_HEAT, 10800)
                .addTo(plasmaForgeRecipes);
        }

        // Giga chad trophy.
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Field_Generator_UEV.get(64),
                ItemList.Field_Generator_UIV.get(64),
                ItemList.Field_Generator_UMV.get(64))
            .fluidInputs(Materials.ExcitedDTEC.getFluid(100_000_000), Materials.SpaceTime.getMolten(18 * STACKS))
            .itemOutputs(ItemList.GigaChad.get(1))
            .duration(86400 * 20 * 2)
            .eut(2_000_000_000)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);

        // Quantum anomaly recipe bypass for UEV+. Avoids RNG.
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ModItems.itemStandarParticleBase, 1, 24),
                getModItem(NewHorizonsCoreMod.ID, "ChromaticLens", 0),
                new ItemStack(huiCircuit, 0, 4))
            .fluidInputs(Materials.ExcitedDTRC.getFluid(92), Materials.Duranium.getMolten(1 * INGOTS))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .fluidOutputs(Materials.DTR.getFluid(46))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 10800)
            .addTo(plasmaForgeRecipes);

        if (Avaritia.isModLoaded()) {
            // Six-Phased Copper
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Avaritia.ID, "Singularity", 8, 5))
                .fluidInputs(
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(1 * STACKS + 8 * INGOTS),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(4 * STACKS + 32 * INGOTS),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(36 * INGOTS),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(9 * STACKS),
                    MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(18 * INGOTS),
                    Materials.Mellion.getMolten(1 * STACKS + 8 * INGOTS))
                .fluidOutputs(Materials.SixPhasedCopper.getMolten(1 * STACKS + 8 * INGOTS))
                .duration(60 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);
        }
    }
}
