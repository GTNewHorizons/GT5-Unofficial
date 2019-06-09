package kekztech;

import blocks.Block_GDCUnit;
import blocks.Block_YSZUnit;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;
import items.ErrorItem;
import items.MetaItem_CraftingComponent;
import items.MetaItem_ReactorComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import tileentities.GTMTE_ModularNuclearReactor;
import tileentities.GTMTE_SOFuelCellMK1;
import tileentities.GTMTE_SOFuelCellMK2;

@Mod(modid = KekzCore.MODID, name = KekzCore.NAME, version = KekzCore.VERSION, 
		dependencies = "required-after:IC2; "
			+ "required-after:gregtech"
		)
public class KekzCore {
	
	public static final String NAME = "KekzTech";
	public static final String MODID = "kekztech";
	public static final String VERSION = "0.1a";
	
	private GTMTE_SOFuelCellMK1 sofc1;
	private GTMTE_SOFuelCellMK2 sofc2;
	private GTMTE_ModularNuclearReactor mdr;
		
	@Mod.Instance("kekztech")
	public static KekzCore instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Items
		ErrorItem.getInstance().registerItem();
		MetaItem_ReactorComponent.getInstance().registerItem();
		MetaItem_CraftingComponent.getInstance().registerItem();
		// Blocks
		Block_YSZUnit.getInstance().registerBlock();
		Block_GDCUnit.getInstance().registerBlock();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event	) {
		sofc1 = new GTMTE_SOFuelCellMK1(5000, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
		sofc2 = new GTMTE_SOFuelCellMK2(5001, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
		mdr = new GTMTE_ModularNuclearReactor(5002, "multimachine.nuclearreactor", "Nuclear Reactor");
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println("Registering KekzTech recipes...");
		
		final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();
		final MetaItem_ReactorComponent reactorItem = MetaItem_ReactorComponent.getInstance();
		
		// Multiblock Controllers
		GameRegistry.addShapedRecipe(sofc1.getStackForm(1), 
				"CCC", "PHP", "FBL",
				'C', Util.getStackofAmountFromOreDict("circuitAdvanced", 1),
				'P', ItemList.Electric_Pump_HV.get(1L, (Object[]) null),
				'H', ItemList.Hull_HV.get(1L, (Object[]) null),
				'F', Util.getStackofAmountFromOreDict("pipeSmallStainlessSteel", 1),
				'B', Util.getStackofAmountFromOreDict("cableGt02Gold", 1),
				'L', Util.getStackofAmountFromOreDict("pipeLargeStainlessSteel", 1));
		GameRegistry.addShapedRecipe(sofc2.getStackForm(1), 
				"CCC", "PHP", "FBL",
				'C', Util.getStackofAmountFromOreDict("circuitMaster", 1),
				'P', ItemList.Electric_Pump_IV.get(1L, (Object[]) null),
				'H', ItemList.Hull_IV.get(1L, (Object[]) null),
				'F', Util.getStackofAmountFromOreDict("pipeSmallUltimate", 1),
				'B', Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1),
				'L', Util.getStackofAmountFromOreDict("pipeMediumUltimate", 1));
		
		// Ceramic Electrolyte Units
		final ItemStack[] yszUnit = {
				GT_Utility.getIntegratedCircuit(6),
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 4),
				Util.getStackofAmountFromOreDict("frameGtYttrium", 1),
				Util.getStackofAmountFromOreDict("rotorStainlessSteel", 1),
				ItemList.Electric_Motor_HV.get(1L, (Object[]) null),
		};
		GT_Values.RA.addAssemblerRecipe(
				yszUnit, 
				Materials.Hydrogen.getGas(4000), 
				new ItemStack(Block_YSZUnit.getInstance(), 1), 
				1200, 480);
		final ItemStack[] gdcUnit = {
				GT_Utility.getIntegratedCircuit(6),
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 8),
				Util.getStackofAmountFromOreDict("frameGtGadolinium", 1),
				Util.getStackofAmountFromOreDict("rotorDesh", 1),
				ItemList.Electric_Motor_IV.get(1L, (Object[]) null),
		};
		GT_Values.RA.addAssemblerRecipe(
				gdcUnit, 
				Materials.Hydrogen.getGas(16000), 
				new ItemStack(Block_GDCUnit.getInstance(), 1), 
				2400, 1920);
		
		// Ceramic plates
		GT_Values.RA.addAlloySmelterRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 10), 
				ItemList.Shape_Mold_Plate.get(1, (Object[]) null),
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 1), 
				400, 480);
		GT_Values.RA.addFormingPressRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10), 
				ItemList.Shape_Mold_Plate.get(1, (Object[]) null),
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1), 
				800, 480);
		
		// Dusts
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
		GT_Values.RA.addChemicalRecipe(
				Materials.Yttrium.getDust(1), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(3000),
				null, craftingItem.getStackOfAmountFromDamage(Items.YttriaDust.getMetaID(), 1), null, 
				400, 30);
		GT_Values.RA.addChemicalRecipe(
				Util.getStackofAmountFromOreDict("dustZirconium", 1), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(2000),
				null, craftingItem.getStackOfAmountFromDamage(Items.ZirconiaDust.getMetaID(), 1), null, 
				400, 30);
		GT_Values.RA.addChemicalRecipe(
				Materials.Cerium.getDust(2), GT_Utility.getIntegratedCircuit(6), Materials.Oxygen.getGas(3000),
				null, craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 2), null, 
				400, 30);
		GT_Values.RA.addMixerRecipe(
				craftingItem.getStackOfAmountFromDamage(Items.YttriaDust.getMetaID(), 1), 
				craftingItem.getStackOfAmountFromDamage(Items.ZirconiaDust.getMetaID(), 5), 
				GT_Utility.getIntegratedCircuit(6), null, null, null, 
				craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6), 
				400, 96);
		GT_Values.RA.addMixerRecipe(
				Util.getStackofAmountFromOreDict("dustGadolinium", 1), 
				craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9), 
				GT_Utility.getIntegratedCircuit(6), null, null, null, 
				craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10), 
				400, 1920);
		
		// Heat Pipes
		GT_Values.RA.addLatheRecipe(
				Util.getStackofAmountFromOreDict("stickCopper", 1),  
				craftingItem.getStackFromDamage(Items.CopperHeatPipe.getMetaID()),
				null, 120, 120);
		GT_Values.RA.addLatheRecipe(
				Util.getStackofAmountFromOreDict("stickSilver", 1),  
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
		
		// Heat Vents
		final ItemStack[] t1HeatVent = {
				craftingItem.getStackOfAmountFromDamage(Items.CopperHeatPipe.getMetaID(), 2),
				ItemList.Electric_Motor_MV.get(1L, (Object[]) null),
				Util.getStackofAmountFromOreDict("rotorSteel", 1),
				Util.getStackofAmountFromOreDict("plateDoubleSteel", 2),
				Util.getStackofAmountFromOreDict("screwSteel", 8),
				Util.getStackofAmountFromOreDict("circuitGood", 1),
				GT_Utility.getIntegratedCircuit(6)
		};
		GT_Values.RA.addAssemblerRecipe(
				t1HeatVent, 
				FluidRegistry.getFluidStack("molten.copper", 144),
				reactorItem.getStackFromDamage(Items.T1HeatVent.getMetaID()),
				200, 120);
		final ItemStack[] t2HeatVent = {
				craftingItem.getStackOfAmountFromDamage(Items.SilverHeatPipe.getMetaID(), 2),
				ItemList.Electric_Motor_HV.get(1L, (Object[]) null),
				Util.getStackofAmountFromOreDict("rotorAluminium", 1),
				Util.getStackofAmountFromOreDict("plateDoubleAluminium", 2),
				Util.getStackofAmountFromOreDict("screwAluminium", 8),
				Util.getStackofAmountFromOreDict("circuitAdvanced", 1),
				GT_Utility.getIntegratedCircuit(6)
		};
		GT_Values.RA.addAssemblerRecipe(
				t2HeatVent, 
				FluidRegistry.getFluidStack("molten.silver", 144),
				reactorItem.getStackFromDamage(Items.T2HeatVent.getMetaID()),
				400, 480);
		final ItemStack[] t3HeatVent = {
				craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideHeatPipe.getMetaID(), 2),
				ItemList.Electric_Motor_IV.get(1L, (Object[]) null),
				Util.getStackofAmountFromOreDict("rotorTungstenSteel", 1),
				Util.getStackofAmountFromOreDict("plateDoubleTungstenSteel", 2),
				Util.getStackofAmountFromOreDict("screwTungsten", 8),
				Util.getStackofAmountFromOreDict("circuitData", 1),
				GT_Utility.getIntegratedCircuit(6)
		};
		GT_Values.RA.addAssemblerRecipe(
				t3HeatVent, 
				FluidRegistry.getFluidStack("molten.gallium", 576),
				reactorItem.getStackFromDamage(Items.T3HeatVent.getMetaID()),
				800, 7680);
		
		System.out.println("...done");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
