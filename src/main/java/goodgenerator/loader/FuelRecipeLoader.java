package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahReactorFuels;
import static goodgenerator.main.GGConfigLoader.NaquadahFuelTime;
import static goodgenerator.main.GGConfigLoader.NaquadahFuelVoltage;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.LNG_BASIC_OUTPUT;
import static gregtech.api.util.GTRecipeConstants.NFR_COIL_TIER;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CHRONOMATIC_GLASS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

public class FuelRecipeLoader {

    public static void RegisterFuel() {
        FluidStack[] inputs = new FluidStack[] { GGMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(1),
            GGMaterial.thoriumBasedLiquidFuelExcited.getFluidOrGas(1),
            GGMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkI.getFluidOrGas(1), GGMaterial.naquadahBasedFuelMkII.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1), GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(1), GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(1) };

        FluidStack[] outputs = new FluidStack[] { GGMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1),
            GGMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1),
            GGMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
            GGMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };
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
                GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 27),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                GGMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                GGMaterial.lightNaquadahFuel.getFluidOrGas(1000))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(100))
            .duration(5 * SECONDS)
            .eut(1_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternative higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 8),
                CHRONOMATIC_GLASS.getDust(9),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                GGMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                GGMaterial.lightNaquadahFuel.getFluidOrGas(1000))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(200))
            .duration(5 * SECONDS)
            .eut(2_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK IV Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                GGMaterial.orundum.get(OrePrefixes.dust, 32))
            .fluidInputs(GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000), Materials.Praseodymium.getMolten(9216L))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250))
            .duration(8 * SECONDS)
            .eut(46_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                GGMaterial.orundum.get(OrePrefixes.dust, 64))
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000),
                new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 720))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(500))
            .duration(8 * SECONDS)
            .eut(75_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // One-step recipe to allow easier scaling for MK VI
        GTValues.RA.stdBuilder()
            .itemInputs(
                GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 54),
                GGMaterial.orundum.get(OrePrefixes.dust, 32),
                ItemRefer.High_Density_Uranium.get(10),
                ItemRefer.High_Density_Plutonium.get(5))
            .fluidInputs(
                GGMaterial.heavyNaquadahFuel.getFluidOrGas(4000),
                GGMaterial.lightNaquadahFuel.getFluidOrGas(5000),
                new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 360),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 6480))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250))
            .duration(10 * TICKS)
            .eut(350_000_000)
            .metadata(NFR_COIL_TIER, 4)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK V Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 8),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 32))
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("heavyradox", 250))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(100_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.SpaceTime, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.TranscendentMetal, 16),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 48))
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("heavyradox", 250))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(750))
            .duration(5 * SECONDS)
            .eut(300_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK VI Naquadah Fuel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 32))
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("molten.shirabon", 360))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(500))
            .duration(12 * SECONDS)
            .eut(320_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.WhiteDwarfMatter, 4),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 48))
            .fluidInputs(
                GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("molten.shirabon", 1440))
            .fluidOutputs(GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(750))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .metadata(NFR_COIL_TIER, 4)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);
    }
}
