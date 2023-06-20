package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.TestOnly;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Iterables;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
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
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Single_Recipe_Check;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Waila;
import gregtech.api.util.OutputHatchWrapper;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.client.GT_SoundLoop;
import gregtech.common.GT_Pollution;
import gregtech.common.gui.modularui.widget.CheckRecipeResultSyncer;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class GT_MetaTileEntity_MultiBlockBase extends MetaTileEntity
    implements ControllerWithOptionalFeatures, IAddGregtechLogo, IAddUIWidgets, IBindPlayerInventoryUI {

    public static boolean disableMaintenance;
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
    public GT_Single_Recipe_Check mSingleRecipeCheck = null;

    public ArrayList<GT_MetaTileEntity_Hatch_Input> mInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Output> mOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_InputBus> mInputBusses = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputBus> mOutputBusses = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Dynamo> mDynamoHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Muffler> mMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Energy> mEnergyHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Maintenance> mMaintenanceHatches = new ArrayList<>();
    protected List<GT_MetaTileEntity_Hatch> mExoticEnergyHatches = new ArrayList<>();
    protected final ProcessingLogic processingLogic;
    @SideOnly(Side.CLIENT)
    protected GT_SoundLoop activitySoundLoop;

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
    }

    // maybe remove this at some point?
    public static boolean isValidMetaTileEntity(MetaTileEntity aMetaTileEntity) {
        return aMetaTileEntity.isValid();
    }

    public static <T extends MetaTileEntity> List<T> filterValidMetaTileEntities(Collection<T> metaTileEntities) {
        return metaTileEntities.stream()
            .filter(MetaTileEntity::isValid)
            .collect(Collectors.toList());
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
                GT_Single_Recipe_Check c = loadSingleRecipeChecker(aNBT.getCompoundTag("mSingleRecipeCheck"));
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

        mWrench = aNBT.getBoolean("mWrench");
        mScrewdriver = aNBT.getBoolean("mScrewdriver");
        mSoftHammer = aNBT.getBoolean("mSoftHammer");
        mHardHammer = aNBT.getBoolean("mHardHammer");
        mSolderingTool = aNBT.getBoolean("mSolderingTool");
        mCrowbar = aNBT.getBoolean("mCrowbar");
    }

    protected GT_Single_Recipe_Check loadSingleRecipeChecker(NBTTagCompound aNBT) {
        return GT_Single_Recipe_Check.tryLoad(this, aNBT);
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
                    } else {
                        stopMachine();
                    }
                } else {
                    stopMachine();
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

    private void checkMaintenance() {
        if (disableMaintenance) {
            mWrench = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mHardHammer = true;
            mSolderingTool = true;
            mCrowbar = true;

            return;
        }
        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                if (tHatch.mAuto
                    && !(mWrench && mScrewdriver && mSoftHammer && mHardHammer && mSolderingTool && mCrowbar))
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
    }

    protected boolean checkRecipe() {
        startRecipeProcessing();
        CheckRecipeResult result = checkProcessing();
        if (result.wasSuccessful() && getProcessStartSound() != null) {
            sendLoopStart(PROCESS_START_SOUND_INDEX);
        }
        this.checkRecipeResult = result;
        endRecipeProcessing();
        return result.wasSuccessful();
    }

    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
            if (onRunningTick(mInventory[1])) {
                markDirty();
                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                    stopMachine();
                }
                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems) if (tStack != null) {
                        try {
                            GT_Mod.achievements.issueAchivementHatch(
                                aBaseMetaTileEntity.getWorld()
                                    .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                tStack);
                        } catch (Exception ignored) {}
                        addOutput(tStack);
                    }
                    if (mOutputFluids != null) {
                        addFluidOutputs(mOutputFluids);
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
                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                        checkRecipe();
                    }
                    if (mOutputFluids != null && mOutputFluids.length > 0) {
                        if (mOutputFluids.length > 1) {
                            try {
                                GT_Mod.achievements.issueAchievement(
                                    aBaseMetaTileEntity.getWorld()
                                        .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                    "oilplant");
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        } else {
            if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
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
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                if (mPollution >= 10000) {
                    if (tHatch.polluteEnvironment(this)) {
                        mPollution -= 10000;
                    }
                } else {
                    break;
                }
            }
        }
        return mPollution < 10000;
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
                criticalStopMachine();
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
     * Checks the Recipe
     *
     * @deprecated Use {@link #checkProcessing()} instead
     */
    @Deprecated
    public boolean checkRecipe(ItemStack aStack) {
        return false;
    }

    @Nonnull
    public CheckRecipeResult checkProcessing() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;

        processingLogic.clear();
        processingLogic.setMachine(this);
        processingLogic.setRecipeMapSupplier(this::getRecipeMap);
        processingLogic.setVoidProtection(protectsExcessItem(), protectsExcessFluid());
        processingLogic.setRecipeLocking(this, isRecipeLockingEnabled());
        processingLogic.setInputFluids(getStoredFluids());
        setProcessingLogicPower(processingLogic);
        if (isInputSeparationEnabled()) {
            for (GT_MetaTileEntity_Hatch_InputBus bus : mInputBusses) {
                processingLogic.setInputItems(bus.mInventory);
                result = processingLogic.process();
                if (result.wasSuccessful()) break;
            }
        } else {
            processingLogic.setInputItems(getStoredInputs());
            result = processingLogic.process();
        }

        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;

        if (processingLogic.getCalculatedEut() > Integer.MAX_VALUE) return CheckRecipeResultRegistry.NO_RECIPE;
        mEUt = (int) processingLogic.getCalculatedEut();

        if (processingLogic.getDuration() > Integer.MAX_VALUE) return CheckRecipeResultRegistry.NO_RECIPE;
        mMaxProgresstime = (int) processingLogic.getDuration();

        if (mEUt > 0) {
            mEUt = (-mEUt);
        }

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        updateSlots();
        return result;
    }

    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputVoltage());
        logic.setAvailableAmperage(1);
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

    public void stopMachine() {
        mOutputItems = null;
        mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        getBaseMetaTileEntity().disableWorking();
        checkRecipeResult = CheckRecipeResultRegistry.NONE;
    }

    public void criticalStopMachine() {
        stopMachine();
        sendSound(INTERRUPT_SOUND_INDEX);
        getBaseMetaTileEntity().setShutdownStatus(true);
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
        if (!isCorrectMachinePart(mInventory[1]) || getRepairStatus() == 0) {
            stopMachine();
            return false;
        }
        if (mRuntime++ > 1000) {
            mRuntime = 0;
            if (getBaseMetaTileEntity().getRandomNumber(6000) == 0) {
                switch (getBaseMetaTileEntity().getRandomNumber(6)) {
                    case 0 -> mWrench = false;
                    case 1 -> mScrewdriver = false;
                    case 2 -> mSoftHammer = false;
                    case 3 -> mHardHammer = false;
                    case 4 -> mSolderingTool = false;
                    case 5 -> mCrowbar = false;
                }
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
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : mDynamoHatches) {
            if (aDynamo == null) {
                return false;
            }
            if (isValidMetaTileEntity(aDynamo)) {
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
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : mDynamoHatches) {
            if (isValidMetaTileEntity(aDynamo)) {
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
        }
        return injected > 0;
    }

    public long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity()
                .getInputVoltage();
        return rVoltage;
    }

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
     * @param maxInputVoltage - Multiblock Max input voltage
     * @param perfectOC       - If the Multiblock OCs perfectly, i.e. the large Chemical Reactor
     */
    protected void calculateOverclockedNessMultiInternal(long aEUt, int aDuration, int mAmperage, long maxInputVoltage,
        boolean perfectOC) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage));
        if (mTier == 0) {
            // Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                // make impossible if too long
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = GT_Utility.safeInt(aEUt >> 2);
                mMaxProgresstime = (int) xMaxProgresstime;
            }
        } else {
            // Long EUt calculation
            long xEUt = aEUt;
            // Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            mMaxProgresstime = aDuration;

            final int ocTimeShift = perfectOC ? 2 : 1;

            while (tempEUt <= V[mTier - 1] * mAmperage) {
                tempEUt <<= 2; // this actually controls overclocking
                // xEUt *= 4;//this is effect of everclocking
                int oldTime = mMaxProgresstime;
                mMaxProgresstime >>= ocTimeShift; // this is effect of overclocking
                if (mMaxProgresstime < 1) {
                    if (oldTime == 1) break;
                    xEUt *= (long) oldTime * (perfectOC ? 1 : 2);
                    break;
                } else {
                    xEUt <<= 2;
                }
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = (int) xEUt;
                if (mEUt == 0) mEUt = 1;
                if (mMaxProgresstime == 0) mMaxProgresstime = 1; // set time to 1 tick
            }
        }
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
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) if (isValidMetaTileEntity(tHatch)) {
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(aEU, false)) return true;
        }
        return false;
    }

    protected static boolean dumpFluid(List<GT_MetaTileEntity_Hatch_Output> aOutputHatches, FluidStack copiedFluidStack,
        boolean restrictiveHatchesOnly) {
        for (GT_MetaTileEntity_Hatch_Output tHatch : aOutputHatches) {
            if (!isValidMetaTileEntity(tHatch) || (restrictiveHatchesOnly && tHatch.mMode == 0)) {
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
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, false);
                if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                    if (simulate) {
                        return true;
                    }
                    tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, true);
                    return tLiquid != null && tLiquid.amount >= aLiquid.amount;
                }
            }
        }
        return false;
    }

    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        aStack = GT_Utility.copyOrNull(aStack);
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : mOutputBusses) {
            if (isValidMetaTileEntity(tHatch) && tHatch.storeAll(aStack)) {
                return true;
            }
        }
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
                if (!outputSuccess && isValidMetaTileEntity(tHatch) && tHatch.outputsItems()) {
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
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
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
        }
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
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
        }
        return false;
    }

    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_OutputBus tHatch : mOutputBusses) {
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity()
                    .getSizeInventory() - 1; i >= 0; i--) {
                    rList.add(
                        tHatch.getBaseMetaTileEntity()
                            .getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                if (isValidMetaTileEntity(tHatch)) {
                    for (FluidStack tFluid : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                        if (tFluid != null) {
                            // GT_Log.out.print("mf: " + tFluid + "\n");
                            rList.add(tFluid);
                        }
                    }
                }
            } else {
                if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                    // GT_Log.out.print("sf: " + tHatch.getFillableStack() + "\n");
                    rList.add(tHatch.getFillableStack());
                }
            }
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity()
                    .getSizeInventory() - 1; i >= 0; i--) {
                    if (tHatch.getBaseMetaTileEntity()
                        .getStackInSlot(i) != null)
                        rList.add(
                            tHatch.getBaseMetaTileEntity()
                                .getStackInSlot(i));
                }
            }
        }
        if (getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(getStackInSlot(1));
        return rList;
    }

    public GT_Recipe_Map getRecipeMap() {
        return null;
    }

    protected ProcessingLogic createProcessingLogic() {
        return null;
    }

    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
    }

    protected void startRecipeProcessing() {
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses)
            if (isValidMetaTileEntity(tHatch)) tHatch.startRecipeProcessing();
    }

    protected void endRecipeProcessing() {
        for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses)
            if (isValidMetaTileEntity(tHatch)) tHatch.endRecipeProcessing();
    }

    protected static <T extends GT_MetaTileEntity_Hatch> T identifyHatch(IGregTechTileEntity aTileEntity,
        int aBaseCasingIndex, Class<T> clazz) {
        if (aTileEntity == null) return null;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!clazz.isInstance(aMetaTileEntity)) return null;
        T hatch = clazz.cast(aMetaTileEntity);
        hatch.updateTexture(aBaseCasingIndex);
        return hatch;
    }

    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
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
            hatch.mRecipeMap = getRecipeMap();
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

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
            }
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxInputVoltage())
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
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
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
        currentTip.add(
            (tag.getBoolean("hasProblems") ? (RED + "** HAS PROBLEMS **") : GREEN + "Running Fine") + RESET
                + "  Efficiency: "
                + tag.getFloat("efficiency")
                + "%");

        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            long energyTier = tag.getLong("energyTier");
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (energyTier > 0) {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.use_with_amperage",
                            GT_Utility.formatNumbers(actualEnergyUsage),
                            GT_Utility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                            GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.produce_with_amperage",
                            GT_Utility.formatNumbers(-actualEnergyUsage),
                            GT_Utility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                            GT_Utility.getColoredTierNameFromTier((byte) energyTier)));
                }
            } else {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.use",
                            GT_Utility.formatNumbers(actualEnergyUsage),
                            GT_Utility.getColoredTierNameFromVoltage(actualEnergyUsage)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.waila.energy.produce",
                            GT_Utility.formatNumbers(-actualEnergyUsage),
                            GT_Utility.getColoredTierNameFromVoltage(-actualEnergyUsage)));
                }
            }
        }
        currentTip.add(
            GT_Waila.getMachineProgressString(isActive, tag.getInteger("maxProgress"), tag.getInteger("progress")));

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
        return getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public void disableWorking() {
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public void enableWorking() {
        getBaseMetaTileEntity().enableWorking();
    }

    @Override
    public ItemStack getControllerSlot() {
        return mInventory[1];
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
        for (final GT_MetaTileEntity_Hatch tBus : filterValidMetaTileEntities(mOutputBusses)) {
            final IInventory tBusInv = tBus.getBaseMetaTileEntity();
            for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                ret.add(tBus.getStackInSlot(i));
            }
        }
        return ret;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return filterValidMetaTileEntities(mOutputHatches);
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
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMetaTileEntities(mOutputBusses)) {
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
        this.mLockedToSingleRecipe = enabled;
    }

    @Override
    public void setSingleRecipeCheck(GT_Single_Recipe_Check recipeCheck) {
        mSingleRecipeCheck = recipeCheck;
    }

    @Override
    public GT_Single_Recipe_Check getSingleRecipeCheck() {
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
            TextWidget.dynamicString(() -> checkRecipeResult.getDisplayString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(widget -> GT_Utility.isStringValid(checkRecipeResult.getDisplayString())))
            .widget(new CheckRecipeResultSyncer(() -> checkRecipeResult, (result) -> checkRecipeResult = result));

        screenElements.widget(
            new TextWidget(GT_Utility.trans("143", "Missing Mining Pipe")).setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> {
                    if (getBaseMetaTileEntity().getErrorDisplayID() == 0
                        && this instanceof GT_MetaTileEntity_DrillerBase) {
                        final ItemStack tItem = inventorySlot.getMcSlot()
                            .getStack();
                        return tItem == null
                            || !GT_Utility.areStacksEqual(tItem, GT_ModHandler.getIC2Item("miningPipe", 1L));
                    }
                    return false;
                }));
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

    @TestOnly
    protected void setEnergyHatches(ArrayList<GT_MetaTileEntity_Hatch_Energy> EnergyHatches) {
        this.mEnergyHatches = EnergyHatches;
    }

    @TestOnly
    protected void setExoticEnergyHatches(List<GT_MetaTileEntity_Hatch> ExoticEnergyHatches) {
        this.mExoticEnergyHatches = ExoticEnergyHatches;
    }
}
