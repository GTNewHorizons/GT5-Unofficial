package miscutil.gregtech;

import gregtech.api.util.GT_Config;
import miscutil.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.gregtech.common.blocks.fluid.GregtechFluidHandler;

public class HANDLER_Gregtech {
	
	public static GT_Config mMaterialProperties = null;

	public static void preInit(){
		if (mMaterialProperties != null){
			GT_Materials.init(mMaterialProperties);
		}	
		GregtechFluidHandler.run();
	}
	
	public static void init(){
		
	}
	
	public static void postInit(){
		
	}
	
}
