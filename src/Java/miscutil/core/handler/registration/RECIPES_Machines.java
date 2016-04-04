package miscutil.core.handler.registration;

import gregtech.api.enums.ItemList;
import miscutil.core.util.ItemUtils;
import miscutil.core.util.Utils;
import miscutil.gregtech.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_Machines {

	//Outputs
	//static ItemStack RECIPE_BufferCore_ULV = new ItemStack(GregtechEnergyBuffer.itemBufferCore);
	static ItemStack RECIPE_SteamCondenser = GregtechItemList.Condensor_MAX.get(1);
	static ItemStack RECIPE_IronBlastFurnace = GregtechItemList.Machine_Iron_BlastFurnace.get(1);
	static ItemStack RECIPE_IronPlatedBricks = GregtechItemList.Casing_IronPlatedBricks.get(1);
	static ItemStack RECIPE_Buffer_ULV = GregtechItemList.Energy_Buffer_1by1_ULV.get(1);
	static ItemStack RECIPE_Buffer_LV = GregtechItemList.Energy_Buffer_1by1_LV.get(1);
	static ItemStack RECIPE_Buffer_MV = GregtechItemList.Energy_Buffer_1by1_MV.get(1);
	static ItemStack RECIPE_Buffer_HV = GregtechItemList.Energy_Buffer_1by1_HV.get(1);
	static ItemStack RECIPE_Buffer_EV = GregtechItemList.Energy_Buffer_1by1_EV.get(1);
	static ItemStack RECIPE_Buffer_IV = GregtechItemList.Energy_Buffer_1by1_IV.get(1);
	static ItemStack RECIPE_Buffer_LuV = GregtechItemList.Energy_Buffer_1by1_LuV.get(1);
	static ItemStack RECIPE_Buffer_ZPM = GregtechItemList.Energy_Buffer_1by1_ZPM.get(1);
	static ItemStack RECIPE_Buffer_UV = GregtechItemList.Energy_Buffer_1by1_UV.get(1);
	static ItemStack RECIPE_Buffer_MAX = GregtechItemList.Energy_Buffer_1by1_MAX.get(1);
	

	//Buffer Cores
	static ItemStack RECIPE_BufferCore_ULV = Utils.getItemStack("miscutils:item.itemBufferCore1", 1);
	static ItemStack RECIPE_BufferCore_LV = Utils.getItemStack("miscutils:item.itemBufferCore2", 1);
	static ItemStack RECIPE_BufferCore_MV = Utils.getItemStack("miscutils:item.itemBufferCore3", 1);
	static ItemStack RECIPE_BufferCore_HV = Utils.getItemStack("miscutils:item.itemBufferCore4", 1);
	static ItemStack RECIPE_BufferCore_EV = Utils.getItemStack("miscutils:item.itemBufferCore5", 1);
	static ItemStack RECIPE_BufferCore_IV = Utils.getItemStack("miscutils:item.itemBufferCore6", 1);
	static ItemStack RECIPE_BufferCore_LuV = Utils.getItemStack("miscutils:item.itemBufferCore7", 1);
	static ItemStack RECIPE_BufferCore_ZPM = Utils.getItemStack("miscutils:item.itemBufferCore8", 1);
	static ItemStack RECIPE_BufferCore_UV = Utils.getItemStack("miscutils:item.itemBufferCore9", 1);
	static ItemStack RECIPE_BufferCore_MAX = Utils.getItemStack("miscutils:item.itemBufferCore10", 1);


	//Wire
	static String wireTier1 = "wireGt08Lead";
	static String wireTier2 = "wireGt08Tin";
	static String wireTier3 = "wireGt08Copper";
	static String wireTier4 = "wireGt08Gold";
	static String wireTier5 = "wireGt08Aluminium";
	static String wireTier6 = "wireGt08Tungsten";
	static String wireTier7 = "wireGt08Osmium";
	static String wireTier8 = "wireGt08Naquadah";
	static String wireTier9 = "wireGt08Superconductor";
	static String wireTier10 = "wireGt16Superconductor";

	//Wire
	static String cableTier1 = "cableGt04Lead";
	static String cableTier2 = "cableGt04Tin";
	static String cableTier3 = "cableGt04Copper";
	static String cableTier4 = "cableGt04Gold";
	static String cableTier5 = "cableGt04Aluminium";
	static String cableTier6 = "cableGt04Tungsten";
	static String cableTier7 = "cableGt04Osmium";
	static String cableTier8 = "cableGt04Naquadah";
	static String cableTier9 = "cableGt04NiobiumTitanium";
	static String cableTier10 = "cableGt08NiobiumTitanium";


	//Plates
	static String plateTier1 = "plateLead";
	static String plateTier2 = "plateTin";
	static String plateTier3 = "plateCopper";
	static String plateTier4 = "plateGold";
	static String plateTier5 = "plateAluminium";
	static String plateTier6 = "plateThorium";
	static String plateTier7 = "plateTungsten";
	static String plateTier8 = "plateTungstenSteel";
	static String plateTier9 = "plateOsmium";
	static String plateTier10 = "plateNaquadah";
	static String plateTier11 = "plateNeutronium";


	//Machine Casings
	static ItemStack machineCasing_ULV = ItemList.Casing_ULV.get(1);
	static ItemStack machineCasing_LV = ItemList.Casing_LV.get(1);
	static ItemStack machineCasing_MV = ItemList.Casing_MV.get(1);
	static ItemStack machineCasing_HV = ItemList.Casing_HV.get(1);
	static ItemStack machineCasing_EV = ItemList.Casing_EV.get(1);
	static ItemStack machineCasing_IV = ItemList.Casing_IV.get(1);
	static ItemStack machineCasing_LuV = ItemList.Casing_LuV.get(1);
	static ItemStack machineCasing_ZPM = ItemList.Casing_ZPM.get(1);
	static ItemStack machineCasing_UV = ItemList.Casing_UV.get(1);
	static ItemStack machineCasing_MAX = ItemList.Casing_MAX.get(1);

	//Cables
	static String cableGt02Electrum = "cableGt02Electrum";


	//Plates
	static String plateElectricalSteel= "plateElectricalSteel";	
	static String plateEnergeticAlloy= "plateEnergeticAlloy";

	//Pipes
	static String pipeLargeCopper="pipeLargeCopper";
	static String pipeHugeSteel="pipeHugeSteel";

	//Pumps
	//gregtech:gt.metaitem.01:32611
	static ItemStack pump_MV = ItemList.Electric_Pump_MV.get(1);

	//Lava Boiler
	static ItemStack boiler_Coal = ItemList.Machine_Bronze_Boiler.get(1);
	static ItemStack blockBricks = Utils.getItemStack("minecraft:brick_block", 1);

	//Batteries
	static String batteryBasic = "batteryBasic";
	static String batteryAdvanced = "batteryAdvanced";
	static String batteryElite = "batteryElite";
	static String batteryMaster = "batteryMaster";
	static String batteryUltimate = "batteryUltimate";

	//Circuits
	static String circuitPrimitive = "circuitPrimitive";
	static String circuitBasic = "circuitBasic";
	static String circuitGood = "circuitGood";
	static String circuitAdvanced = "circuitAdvanced";
	static String circuitData = "circuitData";
	static String circuitElite = "circuitElite";
	static String circuitMaster = "circuitMaster";
	static String circuitUltimate = "circuitUltimate";


	public static final void RECIPES_LOAD(){
		run();
		Utils.LOG_INFO("Loading Recipes for the Various machine blocks.");
	}

	private static void run(){
		//Buffer Core
		ItemUtils.recipeBuilder(
				plateTier1, cableTier1, plateTier1,
				circuitPrimitive, batteryBasic, circuitPrimitive,
				plateTier1, cableTier1, plateTier1,
				RECIPE_BufferCore_ULV);
		ItemUtils.recipeBuilder(
				plateTier2, cableTier2, plateTier2,
				circuitBasic, batteryBasic, circuitBasic,
				plateTier2, cableTier2, plateTier2,
				RECIPE_BufferCore_LV);
		ItemUtils.recipeBuilder(
				plateTier3, cableTier3, plateTier3,
				circuitGood, batteryAdvanced, circuitGood,
				plateTier3, cableTier3, plateTier3,
				RECIPE_BufferCore_MV);
		ItemUtils.recipeBuilder(
				plateTier4, cableTier4, plateTier4,
				circuitAdvanced, batteryAdvanced, circuitAdvanced,
				plateTier4, cableTier4, plateTier4,
				RECIPE_BufferCore_HV);
		ItemUtils.recipeBuilder(
				plateTier5, cableTier5, plateTier5,
				circuitData, batteryElite, circuitData,
				plateTier5, cableTier5, plateTier5,
				RECIPE_BufferCore_EV);

		ItemUtils.recipeBuilder(
				plateTier6, cableTier6, plateTier6,
				circuitData, batteryElite, circuitElite,
				plateTier6, cableTier6, plateTier6,
				RECIPE_BufferCore_IV);
		ItemUtils.recipeBuilder(
				plateTier7, cableTier7, plateTier7,
				circuitElite, batteryMaster, circuitElite,
				plateTier7, cableTier7, plateTier7,
				RECIPE_BufferCore_LuV);
		ItemUtils.recipeBuilder(
				plateTier8, cableTier8, plateTier8,
				circuitMaster, batteryMaster, circuitMaster,
				plateTier8, cableTier8, plateTier8,
				RECIPE_BufferCore_ZPM);
		ItemUtils.recipeBuilder(
				plateTier9, cableTier9, plateTier9,
				circuitMaster, batteryUltimate, circuitUltimate,
				plateTier9, cableTier9, plateTier9,
				RECIPE_BufferCore_UV);
		ItemUtils.recipeBuilder(
				plateTier10, cableTier10, plateTier10,
				circuitUltimate, batteryUltimate, circuitUltimate,
				plateTier10, cableTier10, plateTier10,
				RECIPE_BufferCore_MAX);












		//Battery Buffer #1
		ItemUtils.recipeBuilder(
				wireTier1, RECIPE_BufferCore_ULV, wireTier1,
				wireTier1, machineCasing_ULV, wireTier1,
				circuitPrimitive, null, circuitPrimitive,
				RECIPE_Buffer_ULV);

		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier2, RECIPE_BufferCore_LV, wireTier2,
				wireTier2, machineCasing_LV, wireTier2,
				circuitPrimitive, circuitBasic, circuitPrimitive,
				RECIPE_Buffer_LV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier3, RECIPE_BufferCore_MV, wireTier3,
				wireTier3, machineCasing_MV, wireTier3,
				circuitBasic, circuitGood, circuitBasic,
				RECIPE_Buffer_MV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier4, RECIPE_BufferCore_HV, wireTier4,
				wireTier4, machineCasing_HV, wireTier4,
				circuitGood, circuitAdvanced, circuitGood,
				RECIPE_Buffer_HV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier5, RECIPE_BufferCore_EV, wireTier5,
				wireTier5, machineCasing_EV, wireTier5,
				circuitAdvanced, circuitElite, circuitAdvanced,
				RECIPE_Buffer_EV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier6, RECIPE_BufferCore_IV, wireTier6,
				wireTier6, machineCasing_IV, wireTier6,
				circuitElite, circuitMaster, circuitElite,
				RECIPE_Buffer_IV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier7, RECIPE_BufferCore_LuV, wireTier7,
				wireTier7, machineCasing_LuV, wireTier7,
				circuitMaster, circuitElite, circuitMaster,
				RECIPE_Buffer_LuV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
				wireTier8, machineCasing_ZPM, wireTier8,
				circuitMaster, circuitUltimate, circuitMaster,
				RECIPE_Buffer_ZPM);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier9, RECIPE_BufferCore_UV, wireTier9,
				wireTier9, machineCasing_UV, wireTier9,
				circuitUltimate, circuitMaster, circuitUltimate,
				RECIPE_Buffer_UV);
		//Battery Buffer
		ItemUtils.recipeBuilder(
				wireTier10, RECIPE_BufferCore_MAX, wireTier10,
				wireTier10, machineCasing_MAX, wireTier10,
				circuitUltimate, plateTier11, circuitUltimate,
				RECIPE_Buffer_MAX);


		//Steam Condenser
		ItemUtils.recipeBuilder(
				pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
				plateEnergeticAlloy, pump_MV, plateEnergeticAlloy,
				plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
				RECIPE_SteamCondenser);

		//Iron BF
		ItemUtils.recipeBuilder(
				"plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
				boiler_Coal, machineCasing_ULV, boiler_Coal,
				"plateDoubleAnyIron", "bucketLava", "plateDoubleAnyIron",
				RECIPE_IronBlastFurnace);

		//Iron plated Bricks
		ItemUtils.recipeBuilder(
				"plateAnyIron", RECIPES_Tools.craftingToolHardHammer, "plateAnyIron",
				"plateAnyIron", blockBricks, "plateAnyIron",
				"plateAnyIron", RECIPES_Tools.craftingToolWrench, "plateAnyIron",
				RECIPE_IronPlatedBricks);


		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");























	}			
}
