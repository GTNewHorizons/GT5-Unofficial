package gtPlusPlus.xmod.tinkers.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TinkersUtils {

	private static Object mSmelteryInstance;
	private static Class mSmelteryClassInstance;
	
	private static Object mTinkersRegistryInstance;
	private static Class mTinkersRegistryClass;
	
	private static final Class mToolMaterialClass;

	private static final HashMap<String, Method> mMethodCache = new LinkedHashMap<String, Method>();
		
	
	static {
		setRegistries();	
		mToolMaterialClass = ReflectionUtils.getClass("tconstruct.library.tools.ToolMaterial");	
	}
	
	
	/**
	 * 
	 * @param aSwitch - The Registry to return
	 */
	private static Object getTiConDataInstance(int aSwitch) {
		if (!LoadedMods.TiCon) {
			return null;
		}
		else {

			if (mTinkersRegistryClass == null || mSmelteryClassInstance == null) {
				setRegistries();				
			}
			
			// getSmelteryInstance
			if (aSwitch == 0) {
				
				//Set Smeltery Instance
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
				
				//Return Smeltery Instance
				if (mSmelteryInstance != null) {
					return mSmelteryInstance;
				}
			}
			
			// getTableCastingInstance || getBasinCastingInstance
			else if (aSwitch == 1) {			
				if (mTinkersRegistryClass == null || mTinkersRegistryInstance == null) {					
					if (mTinkersRegistryClass == null) {
						mTinkersRegistryClass = ReflectionUtils.getClass("tconstruct.library.TConstructRegistry");
					}
					if (mTinkersRegistryClass != null) {						
						if (mTinkersRegistryInstance == null) {
							try {
								mTinkersRegistryInstance = ReflectionUtils.getField(mTinkersRegistryClass, "instance").get(null);
							}
							catch (IllegalArgumentException | IllegalAccessException e) {
							}
						}
					}					
				}
				
				//Return Smeltery Instance
				if (mTinkersRegistryInstance != null) {
					return mTinkersRegistryInstance;
				}
			}

			return null;
		}
	}
	
	private static void setRegistries() {
		if (mTinkersRegistryClass == null) {
			mTinkersRegistryClass = ReflectionUtils.getClass("tconstruct.library.TConstructRegistry");
		}
		if (mSmelteryClassInstance == null) {
			mSmelteryClassInstance = ReflectionUtils.getClass("tconstruct.library.crafting.Smeltery");
		}
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
		ReflectionUtils.invokeVoid(getTiConDataInstance(0), "addSmelteryFuel", new Class[] {Fluid.class, int.class, int.class}, new Object[] {fluid, power, duration});
	}

	/**
	 * Returns true if the liquid is a valid smeltery fuel.
	 */
	public static boolean isSmelteryFuel (Fluid fluid){
		return ReflectionUtils.invoke(getTiConDataInstance(0), "isSmelteryFuel", new Class[] {Fluid.class}, new Object[] {fluid});
	}

	/**
	 * Returns the power of a smeltery fuel or 0 if it's not a fuel.
	 */
	public static int getFuelPower (Fluid fluid){
		return (int) ReflectionUtils.invokeNonBool(getTiConDataInstance(0), "getFuelPower", new Class[] {Fluid.class}, new Object[] {fluid});
	}

	/**
	 * Returns the duration of a smeltery fuel or 0 if it's not a fuel.
	 */
	public static int getFuelDuration (Fluid fluid){
		return (int) ReflectionUtils.invokeNonBool(getTiConDataInstance(0), "getFuelDuration", new Class[] {Fluid.class}, new Object[] {fluid});
	}
	
	
	
	
	
	
	
	public static boolean registerFluidType(String name, Block block, int meta, int baseTemperature, Fluid fluid, boolean isToolpart) {
		if (mMethodCache.get("registerFluidType") == null) {
			Method m = ReflectionUtils.getMethod(ReflectionUtils.getClass("tconstruct.library.crafting.FluidType"), "registerFluidType", String.class, Block.class, int.class, int.class, Fluid.class, boolean.class);
			mMethodCache.put("registerFluidType", m);
		}
		try {
			mMethodCache.get("registerFluidType").invoke(null, name, block, meta, baseTemperature, fluid, isToolpart);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	public static boolean addBaseMeltingRecipes(Material aMaterial) {		
		return addMelting(aMaterial.getBlock(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144*9)) &&
		addMelting(aMaterial.getIngot(1), aMaterial.getBlock(), 0, aMaterial.getMeltingPointC(), aMaterial.getFluid(144));
	}
	
	public static boolean addMelting(ItemStack input, Block block, int metadata, int temperature, FluidStack liquid) {
		if (mMethodCache.get("addMelting") == null) {
			Method m = ReflectionUtils.getMethod(mSmelteryClassInstance, "addMelting", ItemStack.class, Block.class, int.class, int.class, FluidStack.class);
			mMethodCache.put("addMelting", m);
		}
		try {			
			mMethodCache.get("addMelting").invoke(null, input, block, metadata, temperature, liquid);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static boolean addBaseBasinRecipes(Material aMaterial) {
		return addBasinRecipe(aMaterial.getBlock(1), aMaterial.getFluid(144*9), (ItemStack) null, true, 100);
	}
	
	public static boolean addBasinRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume, int delay) {
		if (mMethodCache.get("addBasinRecipe") == null) {					
			Method m = ReflectionUtils.getMethod(ReflectionUtils.getClass("tconstruct.library.crafting.LiquidCasting"), "addCastingRecipe", ItemStack.class, FluidStack.class, ItemStack.class, boolean.class, int.class);
			mMethodCache.put("addBasinRecipe", m);
		}
		try {
			mMethodCache.get("addBasinRecipe").invoke(getCastingInstance(0), output, metal, cast, consume, delay);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
	}
	
	
	
	
	
	
	
	public static boolean addBaseCastingRecipes(Material aMaterial) {
		ItemStack ingotcast = getPattern(1);		
		return addCastingTableRecipe(aMaterial.getIngot(1), aMaterial.getFluid(144), ingotcast, false, 50);
	}
	
	public static boolean addCastingTableRecipe(ItemStack output, FluidStack metal, ItemStack cast, boolean consume, int delay) {		
		if (mMethodCache.get("addCastingTableRecipe") == null) {					
			Method m = ReflectionUtils.getMethod(ReflectionUtils.getClass("tconstruct.library.crafting.LiquidCasting"), "addCastingRecipe", ItemStack.class, FluidStack.class, ItemStack.class, boolean.class, int.class);
			mMethodCache.put("addCastingTableRecipe", m);
		}
		try {
			mMethodCache.get("addCastingTableRecipe").invoke(getCastingInstance(1), output, metal, cast, consume, delay);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
	}
	
	
	
	
	
	
	
	
	public static Object getCastingInstance(int aType) {
		
		
		
		Method m = null;
		if (aType == 0) {
			m = ReflectionUtils.getMethod(getTiConDataInstance(1), "getTableCasting", new Class[] {});			
			//return ReflectionUtils.invokeVoid(getTiConDataInstance(1), "getTableCasting", new Class[] {}, new Object[] {});			
		}
		else if (aType == 1) {
			m = ReflectionUtils.getMethod(getTiConDataInstance(1), "getBasinCasting", new Class[] {});
			//return ReflectionUtils.invokeVoid(getTiConDataInstance(1), "getBasinCasting", new Class[] {}, new Object[] {});			
		}
		else {
			//return null;
		}
		
		if (m != null) {
			try {
				return m.invoke(getTiConDataInstance(1), new Object[] {});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	private static Item mTinkerMetalPattern;
	public static ItemStack getPattern(int aType) {		
		if (mTinkerMetalPattern == null) {
			Field m = ReflectionUtils.getField(ReflectionUtils.getClass("tconstruct.smeltery.TinkerSmeltery"), "metalPattern");
			if (m != null) {
				try {
					mTinkerMetalPattern = (Item) m.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}				
			}
		}
		if (mTinkerMetalPattern != null) {
			ItemStack ingotCast = new ItemStack(mTinkerMetalPattern, aType, 0);	
			return ingotCast;
		}
		return ItemUtils.getErrorStack(1, "Bad Tinkers Pattern");
		
		
		
		
		
		
		
	}	
	
	/**
	 * Generates Tinkers {@link ToolMaterial}'s reflectively.
	 * @param name
	 * @param localizationString
	 * @param level
	 * @param durability
	 * @param speed
	 * @param damage
	 * @param handle
	 * @param reinforced
	 * @param stonebound
	 * @param style
	 * @param primaryColor
	 * @return
	 */
	public static Object generateToolMaterial(String name, String localizationString, int level, int durability, int speed, int damage,	float handle, int reinforced, float stonebound, String style, int primaryColor) {
		try {
			Constructor constructor = mToolMaterialClass.getConstructor(String.class, String.class, int.class, int.class, int.class, int.class, float.class, int.class, float.class, String.class, int.class);
			Object myObject = constructor.newInstance(name, localizationString, level, durability, speed, damage, handle, reinforced, stonebound, style, primaryColor);
			return myObject;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void addToolMaterial(int id, Object aToolMaterial) {	
		setRegistries();		
		if (mMethodCache.get("addToolMaterial") == null) {					
			Method m = ReflectionUtils.getMethod(mTinkersRegistryClass, "addtoolMaterial", int.class, mToolMaterialClass);
			mMethodCache.put("addToolMaterial", m);
		}
		try {
			mMethodCache.get("addToolMaterial").invoke(mTinkersRegistryClass, id, aToolMaterial);			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			
		}		
	}
	
	public static void addDefaultToolPartMaterial(int id) {		
		setRegistries();		
		if (mMethodCache.get("addDefaultToolPartMaterial") == null) {					
			Method m = ReflectionUtils.getMethod(mTinkersRegistryClass, "addDefaultToolPartMaterial", int.class);
			mMethodCache.put("addDefaultToolPartMaterial", m);
		}
		try {
			mMethodCache.get("addDefaultToolPartMaterial").invoke(mTinkersRegistryClass, id);			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {			
		}
	}
	
	public static void addBowMaterial(int id, int drawspeed, float maxSpeed) {		
		setRegistries();		
		if (mMethodCache.get("addBowMaterial") == null) {					
			Method m = ReflectionUtils.getMethod(mTinkersRegistryClass, "addBowMaterial", int.class, int.class, float.class);
			mMethodCache.put("addBowMaterial", m);
		}
		try {
			mMethodCache.get("addBowMaterial").invoke(mTinkersRegistryClass, id, drawspeed, maxSpeed);			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {			
		}
	}
	
	public static void addArrowMaterial(int id, float mass, float fragility) {		
		setRegistries();		
		if (mMethodCache.get("addArrowMaterial") == null) {					
			Method m = ReflectionUtils.getMethod(mTinkersRegistryClass, "addArrowMaterial", int.class, float.class, float.class);
			mMethodCache.put("addArrowMaterial", m);
		}
		try {
			mMethodCache.get("addArrowMaterial").invoke(mTinkersRegistryClass, id, mass, fragility);			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {			
		}
	}	

}
