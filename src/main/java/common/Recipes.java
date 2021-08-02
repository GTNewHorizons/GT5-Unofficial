package common;

import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import kekztech.Items;
import kekztech.KekzCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import util.Util;

import java.util.HashMap;

public class Recipes {

	public static final HashMap<String, InfusionRecipe> infusionRecipes = new HashMap<>();
	
	public static void postInit() {
		KekzCore.LOGGER.info("Registering recipes...");
		
		registerRecipes_TFFT();
		registerRecipes_SOFC();
		//registerRecipes_Nuclear();
		registerRecipes_Jars();
		registerRecipes_LSC();
		//registerRecipes_SpaceElevator();
		registerRecipes_Cosmetics();

		KekzCore.LOGGER.info("Finished registering recipes");
	}

	private static void registerRecipes_TFFT() {
		
		// Controller
		final Object[] tfft_recipe = {
				"HFH", "PVP", "CFC",
				'H', OrePrefixes.pipeMedium.get(Materials.StainlessSteel),
				'F', ItemList.Field_Generator_MV.get(1L),
				'P', ItemList.Electric_Pump_HV.get(1L),
				'V', OrePrefixes.rotor.get(Materials.VibrantAlloy),
				'C', OrePrefixes.circuit.get(Materials.Data)
		};
		GT_ModHandler.addCraftingRecipe(TileEntities.fms.getStackForm(1), tfft_recipe);
		
		// Blocks
		final ItemStack[] tfftcasing = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DarkSteel, 3),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderPearl, 3),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
			};
		GT_Values.RA.addAssemblerRecipe(
				tfftcasing,
				FluidRegistry.getFluidStack("molten.polytetrafluoroethylene", 144),
				new ItemStack(Blocks.tfftCasing, 1),
				200, 256);
		final ItemStack[] tfftstoragefield1 = {
			GT_Utility.getIntegratedCircuit(6),
			GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1),
			GT_OreDictUnificator.get(OrePrefixes.plate, Materials.PulsatingIron, 1),
			GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Steel, 1),
			ItemList.Electric_Pump_LV.get(1L)
		};
		GT_Values.RA.addAssemblerRecipe(
				tfftstoragefield1,
				FluidRegistry.getFluidStack("molten.glass", 144),
				new ItemStack(Blocks.tfftStorageField1, 1),
				200, 256);
		final ItemStack[] tfftstoragefield2 = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.PulsatingIron, 4),
				GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1),
				ItemList.Electric_Pump_MV.get(1L)
			};
		GT_Values.RA.addAssemblerRecipe(
				tfftstoragefield2,
				FluidRegistry.getFluidStack("molten.plastic", 576),
				new ItemStack(Blocks.tfftStorageField2, 1),
				200, 480);
		final ItemStack[] tfftstoragefield3 = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 2),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VibrantAlloy, 2),
				GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 1),
				ItemList.Field_Generator_MV.get(1L),
				ItemList.Electric_Pump_HV.get(2L)
			};
		GT_Values.RA.addAssemblerRecipe(
				tfftstoragefield3,
				FluidRegistry.getFluidStack("molten.epoxid", 576),
				new ItemStack(Blocks.tfftStorageField3, 1),
				300, 1920);
		final ItemStack[] tfftstoragefield4 = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 4),
				GT_OreDictUnificator.get(OrePrefixes.plateTriple, Materials.NiobiumTitanium, 1),
				GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.TungstenSteel, 1),
				ItemList.Field_Generator_HV.get(1L),
				ItemList.Electric_Pump_EV.get(1L)
			};
		GT_Values.RA.addAssemblerRecipe(
				tfftstoragefield4,
				FluidRegistry.getFluidStack("molten.epoxid", 1152),
				new ItemStack(Blocks.tfftStorageField4, 1),
				400, 4098);
		final ItemStack[] tfftstoragefield5 = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 4),
				GT_OreDictUnificator.get(OrePrefixes.plateTriple, Materials.HSSS, 1),
				GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Enderium, 1),
				ItemList.Field_Generator_EV.get(1L),
				ItemList.Electric_Pump_IV.get(1L)
			};
		GT_Values.RA.addAssemblerRecipe(
				tfftstoragefield5,
				FluidRegistry.getFluidStack("molten.epoxid", 1152),
				new ItemStack(Blocks.tfftStorageField5, 1),
				400, 6147);
		// Multi Hatch
		final Object[] multi_hatch_HV = {
				"PRP", "UFU", "PRP",
				'P', GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
				'U', ItemList.Electric_Pump_HV.get(1L),
				'F', ItemList.Field_Generator_LV.get(1L)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.tfftMultiHatch), multi_hatch_HV);
		final Object[] multi_hatch_IV = {
				"PRP", "UFU", "PRP",
				'P', GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1),
				'U', ItemList.Electric_Pump_IV.get(1L),
				'F', ItemList.Field_Generator_HV.get(1L)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.tfftMultiHatch), multi_hatch_IV);
		final Object[] multi_hatch_ZPM = {
				"PRP", "UFU", "PRP",
				'P', GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NaquadahAlloy, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 1),
				'U', ItemList.Electric_Pump_ZPM.get(1L),
				'F', ItemList.Field_Generator_IV.get(1L)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.tfftMultiHatch), multi_hatch_ZPM);

		// Conversion recipe from deprecated hatch to new one (old hatch is equal to new IV hatch)
		GT_ModHandler.addShapelessCraftingRecipe(TileEntities.mhIV.getStackForm(1),
				new ItemStack[]{new ItemStack(Blocks.tfftMultiHatch, 1)});
	}
	
	private static void registerRecipes_SOFC() {
		
		final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();
		
		// Controller
		final Object[] mk1_recipe = {
				"CCC", "PHP", "FBL",
				'C', OrePrefixes.circuit.get(Materials.Advanced),
				'P', ItemList.Electric_Pump_HV.get(1L),
				'H', ItemList.Hull_HV.get(1L),
				'F', GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 1),
				'B', GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 1),
				'L', GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1)
		};		
		GT_ModHandler.addCraftingRecipe(TileEntities.sofc1.getStackForm(1), mk1_recipe);
		final Object[] mk2_recipe = {
				"CCC", "PHP", "FBL",
				'C', OrePrefixes.circuit.get(Materials.Master),
				'P', ItemList.Electric_Pump_IV.get(1L),
				'H', ItemList.Hull_IV.get(1L),
				'F', GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1),
				'B', Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1),
				'L', GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1)
		};
		GT_ModHandler.addCraftingRecipe(TileEntities.sofc2.getStackForm(1), mk2_recipe);
		
		// Blocks
		final ItemStack[] yszUnit = {
				GT_Utility.getIntegratedCircuit(6),
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 4),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Yttrium, 1),
				GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
				ItemList.Electric_Motor_HV.get(1L),
		};
		GT_Values.RA.addAssemblerRecipe(
				yszUnit, 
				Materials.Hydrogen.getGas(4000), 
				new ItemStack(Blocks.yszUnit, 1), 
				1200, 480);
		final ItemStack[] gdcUnit = {
				GT_Utility.getIntegratedCircuit(6),
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 8),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
				GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Desh, new ItemStack(ErrorItem.getInstance(), 1), 1),
				ItemList.Electric_Motor_IV.get(1L),
		};
		GT_Values.RA.addAssemblerRecipe(
				gdcUnit, 
				Materials.Hydrogen.getGas(16000), 
				new ItemStack(Blocks.gdcUnit, 1), 
				2400, 1920);
		
		// Items
		GT_Values.RA.addAlloySmelterRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), Loader.isModLoaded("bartworks") ? 3 : 10), 
				ItemList.Shape_Mold_Plate.get(0),
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 1), 
				400, 480);
		GT_Values.RA.addFormingPressRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10), 
				ItemList.Shape_Mold_Plate.get(0),
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1), 
				800, 480);
		
		if (!Loader.isModLoaded("bartworks")) {
			GT_Values.RA.addChemicalRecipe(
					Materials.Yttrium.getDust(1), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(3000),
					null, craftingItem.getStackOfAmountFromDamage(Items.YttriaDust.getMetaID(), 1), null,
					400, 30);
			GT_Values.RA.addChemicalRecipe(
					Util.getStackofAmountFromOreDict("dustZirconium", 1), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(2000),
					null, craftingItem.getStackOfAmountFromDamage(Items.ZirconiaDust.getMetaID(), 1), null,
					400, 30);
		}
		
		GT_Values.RA.addChemicalRecipe(
				Materials.Cerium.getDust(2), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(3000),
				null, craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 2), null, 
				400, 30);
		GT_Values.RA.addMixerRecipe(
				Items.YttriaDust.getOreDictedItemStack(1),
				Items.ZirconiaDust.getOreDictedItemStack(5),
				GT_Utility.getIntegratedCircuit(6), null, null, null,
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6), 
				400, 96);
		GT_Values.RA.addMixerRecipe(
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
				craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9),
				GT_Utility.getIntegratedCircuit(6), null, null, null,
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10), 
				400, 1920);
	}
	
	/*private static void registerRecipes_Nuclear() {
		
		final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();
		
		// Controller
		
		// Blocks
		final ItemStack[] controlrod = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Lead, 1),
				GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 4),
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64)
			};
		GT_Values.RA.addAssemblerRecipe(
				controlrod, 
				null,
				new ItemStack(Blocks.reactorControlRod, 1), 
				800, 480);
		final ItemStack[] reactorchamber = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Lead, 1),
				GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Lead, 9),
				GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 18),
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 2),
			};
		GT_Values.RA.addAssemblerRecipe(
				reactorchamber, 
				FluidRegistry.getFluidStack("wet.concrete", 144),
				new ItemStack(Blocks.reactorChamberOFF, 1), 
				1600, 480);
		
		// Items
		GT_Values.RA.addMixerRecipe(Materials.Boron.getDust(1),	Materials.Arsenic.getDust(1), GT_Utility.getIntegratedCircuit(6), null, 
				null, null, craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideDust.getMetaID(), 2), 
				100, 1920);
		GT_Values.RA.addChemicalRecipe(
				Materials.Ammonia.getCells(2),
				Materials.CarbonDioxide.getCells(1),
				null,
				null,
				craftingItem.getStackOfAmountFromDamage(Items.AmineCarbamiteDust.getMetaID(), 1),
				Util.getStackofAmountFromOreDict("cellEmpty", 3), 
				400, 30);
		GT_Values.RA.addChemicalRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.AmineCarbamiteDust.getMetaID(), 1),
				Materials.Diamond.getDust(16),
				Materials.CarbonDioxide.getGas(1000),
				null,
				craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondDust.getMetaID(), 1),
				null, 1200, 480);
		
		GT_Values.RA.addAutoclaveRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondDust.getMetaID(), 4), 
				Materials.CarbonDioxide.getGas(16000), 
				craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondCrystal.getMetaID(), 1), 10000, 2400, 7680);
		GT_Values.RA.addAutoclaveRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideDust.getMetaID(), 4), 
				Materials.Nitrogen.getGas(4000), 
				craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideCrystal.getMetaID(), 1), 10000, 2400, 1920);
		
		GT_Values.RA.addLatheRecipe(
				GT_OreDictUnificator.get(OrePrefixes.stick, Materials.AnnealedCopper, 1),  
				craftingItem.getStackFromDamage(Items.CopperHeatPipe.getMetaID()),
				null, 120, 120);
		GT_Values.RA.addLatheRecipe(
				GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Silver, 1),
				craftingItem.getStackFromDamage(Items.SilverHeatPipe.getMetaID()),
				null, 120, 480);
		GT_Values.RA.addLatheRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideCrystal.getMetaID(), 4),  
				craftingItem.getStackFromDamage(Items.BoronArsenideHeatPipe.getMetaID()),
				null, 1200, 1920);
		GT_Values.RA.addLatheRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondCrystal.getMetaID(), 4),  
				craftingItem.getStackFromDamage(Items.DiamondHeatPipe.getMetaID()),
				null, 1200, 7680);	
	}*/
	
	private static void registerRecipes_Jars() {

		// Thaumium Reinforced Jar
		final ItemStack[] recipe_jarthaumiumreinforced = {
				GameRegistry.makeItemStack("Thaumcraft:ItemResource", 15, 1, null),
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
		};
		final AspectList aspects_jarthaumiumreinforced = new AspectList()
				.add(Aspect.ARMOR, 64)
				.add(Aspect.ORDER, 32)
				.add(Aspect.WATER, 32)
				.add(Aspect.GREED, 16)
				.add(Aspect.VOID, 16)
				.add(Aspect.AIR, 8);
		infusionRecipes.put("THAUMIUMREINFORCEDJAR",
				ThaumcraftApi.addInfusionCraftingRecipe("THAUMIUMREINFORCEDJAR", new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
						5, aspects_jarthaumiumreinforced, ItemApi.getBlock("blockJar",  0), recipe_jarthaumiumreinforced));
		// Thaumium Reinforced Void Jar
		final ItemStack[] recipe_voidjarupgrade = {
				GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 1),
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderEye, 1),
				ItemApi.getItem("itemNugget", 5)
		};
		final AspectList aspects_voidjarupgrade = new AspectList()
				.add(Aspect.VOID, 14)
				.add(Aspect.MAGIC, 14)
				.add(Aspect.ENTROPY, 14)
				.add(Aspect.WATER, 14);
		infusionRecipes.put("THAUMIUMREINFORCEDVOIDJAR",
				ThaumcraftApi.addInfusionCraftingRecipe("THAUMIUMREINFORCEDJAR", new ItemStack(Blocks.jarThaumiumReinforced, 1, 3),
						2, aspects_voidjarupgrade, new ItemStack(Blocks.jarThaumiumReinforced, 1, 0), recipe_voidjarupgrade));

		final ItemStack[] recipe_jarichor = {
				GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1, 0),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
				GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
				new ItemStack(net.minecraft.init.Blocks.glass_pane),
		};
		final AspectList aspects_jarichor = new AspectList()
				.add(Aspect.ARMOR, 256)
				.add(Aspect.ELDRITCH, 128)
				.add(Aspect.ORDER, 128)
				.add(Aspect.WATER, 128)
				.add(Aspect.GREED, 64)
				.add(Aspect.VOID, 64)
				.add(Aspect.AIR, 32);
		infusionRecipes.put("ICHORJAR", 
				ThaumcraftApi.addInfusionCraftingRecipe("ICHORJAR", new ItemStack(Blocks.jarIchor, 1, 0),
				15, aspects_jarichor, ItemApi.getBlock("blockJar", 0), recipe_jarichor));
		// Ichor Void Jar
		infusionRecipes.put("ICHORVOIDJAR",
				ThaumcraftApi.addInfusionCraftingRecipe("ICHORJAR", new ItemStack(Blocks.jarIchor, 1, 3),
						5, aspects_voidjarupgrade, new ItemStack(Blocks.jarIchor, 1, 0), recipe_voidjarupgrade));

	}

	private static void registerRecipes_LSC(){

		// Controller
		final Object[] lsc_recipe = {
				"LPL", "CBC", "LPL",
				'L', ItemList.IC2_LapotronCrystal.getWithCharge(1L, 10000000),
				'P', ItemList.Circuit_Chip_PIC.get(1L),
				'C', OrePrefixes.circuit.get(Materials.Master),
				'B', new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0),
		};
		GT_ModHandler.addCraftingRecipe(TileEntities.lsc.getStackForm(1), lsc_recipe);

		// Blocks
		final Object[] lcBase_recipe = {
				"WBW", "RLR", "WBW",
				'W', OrePrefixes.plate.get(Materials.Tantalum),
				'B', OrePrefixes.frameGt.get(Materials.TungstenSteel),
				'R', OrePrefixes.stickLong.get(Materials.TungstenSteel),
				'L', OrePrefixes.block.get(Materials.Lapis)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), lcBase_recipe);
		
		// Empty Capacitor
		final Object[] lcEmpty_recipe = {
				"SLS", "L L", "SLS",
				'S', OrePrefixes.screw.get(Materials.Lapis),
				'L', OrePrefixes.plate.get(Materials.Lapis)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6), lcEmpty_recipe);
		
		// EV Capacitor
		final Object[] lcEV_recipe = {
				"SLS", "LCL", "SLS",
				'S', OrePrefixes.screw.get(Materials.Lapis),
				'L', OrePrefixes.plate.get(Materials.Lapis),
				'C', GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7), lcEV_recipe);
		
		//EV cap alt recipe
		GT_Values.RA.addAssemblerRecipe(new ItemStack[] {(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6)), GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W), GT_Utility.getIntegratedCircuit(7)}, null, new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7), 200, 480);
		
		// IV Capacitor
		final Object[] lcIV_recipe = {
				"SLS", "LOL", "SLS",
				'S', OrePrefixes.screw.get(Materials.Lapis),
				'L', OrePrefixes.plate.get(Materials.Lapis),
				'O', ItemList.Energy_LapotronicOrb.get(1L)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), lcIV_recipe);

		//IV cap alt recipe
		GT_Values.RA.addAssemblerRecipe(new ItemStack[] {(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6)), ItemList.Energy_LapotronicOrb.get(1L), GT_Utility.getIntegratedCircuit(1)}, null, new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), 200, 1920);
		
		// LuV Capacitor
		GT_Values.RA.addAssemblylineRecipe(
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), 288000,
				new Object[] {
					GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
					GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
					ItemList.Circuit_Board_Elite.get(1),
					GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 64),
					new Object[]{OrePrefixes.circuit.get(Materials.Master), 4},
					ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
					ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
					ItemList.Circuit_Chip_HPIC.get(64),
					ItemList.Circuit_Parts_DiodeASMD.get(8),
					ItemList.Circuit_Parts_CapacitorASMD.get(8),
					ItemList.Circuit_Parts_ResistorASMD.get(8),
					ItemList.Circuit_Parts_TransistorASMD.get(8),
					GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64)
				},
				new FluidStack[] {
						Materials.SolderingAlloy.getMolten(720)
				},
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2), 2000, 100000
		);
		
		// ZPM Capacitor
		GT_Values.RA.addAssemblylineRecipe(
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2), 288000,
				new Object[] {
					GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
					GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
					GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
					new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
					ItemList.Energy_LapotronicOrb2.get(8L),
					ItemList.Field_Generator_LuV.get(2),
					ItemList.Circuit_Wafer_SoC2.get(64),
					ItemList.Circuit_Wafer_SoC2.get(64),
					ItemList.Circuit_Parts_DiodeASMD.get(8),
					GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32)
		},
				new FluidStack[] {
						Materials.SolderingAlloy.getMolten(2880),
						new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
				},
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3), 2000, 100000
		);
		
		// UV Capacitor
		GT_Values.RA.addAssemblylineRecipe(
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3), 288000,
				new Object[] {
					GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
					GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
					GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
					new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
					new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
					ItemList.Energy_Module.get(8L),
					ItemList.Field_Generator_ZPM.get(2),
					ItemList.Circuit_Wafer_HPIC.get(64),
					ItemList.Circuit_Wafer_HPIC.get(64),
					ItemList.Circuit_Parts_DiodeASMD.get(16),
					GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32)
		},
				new FluidStack[] {
						Materials.SolderingAlloy.getMolten(2880),
						new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
				},
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4), 2000, 200000
		);
		
		// Ultimate Capacitor
		GT_Values.RA.addAssemblylineRecipe(
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4), 288000,
				new Object[] {
						GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
						GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
						GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
						GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
						new Object[]{OrePrefixes.circuit.get(Materials.Bio), 1},
						new Object[]{OrePrefixes.circuit.get(Materials.Bio), 1},
						new Object[]{OrePrefixes.circuit.get(Materials.Bio), 1},
						new Object[]{OrePrefixes.circuit.get(Materials.Bio), 1},
						ItemList.ZPM2.get(8L),
						ItemList.Field_Generator_UHV.get(4),
						ItemList.Circuit_Wafer_UHPIC.get(64),
						ItemList.Circuit_Wafer_UHPIC.get(64),
						ItemList.Circuit_Wafer_SoC2.get(32),
						ItemList.Circuit_Parts_DiodeASMD.get(64)
				},
				new FluidStack[] {
						Materials.SolderingAlloy.getMolten(3760),
						Materials.Naquadria.getMolten(9216),
						new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000)
				},
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5), 2000, 200000
		);

		// Capacitor recycling
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7),
				GT_ModHandler.getIC2Item("lapotronCrystal", 1L, 26),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
				1200, 32);
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
				ItemList.Energy_LapotronicOrb.get(1L),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
				1200, 32);
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
				ItemList.Energy_LapotronicOrb2.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
				1200, 32);
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
				ItemList.Energy_Module.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
				1200, 32);
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
				ItemList.Energy_Cluster.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
				1200, 32);
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
				ItemList.ZPM2.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
				1200, 32);

		// For the people that already made the Really Ultimate Battery but want to use the LSC
		GT_Values.RA.addAssemblerRecipe(
				new ItemStack[] {
						ItemList.ZPM3.get(1),
						GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
						GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
						GT_Utility.getIntegratedCircuit(6)
				},
				null,
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
				12000, 200000
		);
	}

	/*private static void registerRecipes_SpaceElevator() {
		// Controller
		final Object[] se_recipe = {
				"BCB", "CPC", "BCB",
				'B', new ItemStack(Blocks.spaceElevatorStructure, 1, 0),
				'C', OrePrefixes.cableGt16.get(Materials.Aluminium),
				'P', OrePrefixes.circuit.get(Materials.Master)
		};
		GT_ModHandler.addCraftingRecipe(TileEntities.se.getStackForm(1), se_recipe);
		// Blocks
		final Object[] seBase_recipe = {
				"DRD", "RCR", "DRD",
				'D', OrePrefixes.plate.get(Materials.DarkSteel),
				'R', OrePrefixes.stick.get(Materials.Steel),
				'C', OrePrefixes.block.get(Materials.Concrete),
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.spaceElevatorStructure, 1, 0), seBase_recipe);
		final Object[] seCoilHolder_recipe = {
				"DRD", "RCR", "DRD",
				'D', OrePrefixes.plate.get(Materials.DarkSteel),
				'R', OrePrefixes.ring.get(Materials.Steel),
				'C', OrePrefixes.cableGt01.get(Materials.Aluminium)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.spaceElevatorStructure, 1, 1), seCoilHolder_recipe);
	}*/

	private static void registerRecipes_Cosmetics() {
		// Hex Tiles
		final ItemStack[] hexTiles = {
				GT_Utility.getIntegratedCircuit(6),
				GT_OreDictUnificator.get(OrePrefixes.stone, Materials.Concrete, 1),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
				GT_OreDictUnificator.get(OrePrefixes.foil, Materials.DarkSteel, 2)
		};
		GT_Values.RA.addAssemblerRecipe(
				hexTiles,
				FluidRegistry.getFluidStack("molten.plastic", 36),
				new ItemStack(Blocks.largeHexPlate, 2),
				600, 120);
	}
}
