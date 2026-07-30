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
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;

public class FuelRecipeLoader {

    public static void RegisterFuel() {
        FluidStack[] inputs = new FluidStack[] {
            MaterialLibAPI
                .getFluidStack(Materials.UraniumBasedLiquidFuelExcitedState, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.ThoriumBasedLiquidFuelExcitedState, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.PlutoniumBasedLiquidFuelExcitedState, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkI, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkII, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkIII, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkV, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.NaquadahBasedLiquidFuelMkVI, FluidShapes.fluidLiquid, (int) (1)) };

        FluidStack[] outputs = new FluidStack[] {
            MaterialLibAPI.getFluidStack(Materials.UraniumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI.getFluidStack(Materials.ThoriumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.PlutoniumBasedLiquidFuelDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIIDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIIIDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIVDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkVDepleted, FluidShapes.fluidLiquid, (int) (1)),
            MaterialLibAPI
                .getFluidStack(Materials.NaquadahBasedLiquidFuelMkVIDepleted, FluidShapes.fluidLiquid, (int) (1)) };
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
                MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 27),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (800)),
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIII, FluidShapes.fluidLiquid, (int) (100)))
            .duration(5 * SECONDS)
            .eut(1_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternative higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.dust, 9),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (800)),
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIII, FluidShapes.fluidLiquid, (int) (200)))
            .duration(5 * SECONDS)
            .eut(2_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK IV Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 27),
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.dust, 32))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIII, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Praseodymium, FluidShapes.fluidMolten, 1 * STACKS))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (250)))
            .duration(8 * SECONDS)
            .eut(46_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 27),
                MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.dust, 64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIII, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialUtils.legacyGtppFluid(Materials.Hypogen, 240))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (500)))
            .duration(8 * SECONDS)
            .eut(75_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // One-step recipe to allow easier scaling for MK VI
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ExtremelyUnstableNaquadah, Shapes.dust, 54),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.dust, 32),
                ItemRefer.High_Density_Uranium.get(10),
                ItemRefer.High_Density_Plutonium.get(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HeavyNaquadahFuel, FluidShapes.fluidLiquid, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials.LightNaquadahFuel, FluidShapes.fluidLiquid, (int) (5_000)),
                MaterialUtils.legacyGtppFluid(Materials.Hypogen, 120),
                MaterialUtils.legacyGtppFluid(Materials.ChromaticGlass, 45 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (250)))
            .duration(2 * TICKS)
            .eut(350_000_000)
            .metadata(NFR_COIL_TIER, 4)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK V Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.dust, 32))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialUtils.fluid(Materials.HeavyRadox, 250))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkV, FluidShapes.fluidLiquid, (int) (500)))
            .duration(10 * SECONDS)
            .eut(100_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.dust, 48))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkIV, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialUtils.fluid(Materials.HeavyRadox, 250),
                MaterialLibAPI.getFluidStack(Materials.Mellion, FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidMolten, 2 * INGOTS))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkV, FluidShapes.fluidLiquid, (int) (750)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(300_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK VI Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 32))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkV, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (2 * INGOTS + 1 * HALF_INGOTS)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkVI, FluidShapes.fluidLiquid, (int) (750)))
            .duration(12 * SECONDS)
            .eut(320_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 48))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkV, FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (2 * INGOTS + 1 * HALF_INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 60))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NaquadahBasedLiquidFuelMkVI, FluidShapes.fluidLiquid, (int) (1_250)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .metadata(NFR_COIL_TIER, 4)
            .addTo(naquadahFuelRefineFactoryRecipes);
    }
}
