package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.debugCleanroom;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.metatileentity.BaseTileEntity.FLUID_TRANSFER_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.ITEM_TRANSFER_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.NEI_TRANSFER_STEAM_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.NEI_TRANSFER_VOLTAGE_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.POWER_SOURCE_KEY;
import static gregtech.api.metatileentity.BaseTileEntity.SPECIAL_SLOT_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.STALLED_STUTTERING_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.STALLED_VENT_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.metatileentity.BaseTileEntity.UNUSED_SLOT_TOOLTIP;
import static gregtech.api.util.GTRecipeConstants.EXPLODE;
import static gregtech.api.util.GTRecipeConstants.ON_FIRE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UNKNOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.CircularGaugeDrawable;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.objects.overclockdescriber.EUOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.FakeCleanroom;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTWaila;
import gregtech.api.util.OverclockCalculator;
import gregtech.client.GTSoundLoop;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public abstract class MTEBasicMachine extends MTEBasicTank implements RecipeMapWorkable, IConfigurationCircuitSupport,
    IOverclockDescriptionProvider, IAddGregtechLogo, IAddUIWidgets {

    /**
     * return values for checkRecipe()
     */
    protected static final int DID_NOT_FIND_RECIPE = 0, FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS = 1,
        FOUND_AND_SUCCESSFULLY_USED_RECIPE = 2;
    public static final String STEAM_AMOUNT_LANGKEY = "GT5U.machines.steam.amount";

    public static final int OTHER_SLOT_COUNT = 5;
    public final ItemStack[] mOutputItems;
    public final int mInputSlotCount, mAmperage;
    public boolean mAllowInputFromOutputSide = false, mFluidTransfer = false, mItemTransfer = false,
        mHasBeenUpdated = false, mStuttering = false, mCharge = false, mDecharge = false;
    private int errorDisplayID;
    public boolean mDisableFilter = true;
    public boolean mDisableMultiStack = true;
    public int mProgresstime = 0, mMaxProgresstime = 0, mEUt = 0, mOutputBlocked = 0;
    public ForgeDirection mMainFacing = ForgeDirection.WEST;
    public FluidStack mOutputFluid;
    protected final OverclockDescriber overclockDescriber;
    @SideOnly(Side.CLIENT)
    protected GTSoundLoop activitySoundLoop;

    /**
     * Contains the Recipe which has been previously used, or null if there was no previous Recipe, which could have
     * been buffered
     */
    protected GTRecipe mLastRecipe = null;

    private FluidStack mFluidOut;
    protected final FluidStackTank fluidOutputTank = new FluidStackTank(
        () -> mFluidOut,
        fluidStack -> mFluidOut = fluidStack,
        this::getCapacity);

    /**
     * Registers machine with single-line description.
     *
     * @param aOverlays 0 = SideFacingActive 1 = SideFacingInactive 2 = FrontFacingActive 3 = FrontFacingInactive 4 =
     *                  TopFacingActive 5 = TopFacingInactive 6 = BottomFacingActive 7 = BottomFacingInactive ----- Not
     *                  all Array Elements have to be initialised, you can also just use 8 Parameters for the Default
     *                  Pipe Texture Overlays ----- 8 = BottomFacingPipeActive 9 = BottomFacingPipeInactive 10 =
     *                  TopFacingPipeActive 11 = TopFacingPipeInactive 12 = SideFacingPipeActive 13 =
     *                  SideFacingPipeInactive
     */
    public MTEBasicMachine(int aID, String aName, String aNameRegional, int aTier, int aAmperage, String aDescription,
        int aInputSlotCount, int aOutputSlotCount, ITexture... aOverlays) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            OTHER_SLOT_COUNT + aInputSlotCount + aOutputSlotCount + 1,
            aDescription,
            aOverlays);
        mInputSlotCount = Math.max(0, aInputSlotCount);
        mOutputItems = new ItemStack[Math.max(0, aOutputSlotCount)];
        mAmperage = aAmperage;
        overclockDescriber = createOverclockDescriber();
    }

    /**
     * Registers machine with multi-line descriptions.
     */
    public MTEBasicMachine(int aID, String aName, String aNameRegional, int aTier, int aAmperage, String[] aDescription,
        int aInputSlotCount, int aOutputSlotCount, ITexture... aOverlays) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            OTHER_SLOT_COUNT + aInputSlotCount + aOutputSlotCount + 1,
            aDescription,
            aOverlays);
        mInputSlotCount = Math.max(0, aInputSlotCount);
        mOutputItems = new ItemStack[Math.max(0, aOutputSlotCount)];
        mAmperage = aAmperage;
        overclockDescriber = createOverclockDescriber();
    }

    /**
     * For {@link #newMetaEntity}.
     */
    public MTEBasicMachine(String aName, int aTier, int aAmperage, String[] aDescription, ITexture[][][] aTextures,
        int aInputSlotCount, int aOutputSlotCount) {
        super(aName, aTier, OTHER_SLOT_COUNT + aInputSlotCount + aOutputSlotCount + 1, aDescription, aTextures);
        mInputSlotCount = Math.max(0, aInputSlotCount);
        mOutputItems = new ItemStack[Math.max(0, aOutputSlotCount)];
        mAmperage = aAmperage;
        overclockDescriber = createOverclockDescriber();
    }

    /**
     * To be called by the constructor to initialize this instance's overclock behavior
     */
    protected OverclockDescriber createOverclockDescriber() {
        return new EUOverclockDescriber(mTier, mAmperage);
    }

    protected boolean isValidMainFacing(ForgeDirection side) {
        return (side.flag & (UP.flag | DOWN.flag | UNKNOWN.flag)) == 0; // Horizontal
    }

    public boolean setMainFacing(ForgeDirection side) {
        if (!isValidMainFacing(side)) return false;
        mMainFacing = side;
        if (getBaseMetaTileEntity().getFrontFacing() == mMainFacing) {
            getBaseMetaTileEntity().setFrontFacing(side.getOpposite());
        }
        onFacingChange();
        onMachineBlockUpdate();
        return true;
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();
        // Set up the correct facing (front towards player, output opposite) client-side before the server packet
        // arrives
        if (mMainFacing == UNKNOWN) {
            IGregTechTileEntity te = getBaseMetaTileEntity();
            if (te != null && te.getWorld().isRemote) {
                mMainFacing = te.getFrontFacing();
                te.setFrontFacing(te.getBackFacing());
            }
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[14][17][];
        aTextures = Arrays.copyOf(aTextures, 14);

        for (int i = 0; i < aTextures.length; i++) if (aTextures[i] != null) for (byte c = -1; c < 16; c++) {
            if (rTextures[i][c + 1] == null)
                rTextures[i][c + 1] = new ITexture[] { MACHINE_CASINGS[mTier][c + 1], aTextures[i] };
        }

        for (byte c = -1; c < 16; c++) {
            if (rTextures[0][c + 1] == null) rTextures[0][c + 1] = getSideFacingActive(c);
            if (rTextures[1][c + 1] == null) rTextures[1][c + 1] = getSideFacingInactive(c);
            if (rTextures[2][c + 1] == null) rTextures[2][c + 1] = getFrontFacingActive(c);
            if (rTextures[3][c + 1] == null) rTextures[3][c + 1] = getFrontFacingInactive(c);
            if (rTextures[4][c + 1] == null) rTextures[4][c + 1] = getTopFacingActive(c);
            if (rTextures[5][c + 1] == null) rTextures[5][c + 1] = getTopFacingInactive(c);
            if (rTextures[6][c + 1] == null) rTextures[6][c + 1] = getBottomFacingActive(c);
            if (rTextures[7][c + 1] == null) rTextures[7][c + 1] = getBottomFacingInactive(c);
            if (rTextures[8][c + 1] == null) rTextures[8][c + 1] = getBottomFacingPipeActive(c);
            if (rTextures[9][c + 1] == null) rTextures[9][c + 1] = getBottomFacingPipeInactive(c);
            if (rTextures[10][c + 1] == null) rTextures[10][c + 1] = getTopFacingPipeActive(c);
            if (rTextures[11][c + 1] == null) rTextures[11][c + 1] = getTopFacingPipeInactive(c);
            if (rTextures[12][c + 1] == null) rTextures[12][c + 1] = getSideFacingPipeActive(c);
            if (rTextures[13][c + 1] == null) rTextures[13][c + 1] = getSideFacingPipeInactive(c);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        final int textureIndex;
        if ((mMainFacing.flag & (UP.flag | DOWN.flag)) != 0) { // UP or DOWN
            if (sideDirection == facingDirection) {
                textureIndex = active ? 2 : 3;
            } else {
                textureIndex = switch (sideDirection) {
                    case DOWN -> active ? 6 : 7;
                    case UP -> active ? 4 : 5;
                    default -> active ? 0 : 1;
                };
            }
        } else {
            if (sideDirection == mMainFacing) {
                textureIndex = active ? 2 : 3;
            } else {
                if (showPipeFacing() && sideDirection == facingDirection) {
                    textureIndex = switch (sideDirection) {
                        case DOWN -> active ? 8 : 9;
                        case UP -> active ? 10 : 11;
                        default -> active ? 12 : 13;
                    };
                } else {
                    textureIndex = switch (sideDirection) {
                        case DOWN -> active ? 6 : 7;
                        case UP -> active ? 4 : 5;
                        default -> active ? 0 : 1;
                    };
                }
            }
        }
        return mTextures[textureIndex][colorIndex + 1];
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex > 0 && super.isValidSlot(aIndex)
            && aIndex != getCircuitSlot()
            && aIndex != OTHER_SLOT_COUNT + mInputSlotCount + mOutputItems.length;
    }

    @Override
    public boolean isIOSlot(int slot) {
        // Ignore output slots, special slots, battery slots, and circuit slots
        return slot >= getInputSlot() && slot < getInputSlot() + mInputSlotCount;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        // Either mMainFacing or mMainFacing is horizontal
        return ((facing.flag | mMainFacing.flag) & ~(UP.flag | DOWN.flag | UNKNOWN.flag)) != 0;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side != mMainFacing;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return side != mMainFacing && (mAllowInputFromOutputSide || side != getBaseMetaTileEntity().getFrontFacing());
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return side != mMainFacing;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16L;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 64L;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return ((long) mEUt * 2L) / V[mTier] + 1L;
    }

    @Override
    public int getInputSlot() {
        return OTHER_SLOT_COUNT;
    }

    @Override
    public int getOutputSlot() {
        return OTHER_SLOT_COUNT + mInputSlotCount;
    }

    public int getSpecialSlotIndex() {
        return 3;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 1;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 1;
    }

    @Override
    public int rechargerSlotCount() {
        return mCharge ? 1 : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return mDecharge ? 1 : 0;
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
        mProgresstime += aProgress;
        return mMaxProgresstime - mProgresstime;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return getFillableStack() != null || (getRecipeMap() != null && getRecipeMap().containsInput(aFluid));
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public FluidStack getDrainableStack() {
        return mFluidOut;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack aFluid) {
        markDirty();
        mFluidOut = aFluid;
        return mFluidOut;
    }

    @Override
    public boolean isDrainableStackSeparate() {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (!GTMod.proxy.mForceFreeFace) {
            openGui(aPlayer);
            return true;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (aBaseMetaTileEntity.getAirAtSide(side)) {
                openGui(aPlayer);
                return true;
            }
        }
        GTUtility.sendChatTrans(aPlayer, "GT5U.chat.machine.no_free_side");
        return true;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        mMainFacing = ForgeDirection.UNKNOWN;
        if (!getBaseMetaTileEntity().getWorld().isRemote) {
            final GTClientPreference tPreference = GTMod.proxy
                .getClientPreference(getBaseMetaTileEntity().getOwnerUuid());
            if (tPreference != null) {
                mDisableFilter = !tPreference.isSingleBlockInitialFilterEnabled();
                mDisableMultiStack = !tPreference.isSingleBlockInitialMultiStackEnabled();
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mFluidTransfer", mFluidTransfer);
        aNBT.setBoolean("mItemTransfer", mItemTransfer);
        aNBT.setBoolean("mHasBeenUpdated", mHasBeenUpdated);
        aNBT.setBoolean("mAllowInputFromOutputSide", mAllowInputFromOutputSide);
        aNBT.setBoolean("mDisableFilter", mDisableFilter);
        aNBT.setBoolean("mDisableMultiStack", mDisableMultiStack);
        aNBT.setInteger("mEUt", mEUt);
        aNBT.setInteger("mMainFacing", mMainFacing.ordinal());
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        if (mOutputFluid != null) aNBT.setTag("mOutputFluid", mOutputFluid.writeToNBT(new NBTTagCompound()));
        if (mFluidOut != null) aNBT.setTag("mFluidOut", mFluidOut.writeToNBT(new NBTTagCompound()));

        for (int i = 0; i < mOutputItems.length; i++)
            if (mOutputItems[i] != null) GTUtility.saveItem(aNBT, "mOutputItem" + i, mOutputItems[i]);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mFluidTransfer = aNBT.getBoolean("mFluidTransfer");
        mItemTransfer = aNBT.getBoolean("mItemTransfer");
        mHasBeenUpdated = aNBT.getBoolean("mHasBeenUpdated");
        mAllowInputFromOutputSide = aNBT.getBoolean("mAllowInputFromOutputSide");
        mDisableFilter = aNBT.getBoolean("mDisableFilter");
        mDisableMultiStack = aNBT.getBoolean("mDisableMultiStack");
        mEUt = aNBT.getInteger("mEUt");
        mMainFacing = ForgeDirection.getOrientation(aNBT.getInteger("mMainFacing"));
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        mOutputFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mOutputFluid"));
        mFluidOut = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluidOut"));

        for (int i = 0; i < mOutputItems.length; i++) mOutputItems[i] = GTUtility.loadItem(aNBT, "mOutputItem" + i);
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
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;

            doDisplayThings();

            boolean tSucceeded = false;
            boolean isActive = mMaxProgresstime > 0;

            if (isActive && (mProgresstime >= 0 || aBaseMetaTileEntity.isAllowedToWork())) {
                markDirty();
                if (mProgresstime > 0) aBaseMetaTileEntity.setActive(true);
                if (mProgresstime < 0 || drainEnergyForProcess(mEUt)) {
                    if (++mProgresstime >= mMaxProgresstime) {
                        for (int i = 0; i < mOutputItems.length; i++)
                            for (int j = 0; j < mOutputItems.length; j++) if (aBaseMetaTileEntity
                                .addStackToSlot(getOutputSlot() + ((j + i) % mOutputItems.length), mOutputItems[i]))
                                break;
                        if (mOutputFluid != null)
                            if (getDrainableStack() == null) setDrainableStack(mOutputFluid.copy());
                            else if (mOutputFluid.isFluidEqual(getDrainableStack()))
                                getDrainableStack().amount += mOutputFluid.amount;
                        Arrays.fill(mOutputItems, null);
                        mOutputFluid = null;
                        mEUt = 0;
                        mProgresstime = 0;
                        mMaxProgresstime = 0;
                        mStuttering = false;
                        tSucceeded = true;
                        endProcess();
                    }
                    if (mProgresstime > 5) mStuttering = false;
                } else {
                    if (!mStuttering) {
                        stutterProcess();
                        if (canHaveInsufficientEnergy()) mProgresstime = -100;
                        mStuttering = true;
                    }
                }
            } else {
                aBaseMetaTileEntity.setActive(false);
            }

            boolean tRemovedOutputFluid = false;

            if (doesAutoOutputFluids() && getDrainableStack() != null
                && aBaseMetaTileEntity.getFrontFacing() != mMainFacing
                && (tSucceeded || aTick % 20 == 0)) {
                IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
                if (tTank != null) {
                    FluidStack tDrained = drain(1000, false);
                    if (tDrained != null) {
                        final int tFilledAmount = tTank.fill(aBaseMetaTileEntity.getBackFacing(), tDrained, false);
                        if (tFilledAmount > 0)
                            tTank.fill(aBaseMetaTileEntity.getBackFacing(), drain(tFilledAmount, true), true);
                    }
                }
                if (getDrainableStack() == null) tRemovedOutputFluid = true;
            }

            if (doesAutoOutput() && !isOutputEmpty()
                && aBaseMetaTileEntity.getFrontFacing() != mMainFacing
                && (tSucceeded || mOutputBlocked % 300 == 1
                    || aBaseMetaTileEntity.hasInventoryBeenModified()
                    || aTick % 600 == 0)) {
                GTItemTransfer transfer = new GTItemTransfer();

                transfer.outOfMachine(this, aBaseMetaTileEntity.getFrontFacing());
                transfer.dropItems(this);

                transfer.setStacksToTransfer(mOutputItems.length);

                transfer.transfer();
            }

            if (mOutputBlocked != 0) if (isOutputEmpty()) mOutputBlocked = 0;
            else mOutputBlocked++;

            if (allowToCheckRecipe()) {
                if (mMaxProgresstime <= 0 && aBaseMetaTileEntity.isAllowedToWork()
                    && (tRemovedOutputFluid || tSucceeded
                        || aBaseMetaTileEntity.hasInventoryBeenModified()
                        || aTick % 600 == 0
                        || aBaseMetaTileEntity.hasWorkJustBeenEnabled())
                    && hasEnoughEnergyToCheckRecipe()) {
                    if (checkRecipe() == FOUND_AND_SUCCESSFULLY_USED_RECIPE) {
                        if (getSpecialSlot() != null && getSpecialSlot().stackSize <= 0)
                            mInventory[getSpecialSlotIndex()] = null;
                        for (int i = getInputSlot(), j = i + mInputSlotCount; i < j; i++)
                            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
                        for (int i = 0; i < mOutputItems.length; i++) {
                            mOutputItems[i] = GTUtility.copyOrNull(mOutputItems[i]);
                            if (mOutputItems[i] != null && mOutputItems[i].stackSize > 64)
                                mOutputItems[i].stackSize = 64;
                            mOutputItems[i] = GTOreDictUnificator.get(true, mOutputItems[i]);
                        }
                        if (mFluid != null && mFluid.amount <= 0) mFluid = null;
                        mMaxProgresstime = Math.max(1, mMaxProgresstime);
                        if (GTUtility.isDebugItem(mInventory[dechargerSlotStartIndex()])) {
                            mEUt = mMaxProgresstime = 1;
                        }
                        startProcess();
                    } else {
                        mMaxProgresstime = 0;
                        Arrays.fill(mOutputItems, null);
                        mOutputFluid = null;
                    }
                }
            } else {
                if (!mStuttering) {
                    stutterProcess();
                    aBaseMetaTileEntity.setActive(false);
                    mStuttering = true;
                }
            }
        } else {
            updateSounds(getActivitySoundLoop());
        }
        // Only using mNeedsSteamVenting right now and assigning it to 64 to space in the range for more single block
        // machine problems.
        // Value | Class | Field
        // 1 | MTEBasicMachine | mStuttering
        // 64 | MTEBasicMachineBronze | mNeedsSteamVenting
        setErrorDisplayID((getErrorDisplayID() & ~127)); // | (mStuttering ? 1 :
                                                         // 0));
    }

    protected void doDisplayThings() {
        if (!isValidMainFacing(mMainFacing) && isValidMainFacing(getBaseMetaTileEntity().getFrontFacing())) {
            mMainFacing = getBaseMetaTileEntity().getFrontFacing();
        }
        if (isValidMainFacing(mMainFacing) && !mHasBeenUpdated) {
            mHasBeenUpdated = true;
            getBaseMetaTileEntity().setFrontFacing(getBaseMetaTileEntity().getBackFacing());
        }
    }

    protected boolean hasEnoughEnergyToCheckRecipe() {
        return getBaseMetaTileEntity().isUniversalEnergyStored(getMinimumStoredEU() / 2);
    }

    protected boolean drainEnergyForProcess(long aEUt) {
        return getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEUt, false);
    }

    /**
     * Calculates overclock based on {@link #overclockDescriber}.
     */
    protected void calculateCustomOverclock(GTRecipe recipe) {
        OverclockCalculator calculator = overclockDescriber.createCalculator(
            new OverclockCalculator().setRecipeEUt(recipe.mEUt)
                .setDuration(recipe.mDuration),
            recipe);
        calculator.calculate();
        mEUt = (int) calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
    }

    /**
     * Helper method for calculating simple overclock.
     */
    protected void calculateOverclockedNess(int eut, int duration) {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(eut)
            .setEUt(V[mTier] * mAmperage)
            .setDuration(duration)
            .calculate();
        mEUt = (int) calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
    }

    protected ItemStack getSpecialSlot() {
        return mInventory[getSpecialSlotIndex()];
    }

    protected ItemStack getOutputAt(int aIndex) {
        return mInventory[getOutputSlot() + aIndex];
    }

    protected ItemStack[] getAllOutputs() {
        ItemStack[] rOutputs = new ItemStack[mOutputItems.length];
        for (int i = 0; i < mOutputItems.length; i++) rOutputs[i] = getOutputAt(i);
        return rOutputs;
    }

    protected boolean canOutput(GTRecipe aRecipe) {
        return aRecipe != null && (aRecipe.mNeedsEmptyOutput ? isOutputEmpty() && getDrainableStack() == null
            : canOutput(aRecipe.getFluidOutput(0)) && canOutput(aRecipe.mOutputs));
    }

    protected boolean canOutput(ItemStack... aOutputs) {
        if (aOutputs == null) return true;
        ItemStack[] tOutputSlots = getAllOutputs();
        for (int i = 0; i < tOutputSlots.length && i < aOutputs.length; i++)
            if (tOutputSlots[i] != null && aOutputs[i] != null
                && (!GTUtility.areStacksEqual(tOutputSlots[i], aOutputs[i], false)
                    || tOutputSlots[i].stackSize + aOutputs[i].stackSize > tOutputSlots[i].getMaxStackSize())) {
                        mOutputBlocked++;
                        return false;
                    }
        return true;
    }

    protected boolean canOutput(FluidStack aOutput) {
        if (aOutput == null) return true;
        FluidStack drainableStack = getDrainableStack();
        if (drainableStack != null && !drainableStack.isFluidEqual(aOutput)) return false;
        return (drainableStack != null ? drainableStack.amount : 0) + aOutput.amount <= getCapacity();
    }

    protected ItemStack getInputAt(int aIndex) {
        return mInventory[getInputSlot() + aIndex];
    }

    protected ItemStack[] getAllInputs() {
        int tRealInputSlotCount = this.mInputSlotCount + (allowSelectCircuit() ? 1 : 0);
        ItemStack[] rInputs = new ItemStack[tRealInputSlotCount];
        for (int i = 0; i < mInputSlotCount; i++) rInputs[i] = getInputAt(i);
        if (allowSelectCircuit()) rInputs[mInputSlotCount] = getStackInSlot(getCircuitSlot());
        return rInputs;
    }

    protected boolean isOutputEmpty() {
        boolean rIsEmpty = true;
        for (ItemStack tOutputSlotContent : getAllOutputs()) if (tOutputSlotContent != null) {
            rIsEmpty = false;
            break;
        }
        return rIsEmpty;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mMainFacing = ForgeDirection.getOrientation(aValue);
    }

    @Override
    public byte getUpdateData() {
        return (byte) mMainFacing.ordinal();
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 8) GTUtility.doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
    }

    public boolean doesAutoOutput() {
        return mItemTransfer;
    }

    public boolean doesAutoOutputFluids() {
        return mFluidTransfer;
    }

    public boolean allowToCheckRecipe() {
        return true;
    }

    public boolean showPipeFacing() {
        return true;
    }

    /**
     * Called whenever the Machine successfully started a Process, useful for Sound Effects
     */
    public void startProcess() {
        //
    }

    /**
     * Called whenever the Machine successfully finished a Process, useful for Sound Effects
     */
    public void endProcess() {
        //
    }

    /**
     * Called whenever the Machine aborted a Process, useful for Sound Effects
     */
    public void abortProcess() {
        //
    }

    /**
     * Called whenever the Machine aborted a Process but still works on it, useful for Sound Effects
     */
    public void stutterProcess() {
        if (useStandardStutterSound()) sendSound((byte) 8);
    }

    /**
     * Returns the sound resource to use for the activity loop.
     * Subclasses can override to provide their own.
     */
    @SideOnly(Side.CLIENT)
    protected SoundResource getActivitySoundLoop() {
        return null;
    }

    /**
     * Starts the activity sound loop if it isn't already playing.
     */
    @SideOnly(Side.CLIENT)
    protected void updateSounds(SoundResource activitySound) {
        if (activitySound == null) return;

        if (getBaseMetaTileEntity().isActive() && !getBaseMetaTileEntity().isMuffled()) {
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
            stopActivitySound();
        }
    }

    /**
     * Stops the activity sound loop if playing.
     */
    @SideOnly(Side.CLIENT)
    protected void stopActivitySound() {
        if (activitySoundLoop != null) {
            activitySoundLoop.setFadeMe(true);
            activitySoundLoop = null;
        }
    }

    /**
     * If this Machine can have the Insufficient Energy Line Problem
     */
    public boolean canHaveInsufficientEnergy() {
        return true;
    }

    public boolean useStandardStutterSound() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            translateToLocalFormatted(
                "GT5U.infodata.progress",
                EnumChatFormatting.GREEN + formatNumber((mProgresstime / 20)) + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + formatNumber(mMaxProgresstime / 20) + EnumChatFormatting.RESET),
            translateToLocalFormatted(
                "GT5U.infodata.energy",
                EnumChatFormatting.GREEN + formatNumber(getBaseMetaTileEntity().getStoredEU())
                    + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + formatNumber(getBaseMetaTileEntity().getEUCapacity())
                    + EnumChatFormatting.RESET),
            translateToLocalFormatted(
                "GT5U.infodata.currently_uses",
                EnumChatFormatting.RED + formatNumber(mEUt) + EnumChatFormatting.RESET,
                EnumChatFormatting.RED + formatNumber(mEUt == 0 ? 0 : mAmperage) + EnumChatFormatting.RESET) };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing() || side == mMainFacing) {
            if (aPlayer.isSneaking()) {
                mDisableFilter = !mDisableFilter;
                GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + mDisableFilter);
            } else {
                mAllowInputFromOutputSide = !mAllowInputFromOutputSide;
                GTUtility.sendChatTrans(
                    aPlayer,
                    mAllowInputFromOutputSide ? "gt.interact.desc.input_from_output_on"
                        : "gt.interact.desc.input_from_output_off");
            }
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        if (!entityPlayer.isSneaking() || wrenchingSide != mMainFacing)
            return super.onSolderingToolRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
        mDisableMultiStack = !mDisableMultiStack;
        GTUtility.sendChatTrans(entityPlayer, "GT5U.hatch.disableMultiStack." + mDisableMultiStack);
        return true;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        if (side != mMainFacing) return true;
        return CoverRegistry.getCoverPlacer(coverItem)
            .isGuiClickable();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side != mMainFacing && aIndex >= getOutputSlot() && aIndex < getOutputSlot() + mOutputItems.length;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (side == mMainFacing) return false;
        if (aIndex < getInputSlot()) return false;
        if (aIndex >= getInputSlot() + mInputSlotCount) return false;
        if (!mAllowInputFromOutputSide && side == aBaseMetaTileEntity.getFrontFacing()) return false;

        for (int i = getInputSlot(), j = i + mInputSlotCount; i < j; i++) {
            if (GTUtility.areStacksEqual(GTOreDictUnificator.get(aStack), mInventory[i]) && mDisableMultiStack) {
                return i == aIndex;
            }
        }

        return mDisableFilter || allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    /**
     * Test if given stack can be inserted into specified slot. If mDisableMultiStack is false, before execution of this
     * method it is ensured there is no such kind of item inside any input slots already. Otherwise, you don't need to
     * check for it anyway.
     */
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return !mDisableMultiStack || mInventory[aIndex] == null;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    protected final ItemStack[] appendSelectedCircuit(ItemStack... inputs) {
        if (allowSelectCircuit()) {
            ItemStack circuit = getStackInSlot(getCircuitSlot());
            if (circuit != null) {
                ItemStack[] result = Arrays.copyOf(inputs, inputs.length + 1);
                result[inputs.length] = circuit;
                return result;
            }
        }
        return inputs;
    }

    @Override
    public int getCircuitSlot() {
        return 4;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    /**
     * Override this to check the Recipes yourself, super calls to this could be useful if you just want to add a
     * special case
     * <p/>
     * I thought about Enum too, but Enum doesn't add support for people adding other return Systems.
     * <p/>
     * Funny how Eclipse marks the word Enum as not correctly spelled.
     *
     * @return see constants above
     */
    public int checkRecipe() {
        return checkRecipe(false);
    }

    public static boolean isValidForLowGravity(GTRecipe tRecipe, int dimId) {
        if (FakeCleanroom.isLowGravBypassEnabled()) return true;
        return // TODO check or get a better solution
        DimensionManager.getProvider(dimId)
            .getClass()
            .getName()
            .contains("Orbit")
            || DimensionManager.getProvider(dimId)
                .getClass()
                .getName()
                .endsWith("Space")
            || DimensionManager.getProvider(dimId)
                .getClass()
                .getName()
                .endsWith("Asteroids")
            || DimensionManager.getProvider(dimId)
                .getClass()
                .getName()
                .endsWith("SS")
            || DimensionManager.getProvider(dimId)
                .getClass()
                .getName()
                .contains("SpaceStation")
            || DimensionManager.getProvider(dimId)
                .getClass()
                .getName()
                .contains("Mothership");
    }

    /**
     *
     * @param skipOC disables OverclockedNess calculation and check - if you do you must implement your own method...
     * @return DID_NOT_FIND_RECIPE = 0, FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS = 1,
     *         FOUND_AND_SUCCESSFULLY_USED_RECIPE = 2;
     */
    public int checkRecipe(boolean skipOC) {
        RecipeMap<?> tMap = getRecipeMap();
        if (tMap == null) return DID_NOT_FIND_RECIPE;
        GTRecipe tRecipe = tMap.findRecipeQuery()
            .items(getAllInputs())
            .fluids(getFillableStack())
            .specialSlot(getSpecialSlot())
            .voltage(V[mTier])
            .cachedRecipe(mLastRecipe)
            .find();
        if (tRecipe == null) {
            return DID_NOT_FIND_RECIPE;
        }
        if (tRecipe.getMetadataOrDefault(EXPLODE, false) && getBaseMetaTileEntity() != null) {
            getBaseMetaTileEntity().doExplosion(V[mTier] * 4);
            return DID_NOT_FIND_RECIPE;
        }
        if (tRecipe.getMetadataOrDefault(ON_FIRE, false) && getBaseMetaTileEntity() != null) {
            getBaseMetaTileEntity().setOnFire();
            return DID_NOT_FIND_RECIPE;
        }
        if (tRecipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0)
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (GTMod.proxy.mLowGravProcessing && (tRecipe.mSpecialValue == -100 || tRecipe.mSpecialValue == -300)
            && !isValidForLowGravity(tRecipe, getBaseMetaTileEntity().getWorld().provider.dimensionId))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        if (tRecipe.mCanBeBuffered) mLastRecipe = tRecipe;
        if (!canOutput(tRecipe)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }
        ICleanroom cleanroom = cleanroomReference.getCleanroom();
        if (tRecipe.mSpecialValue == -200 || tRecipe.mSpecialValue == -300) {
            if (cleanroom == null || !cleanroom.isValidCleanroom() || cleanroom.getCleanness() == 0) {
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
            }
        }
        if (!tRecipe.isRecipeInputEqual(true, new FluidStack[] { getFillableStack() }, getAllInputs()))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        for (int i = 0; i < mOutputItems.length; i++)
            if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i))
                mOutputItems[i] = tRecipe.getOutput(i);
        if (tRecipe.mSpecialValue == -200 || tRecipe.mSpecialValue == -300) {
            assert cleanroom != null;
            for (int i = 0; i < mOutputItems.length; i++) if (mOutputItems[i] != null
                && getBaseMetaTileEntity().getRandomNumber(10000) > cleanroom.getCleanness()) {
                    if (debugCleanroom) {
                        GTLog.out.println(
                            "BasicMachine: Voiding output due to cleanness failure. Cleanness = "
                                + cleanroom.getCleanness());
                    }
                    mOutputItems[i] = null;
                }
        }
        mOutputFluid = tRecipe.getFluidOutput(0);
        if (!skipOC) {
            calculateCustomOverclock(tRecipe);
            // In case recipe is too OP for that machine
            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] { MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("stutteringSingleBlock")) {
            currenttip.add(translateToLocal(getWailaStutteringLine(tag)));
        } else {
            boolean isActive = tag.getBoolean("isActiveSingleBlock");
            if (isActive) {
                int mEUt = tag.getInteger("eut");
                if (!isSteampowered()) {
                    if (mEUt > 0) {
                        currenttip.add(
                            translateToLocalFormatted(
                                "GT5U.waila.energy.use_with_amperage",
                                formatNumber(mEUt),
                                GTUtility.getAmperageForTier(mEUt, (byte) getInputTier()),
                                GTUtility.getColoredTierNameFromTier((byte) getInputTier())));
                    } else if (mEUt < 0) {
                        currenttip.add(
                            translateToLocalFormatted(
                                "GT5U.waila.energy.produce_with_amperage",
                                formatNumber(-mEUt),
                                GTUtility.getAmperageForTier(-mEUt, (byte) getOutputTier()),
                                GTUtility.getColoredTierNameFromTier((byte) getOutputTier())));
                    }
                } else {
                    if (mEUt > 0) {
                        currenttip.add(
                            translateToLocalFormatted(
                                "GTPP.waila.steam.use",
                                formatNumber(mEUt * 40L),
                                GTUtility.getColoredTierNameFromVoltage(mEUt)));
                    } else if (mEUt < 0) {
                        currenttip.add(
                            translateToLocalFormatted(
                                "GTPP.waila.steam.use",
                                formatNumber(-mEUt * 40L),
                                GTUtility.getColoredTierNameFromVoltage(-mEUt)));
                    }
                }
            }
            currenttip.add(
                GTWaila.getMachineProgressString(
                    isActive,
                    tag.getBoolean("isAllowedToWorkSingleBlock"),
                    tag.getInteger("maxProgressSingleBlock"),
                    tag.getInteger("progressSingleBlock")));
        }

        currenttip.add(
            translateToLocalFormatted(
                "GT5U.waila.machine_facing",
                getFacingNameLocalized(tag.getInteger("mainFacingSingleBlock"))));

        currenttip.add(
            translateToLocalFormatted(
                "GT5U.waila.output_facing",
                getFacingNameLocalized(tag.getInteger("outputFacingSingleBlock"))));
    }

    private static @NotNull String getWailaStutteringLine(NBTTagCompound tag) {
        return tag.getBoolean("blockedSteamVentSingleBlock") ? "GT5U.waila.status.obstructed_steam_vent"
            : "GT5U.waila.status.insufficient_energy";
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setInteger("progressSingleBlock", mProgresstime);
        tag.setInteger("maxProgressSingleBlock", mMaxProgresstime);
        tag.setInteger("mainFacingSingleBlock", mMainFacing.ordinal());
        tag.setBoolean("stutteringSingleBlock", mStuttering);
        tag.setBoolean("blockedSteamVentSingleBlock", cannotVentSteam());

        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("isActiveSingleBlock", tileEntity.isActive());
            tag.setBoolean("isAllowedToWorkSingleBlock", tileEntity.isAllowedToWork());
            tag.setInteger(
                "outputFacingSingleBlock",
                tileEntity.getFrontFacing()
                    .ordinal());
            if (tileEntity.isActive()) tag.setInteger("eut", mEUt);
        }
    }

    @Nonnull
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    // GUI stuff

    @Override
    public int getCircuitSlotX() {
        return 153;
    }

    @Override
    public int getCircuitSlotY() {
        return 63;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        if (getRecipeMap() != null) {
            getRecipeMap().getFrontend()
                .addGregTechLogo(builder, new Pos2d(0, 0));
        } else {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 63));
        }
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBasicMachineBaseGui(this).build(data, syncManager, uiSettings);
    }

    // disable the entire inventory row (including corner column)
    public boolean supportsInventoryRow() {
        return this.doesBindPlayerInventory();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (!isSteampowered()) {
            builder.widget(createFluidAutoOutputButton());
            builder.widget(createItemAutoOutputButton());
        } else {
            builder.widget(createSteamProgressBar(builder));
        }

        BasicUIProperties uiProperties = getUIProperties();
        addIOSlots(builder, uiProperties);

        builder.widget(createChargerSlot(79, 62));

        addProgressBar(builder, uiProperties);

        builder.widget(createMuffleButton());

        builder.widget(
            createErrorStatusArea(
                builder,
                isSteampowered() ? GTUITextures.PICTURE_STALLED_STEAM : GTUITextures.PICTURE_STALLED_ELECTRICITY));
    }

    /**
     * Override to specify UI properties if this machine doesn't work with recipemap.
     */
    protected BasicUIProperties getUIProperties() {
        if (getRecipeMap() != null) {
            BasicUIProperties originalProperties = getRecipeMap().getFrontend()
                .getUIProperties();
            return originalProperties.toBuilder()
                .maxItemInputs(mInputSlotCount)
                .maxItemOutputs(mOutputItems.length)
                .maxFluidInputs(Math.min(originalProperties.maxFluidInputs, 1))
                .maxFluidOutputs(Math.min(originalProperties.maxFluidOutputs, 1))
                .build();
        }
        return BasicUIProperties.builder()
            .maxItemInputs(mInputSlotCount)
            .maxItemOutputs(mOutputItems.length)
            .maxFluidInputs(getCapacity() != 0 ? 1 : 0)
            .maxFluidOutputs(0)
            .build();
    }

    /**
     * Adds item I/O, special item, and fluid I/O slots.
     */
    protected void addIOSlots(ModularWindow.Builder builder, BasicUIProperties uiProperties) {
        UIHelper.forEachSlots(
            (i, backgrounds, pos) -> builder.widget(createItemInputSlot(i, backgrounds, pos)),
            (i, backgrounds, pos) -> builder.widget(createItemOutputSlot(i, backgrounds, pos)),
            (i, backgrounds, pos) -> builder.widget(createSpecialSlot(backgrounds, pos, uiProperties)),
            (i, backgrounds, pos) -> builder.widget(createFluidInputSlot(backgrounds, pos)),
            (i, backgrounds, pos) -> builder.widget(createFluidOutputSlot(backgrounds, pos)),
            getGUITextureSet().getItemSlot(),
            getGUITextureSet().getFluidSlot(),
            uiProperties,
            uiProperties.maxItemInputs,
            uiProperties.maxItemOutputs,
            uiProperties.maxFluidInputs,
            uiProperties.maxFluidOutputs,
            getSteamVariant(),
            Pos2d.ZERO);
    }

    protected void addProgressBar(ModularWindow.Builder builder, BasicUIProperties uiProperties) {
        boolean isSteamPowered = isSteampowered();
        RecipeMap<?> recipeMap = getRecipeMap();
        if (!isSteamPowered && uiProperties.progressBarTexture == null) {
            if (recipeMap != null) {
                // Require progress bar texture for machines working with recipemap, otherwise permit
                throw new RuntimeException("Missing progressbar texture for " + recipeMap.unlocalizedName);
            } else {
                return;
            }
        }
        if (isSteamPowered && uiProperties.progressBarTextureSteam == null) {
            if (recipeMap != null) {
                throw new RuntimeException("Missing steam progressbar texture for " + recipeMap.unlocalizedName);
            } else {
                return;
            }
        }

        builder.widget(
            setNEITransferRect(
                new ProgressBar()
                    .setProgress(() -> maxProgresstime() != 0 ? (float) getProgresstime() / maxProgresstime() : 0)
                    .setTexture(
                        isSteamPowered ? uiProperties.progressBarTextureSteam.get(getSteamVariant())
                            : uiProperties.progressBarTexture.get(),
                        uiProperties.progressBarImageSize)
                    .setDirection(uiProperties.progressBarDirection)
                    .setPos(uiProperties.progressBarPos)
                    .setSize(uiProperties.progressBarSize),
                uiProperties.neiTransferRectId));
        addProgressBarSpecialTextures(builder, uiProperties);
    }

    /**
     * Override this as needed instead of calling.
     */
    protected SlotWidget createItemInputSlot(int index, IDrawable[] backgrounds, Pos2d pos) {
        return (SlotWidget) new SlotWidget(inventoryHandler, getInputSlot() + index).setAccess(true, true)
            .setBackground(backgrounds)
            .setPos(pos);
    }

    /**
     * Override this as needed instead of calling.
     */
    protected SlotWidget createItemOutputSlot(int index, IDrawable[] backgrounds, Pos2d pos) {
        return (SlotWidget) new SlotWidget(inventoryHandler, getOutputSlot() + index).setAccess(true, false)
            .setBackground(backgrounds)
            .setPos(pos);
    }

    /**
     * Override this as needed instead of calling.
     */
    protected SlotWidget createSpecialSlot(IDrawable[] backgrounds, Pos2d pos, BasicUIProperties uiProperties) {
        return (SlotWidget) new SlotWidget(inventoryHandler, getSpecialSlotIndex()).setAccess(true, true)
            .disableShiftInsert()
            .setGTTooltip(
                () -> mTooltipCache.getData(uiProperties.useSpecialSlot ? SPECIAL_SLOT_TOOLTIP : UNUSED_SLOT_TOOLTIP))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(backgrounds)
            .setPos(pos);
    }

    protected FluidSlotWidget createFluidInputSlot(IDrawable[] backgrounds, Pos2d pos) {
        return (FluidSlotWidget) new FluidSlotWidget(fluidTank).setBackground(backgrounds)
            .setPos(pos);
    }

    protected FluidSlotWidget createFluidOutputSlot(IDrawable[] backgrounds, Pos2d pos) {
        return (FluidSlotWidget) new FluidSlotWidget(fluidOutputTank).setInteraction(true, false)
            .setBackground(backgrounds)
            .setPos(pos);
    }

    @Override
    protected SlotWidget createChargerSlot(int x, int y) {
        if (isSteampowered()) {
            return (SlotWidget) createChargerSlot(x, y, UNUSED_SLOT_TOOLTIP, GTValues.emptyStringArray)
                .setBackground(getGUITextureSet().getItemSlot());
        } else {
            return super.createChargerSlot(x, y);
        }
    }

    protected CycleButtonWidget createItemAutoOutputButton() {
        return (CycleButtonWidget) new CycleButtonWidget().setToggle(() -> mItemTransfer, val -> mItemTransfer = val)
            .setStaticTexture(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM)
            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
            .setGTTooltip(() -> mTooltipCache.getData(ITEM_TRANSFER_TOOLTIP))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(25, 62)
            .setSize(18, 18);
    }

    protected CycleButtonWidget createFluidAutoOutputButton() {
        return (CycleButtonWidget) new CycleButtonWidget().setToggle(() -> mFluidTransfer, val -> mFluidTransfer = val)
            .setStaticTexture(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID)
            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
            .setGTTooltip(() -> mTooltipCache.getData(FLUID_TRANSFER_TOOLTIP))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(7, 62)
            .setSize(18, 18);
    }

    // Used for ui syncing
    private long getSteamVar = 0;

    protected Widget createSteamProgressBar(ModularWindow.Builder builder) {
        builder.widget(new FakeSyncWidget.LongSyncer(this::getSteamVar, val -> getSteamVar = val));

        boolean isSteel = getSteamVariant() == SteamVariant.STEEL;
        builder.widget(
            new DrawableWidget().setDrawable(isSteel ? GTUITextures.STEAM_GAUGE_BG_STEEL : GTUITextures.STEAM_GAUGE_BG)
                .dynamicTooltip(
                    () -> Collections.singletonList(
                        translateToLocalFormatted(
                            STEAM_AMOUNT_LANGKEY,
                            numberFormat.format(getSteamVar * 2), // internal steam storage is done at a 2:1 ratio
                            numberFormat.format(maxSteamStore() * 2))))// internal steam storage is done at a 2:1 ratio
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setUpdateTooltipEveryTick(true)
                .setSize(48, 42)
                .setPos(-48, -8));

        return new DrawableWidget().setDrawable(new CircularGaugeDrawable(() -> (float) getSteamVar / maxSteamStore()))
            .setPos(-48 + 21, -8 + 21)
            .setSize(18, 4);
    }

    protected Widget setNEITransferRect(Widget widget, String transferRectID) {
        if (GTUtility.isStringInvalid(transferRectID)) {
            return widget;
        }
        final String transferRectTooltip;
        if (isSteampowered()) {
            transferRectTooltip = translateToLocalFormatted(
                NEI_TRANSFER_STEAM_TOOLTIP,
                overclockDescriber.getTierString());
        } else {
            transferRectTooltip = translateToLocalFormatted(
                NEI_TRANSFER_VOLTAGE_TOOLTIP,
                overclockDescriber.getTierString());
        }
        widget.setNEITransferRect(transferRectID, new Object[] { overclockDescriber }, transferRectTooltip);
        return widget;
    }

    protected void addProgressBarSpecialTextures(ModularWindow.Builder builder, BasicUIProperties uiProperties) {
        if (isSteampowered()) {
            for (Pair<SteamTexture, Pair<Size, Pos2d>> specialTexture : uiProperties.specialTexturesSteam) {
                builder.widget(
                    new DrawableWidget().setDrawable(
                        specialTexture.getLeft()
                            .get(getSteamVariant()))
                        .setSize(
                            specialTexture.getRight()
                                .getLeft())
                        .setPos(
                            specialTexture.getRight()
                                .getRight()));
            }
        } else {
            for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : uiProperties.specialTextures) {
                builder.widget(
                    new DrawableWidget().setDrawable(specialTexture.getLeft())
                        .setSize(
                            specialTexture.getRight()
                                .getLeft())
                        .setPos(
                            specialTexture.getRight()
                                .getRight()));
            }
        }
    }

    protected DrawableWidget createErrorStatusArea(ModularWindow.Builder builder, IDrawable picture) {
        return (DrawableWidget) new DrawableWidget().setDrawable(picture)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setEnabled(
                widget -> !widget.getTooltip()
                    .isEmpty())
            .dynamicTooltip(this::getErrorDescriptions)
            .dynamicTooltipShift(this::getErrorDescriptionsShift)
            .setPos(79, 44)
            .setSize(18, 18)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> mStuttering, val -> mStuttering = val),
                builder,
                (widget, val) -> widget.notifyTooltipChange())
            .attachSyncer(
                new FakeSyncWidget.IntegerSyncer(this::getErrorDisplayID, this::setErrorDisplayID),
                builder,
                (widget, val) -> widget.notifyTooltipChange());
    }

    protected List<String> getErrorDescriptions() {
        final GTTooltipDataCache.TooltipData tooltip = getErrorTooltip();
        return tooltip != null ? tooltip.text : Collections.emptyList();
    }

    protected List<String> getErrorDescriptionsShift() {
        final GTTooltipDataCache.TooltipData tooltip = getErrorTooltip();
        return tooltip != null ? tooltip.shiftText : Collections.emptyList();
    }

    protected GTTooltipDataCache.TooltipData getErrorTooltip() {
        if (isSteampowered()) {
            if (cannotVentSteam()) {
                return mTooltipCache.getData(STALLED_VENT_TOOLTIP);
            }
        }
        if (mStuttering) {
            return mTooltipCache.getData(
                STALLED_STUTTERING_TOOLTIP,
                translateToLocal(POWER_SOURCE_KEY + (isSteampowered() ? "steam" : "power")));
        }
        return null;
    }

    private boolean cannotVentSteam() {
        return (getErrorDisplayID() & 64) != 0;
    }

    protected static int getCapacityForTier(int tier) {
        return switch (tier) {
            case 0 -> 8000;
            case 1 -> 16000;
            case 2 -> 32000;
            case 3 -> 64000;
            case 4 -> 128000;
            default -> 256000;
        };
    }
}
