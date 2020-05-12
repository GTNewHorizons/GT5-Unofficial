package common;

import com.github.technus.tectech.thing.CustomItemList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import items.ErrorItem;
import items.MetaItem_CraftingComponent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Recipes {

	public static final HashMap<String, InfusionRecipe> infusionRecipes = new HashMap<>();
	
	public static void postInit() {
		KekzCore.LOGGER.info("Registering recipes...");
		
		registerRecipes_TFFT();
		registerRecipes_SOFC();
		registerRecipes_Nuclear();
		//registerRecipes_ItemServer();
		registerRecipes_Jars();
		registerRecipes_LSC();

		KekzCore.LOGGER.info("Finished registering recipes");
	}

	private static void lapoCapacitorRecipeAdder(GT_Recipe.GT_Recipe_AssemblyLine baseRecipe, Materials boxMaterial, ItemStack newResearchTrigger, ItemStack result) {
		if(baseRecipe != null) {
			final ArrayList<ItemStack> baseInputs = new ArrayList<>(Arrays.asList(baseRecipe.mInputs));
			if(baseInputs.size() <= 14){
				baseInputs.add(GT_OreDictUnificator.get(OrePrefixes.frameGt, boxMaterial, 4));
				baseInputs.add(GT_OreDictUnificator.get(OrePrefixes.screw, boxMaterial, 24));

				GT_Values.RA.addAssemblylineRecipe(newResearchTrigger, baseRecipe.mResearchTime,
						Util.toItemStackArray(baseInputs), baseRecipe.mFluidInputs, result,
						baseRecipe.mDuration * 2, baseRecipe.mEUt);
				KekzCore.LOGGER.info("Successfully extended Lapotronic Battery recipe for Lapotronic Capacitor of tier " + result.getItemDamage());
			}
		} else {
			KekzCore.LOGGER.info("Base recipe was NULL. Failed to extended Lapotronic Battery recipe for Lapotronic Capacitor of tier " + result.getItemDamage());
		}
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
		GT_ModHandler.addCraftingRecipe(KekzCore.fms.getStackForm(1), tfft_recipe);
		
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
			final Object[] multi_hatch = {
					"PRP", "UFU", "PRP",
					'P', GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 1),
					'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
					'U', ItemList.Electric_Pump_IV.get(1L),
					'F', ItemList.Field_Generator_HV.get(1L)
			};		
			GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.tfftMultiHatch), multi_hatch);
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
		GT_ModHandler.addCraftingRecipe(KekzCore.sofc1.getStackForm(1), mk1_recipe);
		final Object[] mk2_recipe = {
				"CCC", "PHP", "FBL",
				'C', OrePrefixes.circuit.get(Materials.Master),
				'P', ItemList.Electric_Pump_IV.get(1L),
				'H', ItemList.Hull_IV.get(1L),
				'F', GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1),
				'B', Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1),
				'L', GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1)
		};
		GT_ModHandler.addCraftingRecipe(KekzCore.sofc2.getStackForm(1), mk2_recipe);
		
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
	
	private static void registerRecipes_Nuclear() {
		
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
	}
	
	private static void registerRecipes_ItemServer() {
		
		final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();
		
		// Controller
		final Object[] is_recipe = {
				"FRF", "CGC", "PZP",
				'F', GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TungstenSteel, 1),
				'C', ItemList.Conveyor_Module_LuV.get(1L),
				'G', ItemList.Field_Generator_EV.get(1L),
				'P', GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 1),
				'Z', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1)
		};
		GT_ModHandler.addCraftingRecipe(KekzCore.is.getStackForm(1), is_recipe);
		
		// Blocks
		final Object[] is_rack_recipe = {
				"BRB", "CFC", "BRB",
				'B', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlueSteel, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Aluminium, 1),
				'C', GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Platinum, 1),
				'F', GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.itemServerRackCasing), is_rack_recipe);
		final ItemStack[] is_ioport = {
				GT_Utility.getIntegratedCircuit(6),
				new ItemStack(Blocks.itemServerRackCasing),
				GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Platinum, 16),
				GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Platinum, 2),
				ItemList.Field_Generator_HV.get(1L),
				ItemList.Robot_Arm_EV.get(4L)
			};
			GT_Values.RA.addAssemblerRecipe(
					is_ioport, 
					FluidRegistry.getFluidStack("molten.polytetrafluoroethylene", 144),
					new ItemStack(Blocks.itemServerIOPort, 1), 
					200, 7680);
		final Object[] is_blade = {
				"CRC", "CMC", "HPH",
				'C', GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1),
				'R', GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Aluminium, 1),
				'P', GT_OreDictUnificator.get(OrePrefixes.cableGt08, Materials.Platinum, 1),
				'M', ItemList.Electric_Motor_EV.get(1L),
				'H', craftingItem.getStackFromDamage(Items.BoronArsenideHeatPipe.getMetaID()),
		};
		GT_ModHandler.addCraftingRecipe(craftingItem.getStackOfAmountFromDamage(Items.ItemServerBlade.getMetaID(), 8), is_blade);
		final ItemStack[] is_drive = {
				GT_Utility.getIntegratedCircuit(6),
				craftingItem.getStackOfAmountFromDamage(Items.ItemServerBlade.getMetaID(), 8),
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlueSteel, 4),
				GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Aluminium, 16)
			};
			GT_Values.RA.addAssemblerRecipe(
					is_drive, 
					FluidRegistry.getFluidStack("molten.polyethylene", 1152),
					new ItemStack(Blocks.itemServerDrive, 1), 
					200, 7680);
	}
	
	private static void registerRecipes_Jars() {
		
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
				ThaumcraftApi.addInfusionCraftingRecipe("ICHORJAR", new ItemStack(Blocks.jarIchor, 1), 
				15, aspects_jarichor, ItemApi.getBlock("blockJar", 0), recipe_jarichor));
		
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
				ThaumcraftApi.addInfusionCraftingRecipe("THAUMIUMREINFORCEDJAR", new ItemStack(Blocks.jarThaumiumReinforced, 1), 
						5, aspects_jarthaumiumreinforced, ItemApi.getBlock("blockJar",  0), recipe_jarthaumiumreinforced));
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
		GT_ModHandler.addCraftingRecipe(KekzCore.lsc.getStackForm(1), lsc_recipe);

		// Blocks
		final Object[] lcBase_recipe = {
				"WBW", "RLR", "WBW",
				'W', OrePrefixes.plate.get(Materials.Tantalum),
				'B', OrePrefixes.frameGt.get(Materials.TungstenSteel),
				'R', OrePrefixes.stickLong.get(Materials.TungstenSteel),
				'L', OrePrefixes.block.get(Materials.Lapis)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), lcBase_recipe);
		final Object[] lcIV_recipe = {
				"SLS", "LOL", "SLS",
				'S', OrePrefixes.screw.get(Materials.Lapis),
				'L', OrePrefixes.plate.get(Materials.Lapis),
				'O', ItemList.Energy_LapotronicOrb.get(1L)
		};
		GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), lcIV_recipe);

		KekzCore.LOGGER.info("Reading Assembly Line recipes from GregTech recipe map");
		GT_Recipe.GT_Recipe_AssemblyLine arLuV = null;
		// Next two are hardcoded because my code can't find them
		GT_Recipe.GT_Recipe_AssemblyLine arZPM = new GT_Recipe.GT_Recipe_AssemblyLine(
				ItemList.Energy_LapotronicOrb2.get(1L), 288000, new ItemStack[] {
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
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
				ItemList.Energy_Module.get(1), 2000, 100000
		);
		GT_Recipe.GT_Recipe_AssemblyLine arUV = new GT_Recipe.GT_Recipe_AssemblyLine(
				ItemList.Energy_Module.get(1L), 288000, new ItemStack[] {
				GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 1),
				GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 1),
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
				ItemList.Energy_Cluster.get(1), 2000, 200000
		);
		GT_Recipe.GT_Recipe_AssemblyLine arU = null;
		GT_Recipe.GT_Recipe_AssemblyLine arRU = null;
		for(GT_Recipe.GT_Recipe_AssemblyLine ar : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
			if(GT_Utility.areStacksEqual(ar.mOutput, ItemList.Energy_LapotronicOrb2.get(1L), true)) {
				// LuV Lapo Orb
				arLuV = ar;
			} else if(GT_Utility.areStacksEqual(ar.mOutput, ItemList.Energy_Module.get(1L), true)) {
				// ZPM Lapo Orb
				KekzCore.LOGGER.info("Found matching recipe for Energy Module?");
			} else if(GT_Utility.areStacksEqual(ar.mOutput, ItemList.Energy_Cluster.get(1L), true)) {
				// UV Lapo Orb
				KekzCore.LOGGER.info("Found matching recipe for Energy Cluster?");
			} else if(GT_Utility.areStacksEqual(ar.mOutput, ItemList.ZPM2.get(1L), true)) {
				// Ultimate Battery
				arU = ar;
			} else if(GT_Utility.areStacksEqual(ar.mOutput, ItemList.ZPM3.get(1L), true)) {
				// Really Ultimate Battery
				arRU = ar;
			}
		}
		lapoCapacitorRecipeAdder(arLuV, Materials.Osmiridium,
				GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 1),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2));
		lapoCapacitorRecipeAdder(arZPM, Materials.NaquadahAlloy,
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3));
		lapoCapacitorRecipeAdder(arUV, Materials.Neutronium,
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4));
		lapoCapacitorRecipeAdder(arU, Materials.CosmicNeutronium,
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5));
		lapoCapacitorRecipeAdder(arRU, Materials.Infinity,
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
				new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6));

		// Capacitor recycling
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
				ItemList.Energy_LapotronicOrb.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
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
		GT_Values.RA.addUnboxingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
				ItemList.ZPM3.get(1L),
				GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
				1200, 32);
	}
}
