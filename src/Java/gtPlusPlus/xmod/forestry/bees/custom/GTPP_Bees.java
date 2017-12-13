package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.recipes.RecipeManagers;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot_OLD;
import gtPlusPlus.core.item.base.misc.BaseItemMisc;
import gtPlusPlus.core.item.base.misc.BaseItemMisc.MiscTypes;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GTPP_Bees {
	
	//Custom Comb Drop Base Items
	public static Item dropForceGem;
	public static Item dropBiomassBlob;
	public static Item dropEthanolBlob;
	public static Item dropNikoliteDust;
	public static Item dropFluorineBlob;

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
        	
        	for (IAllele o : AlleleManager.alleleRegistry.getRegisteredAlleles().values()){
        		//Utils.LOG_INFO("[Bees-Debug] ==================================================");
        		//Utils.LOG_INFO("[Bees-Debug] Name: "+o.getName());
        		//Utils.LOG_INFO("[Bees-Debug] Name: "+o.getUnlocalizedName());
        		//Utils.LOG_INFO("[Bees-Debug] getUID: "+o.getUID());
        		//Utils.LOG_INFO("[Bees-Debug] isDominant: "+o.isDominant());
        	}
        	
        	//Set Materials and Comb stacks from GT via Reflection
        	setMaterials();
        	setCustomItems();
        	
            combs = new ItemCustomComb();
            combs.initCombsRecipes();
            GTPP_Bee_Definition.initBees();            
        }
    }
    
    private void setCustomItems() {
    	dropForceGem = new BaseItemMisc("Force", new short[]{250, 250, 20}, 64, MiscTypes.GEM, null);
    	dropBiomassBlob = new BaseItemMisc("Biomass", new short[]{33, 225, 24}, 64, MiscTypes.DROP, null);
    	dropEthanolBlob = new BaseItemMisc("Ethanol", new short[]{255, 128, 0}, 64, MiscTypes.DROP, null);
    	
    	//Nikolite may not exist, so lets make it.
    	dropNikoliteDust = ItemUtils.generateSpecialUseDusts("Nikolite", "Nikolite", Utils.rgbtoHexValue(60, 180, 200))[2];
    	if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotNikolite", 1) == null){
    		new BaseItemIngot_OLD("itemIngotNikolite", "Nikolite", Utils.rgbtoHexValue(60, 180, 200), 0);
    	}
    	
    	dropFluorineBlob = new BaseItemMisc("Fluorine", new short[]{30, 230, 230}, 64, MiscTypes.DROP, null);
	}
    
    private void addRecipes(){
    	addExtractorRecipe(ItemUtils.getSimpleStack(dropBiomassBlob), FluidUtils.getFluidStack("biomass", 30));
    	addExtractorRecipe(ItemUtils.getSimpleStack(dropEthanolBlob), FluidUtils.getFluidStack("ethanol", 6));
    	addExtractorRecipe(ItemUtils.getSimpleStack(dropFluorineBlob), FluidUtils.getFluidStack("fluid.fluorine", 4));		
    }
    
    private boolean addExtractorRecipe(ItemStack input, FluidStack output){
    	return GT_Values.RA.addFluidExtractionRecipe(
    			input, 
    			null, 
    			output, 
    			0, 
    			30, 
    			8);
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
    	Object oCombObject = gtCombs.get(null);

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
    	
    	
    	//if (gtCombItemClass.isInstance(oCombObject)){
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
    	/*} 	
    	else {
    		Utils.LOG_INFO("[Bees] oCombObject was not an instance of gregtech.common.items.ItemComb");
    	}*/
    	
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
