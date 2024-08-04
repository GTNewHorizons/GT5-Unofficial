package kubatech.api.eig;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;

public interface IEIGBucketFactory {

    String getNBTIdentifier();

    EIGBucket tryCreateBucket(GT_MetaTileEntity_ExtremeIndustrialGreenhouse greenhouse, ItemStack stack);

    EIGBucket restore(NBTTagCompound nbt);
}
