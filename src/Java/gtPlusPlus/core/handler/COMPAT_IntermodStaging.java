package gtPlusPlus.core.handler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gtPlusPlus.xmod.bartcrops.HANDLER_CropsPlusPlus;
import gtPlusPlus.xmod.bop.HANDLER_BiomesOPlenty;
import gtPlusPlus.xmod.computronics.HANDLER_Computronics;
import gtPlusPlus.xmod.forestry.HANDLER_FR;
import gtPlusPlus.xmod.galacticraft.HANDLER_GalactiCraft;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.growthcraft.HANDLER_GC;
import gtPlusPlus.xmod.ic2.HANDLER_IC2;
import gtPlusPlus.xmod.ob.HANDLER_OpenBlocks;
import gtPlusPlus.xmod.railcraft.HANDLER_Railcraft;
import gtPlusPlus.xmod.reliquary.HANDLER_Reliquary;
import gtPlusPlus.xmod.sc2.HANDLER_SC2;
import gtPlusPlus.xmod.thaumcraft.HANDLER_Thaumcraft;
import gtPlusPlus.xmod.thermalfoundation.HANDLER_TF;
import gtPlusPlus.xmod.tinkers.HANDLER_Tinkers;

public class COMPAT_IntermodStaging {

	public static void preInit(FMLPreInitializationEvent preinit){
		HANDLER_GT.preInit();
		HANDLER_GC.preInit();
		HANDLER_TF.preInit();
		HANDLER_FR.preInit();
		HANDLER_IC2.preInit();
		HANDLER_Computronics.preInit();
		HANDLER_BiomesOPlenty.preInit();
		//HANDLER_Mekanism.preInit();
		HANDLER_Thaumcraft.preInit();
		HANDLER_Tinkers.preInit();
		HANDLER_SC2.preInit();
		HANDLER_GalactiCraft.preInit();
		HANDLER_CropsPlusPlus.preInit(preinit);
		HANDLER_Railcraft.preInit();
		HANDLER_Reliquary.preInit();
		HANDLER_OpenBlocks.preInit();
	}

	public static void init(FMLInitializationEvent init){
		HANDLER_GT.init();
		HANDLER_GC.init();
		HANDLER_TF.init();
		HANDLER_FR.Init();
		HANDLER_IC2.init();
		HANDLER_Computronics.init();
		HANDLER_BiomesOPlenty.init();
		//HANDLER_Mekanism.init();
		HANDLER_Thaumcraft.init();
		HANDLER_Tinkers.init();
		HANDLER_SC2.init();
		HANDLER_GalactiCraft.init();
		HANDLER_CropsPlusPlus.init(init);
		HANDLER_Railcraft.init();
		HANDLER_Reliquary.init();
		HANDLER_OpenBlocks.init();
	}

	public static void postInit(FMLPostInitializationEvent postinit){
		HANDLER_GT.postInit();
		HANDLER_GC.postInit();
		HANDLER_TF.postInit();
		HANDLER_FR.postInit();
		HANDLER_IC2.postInit();
		HANDLER_Computronics.postInit();
		HANDLER_BiomesOPlenty.postInit();
		//HANDLER_Mekanism.postInit();
		HANDLER_Thaumcraft.postInit();
		HANDLER_Tinkers.postInit();
		HANDLER_SC2.postInit();
		HANDLER_GalactiCraft.postInit();
		HANDLER_CropsPlusPlus.postInit(postinit);
		HANDLER_Railcraft.postInit();
		HANDLER_Reliquary.postInit();
		HANDLER_OpenBlocks.postInit();
	}

	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		HANDLER_GT.onLoadComplete(event);
	}


}
