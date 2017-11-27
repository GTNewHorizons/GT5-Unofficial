package gtPlusPlus.core.block;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.block.general.BlockCompressedObsidian;
import gtPlusPlus.core.block.general.BlockNet;
import gtPlusPlus.core.block.general.BlockTankXpConverter;
import gtPlusPlus.core.block.general.FirePit;
import gtPlusPlus.core.block.general.FluidTankInfinite;
import gtPlusPlus.core.block.general.HellFire;
import gtPlusPlus.core.block.general.LightGlass;
import gtPlusPlus.core.block.general.MiningExplosives;
import gtPlusPlus.core.block.general.antigrief.BlockWitherProof;
import gtPlusPlus.core.block.machine.FishTrap;
import gtPlusPlus.core.block.machine.HeliumGenerator;
import gtPlusPlus.core.block.machine.Machine_ModularityTable;
import gtPlusPlus.core.block.machine.Machine_ProjectTable;
import gtPlusPlus.core.block.machine.Machine_TradeTable;
import gtPlusPlus.core.block.machine.Machine_Workbench;
import gtPlusPlus.core.block.machine.Machine_WorkbenchAdvanced;
import gtPlusPlus.core.fluids.FluidRegistryHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public final class ModBlocks {

	public static Block blockFishTrap;
	public static Block blockWorkbench;
	public static Block blockWorkbenchAdvanced;
	//Blocks
	//public static Block blockBloodSteel;
	//public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
	public static Block blockCasings2Misc;
	public static Block blockMetaTileEntity;
	public static Block blockHeliumGenerator;
	public static Block blockNHG;
	public static Block blockCharger;

	public static Block MatterFabricatorEffectBlock;

	public static Fluid fluidSludge = new Fluid("fluid.sludge");
	public static Block blockFluidSludge;

	public static Block blockFirePit;

	public static Block blockOreFluorite;

	public static Block blockMiningExplosive;

	public static Block blockHellfire;
	public static Block blockInfiniteFLuidTank;
	public static Block blockProjectTable;
	public static Block blockTradeTable;
	public static Block blockModularTable;

	public static Block blockWitherGuard;
	public static Block blockXpConverter;
	public static Block blockCompressedObsidian;
	public static Block blockNet;

	public static void init() {
		Utils.LOG_INFO("Initializing Blocks.");
		//blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");

		registerBlocks();
	}

	public static void registerBlocks(){

		Utils.LOG_INFO("Registering Blocks.");
		GameRegistry.registerBlock(MatterFabricatorEffectBlock = new LightGlass(Material.glass, false).setHardness(0.1F).setBlockTextureName(CORE.MODID + ":" + "blockMFEffect").setStepSound(Block.soundTypeGlass), "blockMFEffect");

		//Fluids
		FluidRegistryHandler.registerFluids();

		//Workbench
		blockWorkbench = new Machine_Workbench().setHardness(1.5F);
		blockWorkbenchAdvanced = new Machine_WorkbenchAdvanced().setHardness(2.5F);
		blockHeliumGenerator = new HeliumGenerator();
		blockFirePit = new FirePit();
		blockFishTrap = new FishTrap();
		blockInfiniteFLuidTank = new FluidTankInfinite();
		blockOreFluorite = new BlockBaseOre("oreFluorite", "Fluorite", Material.rock, BlockTypes.ORE, Utils.rgbtoHexValue(120, 120, 30), 3);
		blockMiningExplosive = new MiningExplosives();
		blockHellfire = new HellFire();
		blockProjectTable =  new Machine_ProjectTable();
		blockTradeTable =  new Machine_TradeTable();
		blockModularTable =  new Machine_ModularityTable();
		blockWitherGuard = new BlockWitherProof();
		blockXpConverter = new BlockTankXpConverter();
		blockCompressedObsidian = new BlockCompressedObsidian();
		blockNet = new BlockNet();

	}


}