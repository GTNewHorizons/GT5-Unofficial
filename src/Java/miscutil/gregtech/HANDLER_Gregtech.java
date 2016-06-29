package miscutil.gregtech;

import gregtech.api.util.GT_Config;
import miscutil.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.gregtech.common.blocks.fluid.GregtechFluidHandler;
import miscutil.gregtech.loaders.Processing_Block;
import miscutil.gregtech.loaders.Processing_Ingot1;
import miscutil.gregtech.loaders.Processing_Plate1;

public class HANDLER_Gregtech {
	
	public static GT_Config mMaterialProperties = null;

	public static void preInit(){
		if (mMaterialProperties != null){
			GT_Materials.init(mMaterialProperties);
			GregtechFluidHandler.run();
			new Processing_Ingot1();
			new Processing_Plate1();
			new Processing_Block();
		}
		
	}
	
	public static void init(){
		
	}
	
	public static void postInit(){
		
	}
	
}
