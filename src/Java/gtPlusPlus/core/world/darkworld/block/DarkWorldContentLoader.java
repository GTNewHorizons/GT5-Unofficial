package gtPlusPlus.core.world.darkworld.block;

import static gtPlusPlus.core.world.darkworld.Dimension_DarkWorld.*;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.base.BlockBaseFluid;
import gtPlusPlus.core.item.base.itemblock.ItemBlockBasicTooltip;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.item.ItemBlockToxicEverglades;
import gtPlusPlus.core.world.darkworld.item.ItemDarkWorldPortalTrigger;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidRegistry;

public class DarkWorldContentLoader {

	//Static Vars
	public static BlockDarkWorldSludgeFluid SLUDGE;


	public synchronized static void run() {
		initMisc();
		initItems();
		initBlocks();
	}

	public synchronized static boolean initMisc(){		

		//Fluids
		SLUDGE = (BlockDarkWorldSludgeFluid) new BlockDarkWorldSludgeFluid(
				"sludge", 
				Utils.rgbtoHexValue(30, 130, 30))
				.setDensity(1800)
				.setGaseous(false)
				.setLuminosity(2)
				.setViscosity(25000)
				.setTemperature(300);		
		FluidRegistry.registerFluid(SLUDGE);

		return true;
	}

	public synchronized static boolean initItems(){
		portalItem = (ItemDarkWorldPortalTrigger) (new ItemDarkWorldPortalTrigger().setUnlocalizedName("everglades.trigger"));
		GameRegistry.registerItem(portalItem, "everglades.trigger");	
		return true;
	}

	public synchronized static boolean initBlocks(){		

		//Create Block Instances
		blockFluidLakes = new BlockBaseFluid("Sludge", SLUDGE, BlockDarkWorldSludgeFluid.SLUDGE).setLightLevel(2f).setLightOpacity(1).setBlockName("fluidSludge");
		portalBlock = new BlockDarkWorldPortal();
		blockTopLayer = new BlockDarkWorldGround();
		blockSecondLayer = new BlockDarkWorldPollutedDirt();
		blockPortalFrame = new BlockDarkWorldPortalFrame();
		
		//Registry
		GameRegistry.registerBlock(portalBlock, ItemBlockToxicEverglades.class, "dimensionDarkWorld_portal");
		GameRegistry.registerBlock(blockTopLayer, ItemBlockToxicEverglades.class, "blockDarkWorldGround");
		GameRegistry.registerBlock(blockSecondLayer, ItemBlockToxicEverglades.class, "blockDarkWorldGround2");
		GameRegistry.registerBlock(blockPortalFrame, ItemBlockToxicEverglades.class, "blockDarkWorldPortalFrame");
		
		//Make Flammable
		Blocks.fire.setFireInfo(blockTopLayer, 30, 20);

		return true;
	}


}
