package com.elisis.gtnhlanth.loader;

import com.elisis.gtnhlanth.Tags;
import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

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
        GT_Values.RA.addChemicalRecipe(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 4), 
                null,
                null,
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 1), 
                300
            );
        
        //Hf
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(12), 
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 1),
                null, 
                WerkstoffMaterialPool.Iodine.getFluidOrGas(2000),
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dustTiny, 1), 
                600, 1920, 3400
            );
        
        //Hf * 9
        GT_Values.RA.addBlastRecipe(
                GT_Utility.getIntegratedCircuit(13), 
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 9),
                null, 
                WerkstoffMaterialPool.Iodine.getFluidOrGas(18000),
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 9),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dust, 1), 
                5400, 1920, 3400
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
        		Materials.Ammonia.getGas(1000),
        		WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1000), 
        		Materials.Empty.getCells(1), 
        		400
        	);
        
        //IODINE-START
        //SeaweedAsh
        GT_ModHandler.addSmeltingRecipe(
        		GT_ModHandler.getModItem("harvestcraft", "seaweedItem", 1),
        		WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dustSmall, 1)
        	);
        
        //SeaweedConcentrate
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		Materials.DilutedSulfuricAcid.getFluid(600),
        		WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(600),
        		Materials.Calcite.getDustSmall(2),
        		300,
        		240
        	);
        
      //SeaweedConcentrate * 4
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 4),
        		null,
        		null,
        		null,
        		Materials.DilutedSulfuricAcid.getFluid(2400),
        		WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2400),
        		Materials.Calcite.getDust(2),
        		1200,
        		240
        	);
        
        //Iodine
        GT_Values.RA.addCentrifugeRecipe(
        		Materials.Benzene.getCells(1),
        		null,
        		WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2000),
        		WerkstoffMaterialPool.SeaweedByproducts.getFluidOrGas(200),
        		WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		null,
        		null,
        		new int[] {10000},
        		760,
        		480
        	);
        
        //IODINE-END
       
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.MagnesiumPeroxide.get(OrePrefixes.dust, 2),
        		GT_ModHandler.getModItem("dreamcraft", "PotassiumHydroxideDust", 4),
        		Materials.Oxygen.getGas(2000),
        		Materials.Water.getFluid(2000),
        		WerkstoffMaterialPool.PotassiumPermanganate.get(OrePrefixes.dust, 2),
        		null,
        		150,
        		480,
        		1200	
        	);
        
        /*
        //Fluorosilicic Acid
        GT_Values.RA.addChemicalRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		Materials.SiliconDioxide.getDust(1),
        		Materials.HydrofluoricAcid.getFluid(6000),
        		WerkstoffMaterialPool.FluorosilicicAcid.getFluidOrGas(1000),
        		null,
        		300,
        		600
        	);
        */
        //Sodium Fluorosilicate
        GT_Values.RA.addChemicalRecipe(
        		Materials.Empty.getCells(2),
        		Materials.Salt.getDust(2),
        		WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000),
        		WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(1000),
        		Materials.HydrochloricAcid.getCells(2),
        		600,
        		450
        	);
        
        
        
    }
    
    public static void loadLanthanideRecipes() {
    	
    	// Methanol
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                	MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1)
                }, 
                new FluidStack[] {
                    Materials.Methanol.getFluid(1000),
                    Materials.CarbonMonoxide.getGas(2000),
                    Materials.Oxygen.getGas(3000)
                }, 
                new FluidStack[] {
                	MyMaterial.oxalate.getFluidOrGas(1000)
                	
                },
                null, 
                450, 
                240
            );
            
        // Ethanol
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                    MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1)
                }, 
                new FluidStack[] {
                    Materials.Ethanol.getFluid(1000),
                    Materials.CarbonMonoxide.getGas(3000),
                    Materials.Oxygen.getGas(3000)
                }, 
                new FluidStack[] {
                	MyMaterial.oxalate.getFluidOrGas(1000)
                },
                null, 
                450, 
                240
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
        // MONAZITE
        RecipeAdder.instance.DigesterRecipes.addDigesterRecipe(
        		new FluidStack[] {Materials.NitricAcid.getFluid(350)}, 
        		new ItemStack[] {GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Monazite, 1)},
        		WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(200), 
        		new ItemStack[] {
        			Materials.SiliconDioxide.getDustSmall(2)
        		},
        		1920, 
        		200, 
        		800
        	);
        
        RecipeAdder.instance.DissolutionTankRecipes.addDissolutionTankRecipe(
        		new FluidStack[] {
        				Materials.Water.getFluid(10000), 
        				WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(1000)
        		},
        		new ItemStack[] {
        				Materials.Saltpeter.getDust(1)
        		},
        		WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(11000), 
        		new ItemStack[] {
            			WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dustTiny, 4),
            			WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 1),
            			Materials.Monazite.getDustTiny(2)
            	}, 
        		480, 
        		900, 
        		10
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
        				9000, 7500, 2000, 500, 2000
        		}, 
        		new FluidStack[] {
        				WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(1000)
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
        
        GT_Values.RA.addMultiblockChemicalRecipe(
        		new ItemStack[] {
        				GT_Utility.getIntegratedCircuit(14)
        		},
        		new FluidStack[] {
        				WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(9000),
        				WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1800)
        		}, 
        		null, 
        		new ItemStack[] {
        				WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 3), 
        		},
        		4320, 
        		480
        	);
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 1), 
        		new ItemStack[] {
        				WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1),
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
        
        GT_Values.RA.addThermalCentrifugeRecipe(
        		WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust),
        		Materials.Thorium.getDust(1),
        		Materials.Phosphate.getDust(1),
        		null, 
        		new int[] {
        				10000, 10000
        		},
        		200,
        		480  		
        	);
        
        GT_Values.RA.addChemicalBathRecipe(
        		WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1), 
        		WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(320), 
        		WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1), 
        		null, 
        		null, 
        		new int[] {10000}, 
        		120, 
        		240
        	);
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1),
        		new ItemStack[] {
        				WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
        				WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
        				WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1)
        		},
        		new int[] {9000, 5000, 4000},
        		800,
        		480
        	);
        
        
        GT_Values.RA.addChemicalBathRecipe(
        		WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
        		Materials.HydrofluoricAcid.getFluid(100),
        		WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		new int[] {10000},
        		360,
        		120	
        	);        
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1),
        		new ItemStack[] {
        				Materials.Fluorite.getDust(1),
        				Materials.Uranium.getDust(1),
        				Materials.Uranium.getDust(1),
        				Materials.Uranium.getDust(1),
        				Materials.Uranium235.getDust(1),
        				Materials.Uranium235.getDust(1),
        		},
        		new int[] {9500, 4500, 4000, 3000, 3000, 2000},
        		1000,
        		30
        	);
        
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		300,
        		120, 
        		1200
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		Materials.NitricAcid.getFluid(500),
        		WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000),
        		null,
        		500,
        		480
        	);
        
        GT_Values.RA.addMixerRecipe(
        		Materials.Water.getCells(1),
        		null,
        		null,
        		null,
        		WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000),
        		WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000),
        		Materials.Empty.getCells(1),
        		200,
        		120
        	);
        
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 3),
        		null,
        		null,
        		null,
        		WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000),
        		WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(2000),
        		null,
        		220,
        		120
        	);
        
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(
        		false,
        		null, 
        		new ItemStack[] {
        				WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1)
        		}, 
        		null, 
        		new int[] {
        				1111
        		}, 
        		new FluidStack[] {
        				WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000)
        		},
        		new FluidStack[] {
        				WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000)
        		}, 
        		400, 
        		240, 
        		0
        	);
        
        //BEGIN Cerium
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1),
        		Materials.Empty.getCells(1),
        		WerkstoffLoader.AmmoniumChloride.getFluidOrGas(3000),
        		Materials.Ammonia.getGas(3000),
        		WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 1),
        		Materials.Water.getCells(1),
        		300,
        		450   		
        	);
        
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		Materials.CarbonMonoxide.getGas(6000),
        		WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust),
        		null,
        		200,
        		480,
        		800
        	);
        
        //END Cerium (NMLC)
        
        GT_Recipe.GT_Recipe_Map.sVacuumRecipes.addRecipe( //Uses fluid, outputs item. Yet another hacky recipe
        		false,
        		null,
        		new ItemStack[] {
        				WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1), //TODO: Perhaps add more shit on output
        		},
        		null,
        		new FluidStack[] {
        				WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000)
        		},
        		null,
        		100,
        		240,
        		0
        	);
        
        GT_Values.RA.addElectromagneticSeparatorRecipe(
        		WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1), 
        		WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1), 
        		WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 1),  // Maybe also holmium
        		null, 
        		new int[] {
        				9000, 500
        		}, 
        		600, 
        		1920
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 1),
        		null,
        		Materials.HydricSulfide.getGas(1000),
        		Materials.Water.getFluid(1000),
        		WerkstoffMaterialPool.EuropiumSulfide.get(OrePrefixes.dust, 1),
        		300,
        		8400
        	);
        
        GT_Values.RA.addElectrolyzerRecipe(
        		WerkstoffMaterialPool.EuropiumSulfide.get(OrePrefixes.dust, 2),
        		null,
        		null,
        		null,
        		Materials.Europium.getDust(1),
        		Materials.Sulfur.getDust(1),
        		null,
        		null,
        		null,
        		null,
        		new int[] {
        				10000, 10000
        		},
         		600,
        		33000
        	);
        
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1),
        		null,
        		Materials.Chlorine.getGas(1000),
        		null,
        		WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
        		null,
        		500,
        		480, 
        		1200
        	);
        
        GT_Values.RA.addMixerRecipe(
        		Materials.Salt.getDust(1),
        		WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		Materials.Acetone.getFluid(1000),
        		null,
        		WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 1),
        		200,
        		240
        	);
        
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
        		WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2),
        		null,
        		null,
        		Materials.Acetone.getFluid(1000),
        		null,
        		WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 3),
        		400,
        		240
        	);
        /*
        GT_Values.RA.addCentrifugeRecipe(
        		WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 2),
        		null,
        		null,
        		Materials.Chloromethane.getGas(200),
        		WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dustSmall, 6),       		
        		null,  //TODO
        		null,
        		null,
        		null,
        		null,
        		new int[] {
        				10000, //10000
        		},
        		700,
        		1920	
        	);
        */
        GT_Values.RA.addCentrifugeRecipe(
        		GT_Utility.getIntegratedCircuit(4),
        		WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 8),
        		null,
        		Materials.Chloromethane.getGas(800),
        		WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 6),
        		null, //WerkstoffMaterialPool.UnknownBlend.get(OrePrefixes.dust, 2) TODO
        		null,
        		null,
        		null,
        		null,
        		new int[] {
        				10000, //10000
        		},
        		6300,
        		1920	
        	);
        
        GT_Values.RA.addSifterRecipe(
        		WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 9),
        		new ItemStack[] {
        				Materials.Samarium.getDust(6),
        				Materials.Gadolinium.getDust(3)
        		},
        		new int[] {
        				10000, 10000
        		},
        		400,
        		1920
        	);
        
        
        
        // BASTNASITE (god help me)
        RecipeAdder.instance.DigesterRecipes.addDigesterRecipe(
        		new FluidStack[] {Materials.NitricAcid.getFluid(350)}, 
        		new ItemStack[] {GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 1)},
        		WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(200), 
        		new ItemStack[] {
        			Materials.SiliconDioxide.getDustSmall(2)
        		},
        		1920, 
        		200, 
        		800
        	);
        
        GT_Values.RA.addCrackingRecipe(
        		1, 
        		WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(1000), 
        		GT_ModHandler.getSteam(1000), 
        		WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.getFluidOrGas(2000), 
        		600, 
        		480
        	);
        
        GT_Values.RA.addMixerRecipe(
        		GT_Utility.getIntegratedCircuit(6),
        		WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.get(OrePrefixes.cell, 1),
        		null,
        		null,
        		WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(320),
        		WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1320),
        		null,
        		800,
        		120
        	);
        
        RecipeAdder.instance.DissolutionTankRecipes.addDissolutionTankRecipe(
        		new FluidStack[] {
        				Materials.Water.getFluid(10000),
        				WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1000)
        		}, 
        		new ItemStack[] {
        				Materials.Saltpeter.getDust(1)
        		}, 
        		WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(11000), 
        		new ItemStack[] {
        				WerkstoffMaterialPool.Gangue.get(OrePrefixes.dust, 1)
        		}, 
        		1920, 
        		1000, 
        		10
        	);
        
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(
        		false, 
        		null, 
        		new ItemStack[] {
        				Materials.SiliconDioxide.getDust(1),
        				Materials.Rutile.getDust(1),
        				WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1), //TODO:Change outputs to complement Monazite
        				Materials.Ilmenite.getDust(1)
        		}, 
        		null, 
        		new int[] {
        				9000, 7500, 1000, 500, 2000
        		}, 
        		new FluidStack[] {
        				WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(1000)
        		},
        		new FluidStack[] {
        				WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(400)
        		}, 
        		400, 
        		240, 
        		0
        	);
        
        GT_Values.RA.addBlastRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		null,
        		WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(1000),
        		null, //TODO: Maybe add some useful shit?
        		WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1),
        		null, //See above
        		500,
        		600,
        		1400
        	);
        
        GT_Values.RA.addChemicalBathRecipe(
        		WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1),
        		Materials.HydrochloricAcid.getFluid(500),
        		WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		new int[] {10000},
        		200,
        		30	
        	);
        
        GT_Values.RA.addBlastRecipe(
        		GT_Utility.getIntegratedCircuit(1),
        		WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1),
        		Materials.Oxygen.getGas(1000),
        		Materials.Fluorine.getGas(13),
        		WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1),
        		null, 
        		600,
        		120,
        		1200
        	);
        
        GT_Values.RA.addMixerRecipe(
        		GT_Utility.getIntegratedCircuit(7),
        		WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		Materials.Water.getFluid(200),
        		null,
        		WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1),
        		100,
        		30
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1),
        		null,
        		Materials.Fluorine.getGas(4000),
        		Materials.HydrofluoricAcid.getFluid(4000),
        		WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1),
        		300,
        		480
        	);
        
        GT_Values.RA.addCentrifugeRecipe(
        		WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1),
        		WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		null,
        		new int[] {
        				10000, 9000
        		}, 
        		600,
        		480
        	);
        
        GT_Values.RA.addMixerRecipe(
        		WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		Materials.NitricAcid.getFluid(400),
        		WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.getFluidOrGas(1000),
        		null,
        		300,
        		480
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.get(OrePrefixes.cell, 1),
        		null,
        		Materials.Acetone.getFluid(1000),
        		WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000),
        		null,
        		700,
        		480
        	);
        
        GT_Values.RA.addCentrifugeRecipe(
        		null,
        		null,
        		WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000),
        		null,
        		WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		null,
        		null,
        		new int[] {
        				8000, 5000
        		},
        		900,
        		480	
        	);
        
        
        //Nd RE
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		Materials.HydrochloricAcid.getFluid(1000),
        		null,
        		WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dustSmall, 2),
        		WerkstoffMaterialPool.NeodymiumOxide.get(OrePrefixes.dustSmall, 2),
        		450,
        		800	
        	);
        
        
        //Sm RE
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		Materials.HydrofluoricAcid.getFluid(2000),
        		null,
        		WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 1),
        		null,
        		300,
        		480
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
        		WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1),
        		Materials.HydrofluoricAcid.getFluid(2000),
        		null,
        		WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 2),
        		null,
        		350,
        		480
        		);
        
        
        
        GT_Values.RA.addBlastRecipe(
        		WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 2),
        		Materials.Calcium.getDust(3),
        		null,
        		WerkstoffMaterialPool.CalciumFluoride.getFluidOrGas(3000),
        		Materials.Holmium.getDustSmall(1),
        		WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 1),
        		400,
        		1920,
        		1200
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 1),
        		BotWerkstoffMaterialPool.AmmoniumNitrate.get(OrePrefixes.dust, 1),
        		null,
        		null,
        		WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 1),
        		null,
        		600,
        		480
        	);
  
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 1),
        		Materials.Copper.getDustSmall(1),
        		null,
        		null,
        		WerkstoffMaterialPool.TerbiumNitrate.get(OrePrefixes.dustSmall, 2),
        		WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dustSmall, 2), // Potentially make only Samarium
        		800,
        		1920
        	);
        
        GT_Values.RA.addChemicalRecipe(
        		WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2), 
        		Materials.Calcium.getDust(3), 
        		null,
        		null,
        		WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
        		Materials.TricalciumPhosphate.getDust(1),
        		300,
        		1920
        	);
        
        GT_Values.RA.addCentrifugeRecipe(
        		WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 6),
        		null,
        		null,
        		null,
        		Materials.Samarium.getDust(1),
        		WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 2),
        		WerkstoffMaterialPool.Gangue.get(OrePrefixes.dust, 4),
        		null,
        		null,
        		null,
        		new int[] {
        				9000, 8000, 10000
        		},
        		200,
        		1920        		
        	);
    }
    
    public static void addRandomChemCrafting() {
    	
    	//PTMEG Elastomer
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.Butanediol.get(OrePrefixes.cell, 1),
    			null,
    			WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(4000),
    			WerkstoffMaterialPool.PTMEGElastomer.getMolten(4000),
    			Materials.Empty.getCells(1),
    			1500,
    			480
    		);
    	
    	//Toluene Tetramethyl Diisocyanate
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.TolueneDiisocyanate.get(OrePrefixes.cell, 3),
    			Materials.Hydrogen.getCells(2),
    			WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(1000),
    			WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(2000),
    			Materials.Empty.getCells(5),
    			1200,
    			480
    		);
    	
    	//PTHF
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.TungstophosphoricAcid.get(OrePrefixes.cell, 1),
    			Materials.Oxygen.getCells(1),
    			WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(144),
    			WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(432),
    			Materials.Empty.getCells(2),
    			1000,
    			120
    		);
    	
    	//THF
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.AcidicButanediol.get(OrePrefixes.cell, 1),
    			null,
    			Materials.Ethanol.getFluid(1000),
    			WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(1000),
    			Materials.Empty.getCells(1),
    			800,
    			480
    		);
    	
    	//Acidicised Butanediol
    	GT_Values.RA.addMixerRecipe(
    			Materials.SulfuricAcid.getCells(1),
    			null,
    			null,
    			null,
    			WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000),
    			WerkstoffMaterialPool.AcidicButanediol.getFluidOrGas(1000),
    			Materials.Water.getCells(1),
    			600,
    			2000
    		);
    	
    	//Butanediol
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dustTiny, 1),
    			null,
    			Materials.Butane.getGas(1000),
    			WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000),
    			null,
    			900,
    			1920
    		);
    	
    	//Moly-Te-Oxide Catalyst
    	GT_Values.RA.addMixerRecipe(
    			WerkstoffMaterialPool.MolybdenumIVOxide.get(OrePrefixes.dust, 1),
    			WerkstoffMaterialPool.TelluriumIVOxide.get(OrePrefixes.dust, 1),
    			null,
    			null,
    			null,
    			null,
    			WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 2),
    			300,
    			120
    		);
    	
    	//Tungstophosphoric Acid
    	GT_Values.RA.addChemicalRecipe(
    			Materials.PhosphoricAcid.getCells(1),
    			Materials.HydrochloricAcid.getCells(24),
    			BotWerkstoffMaterialPool.SodiumTungstate.getFluidOrGas(12000),
    			WerkstoffMaterialPool.TungstophosphoricAcid.getFluidOrGas(1000),
    			Materials.Salt.getDust(24),
    			Materials.Empty.getCells(25),
    			500,
    			1024
    		);
    	
    	
    	
    	//Toluene Diisocyanate
    	GT_Values.RA.addChemicalRecipe(
    			WerkstoffMaterialPool.Diaminotoluene.get(OrePrefixes.cell, 1),
    			null,
    			BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2000),
    			WerkstoffMaterialPool.TolueneDiisocyanate.getFluidOrGas(1000),
    			Materials.HydrochloricAcid.getCells(4),
    			900,
    			480
    		);
    	
    	//Diaminotoluene
    	GT_Values.RA.addChemicalRecipe(
    			Materials.Hydrogen.getCells(4),
    			null,
    			WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000),
    			WerkstoffMaterialPool.Diaminotoluene.getFluidOrGas(1000),
    			Materials.Empty.getCells(4),
    			300,
    			480
    		);
    	
    	//Dinitrotoluene
    	GT_Values.RA.addChemicalRecipe(
    			Materials.NitricAcid.getCells(2),
    			null,
    			Materials.Toluene.getFluid(1000),
    			WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000),
    			Materials.Empty.getCells(2),
    			900,
    			480
    		);
		//Digester Control Block
		GT_Values.RA.addAssemblerRecipe(new ItemStack[]
				{ItemList.Hull_IV.get(1L),  ItemList.Super_Tank_EV.get(2L), ItemList.Electric_Motor_IV.get(4L), ItemList.Electric_Pump_IV.get(4L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Desh, 4L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4L), GT_Utility.getIntegratedCircuit(1)},
				Materials.Polytetrafluoroethylene.getMolten(1440), LanthItemList.DIGESTER, 600, 4096);

		//Dissolution Tank
		GT_Values.RA.addAssemblerRecipe(new ItemStack[]
				{ItemList.Hull_EV.get(1L), ItemList.Super_Tank_HV.get(2L), ItemList.Electric_Motor_EV.get(4L), ItemList.Electric_Pump_EV.get(2L), GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 4L), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 4L), GT_Utility.getIntegratedCircuit(2)},
				Materials.Polytetrafluoroethylene.getMolten(720), LanthItemList.DISSOLUTION_TANK, 400, 960);
		
		
		GT_Values.RA.addMixerRecipe(
				Materials.Nichrome.getDust(4),
				Materials.Aluminium.getDust(2),
				WerkstoffMaterialPool.Hafnium.get(OrePrefixes.dust, 1),
				Materials.Tantalum.getDust(2),
				Materials.Tungsten.getDust(5),
				WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 1),
				Materials.Titanium.getDust(1),
				null,
				null,
				null,
				null,
				WerkstoffMaterialPool.MARM247.get(OrePrefixes.dust, 16),
				null,
				null,
				null,
				800,
				7680
			);
		
		// PTMEG Manipulation
		
		GT_Values.RA.addFluidSolidifierRecipe(
				ItemList.Shape_Mold_Ingot.get(0L), 
				WerkstoffMaterialPool.PTMEGElastomer.getMolten(144), 
				WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.ingot, 1), 
				40, 
				64
			);
		
		GT_Values.RA.addFluidSolidifierRecipe(
				ItemList.Shape_Mold_Plate.get(0L), 
				WerkstoffMaterialPool.PTMEGElastomer.getMolten(144), 
				WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.plate, 1), 
				40, 
				64
			);
		
	}
    
    //public static void loadZylon
    
	public static void removeCeriumSources() {
    	
    	GT_Log.out.print(Tags.MODID + ": AAAAAA");
    	
    	HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        //For Crusher
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.mRecipeList) {
            ItemStack input = recipe.mInputs[0];
            //GT_Log.out.print("\n" + input.getDisplayName());
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);            
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID).startsWith("ore") || OreDictionary.getOreName(oreDictID).startsWith("crushed")) /*&& OreDictionary.getOreName(oreDictID).contains("Cerium")*/) {
                    	GT_Log.out.print(OreDictionary.getOreName(oreDictID));
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Lanthanum.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = null;
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
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed") /*&& OreDictionary.getOreName(oreDictID).contains("Cerium")*/) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Lanthanum.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = null;
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
                    if ((OreDictionary.getOreName(oreDictID).startsWith("crushed") || OreDictionary.getOreName(oreDictID).startsWith("purified")) /*&& OreDictionary.getOreName(oreDictID).contains("Cerium")*/) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Lanthanum.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = null;
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
            FluidStack fluidInput = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (recipe.mFluidInputs.length > 0) fluidInput = recipe.mFluidInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID).startsWith("dust") && (!OreDictionary.getOreName(oreDictID).contains("Dephosphated")) /*OreDictionary.getOreName(oreDictID).startsWith("dustPureCerium") || OreDictionary.getOreName(oreDictID).startsWith("dustImpureCerium") || OreDictionary.getOreName(oreDictID).startsWith("dustSpace") || OreDictionary.getOreName(oreDictID).startsWith("dustCerium")*/) {
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
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
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
            /*
            GT_Recipe tRecipe = recipe.copy();
            if (GT_Utility.isStackValid(fluidInput)) {
            	if (fluidInput.getLocalizedName() == MyMaterial.plutoniumBasedLiquidFuel.getDefaultName()) {
            		tRecipe.mOutputs[1] = GT_Utility.copyAmount(tRecipe.mOutputs[1].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
            		reAdd.add(tRecipe);
                	remove.add(tRecipe);
            	}
            	
            }*/
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
                    if (OreDictionary.getOreName(oreDictID).startsWith("crushed") /*&& OreDictionary.getOreName(oreDictID).contains("Cerium")*/) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                            
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize * 2, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
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
        
        //Electrolyzer
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList) {
        	//ItemStack input = recipe.mInputs[0];
        	for (ItemStack input : recipe.mInputs) {
        		GT_Log.out.print(input.getDisplayName() + "\n");
        		if (GT_Utility.isStackValid(input)) {
        			if (input.getDisplayName().startsWith("Hibonite") || input.getDisplayName().startsWith("Lanthanite") || input.getDisplayName().startsWith("Zirconolite") || input.getDisplayName().startsWith("Yttrocerite") || input.getDisplayName().startsWith("Xenotime")) {
        				GT_Recipe tRecipe = recipe.copy();
        				for (int i = 0; i < tRecipe.mOutputs.length; i ++) {
        					if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
        					if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
        						tRecipe.mOutputs[i] = GT_Utility.copyAmount(tRecipe.mOutputs[i].stackSize, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));                               
        					}
        					else if (tRecipe.mOutputs[i].isItemEqual(WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 1))) { //TODO: Does not work and I have zero clue why
        						tRecipe.mOutputs[i] = null;
        					}
        					else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	tRecipe.mOutputs[i] = null;
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
        
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.reInit();
        
        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");
        
        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Electrolyzer done!\n");
        
        /*
        //TODO: This entire block is highly questionable because GT++ doesn't always load predictably
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
            
            
            //Dehydrator
            for (GT_Recipe recipe : GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mRecipeList) {
            	GT_Log.out.print(Arrays.toString(recipe.mInputs));
            	ItemStack input = recipe.mInputs[0];
            	
            	
            	if (GT_Utility.isStackValid(input)) {
            		GT_Recipe tRecipe = recipe.copy();
            		for (int i = 0; i < tRecipe.mOutputs.length; i++) {
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
            
            GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mRecipeList.removeAll(remove);
            GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mRecipeList.addAll(reAdd);
            GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.reInit();
            
            GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");
            
            remove.clear();
            reAdd.clear();
            
            GT_Log.out.print("Dehydrator done!\n");
            
            
            
        }
        */
        
        
        /* DOES NOT WORK, something to do with load times for sifter recipes or some shit
      //Sifter
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sSifterRecipes.mRecipeList) {
        	if (recipe.mInputs.length == 0)
        		break;
        	ItemStack input = recipe.mInputs[0];
        	GT_Log.out.print("Sift ore found " + input.getDisplayName() + "\n");
        	if (GT_Utility.isStackValid(input)) {
        		if (true) {	
        			GT_Log.out.print("Sift ore found and iffed " + input.getDisplayName() + "\n");
        			//GT_Recipe tRecipe = recipe.copy();			
        			remove.add(recipe);
        			break;
        		}
        	}
        	
        }
        
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.reInit();
        
        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");
        
        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Sifter done!\n");
        */
      //Chemical Bath
        for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.mRecipeList) {
        	//ItemStack input = recipe.mInputs[0];
        	for (ItemStack input : recipe.mInputs) {
        		GT_Log.out.print(input.getDisplayName() + "\n");
        		if (GT_Utility.isStackValid(input)) {
        			if (input.getDisplayName().contains("Tin Dust") || input.getDisplayName().contains("Rutile Dust")) {
        				
        				GT_Recipe tRecipe = recipe.copy();			
        				remove.add(recipe);
        				break;
        			}
        		}
        	}
        }
        
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.mRecipeList.removeAll(remove);
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.mRecipeList.addAll(reAdd);
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.reInit();
        
        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");
        
        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Chemical Bath done!\n");
        
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
                                remove.add(tRecipe);                         
                            }
                            else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1)) ) {
                            	remove.add(tRecipe);
                            }
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
    
    

