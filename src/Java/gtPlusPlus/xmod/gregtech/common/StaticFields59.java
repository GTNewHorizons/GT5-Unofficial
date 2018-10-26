package gtPlusPlus.xmod.gregtech.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;

public class StaticFields59 {

	public static final Field mGtBlockCasings5;
	public static final Field mPreventableComponents;
	public static final Field mDisabledItems;
	public static final Method mCalculatePollutionReduction;
	
	//OrePrefixes
	
	static {
		mGtBlockCasings5 = getField(GregTech_API.class, "sBlockCasings5");
		mPreventableComponents = getField(OrePrefixes.class, "mPreventableComponents");
		mDisabledItems = getField(OrePrefixes.class, "mDisabledItems");
		mCalculatePollutionReduction = getMethod(GT_MetaTileEntity_Hatch_Muffler.class, "calculatePollutionReduction", int.class);		
	}

	public static synchronized final Block getBlockCasings5() {
		try {
			return (Block) mGtBlockCasings5.get(GregTech_API.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
	
	public static int calculatePollutionReducation(GT_MetaTileEntity_Hatch_Muffler h, int i) {
		try {
			return (int) mCalculatePollutionReduction.invoke(h, i);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return 0;
		}
	}
	
	public static Field getField(Class a, String b) {
		try {
			return ReflectionUtils.getField(a, b);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	public static Method getMethod(Class a, String b, Class... params) {
		return ReflectionUtils.getMethod(a, b, params);
	}

	public static synchronized final Boolean getOrePrefixesBooleanDisabledItems() {
		try {
			return (Boolean) mDisabledItems.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}

	public static synchronized final Boolean geOrePrefixesBooleanPreventableComponents() {
		try {
			return (Boolean) mPreventableComponents.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return false;
		}
	}
	
	
}
