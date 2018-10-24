package gtPlusPlus.xmod.galacticraft.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.FluidStack;

public class GalacticUtils {

	static final private Class<?> aTieredRocket;
	static final private Class<?> aLandingPad;
	static final private Class<?> aBuggyPad;
	static final private Class<?> aIDockable;
	static final private Class<?> aIFuelable;
	static final private Method getRocketTier;
	static final private Method getRocket;
	static final private Method getBuggy;

	static {
		Class<?> a1, a2, a3, a4, a5;
		Method m1, m2, m3;
		try {
			a1 = Class.forName("micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket");
			a2 = Class.forName("micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad");
			a3 = Class.forName("micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler");
			a4 = Class.forName("micdoodle8.mods.galacticraft.api.entity.IDockable");
			a5 = Class.forName("micdoodle8.mods.galacticraft.api.entity.IFuelable");			
			m1 = ReflectionUtils.getMethod(a1, "getRocketTier");
			m2 = ReflectionUtils.getMethod(a2, "getDockedEntity");
			m3 = ReflectionUtils.getMethod(a3, "getDockedEntity");			
		}
		catch (Throwable t) {
			a1 = null;
			a2 = null;
			a3 = null;
			a4 = null;
			a5 = null;
			m1 = null;
			m2 = null;
			m3 = null;
		}
		aTieredRocket = a1;
		aLandingPad = a2;
		aBuggyPad = a3;
		aIDockable = a4;
		aIFuelable = a5;
		getRocketTier = m1;
		getRocket = m2;
		getBuggy = m3;	
		if (a1 != null && a2 != null && a3 != null && a4 != null && a5 != null && m1 != null && m2 != null && m3 != null) {
			Logger.SPACE("Successfully relfected into 5 classes and 3 methods.");
		}
		else {
			Logger.SPACE("Failed to relfect into Galacticraft classes and methods.");
			if (a1 == null) {
				Logger.SPACE("micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket was null..");
			}
			if (a2 == null) {
				Logger.SPACE("micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad was null..");
			}
			if (a3 == null) {
				Logger.SPACE("micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler was null..");
			}
			if (a4 == null) {
				Logger.SPACE("micdoodle8.mods.galacticraft.api.entity.IDockable was null..");
			}
			if (a5 == null) {
				Logger.SPACE("micdoodle8.mods.galacticraft.api.entity.IFuelable was null..");
			}
			if (m1 == null) {
				Logger.SPACE("getRocketTier was null..");
			}
			if (m2 == null) {
				Logger.SPACE("getDockedEntity was null..");
			}
			if (m3 == null) {
				Logger.SPACE("getDockedEntity(buggy) was null..");
			}
		}
	}

	
	public static int getRocketTier(Entity aEntity) {		
		if (aTieredRocket.isInstance(aEntity)) {
			if (getRocketTier != null) {
				try {
					return (int) getRocketTier.invoke(aEntity, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}			
		}
		return -1;	
	}

	public static int getRocketTier(Object aEntity) {		
		if (aIFuelable.isInstance(aEntity)) {			
			if (aLandingPad.isInstance(aEntity)) {
				Object rocket;
				try {
					rocket = getRocket.invoke(aLandingPad, new Object[] {});
					if (aIDockable.isInstance(rocket) && rocket != null) {
						return getRocketTier((Entity) rocket);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}			
			else if (aBuggyPad.isInstance(aEntity)) {
				Object buggy;
				try {
					buggy = getBuggy.invoke(aBuggyPad, new Object[] {});
					if (aIDockable.isInstance(buggy) && buggy != null) {
						return 0;
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}
		}
		return -1;
	}

	public static boolean isFuelValidForTier(int aTier, FluidStack aFuel) {
		FluidStack aValidForThisTier = getValidFuelForTier(aTier);
		if (aFuel.isFluidEqual(aValidForThisTier)) {
			return true;
		}
		return false;
	}



	public static FluidStack getValidFuelForTier(Entity aEntity) {
		if (aTieredRocket.isInstance(aEntity)) {
			return getValidFuelForTier(getRocketTier(aEntity));
		}
		else {
			Logger.SPACE("Failed to get valid rocket fuel for "+aEntity.getClass().getCanonicalName());
			return getValidFuelForTier(0);
		}	
	}	

	public static FluidStack getValidFuelForTier(int aTier) {		
		if (aTier > 0 && aTier <= 2) {
			return FluidUtils.getFluidStack(RocketFuels.RP1_Plus_Liquid_Oxygen, 1000);
		}
		else if (aTier >= 3 && aTier <= 5) {
			return FluidUtils.getFluidStack(RocketFuels.Dense_Hydrazine_Mix, 1000);			
		}
		else if (aTier >= 6 && aTier <= 7) {
			return FluidUtils.getFluidStack(RocketFuels.Monomethylhydrazine_Plus_Nitric_Acid, 1000);			
		}
		else if (aTier >= 8 && aTier <= 10) {
			return FluidUtils.getFluidStack(RocketFuels.Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1000);			
		}
		else {			
			if (aTier == 0) {
				return Materials.Fuel.getFluid(1000);
			}			
			return null;			
		}		

	}

}
