package gregtech.common.tileentities.machines.multi.purification;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Base class for purification units. This class handles all shared behaviour between units.
 * When inheriting from this, make sure to call super.loadNBTData() and super.saveNBTData()
 * if you override these methods, or linking will break.
 */
public abstract class GT_MetaTileEntity_PurificationUnitBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    // TODO: Balancing
    public static final float WATER_BOOST_NEEDED_FLUID = 0.1f;
    // TODO: Balancing. This is an additive boost
    public static final float WATER_BOOST_BONUS_CHANCE = 0.15f;

    /**
     * Small internal enum to report back the various error cases when linking purification units to the
     * purification plant.
     */
    private enum LinkResult {
        /**
         * Link target was out of range of the main controller
         */
        TOO_FAR,
        /**
         * No valid GT_MetaTileEntity_PurificationPlant was found at the link target position.
         */
        NO_VALID_PLANT,
        /**
         * Link successful
         */
        SUCCESS,
    }

    /**
     * Coordinates of the main purification plant controller. These can be used to find the controller again
     * on world load.
     */
    private int controllerX, controllerY, controllerZ;

    /**
     * Whether a controller was previously set.
     */
    private boolean controllerSet = false;

    /**
     * Pointer to the main purification plant controller.
     */
    private GT_MetaTileEntity_PurificationPlant controller = null;

    protected GT_Recipe currentRecipe = null;

    protected float currentRecipeChance = 0.0f;

    protected GT_MetaTileEntity_PurificationUnitBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitBase(String aName) {
        super(aName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // The individual purification unit structures cannot have maintenance issues, so do nothing.
        return true;
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
        if (mMaxProgresstime > 0) {
            this.markDirty();
            mEfficiency = Math.max(
                0,
                Math.min(
                    mEfficiency + mEfficiencyIncrease,
                    getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
        }
    }

    public boolean doPurificationRecipeCheck() {
        return this.checkRecipe();
    }

    /**
     * Get the success chance of the recipe, from 0 to 1. Never call this while a recipe is running, because items
     * or modifiers used to boost might disappear by the time recipe check comes around,
     * which would invalidate this result.
     */
    public float calculateBoostedSuccessChance() {
        // TODO: Should we error if this is null?
        float recipeChance = this.currentRecipe.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f);
        if (isWaterBoosted(this.currentRecipe)) {
            recipeChance = Math.min(recipeChance + WATER_BOOST_BONUS_CHANCE, 1.0f);
        }
        return recipeChance;
    }

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
    public FluidStack getWaterBoostAmount(GT_Recipe recipe) {
        // Recipes should always be constructed so that output water is always the first fluid output
        FluidStack outputWater = recipe.mFluidOutputs[0];
        int amount = Math.round(outputWater.amount * WATER_BOOST_NEEDED_FLUID);
        return new FluidStack(outputWater.getFluid(), amount);
    }

    /**
     * Returns true if this purification unit contains enough water to apply a water boost for the selected recipe.
     * This should only be called during recipe check! Never call this while a recipe is running, because water used to
     * boost might disappear by the time recipe check comes around, which would invalidate this result.
     *
     * @param recipe The recipe to check the water boost of
     */
    public boolean isWaterBoosted(GT_Recipe recipe) {
        FluidStack inputWater = getWaterBoostAmount(recipe);
        // Simulate input drain to see if we can water boost
        return depleteInput(inputWater, true);
    }

    public void depleteRecipeInputs() {
        for (FluidStack input : this.currentRecipe.mFluidInputs) {
            this.depleteInput(input);
        }
    }

    /**
     * Called after a recipe is found and accepted.
     *
     * @param cycleTime    Time for a full cycle to complete
     * @param progressTime Current progress time
     */
    public void startCycle(int cycleTime, int progressTime) {
        // Important to calculate this before depleting inputs, otherwise we may get issues with boost items
        // disappearing.
        this.currentRecipeChance = this.calculateBoostedSuccessChance();

        // Deplete inputs from water boost if enabled.
        if (isWaterBoosted(this.currentRecipe)) {
            FluidStack inputWater = this.getWaterBoostAmount(this.currentRecipe);
            this.depleteInput(inputWater);
        }

        this.depleteRecipeInputs();

        this.mMaxProgresstime = cycleTime;
        this.mProgresstime = progressTime;
        this.mEfficiency = 10000;
        this.mOutputFluids = this.currentRecipe.mFluidOutputs;
        this.mOutputItems = this.currentRecipe.mOutputs;
        // Set this value, so it can be displayed in Waila. Note that the logic for the units is
        // specifically overridden so setting this value does not actually drain power.
        // Instead, power is drained by the main purification plant controller.
        this.lEUt = -this.getActivePowerUsage();
    }

    public void addRecipeOutputs() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.addFluidOutputs(this.currentRecipe.mFluidOutputs);
        // If this recipe has random item outputs, roll on it and add outputs
        if (this.currentRecipe.mChances != null) {
            // Roll on each output individually
            for (int i = 0; i < this.currentRecipe.mOutputs.length; ++i) {
                // Recipes store probabilities as a value ranging from 1-10000
                int roll = random.nextInt(10000);
                if (roll <= this.currentRecipe.mChances[i]) {
                    this.addOutput(this.currentRecipe.mOutputs[i]);
                }
            }
        } else {
            // Guaranteed item output
            for (int i = 0; i < this.currentRecipe.mOutputs.length; ++i) {
                this.addOutput(this.currentRecipe.mOutputs[i]);
            }
        }
    }

    public void endCycle() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // First see if the recipe succeeded. For some reason random.nextFloat does not compile, so we use this
        // hack instead.
        float successRoll = random.nextInt(0, 10000) / 100.0f;
        if (successRoll <= calculateFinalSuccessChance()) {
            addRecipeOutputs();
        } else {
            onRecipeFail();
        }

        // Reset recipe values for next iteration
        this.mMaxProgresstime = 0;
        this.mProgresstime = 0;
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.currentRecipe = null;
        this.currentRecipeChance = 0.0f;
        this.mOutputItems = null;
        this.mOutputFluids = null;
    }

    /**
     * Outputs fluid when recipe fails.
     */
    private void onRecipeFail() {
        // Possibly output lower quality water.
        // Note that if there is no space for this, it will be voided regardless of fluid void setting!
        FluidStack outputWater = getDegradedOutputWater();
        this.addOutput(outputWater);
    }

    /**
     * On recipe fail, water quality may degrade to the same or lower tier. This function returns the water to output
     * in this case, or null if no water is produced at all.
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
                int amount = this.currentRecipe.mFluidOutputs[0].amount;
                // For tier 1, this is distilled water, so we cannot use the helper function!
                if (waterTier == 1) {
                    return GT_ModHandler.getDistilledWater(amount);
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
    public abstract long getActivePowerUsage();

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // The individual purification unit structures cannot have maintenance issues, so fix them all.
        this.mCrowbar = true;
        this.mWrench = true;
        this.mHardHammer = true;
        this.mSoftHammer = true;
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
        if (aNBT.hasKey("controller")) {
            NBTTagCompound controllerNBT = aNBT.getCompoundTag("controller");
            controllerX = controllerNBT.getInteger("x");
            controllerY = controllerNBT.getInteger("y");
            controllerZ = controllerNBT.getInteger("z");
            controllerSet = true;
        }
        currentRecipeChance = aNBT.getFloat("currentRecipeChance");
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
    }

    private LinkResult trySetControllerFromCoord(int x, int y, int z) {
        IGregTechTileEntity ourBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // First check whether the controller we try to link to is within range. The range is defined
        // as a max distance in each axis.
        if (Math.abs(ourBaseMetaTileEntity.getXCoord() - x) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getYCoord() - y) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getZCoord() - z) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;

        // Find the block at the requested coordinated and check if it is a purification plant controller.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return LinkResult.NO_VALID_PLANT;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return LinkResult.NO_VALID_PLANT;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof GT_MetaTileEntity_PurificationPlant)) return LinkResult.NO_VALID_PLANT;

        // Before linking, unlink from current controller, so we don't end up with units linked to multiple
        // controllers.
        GT_MetaTileEntity_PurificationPlant oldController = getController();
        if (oldController != null) {
            oldController.unregisterLinkedUnit(this);
            this.unlinkController();
        }

        // Now link to new controller
        controllerX = x;
        controllerY = y;
        controllerZ = z;
        controllerSet = true;
        controller = (GT_MetaTileEntity_PurificationPlant) metaTileEntity;
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
        if (!(aPlayer instanceof EntityPlayerMP)) {
            return false;
        }

        // Right-clicking could be a data stick linking action, so try this first.
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }

        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    public GT_MetaTileEntity_PurificationPlant getController() {
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
        GT_MetaTileEntity_PurificationPlant controller = getController();
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
                "This Purification Unit is linked to the Water Purification Plant at " + controllerX
                    + ", "
                    + controllerY
                    + ", "
                    + controllerZ
                    + ".");

            // If recipe is running, display success chance
            if (this.mMaxProgresstime != 0) {
                ret.add(
                    "Success chance: " + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(this.calculateFinalSuccessChance())
                        + "%"
                        + EnumChatFormatting.RESET);
            }

        } else ret.add("This Purification Unit is not linked to any Water Purification Plant.");
        return ret.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.getBoolean("linked")) {
            currenttip.add(
                EnumChatFormatting.AQUA + "Linked to Purification Plant at "
                    + EnumChatFormatting.WHITE
                    + tag.getInteger("controllerX")
                    + ", "
                    + tag.getInteger("controllerY")
                    + ", "
                    + tag.getInteger("controllerZ")
                    + EnumChatFormatting.RESET);
        } else {
            currenttip.add(EnumChatFormatting.AQUA + "Unlinked");
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
     *
     * @return
     */
    public Widget makeSyncerWidgets() {
        return new MultiChildWidget()
            .addChild(new FakeSyncWidget.BooleanSyncer(() -> this.mMachine, machine -> this.mMachine = machine))
            .addChild(new FakeSyncWidget.BooleanSyncer(this::isAllowedToWork, _work -> {}));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
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
