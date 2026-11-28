package gregtech.common.ec.multiblock.internal;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IOnMachineStopComponent;
import gregtech.api.ec.component.IOnRecipeFinishComponent;
import gregtech.api.ec.component.IOnRecipeIdleComponent;
import gregtech.api.ec.component.IOnRecipeStartComponent;
import gregtech.api.ec.component.IOnRecipeTickComponent;
import gregtech.api.ec.component.IOnServerPostTickComponent;
import gregtech.api.ec.component.IOnServerTickFailComponent;
import gregtech.api.ec.component.IPostRunMachineComponent;
import gregtech.api.ec.component.IPreRunMachineComponent;
import gregtech.api.ec.component.IProgressIncrementComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IRecipePollingComponent;
import gregtech.api.ec.component.IRecipeProcessAwareComponent;
import gregtech.api.ec.component.ISavableComponent;
import gregtech.api.ec.component.IScannerInfoComponent;
import gregtech.api.ec.component.ISlotUpdateAwareComponent;
import gregtech.api.ec.component.IWailaDataComponent;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IOnStructureStateChangeComponent;
import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.config.MachineStats;
import gregtech.common.gui.modularui.adapter.CheckRecipeResultAdapter;
import gregtech.common.tileentities.machines.RecipeCheckReason;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderStack;

