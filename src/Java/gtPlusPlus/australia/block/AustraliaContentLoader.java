package gtPlusPlus.australia.block;

import static gtPlusPlus.australia.dimension.Dimension_Australia.*;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.init.Blocks;
import gtPlusPlus.australia.biome.Biome_AustralianDesert;
import gtPlusPlus.australia.item.ItemAustraliaPortalTrigger;
import gtPlusPlus.australia.item.ItemBlockAustralia;

public class AustraliaContentLoader {

	public synchronized static void run() {
		initMisc();
		initItems();
		initBlocks();
	}

	public synchronized static boolean initMisc(){
		return true;
	}

	public synchronized static boolean initItems(){
		portalItem = (ItemAustraliaPortalTrigger) (new ItemAustraliaPortalTrigger().setUnlocalizedName("australia.trigger"));
		GameRegistry.registerItem(portalItem, "australia.trigger");	
		return true;
	}

	public synchronized static boolean initBlocks(){		

		//Create Block Instances
		portalBlock = new BlockAustraliaPortal();
		blockPortalFrame = new BlockDarkWorldPortalFrame();
		
		Biome_AustralianDesert.blockFluidLakes = Blocks.water;
		Biome_AustralianDesert.blockTopLayer = new BlockAustraliaSand();
		Biome_AustralianDesert.blockSecondLayer = new BlockAustraliaTopSoil();
		
		
		//Registry
		GameRegistry.registerBlock(portalBlock, ItemBlockAustralia.class, "dimensionAustraliaPortalBlock");
		GameRegistry.registerBlock(blockPortalFrame, ItemBlockAustralia.class, "blockAustraliaPortalFrame");
		
		GameRegistry.registerBlock(Biome_AustralianDesert.blockTopLayer, ItemBlockAustralia.class, "blockAustralianTopSoil");
		GameRegistry.registerBlock(Biome_AustralianDesert.blockSecondLayer, ItemBlockAustralia.class, "blockAustralianSand");

		return true;
	}


}
