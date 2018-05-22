package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.core.lib.CORE.GTNH;
import static gtPlusPlus.core.lib.LoadedMods.Gregtech;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntityFluid;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaPipeEntity_Cable;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechConduits {
	/**
	 *
	 * The Voltage Tiers. Use this Array instead of the old named Voltage Variables
	 * public static final long[] V = new long[] {0=8, 1=32, 2=128, 3=512, 4=2048, 5=8192, 6=32768, 7=131072, 8=524288, 9=Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
	 *
	 */

	private static OrePrefixes cableGt16;
	static{
		if(GTNH) {
			try {
				cableGt16=(OrePrefixes) GT_Utility.getField(OrePrefixes.class,"cableGt16").get(null);
			}catch (IllegalAccessException | NullPointerException e){
				e.printStackTrace();
			}
		}
	}

	private static int BaseWireID = 30600;
	private static int BasePipeID = 30700;


	public static void run()
	{
		if (Gregtech){
			Logger.INFO("Gregtech5u Content | Registering Custom Cables/Wires/Pipes.");
			if (CORE.ConfigSwitches.enableCustom_Cables) {
				run1();
			}
			if (CORE.ConfigSwitches.enableCustom_Pipes) {
				run2();
			}
		}

	}

	private static void run1(){

		if (LoadedMods.Big_Reactors){
			wireFactory("Blutonium", 8196, BaseWireID, 8, 32, 2, new short[]{28, 28, 218, 0});
			wireFactory("Cyanite", 512, BaseWireID+15, 2, 16, 4, new short[]{27, 130, 178, 0});
			wireFactory("Yellorium", 2048, BaseWireID+30, 4, 16, 2, new short[]{150, 195, 54, 0});
		}

		if (LoadedMods.EnderIO){
			wireFactory("RedstoneAlloy", 32, BaseWireID+45, 0, 2, 1, new short[]{178,34,34, 0});
		}

		if(!GTNH) {
			customWireFactory(ALLOY.LEAGRISIUM, 512, BaseWireID + 56, 1, 2, 2);
			customWireFactory(ELEMENT.getInstance().ZIRCONIUM, 128, BaseWireID + 67, 1, 2, 2);
			customWireFactory(ALLOY.HG1223, 32768, BaseWireID + 78, 2, 8, 4);
			customWireFactory(ALLOY.TRINIUM_TITANIUM, 2048, BaseWireID + 89, 1, 2, 16);
		}


		//superConductorFactory(GT_Materials.Superconductor, 524288, 30660, 0, 0, 8);
		if (LoadedMods.Thaumcraft){
			//superConductorFactory(GT_Materials.Void, 512, 30661, 0, 0, 8);
		}
	}

	private static void run2(){
		generateNonGTFluidPipes(GT_Materials.Staballoy, ALLOY.STABALLOY, BasePipeID, 6250, 7500, true);
		generateNonGTFluidPipes(GT_Materials.Tantalloy60, ALLOY.TANTALLOY_60, BasePipeID+5, 5000, 4250, true);
		generateNonGTFluidPipes(GT_Materials.Tantalloy61, ALLOY.TANTALLOY_61, BasePipeID+10, 6000, 5800, true);
		if (LoadedMods.Thaumcraft){
			generateNonGTFluidPipes(GT_Materials.Void, null, BasePipeID+15, 800, 25000, true);
		}
		generateGTFluidPipes(Materials.Europium, BasePipeID+20, 12000, 7500, true);
		generateNonGTFluidPipes(GT_Materials.Potin, ALLOY.POTIN, BasePipeID+25, 480, 2000, true);
		generateNonGTFluidPipes(GT_Materials.MaragingSteel300, ALLOY.MARAGING300, BasePipeID+30, 7000, 2500, true);
		generateNonGTFluidPipes(GT_Materials.MaragingSteel350, ALLOY.MARAGING350, BasePipeID+35, 8000, 2500, true);
		generateNonGTFluidPipes(GT_Materials.Inconel690, ALLOY.INCONEL_690, BasePipeID+40, 7500, 4800, true);
		generateNonGTFluidPipes(GT_Materials.Inconel792, ALLOY.INCONEL_792, BasePipeID+45, 8000, 5500, true);
		generateNonGTFluidPipes(GT_Materials.HastelloyX, ALLOY.HASTELLOY_X, BasePipeID+50, 10000, 4200, true);

		generateGTFluidPipes(Materials.Tungsten, BasePipeID+55, 4320, 7200, true);
		if (LoadedMods.EnderIO){
			generateGTFluidPipes(Materials.DarkSteel, BasePipeID+60, 2320, 2750, true);
		}
		generateGTFluidPipes(Materials.Clay, BasePipeID+65, 100, 500, false);
		generateGTFluidPipes(Materials.Lead, BasePipeID+70, 720, 1200, true);

	}

	private static void wireFactory(final String Material, final int Voltage, final int ID, final long insulatedLoss, final long uninsulatedLoss, final long Amps, final short[] rgb){
		@SuppressWarnings("deprecation")
		final Materials T = Materials.valueOf(Material);
		int V = GT_Utility.getTier(Voltage);
		if (V == -1){
			Logger.ERROR("Failed to set voltage on "+Material+". Invalid voltage of "+Voltage+"V set.");
			Logger.ERROR(Material+" has defaulted to 8v.");
			V=0;
		}
		makeWires(T, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false, rgb);
	}

	@SuppressWarnings("deprecation")
	private static void makeWires(final Materials aMaterial, final int aStartID, final long aLossInsulated, final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable, final boolean aAutoInsulated, final short[] aRGB)
	{
		Logger.WARNING("Gregtech5u Content | Registered "+aMaterial.name() +" as a new material for Wire & Cable.");
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 0, "wire." + aMaterial.name().toLowerCase() + ".01", "1x " + aMaterial.mDefaultLocalName + " Wire", 0.125F, aMaterial, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 1, "wire." + aMaterial.name().toLowerCase() + ".02", "2x " + aMaterial.mDefaultLocalName + " Wire", 0.25F, aMaterial, aLoss, 2L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 2, "wire." + aMaterial.name().toLowerCase() + ".04", "4x " + aMaterial.mDefaultLocalName + " Wire", 0.375F, aMaterial, aLoss, 4L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 3, "wire." + aMaterial.name().toLowerCase() + ".08", "8x " + aMaterial.mDefaultLocalName + " Wire", 0.50F, aMaterial, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 4, "wire." + aMaterial.name().toLowerCase() + ".12", "12x " + aMaterial.mDefaultLocalName + " Wire", GTNH?0.625F:0.75F, aMaterial, aLoss, 12L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.wireGt16, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 5, "wire." + aMaterial.name().toLowerCase() + ".16", "16x " + aMaterial.mDefaultLocalName + " Wire", GTNH?0.75F:1.0F, aMaterial, aLoss, 16L * aAmperage, aVoltage, false, !aAutoInsulated, aRGB).getStackForm(1L));
		if (aInsulatable)
		{
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 6, "cable." + aMaterial.name().toLowerCase() + ".01", "1x " + aMaterial.mDefaultLocalName + " Cable", 0.25F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 7, "cable." + aMaterial.name().toLowerCase() + ".02", "2x " + aMaterial.mDefaultLocalName + " Cable", 0.375F, aMaterial, aLossInsulated, 2L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 8, "cable." + aMaterial.name().toLowerCase() + ".04", "4x " + aMaterial.mDefaultLocalName + " Cable", 0.5F, aMaterial, aLossInsulated, 4L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 9, "cable." + aMaterial.name().toLowerCase() + ".08", "8x " + aMaterial.mDefaultLocalName + " Cable", 0.625F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			GT_OreDictUnificator.registerOre(OrePrefixes.cableGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 10, "cable." + aMaterial.name().toLowerCase() + ".12", "12x " + aMaterial.mDefaultLocalName + " Cable", GTNH?0.75F:0.875F, aMaterial, aLossInsulated, 12L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			if(GTNH){
				GT_OreDictUnificator.registerOre(cableGt16, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 11, "cable." + aMaterial.name().toLowerCase() + ".16", "16x " + aMaterial.mDefaultLocalName + " Cable", 0.875f, aMaterial, aLossInsulated, 16L * aAmperage, aVoltage, true, false, aRGB).getStackForm(1L));
			}
		}
	}

	private static void customWireFactory(final Material Material, final int Voltage, final int ID, final long insulatedLoss, final long uninsulatedLoss, final long Amps){
		int V = GT_Utility.getTier(Voltage);
		if (V == -1){
			Logger.ERROR("Failed to set voltage on "+Material+". Invalid voltage of "+Voltage+"V set.");
			Logger.ERROR(Material+" has defaulted to 8v.");
			V=0;
		}
		makeCustomWires(Material, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false);
	}

	private static void makeCustomWires(final Material aMaterial, final int aStartID, final long aLossInsulated, final long aLoss, final long aAmperage, final long aVoltage, final boolean aInsulatable, final boolean aAutoInsulated)
	{
		Logger.WARNING("Gregtech5u Content | Registered "+aMaterial.getLocalizedName() +" as a new material for Wire & Cable.");
		registerOre(OrePrefixes.wireGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 0, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".01", "1x " + aMaterial.getLocalizedName() + " Wire", 0.125F, aLoss, 1L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		registerOre(OrePrefixes.wireGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 1, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".02", "2x " + aMaterial.getLocalizedName() + " Wire", 0.25F, aLoss, 2L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		registerOre(OrePrefixes.wireGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 2, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".04", "4x " + aMaterial.getLocalizedName() + " Wire", 0.375F, aLoss, 4L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		registerOre(OrePrefixes.wireGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 3, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".08", "8x " + aMaterial.getLocalizedName() + " Wire", 0.50F, aLoss, 8L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		registerOre(OrePrefixes.wireGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 4, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".12", "12x " + aMaterial.getLocalizedName() + " Wire", GTNH?0.625F:0.75F, aLoss, 12L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		registerOre(OrePrefixes.wireGt16, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 5, "wire." + aMaterial.getLocalizedName().toLowerCase() + ".16", "16x " + aMaterial.getLocalizedName() + " Wire", GTNH?0.75F:1.0F, aLoss, 16L * aAmperage, aVoltage, false, !aAutoInsulated, aMaterial.getRGBA()).getStackForm(1L));
		if (aInsulatable)
		{
			registerOre(OrePrefixes.cableGt01, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 6, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".01", "1x " + aMaterial.getLocalizedName() + " Cable", 0.25F, aLossInsulated, 1L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			registerOre(OrePrefixes.cableGt02, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 7, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".02", "2x " + aMaterial.getLocalizedName() + " Cable", 0.375F, aLossInsulated, 2L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			registerOre(OrePrefixes.cableGt04, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 8, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".04", "4x " + aMaterial.getLocalizedName() + " Cable", 0.5F, aLossInsulated, 4L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			registerOre(OrePrefixes.cableGt08, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 9, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".08", "8x " + aMaterial.getLocalizedName() + " Cable", 0.625F, aLossInsulated, 8L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			registerOre(OrePrefixes.cableGt12, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 10, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".12", "12x " + aMaterial.getLocalizedName() + " Cable", GTNH?0.75F:0.875F, aLossInsulated, 12L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			if(GTNH){
				registerOre(cableGt16, aMaterial, new GregtechMetaPipeEntity_Cable(aStartID + 11, "cable." + aMaterial.getLocalizedName().toLowerCase() + ".16", "16x " + aMaterial.getLocalizedName() + " Cable", 0.875f, aLossInsulated, 16L * aAmperage, aVoltage, true, false, aMaterial.getRGBA()).getStackForm(1L));
			}
		}

		generateWireRecipes(aMaterial);

	}

	private static void superConductorFactory(final GT_Materials Material, final int Voltage, final int ID, final long insulatedLoss, final long uninsulatedLoss, final long Amps){
		final GT_Materials T = Material;
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
			Logger.ERROR("Failed to set voltage on "+Material.name()+". Invalid voltage of "+Voltage+"V set.");
			Logger.ERROR(Material.name()+" has defaulted to 8v.");
			V = 0;
		}
		//makeWires(T, ID, 2L, 4L, 2L, GT_Values.V[V], true, false);
		//makeSuperConductors(T, ID, insulatedLoss, uninsulatedLoss, Amps, GT_Values.V[V], true, false);
		//makeWires(T, ID, bEC ? 2L : 2L, bEC ? 4L : 4L, 2L, gregtech.api.enums.GT_Values.V[V], true, false);
	}


	private static void generateGTFluidPipes(final Materials material, final int startID, final int transferRatePerSec, final int heatResistance, final boolean isGasProof){
		final int transferRatePerTick = transferRatePerSec/20;
		final long mass = material.getMass();
		final long voltage = material.mMeltingPoint >= 2800 ? 64 : 16;
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(material), new GT_MetaPipeEntity_Fluid(startID, "GT_Pipe_"+material.mDefaultLocalName+"_Tiny", "Tiny "+material.mDefaultLocalName+" Fluid Pipe", 0.25F, material, transferRatePerTick*2, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(material), new GT_MetaPipeEntity_Fluid(startID+1, "GT_Pipe_"+material.mDefaultLocalName+"_Small", "Small "+material.mDefaultLocalName+" Fluid Pipe", 0.375F, material, transferRatePerTick*4, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(material), new GT_MetaPipeEntity_Fluid(startID+2, "GT_Pipe_"+material.mDefaultLocalName+"", ""+material.mDefaultLocalName+" Fluid Pipe", 0.5F, material, transferRatePerTick*6, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(material), new GT_MetaPipeEntity_Fluid(startID+3, "GT_Pipe_"+material.mDefaultLocalName+"_Large", "Large "+material.mDefaultLocalName+" Fluid Pipe", 0.75F, material, transferRatePerTick*8, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(material), new GT_MetaPipeEntity_Fluid(startID+4, "GT_Pipe_"+material.mDefaultLocalName+"_Huge", "Huge "+material.mDefaultLocalName+" Fluid Pipe", GTNH?0.875F:1.0F, material, transferRatePerTick*10, heatResistance, isGasProof).getStackForm(1L));
		generatePipeRecipes(material.mDefaultLocalName, mass, voltage);
	}

	private static void generateNonGTFluidPipes(final GT_Materials material, final Material myMaterial, final int startID, final int transferRatePerSec, final int heatResistance, final boolean isGasProof){
		final int transferRatePerTick = transferRatePerSec/10;
		long mass;
		if (myMaterial != null){
			mass = myMaterial.getMass();
		}
		else {
			mass = ELEMENT.getInstance().IRON.getMass();
		}

		int tVoltageMultiplier = (material.mBlastFurnaceTemp >= 2800) ? 64 : 16;

		GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(material), new GregtechMetaPipeEntityFluid(startID, "GT_Pipe_"+material.mDefaultLocalName+"_Tiny", "Tiny "+material.mDefaultLocalName+" Fluid Pipe", 0.25F, material, transferRatePerTick*2, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(material), new GregtechMetaPipeEntityFluid(startID+1, "GT_Pipe_"+material.mDefaultLocalName+"_Small", "Small "+material.mDefaultLocalName+" Fluid Pipe", 0.375F, material, transferRatePerTick*4, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(material), new GregtechMetaPipeEntityFluid(startID+2, "GT_Pipe_"+material.mDefaultLocalName+"", ""+material.mDefaultLocalName+" Fluid Pipe", 0.5F, material, transferRatePerTick*6, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(material), new GregtechMetaPipeEntityFluid(startID+3, "GT_Pipe_"+material.mDefaultLocalName+"_Large", "Large "+material.mDefaultLocalName+" Fluid Pipe", 0.75F, material, transferRatePerTick*8, heatResistance, isGasProof).getStackForm(1L));
		GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(material), new GregtechMetaPipeEntityFluid(startID+4, "GT_Pipe_"+material.mDefaultLocalName+"_Huge", "Huge "+material.mDefaultLocalName+" Fluid Pipe", GTNH?0.875F:1.0F, material, transferRatePerTick*10, heatResistance, isGasProof).getStackForm(1L));
		generatePipeRecipes(material.mDefaultLocalName, mass, tVoltageMultiplier);

	}

	private static void generatePipeRecipes(final String materialName, final long Mass, final long vMulti){

		String output = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
		output = output.replace("-", "").replace("_", "").replace(" ", "");

		if (output.equals("VoidMetal")){
			output = "Void";
		}

		Logger.INFO("Generating "+output+" pipes & respective recipes.");

		ItemStack pipeIngot = ItemUtils.getItemStackOfAmountFromOreDict("ingot"+output, 1);
		ItemStack pipePlate = ItemUtils.getItemStackOfAmountFromOreDict("plate"+output, 1);

		if (pipeIngot == null){
			if (pipePlate != null){
				pipeIngot = pipePlate;
			}
		}

		//Check all pipes are not null
		Logger.WARNING("Generated pipeTiny from "+ materialName +"? "+ ((ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Tiny"+output, 1) != null) ? true : false));
		Logger.WARNING("Generated pipeSmall from "+ materialName +"? "+ ((ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 1) != null) ? true : false));
		Logger.WARNING("Generated pipeNormal from "+ materialName +"? "+ ((ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 1) != null) ? true : false));
		Logger.WARNING("Generated pipeLarge from "+ materialName +"? "+ ((ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1) != null) ? true : false));
		Logger.WARNING("Generated pipeHuge from "+ materialName +"? "+ ((ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1) != null) ? true : false));

		int eut = 120;
		eut = (int) (8 * vMulti);


		//Add the Three Shaped Recipes First
		RecipeUtils.recipeBuilder(
				pipePlate, "craftingToolWrench", pipePlate,
				pipePlate, null, pipePlate,
				pipePlate, "craftingToolHardHammer", pipePlate,
				ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 6));

		RecipeUtils.recipeBuilder(
				pipePlate, pipePlate, pipePlate,
				"craftingToolWrench", null, "craftingToolHardHammer",
				pipePlate, pipePlate, pipePlate,
				ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 2));

		RecipeUtils.recipeBuilder(
				pipePlate, "craftingToolHardHammer", pipePlate,
				pipePlate, null, pipePlate,
				pipePlate, "craftingToolWrench", pipePlate,
				ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1));

		if (pipeIngot != null && ItemUtils.checkForInvalidItems(pipeIngot)) {
			GT_Values.RA.addExtruderRecipe(
					ItemUtils.getSimpleStack(pipeIngot, 1),
					ItemList.Shape_Extruder_Pipe_Tiny.get(0),
					ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe"+"Tiny"+output, 2),
					5, eut);

			GT_Values.RA.addExtruderRecipe(
					ItemUtils.getSimpleStack(pipeIngot, 1),
					ItemList.Shape_Extruder_Pipe_Small.get(0),
					ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Small"+output, 1),
					10, eut);

			GT_Values.RA.addExtruderRecipe(
					ItemUtils.getSimpleStack(pipeIngot, 3),
					ItemList.Shape_Extruder_Pipe_Medium.get(0),
					ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Medium"+output, 1),
					1*20, eut);

			GT_Values.RA.addExtruderRecipe(
					ItemUtils.getSimpleStack(pipeIngot, 6),
					ItemList.Shape_Extruder_Pipe_Large.get(0),
					ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Large"+output, 1),
					2*20, eut);

			GT_Values.RA.addExtruderRecipe(
					ItemUtils.getSimpleStack(pipeIngot, 12),
					ItemList.Shape_Extruder_Pipe_Huge.get(0),
					ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1),
					4*20, eut);

		}

		if ((eut < 512) && !output.equals("Void")){
			try {
				final ItemStack pipePlateDouble = ItemUtils.getItemStackOfAmountFromOreDict("plateDouble"+output, 1).copy();
				if (pipePlateDouble != null) {
					RecipeUtils.recipeBuilder(
							pipePlateDouble, "craftingToolHardHammer", pipePlateDouble,
							pipePlateDouble, null, pipePlateDouble,
							pipePlateDouble, "craftingToolWrench", pipePlateDouble,
							ItemUtils.getItemStackOfAmountFromOreDict("pipe"+"Huge"+output, 1));
				} else {
					Logger.INFO("Failed to add a recipe for "+materialName+" Huge pipes. Double plates probably do not exist.");
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}


	}

	public static boolean registerOre(OrePrefixes aPrefix, Material aMaterial, ItemStack aStack) {
		return registerOre(aPrefix.get(Utils.sanitizeString(aMaterial.getLocalizedName())), aStack);
	}

	public static boolean registerOre(Object aName, ItemStack aStack) {
		if ((aName == null) || (GT_Utility.isStackInvalid(aStack)))
			return false;
		String tName = aName.toString();
		if (GT_Utility.isStringInvalid(tName))
			return false;
		ArrayList<ItemStack> tList = GT_OreDictUnificator.getOres(tName);
		for (int i = 0; i < tList.size(); ++i)
			if (GT_Utility.areStacksEqual((ItemStack) tList.get(i), aStack, true))
				return false;
		OreDictionary.registerOre(tName, GT_Utility.copyAmount(1L, new Object[] { aStack }));
		return true;
	}

	public static boolean generateWireRecipes(Material aMaterial){

		//Adds manual crafting recipe
		RecipeUtils.recipeBuilder(
				Utils.sanitizeString("plate"+aMaterial.getLocalizedName()), CI.craftingToolWireCutter, null,
				null, null, null,
				null, null, null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1));

		//Wire mill
		GT_Values.RA.addWiremillRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("ingot"+aMaterial.getLocalizedName()), 1),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				5*20,
				4);

		//Extruder
		GT_Values.RA.addExtruderRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("ingot"+aMaterial.getLocalizedName()), 1),
				ItemList.Shape_Extruder_Wire.get(0),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				196,
				96);

		GT_Values.RA.addUnboxingRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt01"+aMaterial.getLocalizedName()), 1),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
				null,
				100,
				8);

		//Shapeless Down-Crafting
		//2x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1)},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2)
				);
		//4x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1)},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 4)
				);
		//8x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1)},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 8)
				);
		//12x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt12"+aMaterial.getLocalizedName()), 1)},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 12)
				);
		//16x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt16"+aMaterial.getLocalizedName()), 1)},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 16)
				);


		//1x -> 2x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1)
				);

		//2x -> 4x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1)
				);

		//4x -> 8x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1)
				);

		//8x -> 12x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt12"+aMaterial.getLocalizedName()), 1)
				);

		//12x -> 16x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt12"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt16"+aMaterial.getLocalizedName()), 1)
				);

		//8x -> 16x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt16"+aMaterial.getLocalizedName()), 1)
				);

		//1x -> 4x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1)
				);

		//1x -> 8x
		RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
						ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1)
				},
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1)
				);


		//Wire to Cable
		//1x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 1),
				CI.getNumberedCircuit(24),
				FluidUtils.getFluidStack("molten.rubber", 144),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt01"+aMaterial.getLocalizedName()), 1),
				100,
				8);
		//2x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1),
				CI.getNumberedCircuit(24),
				FluidUtils.getFluidStack("molten.rubber", 144),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt02"+aMaterial.getLocalizedName()), 1),
				100,
				8);
		//4x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1),
				CI.getNumberedCircuit(24),
				FluidUtils.getFluidStack("molten.rubber", 288),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt04"+aMaterial.getLocalizedName()), 1),
				100,
				8);
		//8x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1),
				CI.getNumberedCircuit(24),
				FluidUtils.getFluidStack("molten.rubber", 432),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt08"+aMaterial.getLocalizedName()), 1),
				100,
				8);
		//12x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt12"+aMaterial.getLocalizedName()), 1),
				CI.getNumberedCircuit(24),
				FluidUtils.getFluidStack("molten.rubber", 576),
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt12"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		if(GTNH){
			//16x
			GT_Values.RA.addAssemblerRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt16"+aMaterial.getLocalizedName()), 1),
					CI.getNumberedCircuit(24),
					FluidUtils.getFluidStack("molten.rubber", 720),
					ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("cableGt16"+aMaterial.getLocalizedName()), 1),
					100,
					8);
		}

		//Assemble small wires into bigger wires

		//2x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				CI.getNumberedCircuit(2),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt02"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		//4x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				CI.getNumberedCircuit(4),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt04"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		//8x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				CI.getNumberedCircuit(8),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt08"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		//12x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				CI.getNumberedCircuit(12),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt12"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		//16x
		GT_Values.RA.addAssemblerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt01"+aMaterial.getLocalizedName()), 2),
				CI.getNumberedCircuit(16),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict(Utils.sanitizeString("wireGt16"+aMaterial.getLocalizedName()), 1),
				100,
				8);

		return true;
	}
}