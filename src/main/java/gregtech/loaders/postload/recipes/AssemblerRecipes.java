package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.AvaritiaAddons;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTNHLanthanides;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.IC2NuclearControl;
import static gregtech.api.enums.Mods.IronChests;
import static gregtech.api.enums.Mods.IronTanks;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.EIGHTH_INGOT;
import static gregtech.api.util.GT_RecipeBuilder.HALF_INGOT;
import static gregtech.api.util.GT_RecipeBuilder.INGOTS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.QUARTER_INGOT;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.solderingMats;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.FR_Stick.get(1L))
            .fluidInputs(Materials.SeedOil.getFluid(50L))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1))
            .itemOutputs(ItemList.Block_Plascrete.get(1))
            .fluidInputs(Materials.Concrete.getMolten(144))
            .duration(10 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1))
            .itemOutputs(ItemList.Block_Plascrete.get(1))
            .fluidInputs(Materials.Concrete.getMolten(144))
            .duration(10 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 16),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 4),
                ItemList.Electric_Motor_IV.get(16),
                ItemList.Emitter_EV.get(4),
                ItemList.Duct_Tape.get(64),
                ItemList.Energy_LapotronicOrb.get(1),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.TierdDrone0.get(4))
            .fluidInputs(Materials.AdvancedGlue.getFluid(144))
            .duration(10 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.string, 3), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(new ItemStack(Blocks.carpet, 2, 15 - i))
                    .fluidInputs(Dyes.VALUES[i].getFluidDye(j, 24))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(5)
                    .addTo(assemblerRecipes);
            }
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride, 1),
                ItemList.Paper_Printed_Pages.get(1))
            .itemOutputs(new ItemStack(Items.written_book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.PolyvinylChloride, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3))
            .itemOutputs(new ItemStack(Items.book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(20 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getIC2Item("carbonMesh", 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Component_Filter.get(1))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Component_Filter.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 64),
                ItemList.Circuit_Silicon_Wafer2.get(32),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 64))
            .fluidInputs(Materials.AdvancedGlue.getFluid(500))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 64),
                ItemList.Circuit_Silicon_Wafer3.get(8),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 64))
            .fluidInputs(Materials.AdvancedGlue.getFluid(250))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_LV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_LV.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_MV.get(1))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_HV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_HV.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_EV.get(1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_IV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Master), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_LuV.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_ZPM.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.FluidRegulator_UV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(500000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Valve_LV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Steam_Regulator_LV.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Valve_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Steam_Regulator_MV.get(1))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Valve_HV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Steam_Regulator_HV.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Valve_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Steam_Regulator_EV.get(1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Valve_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Steam_Regulator_IV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_LV.get(1),
                ItemList.Electric_Motor_LV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Steel), 2),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Steam_Valve_LV.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_MV.get(1),
                ItemList.Electric_Motor_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Aluminium), 2),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Steam_Valve_MV.get(1))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_HV.get(1),
                ItemList.Electric_Motor_HV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.StainlessSteel), 2),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Steam_Valve_HV.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_EV.get(1),
                ItemList.Electric_Motor_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.Titanium), 2),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Steam_Valve_EV.get(1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_IV.get(1),
                ItemList.Electric_Motor_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.gear.get(Materials.TungstenSteel), 2),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Steam_Valve_IV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Steel), 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 4),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Schematic.get(1L))
            .duration(30 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Aluminium), 3),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Schematic.get(1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.StainlessSteel), 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Schematic.get(1L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                ItemList.Circuit_Chip_LPIC.get(2L),
                ItemList.HV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_1.get(1L),
                ItemList.Electric_Pump_HV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_HV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L),
                ItemList.Circuit_Chip_PIC.get(2L),
                ItemList.EV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_1.get(1L),
                ItemList.Electric_Pump_EV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_EV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L),
                ItemList.Circuit_Chip_HPIC.get(2L),
                ItemList.IV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_3.get(1L),
                ItemList.Electric_Pump_IV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_IV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                ItemList.Circuit_Chip_LPIC.get(2L),
                ItemList.HV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_1.get(1L),
                ItemList.Electric_Pump_HV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_HV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 1L),
                ItemList.Circuit_Chip_PIC.get(2L),
                ItemList.EV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_1.get(1L),
                ItemList.Electric_Pump_EV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_EV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1L),
                ItemList.Circuit_Chip_HPIC.get(2L),
                ItemList.IV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_3.get(1L),
                ItemList.Electric_Pump_IV.get(1L))
            .itemOutputs(ItemList.Hatch_Energy_IV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L),
                ItemList.Circuit_Chip_LPIC.get(2L),
                ItemList.HV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_1.get(1L),
                ItemList.Electric_Pump_HV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_HV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L),
                ItemList.Circuit_Chip_PIC.get(2L),
                ItemList.EV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_1.get(1L),
                ItemList.Electric_Pump_EV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_EV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L),
                ItemList.Circuit_Chip_HPIC.get(2L),
                ItemList.IV_Coil.get(2L),
                ItemList.Reactor_Coolant_He_3.get(1L),
                ItemList.Electric_Pump_IV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_IV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Gold, 1L),
                ItemList.Circuit_Chip_LPIC.get(2L),
                ItemList.HV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_1.get(1L),
                ItemList.Electric_Pump_HV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_HV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Aluminium, 1L),
                ItemList.Circuit_Chip_PIC.get(2L),
                ItemList.EV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_1.get(1L),
                ItemList.Electric_Pump_EV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_EV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Vanadiumtriindinid, 1L),
                ItemList.Circuit_Chip_HPIC.get(2L),
                ItemList.IV_Coil.get(2L),
                ItemList.Reactor_Coolant_NaK_3.get(1L),
                ItemList.Electric_Pump_IV.get(1L))
            .itemOutputs(ItemList.Hatch_Dynamo_IV.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Long_Distance_Pipeline_Fluid.get(2L))
            .fluidInputs(Materials.Tin.getMolten(144L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Tin, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Steel, 2L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Long_Distance_Pipeline_Item.get(2L))
            .fluidInputs(Materials.Tin.getMolten(144L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L),
                GT_Utility.getIntegratedCircuit(24))
            .itemOutputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(64L))
            .fluidInputs(Materials.Tin.getMolten(144L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Tin, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 9L),
                GT_Utility.getIntegratedCircuit(24))
            .itemOutputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(64L))
            .fluidInputs(Materials.Tin.getMolten(144L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.StainlessSteel, 1L),
                ItemList.Hull_EV.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_EV.get(1L))
            .fluidInputs(Materials.Glass.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.Titanium, 1L),
                ItemList.Hull_IV.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_IV.get(1L))
            .fluidInputs(Materials.Glass.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.TungstenSteel, 1L),
                ItemList.Hull_LuV.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_LuV.get(1L))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.NiobiumTitanium, 1L),
                ItemList.Hull_ZPM.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_ZPM.get(1L))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.MysteriousCrystal, 1L),
                ItemList.Hull_UV.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_UV.get(1L))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.Neutronium, 1L),
                ItemList.Hull_MAX.get(1L),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Hatch_Input_Multi_2x2_UHV.get(1L))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(2304L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L),
                ItemList.Robot_Arm_IV.get(2L),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Casing_Gearbox_TungstenSteel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        { // limiting lifetime of the variables
            ItemStack flask = ItemList.VOLUMETRIC_FLASK.get(1);
            NBTTagCompound nbtFlask = new NBTTagCompound();
            int[] capacities = new int[] { 144, 288, 576, 720, 864, 72, 648, 936, 250, 500, 1000 };
            int[] circuitConfigurations = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 24 };
            for (int i = 0; i < capacities.length; i++) {
                nbtFlask.setInteger("Capacity", capacities[i]);
                flask.setTagCompound(nbtFlask);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.VOLUMETRIC_FLASK.get(1),
                        GT_Utility.getIntegratedCircuit(circuitConfigurations[i]))
                    .itemOutputs(flask)
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(assemblerRecipes);
            }
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Bronze, 1),
                ItemList.Electric_Motor_LV.get(1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_LV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Steel, 1),
                ItemList.Electric_Motor_MV.get(1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_MV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_HV.get(1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_HV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 1),
                ItemList.Electric_Motor_EV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_EV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1),
                ItemList.Electric_Motor_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_IV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Enderium, 1),
                ItemList.Electric_Motor_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Enderium, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_LuV.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1),
                ItemList.Electric_Motor_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_ZPM.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 1),
                ItemList.Electric_Motor_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_UV.get(1))
            .duration(10 * SECONDS)
            .eut(500000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MAX.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.MysteriousCrystal, 1),
                ItemList.Electric_Motor_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Hatch_Muffler_MAX.get(1))
            .duration(10 * SECONDS)
            .eut(2000000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.redstone_torch, 2, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1))
            .itemOutputs(new ItemStack(Items.repeater, 1, 0))
            .fluidInputs(Materials.Concrete.getMolten(144))
            .duration(4 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.leather, 1, 32767), new ItemStack(Items.lead, 1, 32767))
            .itemOutputs(new ItemStack(Items.name_tag, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(72))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.ArtificialLeather", 1L, 0),
                new ItemStack(Items.lead, 1, 32767))
            .itemOutputs(new ItemStack(Items.name_tag, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(72))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 8),
                new ItemStack(Items.compass, 1, 32767))
            .itemOutputs(new ItemStack(Items.map, 1, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tantalum, 1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Manganese, 1))
            .itemOutputs(ItemList.Battery_RE_ULV_Tantalum.get(8))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.charmOfLife1", 4L, 0), GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfLife2", 1L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(TwilightForest.ID, "item.charmOfKeeping1", 4L, 0),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfKeeping2", 1L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(TwilightForest.ID, "item.charmOfKeeping2", 4L, 0),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfKeeping3", 1L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.charmOfLife2", 1L, 0), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfLife1", 4L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(TwilightForest.ID, "item.charmOfKeeping2", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfKeeping1", 4L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(TwilightForest.ID, "item.charmOfKeeping3", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(TwilightForest.ID, "item.charmOfKeeping2", 4L, 0))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 16),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 20))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 23))
            .fluidInputs(Materials.Redstone.getMolten(144))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 17),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 20))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 24))
            .fluidInputs(Materials.Redstone.getMolten(144))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 18),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 20))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 22))
            .fluidInputs(Materials.Redstone.getMolten(144))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 1),
                new ItemStack(Blocks.sand, 1, 32767))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 2L, 0))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 1),
                new ItemStack(Blocks.sand, 1, 32767))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 2L, 600))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Fluix, 1),
                new ItemStack(Blocks.sand, 1, 32767))
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 2L, 1200))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_Wax.get(6), new ItemStack(Items.string, 1, 32767))
            .itemOutputs(getModItem(Forestry.ID, "candle", 24L, 0))
            .fluidInputs(Materials.Water.getFluid(600))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_Wax.get(2), ItemList.FR_Silk.get(1))
            .itemOutputs(getModItem(Forestry.ID, "candle", 8L, 0))
            .fluidInputs(Materials.Water.getFluid(200))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_Silk.get(9), GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(getModItem(Forestry.ID, "craftingMaterial", 1L, 3))
            .fluidInputs(Materials.Water.getFluid(500))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "propolis", 5L, 2), GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(getModItem(Forestry.ID, "craftingMaterial", 1L, 1))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                new ItemStack(Blocks.wool, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.torch, 6, 0))
            .fluidInputs(Materials.Creosote.getFluid(1000))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "craftingMaterial", 5L, 1), GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.piston, 1, 32767), new ItemStack(Items.slime_ball, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.sticky_piston, 1, 0))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.piston, 1, 32767), ItemList.IC2_Resin.get(1))
            .itemOutputs(new ItemStack(Blocks.sticky_piston, 1, 0))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.piston, 1, 32767), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.sticky_piston, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(100))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 3),
                GT_ModHandler.getIC2Item("carbonMesh", 3),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Duct_Tape.get(1))
            .fluidInputs(Materials.Glue.getFluid(300))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StyreneButadieneRubber, 2),
                GT_ModHandler.getIC2Item("carbonMesh", 2),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Duct_Tape.get(1))
            .fluidInputs(Materials.Glue.getFluid(200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1),
                GT_ModHandler.getIC2Item("carbonMesh", 1),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Duct_Tape.get(1))
            .fluidInputs(Materials.Glue.getFluid(100))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Maintenance Hatch Recipes Using BrainTech + Refined Glue. Info: One BrainTech Recipe Is In GT+ü Originally.
        // The Maintenance Hatch Recipe using SuperGlue is there.

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Hull_LV.get(1), ItemList.Duct_Tape.get(2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Hatch_Maintenance.get(1))
            .fluidInputs(Materials.Glue.getFluid(1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3),
                new ItemStack(Items.leather, 1, 32767))
            .itemOutputs(new ItemStack(Items.book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3),
                getModItem(NewHorizonsCoreMod.ID, "item.ArtificialLeather", 1L, 0))
            .itemOutputs(new ItemStack(Items.book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Paper, 1))
            .itemOutputs(new ItemStack(Items.book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Paper_Printed_Pages.get(1), new ItemStack(Items.leather, 1, 32767))
            .itemOutputs(new ItemStack(Items.written_book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Paper_Printed_Pages.get(1),
                getModItem(NewHorizonsCoreMod.ID, "item.ArtificialLeather", 1L, 0))
            .itemOutputs(new ItemStack(Items.written_book, 1, 0))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 4))
            .itemOutputs(ItemList.Cell_Universal_Fluid.get(1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Cake.get(1), new ItemStack(Items.egg, 1, 0))
            .itemOutputs(new ItemStack(Items.cake, 1, 0))
            .fluidInputs(Materials.Milk.getFluid(3000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Bun.get(2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Food_Sliced_Buns.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Bread.get(2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Food_Sliced_Breads.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Baguette.get(2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Food_Sliced_Baguettes.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Buns.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Food_Sliced_Bun.get(2))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Breads.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Food_Sliced_Bread.get(2))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Baguettes.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Food_Sliced_Baguette.get(2))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Sliced_Bun.get(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1))
            .itemOutputs(ItemList.Food_Burger_Meat.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Sliced_Buns.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1))
            .itemOutputs(ItemList.Food_Burger_Meat.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Bun.get(2), ItemList.Food_Chum.get(1))
            .itemOutputs(ItemList.Food_Burger_Chum.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Buns.get(1), ItemList.Food_Chum.get(1))
            .itemOutputs(ItemList.Food_Burger_Chum.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Bun.get(2), ItemList.Food_Sliced_Cheese.get(3))
            .itemOutputs(ItemList.Food_Burger_Cheese.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Buns.get(1), ItemList.Food_Sliced_Cheese.get(3))
            .itemOutputs(ItemList.Food_Burger_Cheese.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Food_Flat_Dough.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1))
            .itemOutputs(ItemList.Food_Raw_Pizza_Meat.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Food_Flat_Dough.get(1), ItemList.Food_Sliced_Cheese.get(3))
            .itemOutputs(ItemList.Food_Raw_Pizza_Cheese.get(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        // SC craft

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2),
                ItemList.Electric_Pump_MV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3))
            .fluidInputs(Materials.Helium.getGas(2000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4),
                ItemList.Electric_Pump_HV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6))
            .fluidInputs(Materials.Helium.getGas(4000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6),
                ItemList.Electric_Pump_EV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9))
            .fluidInputs(Materials.Helium.getGas(6000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8),
                ItemList.Electric_Pump_IV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12))
            .fluidInputs(Materials.Helium.getGas(8000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(
                    OrePrefixes.wireGt01,
                    Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    15),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10),
                ItemList.Electric_Pump_LuV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 15))
            .fluidInputs(Materials.Helium.getGas(12000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 18),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12),
                ItemList.Electric_Pump_ZPM.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18))
            .fluidInputs(Materials.Helium.getGas(16000))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuvwire, 21),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14),
                ItemList.Electric_Pump_UV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21))
            .fluidInputs(Materials.Helium.getGas(20000))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuhvwire, 24),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16),
                ItemList.Electric_Pump_UHV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24))
            .fluidInputs(Materials.Helium.getGas(24000))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2),
                ItemList.Electric_Pump_MV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(4))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4),
                ItemList.Electric_Pump_HV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6),
                ItemList.Electric_Pump_EV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(12))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8),
                ItemList.Electric_Pump_IV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(16))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(
                    OrePrefixes.wireGt01,
                    Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    15),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10),
                ItemList.Electric_Pump_LuV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 15))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(24))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 18),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12),
                ItemList.Electric_Pump_ZPM.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(32))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuvwire, 21),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14),
                ItemList.Electric_Pump_UV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(40))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuhvwire, 24),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16),
                ItemList.Electric_Pump_UHV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(48))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEVBase, 27),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Infinity, 18),
                ItemList.Electric_Pump_UEV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 27))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(56))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIVBase, 30L),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, MaterialsUEVplus.TranscendentMetal, 20L),
                ItemList.Electric_Pump_UIV.get(1L),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 30L))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(68))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMVBase, 33),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, MaterialsUEVplus.SpaceTime, 22),
                ItemList.Electric_Pump_UMV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 33))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(72))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lead, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ULV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.LV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SteelMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Aluminium, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.MV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SteelMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.EnergeticAlloy, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.HV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NeodymiumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.TungstenSteel, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.EV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NeodymiumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iridium, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.IV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, ExternalMaterials.getRuridit(), 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.LuV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ZPM_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.ElectrumFlux, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.UV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(500000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 16),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.UHV_Coil.get(1))
            .duration(10 * SECONDS)
            .eut(2000000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 6),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Wood, 2))
            .fluidInputs(Materials.Glue.getFluid(10))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 3),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Wood, 4))
            .fluidInputs(Materials.Glue.getFluid(20))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1),
                GT_Utility.getIntegratedCircuit(12))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Wood, 6))
            .fluidInputs(Materials.Glue.getFluid(60))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 4),
                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Ingot_IridiumAlloy.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                ItemList.Electric_Motor_MV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Steel, 1),
                new ItemStack(Blocks.iron_bars, 6),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Casing_Grate.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                ItemList.Electric_Motor_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Iridium, 1),
                ItemList.Component_Filter.get(8),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 8))
            .itemOutputs(ItemList.Casing_Vent_T2.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "torchesFromCoal", false)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                    new ItemStack(Items.coal, 1, 32767))
                .itemOutputs(new ItemStack(Blocks.torch, 4))
                .duration(2 * SECONDS)
                .eut(20)
                .addTo(assemblerRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 2),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1))
            .itemOutputs(new ItemStack(Blocks.light_weighted_pressure_plate, 1))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2),
                GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1))
            .itemOutputs(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 7),
                GT_Utility.getIntegratedCircuit(7))
            .itemOutputs(new ItemStack(Items.cauldron, 1))
            .duration(35 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("ironFence", 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 3),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(new ItemStack(Blocks.iron_bars, 4))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 7),
                GT_Utility.getIntegratedCircuit(7))
            .itemOutputs(new ItemStack(Items.cauldron, 1))
            .duration(35 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("ironFence", 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 3),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(new ItemStack(Blocks.iron_bars, 4))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(new ItemStack(Blocks.fence, 1))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2))
            .itemOutputs(new ItemStack(Blocks.tripwire_hook, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2))
            .itemOutputs(new ItemStack(Blocks.tripwire_hook, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3),
                new ItemStack(Items.string, 3, 32767))
            .itemOutputs(new ItemStack(Items.bow, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3),
                ItemList.Component_Minecart_Wheels_Iron.get(2))
            .itemOutputs(new ItemStack(Items.minecart, 1))
            .duration(5 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 3),
                ItemList.Component_Minecart_Wheels_Iron.get(2))
            .itemOutputs(new ItemStack(Items.minecart, 1))
            .duration(4 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 3),
                ItemList.Component_Minecart_Wheels_Steel.get(2))
            .itemOutputs(new ItemStack(Items.minecart, 1))
            .duration(5 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2))
            .itemOutputs(ItemList.Component_Minecart_Wheels_Iron.get(1))
            .duration(5 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2))
            .itemOutputs(ItemList.Component_Minecart_Wheels_Iron.get(1))
            .duration(4 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Steel, 2))
            .itemOutputs(ItemList.Component_Minecart_Wheels_Steel.get(1))
            .duration(3 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.hopper, 1, 32767))
            .itemOutputs(new ItemStack(Items.hopper_minecart, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.tnt, 1, 32767))
            .itemOutputs(new ItemStack(Items.tnt_minecart, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.chest, 1, 32767))
            .itemOutputs(new ItemStack(Items.chest_minecart, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.trapped_chest, 1, 32767))
            .itemOutputs(new ItemStack(Items.chest_minecart, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.furnace, 1, 32767))
            .itemOutputs(new ItemStack(Items.furnace_minecart, 1))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.tripwire_hook, 1), new ItemStack(Blocks.chest, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.trapped_chest, 1))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone, 1, 0), GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(new ItemStack(Blocks.stonebrick, 1, 0))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sandstone, 1, 0), GT_Utility.getIntegratedCircuit(23))
            .itemOutputs(new ItemStack(Blocks.sandstone, 1, 2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sandstone, 1, 1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.sandstone, 1, 0))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sandstone, 1, 2), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Blocks.sandstone, 1, 0))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(GT_ModHandler.getIC2Item("machine", 1))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_ULV.get(1))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_LV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_MV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_HV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_EV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_IV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, ExternalMaterials.getRhodiumPlatedPalladium(), 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_LuV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_ZPM.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_UV.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.Casing_MAX.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Invar, 1))
            .itemOutputs(ItemList.Casing_HeatProof.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Casing_SolidSteel.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1))
            .itemOutputs(ItemList.Casing_FrostProof.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
            .itemOutputs(ItemList.Casing_RobustTungstenSteel.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1))
            .itemOutputs(ItemList.Casing_CleanStainlessSteel.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1))
            .itemOutputs(ItemList.Casing_StableTitanium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 1))
            .itemOutputs(ItemList.Casing_MiningOsmiridium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1))
            .itemOutputs(ItemList.Casing_MiningNeutronium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 1))
            .itemOutputs(ItemList.Casing_MiningBlackPlutonium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 4), ItemList.Casing_LuV.get(1))
            .itemOutputs(ItemList.Casing_Fusion.get(1))
            .fluidInputs(Materials.HSSG.getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 4),
                ItemList.Casing_Fusion.get(1))
            .itemOutputs(ItemList.Casing_Fusion2.get(1))
            .fluidInputs(Materials.NaquadahAlloy.getMolten(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Magnalium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueSteel, 1))
            .itemOutputs(ItemList.Casing_Turbine.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6),
                ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemList.Casing_Turbine1.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6),
                ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemList.Casing_Turbine2.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6),
                ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemList.Casing_Turbine3.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6), ItemList.Casing_Turbine.get(1))
            .itemOutputs(ItemList.Casing_TurbineGasAdvanced.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Casing_SolidSteel.get(1), GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(ItemList.Casing_Chemically_Inert.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(216))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1))
            .itemOutputs(ItemList.Casing_Advanced_Iridium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2),
                    ItemList.Casing_ULV.get(1))
                .itemOutputs(ItemList.Hull_ULV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(1 * SECONDS + 5 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2),
                    ItemList.Casing_LV.get(1))
                .itemOutputs(ItemList.Hull_LV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2),
                    ItemList.Casing_MV.get(1))
                .itemOutputs(ItemList.Hull_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2),
                    ItemList.Casing_MV.get(1))
                .itemOutputs(ItemList.Hull_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2),
                    ItemList.Casing_HV.get(1))
                .itemOutputs(ItemList.Hull_HV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2),
                    ItemList.Casing_EV.get(1))
                .itemOutputs(ItemList.Hull_EV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2),
                    ItemList.Casing_IV.get(1))
                .itemOutputs(ItemList.Hull_IV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2),
                    ItemList.Casing_LuV.get(1))
                .itemOutputs(ItemList.Hull_LuV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2),
                    ItemList.Casing_ZPM.get(1))
                .itemOutputs(ItemList.Hull_ZPM.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                    ItemList.Casing_UV.get(1))
                .itemOutputs(ItemList.Hull_UV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 2),
                    ItemList.Casing_MAX.get(1))
                .itemOutputs(ItemList.Hull_MAX.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(288))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);
        } else {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2),
                    ItemList.Casing_ULV.get(1))
                .itemOutputs(ItemList.Hull_ULV.get(1))
                .duration(1 * SECONDS + 5 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2),
                    ItemList.Casing_LV.get(1))
                .itemOutputs(ItemList.Hull_LV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2),
                    ItemList.Casing_MV.get(1))
                .itemOutputs(ItemList.Hull_MV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2),
                    ItemList.Casing_MV.get(1))
                .itemOutputs(ItemList.Hull_MV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2),
                    ItemList.Casing_HV.get(1))
                .itemOutputs(ItemList.Hull_HV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2),
                    ItemList.Casing_EV.get(1))
                .itemOutputs(ItemList.Hull_EV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2),
                    ItemList.Casing_IV.get(1))
                .itemOutputs(ItemList.Hull_IV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2),
                    ItemList.Casing_LuV.get(1))
                .itemOutputs(ItemList.Hull_LuV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2),
                    ItemList.Casing_ZPM.get(1))
                .itemOutputs(ItemList.Hull_ZPM.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                    ItemList.Casing_UV.get(1))
                .itemOutputs(ItemList.Hull_UV.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 2),
                    ItemList.Casing_MAX.get(1))
                .itemOutputs(ItemList.Hull_MAX.get(1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(16)
                .addTo(assemblerRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 1))
            .itemOutputs(ItemList.Battery_Hull_LV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .duration(4 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(432))
            .duration(16 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3))
            .itemOutputs(ItemList.Battery_Hull_MV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(432))
            .duration(16 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 9))
            .itemOutputs(ItemList.Battery_Hull_HV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(1296))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.string, 4, 32767), new ItemStack(Items.slime_ball, 1, 32767))
            .itemOutputs(new ItemStack(Items.lead, 2))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("batPack", 1L, 32767), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.IC2_ReBattery.get(6))
            .duration(40 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("carbonFiber", 2), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(GT_ModHandler.getIC2Item("carbonMesh", 1))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5),
                new ItemStack(Blocks.chest, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hopper))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5),
                new ItemStack(Blocks.trapped_chest, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hopper))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5),
                new ItemStack(Blocks.chest, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hopper))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5),
                new ItemStack(Blocks.trapped_chest, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hopper))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CobaltBrass, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .itemOutputs(ItemList.Component_Sawblade_Diamond.get(1))
            .duration(16 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Blocks.redstone_torch, 1))
            .duration(1 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1))
            .itemOutputs(new ItemStack(Blocks.torch, 2))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1))
            .itemOutputs(new ItemStack(Blocks.torch, 6))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1), ItemList.IC2_Resin.get(1))
            .itemOutputs(new ItemStack(Blocks.torch, 6))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("tinCableItem", 1),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1))
                .itemOutputs(GT_ModHandler.getIC2Item("insulatedTinCableItem", 1))
                .duration(1 * SECONDS)
                .eut(20)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("copperCableItem", 1),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1))
                .itemOutputs(GT_ModHandler.getIC2Item("insulatedCopperCableItem", 1))
                .duration(1 * SECONDS)
                .eut(20)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("goldCableItem", 1),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2))
                .itemOutputs(GT_ModHandler.getIC2Item("insulatedGoldCableItem", 1))
                .duration(2 * SECONDS)
                .eut(20)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getIC2Item("ironCableItem", 1),
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 3))
                .itemOutputs(GT_ModHandler.getIC2Item("insulatedIronCableItem", 1))
                .duration(3 * SECONDS)
                .eut(20)
                .addTo(assemblerRecipes);
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Items.wooden_sword, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Stone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Items.stone_sword, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Items.iron_sword, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Items.golden_sword, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(new ItemStack(Items.diamond_sword, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(ItemList.Tool_Sword_Bronze.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1))
            .itemOutputs(ItemList.Tool_Sword_Steel.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.wooden_pickaxe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Stone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.stone_pickaxe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.iron_pickaxe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.golden_pickaxe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.diamond_pickaxe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Pickaxe_Bronze.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Pickaxe_Steel.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.wooden_shovel, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Stone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.stone_shovel, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.iron_shovel, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.golden_shovel, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.diamond_shovel, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Shovel_Bronze.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Shovel_Steel.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.wooden_axe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Stone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.stone_axe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.iron_axe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.golden_axe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.diamond_axe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Axe_Bronze.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Axe_Steel.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Wood, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.wooden_hoe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Stone, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.stone_hoe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.iron_hoe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.golden_hoe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(new ItemStack(Items.diamond_hoe, 1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Hoe_Bronze.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2))
            .itemOutputs(ItemList.Tool_Hoe_Steel.getUndamaged(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        // fuel rod assembler recipes

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.ThoriumCell_1.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.ThoriumCell_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.ThoriumCell_1.get(4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.ThoriumCell_4.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.ThoriumCell_2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.ThoriumCell_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Uraniumcell_1.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Uraniumcell_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Uraniumcell_1.get(4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Uraniumcell_4.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Uraniumcell_2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Uraniumcell_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Moxcell_1.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Moxcell_2.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Moxcell_1.get(4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 6),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.Moxcell_4.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Moxcell_2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.Moxcell_4.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.NaquadahCell_1.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.NaquadahCell_2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.NaquadahCell_1.get(4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.NaquadahCell_4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.NaquadahCell_2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.NaquadahCell_4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.MNqCell_1.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.MNqCell_2.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.MNqCell_1.get(4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(ItemList.MNqCell_4.get(1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.MNqCell_2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(ItemList.MNqCell_4.get(1))
            .duration(5 * SECONDS)
            .eut(400)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 4))
            .itemOutputs(ItemList.neutroniumHeatCapacitor.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.NaquadahAlloy.getPlates(8),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1))
            .itemOutputs(ItemList.RadiantNaquadahAlloyCasing.get(1))
            .duration(10 * TICKS)
            .eut(400000)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.KevlarFiber.get(8), GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.WovenKevlar.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1),
                ItemList.NC_SensorKit.get(1),
                ItemList.Emitter_EV.get(1),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 28),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Cover_Metrics_Transmitter.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        ItemStack[] plates = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L) };

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            for (ItemStack tPlate : plates) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.lever, 1, 32767), tPlate, GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(ItemList.Cover_Controller.get(1))
                    .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                    .duration(40 * SECONDS)
                    .eut(16)
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(Blocks.redstone_torch, 1, 32767),
                        tPlate,
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(ItemList.Cover_ActivityDetector.get(1))
                    .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                    .duration(40 * SECONDS)
                    .eut(16)
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767),
                        tPlate,
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(ItemList.Cover_FluidDetector.get(1))
                    .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                    .duration(40 * SECONDS)
                    .eut(16)
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767),
                        tPlate,
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(ItemList.Cover_ItemDetector.get(1))
                    .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                    .duration(40 * SECONDS)
                    .eut(16)
                    .addTo(assemblerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_ModHandler.getIC2Item("ecMeter", 1), tPlate, GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(ItemList.Cover_EnergyDetector.get(1))
                    .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                    .duration(40 * SECONDS)
                    .eut(16)
                    .addTo(assemblerRecipes);
            }

        }
    }

    /**
     * Adds recipes for input buses from ULV to UHV
     */
    public void loadInputBusesRecipes() {
        // ULV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_ULV.get(1))
                .fluidInputs(Materials.Glue.getFluid(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_ULV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_ULV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_ULV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        // LV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LV.get(1))
                .fluidInputs(Materials.Glue.getFluid(5 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(9))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }

        // MV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_MV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_MV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
        }

        // HV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_HV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(3 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_HV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_HV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * QUARTER_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }

        // EV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_EV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_EV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // IV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_IV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_IV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
        }

        // LuV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LuV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_LuV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);
        }

        // ZPM input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 2, 5),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_ZPM.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);
        }

        // UV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 2, 6),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_UV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);
        }

        // UHV input bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    getModItem(AvaritiaAddons.ID, "CompressedChest", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_Bus_MAX.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(assemblerRecipes);
        }
    }

    /**
     * Adds recipes for output buses from ULV to UHV
     */
    public void loadOutputBusesRecipes() {

        /*
         * those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
         * means we aren't in full pack
         */

        if (!NewHorizonsCoreMod.isModLoaded()) {
            return;
        }

        // ULV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_ULV.get(1))
                .fluidInputs(Materials.Glue.getFluid(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_ULV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_ULV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "BabyChest", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_ULV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        // LV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LV.get(1))
                .fluidInputs(Materials.Glue.getFluid(5 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_LV.get(1), new ItemStack(Blocks.chest), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(9))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }

        if (!IronChests.isModLoaded()) {
            return;
        }

        // MV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_MV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 3),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_MV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
        }

        // HV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_HV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(3 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_HV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 0),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_HV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * QUARTER_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }

        // EV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 4),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_EV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 4),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_EV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // IV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_IV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_IV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
        }

        // LuV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 2),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LuV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 1, 2),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_LuV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);
        }

        // ZPM output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 2, 5),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_ZPM.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);
        }

        // UV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_UV.get(1),
                    getModItem(IronChests.ID, "BlockIronChest", 2, 6),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_UV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);
        }

        if (!AvaritiaAddons.isModLoaded()) {
            return;
        }

        // UHV output bus
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MAX.get(1),
                    getModItem(AvaritiaAddons.ID, "CompressedChest", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_Bus_MAX.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(assemblerRecipes);
        }
    }

    /**
     * Adds recipes for input hatches from ULV to UHV
     */
    public void loadInputHatchesRecipes() {
        // ULV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_ULV.get(1))
                .fluidInputs(Materials.Glue.getFluid(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_ULV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_ULV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_ULV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        /*
         * those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
         * means we aren't in full pack
         */

        if (!BuildCraftFactory.isModLoaded()) {
            return;
        }

        // LV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LV.get(1))
                .fluidInputs(Materials.Glue.getFluid(5 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(9))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }

        if (!IronTanks.isModLoaded()) {
            return;
        }

        // MV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_MV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_MV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
        }

        // HV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_HV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(3 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_HV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_HV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * QUARTER_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }

        // EV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.ID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_EV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.ID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_EV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // IV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.ID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_IV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.ID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_IV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
        }

        // LuV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.ID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LuV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.ID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_LuV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);
        }

        // ZPM input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronTanks.ID, "obsidianTank", 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_ZPM.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);
        }

        // UV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_UV.get(1), ItemList.Super_Tank_LV.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_UV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);
        }

        // UHV input hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_MAX.get(1), ItemList.Super_Tank_MV.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.Hatch_Input_MAX.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(assemblerRecipes);
        }
    }

    /**
     * Adds recipes for output hatches from ULV to UHV
     */
    public void loadOutputHatchesRecipes() {
        // ULV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_ULV.get(1))
                .fluidInputs(Materials.Glue.getFluid(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_ULV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_ULV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ULV.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_ULV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        /*
         * those early exits prevents further hatches recipes from being registered, but it's probably fine, as that
         * means we aren't in full pack
         */

        if (!BuildCraftFactory.isModLoaded()) {
            return;
        }

        // LV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LV.get(1))
                .fluidInputs(Materials.Glue.getFluid(5 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    getModItem(BuildCraftFactory.ID, "tankBlock", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(9))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }

        if (!IronTanks.isModLoaded()) {
            return;
        }

        // MV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_MV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_MV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_MV.get(1),
                    getModItem(IronTanks.ID, "copperTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_MV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * EIGHTH_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
        }

        // HV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_HV.get(1))
                .fluidInputs(Materials.Plastic.getMolten(3 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_HV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_HV.get(1),
                    getModItem(IronTanks.ID, "ironTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_HV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * QUARTER_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }

        // EV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.ID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_EV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_EV.get(1),
                    getModItem(IronTanks.ID, "silverTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_EV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * HALF_INGOT))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // IV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.ID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_IV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_IV.get(1),
                    getModItem(IronTanks.ID, "goldTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_IV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(1 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
        }

        // LuV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.ID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LuV.get(1))
                .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LuV.get(1),
                    getModItem(IronTanks.ID, "diamondTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_LuV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(2 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);
        }

        // ZPM output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_ZPM.get(1),
                    getModItem(IronTanks.ID, "obsidianTank", 1),
                    GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_ZPM.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(4 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);
        }

        // UV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_UV.get(1), ItemList.Super_Tank_LV.get(1), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_UV.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(8 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(assemblerRecipes);
        }

        // UHV output hatch
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Hull_MAX.get(1), ItemList.Super_Tank_MV.get(1), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(ItemList.Hatch_Output_MAX.get(1))
                .fluidInputs(Materials.Polybenzimidazole.getMolten(16 * INGOTS))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(assemblerRecipes);
        }
    }

    /**
     * Load all Railcraft recipes for GT Machines
     */
    private void withRailcraft() {
        if (!Railcraft.isModLoaded()) return;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.stone_slab, 1, 0),
                ItemList.RC_Rebar.get(1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Tie_Stone.get(1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.stone_slab, 1, 7),
                ItemList.RC_Rebar.get(1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Tie_Stone.get(1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.RC_Rail_HS.get(16))
            .fluidInputs(Materials.Blaze.getMolten(216))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(ItemList.RC_Rail_HS.get(8))
            .fluidInputs(Materials.ConductiveIron.getMolten(432))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(ItemList.RC_Rail_HS.get(32))
            .fluidInputs(Materials.VibrantAlloy.getMolten(216))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(ItemList.RC_Rail_HS.get(64))
            .fluidInputs(Materials.CrystallineAlloy.getMolten(216))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.RC_Rail_Adv.get(8))
            .fluidInputs(Materials.Redstone.getMolten(216))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.RC_Rail_Adv.get(16))
            .fluidInputs(Materials.RedAlloy.getMolten(216))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.RC_Rail_Adv.get(32))
            .fluidInputs(Materials.ConductiveIron.getMolten(216))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(3),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3),
                GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.RC_Rail_Adv.get(64))
            .fluidInputs(Materials.VibrantAlloy.getMolten(216))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(4))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(8))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(16))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.HSSG, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(32))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Rail_Standard.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.RC_Rail_Electric.get(64))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(96)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Tie_Wood.get(1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(ItemList.RC_Rail_Wooden.get(8))
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Tie_Wood.get(1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Wooden.get(8))
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Tie_Wood.get(1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Wooden.get(16))
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Tie_Wood.get(1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 1),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Wooden.get(32))
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Tie_Wood.get(1),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1),
                GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(ItemList.RC_Rail_Wooden.get(64))
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.RC_Tie_Wood.get(32), GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(ItemList.RC_Bed_Wood.get(24))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.RC_Tie_Wood.get(64), GT_Utility.getIntegratedCircuit(24))
            .itemOutputs(ItemList.RC_Bed_Wood.get(48))
            .duration(10 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.RC_Tie_Stone.get(32), GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(ItemList.RC_Bed_Stone.get(24))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.RC_Tie_Stone.get(64), GT_Utility.getIntegratedCircuit(24))
            .itemOutputs(ItemList.RC_Bed_Stone.get(48))
            .duration(10 * SECONDS)
            .eut(48)
            .addTo(assemblerRecipes);

        ItemStack tRailWood = getModItem(Railcraft.ID, "track", 64, 736);
        if (tRailWood != null) {
            NBTTagCompound tTagWood = new NBTTagCompound();
            tTagWood.setString("track", "railcraft:track.slow");
            tRailWood.stackTagCompound = tTagWood;

            ItemStack tRailWoodB = getModItem(Railcraft.ID, "track.slow", 16);
            NBTTagCompound tTagWoodB = new NBTTagCompound();
            tTagWoodB.setString("track", "railcraft:track.slow.boost");
            tRailWoodB.stackTagCompound = tTagWoodB;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.RC_Bed_Wood.get(1),
                    ItemList.RC_Rail_Wooden.get(6),
                    GT_Utility.getIntegratedCircuit(21))
                .itemOutputs(tRailWood)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                    GT_Utility.getIntegratedCircuit(22))
                .itemOutputs(tRailWoodB)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.rail, 1, 0),
                ItemList.RC_Rail_Adv.get(2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(new ItemStack(Blocks.golden_rail, 16, 0))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Bed_Wood.get(1),
                ItemList.RC_Rail_Standard.get(6),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(new ItemStack(Blocks.rail, 64, 0))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        ItemStack tRailRe = getModItem(Railcraft.ID, "track", 64);
        NBTTagCompound tTagRe = new NBTTagCompound();
        tTagRe.setString("track", "railcraft:track.reinforced");
        tRailRe.stackTagCompound = tTagRe;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Bed_Stone.get(1),
                ItemList.RC_Rail_Reinforced.get(6),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(tRailRe)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        ItemStack tRailReB = getModItem(Railcraft.ID, "track.reinforced", 16);
        NBTTagCompound tTagReB = new NBTTagCompound();
        tTagReB.setString("track", "railcraft:track.reinforced.boost");
        tRailReB.stackTagCompound = tTagReB;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(2, tRailRe),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(tRailReB)
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        ItemStack tRailEl = getModItem(Railcraft.ID, "track", 64);
        NBTTagCompound tTagEl = new NBTTagCompound();
        tTagEl.setString("track", "railcraft:track.electric");
        tRailEl.stackTagCompound = tTagEl;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.RC_Bed_Stone.get(1),
                ItemList.RC_Rail_Electric.get(6),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(tRailEl)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        ItemStack tRailHs = getModItem(Railcraft.ID, "track", 64, 816);
        if (tRailHs != null) {
            NBTTagCompound tTagHs = new NBTTagCompound();
            tTagHs.setString("track", "railcraft:track.speed");
            tRailHs.stackTagCompound = tTagHs;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.RC_Bed_Stone.get(1),
                    ItemList.RC_Rail_HS.get(6),
                    GT_Utility.getIntegratedCircuit(21))
                .itemOutputs(tRailHs)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
        ItemStack tRailHsB = getModItem(Railcraft.ID, "track.speed", 16);
        NBTTagCompound tTagHsB = new NBTTagCompound();
        tTagHsB.setString("track", "railcraft:track.speed.boost");
        tRailHsB.stackTagCompound = tTagHsB;

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(2, tRailHs),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(tRailHsB)
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // --- Wooden Switch Track ---
        ItemStack tRailSS = getModItem(Railcraft.ID, "track.slow", 1, 19986);
        if (tRailSS != null) {
            NBTTagCompound tTagSS = new NBTTagCompound();
            tTagSS.setString("track", "railcraft:track.slow.switch");
            tRailSS.stackTagCompound = tTagSS;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.AnyIron, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailSS)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailSS))
                .duration(5 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSS))
                .duration(5 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSS))
                .duration(5 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSS))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSS))
                .duration(5 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);
        }
        // --- Wooden Wye Track ---
        ItemStack tRailSW = getModItem(Railcraft.ID, "track.slow", 1);
        if (tRailSW != null) {
            NBTTagCompound tTagSW = new NBTTagCompound();
            tTagSW.setString("track", "railcraft:track.slow.wye");
            tRailSW.stackTagCompound = tTagSW;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.AnyIron, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailSW)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailSW))
                .duration(5 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSW))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSW))
                .duration(5 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSW))
                .duration(5 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSW))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSW))
                .duration(5 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);
        }
        // --- Wooden Junction Tack ---
        ItemStack tRailSJ = getModItem(Railcraft.ID, "track.slow", 1);
        if (tRailSJ != null) {
            NBTTagCompound tTagSJ = new NBTTagCompound();
            tTagSJ.setString("track", "railcraft:track.slow.junction");
            tRailSJ.stackTagCompound = tTagSJ;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.AnyIron, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailSJ)
                .duration(5 * SECONDS)
                .eut(8)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailSJ))
                .duration(5 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSJ))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSJ))
                .duration(5 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSJ))
                .duration(5 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSJ))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailWood),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSJ))
                .duration(5 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);
        }
        // --- Switch Tack ---
        ItemStack tRailNS = getModItem(Railcraft.ID, "track", 1, 4767);
        if (tRailNS != null) {
            NBTTagCompound tTagNS = new NBTTagCompound();
            tTagNS.setString("track", "railcraft:track.switch");
            tRailNS.stackTagCompound = tTagNS;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailNS)
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailNS))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailNS))
                .duration(10 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailNS))
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailNS))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailNS))
                .duration(10 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailNS))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

        }
        // --- Wye Tack ---
        ItemStack tRailNW = getModItem(Railcraft.ID, "track", 1, 2144);
        if (tRailNW != null) {
            NBTTagCompound tTagNW = new NBTTagCompound();
            tTagNW.setString("track", "railcraft:track.wye");
            tRailNW.stackTagCompound = tTagNW;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailNW)
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailNW))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailNW))
                .duration(10 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailNW))
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailNW))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailNW))
                .duration(10 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailNW))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Junction Tack ---
        ItemStack tRailNJ = getModItem(Railcraft.ID, "track", 1);
        if (tRailNJ != null) {
            NBTTagCompound tTagNJ = new NBTTagCompound();
            tTagNJ.setString("track", "railcraft:track.junction");
            tRailNJ.stackTagCompound = tTagNJ;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailNJ)
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailNJ))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailNJ))
                .duration(10 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailNJ))
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailNJ))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailNJ))
                .duration(10 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.rail, 2, 0),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailNJ))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Reinforced Switch Track ---
        ItemStack tRailRS = getModItem(Railcraft.ID, "track.reinforced", 1);
        if (tRailRS != null) {
            NBTTagCompound tTagRS = new NBTTagCompound();
            tTagRS.setString("track", "railcraft:track.reinforced.switch");
            tRailRS.stackTagCompound = tTagRS;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailRS)
                .duration(15 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailRS))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailRS))
                .duration(15 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailRS))
                .duration(15 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailRS))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailRS))
                .duration(15 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailRS))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Reinforced Wye Track ---
        ItemStack tRailRW = getModItem(Railcraft.ID, "track.reinforced", 1);
        if (tRailRW != null) {
            NBTTagCompound tTagRW = new NBTTagCompound();
            tTagRW.setString("track", "railcraft:track.reinforced.wye");
            tRailRW.stackTagCompound = tTagRW;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailRW)
                .duration(15 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailRW))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailRW))
                .duration(15 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailRW))
                .duration(15 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailRW))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailRW))
                .duration(15 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailRW))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Reinforced Junction Track ---
        ItemStack tRailRJ = getModItem(Railcraft.ID, "track.reinforced", 1, 764);
        if (tRailRJ != null) {
            NBTTagCompound tTagRJ = new NBTTagCompound();
            tTagRJ.setString("track", "railcraft:track.reinforced.junction");
            tRailRJ.stackTagCompound = tTagRJ;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailRJ)
                .duration(15 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailRJ))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailRJ))
                .duration(15 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailRJ))
                .duration(15 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailRJ))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailRJ))
                .duration(15 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailRe),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailRJ))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- H.S. Switch Track ---
        ItemStack tRailSSw = getModItem(Railcraft.ID, "track.speed", 1, 7916);
        if (tRailSSw != null) {
            NBTTagCompound tTagRSSw = new NBTTagCompound();
            tTagRSSw.setString("track", "railcraft:track.speed.switch");
            tRailSSw.stackTagCompound = tTagRSSw;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailSSw)
                .duration(20 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailSSw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSSw))
                .duration(20 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSSw))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSSw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSSw))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSSw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- H.S. Wye Track ---
        ItemStack tRailSWy = getModItem(Railcraft.ID, "track.speed", 1);
        if (tRailSWy != null) {
            NBTTagCompound tTagRSWy = new NBTTagCompound();
            tTagRSWy.setString("track", "railcraft:track.speed.wye");
            tRailSWy.stackTagCompound = tTagRSWy;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailSWy)
                .duration(20 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.StainlessSteel, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailSWy))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSWy))
                .duration(20 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TungstenSteel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSWy))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSWy))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSWy))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSWy))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- H.S. Transition Track ---
        ItemStack tRailSTt = getModItem(Railcraft.ID, "track.speed", 1, 26865);
        if (tRailSTt != null) {
            NBTTagCompound tTagRSTt = new NBTTagCompound();
            tTagRSTt.setString("track", "railcraft:track.speed.transition");
            tRailSTt.stackTagCompound = tTagRSTt;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    ItemList.RC_Bed_Stone.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailSTt))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    ItemList.RC_Bed_Stone.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ConductiveIron, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailSTt))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    ItemList.RC_Bed_Stone.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailSTt))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    ItemList.RC_Bed_Stone.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailSTt))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailHs),
                    ItemList.RC_Bed_Stone.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.MelodicAlloy, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailSTt))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Electric Switch Track ---
        ItemStack tRailES = getModItem(Railcraft.ID, "track.electric", 1, 10488);
        if (tRailES != null) {
            NBTTagCompound tTagES = new NBTTagCompound();
            tTagES.setString("track", "railcraft:track.electric.switch");
            tRailES.stackTagCompound = tTagES;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Copper, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailES)
                .duration(20 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Gold, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailES))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailES))
                .duration(20 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailES))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Platinum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailES))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.VanadiumGallium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailES))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.springSmall, Materials.Naquadah, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailES))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Electric Wye Track ---
        ItemStack tRailEw = getModItem(Railcraft.ID, "track.electric", 1);
        if (tRailEw != null) {
            NBTTagCompound tTagEw = new NBTTagCompound();
            tTagEw.setString("track", "railcraft:track.electric.wye");
            tRailEw.stackTagCompound = tTagEw;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Copper, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailEw)
                .duration(20 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Gold, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailEw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailEw))
                .duration(20 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailEw))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Platinum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailEw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.VanadiumGallium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailEw))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Naquadah, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailEw))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // --- Electric Junction Track ---
        ItemStack tRailEJ = getModItem(Railcraft.ID, "track.electric", 1);
        if (tRailEJ != null) {
            NBTTagCompound tTagREJ = new NBTTagCompound();
            tTagREJ.setString("track", "railcraft:track.electric.junction");
            tRailEJ.stackTagCompound = tTagREJ;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Copper, 4),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(tRailEJ)
                .duration(20 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Gold, 2),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(2, tRailEJ))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(4, tRailEJ))
                .duration(20 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(8, tRailEJ))
                .duration(20 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Platinum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(16, tRailEJ))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.VanadiumGallium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(32, tRailEJ))
                .duration(20 * SECONDS)
                .eut(256)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(2, tRailEl),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Naquadah, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(GT_Utility.copyAmount(64, tRailEJ))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
        }
        // Shunting Wire
        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(1))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(1))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(16)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Lead, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(4))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Gold, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(8))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(16))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(48)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Platinum, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titanium, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(32))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(assemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 1),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Platinum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(ItemList.RC_ShuntingWire.get(64))
                .fluidInputs(tMat.getMolten(16L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

            // chunkloader upgrade OC

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Plastic_Advanced.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Aluminium, 2),
                    getModItem(Railcraft.ID, "machine.alpha", 1L, 0),
                    getModItem(OpenComputers.ID, "item", 1L, 26),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(OpenComputers.ID, "item", 1L, 62))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(256)
                .addTo(assemblerRecipes);

        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.piston, 1, 0),
                ItemList.FR_Casing_Sturdy.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(NewHorizonsCoreMod.ID, "item.EngineCore", 1L, 0))
            .fluidInputs(Materials.SeedOil.getFluid(250))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.piston, 1, 0),
                ItemList.FR_Casing_Sturdy.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(NewHorizonsCoreMod.ID, "item.EngineCore", 1L, 0))
            .fluidInputs(Materials.Lubricant.getFluid(125))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(ExtraUtilities.ID, "trashcan", 1L, 0),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Obsidian, 4),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 11))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.EngineCore", 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyCopper, 10),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 7))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.EngineCore", 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 10),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 8))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.EngineCore", 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 10),
                GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 9))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Gold, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 4),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 1, 0))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 2L, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnyIron, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 2L, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Bronze, 2),
                new ItemStack(Blocks.iron_bars, 2, 0),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .duration(10 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 2L, 14))
            .duration(20 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 2L, 14))
            .duration(20 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.SteelBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .duration(20 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Plastic, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.AluminiumBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .duration(10 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 4))
            .duration(20 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 4))
            .duration(20 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.StainlessSteelBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .duration(20 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 7))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 7))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Titanium, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.TitaniumBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.TungstenSteelBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 13))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 2L, 13))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NiobiumTitanium, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.ChromeBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .duration(10 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 1))
            .duration(20 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 1))
            .duration(20 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Enderium, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.IridiumBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .duration(20 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 4))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 4))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.OsmiumBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                new ItemStack(Blocks.glass_pane, 2, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 7))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                getModItem(TinkerConstruct.ID, "GlassPane", 2L, 0),
                GT_Utility.getIntegratedCircuit(21))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 2L, 7))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2),
                getModItem(NewHorizonsCoreMod.ID, "item.NeutroniumBars", 2),
                GT_Utility.getIntegratedCircuit(22))
            .itemOutputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Water Tank

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 1L, 14))
            .fluidInputs(Materials.Glue.getFluid(36))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 1L, 14))
            .fluidInputs(Materials.Glue.getFluid(36))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 2))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 1L, 14))
            .fluidInputs(Materials.Glue.getFluid(36))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 4),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 4))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 3L, 14))
            .fluidInputs(Materials.Glue.getFluid(72))
            .duration(20 * SECONDS)
            .eut(30)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 2),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.WoodSealed, 1))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 3L, 14))
            .fluidInputs(Materials.Plastic.getMolten(36))
            .duration(20 * SECONDS)
            .eut(30)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.StainlessSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.WoodSealed, 4))
            .itemOutputs(getModItem(Railcraft.ID, "machine.alpha", 9L, 14))
            .fluidInputs(Materials.Plastic.getMolten(72))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Steam Boilers

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Item_Casing_Iron.get(6), GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 3))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Item_Casing_Steel.get(6), GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(getModItem(Railcraft.ID, "machine.beta", 1L, 4))
            .duration(20 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);
    }

    public void withBartWorks() {
        if (!BartWorks.isModLoaded()) {
            return;
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, ExternalMaterials.getRhodiumPlatedPalladium(), 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Chrome, 1))
            .itemOutputs(ItemList.Casing_Advanced_Rhodium_Palladium.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

    }

    public void withGalacticraftMars() {
        if (!GalacticraftMars.isModLoaded()) {
            return;
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Bronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Aluminium, 1),
                GT_OreDictUnificator.get(OrePrefixes.compressed, Materials.Steel, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Ingot_Heavy1.get(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(72))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(GalacticraftCore.ID, "item.heavyPlating", 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.MeteoricIron, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Ingot_Heavy2.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(72))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(GalacticraftMars.ID, "item.null", 1L, 3),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Desh, 4),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Ingot_Heavy3.get(1))
            .fluidInputs(Materials.Platinum.getMolten(72))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    public void withGalaxySpace() {
        if (!GalaxySpace.isModLoaded()) {
            return;
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2),
                ItemList.Electric_Pump_MV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 2000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4),
                ItemList.Electric_Pump_HV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 4000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6),
                ItemList.Electric_Pump_EV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 6000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8),
                ItemList.Electric_Pump_IV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 8000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(
                    OrePrefixes.wireGt01,
                    Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    15),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10),
                ItemList.Electric_Pump_LuV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 15))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 12000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 18),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12),
                ItemList.Electric_Pump_ZPM.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 16000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuvwire, 21),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14),
                ItemList.Electric_Pump_UV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 21))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 20000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Longasssuperconductornameforuhvwire, 24),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16),
                ItemList.Electric_Pump_UHV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 24000))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEVBase, 27),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Infinity, 18),
                ItemList.Electric_Pump_UEV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 27))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 28000))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMVBase, 33),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, MaterialsUEVplus.SpaceTime, 22),
                ItemList.Electric_Pump_UMV.get(1),
                GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 33))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("liquid helium"), 36000))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
    }

    public void withGTNHLanthAndGTPP() {
        if (!(GTNHLanthanides.isModLoaded() && GTPlusPlus.isModLoaded())) {
            return;
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_EV.get(4),
                ItemList.Field_Generator_EV.get(4),
                getModItem(GTPlusPlus.ID, "itemPlateInconel690", 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Titanium, 16),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.BorosilicateGlass, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polytetrafluoroethylene, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 4),
                ItemList.Shape_Extruder_Wire.get(16))
            .itemOutputs(ItemList.Spinneret.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    public void withIC2NuclearControl() {
        if (!IC2NuclearControl.isModLoaded()) { // Card recycling recipes
            return;
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemVanillaMachineCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemInventoryScannerCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemEnergySensorLocationCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(IC2NuclearControl.ID, "RFSensorCard", 1L, 0), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemMultipleSensorLocationCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // counter

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemMultipleSensorLocationCard", 1L, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // liquid

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemMultipleSensorLocationCard", 1L, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // generator

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemLiquidArrayLocationCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // 2-6 liquid

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemEnergyArrayLocationCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // 2-6 energy

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "ItemSensorLocationCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // non-fluid nuke

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(IC2NuclearControl.ID, "Item55ReactorCard", 1L, 0),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(IC2NuclearControl.ID, "CardAppeng", 1L, 0), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.NC_SensorCard.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_ModHandler.getIC2Item("electronicCircuit", 3))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

    }
}
