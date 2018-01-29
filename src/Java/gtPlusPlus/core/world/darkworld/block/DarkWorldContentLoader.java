package gtPlusPlus.core.world.darkworld.block;

import static gtPlusPlus.core.world.darkworld.Dimension_DarkWorld.*;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.base.BlockBaseFluid;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.world.darkworld.item.itemDarkWorldPortalTrigger;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidRegistry;

public class DarkWorldContentLoader {

	//Static Vars
	public static blockDarkWorldSludgeFluid SLUDGE;


	public synchronized static void run() {
		initMisc();
		initItems();
		initBlocks();
	}

	public synchronized static boolean initMisc(){		

		//Fluids
		SLUDGE = (blockDarkWorldSludgeFluid) new blockDarkWorldSludgeFluid(
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
		portalItem = (itemDarkWorldPortalTrigger) (new itemDarkWorldPortalTrigger().setUnlocalizedName("everglades.trigger"));
		GameRegistry.registerItem(portalItem, "everglades.trigger");	
		return true;
	}

	public synchronized static boolean initBlocks(){		

		//Create Block Instances
		blockFluidLakes = new BlockBaseFluid("Sludge", SLUDGE, blockDarkWorldSludgeFluid.SLUDGE).setLightLevel(2f).setLightOpacity(1).setBlockName("fluidSludge");
		portalBlock = new blockDarkWorldPortal();
		blockTopLayer = new blockDarkWorldGround();
		blockSecondLayer = new blockDarkWorldPollutedDirt();
		blockPortalFrame = new blockDarkWorldPortalFrame();
		
		//Registry
		GameRegistry.registerBlock(blockTopLayer, "blockDarkWorldGround");
		GameRegistry.registerBlock(blockSecondLayer, "blockDarkWorldGround2");
		GameRegistry.registerBlock(blockPortalFrame, "blockDarkWorldPortalFrame");
		
		//Make Flammable
		Blocks.fire.setFireInfo(blockTopLayer, 30, 20);

		return true;
	}


}
