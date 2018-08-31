package gtPlusPlus.core.recipe.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.LOADER_Machine_Components;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class CI {

	//null
	public static ItemStack _NULL = ItemUtils.getSimpleStack(ModItems.AAA_Broken);

	//bits
	public static long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
			| GT_ModHandler.RecipeBits.BUFFERED;
	public static long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
			| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;

	//Circuits
	public static Object circuitPrimitive;
	public static Object circuitTier1;
	public static Object circuitTier2;
	public static Object circuitTier3;
	public static Object circuitTier4;
	public static Object circuitTier5;
	public static Object circuitTier6;
	public static Object circuitTier7;
	public static Object circuitTier8;
	public static Object circuitTier9;

	//Machine Components
	public static ItemStack electricMotor_ULV;
	public static ItemStack electricMotor_LV;
	public static ItemStack electricMotor_MV;
	public static ItemStack electricMotor_HV;
	public static ItemStack electricMotor_EV;
	public static ItemStack electricMotor_IV;
	public static ItemStack electricMotor_LuV;
	public static ItemStack electricMotor_ZPM;
	public static ItemStack electricMotor_UV;
	public static ItemStack electricMotor_MAX;
	public static ItemStack electricPump_ULV;
	public static ItemStack electricPump_LV;
	public static ItemStack electricPump_MV;
	public static ItemStack electricPump_HV;
	public static ItemStack electricPump_EV;
	public static ItemStack electricPump_IV;
	public static ItemStack electricPump_LuV;
	public static ItemStack electricPump_ZPM;
	public static ItemStack electricPump_UV;
	public static ItemStack electricPump_MAX;
	public static ItemStack electricPiston_ULV;
	public static ItemStack electricPiston_LV;
	public static ItemStack electricPiston_MV;
	public static ItemStack electricPiston_HV;
	public static ItemStack electricPiston_EV;
	public static ItemStack electricPiston_IV;
	public static ItemStack electricPiston_LuV;
	public static ItemStack electricPiston_ZPM;
	public static ItemStack electricPiston_UV ;
	public static ItemStack electricPiston_MAX;
	public static ItemStack robotArm_ULV;
	public static ItemStack robotArm_LV;
	public static ItemStack robotArm_MV;
	public static ItemStack robotArm_HV;
	public static ItemStack robotArm_EV;
	public static ItemStack robotArm_IV;
	public static ItemStack robotArm_LuV;
	public static ItemStack robotArm_ZPM;
	public static ItemStack robotArm_UV;
	public static ItemStack robotArm_MAX;
	public static ItemStack conveyorModule_ULV;
	public static ItemStack conveyorModule_LV;
	public static ItemStack conveyorModule_MV;
	public static ItemStack conveyorModule_HV;
	public static ItemStack conveyorModule_EV;
	public static ItemStack conveyorModule_IV;
	public static ItemStack conveyorModule_LuV;
	public static ItemStack conveyorModule_ZPM;
	public static ItemStack conveyorModule_UV;
	public static ItemStack conveyorModule_MAX;
	public static ItemStack emitter_ULV;
	public static ItemStack emitter_LV;
	public static ItemStack emitter_MV;
	public static ItemStack emitter_HV;
	public static ItemStack emitter_EV;
	public static ItemStack emitter_IV;
	public static ItemStack emitter_LuV;
	public static ItemStack emitter_ZPM;
	public static ItemStack emitter_UV;
	public static ItemStack emitter_MAX;
	public static ItemStack fieldGenerator_ULV;
	public static ItemStack fieldGenerator_LV;
	public static ItemStack fieldGenerator_MV;
	public static ItemStack fieldGenerator_HV;
	public static ItemStack fieldGenerator_EV;
	public static ItemStack fieldGenerator_IV;
	public static ItemStack fieldGenerator_LuV;
	public static ItemStack fieldGenerator_ZPM;
	public static ItemStack fieldGenerator_UV;
	public static ItemStack fieldGenerator_MAX;
	public static ItemStack sensor_ULV;
	public static ItemStack sensor_LV;
	public static ItemStack sensor_MV;
	public static ItemStack sensor_HV;
	public static ItemStack sensor_EV;
	public static ItemStack sensor_IV;
	public static ItemStack sensor_LuV;
	public static ItemStack sensor_ZPM;
	public static ItemStack sensor_UV;
	public static ItemStack sensor_MAX;

	//Machine Casings
	public static ItemStack machineCasing_ULV;
	public static ItemStack machineCasing_LV;
	public static ItemStack machineCasing_MV;
	public static ItemStack machineCasing_HV;
	public static ItemStack machineCasing_EV;
	public static ItemStack machineCasing_IV;
	public static ItemStack machineCasing_LuV;
	public static ItemStack machineCasing_ZPM;
	public static ItemStack machineCasing_UV;
	public static ItemStack machineCasing_MAX;

	//Machine Hulls
	public static ItemStack machineHull_ULV;
	public static ItemStack machineHull_LV;
	public static ItemStack machineHull_MV;
	public static ItemStack machineHull_HV;
	public static ItemStack machineHull_EV;
	public static ItemStack machineHull_IV;
	public static ItemStack machineHull_LuV;
	public static ItemStack machineHull_ZPM;
	public static ItemStack machineHull_UV;
	public static ItemStack machineHull_MAX;

	//Gearbox Casings
	public static ItemStack gearboxCasing_Tier_1;
	public static ItemStack gearboxCasing_Tier_2;
	public static ItemStack gearboxCasing_Tier_3;
	public static ItemStack gearboxCasing_Tier_4;

	public static String[] component_Plate;
	public static String[] component_Rod;
	public static String[] component_Ingot;

	//Crafting Tools
	public static String craftingToolWrench = "craftingToolWrench";
	public static String craftingToolHammer_Hard = "craftingToolHardHammer";
	public static String craftingToolHammer_Soft = "craftingToolSoftHammer";
	public static String craftingToolScrewdriver = "craftingToolScrewdriver";
	public static String craftingToolFile = "craftingToolFile";
	public static String craftingToolMortar = "craftingToolMortar";
	public static String craftingToolKnife = "craftingToolKnife";
	public static String craftingToolCrowbar = "craftingToolCrowbar";
	public static String craftingToolSaw = "craftingToolSaw";
	public static String craftingToolWireCutter = "craftingToolWirecutter";
	public static String craftingToolSolderingIron = "craftingToolSolderingIron";

	//Explosives
	public static ItemStack explosivePowderKeg;
	public static ItemStack explosiveTNT;
	public static ItemStack explosiveITNT;

	public static void preInit(){
		
		//Tiered Components
		component_Plate = new String[]{
				getTieredComponent(OrePrefixes.plate, 0),
				getTieredComponent(OrePrefixes.plate, 1),
				getTieredComponent(OrePrefixes.plate, 2),
				getTieredComponent(OrePrefixes.plate, 3),
				getTieredComponent(OrePrefixes.plate, 4),
				getTieredComponent(OrePrefixes.plate, 5),
				getTieredComponent(OrePrefixes.plate, 6),
				getTieredComponent(OrePrefixes.plate, 7),
				getTieredComponent(OrePrefixes.plate, 8),
				getTieredComponent(OrePrefixes.plate, 9),
				getTieredComponent(OrePrefixes.plate, 10),
				getTieredComponent(OrePrefixes.plate, 11)	
		};
		component_Rod = new String[]{
				getTieredComponent(OrePrefixes.stick, 0),
				getTieredComponent(OrePrefixes.stick, 1),
				getTieredComponent(OrePrefixes.stick, 2),
				getTieredComponent(OrePrefixes.stick, 3),
				getTieredComponent(OrePrefixes.stick, 4),
				getTieredComponent(OrePrefixes.stick, 5),
				getTieredComponent(OrePrefixes.stick, 6),
				getTieredComponent(OrePrefixes.stick, 7),
				getTieredComponent(OrePrefixes.stick, 8),
				getTieredComponent(OrePrefixes.stick, 9),
				getTieredComponent(OrePrefixes.stick, 10),
				getTieredComponent(OrePrefixes.stick, 11)					
		};
		component_Ingot = new String[]{
				getTieredComponent(OrePrefixes.ingot, 0),
				getTieredComponent(OrePrefixes.ingot, 1),
				getTieredComponent(OrePrefixes.ingot, 2),
				getTieredComponent(OrePrefixes.ingot, 3),
				getTieredComponent(OrePrefixes.ingot, 4),
				getTieredComponent(OrePrefixes.ingot, 5),
				getTieredComponent(OrePrefixes.ingot, 6),
				getTieredComponent(OrePrefixes.ingot, 7),
				getTieredComponent(OrePrefixes.ingot, 8),
				getTieredComponent(OrePrefixes.ingot, 9),
				getTieredComponent(OrePrefixes.ingot, 10),
				getTieredComponent(OrePrefixes.ingot, 11)					
		};

		//Circuits
		circuitPrimitive = getTieredCircuit(0);
		circuitTier1 = getTieredCircuit(1);
		circuitTier2 = getTieredCircuit(2);
		circuitTier3 = getTieredCircuit(3);
		circuitTier4 = getTieredCircuit(4);
		circuitTier5 = getTieredCircuit(5);
		circuitTier6 = getTieredCircuit(6);
		circuitTier7 = getTieredCircuit(7);
		circuitTier8 = getTieredCircuit(8);
		circuitTier9 = getTieredCircuit(9);

	}	

	public static Object getTieredCircuit(int tier){
		if (CORE.ConfigSwitches.enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && !CORE.GTNH){
			if (tier == 0){
				return GregtechItemList.Old_Circuit_Primitive.get(1);
			}
			else if (tier == 1){
				return GregtechItemList.Old_Circuit_Basic.get(1);				
			}
			else if (tier == 2){
				return GregtechItemList.Old_Circuit_Good.get(1);				
			}
			else if (tier == 3){
				return GregtechItemList.Old_Circuit_Advanced.get(1);				
			}
			else if (tier == 4){
				return GregtechItemList.Old_Circuit_Data.get(1);				
			}
			else if (tier == 5){
				return GregtechItemList.Old_Circuit_Elite.get(1);				
			}
			else if (tier == 6){
				return GregtechItemList.Old_Circuit_Master.get(1);				
			}
			else if (tier == 7){
				return GregtechItemList.Old_Circuit_Ultimate.get(1);				
			}
			else if (tier == 8){
				return GregtechItemList.Circuit_IV.get(1);				
			}
			else if (tier == 9){
				return GregtechItemList.Circuit_LuV.get(1);				
			}
			else if (tier == 10){
				return GregtechItemList.Circuit_ZPM.get(1);				
			}
		}
		else {
			return getTieredCircuitOreDictName((CORE.GTNH && tier >= 6 ? tier - 1 : tier));
		}
		return _NULL;
	}

	public static ItemStack[] getAllCircuitsOfTier(int tier){		
		return ItemUtils.getStackOfAllOreDictGroup(getTieredCircuitOreDictName(tier));
	}

	public static String getTieredCircuitOreDictName(int tier){
		if (tier == 0){
			return "circuitPrimitive";
		}
		else if (tier == 1){	
			return "circuitBasic";			
		}
		else if (tier == 2){	
			return "circuitGood";			
		}
		else if (tier == 3){		
			return "circuitAdvanced";			
		}
		else if (tier == 4){	
			return "circuitData";			
		}
		else if (tier == 5){	
			return "circuitElite";			
		}
		else if (tier == 6){	
			return "circuitMaster";				
		}
		else if (tier == 7){
			return "circuitUltimate";
		}
		else if (tier == 8){
			return "circuitSuperconductor";				
		}
		else if (tier == 9){
			return "circuitInfinite";				
		}
		else if (tier == 10){
			return "circuitQuantum";				
		}
		else {
			return "circuitPrimitive";
		}
	}

	public static ItemStack getNumberedCircuit(int Meta){
		return ItemUtils.getGregtechCircuit(Meta);
	}

	private static Object getMaterialFromTier(int tier){
		if (tier == 0){
			return Materials.Wood;
		}
		else if (tier == 1){
			return Materials.Lead;
		}
		else if (tier == 2){
			return Materials.Bronze;
		}
		else if (tier == 3){
			return Materials.Steel;
		}
		else if (tier == 4){
			return ALLOY.EGLIN_STEEL;
		}
		else if (tier == 5){
			return Materials.Aluminium;
		}
		else if (tier == 6){
			return ALLOY.MARAGING250;
		}
		else if (tier == 7){
			return ALLOY.TANTALLOY_61;
		}
		else if (tier == 8){
			return ALLOY.INCONEL_792;
		}
		else if (tier == 9){
			return ALLOY.ZERON_100;
		}
		else if (tier == 10){
			return Materials.NaquadahEnriched;
		}
		else if (tier == 11){
			return Materials.Neutronium;
		}
		return Materials._NULL;
	}

	public static String getTieredComponent(OrePrefixes type, int tier){
		Object material = getMaterialFromTier(tier);
		if (material != null){
			if (material instanceof Materials){
				//return (ItemStack) type.get(material);
				String materialName = ((Materials) material).mDefaultLocalName;
				Logger.INFO("Searching for a component named "+type.name()+materialName);
				//return ItemUtils.getItemStackOfAmountFromOreDict(type.name()+materialName, 1);
				return (type.name()+materialName);
			}
			else {
				String materialName = (Utils.sanitizeString(((Material) material).getLocalizedName()));
				Logger.INFO("Searching for a component named "+type.name()+materialName);
				//return ItemUtils.getItemStackOfAmountFromOreDict(type.name()+materialName, 1);
				return (type.name()+materialName);
			}
		}
		Logger.INFO("[Components] Failed getting a tiered component. "+type.name()+" | "+tier);
		return null;
	}

	public static ItemStack getDataOrb(){
		if (CORE.ConfigSwitches.enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && !CORE.GTNH){
			return GregtechItemList.Old_Tool_DataOrb.get(1);
		}
		else {
			return ItemList.Tool_DataOrb.get(1);				
		}
	}
	
	public static ItemStack getDataStick(){
		if (CORE.ConfigSwitches.enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && !CORE.GTNH){
			return GregtechItemList.Old_Tool_DataStick.get(1);
		}
		else {
			return ItemList.Tool_DataStick.get(1);				
		}
	}

	public static final ItemStack getTieredMachineHull(int tier){
		if (tier == 0){
			return machineHull_ULV;
		}
		else if (tier == 1){
			return machineHull_LV;
		}
		else if (tier == 2){
			return machineHull_MV;
		}
		else if (tier == 3){
			return machineHull_HV;
		}
		else if (tier == 4){
			return machineHull_EV;
		}
		else if (tier == 5){
			return machineHull_IV;
		}
		else if (tier == 6){
			return machineHull_LuV;
		}
		else if (tier == 7){
			return machineHull_ZPM;
		}
		else if (tier == 8){
			return machineHull_UV;
		}
		else {
			return machineHull_MAX;			
		}
	}

	public static final ItemStack getTieredMachineCasing(int tier){
		if (tier == 0){
			return machineCasing_ULV;
		}
		else if (tier == 1){
			return machineCasing_LV;
		}
		else if (tier == 2){
			return machineCasing_MV;
		}
		else if (tier == 3){
			return machineCasing_HV;
		}
		else if (tier == 4){
			return machineCasing_EV;
		}
		else if (tier == 5){
			return machineCasing_IV;
		}
		else if (tier == 6){
			return machineCasing_LuV;
		}
		else if (tier == 7){
			return machineCasing_ZPM;
		}
		else if (tier == 8){
			return machineCasing_UV;
		}
		else {
			return machineCasing_MAX;			
		}
	}

	public static void init() {		
		//Set Explosives
		try {
			if (ItemList.valueOf("Block_Powderbarrel") != null){
				explosivePowderKeg = ItemList.valueOf("Block_Powderbarrel").get(1).copy();
			}
		} catch (java.lang.IllegalArgumentException Y) {
			explosivePowderKeg = ItemUtils.getSimpleStack(Items.gunpowder).copy();
		}
		explosiveTNT = ItemUtils.getSimpleStack(Blocks.tnt).copy();
		explosiveITNT = Ic2Items.industrialTnt.copy();

		//Machine Casings
		machineCasing_ULV = ItemList.Casing_ULV.get(1);
		machineCasing_LV = ItemList.Casing_LV.get(1);
		machineCasing_MV = ItemList.Casing_MV.get(1);
		machineCasing_HV = ItemList.Casing_HV.get(1);
		machineCasing_EV = ItemList.Casing_EV.get(1);
		machineCasing_IV = ItemList.Casing_IV.get(1);
		machineCasing_LuV = ItemList.Casing_LuV.get(1);
		machineCasing_ZPM = ItemList.Casing_ZPM.get(1);
		machineCasing_UV = ItemList.Casing_UV.get(1);
		machineCasing_MAX = ItemList.Casing_MAX.get(1);

		//Machine Hulls
		machineHull_ULV = ItemList.Hull_ULV.get(1);
		machineHull_LV = ItemList.Hull_LV.get(1);
		machineHull_MV = ItemList.Hull_MV.get(1);
		machineHull_HV = ItemList.Hull_HV.get(1);
		machineHull_EV = ItemList.Hull_EV.get(1);
		machineHull_IV = ItemList.Hull_IV.get(1);
		machineHull_LuV = ItemList.Hull_LuV.get(1);
		machineHull_ZPM = ItemList.Hull_ZPM.get(1);
		machineHull_UV = ItemList.Hull_UV.get(1);
		machineHull_MAX = ItemList.Hull_MAX.get(1);

		//Gear box Casings
		gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
		gearboxCasing_Tier_2 = ItemList.Casing_Gearbox_Steel.get(1);
		gearboxCasing_Tier_3 = ItemList.Casing_Gearbox_Titanium.get(1);
		gearboxCasing_Tier_4 = ItemList.Casing_Gearbox_TungstenSteel.get(1);

		//Machine Components
		LOADER_Machine_Components.initialise();
	}

	public static ItemStack emptyCells(int i) {
		return ItemUtils.getEmptyCell(i);
	}

}
