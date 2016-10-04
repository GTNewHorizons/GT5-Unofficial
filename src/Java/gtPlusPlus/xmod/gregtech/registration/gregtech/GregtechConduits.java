package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.lib.LoadedMods.Gregtech;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntityFluid;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntity_Cable;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntity_SuperConductor;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechConduits {
	/**
	 * 
	 * The Voltage Tiers. Use this Array instead of the old named Voltage Variables
	 * public static final long[] V = new long[] {0=8, 1=32, 2=128, 3=512, 4=2048, 5=8192, 6=32768, 7=131072, 8=524288, 9=Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
	 * 
	 */
	
	
	private static int BasePipeID = 30700;
	
	
	public static void run()
	{
		if (Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Custom Cables/Wires/Pipes.");
			run1();
		}
		
	}
	
	private static void run1(){
		if (LoadedMods.Big_Reactors){
			wireFactory("Blutonium", 8196, 30600, 8, 32, 2);
			wireFactory("Cyanite", 512, 30615, 2, 16, 4);
			wireFactory("Yellorium", 2048, 30630, 4, 16, 2);
		}
		if (LoadedMods.EnderIO){
			wireFactory("RedstoneAlloy", 32, 30645, 1, 4, 1);
		}
		
		superConductorFactory("Superconductor", 524288, 30660, 0, 0, 8);
		superConductorFactory("VoidMetal", 512, 30661, 0, 0, 8);
		
		generateNonGTFluidPipes(GT_Materials.Staballoy, BasePipeID, 6250, 7500, true);
		generateNonGTFluidPipes(GT_Materials.Tantalloy60, BasePipeID+5, 5000, 4250, true);
		generateNonGTFluidPipes(GT_Materials.Tantalloy61, BasePipeID+10, 6000, 5800, true);
		if (LoadedMods.Thaumcraft){
		generateNonGTFluidPipes(GT_Materials.Void, BasePipeID+15, 250, 25000, true);}
		generateGTFluidPipes(Materials.Europium, BasePipeID+20, 12000, 7500, true);
		generateNonGTFluidPipes(GT_Materials.Potin, BasePipeID+25, 375, 2000, true);
		generateNonGTFluidPipes(GT_Materials.MaragingSteel300, BasePipeID+30, 7000, 2500, true);
		generateNonGTFluidPipes(GT_Materials.MaragingSteel350, BasePipeID+35, 8000, 2500, true);
		generateNonGTFluidPipes(GT_Materials.Inconel690, BasePipeID+40, 7500, 4800, true);
		generateNonGTFluidPipes(GT_Materials.Inconel792, BasePipeID+45, 8000, 5500, true);
		generateNonGTFluidPipes(GT_Materials.HastelloyX, BasePipeID+50, 10000, 4200, true);
		
	}	
	
	private static void wireFactory(String Material, int Voltage, int ID, long insulatedLoss, long uninsulatedLoss, long Amps){
		Materials T = Materials.valueOf(Material);
		int V = 0;
		if (Voltage == 8){
			V = 0;
		}
		else if (Voltage == 32){
			V = 1;
		}
		else if (Voltage == 128){
			V = 2;
		}
		else if (Voltage == 512){
			V = 3;
		}
		else if (Voltage == 2048){
			V = 4;
		}
		else if (Voltage == 8196){
			V = 5;
		}
		else if (Voltage == 32768){
			V = 6;
		}
		else if (Voltage == 131072){
			V = 7;
		}
		else if (Voltage == 524288){
			V = 8;
		}
		else if (Voltage == Integer.MAX_VALUE){
			V = 9;
		}
		else {
			Utils.LOG_ERROR("Failed to set voltage on "+Material+". Invalid voltage of "+Voltage+"V set.");
			Utils.LOG_ERROR(Material+" has defaulted to 8v.");
			V = 0;
		}
		//makeWires(T, ID, 2L, 4L, 2L, GT_Values.V[V], true, false);	 
		makeWires(T, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false);	 
		//makeWires(T, ID, bEC ? 2L : 2L, bEC ? 4L : 4L, 2L, gregtech.api.enums.GT_Values.V[V], true, false);	 
	}	

	private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage, long aVoltage, boolean aInsulatable, boolean aAutoInsulated)
	{
		Utils.LOG_WARNING("Gregtech5u Content | Registered "+aMaterial.name() +" as a new material for Wire & Cable.");
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 0, "wire." + aMaterial.name().toLowerCase() + ".01", "1x " + aMaterial.mDefaultLocalName + " Wire", 0.125F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 1, "wire." + aMaterial.name().toLowerCase() + ".02", "2x " + aMaterial.mDefaultLocalName + " Wire", 0.25F, aMaterial, aLoss, 2L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 2, "wire." + aMaterial.name().toLowerCase() + ".04", "4x " + aMaterial.mDefaultLocalName + " Wire", 0.375F, aMaterial, aLoss, 4L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 3, "wire." + aMaterial.name().toLowerCase() + ".08", "8x " + aMaterial.mDefaultLocalName + " Wire", 0.5F, aMaterial, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 4, "wire." + aMaterial.name().toLowerCase() + ".12", "12x " + aMaterial.mDefaultLocalName + " Wire", 0.75F, aMaterial, aLoss, 12L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt16, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 5, "wire." + aMaterial.name().toLowerCase() + ".16", "16x " + aMaterial.mDefaultLocalName + " Wire", 1.0F, aMaterial, aLoss, 16L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		if (aInsulatable)
		{
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 6, "cable." + aMaterial.name().toLowerCase() + ".01", "1x " + aMaterial.mDefaultLocalName + " Cable", 0.25F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, false).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 7, "cable." + aMaterial.name().toLowerCase() + ".02", "2x " + aMaterial.mDefaultLocalName + " Cable", 0.375F, aMaterial, aLossInsulated, 2L * aAmperage, aVoltage, true, false).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 8, "cable." + aMaterial.name().toLowerCase() + ".04", "4x " + aMaterial.mDefaultLocalName + " Cable", 0.5F, aMaterial, aLossInsulated, 4L * aAmperage, aVoltage, true, false).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 9, "cable." + aMaterial.name().toLowerCase() + ".08", "8x " + aMaterial.mDefaultLocalName + " Cable", 0.75F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, false).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 10, "cable." + aMaterial.name().toLowerCase() + ".12", "12x " + aMaterial.mDefaultLocalName + " Cable", 1.0F, aMaterial, aLossInsulated, 12L * aAmperage, aVoltage, true, false).getStackForm(1L));
		}
	}
	
	private static void superConductorFactory(String Material, int Voltage, int ID, long insulatedLoss, long uninsulatedLoss, long Amps){
		GT_Materials T = GT_Materials.valueOf(Material);
		int V = 0;
		if (Voltage == 8){
			V = 0;
		}
		else if (Voltage == 32){
			V = 1;
		}
		else if (Voltage == 128){
			V = 2;
		}
		else if (Voltage == 512){
			V = 3;
		}
		else if (Voltage == 2048){
			V = 4;
		}
		else if (Voltage == 8196){
			V = 5;
		}
		else if (Voltage == 32768){
			V = 6;
		}
		else if (Voltage == 131072){
			V = 7;
		}
		else if (Voltage == 524288){
			V = 8;
		}
		else if (Voltage == Integer.MAX_VALUE){
			V = 9;
		}
		else {
			Utils.LOG_ERROR("Failed to set voltage on "+Material+". Invalid voltage of "+Voltage+"V set.");
			Utils.LOG_ERROR(Material+" has defaulted to 8v.");
			V = 0;
		}
		//makeWires(T, ID, 2L, 4L, 2L, GT_Values.V[V], true, false);	 
		makeSuperConductors(T, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false);	 
		//makeWires(T, ID, bEC ? 2L : 2L, bEC ? 4L : 4L, 2L, gregtech.api.enums.GT_Values.V[V], true, false);	 
	}	
	
	private static void makeSuperConductors(GT_Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage, long aVoltage, boolean aInsulatable, boolean aAutoInsulated)
	{
		Utils.LOG_WARNING("Gregtech5u Content | Registered "+aMaterial.name() +" as a new Super Conductor.");
		registerOre(GregtechOrePrefixes.type2, aMaterial, new GregtechMetaPipeEntity_SuperConductor(aStartID + 5, "wire." + aMaterial.name().toLowerCase() + ".16", "16x " + aMaterial.mDefaultLocalName + " Wire", 1.0F, aMaterial, aLoss, 16L * aAmperage, aVoltage, false, !aAutoInsulated).getStackForm(1L));
		
	}
	
	private static boolean registerOre(GregtechOrePrefixes aPrefix, Object aMaterial, ItemStack aStack) {
        return GT_OreDictUnificator.registerOre(aPrefix.get(aMaterial), aStack);
    }
	
	
	private static void generateGTFluidPipes(Materials material, int startID, int transferRatePerSec, int heatResistance, boolean isGasProof){
		int transferRatePerTick = transferRatePerSec/20;
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(material), new GT_MetaPipeEntity_Fluid(startID, "GT_Pipe_"+material.mDefaultLocalName+"_Tiny", "Tiny "+material.mDefaultLocalName+" Fluid Pipe", 0.25F, material, transferRatePerTick*2, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(material), new GT_MetaPipeEntity_Fluid(startID+1, "GT_Pipe_"+material.mDefaultLocalName+"_Small", "Small "+material.mDefaultLocalName+" Fluid Pipe", 0.375F, material, transferRatePerTick*4, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(material), new GT_MetaPipeEntity_Fluid(startID+2, "GT_Pipe_"+material.mDefaultLocalName+"", ""+material.mDefaultLocalName+" Fluid Pipe", 0.5F, material, transferRatePerTick*6, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(material), new GT_MetaPipeEntity_Fluid(startID+3, "GT_Pipe_"+material.mDefaultLocalName+"_Large", "Large "+material.mDefaultLocalName+" Fluid Pipe", 0.75F, material, transferRatePerTick*8, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(material), new GT_MetaPipeEntity_Fluid(startID+4, "GT_Pipe_"+material.mDefaultLocalName+"_Huge", "Huge "+material.mDefaultLocalName+" Fluid Pipe", 1.0F, material, transferRatePerTick*10, heatResistance, isGasProof).getStackForm(1L));
        generatePipeRecipes(material.mDefaultLocalName);
	}
	
	private static void generateNonGTFluidPipes(GT_Materials material, int startID, int transferRatePerSec, int heatResistance, boolean isGasProof){
		int transferRatePerTick = transferRatePerSec/20;
    	GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(material), new GregtechMetaPipeEntityFluid(startID, "GT_Pipe_"+material.mDefaultLocalName+"_Tiny", "Tiny "+material.mDefaultLocalName+" Fluid Pipe", 0.25F, material, transferRatePerTick*2, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(material), new GregtechMetaPipeEntityFluid(startID+1, "GT_Pipe_"+material.mDefaultLocalName+"_Small", "Small "+material.mDefaultLocalName+" Fluid Pipe", 0.375F, material, transferRatePerTick*4, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(material), new GregtechMetaPipeEntityFluid(startID+2, "GT_Pipe_"+material.mDefaultLocalName+"", ""+material.mDefaultLocalName+" Fluid Pipe", 0.5F, material, transferRatePerTick*6, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(material), new GregtechMetaPipeEntityFluid(startID+3, "GT_Pipe_"+material.mDefaultLocalName+"_Large", "Large "+material.mDefaultLocalName+" Fluid Pipe", 0.75F, material, transferRatePerTick*8, heatResistance, isGasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(material), new GregtechMetaPipeEntityFluid(startID+4, "GT_Pipe_"+material.mDefaultLocalName+"_Huge", "Huge "+material.mDefaultLocalName+" Fluid Pipe", 1.0F, material, transferRatePerTick*10, heatResistance, isGasProof).getStackForm(1L));
        generatePipeRecipes(material.mDefaultLocalName);
	
	}
	
	private static void generatePipeRecipes(String materialName){
		
		String output = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
		output = output.replace("-", "").replace("_", "").replace(" ", "");
		
		Utils.LOG_INFO("Generating "+output+" pipes & respective recipes.");		
		
		ItemStack pipeIngot = UtilsItems.getItemStackOfAmountFromOreDict("ingot"+output, 1).copy();
		ItemStack pipePlate = UtilsItems.getItemStackOfAmountFromOreDict("plate"+output, 1).copy();
		
		//Check all pipes are not null
		Utils.LOG_WARNING("Generated pipeTiny from "+ materialName +"? "+ ((UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Tiny"+output, 1) != null) ? true : false));
		Utils.LOG_WARNING("Generated pipeSmall from "+ materialName +"? "+ ((UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 1) != null) ? true : false));
		Utils.LOG_WARNING("Generated pipeNormal from "+ materialName +"? "+ ((UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 1) != null) ? true : false));
		Utils.LOG_WARNING("Generated pipeLarge from "+ materialName +"? "+ ((UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1) != null) ? true : false));
		Utils.LOG_WARNING("Generated pipeHuge from "+ materialName +"? "+ ((UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1) != null) ? true : false));
				
		int eut = 128;
		if (
				materialName.toLowerCase().contains("hastelloy") ||
				materialName.toLowerCase().contains("staballoy") ||
				materialName.toLowerCase().contains("tantalloy") ||
				materialName.toLowerCase().contains("europium") ||
				materialName.toLowerCase().contains("crystal") ||
				materialName.toLowerCase().contains("zeron") ||
				materialName.toLowerCase().contains("inconel")
				){
			eut = 512;
		}
		else {
			eut = 128;
		}
		
		//Add the Three Shaped Recipes First		
		UtilsRecipe.recipeBuilder(
				pipePlate, "craftingToolWrench", pipePlate,
				pipePlate, null, pipePlate,
				pipePlate, "craftingToolHardHammer", pipePlate,
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 6));
		
		UtilsRecipe.recipeBuilder(
				pipePlate, pipePlate, pipePlate,
				"craftingToolWrench", null, "craftingToolHardHammer",
				pipePlate, pipePlate, pipePlate,
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 2));
		
		UtilsRecipe.recipeBuilder(
				pipePlate, "craftingToolHardHammer", pipePlate,
				pipePlate, null, pipePlate,
				pipePlate, "craftingToolWrench", pipePlate,
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1));
		
		GT_Values.RA.addExtruderRecipe(
				UtilsItems.getSimpleStack(pipeIngot, 1),
				ItemList.Shape_Extruder_Pipe_Tiny.get(0),
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Tiny"+output, 2),
				5, eut);
		
		GT_Values.RA.addExtruderRecipe(
				UtilsItems.getSimpleStack(pipeIngot, 1),
				ItemList.Shape_Extruder_Pipe_Small.get(0),
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 1),
				10, eut);
		
		GT_Values.RA.addExtruderRecipe(
				UtilsItems.getSimpleStack(pipeIngot, 3),
				ItemList.Shape_Extruder_Pipe_Medium.get(0),
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 1),
				20, eut);
		
		GT_Values.RA.addExtruderRecipe(
				UtilsItems.getSimpleStack(pipeIngot, 6),
				ItemList.Shape_Extruder_Pipe_Large.get(0),
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1),
				2*20, eut);
		
		GT_Values.RA.addExtruderRecipe(
				UtilsItems.getSimpleStack(pipeIngot, 12),
				ItemList.Shape_Extruder_Pipe_Huge.get(0),
				UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1),
				4*20, eut);
		
		if (eut < 512){
			ItemStack pipePlateDouble = UtilsItems.getItemStackOfAmountFromOreDict("plateDouble"+output, 1).copy();
			UtilsRecipe.recipeBuilder(
					pipePlateDouble, "craftingToolHardHammer", pipePlateDouble,
					pipePlateDouble, null, pipePlateDouble,
					pipePlateDouble, "craftingToolWrench", pipePlateDouble,
					UtilsItems.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1));
		}
		
		
	}
	
	private static ItemStack getOredictStack(String oredictName, int amount){
		ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		if (!oreDictList.isEmpty()){
		ItemStack returnValue = oreDictList.get(0).copy();
		returnValue.stackSize = amount;
		return returnValue;
		}
	 return UtilsItems.getSimpleStack(ModItems.AAA_Broken, amount);
	}
}
