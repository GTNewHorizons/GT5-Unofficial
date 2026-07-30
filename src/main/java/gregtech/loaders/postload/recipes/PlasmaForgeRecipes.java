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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class PlasmaForgeRecipes implements Runnable {

    @Override
    public void run() {
        // Dimensionally transcendent plasma forge recipes.
        // Ordered so that recipes using higher tier catalysts are prioritized.

        {
            // Dimensionally Shifted Superfluid

            Fluid celestialTungstenPlasma = MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten);

            // Tier 5
            // Best recipe, unlocks with Stellar Catalyst.
            // Quadruples the cost of everything except for Metastable and Celestial, which are only doubled,
            // but gives 4x the output.
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, 8_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (4 * INGOTS)),
                    MaterialLibAPI
                        .getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (12_800)),
                    new FluidStack(celestialTungstenPlasma, 1 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.HeavyRadox, FluidShapes.fluidLiquid, 32_000),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, (int) (2_000)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 360_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 4_000))
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
                    MaterialLibAPI.getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, 2_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (2 * INGOTS)),
                    MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (3_200)),
                    new FluidStack(celestialTungstenPlasma, 48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.HeavyRadox, FluidShapes.fluidLiquid, 4_000),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, (int) (2_000)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 90_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 2_000))
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
                    MaterialLibAPI.getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                    MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (1_600)),
                    new FluidStack(celestialTungstenPlasma, 24 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.SuperHeavyRadox, FluidShapes.fluidLiquid, 2_000),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, (int) (2_000)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 30_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1_000))
                .duration(30 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);

            // Tier 2
            // First recipe using AwDr coil and super heavy radox
            GTValues.RA.stdBuilder()
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.stablebaryonicmatter, FluidShapes.fluidLiquid, 250),
                    MaterialLibAPI
                        .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                    MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, (int) (400)),
                    new FluidStack(celestialTungstenPlasma, 24 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.SuperHeavyRadox, FluidShapes.fluidLiquid, 2_000),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTPC, FluidShapes.fluidLiquid, (int) (1_000)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 7_500),
                    MaterialLibAPI
                        .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 250))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, (int) (100_000_000)),
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (18 * STACKS)))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, (int) (92)),
                MaterialLibAPI.getFluidStack(Materials.Duranium, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 46))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .metadata(COIL_HEAT, 10800)
            .addTo(plasmaForgeRecipes);

        if (Avaritia.isModLoaded()) {
            // Six-Phased Copper
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Avaritia.ID, "Singularity", 8, 5))
                .fluidInputs(
                    MaterialUtils.anyFluid(Materials.CelestialTungsten, 1 * STACKS + 8 * INGOTS),
                    MaterialUtils.anyFluid(Materials.AstralTitanium, 4 * STACKS + 32 * INGOTS),
                    MaterialUtils.anyFluid(Materials.Hypogen, 36 * INGOTS),
                    MaterialUtils.anyFluid(Materials.ChromaticGlass, 9 * STACKS),
                    MaterialUtils.anyFluid(Materials.Rhugnor, 18 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Mellion, FluidShapes.fluidMolten, (int) (1 * STACKS + 8 * INGOTS)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SixPhasedCopper,
                        FluidShapes.fluidMolten,
                        (int) (1 * STACKS + 8 * INGOTS)))
                .duration(60 * SECONDS)
                .eut((int) TierEU.RECIPE_UMV)
                .metadata(COIL_HEAT, 12600)
                .addTo(plasmaForgeRecipes);
        }

        // Chipped Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                // this is very gross
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Magmatter, 1)),
                MaterialLibAPI.getStack(Materials.Eternity, Shapes.round, (int) (64)),
                MaterialLibAPI.getStack(Materials.Magmatter, Shapes.round, 64),
                MaterialLibAPI.getStack(Materials.Hexanite, Shapes.round, (int) (64)),
                MaterialLibAPI.getStack(Materials.Ruby, Shapes.gemExquisite, (int) (64)),
                MaterialLibAPI.getStack(Materials.Jasper, Shapes.gemExquisite, (int) (64)),
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.gemExquisite, (int) (64)),
                MaterialLibAPI.getStack(Materials.Opal, Shapes.gemExquisite, (int) (64)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.sgcrystalslurry, FluidShapes.fluidLiquid, 5_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, (int) (72_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemChipped, (int) (64)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 144_000))
            .duration(420 * SECONDS)
            .eut((int) TierEU.RECIPE_MAX)
            .metadata(COIL_HEAT, 13500)
            .addTo(plasmaForgeRecipes);

    }
}
