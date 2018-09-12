package gtPlusPlus.core.util.minecraft.gregtech;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.common.GT_Proxy;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;

public class PollutionUtils {

	private static boolean mIsPollutionEnabled = true;

	static {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {
			mIsPollutionEnabled = mPollution();
		}
		else {
			mIsPollutionEnabled = false;
		}
	}

	public static boolean mPollution() {
		try {
			GT_Proxy GT_Pollution = GT_Mod.gregtechproxy;
			if (GT_Pollution != null) {
				Field mPollution = ReflectionUtils.getField(GT_Pollution.getClass(), "mPollution");
				if (mPollution != null) {
					return mPollution.getBoolean(GT_Pollution);
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
		}
		return false;
	}

	public static boolean addPollution(IGregTechTileEntity te, int pollutionValue) {
		if (mIsPollutionEnabled)
		try {
			Class<?> GT_Pollution = Class.forName("gregtech.common.GT_Pollution");
			if (GT_Pollution != null) {
				Method addPollution = GT_Pollution.getMethod("addPollution", IGregTechTileEntity.class, int.class);
				if (addPollution != null) {
					addPollution.invoke(null, te, pollutionValue);
					return true;
				}
			}
		} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
		}
		return false;
	}

	public static int getPollution(IGregTechTileEntity te) {
		if (mIsPollutionEnabled)
		try {
			Class<?> GT_Pollution = Class.forName("gregtech.common.GT_Pollution");
			if (GT_Pollution != null) {
				Method addPollution = GT_Pollution.getMethod("getPollution", IGregTechTileEntity.class);
				if (addPollution != null) {
					return (int) addPollution.invoke(null, te);
				}
			}
		} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
		}
		return 0;
	}

	public static boolean addPollution(Object aTileOfSomeSort, int pollutionValue) {
		if (mIsPollutionEnabled)
		try {
			Class<?> GT_Pollution = Class.forName("gregtech.common.GT_Pollution");
			if (GT_Pollution != null) {
				Method addPollution = GT_Pollution.getMethod("addPollution", Chunk.class, int.class);
				if (addPollution != null) {
					IHasWorldObjectAndCoords j = (IHasWorldObjectAndCoords) aTileOfSomeSort;
					if (j != null) {
						Chunk c = j.getWorld().getChunkFromBlockCoords(j.getXCoord(), j.getZCoord());
						addPollution.invoke(null, c, pollutionValue);
						return true;
					} else {
						TileEntity t = (TileEntity) aTileOfSomeSort;
						if (t != null) {
							Chunk c = t.getWorldObj().getChunkFromBlockCoords(t.xCoord, t.zCoord);
							addPollution.invoke(null, c, pollutionValue);
							return true;
						}
					}

				}
			}
		} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
		}
		return false;

	}

}
