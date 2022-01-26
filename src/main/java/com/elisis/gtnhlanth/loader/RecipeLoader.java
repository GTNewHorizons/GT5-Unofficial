package com.elisis.gtnhlanth.loader;

import static goodgenerator.items.MyMaterial.naquadahEarth;

import java.util.HashSet;

import com.elisis.gtnhlanth.Tags;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.crossmod.LoadedList;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GT_Bees;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_MultisUsingFluidInsteadOfCells;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeLoader {

    private static final Materials[] BLACKLIST = null;

	public static void loadGeneral() {
        
        /* ZIRCONIUM */
        //ZrCl4
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1), 
                WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 1),
                Materials.HydrochloricAcid.getFluid(4000), 
                Materials.Water.getFluid(2000), 
                WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 1), 
                300
            );
        
        //ZrCl4-H2O
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 1),
                Materials.Water.getFluid(1000),
                WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000),
                null,
                200
            );
        
        
        //Zr
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(2), 
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2), 
                WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000), 
                null, //No fluid output
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.ingotHot, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 2), 
                600, 1920, 4500
            );
        
            
        /* HAFNIUM */
        //HfCl4
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1), 
                WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 1),
                Materials.HydrochloricAcid.getFluid(4000), 
                Materials.Water.getFluid(2000), 
                WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 1), 
                300
            );
        
        //HfCl4-H2O
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 1),
                Materials.Water.getFluid(1000),
                WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000),
                null,
                200
            );
        
        //LP-Hf
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(2), 
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000), 
                null, //No fluid output
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 2), 
                600, 1920, 2700
            );
        
        //HfI4
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1), 
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.Iodine.getFluidOrGas(4000), 
                null,
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 1), 
                300
            );
        
        //Hf
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(12), 
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 1),
                null, 
                WerkstoffMaterialPool.Iodine.getFluidOrGas(4000),
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dustTiny, 1), 
                600, 1920, 3400
            );
        
        
        //Zirconia-Hafnia
        GT_Values.RA.addCentrifugeRecipe(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 1), 
                null, 
                null, 
                null,
                WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 1), 
                WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 1),
                null, 
                null, 
                null, 
                null, 
                new int[] {10000, 10000}, 
                600, 
                1920
            );
        
        //Ammonium Nitrate
        GT_Values.RA.addChemicalRecipe(
        		GT_Utility.getIntegratedCircuit(12), 
        		Materials.NitricAcid.getCells(1), 
        		Materials.Ammonium.getFluid(1000),
        		null, 
        		null, 
        		400
        	);
        
        
        
    }
    
    public static void loadLanthanideRecipes() {
        
    	removeCeriumSources();
    	
    	// Methanol
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                	MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 0)
                }, 
                new FluidStack[] {
                    Materials.Methanol.getFluid(1000),
                    Materials.CarbonMonoxide.getFluid(2000),
                    Materials.Oxygen.getFluid(3000)
                }, 
                new FluidStack[] {
                	MyMaterial.oxalate.getFluidOrGas(1000)
                	
                },
                null, 
                450, 
                600
            );
            
        // Ethanol
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                    MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 0)
                }, 
                new FluidStack[] {
                    Materials.Ethanol.getFluid(1000),
                    Materials.CarbonMonoxide.getFluid(3000),
                    Materials.Oxygen.getFluid(3000)
                }, 
                new FluidStack[] {
                	MyMaterial.oxalate.getFluidOrGas(1000)
                },
                null, 
                450, 
                600
            );
        
        // CeCl3
        GT_Values.RA.addChemicalRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1),
        		WerkstoffLoader.AmmoniumChloride.getFluidOrGas(3000),
        		Materials.Ammonia.getFluid(3000),
        		WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 1),
        		300,
        		450
        	);
        
        //GT_Values.RA.addChemicalRecipe(
        	//	GT_Utility.getIntegratedCircuit(2),
        		//WerkstoffMaterialPool.CeriumDioxide
        		//
        		//)
        
        // Cerium Oxalate
        GT_Values.RA.addChemicalRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 2),
        		MyMaterial.oxalate.getFluidOrGas(3000),
        		Materials.HydrochloricAcid.getFluid(6000),
        		WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 1),
        		300,
        		450	
        	);
        
        		
        // Cerium III Oxide
        GT_Values.RA.addBlastRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 1),
        		null,
        		Materials.Water.getFluid(2000),
        		WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 1),
        		null,
        		600,
        		1920,
        		1500
        	);
        
        // Cerium
        GT_Values.RA.addElectrolyzerRecipe(
        		WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5),
        		null,
        		null,
        		Materials.Oxygen.getFluid(3000),
        		GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 2),
        		null,
        		null,
        		null,
        		null,
        		null,
        		new int[]{10000},
        		150,
        		120
        	);
        
        
        //CHAIN BEGIN
        
        RecipeAdder.instance.DigesterRecipes.addDigesterRecipe(
        		new FluidStack[] {Materials.NitricAcid.getFluid(350)}, 
        		new ItemStack[] {GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Monazite, 1)},
        		WerkstoffMaterialPool.MuddyRareEarthSolution.getFluidOrGas(200), 
        		new ItemStack[] {
        			Materials.SiliconDioxide.getDustSmall(2)
        		},
        		1920, 
        		200, 
        		2700
        	);
        
        RecipeAdder.instance.DissolutionTankRecipes.addDissolutionTankRecipe(
        		new FluidStack[] {
        				Materials.Water.getFluid(10000), 
        				WerkstoffMaterialPool.MuddyRareEarthSolution.getFluidOrGas(1000)
        		},
        		null,
        		WerkstoffMaterialPool.DilutedRareEarthMud.getFluidOrGas(11000), 
        		new ItemStack[] {
            			WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dustTiny, 4),
            			WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 1),
            			Materials.Monazite.getDustTiny(2)
            	}, 
        		480, 
        		900, 
        		0
        	);
        
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(
        		false, 
        		null, 
        		new ItemStack[] {
        				WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1),
        				Materials.SiliconDioxide.getDust(1),
        				Materials.Rutile.getDust(1),
        				WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
        				Materials.Ilmenite.getDust(1)
        		}, 
        		null, 
        		new int[] {
        				8000, 7500, 1000, 500, 2000
        		}, 
        		new FluidStack[] {
        				WerkstoffMaterialPool.DilutedRareEarthMud.getFluidOrGas(1000)
        		},
        		null, 
        		400, 
        		240, 
        		0
        	);
        
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1), 
        		null, null, null, null, null, 
        		Materials.Water.getFluid(6000), 
        		WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(7000), 
        		null, 
        		480, 
        		400
        	);
        
        GT_Values.RA.addMultiblockChemicalRecipe(
        		new ItemStack[] {
        				GT_Utility.getIntegratedCircuit(13)
        		},
        		new FluidStack[] {
        				WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(1000),
        				WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(200)
        		}, 
        		null, 
        		new ItemStack[] {
        				WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dustTiny, 3), 
        		},
        		480, 
        		480
        	);
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 1), 
        		new ItemStack[] {
        				WerkstoffMaterialPool.RareEarthFiltrate.get(OrePrefixes.dust, 1),
        				WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1)			
        		}, 
        		new int[] {9000, 7000}, 
        		600, 
        		256
        	);
        
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1), 
        		null, 
        		null,
        		null,
        		WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1), 
        		null, 
        		300, 
        		128, 
        		1500
        	);
        
        GT_Values.RA.addChemicalBathRecipe(
        		WerkstoffMaterialPool.RareEarthFiltrate.get(OrePrefixes.dust, 1), 
        		WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(320), 
        		WerkstoffMaterialPool.NeutralizedRareEarthFiltrate.get(OrePrefixes.dust, 1), 
        		null, 
        		null, 
        		new int[] {10000}, 
        		120, 
        		240
        	);
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.NeutralizedRareEarthFiltrate.get(OrePrefixes.dust, 1),
        		new ItemStack[] {
        				WerkstoffMaterialPool.RareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
        				WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
        				WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1)
        		},
        		new int[] {9000, 5000, 4000},
        		800,
        		480
        	);
        
        
        
        
        
        
        
        
        		
        		
        		
        		
        	
        
        
        
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void removeCeriumSources() {
    	
    	GT_Log.out.print(Tags.MODID + ": AAAAAA");
    	
    	HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        //For Crusher
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID).startsWith("ore") || OreDictionary.getOreName(oreDictID).startsWith("crushed")) && OreDictionary.getOreName(oreDictID).contains("Cerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Crusher done!\n");

        //For Washer
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed") && OreDictionary.getOreName(oreDictID).contains("Cerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Washer done!\n");

        //For Thermal Centrifuge
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed") && OreDictionary.getOreName(oreDictID).contains("Cerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Naquadah.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, naquadahEarth.get(OrePrefixes.dust, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Thermal Centrifuge done!\n");

        //For Centrifuge
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                if (input.isItemEqual(GT_Bees.combs.getStackForType(CombType.DOB))){
                    GT_Recipe tRecipe = recipe.copy();
                    for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                        if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                        }
                    }
                    if (!tRecipe.equals(recipe)){
                        reAdd.add(tRecipe);
                        remove.add(recipe);
                    }
                }
                else for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("dustPureCerium") || OreDictionary.getOreName(oreDictID).startsWith("dustImpureCerium") || OreDictionary.getOreName(oreDictID).startsWith("dustSpace") || OreDictionary.getOreName(oreDictID).startsWith("dustCerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));                         
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                         
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Centrifuge done!\n");

        //For Hammer
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed") && OreDictionary.getOreName(oreDictID).contains("Cerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                            
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Hammer done!\n");

        if (LoadedList.GTPP) {
            //For Multi Centrifuge
            //Blame alk. She made some shit in it, NEI will break down if anyone modify the hash list directly.
            GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList.clear();
            RecipeGen_MultisUsingFluidInsteadOfCells.generateRecipesNotUsingCells(GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT);
            GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.reInit();

            //For Simple Washer
            for (GT_Recipe recipe : GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList) {
                ItemStack input = recipe.mInputs[0];
                if (GT_Utility.isStackValid(input)) {
                    int[] oreDict = OreDictionary.getOreIDs(input);
                    for (int oreDictID : oreDict) {
                        if (OreDictionary.getOreName(oreDictID).startsWith("dustImpureCerium")) {
                            GT_Recipe tRecipe = recipe.copy();
                            for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                                if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                                if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                               
                                }
                            }
                            if (!tRecipe.equals(recipe)){
                                reAdd.add(tRecipe);
                                remove.add(recipe);
                            }
                            break;
                        }
                    }
                }
            }
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.removeAll(remove);
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.mRecipeList.addAll(reAdd);
            GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes.reInit();

            GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

            remove.clear();
            reAdd.clear();

            GT_Log.out.print("Simple Washer done!\n");
        }

        //For ByProduct List
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sByProductList.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("ore") && OreDictionary.getOreName(oreDictID).contains("Cerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                            
                            }
                        }
                        if (!tRecipe.equals(recipe)){
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        GT_Recipe.GT_Recipe_Map.sByProductList.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sByProductList.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sByProductList.reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("ByProduct List done!\n");
    }
}
    
    

