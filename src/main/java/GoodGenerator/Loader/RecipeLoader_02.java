package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.util.CrackRecipeAdder;
import GoodGenerator.util.ItemRefer;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_02 {

    public static void RecipeLoad(){

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel,1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueAlloy,1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV,32),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium,32),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite,1)
                },
                null,
                ItemRefer.Speeding_Pipe.get(1),
                300,
                1920
        );

        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.incoloy903, 2400, 1920, 3700, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.adamantiumAlloy, 2500, 1920, 5000, true);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        MyMaterial.zircaloy4.get(OrePrefixes.plate, 4),
                        MyMaterial.zircaloy2.get(OrePrefixes.ring, 2),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Advanced_Fuel_Rod.get(1),
                200,
                120
        );

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Uranium.get(1),
                ItemRefer.Fuel_Rod_U_1.get(1),
                null,
                400,
                120
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_1.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_U_2.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_2.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_1.get(4),
                        MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                        GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                220,
                1920
        );

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Plutonium.get(1),
                ItemRefer.Fuel_Rod_Pu_1.get(1),
                null,
                400,
                120
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_1.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_2.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_2.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_1.get(4),
                        MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                        GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                220,
                1920
        );

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.YOTTank_Casing.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE,
                new Object[]{
                        "BPB","FOF","BPB",
                        'B', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackSteel,1),
                        'P', GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel,1),
                        'F', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene,1),
                        'O', GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel,1),
                }
        );

        GT_ModHandler.addCraftingRecipe(
                ItemRefer.YOTTank.get(1),
                GT_ModHandler.RecipeBits.DISMANTLEABLE,
                new Object[]{
                        "SPS","ECE","SLS",
                        'S', GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlueSteel,1),
                        'P', ItemList.Cover_Screen.get(1),
                        'E', "circuitData",
                        'L', GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium,1),
                        'C', ItemRefer.YOTTank_Casing.get(1)
                }
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_Steel.get(12L),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin,4),
                        GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_Aluminium.get(3L),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin,4),
                        GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_StainlessSteel.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Tin,4),
                        GT_Utility.getIntegratedCircuit(1)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                200,
                480
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_Titanium.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium,8),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel,4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_TungstenSteel.get(18L),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel,4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        ItemList.Large_Fluid_Cell_Chrome.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.BlackSteel,4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                Materials.Polytetrafluoroethylene.getMolten(2304),
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T1.get(1),
                10000,
                new Object[]{
                        ItemRefer.Fluid_Storage_Core_T1.get(32),
                        ItemRefer.Fluid_Storage_Core_T1.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.BlackSteel,4)
                },
                new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(288),
                        Materials.Lubricant.getFluid(1000)
                },
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                200,
                7680
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T2.get(1),
                20000,
                new Object[]{
                        ItemRefer.Fluid_Storage_Core_T2.get(48),
                        ItemRefer.Fluid_Storage_Core_T2.get(48),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene,16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium,4)
                },
                new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(2304),
                        Materials.Lubricant.getFluid(4000)
                },
                ItemRefer.Fluid_Storage_Core_T3.get(1),
                800,
                122880
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T3.get(1),
                40000,
                new Object[]{
                        ItemRefer.Fluid_Storage_Core_T3.get(48),
                        ItemRefer.Fluid_Storage_Core_T3.get(48),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium,4)
                },
                new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(18432),
                        Materials.Lubricant.getFluid(16000)
                },
                ItemRefer.Fluid_Storage_Core_T4.get(1),
                3200,
                1966080
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T4.get(1),
                80000,
                new Object[]{
                        ItemRefer.Fluid_Storage_Core_T4.get(48),
                        ItemRefer.Fluid_Storage_Core_T4.get(48),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium,4)
                },
                new FluidStack[]{
                        Materials.Draconium.getMolten(2304),
                        Materials.Titanium.getMolten(288),
                        Materials.Lubricant.getFluid(64000)
                },
                ItemRefer.Fluid_Storage_Core_T5.get(1),
                6400,
                3921160
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.Fluid_Storage_Core_T5.get(1),
                160000,
                new Object[]{
                        ItemRefer.Fluid_Storage_Core_T5.get(48),
                        ItemRefer.Fluid_Storage_Core_T5.get(48),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole,32),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened,4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.DraconiumAwakened,4)
                },
                new FluidStack[]{
                        Materials.Draconium.getMolten(9216),
                        Materials.TungstenSteel.getMolten(4608),
                        Materials.Lubricant.getFluid(128000)
                },
                ItemRefer.Fluid_Storage_Core_T6.get(1),
                12800,
                7842320
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel,1),
                        ItemRefer.Fluid_Storage_Core_T1.get(10),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel,4),
                        ItemList.Electric_Pump_HV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel,4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                Materials.Plastic.getMolten(144),
                ItemRefer.YOTTank_Cell_T1.get(1),
                400,
                480
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel,1),
                        ItemRefer.Fluid_Storage_Core_T2.get(10),
                        WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4),
                        ItemList.Electric_Pump_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium,4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                Materials.Polytetrafluoroethylene.getMolten(144),
                ItemRefer.YOTTank_Cell_T2.get(1),
                1600,
                7680
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T2.get(1),
                20000,
                new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah,1),
                        ItemRefer.Fluid_Storage_Core_T3.get(5),
                        ItemRefer.Fluid_Storage_Core_T3.get(5),
                        new Object[]{"circuitUltimate", 4},
                        new Object[]{"circuitUltimate", 4},
                        ItemList.Electric_Pump_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium,4),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NiobiumTitanium,4),
                        MyMaterial.adamantiumAlloy.get(OrePrefixes.plate, 16),
                        MyMaterial.adamantiumAlloy.get(OrePrefixes.plate, 16)
                },
                new FluidStack[]{
                        Materials.Quantium.getMolten(1440),
                        FluidRegistry.getFluidStack("ic2coolant",8000),
                        Materials.Lubricant.getFluid(8000)
                },
                ItemRefer.YOTTank_Cell_T3.get(1),
                1600,
                122880
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T3.get(1),
                40000,
                new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium,1),
                        ItemRefer.Fluid_Storage_Core_T4.get(5),
                        ItemRefer.Fluid_Storage_Core_T4.get(5),
                        new Object[]{"circuitInfinite", 4},
                        new Object[]{"circuitInfinite", 4},
                        ItemList.Electric_Pump_UHV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal,4),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.MysteriousCrystal,4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectrumFlux,16)
                },
                new FluidStack[]{
                        Materials.Draconium.getMolten(1440),
                        FluidRegistry.getFluidStack("ic2coolant",16000),
                        Materials.Lubricant.getFluid(16000)
                },
                ItemRefer.YOTTank_Cell_T4.get(1),
                3200,
                1966080
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T4.get(1),
                80000,
                new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium,1),
                        ItemRefer.Fluid_Storage_Core_T5.get(5),
                        ItemRefer.Fluid_Storage_Core_T5.get(5),
                        new Object[]{"circuitBio", 4},
                        new Object[]{"circuitBio", 4},
                        ItemList.Electric_Pump_UEV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened,4),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened,4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium,16)
                },
                new FluidStack[]{
                        Materials.Draconium.getMolten(1440),
                        FluidRegistry.getFluidStack("ic2coolant",16000),
                        Materials.Lubricant.getFluid(16000)
                },
                ItemRefer.YOTTank_Cell_T5.get(1),
                6400,
                3921160
        );

        GT_Values.RA.addAssemblylineRecipe(
                ItemRefer.YOTTank_Cell_T5.get(1),
                160000,
                new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity,1),
                        ItemRefer.Fluid_Storage_Core_T6.get(5),
                        ItemRefer.Fluid_Storage_Core_T6.get(5),
                        ItemList.Field_Generator_UEV.get(4),
                        ItemList.Field_Generator_UEV.get(4),
                        ItemList.Electric_Pump_UEV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar,4),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar,4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity,16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity,16)
                },
                new FluidStack[]{
                        Materials.DraconiumAwakened.getMolten(1440),
                        FluidRegistry.getFluidStack("ic2coolant",32000),
                        Materials.Lubricant.getFluid(32000)
                },
                ItemRefer.YOTTank_Cell_T6.get(1),
                12800,
                7842320
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T1.get(1),
                ItemRefer.Fluid_Storage_Core_T1.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel,1),
                100,
                480
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T2.get(1),
                ItemRefer.Fluid_Storage_Core_T2.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel,1),
                100,
                480
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T3.get(1),
                ItemRefer.Fluid_Storage_Core_T3.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadah,1),
                100,
                480
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T4.get(1),
                ItemRefer.Fluid_Storage_Core_T4.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium,1),
                100,
                480
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T5.get(1),
                ItemRefer.Fluid_Storage_Core_T5.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium,1),
                100,
                480
        );

        GT_Values.RA.addUnboxingRecipe(
                ItemRefer.YOTTank_Cell_T6.get(1),
                ItemRefer.Fluid_Storage_Core_T6.get(10),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity,1),
                100,
                480
        );
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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

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
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );
    }
}
