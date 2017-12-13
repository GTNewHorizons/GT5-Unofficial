package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.lib.CORE;

public class GTPP_Bees {

    public static ItemCustomComb combs;
    
    public static Materials PTFE;
    public static Materials PBS;
    

    public GTPP_Bees() {
        if (Loader.isModLoaded("Forestry") /*&& tryGetBeesBoolean()*/) {
        	
        	setMaterials();
        	
            combs = new ItemCustomComb();
            combs.initCombsRecipes();
            GTPP_Bee_Definition.initBees();            
        }
    }
    
    private static boolean tryGetBeesBoolean(){
		try {
			Class mProxy = Class.forName("gregtech.GT_Mod.gregtechproxy");
			Field mNerf = FieldUtils.getDeclaredField(mProxy, "mGTBees", true);
			boolean returnValue = (boolean) mNerf.get(GT_Mod.gregtechproxy);
			return returnValue;
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			return false;
		}		
	}
    
    private void setMaterials(){
    	PTFE = trySetValue("Polytetrafluoroethylene");
    	PBS = trySetValue("StyreneButadieneRubber");
    }
    
    private Materials trySetValue(String material){
    	Materials mTemp =  Materials.valueOf(material);
    	if (mTemp != null){
    		return mTemp;
    	}
    	return Materials._NULL;    	
    }
}