public abstract class BaseRecipeLogicComponent extends BaseMultiblockComponent implements IRecipeLogicComponent,
    ISavableComponent, IOnServerPostTickComponent, IOnStructureStateChangeComponent, IOnServerTickFailComponent,
    IOnMachineStopComponent, IScannerInfoComponent, IWailaDataComponent, IGuiSyncableComponent {

    protected boolean mRunningOnLoad = false;
    protected int mProgresstime = 0, mMaxProgresstime = 0, mRuntime = 0, lastParallel = 1;
    protected long mEUt = 0;
    /**
     * A pending IMMEDIATE recipe-check push (new inputs, drained output, user/structure change); never throttled.
     */
    protected boolean recipeCheckImmediately = false;
    /**
     * A pending THROTTLED recipe-check push (ME restock, power trickle); deferred while the fail cooldown is active.
     */
    protected boolean recipeCheckThrottled = false;
    /**
     * While {@code mTotalRunTime < this}, THROTTLED rechecks are deferred (the push flag is kept until it expires).
     * Set after a failed check to {@code now + MachineStats.machines.recipeCheckFailCooldown}; 0 means no cooldown.
     * IMMEDIATE pushes ignore this entirely.
     */
    protected long recipeCheckCooldownUntil = 0;

    protected @NotNull CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.NONE;
    protected long mLastWorkingTick = 0;
    protected long recipesDone = 0;

    public ItemStack[] mOutputItems = null;
    public FluidStack[] mOutputFluids = null;
    public List<ItemStack> mPendingItems = new ArrayList<>();
    public List<FluidStack> mPendingFluids = new ArrayList<>();

    @Override
    public void onStructureFormed(@NotNull IGregTechTileEntity igte) {
        scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
    }

    @Override
    public void onServerTickFail(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        checkRecipeResult = CheckRecipeResultRegistry.CRASH;
    }

    @Override
    public void onMachineStop(@NotNull ShutDownReason reason) {
        mLastWorkingTick = getTotalRunTime();
        mOutputItems = null;
        mOutputFluids = null;
        mEUt = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        lastParallel = 1;
    }

    @Override
    public void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder) {
        builder.addIf(mProgresstime > 0, Section.PROGRESS, data -> {
            data.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.multiblock.scanner.Progress",
                    formatNumber(mProgresstime / 20),
                    formatNumber(mMaxProgresstime / 20)));
        })
            .addIf(
                recipesDone > 0,
                Section.RECIPES_DONE,
                data -> {
                    data.add(
                        StatCollector
                            .translateToLocalFormatted("GT5U.multiblock.recipesDone.fmt", formatNumber(recipesDone)));
                });
    }

    @Override
    public void appendInfoMap(@NotNull Map<String, String> map) {
        map.put("progressTime", Integer.toString(mProgresstime));
        map.put("maxProgressTime", Integer.toString(mMaxProgresstime));
    }

    @Override
    public void appendWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x,
        int y, int z) {
        tag.setInteger("progress", mProgresstime);
        tag.setInteger("maxProgress", mMaxProgresstime);
        if (mOutputItems != null) {
            int index = 0;
            for (ItemStack stack : mOutputItems) {
                if (stack == null) continue;
                tag.setString("outputItemIcon" + index, TTRenderStack.create(stack, true));
                // Send the raw stack and localize on the client. getWailaNBTData runs server-side,
                // where the client language file is not loaded, so localizing here yields English on dedicated servers.
                NBTTagCompound outputItemStack = new NBTTagCompound();
                stack.writeToNBT(outputItemStack);
                tag.setTag("outputItemStack" + index, outputItemStack);
                tag.setInteger("outputItemCount" + index, stack.stackSize);
                index++;
            }
            if (index != 0) tag.setInteger("outputItemLength", index);
        }
        if (mOutputFluids != null) {
            int index = 0;
            for (FluidStack stack : mOutputFluids) {
                if (stack == null) continue;
                tag.setString(
                    "outputFluidIcon" + index,
                    TTRenderStack.create(GTUtility.getFluidDisplayStack(stack, false), true));
                // Store the internal fluid name and localize on the client. getWailaNBTData runs server-side,
                // where the client language file is not loaded, so localizing here yields English on dedicated servers.
                tag.setString(
                    "outputFluidName" + index,
                    stack.getFluid()
                        .getName());
                tag.setLong("outputFluidCount" + index, GTUtility.getFluidAmount(stack));
                index++;
            }
            if (index != 0) tag.setInteger("outputFluidLength", index);
        }
    }

    @Override
    public void registerGuiSyncValues(@NotNull PanelSyncManager syncManager) {
        syncManager
            .syncValue("lastWorkingTick", new LongSyncValue(() -> mLastWorkingTick, (val) -> mLastWorkingTick = val));
        syncManager.syncValue(
            "checkRecipeResult",
            GenericSyncValue.builder(CheckRecipeResult.class)
                .getter(() -> checkRecipeResult)
                .setter((val) -> checkRecipeResult = val)
                .adapter(new CheckRecipeResultAdapter())
                .build());
        syncManager.syncValue(
            "fluidOutput",
            new GenericListSyncHandler<>(
                () -> mOutputFluids != null ? Arrays.asList(mOutputFluids) : Collections.emptyList(),
                val -> mOutputFluids = val.toArray(new FluidStack[0]),
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack,
                (a, b) -> a.isFluidEqual(b) && GTUtility.getFluidAmount(a) == GTUtility.getFluidAmount(b),
                null));

        syncManager.syncValue("progressTime", new IntSyncValue(() -> mProgresstime, val -> mProgresstime = val));
        syncManager
            .syncValue("maxProgressTime", new IntSyncValue(() -> mMaxProgresstime, val -> mMaxProgresstime = val));

        GenericListSyncHandler<ItemStack> itemOutputSyncer = new GenericListSyncHandler<>(
            () -> mOutputItems != null ? Arrays.asList(mOutputItems) : Collections.emptyList(),
            val -> mOutputItems = val.toArray(new ItemStack[0]),
            NetworkUtils::readItemStack,
            NetworkUtils::writeItemStack,
            (a, b) -> a.isItemEqual(b) && a.stackSize == b.stackSize,
            null);
        syncManager.syncValue("itemOutput", itemOutputSyncer);

        syncManager.syncValue(
            "fluidPending",
            new GenericListSyncHandler<>(
                () -> mPendingFluids,
                val -> mPendingFluids = val,
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack,
                (a, b) -> a.isFluidEqual(b) && GTUtility.getFluidAmount(a) == GTUtility.getFluidAmount(b),
                null));

        syncManager.syncValue(
            "itemPending",
            new GenericListSyncHandler<>(
                () -> mPendingItems,
                val -> mPendingItems = val,
                NetworkUtils::readItemStack,
                NetworkUtils::writeItemStack,
                (a, b) -> a.isItemEqual(b) && a.stackSize == b.stackSize,
                null));
    }

    @Override
    public long getEUt() {
        return mEUt;
    }

    @Override
    public void setEUt(long eut) {
        mEUt = eut;
    }

    @Override
    public int getProgressTime() {
        return mProgresstime;
    }

    @Override
    public void setProgressTime(int progressTime) {
        mProgresstime = progressTime;
    }

    @Override
    public int getMaxProgressTime() {
        return mMaxProgresstime;
    }

    @Override
    public void setMaxProgressTime(int maxProgressTime) {
        mMaxProgresstime = maxProgressTime;
    }

    @Override
    public int getRuntime() {
        return mRuntime;
    }

    @Override
    public long getLastWorkingTick() {
        return mLastWorkingTick;
    }

    @Override
    public void scheduleRecipeCheck(RecipeCheckReason reason) {
        if (reason.throttled) {
            recipeCheckThrottled = true;
        } else {
            recipeCheckImmediately = true;
        }
    }

    @Override
    public @Nullable ItemStack[] getOutputItems() {
        return mOutputItems;
    }

    @Override
    public void setOutputItems(@Nullable ItemStack[] outputItems) {
        mOutputItems = outputItems;
    }

    @Override
    public @Nullable FluidStack[] getOutputFluids() {
        return mOutputFluids;
    }

    @Override
    public void setOutputFluids(@Nullable FluidStack[] outputFluids) {
        mOutputFluids = outputFluids;
    }

    @Override
    public boolean hasPendingOutputs() {
        return !mPendingItems.isEmpty() || !mPendingFluids.isEmpty();
    }

    @Override
    public @NotNull CheckRecipeResult getCheckRecipeResult() {
        return checkRecipeResult;
    }

    @Override
    public void setCheckRecipeResult(@NotNull CheckRecipeResult result) {
        this.checkRecipeResult = result;
    }

    @Override
    public long getRecipesDone() {
        return recipesDone;
    }

    protected void incrementProgressTime() {
        if (progressIncrement != null) {
            progressIncrement.incrementProgressTime(this);
        } else {
            mProgresstime++;
        }
    }

    protected void outputAfterRecipe() {

    }

    protected boolean protectsExcessItem() {
        if (voidingConfig == null) return false;
        return voidingConfig.protectsExcessItem();
    }

    protected boolean protectsExcessFluid() {
        if (voidingConfig == null) return false;
        return voidingConfig.protectsExcessFluid();
    }

    public boolean addPendingOutputs(@NotNull ItemStack[] outputItems) {
        var remaining = protectsExcessItem() ? mPendingItems : null;
        return addItemOutputs(Arrays.asList(outputItems), remaining);
    }

    protected boolean addItemOutputs(@NotNull List<ItemStack> outputItems, @Nullable List<ItemStack> remaining) {
        if (outputItems.isEmpty()) return true;
        if (itemOutputLogic == null) {
            if (remaining != null) {
                remaining.addAll(outputItems);
            }
            return false;
        }
        return itemOutputLogic.addOutputPartial(outputItems, protectsExcessItem(), remaining);
    }

    public boolean addPendingOutputs(@NotNull FluidStack[] outputFluids) {
        var remaining = protectsExcessFluid() ? mPendingFluids : null;
        return addFluidOutputs(Arrays.asList(outputFluids), remaining);
    }

    protected boolean addFluidOutputs(@NotNull List<FluidStack> outputFluids, @Nullable List<FluidStack> remaining) {
        if (outputFluids.isEmpty()) return true;
        if (fluidOutputLogic == null) {
            if (remaining != null) {
                remaining.addAll(outputFluids);
            }
            return false;
        }
        return fluidOutputLogic.addOutputPartial(outputFluids, protectsExcessFluid(), remaining);
    }

    protected boolean tryFlushPendingOutputs() {
        if (mPendingItems.isEmpty() && mPendingFluids.isEmpty()) return true;
        if (!recipeCheckImmediately && getTotalRunTime() % 20 != 0) return false;
        recipeCheckImmediately = false;
        if (!mPendingItems.isEmpty()) {
            List<ItemStack> pending = new ArrayList<>();
            addItemOutputs(mPendingItems, protectsExcessItem() ? pending : null);
            mPendingItems = pending;
        }
        if (!mPendingFluids.isEmpty()) {
            List<FluidStack> pending = new ArrayList<>();
            addFluidOutputs(mPendingFluids, protectsExcessFluid() ? pending : null);
            mPendingFluids = pending;
        }
        markDirty();
        if (mPendingItems.isEmpty() && mPendingFluids.isEmpty()) {
            scheduleRecipeCheck(RecipeCheckReason.IMMEDIATE);
            return true;
        }
        return false;
    }

    public void startRecipeProcessing() {
        for (IRecipeProcessAwareComponent component : recipeProcessAwares) {
            component.startRecipeProcessing();
        }
    }

    public void endRecipeProcessing() {
        CheckRecipeResult worstResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (IRecipeProcessAwareComponent component : recipeProcessAwares) {
            var result = component.endRecipeProcessing();
            if (!result.wasSuccessful()) {
                worstResult = result;
            }
        }
        setResultIfFailure(worstResult);
    }

    public void setResultIfFailure(CheckRecipeResult result) {
        if (!result.wasSuccessful()) {
            this.checkRecipeResult = result;
        }
    }

    public void updateSlots() {
        for (ISlotUpdateAwareComponent component : slotUpdateAwares) {
            component.updateSlots();
        }
        ItemStack controllerStack = getControllerSlot();
        if (controllerStack != null && controllerStack.stackSize <= 0) {
            getMachine().setControllerSlot(null);
        }
    }

    /**
     * Checks recipe and setup machine if it's successful.
     * <p>
     * For generic machine working with recipemap.
     */
    @NotNull
    public abstract CheckRecipeResult checkProcessing();

    /**
     * Starts checking recipe with some operations needed to actually run the check. Overriding this without due care
     * may result in dupe of items, hence it's marked as final.
     * <p>
     * See {@link #checkProcessing()} for what you want to override.
     *
     * @return If successfully found recipe and/or started processing
     */
    protected final boolean checkRecipe() {
        startRecipeProcessing();
        CheckRecipeResult result = checkProcessing();
        if (!CheckRecipeResultRegistry.isRegistered(result.getID())) {
            throw new RuntimeException(String.format("Result %s is not registered for registry", result.getID()));
        }
        this.checkRecipeResult = result;
        endRecipeProcessing();
        // Don't use `result` here because `endRecipeProcessing()` might mutate `this.checkRecipeResult`
        boolean success = this.checkRecipeResult.wasSuccessful();
        // Arm the fail cooldown (see recipeCheckFailCooldown) that defers THROTTLED rechecks (ME restock, power
        // trickle). IMMEDIATE pushes ignore it, so only machines with throttleable sources are affected, and the
        // default of 0 keeps everything instant.
        if (success) {
            recipeCheckCooldownUntil = 0;
            lastParallel = getCurrentParallels();
            for (var component : onRecipeStarts) {
                component.onRecipeStart(this);
            }
        } else {
            int cooldown = MachineStats.machines.recipeCheckFailCooldown;
            recipeCheckCooldownUntil = cooldown > 0 ? getTotalRunTime() + cooldown : 0;
        }
        return success;
    }

    protected int getCurrentParallels() {
        return 1;
    }

    protected boolean shouldCheckRecipeThisTick(long aTick) {
        // Recipe checks are purely event-driven: hatches and config changes push via scheduleRecipeCheck(reason)
        // instead of being polled on a periodic timer.
        if (recipeCheckImmediately) {
            // An immediate push (new inputs, drained output, user/structure change) always runs, and covers any
            // pending throttled push too.
            recipeCheckImmediately = false;
            recipeCheckThrottled = false;
            return true;
        }
        if (recipeCheckThrottled && getTotalRunTime() >= recipeCheckCooldownUntil) {
            // A throttleable push (ME restock, power trickle) runs once the post-failure cooldown has expired.
            recipeCheckThrottled = false;
            return true;
        }
        long timeElapsed = getTotalRunTime() - mLastWorkingTick;
        for (var component : recipePollings) {
            if (component.shouldPollRecipe(getTotalRunTime(), timeElapsed)) {
                return true;
            }
        }
        return false;
    }

    protected void runMachineInternal(@NotNull IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mMaxProgresstime > 0) {
            if (++mRuntime > 1000) mRuntime = 1;
            for (var component : onRecipeTicks) {
                if (!component.onRecipeTick(this)) return;
            }
            markDirty();
            incrementProgressTime();
            if (mMaxProgresstime > 0 && mProgresstime >= mMaxProgresstime) {
                for (var component : onRecipeFinishes) {
                    component.onRecipeFinish(this);
                }
                if (mOutputItems != null) addPendingOutputs(mOutputItems);
                if (mOutputFluids != null) addPendingOutputs(mOutputFluids);
                mOutputItems = null;
                mOutputFluids = null;
                outputAfterRecipe();
                mProgresstime = 0;
                mMaxProgresstime = 0;
                recipesDone += lastParallel;
                lastParallel = 1;
                mLastWorkingTick = getTotalRunTime();
                if (mPendingItems.isEmpty() && mPendingFluids.isEmpty() && aBaseMetaTileEntity.isAllowedToWork()) {
                    checkRecipe();
                }
            }
        } else {
            // Check if the machine is enabled in the first place!
            if (aBaseMetaTileEntity.isAllowedToWork()) {

                // shouldCheckRecipeThisTick() consumes pending pushes and applies the post-failure cooldown to
                // throttled ones; hasInventoryBeenModified() covers a change to the controller's own inventory slot.
                if (aBaseMetaTileEntity.hasWorkJustBeenEnabled() || shouldCheckRecipeThisTick(aTick)
                    || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                    if (checkRecipe()) {
                        markDirty();
                    }
                }
            }
            if (mMaxProgresstime <= 0) {
                for (var component : onRecipeIdles) {
                    component.onRecipeIdle(this);
                }
            }
        }
    }

    protected void runMachine(@NotNull IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!tryFlushPendingOutputs()) return;
        for (var component : preRunMachines) {
            if (!component.onPreRunMachine(this, aBaseMetaTileEntity, aTick)) {
                return;
            }
        }
        runMachineInternal(aBaseMetaTileEntity, aTick);
        for (var component : postRunMachines) {
            component.onPostRunMachine(this, aBaseMetaTileEntity, aTick);
        }
    }

    @Override
    public void onServerPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (isMachineFormed() && (maintenanceLogic == null || maintenanceLogic.getRepairStatus() > 0)) {
            runMachine(aBaseMetaTileEntity, aTick);
        }
        aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
    }

    @Override
    public int getServerTickPriority() {
        return ServerTickPriority.RECIPE.priority;
    }

    @Override
    public @NotNull String getSaveKey() {
        return "recipe";
    }

    @Override
    public @Nullable NBTTagCompound saveComponentData() {
        var aNBT = new NBTTagCompound();
        aNBT.setLong("mEUt", mEUt);
        aNBT.setInteger("mRuntime", mRuntime);
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        aNBT.setLong("mLastWorkingTick", mLastWorkingTick);
        aNBT.setLong("recipesDone", recipesDone);
        aNBT.setString("checkRecipeResultID", checkRecipeResult.getID());
        aNBT.setTag("checkRecipeResult", checkRecipeResult.writeToNBT(new NBTTagCompound()));
        if (mOutputItems != null) {
            aNBT.setInteger("mOutputItemsLength", mOutputItems.length);
            for (int i = 0; i < mOutputItems.length; i++) if (mOutputItems[i] != null) {
                GTUtility.saveItem(aNBT, "mOutputItem" + i, mOutputItems[i]);
            }
        }
        if (mOutputFluids != null) {
            aNBT.setInteger("mOutputFluidsLength", mOutputFluids.length);
            for (int i = 0; i < mOutputFluids.length; i++) if (mOutputFluids[i] != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                mOutputFluids[i].writeToNBT(tNBT);
                aNBT.setTag("mOutputFluids" + i, tNBT);
            }
        }
        if (mPendingItems != null) {
            aNBT.setInteger("mPendingItemsLength", mPendingItems.size());
            for (int i = 0; i < mPendingItems.size(); i++) if (mPendingItems.get(i) != null) {
                GTUtility.saveItem(aNBT, "mPendingItem" + i, mPendingItems.get(i));
            }
        }
        if (mPendingFluids != null) {
            aNBT.setInteger("mPendingFluidsLength", mPendingFluids.size());
            for (int i = 0; i < mPendingFluids.size(); i++) if (mPendingFluids.get(i) != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                mPendingFluids.get(i)
                    .writeToNBT(tNBT);
                aNBT.setTag("mPendingFluids" + i, tNBT);
            }
        }
        aNBT.setInteger("lastParallel", lastParallel);
        return aNBT;
    }

    @Override
    public boolean wasRunningOnLoad() {
        return mRunningOnLoad;
    }

    @Override
    public void clearRunningOnLoad() {
        mRunningOnLoad = false;
    }

    @Override
    public void loadComponentData(@NotNull NBTTagCompound nbt) {
        loadLegacyNBTData(nbt);
    }

    @Override
    public void loadLegacyNBTData(@NotNull NBTTagCompound aNBT) {
        mEUt = aNBT.getLong("mEUt");
        mRuntime = aNBT.getInteger("mRuntime");
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        if (mMaxProgresstime > 0) mRunningOnLoad = true;
        mLastWorkingTick = aNBT.getLong("mLastWorkingTick");
        recipesDone = aNBT.getLong("recipesDone");
        String checkRecipeResultID = aNBT.getString("checkRecipeResultID");
        if (CheckRecipeResultRegistry.isRegistered(checkRecipeResultID)) {
            CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(checkRecipeResultID)
                .newInstance();
            result.readFromNBT(aNBT.getCompoundTag("checkRecipeResult"));
            checkRecipeResult = result;
        }
        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++) mOutputItems[i] = GTUtility.loadItem(aNBT, "mOutputItem" + i);
        }

        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++)
                mOutputFluids[i] = GTUtility.loadFluid(aNBT, "mOutputFluids" + i);
        }

        int aPendingItemsLength = aNBT.getInteger("mPendingItemsLength");
        if (aPendingItemsLength > 0) {
            mPendingItems.clear();
            for (int i = 0; i < aPendingItemsLength; i++)
                mPendingItems.add(GTUtility.loadItem(aNBT, "mPendingItem" + i));
        }

        int aPendingFluidsLength = aNBT.getInteger("mPendingFluidsLength");
        if (aPendingFluidsLength > 0) {
            mPendingFluids.clear();
            for (int i = 0; i < aPendingFluidsLength; i++)
                mPendingFluids.add(GTUtility.loadFluid(aNBT, "mPendingFluids" + i));
        }
        lastParallel = Math.max(1, aNBT.getInteger("lastParallel"));
    }

    private @Nullable IVoidingConfigComponent voidingConfig;
    private @Nullable IMultiblockItemOutputLogicComponent itemOutputLogic;
    private @Nullable IMultiblockFluidOutputLogicComponent fluidOutputLogic;
    private @Nullable IMaintenanceLogicComponent maintenanceLogic;
    private @Nullable IProgressIncrementComponent progressIncrement;
    private IRecipeProcessAwareComponent[] recipeProcessAwares;
    private ISlotUpdateAwareComponent[] slotUpdateAwares;
    private IOnRecipeStartComponent[] onRecipeStarts;
    private IOnRecipeTickComponent[] onRecipeTicks;
    private IOnRecipeFinishComponent[] onRecipeFinishes;
    private IOnRecipeIdleComponent[] onRecipeIdles;
    private IRecipePollingComponent[] recipePollings;
    private IPreRunMachineComponent[] preRunMachines;
    private IPostRunMachineComponent[] postRunMachines;

    @Override
    public void onComponentsReady() {
        voidingConfig = tryGetComponent(IVoidingConfigComponent.KEY);
        itemOutputLogic = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        fluidOutputLogic = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        maintenanceLogic = tryGetComponent(IMaintenanceLogicComponent.KEY);
        progressIncrement = tryGetComponent(IProgressIncrementComponent.KEY);
        recipeProcessAwares = getComponents(IRecipeProcessAwareComponent.KEY);
        slotUpdateAwares = getComponents(ISlotUpdateAwareComponent.KEY);
        onRecipeStarts = getComponents(IOnRecipeStartComponent.KEY);
        onRecipeTicks = getComponents(IOnRecipeTickComponent.KEY);
        onRecipeFinishes = getComponents(IOnRecipeFinishComponent.KEY);
        onRecipeIdles = getComponents(IOnRecipeIdleComponent.KEY);
        recipePollings = getComponents(IRecipePollingComponent.KEY);
        preRunMachines = getComponents(IPreRunMachineComponent.KEY);
        postRunMachines = getComponents(IPostRunMachineComponent.KEY);
    }
}
