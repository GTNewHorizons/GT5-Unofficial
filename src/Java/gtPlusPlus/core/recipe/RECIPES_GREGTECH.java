package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.lib.CORE.GTNH;

import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.HotFuel;
import gregtech.api.util.ThermalFuel;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.crafting.ItemDummyResearch.ASSEMBLY_LINE_RESEARCH;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EnchantingUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import gtPlusPlus.everglades.dimension.Dimension_Everglades;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RECIPES_GREGTECH {

	public static void run() {
		Logger.INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
		execute();
	}

	private static void execute() {
		cokeOvenRecipes();
		electrolyzerRecipes();
		assemblerRecipes();
		fluidcannerRecipes();
		distilleryRecipes();
		extractorRecipes();
		fluidExtractorRecipes();
		chemicalBathRecipes();
		chemicalReactorRecipes();
		dehydratorRecipes();
		blastFurnaceRecipes();

		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			largeChemReactorRecipes();
			fusionRecipes();
		}

		fissionFuelRecipes();
		autoclaveRecipes();
		compressorRecipes();
		mixerRecipes();
		macerationRecipes();
		centrifugeRecipes();
		benderRecipes();
		cyclotronRecipes();
		blastSmelterRecipes();
		//advancedMixerRecipes();
		sifterRecipes();
		electroMagneticSeperatorRecipes();
		extruderRecipes();
		cuttingSawRecipes();
		breweryRecipes();
		laserEngraverRecipes();
		assemblyLineRecipes();
		
		addFuels();
	}

	private static void fusionRecipes() {		

	    /**
	     * Adds a Fusion reactor Recipe
	     *
	     * @param aInput1                        = first Input (not null, and respects StackSize)
	     * @param aInput2                        = second Input (not null, and respects StackSize)
	     * @param aOutput                        = Output of the Fusion (can be null, and respects StackSize)
	     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
	     * @param aFusionEnergyPerTick           = The EU generated per Tick (can even be negative!)
	     * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
	     */		
		
		//Hydrogen Plasma
        /*CORE.RA.addFusionReactorRecipe(
        		Particle.getIon("Hydrogen", 0),
        		Particle.getIon("Hydrogen", 0),
        		Materials.Hydrogen.getPlasma(1),
        		5000,
        		16,
        		4096,
        		40000000);*/
		
		
		//Hypogen Creation
		GT_Values.RA.addFusionReactorRecipe(
				Materials.Neutronium.getMolten(128),
				ALLOY.QUANTUM.getFluid(256),
				ELEMENT.STANDALONE.HYPOGEN.getFluid(4),
				2048 * 4,
				(int) GT_Values.V[9],
				600000000 * 2);
		
	}

	private static void assemblyLineRecipes() {
		

		
		ItemStack[] aCoilWire = new ItemStack[] {
				ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 0, GTNH ? 64 : 32),
				ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 1, GTNH ? 48 : 16),
				ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 2, GTNH ? 32 : 8),
				ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 3, GTNH ? 16 : 4),
		};
		
		
		
		
		//Containment Casings
		CORE.RA.addAssemblylineRecipe(
				ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1), 
				20 * 60 * 30,
				new ItemStack[] {
						ItemList.Field_Generator_IV.get(GTNH ? 32 : 16),
						ItemList.Electric_Motor_EV.get(GTNH ? 64 : 32),
						ItemList.Energy_LapotronicOrb.get(GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.cableGt12, 7, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.wireGt16, 6, GTNH ? 64 : 32),
						ItemUtils.getOrePrefixStack(OrePrefixes.plate, Materials.Naquadria, GTNH ? 64 : 16),
						ELEMENT.getInstance().GADOLINIUM.getDust(GTNH ? 32 : 8),
						ELEMENT.getInstance().SAMARIUM.getDust(GTNH ? 16 : 4),
						ALLOY.ARCANITE.getGear(GTNH ? 8 : 2),
						CI.getTieredComponent(OrePrefixes.circuit, 5, GTNH ? 64 : 32),
						CI.getTieredComponent(OrePrefixes.circuit, 6, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.circuit, 7, GTNH ? 16 : 8),
						GregtechItemList.Laser_Lens_Special.get(1),
						aCoilWire[3]
				}, 
				new FluidStack[] {
						ALLOY.NITINOL_60.getFluid(144 * 9 * (GTNH ? 4 : 2)),
						ALLOY.ENERGYCRYSTAL.getFluid(144 * 9 * (GTNH ? 8 : 4)),
						ALLOY.TUMBAGA.getFluid(144 * 9 * (GTNH ? 32 : 8)),
						ALLOY.NICHROME.getFluid(144 * 1 * (GTNH ? 16 : 4)),
						
				},
				ItemUtils.getSimpleStack(ModBlocks.blockCasings3Misc, 15, 32), 
				20 * 60 * 10 * (GTNH ? 2 : 1),
				(int) GT_Values.V[6]);
		
		//Slow Fusion Controller
		CORE.RA.addAssemblylineRecipe(
				GregtechItemList.COMET_Cyclotron.get(1), 
				20 * 60 * 30,
				new ItemStack[] {
						CI.getFieldGenerator(6, GTNH ? 32 : 8),
						ItemList.Electric_Motor_EV.get(GTNH ? 32 : 8),
						ItemList.Tool_Scanner.get(GTNH ? 4 : 2),
						CI.getTieredComponent(OrePrefixes.cableGt12, 6, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.wireGt16, 5, GTNH ? 64 : 32),
						CI.getTieredComponent(OrePrefixes.plate, 6, GTNH ? 64 : 32),
						ELEMENT.getInstance().GADOLINIUM.getDust(GTNH ? 32 : 8),
						ELEMENT.getInstance().SAMARIUM.getDust(GTNH ? 16 : 4),
						ALLOY.INCOLOY_MA956.getGear(GTNH ? 8 : 2),
						CI.getTieredComponent(OrePrefixes.circuit, 5, GTNH ? 64 : 32),
						CI.getTieredComponent(OrePrefixes.circuit, 6, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.circuit, 7, GTNH ? 16 : 8)
				}, 
				new FluidStack[] {
						CI.getTieredFluid(6, 144 * 9 * (GTNH ? 8 : 4)),
						CI.getTertiaryTieredFluid(6, 144 * 9 * (GTNH ? 8 : 4)),
						CI.getAlternativeTieredFluid(6, 144 * 9 * (GTNH ? 8 : 4)),
						CI.getTieredFluid(5, 144 * 9 * (GTNH ? 16 : 8)),						
				},
				GregtechItemList.Miniature_Fusion.get(1), 
				20 * 60 * 5 * (GTNH ? 2 : 1),
				(int) GT_Values.V[7]);
		
		
		//Plasma Tank
		CORE.RA.addAssemblylineRecipe(
				ItemUtils.getOrePrefixStack(OrePrefixes.pipeMedium, Materials.Superconductor, 1), 
				20 * 60 * 5,
				new ItemStack[] {
						CI.getTieredComponent(OrePrefixes.plate, 5, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.circuit, 5, GTNH ? 16 : 4),
						CI.getTieredComponent(OrePrefixes.pipeHuge, 5, GTNH ? 16 : 4),
						CI.getTieredComponent(OrePrefixes.cableGt08, 7, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.gearGt, 6, GTNH ? 8 : 4),
						aCoilWire[2]
				}, 
				new FluidStack[] {
						CI.getTieredFluid(4, 144 * 9 * (GTNH ? 16 : 8)),
						CI.getTertiaryTieredFluid(4, 144 * 9 * (GTNH ? 16 : 8)),
						CI.getAlternativeTieredFluid(4, 144 * 9 * (GTNH ? 16 : 8)),
						
				},
				GregtechItemList.Plasma_Tank.get(1), 
				20 * 60 * 1 * (GTNH ? 2 : 1),
				(int) GT_Values.V[5]);
		
		
		/*
		 * Contianment casings
		 */
		
		ItemStack[] aGemCasings = new ItemStack[] {
				GregtechItemList.Battery_Casing_Gem_1.get(1), 
				GregtechItemList.Battery_Casing_Gem_2.get(1), 
				GregtechItemList.Battery_Casing_Gem_3.get(1), 
				GregtechItemList.Battery_Casing_Gem_4.get(1), 
		};
		ItemStack[] aResearch = new ItemStack[] {
				Particle.getBaseParticle(Particle.UNKNOWN), 
				GregtechItemList.Battery_Casing_Gem_1.get(1), 
				GregtechItemList.Battery_Casing_Gem_2.get(1), 
				GregtechItemList.Battery_Casing_Gem_3.get(1),
		};
		
		int aCasingSlot = 0;
		for (int j = 6; j < 10; j++) {
			CORE.RA.addAssemblylineRecipe(
					aResearch[aCasingSlot], 
					20 * 60 * 60,
					new ItemStack[] {
							CI.getTieredComponent(OrePrefixes.plate, j-1, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.circuit, j-2, GTNH ? 16 : 8),
							CI.getTieredComponent(OrePrefixes.cableGt08, j+1, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.gearGt, j-1, GTNH ? 8 : 4),
							aCoilWire[aCasingSlot]
					}, 
					new FluidStack[] {
							CI.getTieredFluid(j, 144 * 3 * (GTNH ? 16 : 8)),
							CI.getTertiaryTieredFluid(j-2, 144 * 4 * (GTNH ? 16 : 8)),
							CI.getAlternativeTieredFluid(j, 144 * 6 * (GTNH ? 16 : 8)),
							
					},
					aGemCasings[aCasingSlot++], 
					20 * 60 * 1 * (GTNH ? 2 : 1),
					(int) GT_Values.V[j]);
		}
		
		/*
		 * Gem Battery Recipes
		 */
		
		ItemStack[] aGemBatteries = new ItemStack[] {
				GregtechItemList.Battery_Gem_1.get(1), 
				GregtechItemList.Battery_Gem_2.get(1), 
				GregtechItemList.Battery_Gem_3.get(1), 
				GregtechItemList.Battery_Gem_4.get(1), 
		};
		
		ItemStack[] aExoticInputs = new ItemStack[] {
				Particle.getBaseParticle(Particle.PROTON), 
				Particle.getBaseParticle(Particle.ELECTRON), 
				Particle.getBaseParticle(Particle.CHARM), 
				Particle.getBaseParticle(Particle.GRAVITON)			
		};
		aCasingSlot = 0;
		for (int j = 6; j < 10; j++) {
			CORE.RA.addAssemblylineRecipe(
					aExoticInputs[aCasingSlot], 
					20 * 60 * 60 * 5,
					new ItemStack[] {
							aGemCasings[aCasingSlot],
							ItemUtils.getSimpleStack(aExoticInputs[aCasingSlot], GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.plate, j, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.circuit, j, GTNH ? 16 : 8),
							CI.getTieredComponent(OrePrefixes.wireGt16, j+1, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.bolt, j, GTNH ? 8 : 4),
							CI.getTieredComponent(OrePrefixes.screw, j-1, GTNH ? 8 : 4),
					}, 
					new FluidStack[] {
							CI.getTieredFluid(j, 144 * 3 * (GTNH ? 16 : 8)),
							CI.getTertiaryTieredFluid(j-2, 144 * 4 * (GTNH ? 16 : 8)),
							CI.getAlternativeTieredFluid(j, 144 * 6 * (GTNH ? 16 : 8)),	
							CI.getTertiaryTieredFluid(j-1, 144 * 5 * (GTNH ? 16 : 8)),							
					},
					aGemBatteries[aCasingSlot++], 
					20 * 60 * 1 * (GTNH ? 2 : 1),
					(int) GT_Values.V[j]);
		}
		
		
		//Nano Healer
		CORE.RA.addAssemblylineRecipe(
				ItemUtils.simpleMetaStack(Items.golden_apple, 1, 1), 
				20 * 60 * 10,
				new ItemStack[] {
						ItemUtils.getSimpleStack(aGemCasings[2], GTNH ? 4 : 2),
						CI.getTieredComponent(OrePrefixes.plate, 8, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.circuit, 7, GTNH ? 16 : 4),
						CI.getTieredComponent(OrePrefixes.cableGt02, 7, GTNH ? 16 : 8),
						CI.getTieredComponent(OrePrefixes.gearGt, 6, GTNH ? 6 : 3),
						CI.getTieredComponent(OrePrefixes.screw, 7, GTNH ? 16 : 8),
						CI.getTieredComponent(OrePrefixes.bolt, 5, GTNH ? 24 : 12),
						CI.getTieredComponent(OrePrefixes.frameGt, 4, GTNH ? 12 : 6),
						aCoilWire[3]
				}, 
				new FluidStack[] {
						CI.getTieredFluid(7, 144 * 18 * (GTNH ? 16 : 8)),
						CI.getTertiaryTieredFluid(7, 144 * 18 * (GTNH ? 16 : 8)),
						CI.getAlternativeTieredFluid(6, 144 * 18 * (GTNH ? 16 : 8)),
						CI.getAlternativeTieredFluid(7, 144 * 18 * (GTNH ? 16 : 8)),
						
				},
				ItemUtils.getItemStack("miscutils:personalHealingDevice", 1), 
				20 * 60 * 30 * (GTNH ? 2 : 1),
				(int) GT_Values.V[7]);
		
		
		
		//Charge Pack LuV-UV

		ItemStack[] aChargeResearch = new ItemStack[] {
				ItemUtils.getItemStack("miscutils:item.itemBufferCore7", 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack1, 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack2, 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack3, 1),
		};
		
		ItemStack[] aChargeOutputs = new ItemStack[] {
				ItemUtils.getSimpleStack(ModItems.itemChargePack1, 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack2, 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack3, 1),
				ItemUtils.getSimpleStack(ModItems.itemChargePack4, 1),			
		};

		ItemStack[] aBufferCoreInputs = new ItemStack[] {
				ItemUtils.getItemStack("miscutils:item.itemBufferCore7", GTNH ? 8 : 4),
				ItemUtils.getItemStack("miscutils:item.itemBufferCore8", GTNH ? 8 : 4),
				ItemUtils.getItemStack("miscutils:item.itemBufferCore9", GTNH ? 8 : 4),
				ItemUtils.getItemStack("miscutils:item.itemBufferCore10", GTNH ? 8 : 4),
		};
		
		int aCurrSlot = 0;
		for (int h = 6; h < 10; h++) {
			CORE.RA.addAssemblylineRecipe(
					aChargeResearch[aCurrSlot], 
					20 * 60 * 10 * (aCurrSlot + 1),
					new ItemStack[] {
							ItemUtils.getSimpleStack(
							aGemBatteries[aCurrSlot], GTNH ? 4 : 2),
							aBufferCoreInputs[aCurrSlot],
							aCoilWire[aCurrSlot],
							CI.getTieredComponent(OrePrefixes.plate, h, GTNH ? 16 : 8),
							CI.getTieredComponent(OrePrefixes.plate, h-1, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.circuit, h, GTNH ? 16 : 4),
							CI.getTieredComponent(OrePrefixes.circuit, h-1, GTNH ? 32 : 8),
							CI.getTieredComponent(OrePrefixes.cableGt12, h-1, GTNH ? 32 : 16),
							CI.getTieredComponent(OrePrefixes.screw, h, GTNH ? 16 : 8),
							CI.getTieredComponent(OrePrefixes.bolt, h-2, GTNH ? 32 : 16),
							CI.getElectricMotor(h, GTNH ? 8 : 4),
							CI.getFieldGenerator(h-1, 2),
							CI.getRobotArm(h-2, GTNH ? 4 : 2),
					}, 
					new FluidStack[] {
							CI.getTieredFluid(h, 144 * 18 * (GTNH ? 8 : 4)),
							CI.getTertiaryTieredFluid(h-1, 144 * 18 * (GTNH ? 8 : 4)),
							CI.getAlternativeTieredFluid(h-1, 144 * 18 * (GTNH ? 8 : 4)),
							CI.getAlternativeTieredFluid(h-2, 144 * 18 * (GTNH ? 8 : 4)),							
					},
					aChargeOutputs[aCurrSlot], 
					20 * 60 * 30 * (GTNH ? 2 : 1) * (aCurrSlot+1),
					(int) GT_Values.V[h]);
			aCurrSlot++;
		}
		
		
		
		
		
		
		
		
		
	}

	private static void laserEngraverRecipes() {		

		GT_Values.RA.addLaserEngraverRecipe(
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 6L),
				GregtechItemList.Laser_Lens_Special.get(0),
				ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1),
				20 * 60 * 3,
				MaterialUtils.getVoltageForTier(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.vTier));
		
		GT_Values.RA.addLaserEngraverRecipe(
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 8L),
				GregtechItemList.Laser_Lens_Special.get(0),
				ELEMENT.STANDALONE.ASTRAL_TITANIUM.getDust(1),
				20 * 60 * 2,
				MaterialUtils.getVoltageForTier(ELEMENT.STANDALONE.ASTRAL_TITANIUM.vTier));
		
		GT_Values.RA.addLaserEngraverRecipe(
				ALLOY.NITINOL_60.getBlock(2),
				GregtechItemList.Laser_Lens_Special.get(0),
				ELEMENT.STANDALONE.ADVANCED_NITINOL.getBlock(1),
				20 * 60 * 1,
				MaterialUtils.getVoltageForTier(ELEMENT.STANDALONE.ADVANCED_NITINOL.vTier));
		
		GT_Values.RA.addLaserEngraverRecipe(
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 64L),
				GregtechItemList.Laser_Lens_Special.get(0),
				ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getDust(1),
				20 * 60 * 5,
				MaterialUtils.getVoltageForTier(ELEMENT.STANDALONE.CHRONOMATIC_GLASS.vTier));	

		
		GT_Values.RA.addLaserEngraverRecipe(
				CI.getFieldGenerator(6, 1),
				CI.getEmitter(7, 2),
				ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1),
				20 * 60 * 5,
				MaterialUtils.getVoltageForTier(5));
		
		
	}

	private static void breweryRecipes() {
		CORE.RA.addBrewingRecipe(14, EnchantingUtils.getMobEssence(100), EnchantingUtils.getLiquidXP(1332), 100, 120, false);
		CORE.RA.addBrewingRecipe(14, EnchantingUtils.getLiquidXP(1332), EnchantingUtils.getMobEssence(100), 100, 120, false);		
	}

	private static void cuttingSawRecipes() {
		GT_Values.RA.addCutterRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1), //Input
				ItemUtils.getItemStackOfAmountFromOreDict("plateMeatRaw", 9), //Output
				null,
				16, //Time
				8); //EU
	}

	private static void electrolyzerRecipes() {
		GT_Values.RA.addElectrolyzerRecipe(
				ItemUtils.getSimpleStack(ModItems.dustDecayedRadium226, 1),
				null,
				null,
				FluidUtils.getFluidStack("radon", !GTNH ? 500 : 144),
				null,
				null, 
				null, 
				null,
				null,
				null,
				new int[]{}, 
				20*90, 
				240);
	}

	private static void extruderRecipes() {
		// Osmium Credits
		if (GT_Values.RA.addExtruderRecipe(ItemUtils.getItemStackOfAmountFromOreDict("blockOsmium", 1),
				ItemList.Shape_Mold_Credit.get(0), ItemList.Credit_Greg_Osmium.get(1),
				(int) Math.max(Materials.Osmium.getMass() * 2L * 20, 1), 1024)) {
			Logger.WARNING("Extruder Recipe: Osmium Credit - Success");
		}
		else {
			Logger.WARNING("Extruder Recipe: Osmium Credit - Failed");
		}
	}

	private static void blastSmelterRecipes() {

		if (!GTNH) {
			// Trinium
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { 
							ItemUtils.getGregtechCircuit(8),
							ELEMENT.getInstance().BISMUTH.getDust(8),
							ELEMENT.getInstance().IRON.getDust(64),
							ELEMENT.getInstance().CARBON.getDust(16),
							ELEMENT.getInstance().GOLD.getDust(16),
							ELEMENT.getInstance().SILVER.getDust(16),
							ELEMENT.getInstance().OSMIUM.getDust(4),
							ELEMENT.getInstance().IRIDIUM.getDust(4),
							ELEMENT.getInstance().CERIUM.getDust(8)
					},
					FluidUtils.getFluidStack("molten.trinium", 136 * 144), 0, 20 * 3000,
					2040*4);
		}
		
		//Eglin Steel
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(6),
						ELEMENT.getInstance().IRON.getDust(4),
						ALLOY.KANTHAL.getDust(1),
						ALLOY.INVAR.getDust(5),
						ELEMENT.getInstance().SULFUR.getDust(1),
						ELEMENT.getInstance().CARBON.getDust(1),
						ELEMENT.getInstance().SILICON.getDust(4)
				},
				ALLOY.EGLIN_STEEL.getFluid(16 * 144),
				0, 
				20 * 45,
				120);
			
		//HG1223
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(5),
						ELEMENT.getInstance().MERCURY.getCell(1),
						ELEMENT.getInstance().BARIUM.getDust(2),
						ELEMENT.getInstance().CALCIUM.getDust(2),
						ELEMENT.getInstance().COPPER.getDust(3),
				},
				ELEMENT.getInstance().OXYGEN.getFluid(8000),
				ALLOY.HG1223.getFluid(16 * 144),
				new ItemStack[] { 
						CI.emptyCells(1)
				},
				100, //Output Chance 
				20 * 120,
				122880);
		
		
		

		// Germanium Roasting
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(15),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedSphalerite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(2000),
				ELEMENT.getInstance().GERMANIUM.getFluid(288),
				0,
				20 * 300,
				4000);

		// Selenium Roasting
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(16),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPyrite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(4000),
				ELEMENT.getInstance().SELENIUM.getFluid(144),
				0,
				20 * 300,
				2000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(17),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedChalcopyrite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(4000),
				ELEMENT.getInstance().SELENIUM.getFluid(144),
				0,
				20 * 300,
				2000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(18),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedGalena", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(4000),
				ELEMENT.getInstance().SELENIUM.getFluid(144),
				0,
				20 * 300,
				2000);
		

		// Ruthenium Roasting
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(19),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIridium", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(2000),
				ELEMENT.getInstance().RUTHENIUM.getFluid(288),
				0,
				20 * 300,
				8000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(19),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedOsmium", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(2000),
				ELEMENT.getInstance().RUTHENIUM.getFluid(288),
				0,
				20 * 300,
				8000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(19),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPlatinum", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(2000),
				ELEMENT.getInstance().RUTHENIUM.getFluid(288),
				0,
				20 * 300,
				8000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(19),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCooperite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(8000),
				ELEMENT.getInstance().RUTHENIUM.getFluid(144),
				0,
				20 * 300,
				8000);
		
		// Rhenium Roasting
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(20),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedScheelite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(10000),
				ELEMENT.getInstance().RHENIUM.getFluid(144),
				0,
				20 * 300,
				4000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(20),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMolybdenite", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(7500),
				ELEMENT.getInstance().RHENIUM.getFluid(144),
				0,
				20 * 300,
				4000);
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(20),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMolybdenum", 8),
						ELEMENT.getInstance().CARBON.getDust(32),
				},
				Materials.SulfuricAcid.getFluid(5000),
				ELEMENT.getInstance().RHENIUM.getFluid(288),
				0,
				20 * 300,
				4000);
		
		//Thallium Roasting
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { 
						ItemUtils.getGregtechCircuit(21),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIron", 12),
						ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPyrite", 16),
						ELEMENT.getInstance().CARBON.getDust(64),
				},
				Materials.SulfuricAcid.getFluid(5000),
				ELEMENT.getInstance().THALLIUM.getFluid(288+144),
				0,
				20 * 300,
				8000);
		
		
		

	}

	private static void fluidcannerRecipes() {
		// Sulfuric Acid
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle),
				ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion), FluidUtils.getFluidStack("sulfuricacid", 250),
				null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion),
				ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("sulfuricacid", 250));

		// Hydrofluoric Acid
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle),
				ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
				FluidUtils.getFluidStack("hydrofluoricacid", 250), null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
				ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("hydrofluoricacid", 250));

		if (Utils.getGregtechVersionAsInt() >= 50929) {
			// Hydrofluoric Acid
			GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle),
					ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
					FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 250), null);
			GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
					ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 250));
		}
	}

	private static void cokeOvenRecipes() {
		Logger.INFO("Loading Recipes for Industrial Coking Oven.");

		// Wood to Charcoal
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 20L), 20,
				GT_ModHandler.getSteam(1000), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 24L),
				FluidUtils.getFluidStack("fluid.coalgas", 1440), 60, 30);

		// Coal to Coke
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), 22,
				GT_ModHandler.getSteam(1000), ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 10),
				FluidUtils.getFluidStack("fluid.coalgas", 2880), 30, 120);

		// Coke & Coal
		CORE.RA.addCokeOvenRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
				ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 6), GT_ModHandler.getSteam(2000),
				FluidUtils.getFluidStack("fluid.coalgas", 5040),
				ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 14), 60 * 20, 240);

	}

	private static void matterFabRecipes() {
		Logger.INFO("Loading Recipes for Matter Fabricator.");

		try {

			CORE.RA.addMatterFabricatorRecipe(Materials.UUAmplifier.getFluid(1L), // Fluid
					// Input
					Materials.UUMatter.getFluid(1L), // Fluid Output
					800, // Time in ticks
					32); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addMatterFabricatorRecipe(null, // Fluid Input
					Materials.UUMatter.getFluid(1L), // Fluid Output
					3200, // Time in ticks
					32); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

	}

	private static void dehydratorRecipes() {
		Logger.INFO("Loading Recipes for Chemical Dehydrator.");

		try {
			// Makes Lithium Carbonate
			CORE.RA.addDehydratorRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricLithium", 1), 
					FluidUtils.getFluidStack("sulfuriclithium", 440), 
					new ItemStack[] {
							CI.emptyCells(1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 3),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 3) 
			}, 
					30 * 20, // Time in ticks
					30); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("[cellSulfuricLithium] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			ItemStack cells = CI.emptyCells(12);

			final ItemStack[] input = { cells, ItemUtils.getItemStackOfAmountFromOreDict("dustLepidolite", 20) };

			CORE.RA.addDehydratorRecipe(input, // Item input (Array, up to 2)
					FluidUtils.getFluidStack("sulfuricacid", 10000), 
					FluidUtils.getFluidStack("sulfuriclithium", 10000),
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4),
							ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 10),
							ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3), // LithiumCarbonate
			}, // Output Array of Items - Upto 9,
					new int[] { 0 },
					75 * 20, // Time in ticks
					1000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustLepidolite] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10)
					}, 
					FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 1440), 
					null, 
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumTetrafluoride", 10),
							CI.emptyCells(10) 
					}, 
					new int[] { 0 }, 150 * 20, // Time in ticks
					2000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustUraniumTetrafluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10)
					}, // Item
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1440), // Fluid
					null, // Fluid output (slot 2)
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumHexafluoride", 10),
							CI.emptyCells(10) }, // Output
					new int[] { 0 }, 300 * 20, // Time in ticks
					4000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustUraniumHexafluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Raisins from Grapes
		try {

			ItemStack cropGrape = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cropGrape", 1);
			ItemStack foodRaisins = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foodRaisins", 1);
			
			if (cropGrape != null && foodRaisins != null)			
			CORE.RA.addDehydratorRecipe(new ItemStack[] { 
					cropGrape
			}, // Item
					null, // Fluid input (slot 1)
					null, // Fluid output (slot 2)
					new ItemStack[] { 
							foodRaisins
			}, // Output
					new int[] { 0 }, 10, // Time in ticks
					2); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[foodRaisins] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Calcium Hydroxide
		if ((ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1)))	|| LoadedMods.IHL) {
			try {
				
				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 10) }, // Item
						FluidUtils.getFluidStack("water", 10000), // Fluid input
						// (slot 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 20) }, // Output
						new int[] { 0 }, 120 * 20, // Time in ticks
						120); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustCalciumHydroxide] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}	

		}

		// Process Waste Water
		try {

			CORE.RA.addDehydratorRecipe(null, 
					FluidUtils.getFluidStack("fluid.sludge", 1000), 
					FluidUtils.getFluidStack("nitricacid", 10), 
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTinyIron", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyCopper", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyTin", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyNickel", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyCobalt", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAluminium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinySilver", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyGold", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTinyIridium", 1) },
					new int[] { 10, 5, 5, 4, 4, 3, 2, 2, 1 },
					2 * 20, 
					500); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[sludge] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}



		// 2 LiOH + CaCO3
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[] {
							ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 5)
					}, // Item
					null, // Fluid input (slot 1)
					null, // Fluid output (slot 2)
					new ItemStack[] {
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 3) 
					}, // Output
					new int[] { 0 }, 120 * 20, // Time in ticks
					1000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustLi2CO3CaOH2] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// LiOH Liquid to Dust
		try {

			CORE.RA.addDehydratorRecipe(new ItemStack[] {
					ItemUtils.getGregtechCircuit(0)
			}, // Item
					FluidUtils.getFluidStack("lithiumhydroxide", 144), // Fluid
					null, // Fluid output (slot 2)
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 1)
			}, // Output
					new int[] { 0 },
					1 * 20, // Time in ticks
					64); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustLithiumHydroxide] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Zirconium Chloride -> TetraFluoride
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 9),
							CI.emptyCells(9)
					}, // Item
					FluidUtils.getFluidStack("hydrofluoricacid", 9 * 144), 
					null, 
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 9),
							FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(9)
							},
					new int[] { 0 }, 120 * 20, // Time in ticks
					500); // EU


			if (Utils.getGregtechVersionAsInt() >= 50929) {
				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 9),
								CI.emptyCells(9) },
						FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 18 * 144),
						null,
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 9),
								FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(9) },						
						new int[] { 0 },
						120 * 20, // Time in ticks
						500); // EU
			}


		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustZrF4] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// CaF2 + H2SO4 â†’ CaSO4(solid) + 2 HF
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[] { 
							ItemUtils.getItemStackOfAmountFromOreDict("dustFluorite", 37),
							CI.emptyCells(16)
					},
					FluidUtils.getFluidStack("sulfuricacid", 56 * 144),
					null, // Fluid output (slot 2)
					new ItemStack[] {
							ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumSulfate", 30),
							ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 16),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 2)
					}, 
					new int[] { 0, 0, 100, 100, 300, 200 },
					10 * 60 * 20, 
					230); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustFluorite] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
		try {
			CORE.RA.addDehydratorRecipe(
					new ItemStack[] {
							ItemUtils.getItemStackOfAmountFromOreDict("cellBerylliumHydroxide", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("cellAmmoniumBifluoride", 4)
					}, 
					null, // Fluid input (slot 1)
					FluidUtils.getFluidStack("ammoniumtetrafluoroberyllate", 6000), 
					new ItemStack[] {
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 4),
							CI.emptyCells(2)
					}, 
					new int[] { 0, 0, 0 }, 
					32 * 20, // Time in ticks
					64); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[ammoniumtetrafluoroberyllate] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// (NH4)2BeF4 → 2 NH3 + 2 HF + BeF2
		try {
			CORE.RA.addDehydratorRecipe(
					new ItemStack[] {
							CI.emptyCells(5) 
					},
					FluidUtils.getFluidStack("ammoniumtetrafluoroberyllate", 5000), 
					null, // Fluid output (slot 2)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("cellBerylliumFluoride", 1) }, 
					new int[] { 0, 0, 0 }, 
					5 * 60 * 20, // Time in ticks
					120); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[cellBerylliumFluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Makes Styrene
		CORE.RA.addDehydratorRecipe(
				CI.emptyCells(3), // Item Input
				FluidUtils.getFluidStack("fluid.ethylbenzene", 1000), // Fluid
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("cellStyrene", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 2) }, // Output		
				3 * 20, // Time in ticks
				30); // EU
		
		/*
		 * Try Add custom Recipe for drying leather
		 */		
		if (LoadedMods.PamsHarvestcraft && Loader.isModLoaded("Backpack")) {			
			ItemStack aLeather1, aLeather2;			
			aLeather1 = ItemUtils.getCorrectStacktype("harvestcraft:hardenedleatherItem", 1);
			aLeather2 = ItemUtils.getCorrectStacktype("Backpack:tannedLeather", 1);			
			CORE.RA.addDehydratorRecipe(
					aLeather1,
					GT_Values.NF,
					new ItemStack[] {
							aLeather2
					},
					5 * 20,
					180);
		}

	}

	private static void largeChemReactorRecipes() {
		//Styrene
		CORE.RA.addMultiblockChemicalRecipe(
				new ItemStack[]{CI.getNumberedCircuit(24)},
				new FluidStack[]{
						FluidUtils.getFluidStack("fluid.ethylbenzene", 1000)
				},
				new FluidStack[]{
						MaterialUtils.getMaterial("Styrene").getFluid(1000),
						Materials.Hydrogen.getGas(2000)
				},
				null,
				30,
				30);
		//Short-cut Styrene
		CORE.RA.addMultiblockChemicalRecipe(
				new ItemStack[]{CI.getNumberedCircuit(24)},
				new FluidStack[]{
						MaterialUtils.getMaterial("Ethylene").getGas(500), 
						MaterialUtils.getMaterial("Benzene").getFluid(500)
				},
				new FluidStack[]{
						MaterialUtils.getMaterial("Styrene").getFluid(500),
						Materials.Hydrogen.getGas(1000)
				},
				null,
				240,
				120);
	}

	private static void fissionFuelRecipes() {
		try {

			final String salt_LiFBeF2ThF4UF4 = "LiFBeF2ThF4UF4".toLowerCase();
			final String salt_LiFBeF2ZrF4U235 = "LiFBeF2ZrF4U235".toLowerCase();
			final String salt_LiFBeF2ZrF4UF4 = "LiFBeF2ZrF4UF4".toLowerCase();

			final FluidStack LithiumFluoride = FluidUtils.getFluidStack("molten.lithiumfluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack BerylliumFluoride = FluidUtils.getFluidStack("molten.berylliumfluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack ThoriumFluoride = FluidUtils.getFluidStack("molten.thoriumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack ZirconiumFluoride = FluidUtils.getFluidStack("zirconiumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack UraniumTetraFluoride = FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack Uranium235 = FluidUtils.getFluidStack("molten.uranium235", 1000); // Re-usable
			// FluidStacks

			final FluidStack LiFBeF2ThF4UF4 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ThF4UF4, 100); // Re-usable
			// FluidStacks
			final FluidStack LiFBeF2ZrF4U235 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ZrF4U235, 100); // Re-usable
			// FluidStacks
			final FluidStack LiFBeF2ZrF4UF4 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ZrF4UF4, 100); // Re-usable
			// FluidStacks

			// 7LiF - BeF2 - ZrF4 - UF4 - 650C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 650), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 250), // Input
					// B
					FluidUtils.getFluidStack(ZirconiumFluoride, 80), // Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 70), // Input
					// D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4UF4, 1000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					60 * 60 * 20, // Duration
					500);

			// 7LiF - BeF2 - ZrF4 - U235 - 590C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 550), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 150), // Input
					// B
					FluidUtils.getFluidStack(ZirconiumFluoride, 60), // Input C
					FluidUtils.getFluidStack(Uranium235, 240), // Input D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4U235, 1000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					45 * 60 * 20, // Duration
					500);

			// 7liF - BeF2 - ThF4 - UF4 - 566C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 620), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 280), // Input
					// B
					FluidUtils.getFluidStack(ThoriumFluoride, 70), // Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 70), // Input
					// D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ThF4UF4, 1000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					60 * 60 * 20, // Duration
					500);

		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
	}

	private static void assemblerRecipes() {
		// ItemUtils.getSimpleStack(GregtechItemList.Casing_Vanadium_Redox.get(1)
		addAR(ItemUtils.getItemStackOfAmountFromOreDict("plateVanadium", 32),
				ItemUtils.getItemStackOfAmountFromOreDict("frameGtVanadiumSteel", 8),
				FluidUtils.getFluidStack("oxygen", 8000),
				ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 0, 4), 16, 60);
		addAR(ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 0, 2),
				ItemUtils.getItemStackOfAmountFromOreDict("plateVanadiumGallium", 8),
				FluidUtils.getFluidStack("molten.tantalum", 144 * 4),
				ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 1, 8), 32, 120);
		addAR(ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 1, 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateDenseLead", 4), FluidUtils.getFluidStack("oxygen", 16000),
				ItemUtils.getSimpleStack(GregtechItemList.Casing_Vanadium_Redox.get(1), 1), 64, 240);

		//Tier 2-6
		ItemStack T1 = GregtechItemList.Casing_Vanadium_Redox.get(1);
		ItemStack T2 = GregtechItemList.Casing_Vanadium_Redox_IV.get(1);
		ItemStack T3 = GregtechItemList.Casing_Vanadium_Redox_LuV.get(1);
		ItemStack T4 = GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1);
		ItemStack T5 = GregtechItemList.Casing_Vanadium_Redox_UV.get(1);
		ItemStack T6 = GregtechItemList.Casing_Vanadium_Redox_MAX.get(1);

		addAR(T1,
				ItemUtils.getItemStackOfAmountFromOreDict("plateDenseTitanium", 4),
				FluidUtils.getFluidStack("nitrogen", 16000),
				T2, 120, 2000);
		addAR(T2,
				ItemUtils.getItemStackOfAmountFromOreDict("plateDenseTungstenSteel", 4),
				FluidUtils.getFluidStack("helium", 8000),
				T3, 250, 8000);
		addAR(T3,
				ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 16),
				FluidUtils.getFluidStack("argon", 4000),
				T4, 500, 32000);
		addAR(T4,
				ItemUtils.getItemStackOfAmountFromOreDict("plateDenseNaquadah", 4),
				FluidUtils.getFluidStack("radon", 4000),
				T5, 1000, 128000);
		addAR(T5,
				ItemUtils.getItemStackOfAmountFromOreDict("plateDenseAmericium", 4),
				FluidUtils.getFluidStack("molten.krypton", 500),
				T6, 2000, 512000);

		/*addAR(ItemUtils.getItemStackOfAmountFromOreDict("plateIncoloy020", 16),
				ItemUtils.getItemStackOfAmountFromOreDict("frameGtIncoloyMA956", 4), null,
				GregtechItemList.Casing_Power_SubStation.get(4), 80, 120);*/
		

		
		
		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				 GregtechItemList.Casing_Multi_Use.get(1),
					ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(6), 1),
					ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(4), 8),
					CI.sensor_HV,
					CI.emitter_HV,
					CI.fieldGenerator_HV,				
				},
				null,
				ItemUtils.getSimpleStack(Dimension_Everglades.blockPortalFrame),
				20*20,
				2048);
		
		
		


		
		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {ItemUtils.getSimpleStack(ModItems.itemRope, 6)}, null, ItemUtils.getSimpleStack(ModBlocks.blockNet, 2), 1*20, 8);
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {ItemUtils.getSimpleStack(CI.explosiveITNT, 2), ItemUtils.getSimpleStack(CI.explosiveTNT, 4), ELEMENT.getInstance().SULFUR.getDust(2), ELEMENT.getInstance().IRON.getFrameBox(1)}, null, ItemUtils.getSimpleStack(ModBlocks.blockMiningExplosive, 3), 5*20, 60);		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {ItemUtils.getSimpleStack(Items.nether_star), ItemUtils.getItemStackOfAmountFromOreDict("plateTungstenSteel", 8), ItemUtils.getItemStackOfAmountFromOreDict("stickBlackSteel", 8)}, null, ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard, 32), 30*20, 500);
			
		

		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				CI.electricPump_LV,
				CI.electricMotor_LV,
				ItemUtils.getItemStackOfAmountFromOreDict("circuitBasic", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("ringBrass", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("stickBrass", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateSteel", 2)				
		}, null, ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 1, 1), 10*20, 30);		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				CI.electricPump_MV,
				CI.electricMotor_MV,
				ItemUtils.getItemStackOfAmountFromOreDict("circuitAdvanced", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("ringInvar", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("stickInvar", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateAluminium", 2)				
		}, null, ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 2, 1), 10*20*2, 120);		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				CI.electricPump_HV,
				CI.electricMotor_HV,
				ItemUtils.getItemStackOfAmountFromOreDict("circuitData", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("ringChrome", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("stickChrome", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateStainlessSteel", 2)				
		}, null, ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 3, 1), 10*20*3, 480);
		
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				CI.electricPump_EV,
				CI.electricMotor_EV,
				ItemUtils.getItemStackOfAmountFromOreDict("circuitElite", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("ringTitanium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("stickTitanium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateTungstenSteel", 2)				
		}, null, ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 4, 1), 10*20*4, 1960);


	}

	private static boolean addAR(final ItemStack inputA, final ItemStack inputB, final FluidStack inputFluidA,
			final ItemStack outputA, final int seconds, final int voltage) {
		// return GT_Values.RA.addAssemblerRecipe(inputA, inputB, outputA,
		// seconds*20, voltage);
		return GT_Values.RA.addAssemblerRecipe(inputA, inputB, inputFluidA, outputA, seconds * 20, voltage);
	}

	private static void distilleryRecipes() {
		Logger.INFO("Registering Distillery/Distillation Tower Recipes.");
		GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]),
				FluidUtils.getFluidStack("air", 1000), FluidUtils.getFluidStack("helium", 1), 400, 30, false);
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("air", 20000),
				FluidUtils.getFluidStackArray("helium", 25), ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob, 1),
				200, 60);

		// Apatite Distillation
		/*
		 * so if you dissolve aparite in sulphuric acid you'll get a mixture of
		 * SO2, H2O, HF and HCl
		 */
		final FluidStack[] apatiteOutput = { FluidUtils.getFluidStack("sulfurousacid", 3800),
				FluidUtils.getFluidStack("hydrogenchloride", 1000), FluidUtils.getFluidStack("hydrofluoricacid", 400) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfuricapatite", 5200), apatiteOutput, null,
				45 * 20, 256);

		final FluidStack[] sulfurousacidOutput = { FluidUtils.getFluidStack("sulfurdioxide", 500),
				FluidUtils.getFluidStack("water", 500) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfurousacid", 1000), sulfurousacidOutput,
				null, 10 * 20, 60);

		final FluidStack[] sulfurdioxideOutput = { FluidUtils.getFluidStack("oxygen", 144 * 2) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfurdioxide", 144 * 3), sulfurdioxideOutput,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), 5 * 20, 30);
	}

	private static void addFuels() {
		Logger.INFO("Registering New Fuels.");

		if (!GTNH) {
			GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketFire_water", 0, 1), null, 120, 0);
			GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketRocket_fuel", 0, 1), null, 112, 0);
			GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketHootch", 0, 1), null, 36, 0);
		}

		HotFuel.addNewHotFuel(GT_ModHandler.getLava(83), GT_Values.NF,
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("nuggetCopper", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetGold", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetSilver", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetTantalum", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTungstate", 1),
						ItemUtils.getSimpleStack(Blocks.obsidian) },
				new int[] { 2000, 1000, 250, 250, 250, 250, 500 }, 0);

		HotFuel.addNewHotFuel(FluidUtils.getFluidStack("ic2pahoehoelava", 83), GT_Values.NF,
				new ItemStack[] { 
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetBronze", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetElectrum", 1),
						ItemUtils.getSimpleStack(Blocks.obsidian) },
				new int[] { 750, 250, 1850 }, 0);

		/*
		 * HotFuel.addNewHotFuel( FluidUtils.getFluidStack("ic2hotcoolant",
		 * 100), GT_Values.NF, new ItemStack[]{}, new int[]{}, 0);
		 */

		ThermalFuel.addSteamTurbineFuel(FluidUtils.getFluidStack("steam", 1024));

		// CORE.RA.addFuel(UtilsItems.simpleMetaStack("EnderIO:bucketRocket_fuel",
		// 0, 1), null, 112, 0);
		GT_Values.RA.addFuel(ItemUtils.getSimpleStack(Items.lava_bucket), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(2), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(11), null, 24, 2);
		// System.exit(1);
	}

	private static void extractorRecipes() {
		Logger.INFO("Registering Extractor Recipes.");
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Sodium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Cadmium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Lithium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
	}

	private static void fluidExtractorRecipes() {		
		//FLiBe fuel
		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLi2BeF4", 1), null,
				FluidUtils.getFluidStack("li2bef4", 144), 10000, 100, 500);
		//LFTR Fuel 1
		GT_Values.RA.addFluidExtractionRecipe(NUCLIDE.LiFBeF2ZrF4U235.getDust(1), null,
				NUCLIDE.LiFBeF2ZrF4U235.getFluid(144), 10000, 250, 1000);
		GT_Values.RA.addFluidExtractionRecipe(NUCLIDE.LiFBeF2ZrF4UF4.getDust(1), null,
				NUCLIDE.LiFBeF2ZrF4UF4.getFluid(144), 10000, 150, 2000);
		GT_Values.RA.addFluidExtractionRecipe(NUCLIDE.LiFBeF2ThF4UF4.getDust(1), null,
				NUCLIDE.LiFBeF2ThF4UF4.getFluid(144), 10000, 200, 1500);
		
		//ZIRCONIUM_TETRAFLUORIDE
		GT_Values.RA.addFluidExtractionRecipe(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getDust(1), null,
				FluidUtils.getFluidStack(ModItems.fluidZrF4, 144), 10000, 200, 512+256);
		
		
		
