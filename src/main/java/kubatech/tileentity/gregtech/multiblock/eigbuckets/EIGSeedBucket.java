package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;

public class EIGSeedBucket extends EIGBucket {

    public static final IEIGBucketFactory factory = new EIGSeedBucket.Factory();
    private static final String NBT_IDENTIFIER = "SEED";
    private static final int REVISION_NUMBER = 0;
    private final static int NUMBER_OF_DROPS_TO_SIMULATE = 100;
    private static final EIGSeedBucket.GreenHouseWorld fakeWorld = new EIGSeedBucket.GreenHouseWorld(5, 5, 5);
    private static final int FORTUNE_LEVEL = 0;

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(GT_MetaTileEntity_ExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            return new EIGSeedBucket(greenhouse, input);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGSeedBucket(nbt);
        }

    }

    private boolean isValid = false;
    private EIGDropTable drops = new EIGDropTable();

    private EIGSeedBucket(GT_MetaTileEntity_ExtremeIndustrialGreenhouse greenhouse, ItemStack seed) {
        super(seed, 1, null);
        this.recalculateDrops(greenhouse);
    }

    private EIGSeedBucket(NBTTagCompound nbt) {
        super(nbt);
        this.drops = new EIGDropTable(nbt, "drops");
        this.isValid = nbt.getInteger("version") == REVISION_NUMBER && !this.drops.isEmpty();
    }

    @Override
    public NBTTagCompound save() {
        NBTTagCompound nbt = super.save();
        nbt.setTag("drops", this.drops.save());
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
    public boolean revalidate(GT_MetaTileEntity_ExtremeIndustrialGreenhouse greenhouse) {
        this.recalculateDrops(greenhouse);
        return this.isValid();
    }

    public void recalculateDrops(GT_MetaTileEntity_ExtremeIndustrialGreenhouse greenhouse) {
        this.isValid = false;
        int optimalGrowthMetadata = 7;
        // Get the relevant item and block for this item.
        Item item = this.seed.getItem();
        Block block;
        if (!(item instanceof IPlantable)) return;
        if (item instanceof ItemSeeds) {
            block = ((ItemSeeds) item).getPlant(fakeWorld, 0, 0, 0);
        } else if (item instanceof ItemSeedFood) {
            block = ((ItemSeedFood) item).getPlant(fakeWorld, 0, 0, 0);
        } else {
            // We can't plant it, we can't handle it, get out.
            return;
        }

        // Natura crops have an optimal harvest stage of 8.
        GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(item);
        if (u != null && Objects.equals(u.modId, "Natura")) optimalGrowthMetadata = 8;

        // Pre-Generate drops.
        EIGDropTable drops = new EIGDropTable();
        World world = greenhouse.getBaseMetaTileEntity()
            .getWorld();
        for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
            ArrayList<ItemStack> blockDrops = block.getDrops(world, 0, 0, 0, optimalGrowthMetadata, FORTUNE_LEVEL);
            for (ItemStack drop : blockDrops) {
                if (GT_Utility.areStacksEqual(this.seed, drop, true)) continue;
                drops.addDrop(drop, drop.stackSize / (double) NUMBER_OF_DROPS_TO_SIMULATE);
            }
        }
        // make sure we actually got a drop
        if (drops.isEmpty()) return;

        // and we are good, see ya.
        this.drops = drops;
        this.isValid = true;
    }

    private static class GreenHouseWorld extends GT_DummyWorld {

        public int x, y, z, meta = 0;
        public Block block;

        GreenHouseWorld(int x, int y, int z) {
            super();
            this.x = x;
            this.y = y;
            this.z = z;
            this.rand = new EIGSeedBucket.GreenHouseRandom();
        }

        @Override
        public int getBlockMetadata(int aX, int aY, int aZ) {
            if (aX == x && aY == y && aZ == z) return 7;
            return 0;
        }

        @Override
        public Block getBlock(int aX, int aY, int aZ) {
            if (aY == y - 1) return Blocks.farmland;
            return Blocks.air;
        }

        @Override
        public int getBlockLightValue(int p_72957_1_, int p_72957_2_, int p_72957_3_) {
            return 10;
        }

        @Override
        public boolean setBlock(int aX, int aY, int aZ, Block aBlock, int aMeta, int aFlags) {
            if (aBlock == Blocks.air) return false;
            if (aX == x && aY == y && aZ == z) return false;
            block = aBlock;
            meta = aMeta;
            return true;
        }
    }

    private static class GreenHouseRandom extends Random {

        private static final long serialVersionUID = -387271808935248890L;

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

}
