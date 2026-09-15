package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import tb.common.block.BlockRainbowCactus;
import tb.init.TBBlocks;

// This is more of a proof of concept to show how to implement a custom handler.

public class EIGRainbowCactusBucket extends EIGBucket {

    public final static IEIGBucketFactory factory = new EIGRainbowCactusBucket.Factory();
    private static final String NBT_IDENTIFIER = "TB:RAINCACTI";
    private static final int REVISION_NUMBER = 0;

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            // check if input is rainbow cacti;
            if (!(Block.getBlockFromItem(input.getItem()) instanceof BlockRainbowCactus)) return null;
            return new EIGRainbowCactusBucket(input, 1);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGRainbowCactusBucket(nbt);
        }

    }

    private final Random random = new Random();

    public EIGRainbowCactusBucket(ItemStack seed, int seedCount) {
        super(seed, seedCount, null);
    }

    public EIGRainbowCactusBucket(NBTTagCompound nbt) {
        super(nbt);
    }

    @Override
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        return this.isValid();
    }

    @Override
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    @Override
    public void addProgress(double multiplier, EIGDropTable tracker) {
        if (!this.isValid()) return;
        // TODO: make the addDyeDropsToOutput static in TB.
        ArrayList<ItemStack> drops = new ArrayList<>();
        ((BlockRainbowCactus) TBBlocks.rainbowCactus).addDyeDropsToOutput(this.random, drops);
        for (ItemStack drop : drops) {
            tracker.addDrop(drop, drop.stackSize * multiplier * this.seedCount);
        }
    }

}
