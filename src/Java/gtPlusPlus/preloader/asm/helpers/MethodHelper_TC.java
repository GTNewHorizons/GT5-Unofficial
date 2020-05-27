package gtPlusPlus.preloader.asm.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class MethodHelper_TC {
	
	private static Class mThaumcraftCraftingManager;

    public static AspectList generateTags(final Item item, final int meta, final ArrayList<List> history) {
        int tmeta = meta;
        if (item == null) {
        	return null;
        }
        //Preloader_Logger.INFO("Generating aspect tags for "+item.getUnlocalizedName()+":"+meta);
        try {
            tmeta = ((new ItemStack(item, 1, meta).getItem().isDamageable() || !new ItemStack(item, 1, meta).getItem().getHasSubtypes()) ? 32767 : meta);
        }
        catch (Exception ex) {}
        //Preloader_Logger.INFO("Set Meta to "+tmeta);
        if (ThaumcraftApi.exists(item, tmeta)) {
            return ThaumcraftCraftingManager.getObjectTags(new ItemStack(item, 1, tmeta));
        }
        if (history.contains(Arrays.asList(item, tmeta))) {
            return null;
        }
        history.add(Arrays.asList(item, tmeta));
        if (history.size() < 100) {
            AspectList ret = generateTagsFromRecipes(item, (tmeta == 32767) ? 0 : meta, history);
            ret = capAspects(ret, 64);
            ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, tmeta), ret);
            return ret;
        }
        return null;
    }
    
    private static AspectList capAspects(final AspectList sourcetags, final int amount) {
        if (sourcetags == null) {
            return sourcetags;
        }
        final AspectList out = new AspectList();
        for (final Aspect aspect : sourcetags.getAspects()) {
            out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
        }
        return out;
    }
    
    private static AspectList generateTagsFromRecipes(final Item item, final int meta, final ArrayList<List> history) {
        AspectList ret = null;
        ret = generateTagsFromCrucibleRecipes(item, meta, history);
        if (ret != null) {
            return ret;
        }
        ret = generateTagsFromArcaneRecipes(item, meta, history);
        if (ret != null) {
            return ret;
        }
        ret = generateTagsFromInfusionRecipes(item, meta, history);
        if (ret != null) {
            return ret;
        }
        ret = generateTagsFromCraftingRecipes(item, meta, history);
        return ret;
    }
    
    private static boolean isClassSet() {
    	if (mThaumcraftCraftingManager == null) {
    		mThaumcraftCraftingManager = ReflectionUtils.getClass("thaumcraft.common.lib.crafting.ThaumcraftCraftingManager");
    	}    	
    	return true;
    }

    private static Method mGetTagsFromCraftingRecipes;
    private static Method mGetTagsFromInfusionRecipes;
    private static Method mGetTagsFromArcaneRecipes;
    private static Method mGetTagsFromCrucibleRecipes;
    
	private static AspectList generateTagsFromCraftingRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromCraftingRecipes == null) {
			mGetTagsFromCraftingRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromCraftingRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromCraftingRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromInfusionRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromInfusionRecipes == null) {
			mGetTagsFromInfusionRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromInfusionRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromInfusionRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromArcaneRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromArcaneRecipes == null) {
			mGetTagsFromArcaneRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromArcaneRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromArcaneRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromCrucibleRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromCrucibleRecipes == null) {
			mGetTagsFromCrucibleRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromCrucibleRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromCrucibleRecipes, new Object[] {item, meta, history});
	}
	
}
