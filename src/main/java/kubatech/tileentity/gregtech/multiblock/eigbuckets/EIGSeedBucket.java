package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import com.mitchej123.hodgepodge.mixins.interfaces.INetherSeed;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTUtility;
import gregtech.common.GTDummyWorld;
import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumic.tinkerer.common.core.helper.AspectCropLootManager;
import thaumic.tinkerer.common.item.ItemInfusedSeeds;

public class EIGSeedBucket extends EIGBucket {

    public static final IEIGBucketFactory factory = new EIGSeedBucket.Factory();
    private static final String NBT_IDENTIFIER = "SEED";
    private static final int REVISION_NUMBER = 0;
    private static final int NUMBER_OF_DROPS_TO_SIMULATE = 1000;
    private static final int FORTUNE_LEVEL = 0;
    private static final EIGSeedBucket.GreenHouseWorld fakeWorld = new EIGSeedBucket.GreenHouseWorld(5, 5, 5);

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            return new EIGSeedBucket(greenhouse, input);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGSeedBucket(nbt);
        }

    }

    private boolean isValid = false;
    private EIGDropTable drops = new EIGDropTable();

    private boolean isInfusedSeed = false;

    // ensure infused seeds cost 8 slots
    public int getSlotCost() {
        return this.isInfusedSeed ? 8 : 1;
    }

    private EIGSeedBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack seed) {
        super(seed, 1, null);
        this.isInfusedSeed = isInfusedSeedItem(seed);
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
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        this.recalculateDrops(greenhouse);
        return this.isValid();
    }

    private static boolean isInfusedSeedItem(ItemStack seed) {
        return seed.getItem()
            .getClass()
            .getName()
            .equals("thaumic.tinkerer.common.item.ItemInfusedSeeds");
    }

    public void recalculateDrops(MTEExtremeIndustrialGreenhouse greenhouse) {
        this.isValid = false;

        if (Loader.isModLoaded("ThaumicTinkerer") && isInfusedSeedItem(this.seed)) {
            if (recalculateDropsForInfusedSeed(greenhouse)) {
                this.isValid = true;
            }
            return;
        }

        int optimalGrowthMetadata = 7;
        // Get the relevant item and block for this item.
        Item item = this.seed.getItem();
        Block block;
        if (!(item instanceof IPlantable)) return;

        // Order is important due to ItemNetherSeed being a child of both INetherSeed and ItemSeeds
        switch (item) {
            case INetherSeed netherSeed -> block = netherSeed.hodgepodge$getPlant(fakeWorld, 0, 0, 0);
            case ItemSeeds itemSeeds -> block = itemSeeds.getPlant(fakeWorld, 0, 0, 0);
            case ItemSeedFood itemSeedFood -> block = itemSeedFood.getPlant(fakeWorld, 0, 0, 0);
            default -> {
                // We can't plant it, we can't handle it, get out.
                return;
            }
        }

        // Natura crops have an optimal harvest stage of 8.
        GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(item);
        if (u != null && Objects.equals(u.modId, "Natura")) optimalGrowthMetadata = 8;

        // Pre-Generate drops.
        EIGDropTable drops = new EIGDropTable();
        World world = greenhouse.getBaseMetaTileEntity()
            .getWorld();

        fakeWorld.dropTable = drops;

        for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
            ArrayList<ItemStack> blockDrops = block.getDrops(fakeWorld, 0, 0, 0, optimalGrowthMetadata, FORTUNE_LEVEL);
            for (ItemStack drop : blockDrops) {
                drops.addDrop(drop, drop.stackSize);
            }
        }

        // reduce the number of drops to account for the seeds
        if (!removeSeedFromDrops(world, drops, this.seed, NUMBER_OF_DROPS_TO_SIMULATE)) return;

        // reduce drop count to account for the number of simulations
        drops.entrySet()
            .forEach(x -> x.setValue(x.getValue() / NUMBER_OF_DROPS_TO_SIMULATE));

        // make sure we actually got a drop
        if (drops.isEmpty()) return;

        // and we are good, see ya.
        this.drops = drops;
        this.isValid = true;
    }

    // specially account for infused seeds
    private boolean recalculateDropsForInfusedSeed(MTEExtremeIndustrialGreenhouse greenhouse) {
        Aspect aspect = ItemInfusedSeeds.getAspect(this.seed);
        if (aspect == null) return false;

        AspectList tendencies = ItemInfusedSeeds.getAspectTendencies(this.seed);
        if (tendencies == null) tendencies = new AspectList();

        int entropyTendency = Math.max(0, tendencies.getAmount(Aspect.ENTROPY));
        int orderTendency = Math.max(0, tendencies.getAmount(Aspect.ORDER));

        final int SIMULATIONS = 1000;
        EIGDropTable cumulativeDrops = new EIGDropTable();

        for (int sim = 0; sim < SIMULATIONS; sim++) {
            int seedstack = 0;
            double pSeed = (double) (entropyTendency * entropyTendency) / 10000.0;
            while (Math.random() < pSeed) {
                seedstack++;
            }
            
            if (entropyTendency > 0) {
                ItemStack seedDrop = this.seed.copy();
                seedDrop.stackSize = seedstack;
                cumulativeDrops.addDrop(seedDrop, seedstack);
            }
            

            double pLoot = (double) orderTendency / 75.0;
            do {
                ItemStack loot = AspectCropLootManager.getLootForAspect(aspect);
                if (loot != null) {
                    cumulativeDrops.addDrop(loot, loot.stackSize);
                }
            } while (Math.random() < pLoot);
        }

        // average the drops
        EIGDropTable averageDrops = new EIGDropTable();
        for (Map.Entry<ItemStack, Double> entry : cumulativeDrops.entrySet()) {
            ItemStack stack = entry.getKey()
                .copy();
            stack.stackSize = 1;
            double averageCount = entry.getValue() / SIMULATIONS;
            averageDrops.addDrop(stack, averageCount);
        }

        this.drops = averageDrops;
        return true;
    }

    private boolean removeSeedFromDrops(World world, EIGDropTable drops, ItemStack seed, int seedsToConsume) {
        // make a safe copy of the seed just in case
        ItemStack seedSafe = seed.copy();
        seedSafe.stackSize = 1;
        // first check if we dropped an item identical to our seed item.
        int inputSeedDropCountAfterRemoval = (int) Math.round(drops.getItemAmount(seedSafe)) - seedsToConsume;
        // true seeds should be removed entirely, potatos/carrots should only be reduced.
        boolean isTrueSeed = Arrays.stream(OreDictionary.getOreIDs(seedSafe))
            .anyMatch(id -> id == OreDictionary.getOreID("listAllseed"));
        if (inputSeedDropCountAfterRemoval > 0 && !isTrueSeed) {
            drops.setItemAmount(seedSafe, inputSeedDropCountAfterRemoval);
        } else {
            drops.removeItem(seedSafe);
        }
        // return true if we were able to find enough seeds in the drops.
        if (inputSeedDropCountAfterRemoval >= 0) return true;

        // else try to find items that can be crafted into the seed
        int seedsToCraft = -inputSeedDropCountAfterRemoval;
        IRecipe[] validRecipes = CraftingManager.getInstance()
            .getRecipeList()
            .parallelStream()
            .filter(recipe -> GTUtility.areStacksEqual(recipe.getRecipeOutput(), seed))
            .toArray(IRecipe[]::new);

        // if no recipes outputs the input seed, abort.
        if (validRecipes.length == 0) return false;

        // check the recipes we found for one that can consume our seed
        for (Iterator<Map.Entry<ItemStack, Double>> dropIterator = drops.entrySet()
            .iterator(); dropIterator.hasNext();) {
            Map.Entry<ItemStack, Double> entry = dropIterator.next();
            int inputCount = (int) Math.round(entry.getValue());
            ItemStack input = entry.getKey()
                .copy();
            input.stackSize = 1;
            EIGCraftingSeedFinder seedFinder = new EIGCraftingSeedFinder(input);
            for (IRecipe recipe : validRecipes) {
                if (recipe.matches(seedFinder, world)) {
                    // account for recipes that potentially drop more than 1 seed per input.
                    int outputsPerCraft = recipe.getCraftingResult(seedFinder).stackSize;
                    int craftableSeeds = outputsPerCraft * inputCount;
                    if (seedsToCraft >= craftableSeeds) {
                        // if the entire drop is consumed, just remove it from the list.
                        dropIterator.remove();
                        seedsToCraft -= craftableSeeds;
                        if (seedsToCraft <= 0) {
                            return true;
                        }
                    } else {
                        // else remove the right amount from the drop, and get out.
                        entry.setValue(entry.getValue() - (double) seedsToCraft / outputsPerCraft);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    static class EIGCraftingSeedFinder extends InventoryCrafting {

        public ItemStack recipeInput;

        public EIGCraftingSeedFinder(ItemStack recipeInput) {
            super(null, 3, 3);
            this.recipeInput = recipeInput;
        }

        @Override
        public ItemStack getStackInSlot(int p_70301_1_) {
            if (p_70301_1_ == 0) return this.recipeInput.copy();
            return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int par1) {
            return null;
        }

        @Override
        public ItemStack decrStackSize(int par1, int par2) {
            return null;
        }

        @SuppressWarnings("EmptyMethod")
        @Override
        public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {}
    }

    private static class GreenHouseWorld extends GTDummyWorld {

        public int x, y, z, meta = 0;
        public Block block;
        public EIGDropTable dropTable;

        GreenHouseWorld(int x, int y, int z) {
            super();
            this.x = x;
            this.y = y;
            this.z = z;
            this.rand = new Random();
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
        public boolean spawnEntityInWorld(Entity entity) {
            if (this.dropTable == null) {
                return false;
            }

            if (entity instanceof EntityLivingBase livingEntity) {
                livingEntity.captureDrops = true;

                livingEntity.onDeath(DamageSource.generic);
                livingEntity.captureDrops = false;

                if (livingEntity.capturedDrops != null && !livingEntity.capturedDrops.isEmpty()) {
                    for (EntityItem drop : livingEntity.capturedDrops) {
                        ItemStack itemStack = drop.getEntityItem();
                        this.dropTable.addDrop(itemStack, itemStack.stackSize);
                    }
                }
            }
            return false;
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
}
