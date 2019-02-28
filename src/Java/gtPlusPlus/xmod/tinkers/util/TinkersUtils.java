package gtPlusPlus.xmod.tinkers.util;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;

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
					mSmelteryClassInstance = ReflectionUtils.getClass("tconstruct.library.crafting.Smeltery");
				}
				if (mSmelteryClassInstance != null) {
					try {
						mSmelteryInstance = ReflectionUtils.getField(mSmelteryClassInstance, "instance").get(null);
					}
					catch (IllegalArgumentException | IllegalAccessException e) {
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
				return (boolean) ReflectionUtils.getField(ReflectionUtils.getClass("PHConstruct"), "tconComesFirst").get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return false;
	}

	public static final boolean stopTiconLoadingFirst() {
		if (isTiConFirstInOD()) {
			try {
				ReflectionUtils.setFinalFieldValue(ReflectionUtils.getClass("PHConstruct"), "tconComesFirst", false);
				if ((boolean) ReflectionUtils.getField(ReflectionUtils.getClass("PHConstruct"), "tconComesFirst").get(null) == false) {
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
	
	public static boolean registerFluidType(String name, Block block, int meta, int baseTemperature, Fluid fluid, boolean isToolpart) {
		
		
		
		//FluidType.registerFluidType(mLocalName, aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(0).getFluid(), true);
		return false;
	}
	
	public static boolean addMelting(Material aMaterial) {
		Smeltery.addMelting(aMaterial.getBlock(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144*9));
		Smeltery.addMelting(aMaterial.getIngot(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144));
		return false;
	}
	
	public static boolean addBasinRecipe(Material aMaterial) {
		TConstructRegistry.getBasinCasting().addCastingRecipe(aMaterial.getBlock(1),
				aMaterial.getFluid(144*9), (ItemStack) null, true, 100);
		return false;
	}
	
	public static boolean addCastingTableRecipe(Material aMaterial) {		
		ItemStack ingotcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);		
		TConstructRegistry.getTableCasting().addCastingRecipe(aMaterial.getIngot(1), aMaterial.getFluid(144), ingotcast, false, 50);
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
