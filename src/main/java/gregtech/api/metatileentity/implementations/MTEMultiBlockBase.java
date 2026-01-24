package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.recipe.check.SingleRecipeCheck.getDisplayString;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gregtech.api.util.GTUtility.formatShortenedLong;
import static gregtech.api.util.GTUtility.min;
import static gregtech.api.util.GTUtility.truncateText;
import static gregtech.api.util.GTUtility.validMTEList;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.TestOnly;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.FluidDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.ChangeableWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.CheckboxWidget;
import gregtech.api.gui.widgets.StructureErrorSyncer;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.modularui.IBindPlayerInventoryUI;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.LocSer;
import gregtech.api.util.OutputHatchWrapper;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.client.GTSoundLoop;
import gregtech.common.config.MachineStats;
import gregtech.common.data.GTCoilTracker;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.widget.CheckRecipeResultSyncer;
import gregtech.common.gui.modularui.widget.ShutDownReasonSyncer;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.pollution.Pollution;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputHatchWithPattern;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.ISmartInputHatch;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import gregtech.common.tileentities.machines.multi.MTELargeTurbine;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public abstract class MTEMultiBlockBase extends MetaTileEntity implements IControllerWithOptionalFeatures,
    IAddGregtechLogo, IGuiHolder<PosGuiData>, IAddUIWidgets, IBindPlayerInventoryUI {

    public static boolean disableMaintenance;
    public boolean hasMaintenanceChecks = getDefaultHasMaintenanceChecks();
    public boolean mMachine = false, mWrench = false, mScrewdriver = false, mSoftMallet = false, mHardHammer = false,
        mSolderingTool = false, mCrowbar = false, mRunningOnLoad = false;
    public boolean mStructureChanged = false;
    private int errorDisplayID;
    public int mPollution = 0, mProgresstime = 0, mMaxProgresstime = 0, mEUt = 0, mEfficiencyIncrease = 0,
        mStartUpCheck = 100, mRuntime = 0, mEfficiency = 0, lastParallel = 0;
    public long recipesDone = 0;
    public volatile boolean mUpdated = false;
    public int mUpdate = 0;
    public ItemStack[] mOutputItems = null;
    public FluidStack[] mOutputFluids = null;
    public String mNEI;
    public int damageFactorLow = 5;
    public float damageFactorHigh = 0.6f;
    public int machineMode = 0;
    protected List<UITexture> machineModeIcons;

    public boolean mLockedToSingleRecipe = getDefaultRecipeLockingMode();
    protected boolean inputSeparation = getDefaultInputSeparationMode();
    protected VoidingMode voidingMode = getDefaultVoidingMode();
    protected boolean batchMode = getDefaultBatchMode() && supportsBatchMode();
    protected @Nonnull CheckRecipeResult checkRecipeResult = CheckRecipeResultRegistry.NONE;
    protected int powerPanelMaxParallel = 1;
    protected boolean alwaysMaxParallel = true;
    protected int maxParallel = 1;
    protected boolean makePowerfailEvents = true;
    // for the wireless maintenance detector cover gui, to display / not display the turbine row.
    protected boolean usesTurbine = false;
    protected boolean canBeMuffled = true;
    protected boolean debugEnergyPresent = false;

    protected static final String INPUT_SEPARATION_NBT_KEY = "inputSeparation";
    protected static final String VOID_EXCESS_NBT_KEY = "voidExcess";
    protected static final String VOIDING_MODE_NBT_KEY = "voidingMode";
    protected static final String BATCH_MODE_NBT_KEY = "batchMode";
    protected SingleRecipeCheck mSingleRecipeCheck = null;

    public ArrayList<MTEHatchInput> mInputHatches = new ArrayList<>();
    public ArrayList<MTEHatchOutput> mOutputHatches = new ArrayList<>();
    public ArrayList<MTEHatchInputBus> mInputBusses = new ArrayList<>();
    public ArrayList<MTEHatchOutputBus> mOutputBusses = new ArrayList<>();
    public ArrayList<IDualInputHatch> mDualInputHatches = new ArrayList<>();
    public ArrayList<ISmartInputHatch> mSmartInputHatches = new ArrayList<>();
    public ArrayList<MTEHatchDynamo> mDynamoHatches = new ArrayList<>();
    public ArrayList<MTEHatchMuffler> mMufflerHatches = new ArrayList<>();
    public ArrayList<MTEHatchEnergy> mEnergyHatches = new ArrayList<>();
    public ArrayList<MTEHatchMaintenance> mMaintenanceHatches = new ArrayList<>();

    /**
     * The list of coils in this multi's structure. Use
     * {@link gregtech.api.util.GTStructureUtility#activeCoils(IStructureElement)} to add them automatically.
     */
    public LongArrayList mCoils = new LongArrayList();
    private GTCoilTracker.MultiCoilLease coilLease = null;

    protected List<MTEHatch> mExoticEnergyHatches = new ArrayList<>();
    protected final ProcessingLogic processingLogic;
    @SideOnly(Side.CLIENT)
    protected GTSoundLoop activitySoundLoop;

    protected long mLastWorkingTick = 0, mTotalRunTime = 0;
    private static final int CHECK_INTERVAL = 100; // How often should we check for a new recipe on an idle machine?
    private final int randomTickOffset = (int) (Math.random() * CHECK_INTERVAL + 1);

    /** A list of unparameterized structure errors. */
    private EnumSet<StructureError> structureErrors = EnumSet.noneOf(StructureError.class);

    /**
     * Any implementation-defined error data. Private so that multis have to use the parameters (to make it easier to
     * refactor if needed).
     */
    private NBTTagCompound structureErrorContext = new NBTTagCompound();

    protected static final byte INTERRUPT_SOUND_INDEX = 8;
    protected static final byte PROCESS_START_SOUND_INDEX = 1;

    public MTEMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 2);
        this.processingLogic = null;
        MTEMultiBlockBase.disableMaintenance = MachineStats.machines.disableMaintenanceChecks;
        this.damageFactorLow = MachineStats.machines.damageFactorLow;
        this.damageFactorHigh = MachineStats.machines.damageFactorHigh;
        this.mNEI = "";
        if (!shouldCheckMaintenance()) fixAllIssues();
    }

    public MTEMultiBlockBase(String aName) {
        super(aName, 2);
        this.processingLogic = createProcessingLogic();
        MTEMultiBlockBase.disableMaintenance = MachineStats.machines.disableMaintenanceChecks;
        this.damageFactorLow = MachineStats.machines.damageFactorLow;
        this.damageFactorHigh = MachineStats.machines.damageFactorHigh;
        if (!shouldCheckMaintenance()) fixAllIssues();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (supportsSingleRecipeLocking()) {
            mLockedToSingleRecipe = !mLockedToSingleRecipe;
            if (mLockedToSingleRecipe) {
                GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.mb.locking_on");
            } else {
                GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.mb.locking_off");
                mSingleRecipeCheck = null;
            }
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
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

    public long getTotalRuntimeInTicks() {
        return mTotalRunTime;
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
        aNBT.setInteger("powerPanelMaxParallel", powerPanelMaxParallel);
        aNBT.setLong("mTotalRunTime", mTotalRunTime);
        aNBT.setLong("mLastWorkingTick", mLastWorkingTick);
        aNBT.setLong("recipesDone", recipesDone);
        aNBT.setString("checkRecipeResultID", checkRecipeResult.getID());
        aNBT.setTag("checkRecipeResult", checkRecipeResult.writeToNBT(new NBTTagCompound()));
        if (supportsMachineModeSwitch()) {
            aNBT.setInteger("machineMode", machineMode);
        }

        if (supportsSingleRecipeLocking()) {
            aNBT.setBoolean("mLockedToSingleRecipe", mLockedToSingleRecipe);
            if (mLockedToSingleRecipe && mSingleRecipeCheck != null)
                aNBT.setTag("mSingleRecipeCheck", mSingleRecipeCheck.writeToNBT());
        }

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
        if (processingLogic != null) {
            aNBT.setInteger("lastParallel", processingLogic.getCurrentParallels());
        }
        aNBT.setBoolean("mWrench", mWrench);
        aNBT.setBoolean("mScrewdriver", mScrewdriver);
        aNBT.setBoolean("mSoftMallet", mSoftMallet);
        aNBT.setBoolean("mHardHammer", mHardHammer);
        aNBT.setBoolean("mSolderingTool", mSolderingTool);
        aNBT.setBoolean("mCrowbar", mCrowbar);
        aNBT.setBoolean(BATCH_MODE_NBT_KEY, batchMode);
        aNBT.setBoolean(INPUT_SEPARATION_NBT_KEY, inputSeparation);
        aNBT.setBoolean("alwaysMaxParallel", alwaysMaxParallel);
        aNBT.setBoolean("makePowerfailEvents", makePowerfailEvents);
        aNBT.setString(VOIDING_MODE_NBT_KEY, voidingMode.name);
        aNBT.setBoolean("usesTurbine", usesTurbine);
        aNBT.setBoolean("canBeMuffled", canBeMuffled);
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
        mTotalRunTime = aNBT.getLong("mTotalRunTime");
        mLastWorkingTick = aNBT.getLong("mLastWorkingTick");
        recipesDone = aNBT.getLong("recipesDone");
        // If the key doesn't exist it should default true
        alwaysMaxParallel = !aNBT.hasKey("alwaysMaxParallel") || aNBT.getBoolean("alwaysMaxParallel");
        powerPanelMaxParallel = aNBT.getInteger("powerPanelMaxParallel");
        makePowerfailEvents = !aNBT.hasKey("makePowerfailEvents") || aNBT.getBoolean("makePowerfailEvents");
        usesTurbine = aNBT.hasKey("usesTurbine") && aNBT.getBoolean("usesTurbine");
        canBeMuffled = aNBT.hasKey("canBeMuffled") && aNBT.getBoolean("canBeMuffled");
        String checkRecipeResultID = aNBT.getString("checkRecipeResultID");
        if (CheckRecipeResultRegistry.isRegistered(checkRecipeResultID)) {
            CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(checkRecipeResultID)
                .newInstance();
            result.readFromNBT(aNBT.getCompoundTag("checkRecipeResult"));
            checkRecipeResult = result;
        }

        if (aNBT.hasKey("machineMode")) {
            machineMode = aNBT.getInteger("machineMode");
        }
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
            for (int i = 0; i < mOutputItems.length; i++) mOutputItems[i] = GTUtility.loadItem(aNBT, "mOutputItem" + i);
        }

        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++)
                mOutputFluids[i] = GTUtility.loadFluid(aNBT, "mOutputFluids" + i);
        }
        if (processingLogic != null) {
            lastParallel = aNBT.getInteger("lastParallel");
        }
        if (shouldCheckMaintenance()) {
            mWrench = aNBT.getBoolean("mWrench");
            mScrewdriver = aNBT.getBoolean("mScrewdriver");
            mSoftMallet = aNBT.getBoolean("mSoftMallet") || aNBT.getBoolean("mSoftHammer");
            mHardHammer = aNBT.getBoolean("mHardHammer");
            mSolderingTool = aNBT.getBoolean("mSolderingTool");
            mCrowbar = aNBT.getBoolean("mCrowbar");
        } else fixAllIssues();
    }

    protected SingleRecipeCheck loadSingleRecipeChecker(NBTTagCompound aNBT) {
        return SingleRecipeCheck.tryLoad(getRecipeMap(), aNBT);
    }

    public boolean saveOtherHatchConfiguration(EntityPlayer player) {
        return true;
    }

    public boolean loadOtherHatchConfiguration(EntityPlayer player) {
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isServerSide() && GTUtil.saveMultiblockInputConfiguration(this, aPlayer)) {
            aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.SAVE"));
            return;
        }
        super.onLeftclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (GTUtil.hasMultiblockInputConfiguration(aPlayer.getHeldItem())) {
            if (aBaseMetaTileEntity.isServerSide()) {
                if (GTUtil.loadMultiblockInputConfiguration(this, aPlayer)) {
                    aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.LOAD"));
                } else {
                    aPlayer
                        .addChatComponentMessage(new ChatComponentTranslation("GT5U.MULTI_MACHINE_CONFIG.LOAD.FAIL"));
                }
            }
            return true;
        }
        openGui(aPlayer);
        return true;
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchLevel2.toTileEntityBaseType();
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
        mSmartInputHatches.clear();

        mCoils.clear();
        deactivateCoilLease();
        debugEnergyPresent = false;
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

            doStructureValidation();
        }
        mStructureChanged = false;
        return mMachine;
    }

    protected final void doStructureValidation() {
        structureErrors = EnumSet.noneOf(StructureError.class);
        structureErrorContext = new NBTTagCompound();

        // only run validation when the structure check passes, so that we don't confuse people
        if (mMachine) {
            validateStructure(structureErrors, structureErrorContext);

            if (hasStructureErrors()) mMachine = false;
        }
    }

    /**
     * Validates this multi's structure (hatch/casing counts mainly) for any errors. The multi will not form if any
     * errors are added to {@code errors}. Only runs when {@link #checkMachine} is successful.
     *
     * @param errors  Add errors to this.
     * @param context Generic data blob that is synced with the client.
     */
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {

    }

    /**
     * Scans {@code errors}, {@code context}, or other fields as needed and emits localized structure error messages.
     * The {@code errors} and {@code context} params are synced already, but any other fields must be manually synced.
     * Note that the parameters may not be in sync due to network latency (they are synced separately). You shouldn't
     * rely on a field being in {@code context} if an error is present in {@code errors}. This method is typically only
     * called on the client, but it may be called on the server in the future. Don't use {@link I18n#format} since
     * that's client-only (use {@link net.minecraft.util.StatCollector#translateToLocal} & its variants instead).
     *
     * @param errors  The errors generated by {@link #validateStructure}.
     * @param context Generic context blob generated by {@link #validateStructure}.
     * @param lines   Add text to this. These lines will be shown in the controller GUI.
     */
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {

    }

    /**
     * Controls whether the error message widget is shown. If you have any new structure status fields, make sure to
     * check them here.
     */
    protected boolean hasStructureErrors() {
        return !structureErrors.isEmpty();
    }

    /**
     * Returns the error ID displayed on the GUI.
     */
    public int getErrorDisplayID() {
        return errorDisplayID;
    }

    /**
     * Sets the error ID displayed on the GUI.
     */
    public void setErrorDisplayID(int errorID) {
        this.errorDisplayID = errorID;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Time Counter
            mTotalRunTime++;
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
            setErrorDisplayID(
                (getErrorDisplayID() & ~127) | (mWrench ? 0 : 1)
                    | (mScrewdriver ? 0 : 2)
                    | (mSoftMallet ? 0 : 4)
                    | (mHardHammer ? 0 : 8)
                    | (mSolderingTool ? 0 : 16)
                    | (mCrowbar ? 0 : 32)
                    | (mMachine ? 0 : 64));

            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            setMufflers(aBaseMetaTileEntity.isActive() && mPollution > 0);

            boolean isActive = mMaxProgresstime > 0;

            if (!mMachine || !isActive) {
                deactivateCoilLease();
            }

            if (mMachine && !mCoils.isEmpty() && isActive && coilLease == null) {
                coilLease = GTCoilTracker.activate(this, mCoils);
            }
        } else {
            doActivitySound(getActivitySoundLoop());
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

        if (getRepairStatus() != getIdealStatus()) {
            for (MTEHatchMaintenance tHatch : validMTEList(mMaintenanceHatches)) {
                boolean tDidRepair = false;

                if (tHatch.mAuto) tDidRepair = tHatch.autoMaintainance();

                // For each tool, only if needed, collect the tool flags
                // that this Maintenance Hatch has provided
                if (tHatch.mWrench && !mWrench) tDidRepair = mWrench = true;
                if (tHatch.mScrewdriver && !mScrewdriver) tDidRepair = mScrewdriver = true;
                if (tHatch.mSoftMallet && !mSoftMallet) tDidRepair = mSoftMallet = true;
                if (tHatch.mHardHammer && !mHardHammer) tDidRepair = mHardHammer = true;
                if (tHatch.mSolderingTool && !mSolderingTool) tDidRepair = mSolderingTool = true;
                if (tHatch.mCrowbar && !mCrowbar) tDidRepair = mCrowbar = true;

                tHatch.mWrench = false;
                tHatch.mScrewdriver = false;
                tHatch.mSoftMallet = false;
                tHatch.mHardHammer = false;
                tHatch.mSolderingTool = false;
                tHatch.mCrowbar = false;

                if (tDidRepair) tHatch.onMaintenancePerformed(this);
            }
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
        // Do the same for Smart Input Hatches
        for (ISmartInputHatch smartInputHatch : mSmartInputHatches) {
            shouldCheck |= smartInputHatch.justUpdated();
        }
        if (shouldCheck) return true;

        // Perform more frequent recipe change after the machine just shuts down.
        long timeElapsed = mTotalRunTime - mLastWorkingTick;

        if (timeElapsed >= CHECK_INTERVAL) return (mTotalRunTime + randomTickOffset) % CHECK_INTERVAL == 0;
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
                if (mMaxProgresstime > 0) {
                    incrementProgressTime();

                    if (mProgresstime >= mMaxProgresstime) {
                        if (mOutputItems != null) {
                            addItemOutputs(mOutputItems);
                            mOutputItems = null;
                        }
                        if (mOutputFluids != null) {
                            addFluidOutputs(mOutputFluids);
                            mOutputFluids = null;
                        }
                        outputAfterRecipe();
                        mEfficiency = Math.max(
                            0,
                            Math.min(
                                mEfficiency + mEfficiencyIncrease,
                                getMaxEfficiency(getControllerSlot())
                                    - ((getIdealStatus() - getRepairStatus()) * 1000)));
                        mOutputItems = null;
                        mProgresstime = 0;
                        mMaxProgresstime = 0;
                        if (processingLogic != null) {
                            recipesDone += Math.max(processingLogic.getCurrentParallels(), lastParallel);
                        } else {
                            recipesDone += 1;
                        }
                        mEfficiencyIncrease = 0;
                        mLastWorkingTick = mTotalRunTime;
                        if (aBaseMetaTileEntity.isAllowedToWork()) {
                            checkRecipe();
                        }
                    }
                }
            }
        } else {
            // Check if the machine is enabled in the first place!
            if (aBaseMetaTileEntity.isAllowedToWork()) {

                if (shouldCheckRecipeThisTick(aTick) || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                    || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                    if (checkRecipe()) {
                        markDirty();
                    }
                }
            }
            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
        }
    }

    protected void outputAfterRecipe() {

    }

    protected void incrementProgressTime() {
        mProgresstime++;
    }

    public boolean polluteEnvironment(int aPollutionLevel) {
        final int VENT_AMOUNT = 10_000;
        // Early exit if pollution is disabled
        if (!GTMod.proxy.mPollution) return true;
        mPollution += aPollutionLevel;
        if (mPollution < VENT_AMOUNT) return true;
        if (mMufflerHatches.size() == 0) {
            // No muffler present. Fail.
            return false;
        } else if (mMufflerHatches.size() == 1) {
            // One muffler, use simple method for performance.
            MTEHatchMuffler muffler = mMufflerHatches.get(0);
            if (muffler == null || !muffler.isValid()) {
                // Muffler invalid. Fail.
                mMufflerHatches.remove(0);
                return false;
            } else {
                if (muffler.polluteEnvironment(this, VENT_AMOUNT)) {
                    mPollution -= VENT_AMOUNT;
                } else {
                    // Muffler blocked. Fail.
                    return false;
                }
            }
        } else {
            // Multiple mufflers, split pollution output evenly between all of them.
            int mufflerCount = 0;
            int ventAmount = 0; // Allow venting of up to VENT_AMOUNT of pollution per muffler.
            for (MTEHatchMuffler muffler : validMTEList(mMufflerHatches)) {
                mufflerCount++;
                if (ventAmount + VENT_AMOUNT <= mPollution) {
                    ventAmount += VENT_AMOUNT;
                }
            }
            // This might lose some small amount of pollution due to rounding, this is fine.
            ventAmount /= mufflerCount;

            for (MTEHatchMuffler muffler : validMTEList(mMufflerHatches)) {
                if (muffler.polluteEnvironment(this, ventAmount)) {
                    mPollution -= ventAmount;
                } else {
                    // Muffler blocked. Fail.
                    return false;
                }
            }
        }
        return mPollution < VENT_AMOUNT;
    }

    /**
     * How much pollution this outputs to the environment. 100 = outputs all, 0 = outputs none. Calculated as an average
     * across all muffler hatches.
     *
     * @return Fraction of pollution output to the environment (out of 100).
     */
    public int getAveragePollutionPercentage() {
        int pollutionPercent = 0;
        int mufflerCount = 0;
        for (MTEHatchMuffler muffler : validMTEList(mMufflerHatches)) {
            pollutionPercent += muffler.calculatePollutionReduction(100);
            mufflerCount++;
        }
        if (mufflerCount > 0) {
            pollutionPercent /= mufflerCount;
        } else {
            pollutionPercent = 100;
        }
        return pollutionPercent;
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
                    GTUtility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
            }
            case INTERRUPT_SOUND_INDEX -> GTUtility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            if (getProcessStartSound() != null)
                GTUtility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void doActivitySound(SoundResource activitySound) {
        if (getBaseMetaTileEntity().isActive() && activitySound != null && !getBaseMetaTileEntity().isMuffled()) {
            if (activitySoundLoop == null) {
                activitySoundLoop = new GTSoundLoop(
                    activitySound.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop.setFadeMe(true);
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
    protected SoundResource getActivitySoundLoop() {
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
     * Check the ItemStack in the controller slot.
     *
     * @param aStack the ItemStack in the controller slot.
     * @return {@code true} if the item is valid for this machine or the machine doesn't have restrictions on the
     *         controller slot.
     */
    public boolean isCorrectMachinePart(@Nullable ItemStack aStack) {
        return true;
    }

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
     * Initializes processing logic for use. Unlike {@link #createProcessingLogic}, this method is called every time
     * checking for recipes.
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
     * Initializes processing logic for use, specifically for power-related parameters. Unlike
     * {@link #createProcessingLogic}, this method is called every time checking for recipes.
     */
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        boolean useSingleAmp = !debugEnergyPresent && mEnergyHatches.size() == 1 && mExoticEnergyHatches.isEmpty();
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(useSingleAmp ? 1 : getMaxInputAmps());
        logic.setAmperageOC(true);
    }

    protected boolean supportsCraftingMEBuffer() {
        return true;
    }

    /**
     * Iterates over hatches and tries to find recipe. Assume {@link #processingLogic} is already set up for use. If
     * return value is successful, inputs are consumed.
     */
    @Nonnull
    protected CheckRecipeResult doCheckRecipe() {
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;

        // check crafting input hatches first
        for (IDualInputHatch dualInputHatch : mDualInputHatches) {
            ItemStack[] sharedItems = dualInputHatch.getSharedItems();
            for (var it = dualInputHatch.inventories(); it.hasNext();) {
                IDualInputInventory slot = it.next();

                if (!slot.isEmpty()) {
                    // try to cache the possible recipes from pattern
                    if (slot instanceof IDualInputInventoryWithPattern withPattern) {
                        if (!processingLogic.tryCachePossibleRecipesFromPattern(withPattern)) {
                            // move on to next slots if it returns false, which means there is no possible recipes with
                            // given pattern.
                            continue;
                        }
                    }

                    processingLogic.setInputItems(ArrayUtils.addAll(sharedItems, slot.getItemInputs()));
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

        result = checkRecipeForCustomHatches(result);
        if (result.wasSuccessful()) {
            return result;
        }

        // Use hatch colors if any; fallback to color 1 otherwise.
        short hatchColors = getHatchColors();
        boolean doColorChecking = hatchColors != 0;
        if (!doColorChecking) hatchColors = 0b1;

        for (byte color = 0; color < (doColorChecking ? 16 : 1); color++) {
            if (isColorAbsent(hatchColors, color)) continue;
            processingLogic.setInputFluids(getStoredFluidsForColor(Optional.of(color)));
            if (isInputSeparationEnabled()) {
                if (mInputBusses.isEmpty()) {
                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) return foundResult;
                    // Recipe failed in interesting way, so remember that and continue searching
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) result = foundResult;
                } else {
                    for (MTEHatchInputBus bus : mInputBusses) {
                        if (bus instanceof MTEHatchCraftingInputME) continue;
                        byte busColor = bus.getColor();
                        if (busColor != -1 && busColor != color) continue;
                        List<ItemStack> inputItems = new ArrayList<>();
                        for (int i = bus.getSizeInventory() - 1; i >= 0; i--) {
                            ItemStack stored = bus.getStackInSlot(i);
                            if (stored != null) inputItems.add(stored);
                        }
                        if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                            inputItems.add(getControllerSlot());
                        }
                        processingLogic.setInputItems(inputItems);
                        CheckRecipeResult foundResult = processingLogic.process();
                        if (foundResult.wasSuccessful()) return foundResult;
                        // Recipe failed in interesting way, so remember that and continue searching
                        if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) result = foundResult;
                    }
                }
            } else {
                List<ItemStack> inputItems = getStoredInputsForColor(Optional.of(color));
                if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                    inputItems.add(getControllerSlot());
                }
                processingLogic.setInputItems(inputItems);
                CheckRecipeResult foundResult = processingLogic.process();
                if (foundResult.wasSuccessful()) return foundResult;
                // Recipe failed in interesting way, so remember that
                if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) result = foundResult;
            }
        }
        return result;
    }

    /**
     * Returns whether the given color is absent in the hatch color bitmask.
     *
     * @param hatchColors bitmask of present colors (one bit per color index).
     * @param color       color index to check (0–15).
     * @return {@code true} if the color is absent, {@code false} if present.
     */
    private static boolean isColorAbsent(short hatchColors, byte color) {
        return (hatchColors & (1 << color)) == 0;
    }

    /**
     * Builds a bitmask of all input bus and hatch colors. Each set bit in the result marks a present color index.
     *
     * @return bitmask of used color indices.
     */
    private short getHatchColors() {
        short hatchColors = 0;

        for (var bus : mInputBusses) hatchColors |= (short) (1 << bus.getColor());
        for (var hatch : mInputHatches) hatchColors |= (short) (1 << hatch.getColor());

        if (this instanceof MTESteamMultiBase<?>steamMultiBase) {
            for (var bus : steamMultiBase.mSteamInputs) hatchColors |= (short) (1 << bus.getColor());
            for (var hatch : steamMultiBase.mSteamInputFluids) hatchColors |= (short) (1 << hatch.getColor());
        }

        return hatchColors;
    }

    /**
     * Override to perform additional checkRecipe logic. It gets called after CRIBs and before ordinary hatches.
     *
     * @param lastResult Last result of checkRecipe. It might contain interesting info about failure, so don't blindly
     *                   overwrite it. Refer to {@link #doCheckRecipe} for how to handle it.
     * @return Result of the checkRecipe.
     */
    @Nonnull
    protected CheckRecipeResult checkRecipeForCustomHatches(CheckRecipeResult lastResult) {
        return lastResult;
    }

    /**
     * Performs additional check for {@link #processingLogic} after all the calculations are done. As many as checks
     * should be done inside of custom {@link ProcessingLogic}, which you can specify with
     * {@link #createProcessingLogic()}, because when this method is called, inputs might have been already consumed.
     * However, certain checks cannot be done like that; Checking energy overflow should be suppressed for long-power
     * machines for example.
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
     * Called after {@link #doCheckRecipe} and {@link #postCheckRecipe} being successful. Override to set energy usage
     * for this machine.
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
     * Gets the maximum efficiency of this machine with current state.
     * <p>
     * The ItemStack argument is supposed to be the one in the controller slot, but in some implementation, it directly
     * gives {@code null}.
     *
     * @param aStack the ItemStack in the controller slot or null.
     * @return the maximum efficiency in the range of 0 to 10,000.
     */
    @Range(from = 0, to = 10000)
    public int getMaxEfficiency(@Nullable ItemStack aStack) {
        return 10000;
    }

    /**
     * Gets the pollution this Device outputs to a Muffler per tick (10000 = one Pullution Block)
     *
     * @param aStack what is in controller
     */
    public int getPollutionPerTick(ItemStack aStack) {
        return getPollutionPerSecond(aStack) / 20;
    }

    /**
     * Gets the pollution produced per second by this multiblock, default to 0. Override this with its actual value in
     * the code of the multiblock.
     *
     * This returns the unmodified raw pollution value, not the one after muffler discounts.
     *
     * @param aStack what is in controller
     */
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    /**
     * Gets the factor value to damage the ItemStack in the controller slot.
     * <p>
     * This function will be called only if the ItemStack is {@link MetaGeneratedTool01}, and the actual applied damage
     * value is multiplied by
     * <a href="https://www.wolframalpha.com/input?i=plot+min%28x%2F5%2C+x%5E%280.6%29%29+range+0+to+512">a formula</a>
     * calculated with {@link #damageFactorLow} and {@link #damageFactorHigh}. See {@link #doRandomMaintenanceDamage()}
     * for the details.
     *
     * @param aStack the ItemStack in the controller slot.
     * @return the factor value to damage the ItemStack in the controller slot.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    public int getDamageToComponent(@Nullable ItemStack aStack) {
        return 0;
    }

    /**
     * @deprecated no longer being called.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

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
        mLastWorkingTick = mTotalRunTime;
        mOutputItems = null;
        mOutputFluids = null;
        mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        IGregTechTileEntity igte = getBaseMetaTileEntity();
        igte.disableWorking();
        igte.setShutDownReason(reason);
        igte.setShutdownStatus(true);
        if (reason.wasCritical()) {
            sendSound(INTERRUPT_SOUND_INDEX);
        }
        if (makePowerfailEvents && reason == ShutDownReasonRegistry.POWER_LOSS) {
            GTMod.proxy.powerfailTracker.createPowerfailEvent(igte);
        }
    }

    public int getRepairStatus() {
        return (mWrench ? 1 : 0) + (mScrewdriver ? 1 : 0)
            + (mSoftMallet ? 1 : 0)
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
        if (!isCorrectMachinePart(getControllerSlot())) {
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
                if (mInventory[1].getItem() instanceof MetaGeneratedTool01 metaGeneratedTool) {
                    metaGeneratedTool.doDamage(
                        mInventory[1],
                        (long) getDamageToComponent(getControllerSlot()) * (long) Math.min(
                            Math.abs(mEUt) / this.damageFactorLow,
                            Math.pow(Math.abs(mEUt), this.damageFactorHigh)));
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
            case 2 -> mSoftMallet = false;
            case 3 -> mHardHammer = false;
            case 4 -> mSolderingTool = false;
            case 5 -> mCrowbar = false;
        }
    }

    public void explodeMultiblock() {

        GTLog.exp.println(
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

        Pollution.addPollution(getBaseMetaTileEntity(), GTMod.proxy.mPollutionOnExplosion);
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
        if (!mDynamoHatches.isEmpty()) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long totalOutput = 0;
        long aFirstVoltageFound = -1;
        boolean aFoundMixedDynamos = false;
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
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
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
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
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) rVoltage += tHatch.getBaseMetaTileEntity()
            .getInputVoltage();
        return rVoltage;
    }

    public long getAverageInputVoltage() {
        return ExoticEnergyInputHelper.getAverageInputVoltageMulti(mEnergyHatches);
    }

    public long getMaxInputAmps() {
        return ExoticEnergyInputHelper.getMaxWorkingInputAmpsMulti(mEnergyHatches);
    }

    public long getMaxInputEu() {
        return ExoticEnergyInputHelper.getTotalEuMulti(mEnergyHatches);
    }

    /**
     * Sums up max input EU/t of energy hatches, amperage included.
     */
    public long getMaxInputPower() {
        long eut = 0;
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
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
        if (!mEnergyHatches.isEmpty()) {
            rTier = mEnergyHatches.get(0)
                .getInputTier();
            for (int i = 1; i < mEnergyHatches.size(); i++) {
                if (mEnergyHatches.get(i)
                    .getInputTier() != rTier) return 0;
            }
        }

        return rTier;
    }

    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0) return true;

        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            long tDrain = Math.min(
                tHatch.getBaseMetaTileEntity()
                    .getStoredEU(),
                aEU);
            tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(tDrain, false);// basicly copied from
                                                          // ExoticEnergyInputHelper, makes
                                                          // machine
                                                          // use all hatches for power
            aEU -= tDrain;

            if (aEU <= 0) return true;
        }

        return false;
    }

    protected static boolean dumpFluid(List<MTEHatchOutput> aOutputHatches, FluidStack copiedFluidStack,
        boolean restrictiveHatchesOnly) {
        for (MTEHatchOutput tHatch : validMTEList(aOutputHatches)) {
            if (restrictiveHatchesOnly && tHatch.mMode == 0) {
                continue;
            }
            if (!tHatch.canStoreFluid(copiedFluidStack)) continue;

            if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                if (!tMEHatch.canFillFluid()) continue;
            }

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

    protected void addFluidOutputs(FluidStack[] outputFluids) {
        for (FluidStack outputFluidStack : outputFluids) {
            addOutput(outputFluidStack);
        }
    }

    public boolean depleteInput(FluidStack aLiquid) {
        return depleteInput(aLiquid, false);
    }

    public boolean depleteInput(FluidStack aLiquid, boolean simulate) {
        if (aLiquid == null) return false;
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
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

    /**
     * Ejects a stack. Items are only ejected when the whole stack can be ejected into output busses.
     *
     * @param stack The stack to eject. Ejected items are subtracted from this stack.
     * @return True when the whole stack was ejected, false otherwise.
     */
    public boolean addOutputAtomic(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return false;

        int initial = stack.stackSize;

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);
        ejectionHelper.ejectStack(stack);

        if (stack.stackSize == 0) {
            ejectionHelper.commit();
            return true;
        } else {
            // Restore the original stack size because we didn't end up doing anything.
            stack.stackSize = initial;
            return false;
        }
    }

    /**
     * Ejects a stack as well as it can. Items are ejected regardless of whether the whole stack can fit.
     *
     * @param stack The stack to eject. Ejected items are subtracted from this stack.
     */
    public void addOutputPartial(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return;

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);
        ejectionHelper.ejectStack(stack);
        ejectionHelper.commit();
    }

    /**
     * Adds items to this multi's output busses. Voids anything that could not fit.
     *
     * @param outputItems The items to eject. Not modified.
     * @return True when all items were ejected, false otherwise.
     */
    public boolean addItemOutputs(ItemStack[] outputItems) {
        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);
        int ejected = ejectionHelper.ejectItems(Arrays.asList(outputItems), 1);
        ejectionHelper.commit();

        return ejected == 1;
    }

    public boolean depleteInput(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GTUtility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            final IGregTechTileEntity baseMetaTileEntity = tHatch.getBaseMetaTileEntity();
            ItemStack stackInFirstSlot = baseMetaTileEntity.getStackInSlot(0);
            if (GTUtility.areStacksEqual(aStack, stackInFirstSlot)) {
                if (stackInFirstSlot.stackSize >= aStack.stackSize) {
                    baseMetaTileEntity.decrStackSize(0, aStack.stackSize);
                    return true;
                }
            }
        }
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            tHatch.mRecipeMap = getRecipeMap();
            final IGregTechTileEntity baseMetaTileEntity = tHatch.getBaseMetaTileEntity();
            for (int i = baseMetaTileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack stackInSlot = baseMetaTileEntity.getStackInSlot(i);
                if (GTUtility.areStacksEqual(aStack, stackInSlot)) {
                    if (stackInSlot.stackSize >= aStack.stackSize) {
                        baseMetaTileEntity.decrStackSize(i, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<FluidStack> getStoredFluids() {
        return getStoredFluidsForColor(Optional.empty());
    }

    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        ArrayList<FluidStack> rList = new ArrayList<>();
        Map<Fluid, FluidStack> inputsFromME = new HashMap<>();
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            byte hatchColor = tHatch.getColor();
            if (color.isPresent() && hatchColor != -1 && hatchColor != color.get()) continue;
            setHatchRecipeMap(tHatch);
            if (tHatch instanceof MTEHatchMultiInput multiInputHatch) {
                for (FluidStack tFluid : multiInputHatch.getStoredFluid()) {
                    if (tFluid != null) {
                        rList.add(tFluid);
                    }
                }
            } else if (tHatch instanceof MTEHatchInputME meHatch) {
                for (FluidStack fluidStack : meHatch.getStoredFluids()) {
                    if (fluidStack != null) {
                        // Prevent the same fluid from different ME hatches from being recognized
                        inputsFromME.put(fluidStack.getFluid(), fluidStack);
                    }
                }
            } else if (tHatch instanceof MTEHatchInputDebug debugHatch) {
                for (FluidStack fluid : debugHatch.getFluidList()) {
                    if (fluid != null) {
                        FluidStack stack = fluid.copy();
                        stack.amount = Integer.MAX_VALUE;
                        rList.add(stack);
                    }
                }
            } else {
                FluidStack fillableStack = tHatch.getFillableStack();
                if (fillableStack != null) {
                    rList.add(fillableStack);
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
    public boolean drain(MTEHatch hatch, FluidStack fluid, boolean doDrain) {
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

        if (hatch instanceof MTEHatchInput tHatch && tHatch.isValid()) {
            if (tHatch instanceof MTEHatchInputME meHatch) {
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
        return getStoredInputsForColor(Optional.empty());
    }

    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            if (tHatch instanceof MTEHatchCraftingInputME) {
                continue;
            }
            byte busColor = tHatch.getColor();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof MTEHatchInputBusME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        ItemStack stackInSlot1 = getStackInSlot(1);
        if (stackInSlot1 != null && stackInSlot1.getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(stackInSlot1);
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
                rList.addAll(Arrays.asList(dualInputHatch.getAllItems()));
            }
        }

        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            if (tHatch instanceof MTEHatchCraftingInputME) {
                continue;
            }
            tHatch.mRecipeMap = getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof MTEHatchInputBusME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        ItemStack stackInSlot1 = getStackInSlot(1);
        if (stackInSlot1 != null && stackInSlot1.getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(stackInSlot1);
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
        return rList;
    }

    public Map<GTUtility.ItemId, ItemStack> getStoredInputsFromME() {
        Map<GTUtility.ItemId, ItemStack> inputsFromME = new Object2ReferenceOpenHashMap<>();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) {
            if (tHatch instanceof MTEHatchInputBusME meBus) {
                for (int i = meBus.getSizeInventory() - 1; i >= 0; i--) {
                    ItemStack itemStack = meBus.getStackInSlot(i);
                    if (itemStack != null) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    }
                }
            }
        }
        return inputsFromME;
    }

    public Map<Fluid, FluidStack> getStoredFluidsFromME() {
        Map<Fluid, FluidStack> fluidsFromME = new Reference2ReferenceOpenHashMap<>();
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            if (tHatch instanceof MTEHatchInputME meHatch) {
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
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) tHatch.updateSlots();
        for (MTEHatchInputBus tHatch : validMTEList(mInputBusses)) tHatch.updateSlots();
    }

    public void startRecipeProcessing() {
        mDualInputHatches.removeIf(mte -> mte == null || !((MetaTileEntity) mte).isValid());

        for (MTEHatchInputBus hatch : validMTEList(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        for (MTEHatchInput hatch : validMTEList(mInputHatches)) {
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

    public void endRecipeProcessing() {
        for (MTEHatchInputBus hatch : validMTEList(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
        for (MTEHatchInput hatch : validMTEList(mInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof IDualInputHatch hatch) {
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            if (hatch instanceof IDualInputHatchWithPattern withPattern) {
                withPattern.setProcessingLogic(processingLogic);
            }
            return mDualInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof ISmartInputHatch hatch) {
            // Only add them to be iterated if enabled for performance reasons
            if (hatch.doFastRecipeCheck()) {
                mSmartInputHatches.add(hatch);
            }
        }
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            setHatchRecipeMap(hatch);
            return mInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchInputBus hatch) {
            hatch.mRecipeMap = getRecipeMap();
            return mInputBusses.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput hatch) return mOutputHatches.add(hatch);
        if (aMetaTileEntity instanceof MTEHatchOutputBus hatch) return mOutputBusses.add(hatch);
        if (aMetaTileEntity instanceof MTEHatchEnergy hatch) return mEnergyHatches.add(hatch);
        if (aMetaTileEntity instanceof MTEHatchDynamo hatch) return mDynamoHatches.add(hatch);
        if (aMetaTileEntity instanceof MTEHatchMaintenance hatch) {

            if (hatch instanceof MTEHatchDroneDownLink droneDownLink) {
                droneDownLink.registerMachineController(this);
            }

            return mMaintenanceHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchMuffler hatch) return mMufflerHatches.add(hatch);
        return false;
    }

    public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchMaintenance hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());

            if (hatch instanceof MTEHatchDroneDownLink droneDownLink) {
                droneDownLink.registerMachineController(this);
            }

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
        if (aMetaTileEntity instanceof MTEHatchEnergy hatch) {
            if (aMetaTileEntity instanceof MTEHatchEnergyDebug) {
                debugEnergyPresent = true;
            }
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mEnergyHatches.add(hatch);
        }
        return false;
    }

    public boolean addMultiAmpEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchEnergyMulti hatch && hatch.getHatchType() == 1) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mExoticEnergyHatches.add(hatch);
        }
        return false;
    }

    public boolean addExoticEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch && ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
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
        if (aMetaTileEntity instanceof MTEHatchDynamo hatch) {
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
        if (aMetaTileEntity instanceof MTEHatchMuffler hatch) {
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
        if (aMetaTileEntity instanceof MTEHatchSteamBusInput) return false;
        if (aMetaTileEntity instanceof ISmartInputHatch hatch) {
            mSmartInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchInputBus hatch) {
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
        if (aMetaTileEntity instanceof MTEHatchSteamBusOutput) return false;
        if (aMetaTileEntity instanceof MTEHatchOutputBus hatch) {
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
        if (aMetaTileEntity instanceof ISmartInputHatch hatch) {
            mSmartInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
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
        if (aMetaTileEntity instanceof MTEHatchOutput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mOutputHatches.add(hatch);
        }
        return false;
    }

    protected void setHatchRecipeMap(MTEHatchInput hatch) {
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
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            final IGregTechTileEntity baseMetaTileEntity = tHatch.getBaseMetaTileEntity();
            storedEnergy += baseMetaTileEntity.getStoredEU();
            maxEnergy += baseMetaTileEntity.getEUCapacity();
        }

        return new String[] {
            /* 1 */ translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + formatNumber(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %",
            /* 7 */ translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    public Map<String, String> getInfoMap() {
        long energy = 0, maxEnergy = 0, maxEnergyUsage = 0, minEnergyTier = Long.MAX_VALUE;

        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            IGregTechTileEntity energyHatch = tHatch.getBaseMetaTileEntity();
            energy += energyHatch.getStoredEU();
            maxEnergy += energyHatch.getEUCapacity();
            maxEnergyUsage += energyHatch.getInputAmperage() * energyHatch.getInputVoltage();
            minEnergyTier = Math.min(minEnergyTier, energyHatch.getInputVoltage());
        }

        minEnergyTier = minEnergyTier == Long.MAX_VALUE ? 0 : minEnergyTier;

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("progressTime", Integer.toString(mProgresstime));
        infoMap.put("maxProgressTime", Integer.toString(mMaxProgresstime));
        infoMap.put("energy", Long.toString(energy));
        infoMap.put("maxEnergy", Long.toString(maxEnergy));
        infoMap.put("energyUsage", Long.toString(getActualEnergyUsage()));
        infoMap.put("maxEnergyUsage", Long.toString(maxEnergyUsage));
        infoMap.put("minEnergyTier", Long.toString(minEnergyTier));
        infoMap.put("maintenanceIssues", Integer.toString(getIdealStatus() - getRepairStatus()));
        infoMap.put("energyEfficiency", Double.toString(mEfficiency / 10_000F));
        infoMap.put("pollution", Double.toString(getAveragePollutionPercentage() / 100F));

        return infoMap;
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

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + translateToLocalFormatted("GT5U.waila.multiblock.status.incomplete") + RESET);
        }
        String efficiency = RESET
            + translateToLocalFormatted("GT5U.waila.multiblock.status.efficiency", tag.getFloat("efficiency"));
        if (tag.getBoolean("hasProblems")) {
            currentTip.add(RED + translateToLocal("GT5U.waila.multiblock.status.has_problem") + efficiency);
        } else if (!tag.getBoolean("incompleteStructure")) {
            currentTip.add(GREEN + translateToLocal("GT5U.waila.multiblock.status.running_fine") + efficiency);
        }

        boolean isActive = tag.getBoolean("isActive");

        if (isActive) {
            long energyTier = tag.getLong("energyTier");
            long actualEnergyUsage = tag.getLong("energyUsage");
            if (energyTier > 0) {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        translateToLocalFormatted(
                            "GT5U.waila.energy.use_with_amperage",
                            formatNumber(actualEnergyUsage),
                            GTUtility.getAmperageForTier(actualEnergyUsage, (byte) energyTier),
                            GTUtility.getColoredTierNameFromTier((byte) energyTier)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        translateToLocalFormatted(
                            "GT5U.waila.energy.produce_with_amperage",
                            formatNumber(-actualEnergyUsage),
                            GTUtility.getAmperageForTier(-actualEnergyUsage, (byte) energyTier),
                            GTUtility.getColoredTierNameFromTier((byte) energyTier)));
                }
            } else {
                if (actualEnergyUsage > 0) {
                    currentTip.add(
                        translateToLocalFormatted(
                            "GT5U.waila.energy.use",
                            formatNumber(actualEnergyUsage),
                            GTUtility.getColoredTierNameFromVoltage(actualEnergyUsage)));
                } else if (actualEnergyUsage < 0) {
                    currentTip.add(
                        translateToLocalFormatted(
                            "GT5U.waila.energy.produce",
                            formatNumber(-actualEnergyUsage),
                            GTUtility.getColoredTierNameFromVoltage(-actualEnergyUsage)));
                }
            }
        }

        boolean isLockedToRecipe = tag.getBoolean("isLockedToRecipe");
        String lockedRecipe = tag.getString("lockedRecipeName");

        if (!isActive && isLockedToRecipe && !lockedRecipe.isEmpty()) {
            // Display locked recipe when the machine is idle
            currentTip.add(translateToLocal("GT5U.waila.multiblock.status.locked_recipe"));
            String[] lines = lockedRecipe.split("\n");
            currentTip.addAll(Arrays.asList(lines));
        } else if (isActive) {
            int outputItemLength = tag.getInteger("outputItemLength");
            int outputFluidLength = tag.getInteger("outputFluidLength");
            int totalOutputs = outputItemLength + outputFluidLength;

            if (totalOutputs > 0) {
                currentTip.add(translateToLocal("GT5U.waila.producing"));
                if (isLockedToRecipe) {
                    currentTip.add(translateToLocal("GT5U.waila.multiblock.status.locked_recipe"));
                }
                for (int i = 0; i < min(3, outputItemLength); i++) {
                    currentTip.add(
                        "  " + LocSer.decodeFromBytes(tag.getByteArray("outputItemB" + i))
                            .display() + " x " + formatNumber(tag.getInteger("outputItemCount" + i)));
                }
                for (int i = 0; i < min(3 - outputItemLength, outputFluidLength); i++) {
                    currentTip.add(
                        "  " + LocSer.decodeFromBytes(tag.getByteArray("outputFluidB" + i))
                            .display() + " x " + formatNumber(tag.getInteger("outputFluidCount" + i)) + "L");
                }
                if (totalOutputs > 3) {
                    currentTip.add(
                        translateToLocalFormatted("GT5U.waila.producing.andmore", formatNumber((totalOutputs - 3))));
                }
            }
        }
        currentTip.add(
            GTWaila.getMachineProgressString(
                isActive,
                tag.getBoolean("isAllowedToWork"),
                tag.getInteger("maxProgress"),
                tag.getInteger("progress")));
        // Show ns on the tooltip
        if (GTMod.proxy.wailaAverageNS && tag.hasKey("averageNS")) {
            int tAverageTime = tag.getInteger("averageNS");
            currentTip
                .add(translateToLocalFormatted("GT5U.waila.multiblock.status.cpu_load", formatNumber(tAverageTime)));
        }

        if (tag.getInteger("maxParallelRecipes") > 1) {
            currentTip.add(
                StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                    + EnumChatFormatting.WHITE
                    + tag.getInteger("maxParallelRecipes"));
        }

        if (tag.hasKey("mode")) {
            currentTip.add(
                StatCollector.translateToLocal("GT5U.multiblock.runningMode") + " "
                    + EnumChatFormatting.WHITE
                    + tag.getString("mode")
                    + EnumChatFormatting.RESET);
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
        tag.setBoolean("incompleteStructure", (getErrorDisplayID() & 64) != 0);
        tag.setInteger("maxParallelRecipes", getMaxParallelRecipes());
        tag.setBoolean("isLockedToRecipe", isRecipeLockingEnabled());
        SingleRecipeCheck lockedRecipe = getSingleRecipeCheck();
        tag.setString(
            "lockedRecipeName",
            lockedRecipe != null ? getDisplayString(lockedRecipe.getRecipe(), false, true, false, true) : "");

        if (mOutputItems != null) {
            int index = 0;
            for (ItemStack stack : mOutputItems) {
                if (stack == null) continue;
                tag.setByteArray(
                    "outputItemB" + index,
                    LocSer.itemStackName(stack)
                        .encodeToBytes());
                tag.setInteger("outputItemCount" + index, stack.stackSize);
                index++;
            }
            if (index != 0) tag.setInteger("outputItemLength", index);
        }
        if (mOutputFluids != null) {
            int index = 0;
            for (FluidStack stack : mOutputFluids) {
                if (stack == null) continue;
                tag.setByteArray(
                    "outputFluidB" + index,
                    LocSer.fluidStackName(stack)
                        .encodeToBytes());
                tag.setInteger("outputFluidCount" + index, stack.amount);
                index++;
            }
            if (index != 0) tag.setInteger("outputFluidLength", index);
        }

        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("isActive", tileEntity.isActive());
            tag.setBoolean("isAllowedToWork", tileEntity.isAllowedToWork());
            if (tileEntity.isActive()) {
                if (mEUt < 0) tag.setLong("energyUsage", getActualEnergyUsage());
                else tag.setLong("energyUsage", (long) -mEUt * mEfficiency / 10000);
                tag.setLong("energyTier", getInputVoltageTier());
            }
        }

        final GTClientPreference preference = GTMod.proxy.getClientPreference(player.getUniqueID());
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

    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected void setMufflers(boolean state) {
        final int size = mMufflerHatches.size();
        for (int i = 0; i < size; i++) {
            final MTEHatchMuffler muffler = mMufflerHatches.get(i);
            final IGregTechTileEntity tile = muffler.getBaseMetaTileEntity();
            if (tile == null || tile.isDead()) continue;
            tile.setActive(state);
        }
    }

    @Override
    protected void onGuiOpened(EntityPlayer player) {
        super.onGuiOpened(player);

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte != null && igte.getLastShutDownReason() == ShutDownReasonRegistry.POWER_LOSS) {
            GTMod.proxy.powerfailTracker.removePowerfailEvents(igte);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        // Deactivate mufflers
        setMufflers(false);

        deactivateCoilLease();

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte != null && igte.getLastShutDownReason() == ShutDownReasonRegistry.POWER_LOSS) {
            GTMod.proxy.powerfailTracker.removePowerfailEvents(igte);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();

        deactivateCoilLease();
    }

    protected void deactivateCoilLease() {
        if (coilLease != null) {
            GTCoilTracker.deactivate(coilLease);
            coilLease = null;
        }
    }

    public List<MTEHatch> getExoticEnergyHatches() {
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
     * Checks if all the item / fluid outputs of the recipe can be outputted to the buses / hatches. If void protection
     * is enabled, it also checks for {@link #protectsExcessItem()} and {@link #protectsExcessFluid()}, so you don't
     * need to call them along with this method.
     * <p>
     * If you're using {@link ParallelHelper}, it will handle void protection and return 0 parallel if all the output
     * cannot be dumped into buses / hatches. In that case you won't use this method.
     */
    protected boolean canOutputAll(@Nonnull GTRecipe recipe) {
        return canOutputAll(recipe.mOutputs, recipe.mFluidOutputs);
    }

    /**
     * Checks if all the items can be outputted to the output buses. If void protection is enabled, it also checks for
     * {@link #protectsExcessItem()}, so you don't need to call it along with this method.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canOutputAll(ItemStack[] items) {
        return canOutputAll(items, null);
    }

    /**
     * Checks if all the fluids can be outputted to the output hatches. If void protection is enabled, it also checks
     * for {@link #protectsExcessFluid()}, so you don't need to call it along with this method.
     */
    protected boolean canOutputAll(FluidStack[] fluids) {
        return canOutputAll(null, fluids);
    }

    /**
     * Checks if all the items / fluids can be outputted to output buses / hatches. If void protection is enabled, it
     * also checks for {@link #protectsExcessItem()} and {@link #protectsExcessFluid()}, so you don't need to call them
     * along with this method.
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
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return baseMetaTileEntity != null && baseMetaTileEntity.isAllowedToWork();
    }

    @Override
    public void disableWorking() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.disableWorking();
        }
    }

    @Override
    public void enableWorking() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.enableWorking();
        }
    }

    @Override
    public boolean isMuffled() {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            return baseMetaTileEntity.isMuffled();
        }
        return false;
    }

    @Override
    public void setMuffled(boolean value) {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity != null) {
            baseMetaTileEntity.setMuffler(value);
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
    public Pos2d getStructureUpdateButtonPos() {
        return new Pos2d(174, 130);
    }

    @Override
    public int getStructureUpdateTime() {
        return mUpdate;
    }

    @Override
    public void setStructureUpdateTime(int time) {
        mUpdate = time;
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
    public List<IOutputBus> getOutputBusses() {
        List<IOutputBus> output = new ArrayList<>(mOutputBusses.size());

        for (int i = 0, mOutputBussesSize = mOutputBusses.size(); i < mOutputBussesSize; i++) {
            MTEHatchOutputBus outputBus = mOutputBusses.get(i);

            if (outputBus.isValid()) output.add(outputBus);
        }

        return output;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        ArrayList<MTEHatchOutput> totalHatches = new ArrayList<>(filterValidMTEs(mOutputHatches));
        totalHatches.removeIf(hatch -> hatch instanceof MTEHatchVoid && !hatch.isFluidLocked());
        return totalHatches;
    }

    /**
     * Util method for DT-like structure to collect list of output hatches.
     */
    protected <T extends MTEHatchOutput> List<? extends IFluidStore> getFluidOutputSlotsByLayer(FluidStack[] toOutput,
        List<List<T>> hatchesByLayer) {
        List<IFluidStore> ret = new ArrayList<>();
        for (int i = 0; i < toOutput.length; i++) {
            if (i >= hatchesByLayer.size()) {
                break;
            }
            FluidStack fluidOutputForLayer = toOutput[i];
            for (MTEHatchOutput hatch : hatchesByLayer.get(i)) {
                if (!hatch.isValid()) continue;
                if (fluidOutputForLayer != null) {
                    ret.add(new OutputHatchWrapper(hatch, f -> GTUtility.areFluidsEqual(f, fluidOutputForLayer)));
                } else {
                    ret.add(hatch);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean canDumpItemToME(List<GTUtility.ItemId> outputs) {
        List<MTEHatchOutputBusME> meBusses = GTUtility.getMTEsOfType(mOutputBusses, MTEHatchOutputBusME.class);

        for (GTUtility.ItemId output : outputs) {
            boolean handled = false;

            for (MTEHatchOutputBusME busME : meBusses) {
                // If the bus has reached its max capacity, it can't accept anything
                if (!busME.canAcceptItem()) continue;

                // If the bus is unfiltered or is filtered to this item, we can eject the stack fully
                // We don't care about bus ordering here because we're just checking if it's possible
                if (!busME.isFiltered() || busME.isFilteredToItem(output)) {
                    handled = true;
                    break;
                }
            }

            if (!handled) return false;
        }

        return true;
    }

    @Override
    public boolean canDumpFluidToME() {
        for (IFluidStore tHatch : getFluidOutputSlots(GTValues.emptyFluidStackArray)) {
            if (tHatch instanceof MTEHatchOutputME) {
                if (((MTEHatchOutputME) tHatch).isFluidLocked()) {
                    return false;
                }

                if (((MTEHatchOutputME) tHatch).canAcceptFluid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Do not use this method as a supplier to ProcessingLogic, use getTrueParallel()
     *
     * @return The absolute maximum number of parallels possible right now.
     */
    public int getMaxParallelRecipes() {
        return 1;
    }

    /**
     * This method should be used as a supplier to ProcessingLogic, not getMaxParallelRecipes()
     *
     * @return Get real parallel count based on the maximum and the limit imposed in the power panel. Always returns at
     *         least 1.
     */
    public final int getTrueParallel() {
        return Math.max(
            1,
            alwaysMaxParallel ? getMaxParallelRecipes() : Math.min(getMaxParallelRecipes(), powerPanelMaxParallel));
    }

    public int getmStartUpCheck() {
        return mStartUpCheck;
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

    /**
     * Creates the icon list for this machine. Override this and add the overlays to machineModeIcons in order.
     */
    public void setMachineModeIcons() {

    }

    /**
     * Override this if you are a multi-machine and want a GUI button. You will also want to override
     * setMachineModeIcons(). Override nextMachineMode() if you have more than 2 modes.
     */
    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    @Override
    public int getMachineMode() {
        return machineMode;
    }

    @Override
    public UITexture getMachineModeIcon(int index) {
        if (index > machineModeIcons.size() - 1) return null;
        return machineModeIcons.get(index);
    }

    @Override
    public void setMachineMode(int index) {
        machineMode = index;
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == 0) return 1;
        else return 0;
    }

    @Override
    public Pos2d getMachineModeSwitchButtonPos() {
        return new Pos2d(80, 91);
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
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85));
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        builder.widget(
            inventorySlot.setPos(173, 167)
                .setBackground(GTUITextures.SLOT_DARK_GRAY));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(
            new Scrollable().setVerticalScroll()
                .widget(screenElements)
                .setPos(10, 7)
                .setSize(182, 79));

        if (supportsMachineModeSwitch() && machineModeIcons == null) {
            machineModeIcons = new ArrayList<>(4);
            setMachineModeIcons();
        }
        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createMuffleButton(builder, this.canBeMuffled()))
            .widget(createInputSeparationButton(builder))
            .widget(createModeSwitchButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder))
            .widget(createStructureUpdateButton(builder));

        if (supportsPowerPanel()) {
            builder.widget(createPowerPanelButton(builder));
            buildContext.addSyncedWindow(POWER_PANEL_WINDOW_ID, this::createPowerPanel);
        }
    }

    // Until other features are implemented, this will be the same as supporting parallel.
    @Override
    public boolean supportsPowerPanel() {
        return true;
    }

    // For MUI2 corner column toggling

    public boolean supportsTerminalRightCornerColumn() {
        return true;
    }

    public boolean supportsTerminalLeftCornerColumn() {
        return false;
    }

    public boolean supportsLogo() {
        return true;
    }

    public boolean supportsShutdownReasonHoverable() {
        return true;
    }

    public boolean supportsMaintenanceIssueHoverable() {
        return this.getDefaultHasMaintenanceChecks();
    }

    @Override
    public Pos2d getPowerPanelButtonPos() {
        return new Pos2d(174, 91);
    }

    @Override
    public ModularWindow createPowerPanel(EntityPlayer player) {
        if (getBaseMetaTileEntity().isServerSide()) maxParallel = getMaxParallelRecipes();
        if (alwaysMaxParallel) powerPanelMaxParallel = maxParallel;

        final int w = 120;
        final int h = 130;
        final int parentW = getGUIWidth();
        final int parentH = getGUIHeight();

        ModularWindow.Builder builder = ModularWindow.builder(w, h);

        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(parentW, parentH))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(parentW, parentH), new Size(w, h))
                        .add(w - 3, 0)));

        // Window header
        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + translateToLocal("GT5U.gui.text.power_panel")).setPos(0, 2)
                .setSize(120, 18));

        // Syncing widgets
        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getMaxParallelRecipes, val -> maxParallel = val));
        builder
            .widget(new FakeSyncWidget.IntegerSyncer(() -> powerPanelMaxParallel, val -> powerPanelMaxParallel = val));
        builder.widget(new FakeSyncWidget.BooleanSyncer(() -> alwaysMaxParallel, val -> alwaysMaxParallel = val));
        builder.widget(new FakeSyncWidget.BooleanSyncer(() -> makePowerfailEvents, val -> makePowerfailEvents = val));

        // "Max parallel" header text
        builder.widget(
            TextWidget.localised("GTPP.CC.parallel")
                .setPos(0, 24)
                .setSize(120, 18));

        // Max parallel setter text box
        NumericWidget textField = (NumericWidget) new NumericWidget()
            .setSetter(val -> powerPanelMaxParallel = (int) val)
            .setGetter(() -> powerPanelMaxParallel)
            .setValidator(val -> {
                // This validator sets the bounds for the text box - if "Always use maximum" is active, the bounds
                // are
                // set to (maxParallel, maxParallel). If not, they are set to (1, maxParallel)
                powerPanelMaxParallel = (int) Math
                    .min(maxParallel, Math.max(val, (alwaysMaxParallel ? maxParallel : 1)));
                return powerPanelMaxParallel;
            })
            .setDefaultValue(powerPanelMaxParallel)
            .setScrollValues(1, 4, 64)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .dynamicTooltip(
                () -> Collections.singletonList(
                    alwaysMaxParallel ? translateToLocalFormatted("GT5U.gui.text.lockedvalue", maxParallel)
                        : translateToLocalFormatted("GT5U.gui.text.rangedvalue", 1, maxParallel)))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(72, 18)
            .setPos(12, 40)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);

        builder.widget(textField);
        builder.widget(createMaxParallelCheckBox(textField));

        builder.widget(
            new TextWidget(translateToLocal("GT5U.gui.text.powerfail_events")).setPos(7, 59)
                .setSize(85, 16));
        builder.widget(
            new CheckboxWidget(() -> makePowerfailEvents, (_cb, checked) -> makePowerfailEvents = checked)
                .setPos(92, 59)
                .setSize(16, 16));

        return builder.build();
    }

    public ButtonWidget createMaxParallelCheckBox(NumericWidget textField) {
        Widget button = new CheckboxWidget(() -> alwaysMaxParallel, (_cb, checked) -> {
            textField.notifyTooltipChange();
            if (getBaseMetaTileEntity().isClientSide()) return;
            alwaysMaxParallel = checked;
        }).addTooltip(translateToLocal("GT5U.gui.button.max_parallel"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(92, 41)
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public boolean shouldDisplayCheckRecipeResult() {
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    public boolean supportsInventoryRow() {
        return this.doesBindPlayerInventory();
    }

    public boolean shouldDisplayShutDownReason() {
        return true;
    }

    protected final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public String generateCurrentRecipeInfoString() {
        StringBuffer ret = new StringBuffer(translateToLocal("GT5U.gui.text.progress"));
        ret.append(" ");

        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.format((double) mProgresstime / 20, ret);
        ret.append("s / ");
        numberFormat.format((double) mMaxProgresstime / 20, ret);
        ret.append("s (");
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.format((double) mProgresstime / mMaxProgresstime * 100, ret);
        ret.append("%)");
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        return ret.toString();
    }

    protected Widget generateCurrentRecipeInfoWidget() {
        final DynamicPositionedColumn processingDetails = new DynamicPositionedColumn();

        if (mOutputItems != null) {
            final Map<ItemStack, Long> nameToAmount = new HashMap<>();

            for (ItemStack item : mOutputItems) {
                if (item == null || item.stackSize <= 0) continue;
                nameToAmount.merge(item, (long) item.stackSize, Long::sum);
            }

            final List<Map.Entry<ItemStack, Long>> sortedMap = nameToAmount.entrySet()
                .stream()
                .sorted(
                    Map.Entry.<ItemStack, Long>comparingByValue()
                        .reversed())
                .collect(Collectors.toList());

            for (Map.Entry<ItemStack, Long> entry : sortedMap) {
                Long itemCount = entry.getValue();
                String itemName = entry.getKey()
                    .getDisplayName();
                String itemAmountString = EnumChatFormatting.WHITE + " x "
                    + EnumChatFormatting.GOLD
                    + formatShortenedLong(itemCount)
                    + EnumChatFormatting.WHITE
                    + appendRate(false, itemCount, true);
                String lineText = EnumChatFormatting.AQUA + truncateText(itemName, 40 - itemAmountString.length())
                    + itemAmountString;
                String lineTooltip = EnumChatFormatting.AQUA + itemName + "\n" + appendRate(false, itemCount, false);

                processingDetails.widget(
                    new MultiChildWidget().addChild(
                        new ItemDrawable(
                            entry.getKey()
                                .copy()).asWidget()
                                    .setSize(8, 8)
                                    .setPos(0, 0))
                        .addChild(
                            new TextWidget(lineText).setTextAlignment(Alignment.CenterLeft)
                                .addTooltip(lineTooltip)
                                .setPos(10, 1)));
            }
        }
        if (mOutputFluids != null) {
            final Map<FluidStack, Long> nameToAmount = new HashMap<>();

            for (FluidStack fluid : mOutputFluids) {
                if (fluid == null || fluid.amount <= 0) continue;
                nameToAmount.merge(fluid, (long) fluid.amount, Long::sum);
            }

            final List<Map.Entry<FluidStack, Long>> sortedMap = nameToAmount.entrySet()
                .stream()
                .sorted(
                    Map.Entry.<FluidStack, Long>comparingByValue()
                        .reversed())
                .collect(Collectors.toList());

            for (Map.Entry<FluidStack, Long> entry : sortedMap) {
                Long itemCount = entry.getValue();
                String itemName = entry.getKey()
                    .getLocalizedName();
                String itemAmountString = EnumChatFormatting.WHITE + " x "
                    + EnumChatFormatting.GOLD
                    + formatShortenedLong(itemCount)
                    + "L"
                    + EnumChatFormatting.WHITE
                    + appendRate(false, itemCount, true);
                String lineText = EnumChatFormatting.AQUA + truncateText(itemName, 40 - itemAmountString.length())
                    + itemAmountString;
                String lineTooltip = EnumChatFormatting.AQUA + itemName + "\n" + appendRate(true, itemCount, false);

                processingDetails.widget(
                    new MultiChildWidget().addChild(
                        new FluidDrawable().setFluid(
                            entry.getKey()
                                .getFluid())
                            .asWidget()
                            .setSize(8, 8)
                            .setPos(0, 0))
                        .addChild(
                            new TextWidget(lineText).setTextAlignment(Alignment.CenterLeft)
                                .addTooltip(lineTooltip)
                                .setPos(10, 1)));

            }
        }
        return processingDetails;
    }

    protected String appendRate(boolean isLiquid, Long amount, boolean isFormatShortened) {
        final StringBuffer ret = new StringBuffer();
        final DecimalFormat df = new DecimalFormat("0.00");
        final double progressTime = (double) mMaxProgresstime / 20;
        double perSecond = amount / progressTime;
        double perMinute = perSecond * 60;
        double perHour = perSecond * 3_600;
        double perDay = perSecond * 86_400;

        final String amountText = translateToLocal("GT5U.gui.text.amount") + " ";
        final String perSecondText = translateToLocal("GT5U.gui.text.per_second") + " ";
        final String perMinuteText = translateToLocal("GT5U.gui.text.per_minute") + " ";
        final String perHourText = translateToLocal("GT5U.gui.text.per_hour") + " ";
        final String perDayText = translateToLocal("GT5U.gui.text.per_day") + " ";

        final Function<Double, Double> roundNumber = (number) -> {
            if (Math.abs(number) < 10) {
                return Math.round(number * 100) / 100.0;
            } else {
                return Math.floor(number);
            }
        };

        if (isFormatShortened) {
            ret.append(" (");
            ret.append(EnumChatFormatting.GRAY);
            ret.append(perSecond > 1 ? formatShortenedLong((long) perSecond) : df.format(perSecond));
            ret.append("/s");
            ret.append(EnumChatFormatting.WHITE);
            ret.append(")");
        } else {
            ret.append(EnumChatFormatting.RESET);
            ret.append(
                amountText + EnumChatFormatting.GOLD
                    + formatNumber(amount)
                    + (isLiquid ? "L" : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perSecondText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perSecond))
                    + (isLiquid ? "L" : "")
                    + (perSecond > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perSecond)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perMinuteText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perMinute))
                    + (isLiquid ? "L" : "")
                    + (perMinute > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perMinute)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perHourText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perHour))
                    + (isLiquid ? "L" : "")
                    + (perHour > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perHour)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
            ret.append("\n");
            ret.append(
                perDayText + EnumChatFormatting.GOLD
                    + formatNumber(roundNumber.apply(perDay))
                    + (isLiquid ? "L" : "")
                    + (perDay > 1_000_000
                        ? EnumChatFormatting.WHITE + " ["
                            + EnumChatFormatting.GRAY
                            + formatShortenedLong((long) perDay)
                            + EnumChatFormatting.WHITE
                            + "]"
                        : "")
                    + EnumChatFormatting.RESET);
        }
        return ret.toString();
    }

    protected String generateCurrentProgress() {
        StringBuffer ret = new StringBuffer(translateToLocal("GT5U.gui.text.progress"));
        ret.append(" ");

        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.format((double) mProgresstime / mMaxProgresstime * 100, ret);
        numberFormat.setMinimumIntegerDigits(1);
        ret.append("% ");
        ret.append(EnumChatFormatting.GRAY);
        ret.append("(");
        ret.append(EnumChatFormatting.WHITE);
        numberFormat.setMinimumFractionDigits((mMaxProgresstime / 20) > 1_000 ? 0 : 2);
        numberFormat.setMaximumFractionDigits((mMaxProgresstime / 20) > 1_000 ? 0 : 2);
        numberFormat.format((double) mProgresstime / 20, ret);
        ret.append("s");
        ret.append(EnumChatFormatting.GRAY);
        ret.append("/");
        ret.append(EnumChatFormatting.WHITE);
        numberFormat.format((double) mMaxProgresstime / 20, ret);
        ret.append("s");
        ret.append(EnumChatFormatting.GRAY);
        ret.append(")");
        ret.append(EnumChatFormatting.RESET);
        ret.append("\n");
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
        return ret.toString();
    }

    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.setSynced(false)
            .setSpace(0);

        screenElements.widget(new StructureErrorSyncer(() -> structureErrors, value -> structureErrors = value));

        screenElements.widget(
            new FakeSyncWidget<>(
                () -> structureErrorContext,
                data -> structureErrorContext = data,
                ByteBufUtils::writeTag,
                ByteBufUtils::readTag));

        screenElements.widgets(TextWidget.dynamicString(() -> {
            ArrayList<String> lines = new ArrayList<>();
            localizeStructureErrors(structureErrors, structureErrorContext, lines);
            return String.join("\n", lines);
        })
            .setSynced(false)
            .setTextAlignment(Alignment.CenterLeft)
            .setDefaultColor(EnumChatFormatting.DARK_RED)
            .setEnabled(w -> hasStructureErrors()));

        if (supportsMachineModeSwitch()) {
            screenElements.widget(
                TextWidget
                    .dynamicString(() -> translateToLocalFormatted("gt.interact.desc.mb.mode", getMachineModeName()))
                    .setTextAlignment(Alignment.CenterLeft));
        }
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("132", "Pipe is loose. (Wrench)")).setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mWrench && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("133", "Screws are loose. (Screwdriver)"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mScrewdriver && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("134", "Something is stuck. (Soft Mallet)"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSoftMallet && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftMallet, val -> mSoftMallet = val));
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("135", "Platings are dented. (Hammer)"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mHardHammer && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("136", "Circuitry burned out. (Soldering)"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSolderingTool && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        screenElements
            .widget(
                new TextWidget(GTUtility.trans("137", "That doesn't belong there. (Crowbar)"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mCrowbar && mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        screenElements.widget(
            new TextWidget(translateToLocal("gt.interact.desc.mb.incomplete")).setTextAlignment(Alignment.CenterLeft)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));
        screenElements.widget(
            new TextWidget(translateToLocal("GT5U.gui.text.too_uncertain")).setTextAlignment(Alignment.CenterLeft)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> (getErrorDisplayID() & 128) != 0));
        screenElements.widget(
            new TextWidget(translateToLocal("GT5U.gui.text.invalid_parameters")).setTextAlignment(Alignment.CenterLeft)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> (getErrorDisplayID() & 256) != 0));

        if (showMachineStatusInGUI()) {
            screenElements.widget(
                new TextWidget(translateToLocal("gt.interact.desc.mb.idle.1")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
                .widget(new FakeSyncWidget.IntegerSyncer(this::getErrorDisplayID, this::setErrorDisplayID))
                .widget(
                    new FakeSyncWidget.BooleanSyncer(
                        () -> getBaseMetaTileEntity().isActive(),
                        val -> getBaseMetaTileEntity().setActive(val)));
            screenElements.widget(
                new TextWidget(translateToLocal("gt.interact.desc.mb.idle.2")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));
            screenElements.widget(
                new TextWidget(translateToLocal("gt.interact.desc.mb.idle.3")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()));

            screenElements.widget(
                new TextWidget(translateToLocal("gt.interact.desc.mb.running")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive()));
        }

        screenElements.widget(TextWidget.dynamicString(() -> {
            Duration time = Duration.ofSeconds((mTotalRunTime - mLastWorkingTick) / 20);
            return translateToLocalFormatted(
                "GT5U.gui.text.shutdown_duration",
                time.toHours(),
                time.toMinutes() % 60,
                time.getSeconds() % 60);
        })
            .setSynced(false)
            .setTextAlignment(Alignment.CenterLeft)
            .setEnabled(
                widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                    && getBaseMetaTileEntity().wasShutdown()))
            .widget(new FakeSyncWidget.LongSyncer(() -> mTotalRunTime, time -> mTotalRunTime = time))
            .widget(new FakeSyncWidget.LongSyncer(() -> mLastWorkingTick, time -> mLastWorkingTick = time));
        screenElements.widget(
            TextWidget.dynamicString(
                () -> getBaseMetaTileEntity().getLastShutDownReason()
                    .getDisplayString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                        && GTUtility.isStringValid(
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
                        && GTUtility.isStringValid(checkRecipeResult.getDisplayString())
                        && (isAllowedToWork() || getBaseMetaTileEntity().isActive()
                            || checkRecipeResult.persistsOnShutdown())))
            .widget(new CheckRecipeResultSyncer(() -> checkRecipeResult, (result) -> checkRecipeResult = result));

        if (showRecipeTextInGUI()) {
            screenElements.widget(
                TextWidget.dynamicString(this::generateCurrentProgress)
                    .setSynced(false)
                    .setTextAlignment(new Alignment(-1, -1))
                    .setSize(180, 12)
                    .setEnabled(
                        widget -> (mOutputFluids != null && mOutputFluids.length > 0)
                            || (mOutputItems != null && mOutputItems.length > 0)
                            || (mMaxProgresstime > 0)));
            final ChangeableWidget recipeOutputItemsWidget = new ChangeableWidget(
                this::generateCurrentRecipeInfoWidget);
            // Display current recipe
            screenElements.widget(
                new FakeSyncWidget.ListSyncer<>(
                    () -> mOutputFluids != null ? Arrays.stream(mOutputFluids)
                        .map(fluidStack -> {
                            if (fluidStack == null) return null;
                            return new FluidStack(fluidStack, fluidStack.amount) {

                                @Override
                                public boolean isFluidEqual(FluidStack other) {
                                    return super.isFluidEqual(other) && amount == other.amount;
                                }
                            };
                        })
                        .collect(Collectors.toList()) : Collections.emptyList(),
                    val -> {
                        mOutputFluids = val.toArray(new FluidStack[0]);
                        recipeOutputItemsWidget.notifyChangeNoSync();
                    },
                    NetworkUtils::writeFluidStack,
                    NetworkUtils::readFluidStack))
                .widget(
                    new FakeSyncWidget.ListSyncer<>(
                        () -> mOutputItems != null ? Arrays.asList(mOutputItems) : Collections.emptyList(),
                        val -> {
                            mOutputItems = val.toArray(new ItemStack[0]);
                            recipeOutputItemsWidget.notifyChangeNoSync();
                        },
                        NetworkUtils::writeItemStack,
                        NetworkUtils::readItemStack))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mProgresstime, val -> mProgresstime = val))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, val -> {
                    mMaxProgresstime = val;
                    recipeOutputItemsWidget.notifyChangeNoSync();
                }));
            screenElements.widget(recipeOutputItemsWidget);
        }

        screenElements.widget(
            new TextWidget(GTUtility.trans("144", "Missing Turbine Rotor")).setTextAlignment(Alignment.CenterLeft)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setEnabled(widget -> {
                    if (getBaseMetaTileEntity().isAllowedToWork()) return false;
                    if (getErrorDisplayID() == 0 && this instanceof MTELargeTurbine) {
                        final ItemStack tItem = inventorySlot.getMcSlot()
                            .getStack();
                        return tItem == null
                            || !(tItem.getItem() == MetaGeneratedTool01.INSTANCE && tItem.getItemDamage() >= 170
                                && tItem.getItemDamage() <= 177);
                    }
                    return false;
                }));
    }

    public boolean showRecipeTextInGUI() {
        return true;
    }

    public boolean showMachineStatusInGUI() {
        return true;
    }

    @TestOnly
    protected void setEnergyHatches(ArrayList<MTEHatchEnergy> EnergyHatches) {
        this.mEnergyHatches = EnergyHatches;
    }

    @TestOnly
    protected void setExoticEnergyHatches(List<MTEHatch> ExoticEnergyHatches) {
        this.mExoticEnergyHatches = ExoticEnergyHatches;
    }

    public void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftMallet = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public final ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return getGui().build(guiData, syncManager, uiSettings);
    }

    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this);
    }

    public boolean getDefaultHasMaintenanceChecks() {
        return true;
    }

    protected boolean requiresMuffler() {
        return getPollutionPerSecond(null) > 0;
    }

    public boolean shouldCheckMaintenance() {
        return !disableMaintenance && hasMaintenanceChecks;
    }

    public void setMaxParallelForPanel(int parallel) {
        this.maxParallel = parallel;
    }

    @Nonnull
    public CheckRecipeResult getCheckRecipeResult() {
        return checkRecipeResult;
    }

    public void setCheckRecipeResult(@Nonnull CheckRecipeResult checkRecipeResult) {
        this.checkRecipeResult = checkRecipeResult;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public int getMaxProgresstime() {
        return mMaxProgresstime;
    }

    public long getTotalRunTime() {
        return mTotalRunTime;
    }

    public void setTotalRunTime(long mTotalRunTime) {
        this.mTotalRunTime = mTotalRunTime;
    }

    public long getLastWorkingTick() {
        return mLastWorkingTick;
    }

    public void setLastWorkingTick(long mLastWorkingTick) {
        this.mLastWorkingTick = mLastWorkingTick;
    }

    public EnumSet<StructureError> getStructureErrors() {
        return structureErrors;
    }

    public void setStructureErrors(EnumSet<StructureError> structureErrors) {
        this.structureErrors = structureErrors;
    }

    public int getPowerPanelMaxParallel() {
        return powerPanelMaxParallel;
    }

    public void setPowerPanelMaxParallel(int powerPanelMaxParallel) {
        this.powerPanelMaxParallel = powerPanelMaxParallel;
    }

    public boolean isAlwaysMaxParallel() {
        return alwaysMaxParallel;
    }

    public void setAlwaysMaxParallel(boolean alwaysMaxParallel) {
        this.alwaysMaxParallel = alwaysMaxParallel;
    }

    public boolean usesTurbines() {
        return usesTurbine;
    }

    public boolean canBeMuffled() {
        return canBeMuffled;
    }

    public boolean makesPowerfailEvents() {
        return makePowerfailEvents;
    }

    public void setPowerfailEventCreationStatus(boolean status) {
        makePowerfailEvents = status;
    }

    public boolean hasRunningText() {
        return true;
    }
}
