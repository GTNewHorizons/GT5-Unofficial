package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.TestOnly;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.ControllerWithOptionalFeatures;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.modularui.IBindPlayerInventoryUI;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_ClientPreference;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Waila;
import gregtech.api.util.OutputHatchWrapper;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.client.GT_SoundLoop;
import gregtech.common.GT_Pollution;
import gregtech.common.gui.modularui.widget.CheckRecipeResultSyncer;
import gregtech.common.gui.modularui.widget.ShutDownReasonSyncer;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Input_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class GT_MetaTileEntity_MultiBlockBase extends MetaTileEntity
    implements ControllerWithOptionalFeatures, IAddGregtechLogo, IAddUIWidgets, IBindPlayerInventoryUI {

    public static boolean disableMaintenance;
    public boolean hasMaintenanceChecks = getDefaultHasMaintenanceChecks();
    public boolean mMachine = false, mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false,
        mSolderingTool = false, mCrowbar = false, mRunningOnLoad = false;
    public boolean mStructureChanged = false;
    public int mPollution = 0, mProgresstime = 0, mMaxProgresstime = 0, mEUt = 0, mEfficiencyIncrease = 0,
        mStartUpCheck = 100, mRuntime = 0, mEfficiency = 0;
    public volatile boolean mUpdated = false;
    public int mUpdate = 0;
    public ItemStack[] mOutputItems = null;
    public FluidStack[] mOutputFluids = null;
    public String mNEI;
    public int damageFactorLow = 5;
    public float damageFactorHigh = 0.6f;

    public boolean mLockedToSingleRecipe = getDefaultRecipeLockingMode();
    protected boolean inputSeparation = getDefaultInputSeparationMode();
    protected VoidingMode voidingMode = getDefaultVoidingMode();
    protected boolean batchMode = getDefaultBatchMode();
    private @Nonnull CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.NONE;

    protected static final String INPUT_SEPARATION_NBT_KEY = "inputSeparation";
    protected static final String VOID_EXCESS_NBT_KEY = "voidExcess";
    protected static final String VOIDING_MODE_NBT_KEY = "voidingMode";
    protected static final String BATCH_MODE_NBT_KEY = "batchMode";
    protected SingleRecipeCheck mSingleRecipeCheck = null;

    public ArrayList<GT_MetaTileEntity_Hatch_Input> mInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Output> mOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_InputBus> mInputBusses = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputBus> mOutputBusses = new ArrayList<>();
    public ArrayList<IDualInputHatch> mDualInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Dynamo> mDynamoHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Muffler> mMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Energy> mEnergyHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Maintenance> mMaintenanceHatches = new ArrayList<>();
    protected List<GT_MetaTileEntity_Hatch> mExoticEnergyHatches = new ArrayList<>();
    protected final ProcessingLogic processingLogic;
    @SideOnly(Side.CLIENT)
    protected GT_SoundLoop activitySoundLoop;

    private long mLastWorkingTick = 0;

    protected static final byte INTERRUPT_SOUND_INDEX = 8;
    protected static final byte PROCESS_START_SOUND_INDEX = 1;

    public GT_MetaTileEntity_MultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 2);
        this.processingLogic = null;
        GT_MetaTileEntity_MultiBlockBase.disableMaintenance = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
        this.damageFactorLow = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorLow", 5);
        this.damageFactorHigh = (float) GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorHigh", 0.6f);
        this.mNEI = "";
        if (!shouldCheckMaintenance()) fixAllIssues();
    }

    public GT_MetaTileEntity_MultiBlockBase(String aName) {
        super(aName, 2);
        this.processingLogic = createProcessingLogic();
        GT_MetaTileEntity_MultiBlockBase.disableMaintenance = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.disableMaintenance", false);
        this.damageFactorLow = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorLow", 5);
        this.damageFactorHigh = (float) GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "MultiBlockMachines.damageFactorHigh", 0.6f);
        if (!shouldCheckMaintenance()) fixAllIssues();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (supportsSingleRecipeLocking()) {
            mLockedToSingleRecipe = !mLockedToSingleRecipe;
            if (mLockedToSingleRecipe) {
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    GT_Utility.trans("223", "Single recipe locking enabled. Will lock to next recipe."));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("220", "Single recipe locking disabled."));
                mSingleRecipeCheck = null;
            }
        }
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex > 0;
    }

    @Override
    public int getProgresstime() {
        return mProgresstime;
    }

    @Override
    public int maxProgresstime() {
        return mMaxProgresstime;
    }

    @Override
    public int increaseProgress(int aProgress) {
        return aProgress;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mEUt", mEUt);
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        aNBT.setInteger("mEfficiencyIncrease", mEfficiencyIncrease);
        aNBT.setInteger("mEfficiency", mEfficiency);
        aNBT.setInteger("mPollution", mPollution);
        aNBT.setInteger("mRuntime", mRuntime);
        if (supportsSingleRecipeLocking()) {
            aNBT.setBoolean("mLockedToSingleRecipe", mLockedToSingleRecipe);
            if (mLockedToSingleRecipe && mSingleRecipeCheck != null)
                aNBT.setTag("mSingleRecipeCheck", mSingleRecipeCheck.writeToNBT());
        }

        if (mOutputItems != null) {
            aNBT.setInteger("mOutputItemsLength", mOutputItems.length);
            for (int i = 0; i < mOutputItems.length; i++) if (mOutputItems[i] != null) {
                GT_Utility.saveItem(aNBT, "mOutputItem" + i, mOutputItems[i]);
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
        aNBT.setBoolean("mWrench", mWrench);
        aNBT.setBoolean("mScrewdriver", mScrewdriver);
        aNBT.setBoolean("mSoftHammer", mSoftHammer);
        aNBT.setBoolean("mHardHammer", mHardHammer);
        aNBT.setBoolean("mSolderingTool", mSolderingTool);
        aNBT.setBoolean("mCrowbar", mCrowbar);
        aNBT.setBoolean(BATCH_MODE_NBT_KEY, batchMode);
        aNBT.setBoolean(INPUT_SEPARATION_NBT_KEY, inputSeparation);
        aNBT.setString(VOIDING_MODE_NBT_KEY, voidingMode.name);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mEUt = aNBT.getInteger("mEUt");
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        if (mMaxProgresstime > 0) mRunningOnLoad = true;
        mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
        mEfficiency = aNBT.getInteger("mEfficiency");
        mPollution = aNBT.getInteger("mPollution");
        mRuntime = aNBT.getInteger("mRuntime");
        if (supportsSingleRecipeLocking()) {
            mLockedToSingleRecipe = aNBT.getBoolean("mLockedToSingleRecipe");
            if (mLockedToSingleRecipe && aNBT.hasKey("mSingleRecipeCheck", Constants.NBT.TAG_COMPOUND)) {
                SingleRecipeCheck c = loadSingleRecipeChecker(aNBT.getCompoundTag("mSingleRecipeCheck"));
                if (c != null) mSingleRecipeCheck = c;
                // the old recipe is gone. we disable the machine to prevent making garbage in case of shared inputs
                // maybe use a better way to inform player in the future.
                else getBaseMetaTileEntity().disableWorking();
            }
        }
        batchMode = aNBT.getBoolean(BATCH_MODE_NBT_KEY);
        inputSeparation = aNBT.getBoolean(INPUT_SEPARATION_NBT_KEY);
        if (aNBT.hasKey(VOIDING_MODE_NBT_KEY, Constants.NBT.TAG_STRING)) {
            voidingMode = VoidingMode.fromName(aNBT.getString(VOIDING_MODE_NBT_KEY));
        } else if (aNBT.hasKey(VOID_EXCESS_NBT_KEY)) {
            // backward compatibility
            voidingMode = aNBT.getBoolean(VOID_EXCESS_NBT_KEY) ? VoidingMode.VOID_ALL : VoidingMode.VOID_NONE;
        }
        if (!getAllowedVoidingModes().contains(voidingMode)) voidingMode = getDefaultVoidingMode();

        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++)
                mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        }

        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++)
                mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
        }
        if (shouldCheckMaintenance()) {
            mWrench = aNBT.getBoolean("mWrench");
            mScrewdriver = aNBT.getBoolean("mScrewdriver");
            mSoftHammer = aNBT.getBoolean("mSoftHammer");
            mHardHammer = aNBT.getBoolean("mHardHammer");
            mSolderingTool = aNBT.getBoolean("mSolderingTool");
            mCrowbar = aNBT.getBoolean("mCrowbar");
        } else fixAllIssues();
    }

    protected SingleRecipeCheck loadSingleRecipeChecker(NBTTagCompound aNBT) {
        return SingleRecipeCheck.tryLoad(getRecipeMap(), aNBT);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 2;
    }

    /**
     * Set the structure as having changed, and trigger an update.
     */
    public void onStructureChange() {
        mStructureChanged = true;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdated = true;
    }

    /**
     * ClearHatches as a part of structure check. If your multiblock has any hatches that need clearing override this
     * method, call super, and clear your own hatches
     */
    public void clearHatches() {
        mInputHatches.clear();
        mInputBusses.clear();
        mOutputHatches.clear();
        mOutputBusses.clear();
        mDynamoHatches.clear();
        mEnergyHatches.clear();
        setMufflers(false);
        mMufflerHatches.clear();
        mMaintenanceHatches.clear();
        mDualInputHatches.clear();
    }

    public boolean checkStructure(boolean aForceReset) {
        return checkStructure(aForceReset, getBaseMetaTileEntity());
    }

    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        if (!aBaseMetaTileEntity.isServerSide()) return mMachine;
        // Only trigger an update if forced (from onPostTick, generally), or if the structure has changed
        if ((mStructureChanged || aForceReset)) {
            clearHatches();
            mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
        }
        mStructureChanged = false;
        return mMachine;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (mUpdated) {
                // duct tape fix for too many updates on an overloaded server, causing the structure check to not run
                if (mUpdate <= 0) mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0 || --mStartUpCheck == 0) {
                checkStructure(true, aBaseMetaTileEntity);
            }

            if (mStartUpCheck < 0) {
                if (mMachine) {
                    checkMaintenance();
                    if (getRepairStatus() > 0) {
                        runMachine(aBaseMetaTileEntity, aTick);
                    } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                        stopMachine(ShutDownReasonRegistry.NO_REPAIR);
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
                }
            }
            aBaseMetaTileEntity.setErrorDisplayID(
                (aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1)
                    | (mScrewdriver ? 0 : 2)
                    | (mSoftHammer ? 0 : 4)
                    | (mHardHammer ? 0 : 8)
                    | (mSolderingTool ? 0 : 16)
                    | (mCrowbar ? 0 : 32)
                    | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active = aBaseMetaTileEntity.isActive() && mPollution > 0;
            setMufflers(active);
        } else {
            if (!aBaseMetaTileEntity.hasMufflerUpgrade()) {
                doActivitySound(getActivitySoundLoop());
            }
        }
    }

    @Override
    public void onTickFail(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onTickFail(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.disableWorking();
            checkRecipeResult = CheckRecipeResultRegistry.CRASH;
        }
    }

    public void checkMaintenance() {
        if (!shouldCheckMaintenance()) return;

        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : filterValidMTEs(mMaintenanceHatches)) {
            if (tHatch.mAuto && !(mWrench && mScrewdriver && mSoftHammer && mHardHammer && mSolderingTool && mCrowbar))
                tHatch.autoMaintainance();
            if (tHatch.mWrench) mWrench = true;
            if (tHatch.mScrewdriver) mScrewdriver = true;
            if (tHatch.mSoftHammer) mSoftHammer = true;
            if (tHatch.mHardHammer) mHardHammer = true;
            if (tHatch.mSolderingTool) mSolderingTool = true;
            if (tHatch.mCrowbar) mCrowbar = true;

            tHatch.mWrench = false;
            tHatch.mScrewdriver = false;
            tHatch.mSoftHammer = false;
            tHatch.mHardHammer = false;
            tHatch.mSolderingTool = false;
            tHatch.mCrowbar = false;
        }
    }

    /**
     * Starts checking recipe with some operations needed to actually run the check. Overriding this without due care
     * may result in dupe of items, hence it's marked as final.
     * <p>
     * See {@link #createProcessingLogic()} or {@link #checkProcessing()} for what you want to override.
     *
     * @return If successfully found recipe and/or started processing
     */
    protected final boolean checkRecipe() {
        startRecipeProcessing();
        CheckRecipeResult result = checkProcessing();
        if (!CheckRecipeResultRegistry.isRegistered(result.getID())) {
            throw new RuntimeException(String.format("Result %s is not registered for registry", result.getID()));
        }
        if (result.wasSuccessful()) {
            sendStartMultiBlockSoundLoop();
        }
        this.checkRecipeResult = result;
        endRecipeProcessing();
        // Don't use `result` here because `endRecipeProcessing()` might mutate `this.checkRecipeResult`
        return this.checkRecipeResult.wasSuccessful();
    }

    private boolean shouldCheckRecipeThisTick(long aTick) {
        // do a recipe check if any crafting input hatch just got pushed in items
        boolean shouldCheck = false;
        // check all of them (i.e. do not return early) to reset the state of all of them.
        for (IDualInputHatch craftingInputMe : mDualInputHatches) {
            shouldCheck |= craftingInputMe.justUpdated();
        }
        if (shouldCheck) return true;

        // Perform more frequent recipe change after the machine just shuts down.
        long timeElapsed = aTick - mLastWorkingTick;

        if (timeElapsed >= 100) return aTick % 100 == 0;
        // Batch mode should be a lot less aggressive at recipe checking
        if (!isBatchModeEnabled()) {
            return timeElapsed == 5 || timeElapsed == 12
                || timeElapsed == 20
                || timeElapsed == 30
                || timeElapsed == 40
                || timeElapsed == 55
                || timeElapsed == 70
                || timeElapsed == 85;
        }
        return false;
    }

    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
            if (onRunningTick(mInventory[1])) {
                markDirty();
                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                    stopMachine(ShutDownReasonRegistry.POLLUTION_FAIL);
                }
                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                    if (mOutputItems != null) {
                        for (ItemStack tStack : mOutputItems) {
                            if (tStack != null) {
                                try {
                                    GT_Mod.achievements.issueAchivementHatch(
                                        aBaseMetaTileEntity.getWorld()
                                            .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                        tStack);
                                } catch (Exception ignored) {}
                                addOutput(tStack);
                            }
                        }
                        mOutputItems = null;
                    }
                    if (mOutputFluids != null) {
                        addFluidOutputs(mOutputFluids);
                        if (mOutputFluids.length > 1) {
                            try {
                                GT_Mod.achievements.issueAchievement(
                                    aBaseMetaTileEntity.getWorld()
                                        .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                    "oilplant");
                            } catch (Exception ignored) {}
                        }
                        mOutputFluids = null;
                    }
                    mEfficiency = Math.max(
                        0,
                        Math.min(
                            mEfficiency + mEfficiencyIncrease,
                            getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                    mOutputItems = null;
                    mProgresstime = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                    mLastWorkingTick = aTick;
                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                        checkRecipe();
                    }
                }
            }
        } else {
            if (shouldCheckRecipeThisTick(aTick) || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                || aBaseMetaTileEntity.hasInventoryBeenModified()) {

                if (aBaseMetaTileEntity.isAllowedToWork()) {
                    if (checkRecipe()) {
                        markDirty();
                    }
                }
                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
            }
        }
    }

    public boolean polluteEnvironment(int aPollutionLevel) {
        mPollution += aPollutionLevel;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            if (mPollution >= 10000) {
                if (tHatch.polluteEnvironment(this)) {
                    mPollution -= 10000;
                }
            } else {
                break;
            }
        }
        return mPollution < 10000;
    }

    protected void sendStartMultiBlockSoundLoop() {
        if (getProcessStartSound() != null) {
            sendLoopStart(PROCESS_START_SOUND_INDEX);
        }
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        switch (aIndex) {
            case PROCESS_START_SOUND_INDEX -> {
                if (getProcessStartSound() != null)
                    GT_Utility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
            }
            case INTERRUPT_SOUND_INDEX -> GT_Utility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            if (getProcessStartSound() != null)
                GT_Utility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void doActivitySound(ResourceLocation activitySound) {
        if (getBaseMetaTileEntity().isActive() && activitySound != null) {
            if (activitySoundLoop == null) {
                activitySoundLoop = new GT_SoundLoop(activitySound, getBaseMetaTileEntity(), false, true);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop = null;
            }
        }
    }

    /**
     * @return Time before the start process sound is played again
     */
    protected int getTimeBetweenProcessSounds() {
        return 100;
    }

    /**
     * @return Sound that will be played once, when the recipe check was valid
     */
    protected SoundResource getProcessStartSound() {
        return null;
    }

    /**
     * @return Sound that will be looped for as long as the machine is doing a recipe
     */
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySoundLoop() {
        return null;
    }

    /**
     * Called every tick the Machine runs
     */
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUt > 0) {
            addEnergyOutput(((long) mEUt * mEfficiency) / 10000);
            return true;
        }
        if (mEUt < 0) {
            if (!drainEnergyInput(getActualEnergyUsage())) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    protected long getActualEnergyUsage() {
        return ((long) -mEUt * 10_000) / Math.max(1000, mEfficiency);
    }

    /**
     * Checks if this is a Correct Machine Part for this kind of Machine (Turbine Rotor for example)
     */
    public abstract boolean isCorrectMachinePart(ItemStack aStack);

    /**
     * @deprecated Use {@link #createProcessingLogic()} or {@link #checkProcessing()}
     */
    @Deprecated
    public boolean checkRecipe(ItemStack aStack) {
        return false;
    }

    /**
     * Checks recipe and setup machine if it's successful.
     * <p>
     * For generic machine working with recipemap, use {@link #createProcessingLogic()} to make use of shared codebase.
     */
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    /**
     * @return If controller slot should be considered as inputs for {@link #doCheckRecipe}
     */
    protected boolean canUseControllerSlotForRecipe() {
        return true;
    }

    /**
     * Initializes processing logic for use. Unlike {@link #createProcessingLogic}, this method is called
     * every time checking for recipes.
     */
    protected void setupProcessingLogic(ProcessingLogic logic) {
        logic.clear();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
        logic.setBatchSize(isBatchModeEnabled() ? getMaxBatchSize() : 1);
        logic.setRecipeLocking(this, isRecipeLockingEnabled());
        setProcessingLogicPower(logic);
    }

    /**
     * Initializes processing logic for use, specifically for power-related parameters.
     * Unlike {@link #createProcessingLogic}, this method is called every time checking for recipes.
     */
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        logic.setAmperageOC(mEnergyHatches.size() != 1);
    }

    protected boolean supportsCraftingMEBuffer() {
        return true;
    }

    /**
     * Iterates over hatches and tries to find recipe. Assume {@link #processingLogic} is already set up for use.
     * If return value is successful, inputs are consumed.
     */
    @Nonnull
    protected CheckRecipeResult doCheckRecipe() {
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;
        // check crafting input hatches first
        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                for (var it = dualInputHatch.inventories(); it.hasNext();) {
                    IDualInputInventory slot = it.next();
                    processingLogic.setInputItems(slot.getItemInputs());
                    processingLogic.setInputFluids(slot.getFluidInputs());
                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        }

        processingLogic.setInputFluids(getStoredFluids());

        if (isInputSeparationEnabled()) {
            if (mInputBusses.isEmpty()) {
                CheckRecipeResult foundResult = processingLogic.process();
                if (foundResult.wasSuccessful()) {
                    return foundResult;
                }
                if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                    // Recipe failed in interesting way, so remember that and continue searching
                    result = foundResult;
                }
            } else {
                for (GT_MetaTileEntity_Hatch_InputBus bus : mInputBusses) {
                    if (bus instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME) {
                        continue;
                    }
                    List<ItemStack> inputItems = new ArrayList<>();
                    for (int i = bus.getSizeInventory() - 1; i >= 0; i--) {
                        ItemStack stored = bus.getStackInSlot(i);
                        if (stored != null) {
                            inputItems.add(stored);
                        }
                    }
                    if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                        inputItems.add(getControllerSlot());
                    }
                    processingLogic.setInputItems(inputItems.toArray(new ItemStack[0]));
                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        } else {
            List<ItemStack> inputItems = getStoredInputs();
            if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                inputItems.add(getControllerSlot());
            }
            processingLogic.setInputItems(inputItems);
            CheckRecipeResult foundResult = processingLogic.process();
            if (foundResult.wasSuccessful()) {
                return foundResult;
            }
            if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                // Recipe failed in interesting way, so remember that
                result = foundResult;
            }
        }
        return result;
    }

    /**
     * Performs additional check for {@link #processingLogic} after all the calculations are done.
     * As many as checks should be done inside of custom {@link ProcessingLogic}, which you can specify with
     * {@link #createProcessingLogic()}, because when this method is called, inputs might have been already consumed.
     * However, certain checks cannot be done like that; Checking energy overflow should be suppressed for
     * long-power machines for example.
     *
     * @return Modified (or not modified) result
     */
    @Nonnull
    protected CheckRecipeResult postCheckRecipe(@Nonnull CheckRecipeResult result,
        @Nonnull ProcessingLogic processingLogic) {
        if (result.wasSuccessful() && processingLogic.getCalculatedEut() > Integer.MAX_VALUE) {
            return CheckRecipeResultRegistry.POWER_OVERFLOW;
        }
        return result;
    }

    /**
     * Called after {@link #doCheckRecipe} and {@link #postCheckRecipe} being successful.
     * Override to set energy usage for this machine.
     */
    protected void setEnergyUsage(ProcessingLogic processingLogic) {
        // getCalculatedEut() is guaranteed to not exceed int by postCheckRecipe()
        mEUt = (int) processingLogic.getCalculatedEut();
        if (mEUt > 0) {
            mEUt = (-mEUt);
        }
    }

    protected int getMaxBatchSize() {
        return 128;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     */
    public abstract boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack);

    /**
     * Gets the maximum Efficiency that spare Part can get (0 - 10000)
     */
    public abstract int getMaxEfficiency(ItemStack aStack);

    /**
     * Gets the pollution this Device outputs to a Muffler per tick (10000 = one Pullution Block)
     */
    public int getPollutionPerTick(ItemStack aStack) {
        return getPollutionPerSecond(aStack) / 20;
    }

    /**
     * Gets the pollution produced per second by this multiblock, default to 0. Override this with its actual value in
     * the code of the multiblock.
     */
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    /**
     * Gets the damage to the ItemStack, usually 0 or 1.
     */
    public abstract int getDamageToComponent(ItemStack aStack);

    /**
     * If it explodes when the Component has to be replaced.
     */
    public abstract boolean explodesOnComponentBreak(ItemStack aStack);

    /**
     * @deprecated Use {@link #stopMachine(ShutDownReason)}
     */
    @Deprecated
    public void stopMachine() {
        stopMachine(ShutDownReasonRegistry.NONE);
    }

    /**
     * @deprecated Use {@link #stopMachine(ShutDownReason)}
     */
    @Deprecated
    public void criticalStopMachine() {
        stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
    }

    public void stopMachine(@Nonnull ShutDownReason reason) {
        if (!ShutDownReasonRegistry.isRegistered(reason.getID())) {
            throw new RuntimeException(String.format("Reason %s is not registered for registry", reason.getID()));
        }
        mOutputItems = null;
        mOutputFluids = null;
        mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        getBaseMetaTileEntity().disableWorking();
        getBaseMetaTileEntity().setShutDownReason(reason);
        getBaseMetaTileEntity().setShutdownStatus(true);
        if (reason.wasCritical()) {
            sendSound(INTERRUPT_SOUND_INDEX);
        }
    }

    public int getRepairStatus() {
        return (mWrench ? 1 : 0) + (mScrewdriver ? 1 : 0)
            + (mSoftHammer ? 1 : 0)
            + (mHardHammer ? 1 : 0)
            + (mSolderingTool ? 1 : 0)
            + (mCrowbar ? 1 : 0);
    }

    public int getIdealStatus() {
        return 6;
    }

    public int getCurrentEfficiency(ItemStack itemStack) {
        int maxEff = getMaxEfficiency(itemStack);
        return maxEff - (getIdealStatus() - getRepairStatus()) * maxEff / 10;
    }

    public boolean doRandomMaintenanceDamage() {
        if (!isCorrectMachinePart(mInventory[1])) {
            stopMachine(ShutDownReasonRegistry.NO_MACHINE_PART);
            return false;
        }
        if (shouldCheckMaintenance() && getRepairStatus() == 0) {
            stopMachine(ShutDownReasonRegistry.NO_REPAIR);
            return false;
        }
        if (mRuntime++ > 1000) {
            mRuntime = 0;
            if (shouldCheckMaintenance() && getBaseMetaTileEntity().getRandomNumber(6000) == 0) {
                causeMaintenanceIssue();
            }
            if (mInventory[1] != null && getBaseMetaTileEntity().getRandomNumber(2) == 0
                && !mInventory[1].getUnlocalizedName()
                    .startsWith("gt.blockmachines.basicmachine.")) {
                if (mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
                    NBTTagCompound tNBT = mInventory[1].getTagCompound();
                    ((GT_MetaGenerated_Tool) mInventory[1].getItem()).doDamage(
                        mInventory[1],
                        (long) getDamageToComponent(mInventory[1])
                            * (long) Math.min(mEUt / this.damageFactorLow, Math.pow(mEUt, this.damageFactorHigh)));
                    if (mInventory[1].stackSize == 0) mInventory[1] = null;
                }
            }
        }
        return true;
    }

    public void causeMaintenanceIssue() {
        switch (getBaseMetaTileEntity().getRandomNumber(6)) {
            case 0 -> mWrench = false;
            case 1 -> mScrewdriver = false;
            case 2 -> mSoftHammer = false;
            case 3 -> mHardHammer = false;
            case 4 -> mSolderingTool = false;
            case 5 -> mCrowbar = false;
        }
    }

    public void explodeMultiblock() {

        GT_Log.exp.println(
            "MultiBlockExplosion at: " + this.getBaseMetaTileEntity()
                .getXCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getYCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getZCoord()
                + " DIMID: "
                + this.getBaseMetaTileEntity()
                    .getWorld().provider.dimensionId
                + ".");

        GT_Pollution.addPollution(getBaseMetaTileEntity(), GT_Mod.gregtechproxy.mPollutionOnExplosion);
        mInventory[1] = null;
        // noinspection unchecked // In this case, the inspection only indicates that the array can be abused in runtime
        Iterable<MetaTileEntity> allHatches = Iterables.concat(
            mInputBusses,
            mOutputBusses,
            mInputHatches,
            mOutputHatches,
            mDynamoHatches,
            mMufflerHatches,
            mEnergyHatches,
            mMaintenanceHatches);
        for (MetaTileEntity tTileEntity : allHatches) {
            if (tTileEntity != null && tTileEntity.getBaseMetaTileEntity() != null) {
                tTileEntity.getBaseMetaTileEntity()
                    .doExplosion(V[8]);
            }
        }
        getBaseMetaTileEntity().doExplosion(V[8]);
    }

    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0) {
            return true;
        }
        if (mDynamoHatches.size() > 0) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0;
        long aFirstVoltageFound = -1;
        boolean aFoundMixedDynamos = false;
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : filterValidMTEs(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            long aTotal = aDynamo.maxAmperesOut() * aVoltage;
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            } else {
                if (aFirstVoltageFound != aVoltage) {
                    aFoundMixedDynamos = true;
                }
            }
            totalOutput += aTotal;
        }

        if (totalOutput < aEU || (aFoundMixedDynamos && !aAllowMixedVoltageDynamos)) {
            explodeMultiblock();
            return false;
        }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;
        int ampsOnCurrentHatch;
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : filterValidMTEs(mDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);
            for (int i = 0; i < ampsOnCurrentHatch; i++) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aVoltage, false);
            }
            injected += aVoltage * ampsOnCurrentHatch;
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        return injected > 0;
    }

    /**
     * Sums up voltage of energy hatches. Amperage does not matter.
     */
    public long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches))
            rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        return rVoltage;
    }

    public long getAverageInputVoltage() {
        return GT_ExoticEnergyInputHelper.getAverageInputVoltageMulti(mEnergyHatches);
    }

    public long getMaxInputAmps() {
        return GT_ExoticEnergyInputHelper.getMaxWorkingInputAmpsMulti(mEnergyHatches);
    }

    public long getMaxInputEu() {
        return GT_ExoticEnergyInputHelper.getTotalEuMulti(mEnergyHatches);
    }

    /**
     * Sums up max input EU/t of energy hatches, amperage included.
     */
    public long getMaxInputPower() {
        long eut = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            IGregTechTileEntity baseTile = tHatch.getBaseMetaTileEntity();
            eut += baseTile.getInputVoltage() * baseTile.getInputAmperage();
        }
        return eut;
    }

    /**
     * Returns voltage tier of energy hatches. If multiple tiers are found, returns 0.
     */
    public long getInputVoltageTier() {
        long rTier = 0;
        if (mEnergyHatches.size() > 0) {
            rTier = mEnergyHatches.get(0)
                .getInputTier();
            for (int i = 1; i < mEnergyHatches.size(); i++) {
                if (mEnergyHatches.get(i)
                    .getInputTier() != rTier) return 0;
            }
        }

        return rTier;
    }

    /**
     * Calcualtes the overclockedness using long integers
     *
     * @param aEUt            - recipe EUt
     * @param aDuration       - recipe Duration
     * @param mAmperage       - should be 1 ?
     * @param maxInputVoltage - Multiblock Max input voltage. Voltage is rounded up to higher tier voltage.
     * @param perfectOC       - If the Multiblock OCs perfectly, i.e. the large Chemical Reactor
     */
    protected void calculateOverclockedNessMultiInternal(long aEUt, int aDuration, int mAmperage, long maxInputVoltage,
        boolean perfectOC) {
        byte tier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(aEUt)
            .setEUt(V[tier] * mAmperage)
            .setDuration(aDuration)
            .setDurationDecreasePerOC(perfectOC ? 2 : 1)
            .calculate();
        mEUt = (int) calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
    }

    @Deprecated
    protected void calculateOverclockedNessMulti(int aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt, aDuration, mAmperage, maxInputVoltage, false);
    }

    protected void calculateOverclockedNessMulti(long aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt, aDuration, mAmperage, maxInputVoltage, false);
    }

    @Deprecated
    protected void calculatePerfectOverclockedNessMulti(int aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt, aDuration, mAmperage, maxInputVoltage, true);
    }

    protected void calculatePerfectOverclockedNessMulti(long aEUt, int aDuration, int mAmperage, long maxInputVoltage) {
        calculateOverclockedNessMultiInternal(aEUt, aDuration, mAmperage, maxInputVoltage, true);
    }

    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0) return true;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(aEU, false)) return true;
        }
        return false;
    }

    protected static boolean dumpFluid(List<GT_MetaTileEntity_Hatch_Output> aOutputHatches, FluidStack copiedFluidStack,
        boolean restrictiveHatchesOnly) {
        for (GT_MetaTileEntity_Hatch_Output tHatch : filterValidMTEs(aOutputHatches)) {
            if (restrictiveHatchesOnly && tHatch.mMode == 0) {
                continue;
            }
            if (!tHatch.canStoreFluid(copiedFluidStack)) continue;
            int tAmount = tHatch.fill(copiedFluidStack, false);
            if (tAmount >= copiedFluidStack.amount) {
                boolean filled = tHatch.fill(copiedFluidStack, true) >= copiedFluidStack.amount;
                tHatch.onEmptyingContainerWhenEmpty();
                return filled;
            } else if (tAmount > 0) {
                copiedFluidStack.amount = copiedFluidStack.amount - tHatch.fill(copiedFluidStack, true);
                tHatch.onEmptyingContainerWhenEmpty();
            }
        }
        return false;
    }

    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack copiedFluidStack = aLiquid.copy();
        if (!dumpFluid(mOutputHatches, copiedFluidStack, true)) {
            dumpFluid(mOutputHatches, copiedFluidStack, false);
        }
        return false;
    }

    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (FluidStack outputFluidStack : mOutputFluids2) {
            addOutput(outputFluidStack);
        }
    }

    public boolean depleteInput(FluidStack aLiquid) {
        return depleteInput(aLiquid, false);
    }

    public boolean depleteInput(FluidStack aLiquid, boolean simulate) {
        if (aLiquid == null) return false;
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            FluidStack tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, false);
            if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                if (simulate) {
                    return true;
                }
                tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, true);
                return tLiquid != null && tLiquid.amount >= aLiquid.amount;
            }
        }
        return false;
    }

    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        aStack = GT_Utility.copyOrNull(aStack);
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : filterValidMTEs(mOutputBusses)) {
            if (tHatch.storeAll(aStack)) {
                return true;
            }
        }
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (GT_MetaTileEntity_Hatch_Output tHatch : filterValidMTEs(mOutputHatches)) {
                if (!outputSuccess && tHatch.outputsItems()) {
                    if (tHatch.getBaseMetaTileEntity()
                        .addStackToSlot(1, single)) outputSuccess = true;
                }
            }
        }
        return outputSuccess;
    }

    public boolean depleteInput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            if (GT_Utility.areStacksEqual(
                aStack,
                tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(0))) {
                if (tHatch.getBaseMetaTileEntity()
                    .getStackInSlot(0).stackSize >= aStack.stackSize) {
                    tHatch.getBaseMetaTileEntity()
                        .decrStackSize(0, aStack.stackSize);
                    return true;
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : filterValidMTEs(mInputBusses)) {
            tHatch.mRecipeMap = getRecipeMap();
            for (int i = tHatch.getBaseMetaTileEntity()
                .getSizeInventory() - 1; i >= 0; i--) {
                if (GT_Utility.areStacksEqual(
                    aStack,
                    tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i))) {
                    if (tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i).stackSize >= aStack.stackSize) {
                        tHatch.getBaseMetaTileEntity()
                            .decrStackSize(i, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : filterValidMTEs(mOutputBusses)) {
            for (int i = tHatch.getBaseMetaTileEntity()
                .getSizeInventory() - 1; i >= 0; i--) {
                rList.add(
                    tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i));
            }
        }
        return rList;
    }

    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        Map<Fluid, FluidStack> inputsFromME = new HashMap<>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput multiInputHatch) {
                for (FluidStack tFluid : multiInputHatch.getStoredFluid()) {
                    if (tFluid != null) {
                        rList.add(tFluid);
                    }
                }
            } else if (tHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
                for (FluidStack fluidStack : meHatch.getStoredFluids()) {
                    if (fluidStack != null) {
                        // Prevent the same fluid from different ME hatches from being recognized
                        inputsFromME.put(fluidStack.getFluid(), fluidStack);
                    }
                }
            } else {
                if (tHatch.getFillableStack() != null) {
                    rList.add(tHatch.getFillableStack());
                }
            }
        }

        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    /**
     * Drains fluid from the given hatch, including {@link IDualInputHatch}. Should never be used during recipe check!
     *
     * @param doDrain If false, fluid will not actually be consumed
     * @return Whether the hatch contains enough fluid to drain
     */
    public boolean drain(GT_MetaTileEntity_Hatch hatch, FluidStack fluid, boolean doDrain) {
        if (fluid == null || hatch == null) return false;
        if (supportsCraftingMEBuffer() && hatch instanceof IDualInputHatch tHatch && tHatch.supportsFluids()) {
            Optional<IDualInputInventory> inventory = tHatch.getFirstNonEmptyInventory();
            if (inventory.isPresent()) {
                for (FluidStack storedFluid : Lists.newArrayList(
                    inventory.get()
                        .getFluidInputs())) {
                    if (fluid.isFluidEqual(storedFluid)) {
                        if (doDrain) storedFluid.amount = Math.max(storedFluid.amount - fluid.amount, 0);
                        return storedFluid.amount >= fluid.amount;
                    }
                }
            }
        }

        if (hatch instanceof GT_MetaTileEntity_Hatch_Input tHatch && tHatch.isValid()) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
                meHatch.startRecipeProcessing();
                FluidStack tFluid = meHatch.drain(ForgeDirection.UNKNOWN, fluid, doDrain);
                meHatch.endRecipeProcessing(this);
                return tFluid != null && tFluid.amount >= fluid.amount;
            } else {
                FluidStack tFluid = tHatch.drain(ForgeDirection.UNKNOWN, fluid, doDrain);
                return tFluid != null && tFluid.amount >= fluid.amount;
            }
        }

        return false;
    }

    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        Map<GT_Utility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : filterValidMTEs(mInputBusses)) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME) {
                continue;
            }
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof GT_MetaTileEntity_Hatch_InputBus_ME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GT_Utility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(getStackInSlot(1));
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    /**
     * Anything that is usually separated off in {@link #getStoredInputs()} (like crafting input bus/buffer) is also
     * included here.
     */
    public ArrayList<ItemStack> getAllStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();

        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                Iterator<? extends IDualInputInventory> inventoryIterator = dualInputHatch.inventories();
                while (inventoryIterator.hasNext()) {
                    ItemStack[] items = inventoryIterator.next()
                        .getItemInputs();
                    if (items == null) {
                        continue;
                    }
                    for (int i = 0; i < items.length; i++) {
                        ItemStack item = items[i];
                        if (item != null) {
                            rList.add(item);
                        }
                    }
                }
            }
        }

        Map<GT_Utility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : filterValidMTEs(mInputBusses)) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME) {
                continue;
            }
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof GT_MetaTileEntity_Hatch_InputBus_ME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GT_Utility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(getStackInSlot(1));
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    public Map<GT_Utility.ItemId, ItemStack> getStoredInputsFromME() {
        Map<GT_Utility.ItemId, ItemStack> inputsFromME = new Object2ReferenceOpenHashMap<>();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : filterValidMTEs(mInputBusses)) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_InputBus_ME meBus) {
                for (int i = meBus.getSizeInventory() - 1; i >= 0; i--) {
                    ItemStack itemStack = meBus.getStackInSlot(i);
                    if (itemStack != null) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GT_Utility.ItemId.createNoCopy(itemStack), itemStack);
                    }
                }
            }
        }
        return inputsFromME;
    }

    public Map<Fluid, FluidStack> getStoredFluidsFromME() {
        Map<Fluid, FluidStack> fluidsFromME = new Reference2ReferenceOpenHashMap<>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
                for (FluidStack fluid : meHatch.getStoredFluids()) {
                    if (fluid != null) {
                        // Prevent the same fluid from different ME hatches from being recognized
                        fluidsFromME.put(fluid.getFluid(), fluid);
                    }
                }
            }
        }
        return fluidsFromME;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    /**
     * Creates logic to run recipe check based on recipemap. This runs only once, on class instantiation.
     * <p>
     * If this machine doesn't use recipemap or does some complex things, override {@link #checkProcessing()}.
     */
    @ApiStatus.OverrideOnly
    protected ProcessingLogic createProcessingLogic() {
        return null;
    }

    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) tHatch.updateSlots();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : filterValidMTEs(mInputBusses)) tHatch.updateSlots();
    }

    protected void startRecipeProcessing() {
        for (GT_MetaTileEntity_Hatch_InputBus hatch : filterValidMTEs(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        for (GT_MetaTileEntity_Hatch_Input hatch : filterValidMTEs(mInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
    }

    public void setResultIfFailure(CheckRecipeResult result) {
        if (!result.wasSuccessful()) {
            this.checkRecipeResult = result;
        }
    }

    protected void endRecipeProcessing() {
        for (GT_MetaTileEntity_Hatch_InputBus hatch : filterValidMTEs(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
        for (GT_MetaTileEntity_Hatch_Input hatch : filterValidMTEs(mInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof IDualInputHatch hatch) {
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mDualInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            setHatchRecipeMap((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        return false;
    }

    public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mMaintenanceHatches.add(hatch);
        }
        return false;
    }

    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mEnergyHatches.add(hatch);
        }
        return false;
    }

    public boolean addExoticEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch
            && GT_ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mExoticEnergyHatches.add(hatch);
        }
        return false;
    }

    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mDynamoHatches.add(hatch);
        }
        return false;
    }

    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mMufflerHatches.add(hatch);
        }
        return false;
    }

    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addInputBusToMachineList(aTileEntity, aBaseCasingIndex)
            || addInputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addOutputBusToMachineList(aTileEntity, aBaseCasingIndex)
            || addOutputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addInputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof IDualInputHatch hatch) {
            if (!supportsCraftingMEBuffer()) return false;
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mDualInputHatches.add(hatch);
        }

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            hatch.mRecipeMap = getRecipeMap();
            return mInputBusses.add(hatch);
        }
        return false;
    }

    public boolean addOutputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mOutputBusses.add(hatch);
        }
        return false;
    }

    public boolean addInputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            setHatchRecipeMap(hatch);
            return mInputHatches.add(hatch);
        }
        return false;
    }

    public boolean addOutputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mOutputHatches.add(hatch);
        }
        return false;
    }

    protected void setHatchRecipeMap(GT_MetaTileEntity_Hatch_Input hatch) {
        if (filtersFluid()) {
            hatch.mRecipeMap = getRecipeMap();
        }
    }

    /**
     * @return If this multi filters fluid input for hatches based on recipemap.
     */
    protected boolean filtersFluid() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return supportsSlotAutomation(aIndex);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return supportsSlotAutomation(aIndex);
    }

    protected ItemStack[] getCompactedInputs() {
        // TODO: repalce method with a cleaner one
        ArrayList<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS = tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (!GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) continue;
                if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
                    tInputList.remove(j--);
                    tInputList_sS = tInputList.size();
                } else {
                    tInputList.remove(i--);
                    tInputList_sS = tInputList.size();
                    break;
                }
            }
        }
        return tInputList.toArray(new ItemStack[0]);
    }

    protected FluidStack[] getCompactedFluids() {
        // TODO: repalce method with a cleaner one
        ArrayList<FluidStack> tFluidList = getStoredFluids();
        int tFluidList_sS = tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (!GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) continue;

                if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                    tFluidList.remove(j--);
                    tFluidList_sS = tFluidList.size();
                } else {
                    tFluidList.remove(i--);
                    tFluidList_sS = tFluidList.size();
                    break;
                }
            }
        }
        return tFluidList.toArray(new FluidStack[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
        }
        String efficiency = RESET + "  Efficiency: " + tag.getFloat("efficiency") + "%";
        if (tag.getBoolean("hasProblems")) {
            currentTip.add(RED + "** HAS PROBLEMS **" + efficiency);
        } else if (!tag.getBoolean("incompleteStructure")) {
            currentTip.add(GREEN + "Running Fine" + efficiency);
        }

        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long energyTier = tag.getLong("energyTier");
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (energyTier > 0) {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.use_with_amperage",
                            formatNumbers(actualEnergyUsage),
                            GT_Utility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                            GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.produce_with_amperage",
                            formatNumbers(-actualEnergyUsage),
                            GT_Utility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                            GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
                }
            } else {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.use",
                            formatNumbers(actualEnergyUsage),
                            GT_Utility.getColoredTierNameFromVoltage(actualEnergyUsage)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.produce",
                            formatNumbers(-actualEnergyUsage),
                            GT_Utility.getColoredTierNameFromVoltage(-actualEnergyUsage)));
                }
            }
        }
        currentTip.add(
            GT_Waila.getMachineProgressString(isActive, tag.getInteger("maxProgress"), tag.getInteger("progress")));
        // Show ns on the tooltip
        if (GT_Mod.gregtechproxy.wailaAverageNS && tag.hasKey("averageNS")) {
            int tAverageTime = tag.getInteger("averageNS");
            currentTip.add("Average CPU load of ~" + formatNumbers(tAverageTime) + " ns");
        }
        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    public final void getMTEWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
        tag.setFloat("efficiency", mEfficiency / 100.0F);
        tag.setInteger("progress", mProgresstime);
        tag.setInteger("maxProgress", mMaxProgresstime);
        tag.setBoolean("incompleteStructure", (getBaseMetaTileEntity().getErrorDisplayID() & 64) != 0);

        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("isActive", tileEntity.isActive());
            if (tileEntity.isActive()) {
                if (mEUt < 0) tag.setLong("energyUsage", getActualEnergyUsage());
                else tag.setLong("energyUsage", (long) -mEUt * mEfficiency / 10000);
                tag.setLong("energyTier", getInputVoltageTier());
            }
        }

        final GT_ClientPreference preference = GT_Mod.gregtechproxy.getClientPreference(player.getUniqueID());
        if (preference != null && preference.isWailaAverageNSEnabled()) {
            getBaseMetaTileEntity().startTimeStatistics();
            int tAverageTime = 0;
            int amountOfZero = 0;
            for (int tTime : this.getBaseMetaTileEntity()
                .getTimeStatistics()) {
                tAverageTime += tTime;
                if (tTime == 0) {
                    amountOfZero += 1;
                }
            }

            // tick time zero means it has not been updated yet
            int samples = getBaseMetaTileEntity().getTimeStatistics().length - amountOfZero;
            if (samples > 0) {
                tag.setInteger("averageNS", tAverageTime / samples);
            }
        }
    }

    protected void setMufflers(boolean state) {
        for (GT_MetaTileEntity_Hatch_Muffler aMuffler : mMufflerHatches) {
            final IGregTechTileEntity iGTTileEntity = aMuffler.getBaseMetaTileEntity();
            if (iGTTileEntity != null && !iGTTileEntity.isDead()) {
                iGTTileEntity.setActive(state);
            }
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        // Deactivate mufflers
        setMufflers(false);
    }

    public List<GT_MetaTileEntity_Hatch> getExoticEnergyHatches() {
        return mExoticEnergyHatches;
    }

    /**
     * @return Returns true if there is 1 TT Energy Hatch OR up to 2 Energy Hatches
     */
    public boolean checkExoticAndNormalEnergyHatches() {
        if (mExoticEnergyHatches.isEmpty() && mEnergyHatches.isEmpty()) {
            return false;
        }

        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) {
                return false;
            }

            if (mExoticEnergyHatches.size() != 1) {
                return false;
            }
        }

        return mEnergyHatches.size() <= 2;
    }

    /**
     * Checks if all the item / fluid outputs of the recipe can be outputted to the buses / hatches.
     * If void protection is enabled, it also checks for {@link #protectsExcessItem()} and
     * {@link #protectsExcessFluid()}, so you don't need to call them along with this method.
     * <p>
     * If you're using {@link GT_ParallelHelper}, it will handle void protection and return 0 parallel
     * if all the output cannot be dumped into buses / hatches. In that case you won't use this method.
     */
    protected boolean canOutputAll(@Nonnull GT_Recipe recipe) {
        return canOutputAll(recipe.mOutputs, recipe.mFluidOutputs);
    }

    /**
     * Checks if all the items can be outputted to the output buses.
     * If void protection is enabled, it also checks for {@link #protectsExcessItem()},
     * so you don't need to call it along with this method.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canOutputAll(ItemStack[] items) {
        return canOutputAll(items, null);
    }

    /**
     * Checks if all the fluids can be outputted to the output hatches.
     * If void protection is enabled, it also checks for {@link #protectsExcessFluid()},
     * so you don't need to call it along with this method.
     */
    protected boolean canOutputAll(FluidStack[] fluids) {
        return canOutputAll(null, fluids);
    }

    /**
     * Checks if all the items / fluids can be outputted to output buses / hatches.
     * If void protection is enabled, it also checks for {@link #protectsExcessItem()} and
     * {@link #protectsExcessFluid()}, so you don't need to call them along with this method.
     */
    protected boolean canOutputAll(@Nullable ItemStack[] items, @Nullable FluidStack[] fluids) {
        if (!protectsExcessItem() && !protectsExcessFluid()) {
            return true;
        }

        VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper().setMachine(this)
            .setItemOutputs(items)
            .setFluidOutputs(fluids)
            .build();
        return voidProtectionHelper.getMaxParallel() > 0;
    }

    @Override
    public boolean isAllowedToWork() {
        return getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public void disableWorking() {
        if (getBaseMetaTileEntity() != null) {
            getBaseMetaTileEntity().disableWorking();
        }
    }

    @Override
    public void enableWorking() {
        if (getBaseMetaTileEntity() != null) {
            getBaseMetaTileEntity().enableWorking();
        }
    }

    public ItemStack getControllerSlot() {
        return mInventory[getControllerSlotIndex()];
    }

    public final int getControllerSlotIndex() {
        return 1;
    }

    // True if the slot with index aSlot may be interacted with through automation
    protected boolean supportsSlotAutomation(int aSlot) {
        return false;
    }

    @Override
    public Pos2d getPowerSwitchButtonPos() {
        return new Pos2d(174, 148);
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return voidingMode;
    }

    @Override
    public void setVoidingMode(VoidingMode mode) {
        this.voidingMode = mode;
    }

    @Override
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        List<ItemStack> ret = new ArrayList<>();
        for (final GT_MetaTileEntity_Hatch tBus : filterValidMTEs(mOutputBusses)) {
            final IInventory tBusInv = tBus.getBaseMetaTileEntity();
            for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                ret.add(tBus.getStackInSlot(i));
            }
        }
        return ret;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return filterValidMTEs(mOutputHatches);
    }

    /**
     * Util method for DT-like structure to collect list of output hatches.
     */
    protected <T extends GT_MetaTileEntity_Hatch_Output> List<? extends IFluidStore> getFluidOutputSlotsByLayer(
        FluidStack[] toOutput, List<List<T>> hatchesByLayer) {
        List<IFluidStore> ret = new ArrayList<>();
        for (int i = 0; i < toOutput.length; i++) {
            if (i >= hatchesByLayer.size()) {
                break;
            }
            FluidStack fluidOutputForLayer = toOutput[i];
            for (GT_MetaTileEntity_Hatch_Output hatch : hatchesByLayer.get(i)) {
                if (!hatch.isValid()) continue;
                if (fluidOutputForLayer != null) {
                    ret.add(new OutputHatchWrapper(hatch, f -> GT_Utility.areFluidsEqual(f, fluidOutputForLayer)));
                } else {
                    ret.add(hatch);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean canDumpItemToME() {
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mOutputBusses)) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canDumpFluidToME() {
        for (IFluidStore tHatch : getFluidOutputSlots(new FluidStack[0])) {
            if (tHatch instanceof GT_MetaTileEntity_Hatch_Output_ME) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Pos2d getVoidingModeButtonPos() {
        return new Pos2d(8, 91);
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return inputSeparation;
    }

    @Override
    public void setInputSeparation(boolean enabled) {
        this.inputSeparation = enabled;
    }

    @Override
    public Pos2d getInputSeparationButtonPos() {
        return new Pos2d(26, 91);
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public boolean isBatchModeEnabled() {
        return batchMode;
    }

    @Override
    public void setBatchMode(boolean enabled) {
        this.batchMode = enabled;
    }

    @Override
    public Pos2d getBatchModeButtonPos() {
        return new Pos2d(44, 91);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return mLockedToSingleRecipe;
    }

    @Override
    public void setRecipeLocking(boolean enabled) {
        mLockedToSingleRecipe = enabled;
        if (!enabled) {
            setSingleRecipeCheck(null);
        }
    }

    @Override
    public void setSingleRecipeCheck(SingleRecipeCheck recipeCheck) {
        mSingleRecipeCheck = recipeCheck;
    }

    @Override
    public SingleRecipeCheck getSingleRecipeCheck() {
        return mSingleRecipeCheck;
    }

    @Override
    public Pos2d getRecipeLockingButtonPos() {
        return new Pos2d(62, 91);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public int getGUIWidth() {
        return 198;
    }

    @Override
    public int getGUIHeight() {
        return 192;
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.bindPlayerInventory(buildContext.getPlayer(), new Pos2d(7, 109), getGUITextureSet().getItemSlot());
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85));
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        builder.widget(
            inventorySlot.setPos(173, 167)
                .setBackground(GT_UITextures.SLOT_DARK_GRAY));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(screenElements);

        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createInputSeparationButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {}

    protected boolean shouldDisplayCheckRecipeResult() {
        return true;
    }

    public boolean shouldDisplayShutDownReason() {
        return true;
    }

    protected final NumberFormatMUI numberFormat = new NumberFormatMUI();

    protected String generateCurrentRecipeInfoString() {
        StringBuffer ret = new StringBuffer(EnumChatFormatting.WHITE + "Progress: ");

        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.format((double) mProgresstime / 20, ret);
        ret.append("s / ");
        numberFormat.format((double) mMaxProgresstime / 20, ret);
        ret.append("s (");
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.format((double) mProgresstime / mMaxProgresstime * 100, ret);
        ret.append("%)\n");
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        IntConsumer appendRate = (amount) -> {
            double processPerTick = (double) amount / mMaxProgresstime * 20;
            if (processPerTick > 1) {
                ret.append(" (");
                numberFormat.format(Math.round(processPerTick * 10) / 10.0, ret);
                ret.append("/s)");
            } else {
                ret.append(" (");
                numberFormat.format(Math.round(1 / processPerTick * 10) / 10.0, ret);
                ret.append("s/ea)");
            }
        };

        int lines = 0;
        int MAX_LINES = 5;

        if (mOutputItems != null) {
            for (var item : mOutputItems) {
                if (item == null) continue;
                if (lines >= MAX_LINES) {
                    ret.append("...");
                    return ret.toString();
                }
                lines++;
                ret.append(EnumChatFormatting.AQUA)
                    .append(item.getDisplayName())
                    .append(EnumChatFormatting.WHITE)
                    .append(" x ")
                    .append(EnumChatFormatting.GOLD);
                numberFormat.format(item.stackSize, ret);
                ret.append(EnumChatFormatting.WHITE);
                appendRate.accept(item.stackSize);
                ret.append('\n');
            }
        }
        if (mOutputFluids != null) {
            for (var fluid : mOutputFluids) {
                if (fluid == null) continue;
                if (lines >= MAX_LINES) {
                    ret.append("...");
                    return ret.toString();
                }
                lines++;
                ret.append(EnumChatFormatting.AQUA)
                    .append(fluid.getLocalizedName())
                    .append(EnumChatFormatting.WHITE)
                    .append(" x ")
                    .append(EnumChatFormatting.GOLD);
                numberFormat.format(fluid.amount, ret);
                ret.append("L")
                    .append(EnumChatFormatting.WHITE);
                appendRate.accept(fluid.amount);
                ret.append('\n');
            }
        }
        return ret.toString();
    }

    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(10, 7);

        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("132", "Pipe is loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mWrench))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("133", "Screws are loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mScrewdriver))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("134", "Something is stuck.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSoftHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftHammer, val -> mSoftHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("135", "Platings are dented.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mHardHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("136", "Circuitry burned out.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSolderingTool))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("137", "That doesn't belong there."))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mCrowbar))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));
        screenElements.widget(
            new TextWidget("Too Uncertain.").setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> (getBaseMetaTileEntity().getErrorDisplayID() & 128) != 0));
        screenElements.widget(
            new TextWidget("Invalid Parameters.").setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> (getBaseMetaTileEntity().getErrorDisplayID() & 256) != 0));

        screenElements.widget(
            new TextWidget(GT_Utility.trans("139", "Hit with Soft Mallet")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> getBaseMetaTileEntity().getErrorDisplayID(),
                    val -> getBaseMetaTileEntity().setErrorDisplayID(val)))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)));
        screenElements.widget(
            new TextWidget(GT_Utility.trans("140", "to (re-)start the Machine")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));
        screenElements.widget(
            new TextWidget(GT_Utility.trans("141", "if it doesn't start.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));
        screenElements.widget(
            new TextWidget(GT_Utility.trans("142", "Running perfectly.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive()));

        screenElements.widget(
            TextWidget.dynamicString(
                () -> getBaseMetaTileEntity().getLastShutDownReason()
                    .getDisplayString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                        && GT_Utility.isStringValid(
                            getBaseMetaTileEntity().getLastShutDownReason()
                                .getDisplayString())
                        && getBaseMetaTileEntity().wasShutdown()))
            .widget(
                new ShutDownReasonSyncer(
                    () -> getBaseMetaTileEntity().getLastShutDownReason(),
                    reason -> getBaseMetaTileEntity().setShutDownReason(reason)))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().wasShutdown(),
                    wasShutDown -> getBaseMetaTileEntity().setShutdownStatus(wasShutDown)));

        screenElements.widget(
            TextWidget.dynamicString(() -> checkRecipeResult.getDisplayString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> shouldDisplayCheckRecipeResult()
                        && GT_Utility.isStringValid(checkRecipeResult.getDisplayString())
                        && (isAllowedToWork() || getBaseMetaTileEntity().isActive()
                            || checkRecipeResult.persistsOnShutdown())))
            .widget(new CheckRecipeResultSyncer(() -> checkRecipeResult, (result) -> checkRecipeResult = result));

        if (showRecipeTextInGUI()) {
            // Display current recipe
            screenElements.widget(
                TextWidget.dynamicString(this::generateCurrentRecipeInfoString)
                    .setSynced(false)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(
                        widget -> (mOutputFluids != null && mOutputFluids.length > 0)
                            || (mOutputItems != null && mOutputItems.length > 0)))
                .widget(
                    new FakeSyncWidget.ListSyncer<>(
                        () -> mOutputFluids != null ? Arrays.asList(mOutputFluids) : Collections.emptyList(),
                        val -> mOutputFluids = val.toArray(new FluidStack[0]),
                        NetworkUtils::writeFluidStack,
                        NetworkUtils::readFluidStack))
                .widget(
                    new FakeSyncWidget.ListSyncer<>(
                        () -> mOutputItems != null ? Arrays.asList(mOutputItems) : Collections.emptyList(),
                        val -> mOutputItems = val.toArray(new ItemStack[0]),
                        NetworkUtils::writeItemStack,
                        NetworkUtils::readItemStack))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mProgresstime, val -> mProgresstime = val))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, val -> mMaxProgresstime = val));
        }

        screenElements.widget(
            new TextWidget(GT_Utility.trans("144", "Missing Turbine Rotor")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> {
                    if (getBaseMetaTileEntity().isAllowedToWork()) return false;
                    if (getBaseMetaTileEntity().getErrorDisplayID() == 0
                        && this instanceof GT_MetaTileEntity_LargeTurbine) {
                        final ItemStack tItem = inventorySlot.getMcSlot()
                            .getStack();
                        return tItem == null
                            || !(tItem.getItem() == GT_MetaGenerated_Tool_01.INSTANCE && tItem.getItemDamage() >= 170
                                && tItem.getItemDamage() <= 177);
                    }
                    return false;
                }));
    }

    protected boolean showRecipeTextInGUI() {
        return true;
    }

    @TestOnly
    protected void setEnergyHatches(ArrayList<GT_MetaTileEntity_Hatch_Energy> EnergyHatches) {
        this.mEnergyHatches = EnergyHatches;
    }

    @TestOnly
    protected void setExoticEnergyHatches(List<GT_MetaTileEntity_Hatch> ExoticEnergyHatches) {
        this.mExoticEnergyHatches = ExoticEnergyHatches;
    }

    public void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    public boolean getDefaultHasMaintenanceChecks() {
        return true;
    }

    public boolean shouldCheckMaintenance() {
        return !disableMaintenance && hasMaintenanceChecks;
    }
}
