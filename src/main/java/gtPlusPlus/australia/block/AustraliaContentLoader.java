package gtPlusPlus.australia.block;

import static gtPlusPlus.australia.dimension.Dimension_Australia.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.australia.biome.type.Biome_AustralianDesert;
import gtPlusPlus.australia.biome.type.Biome_AustralianDesert2;
import gtPlusPlus.australia.item.ItemAustraliaPortalTrigger;
import gtPlusPlus.australia.item.ItemBlockAustralia;

public class AustraliaContentLoader {

	private static Block blockAustralianSand;
	private static Block blockAustralianTopSoil;
	
	public static AutoMap<Block> mValidGenerationBlocks = new AutoMap<Block>();

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
		blockAustralianSand = new BlockAustraliaSand();
		blockAustralianTopSoil = new BlockAustraliaTopSoil();	
		mValidGenerationBlocks.put(blockAustralianSand);
		mValidGenerationBlocks.put(blockAustralianTopSoil);
		
		//Registry
		GameRegistry.registerBlock(portalBlock, ItemBlockAustralia.class, "dimensionAustraliaPortalBlock");
		GameRegistry.registerBlock(blockPortalFrame, ItemBlockAustralia.class, "blockAustraliaPortalFrame");
		
		GameRegistry.registerBlock(blockAustralianSand, ItemBlockAustralia.class, "blockAustralianTopSoil");
		GameRegistry.registerBlock(blockAustralianTopSoil, ItemBlockAustralia.class, "blockAustralianSand");

		//Set Biome Blocks up		
		Biome_AustralianDesert.blockFluidLakes = Blocks.water;
		Biome_AustralianDesert.blockTopLayer = blockAustralianSand;
		Biome_AustralianDesert.blockSecondLayer = blockAustralianTopSoil;
		
		Biome_AustralianDesert2.blockFluidLakes = Blocks.water;
		Biome_AustralianDesert2.blockTopLayer = blockAustralianSand;
		Biome_AustralianDesert2.blockSecondLayer = blockAustralianTopSoil;

		return true;
	}


}
