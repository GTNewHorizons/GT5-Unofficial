package goodgenerator.loader;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import goodgenerator.crossmod.LoadedList;
import goodgenerator.items.MyMaterial;
import goodgenerator.util.CrackRecipeAdder;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Disassembler;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeLoader_02 {

    public static void RecipeLoad() {
        // Fix exploit with disassembler
        // Superconducting Coil is already blacklisted
        GT_MetaTileEntity_Disassembler.getBlackList()
                .add(new GT_ItemStack(GT_ModHandler.getModItem("miscutils", "gtplusplus.blockcasings.3", 1, 13)));
        GT_MetaTileEntity_Disassembler.getBlackList().add(new GT_ItemStack(ItemList.Casing_Fusion_Coil.get(1)));

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueAlloy, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 32),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1)
                },
                null,
                ItemRefer.Speeding_Pipe.get(1),
                300,
                1920);

        // Compact MK1 Fusion Disassembly Recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {ItemRefer.Compact_Fusion_Coil_T0.get(1)},
                null,
                ItemList.Casing_Coil_Superconductor.get(3),
                600,
                120000);

        // Compact MK2 Fusion Disassembly Recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Compact_Fusion_Coil_T1.get(1),
                },
                null,
                ItemList.Casing_Fusion_Coil.get(3),
                600,
                480000);

        // Compact MK3 Fusion Disassembly Recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Compact_Fusion_Coil_T2.get(1),
                },
                null,
                ItemList.Casing_Fusion_Coil.get(3),
                600,
                1920000);

        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.incoloy903, 2400, 1920, 3700, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.adamantiumAlloy, 2500, 1920, 5500, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.marM200, 200, 7680, 5000, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.marM200, 220, 7680, 5000, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.signalium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.lumiium, 1600, 30720, 4000, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.artheriumSn, 500, 122880, 6500, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.titaniumBetaC, 400, 7680, 5300, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.dalisenite, 800, 491520, 8700, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.hikarium, 1200, 30720, 5400, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.tairitsu, 1200, 1966080, 7400, true);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    MyMaterial.zircaloy4.get(OrePrefixes.plate, 4),
                    MyMaterial.zircaloy2.get(OrePrefixes.ring, 2),
                    GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Advanced_Fuel_Rod.get(1),
                200,
                120);

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Uranium.get(1),
                ItemRefer.Fuel_Rod_U_1.get(1),
                null,
                400,
                120);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_U_1.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_U_2.get(1),
                200,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_U_2.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                200,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_U_1.get(4),
                    MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                    GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                220,
                1920);

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Plutonium.get(1),
                ItemRefer.Fuel_Rod_Pu_1.get(1),
                null,
                400,
                120);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_Pu_1.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_2.get(1),
                200,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_Pu_2.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                200,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_Pu_1.get(4),
                    MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                    GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                220,
                1920);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.YOTTank_Casing.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "BPB",
                    "FOF",
                    "BPB",
                    'B',
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackSteel, 1),
                    'P',
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1),
                    'F',
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1),
                    'O',
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                });

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.YOTTank.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "SPS",
                    "ECE",
                    "SLS",
                    'S',
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlueSteel, 1),
                    'P',
                    ItemList.Cover_Screen.get(1),
                    'E',
                    "circuitData",
                    'L',
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 1),
                    'C',
                    ItemRefer.YOTTank_Casing.get(1)
                });

        if (LoadedList.EXTRA_CELLS) {
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Hatch_Output_IV.get(1),
                        GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiPart", 1, 440),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CertusQuartz, 8),
                        GT_Utility.getIntegratedCircuit(1)
                    },
                    Materials.Plastic.getMolten(144),
                    Loaders.YFH,
                    200,
                    1920);
        }

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_Steel.get(12L),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                    GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_Aluminium.get(3L),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                    GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_StainlessSteel.get(2L),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin, 4),
                    GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_Titanium.get(64L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_TungstenSteel.get(18L),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Large_Fluid_Cell_Chrome.get(4L),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fluid_Storage_Core_T1.get(32),
                    ItemRefer.Fluid_Storage_Core_T1.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel, 16),
                    GT_Utility.getIntegratedCircuit(10)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680);

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                20000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.StainlessSteel, 4),
                    ItemList.Electric_Pump_HV.get(8),
                    ItemList.Quantum_Tank_LV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmium, 8),
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 1L, 6),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polycaprolactam, 16),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polycaprolactam, 16)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 2304), Materials.Lubricant.getFluid(4000)},
                ItemRefer.Fluid_Storage_Core_T3.get(1),
                400,
                32000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T3.get(1),
                40000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Titanium, 4),
                    ItemList.Electric_Pump_EV.get(8),
                    ItemList.Quantum_Tank_LV.get(4),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 8),
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 4L, 6),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 32),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 32),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 64)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 18432), Materials.Lubricant.getFluid(16000)},
                ItemRefer.Fluid_Storage_Core_T4.get(1),
                400,
                128000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T4.get(1),
                80000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.MysteriousCrystal, 4),
                    ItemList.Electric_Pump_IV.get(8),
                    ItemList.Quantum_Tank_HV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8),
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 16L, 6),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Polycaprolactam, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Polycaprolactam, 12),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 64)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(2304),
                    Materials.Titanium.getMolten(288),
                    Materials.Lubricant.getFluid(64000)
                },
                ItemRefer.Fluid_Storage_Core_T5.get(1),
                400,
                520000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T5.get(1),
                160000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 4),
                    ItemList.Electric_Pump_LuV.get(8),
                    ItemList.Quantum_Tank_EV.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8),
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 64L, 6),
                    ItemList.Machine_IV_Compressor.get(64)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(2304),
                    Materials.Titanium.getMolten(288),
                    Materials.Lubricant.getFluid(64000)
                },
                ItemRefer.Fluid_Storage_Core_T6.get(1),
                400,
                2090000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T6.get(1),
                320000,
                new Object[] {
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 64L, 6),
                    ItemList.Electric_Pump_ZPM.get(8),
                    GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 1L, 0),
                    ItemList.Quantum_Tank_EV.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.InfinityCatalyst, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(23040),
                    new FluidStack(solderIndalloy, 2304),
                    Materials.InfinityCatalyst.getMolten(1140)
                },
                ItemRefer.Fluid_Storage_Core_T7.get(1),
                400,
                8300000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T7.get(1),
                640000,
                new Object[] {
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 64L, 6),
                    ItemList.Electric_Pump_UV.get(8),
                    GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 2L, 0),
                    ItemList.Quantum_Tank_EV.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(36864),
                    new FluidStack(solderIndalloy, 30240),
                    Materials.InfinityCatalyst.getMolten(5670)
                },
                ItemRefer.Fluid_Storage_Core_T8.get(1),
                400,
                33554000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T8.get(1),
                12800000,
                new Object[] {
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 64L, 6),
                    ItemList.Electric_Pump_UHV.get(8),
                    GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 2L, 0),
                    ItemList.Quantum_Tank_IV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Infinity, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 8)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(36864),
                    new FluidStack(solderIndalloy, 30240),
                    Materials.Transcendent.getMolten(1440),
                    Materials.InfinityCatalyst.getMolten(5670)
                },
                ItemRefer.Fluid_Storage_Core_T9.get(1),
                400,
                134217000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T9.get(1),
                25600000,
                new Object[] {
                    GT_ModHandler.getModItem("GalacticraftMars", "item.null", 64L, 6),
                    ItemList.Electric_Pump_UEV.get(8),
                    GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 4L, 0),
                    ItemList.Quantum_Tank_IV.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.CosmicNeutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.CosmicNeutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 1)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(36864),
                    new FluidStack(solderIndalloy, 46080),
                    Materials.Transcendent.getMolten(4320),
                    Materials.InfinityCatalyst.getMolten(17010)
                },
                ItemRefer.Fluid_Storage_Core_T10.get(1),
                400,
                536800000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                    ItemRefer.Fluid_Storage_Core_T1.get(10),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4),
                    ItemList.Electric_Pump_HV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.YOTTank_Cell_T1.get(1),
                400,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                    ItemRefer.Fluid_Storage_Core_T2.get(10),
                    WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4),
                    ItemList.Electric_Pump_EV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                Materials.Polytetrafluoroethylene.getMolten(144),
                ItemRefer.YOTTank_Cell_T2.get(1),
                1000,
                7680);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T2.get(1),
                20000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1),
                    ItemRefer.Fluid_Storage_Core_T3.get(5),
                    ItemRefer.Fluid_Storage_Core_T3.get(5),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 4},
                    ItemList.Electric_Pump_IV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium, 4),
                    MyMaterial.adamantiumAlloy.get(OrePrefixes.plate, 16),
                    MyMaterial.adamantiumAlloy.get(OrePrefixes.plate, 16)
                },
                new FluidStack[] {
                    Materials.Quantium.getMolten(1440),
                    FluidRegistry.getFluidStack("ic2coolant", 8000),
                    Materials.Lubricant.getFluid(8000)
                },
                ItemRefer.YOTTank_Cell_T3.get(1),
                1000,
                32000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T3.get(1),
                40000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                    ItemRefer.Fluid_Storage_Core_T4.get(5),
                    ItemRefer.Fluid_Storage_Core_T4.get(5),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    ItemList.Electric_Pump_LuV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal, 4),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux, 16)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(1440),
                    FluidRegistry.getFluidStack("ic2coolant", 16000),
                    Materials.Lubricant.getFluid(16000)
                },
                ItemRefer.YOTTank_Cell_T4.get(1),
                1000,
                128000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T4.get(1),
                80000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                    ItemRefer.Fluid_Storage_Core_T5.get(5),
                    ItemRefer.Fluid_Storage_Core_T5.get(5),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 4},
                    ItemList.Electric_Pump_ZPM.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 4),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16)
                },
                new FluidStack[] {
                    Materials.Draconium.getMolten(1440),
                    FluidRegistry.getFluidStack("ic2coolant", 16000),
                    Materials.Lubricant.getFluid(16000)
                },
                ItemRefer.YOTTank_Cell_T5.get(1),
                1000,
                520000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T5.get(1),
                160000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                    ItemRefer.Fluid_Storage_Core_T6.get(2),
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 4L},
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 4L},
                    ItemList.Electric_Pump_UV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 4),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16)
                },
                new FluidStack[] {
                    Materials.DraconiumAwakened.getMolten(1440),
                    FluidRegistry.getFluidStack("ic2coolant", 46080),
                    Materials.Lubricant.getFluid(32000)
                },
                ItemRefer.YOTTank_Cell_T6.get(1),
                1000,
                2090000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T6.get(1),
                160000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                    ItemRefer.Fluid_Storage_Core_T7.get(2),
                    new Object[] {OrePrefixes.circuit.get(Materials.Bio), 4L},
                    new Object[] {OrePrefixes.circuit.get(Materials.Bio), 4L},
                    ItemList.Electric_Pump_UHV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 16)
                },
                new FluidStack[] {
                    Materials.DraconiumAwakened.getMolten(14400),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080)
                },
                ItemRefer.YOTTank_Cell_T7.get(1),
                1000,
                8300000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T7.get(1),
                160000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                    ItemRefer.Fluid_Storage_Core_T8.get(2),
                    new Object[] {OrePrefixes.circuit.get(Materials.Nano), 4L},
                    new Object[] {OrePrefixes.circuit.get(Materials.Nano), 4L},
                    ItemList.Electric_Pump_UEV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32)
                },
                new FluidStack[] {
                    Materials.DraconiumAwakened.getMolten(14400),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080)
                },
                ItemRefer.YOTTank_Cell_T8.get(1),
                1000,
                33554000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T8.get(1),
                200000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 2),
                    ItemRefer.Fluid_Storage_Core_T9.get(2),
                    new Object[] {OrePrefixes.circuit.get(Materials.Piko), 4L},
                    new Object[] {OrePrefixes.circuit.get(Materials.Piko), 4L},
                    ItemList.Electric_Pump_UIV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 32)
                },
                new FluidStack[] {
                    Materials.DraconiumAwakened.getMolten(14400),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080)
                },
                ItemRefer.YOTTank_Cell_T9.get(1),
                1000,
                134217000);
        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T9.get(1),
                240000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 2),
                    ItemRefer.Fluid_Storage_Core_T10.get(2),
                    new Object[] {OrePrefixes.circuit.get(Materials.Quantum), 6L},
                    new Object[] {OrePrefixes.circuit.get(Materials.Quantum), 6L},
                    ItemList.Electric_Pump_UMV.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 5),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 5)
                },
                new FluidStack[] {
                    Materials.DraconiumAwakened.getMolten(14400),
                    Materials.Transcendent.getMolten(1440),
                    FluidRegistry.getFluidStack("supercoolant", 46080),
                    Materials.Lubricant.getFluid(46080)
                },
                ItemRefer.YOTTank_Cell_T10.get(1),
                1000,
                536800000);

        // Craft 2x64X Tier to 1X+1 Tier
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fluid_Storage_Core_T6.get(64),
                    ItemRefer.Fluid_Storage_Core_T6.get(64),
                    GT_Utility.getIntegratedCircuit(2)
                },
                GT_Values.NF,
                ItemRefer.Fluid_Storage_Core_T7.get(1),
                200,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fluid_Storage_Core_T7.get(64),
                    ItemRefer.Fluid_Storage_Core_T7.get(64),
                    GT_Utility.getIntegratedCircuit(2)
                },
                GT_Values.NF,
                ItemRefer.Fluid_Storage_Core_T8.get(1),
                200,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fluid_Storage_Core_T8.get(64),
                    ItemRefer.Fluid_Storage_Core_T8.get(64),
                    GT_Utility.getIntegratedCircuit(2)
                },
                GT_Values.NF,
                ItemRefer.Fluid_Storage_Core_T9.get(1),
                200,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fluid_Storage_Core_T9.get(64),
                    ItemRefer.Fluid_Storage_Core_T9.get(64),
                    GT_Utility.getIntegratedCircuit(2)
                },
                GT_Values.NF,
                ItemRefer.Fluid_Storage_Core_T10.get(1),
                200,
                7680);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T1.get(1),
                ItemRefer.Fluid_Storage_Core_T1.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                100,
                480);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T2.get(1),
                ItemRefer.Fluid_Storage_Core_T2.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                100,
                480);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T3.get(1),
                ItemRefer.Fluid_Storage_Core_T3.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah, 1),
                100,
                480);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T4.get(1),
                ItemRefer.Fluid_Storage_Core_T4.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                100,
                480);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T5.get(1),
                ItemRefer.Fluid_Storage_Core_T5.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                100,
                480);

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T6.get(1),
                ItemRefer.Fluid_Storage_Core_T6.get(2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                100,
                480);
        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T7.get(1),
                ItemRefer.Fluid_Storage_Core_T7.get(2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                100,
                480);
        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T8.get(1),
                ItemRefer.Fluid_Storage_Core_T8.get(2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                100,
                480);
        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T9.get(1),
                ItemRefer.Fluid_Storage_Core_T9.get(2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                100,
                480);
        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T10.get(1),
                ItemRefer.Fluid_Storage_Core_T10.get(2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                100,
                480);

        GT_Values.RA.addAutoclaveRecipe(
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1),
                MyMaterial.naquadahGas.getFluidOrGas(250),
                WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1),
                10000,
                400,
                480);

        GT_Values.RA.addChemicalBathRecipe(
                Materials.Firestone.getGems(1),
                MyMaterial.lightNaquadahFuel.getFluidOrGas(144),
                WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1),
                null,
                null,
                new int[] {10000},
                400,
                1980);

        GT_Values.RA.addChemicalBathRecipe(
                Materials.Diamond.getGems(1),
                MyMaterial.heavyNaquadahFuel.getFluidOrGas(144),
                WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1),
                null,
                null,
                new int[] {10000},
                400,
                1980);

        GT_Values.RA.addDistillationTowerRecipe(
                Materials.HeavyFuel.getFluid(1000),
                new FluidStack[] {
                    Materials.Toluene.getFluid(400), Materials.Benzene.getFluid(400), Materials.Phenol.getFluid(250)
                },
                null,
                120,
                480);

        GT_Values.RA.addFluidExtractionRecipe(
                Materials.Coal.getGems(1),
                Materials.Ash.getDust(1),
                FluidRegistry.getFluidStack("fluid.coaltar", 250),
                10,
                60,
                120);

        if (OreDictionary.getOres("fuelCoke").size() > 0) {
            GT_Values.RA.addFluidExtractionRecipe(
                    OreDictionary.getOres("fuelCoke").get(0),
                    Materials.Ash.getDust(1),
                    FluidRegistry.getFluidStack("fluid.coaltar", 500),
                    10,
                    60,
                    120);
        }

        if (LoadedList.GTPP) {
            GT_Values.RA.addDistilleryRecipe(
                    24,
                    FluidRegistry.getFluidStack("fluid.coaltaroil", 100),
                    MyMaterial.cyclopentadiene.getFluidOrGas(30),
                    100,
                    120,
                    false);
        } else {
            GT_Values.RA.addDistilleryRecipe(
                    24,
                    FluidRegistry.getFluidStack("fluid.coaltar", 300),
                    MyMaterial.cyclopentadiene.getFluidOrGas(100),
                    100,
                    120,
                    false);
        }

        GT_Values.RA.addDistilleryRecipe(
                24, Materials.WoodTar.getFluid(500), MyMaterial.cyclopentadiene.getFluidOrGas(20), 100, 120, false);

        // FeCl2 + Cl = FeCl3
        GT_Values.RA.addChemicalRecipe(
                MyMaterial.ferrousChloride.get(OrePrefixes.cell, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(1000),
                null,
                Materials.IronIIIChloride.getCells(1),
                40);

        // FeCl3 + H = FeCl2 + HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.IronIIIChloride.getCells(1),
                GT_Utility.getIntegratedCircuit(7),
                Materials.Hydrogen.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                MyMaterial.ferrousChloride.get(OrePrefixes.cell, 1),
                80,
                120);

        // NH3 + 2C2H6O = C4H11N + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethanol.getFluid(2000),
                Materials.Water.getFluid(2000),
                MyMaterial.diethylamine.get(OrePrefixes.cell, 1),
                200,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {
                    MyMaterial.cyclopentadiene.getFluidOrGas(2000),
                    MyMaterial.ferrousChloride.getFluidOrGas(1000),
                    MyMaterial.diethylamine.getFluidOrGas(8000),
                    Materials.Ice.getSolid(4000)
                },
                new FluidStack[] {MyMaterial.impureFerroceneMixture.getFluidOrGas(15000)},
                null,
                2400,
                120);

        GT_Values.RA.addMixerRecipe(
                MyMaterial.ether.get(OrePrefixes.cell, 1),
                GT_Utility.getIntegratedCircuit(1),
                null,
                null,
                MyMaterial.impureFerroceneMixture.getFluidOrGas(7500),
                MyMaterial.ferroceneWaste.getFluidOrGas(5000),
                MyMaterial.ferroceneSolution.get(OrePrefixes.cell, 1),
                800,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                MyMaterial.ferroceneWaste.getFluidOrGas(1000),
                new FluidStack[] {
                    Materials.Water.getFluid(400),
                    MyMaterial.diethylamine.getFluidOrGas(800),
                    MyMaterial.ether.getFluidOrGas(500)
                },
                null,
                600,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                MyMaterial.ferroceneSolution.getFluidOrGas(2000),
                new FluidStack[] {MyMaterial.ether.getFluidOrGas(1000)},
                MyMaterial.ferrocene.get(OrePrefixes.dust, 1),
                600,
                120);

        if (LoadedList.GTPP) {
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] {MyMaterial.ferrocene.get(OrePrefixes.dust, 4), Materials.SodiumHydroxide.getDust(8)
                    },
                    new FluidStack[] {
                        FluidRegistry.getFluidStack("fluid.kerosene", 40000),
                        Materials.Naphtha.getFluid(3000),
                        MyMaterial.diethylamine.getFluidOrGas(1000)
                    },
                    new FluidStack[] {MyMaterial.ironedKerosene.getFluidOrGas(44000)},
                    null,
                    2400,
                    1920);
        }

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                    MyMaterial.ferrocene.get(OrePrefixes.dust, 4),
                },
                new FluidStack[] {
                    FluidRegistry.getFluidStack("combustionpromotor", 4000),
                    Materials.Naphtha.getFluid(40000),
                    Materials.LightFuel.getFluid(3000),
                    Materials.LPG.getFluid(1000),
                    Materials.Tetranitromethane.getFluid(2000),
                },
                new FluidStack[] {MyMaterial.ironedFuel.getFluidOrGas(50000)},
                null,
                2400,
                7680);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Combustion_Generator_EV.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "PCP",
                    "MHM",
                    "GWG",
                    'G',
                    GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 1),
                    'C',
                    "circuitData",
                    'W',
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1),
                    'P',
                    ItemList.Electric_Piston_EV,
                    'H',
                    ItemList.Hull_EV,
                    'M',
                    ItemList.Electric_Motor_EV
                });

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Combustion_Generator_IV.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "PCP",
                    "MHM",
                    "GWG",
                    'G',
                    GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.TungstenSteel, 1),
                    'C',
                    "circuitElite",
                    'W',
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 1),
                    'P',
                    ItemList.Electric_Piston_IV,
                    'H',
                    ItemList.Hull_IV,
                    'M',
                    ItemList.Electric_Motor_IV
                });

        GT_Values.RA.addFluidCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.Fuel_Rod_LU_1.get(1),
                MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(250),
                null,
                100,
                1920);

        GT_Values.RA.addFluidCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.Fuel_Rod_LPu_1.get(1),
                MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(250),
                null,
                100,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LPu_1.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_LPu_2.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LPu_2.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_LPu_4.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LPu_1.get(4),
                    MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                    GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_LPu_4.get(1),
                220,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LU_1.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_LU_2.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LU_2.get(2),
                    MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                    GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_LU_4.get(1),
                200,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemRefer.Fuel_Rod_LU_1.get(4),
                    MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                    GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_LU_4.get(1),
                220,
                7680);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
                FluidRegistry.getFluidStack("lava", 20000),
                FluidRegistry.getFluidStack("ic2pahoehoelava", 20000),
                FluidRegistry.getFluidStack("ic2distilledwater", 20000),
                FluidRegistry.getFluidStack("steam", 3200000),
                FluidRegistry.getFluidStack("ic2superheatedsteam", 1600000),
                10000);

        MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
                FluidRegistry.getFluidStack("ic2hotcoolant", 16000),
                FluidRegistry.getFluidStack("ic2coolant", 16000),
                FluidRegistry.getFluidStack("ic2distilledwater", 20000),
                FluidRegistry.getFluidStack("ic2superheatedsteam", 3200000),
                FluidRegistry.getFluidStack("supercriticalsteam", 32000),
                8000);

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Lepidolite, 1),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.RockSalt.getDust(1),
                MyMaterial.lithiumChloride.get(OrePrefixes.dust, 3),
                Materials.Cryolite.getDust(4),
                new int[] {8000, 8000, 8000},
                140,
                120);

        GT_Values.RA.addBlastRecipe(
                MyMaterial.marM200.get(OrePrefixes.ingot, 18),
                Materials.Cerium.getIngots(1),
                MyMaterial.lithiumChloride.getMolten(144),
                null,
                MyMaterial.marCeM200.get(OrePrefixes.ingotHot, 19),
                null,
                5700,
                122880,
                4500);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.SC_Turbine_Casing.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "PhP",
                    "GCG",
                    "PwP",
                    'G',
                    MyMaterial.marM200.get(OrePrefixes.gearGt, 1),
                    'C',
                    ItemList.Casing_Turbine.get(1),
                    'P',
                    MyMaterial.marCeM200.get(OrePrefixes.plate, 1),
                });

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    MyMaterial.marM200.get(OrePrefixes.gearGt, 2),
                    MyMaterial.marCeM200.get(OrePrefixes.plate, 4),
                    ItemList.Casing_Turbine.get(1)
                },
                null,
                ItemRefer.SC_Turbine_Casing.get(1),
                300,
                480);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.SC_Fluid_Turbine.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "NPN",
                    "GHG",
                    "IPI",
                    'N',
                    "circuitMaster",
                    'P',
                    MyMaterial.marM200.get(OrePrefixes.plate, 1),
                    'H',
                    ItemList.Hull_IV.get(1),
                    'G',
                    MyMaterial.marCeM200.get(OrePrefixes.gearGt, 1),
                    'I',
                    MyMaterial.incoloy903.get(OrePrefixes.pipeLarge, 1)
                });

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    MyMaterial.marM200.get(OrePrefixes.plate, 2),
                    MyMaterial.marCeM200.get(OrePrefixes.gearGt, 2),
                    MyMaterial.incoloy903.get(OrePrefixes.pipeLarge, 2),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                    ItemList.Hull_IV.get(1)
                },
                null,
                ItemRefer.SC_Fluid_Turbine.get(1),
                300,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    MyMaterial.incoloy903.get(OrePrefixes.plate, 4),
                    MyMaterial.marCeM200.get(OrePrefixes.plate, 4),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NiobiumTitanium, 1),
                    GT_Utility.getIntegratedCircuit(8)
                },
                null,
                ItemRefer.Pressure_Resistant_Wall.get(1),
                1000,
                480);

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.Extreme_Heat_Exchanger.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] {
                    "EPE",
                    "PHP",
                    "SPS",
                    'P',
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1),
                    'H',
                    ItemList.Hull_IV.get(1),
                    'S',
                    MyMaterial.marM200.get(OrePrefixes.plate, 1),
                    'E',
                    GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1)
                });

        GT_Values.RA.addChemicalBathRecipe(
                ItemRefer.Salty_Root.get(1),
                GT_ModHandler.getWater(100),
                Materials.Salt.getDust(1),
                Materials.RockSalt.getDust(1),
                Materials.Saltpeter.getDust(1),
                new int[] {9500, 8000, 5000},
                100,
                30);

        if (LoadedList.GTNH_CORE) {
            CrackRecipeAdder.addUniversalAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngravedGoldChip", 16),
                        ItemList.Circuit_Chip_SoC2.get(8),
                        ItemList.Circuit_Chip_NOR.get(32),
                        MyMaterial.signalium.get(OrePrefixes.bolt, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                        GT_ModHandler.getIC2Item("reactorVent", 1L, 1)
                    },
                    ItemRefer.HiC_T1.get(1),
                    288,
                    1200,
                    7680,
                    false);

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Robot_Arm_IV.get(4),
                        ItemRefer.HiC_T1.get(4),
                        ItemList.Tool_DataOrb.get(3),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08, Materials.Titanium, 4),
                        MyMaterial.hikarium.get(OrePrefixes.gearGt, 4),
                        MyMaterial.marM200.get(OrePrefixes.plateDouble, 2),
                        ItemRefer.IC2_Ir_Plate.get(2),
                        MyMaterial.lumiium.get(OrePrefixes.bolt, 48),
                    },
                    Materials.Palladium.getMolten(1152),
                    ItemRefer.Precise_Assembler.get(1),
                    1800,
                    7680,
                    false);

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Casing_ZPM.get(3),
                        ItemList.Robot_Arm_EV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, MyMaterial.lumiium.getBridgeMaterial(), 2),
                        MyMaterial.marCeM200.get(OrePrefixes.plateDouble, 2),
                        ItemRefer.HiC_T1.get(1),
                        MyMaterial.signalium.get(OrePrefixes.bolt, 32),
                        MyMaterial.titaniumBetaC.get(OrePrefixes.gearGtSmall, 8)
                    },
                    Materials.BlackSteel.getMolten(576),
                    ItemRefer.Precise_Electronic_Unit_T1.get(2),
                    800,
                    7680,
                    false);

            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                    new ItemStack[] {
                        ItemRefer.HiC_T1.get(2),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngravedDiamondCrystalChip", 8),
                        ItemList.Circuit_Chip_NAND.get(16),
                        GT_ModHandler.getIC2Item("reactorVentCore", 1L, 1)
                    },
                    new FluidStack[] {
                        Materials.Plastic.getMolten(288),
                        MyMaterial.signalium.getMolten(144),
                        MyMaterial.lumiium.getMolten(72),
                        Materials.Enderium.getMolten(72)
                    },
                    ItemRefer.HiC_T2.get(1),
                    30720,
                    100,
                    1);

            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                    new ItemStack[] {
                        ItemRefer.HiC_T2.get(2),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(8),
                        ItemList.Circuit_Chip_CrystalSoC2.get(1),
                        GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 1)
                    },
                    new FluidStack[] {
                        MyMaterial.adamantiumAlloy.getMolten(576),
                        MyMaterial.signalium.getMolten(288),
                        MyMaterial.lumiium.getMolten(144),
                        Materials.TungstenCarbide.getMolten(72)
                    },
                    ItemRefer.HiC_T3.get(1),
                    122880,
                    100,
                    2);

            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                    new ItemStack[] {
                        ItemRefer.HiC_T3.get(2),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngravedEnergyChip", 8),
                        ItemList.Circuit_Chip_QuantumCPU.get(16),
                        GT_ModHandler.getIC2Item("reactorVentGold", 1L, 1)
                    },
                    new FluidStack[] {
                        MyMaterial.marM200.getMolten(1152),
                        MyMaterial.signalium.getMolten(576),
                        MyMaterial.lumiium.getMolten(288),
                        MyMaterial.artheriumSn.getMolten(144)
                    },
                    ItemRefer.HiC_T4.get(1),
                    491520,
                    100,
                    3);

            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                    new ItemStack[] {
                        ItemRefer.HiC_T4.get(2),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngravedManyullynCrystalChip", 8),
                        ItemList.Circuit_Chip_BioCPU.get(1),
                        Ic2Items.reactorVentSpread
                    },
                    new FluidStack[] {
                        MyMaterial.titaniumBetaC.getMolten(1728),
                        MyMaterial.signalium.getMolten(1152),
                        MyMaterial.lumiium.getMolten(576),
                        MyMaterial.dalisenite.getMolten(288)
                    },
                    ItemRefer.HiC_T5.get(1),
                    1966080,
                    100,
                    3);

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Casing_UV.get(3),
                        ItemList.Robot_Arm_LuV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.ElectrumFlux, 4),
                        ItemRefer.HiC_T2.get(1),
                        ItemRefer.Precise_Electronic_Unit_T1.get(1),
                        MyMaterial.marCeM200.get(OrePrefixes.bolt, 32),
                        MyMaterial.artheriumSn.get(OrePrefixes.gearGtSmall, 8),
                    },
                    MyMaterial.adamantiumAlloy.getMolten(1152),
                    ItemRefer.Precise_Electronic_Unit_T2.get(4),
                    4800,
                    122880);

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemList.Casing_MAX.get(3),
                        ItemList.Field_Generator_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4),
                        ItemRefer.HiC_T3.get(1),
                        ItemRefer.Precise_Electronic_Unit_T2.get(1),
                        MyMaterial.titaniumBetaC.get(OrePrefixes.bolt, 32),
                        MyMaterial.dalisenite.get(OrePrefixes.gearGtSmall, 8),
                    },
                    MyMaterial.artheriumSn.getMolten(1152),
                    ItemRefer.Precise_Electronic_Unit_T3.get(4),
                    4800,
                    491520);
        }

        // Compact MK1 Fusion Coil
        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Casing_Coil_Superconductor.get(3),
                    ItemRefer.HiC_T2.get(1),
                    ItemRefer.Special_Ceramics_Plate.get(2)
                },
                new FluidStack[] {MyMaterial.marM200.getMolten(1152), MyMaterial.zircaloy4.getMolten(288)},
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                9001,
                1200,
                1);

        // Compact MK2 Fusion Coil
        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Casing_Fusion_Coil.get(3),
                    ItemRefer.Quartz_Crystal_Resonator.get(2),
                    ItemRefer.HiC_T3.get(1),
                },
                new FluidStack[] {MyMaterial.artheriumSn.getMolten(576), MyMaterial.titaniumBetaC.getMolten(144)},
                ItemRefer.Compact_Fusion_Coil_T1.get(1),
                14000,
                800,
                2);

        // Compact MK3 Fusion Coil
        MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                new ItemStack[] {
                    ItemList.Casing_Fusion_Coil.get(3),
                    ItemRefer.Radiation_Protection_Plate.get(2),
                    ItemList.QuantumStar.get(4),
                    ItemRefer.HiC_T4.get(1)
                },
                new FluidStack[] {MyMaterial.dalisenite.getMolten(576), MyMaterial.hikarium.getMolten(144)},
                ItemRefer.Compact_Fusion_Coil_T2.get(1),
                114514,
                800,
                3);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.FusionComputer_LuV.get(48),
                    ItemRefer.HiC_T1.get(8),
                    MyMaterial.marCeM200.get(OrePrefixes.plate, 32),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 8),
                    ItemList.Circuit_Wafer_HPIC.get(16),
                    ItemList.Field_Generator_LuV.get(4),
                    MyMaterial.marM200.get(OrePrefixes.stickLong, 8)
                },
                MyMaterial.adamantiumAlloy.getMolten(9216),
                ItemRefer.Compact_Fusion_MK1.get(1),
                1200,
                30000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Compact_Fusion_MK1.get(1),
                24000,
                new Object[] {
                    ItemList.FusionComputer_ZPMV.get(48),
                    new Object[] {"circuitUltimate", 1},
                    new Object[] {"circuitUltimate", 1},
                    new Object[] {"circuitUltimate", 1},
                    new Object[] {"circuitUltimate", 1},
                    ItemList.Circuit_Wafer_UHPIC.get(32),
                    ItemList.ZPM_Coil.get(16),
                    ItemList.Neutron_Reflector.get(4),
                    ItemRefer.HiC_T2.get(8),
                    ItemList.Field_Generator_ZPM.get(8),
                    MyMaterial.artheriumSn.get(OrePrefixes.gearGtSmall, 32)
                },
                new FluidStack[] {
                    MyMaterial.marCeM200.getMolten(2304),
                    WerkstoffLoader.HDCS.getMolten(1152),
                    MyMaterial.artheriumSn.getMolten(288)
                },
                ItemRefer.Compact_Fusion_MK2.get(1),
                6000,
                60000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Compact_Fusion_MK2.get(1),
                24000,
                new Object[] {
                    ItemList.FusionComputer_UV.get(48),
                    new Object[] {"circuitSuperconductor", 1},
                    new Object[] {"circuitSuperconductor", 1},
                    new Object[] {"circuitSuperconductor", 1},
                    new Object[] {"circuitSuperconductor", 1},
                    ItemList.Circuit_Wafer_NPIC.get(64),
                    ItemList.UV_Coil.get(16),
                    ItemRefer.Advanced_Radiation_Protection_Plate.get(8),
                    ItemRefer.HiC_T3.get(8),
                    ItemList.Field_Generator_UV.get(8),
                    WerkstoffLoader.HDCS.get(OrePrefixes.gearGtSmall, 64)
                },
                new FluidStack[] {
                    MyMaterial.titaniumBetaC.getMolten(2304),
                    MyMaterial.dalisenite.getMolten(1152),
                    Materials.Americium.getMolten(288)
                },
                ItemRefer.Compact_Fusion_MK3.get(1),
                6000,
                90000);

        // Compact MK4 Fusion Coil
        if (LoadedList.GTPP) {
            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
                    new ItemStack[] {
                        GT_ModHandler.getModItem("miscutils", "gtplusplus.blockcasings.3", 3, 13),
                        ItemRefer.HiC_T5.get(1),
                        GT_ModHandler.getModItem("miscutils", "item.itemBufferCore4", 1),
                    },
                    new FluidStack[] {
                        FluidRegistry.getFluidStack("molten.energycrystal", 1152),
                        FluidRegistry.getFluidStack("molten.laurenium", 144)
                    },
                    ItemRefer.Compact_Fusion_Coil_T3.get(1),
                    520000,
                    2000,
                    3);

            // Compact MK4 Fusion Disassembly Recipe
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] {
                        ItemRefer.Compact_Fusion_Coil_T3.get(1),
                    },
                    null,
                    GT_ModHandler.getModItem("miscutils", "gtplusplus.blockcasings.3", 3, 13),
                    600,
                    7680000);

            int tID = GregTech_API.METATILEENTITIES[31076] != null ? 31076 : 965;

            GT_Values.RA.addAssemblylineRecipe(
                    ItemRefer.Compact_Fusion_MK3.get(1),
                    24000,
                    new Object[] {
                        GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 48, tID),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorLuV, 32),
                        ItemList.Circuit_Wafer_PPIC.get(64),
                        ItemList.UHV_Coil.get(16),
                        ItemRefer.Compact_Fusion_Coil_T2.get(8),
                        ItemRefer.HiC_T4.get(8)
                    },
                    new FluidStack[] {
                        FluidRegistry.getFluidStack("molten.teflon", 1152),
                        MyMaterial.dalisenite.getMolten(576),
                        FluidRegistry.getFluidStack("molten.botmium", 288)
                    },
                    ItemRefer.Compact_Fusion_MK4.get(1),
                    6000,
                    520000);

            //            MyRecipeAdder.instance.addPreciseAssemblerRecipe(
            //                    new ItemStack[] {
            //                            ItemRefer.Compact_Fusion_Coil_T3.get(1),
            //                            GT_ModHandler.getModItem("miscutils", "gtplusplus.blockcasings.3", 1, 13),
            //                            ItemRefer.HiC_T5.get(4),
            //                            GT_ModHandler.getModItem("miscutils", "item.itemBufferCore6", 1),
            //                    },
            //                    new FluidStack[] {
            //                            FluidRegistry.getFluidStack("molten.laurenium", 1152),
            //                            MyMaterial.hikarium.getMolten(576)
            //                    },
            //                    ItemRefer.Compact_Fusion_Coil_T4.get(1),
            //                    1100000,
            //                    1919,
            //                    3
            //            );

            // Compact MK5 Computer
            //            GT_Values.RA.addAssemblylineRecipe(
            //                ItemRefer.Compact_Fusion_MK4.get(1),
            //                24000,
            //                new Object[] {
            //                    GT_ModHandler.getModItem("gregtech", "gt.blockmachines", 48, 965),
            //                    GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUV, 32),
            //                    ItemList.Circuit_Wafer_QPIC.get(64),
            //                    ItemList.UHV_Coil.get(64),
            //                    ItemRefer.Compact_Fusion_Coil_T3.get(8),
            //                    ItemRefer.HiC_T5.get(8)
            //                },
            //                new FluidStack[] {
            //                    MyMaterial.tairitsu.getMolten( 1152),
            //                    MyMaterial.artheriumSn.getMolten(576),
            //                    FluidRegistry.getFluidStack("molten.rhugnor", 288)
            //                },
            //                ItemRefer.Compact_Fusion_MK5.get(1),
            //                6000,
            //                1100000
            //            );
        }

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {Materials.Antimony.getDust(8), GT_Utility.getIntegratedCircuit(24)},
                new FluidStack[] {
                    MyMaterial.ether.getFluidOrGas(1000), Materials.Fluorine.getGas(40000), Materials.Ice.getSolid(8000)
                },
                new FluidStack[] {MyMaterial.antimonyPentafluorideSolution.getFluidOrGas(8000)},
                null,
                800,
                7680);

        GT_Values.RA.addUniversalDistillationRecipe(
                MyMaterial.antimonyPentafluorideSolution.getFluidOrGas(4000),
                new FluidStack[] {
                    MyMaterial.antimonyPentafluoride.getFluidOrGas(4000), MyMaterial.ether.getFluidOrGas(500)
                },
                null,
                100,
                120);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Plastic, 2),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                    GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Concrete.getMolten(2304),
                ItemRefer.Coolant_Tower.get(1),
                200,
                120);
    }

    public static void InitLoadRecipe() {
        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_1.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(32),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Uranium.getDust(8),
                Materials.Plutonium.getDust(2),
                Materials.Graphite.getDust(8),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_2.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(64),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Uranium.getDust(16),
                Materials.Plutonium.getDust(4),
                Materials.Graphite.getDust(16),
                Materials.Uranium235.getDust(2),
                Materials.Plutonium241.getDust(2),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_4.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(128),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Uranium.getDust(32),
                Materials.Plutonium.getDust(8),
                Materials.Graphite.getDust(32),
                Materials.Uranium235.getDust(4),
                Materials.Plutonium241.getDust(4),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_1.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(32),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Plutonium.getDust(5),
                Materials.Plutonium241.getDust(2),
                Materials.Carbon.getDust(2),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_2.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(64),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Plutonium.getDust(10),
                Materials.Plutonium241.getDust(4),
                Materials.Carbon.getDust(4),
                Materials.Uranium.getDust(2),
                Materials.Uranium235.getDust(2),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_4.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(128),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Plutonium.getDust(20),
                Materials.Plutonium241.getDust(8),
                Materials.Carbon.getDust(8),
                Materials.Uranium.getDust(4),
                Materials.Uranium235.getDust(4),
                new int[] {10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LPu_Depleted_1.get(1),
                null,
                null,
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(250),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LPu_Depleted_2.get(1),
                null,
                null,
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(500),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LPu_Depleted_4.get(1),
                null,
                null,
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LU_Depleted_1.get(1),
                null,
                null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(250),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LU_Depleted_2.get(1),
                null,
                null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(500),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_LU_Depleted_4.get(1),
                null,
                null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                null,
                null,
                null,
                null,
                null,
                new int[] {10000},
                200,
                7680);

        GT_Values.RA.addMixerRecipe(
                Materials.Glowstone.getDust(4),
                Materials.Redstone.getDust(2),
                Materials.Aluminium.getDust(1),
                GT_Utility.getIntegratedCircuit(3),
                null,
                null,
                ItemRefer.High_Energy_Mixture.get(4),
                240,
                120);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemRefer.High_Energy_Mixture.get(2),
                Materials.PhosphoricAcid.getFluid(4000),
                MyMaterial.lumiinessence.get(OrePrefixes.dust, 1),
                600,
                240);

        GT_Values.RA.addMixerRecipe(
                Materials.AnnealedCopper.getDust(4),
                Materials.Ardite.getDust(2),
                Materials.RedAlloy.getDust(2),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Redstone.getMolten(288),
                null,
                MyMaterial.signalium.get(OrePrefixes.dust, 1),
                240,
                120);

        GT_Values.RA.addMixerRecipe(
                Materials.TinAlloy.getDust(4),
                Materials.SterlingSilver.getDust(2),
                MyMaterial.lumiinessence.get(OrePrefixes.dust, 2),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Glowstone.getMolten(288),
                null,
                MyMaterial.lumiium.get(OrePrefixes.dust, 1),
                240,
                120);
    }

    public static void FinishLoadRecipe() {
        for (GT_Recipe plasmaFuel : GT_Recipe.GT_Recipe_Map.sPlasmaFuels.mRecipeList) {
            FluidStack tPlasma = GT_Utility.getFluidForFilledItem(plasmaFuel.mInputs[0], true);
            if (tPlasma == null) {
                continue;
            }
            int tUnit = plasmaFuel.mSpecialValue;
            if (tUnit > 200_000) {
                tPlasma.amount = 1500;
            } else if (tUnit > 100_000) {
                tPlasma.amount = 1000;
            } else if (tUnit > 50_000) {
                tPlasma.amount = 800;
            } else if (tUnit > 10_000) {
                tPlasma.amount = 500;
            } else {
                tPlasma.amount = 100;
            }

            String tPlasmaName = FluidRegistry.getFluidName(tPlasma);

            if (tPlasmaName.split("\\.", 2).length == 2) {
                String tOutName = tPlasmaName.split("\\.", 2)[1];
                FluidStack output = FluidRegistry.getFluidStack(tOutName, tPlasma.amount);
                if (output == null) output = FluidRegistry.getFluidStack("molten." + tOutName, tPlasma.amount);
                if (output != null) {
                    long waterAmount = (long) tUnit * 3 * tPlasma.amount / 160;
                    long superHeatedSteamAmount = (long) tUnit * 3 * tPlasma.amount;
                    long criticalSteamAmount = (long) tUnit * 3 * tPlasma.amount / 100;
                    while (superHeatedSteamAmount > Integer.MAX_VALUE) {
                        GT_Log.out.print("Superheated steam amount exceeded max int for plasma " + tOutName);
                        waterAmount /= 2;
                        superHeatedSteamAmount /= 2;
                        criticalSteamAmount /= 2;
                    }
                    MyRecipeAdder.instance.addExtremeHeatExchangerRecipe(
                            tPlasma,
                            output,
                            FluidRegistry.getFluidStack("ic2distilledwater", (int) waterAmount),
                            FluidRegistry.getFluidStack("ic2superheatedsteam", (int) superHeatedSteamAmount),
                            FluidRegistry.getFluidStack("supercriticalsteam", (int) criticalSteamAmount),
                            1);
                }
            }
        }
    }
}
