package miscutil.core.block;

import miscutil.core.block.antigrief.TowerDevice;
import miscutil.core.creativetabs.AddToCreativeTab;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.GregtechMetaCasingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModBlocks {

	//Blood Steel
	public static Block blockBloodSteel;
	public static Block blockStaballoy;
	//public static Block blockIronPlatedBricks;
	public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
	
	//public static Block blockBloodSteelChest;

	//BloodSteelorial Furnace
	//public static Block tutFurnace;
	//public static Block tutFurnaceActive;

	//BloodSteelorial Chest
	//public static Block tutChest;

	//Arcane Infuser
	//public static Block arcaneInfuser;
	//public static Block arcaneInfuserActive;
	
	//Block Storage
	//public static Block emxBlockStorage;


	public static void init() {
		Utils.LOG_INFO("Initializing Blocks.");
		blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");
		
		//BloodSteelorial Furnace - Must Init blocks first as they're not static.  
		/** if (CORE.DEBUG){
			FMLLog.info("Loading Furnace.");}
		tutFurnace= new BloodSteelFurnace(false).setBlockName("BloodSteelFurnace").setCreativeTab(TMCreativeTabs.tabBlock);
		tutFurnaceActive= new BloodSteelFurnace(true).setBlockName("BloodSteelFurnaceActive");

		//Arcane Infuser - Must Init blocks first as they're not static.
		if (CORE.DEBUG){
			FMLLog.info("Loading Arcane Infuser.");}
		arcaneInfuser = new ArcaneInfuser(false).setBlockName("ArcaneInfuser").setCreativeTab(TMCreativeTabs.tabBlock);
		arcaneInfuserActive = new ArcaneInfuser(true).setBlockName("ArcaneInfuserActive");

		//Blood Steel Chest
		if (CORE.DEBUG){
			FMLLog.info("Loading Blood Steel Chest.");}
		tutChest = new BloodSteelChest(0).setBlockName("BloodSteelChest").setCreativeTab(TMCreativeTabs.tabBlock);

		 */
		//BlockStorage
		//emxBlockStorage = new BlockStorage();
		
		//Register Blocks next - TODO
		registerBlocks(); 
	}

	public static void registerBlocks(){
		
		Utils.LOG_INFO("Registering Blocks.");

		//Blood Steel Block
		GameRegistry.registerBlock(blockBloodSteel = new BasicBlock("blockBloodSteel", Material.iron), "blockBloodSteel");
		
		//Staballoy Block
		GameRegistry.registerBlock(blockStaballoy = new BasicBlock("blockStaballoy", Material.iron), "blockStaballoy");
		
		//Casing Blocks
		blockCasingsMisc = new GregtechMetaCasingBlocks();

		//Blood Steel Block //Name, Material, Hardness, Resistance, Light level, Tool, tool level, sound
		//GameRegistry.registerBlock(blockToolBuilder = new AdvancedBlock("blockToolBuilder", Material.circuits, TMCreativeTabs.tabMachines, 1F, 5F, 0F, "pickaxe", 1, Block.soundTypeWood), "blockToolBuilder");

		/** TODO re-enable blocks when working.


		//Blood Steel Chest
		GameRegistry.registerBlock(tutChest, tutChest.getUnlocalizedName());

		//BloodSteelorial Furnace
		GameRegistry.registerBlock(tutFurnace, tutFurnace.getUnlocalizedName());
		GameRegistry.registerBlock(tutFurnaceActive, tutFurnaceActive.getUnlocalizedName());

		//Arcane Infuser
		GameRegistry.registerBlock(arcaneInfuser, arcaneInfuser.getUnlocalizedName());
		GameRegistry.registerBlock(arcaneInfuserActive, arcaneInfuserActive.getUnlocalizedName());
		 **/
		
		//Block Storage
		//GameRegistry.registerBlock(emxBlockStorage, emxBlockStorage.getUnlocalizedName());
		
		//blockGriefSaver Block
		//GameRegistry.registerBlock(blockGriefSaver = new AdvancedBlock("blockGriefSaver", Material.circuits, tabBlock, 2.0F, 10.0F, 0, "axe", 2, Block.soundTypeMetal), "blockGriefSaver");
		GameRegistry.registerBlock(blockGriefSaver, "blockGriefSaver");
	}

}