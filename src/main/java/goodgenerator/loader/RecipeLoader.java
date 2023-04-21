package goodgenerator.loader;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.crossmod.LoadedList;
import goodgenerator.items.MyMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MaterialFix;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class RecipeLoader {

    public static void RecipeLoad() {

        // Radiation Protection Plate
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Lead.getMolten(1152),
                ItemRefer.Radiation_Protection_Plate.get(1),
                400,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lanthanum, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Lead.getMolten(1152),
                ItemRefer.Radiation_Protection_Plate.get(1),
                400,
                1920);

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // LNR Controller
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Generator_Naquadah_Mark_III.get(1).copy(),
                100000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 8),
                        ItemRefer.Radiation_Protection_Plate.get(16), ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(8),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorLuV, 8),
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Naquadah, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 16) },
                new FluidStack[] { Materials.Trinium.getMolten(576), new FluidStack(solderIndalloy, 4608),
                        Materials.Lubricant.getFluid(8000) },
                ItemRefer.Large_Naquadah_Reactor.get(1),
                16200,
                122880);

        // LNR Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Radiation_Protection_Plate.get(6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                        ItemList.Field_Generator_MV.get(1), GT_Utility.getIntegratedCircuit(1) },
                null,
                ItemRefer.Field_Restriction_Casing.get(1),
                400,
                1920);

        // LNR Frame
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 4),
                        GT_Utility.getIntegratedCircuit(24) },
                null,
                ItemRefer.Radiation_Proof_Steel_Frame_Box.get(1),
                320,
                1920);

        // Uranium Liquid Fuel Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { MyMaterial.graphiteUraniumMixture.get(OrePrefixes.dust, 4),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.TungstenCarbide, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                ItemRefer.Wrapped_Uranium_Ingot.get(1),
                1400,
                480);

        GT_Values.RA.addImplosionRecipe(
                ItemRefer.Wrapped_Uranium_Ingot.get(4),
                8,
                ItemRefer.High_Density_Uranium_Nugget.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenCarbide, 8));

        GT_Values.RA.addCompressorRecipe(
                ItemRefer.High_Density_Uranium_Nugget.get(9),
                ItemRefer.High_Density_Uranium.get(1),
                600,
                480);

        GT_Values.RA.addMixerRecipe(
                ItemRefer.High_Density_Uranium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Radon.getGas(1000L),
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(1000),
                null,
                200,
                15360);

        GT_Values.RA.addFusionReactorRecipe(
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(10),
                Materials.Hydrogen.getGas(100L),
                MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(10),
                40,
                7680,
                200000000);

        // Thorium Liquid Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { MyMaterial.uraniumCarbideThoriumMixture.get(OrePrefixes.dust, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                ItemRefer.Wrapped_Thorium_Ingot.get(1),
                300,
                480);

        GT_Values.RA.addImplosionRecipe(
                ItemRefer.Wrapped_Thorium_Ingot.get(1),
                4,
                ItemRefer.High_Density_Thorium_Nugget.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 8));

        GT_Values.RA.addCompressorRecipe(
                ItemRefer.High_Density_Thorium_Nugget.get(9),
                ItemRefer.High_Density_Thorium.get(1),
                200,
                120);

        GT_Values.RA.addMixerRecipe(
                ItemRefer.High_Density_Thorium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Mercury.getFluid(1000L),
                MyMaterial.thoriumBasedLiquidFuel.getFluidOrGas(4000),
                null,
                3000,
                240);

        GT_Values.RA.addMixerRecipe(
                MyMaterial.thoriumBasedLiquidFuel.get(OrePrefixes.cell, 1),
                GT_Utility.getIntegratedCircuit(1),
                null,
                null,
                Materials.Helium.getPlasma(250L),
                null,
                MyMaterial.thoriumBasedLiquidFuelExcited.get(OrePrefixes.cell, 1),
                120,
                3840);

        // Liquid Plutonium Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { MyMaterial.plutoniumOxideUraniumMixture.get(OrePrefixes.dust, 8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.HSSS, 4),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                ItemRefer.Wrapped_Plutonium_Ingot.get(1),
                1800,
                2040);

        GT_Values.RA.addImplosionRecipe(
                ItemRefer.Wrapped_Plutonium_Ingot.get(2),
                16,
                ItemRefer.High_Density_Plutonium_Nugget.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.HSSS, 8));

        GT_Values.RA.addCompressorRecipe(
                ItemRefer.High_Density_Plutonium_Nugget.get(9),
                ItemRefer.High_Density_Plutonium.get(1),
                1200,
                120);

        GT_Values.RA.addMixerRecipe(
                ItemRefer.High_Density_Plutonium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 2),
                GT_Utility.getIntegratedCircuit(1),
                null,
                null,
                MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(1000),
                null,
                360,
                30720);

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lutetium.getMolten(16),
                MyMaterial.plutoniumBasedLiquidFuel.getFluidOrGas(20),
                MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(20),
                20,
                15360,
                220000000);

        // Th-232
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 24),
                        MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 0), GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { FluidRegistry.getFluidStack("nitricacid", 6000) },
                new FluidStack[] { MyMaterial.oxalate.getFluidOrGas(3000), Materials.NitricOxide.getGas(6000) },
                null,
                600,
                120);

        // Th + 2O = ThO2
        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1),
                null,
                Materials.Oxygen.getGas(2000),
                null,
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 3),
                null,
                100,
                480,
                1200);

        // Th + 8HNO3 =HF= Th(NO3)4 + 4NO2 + 4H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Thorium.getDust(1), },
                new FluidStack[] { Materials.HydrofluoricAcid.getFluid(100),
                        FluidRegistry.getFluidStack("nitricacid", 8000) },
                new FluidStack[] { MyMaterial.thoriumNitrate.getFluidOrGas(1000),
                        Materials.NitrogenDioxide.getGas(4000) },
                null,
                40,
                120);

        // 4NaOH + Th(NO3)4 = Th(OH)4 + 4NaNO3
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.SodiumHydroxide.getDust(12),
                MyMaterial.thoriumNitrate.getFluidOrGas(1000),
                null,
                MyMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9),
                WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 20),
                200,
                120);
        // 2 NaNO3 + H2SO4 = Na2SO4 + 2HNO3
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 10),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.NitricAcid.getFluid(2000),
                WerkstoffLoader.Sodiumsulfate.get(OrePrefixes.dust, 7),
                null,
                200,
                480);

        // Th(OH)4 + 4HF = ThF4 + 4H2O
        GT_Values.RA.addChemicalRecipe(
                MyMaterial.thoriumHydroxide.get(OrePrefixes.dust, 9),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydrofluoricAcid.getFluid(4000),
                MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000),
                null,
                null,
                400,
                30);

        // Zn + 2Cl = ZnCl2
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                null,
                MyMaterial.zincChloride.get(OrePrefixes.dust, 3),
                null,
                100,
                30);

        // ZnCl2 + 3Ca + ThF4 = ZnTh + CaCl2 + 2CaF2
        GT_Values.RA.addBlastRecipe(
                MyMaterial.zincChloride.get(OrePrefixes.dust, 3),
                Materials.Calcium.getDust(3),
                MyMaterial.thorium232Tetrafluoride.getFluidOrGas(1000),
                WerkstoffLoader.CalciumChloride.getFluidOrGas(3000),
                MyMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1),
                WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 6),
                300,
                120,
                3000);

        GT_Values.RA.addBlastRecipe(
                MyMaterial.zincThoriumAlloy.get(OrePrefixes.ingot, 1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Argon.getGas(250),
                Materials.Zinc.getMolten(144),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1),
                null,
                150,
                480,
                1900);

        // 2V + 5O = V2O5
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(24),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 2),
                Materials.Oxygen.getGas(5000),
                null,
                MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 7),
                null,
                200,
                120,
                2500);

        // Atomic Separation Catalyst
        ItemStack[] mat1 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Firestone, 4) };
        ItemStack[] mat2 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Draconium, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium, 4), };
        ItemStack[] mat3 = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ardite, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manyullyn, 4) };

        for (ItemStack m1 : mat1) {
            for (ItemStack m2 : mat2) {
                for (ItemStack m3 : mat3) {
                    GT_Values.RA.addMixerRecipe(
                            m1,
                            m2,
                            m3,
                            GT_Utility.getIntegratedCircuit(4),
                            Materials.Naquadah.getMolten(288),
                            null,
                            ItemRefer.Raw_Atomic_Separation_Catalyst.get(63),
                            300,
                            480);
                }
            }
        }

        GT_Values.RA.addFormingPressRecipe(
                WerkstoffLoader.Tiberium.get(OrePrefixes.plate, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 8),
                MyMaterial.orundum.get(OrePrefixes.plate, 1),
                400,
                3000);

        GT_Values.RA.addBlastRecipe(
                MyMaterial.orundum.get(OrePrefixes.plate, 2),
                ItemRefer.Raw_Atomic_Separation_Catalyst.get(4),
                Materials.Plutonium.getMolten(144),
                null,
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1),
                null,
                3600,
                480,
                5000);

        GT_Values.RA.addVacuumFreezerRecipe(
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1),
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1),
                450,
                960);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.naquadahGas.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(4),
                new FluidStack[] { Materials.Helium.getGas(300), WerkstoffLoader.Neon.getFluidOrGas(50),
                        Materials.Argon.getGas(80), WerkstoffLoader.Krypton.getFluidOrGas(20),
                        WerkstoffLoader.Xenon.getFluidOrGas(40), Materials.Radon.getGas(14000) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadah, 1),
                6,
                4020,
                300);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.lightNaquadahFuel.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(4),
                new FluidStack[] { Materials.Radon.getGas(1400), MyMaterial.naquadahGas.getFluidOrGas(400),
                        Materials.Uranium.getMolten(648), MyMaterial.heavyNaquadahFuel.getFluidOrGas(280),
                        Materials.Plutonium.getMolten(576), MyMaterial.naquadahAsphalt.getFluidOrGas(140) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1),
                6,
                4020,
                450);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(6),
                new FluidStack[] { Materials.Radon.getGas(1000), MyMaterial.naquadahGas.getFluidOrGas(450),
                        MyMaterial.lightNaquadahFuel.getFluidOrGas(560), Materials.Uranium.getMolten(720),
                        Materials.Lutetium.getMolten(648), MyMaterial.naquadahAsphalt.getFluidOrGas(240) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1),
                6,
                4020,
                500);

        CrackRecipeAdder.crackerAdder(
                MyMaterial.naquadahAsphalt.getFluidOrGas(1000),
                MyMaterial.atomicSeparationCatalyst.getMolten(12),
                new FluidStack[] { MyMaterial.lightNaquadahFuel.getFluidOrGas(600), Materials.Uranium.getMolten(1152),
                        Materials.Thorium.getMolten(864), Materials.Plutonium.getMolten(792),
                        Materials.Thulium.getMolten(216), MyMaterial.heavyNaquadahFuel.getFluidOrGas(350) },
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 1),
                6,
                4020,
                800);

        // 2C2H6O =H2SO4= C4H10O + H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.Ethanol.getFluid(1000), Materials.SulfuricAcid.getFluid(1000) },
                new FluidStack[] { MyMaterial.ether.getFluidOrGas(500), Materials.DilutedSulfuricAcid.getFluid(1500) },
                null,
                510,
                120);

        GT_Values.RA.addChemicalRecipe(
                Materials.GasolineRaw.getCells(9),
                Materials.Ethanol.getCells(1),
                MyMaterial.ethanolGasoline.get(OrePrefixes.cell, 10),
                15,
                120);

        GT_Values.RA.addFuel(MyMaterial.ether.get(OrePrefixes.cell), null, 537, 0);
        GT_Values.RA.addFuel(MyMaterial.ether.get(OrePrefixes.cell), null, 537, 1);
        GT_Values.RA.addFuel(MyMaterial.ethanolGasoline.get(OrePrefixes.cell), null, 1100, 0);
        GT_Values.RA.addFuel(MyMaterial.cyclopentadiene.get(OrePrefixes.cell), null, 70, 1);
        GT_Values.RA.addFuel(MyMaterial.ironedFuel.get(OrePrefixes.cell), null, 2248, 0);
        GT_Values.RA.addFuel(MyMaterial.ironedKerosene.get(OrePrefixes.cell), null, 1824, 0);

        // Sb + 3Cl = SbCl3
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Antimony, 1), },
                new FluidStack[] { MyMaterial.ether.getFluidOrGas(1000), Materials.Chlorine.getGas(3000) },
                new FluidStack[] { MyMaterial.antimonyTrichloride.getFluidOrGas(1000) },
                null,
                60,
                30);

        // SbCl3 + 2Cl = SbCl5
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1), },
                new FluidStack[] { MyMaterial.antimonyTrichloride.getFluidOrGas(1000),
                        Materials.Chlorine.getGas(2000) },
                new FluidStack[] { MyMaterial.antimonyPentachlorideSolution.getFluidOrGas(1000) },
                null,
                180,
                480);

        GT_Values.RA.addUniversalDistillationRecipe(
                MyMaterial.antimonyPentachlorideSolution.getFluidOrGas(1000),
                new FluidStack[] { MyMaterial.ether.getFluidOrGas(1000),
                        MyMaterial.antimonyPentachloride.getFluidOrGas(1000) },
                null,
                600,
                120);

        // SbCl5 + 5HF = SbF5 + 5HCl
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1), },
                new FluidStack[] { MyMaterial.antimonyPentachloride.getFluidOrGas(1000),
                        Materials.HydrofluoricAcid.getFluid(5000) },
                new FluidStack[] { MyMaterial.antimonyPentafluoride.getFluidOrGas(1000),
                        Materials.HydrochloricAcid.getFluid(5000) },
                null,
                420,
                30);

        // SbH5 + HF = HSbF6
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1), },
                new FluidStack[] { MyMaterial.antimonyPentafluoride.getFluidOrGas(1000),
                        Materials.HydrofluoricAcid.getFluid(1000) },
                new FluidStack[] { MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000), },
                null,
                840,
                2040);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 16),
                GT_Utility.getIntegratedCircuit(16),
                Materials.HydrofluoricAcid.getFluid(3000),
                MyMaterial.acidNaquadahEmulsion.getFluidOrGas(2000),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 3),
                null,
                3600,
                2040,
                3400);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 8) },
                new FluidStack[] { MyMaterial.acidNaquadahEmulsion.getFluidOrGas(1000), },
                new FluidStack[] { MyMaterial.naquadahEmulsion.getFluidOrGas(1000), },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.AntimonyTrioxide, 1),
                        WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 4) },
                240,
                30);

        GT_Values.RA.addUniversalDistillationRecipe(
                MyMaterial.naquadahSolution.getFluidOrGas(20),
                new FluidStack[] { MyMaterial.naquadahAsphalt.getFluidOrGas(2),
                        MyMaterial.heavyNaquadahFuel.getFluidOrGas(5), MyMaterial.lightNaquadahFuel.getFluidOrGas(10),
                        FluidRegistry.getFluidStack("water", 10), MyMaterial.naquadahGas.getFluidOrGas(60) },
                null,
                20,
                2040);

        GT_Values.RA.addFuel(MyMaterial.naquadahGas.get(OrePrefixes.cell), null, 1024, 1);

        GT_Values.RA.addFusionReactorRecipe(
                MyMaterial.lightNaquadahFuel.getFluidOrGas(780),
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(360),
                MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(100),
                500,
                26000,
                320000000);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 4),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 32), },
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(100),
                        MyMaterial.naquadahGas.getFluidOrGas(1500) },
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(100) },
                null,
                500,
                525000);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 32),
                GT_Utility.getIntegratedCircuit(16),
                MyMaterial.fluoroantimonicAcid.getFluidOrGas(4000),
                MyMaterial.acidNaquadahEmulsion.getFluidOrGas(8000),
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 17),
                null,
                3600,
                4080,
                3400);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Generator_Naquadah_Mark_V.get(1).copy(),
                500000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 8),
                        ItemRefer.Advanced_Radiation_Protection_Plate.get(64), ItemList.Field_Generator_UV.get(8),
                        ItemList.Electric_Pump_UHV.get(2), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.MysteriousCrystal, 8),
                        ItemList.Circuit_Wafer_NPIC.get(16), ItemList.UHV_Coil.get(64),
                        new Object[] { "craftingLensYellow", 16 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Thulium, 64) },
                new FluidStack[] { Materials.Quantium.getMolten(9216L), Materials.DraconiumAwakened.getMolten(4608L),
                        MyMaterial.extremelyUnstableNaquadah.getMolten(1440), new FluidStack(solderIndalloy, 14400) },
                ItemRefer.Naquadah_Fuel_Refinery.get(1),
                36000,
                1919810);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Radiation_Protection_Plate.get(1),
                80000,
                new Object[] { ItemRefer.Radiation_Protection_Plate.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Trinium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                        ItemRefer.Radiation_Protection_Plate.get(1), },
                new FluidStack[] { new FluidStack(solderIndalloy, 1152) },
                ItemRefer.Advanced_Radiation_Protection_Plate.get(1),
                1000,
                65536);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Field_Restriction_Casing.get(1),
                250000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Thulium, 1),
                        ItemRefer.Advanced_Radiation_Protection_Plate.get(6), ItemList.Field_Generator_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Manyullyn, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Manyullyn, 32),
                        MyMaterial.orundum.get(OrePrefixes.plate, 4) },
                new FluidStack[] { Materials.TungstenSteel.getMolten(1152), new FluidStack(solderIndalloy, 2304) },
                ItemRefer.Naquadah_Fuel_Refinery_Casing.get(1),
                500,
                65536);

        if (LoadedList.GTNH_CORE) {
            GT_Values.RA.addAssemblylineRecipe(
                    MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot),
                    300000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                            ItemList.Field_Generator_UV.get(2), ItemList.Electric_Pump_UV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.BlackPlutonium, 16),
                            ItemList.Circuit_Wafer_PPIC.get(32),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L }, },
                    new FluidStack[] { WerkstoffLoader.Krypton.getFluidOrGas(1000),
                            Materials.ElectrumFlux.getMolten(9216), Materials.Lubricant.getFluid(128000) },
                    ItemRefer.Field_Restriction_Coil_T1.get(1),
                    18000,
                    114514);

            GT_Values.RA.addAssemblylineRecipe(
                    ItemRefer.Field_Restriction_Coil_T1.get(1),
                    350000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                            ItemList.Field_Generator_UHV.get(2), ItemList.Electric_Pump_UHV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 16),
                            ItemList.Circuit_Wafer_PPIC.get(48),
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L }, },
                    new FluidStack[] { Materials.Radon.getPlasma(1000), Materials.DraconiumAwakened.getMolten(9216),
                            Materials.Lubricant.getFluid(128000), },
                    ItemRefer.Field_Restriction_Coil_T2.get(1),
                    36000,
                    114514);

            GT_Values.RA.addAssemblylineRecipe(
                    ItemRefer.Field_Restriction_Coil_T2.get(1),
                    400000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                            ItemList.Field_Generator_UEV.get(2), ItemList.Electric_Pump_UEV.get(8),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 8),
                            GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Infinity, 16),
                            ItemList.Circuit_Wafer_PPIC.get(64),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L }, },
                    new FluidStack[] { WerkstoffLoader.Oganesson.getFluidOrGas(1000),
                            Materials.Neutronium.getMolten(9216), Materials.Lubricant.getFluid(128000), },
                    ItemRefer.Field_Restriction_Coil_T3.get(1),
                    72000,
                    114514);
        } else {
            GT_Values.RA.addAssemblylineRecipe(
                    MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot),
                    300000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.VanadiumGallium, 1),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 32),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 16),
                            ItemList.Neutron_Reflector.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gallium, 32),
                            GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Indium, 16),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmiridium, 16),
                            ItemRefer.IC2_Ir_Plate.get(32),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.CrystallineAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.CrystallineAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.CrystallineAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.CrystallineAlloy, 64),
                            ItemList.Tool_DataStick.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Cobalt, 32) },
                    new FluidStack[] { Materials.Trinium.getMolten(2304), Materials.Platinum.getMolten(4608),
                            new FluidStack(solderIndalloy, 9216), FluidRegistry.getFluidStack("ic2coolant", 8000) },
                    ItemRefer.Field_Restriction_Coil_T1.get(1),
                    900,
                    114514);

            GT_Values.RA.addAssemblylineRecipe(
                    ItemRefer.Field_Restriction_Coil_T1.get(1),
                    350000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Molybdenum, 1),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 16),
                            ItemList.Field_Generator_LuV.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NiobiumTitanium, 16),
                            GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 16),
                            WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGt, 8),
                            ItemList.Circuit_Wafer_QPIC.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Indium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Indium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Indium, 64),
                            ItemList.Energy_LapotronicOrb.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlackPlutonium, 32) },
                    new FluidStack[] { Materials.Tritanium.getMolten(2304), Materials.Palladium.getMolten(4608),
                            new FluidStack(solderIndalloy, 9216), FluidRegistry.getFluidStack("ic2coolant", 8000) },
                    ItemRefer.Field_Restriction_Coil_T2.get(1),
                    2700,
                    114514);

            GT_Values.RA.addAssemblylineRecipe(
                    ItemRefer.Field_Restriction_Coil_T2.get(1),
                    400000,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 32),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 16),
                            ItemList.Sensor_UV.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 16),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SamariumMagnetic, 4),
                            GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.BlackPlutonium, 16),
                            ItemList.Circuit_Chip_CrystalSoC2.get(32),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Indium, 32),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.VibrantAlloy, 64),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.VibrantAlloy, 64),
                            ItemList.Energy_LapotronicOrb2.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16) },
                    new FluidStack[] { Materials.Americium.getMolten(2304), Materials.Osmium.getMolten(4608),
                            new FluidStack(solderIndalloy, 9216), FluidRegistry.getFluidStack("ic2coolant", 8000) },
                    ItemRefer.Field_Restriction_Coil_T3.get(1),
                    8100,
                    114514);
        }

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BorosilicateGlass, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ReinforceGlass, 6),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                        ItemList.Field_Generator_HV.get(4), GT_Utility.getIntegratedCircuit(6) },
                Materials.Naquadria.getMolten(288),
                ItemRefer.Field_Restriction_Glass.get(1),
                300,
                120000);

        // Ca + O = CaO
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                Materials.Oxygen.getGas(1000),
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, 2),
                10,
                30);

        // AlN = Al + N
        GT_Values.RA.addElectrolyzerRecipe(
                ItemRefer.Aluminum_Nitride_Dust.get(2),
                null,
                null,
                Materials.Nitrogen.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                null,
                null,
                null,
                null,
                null,
                new int[] { 10000 },
                100,
                120);

        GT_Values.RA.addMixerRecipe(
                ItemRefer.Aluminum_Nitride_Dust.get(4),
                WerkstoffLoader.YttriumOxide.get(OrePrefixes.dust, 5),
                GT_Utility.getIntegratedCircuit(9),
                null,
                FluidRegistry.getFluidStack("advancedglue", 1000),
                null,
                ItemRefer.Special_Ceramics_Dust.get(9),
                100,
                1980);

        GT_Values.RA.addMixerRecipe(
                ItemRefer.Aluminum_Nitride_Dust.get(4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 5),
                GT_Utility.getIntegratedCircuit(9),
                null,
                FluidRegistry.getFluidStack("advancedglue", 1000),
                null,
                ItemRefer.Special_Ceramics_Dust.get(9),
                100,
                1980);

        GT_Values.RA.addExtruderRecipe(
                ItemRefer.Special_Ceramics_Dust.get(2),
                ItemList.Shape_Extruder_Plate.get(0L),
                ItemRefer.Special_Ceramics_Plate.get(1),
                400,
                480);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Raw_Cylinder.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "PPP", "PFP", "PPP", 'P', ItemRefer.Special_Ceramics_Plate.get(1), 'F',
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1) });

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Raw_Cylinder.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemRefer.Titanium_Plated_Cylinder.get(1),
                300,
                1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2) },
                new FluidStack[] { FluidRegistry.getFluidStack("liquidoxygen", 1000),
                        Materials.NitrogenDioxide.getGas(1000) },
                new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 500) },
                null,
                200,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                        WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2) },
                new FluidStack[] { FluidRegistry.getFluidStack("liquidoxygen", 1000),
                        Materials.NitrogenDioxide.getGas(1000) },
                new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 500) },
                null,
                200,
                120);

        if (FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1000) != null) {
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2) },
                    new FluidStack[] { FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000) },
                    new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 2000) },
                    null,
                    200,
                    120);

            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                            WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2) },
                    new FluidStack[] { FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000) },
                    new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 2000) },
                    null,
                    200,
                    120);
        }

        if (FluidRegistry.getFluidStack("hydrogen peroxide", 1000) != null) {
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2) },
                    new FluidStack[] { FluidRegistry.getFluidStack("hydrogen peroxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000) },
                    new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 2000) },
                    null,
                    200,
                    120);

            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(16),
                            WerkstoffLoader.SodiumNitrate.get(OrePrefixes.dust, 2) },
                    new FluidStack[] { FluidRegistry.getFluidStack("hydrogen peroxide", 1000),
                            Materials.NitrogenDioxide.getGas(1000) },
                    new FluidStack[] { FluidRegistry.getFluidStack("combustionpromotor", 2000) },
                    null,
                    200,
                    120);
        }

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Universal_Chemical_Fuel_Engine.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "TZT", "ALB", "WGW", 'T',
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1), 'Z', "circuitUltimate",
                        'A', ItemList.Machine_Multi_DieselEngine.get(1), 'B',
                        ItemList.Machine_Multi_ExtremeDieselEngine.get(1), 'L', ItemList.Hull_LuV, 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 1), 'G',
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 1), });

        // neutron activator
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4),
                        GT_Utility.getIntegratedCircuit(8) },
                FluidRegistry.getFluidStack("dye.chemical.dyecyan", 144),
                ItemRefer.Plastic_Case.get(1),
                100,
                28);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 4),
                        GT_Utility.getIntegratedCircuit(8) },
                FluidRegistry.getFluidStack("dye.watermixed.dyecyan", 144),
                ItemRefer.Plastic_Case.get(1),
                100,
                28);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Plastic_Case.get(1),
                new Object[] { "PCP", "CDC", "PCP", 'P',
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.PolyvinylChloride, 1), 'C',
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Plastic, 1), 'D', "dyeCyan" });

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemList.Circuit_Board_Coated_Basic.get(1), ItemList.Circuit_Chip_ULPIC.get(1),
                        ItemList.ULV_Coil.get(2), ItemList.Battery_RE_ULV_Tantalum.get(1) },
                new FluidStack[] { Materials.RedAlloy.getMolten(144), Materials.Aluminium.getMolten(144) },
                ItemRefer.Micro_Heater.get(1),
                120,
                40,
                1);

        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4),
                Materials.Water.getFluid(1000),
                ItemRefer.Quartz_Wafer.get(1),
                3333,
                6000,
                30,
                true);

        GT_Values.RA.addAutoclaveRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Quartzite, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4),
                FluidRegistry.getFluidStack("ic2distilledwater", 1000),
                ItemRefer.Quartz_Wafer.get(1),
                10000,
                1500,
                30,
                true);

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemRefer.Quartz_Wafer.get(1), ItemRefer.Special_Ceramics_Plate.get(2),
                        ItemRefer.Micro_Heater.get(1), ItemList.Circuit_Chip_ILC.get(4) },
                new FluidStack[] { Materials.EnergeticAlloy.getMolten(72), Materials.Silver.getMolten(18) },
                ItemRefer.Quartz_Crystal_Resonator.get(1),
                480,
                40,
                1);

        CrackRecipeAdder.addUniversalAssemblerRecipe(
                new ItemStack[] { ItemRefer.Quartz_Crystal_Resonator.get(2), ItemRefer.Plastic_Case.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), ItemList.Cover_Screen.get(1),
                        ItemList.Circuit_Parts_Diode.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8), },
                ItemRefer.Inverter.get(1),
                144,
                240,
                120,
                false);

        CrackRecipeAdder.addUniversalAssemblerRecipe(
                new ItemStack[] { ItemRefer.Quartz_Crystal_Resonator.get(2), ItemRefer.Plastic_Case.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), ItemList.Cover_Screen.get(1),
                        ItemList.Circuit_Parts_DiodeSMD.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8), },
                ItemRefer.Inverter.get(1),
                144,
                240,
                120,
                false);

        CrackRecipeAdder.addUniversalAssemblerRecipe(
                new ItemStack[] { ItemRefer.Quartz_Crystal_Resonator.get(2), ItemRefer.Plastic_Case.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1), ItemList.Cover_Screen.get(1),
                        ItemList.Circuit_Parts_DiodeASMD.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8), },
                ItemRefer.Inverter.get(1),
                144,
                240,
                120,
                false);

        GT_ModHandler.addCraftingRecipe(
                Loaders.NeutronAccelerators[0].copy(),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "WPM", "CHI", "WPM", 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 1), 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 1), 'M',
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Lead, 1), 'C',
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1), 'H', ItemList.Hull_ULV, 'I',
                        ItemRefer.Inverter.get(1), });

        GT_ModHandler.addCraftingRecipe(
                Loaders.NeutronAccelerators[1].copy(),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { "WPM", "CHI", "WPM", 'W',
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1), 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 1), 'M',
                        ItemList.Electric_Motor_LV, 'C', "plateAnyRubber", 'H', ItemList.Hull_LV, 'I',
                        ItemRefer.Inverter.get(1), });

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Inverter.get(1), ItemList.Hull_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnyCopper, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 2),
                        ItemList.Electric_Motor_MV.get(2), },
                null,
                Loaders.NeutronAccelerators[2].copy(),
                300,
                120);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Inverter.get(1), ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.PolyvinylChloride, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Beryllium, 2),
                        ItemList.Electric_Motor_HV.get(2), },
                null,
                Loaders.NeutronAccelerators[3].copy(),
                300,
                480);

        GT_Values.RA.addAssemblylineRecipe(
                Loaders.NeutronAccelerators[5].copy(),
                20000,
                new Object[] { ItemRefer.Inverter.get(2), ItemList.Hull_LuV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NeodymiumMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NeodymiumMagnetic, 4),
                        ItemList.Electric_Motor_LuV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorMV, 4), },
                new FluidStack[] { Materials.Argon.getGas(3000) },
                Loaders.NeutronAccelerators[6].copy(),
                300,
                30720);

        GT_Values.RA.addAssemblylineRecipe(
                Loaders.NeutronAccelerators[6].copy(),
                20000,
                new Object[] { ItemRefer.Inverter.get(2), ItemList.Hull_ZPM.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Polybenzimidazole, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SamariumMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SamariumMagnetic, 4),
                        ItemList.Electric_Motor_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorEV, 4), },
                new FluidStack[] { WerkstoffLoader.Xenon.getFluidOrGas(3000) },
                Loaders.NeutronAccelerators[7].copy(),
                300,
                122880);

        GT_Values.RA.addAssemblylineRecipe(
                Loaders.NeutronAccelerators[7].copy(),
                20000,
                new Object[] { ItemRefer.Inverter.get(4), ItemList.Hull_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateTriple, Materials.Polybenzimidazole, 4),
                        ItemList.ZPM_Coil.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NickelZincFerrite, 16),
                        ItemList.ZPM_Coil.get(4), ItemList.Electric_Motor_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 4), },
                new FluidStack[] { WerkstoffLoader.Oganesson.getFluidOrGas(3000) },
                Loaders.NeutronAccelerators[8].copy(),
                300,
                491520);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Casing_IV.get(1L), ItemList.Cover_ActivityDetector.get(1L),
                        ItemList.Cover_Screen.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1), ItemList.Sensor_HV.get(2),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Helium.getGas(1000),
                Loaders.NS.copy(),
                200,
                1920);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Neutron_Source.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { " P ", "PUP", " P ", 'P',
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 1), 'U',
                        ItemRefer.High_Density_Uranium.get(1) });

        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] { ItemRefer.HiC_T2.get(2), ItemList.Emitter_EV.get(2),
                        ItemRefer.Neutron_Source.get(1) },
                new FluidStack[] { Materials.StainlessSteel.getMolten(576), Materials.TungstenCarbide.getMolten(144) },
                Loaders.NA.copy(),
                7680,
                100,
                1);
    }

    public static void InitLoadRecipe() {

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Inverter.get(1), ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.IronMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenCarbide, 2),
                        ItemList.Electric_Motor_EV.get(2), },
                null,
                Loaders.NeutronAccelerators[4].copy(),
                300,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemRefer.Inverter.get(1), ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SteelMagnetic, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenCarbide, 2),
                        ItemList.Electric_Motor_IV.get(2), },
                null,
                Loaders.NeutronAccelerators[5].copy(),
                300,
                7680);

        // Al2O3 + 2N + 3C = 2AlN + 3CO
        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3),
                FluidRegistry.getFluidStack("liquidnitrogen", 2000),
                Materials.CarbonMonoxide.getGas(3000),
                ItemRefer.Aluminum_Nitride_Dust.get(2),
                null,
                200,
                1920,
                4600);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3),
                FluidRegistry.getFluidStack("liquidnitrogen", 2000),
                Materials.CarbonMonoxide.getGas(3000),
                ItemRefer.Aluminum_Nitride_Dust.get(2),
                null,
                200,
                1920,
                4600);

        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3),
                FluidRegistry.getFluidStack("liquidnitrogen", 2000),
                Materials.CarbonMonoxide.getGas(3000),
                ItemRefer.Aluminum_Nitride_Dust.get(2),
                null,
                200,
                1920,
                4600);

        GT_Values.RA.addBlastRecipe(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1),
                GT_Utility.getIntegratedCircuit(1),
                null,
                null,
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot),
                null,
                8000,
                114514,
                7000);

        GT_Values.RA.addVacuumFreezerRecipe(
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1),
                MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1),
                200,
                30720);

        GT_Values.RA.addVacuumFreezerRecipe(
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingotHot, 1),
                MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.ingot, 1),
                400,
                30720);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Oganesson.getFluidOrGas(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                new int[] { 1000, 500, 400, 50, 20, 5 },
                100,
                30);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Oganesson.getFluidOrGas(864),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 32),
                new int[] { 9900, 9500, 9000, 8000, 5000, 3000 },
                2500,
                30000);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Oganesson.getFluidOrGas(720),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 32),
                new int[] { 9500, 9000, 8000, 7000, 5000, 4000 },
                2000,
                30000);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Oganesson.getFluidOrGas(144),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Americium, 32),
                WerkstoffLoader.Californium.get(OrePrefixes.dust, 32),
                null,
                new int[] { 9000, 8500, 5000, 4000, 2000 },
                8000,
                2040);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(10),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barium, 6L),
                null,
                null,
                null,
                new int[] { 6000, 1000, 5000 },
                1000,
                1040);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                null,
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Praseodymium, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 4),
                new int[] { 10000, 8000, 10000, 8000, 3000, 5000 },
                1500,
                1040);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Krypton.getFluidOrGas(144),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tritanium, 9),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 2),
                null,
                null,
                null,
                new int[] { 5000, 8000, 7500 },
                2500,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.thoriumTetrafluoride.getFluidOrGas(1000),
                MyMaterial.thorium232Tetrafluoride.getFluidOrGas(750),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Thorium, 1),
                null,
                null,
                null,
                null,
                null,
                null,
                100,
                480);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahEmulsion.getFluidOrGas(1000),
                MyMaterial.naquadahSolution.getFluidOrGas(500),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 4),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 2),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 1),
                new int[] { 8000, 7500, 5000, 2000, 500, 100 },
                800,
                120);

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                null,
                MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(144),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 48),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 32),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 32),
                null,
                new int[] { 9000, 8500, 5000, 4000, 2000 },
                6000,
                2040);

        GT_Values.RA.addCentrifugeRecipe(
                MyMaterial.radioactiveSludge.get(OrePrefixes.dust, 4),
                null,
                null,
                Materials.Radon.getGas(20),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1),
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1),
                new int[] { 10000, 9500, 8000, 2500, 2000, 2000 },
                900,
                120);
    }

    public static void Fixer() {
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.atomicSeparationCatalyst);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.extremelyUnstableNaquadah);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.metastableOganesson);
        MaterialFix.MaterialFluidExtractionFix(MyMaterial.shirabon);
    }
}
