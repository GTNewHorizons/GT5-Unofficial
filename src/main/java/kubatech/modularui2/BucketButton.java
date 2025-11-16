package kubatech.modularui2;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.widgets.ButtonWidget;

public class BucketButton<T extends InventoryBucket> extends ButtonWidget<BucketButton<T>> {

    public T bucket;

    public NBTTagCompound saveBucketNBT() {
        return bucket.save();
    }
}
