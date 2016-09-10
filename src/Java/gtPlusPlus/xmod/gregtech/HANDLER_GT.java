package gtPlusPlus.xmod.gregtech;

import gregtech.api.util.GT_Config;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.ProcessingToolHeadChoocher;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;

public class HANDLER_GT {
	
	public static GT_Config mMaterialProperties = null;

	public static void preInit(){
		new MetaGeneratedGregtechItems();
		if (mMaterialProperties != null){
			GT_Materials.init(mMaterialProperties);
			GregtechFluidHandler.run();
			//new Processing_Ingot1();
			//new Processing_Plate1();
			//new Processing_Block();
		}
		//new Processing_HotIngots();
	}
	
	public static void init(){
		
		//Add Custom Pipes, Wires and Cables.
		GregtechConduits.run();
		new ProcessingToolHeadChoocher();
		new MetaGeneratedGregtechTools();
		/*if (Meta_GT_Proxy.mSortToTheEnd) {
			new GT_ItemIterator().run();
			Meta_GT_Proxy.registerUnificationEntries();
			new GT_FuelLoader().run();
		}*/
	}
	
	public static void postInit(){
		/*Meta_GT_Proxy.activateOreDictHandler();
		if (Meta_GT_Proxy.mSortToTheEnd) {
			Meta_GT_Proxy.registerUnificationEntries();
		} else {
			new GT_ItemIterator().run();
			Meta_GT_Proxy.registerUnificationEntries();
			new GT_FuelLoader().run();
		}*/
	}
	
}
