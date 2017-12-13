package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;

public class GTPP_Bees {

	//Base Comb Item
    public static ItemCustomComb combs;

    //Combs obtained via reflection
    public static ItemStack Comb_Slag;
    public static ItemStack Comb_Stone;
    
    //Materials obtained via reflection
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
			Class<?> mProxy = Class.forName("gregtech.GT_Mod.gregtechproxy");
			Field mNerf = FieldUtils.getDeclaredField(mProxy, "mGTBees", true);
			boolean returnValue = (boolean) mNerf.get(GT_Mod.gregtechproxy);
			return returnValue;
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			return false;
		}		
	}
    
    private void setMaterials(){
    	try {
    		
    	Class<?> gtBees = Class.forName("gregtech.loaders.misc.GT_Bees");
    	Class<?> gtCombItemClass = Class.forName("gregtech.common.items.ItemComb");
    	Class gtCombEnumClass = Class.forName("gregtech.common.items.CombType");
    	Field gtCombs = FieldUtils.getDeclaredField(gtBees, "combs", true);
    	Enum gtCombTypeSlag = Enum.valueOf(gtCombEnumClass, "SLAG");
    	Enum gtCombTypeStone = Enum.valueOf(gtCombEnumClass, "STONE");    	
    	Object oCombObject = gtCombs.get(gtBees);
    	
    	if (gtCombItemClass.isInstance(oCombObject)){
    		Method getStackForType;
    		getStackForType = gtCombItemClass.getDeclaredMethod("getStackForType", gtCombEnumClass);
    		if (Comb_Slag == null){
    			Comb_Slag = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeSlag);
        	}
        	if (Comb_Stone == null){
        		Comb_Stone = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeStone);        		
        	}
    	} 	
    	
    	}
    	catch (NullPointerException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e){
    		
    	}
    	
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
