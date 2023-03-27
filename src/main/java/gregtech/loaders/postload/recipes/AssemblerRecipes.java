package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.GT_Values.MOD_ID_RC;
import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.*;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.*;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class AssemblerRecipes implements Runnable {

    @Override
    public void run() {

        this.withBartWorks();
        this.withGalacticraftMars();
        this.withRailcraft();
        this.withGalaxySpace();
        this.withGTNHLanthAndGTPP();
        this.loadInputBusesRecipes();
        this.loadInputHatchesRecipes();
        this.loadOutputBusesRecipes();
        this.loadOutputHatchesRecipes();
        this.withIC2NuclearControl();

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1L),
                Materials.Concrete.getMolten(144L),
                ItemList.Block_Plascrete.get(1L),
                200,
                48);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1L),
                Materials.Concrete.getMolten(144L),
                ItemList.Block_Plascrete.get(1L),
                200,
                48);

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {

                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack(Items.string, 3),
                        GT_Utility.getIntegratedCircuit(3),
                        Dyes.VALUES[i].getFluidDye(j, 24),
                        new ItemStack(Blocks.carpet, 2, 15 - i),
                        128,
                        5);
            }
        }

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride, 1),
                ItemList.Paper_Printed_Pages.get(1L),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.written_book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.book, 1, 0),
                20,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_ModHandler.getIC2Item("carbonMesh", 4L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.Component_Filter.get(1L),
                1600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Plastic.getFluid(144),
                ItemList.Component_Filter.get(1),
                1600,
                16);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 64),
                        ItemList.Circuit_Silicon_Wafer2.get(32L), GT_Utility.getIntegratedCircuit(1) },
                Materials.AdvancedGlue.getFluid(500L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 64),
                3200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 64),
                        ItemList.Circuit_Silicon_Wafer3.get(8L), GT_Utility.getIntegratedCircuit(1) },
                Materials.AdvancedGlue.getFluid(250L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 64),
                800,
                7920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_LV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_LV.get(1L),
                400,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_MV.get(1L),
                350,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_HV.get(1L),
                300,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_EV.get(1L),
                250,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_LuV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Master), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_LuV.get(1L),
                150,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_ZPM.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_ZPM.get(1L),
                100,
                (int) TierEU.RECIPE_ZPM);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.FluidRegulator_UV.get(1L),
                50,
                500000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Steam_Valve_LV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Steam_Regulator_LV.get(1L),
                400,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Steam_Valve_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Steam_Regulator_MV.get(1L),
                350,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Steam_Valve_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Steam_Regulator_HV.get(1L),
                300,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Steam_Valve_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Steam_Regulator_EV.get(1L),
                250,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Steam_Valve_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 2L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Steam_Regulator_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_LV.get(1L), ItemList.Electric_Motor_LV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Steel), 2L),
                        GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                ItemList.Steam_Valve_LV.get(1L),
                400,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_MV.get(1L), ItemList.Electric_Motor_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Aluminium), 2L),
                        GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                ItemList.Steam_Valve_MV.get(1L),
                350,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_HV.get(1L), ItemList.Electric_Motor_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.StainlessSteel), 2L),
                        GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                ItemList.Steam_Valve_HV.get(1L),
                300,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_EV.get(1L), ItemList.Electric_Motor_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Titanium), 2L),
                        GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                ItemList.Steam_Valve_EV.get(1L),
                250,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_IV.get(1L), ItemList.Electric_Motor_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.TungstenSteel), 2L),
                        GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                ItemList.Steam_Valve_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 4),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Schematic.get(1L),
                600,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Schematic.get(1L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 1),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Schematic.get(1L),
                150,
                48);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                        ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_HV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_HV.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L),
                        ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_EV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_EV.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L),
                        ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_3.get(1L), ItemList.Electric_Pump_IV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                        ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_HV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_HV.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L),
                        ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_EV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_EV.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L),
                        ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_3.get(1L), ItemList.Electric_Pump_IV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Energy_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L),
                        ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_HV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_HV.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L),
                        ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_1.get(1L), ItemList.Electric_Pump_EV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_EV.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L),
                        ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_3.get(1L), ItemList.Electric_Pump_IV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L),
                        ItemList.Circuit_Chip_LPIC.get(2L), ItemList.HV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_HV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_HV.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L),
                        ItemList.Circuit_Chip_PIC.get(2L), ItemList.EV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_1.get(1L), ItemList.Electric_Pump_EV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_EV.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L),
                        ItemList.Circuit_Chip_HPIC.get(2L), ItemList.IV_Coil.get(2L),
                        ItemList.Reactor_Coolant_NaK_3.get(1L), ItemList.Electric_Pump_IV.get(1L) },
                GT_Values.NF,
                ItemList.Hatch_Dynamo_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L),
                        GT_Utility.getIntegratedCircuit(2) },
                Materials.Tin.getMolten(144L),
                ItemList.Long_Distance_Pipeline_Fluid.get(2L),
                300,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Tin, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L),
                        GT_Utility.getIntegratedCircuit(2) },
                Materials.Tin.getMolten(144L),
                ItemList.Long_Distance_Pipeline_Item.get(2L),
                300,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L),
                        GT_Utility.getIntegratedCircuit(24) },
                Materials.Tin.getMolten(144L),
                ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(64L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Tin, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L),
                        GT_Utility.getIntegratedCircuit(24) },
                Materials.Tin.getMolten(144L),
                ItemList.Long_Distance_Pipeline_Item_Pipe.get(64L),
                600,
                24);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.StainlessSteel, 1L),
                        ItemList.Hull_EV.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Glass.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_EV.get(1L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.Titanium, 1L),
                        ItemList.Hull_IV.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Glass.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_IV.get(1L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.TungstenSteel, 1L),
                        ItemList.Hull_LuV.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Polytetrafluoroethylene.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_LuV.get(1L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.NiobiumTitanium, 1L),
                        ItemList.Hull_ZPM.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Polytetrafluoroethylene.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_ZPM.get(1L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.MysteriousCrystal, 1L),
                        ItemList.Hull_UV.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Polybenzimidazole.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_UV.get(1L),
                600,
                24);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.Neutronium, 1L),
                        ItemList.Hull_MAX.get(1L), GT_Utility.getIntegratedCircuit(4) },
                Materials.Polybenzimidazole.getMolten(2304L),
                ItemList.Hatch_Input_Multi_2x2_UHV.get(1L),
                600,
                24);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L),
                        ItemList.Robot_Arm_IV.get(2L), GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Casing_Gearbox_TungstenSteel.get(1L),
                200,
                (int) TierEU.RECIPE_LV);

        { // limiting lifetime of the variables
            ItemStack flask = ItemList.VOLUMETRIC_FLASK.get(1);
            NBTTagCompound nbtFlask = new NBTTagCompound();
            nbtFlask.setInteger("Capacity", 144);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(1),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 288);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(2),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 576);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(3),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 720);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(4),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 864);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(5),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 72);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(6),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 648);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(7),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 936);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(8),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 250);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(10),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            nbtFlask.setInteger("Capacity", 500);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    ItemList.VOLUMETRIC_FLASK.get(1),
                    GT_Utility.getIntegratedCircuit(11),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
            // make the 1000L recipe actually in
            ItemStack flask500 = flask.copy();
            nbtFlask.setInteger("Capacity", 1000);
            flask.setTagCompound(nbtFlask);
            GT_Values.RA.addAssemblerRecipe(
                    flask500,
                    GT_Utility.getIntegratedCircuit(24),
                    flask,
                    10,
                    (int) TierEU.RECIPE_LV);
        }

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Bronze, 1L),
                        ItemList.Electric_Motor_LV.get(1L), GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_LV.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Steel, 1L),
                        ItemList.Electric_Motor_MV.get(1L), GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_MV.get(1L),
                200,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1L),
                        ItemList.Electric_Motor_HV.get(1L), GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_HV.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 1L),
                        ItemList.Electric_Motor_EV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_EV.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L),
                        ItemList.Electric_Motor_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_IV.get(1L),
                200,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Enderium, 1L),
                        ItemList.Electric_Motor_LuV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Enderium, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_LuV.get(1L),
                200,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1L),
                        ItemList.Electric_Motor_ZPM.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_ZPM.get(1L),
                200,
                (int) TierEU.RECIPE_ZPM);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 1L),
                        ItemList.Electric_Motor_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_UV.get(1L),
                200,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.MysteriousCrystal, 1L),
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 1L),
                        GT_Utility.getIntegratedCircuit(3) },
                GT_Values.NF,
                ItemList.Hatch_Muffler_MAX.get(1L),
                200,
                2000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.redstone_torch, 2, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                Materials.Concrete.getMolten(144L),
                new ItemStack(Items.repeater, 1, 0),
                80,
                10);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.leather, 1, 32767),
                new ItemStack(Items.lead, 1, 32767),
                Materials.Glue.getFluid(72L),
                new ItemStack(Items.name_tag, 1, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0),
                new ItemStack(Items.lead, 1, 32767),
                Materials.Glue.getFluid(72L),
                new ItemStack(Items.name_tag, 1, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 8L),
                new ItemStack(Items.compass, 1, 32767),
                GT_Values.NF,
                new ItemStack(Items.map, 1, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tantalum, 1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Manganese, 1L),
                Materials.Plastic.getMolten(144L),
                ItemList.Battery_RE_ULV_Tantalum.get(8L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfLife1", 4L, 0),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfLife2", 1L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfLife2", 1L, 0),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfLife1", 4L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 16),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20),
                Materials.Redstone.getMolten(144L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 23),
                64,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 17),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20),
                Materials.Redstone.getMolten(144L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 24),
                64,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 18),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 20),
                Materials.Redstone.getMolten(144L),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 22),
                64,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 1L),
                new ItemStack(Blocks.sand, 1, 32767),
                GT_Values.NF,
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 0),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 1L),
                new ItemStack(Blocks.sand, 1, 32767),
                GT_Values.NF,
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 600),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Fluix, 1L),
                new ItemStack(Blocks.sand, 1, 32767),
                GT_Values.NF,
                getModItem(GT_MachineRecipeLoader.aTextAE, "item.ItemCrystalSeed", 2L, 1200),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.FR_Wax.get(6L),
                new ItemStack(Items.string, 1, 32767),
                Materials.Water.getFluid(600L),
                getModItem(GT_MachineRecipeLoader.aTextForestry, "candle", 24L, 0),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.FR_Wax.get(2L),
                ItemList.FR_Silk.get(1L),
                Materials.Water.getFluid(200L),
                getModItem(GT_MachineRecipeLoader.aTextForestry, "candle", 8L, 0),
                16,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.FR_Silk.get(9L),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Water.getFluid(500L),
                getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 1L, 3),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "propolis", 5L, 2),
                GT_Utility.getIntegratedCircuit(5),
                GT_Values.NF,
                getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 1L, 1),
                16,
                8);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "sturdyMachine", 1L, 0),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 4L),
                Materials.Water.getFluid(5000L),
                ItemList.FR_Casing_Hardened.get(1L),
                64,
                32);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L),
                GT_Utility.getIntegratedCircuit(8),
                GT_Values.NF,
                ItemList.FR_Casing_Sturdy.get(1L),
                32,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Blocks.wool, 1, 32767),
                Materials.Creosote.getFluid(1000L),
                new ItemStack(Blocks.torch, 6, 0),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "craftingMaterial", 5L, 1),
                GT_Utility.getIntegratedCircuit(5),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                64,
                8);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.piston, 1, 32767),
                new ItemStack(Items.slime_ball, 1, 32767),
                GT_Values.NF,
                new ItemStack(Blocks.sticky_piston, 1, 0),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.piston, 1, 32767),
                ItemList.IC2_Resin.get(1L),
                GT_Values.NF,
                new ItemStack(Blocks.sticky_piston, 1, 0),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.piston, 1, 32767),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Glue.getFluid(100L),
                new ItemStack(Blocks.sticky_piston, 1, 0),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 3L),
                        GT_ModHandler.getIC2Item("carbonMesh", 3L), GT_Utility.getIntegratedCircuit(1) },
                Materials.Glue.getFluid(300L),
                ItemList.Duct_Tape.get(1L),
                100,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 2L),
                        GT_ModHandler.getIC2Item("carbonMesh", 2L), GT_Utility.getIntegratedCircuit(2) },
                Materials.Glue.getFluid(200L),
                ItemList.Duct_Tape.get(1L),
                100,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1L),
                        GT_ModHandler.getIC2Item("carbonMesh", 1L), GT_Utility.getIntegratedCircuit(3) },
                Materials.Glue.getFluid(100L),
                ItemList.Duct_Tape.get(1L),
                100,
                (int) TierEU.RECIPE_EV);
        // Maintenance Hatch Recipes Using BrainTech + Refined Glue. Info: One BrainTech Recipe Is In GT+ü Originally.
        // The Maintenance Hatch Recipe using SuperGlue is there.
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LV.get(1), ItemList.Duct_Tape.get(2),
                        GT_Utility.getIntegratedCircuit(2) },
                Materials.Glue.getFluid(1000L),
                ItemList.Hatch_Maintenance.get(1L),
                100,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L),
                new ItemStack(Items.leather, 1, 32767),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L),
                getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Paper, 1L),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Paper_Printed_Pages.get(1L),
                new ItemStack(Items.leather, 1, 32767),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.written_book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Paper_Printed_Pages.get(1L),
                getModItem(MOD_ID_DC, "item.ArtificialLeather", 1L, 0),
                Materials.Glue.getFluid(20L),
                new ItemStack(Items.written_book, 1, 0),
                32,
                8);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 4L) },
                GT_Values.NF,
                ItemList.Cell_Universal_Fluid.get(1L),
                128,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Baked_Cake.get(1L),
                new ItemStack(Items.egg, 1, 0),
                Materials.Milk.getFluid(3000L),
                new ItemStack(Items.cake, 1, 0),
                100,
                8);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Bun.get(2L),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                ItemList.Food_Sliced_Buns.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Bread.get(2L),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                ItemList.Food_Sliced_Breads.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Baguette.get(2L),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                ItemList.Food_Sliced_Baguettes.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Buns.get(1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                ItemList.Food_Sliced_Bun.get(2L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Breads.get(1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                ItemList.Food_Sliced_Bread.get(2L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Baguettes.get(1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                ItemList.Food_Sliced_Baguette.get(2L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Bun.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L),
                GT_Values.NF,
                ItemList.Food_Burger_Meat.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Buns.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L),
                GT_Values.NF,
                ItemList.Food_Burger_Meat.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Bun.get(2L),
                ItemList.Food_Chum.get(1L),
                GT_Values.NF,
                ItemList.Food_Burger_Chum.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Buns.get(1L),
                ItemList.Food_Chum.get(1L),
                GT_Values.NF,
                ItemList.Food_Burger_Chum.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Bun.get(2L),
                ItemList.Food_Sliced_Cheese.get(3L),
                GT_Values.NF,
                ItemList.Food_Burger_Cheese.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Sliced_Buns.get(1L),
                ItemList.Food_Sliced_Cheese.get(3L),
                GT_Values.NF,
                ItemList.Food_Burger_Cheese.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Flat_Dough.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L),
                GT_Values.NF,
                ItemList.Food_Raw_Pizza_Meat.get(1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Food_Flat_Dough.get(1L),
                ItemList.Food_Sliced_Cheese.get(3L),
                GT_Values.NF,
                ItemList.Food_Raw_Pizza_Cheese.get(1L),
                100,
                4);

        // SC craft
        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] {
                                GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2L),
                                ItemList.Electric_Pump_MV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                        Materials.Helium.getGas(2000L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3L),
                        400,
                        (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4L),
                        ItemList.Electric_Pump_HV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(4000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6L),
                400,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6L),
                        ItemList.Electric_Pump_EV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(6000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9L),
                400,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8L),
                        ItemList.Electric_Pump_IV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(8000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12L),
                800,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                                15L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10L),
                        ItemList.Electric_Pump_LuV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(12000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 15L),
                800,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 18L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12L),
                        ItemList.Electric_Pump_ZPM.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(16000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18L),
                1600,
                (int) TierEU.RECIPE_ZPM);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuvwire, 21L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14L),
                        ItemList.Electric_Pump_UV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(20000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21L),
                1600,
                (int) TierEU.RECIPE_UV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuhvwire, 24L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16L),
                        ItemList.Electric_Pump_UHV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                Materials.Helium.getGas(24000L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24L),
                3200,
                (int) TierEU.RECIPE_UHV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lead, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.ULV_Coil.get(1L),
                200,
                8);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.LV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SteelMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Aluminium, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.MV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SteelMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.EnergeticAlloy, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.HV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NeodymiumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.TungstenSteel, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.EV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NeodymiumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iridium, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.IV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.LuV_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.ZPM_Coil.get(1L),
                200,
                (int) TierEU.RECIPE_ZPM);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.ElectrumFlux, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.UV_Coil.get(1L),
                200,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 16L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.UHV_Coil.get(1L),
                200,
                2000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 6L),
                        GT_Utility.getIntegratedCircuit(2) },
                Materials.Glue.getFluid(10),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 2L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 3L),
                        GT_Utility.getIntegratedCircuit(4) },
                Materials.Glue.getFluid(20),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Wood, 4L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1L),
                        GT_Utility.getIntegratedCircuit(12) },
                Materials.Glue.getFluid(60),
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Wood, 6L),
                200,
                (int) TierEU.RECIPE_LV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1L),
                        GT_Utility.getIntegratedCircuit(2) },
                GT_Values.NF,
                ItemList.Ingot_IridiumAlloy.get(1L),
                1200,
                (int) TierEU.RECIPE_IV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1L),
                        ItemList.Electric_Motor_UV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Iridium, 1L),
                        ItemList.Component_Filter.get(8L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 8L), },
                null,
                ItemList.Casing_Vent_T2.get(1L),
                30 * 20,
                (int) TierEU.RECIPE_LuV);

        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "torchesFromCoal", false)) {
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                    new ItemStack(Items.coal, 1, 32767),
                    new ItemStack(Blocks.torch, 4),
                    400,
                    1);
        }
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 2L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1L),
                new ItemStack(Blocks.light_weighted_pressure_plate, 1),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1L),
                new ItemStack(Blocks.heavy_weighted_pressure_plate, 1),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 6L),
                GT_Utility.getIntegratedCircuit(6),
                new ItemStack(Items.iron_door, 1),
                600,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 7L),
                GT_Utility.getIntegratedCircuit(7),
                new ItemStack(Items.cauldron, 1),
                700,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getIC2Item("ironFence", 1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 3L),
                GT_Utility.getIntegratedCircuit(3),
                new ItemStack(Blocks.iron_bars, 4),
                300,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 6L),
                GT_Utility.getIntegratedCircuit(6),
                new ItemStack(Items.iron_door, 1),
                600,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 7L),
                GT_Utility.getIntegratedCircuit(7),
                new ItemStack(Items.cauldron, 1),
                700,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L),
                GT_Utility.getIntegratedCircuit(1),
                GT_ModHandler.getIC2Item("ironFence", 1L),
                100,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 3L),
                GT_Utility.getIntegratedCircuit(3),
                new ItemStack(Blocks.iron_bars, 4),
                300,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L),
                GT_Utility.getIntegratedCircuit(3),
                new ItemStack(Blocks.fence, 1),
                300,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L),
                new ItemStack(Blocks.tripwire_hook, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2L),
                new ItemStack(Blocks.tripwire_hook, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L),
                new ItemStack(Items.string, 3, 32767),
                new ItemStack(Items.bow, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3L),
                ItemList.Component_Minecart_Wheels_Iron.get(2L),
                new ItemStack(Items.minecart, 1),
                500,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 3L),
                ItemList.Component_Minecart_Wheels_Iron.get(2L),
                new ItemStack(Items.minecart, 1),
                400,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 3L),
                ItemList.Component_Minecart_Wheels_Steel.get(2L),
                new ItemStack(Items.minecart, 1),
                300,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L),
                ItemList.Component_Minecart_Wheels_Iron.get(1L),
                500,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2L),
                ItemList.Component_Minecart_Wheels_Iron.get(1L),
                400,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Steel, 2L),
                ItemList.Component_Minecart_Wheels_Steel.get(1L),
                300,
                2);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.minecart, 1),
                new ItemStack(Blocks.hopper, 1, 32767),
                new ItemStack(Items.hopper_minecart, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.minecart, 1),
                new ItemStack(Blocks.tnt, 1, 32767),
                new ItemStack(Items.tnt_minecart, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.minecart, 1),
                new ItemStack(Blocks.chest, 1, 32767),
                new ItemStack(Items.chest_minecart, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.minecart, 1),
                new ItemStack(Blocks.trapped_chest, 1, 32767),
                new ItemStack(Items.chest_minecart, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.minecart, 1),
                new ItemStack(Blocks.furnace, 1, 32767),
                new ItemStack(Items.furnace_minecart, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.tripwire_hook, 1),
                new ItemStack(Blocks.chest, 1, 32767),
                new ItemStack(Blocks.trapped_chest, 1),
                200,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.stone, 1, 0),
                GT_Utility.getIntegratedCircuit(4),
                new ItemStack(Blocks.stonebrick, 1, 0),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.sandstone, 1, 0),
                GT_Utility.getIntegratedCircuit(23),
                new ItemStack(Blocks.sandstone, 1, 2),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.sandstone, 1, 1),
                GT_Utility.getIntegratedCircuit(1),
                new ItemStack(Blocks.sandstone, 1, 0),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.sandstone, 1, 2),
                GT_Utility.getIntegratedCircuit(1),
                new ItemStack(Blocks.sandstone, 1, 0),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 8L),
                GT_Utility.getIntegratedCircuit(8),
                GT_ModHandler.getIC2Item("machine", 1L),
                25,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_ULV.get(1L),
                25,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_LV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_MV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_HV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_EV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_IV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_LuV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_ZPM.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_UV.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L),
                GT_Utility.getIntegratedCircuit(8),
                ItemList.Casing_MAX.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Invar, 1L),
                ItemList.Casing_HeatProof.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L),
                ItemList.Casing_SolidSteel.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1L),
                ItemList.Casing_FrostProof.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L),
                ItemList.Casing_RobustTungstenSteel.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1L),
                ItemList.Casing_CleanStainlessSteel.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1L),
                ItemList.Casing_StableTitanium.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 1L),
                ItemList.Casing_MiningOsmiridium.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                ItemList.Casing_MiningNeutronium.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 1L),
                ItemList.Casing_MiningBlackPlutonium.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 4L),
                ItemList.Casing_LuV.get(1L),
                Materials.HSSG.getMolten(288),
                ItemList.Casing_Fusion.get(1L),
                100,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 4L),
                ItemList.Casing_Fusion.get(1L),
                Materials.NaquadahAlloy.getMolten(288),
                ItemList.Casing_Fusion2.get(1L),
                200,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Magnalium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueSteel, 1L),
                ItemList.Casing_Turbine.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6L),
                ItemList.Casing_Turbine.get(1L),
                ItemList.Casing_Turbine1.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6L),
                ItemList.Casing_Turbine.get(1L),
                ItemList.Casing_Turbine2.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L),
                ItemList.Casing_Turbine.get(1L),
                ItemList.Casing_Turbine3.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6L),
                ItemList.Casing_Turbine.get(1L),
                ItemList.Casing_TurbineGasAdvanced.get(1L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.Casing_SolidSteel.get(1),
                GT_Utility.getIntegratedCircuit(6),
                Materials.Polytetrafluoroethylene.getMolten(216),
                ItemList.Casing_Chemically_Inert.get(1),
                50,
                16);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1L),
                ItemList.Casing_Advanced_Iridium.get(1L),
                50,
                16);

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L),
                    ItemList.Casing_ULV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_ULV.get(1L),
                    25,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L),
                    ItemList.Casing_LV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_LV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L),
                    ItemList.Casing_MV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_MV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L),
                    ItemList.Casing_MV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_MV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L),
                    ItemList.Casing_HV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_HV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2L),
                    ItemList.Casing_EV.get(1L),
                    Materials.Plastic.getMolten(288),
                    ItemList.Hull_EV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2L),
                    ItemList.Casing_IV.get(1L),
                    Materials.Polytetrafluoroethylene.getMolten(288),
                    ItemList.Hull_IV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L),
                    ItemList.Casing_LuV.get(1L),
                    Materials.Polytetrafluoroethylene.getMolten(288),
                    ItemList.Hull_LuV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L),
                    ItemList.Casing_ZPM.get(1L),
                    Materials.Polybenzimidazole.getMolten(288),
                    ItemList.Hull_ZPM.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L),
                    ItemList.Casing_UV.get(1L),
                    Materials.Polybenzimidazole.getMolten(288),
                    ItemList.Hull_UV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 2L),
                    ItemList.Casing_MAX.get(1L),
                    Materials.Polybenzimidazole.getMolten(288),
                    ItemList.Hull_MAX.get(1L),
                    50,
                    16);
        } else {
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L),
                    ItemList.Casing_ULV.get(1L),
                    ItemList.Hull_ULV.get(1L),
                    25,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L),
                    ItemList.Casing_LV.get(1L),
                    ItemList.Hull_LV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L),
                    ItemList.Casing_MV.get(1L),
                    ItemList.Hull_MV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L),
                    ItemList.Casing_MV.get(1L),
                    ItemList.Hull_MV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L),
                    ItemList.Casing_HV.get(1L),
                    ItemList.Hull_HV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2L),
                    ItemList.Casing_EV.get(1L),
                    ItemList.Hull_EV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2L),
                    ItemList.Casing_IV.get(1L),
                    ItemList.Hull_IV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L),
                    ItemList.Casing_LuV.get(1L),
                    ItemList.Hull_LuV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L),
                    ItemList.Casing_ZPM.get(1L),
                    ItemList.Hull_ZPM.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L),
                    ItemList.Casing_UV.get(1L),
                    ItemList.Hull_UV.get(1L),
                    50,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 2L),
                    ItemList.Casing_MAX.get(1L),
                    ItemList.Hull_MAX.get(1L),
                    50,
                    16);
        }

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 1L),
                Materials.Plastic.getMolten(144),
                ItemList.Battery_Hull_LV.get(1L),
                800,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3L),
                Materials.Plastic.getMolten(432),
                ItemList.Battery_Hull_MV.get(1L),
                1600,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3L),
                Materials.Plastic.getMolten(432),
                ItemList.Battery_Hull_MV.get(1L),
                1600,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 9L),
                Materials.Plastic.getMolten(1296),
                ItemList.Battery_Hull_HV.get(1L),
                3200,
                4);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Items.string, 4, 32767),
                new ItemStack(Items.slime_ball, 1, 32767),
                new ItemStack(Items.lead, 2),
                200,
                2);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.IC2_Compressed_Coal_Ball.get(8L),
                new ItemStack(Blocks.brick_block, 1),
                ItemList.IC2_Compressed_Coal_Chunk.get(1L),
                400,
                4);

        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getIC2Item("waterMill", 2L),
                GT_Utility.getIntegratedCircuit(2),
                GT_ModHandler.getIC2Item("generator", 1L),
                6400,
                8);
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getIC2Item("batPack", 1L, 32767),
                GT_Utility.getIntegratedCircuit(1),
                ItemList.IC2_ReBattery.get(6L),
                800,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_ModHandler.getIC2Item("carbonFiber", 2L),
                GT_Utility.getIntegratedCircuit(2),
                GT_ModHandler.getIC2Item("carbonMesh", 1L),
                800,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 4L),
                GT_ModHandler.getIC2Item("generator", 1L),
                GT_ModHandler.getIC2Item("waterMill", 2L),
                6400,
                8);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5L),
                new ItemStack(Blocks.chest, 1, 32767),
                new ItemStack(Blocks.hopper),
                800,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5L),
                new ItemStack(Blocks.trapped_chest, 1, 32767),
                new ItemStack(Blocks.hopper),
                800,
                2);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5L),
                new ItemStack(Blocks.chest, 1, 32767),
                new ItemStack(Blocks.hopper),
                800,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5L),
                new ItemStack(Blocks.trapped_chest, 1, 32767),
                new ItemStack(Blocks.hopper),
                800,
                2);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Magnalium, 2L),
                GT_ModHandler.getIC2Item("generator", 1L),
                GT_ModHandler.getIC2Item("windMill", 1L),
                6400,
                8);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                new ItemStack(Items.blaze_powder, 1, 0),
                new ItemStack(Items.ender_eye, 1, 0),
                400,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 6L),
                new ItemStack(Items.blaze_rod, 1, 0),
                new ItemStack(Items.ender_eye, 6, 0),
                2500,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CobaltBrass, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                ItemList.Component_Sawblade_Diamond.get(1L),
                1600,
                2);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L),
                new ItemStack(Blocks.redstone_lamp, 1),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Blocks.redstone_torch, 1),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4L),
                new ItemStack(Items.compass, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 4L),
                new ItemStack(Items.compass, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 4L),
                new ItemStack(Items.clock, 1),
                400,
                4);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                new ItemStack(Blocks.torch, 2),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                new ItemStack(Blocks.torch, 6),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                ItemList.IC2_Resin.get(1L),
                new ItemStack(Blocks.torch, 6),
                400,
                1);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 8L),
                new ItemStack(Items.flint, 1),
                ItemList.IC2_Compressed_Coal_Ball.get(1L),
                400,
                4);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getIC2Item("tinCableItem", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L),
                    GT_ModHandler.getIC2Item("insulatedTinCableItem", 1L),
                    100,
                    2);
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getIC2Item("copperCableItem", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L),
                    GT_ModHandler.getIC2Item("insulatedCopperCableItem", 1L),
                    100,
                    2);
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getIC2Item("goldCableItem", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L),
                    GT_ModHandler.getIC2Item("insulatedGoldCableItem", 1L),
                    200,
                    2);
            GT_Values.RA.addAssemblerRecipe(
                    GT_ModHandler.getIC2Item("ironCableItem", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 3L),
                    GT_ModHandler.getIC2Item("insulatedIronCableItem", 1L),
                    300,
                    2);
        }
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Items.wooden_sword, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Stone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Items.stone_sword, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Items.iron_sword, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Items.golden_sword, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Diamond, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                new ItemStack(Items.diamond_sword, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Bronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                ItemList.Tool_Sword_Bronze.getUndamaged(1L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                ItemList.Tool_Sword_Steel.getUndamaged(1L),
                100,
                16);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.wooden_pickaxe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Stone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.stone_pickaxe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.iron_pickaxe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.golden_pickaxe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Diamond, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.diamond_pickaxe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Bronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Pickaxe_Bronze.getUndamaged(1L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Pickaxe_Steel.getUndamaged(1L),
                100,
                16);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.wooden_shovel, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Stone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.stone_shovel, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.iron_shovel, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.golden_shovel, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Diamond, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.diamond_shovel, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Bronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Shovel_Bronze.getUndamaged(1L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Shovel_Steel.getUndamaged(1L),
                100,
                16);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.wooden_axe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Stone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.stone_axe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.iron_axe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.golden_axe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Diamond, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.diamond_axe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Bronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Axe_Bronze.getUndamaged(1L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Axe_Steel.getUndamaged(1L),
                100,
                16);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.wooden_hoe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Stone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.stone_hoe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.iron_hoe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.golden_hoe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Diamond, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                new ItemStack(Items.diamond_hoe, 1),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Bronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Hoe_Bronze.getUndamaged(1L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L),
                ItemList.Tool_Hoe_Steel.getUndamaged(1L),
                100,
                16);

        // fuel rod assembler recipes
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ThoriumCell_1.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                ItemList.ThoriumCell_2.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ThoriumCell_1.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6L),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemList.ThoriumCell_4.get(1L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ThoriumCell_2.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                ItemList.ThoriumCell_4.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Uraniumcell_1.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                ItemList.Uraniumcell_2.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Uraniumcell_1.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6L),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemList.Uraniumcell_4.get(1L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Uraniumcell_2.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                ItemList.Uraniumcell_4.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Moxcell_1.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                ItemList.Moxcell_2.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Moxcell_1.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6L),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemList.Moxcell_4.get(1L),
                300,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Moxcell_2.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4L),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                ItemList.Moxcell_4.get(1L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.NaquadahCell_1.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                ItemList.NaquadahCell_2.get(1L),
                100,
                400);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.NaquadahCell_1.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6L),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemList.NaquadahCell_4.get(1L),
                150,
                400);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.NaquadahCell_2.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                ItemList.NaquadahCell_4.get(1L),
                100,
                400);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.MNqCell_1.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                ItemList.MNqCell_2.get(1L),
                100,
                400);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.MNqCell_1.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6L),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                ItemList.MNqCell_4.get(1L),
                150,
                400);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.MNqCell_2.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                ItemList.MNqCell_4.get(1L),
                100,
                400);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 4L),
                ItemList.neutroniumHeatCapacitor.get(1L),
                100,
                120000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { Materials.NaquadahAlloy.getPlates(8),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1) },
                null,
                ItemList.RadiantNaquadahAlloyCasing.get(1),
                10,
                400000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.KevlarFiber.get(8L), GT_Utility.getIntegratedCircuit(8) },
                GT_Values.NF,
                ItemList.WovenKevlar.get(1L),
                300,
                (int) TierEU.RECIPE_EV);

        for (Materials tMat : Materials.values()) {
            if (tMat.isProperSolderingFluid()) {
                int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                        : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
                for (ItemStack tPlate : new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L) }) {
                    GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { new ItemStack(Blocks.lever, 1, 32767), tPlate,
                                    GT_Utility.getIntegratedCircuit(1) },
                            tMat.getMolten(144L * tMultiplier / 2L),
                            ItemList.Cover_Controller.get(1L),
                            800,
                            16);
                    GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { new ItemStack(Blocks.redstone_torch, 1, 32767), tPlate,
                                    GT_Utility.getIntegratedCircuit(1) },
                            tMat.getMolten(144L * tMultiplier / 2L),
                            ItemList.Cover_ActivityDetector.get(1L),
                            800,
                            16);
                    GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767), tPlate,
                                    GT_Utility.getIntegratedCircuit(1) },
                            tMat.getMolten(144L * tMultiplier / 2L),
                            ItemList.Cover_FluidDetector.get(1L),
                            800,
                            16);
                    GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767), tPlate,
                                    GT_Utility.getIntegratedCircuit(1) },
                            tMat.getMolten(144L * tMultiplier / 2L),
                            ItemList.Cover_ItemDetector.get(1L),
                            800,
                            16);
                    GT_Values.RA.addAssemblerRecipe(
                            new ItemStack[] { GT_ModHandler.getIC2Item("ecMeter", 1L), tPlate,
                                    GT_Utility.getIntegratedCircuit(1) },
                            tMat.getMolten(144L * tMultiplier / 2L),
                            ItemList.Cover_EnergyDetector.get(1L),
                            800,
                            16);
                }
            }
        }
    }

    /**
     * Adds recipes for input buses from ULV to UHV
     */
    public void loadInputBusesRecipes(){
        // ULV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(1*INGOTS)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1* HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1* EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);
        }

        // LV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(5 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(9)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);
        }

        // MV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);
        }

        // HV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(3 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * QUARTER_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);
        }

        // EV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 4),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 4),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);
        }

        // IV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1* INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);
        }

        // LuV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 2),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 2),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);
        }

        // ZPM input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 2, 5),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_ZPM.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(sAssemblerRecipes);
        }

        //UV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 2, 6),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_UV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(sAssemblerRecipes);
        }

        // UHV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    getModItem(AvaritiaAddons.modID, "CompressedChest", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_Bus_MAX.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(sAssemblerRecipes);
        }
    }

    /**
     * Adds recipes for output buses from ULV to UHV
     */
    public void loadOutputBusesRecipes(){

        /*  those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
            means we aren't in full pack */

        if (!NewHorizonsCoreMod.isModLoaded()){
            return;
        }

        // ULV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.modID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);
        }

        // LV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(5 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    new ItemStack(Blocks.chest),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(9)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);
        }

        if (!IronChests.isModLoaded()){
            return;
        }

        // MV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);
        }

        // HV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(3 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*QUARTER_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);
        }

        // EV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1,4),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 4),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);
        }

        // IV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest",1, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);
        }

        // LuV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1,2),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 1,2),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);
        }

        // ZPM output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 2, 5),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ZPM.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(sAssemblerRecipes);
        }

        // UV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    getModItem(IronChests.modID, "BlockIronChest", 2, 6),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_UV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(sAssemblerRecipes);
        }

        if (!AvaritiaAddons.isModLoaded()){
            return;
        }

        // UHV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    getModItem(AvaritiaAddons.modID, "CompressedChest", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MAX.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(sAssemblerRecipes);
        }
    }

    /**
     * Adds recipes for input hatches from ULV to UHV
     */
    public void loadInputHatchesRecipes(){
        // ULV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(1*INGOTS)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1* HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1* EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4)
                )
                .noFluidOutputs()
                .duration(24* SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);
        }

        /*  those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
            means we aren't in full pack */

        if(!BuildCraftFactory.isModLoaded()){
            return;
        }

        // LV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(5 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(9)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);
        }

        if (!IronTanks.isModLoaded()){
            return;
        }

        // MV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_MV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);
        }

        // HV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_HV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(3 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * QUARTER_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);
        }

        // EV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.modID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.modID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);
        }

        // IV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.modID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.modID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1* INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);
        }

        // LuV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.modID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.modID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);
        }

        // ZPM input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronTanks.modID, "obsidianTank", 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_ZPM.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(sAssemblerRecipes);
        }

        //UV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    ItemList.Super_Tank_LV.get(1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_UV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(sAssemblerRecipes);
        }

        // UHV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    ItemList.Super_Tank_MV.get(1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Hatch_Input_MAX.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(sAssemblerRecipes);
        }
    }

    /**
     * Adds recipes for output hatches from ULV to UHV
     */
    public void loadOutputHatchesRecipes(){
        // ULV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ULV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(sAssemblerRecipes);
        }

        /*  those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
            means we aren't in full pack */

        if (!BuildCraftFactory.isModLoaded()){
            return;
        }

        // LV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Glue.getFluid(5 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.modID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(9)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sAssemblerRecipes);
        }

        if (!IronTanks.isModLoaded()){
            return;
        }

        // MV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.modID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * EIGHTH_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(sAssemblerRecipes);
        }

        // HV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Plastic.getFluid(3 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.modID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_HV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*QUARTER_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sAssemblerRecipes);
        }

        // EV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.modID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.modID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_EV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1*HALF_INGOT)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(sAssemblerRecipes);
        }

        // IV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.modID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.modID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_IV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(1 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sAssemblerRecipes);
        }

        // LuV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.modID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polytetrafluoroethylene.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.modID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_LuV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(2 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(sAssemblerRecipes);
        }

        // ZPM output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronTanks.modID, "obsidianTank", 1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_ZPM.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(4 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(sAssemblerRecipes);
        }

        // UV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    ItemList.Super_Tank_LV.get(1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_UV.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(8 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(sAssemblerRecipes);
        }

        // UHV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    ItemList.Super_Tank_MV.get(1),
                    GT_Utility.getIntegratedCircuit(2)
                )
                .itemOutputs(
                    ItemList.Hatch_Output_Bus_MAX.get(1)
                )
                .fluidInputs(
                    Materials.Polybenzimidazole.getFluid(16 * INGOTS)
                )
                .noFluidOutputs()
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(sAssemblerRecipes);
        }
    }

    /**
     * Load all Railcraft recipes for GT Machines
     */
    private void withRailcraft() {
        if (!Railcraft.isModLoaded()) return;
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 1, 0), ItemList.RC_Rebar.get(1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Tie_Stone.get(1L),
                128,
                8);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 1, 7), ItemList.RC_Rebar.get(1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Tie_Stone.get(1L),
                128,
                8);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(8) },
                Materials.Blaze.getMolten(216L),
                ItemList.RC_Rail_HS.get(16L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(9) },
                Materials.ConductiveIron.getMolten(432L),
                ItemList.RC_Rail_HS.get(8L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(9) },
                Materials.VibrantAlloy.getMolten(216L),
                ItemList.RC_Rail_HS.get(32L),
                100,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(9) },
                Materials.CrystallineAlloy.getMolten(216L),
                ItemList.RC_Rail_HS.get(64L),
                100,
                48);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(8) },
                Materials.Redstone.getMolten(216L),
                ItemList.RC_Rail_Adv.get(8L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(8) },
                Materials.RedAlloy.getMolten(216L),
                ItemList.RC_Rail_Adv.get(16L),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(8) },
                Materials.ConductiveIron.getMolten(216L),
                ItemList.RC_Rail_Adv.get(32L),
                100,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(3L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L),
                        GT_Utility.getIntegratedCircuit(8) },
                Materials.VibrantAlloy.getMolten(216L),
                ItemList.RC_Rail_Adv.get(64L),
                100,
                48);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(1L),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(1L),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(2L),
                50,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(4L),
                50,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(8L),
                50,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(16L),
                50,
                48);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.HSSG, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(32L),
                50,
                64);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Rail_Standard.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                ItemList.RC_Rail_Electric.get(64L),
                50,
                96);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Tie_Wood.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L),
                        GT_Utility.getIntegratedCircuit(10) },
                GT_Values.NF,
                ItemList.RC_Rail_Wooden.get(8L),
                133,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Tie_Wood.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L),
                        GT_Utility.getIntegratedCircuit(11) },
                GT_Values.NF,
                ItemList.RC_Rail_Wooden.get(8L),
                133,
                4);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Tie_Wood.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1L),
                        GT_Utility.getIntegratedCircuit(11) },
                GT_Values.NF,
                ItemList.RC_Rail_Wooden.get(16L),
                133,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Tie_Wood.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 1L),
                        GT_Utility.getIntegratedCircuit(11) },
                GT_Values.NF,
                ItemList.RC_Rail_Wooden.get(32L),
                133,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Tie_Wood.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1L),
                        GT_Utility.getIntegratedCircuit(11) },
                GT_Values.NF,
                ItemList.RC_Rail_Wooden.get(64L),
                133,
                48);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.RC_Tie_Wood.get(32L),
                GT_Utility.getIntegratedCircuit(20),
                ItemList.RC_Bed_Wood.get(24L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.RC_Tie_Wood.get(64L),
                GT_Utility.getIntegratedCircuit(24),
                ItemList.RC_Bed_Wood.get(48L),
                200,
                48);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.RC_Tie_Stone.get(32L),
                GT_Utility.getIntegratedCircuit(20),
                ItemList.RC_Bed_Stone.get(24L),
                200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.RC_Tie_Stone.get(64L),
                GT_Utility.getIntegratedCircuit(24),
                ItemList.RC_Bed_Stone.get(48L),
                200,
                48);
        ItemStack tRailWood = getModItem(MOD_ID_RC, "track", 64, 736);
        if (tRailWood != null) {
            NBTTagCompound tTagWood = new NBTTagCompound();
            tTagWood.setString("track", "railcraft:track.slow");
            tRailWood.stackTagCompound = tTagWood;

            ItemStack tRailWoodB = getModItem(MOD_ID_RC, "track.slow", 16);
            NBTTagCompound tTagWoodB = new NBTTagCompound();
            tTagWoodB.setString("track", "railcraft:track.slow.boost");
            tRailWoodB.stackTagCompound = tTagWoodB;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.RC_Bed_Wood.get(1L), ItemList.RC_Rail_Wooden.get(6L),
                            GT_Utility.getIntegratedCircuit(21) },
                    GT_Values.NF,
                    tRailWood,
                    100,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L),
                            GT_Utility.getIntegratedCircuit(22) },
                    GT_Values.NF,
                    tRailWoodB,
                    200,
                    (int) TierEU.RECIPE_LV);
        }
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.rail, 1, 0), ItemList.RC_Rail_Adv.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L),
                        GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                new ItemStack(Blocks.golden_rail, 16, 0),
                300,
                (int) TierEU.RECIPE_LV);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Bed_Wood.get(1L), ItemList.RC_Rail_Standard.get(6L),
                        GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                new ItemStack(Blocks.rail, 64, 0),
                200,
                (int) TierEU.RECIPE_LV);

        ItemStack tRailRe = getModItem(MOD_ID_RC, "track", 64);
        NBTTagCompound tTagRe = new NBTTagCompound();
        tTagRe.setString("track", "railcraft:track.reinforced");
        tRailRe.stackTagCompound = tTagRe;

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Bed_Stone.get(1L), ItemList.RC_Rail_Reinforced.get(6L),
                        GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                tRailRe,
                200,
                (int) TierEU.RECIPE_LV);

        ItemStack tRailReB = getModItem(MOD_ID_RC, "track.reinforced", 16);
        NBTTagCompound tTagReB = new NBTTagCompound();
        tTagReB.setString("track", "railcraft:track.reinforced.boost");
        tRailReB.stackTagCompound = tTagReB;

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L),
                        GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                tRailReB,
                300,
                (int) TierEU.RECIPE_LV);

        ItemStack tRailEl = getModItem(MOD_ID_RC, "track", 64);
        NBTTagCompound tTagEl = new NBTTagCompound();
        tTagEl.setString("track", "railcraft:track.electric");
        tRailEl.stackTagCompound = tTagEl;

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.RC_Bed_Stone.get(1L), ItemList.RC_Rail_Electric.get(6L),
                        GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                tRailEl,
                200,
                (int) TierEU.RECIPE_LV);

        ItemStack tRailHs = getModItem(MOD_ID_RC, "track", 64, 816);
        if (tRailHs != null) {
            NBTTagCompound tTagHs = new NBTTagCompound();
            tTagHs.setString("track", "railcraft:track.speed");
            tRailHs.stackTagCompound = tTagHs;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.RC_Bed_Stone.get(1L), ItemList.RC_Rail_HS.get(6L),
                            GT_Utility.getIntegratedCircuit(21) },
                    GT_Values.NF,
                    tRailHs,
                    200,
                    (int) TierEU.RECIPE_LV);
        }
        ItemStack tRailHsB = getModItem(MOD_ID_RC, "track.speed", 16);
        NBTTagCompound tTagHsB = new NBTTagCompound();
        tTagHsB.setString("track", "railcraft:track.speed.boost");
        tRailHsB.stackTagCompound = tTagHsB;

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L),
                        GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                tRailHsB,
                300,
                (int) TierEU.RECIPE_LV);

        // --- Wooden Switch Track ---
        ItemStack tRailSS = getModItem(MOD_ID_RC, "track.slow", 1, 19986);
        if (tRailSS != null) {
            NBTTagCompound tTagSS = new NBTTagCompound();
            tTagSS.setString("track", "railcraft:track.slow.switch");
            tRailSS.stackTagCompound = tTagSS;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.AnyIron, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailSS,
                    100,
                    8);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailSS),
                    100,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSS),
                    100,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSS),
                    100,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSS),
                    100,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSS),
                    100,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSS),
                    100,
                    256);
        }
        // --- Wooden Wye Track ---
        ItemStack tRailSW = getModItem(MOD_ID_RC, "track.slow", 1);
        if (tRailSW != null) {
            NBTTagCompound tTagSW = new NBTTagCompound();
            tTagSW.setString("track", "railcraft:track.slow.wye");
            tRailSW.stackTagCompound = tTagSW;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.AnyIron, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailSW,
                    100,
                    8);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailSW),
                    100,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSW),
                    100,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSW),
                    100,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSW),
                    100,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSW),
                    100,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSW),
                    100,
                    256);
        }
        // --- Wooden Junction Tack ---
        ItemStack tRailSJ = getModItem(MOD_ID_RC, "track.slow", 1);
        if (tRailSJ != null) {
            NBTTagCompound tTagSJ = new NBTTagCompound();
            tTagSJ.setString("track", "railcraft:track.slow.junction");
            tRailSJ.stackTagCompound = tTagSJ;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.AnyIron, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailSJ,
                    100,
                    8);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailSJ),
                    100,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSJ),
                    100,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSJ),
                    100,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSJ),
                    100,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSJ),
                    100,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailWood),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSJ),
                    100,
                    256);
        }
        // --- Switch Tack ---
        ItemStack tRailNS = getModItem(MOD_ID_RC, "track", 1, 4767);
        if (tRailNS != null) {
            NBTTagCompound tTagNS = new NBTTagCompound();
            tTagNS.setString("track", "railcraft:track.switch");
            tRailNS.stackTagCompound = tTagNS;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailNS,
                    200,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailNS),
                    200,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailNS),
                    200,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailNS),
                    200,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailNS),
                    200,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailNS),
                    200,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailNS),
                    200,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Wye Tack ---
        ItemStack tRailNW = getModItem(MOD_ID_RC, "track", 1, 2144);
        if (tRailNW != null) {
            NBTTagCompound tTagNW = new NBTTagCompound();
            tTagNW.setString("track", "railcraft:track.wye");
            tRailNW.stackTagCompound = tTagNW;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailNW,
                    200,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailNW),
                    200,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailNW),
                    200,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailNW),
                    200,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailNW),
                    200,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailNW),
                    200,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailNW),
                    200,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Junction Tack ---
        ItemStack tRailNJ = getModItem(MOD_ID_RC, "track", 1);
        if (tRailNJ != null) {
            NBTTagCompound tTagNJ = new NBTTagCompound();
            tTagNJ.setString("track", "railcraft:track.junction");
            tRailNJ.stackTagCompound = tTagNJ;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailNJ,
                    200,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailNJ),
                    200,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailNJ),
                    200,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailNJ),
                    200,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailNJ),
                    200,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailNJ),
                    200,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { new ItemStack(Blocks.rail, 2, 0),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailNJ),
                    200,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Reinforced Switch Track ---
        ItemStack tRailRS = getModItem(MOD_ID_RC, "track.reinforced", 1);
        if (tRailRS != null) {
            NBTTagCompound tTagRS = new NBTTagCompound();
            tTagRS.setString("track", "railcraft:track.reinforced.switch");
            tRailRS.stackTagCompound = tTagRS;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailRS,
                    300,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailRS),
                    300,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailRS),
                    300,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailRS),
                    300,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailRS),
                    300,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailRS),
                    300,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailRS),
                    300,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Reinforced Wye Track ---
        ItemStack tRailRW = getModItem(MOD_ID_RC, "track.reinforced", 1);
        if (tRailRW != null) {
            NBTTagCompound tTagRW = new NBTTagCompound();
            tTagRW.setString("track", "railcraft:track.reinforced.wye");
            tRailRW.stackTagCompound = tTagRW;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailRW,
                    300,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailRW),
                    300,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailRW),
                    300,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailRW),
                    300,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailRW),
                    300,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailRW),
                    300,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailRW),
                    300,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Reinforced Junction Track ---
        ItemStack tRailRJ = getModItem(MOD_ID_RC, "track.reinforced", 1, 764);
        if (tRailRJ != null) {
            NBTTagCompound tTagRJ = new NBTTagCompound();
            tTagRJ.setString("track", "railcraft:track.reinforced.junction");
            tRailRJ.stackTagCompound = tTagRJ;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailRJ,
                    300,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailRJ),
                    300,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailRJ),
                    300,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailRJ),
                    300,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailRJ),
                    300,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailRJ),
                    300,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailRe),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailRJ),
                    300,
                    (int) TierEU.RECIPE_HV);
        }
        // --- H.S. Switch Track ---
        ItemStack tRailSSw = getModItem(MOD_ID_RC, "track.speed", 1, 7916);
        if (tRailSSw != null) {
            NBTTagCompound tTagRSSw = new NBTTagCompound();
            tTagRSSw.setString("track", "railcraft:track.speed.switch");
            tRailSSw.stackTagCompound = tTagRSSw;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailSSw,
                    400,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailSSw),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSSw),
                    400,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSSw),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSSw),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSSw),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSSw),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // --- H.S. Wye Track ---
        ItemStack tRailSWy = getModItem(MOD_ID_RC, "track.speed", 1);
        if (tRailSWy != null) {
            NBTTagCompound tTagRSWy = new NBTTagCompound();
            tTagRSWy.setString("track", "railcraft:track.speed.wye");
            tRailSWy.stackTagCompound = tTagRSWy;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailSWy,
                    400,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailSWy),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSWy),
                    400,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSWy),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSWy),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSWy),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSWy),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // --- H.S. Transition Track ---
        ItemStack tRailSTt = getModItem(MOD_ID_RC, "track.speed", 1, 26865);
        if (tRailSTt != null) {
            NBTTagCompound tTagRSTt = new NBTTagCompound();
            tTagRSTt.setString("track", "railcraft:track.speed.transition");
            tRailSTt.stackTagCompound = tTagRSTt;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs), ItemList.RC_Bed_Stone.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailSTt),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs), ItemList.RC_Bed_Stone.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ConductiveIron, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailSTt),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs), ItemList.RC_Bed_Stone.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailSTt),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs), ItemList.RC_Bed_Stone.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailSTt),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailHs), ItemList.RC_Bed_Stone.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.MelodicAlloy, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailSTt),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Electric Switch Track ---
        ItemStack tRailES = getModItem(MOD_ID_RC, "track.electric", 1, 10488);
        if (tRailES != null) {
            NBTTagCompound tTagES = new NBTTagCompound();
            tTagES.setString("track", "railcraft:track.electric.switch");
            tRailES.stackTagCompound = tTagES;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Copper, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailES,
                    400,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Gold, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailES),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Electrum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailES),
                    400,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailES),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Platinum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailES),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.VanadiumGallium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailES),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Naquadah, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailES),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Electric Wye Track ---
        ItemStack tRailEw = getModItem(MOD_ID_RC, "track.electric", 1);
        if (tRailEw != null) {
            NBTTagCompound tTagEw = new NBTTagCompound();
            tTagEw.setString("track", "railcraft:track.electric.wye");
            tRailEw.stackTagCompound = tTagEw;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Copper, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailEw,
                    400,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Gold, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailEw),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Electrum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailEw),
                    400,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailEw),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Platinum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailEw),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.VanadiumGallium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailEw),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Naquadah, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailEw),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // --- Electric Junction Track ---
        ItemStack tRailEJ = getModItem(MOD_ID_RC, "track.electric", 1);
        if (tRailEJ != null) {
            NBTTagCompound tTagREJ = new NBTTagCompound();
            tTagREJ.setString("track", "railcraft:track.electric.junction");
            tRailEJ.stackTagCompound = tTagREJ;

            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Copper, 4L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    tRailEJ,
                    400,
                    16);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Gold, 2L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(2, tRailEJ),
                    400,
                    (int) TierEU.RECIPE_LV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(4, tRailEJ),
                    400,
                    48);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(8, tRailEJ),
                    400,
                    64);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Platinum, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(16, tRailEJ),
                    400,
                    (int) TierEU.RECIPE_MV);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.VanadiumGallium, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(32, tRailEJ),
                    400,
                    256);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.copyAmount(2, tRailEl),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Naquadah, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    GT_Values.NF,
                    GT_Utility.copyAmount(64, tRailEJ),
                    400,
                    (int) TierEU.RECIPE_HV);
        }
        // Shunting Wire
        for (Materials tMat : Materials.values()) {
            if (tMat.isProperSolderingFluid()) {
                int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                        : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(1L),
                        200,
                        16);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(1L),
                        200,
                        16);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(4L),
                        200,
                        24);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(8L),
                        200,
                        (int) TierEU.RECIPE_LV);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(16L),
                        200,
                        48);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Platinum, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(32L),
                        200,
                        64);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Platinum, 1L),
                                GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(16L * tMultiplier / 2L),
                        ItemList.RC_ShuntingWire.get(64L),
                        200,
                        (int) TierEU.RECIPE_MV);
                // chunkloader upgrade OC
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Circuit_Board_Plastic_Advanced.get(1L),
                                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Aluminium, 2L),
                                getModItem(MOD_ID_RC, "machine.alpha", 1L, 0),
                                getModItem("OpenComputers", "item", 1L, 26), GT_Utility.getIntegratedCircuit(1) },
                        tMat.getMolten(144L * tMultiplier / 2L),
                        getModItem("OpenComputers", "item", 1L, 62),
                        250,
                        256);
            }
        }

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.piston, 1, 0), ItemList.FR_Casing_Sturdy.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.SeedOil.getFluid(250L),
                getModItem(MOD_ID_DC, "item.EngineCore", 1L, 0),
                100,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.piston, 1, 0), ItemList.FR_Casing_Sturdy.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Lubricant.getFluid(125L),
                getModItem(MOD_ID_DC, "item.EngineCore", 1L, 0),
                100,
                16);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem("ExtraUtilities", "trashcan", 1L, 0),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Obsidian, 4L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 11),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem(MOD_ID_DC, "item.EngineCore", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyCopper, 10L),
                        GT_Utility.getIntegratedCircuit(10) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 7),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem(MOD_ID_DC, "item.EngineCore", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 10L),
                        GT_Utility.getIntegratedCircuit(10) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 8),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem(MOD_ID_DC, "item.EngineCore", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 10L),
                        GT_Utility.getIntegratedCircuit(10) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 9),
                200,
                16);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Gold, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 2L),
                        GT_Utility.getIntegratedCircuit(1) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.alpha", 1, 0),
                600,
                (int) TierEU.RECIPE_HV);

        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2L),
                GT_Utility.getIntegratedCircuit(20),
                getModItem(MOD_ID_RC, "machine.beta", 1L, 0),
                600,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 2L, 1),
                800,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 2L, 1),
                800,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Bronze, 2L),
                        new ItemStack(Blocks.iron_bars, 2, 0), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 2),
                800,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2L),
                        GT_Utility.getIntegratedCircuit(20) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 13),
                200,
                64);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 2L, 14),
                400,
                64);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 2L, 14),
                400,
                64);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 2L),
                        getModItem(MOD_ID_DC, "item.SteelBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.beta", 1L, 15),
                400,
                64);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L),
                GT_Utility.getIntegratedCircuit(20),
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 0),
                600,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 1),
                800,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 1),
                800,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Plastic, 2L),
                        getModItem(MOD_ID_DC, "item.AluminiumBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 2),
                800,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L),
                        GT_Utility.getIntegratedCircuit(20) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 3),
                200,
                256);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 4),
                400,
                256);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 4),
                400,
                256);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 2L),
                        getModItem(MOD_ID_DC, "item.StainlessSteelBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 5),
                400,
                256);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2L),
                GT_Utility.getIntegratedCircuit(20),
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 6),
                600,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 7),
                800,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 7),
                800,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 2L),
                        getModItem(MOD_ID_DC, "item.TitaniumBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 8),
                800,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2L),
                        GT_Utility.getIntegratedCircuit(20) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 9),
                200,
                960);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 10),
                400,
                960);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 10),
                400,
                960);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 2L),
                        getModItem(MOD_ID_DC, "item.TungstenSteelBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 11),
                400,
                960);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2L),
                GT_Utility.getIntegratedCircuit(20),
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 12),
                600,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 13),
                800,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 2L, 13),
                800,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NiobiumTitanium, 2L),
                        getModItem(MOD_ID_DC, "item.ChromeBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 14),
                800,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2L),
                        GT_Utility.getIntegratedCircuit(20) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 1L, 0),
                200,
                4096);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 1),
                400,
                4096);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 1),
                400,
                4096);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Enderium, 2L),
                        getModItem(MOD_ID_DC, "item.IridiumBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 1L, 2),
                400,
                4096);
        GT_Values.RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2L),
                GT_Utility.getIntegratedCircuit(20),
                getModItem(MOD_ID_RC, "machine.eta", 1L, 3),
                600,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 4),
                800,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 4),
                800,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2L),
                        getModItem(MOD_ID_DC, "item.OsmiumBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 1L, 5),
                800,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                        GT_Utility.getIntegratedCircuit(20) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 1L, 6),
                200,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                        new ItemStack(Blocks.glass_pane, 2, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 7),
                400,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                        getModItem("TConstruct", "GlassPane", 2L, 0), GT_Utility.getIntegratedCircuit(21) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 2L, 7),
                400,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                        getModItem(MOD_ID_DC, "item.NeutroniumBars", 2L), GT_Utility.getIntegratedCircuit(22) },
                GT_Values.NF,
                getModItem(MOD_ID_RC, "machine.eta", 1L, 8),
                400,
                (int) TierEU.RECIPE_LuV);
        // Water Tank
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2L) },
                Materials.Glue.getFluid(36L),
                getModItem(MOD_ID_RC, "machine.alpha", 1L, 14),
                200,
                8,
                false);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2L) },
                Materials.Glue.getFluid(36L),
                getModItem(MOD_ID_RC, "machine.alpha", 1L, 14),
                200,
                8,
                false);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2L) },
                Materials.Glue.getFluid(36L),
                getModItem(MOD_ID_RC, "machine.alpha", 1L, 14),
                200,
                8,
                false);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 4L) },
                Materials.Glue.getFluid(72L),
                getModItem(MOD_ID_RC, "machine.alpha", 3L, 14),
                400,
                30,
                false);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.WoodSealed, 1L) },
                Materials.Plastic.getMolten(36L),
                getModItem(MOD_ID_RC, "machine.alpha", 3L, 14),
                400,
                30,
                false);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.StainlessSteel, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.WoodSealed, 4L) },
                Materials.Plastic.getMolten(72L),
                getModItem(MOD_ID_RC, "machine.alpha", 9L, 14),
                400,
                120,
                false);
        // Steam Boilers
        GT_Values.RA.addAssemblerRecipe(
                ItemList.IC2_Item_Casing_Iron.get(6L),
                GT_Utility.getIntegratedCircuit(6),
                getModItem(MOD_ID_RC, "machine.beta", 1L, 3),
                400,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
                ItemList.IC2_Item_Casing_Steel.get(6L),
                GT_Utility.getIntegratedCircuit(6),
                getModItem(MOD_ID_RC, "machine.beta", 1L, 4),
                400,
                64);
    }

    public void withBartWorks() {
        if (BartWorks.isModLoaded()) {
            return;
        }

        GT_Values.RA.addAssemblerRecipe(
                getModItem("bartworks", "gt.bwMetaGeneratedplate", 6L, 88),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Chrome, 1L),
                ItemList.Casing_Advanced_Rhodium_Palladium.get(1L),
                50,
                16);

    }

    public void withGalacticraftMars() {
        if (!GalacticraftMars.isModLoaded()) {
            return;
        }

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Bronze, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Aluminium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Steel, 1L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.StainlessSteel.getMolten(72L),
                ItemList.Ingot_Heavy1.get(1L),
                300,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem("GalacticraftCore", "item.heavyPlating", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.MeteoricIron, 2L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.TungstenSteel.getMolten(72L),
                ItemList.Ingot_Heavy2.get(1L),
                300,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getModItem("GalacticraftMars", "item.null", 1L, 3),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Desh, 4L),
                        GT_Utility.getIntegratedCircuit(1) },
                Materials.Platinum.getMolten(72L),
                ItemList.Ingot_Heavy3.get(1L),
                300,
                (int) TierEU.RECIPE_IV);

    }

    public void withGalaxySpace() {
        if (!GalaxySpace.isModLoaded()) {
            return ;
        }

        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] {
                                GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2L),
                                ItemList.Electric_Pump_MV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                        new FluidStack(FluidRegistry.getFluid("liquid helium"), 2000),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3L),
                        320,
                        (int) TierEU.RECIPE_MV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4L),
                        ItemList.Electric_Pump_HV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 4000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6L),
                320,
                (int) TierEU.RECIPE_HV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6L),
                        ItemList.Electric_Pump_EV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 6000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9L),
                320,
                (int) TierEU.RECIPE_EV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8L),
                        ItemList.Electric_Pump_IV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 8000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12L),
                640,
                (int) TierEU.RECIPE_IV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                                15L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10L),
                        ItemList.Electric_Pump_LuV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 12000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 15L),
                640,
                (int) TierEU.RECIPE_LuV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 18L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12L),
                        ItemList.Electric_Pump_ZPM.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 16000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18L),
                1280,
                (int) TierEU.RECIPE_ZPM);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuvwire, 21L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14L),
                        ItemList.Electric_Pump_UV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 20000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21L),
                1280,
                (int) TierEU.RECIPE_UV);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuhvwire, 24L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16L),
                        ItemList.Electric_Pump_UHV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 24000),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24L),
                2560,
                (int) TierEU.RECIPE_UHV);
        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] {
                                GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.SuperconductorUEVBase, 27L),
                                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Infinity, 18L),
                                ItemList.Electric_Pump_UEV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                        new FluidStack(FluidRegistry.getFluid("liquid helium"), 28000),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 27L),
                        3200,
                        (int) TierEU.RECIPE_UEV);
        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] {
                                GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.SuperconductorUMVBase, 33L),
                                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.SpaceTime, 22L),
                                ItemList.Electric_Pump_UMV.get(1L), GT_Utility.getIntegratedCircuit(9) },
                        new FluidStack(FluidRegistry.getFluid("liquid helium"), 36000),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 33L),
                        3200,
                        (int) TierEU.RECIPE_UMV);


    }

    public void withGTNHLanthAndGTPP(){
        if (!(GTNHLanthanides.isModLoaded() && GTPlusPlus.isModLoaded())) {
            return;
        }

        GT_Values.RA
            .addAssemblerRecipe(
                new ItemStack[] { ItemList.Electric_Pump_EV.get(4L), ItemList.Field_Generator_EV.get(4L),
                    getModItem(MOD_ID_GTPP, "itemPlateInconel690", 4L),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.BorosilicateGlass, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2L),
                    GT_OreDictUnificator
                        .get(OrePrefixes.pipeTiny, Materials.Polytetrafluoroethylene, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 4L),
                    ItemList.Shape_Extruder_Wire.get(16L) },
                Materials.SolderingAlloy.getGas(144L),
                ItemList.Spinneret.get(1L),
                2400,
                (int) TierEU.RECIPE_EV);

    }

    public void withIC2NuclearControl(){
        if (!IC2NuclearControl.isModLoaded()) { // Card recycling recipes
            return;
        }

        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemVanillaMachineCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemInventoryScannerCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemEnergySensorLocationCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "RFSensorCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 1L),
            200,
            (int) TierEU.RECIPE_LV); // counter
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 1),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 1L),
            200,
            (int) TierEU.RECIPE_LV); // liquid
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 2),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV); // generator
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemLiquidArrayLocationCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV); // 2-6 liquid
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemEnergyArrayLocationCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV); // 2-6 energy
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "ItemSensorLocationCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2L),
            200,
            (int) TierEU.RECIPE_LV); // non-fluid nuke
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "Item55ReactorCard", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            getModItem("IC2NuclearControl", "CardAppeng", 1L, 0),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 2L),
            200,
            (int) TierEU.RECIPE_LV);
        GT_Values.RA.addAssemblerRecipe(
            ItemList.NC_SensorCard.get(1L),
            GT_Utility.getIntegratedCircuit(1),
            GT_ModHandler.getIC2Item("electronicCircuit", 3L),
            200,
            (int) TierEU.RECIPE_LV);

    }
}