/*		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLiBeF2ZrF4U235", 1), null,
				FluidUtils.getFluidStack("molten.libef2zrf4u235", 144), 10000, 250, 1000);
		//LFTR Fuel 2
		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLiBeF2ZrF4UF4", 1), null,
				FluidUtils.getFluidStack("molten.libef2zrf4uf4", 144), 10000, 150, 2000);
		//LFTR Fuel 2
		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLiBeF2ThF4UF4", 1), null,
				FluidUtils.getFluidStack("molten.libef2thf4uf4", 144), 10000, 200, 1500);*/
	}

	private static void chemicalBathRecipes() {
		int[] chances = {9000, 6000, 3000};
		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 12),
				FluidUtils.getFluidStack("chlorine", 2400),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 5), chances, 30 * 20, 480);
		chances = new int[]{9000, 3000, 1000};
		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustRutile", 5),
				FluidUtils.getFluidStack("chlorine", 4000),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTitanium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1),
				chances, 30 * 20, 1024);		

		GT_Values.RA.addChemicalBathRecipe(FLUORIDES.FLUORITE.getCrushed(2), FluidUtils.getFluidStack("hydrogen", 2000),
				FLUORIDES.FLUORITE.getCrushedPurified(8), FLUORIDES.FLUORITE.getDustImpure(4),
				FLUORIDES.FLUORITE.getDustPurified(2), new int[] { 10000, 5000, 1000 }, 30 * 20, 240);

		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 10),
				FluidUtils.getFluidStack("hydrofluoricacid", 10 * 144),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 10), null, null, new int[] {}, 90 * 20,
				500);

	}

	private static void centrifugeRecipes() {
		GT_Values.RA.addCentrifugeRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustThorium", 8), GT_Values.NI,
				GT_Values.NF, GT_Values.NF, ELEMENT.getInstance().THORIUM232.getDust(2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallThorium", 20),
				ELEMENT.getInstance().URANIUM232.getDust(1), GT_Values.NI, GT_Values.NI, GT_Values.NI,
				new int[] { 0, 0, 10 }, 500 * 20, 2000);

	}

	private static void mixerRecipes() {
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), null, null, null,
				FluidUtils.getFluidStack("oxygen", 288), FluidUtils.getFluidStack("sulfurdioxide", 432), null, 600, 60);
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustUranium233", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1), null, null,
				FluidUtils.getFluidStack("hydrofluoricacid", 144 * 5),
				FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 144 * 5), null, 3000, 500);
		/*GT_Values.RA.addMixerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellMercury", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBarium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalcium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 3),
				null,
				ALLOY.HG1223.getFluid(144*16), 
				CI.emptyCells(1),
				30 * 20,
				500);*/
	}

	private static void chemicalReactorRecipes() {
		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 5), // Input
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5), // Input
				null, // Fluid Input
				null, // Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 10), // Output
				// Stack
				600 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 5), // Input
				null, // Input Stack 2
				FluidUtils.getFluidStack("hydrofluoricacid", 5 * 144), // Fluid
				// Input
				FluidUtils.getFluidStack("water", 5 * 144), // Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 5), // Output
				// Stack
				600 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustApatite", 16), null,
				FluidUtils.getFluidStack("sulfuricacid", 144 * 32),
				FluidUtils.getFluidStack("sulfuricapatite", 144 * 8),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallSulfur", 1), 20 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1), null,
				FluidUtils.getFluidStack("sulfuricacid", 144 * 8), FluidUtils.getFluidStack("sulfuriclithium", 144 * 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium7", 1), 20 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 16), FluidUtils.getFluidStack("water", 1000),
				FluidUtils.getFluidStack("lithiumhydroxide", 144 * 4),
				CI.emptyCells(1), 300 * 20);

		// LFTR Fuel Related Compounds
		if (GTNH) {
			// Hydroxide
			AddGregtechRecipe.addChemicalRecipeForBasicMachineOnly(
					ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("hydroxide", 2000),
					CI.emptyCells(2), GT_Values.NI, 8 * 20, 30);
			// Beryllium Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustBeryllium", 7),
					ItemUtils.getGregtechCircuit(3), FluidUtils.getFluidStack("hydroxide", 1000),
					FluidUtils.getFluidStack("berylliumhydroxide", 2000), GT_Values.NI, 8 * 20);
			// Ammonium Bifluoride
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
					ItemUtils.getGregtechCircuit(3), FluidUtils.getFluidStack("ammonium", 1000),
					FluidUtils.getFluidStack("ammoniumbifluoride", 2000),
					CI.emptyCells(1), 26 * 20);
			// Ammonium
			AddGregtechRecipe.addChemicalRecipeForBasicMachineOnly(
					ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("ammonium", 2000),
					CI.emptyCells(2), GT_Values.NI, 20 * 20, 30);
		}

		if (!GTNH) {
			// Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("hydroxide", 2000),
					CI.emptyCells(2), 8 * 20);
			// Ammonia (moved to GTNH core mod)
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 3),
					ItemUtils.getItemStackOfAmountFromOreDict("dustMagnetite", 0),
					FluidUtils.getFluidStack("nitrogen", 1000), FluidUtils.getFluidStack("ammonia", 1000),
					CI.emptyCells(3), 14 * 20);
			// Beryllium Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustBeryllium", 7), GT_Values.NI,
					FluidUtils.getFluidStack("hydroxide", 1000), FluidUtils.getFluidStack("berylliumhydroxide", 2000),
					GT_Values.NI, 8 * 20);
			// Ammonium Bifluoride
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
					GT_Values.NI, FluidUtils.getFluidStack("ammonium", 1000),
					FluidUtils.getFluidStack("ammoniumbifluoride", 2000),
					CI.emptyCells(1), 26 * 20);
			// Ammonium
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("ammonium", 2000),
					CI.emptyCells(2), 20 * 20);
		}

		ItemStack temp_GT5u_SA = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellHydrofluoricAcid_GT5U", 5);
		if (temp_GT5u_SA != null) {
			//Refine GT HF into GT++ HF
			GT_Values.RA.addChemicalRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2),
					temp_GT5u_SA,
					null, // Fluid Input
					FluidUtils.getFluidStack("hydrofluoricacid", 6000), // Fluid Output
					CI.emptyCells(7),
					2 * 20);
		}
		
		
		//Technetium
		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustTechnetium99", 1), null,
				FluidUtils.getFluidStack("sulfuricacid", 1000), FluidUtils.getFluidStack("sulfuricacid", 144 * 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTechnetium", 1), 100 * 20);
		


	}

	private static void blastFurnaceRecipes() {

	    //public boolean addBlastRecipe(
		//ItemStack aInput1, ItemStack aInput2, 
		//FluidStack aFluidInput, FluidStack aFluidOutput, 
		//ItemStack aOutput1, ItemStack aOutput2,
		//int aDuration, int aEUt, int aLevel)		
		
		GT_Values.RA.addBlastRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBerylliumFluoride", 1), GT_Values.NF, GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2BeF4", 3), null, 60 * 20, 2000, 3000);
		GT_Values.RA.addBlastRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1), null, GT_Values.NF,
				GT_Values.NF, ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1), null, 60 * 20, 340, 300);
		
		
		//Synthetic Graphite
		GT_Values.RA.addBlastRecipe(
				CI.getNumberedCircuit(22),
				ALLOY.SILICON_CARBIDE.getDust(16),
				ELEMENT.getInstance().NITROGEN.getFluid(4000),
				GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustGraphite", 8),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallSilicon", 8),
				60 * 20,
				MaterialUtils.getVoltageForTier(GTNH ? 5 : 4),
				4500);
		
		
	}

	private static void autoclaveRecipes() {
		GT_Values.RA.addAutoclaveRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 9),
				FluidUtils.getFluidStack("chlorine", 9 * 4 * 144),
				ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 9), 0, 120 * 20, 30);
	}

	private static void benderRecipes() {

		if (CORE.ConfigSwitches.enableMultiblock_PowerSubstation) {
			GT_Values.RA.addBenderRecipe(ItemUtils.getItemStackOfAmountFromOreDict("ingotVanadium", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("plateVanadium", 1), 8, 16);
		}
	}

	private static void compressorRecipes() {
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallClay", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("plateClay", 1));
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallMeatRaw", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("plateMeatRaw", 1));
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 9),
				ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1));
		
		CORE.RA.addCompressorRecipe(ItemList.FusionComputer_UV.get(9), GregtechItemList.Compressed_Fusion_Reactor.get(1), (int) GT_Values.V[7], (int) GT_Values.V[8]);
	}

	private static void macerationRecipes() {
		GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1));

		GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 9));
		/*
		 * GT_ModHandler.addPulverisationRecipe( FLUORIDES.FLUORITE.getOre(1),
		 * FLUORIDES.FLUORITE.getDust(4));
		 */

		if (ItemUtils.simpleMetaStack("chisel:limestone", 0, 1) != null) {
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("limestone", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("dustCalcite", 4));
		}

	}

	public static boolean addPulverisationRecipe(final ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
			final ItemStack aOutput3) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if ((GT_Utility.isStackInvalid(aInput)) || (GT_Utility.isStackInvalid(aOutput1))) {
			return false;
		}
		if (GT_Utility.getContainerItem(aInput, false) == null) {

			if (GregTech_API.sRecipeFile.get(ConfigCategories.Machines.maceration, aInput, true)) {
				GT_Utility.addSimpleIC2MachineRecipe(aInput, GT_ModHandler.getMaceratorRecipeList(), null,
						new Object[] { aOutput1 });
			}
			GT_Values.RA.addPulveriserRecipe(aInput, new ItemStack[] { aOutput1, aOutput2, aOutput3 },
					new int[] { 10000, 10000, 10000 }, 400, 2);
		}
		return true;
	}

	private static void cyclotronRecipes() {

		// Polonium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.bismuth", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_PO210.get(1) }, null, new int[] { 100 }, 20 * 300 * 100, 2040 * 4,
				500 * 20);

		// Americium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.americium", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_AM241.get(4) }, null, new int[] { 2500 }, 20 * 300 * 100, 1020 * 4,
				500 * 20); //PO Special Value

		// Strontium u235
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.uranium235", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 570 }, 20 * 300 * 100, 1020 * 4,
				500 * 20); //PO Special Value

		// Strontium u233
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.uranium233", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 660 }, 20 * 300 * 100, 1020 * 4,
				500 * 20); //PO Special Value

		// Strontium pu239
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.plutonium239", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 220 }, 20 * 300 * 100, 1020 * 4,
				500 * 20); //PO Special Value

		// Plutonium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.plutonium238", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_PU238.get(2) }, null, new int[] { 780 }, 20 * 300 * 100, 1020 * 4,
				500 * 20); //PO Special Value

		// Neptunium
		CORE.RA.addCyclotronRecipe(new ItemStack[] {ELEMENT.getInstance().URANIUM238.getDust(1) }, FluidUtils.getFluidStack("deuterium", 400),
				new ItemStack[] {ItemUtils.getSimpleStack(ModItems.dustNeptunium238)}, null, new int[] { 500 }, 20 * 5, 500 * 4,
				500 * 20); //PO Special Value
		
		
		/**
		 * Particle Science
		 */

		
		// Quark Smash
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(3),
				FluidUtils.getFluidStack("plasma.hydrogen", 100),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.UP),
						Particle.getBaseParticle(Particle.DOWN),
						Particle.getBaseParticle(Particle.CHARM),
						Particle.getBaseParticle(Particle.STRANGE),
						Particle.getBaseParticle(Particle.TOP),
						Particle.getBaseParticle(Particle.BOTTOM),
						}, 
				null,
				new int[] { 50, 50, 50, 50, 50, 50 },
				20 * 300 * 9,
				(int) GT_Values.V[7],
				750 * 20);

		// Lepton Smash
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(6),
				FluidUtils.getFluidStack("plasma.helium", 1500),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.ELECTRON),
						Particle.getBaseParticle(Particle.MUON),
						Particle.getBaseParticle(Particle.TAU),
						Particle.getBaseParticle(Particle.ELECTRON_NEUTRINO),
						Particle.getBaseParticle(Particle.MUON_NEUTRINO),
						Particle.getBaseParticle(Particle.TAU_NEUTRINO),
						}, 
				null,
				new int[] { 600, 40, 20, 15, 10, 5 },
				20 * 300 * 8,
				(int) GT_Values.V[7],
				750 * 20);

		// Boson Smash
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(9),
				FluidUtils.getFluidStack("plasma.helium", 1500),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.GLUON),
						Particle.getBaseParticle(Particle.PHOTON),
						Particle.getBaseParticle(Particle.Z_BOSON),
						Particle.getBaseParticle(Particle.W_BOSON),
						Particle.getBaseParticle(Particle.HIGGS_BOSON),
						}, 
				null,
				new int[] { 160, 260, 150, 150, 1 },
				20 * 300 * 6,
				(int) GT_Values.V[7],
				750 * 20);
		
		
		// Mixed Smash 1
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(12),
				FluidUtils.getFluidStack("plasma.beryllium", 2500),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.GRAVITON),
						Particle.getBaseParticle(Particle.ETA_MESON),
						Particle.getBaseParticle(Particle.PION),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.NEUTRON),
						Particle.getBaseParticle(Particle.LAMBDA),
						Particle.getBaseParticle(Particle.OMEGA),
						Particle.getBaseParticle(Particle.HIGGS_BOSON),
						}, 
				null,
				new int[] { 10, 20, 20, 10, 10, 5, 5, 2 },
				17 * 247 * 32,
				(int) GT_Values.V[8],
				750 * 20);
		
		// Graviton Smash
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(15),
				FluidUtils.getFluidStack("plasma.hydrogen", GTNH ? 50 : 10),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.GRAVITON),
						Particle.getBaseParticle(Particle.UNKNOWN)
						}, 
				null,
				new int[] {15, 100},
				20 * (GTNH ? 90 : 30),
				(int) GT_Values.V[6],
				1000 * 20);		
		
		FluidStack aPlasma = Materials.Duranium.getMolten(GTNH ? 40 : 10);
		FluidStack aPlasma_NULL = Materials._NULL.getPlasma(1);
		
		if (aPlasma == null || aPlasma.isFluidEqual(aPlasma_NULL)) {
			aPlasma = Materials.Americium.getMolten(GTNH ? 20 : 5);
		}
		
		// Quantum Anomaly
		CORE.RA.addCyclotronRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(24),
						Particle.getBaseParticle(Particle.UNKNOWN),
				},
				aPlasma,
				new ItemStack[] {
						GregtechItemList.Laser_Lens_Special.get(1)
						}, 
				null,
				new int[] {100},
				20 * (GTNH ? 300 : 60),
				(int) GT_Values.V[6],
				1000 * 20);
		
		/*
		 * Ions
		 */
		
		int IonCount = 2;
		int tenCountA = (GTNH ? 2 : 1);
		int tenCountB = 0;
		for (String y : IonParticles.MetaToNameMap.values()) {			
			if (y.toLowerCase().contains("hydrogen")) {
				continue;
			}			
			FluidStack aPlasma2 = FluidUtils.getFluidStack("plasma."+y.toLowerCase(), 2);
			Materials aTestMat = MaterialUtils.getMaterial(y);
			FluidStack aPlasma3 = aTestMat != null ? aTestMat.getPlasma(2) : aPlasma2;			
			
			// Ionize Plasma
			if ((aPlasma2 != null && !aPlasma2.isFluidEqual(aPlasma_NULL)) || (aPlasma3 != null && !aPlasma3.isFluidEqual(aPlasma_NULL))) {			
			CORE.RA.addCyclotronRecipe(
					CI.getNumberedCircuit(1+(tenCountA-1)),
					aPlasma2 != null ? aPlasma2 : aPlasma3,
					new ItemStack[] {
							Particle.getIon(y, 1),
							Particle.getIon(y, 2),
							Particle.getIon(y, 3),
							Particle.getIon(y, -1),
							Particle.getIon(y, -2),
							Particle.getIon(y, -3),
							Particle.getIon(y, 1),
							Particle.getIon(y, 2),
							Particle.getIon(y, -1),
							}, 
					null,
					new int[] { 275, 250, 225, 275, 250, 225, 275, 250, 275},
					20 * 20 * (IonCount++) * tenCountA,
					(int) GT_Values.V[7],
					1500 * 20 * tenCountA);
			}
			else {
				Logger.INFO("Plasma for "+y+" does not exist, please report this to Alkalus.");
			}
			
			if (tenCountB == 12) {
				tenCountB = 0;
				tenCountA++;
			}
			else {
				tenCountB++;
			}
		}
		
		// Generate Hydrogen Ion Recipe
		CORE.RA.addCyclotronRecipe(
				CI.getNumberedCircuit(24),
				FluidUtils.getWildcardFluidStack("hydrogen", 1000),
				new ItemStack[] {
						Particle.getIon("Hydrogen", 1),
						Particle.getIon("Hydrogen", 2),
						Particle.getIon("Hydrogen", 3),
						Particle.getIon("Hydrogen", 1),
						Particle.getIon("Hydrogen", 2),
						Particle.getIon("Hydrogen", 3),
						Particle.getIon("Hydrogen", -1),
						Particle.getIon("Hydrogen", -2),
						Particle.getIon("Hydrogen", -3)
						}, 
				null,
				new int[] { 125, 125, 125, 125, 125, 125, 125, 125, 125 },
				20 * 20,
				(int) GT_Values.V[6],
				15000);
		
		// Generate Hydrogen Plasma Recipe
		CORE.RA.addCyclotronRecipe(
				new ItemStack[] {
						Particle.getIon("Hydrogen", 0),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1)
						},
				null,
				new ItemStack[] {
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.NEUTRON),
						Particle.getBaseParticle(Particle.ELECTRON),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						CI.emptyCells(1)
						}, 
				FluidUtils.getFluidStack("plasma.hydrogen", 1),
				new int[] { 250, 250, 250, 500, 500, 500, 10000 },
				20 * 60 * 2,
				(int) GT_Values.V[7],
				750 * 20);

		
		// Generate Protons Easily
		CORE.RA.addCyclotronRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(20),
						Particle.getIon("Hydrogen", 0)
						},
				FluidUtils.getWildcardFluidStack("hydrogen", 100),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						}, 
				null,
				new int[] { 750, 750, 750, 750, 750, 750,  750, 750, 750 },
				20 * 20,
				(int) GT_Values.V[6],
				15000);
		
		CORE.RA.addCyclotronRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(22),
						Particle.getBaseParticle(Particle.UNKNOWN),
						},
				FluidUtils.getWildcardFluidStack("hydrogen", 100),
				new ItemStack[] {
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						Particle.getBaseParticle(Particle.PROTON),
						}, 
				null,
				new int[] { 375, 375, 375, 375, 375, 375, 375, 375, 375  },
				20 * 20,
				(int) GT_Values.V[6],
				15000);
		
		
		//Create Strange Dust		
		CORE.RA.addCyclotronRecipe(
				new ItemStack[] {
						ELEMENT.getInstance().PLUTONIUM238.getDust(1),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						Particle.getBaseParticle(Particle.UNKNOWN),
						},
				FluidUtils.getFluidStack(FluidUtils.getWildcardFluidStack("ender", 1000), 1000),
				new ItemStack[] {
						ORES.DEEP_EARTH_REACTOR_FUEL_DEPOSIT.getDust(1)
						}, 
				null,
				new int[] { 2500 },
				20 * 60 * 15,
				(int) GT_Values.V[7],
				15000);
		
		
		
		

	}

	private static void sifterRecipes() {		

		// Zirconium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIlmenite", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyWroughtIron", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustHafnium", 1) },
				new int[] { 5000, 2500, 1000, 1000, 300, 300 }, 20 * 30, 500);
		
		// Zirconium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedTin", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZinc", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
				new int[] { 10000, 5000, 1500, 1000, 500, 500 }, 20 * 30, 500);

		// Zirconium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
				new int[] { 10000, 5000, 1500, 1000, 500, 500 }, 20 * 30, 500);

		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUranium", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLead", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 1000, 500, 500, 500 }, 20 * 30, 500);
		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUraninite", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUraninite", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyUranium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 500, 250, 250, 250 }, 20 * 30, 500);
		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPitchblende", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPitchblende", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLead", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 500, 250, 250, 250 }, 20 * 30, 500);
	}

	private static void electroMagneticSeperatorRecipes() {
		// Zirconium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedBauxite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBauxite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallRutile", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("nuggetZirconium", 1), new int[] { 10000, 2500, 4000 },
				20 * 20, 24);
		// Zircon
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedMagnetite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustMagnetite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1), new int[] { 10000, 1250, 2500 },
				20 * 20, 24);
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallZircon", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZircon", 1), new int[] { 10000, 1250, 2500 },
				20 * 20, 24);
		
		

		if (!GTNH) {		
		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedNaquadah", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustNaquadah", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallNaquadahEnriched", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 2500, 5000 },
				20 * 20, 24);

		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIridium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustIridium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallOsmium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 2500, 5000 },
				20 * 20, 24);

		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedWulfenite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustWulfenite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 3000, 3000 },
				20 * 20, 24);		
		}
		
		
		
		
	}

	private static void advancedMixerRecipes() {
		// HgBa2Ca2Cu3O8
		/*CORE.RA.addMixerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellMercury", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBarium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalcium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 3),
				FluidUtils.getFluidStack("oxygen", 8000),
				null, CI.emptyCells(1), ALLOY.HG1223.getDust(16), null, null,
				30 * 20, 500);	*/

	}

}
