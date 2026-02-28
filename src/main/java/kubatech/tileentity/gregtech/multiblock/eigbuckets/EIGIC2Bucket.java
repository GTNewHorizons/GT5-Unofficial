package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.CropStickreed;
import ic2.core.crop.IC2Crops;
import ic2.core.crop.TileEntityCrop;
import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public class EIGIC2Bucket extends EIGBucket {

    public final static IEIGBucketFactory factory = new EIGIC2Bucket.Factory();
    private static final String NBT_IDENTIFIER = "IC2";
    private static final int REVISION_NUMBER = 0;

    // region crop simulation variables

    private final static int NUMBER_OF_DROPS_TO_SIMULATE = 1000;
    // nutrient factors
    /**
     * Set to true if you want to assume the crop is on wet farmland for a +2 bonus to nutrients
     */
    private static final boolean IS_ON_WET_FARMLAND = true;
    /**
     * The amount of water stored in the crop stick when hydration is turned on. bounds of 0 to 200 inclusive
     */
    private static final int WATER_STORAGE_VALUE = 200;
    // nutrient factors
    /**
     * The number of blocks of dirt we assume are under. Subtract 1 if we have a block under our crop. bounds of 0 to 3,
     * inclusive
     */
    private static final int NUMBER_OF_DIRT_BLOCKS_UNDER = 0;
    /**
     * The amount of fertilizer stored in the crop stick bounds of 0 to 200, inclusive
     */
    private static final int FERTILIZER_STORAGE_VALUE = 0;
    // air quality factors
    /**
     * How many blocks in a 3x3 area centered on the crop do not contain solid blocks or other crops. Max value is 8
     * because the crop always counts itself. bound of 0-8 inclusive
     */
    private static final int CROP_OBSTRUCTION_VALUE = 5;
    /**
     * Being able to see the sky gives a +2 bonus to the air quality
     */
    private static final boolean CROP_CAN_SEE_SKY = false;

    // endregion crop simulation variables

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            // Check if input is a seed.
            if (!ItemList.IC2_Crop_Seeds.isStackEqual(input, true, true)) return null;
            if (!input.hasTagCompound()) return null;
            // Validate that stat nbt data exists.
            NBTTagCompound nbt = input.getTagCompound();
            if (!(nbt.hasKey("growth") && nbt.hasKey("gain") && nbt.hasKey("resistance"))) return null;

            CropCard cc = IC2Crops.instance.getCropCard(input);
            if (cc == null) return null;
            return new EIGIC2Bucket(greenhouse, input);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGIC2Bucket(nbt);
        }
    }

    public final boolean useNoHumidity;
    /**
     * The average amount of growth cycles needed to reach maturity.
     */
    private double growthTime = 0;
    private EIGDropTable drops = new EIGDropTable();
    private boolean isValid = false;

    /**
     * Used to migrate old EIG greenhouse slots to the new bucket system, needs custom handling as to not void the
     * support blocks.
     *
     * @implNote DOES NOT VALIDATE THE CONTENTS OF THE BUCKET, YOU'LL HAVE TO REVALIDATE WHEN THE WORLD IS LOADED.
     *
     * @param seed          The item stack for the item that served as the seed before
     * @param count         The number of seed in the bucket
     * @param supportBlock  The block that goes under the bucket
     * @param useNoHumidity Whether to use no humidity in growth speed calculations.
     */
    public EIGIC2Bucket(ItemStack seed, int count, ItemStack supportBlock, boolean useNoHumidity) {
        super(seed, count, supportBlock == null ? null : new ItemStack[] { supportBlock });
        this.useNoHumidity = useNoHumidity;
        // revalidate me
        this.isValid = false;
    }

    private EIGIC2Bucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack seed) {
        super(seed, 1, null);
        this.useNoHumidity = greenhouse.isInNoHumidityMode();
        this.recalculateDrops(greenhouse);
    }

    private EIGIC2Bucket(NBTTagCompound nbt) {
        super(nbt);
        this.useNoHumidity = nbt.getBoolean("useNoHumidity");
        // If the invalid key exists then drops and growth time haven't been saved
        if (!nbt.hasKey("invalid")) {
            this.drops = new EIGDropTable(nbt, "drops");
            this.growthTime = nbt.getDouble("growthTime");
            this.isValid = nbt.getInteger("version") == REVISION_NUMBER && this.growthTime > 0 && !this.drops.isEmpty();
        }
    }

    @Override
    public NBTTagCompound save() {
        NBTTagCompound nbt = super.save();
        nbt.setBoolean("useNoHumidity", this.useNoHumidity);
        if (this.isValid) {
            nbt.setTag("drops", this.drops.save());
            nbt.setDouble("growthTime", this.growthTime);
        } else {
            nbt.setBoolean("invalid", true);
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
        // abort early if the bucket is invalid
        if (!this.isValid()) return;
        // else apply drops to tracker
        double growthPercent = multiplier / (this.growthTime * TileEntityCrop.tickRate);
        if (this.drops != null) {
            this.drops.addTo(tracker, this.seedCount * growthPercent);
        }
    }

    @Override
    protected void getAdditionalInfoData(StringBuilder sb) {
        sb.append(" | ")
            .append(StatCollector.translateToLocal("kubatech.infodata.bucket.humidity"))
            .append(" ");
        sb.append(this.useNoHumidity ? "Off" : "On");
    }

    @Override
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        this.recalculateDrops(greenhouse);
        return this.isValid();
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.isValid;
    }

    /**
     * (Re-)calculates the pre-generated drop table for this bucket.
     *
     * @param greenhouse The {@link MTEExtremeIndustrialGreenhouse} that contains this bucket.
     */
    public void recalculateDrops(MTEExtremeIndustrialGreenhouse greenhouse) {
        this.isValid = false;
        World world = greenhouse.getBaseMetaTileEntity()
            .getWorld();
        int[] abc = new int[] { 0, -2, 3 };
        int[] xyz = new int[] { 0, 0, 0 };
        greenhouse.getExtendedFacing()
            .getWorldOffset(abc, xyz);
        xyz[0] += greenhouse.getBaseMetaTileEntity()
            .getXCoord();
        xyz[1] += greenhouse.getBaseMetaTileEntity()
            .getYCoord();
        xyz[2] += greenhouse.getBaseMetaTileEntity()
            .getZCoord();
        boolean cheating = false;
        FakeTileEntityCrop crop;
        try {
            if (greenhouse.isOldStructure()) {
                if (world.getBlock(xyz[0], xyz[1] - 2, xyz[2]) != Casings.CleanStainlessSteelMachineCasing.getBlock()
                    || world.getBlockMetadata(xyz[0], xyz[1] - 2, xyz[2])
                        != Casings.CleanStainlessSteelMachineCasing.meta) {
                    // no
                    cheating = true;
                    return;
                }
            } else {
                if (world.getBlock(xyz[0], xyz[1] - 2, xyz[2]) != Casings.SterileFarmCasing.getBlock()
                    || world.getBlockMetadata(xyz[0], xyz[1] - 2, xyz[2]) != Casings.SterileFarmCasing.meta) {
                    // no
                    cheating = true;
                    return;
                }
            }

            // instantiate the TE in which we grow the seed.
            crop = new FakeTileEntityCrop(this, greenhouse, xyz);
            if (!crop.isValid) return;
            CropCard cc = crop.getCrop();

            // region can grow checks

            // Check if we can put the current block under the soil.
            if (this.supportItems != null && this.supportItems.length == 1 && this.supportItems[0] != null) {
                if (!setBlock(this.supportItems[0], xyz[0], xyz[1] - 2, xyz[2], world)) {
                    return;
                }
                // update nutrients if we need a block under.
                crop.updateNutrientsForBlockUnder();
            }

            // Check if the crop has a chance to die in the current environment
            if (calcAvgGrowthRate(crop, cc, 0) < 0) return;
            // Check if the crop has a chance to grow in the current environment.
            if (calcAvgGrowthRate(crop, cc, 6) <= 0) return;

            ItemStack blockInputStackToConsume = null;
            if (!crop.canMature()) {
                // If the block we have in storage no longer functions, we are no longer valid, the seed and block
                // should be ejected if possible.
                if (this.supportItems != null) return;
                // assume we need a block under the farmland/fertilized dirt and update nutrients accordingly
                crop.updateNutrientsForBlockUnder();
                // Try to find the needed block in the inputs
                boolean canGrow = false;
                ArrayList<ItemStack> inputs = greenhouse.getStoredInputs();
                for (ItemStack potentialBlock : inputs) {
                    // if the input can't be placed in the world skip to the next input
                    if (potentialBlock == null || potentialBlock.stackSize <= 0) continue;
                    if (!setBlock(potentialBlock, xyz[0], xyz[1] - 2, xyz[2], world)) continue;
                    // check if the crop can grow with the block under it.
                    if (!crop.canMature()) continue;
                    // If we don't have enough blocks to consume, abort.
                    if (this.seedCount > potentialBlock.stackSize) return;
                    canGrow = true;
                    blockInputStackToConsume = potentialBlock;
                    // Don't consume the block just yet, we do that once everything is valid.
                    ItemStack newSupport = potentialBlock.copy();
                    newSupport.stackSize = 1;
                    this.supportItems = new ItemStack[] { newSupport };
                    break;
                }

                if (!canGrow) return;
            }

            // check if the crop does a block under check and try to put a requested block if possible
            if (this.supportItems == null) {
                // some crops get increased outputs if a specific block is under them.
                cc.getGain(crop);
                if (crop.hasRequestedBlockUnder()) {
                    ArrayList<ItemStack> inputs = greenhouse.getStoredInputs();
                    boolean keepLooking = !inputs.isEmpty();
                    if (keepLooking && !crop.reqBlockOreDict.isEmpty()) {
                        oreDictLoop: for (String reqOreDictName : crop.reqBlockOreDict) {
                            if (reqOreDictName == null || OreDictionary.doesOreNameExist(reqOreDictName)) continue;
                            int oreId = OreDictionary.getOreID(reqOreDictName);
                            for (ItemStack potentialBlock : inputs) {
                                if (potentialBlock == null || potentialBlock.stackSize <= 0) continue;
                                for (int inputOreId : OreDictionary.getOreIDs(potentialBlock)) {
                                    if (inputOreId != oreId) continue;
                                    blockInputStackToConsume = potentialBlock;
                                    // Don't consume the block just yet, we do that once everything is valid.
                                    ItemStack newSupport = potentialBlock.copy();
                                    newSupport.stackSize = 1;
                                    this.supportItems = new ItemStack[] { newSupport };
                                    keepLooking = false;
                                    crop.updateNutrientsForBlockUnder();
                                    break oreDictLoop;
                                }
                            }
                        }
                    }
                    if (keepLooking && !crop.reqBlockSet.isEmpty()) {
                        blockLoop: for (Block reqBlock : crop.reqBlockSet) {
                            if (reqBlock == null || reqBlock instanceof BlockLiquid) continue;
                            for (ItemStack potentialBlockStack : inputs) {
                                // TODO: figure out a way to handle liquid block requirements
                                // water lilly looks for water and players don't really have access to those.
                                if (potentialBlockStack == null || potentialBlockStack.stackSize <= 0) continue;
                                // check if it places a block that is equal to the the one we are looking for
                                Block inputBlock = Block.getBlockFromItem(potentialBlockStack.getItem());
                                if (inputBlock != reqBlock) continue;
                                blockInputStackToConsume = potentialBlockStack;
                                // Don't consume the block just yet, we do that once everything is valid.
                                ItemStack newSupport = potentialBlockStack.copy();
                                newSupport.stackSize = 1;
                                this.supportItems = new ItemStack[] { newSupport };
                                crop.updateNutrientsForBlockUnder();
                                break blockLoop;
                            }
                        }
                    }
                }
            }

            // check if the crop can be harvested at its max size
            // Eg: the Eating plant cannot be harvested at its max size of 6, only 4 or 5 can
            crop.setSize((byte) cc.maxSize());
            if (!cc.canBeHarvested(crop)) return;

            // endregion can grow checks

            // region drop rate calculations

            // PRE CALCULATE DROP RATES
            // TODO: Add better loot table handling for crops like red wheat
            // berries, etc.
            EIGDropTable drops = new EIGDropTable();
            // Multiply drop sizes by the average number drop rounds per harvest.
            double avgDropRounds = getRealAverageDropRounds(crop, cc);
            double avgStackIncrease = getRealAverageDropIncrease(crop, cc);
            HashMap<Integer, Integer> sizeAfterHarvestFrequencies = new HashMap<>();
            for (int i = 0; i < NUMBER_OF_DROPS_TO_SIMULATE; i++) {
                // try generating some loot drop
                ItemStack drop = cc.getGain(crop);
                if (drop == null || drop.stackSize <= 0) continue;
                sizeAfterHarvestFrequencies.merge((int) cc.getSizeAfterHarvest(crop), 1, Integer::sum);

                // Merge the new drop with the current loot table.
                double avgAmount = (drop.stackSize + avgStackIncrease) * avgDropRounds;
                drops.addDrop(drop, avgAmount / NUMBER_OF_DROPS_TO_SIMULATE);
            }
            if (drops.isEmpty()) return;

            // endregion drop rate calculations

            // region growth time calculation

            // Just doing average(ceil(stageGrowth/growthSpeed)) isn't good enough it's off by as much as 20%
            double avgGrowthCyclesToHarvest = calcRealAvgGrowthRate(crop, cc, sizeAfterHarvestFrequencies);
            if (avgGrowthCyclesToHarvest <= 0) {
                return;
            }

            // endregion growth time calculation

            // Consume new under block if necessary
            if (blockInputStackToConsume != null) blockInputStackToConsume.stackSize -= this.seedCount;
            // We are good return success
            this.growthTime = avgGrowthCyclesToHarvest;
            this.drops = drops;
            this.isValid = true;
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            // always reset the world to it's original state
            if (!cheating) {
                if (greenhouse.isOldStructure()) world.setBlock(
                    xyz[0],
                    xyz[1] - 2,
                    xyz[2],
                    Casings.CleanStainlessSteelMachineCasing.getBlock(),
                    Casings.CleanStainlessSteelMachineCasing.meta,
                    0);
                else world.setBlock(
                    xyz[0],
                    xyz[1] - 2,
                    xyz[2],
                    Casings.SterileFarmCasing.getBlock(),
                    Casings.SterileFarmCasing.meta,
                    0);
            }
            // world.setBlockToAir(xyz[0], xyz[1], xyz[2]);
        }
    }

    /**
     * Attempts to place a block in the world, used for testing crop viability and drops.
     *
     * @param stack The {@link ItemStack} to place.
     * @param x     The x coordinate at which to place the block.
     * @param y     The y coordinate at which to place the block.
     * @param z     The z coordinate at which to place the block.
     * @param world The world in which to place the block.
     * @return true of a block was placed.
     */
    private static boolean setBlock(ItemStack stack, int x, int y, int z, World world) {
        Item item = stack.getItem();
        Block b = Block.getBlockFromItem(item);
        if (b == Blocks.air || !(item instanceof ItemBlock)) return false;
        short tDamage = (short) item.getDamage(stack);

        try (OreInfo<?> info = OreManager.getOreInfo(b, tDamage)) {
            if (info != null) {
                info.isNatural = true;

                ImmutableBlockMeta oreBlock = OreManager.getAdapter(info)
                    .getBlock(info);

                world.setBlock(x, y, z, oreBlock.getBlock(), oreBlock.getBlockMeta(), 3);

                if (oreBlock.matches(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z))) return true;
            }
        }

        world.setBlock(x, y, z, b, tDamage, 0);

        return true;
    }

    // region drop rate calculations

    /**
     * Calculates the average number of separate item drops to be rolled per harvest using information obtained by
     * decompiling IC2.
     *
     * @see TileEntityCrop#harvest_automated(boolean)
     * @param te The {@link TileEntityCrop} holding the crop
     * @param cc The {@link CropCard} of the seed
     * @return The average number of drops to computer per harvest
     */
    private static double getRealAverageDropRounds(TileEntityCrop te, CropCard cc) {
        // this should be ~99.995% accurate
        double chance = (double) cc.dropGainChance() * GTUtility.powInt(1.03, te.getGain());
        // this is essentially just performing an integration using the composite trapezoidal rule.
        double min = -10, max = 10;
        int steps = 10000;
        double stepSize = (max - min) / steps;
        double sum = 0;
        for (int k = 1; k <= steps - 1; k++) {
            sum += getWeightedDropChance(min + k * stepSize, chance);
        }
        double minVal = getWeightedDropChance(min, chance);
        double maxVal = getWeightedDropChance(max, chance);
        return stepSize * ((minVal + maxVal) / 2 + sum);
    }

    /**
     * Evaluates the value of y for a standard normal distribution
     *
     * @param x The value of x to evaluate
     * @return The value of y
     */
    private static double stdNormDistr(double x) {
        return Math.exp(-0.5 * (x * x)) / SQRT2PI;
    }

    private static final double SQRT2PI = Math.sqrt(2.0d * Math.PI);

    /**
     * Calculates the weighted drop chance using
     *
     * @param x      The value rolled by nextGaussian
     * @param chance the base drop chance
     * @return the weighted drop chance
     */
    private static double getWeightedDropChance(double x, double chance) {
        return Math.max(0L, Math.round(x * chance * 0.6827d + chance)) * stdNormDistr(x);
    }

    /**
     * Calculates the average drop of the stack size caused by seed's gain using information obtained by decompiling
     * IC2.
     *
     * @see TileEntityCrop#harvest_automated(boolean)
     * @param te The {@link TileEntityCrop} holding the crop
     * @param cc The {@link CropCard} of the seed
     * @return The average number of drops to computer per harvest
     */
    private static double getRealAverageDropIncrease(TileEntityCrop te, CropCard cc) {
        // yes gain has the amazing ability to sometimes add 1 to your stack size!
        return (te.getGain() + 1) / 100.0d;
    }

    // endregion drop rate calculations

    // region growth time approximation

    /**
     * Calculates the average number growth cycles needed for a crop to grow to maturity.
     *
     * @see EIGIC2Bucket#calcAvgGrowthRate(TileEntityCrop, CropCard, int)
     * @param te The {@link TileEntityCrop} holding the crop
     * @param cc The {@link CropCard} of the seed
     * @return The average growth rate as a floating point number
     */
    private static double calcRealAvgGrowthRate(TileEntityCrop te, CropCard cc,
        HashMap<Integer, Integer> sizeAfterHarvestFrequencies) {
        // Compute growth speeds.
        int[] growthSpeeds = new int[7];
        for (int i = 0; i < 7; i++) growthSpeeds[i] = calcAvgGrowthRate(te, cc, i);

        // if it's stick reed, we know what the distribution should look like
        if (cc.getClass() == CropStickreed.class) {
            sizeAfterHarvestFrequencies.clear();
            sizeAfterHarvestFrequencies.put(1, 1);
            sizeAfterHarvestFrequencies.put(2, 1);
            sizeAfterHarvestFrequencies.put(3, 1);
        }

        // Get the duration of all growth stages
        int[] growthDurations = new int[cc.maxSize()];
        // , index 0 is assumed to be 0 since stage 0 is usually impossible.
        // The frequency table should prevent stage 0 from having an effect on the result.
        growthDurations[0] = 0; // stage 0 doesn't usually exist.
        for (byte i = 1; i < growthDurations.length; i++) {
            te.setSize(i);
            growthDurations[i] = cc.growthDuration(te);
        }

        return calcRealAvgGrowthRate(growthSpeeds, growthDurations, sizeAfterHarvestFrequencies);
    }

    /**
     * Calculates the average number growth cycles needed for a crop to grow to maturity.
     *
     * @implNote This method is entirely self-contained and can therefore be unit tested.
     *
     * @param growthSpeeds        The speeds at which the crop can grow.
     * @param stageGoals          The total to reach for each stage
     * @param startStageFrequency How often the growth starts from a given stage
     * @return The average growth rate as a floating point number
     */
    public static double calcRealAvgGrowthRate(int[] growthSpeeds, int[] stageGoals,
        HashMap<Integer, Integer> startStageFrequency) {

        // taking out the zero rolls out of the calculation tends to make the math more accurate for lower speeds.
        int[] nonZeroSpeeds = Arrays.stream(growthSpeeds)
            .filter(x -> x > 0)
            .toArray();
        int zeroRolls = growthSpeeds.length - nonZeroSpeeds.length;
        if (zeroRolls >= growthSpeeds.length) return -1;

        // compute stage lengths and stage frequencies
        double[] avgCyclePerStage = new double[stageGoals.length];
        double[] normalizedStageFrequencies = new double[stageGoals.length];
        long frequenciesSum = startStageFrequency.values()
            .parallelStream()
            .mapToInt(x -> x)
            .sum();
        for (int i = 0; i < stageGoals.length; i++) {
            avgCyclePerStage[i] = calcAvgCyclesToGoal(nonZeroSpeeds, stageGoals[i]);
            normalizedStageFrequencies[i] = startStageFrequency.getOrDefault(i, 0) * stageGoals.length
                / (double) frequenciesSum;
        }

        // Compute multipliers based on how often the growth starts at a given rate.
        double[] frequencyMultipliers = new double[avgCyclePerStage.length];
        Arrays.fill(frequencyMultipliers, 1.0d);
        conv1DAndCopyToSignal(
            frequencyMultipliers,
            normalizedStageFrequencies,
            new double[avgCyclePerStage.length],
            0,
            frequencyMultipliers.length,
            0);

        // apply multipliers to length
        for (int i = 0; i < avgCyclePerStage.length; i++) avgCyclePerStage[i] *= frequencyMultipliers[i];

        // lengthen average based on number of 0 rolls.
        double average = Arrays.stream(avgCyclePerStage)
            .average()
            .orElse(-1);
        if (average <= 0) return -1;
        if (zeroRolls > 0) {
            average = average / nonZeroSpeeds.length * growthSpeeds.length;
        }

        // profit
        return average;
    }

    /**
     * Computes the average number of rolls of an N sided fair dice with irregular number progressions needed to surpass
     * a given total.
     *
     * @param speeds The speeds at which the crop grows.
     * @param goal   The total to match or surpass.
     * @return The average number of rolls of speeds to meet or surpass the goal.
     */
    private static double calcAvgCyclesToGoal(int[] speeds, int goal) {
        // even if the goal is 0, it will always take at least 1 cycle.
        if (goal <= 0) return 1;
        double mult = 1.0d;
        int goalCap = speeds[speeds.length - 1] * 1000;
        if (goal > goalCap) {
            mult = (double) goal / goalCap;
            goal = goalCap;
        }
        // condition start signal
        double[] signal = new double[goal];
        Arrays.fill(signal, 0);
        signal[0] = 1;

        // Create kernel out of our growth speeds
        double[] kernel = tabulate(speeds, 1.0d / speeds.length);
        double[] convolutionTarget = new double[signal.length];

        // Perform convolutions on the signal until it's too weak to be recognised.
        double p, avgRolls = 1;
        int iterNo = 0;
        // 1e-1 is a threshold, you can increase it for to increase the accuracy of the output.
        // 1e-1 is already accurate enough that any value beyond that is unwarranted.
        int min = speeds[0];
        int max = speeds[speeds.length - 1];
        do {
            avgRolls += p = conv1DAndCopyToSignal(signal, kernel, convolutionTarget, min, max, iterNo);
            iterNo += 1;
        } while (p >= 1e-1 / goal);
        return avgRolls * mult;
    }

    /**
     * Creates an array that corresponds to the amount of times a number appears in a list.
     * <p>
     * Ex: {1,2,3,4} -> {0,1,1,1,1}, {0,2,2,4} -> {1,0,2,0,1}
     *
     * @param bin        The number list to tabulate
     * @param multiplier A multiplier to apply the output list
     * @return The number to tabulate
     */
    private static double[] tabulate(int[] bin, double multiplier) {
        double[] ret = new double[bin[bin.length - 1] + 1];
        Arrays.fill(ret, 0);
        for (int i : bin) ret[i] += multiplier;
        return ret;
    }

    /**
     * Computes a 1D convolution of a signal and stores the results in the signal array. Essentially performs `X <-
     * convolve(X,rev(Y))[1:length(X)]` in R
     *
     * @param signal            The signal to apply the convolution to.
     * @param kernel            The kernel to compute with.
     * @param fixedLengthTarget A memory optimisation so we don't just create a ton of arrays since we overwrite it.
     *                          Should be the same length as the signal.
     */
    private static double conv1DAndCopyToSignal(double[] signal, double[] kernel, double[] fixedLengthTarget,
        int minValue, int maxValue, int iterNo) {
        // for a 1d convolution we would usually use kMax = signal.length + kernel.length - 1
        // but since we are directly applying our result to our signal, there is no reason to compute
        // values where k > signal.length.
        // we could probably run this loop in parallel.
        double sum = 0;
        int maxK = Math.min(signal.length, (iterNo + 1) * maxValue + 1);
        int startAt = Math.min(signal.length, minValue * (iterNo + 1));
        int k = Math.max(0, startAt - kernel.length);
        for (; k < startAt; k++) fixedLengthTarget[k] = 0;
        for (; k < maxK; k++) {
            // I needs to be a valid index of the kernel.
            fixedLengthTarget[k] = 0;
            for (int i = Math.max(0, k - kernel.length + 1); i <= k; i++) {
                double v = signal[i] * kernel[k - i];
                sum += v;
                fixedLengthTarget[k] += v;
            }
        }
        System.arraycopy(fixedLengthTarget, 0, signal, 0, signal.length);
        return sum;
    }

    /**
     * Calculates the average growth rate of an ic2 crop using information obtained though decompiling IC2. Calls to
     * random functions have been either replaced with customisable values or boundary tests.
     *
     * @see TileEntityCrop#calcGrowthRate()
     * @param te      The {@link TileEntityCrop} holding the crop
     * @param cc      The {@link CropCard} of the seed
     * @param rngRoll The role for the base rng
     * @return The amounts of growth point added to the growth progress in average every growth tick
     */
    private static int calcAvgGrowthRate(TileEntityCrop te, CropCard cc, int rngRoll) {
        // the original logic uses IC2.random.nextInt(7)
        int base = 3 + rngRoll + te.getGrowth();
        int need = Math.max(0, (cc.tier() - 1) * 4 + te.getGrowth() + te.getGain() + te.getResistance());
        int have = cc.weightInfluences(te, te.getHumidity(), te.getNutrients(), te.getAirQuality()) * 5;

        if (have >= need) {
            // The crop has a good enough environment to grow normally
            return base * (100 + (have - need)) / 100;
        } else {
            // this only happens if we don't have enough
            // resources to grow properly.
            int neg = (need - have) * 4;

            if (neg > 100) {
                // a crop with a resistance 31 will never die since the original
                // checks for `IC2.random.nextInt(32) > this.statResistance`
                // so assume that the crop will eventually die if it doesn't
                // have maxed out resistance stats. 0 means no growth this tick
                // -1 means the crop dies.
                return te.getResistance() >= 31 ? 0 : -1;
            }
            // else apply neg to base
            return Math.max(0, base * (100 - neg) / 100);
        }
    }

    // endregion growth time approximation

    // region deterministic environmental calculations

    /**
     * Calculates the humidity at the location of the controller using information obtained by decompiling IC2. Returns
     * 0 if the greenhouse is in no humidity mode.
     *
     * @see EIGIC2Bucket#IS_ON_WET_FARMLAND
     * @see EIGIC2Bucket#WATER_STORAGE_VALUE
     * @see TileEntityCrop#updateHumidity()
     * @param greenhouse The {@link MTEExtremeIndustrialGreenhouse} that holds the seed.
     * @return The humidity environmental value at the controller's location.
     */
    public static byte getHumidity(MTEExtremeIndustrialGreenhouse greenhouse, boolean useNoHumidity) {
        if (useNoHumidity) return 0;
        int value = Crops.instance.getHumidityBiomeBonus(
            greenhouse.getBaseMetaTileEntity()
                .getBiome());
        if (IS_ON_WET_FARMLAND) value += 2;
        // we add 2 if we have more than 5 water in storage
        if (WATER_STORAGE_VALUE >= 5) value += 2;
        // add 1 for every 25 water stored (max of 200
        value += (WATER_STORAGE_VALUE + 24) / 25;
        return (byte) value;
    }

    /**
     * Calculates the nutrient value at the location of the controller using information obtained by decompiling IC2
     *
     * @see EIGIC2Bucket#NUMBER_OF_DIRT_BLOCKS_UNDER
     * @see EIGIC2Bucket#FERTILIZER_STORAGE_VALUE
     * @see TileEntityCrop#updateNutrients()
     * @param greenhouse The {@link MTEExtremeIndustrialGreenhouse} that holds the seed.
     * @return The nutrient environmental value at the controller's location.
     */
    public static byte getNutrients(MTEExtremeIndustrialGreenhouse greenhouse) {
        int value = Crops.instance.getNutrientBiomeBonus(
            greenhouse.getBaseMetaTileEntity()
                .getBiome());
        value += NUMBER_OF_DIRT_BLOCKS_UNDER;
        value += (FERTILIZER_STORAGE_VALUE + 19) / 20;
        return (byte) value;
    }

    /**
     * Calculates the air quality at the location of the controller bucket using information obtained by decompiling IC2
     *
     * @see EIGIC2Bucket#CROP_OBSTRUCTION_VALUE
     * @see EIGIC2Bucket#CROP_CAN_SEE_SKY
     * @see TileEntityCrop#updateAirQuality()
     * @param greenhouse The {@link MTEExtremeIndustrialGreenhouse} that holds the seed.
     * @return The air quality environmental value at the controller's location.
     */
    public static byte getAirQuality(MTEExtremeIndustrialGreenhouse greenhouse) {
        // clamp height bonus to 0-4, use the height of the crop itself
        // TODO: check if we want to add the extra +2 for the actual height of the crop stick in the EIG.
        int value = Math.max(
            0,
            Math.min(
                4,
                (greenhouse.getBaseMetaTileEntity()
                    .getYCoord() - 64) / 15));
        // min value of fresh is technically 8 since the crop itself will count as an obstruction at xOff = 0, zOff = 0
        value += CROP_OBSTRUCTION_VALUE / 2;
        // you get a +2 bonus for being able to see the sky
        if (CROP_CAN_SEE_SKY) value += 2;
        return (byte) value;
    }

    // endregion deterministic environmental calculations

    private static class FakeTileEntityCrop extends TileEntityCrop {

        private boolean isValid;
        public Set<Block> reqBlockSet = new HashSet<>();
        public Set<String> reqBlockOreDict = new HashSet<>();
        private int lightLevel = 15;

        public FakeTileEntityCrop(EIGIC2Bucket bucket, MTEExtremeIndustrialGreenhouse greenhouse, int[] xyz) {
            super();
            this.isValid = false;
            this.ticker = 1;

            // put seed in crop stick
            CropCard cc = Crops.instance.getCropCard(bucket.seed);
            this.setCrop(cc);
            NBTTagCompound nbt = bucket.seed.getTagCompound();
            this.setGrowth(nbt.getByte("growth"));
            this.setGain(nbt.getByte("gain"));
            this.setResistance(nbt.getByte("resistance"));
            this.setWorldObj(
                greenhouse.getBaseMetaTileEntity()
                    .getWorld());

            this.xCoord = xyz[0];
            this.yCoord = xyz[1];
            this.zCoord = xyz[2];
            this.blockType = Block.getBlockFromItem(Ic2Items.crop.getItem());
            this.blockMetadata = 0;

            this.waterStorage = bucket.useNoHumidity ? 0 : WATER_STORAGE_VALUE;
            this.humidity = EIGIC2Bucket.getHumidity(greenhouse, bucket.useNoHumidity);
            this.nutrientStorage = FERTILIZER_STORAGE_VALUE;
            this.nutrients = EIGIC2Bucket.getNutrients(greenhouse);
            this.airQuality = EIGIC2Bucket.getAirQuality(greenhouse);

            this.isValid = true;
        }

        public boolean canMature() {
            CropCard cc = this.getCrop();
            this.size = cc.maxSize() - 1;
            // try with a high light level
            this.lightLevel = 15;
            if (cc.canGrow(this)) return true;
            // and then with a low light level.
            this.lightLevel = 9;
            return cc.canGrow(this);
        }

        @Override
        public boolean isBlockBelow(Block reqBlock) {
            this.reqBlockSet.add(reqBlock);
            return super.isBlockBelow(reqBlock);
        }

        @Override
        public boolean isBlockBelow(String oreDictionaryName) {
            this.reqBlockOreDict.add(oreDictionaryName);
            return super.isBlockBelow(oreDictionaryName);
        }

        // region environment simulation

        @Override
        public int getLightLevel() {
            // 9 should allow most light dependent crops to grow
            // the only exception I know of the eating plant which checks
            return this.lightLevel;
        }

        @Override
        public byte getHumidity() {
            return this.humidity;
        }

        @Override
        public byte updateHumidity() {
            return this.humidity;
        }

        @Override
        public byte getNutrients() {
            return this.nutrients;
        }

        @Override
        public byte updateNutrients() {
            return this.nutrients;
        }

        @Override
        public byte getAirQuality() {
            return this.airQuality;
        }

        @Override
        public byte updateAirQuality() {
            return this.nutrients;
        }

        // endregion environment simulation

        /**
         * Updates the nutrient value based on the fact tha the crop needs a block under it.
         */
        public void updateNutrientsForBlockUnder() {
            // -1 because the farm land is included in the root check.
            if ((this.getCrop()
                .getrootslength(this) - 1
                - NUMBER_OF_DIRT_BLOCKS_UNDER) <= 0 && this.nutrients > 0) {
                this.nutrients--;
            }
        }

        /**
         * Checks if the crop stick has requested a block to be under it yet.
         *
         * @return true if a block under check was made.
         */
        public boolean hasRequestedBlockUnder() {
            return !this.reqBlockSet.isEmpty() || !this.reqBlockOreDict.isEmpty();
        }
    }

}
