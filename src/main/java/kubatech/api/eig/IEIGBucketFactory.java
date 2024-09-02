package kubatech.api.eig;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public interface IEIGBucketFactory {

    String getNBTIdentifier();

    EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack stack);

    EIGBucket restore(NBTTagCompound nbt);
}
