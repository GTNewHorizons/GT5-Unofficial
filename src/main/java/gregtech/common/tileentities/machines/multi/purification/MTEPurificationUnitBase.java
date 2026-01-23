package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.gui.modularui.multiblock.MTEPurificationUnitBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Base class for purification units. This class handles all shared behaviour between units. When inheriting from this,
 * make sure to call super.loadNBTData() and super.saveNBTData() if you override these methods, or linking will break.
 */
public abstract class MTEPurificationUnitBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    /**
     * Ratio of output fluid that needs to be inserted back as input to trigger a "water boost". Must be in [0, 1].
     */
    public static final float WATER_BOOST_NEEDED_FLUID = 0.1f;
    /**
     * Additive bonus to success chance when water boost is active. Must be in [0, 1]
     */
    public static final float WATER_BOOST_BONUS_CHANCE = 0.15f;

    /**
     * Small internal enum to report back the various error cases when linking purification units to the purification
     * plant.
     */
    private enum LinkResult {
        /**
         * Link target was out of range of the main controller
         */
        TOO_FAR,
        /**
         * No valid MTEPurificationPlant was found at the link target position.
         */
        NO_VALID_PLANT,
        /**
         * Link successful
         */
        SUCCESS,
    }

    /**
     * Coordinates of the main purification plant controller. These can be used to find the controller again on world
     * load.
     */
    private int controllerX, controllerY, controllerZ;

    /**
     * Whether a controller was previously set.
     */
    private boolean controllerSet = false;

    /**
     * Pointer to the main purification plant controller.
     */
    private MTEPurificationPlant controller = null;

    /**
     * The current recipe being run in the purification unit. Note that purification unit recipes are a bit special, so
     * input and output in the recipe might not exactly match the required inputs and produced outputs. For more
     * information, always look at the purification unit tooltip and implementation.
     */
    protected GTRecipe currentRecipe = null;

    /**
     * Current chance of the recipe succeeding, always in [0, 100]. A chance above 100 will be interpreted as 100.
     */
    protected float currentRecipeChance = 0.0f;

    /**
     * Configured parallel amount. Only water I/O and power scale.
     */
    protected int maxParallel = 1;

    protected int effectiveParallel = 1;

    protected ArrayList<FluidStack> storedFluids = null;

    protected MTEPurificationUnitBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEPurificationUnitBase(String aName) {
        super(aName);
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // The individual purification unit structures cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    protected void setHatchRecipeMap(MTEHatchInput hatch) {
        // Do nothing, we don't want to lock hatches to recipe maps since this can cause
        // them to reject our catalyst fluids
    }

    /**
     * Used to more easily grab a correct texture index from a block + meta.
     *
     * @param block Block to use as base. Must implement GT_Block_Casings_Abstract
     * @param meta  Metadata of the block to pick the actual block
     * @return The correct index into the global texture atlas.
     */
    protected static int getTextureIndex(Block block, int meta) {
        return ((BlockCasingsAbstract) block).getTextureIndex(meta);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        // Try to re-link to controller periodically, for example on game load.
        if (aTimer % 100 == 5 && controllerSet && getController() == null) {
            trySetControllerFromCoord(controllerX, controllerY, controllerZ);
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        // Main controller updates progress time. We can do I/O logic here.
        // The logic for operating purification units is typically implemented by overriding this behaviour.
        if (mMaxProgresstime > 0) {
            this.markDirty();
            // Do not take maintenance into consideration, because purification units do not get
            // maintenance issues.
            // Technically, this entire efficiency stat is a bit useless for purification units, since
            // their power draw does not actually depend on it, but it's nice to keep around for consistency with other
            // multiblocks. This way, you still gradually see the efficiency go down when it powers down.
            mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
        }
    }

    protected CheckRecipeResult findRecipeForInputs(FluidStack[] fluidInputs, ItemStack... itemInputs) {
        RecipeMap<?> recipeMap = this.getRecipeMap();

        // Grab a stream of recipes and find the one with the highest success chance
        Stream<GTRecipe> recipes = recipeMap.findRecipeQuery()
            .fluids(fluidInputs)
            .items(itemInputs)
            .findAll();
        GTRecipe recipe = recipes
            .max(Comparator.comparing(r -> r.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f)))
            .orElse(null);

        if (recipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (this.protectsExcessFluid() && !this.canOutputAll(recipe.mFluidOutputs)) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        if (this.protectsExcessItem() && !this.canOutputAll(recipe.mOutputs)) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.currentRecipe = recipe;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /**
     * By default, only checks fluid input.
     *
     * @return
     */
    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        this.storedFluids = this.getStoredFluids();
        CheckRecipeResult result = overrideRecipeCheck();
        if (result == null) result = findRecipeForInputs(storedFluids.toArray(new FluidStack[] {}));

        // If we had a successful result, calculate effective parallel
        if (result.wasSuccessful()) {
            FluidStack waterInput = this.currentRecipe.mFluidInputs[0];
            // Count total available purified water input of the previous step
            long amountAvailable = 0;
            for (FluidStack fluid : this.storedFluids) {
                if (fluid.isFluidEqual(waterInput)) {
                    amountAvailable += fluid.amount;
                }
            }

            // Determine effective parallel
            effectiveParallel = (int) Math.min(maxParallel, Math.floorDiv(amountAvailable, waterInput.amount));
            // This should not happen, throw an error
            if (effectiveParallel == 0) return CheckRecipeResultRegistry.INTERNAL_ERROR;
        }

        return result;
    }

    public CheckRecipeResult overrideRecipeCheck() {
        return null;
    }

    /**
     * Equivalent to checkRecipe(), but public because the purification plant needs to access it and checkRecipe() is
     * protected.
     *
     * @return True if successfully found a recipe and/or started processing/
     */
    public boolean doPurificationRecipeCheck() {
        effectiveParallel = 1;
        return this.checkRecipe();
    }

    /**
     * Get the success chance of the recipe, from 0 to 100. Never call this while a recipe is running, because items or
     * modifiers used to boost might disappear by the time recipe check comes around, which would invalidate this
     * result.
     */
    public float calculateBoostedSuccessChance() {
        // If this.currentRecipe is null, there is a bug, so throwing a NPE is fine.
        float recipeChance = this.currentRecipe.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f);
        // Apply water boost if available.
        if (isWaterBoosted(this.currentRecipe)) {
            recipeChance = Math.min(recipeChance + WATER_BOOST_BONUS_CHANCE * 100.0f, 100.0f);
        }
        return recipeChance;
    }

    /**
     * By default, the final recipe success chance is simply the success chance calculated on recipe check. This applies
     * water boosts when needed to the base chance. Purification units can override this to perform more complex success
     * chance calculations, that even take into account what happened during the runtime of the recipe.
     *
     * @return The success chance of the recipe, at the point in time the outputs are to be produced.
     */
    public float calculateFinalSuccessChance() {
        return this.currentRecipeChance;
    }

    /**
     * Get the tier of water this unit makes. Starts at 1.
     */
    public abstract int getWaterTier();

    /**
     * Get the amount of water needed to execute a water boost, in mb.
     */
    public FluidStack getWaterBoostAmount(GTRecipe recipe) {
        // Recipes should always be constructed so that output water is always the first fluid output
        FluidStack outputWater = recipe.mFluidOutputs[0];
        int amount = Math.round(outputWater.amount * WATER_BOOST_NEEDED_FLUID * this.effectiveParallel);
        return new FluidStack(outputWater.getFluid(), amount);
    }

    /**
     * Returns true if this purification unit contains enough water to apply a water boost for the selected recipe. This
     * should only be called during recipe check! Never call this while a recipe is running, because water used to boost
     * might disappear by the time recipe check comes around, which would invalidate this result.
     *
     * @param recipe The recipe to check the water boost of
     */
    public boolean isWaterBoosted(GTRecipe recipe) {
        FluidStack inputWater = getWaterBoostAmount(recipe);
        // Simulate input drain to see if we can water boost
        return depleteInput(inputWater, true);
    }

    /**
     * Consumes all <b>fluid</b> inputs of the current recipe. Should only scale the first fluid input with water
     */
    public void depleteRecipeInputs() {
        for (int i = 0; i < this.currentRecipe.mFluidInputs.length; ++i) {
            FluidStack input = this.currentRecipe.mFluidInputs[i];
            FluidStack copyWithParallel = input.copy();
            if (i == 0) {
                copyWithParallel.amount = input.amount * effectiveParallel;
            }
            this.depleteInput(copyWithParallel);
        }
    }

    /**
     * Called after a recipe is found and accepted.
     *
     * @param cycleTime    Time for a full cycle to complete
     * @param progressTime Current progress time
     */
    public void startCycle(int cycleTime, int progressTime) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        startRecipeProcessing();
        // Important to calculate this before depleting inputs, otherwise we may get issues with boost items
        // disappearing.
        this.currentRecipeChance = this.calculateBoostedSuccessChance();

        // Deplete inputs from water boost if enabled.
        if (isWaterBoosted(this.currentRecipe)) {
            FluidStack inputWater = this.getWaterBoostAmount(this.currentRecipe);
            this.depleteInput(inputWater);
        }

        // Consume inputs, only if debug mode is off
        if (!getController().debugModeOn()) {
            this.depleteRecipeInputs();
        }
        // Initialize recipe and progress information.
        this.mMaxProgresstime = cycleTime;
        this.mProgresstime = progressTime;
        this.mEfficiency = 10000;
        // These need to be set so the GUI code can display the produced outputs

        // Make sure to scale purified water output with parallel amount.
        // Make sure to make a full copy of the array, so we don't go modifying recipes
        FluidStack[] fluidOutputs = new FluidStack[this.currentRecipe.mFluidOutputs.length];
        for (int i = 0; i < this.currentRecipe.mFluidOutputs.length; ++i) {
            fluidOutputs[i] = this.currentRecipe.mFluidOutputs[i].copy();
            // Clamp the fluid output to max int to avoid overflow at extreme parallels
            fluidOutputs[i].amount = (int) Math
                .min((long) effectiveParallel * fluidOutputs[i].amount, Integer.MAX_VALUE);
        }

        ItemStack[] recipeOutputs = this.currentRecipe.mOutputs;
        ItemStack[] itemOutputs = new ItemStack[recipeOutputs.length];
        int[] mChances = this.currentRecipe.mChances;

        // If this recipe has random item outputs, roll on it and add to outputs
        if (mChances != null) {
            // Roll on each output individually
            for (int i = 0; i < recipeOutputs.length; ++i) {
                // Recipes store probabilities as a value ranging from 1-10000
                int roll = random.nextInt(10000);
                if (roll <= mChances[i]) {
                    itemOutputs[i] = recipeOutputs[i].copy();
                }
            }
        } else {
            // Guaranteed item output
            for (int i = 0; i < recipeOutputs.length; ++i) {
                itemOutputs[i] = recipeOutputs[i].copy();
            }
        }

        this.mOutputFluids = fluidOutputs;
        this.mOutputItems = itemOutputs;
        // Set this value, so it can be displayed in Waila. Note that the logic for the units is
        // specifically overridden so setting this value does not actually drain power.
        // Instead, power is drained by the main purification plant controller.
        this.lEUt = -this.getActualPowerUsage();
        endRecipeProcessing();
    }

    public void addRecipeOutputs() {
        if (mOutputFluids != null) this.addFluidOutputs(mOutputFluids);
        if (mOutputItems != null) this.addItemOutputs(mOutputItems);
    }

    public void endCycle() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Only add output if debug mode was not on
        if (!getController().debugModeOn()) {
            // First see if the recipe succeeded. For some reason random.nextFloat does not compile, so we use this
            // hack instead.
            float successRoll = random.nextInt(0, 10000) / 100.0f;
            if (successRoll <= calculateFinalSuccessChance()) {
                addRecipeOutputs();
            } else {
                onRecipeFail();
            }
        }

        // Reset recipe values for next iteration
        checkRecipeResult = CheckRecipeResultRegistry.CYCLE_IDLE;
        this.mMaxProgresstime = 0;
        this.mProgresstime = 0;
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.currentRecipe = null;
        this.currentRecipeChance = 0.0f;
        this.mOutputItems = null;
        this.mOutputFluids = null;
        this.effectiveParallel = 1;
    }

    /**
     * Outputs fluid when recipe fails.
     */
    private void onRecipeFail() {
        // Possibly output lower quality water.
        // Note that if there is no space for this, it will be voided regardless of fluid void setting!
        if (mOutputFluids != null) {
            FluidStack outputWater = getDegradedOutputWater();
            this.addOutput(outputWater);
        }
    }

    /**
     * On recipe fail, water quality may degrade to the same or lower tier. This function returns the water to output in
     * this case, or null if no water is produced at all.
     */
    private FluidStack getDegradedOutputWater() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int roll = random.nextInt(0, 2);
        // 50% chance to not output anything at all
        if (roll == 0) return null;

        for (int waterTier = getWaterTier(); waterTier > 0; --waterTier) {
            // 50% chance every time of degrading into the previous tier
            roll = random.nextInt(0, 2);
            if (roll == 1) {
                // Rolled good, stop the loop and output water below current tier
                int amount = mOutputFluids[0].amount;
                // For tier 1, this is distilled water, so we cannot use the helper function!
                if (waterTier == 1) {
                    return GTModHandler.getDistilledWater(amount);
                }
                Materials water = PurifiedWaterHelpers.getPurifiedWaterTier(waterTier - 1);
                return water.getFluid(amount);
            }
            // Bad roll, keep looping and degrade quality even further
        }
        // Rolled bad on every iteration, no output for you
        return null;
    }

    /**
     * Get the EU/t usage of this unit while it is running.
     */
    public abstract long getBasePowerUsage();

    public long getActualPowerUsage() {
        return getBasePowerUsage() * effectiveParallel;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // The individual purification unit structures cannot have maintenance issues, so fix them all.
        this.mCrowbar = true;
        this.mWrench = true;
        this.mHardHammer = true;
        this.mSoftMallet = true;
        this.mSolderingTool = true;
        this.mScrewdriver = true;
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // If a linked controller was found, load its coordinates.
        // The unit will try to link to the real controller block periodically in onPostTick()
        // We cannot do this linking here yet because the controller block might not be loaded yet.
        // TODO: We could try though?
        if (aNBT.hasKey("controller")) {
            NBTTagCompound controllerNBT = aNBT.getCompoundTag("controller");
            controllerX = controllerNBT.getInteger("x");
            controllerY = controllerNBT.getInteger("y");
            controllerZ = controllerNBT.getInteger("z");
            controllerSet = true;
        }
        currentRecipeChance = aNBT.getFloat("currentRecipeChance");
        if (aNBT.hasKey("configuredParallel")) {
            maxParallel = aNBT.getInteger("configuredParallel");
        }
        if (aNBT.hasKey("effectiveParallel")) {
            effectiveParallel = aNBT.getInteger("effectiveParallel");
        }
    }

    public NBTTagCompound saveLinkDataToNBT() {
        NBTTagCompound controllerNBT = new NBTTagCompound();
        controllerNBT.setInteger("x", controllerX);
        controllerNBT.setInteger("y", controllerY);
        controllerNBT.setInteger("z", controllerZ);
        return controllerNBT;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (controllerSet) {
            NBTTagCompound controllerNBT = saveLinkDataToNBT();
            aNBT.setTag("controller", controllerNBT);
        }
        aNBT.setFloat("currentRecipeChance", currentRecipeChance);
        aNBT.setInteger("configuredParallel", maxParallel);
        aNBT.setInteger("effectiveParallel", effectiveParallel);
    }

    private LinkResult trySetControllerFromCoord(int x, int y, int z) {
        IGregTechTileEntity ourBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // First check whether the controller we try to link to is within range. The range is defined
        // as a max distance in each axis.
        if (Math.abs(ourBaseMetaTileEntity.getXCoord() - x) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getYCoord() - y) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getZCoord() - z) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;

        // Find the block at the requested coordinated and check if it is a purification plant controller.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return LinkResult.NO_VALID_PLANT;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return LinkResult.NO_VALID_PLANT;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof MTEPurificationPlant)) return LinkResult.NO_VALID_PLANT;

        // Before linking, unlink from current controller, so we don't end up with units linked to multiple
        // controllers.
        MTEPurificationPlant oldController = getController();
        if (oldController != null) {
            oldController.unregisterLinkedUnit(this);
            this.unlinkController();
        }

        // Now link to new controller
        controllerX = x;
        controllerY = y;
        controllerZ = z;
        controllerSet = true;
        controller = (MTEPurificationPlant) metaTileEntity;
        controller.registerLinkedUnit(this);
        return LinkResult.SUCCESS;
    }

    private boolean tryLinkDataStick(EntityPlayer aPlayer) {
        // Make sure the held item is a data stick
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return false;
        }

        // Make sure this data stick is a proper purification plant link data stick.
        if (!dataStick.hasTagCompound() || !dataStick.stackTagCompound.getString("type")
            .equals("PurificationPlant")) {
            return false;
        }

        // Now read link coordinates from the data stick.
        NBTTagCompound nbt = dataStick.stackTagCompound;
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");

        // Try to link, and report the result back to the player.
        LinkResult result = trySetControllerFromCoord(x, y, z);
        if (result == LinkResult.SUCCESS) {
            aPlayer.addChatMessage(new ChatComponentText("Link successful"));
        } else if (result == LinkResult.TOO_FAR) {
            aPlayer.addChatMessage(new ChatComponentText("Link failed: Out of range."));
        } else if (result == LinkResult.NO_VALID_PLANT) {
            aPlayer.addChatMessage(new ChatComponentText("Link failed: No Purification Plant found at link location"));
        }

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {

        if (aBaseMetaTileEntity.getWorld().isRemote) {
            return true;
        }
        // Right-clicking could be a data stick linking action, so try this first.
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }

        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    public MTEPurificationPlant getController() {
        if (controller == null) return null;
        // Controller disappeared
        if (controller.getBaseMetaTileEntity() == null) return null;
        return controller;
    }

    // If the controller is broken this can be called to explicitly unlink the controller, so we don't have any
    // references lingering around
    public void unlinkController() {
        this.controllerSet = false;
        this.controller = null;
        this.controllerX = 0;
        this.controllerY = 0;
        this.controllerZ = 0;
    }

    @Override
    public void onBlockDestroyed() {
        // When this block is destroyed, explicitly unlink it from the controller if there is any.
        MTEPurificationPlant controller = getController();
        if (controller != null) {
            controller.unregisterLinkedUnit(this);
        }
        super.onBlockDestroyed();
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        // If this purification unit is linked to a controller, add this info to the scanner output.
        if (getController() != null) {
            ret.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.purification_unit_base.linked_at",
                    controllerX,
                    controllerY,
                    controllerZ));

            // If recipe is running, display success chance
            if (this.mMaxProgresstime != 0) {
                ret.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.infodata.purification_unit_base.success_chance",
                        EnumChatFormatting.YELLOW + formatNumber(this.calculateFinalSuccessChance())
                            + "%"
                            + EnumChatFormatting.RESET));
            }

        } else ret.add(StatCollector.translateToLocal("GT5U.infodata.purification_unit_base.not_linked"));
        ret.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.parallel.current",
                "" + EnumChatFormatting.YELLOW + this.effectiveParallel));
        return ret.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.getBoolean("linked")) {
            currenttip.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.purification_unit_base.linked_to",
                    tag.getInteger("controllerX"),
                    tag.getInteger("controllerY"),
                    tag.getInteger("controllerZ")));
        } else {
            currenttip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("GT5U.waila.base.unlinked"));
        }

        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {

        tag.setBoolean("linked", getController() != null);
        if (getController() != null) {
            tag.setInteger("controllerX", controllerX);
            tag.setInteger("controllerY", controllerY);
            tag.setInteger("controllerZ", controllerZ);
        }

        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    public PurificationUnitStatus status() {
        if (!this.mMachine) {
            return PurificationUnitStatus.INCOMPLETE_STRUCTURE;
        } else if (!this.isAllowedToWork()) {
            return PurificationUnitStatus.DISABLED;
        } else {
            return PurificationUnitStatus.ONLINE;
        }
    }

    /**
     * Creates all widgets needed to sync this unit's status with the client
     */
    public Widget makeSyncerWidgets() {
        return new MultiChildWidget()
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> this.mMachine, machine -> this.mMachine = machine))
            .addChild(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, _work -> {}));
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEPurificationUnitBaseGui(this);
    }

    public int getMaxParallel() {
        return maxParallel;
    }

    public void setMaxParallel(int value) {
        maxParallel = value;
    }

    @Override
    public boolean supportsMaintenanceIssueHoverable() {
        return false;
    }

    @Override
    public boolean supportsLogo() {
        return false;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return EnumSet.of(VoidingMode.VOID_NONE);
    }

    @Override
    protected boolean supportsCraftingMEBuffer() {
        return false;
    }
}
