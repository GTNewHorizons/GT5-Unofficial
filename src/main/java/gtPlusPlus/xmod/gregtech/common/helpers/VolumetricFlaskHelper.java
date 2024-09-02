package gtPlusPlus.xmod.gregtech.common.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

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

    public static boolean isVolumetricFlask(ItemStack aStack) {
        return isNormalVolumetricFlask(aStack) || isLargeVolumetricFlask(aStack) || isGiganticVolumetricFlask(aStack);
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
        if (aStack.hasTagCompound()) {
            NBTTagCompound nbt = aStack.getTagCompound();
            if (nbt.hasKey("Fluid", 10)) return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
        }
        return null;
    }

    public static void setFluid(ItemStack stack, FluidStack fluidStack) {
        boolean removeFluid = (fluidStack == null) || (fluidStack.amount <= 0);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            if (removeFluid) return;
            stack.setTagCompound(nbt = new NBTTagCompound());
        }
        if (removeFluid) {
            nbt.removeTag("Fluid");
            if (nbt.hasNoTags()) {
                stack.setTagCompound(null);
            }
        } else {
            nbt.setTag("Fluid", fluidStack.writeToNBT(new NBTTagCompound()));
        }
    }

    public static int getFlaskCapacity(ItemStack aStack) {
        int capacity = 1000;
        if (aStack.hasTagCompound()) {
            NBTTagCompound nbt = aStack.getTagCompound();
            if (nbt.hasKey("Capacity", 3)) capacity = nbt.getInteger("Capacity");
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

    public static int fillFlask(ItemStack stack, FluidStack resource, boolean doFill) {
        if (stack.stackSize != 1) return 0;
        if ((resource == null) || (resource.amount <= 0)) {
            return 0;
        }
        FluidStack fluidStack = getFlaskFluid(stack);
        if (fluidStack == null) {
            fluidStack = new FluidStack(resource, 0);
        } else if (!fluidStack.isFluidEqual(resource)) {
            return 0;
        }
        int amount = Math.min(getMaxFlaskCapacity(stack) - fluidStack.amount, resource.amount);
        if ((doFill) && (amount > 0)) {
            fluidStack.amount += amount;
            setFluid(stack, fluidStack);
        }
        return amount;
    }

    public static Item generateNewFlask(String unlocalized, String english, int maxCapacity) {
        return new ItemVolumetricFlask(unlocalized, english, maxCapacity);
    }
}
