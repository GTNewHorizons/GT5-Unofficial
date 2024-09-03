package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IPlantable;

import kubatech.api.IBlockStemAccesor;
import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public class EIGStemBucket extends EIGBucket {

    public final static IEIGBucketFactory factory = new EIGStemBucket.Factory();
    private static final String NBT_IDENTIFIER = "STEM";
    private static final int REVISION_NUMBER = 0;
    private final static int NUMBER_OF_DROPS_TO_SIMULATE = 100;

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            // Check if input is a flower, reed or cacti. They all drop their source item multiplied by their seed count
            Item item = input.getItem();
            if (!(item instanceof IPlantable)) return null;
            Block block = ((IPlantable) item).getPlant(
                greenhouse.getBaseMetaTileEntity()
                    .getWorld(),
                0,
                0,
                0);
            if (!(block instanceof BlockStem)) return null;
            return new EIGStemBucket(greenhouse, input);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGStemBucket(nbt);
        }
    }

    private boolean isValid = false;
    private EIGDropTable drops = new EIGDropTable();

    private EIGStemBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
        super(input, 1, null);
        recalculateDrops(greenhouse);
    }

    private EIGStemBucket(NBTTagCompound nbt) {
        super(nbt);
        this.drops = new EIGDropTable(nbt, "drops");
        this.isValid = nbt.getInteger("version") == REVISION_NUMBER && !this.drops.isEmpty();
    }

    @Override
    public NBTTagCompound save() {
        NBTTagCompound nbt = super.save();
        if (this.drops != null) {
            nbt.setTag("drops", this.drops.save());
        }
        nbt.setInteger("version", REVISION_NUMBER);
        return nbt;
    }

    @Override
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    @Override
    public void addProgress(double multiplier, EIGDropTable tracker) {
        if (!this.isValid()) return;
        this.drops.addTo(tracker, multiplier * this.seedCount);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.isValid;
    }

    @Override
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        recalculateDrops(greenhouse);
        return this.isValid();
    }

    /**
     * Attempts to predetermine what item the stem crop will drop.
     *
     * @param greenhouse The greenhouse that houses this bucket.
     */
    public void recalculateDrops(MTEExtremeIndustrialGreenhouse greenhouse) {
        this.isValid = false;
        Item item = this.seed.getItem();
        if (!(item instanceof IPlantable)) return;
        Block stemBlock = ((IPlantable) item).getPlant(
            greenhouse.getBaseMetaTileEntity()
                .getWorld(),
            0,
            0,
            0);
        if (!(stemBlock instanceof BlockStem)) return;
        Block cropBlock = ((IBlockStemAccesor) stemBlock).getCropBlock();
        if (cropBlock == null || cropBlock == Blocks.air) return;
        // if we know some crops needs a specific metadata, remap here
        int metadata = 0;

        EIGDropTable drops = new EIGDropTable();

        for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
            // simulate 1 round of drops
            ArrayList<ItemStack> blockDrops = cropBlock.getDrops(
                greenhouse.getBaseMetaTileEntity()
                    .getWorld(),
                greenhouse.getBaseMetaTileEntity()
                    .getXCoord(),
                greenhouse.getBaseMetaTileEntity()
                    .getYCoord(),
                greenhouse.getBaseMetaTileEntity()
                    .getZCoord(),
                metadata,
                0);
            if (blockDrops == null || blockDrops.isEmpty()) continue;
            // if the droped item is a block that places itself, assume this is the only possible drop
            // eg: pumpkin, redlon
            if (i == 0 && blockDrops.size() == 1) {
                ItemStack drop = blockDrops.get(0);
                if (drop != null && drop.stackSize >= 1 && drop.getItem() == Item.getItemFromBlock(cropBlock)) {
                    drops.addDrop(drop, drop.stackSize);
                    break;
                }
            }
            // else append all the drops
            for (ItemStack drop : blockDrops) {
                drops.addDrop(drop, drop.stackSize / (double) NUMBER_OF_DROPS_TO_SIMULATE);
            }
        }
        // check that we did in fact drop something.s
        if (drops.isEmpty()) return;

        // all checks passed we are good to go
        this.drops = drops;
        this.isValid = true;
    }
}
