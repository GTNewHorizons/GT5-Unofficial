package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.metatileentity.BaseMetaTileEntityEx;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public class Gregtech_Blocks {

	public static void run(){

		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		//ModBlocks.blockMetaTileEntity = new GregtechBlockMachines();
		//registerDefailtGtTe();


		

	}

	//Register default Tile Entity
	private static void registerDefailtGtTe(){
		Utils.LOG_INFO("Registering new GT TileEntities.");

		BaseMetaTileEntityEx tBaseMetaTileEntity = Meta_GT_Proxy.constructBaseMetaTileEntity();

		Utils.LOG_INFO("Testing BaseMetaTileEntity.");
		if (tBaseMetaTileEntity == null) {
			Utils.LOG_INFO("Fatal Error ocurred while initializing TileEntities, crashing Minecraft.");
			throw new RuntimeException("");
		}
		Utils.LOG_INFO("Registering the BaseMetaTileEntityEx.");
		GameRegistry.registerTileEntity(tBaseMetaTileEntity.getClass(), "BaseMetaTileEntityEx");
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", tBaseMetaTileEntity.getClass().getName());
	}

}
