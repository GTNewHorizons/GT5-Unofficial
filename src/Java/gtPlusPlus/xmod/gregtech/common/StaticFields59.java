package gtPlusPlus.xmod.gregtech.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.common.GT_Proxy;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ProxyFinder;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class StaticFields59 {

	
	public static boolean mGT6StylePipes;
	
	public static final Field mGtBlockCasings5;
	public static final Field mPreventableComponents;
	public static final Field mDisabledItems;
	public static final Field mMultiblockChemicalRecipes;
	public static final Field mPyrolyseRecipes;
	public static final Field mDescriptionArray;
	public static final Field mCasingTexturePages;
	public static final Field mAssLineVisualMapNEI;
	public static final GT_Recipe_Map sAssemblylineVisualRecipes;
	
	public static final Method mCalculatePollutionReduction;
	public static final Method mAddFurnaceRecipe;

	private static final Map<String, Materials> mMaterialCache = new LinkedHashMap<String, Materials>();


	//OrePrefixes

	static {
		Logger.INFO("[SH] Creating Static Helper for various fields which require reflective access.");
		
		mGtBlockCasings5 = getField(GregTech_API.class, "sBlockCasings5");
		Logger.INFO("[SH] Got Field: sBlockCasings5");
		mPreventableComponents = getField(OrePrefixes.class, "mPreventableComponents");
		Logger.INFO("[SH] Got Field: mPreventableComponents");
		mDisabledItems = getField(OrePrefixes.class, "mDisabledItems");
		Logger.INFO("[SH] Got Field: mDisabledItems");
		mDescriptionArray = getField(GT_MetaTileEntity_TieredMachineBlock.class, "mDescriptionArray");
		Logger.INFO("[SH] Got Field: mDescriptionArray");
		mCasingTexturePages = getField(BlockIcons.class, "casingTexturePages");
		Logger.INFO("[SH] Got Field: casingTexturePages");

		mAssLineVisualMapNEI = getField(GT_Recipe_Map.class, "sAssemblylineVisualRecipes");
		Logger.INFO("[SH] Got Field: mAssLineVisualMapNEI");
		GT_Recipe_Map aTemp;
		if (mAssLineVisualMapNEI != null) {
			try {
				aTemp = (GT_Recipe_Map) mAssLineVisualMapNEI.get(null);
				Logger.INFO("[SH] Got Field: sAssemblylineVisualRecipes");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				aTemp = null;
			}
		} else {
			aTemp = null;
		}

		sAssemblylineVisualRecipes = aTemp;
		
		mMultiblockChemicalRecipes = getField(GT_Recipe_Map.class, "sMultiblockChemicalRecipes");
		Logger.INFO("[SH] Got Field: sMultiblockChemicalRecipes");	
		if (ReflectionUtils.doesFieldExist(GT_Recipe.GT_Recipe_Map.class, "sPyrolyseRecipes")) {
			mPyrolyseRecipes = getField(GT_Recipe_Map.class, "sPyrolyseRecipes");
			Logger.INFO("[SH] Got Field: sPyrolyseRecipes");
		}
		else {
			mPyrolyseRecipes = null;
		}

		mCalculatePollutionReduction = getMethod(GT_MetaTileEntity_Hatch_Muffler.class, "calculatePollutionReduction",
				int.class);
		Logger.INFO("[SH] Got Method: calculatePollutionReduction");

		// Yep...
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			mAddFurnaceRecipe = getMethod(GT_ModHandler.class, "addSmeltingAndAlloySmeltingRecipe", ItemStack.class,
					ItemStack.class);
			Logger.INFO("[SH] Got Method: addSmeltingAndAlloySmeltingRecipe");
		} else {
			mAddFurnaceRecipe = getMethod(GT_ModHandler.class, "addSmeltingAndAlloySmeltingRecipe", ItemStack.class,
					ItemStack.class, boolean.class);
			Logger.INFO("[SH] Got Method: addSmeltingAndAlloySmeltingRecipe");
		}

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
		return ReflectionUtils.getField(a, b);		
	}

	public static Method getMethod(Class a, String b, Class... params) {
		return ReflectionUtils.getMethod(a, b, params);
	}

	public static synchronized final Collection<Materials> getOrePrefixesBooleanDisabledItems() {
		try {
			return (Collection<Materials>) mDisabledItems.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new ArrayList<Materials>();
		}
	}
	

	public static synchronized final List<OrePrefixes> geOrePrefixesBooleanPreventableComponents() {
		try {
			return (List<OrePrefixes>) mPreventableComponents.get(OrePrefixes.class);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new ArrayList<OrePrefixes>();
		}
	}

	public static synchronized final GT_Recipe_Map getLargeChemicalReactorRecipeMap() {
		try {
			return (GT_Recipe_Map) mMultiblockChemicalRecipes.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	public static synchronized final GT_Recipe_Map getPyrolyseRecipeMap() {
		try {
			return mPyrolyseRecipes != null ? (GT_Recipe_Map) mPyrolyseRecipes.get(null) : null;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	public static Materials getMaterial(String aMaterialName) {
		Materials m = mMaterialCache.get(aMaterialName);
		if (m != null) {
			return m;
		}
		else {			
			m = Materials.get(aMaterialName);
			if (m != null) {
				mMaterialCache.put(aMaterialName, m);
				return m;
			}
			return null;
		}
	}
	
	public static String[] getDescriptionArray(GT_MetaTileEntity_TieredMachineBlock aTile) {
		try {
			return (String[]) mDescriptionArray.get(aTile);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return new String[] {aTile.mDescription};
		}
	}
	
	public static ITexture getCasingTexturePages(int a, int b) {
		try {
			ITexture[][] g = (ITexture[][]) mCasingTexturePages.get(null);
			if (g != null) {
				return g[a][b];
			}
		}
		catch (Throwable t) {
			
		}
		return null;
	}


	public static Object getFieldFromGregtechProxy(String fieldName) {
		return getFieldFromGregtechProxy(Utils.isServer() ? false : true, fieldName);
	}
	
	public static Object getFieldFromGregtechProxy(boolean client, String fieldName) {
		Object proxyGT;
	
		if (Meta_GT_Proxy.mProxies[0] != null && client) {
			proxyGT = Meta_GT_Proxy.mProxies[0];
		} else if (Meta_GT_Proxy.mProxies[1] != null && !client) {
			proxyGT = Meta_GT_Proxy.mProxies[1];
		} else {
			try {
				proxyGT = (client ? ProxyFinder.getClientProxy(GT_Mod.instance)
						: ProxyFinder.getServerProxy(GT_Mod.instance));
			} catch (final ReflectiveOperationException e1) {
				proxyGT = null;
				Logger.INFO("Failed to obtain instance of GT " + (client ? "Client" : "Server") + " proxy.");
			}
			if (Meta_GT_Proxy.mProxies[0] == null && client) {
				Meta_GT_Proxy.mProxies[0] = (GT_Proxy) proxyGT;
			} else if (Meta_GT_Proxy.mProxies[1] == null && !client) {
				Meta_GT_Proxy.mProxies[1] = (GT_Proxy) proxyGT;
			}
		}
	
		if (proxyGT != null && proxyGT instanceof GT_Proxy) {
			try {
				return ReflectionUtils.getField(proxyGT.getClass(), fieldName).get(proxyGT);
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return null;
	}
	
	public static int getHeatingCapacityForCoil(Block aBlock, int aMeta) {		
		if (aBlock == GregTech_API.sBlockCasings1 && (aMeta >= 12 && aMeta <= 14)) {
			return getHeatingCapacityForCoilTier(aMeta == 12 ? 1 : aMeta == 13 ? 2 : 3);
		}
		else if (aBlock == getBlockCasings5() && (aMeta >= 0 && aMeta <= 8)) {
			return getHeatingCapacityForCoilTier(aMeta);
		}
		return 0;
	}
	
	public static int getHeatingCapacityForCoilTier(int aCoilTier) {
		int mHeatingCapacity = 0;
		switch (aCoilTier) {
		case 0:
			mHeatingCapacity = 1800;
			break;
		case 1:
			mHeatingCapacity = 2700;
			break;
		case 2:
			mHeatingCapacity = 3600;
			break;
		case 3:
			mHeatingCapacity = 4500;
			break;
		case 4:
			mHeatingCapacity = 5400;
			break;
		case 5:
			mHeatingCapacity = 7200;
			break;
		case 6:
			mHeatingCapacity = 9000;
			break;
		case 7:
			mHeatingCapacity = 9900;
			break;
		case 8:
			mHeatingCapacity = 10800;
			break;
		default:
			Logger.INFO("Heating Coils are bad.");
			mHeatingCapacity = 0;
		}
		if (CORE.GTNH && aCoilTier <= 6) {
			mHeatingCapacity += 1;
		}
		
		return mHeatingCapacity;
	}


}
