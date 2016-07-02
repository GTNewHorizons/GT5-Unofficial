package miscutil.core.xmod.gregtech;

import gregtech.api.util.GT_Config;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.core.xmod.gregtech.common.blocks.fluid.GregtechFluidHandler;

public class HANDLER_GT {
	
	public static GT_Config mMaterialProperties = null;

	public static void preInit(){
		if (mMaterialProperties != null){
			GT_Materials.init(mMaterialProperties);
			
			//new Processing_Ingot1();
			//new Processing_Plate1();
			//new Processing_Block();
		}
		
	}
	
	public static void init(){
		GregtechFluidHandler.run();
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
