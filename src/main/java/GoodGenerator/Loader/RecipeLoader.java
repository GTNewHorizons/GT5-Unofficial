package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.util.MaterialFix;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.util.GT_Utility;

public class RecipeLoader {
    public static void RecipeLoad(){

        //Radiation Protection Plate
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 16L),
                                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.NaquadahAlloy,8L),
                                GT_OreDictUnificator.get(OrePrefixes.foil,Materials.Neutronium,1L),
                                GT_Utility.getIntegratedCircuit(1)},
                Materials.Lead.getMolten(1152),
                new ItemStack(Loaders.radiationProtectionPlate),
                400,
                1920
        );

        //LNR Controller
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Generator_Naquadah_Mark_V.get(1).copy(),
                100000,
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium,8),
                        new ItemStack(Loaders.radiationProtectionPlate,16),
                        ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.circuit,Materials.Infinite,2),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08,Materials.Tungsten,8),
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge,Materials.Lead,4),
                        GT_OreDictUnificator.get(OrePrefixes.plate,Materials.NaquadahAlloy,8),
                        GT_OreDictUnificator.get(OrePrefixes.screw,Materials.Osmium,16)
                },
                new FluidStack[]{
                        Materials.Trinium.getMolten(576),
                        Materials.SolderingAlloy.getMolten(4608),
                        Materials.Lubricant.getFluid(8000)
                },
                Loaders.MAR.copy(),
                16200,
                122880
        );

        //LNR Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        new ItemStack(Loaders.radiationProtectionPlate,6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.Europium,1),
                        ItemList.Field_Generator_MV.get(1),
                        GT_Utility.getIntegratedCircuit(1)
                },
                null,
                new ItemStack(Loaders.MAR_Casing),
                400,
                1920
        );

        //LNR Frame
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.stickLong,Materials.NaquadahAlloy,8),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.HSSE,4),
                        GT_Utility.getIntegratedCircuit(24)
                },
                null,
                new ItemStack(Loaders.radiationProtectionSteelFrame),
                320,
                1920
        );

        //Uranium Liquid Fuel Process Line
        GT_Values.RA.addAssemblerRecipe(
               new ItemStack[]{
                        MyMaterial.graphiteUraniumMixture.get(OrePrefixes.dust,4),
                        GT_OreDictUnificator.get(OrePrefixes.foil,Materials.TungstenCarbide,16),
                        GT_Utility.getIntegratedCircuit(1)
               },
                null,
                new ItemStack(Loaders.wrappedUraniumIngot),
                1400,
                480
        );

        GT_Values.RA.addImplosionRecipe(
                new ItemStack(Loaders.wrappedUraniumIngot, 4),
                8,
                new ItemStack(Loaders.highDensityUraniumNugget),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.TungstenCarbide,8)
        );

        GT_Values.RA.addCompressorRecipe(
                new ItemStack(Loaders.highDensityUraniumNugget,9),
                new ItemStack(Loaders.highDensityUranium),
                600,
                480
        );

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Loaders.highDensityUranium),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rubidium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium,4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Radon.getGas(1000L),
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(1000),
                null,
                200,
                15360
        );

        GT_Values.RA.addFusionReactorRecipe(
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(10),
                Materials.Hydrogen.getGas(100L),
                MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(10),
                40,
                7680,
                200000000
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(10),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead,16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth,1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barium,6L),
                null,null,null,
                new int[]{6000,1000,5000},
                1000,1040
        );

        //Thorium Liquid Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        MyMaterial.uraniumCarbideThoriumMixture.get(OrePrefixes.dust,64),
                        GT_OreDictUnificator.get(OrePrefixes.foil,Materials.TungstenSteel,4),
                        GT_Utility.getIntegratedCircuit(1)
                },
                null,
                new ItemStack(Loaders.wrappedThoriumIngot),
                1000,
                480
        );

        GT_Values.RA.addImplosionRecipe(
                new ItemStack(Loaders.wrappedThoriumIngot),
                4,
                new ItemStack(Loaders.highDensityThoriumNugget),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.TungstenSteel,3)
        );

        GT_Values.RA.addCompressorRecipe(
                new ItemStack(Loaders.highDensityThoriumNugget,9),
                new ItemStack(Loaders.highDensityThorium),
                400,
                120
        );

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Loaders.highDensityThorium),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lithium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Draconium,2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Mercury.getFluid(1000L),
                MyMaterial.thoriumBasedLiquidFuel.getFluidOrGas(4000),
                null,
                3000,
                240
        );

        GT_Values.RA.addMixerRecipe(
                MyMaterial.thoriumBasedLiquidFuel.get(OrePrefixes.cell,1),
                GT_Utility.getIntegratedCircuit(1),
                null,null,
                Materials.Helium.getPlasma(750L),
                null,
                MyMaterial.thoriumBasedLiquidFuelExcited.get(OrePrefixes.cell,1),
                120,
                3840
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),null,
                MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Praseodymium,1),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Boron,2),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Indium,4),
                new int[]{9000,7000,1500,800,3000,5000},
                1500,
                1040
        );

        //Liquid Plutonium Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        MyMaterial.plutoniumOxideUraniumMixture.get(OrePrefixes.dust,8),
                        GT_OreDictUnificator.get(OrePrefixes.foil,Materials.HSSS,16),
                        GT_Utility.getIntegratedCircuit(1)
                },
                null,
                new ItemStack(Loaders.wrappedPlutoniumIngot),
                1800,
                2040
        );

        GT_Values.RA.addImplosionRecipe(
                new ItemStack(Loaders.wrappedPlutoniumIngot,2),
                16,
                new ItemStack(Loaders.highDensityPlutoniumNugget),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.HSSS,8)
        );

        GT_Values.RA.addCompressorRecipe(
                new ItemStack(Loaders.highDensityPlutoniumNugget,9),
                new ItemStack(Loaders.highDensityPlutonium),
                1200,
                120
        );

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Loaders.highDensityPlutonium),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Neutronium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Caesium,16),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Americium,2),
                GT_Utility.getIntegratedCircuit(1),
                null,null,
                MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(1000),
                null,
                360,
                30720
        );

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lutetium.getMolten(16),
                MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(20),
                MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(20),
                20,
                15360,
                220000000
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),null,
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Krypton.getFluidOrGas(144),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Tritanium,9),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Cerium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Gold,2),
                null,null,null,
                new int[]{5000,8000,7500},
                2500,
                7680
        );

        //Th-233
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Sugar,16),
                        MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust,0),
                        GT_Utility.getIntegratedCircuit(1)
                },
                new FluidStack[]{
                        FluidRegistry.getFluidStack("nitricacid", 6000)
                },
                new FluidStack[]{
                        MyMaterial.oxalate.getFluidOrGas(3000),
                        Materials.NitricOxide.getGas(6000)
                },
                null,
                600,
                120
        );

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Thorium,1),
                null,
                Materials.Oxygen.getGas(2000),
                null,
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust,1),
                null,
                100,
                480,
                1200
        );

        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust,1),
                null,
                FluidRegistry.getFluidStack("nitricacid", 4000),
                MyMaterial.thoriumNitrate.getFluidOrGas(1000),
                null,
                40,
                30
        );

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        GT_Utility.getIntegratedCircuit(1)
                },
                new FluidStack[]{
                        MyMaterial.thoriumNitrate.getFluidOrGas(1000),
                        MyMaterial.oxalate.getFluidOrGas(4000)
                },
                new FluidStack[]{
                        Materials.NitrogenDioxide.getGas(4000),
                        Materials.CarbonDioxide.getGas(4000)
                },
                new ItemStack[]{
                        MyMaterial.thoriumOxalate.get(OrePrefixes.dust,1)
                },
                100,
                120
        );

        GT_Values.RA.addChemicalRecipe(
                MyMaterial.thoriumOxalate.get(OrePrefixes.dust,1),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.SodiumHydroxide,4),
                null,
                null,
                MyMaterial.thoriumHydroxide.get(OrePrefixes.dust,1),
                MyMaterial.sodiumOxalate.get(OrePrefixes.dust,2),
                40,
                120
        );

        GT_Values.RA.addChemicalRecipe(
                MyMaterial.sodiumOxalate.get(OrePrefixes.dust,1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydrochloricAcid.getFluid(2000),
                MyMaterial.oxalate.getFluidOrGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Salt,2),
                null,
                20,
                30
        );

        GT_Values.RA.addChemicalRecipe(
                MyMaterial.thoriumHydroxide.get(OrePrefixes.dust,1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydrochloricAcid.getFluid(4000),
                MyMaterial.thoriumTetrachloride.getFluidOrGas(1000),
                null,
                null,
                200,
                120
        );

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[]{
                        GT_Utility.getIntegratedCircuit(1),
                },
                new FluidStack[]{
                        MyMaterial.thoriumTetrachloride.getFluidOrGas(1000),
                        Materials.HydrofluoricAcid.getFluid(4000)
                },
                new FluidStack[]{
                        MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000),
                        Materials.HydrochloricAcid.getFluid(4000)
                },
                null,
                240,
                480
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000),
                MyMaterial.thorium232Tetrafluoride.getFluidOrGas(250),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall,Materials.Thorium,3),
                null,null,null,null,null,null,
                100,
                480
        );

        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.thorium232Tetrafluoride.getFluidOrGas(1000),
                Materials.Fluorine.getGas(4000),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust,1),
                null,
                100,
                120,
                1200
        );

        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(24),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Vanadium,2),
                Materials.Oxygen.getGas(5000),
                null,
                MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust,1),
                null,
                200,
                120,
                2500
        );

        //Atomic Separation Catalyst
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Shadow,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Sunnarium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ardite,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,9),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Shadow,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Sunnarium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Manyullyn,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,9),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Shadow,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ichorium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ardite,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,27),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Shadow,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ichorium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Manyullyn,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,27),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Bedrockium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Sunnarium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ardite,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,9),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Bedrockium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Sunnarium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Manyullyn,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,9),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Bedrockium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ichorium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ardite,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,27),
                300,
                480
        );

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Blaze,32),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Bedrockium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Ichorium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Manyullyn,4),
                GT_Utility.getIntegratedCircuit(4),null,
                Materials.Naquadah.getMolten(288),
                null,
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,27),
                300,
                480
        );

        GT_Values.RA.addFormingPressRecipe(
                WerkstoffLoader.Tiberium.get(OrePrefixes.plate,4),
                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.Silicon,8),
                MyMaterial.orundum.get(OrePrefixes.plate,1),
                400,
                3000
        );

        GT_Values.RA.addBlastRecipe(
                MyMaterial.orundum.get(OrePrefixes.plate,2),
                new ItemStack(Loaders.rawAtomicSeparationCatalyst,4),
                Materials.Plutonium.getMolten(144),
                null,
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot,1),
                null,
                3600,
                480,
                5000
        );

        GT_Values.RA.addVacuumFreezerRecipe(
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot,1),
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot,1),
                450,
                960
        );

        GT_Values.RA.addBlastRecipe(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust,1),
                GT_Utility.getIntegratedCircuit(1),
                null,null,
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot),
                null,
                8000,
                114514,
                7000
        );
    }

    public static void Fixer(){
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.atomicSeparationCatalyst);
    }
}
