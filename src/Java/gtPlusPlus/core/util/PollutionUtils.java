package gtPlusPlus.core.util;

import java.lang.reflect.*;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.GT_Proxy;

public class PollutionUtils {

	public static boolean mPollution (){
		try {
			GT_Proxy GT_Pollution = GT_Mod.gregtechproxy;
			if (GT_Pollution != null){
				Field mPollution = GT_Pollution.getClass().getField("mPollution");
				if (mPollution != null){
					return mPollution.getBoolean(GT_Pollution);
				}
			}
		}
		catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
			return false;
		}
		return false;
	}

	public static boolean addPollution(IGregTechTileEntity te, int pollutionValue){
		try {
			Class<?> GT_Pollution = Class.forName("gregtech.common.GT_Pollution");
			if (GT_Pollution != null){
				Method addPollution = GT_Pollution.getMethod("addPollution", IGregTechTileEntity.class, int.class);
				if (addPollution != null){
					addPollution.invoke(null, te, pollutionValue);
					return true;
				}
			}
		}
		catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return false;
	}

	public static int getPollution(IGregTechTileEntity te){
		try {
			Class<?> GT_Pollution = Class.forName("gregtech.common.GT_Pollution");
			if (GT_Pollution != null){
				Method addPollution = GT_Pollution.getMethod("getPollution", IGregTechTileEntity.class);
				if (addPollution != null){
					return (int) addPollution.invoke(null, te);
				}
			}
		}
		catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return 0;
		}
		return 0;
	}

}
