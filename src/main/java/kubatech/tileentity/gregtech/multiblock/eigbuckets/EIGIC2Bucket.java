package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

// TODO: REMOVE AFTER ONE MAJOR VERSION AFTER CROPNH'S RELEASE INTO THE PACK
public class EIGIC2Bucket extends EIGBucket {

    public final static IEIGBucketFactory factory = new EIGIC2Bucket.Factory();
    private static final String NBT_IDENTIFIER = "IC2";
    private static final int REVISION_NUMBER = 0;

    // endregion crop simulation variables

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            return null;
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGIC2Bucket(nbt);
        }
    }

    public EIGIC2Bucket(ItemStack stack, int count, ItemStack catalyst) {
        super(stack, count, new ItemStack[] { catalyst });
    }

    private EIGIC2Bucket(NBTTagCompound nbt) {
        super(nbt);
    }

    @Override
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    @Override
    public void addProgress(double multiplier, EIGDropTable tracker) {

    }

    @Override
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        // this will cause the EIG to auto-eject the seeds
        return this.isValid();
    }
}
