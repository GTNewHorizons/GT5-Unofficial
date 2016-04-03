package miscutil.core.item.tool;

import miscutil.core.item.ModItems;
import miscutil.core.util.ItemUtils;
import miscutil.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Loader_TOOLS {
	
	//Outputs
	static ItemStack RECIPE_StaballoyPickaxe = new ItemStack(ModItems.itemStaballoyPickaxe);
	static ItemStack RECIPE_StaballoyAxe = new ItemStack(ModItems.itemStaballoyAxe);
	static ItemStack RECIPE_SandstoneHammer = new ItemStack(ModItems.itemSandstoneHammer);
	static ItemStack RECIPE_StaballoyIngot = new ItemStack(ModItems.itemIngotStaballoy);
	static ItemStack RECIPE_SandStone = new ItemStack(Blocks.sandstone, 2);
	static ItemStack RECIPE_Sand = new ItemStack(Blocks.sand, 4);
	
	static ItemStack RECIPE_DivisionSigil = new ItemStack(Utils.getItem("ExtraUtilities:divisionSigil"));
	
	//MC Items
	static Item Apple = Items.apple;
	
	//MC Blocks
	static Block CobbleStone = Blocks.cobblestone;
	static Block Dirt = Blocks.dirt;
	static Block Sand = Blocks.sand;
	static Block Gravel = Blocks.gravel;
	static Block Sandstone = Blocks.sandstone;
	
	//null
	static String empty = " ";
	
	//Batteries
	static String batteryBasic = "batteryBasic";
	static String batteryAdvanced = "batteryAdvanced";
	static String batteryElite = "batteryElite";
	static String batteryMaster = "batteryMaster";
	static String batteryUltimate = "batteryUltimate";
	
	//Circuits
	static String circuitPrimitive = "circuitPrimitive";
	static String circuitBasic = "circuitBasic";
	static String circuitGood = "circuitGood";
	static String circuitAdvanced = "circuitAdvanced";
	static String circuitElite = "circuitElite";
	static String circuitMaster = "circuitMaster";
	static String circuitUltimate = "circuitUltimate";
	
	//Cables
	static String cableGt01Electrum = "cableGt01Electrum";
	static String cableGt02Electrum = "cableGt02Electrum";
	static String cableGt01RedstoneAlloy = "cableGt01RedstoneAlloy";
	static String cableGt02RedstoneAlloy = "cableGt02RedstoneAlloy";
	static String cableGt01Copper = "cableGt01Copper";
	static String cableGt02Copper = "cableGt02Copper";
	static String cableGt01AnnealedCopper = "cableGt01AnnealedCopper";
	static String cableGt02AnnealedCopper = "cableGt02AnnealedCopper";
	
	//Rods
	static String stickStaballoy= "stickStaballoy";
	static String stickTitanium= "stickTitanium";
	static String stickIron= "stickIron";
	static String stickGold= "stickGold";
	static String stickSilver= "stickSilver";
	static String stickSteel= "stickSteel";
	static String stickBronze= "stickBronze";
	static String stickTungsten= "stickTungsten";
	static String stickRedAlloy= "stickRedAlloy";
	static String stickInvar= "stickInvar";
	static String stickElectrum= "stickElectrum";
	static String stickElectricalSteel= "stickElectricalSteel";
	
	//Plates
	static String plateStaballoy= "plateStaballoy";
	static String plateTitanium= "plateTitanium";
	static String plateIron= "plateIron";
	static String plateGold= "plateGold";
	static String plateSilver= "plateSilver";
	static String plateSteel= "plateSteel";
	static String plateBronze= "plateBronze";
	static String plateTungsten= "plateTungsten";
	static String plateRedAlloy= "plateRedAlloy";
	static String plateInvar= "plateInvar";
	static String plateElectrum= "plateElectrum";
	static String plateElectricalSteel= "plateElectricalSteel";
	
	//Ingots
	static String ingotStaballoy= "ingotStaballoy";
	static String ingotTitanium= "ingotTitanium";
	static String ingotIron= "ingotIron";
	static String ingotGold= "ingotGold";
	static String ingotSilver= "ingotSilver";
	static String ingotSteel= "ingotSteel";
	static String ingotBronze= "ingotBronze";
	static String ingotTungsten= "ingotTungsten";
	static String ingotRedAlloy= "ingotRedAlloy";
	static String ingotInvar= "ingotInvar";
	static String ingotElectrum= "ingotElectrum";
	static String ingotUranium= "ingotUranium";
	static String ingotElectricalSteel= "ingotElectricalSteel";
	
	//Crafting Tools
	static String craftingToolHardHammer = "craftingToolHardHammer";
	static String craftingToolSoftHammer = "craftingToolSoftHammer";
	static String craftingToolFile = "craftingToolFile";
	static String craftingToolSaw = "craftingToolSaw";
	static String craftingToolPickaxe = "craftingToolPickaxe";
	static String craftingToolWrench = "craftingToolWrench";
	static String craftingToolCrowbar = "craftingToolCrowbar";
	static String craftingToolKnife = "craftingToolKnife";
	static String craftingToolScrewdriver = "craftingToolScrewdriver";
	
	static ItemStack sandHammer = new ItemStack (ModItems.itemSandstoneHammer, 1, OreDictionary.WILDCARD_VALUE);
	static String craftingToolSandHammer = "craftingToolSandHammer";
	
	public static final void run(){		
		
			//plateStaballoy = new ItemStack(ModItems.itemPlateStaballoy);
			//ingotStaballoy = new ItemStack(ModItems.itemIngotStaballoy);
			
			//Pickaxes
			registerPickaxes();
		
	}
	
	private static void registerPickaxes(){
		//Staballoy Pickaxe
		ItemUtils.recipeBuilder(
				plateStaballoy, plateStaballoy, ingotStaballoy,
				craftingToolFile, stickTungsten, craftingToolHardHammer,
				craftingToolWrench, stickTungsten, craftingToolHardHammer,
				RECIPE_StaballoyPickaxe);
		
		//Staballoy Axe
		ItemUtils.recipeBuilder(
				plateStaballoy, ingotStaballoy, craftingToolHardHammer,
				plateStaballoy, stickTungsten, craftingToolHardHammer,
				craftingToolFile, stickTungsten, craftingToolWrench,
				RECIPE_StaballoyAxe);
		
		//Cobble to Sand
		ItemUtils.recipeBuilder(
				CobbleStone, CobbleStone, CobbleStone,
				CobbleStone, sandHammer, CobbleStone,
				CobbleStone, CobbleStone, CobbleStone,
				RECIPE_Sand);
		
		//Sand to Sandstone
		ItemUtils.recipeBuilder(
				Sand, Sand, Sand,
				Sand, sandHammer, Sand,
				Sand, Sand, Sand,
				RECIPE_SandStone);
		
		//Sandstone Hammer
		ItemUtils.recipeBuilder(
				plateElectrum, ingotElectrum, plateElectrum,
				craftingToolScrewdriver, stickBronze, craftingToolHardHammer,
				null, stickSteel, null,
				RECIPE_SandstoneHammer);
		
		//Division Sigil
		ItemUtils.recipeBuilder(
				"plateNetherStar", "gemIridium", "plateNetherStar",
				"plateIridium", craftingToolHardHammer, "plateIridium",
				"plateNetherStar", "gemIridium", "plateNetherStar",
				RECIPE_DivisionSigil);
		
		
	}
	
}
