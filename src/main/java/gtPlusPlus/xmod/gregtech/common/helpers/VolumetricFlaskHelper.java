package gtPlusPlus.xmod.gregtech.common.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.enums.ItemList;
import gregtech.common.items.ItemVolumetricFlask;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class VolumetricFlaskHelper {

    public static ItemStack getVolumetricFlask(int aAmount) {
        return ItemList.VOLUMETRIC_FLASK.get(aAmount);
    }

    public static ItemStack getLargeVolumetricFlask(int aAmount) {
        return GregtechItemList.VOLUMETRIC_FLASK_8k.get(aAmount);
    }

    public static ItemStack getGiganticVolumetricFlask(int aAmount) {
        return GregtechItemList.VOLUMETRIC_FLASK_32k.get(aAmount);
    }

    public static ItemStack getKleinBottle(int aAmount) {
        return GregtechItemList.KLEIN_BOTTLE.get(aAmount);
    }

    public static boolean isVolumetricFlask(ItemStack aStack) {
        return isNormalVolumetricFlask(aStack) || isLargeVolumetricFlask(aStack)
            || isGiganticVolumetricFlask(aStack)
            || isKleinBottle(aStack);
    }

    public static boolean isNormalVolumetricFlask(ItemStack aStack) {
        return aStack.getItem() == ItemList.VOLUMETRIC_FLASK.getItem();
    }

    public static boolean isLargeVolumetricFlask(ItemStack aStack) {
        return GregtechItemList.VOLUMETRIC_FLASK_8k.getItem() == aStack.getItem();
    }

    public static boolean isGiganticVolumetricFlask(ItemStack aStack) {
        return GregtechItemList.VOLUMETRIC_FLASK_32k.getItem() == aStack.getItem();
    }

    public static boolean isKleinBottle(ItemStack aStack) {
        return GregtechItemList.KLEIN_BOTTLE.getItem() == aStack.getItem();
    }

    public static int getMaxFlaskCapacity(ItemStack aStack) {
        if (aStack != null) {
            Item aItem = aStack.getItem();
            if (aItem instanceof ItemVolumetricFlask) {
                return ((ItemVolumetricFlask) aItem).getMaxCapacity();
            }
        }
        return 0;
    }

    public static boolean isFlaskEmpty(ItemStack aStack) {
        return getFlaskFluid(aStack) == null;
    }

    public static FluidStack getFlaskFluid(ItemStack aStack) {
        if (ItemStackNBT.hasKey(aStack, "Fluid", NBT.TAG_COMPOUND)) {
            return FluidStack.loadFluidStackFromNBT(ItemStackNBT.getCompoundTag(aStack, "Fluid"));
        }
        return null;
    }

    public static void setFluid(ItemStack stack, FluidStack fluidStack) {
        final boolean removeFluid = (fluidStack == null) || (fluidStack.amount <= 0);
        if (removeFluid) {
            ItemStackNBT.removeTag(stack, "Fluid");
        } else {
            ItemStackNBT.setTag(stack, "Fluid", fluidStack.writeToNBT(new NBTTagCompound()));
        }
    }

    public static int getFlaskCapacity(ItemStack aStack) {
        int capacity = 1000;
        if (ItemStackNBT.hasKey(aStack, "Capacity", NBT.TAG_INT)) {
            capacity = ItemStackNBT.getInteger(aStack, "Capacity");
        }
        return Math.min(getMaxFlaskCapacity(aStack), capacity);
    }

    public static void setFlaskCapacity(ItemStack aStack, int aCapacity) {
        if (aStack == null || aCapacity <= 0) return;
        aCapacity = Math.min(aCapacity, getMaxFlaskCapacity(aStack));
        ItemStackNBT.setInteger(aStack, "Capacity", aCapacity);
    }

    public static Item generateNewFlask(String unlocalized, String english, int maxCapacity) {
        return new ItemVolumetricFlask(unlocalized, english, maxCapacity);
    }
}
