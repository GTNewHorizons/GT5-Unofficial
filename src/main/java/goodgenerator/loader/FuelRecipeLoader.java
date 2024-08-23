package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.naquadahReactorFuels;
import static goodgenerator.main.GG_Config_Loader.NaquadahFuelTime;
import static goodgenerator.main.GG_Config_Loader.NaquadahFuelVoltage;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.LNG_BASIC_OUTPUT;
import static gregtech.api.util.GT_RecipeConstants.NFR_COIL_TIER;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CHRONOMATIC_GLASS;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.ELEMENT;

public class FuelRecipeLoader {

    public static void RegisterFuel() {
        FluidStack[] inputs = new FluidStack[] { MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(1),
            MyMaterial.thoriumBasedLiquidFuelExcited.getFluidOrGas(1),
            MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(1), MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1), MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(1), MyMaterial.naquadahBasedFuelMkVI.getFluidOrGas(1) };

        FluidStack[] outputs = new FluidStack[] { MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1),
            MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1),
            MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
            MyMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };
        for (int i = 0; i < 9; i++) {
            GT_Values.RA.stdBuilder()
                .fluidInputs(inputs[i])
                .fluidOutputs(outputs[i])
                .duration(NaquadahFuelTime[i])
                .eut(0)
                .metadata(LNG_BASIC_OUTPUT, NaquadahFuelVoltage[i])
                .addTo(naquadahReactorFuels);
        }

        // MK III Naquadah Fuel
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 27),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                MyMaterial.lightNaquadahFuel.getFluidOrGas(1000))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(100))
            .duration(5 * SECONDS)
            .eut(1_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternative higher tier recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 8),
                CHRONOMATIC_GLASS.getDust(9),
                ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1))
            .fluidInputs(
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                MyMaterial.lightNaquadahFuel.getFluidOrGas(1000))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(200))
            .duration(5 * SECONDS)
            .eut(2_100_000)
            .metadata(NFR_COIL_TIER, 1)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK IV Naquadah Fuel
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                MyMaterial.orundum.get(OrePrefixes.dust, 32))
            .fluidInputs(MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000), Materials.Praseodymium.getMolten(9216L))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250))
            .duration(8 * SECONDS)
            .eut(46_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                MyMaterial.orundum.get(OrePrefixes.dust, 64))
            .fluidInputs(
                MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000),
                new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 720))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(500))
            .duration(8 * SECONDS)
            .eut(75_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // One-step recipe to allow easier scaling for MK VI
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 54),
                MyMaterial.orundum.get(OrePrefixes.dust, 32),
                ItemRefer.High_Density_Uranium.get(10),
                ItemRefer.High_Density_Plutonium.get(5))
            .fluidInputs(
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(4000),
                MyMaterial.lightNaquadahFuel.getFluidOrGas(5000),
                new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 360),
                new FluidStack(FluidRegistry.getFluid("molten.chromaticglass"), 6480))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250))
            .duration(10 * TICKS)
            .eut(350_000_000)
            .metadata(NFR_COIL_TIER, 4)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK V Naquadah Fuel
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 8),
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 32))
            .fluidInputs(
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("heavyradox", 250))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(500))
            .duration(10 * SECONDS)
            .eut(100_000_000)
            .metadata(NFR_COIL_TIER, 2)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.SpaceTime, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.TranscendentMetal, 16),
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 48))
            .fluidInputs(
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("heavyradox", 250))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(750))
            .duration(5 * SECONDS)
            .eut(300_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // MK VI Naquadah Fuel
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ELEMENT.STANDALONE.ASTRAL_TITANIUM.getDust(64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 32))
            .fluidInputs(
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("molten.shirabon", 360))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkVI.getFluidOrGas(500))
            .duration(12 * SECONDS)
            .eut(320_000_000)
            .metadata(NFR_COIL_TIER, 3)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);

        // Alternate higher tier recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.WhiteDwarfMatter, 4),
                ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getDust(64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 48))
            .fluidInputs(
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(2000),
                FluidRegistry.getFluidStack("molten.shirabon", 1440))
            .fluidOutputs(MyMaterial.naquadahBasedFuelMkVI.getFluidOrGas(750))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .metadata(NFR_COIL_TIER, 4)
            .noOptimize()
            .addTo(naquadahFuelRefineFactoryRecipes);
    }
}
