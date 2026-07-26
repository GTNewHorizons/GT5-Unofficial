package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahReactorFuels;
import static goodgenerator.main.GGConfigLoader.NaquadahFuelTime;
import static goodgenerator.main.GGConfigLoader.NaquadahFuelVoltage;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.LNG_BASIC_OUTPUT;
import static gregtech.api.util.GTRecipeConstants.NFR_COIL_TIER;

import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MU;

public class FuelRecipeLoader {

    public static void RegisterFuel() {
        FluidStack[] inputs = new FluidStack[] {
            MaterialLibAPI.getFluidStack(
                Materials2Materials.UraniumBasedLiquidFuelExcitedState,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.ThoriumBasedLiquidFuelExcitedState,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.PlutoniumBasedLiquidFuelExcitedState,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkI,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkII,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIII,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkV,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkVI,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)) };

        FluidStack[] outputs = new FluidStack[] {
            MaterialLibAPI.getFluidStack(
                Materials2Materials.UraniumBasedLiquidFuelDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.ThoriumBasedLiquidFuelDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.PlutoniumBasedLiquidFuelDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIIDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIIIDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkIVDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkVDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)),
            MaterialLibAPI.getFluidStack(
                Materials2Materials.NaquadahBasedLiquidFuelMkVIDepleted,
                Materials2FluidShapes.fluidLiquid,
                (int) (1)) };
        for (int i = 0; i < 9; i++) {
            GTValues.RA.stdBuilder()
                .fluidInputs(inputs[i])
                .fluidOutputs(outputs[i])
                .duration(NaquadahFuelTime[i])
                .eut(0)
                .metadata(LNG_BASIC_OUTPUT, NaquadahFuelVoltage[i])
                .addTo(naquadahReactorFuels);
        }

        // MK III Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MU.stack(OrePrefixes.dust, Materials2Materials.ExtremelyUnstableNaquadah, 4),
                MU.stack(OrePrefixes.dust, Materials2Materials.Tiberium, 27),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HeavyNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (800)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LightNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIII,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(5 * SECONDS)
            .eut(1_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternative higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MU.stack(OrePrefixes.dust, Materials2Materials.ExtremelyUnstableNaquadah, 8),
                MaterialLibAPI.getStack(Materials2Materials.ChromaticGlass, Materials2Shapes.dust, 9),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HeavyNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (800)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LightNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIII,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (200)))
            .duration(5 * SECONDS)
            .eut(2_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK IV Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MU.stack(OrePrefixes.dust, Materials2Materials.ExtremelyUnstableNaquadah, 27),
                MaterialLibAPI.getStack(Materials2Materials.NetherStar, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.DraconiumAwakened, Materials2Shapes.dust, 64),
                MU.stack(OrePrefixes.dust, Materials2Materials.Orundum, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIII,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Praseodymium, Materials2FluidShapes.fluidMolten, 1 * STACKS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .duration(8 * SECONDS)
            .eut(46_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MU.stack(OrePrefixes.dust, Materials2Materials.ExtremelyUnstableNaquadah, 27),
                MaterialLibAPI.getStack(Materials2Materials.Bedrockium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.DraconiumAwakened, Materials2Shapes.dust, 64),
                MU.stack(OrePrefixes.dust, Materials2Materials.Orundum, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIII,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MU.legacyGtppFluid(Materials2Materials.Hypogen, 240))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (500)))
            .duration(8 * SECONDS)
            .eut(75_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // One-step recipe to allow easier scaling for MK VI
        GTValues.RA.stdBuilder()
            .itemInputs(
                MU.stack(OrePrefixes.dust, Materials2Materials.ExtremelyUnstableNaquadah, 54),
                MU.stack(OrePrefixes.dust, Materials2Materials.Orundum, 32),
                ItemRefer.High_Density_Uranium.get(10),
                ItemRefer.High_Density_Plutonium.get(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HeavyNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LightNaquadahFuel,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (5_000)),
                MU.legacyGtppFluid(Materials2Materials.Hypogen, 120),
                MU.legacyGtppFluid(Materials2Materials.ChromaticGlass, 45 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (250)))
            .duration(2 * TICKS)
            .eut(350_000_000)
            .metadata(NFR_COIL_TIER, 4)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK V Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.dust, 8),
                MU.stack(OrePrefixes.dust, Materials2Materials.AtomicSeparationCatalyst, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MU.fluid(Materials2Materials.HeavyRadox, 250))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (500)))
            .duration(10 * SECONDS)
            .eut(100_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.dust, 16),
                MU.stack(OrePrefixes.dust, Materials2Materials.AtomicSeparationCatalyst, 48))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkIV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MU.fluid(Materials2Materials.HeavyRadox, 250),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mellion, Materials2FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (750)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(300_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK VI Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.AstralTitanium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Tritanium, Materials2Shapes.dust, 32))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Shirabon,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * INGOTS + 1 * HALF_INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkVI,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (750)))
            .duration(12 * SECONDS)
            .eut(320_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CelestialTungsten, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Tritanium, Materials2Shapes.dust, 48))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkV,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Shirabon,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * INGOTS + 1 * HALF_INGOTS)),
                MaterialLibAPI.getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, 60))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NaquadahBasedLiquidFuelMkVI,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_250)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .metadata(NFR_COIL_TIER, 4)
            .addTo(naquadahFuelRefineFactoryRecipes);
    }
}
