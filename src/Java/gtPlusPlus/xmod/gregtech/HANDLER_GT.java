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
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.australia.gen.gt.WorldGen_GT_Australia;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.OldCircuitHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import gtPlusPlus.xmod.gregtech.api.world.GTPP_Worldgen;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.*;
import gtPlusPlus.xmod.gregtech.recipes.RecipesToRemove;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechNitroDieselFix;
import net.minecraft.item.Item;
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
		}

		if (ConfigSwitches.enableOldGTcircuits && !CORE.GTNH){
			OldCircuitHandler.init();
		}
		
		//Generates recipes for all gregtech smelting and alloy smelting combinations.
		//RecipeGen_BlastSmelterGT.generateRecipes();
		//new RecipeGen_BlastSmelterGT_Ex();

	}

	public static void postInit(){
		
		//Only loads if the config option is true (default: true)
		if (CORE.ConfigSwitches.enableSkookumChoochers){
			new ProcessingToolHeadChoocher().run();
		}
		new ProcessingAngelGrinder().run();
		new ProcessingElectricSnips().run();
		new ProcessingElectricButcherKnife().run();
		new ProcessingElectricLighter().run();
		
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
		cleanAssemblyLineRecipeMap();
		RecipesToRemove.go();
	}
	
	private static int removeCrudeTurbineRotors() {
		int aRemoved = 0;
		int CUT = CORE.turbineCutoffBase;
		Item aU;
		Collection<GT_Recipe> aAssRecipes = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList;
		//170, 172, 174, 176
		if (aAssRecipes.size() > 0 && (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH)) {
			recipe: for (GT_Recipe aG : aAssRecipes) {
				if (aG.mOutputs != null && aG.mOutputs.length > 0) {
					outputs: for (ItemStack aI : aG.mOutputs) {
						if (aI == null) {
							continue;
						}
						aU = aI.getItem();
						if (aU == null) {
							continue;
						}						
						if (aU instanceof GT_MetaGenerated_Tool_01) {
							int aMeta = aI.getItemDamage();
							//Logger.INFO("Found assembler recipe outputting a GT Tool with a meta value of "+aMeta);
							if (aMeta >= 170 && aMeta <= 176) {
								//Found a Turbine
								int aCutoff = aMeta == 170 ? CUT : (aMeta == 172 ? CUT*2 : (aMeta == 174 ? CUT*3 : CUT*4));
								String aType = aMeta == 170 ? "Small " : (aMeta == 172 ? "" : (aMeta == 174 ? "Large " : "Huge "));
								Materials aMainMaterial = GT_MetaGenerated_Tool.getPrimaryMaterial(aI);
								Materials aSecondaryMaterial = GT_MetaGenerated_Tool.getSecondaryMaterial(aI);	
								long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(aI);								
								//Logger.INFO("Found "+aType+"Turbine made out of "+getMaterialName(aMainMaterial)+", using "+getMaterialName(aSecondaryMaterial));
								if (rotorDurabilityMax < aCutoff) {
									Logger.WARNING("[Turbine Cleanup] "+getMaterialName(aMainMaterial)+" "+aType+"Turbines have "+rotorDurabilityMax+", which is below the cutoff durability of "+aCutoff+", disabling.");
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
	
	/**
	 * Should clean out any invalid Assembly Line recipes, if the map actually exists.
	 * Prevents NPE's being thrown by GT's AL handler. (Fucking Annoying)
	 * @return - Amount of Recipes removed, which were invalid in some way.
	 */
	private static int cleanAssemblyLineRecipeMap() {		
		GT_Recipe_Map g = StaticFields59.sAssemblylineVisualRecipes;
		if (g == null) {
			return 0;
		}
		else {
			AutoMap<GT_Recipe> aNewMap = new AutoMap<GT_Recipe>();
			AutoMap<GT_Recipe> aBadRecipeTempMap = new AutoMap<GT_Recipe>();
			for (GT_Recipe r : g.mRecipeList) {
				if (r != null) {
					if (r.mOutputs == null || r.mOutputs.length == 0 || r.mOutputs[0] == null) {
						aBadRecipeTempMap.put(r.copy());
						continue;
					}
					else {
						aNewMap.put(r.copy());
					}					
				}
			}			
			if (aNewMap.size() > 0) {
				g.mRecipeList.clear();
				for (GT_Recipe i : aNewMap) {
					g.add(i);
				}
			}
			if (aBadRecipeTempMap.size() > 0) {
				Logger.INFO("Found "+aBadRecipeTempMap.size()+" bad Assembly Line Recipes, attempting to dump all data about them.");
				Logger.INFO("This data should be given to the mod author for the recipe in question.");
				for (GT_Recipe i : aBadRecipeTempMap) {
					if (i == null) {
						Logger.INFO("Found NULL recipe. Impossible to determine who added this one. Please Report to Alkalus on Github.");
					}
					else {
						if (i.mOutputs == null || i.mOutputs.length == 0 || i.mOutputs[0] == null) {
							Logger.INFO("Found recipe with NULL output array, this will cause some issues. Attempting to determine other info about recipe.");
							if (i.mInputs != null && i.mInputs.length > 0) {
								Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(i.mInputs));
							}
							else {
								Logger.INFO("Recipe had no valid inputs.");								
							}
							Logger.INFO("Time: "+i.mDuration);
							Logger.INFO("EU/T: "+i.mEUt);
							Logger.INFO("Special: "+i.mSpecialValue);							
						}
						else {
							Logger.INFO("Found bad recipe, Attempting to determine other info.");
							if (i.mInputs != null && i.mInputs.length > 0) {
								Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(i.mInputs));
							}
							else {
								Logger.INFO("Recipe had no valid inputs.");								
							}
							if (i.mOutputs != null && i.mOutputs.length > 0) {
								Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(i.mOutputs));
							}
							else {
								Logger.INFO("Recipe had no valid outputs.");								
							}
							Logger.INFO("Time: "+i.mDuration);
							Logger.INFO("EU/T: "+i.mEUt);
							Logger.INFO("Special: "+i.mSpecialValue);
						}
					}
				}				
			}
			else {
				Logger.INFO("No bad Assembly Line recipes found, this is great news!");				
			}
			return aBadRecipeTempMap.size();			
		}		
	}

}
