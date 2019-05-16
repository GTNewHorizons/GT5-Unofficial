package gtPlusPlus.xmod.gregtech;

import static gtPlusPlus.core.recipe.common.CI.bits;
import static gtPlusPlus.core.util.minecraft.MaterialUtils.getMaterialName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.australia.gen.gt.WorldGen_GT_Australia;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.OldCircuitHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.everglades.gen.gt.WorldGen_GT;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.util.GTPP_Config;
import gtPlusPlus.xmod.gregtech.api.world.GTPP_Worldgen;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.Gregtech_Blocks;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingAngelGrinder;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricButcherKnife;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricLighter;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingElectricSnips;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingToolHeadChoocher;
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
		if (ConfigSwitches.enableHarderRecipesForHighTierCasings) {
	        removeOldHighTierCasingRecipes();		    
		}		
		RecipesToRemove.go();
	}

	private static GT_Recipe replaceItemInRecipeWithAnother(GT_Recipe aRecipe, ItemStack aExisting, ItemStack aNewItem) {
	    ItemStack[] aInputItemsCopy = aRecipe.mInputs;
	    String aOutputName = ItemUtils.getItemName(aRecipe.mOutputs[0]);
	    boolean aDidChange = false;
        Logger.INFO("Attempting to Modify Recipe for "+aOutputName);
	    for (int i=0;i<aRecipe.mInputs.length;i++) {
	        ItemStack aCurrentInputSlot = aRecipe.mInputs[i];
	        if (aCurrentInputSlot != null) {
	            if (GT_Utility.areStacksEqual(aCurrentInputSlot, aExisting, true)) {
	                aInputItemsCopy[i] = ItemUtils.getSimpleStack(aNewItem, aCurrentInputSlot.stackSize);
	                aDidChange = true;
	            }
	        }
	    }
	    if (aDidChange) {
	        aRecipe.mInputs = aInputItemsCopy;
	        Logger.INFO("Modifed Recipe for "+aOutputName);
	        return aRecipe;
	    }
	    else {
            Logger.INFO("Failed to Modify Recipe for "+aOutputName);
	        return aRecipe;
	    }
	}
	
	private static void updateRecipeMap(GT_Recipe aOld, GT_Recipe aNew, GT_Recipe_Map aMap) {
	    RecipeUtils.removeGtRecipe(aOld, aMap);
	    RecipeUtils.addGtRecipe(aNew, aMap);
	    Logger.INFO("Updating recipe map: "+aMap.mNEIName);
	    Logger.INFO("Removed Recipe with hash: "+aOld.hashCode());
	    Logger.INFO("Added Recipe with hash: "+aNew.hashCode());
        
	}

	private static void removeOldHighTierCasingRecipes() {

        Logger.INFO("Trying to appropriately retier GT Machine Hulls/Casings from LuV+");
		final Object aHardCasingsTest = StaticFields59.getFieldFromGregtechProxy("mHardMachineCasings");

		boolean aHardCasings = aHardCasingsTest != null ? (boolean) aHardCasingsTest : false;
		
        Logger.INFO("Are Hard casings/hulls enabled within GT? "+(aHardCasingsTest == null ? "Version does not support config option" : aHardCasings));

		// Static objects to save memory		
		ItemStack aCasing_LUV = CI.machineCasing_LuV;
		ItemStack aCasing_ZPM = CI.machineCasing_ZPM;
		ItemStack aCasing_UV = CI.machineCasing_UV;
		ItemStack aCasing_MAX = CI.machineCasing_MAX;

		ItemStack aHull_LUV = CI.machineHull_LuV;
		ItemStack aHull_ZPM = CI.machineHull_ZPM;
		ItemStack aHull_UV = CI.machineHull_UV;
		ItemStack aHull_MAX = CI.machineHull_MAX;

        int aTier_LUV = 5;
        int aTier_ZPM = 6;
        int aTier_UV = 7;
        //int aTier_MAX = 8;
		

		ItemStack[] aCasings = new ItemStack[] {aCasing_LUV, aCasing_ZPM, aCasing_UV, aCasing_MAX};
		ItemStack[] aHulls = new ItemStack[] {aHull_LUV, aHull_ZPM, aHull_UV, aHull_MAX};

		// Remove Hand Crafting Recipes

		// Casings
        Logger.INFO("Removing shaped crafting for Casings.");
		RecipeUtils.removeRecipeByOutput(aCasing_LUV);
		RecipeUtils.removeRecipeByOutput(aCasing_ZPM);
		RecipeUtils.removeRecipeByOutput(aCasing_UV);
		//RecipeUtils.removeRecipeByOutput(aCasing_MAX);	

		// Hulls
        Logger.INFO("Removing shaped crafting for Hulls.");
		RecipeUtils.removeRecipeByOutput(aHull_LUV);
		RecipeUtils.removeRecipeByOutput(aHull_ZPM);
		RecipeUtils.removeRecipeByOutput(aHull_UV);
		//RecipeUtils.removeRecipeByOutput(aHull_MAX);		

		// Modify Assembler Recipes
        Logger.INFO("Attempting to modify existing Assembly recipes for Casings & Hulls, this should provide best compatibility.");
        int aUpdateCount = 0;
        
        AutoMap<Pair<GT_Recipe, GT_Recipe>> aDataToModify = new AutoMap<Pair<GT_Recipe, GT_Recipe>>();
        
        
		Outer :for (final GT_Recipe r : GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList) {		    
		    
		    if (r != null && r.mOutputs != null && r.mOutputs.length > 0) {

		        GT_Recipe aOldRecipeCopy = r;
		        GT_Recipe aNewRecipe = r.copy();

		        //Casings
		        Inner : for (ItemStack aCasingObject : aCasings) {
		            if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasingObject)) {
		                String aOutputName = ItemUtils.getItemName(aOldRecipeCopy.mOutputs[0]);
		                Logger.INFO("Attempting to Modify Assembly Recipe for "+aOutputName);		                
		                //Replace Chrome
		                if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_LUV)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 1), ELEMENT.getInstance().SELENIUM.getPlate(1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
		                    aUpdateCount++;
		                    continue Outer;
		                }
		                //Replace Iridium
		                else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_ZPM)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateIridium", 1), CI.getPlate(aTier_ZPM, 1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
                            aUpdateCount++;
		                    continue Outer;
		                }
		                //Replace Osmium
		                else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aCasing_UV)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateOsmium", 1), CI.getPlate(aTier_UV, 1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
                            aUpdateCount++;
		                    continue Outer;
		                }
		                //else if (aOldRecipeCopy.mOutputs[0] == aCasing_LUV) {
		                //	aOldRecipeCopy = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 8), CI.getPlate(aTier_MAX, 8));
		                //  updateRecipeMap(aOldRecipeCopy, aNewRecipe, GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);
		                //}
		                else {
		                    continue Inner;
		                }
		            }					
		        }

		        //Hulls
		        Inner : for (ItemStack aHullObject : aHulls) {
		            if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHullObject)) {
                        String aOutputName = ItemUtils.getItemName(aOldRecipeCopy.mOutputs[0]);
                        Logger.INFO("Attempting to Modify Assembly Recipe for "+aOutputName);   						
		                //Replace Chrome
		                if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_LUV)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 1), ELEMENT.getInstance().SELENIUM.getPlate(1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
                            aUpdateCount++;
		                    continue Outer;
		                }
		                //Replace Iridium
		                else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_ZPM)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateIridium", 1), CI.getPlate(aTier_ZPM, 1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
                            aUpdateCount++;
		                    continue Outer;
		                }
		                //Replace Osmium
		                else if (GT_Utility.areStacksEqual(aOldRecipeCopy.mOutputs[0], aHull_UV)) {
		                    aNewRecipe = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateOsmium", 1), CI.getPlate(aTier_UV, 1));
		                    aDataToModify.put(new Pair<GT_Recipe, GT_Recipe>(r, aNewRecipe));
                            aUpdateCount++;
		                    continue Outer;
		                }
		                //else if (aOldRecipeCopy.mOutputs[0] == aHull_LUV) {
		                //	aOldRecipeCopy = replaceItemInRecipeWithAnother(aOldRecipeCopy, ItemUtils.getItemStackOfAmountFromOreDict("plateChrome", 8), CI.getPlate(aTier_MAX, 8));
		                //  updateRecipeMap(aOldRecipeCopy, aNewRecipe, GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);
		                //}
		                else {
		                    continue Inner;
		                }
		            }					
		        }
		    }
		}

        Logger.INFO("There is "+aUpdateCount+" recipes flagged for update.");

        if (aUpdateCount > 0) {            
            for (Pair<GT_Recipe, GT_Recipe> g : aDataToModify) {
                updateRecipeMap(g.getKey(), g.getValue(), GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);                
            }    
            Logger.INFO("Modified "+aUpdateCount+" recipes.");       
        }
        


        Logger.INFO("Adding new Shaped recipes for Casings.");
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_LuV.get(1), bits, new Object[]{"PPP", "PwP", "PPP", 'P', ELEMENT.getInstance().SELENIUM.getPlate(1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_ZPM.get(1), bits, new Object[]{"PPP", "PwP", "PPP", 'P', CI.getPlate(aTier_ZPM, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_UV.get(1), bits, new Object[]{"PPP", "PwP", "PPP", 'P', CI.getPlate(aTier_UV, 1)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Casing_MAX.get(1), bits, new Object[]{"PPP", "PwP", "PPP", 'P', OrePrefixes.plate.get(Materials.Neutronium)});

		if (!aHardCasings) {
	        Logger.INFO("Adding new easy Shaped recipes for Hulls.");
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_LuV.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED, new Object[]{"CMC", 'M', ItemList.Casing_LuV, 'C',
	                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium)});
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_ZPM.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
	                new Object[]{"CMC", 'M', ItemList.Casing_ZPM, 'C', OrePrefixes.cableGt01.get(Materials.Naquadah)});
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_UV.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED, new Object[]{"CMC", 'M', ItemList.Casing_UV, 'C',
	                        OrePrefixes.wireGt04.get(Materials.NaquadahAlloy)});
	        /*GT_ModHandler.addCraftingRecipe(ItemList.Hull_MAX.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED, new Object[]{"CMC", 'M', ItemList.Casing_MAX, 'C',
	                        OrePrefixes.wireGt01.get(Materials.Superconductor)});*/
		}
		else {
	        Logger.INFO("Adding new hard Shaped recipes for Hulls.");
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_LuV.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
	                new Object[]{"PHP", "CMC", 'M', ItemList.Casing_LuV, 'C',
	                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
	                        ELEMENT.getInstance().SELENIUM.getPlate(1), 'P', OrePrefixes.plate.get(Materials.Plastic)});
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_ZPM.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
	                new Object[]{"PHP", "CMC", 'M', ItemList.Casing_ZPM, 'C',
	                        OrePrefixes.cableGt01.get(Materials.Naquadah), 'H',
	                        CI.getPlate(aTier_ZPM, 1), 'P',
	                        OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
	        GT_ModHandler.addCraftingRecipe(ItemList.Hull_UV.get(1),
	                RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
	                new Object[]{"PHP", "CMC", 'M', ItemList.Casing_UV, 'C',
	                        OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H',
	                        CI.getPlate(aTier_UV, 1), 'P',
	                        OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
            /*GT_ModHandler.addCraftingRecipe(ItemList.Hull_MAX.get(1),
                    RecipeBits.NOT_REMOVABLE | RecipeBits.BUFFERED,
                    new Object[]{"PHP", "CMC", 'M', ItemList.Casing_MAX, 'C',
                            OrePrefixes.wireGt01.get(Materials.Superconductor), 'H',
                            OrePrefixes.plate.get(Materials.Neutronium), 'P',
                            OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});*/
		}



		//Casings
		
        /*GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 8L),
        		ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]),
        		ItemList.Casing_LuV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 8L),
        		ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]),
        		ItemList.Casing_ZPM.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 8L),
        		ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]),
        		ItemList.Casing_UV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L),
        		ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]),
        		ItemList.Casing_MAX.get(1), 50, 16);	*/	



		//Hulls

		//Hard Hulls
        /*GT_Values.RA.addAssemblerRecipe(
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L),
        		ItemList.Casing_LuV.get(1), Materials.Plastic.getMolten(288L),
        		ItemList.Hull_LuV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L),
        		ItemList.Casing_ZPM.get(1), Materials.Polytetrafluoroethylene.getMolten(288L),
        		ItemList.Hull_ZPM.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L),
        		ItemList.Casing_UV.get(1), Materials.Polytetrafluoroethylene.getMolten(288L),
        		ItemList.Hull_UV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 2L),
        		ItemList.Casing_MAX.get(1), Materials.Polytetrafluoroethylene.getMolten(288L),
        		ItemList.Hull_MAX.get(1), 50, 16);*/

		//Easy Hulls
        /*GT_Values.RA.addAssemblerRecipe(
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L),
        		ItemList.Casing_LuV.get(1), ItemList.Hull_LuV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L),
        		ItemList.Casing_ZPM.get(1), ItemList.Hull_ZPM.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L),
        		ItemList.Casing_UV.get(1), ItemList.Hull_UV.get(1), 50, 16);
        GT_Values.RA.addAssemblerRecipe(
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 2L),
        		ItemList.Casing_MAX.get(1), ItemList.Hull_MAX.get(1), 50, 16);*/
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
