package gtPlusPlus.xmod.tinkers.util;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraftforge.fluids.Fluid;

public class TinkersUtils {

	private static Object mSmelteryInstance;
	private static Class mSmelteryClassInstance;

	public static Object getSmelteryInstance() {
		if (!LoadedMods.TiCon) {
			return null;
		}
		else {
			if (mSmelteryInstance == null || mSmelteryClassInstance == null) {
				if (mSmelteryClassInstance == null) {
					try {
						mSmelteryClassInstance = Class.forName("tconstruct.library.crafting.Smeltery");
					}
					catch (ClassNotFoundException e) {}
				}
				if (mSmelteryClassInstance != null) {
					try {
						mSmelteryInstance = ReflectionUtils.getField(mSmelteryClassInstance, "instance").get(null);
					}
					catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
					}
				}
			}						
		}		
		if (mSmelteryInstance != null) {
			return mSmelteryInstance;
		}		
		return null;
	}

	public static final boolean isTiConFirstInOD() {
		if (LoadedMods.TiCon) {
			try {
				return (boolean) ReflectionUtils.getField(Class.forName("PHConstruct"), "tconComesFirst").get(null);
			}
			catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
			}
		}
		return false;
	}

	public static final boolean stopTiconLoadingFirst() {
		if (isTiConFirstInOD()) {
			try {
				ReflectionUtils.setFieldValue(Class.forName("PHConstruct"), "tconComesFirst", false);
				if ((boolean) ReflectionUtils.getField(Class.forName("PHConstruct"), "tconComesFirst").get(null) == false) {
					return true;
				}
				//Did not work, let's see where TiCon uses this and prevent it.
				else {
					ItemUtils.getNonTinkersDust("", 1);
				}
			}
			catch (Exception e) {}
		}		
		return false;
	}

	/**
	 * Add a new fluid as a valid Smeltery fuel.
	 * @param fluid The fluid.
	 * @param power The temperature of the fluid. This also influences the melting speed. Lava is 1000.
	 * @param duration How long one "portion" of liquid fuels the smeltery. Lava is 10.
	 */
	public static void addSmelteryFuel (Fluid fluid, int power, int duration){
		ReflectionUtils.invokeVoid(getSmelteryInstance(), "addSmelteryFuel", new Class[] {Fluid.class, int.class, int.class}, new Object[] {fluid, power, duration});
	}

	/**
	 * Returns true if the liquid is a valid smeltery fuel.
	 */
	public static boolean isSmelteryFuel (Fluid fluid){
		return ReflectionUtils.invoke(getSmelteryInstance(), "isSmelteryFuel", new Class[] {Fluid.class}, new Object[] {fluid});
	}

	/**
	 * Returns the power of a smeltery fuel or 0 if it's not a fuel.
	 */
	public static int getFuelPower (Fluid fluid){
		return (int) ReflectionUtils.invokeNonBool(getSmelteryInstance(), "getFuelPower", new Class[] {Fluid.class}, new Object[] {fluid});
	}

	/**
	 * Returns the duration of a smeltery fuel or 0 if it's not a fuel.
	 */
	public static int getFuelDuration (Fluid fluid){
		return (int) ReflectionUtils.invokeNonBool(getSmelteryInstance(), "getFuelDuration", new Class[] {Fluid.class}, new Object[] {fluid});
	}

}
