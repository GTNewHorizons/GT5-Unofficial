package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;
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

	//public static GTPP_Branch_Definition definition;
    

    public GTPP_Bees() {
        if (Loader.isModLoaded("Forestry") /*&& tryGetBeesBoolean()*/) {
        	
        	//Set Bee Defs from GT via Reflection
        	//definition = new GTPP_Branch_Definition();
        	
        	//Set Materials and Comb stacks from GT via Reflection
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
    	gtCombs.setAccessible(true);
    	ReflectionUtils.makeAccessible(gtCombs);
    	Enum gtCombTypeSlag = Enum.valueOf(gtCombEnumClass, "SLAG");
    	Enum gtCombTypeStone = Enum.valueOf(gtCombEnumClass, "STONE");    	
    	Item oCombObject = (Item) gtCombs.get(null);

    	Utils.LOG_INFO("[Bees-Debug] Field getModifiers: "+gtCombs.getModifiers());
    	Utils.LOG_INFO("[Bees-Debug] Field toGenericString: "+gtCombs.toGenericString());
    	Utils.LOG_INFO("[Bees-Debug] Field getClass: "+gtCombs.getClass());
    	Utils.LOG_INFO("[Bees-Debug] Field isEnumConstant: "+gtCombs.isEnumConstant());
    	Utils.LOG_INFO("[Bees-Debug] Field isSynthetic: "+gtCombs.isSynthetic());
    	Utils.LOG_INFO("[Bees-Debug] Field get(gtBees) != null: "+(gtCombs.get(gtBees) != null));
    	Utils.LOG_INFO("[Bees-Debug] Field isAccessible: "+gtCombs.isAccessible());
    	    	

		Utils.LOG_INFO("[Bees] gtBees: "+(gtBees != null));
		Utils.LOG_INFO("[Bees] gtCombItemClass: "+(gtCombItemClass != null));
		Utils.LOG_INFO("[Bees] gtCombEnumClass: "+(gtCombEnumClass != null));
		Utils.LOG_INFO("[Bees] gtCombs: "+(gtCombs != null));
		Utils.LOG_INFO("[Bees] gtCombTypeSlag: "+(gtCombTypeSlag != null));
		Utils.LOG_INFO("[Bees] gtCombTypeStone: "+(gtCombTypeStone != null));
		Utils.LOG_INFO("[Bees] oCombObject: "+(oCombObject != null));
    	
    	
    	if (gtCombItemClass.isInstance(oCombObject)){
    		Method getStackForType;
    		getStackForType = gtCombItemClass.getDeclaredMethod("getStackForType", gtCombEnumClass);
    		
        	if (getStackForType != null) {
        		Utils.LOG_INFO("[Bees] Found Method: getStackForType");
        	}
    		if (Comb_Slag == null){
    			Comb_Slag = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeSlag);
        	}
        	if (Comb_Stone == null){
        		Comb_Stone = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeStone);        		
        	}
    	} 	
    	else {
    		Utils.LOG_INFO("[Bees] oCombObject was not an instance of gregtech.common.items.ItemComb");
    	}
    	
    	}
    	catch (NullPointerException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e){
    		Utils.LOG_INFO("[Bees] Bad Reflection. setMaterials()");
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
