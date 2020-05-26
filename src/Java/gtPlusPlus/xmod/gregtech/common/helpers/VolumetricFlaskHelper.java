package gtPlusPlus.xmod.gregtech.common.helpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class VolumetricFlaskHelper {
	
	private static final Class sClassVolumetricFlask;
	private static final Method sMethodGetFlaskMaxCapacity;
	private static Item mFlask;

	static {
		if (Meta_GT_Proxy.sDoesVolumetricFlaskExist) {
			sClassVolumetricFlask = ReflectionUtils.getClass("gregtech.common.items.GT_VolumetricFlask");
			sMethodGetFlaskMaxCapacity = ReflectionUtils.getMethod(sClassVolumetricFlask, "getMaxCapacity", new Class[] {});
		}
		else {
			sClassVolumetricFlask = null;
			sMethodGetFlaskMaxCapacity = null;
		}
	}

	public static ItemStack getVolumetricFlask(int aAmount) {
		ItemStack aFlask = ItemUtils.getValueOfItemList("VOLUMETRIC_FLASK", aAmount, (ItemStack) null);
		return aFlask;
	}
	
	public static ItemStack getLargeVolumetricFlask(int aAmount) {
		ItemStack aFlask = GregtechItemList.VOLUMETRIC_FLASK_8k.get(aAmount);
		return aFlask;
	}
	
	public static ItemStack getGiganticVolumetricFlask(int aAmount) {
		ItemStack aFlask = GregtechItemList.VOLUMETRIC_FLASK_64k.get(aAmount);
		return aFlask;
	}
	
	public static boolean isVolumetricFlask(ItemStack aStack) {
		if (mFlask == null) {
			ItemStack aFlask = ItemUtils.getValueOfItemList("VOLUMETRIC_FLASK", 1, (ItemStack) null);
			if (aFlask != null) {
				mFlask = aFlask.getItem();				
			}
		}
		if (aStack.getItem() == mFlask) {
			return true;
		}
		return false;
	}
	
	public static int getMaxFlaskCapacity(ItemStack aStack) {
		if (aStack != null && sMethodGetFlaskMaxCapacity != null) {
			Item aItem = aStack.getItem();
			if (sClassVolumetricFlask.isInstance(aItem)) {
				int aMaxCapacity = (int) ReflectionUtils.invokeNonBool(aItem, sMethodGetFlaskMaxCapacity, new Object[] {});
				return aMaxCapacity;
			}
		}
		return 0;
	}
	
	public static int getFlaskCapacity(ItemStack aStack) {
        int capacity = 1000;
        if (aStack.hasTagCompound()) {
            NBTTagCompound nbt = aStack.getTagCompound();
            if (nbt.hasKey("Capacity", 3))
                capacity = nbt.getInteger("Capacity");
        }
        return Math.min(getMaxFlaskCapacity(aStack), capacity);
	}
	
	public static boolean setNewFlaskCapacity(ItemStack aStack, int aCapacity) {
		if (aStack == null || aCapacity <= 0) {
			return false;
		}		
		aCapacity = Math.min(aCapacity, getMaxFlaskCapacity(aStack));
        NBTTagCompound nbt = aStack.getTagCompound();
        if (nbt == null) {
        	aStack.setTagCompound(nbt = new NBTTagCompound());
        }
        nbt.setInteger("Capacity", aCapacity);
        return true;
	}

	public static Item generateNewFlask(String unlocalized, String english, int maxCapacity) {
		Constructor aFlask = ReflectionUtils.getConstructor(sClassVolumetricFlask, new Class[] {String.class, String.class, int.class});
		if (aFlask != null) {			
			Object aInstance = ReflectionUtils.createNewInstanceFromConstructor(aFlask, new Object[] {unlocalized, english, maxCapacity});
			if (aInstance != null && aInstance instanceof Item) {
				Item aNewFlaskItem = (Item) aInstance;
				return aNewFlaskItem;
			}
		}
		return null;		
	}
	
}
