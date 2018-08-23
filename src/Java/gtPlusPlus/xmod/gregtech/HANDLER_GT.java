package gtPlusPlus.xmod.gregtech;

import static gtPlusPlus.core.util.minecraft.MaterialUtils.getMaterialName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.gen.gt.WorldGen_GT_Australia;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.OldCircuitHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import gtPlusPlus.xmod.gregtech.api.world.GTPP_Worldgen;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.*;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechNitroDieselFix;
import net.minecraft.item.ItemStack;

public class HANDLER_GT {

	public static GT_Config mMaterialProperties = null;
	
	public static GTPP_Config sCustomWorldgenFile = null;
	public static final List<WorldGen_GT> sWorldgenListEverglades = new ArrayList<WorldGen_GT>();
	public static final List<WorldGen_GT_Australia> sWorldgenListAustralia = new ArrayList<WorldGen_GT_Australia>();
	public static final List<GTPP_Worldgen> sCustomWorldgenList = new ArrayList<GTPP_Worldgen>();

	public static void preInit(){
		
		if (mMaterialProperties != null){
			GT_Materials.init(mMaterialProperties);
		}
		
		if (ConfigSwitches.enableOldGTcircuits && !CORE.GTNH){
			OldCircuitHandler.preInit();
		}
		
		GregtechFluidHandler.run();		
		
	}

	public static void init(){

		//Load General Blocks and set up some Basic Meta Tile Entity states
		Gregtech_Blocks.run();

		//Add Custom Pipes, Wires and Cables.
		GregtechConduits.run();
		
		//Register Tile Entities
		COMPAT_HANDLER.registerGregtechMachines();


		//Only loads if the config option is true (default: true)
		if (CORE.ConfigSwitches.enableSkookumChoochers){
			new MetaGeneratedGregtechTools();
			new ProcessingToolHeadChoocher().run();
		}

		if (ConfigSwitches.enableOldGTcircuits && !CORE.GTNH){
			OldCircuitHandler.init();
		}
		
		//Generates recipes for all gregtech smelting and alloy smelting combinations.
		//RecipeGen_BlastSmelterGT.generateRecipes();
		//new RecipeGen_BlastSmelterGT_Ex();

	}

	public static void postInit(){
		if (CORE.ConfigSwitches.enableNitroFix){
			GregtechNitroDieselFix.run();
		}

		if (ConfigSwitches.enableOldGTcircuits && !CORE.GTNH){
			OldCircuitHandler.postInit();
		}
		
		
		//Register some custom recipe maps for any enabled multiblocks.
		//MultiblockRecipeMapHandler.run();
	}
	
	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		removeCrudeTurbineRotors();
	}
	
	private static int removeCrudeTurbineRotors() {
		int aRemoved = 0;
		
		Collection<GT_Recipe> aAssRecipes = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList;
		//170, 172, 174, 176
		if (aAssRecipes.size() > 0) {
			recipe: for (GT_Recipe aG : aAssRecipes) {
				if (aG.mOutputs != null && aG.mOutputs.length > 0) {
					outputs: for (ItemStack aI : aG.mOutputs) {
						if (aI.getItem() instanceof GT_MetaGenerated_Tool_01) {
							int aMeta = aI.getItemDamage();
							Logger.INFO("Found assembler recipe outputting a GT Tool with a meta value of "+aMeta);
							if (aMeta >= 170 && aMeta <= 176) {
								//Found a Turbine
								int aCutoff = aMeta == 170 ? 75000 : (aMeta == 172 ? 150000 : (aMeta == 174 ? 225000 : 300000));
								String aType = aMeta == 170 ? "Small " : (aMeta == 172 ? "" : (aMeta == 174 ? "Large " : "Huge "));
								Materials aMainMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(aI);
								Materials aSecondaryMaterial = GT_MetaGenerated_Tool.getSecondaryMaterial(aI);	
								long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(aI);								
								Logger.INFO("Found "+aType+"Turbine made out of "+getMaterialName(aMainMaterial)+", using "+getMaterialName(aSecondaryMaterial));
								if (rotorDurabilityMax < aCutoff) {
									Logger.INFO("Disabled this recipe as "+rotorDurabilityMax+" is below the cutoff durability of "+aCutoff+" for "+aType+"Turbines.");
									aG.mEnabled = false;
									aG.mHidden = true;
									aG.mCanBeBuffered = false;
									aRemoved++;
								}
								else {
									break outputs;
								}
								
							}
							else {
								continue outputs;
							}
						}
						else {
							continue outputs;
						}
					}
				}
				else {
					continue recipe;
				}
			}
		}
		
		Logger.INFO("Removed "+aRemoved+" useless Turbines.");
		
		return aRemoved;
	}

}
