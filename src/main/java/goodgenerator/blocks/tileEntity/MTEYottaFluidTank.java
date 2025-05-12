package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.CharExchanger.formatNumber;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTStructureUtility.*;
import static java.lang.String.valueOf;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.Reference.MODID;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableArray;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTEYOTTAHatch;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.LongRunningAverage;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.widget.FluidDisplaySyncHandler;
import gregtech.common.gui.modularui.widget.FluidSlotDisplayOnly;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.modularui2.widget.TransparentSingleChildWidget;
import tectech.thing.metaTileEntity.multi.base.Parameter;

public class MTEYottaFluidTank extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<MTEYottaFluidTank> multiDefinition = null;
    protected final ArrayList<MTEYOTTAHatch> mYottaHatch = new ArrayList<>();

    private static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);

    /** Tank capacity */
    public BigInteger mStorage = BigInteger.ZERO;
    /** Amount of fluid millibuckets currently in the tank */
    public BigInteger mStorageCurrent = BigInteger.ZERO;
    /**
     * Fluid type currently stored in the tank, can be null. Stack size is always 1, real amount is stored in
     * mStorageCurrent.
     */
    public FluidStack mFluid = null;
    /**
     * Fluid type currently the tank is currently locked to, can be null. Stack size is always 1, real amount is stored
     * in mStorageCurrent.
     */
    public FluidStack mLockedFluid = null;
    protected boolean isFluidLocked = false;
    protected int glassTier = -1;
    protected int maxCell;
    protected final String YOTTANK_BOTTOM = mName + "bottom";
    protected final String YOTTANK_MID = mName + "mid";
    protected final String YOTTANK_TOP = mName + "top";
    protected final NumberFormatMUI numberFormat = new NumberFormatMUI();
    private int workTickCounter = 0;

    public static final BigInteger MAX_INT_BIGINT = BigInteger.valueOf(Integer.MAX_VALUE);

    protected boolean voidExcessEnabled = false;

    private final LongRunningAverage fluidInputValues1m = new LongRunningAverage(60 * 20);
    private final LongRunningAverage fluidOutputValues1m = new LongRunningAverage(60 * 20);

    protected Parameter.IntegerParameter tickRateUpdate;

    @Override
    protected void initParameters() {
        tickRateUpdate = new Parameter.IntegerParameter(
            20,
            () -> 1,
            () -> 100,
            "gt.blockmachines.YottaFluidTank.cfgi.0");
        parameterList.add(tickRateUpdate);
    }

    public MTEYottaFluidTank(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTEYottaFluidTank(String name) {
        super(name);
    }

    public String getCap() {
        return mStorage.toString(10);
    }

    public String getStored() {
        return mStorageCurrent.toString(10);
    }

    public String getFluidName() {
        if (mFluid == null) return StatCollector.translateToLocal("scanner.info.YOTTank.empty");
        return mFluid.getLocalizedName();
    }

    public String getLockedFluidName() {
        if (!isFluidLocked) return StatCollector.translateToLocal("scanner.info.YOTTank.none");
        if (mLockedFluid == null) return StatCollector.translateToLocal("scanner.info.YOTTank.next");
        return mLockedFluid.getLocalizedName();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        String tAmount = aNBT.getString("mStorage");
        String tAmountCurrent = aNBT.getString("mStorageCurrent");
        if (StringUtils.isEmpty(tAmount)) tAmount = "0";
        if (StringUtils.isEmpty(tAmountCurrent)) tAmountCurrent = "0";
        mStorage = new BigInteger(tAmount, 10);
        mStorageCurrent = new BigInteger(tAmountCurrent, 10);
        mFluid = FluidRegistry.getFluidStack(aNBT.getString("mFluidName"), 1);
        mLockedFluid = FluidRegistry.getFluidStack(aNBT.getString("mLockedFluidName"), 1);
        voidExcessEnabled = aNBT.getBoolean("voidExcessEnabled");
        isFluidLocked = aNBT.getBoolean("isFluidLocked");
        tickRateUpdate.setValue(aNBT.getInteger("tickRate"));
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mStorage", mStorage.toString(10));
        aNBT.setString("mStorageCurrent", mStorageCurrent.toString(10));
        aNBT.setString(
            "mFluidName",
            mFluid == null ? ""
                : mFluid.getFluid()
                    .getName());
        aNBT.setString(
            "mLockedFluidName",
            mLockedFluid == null ? ""
                : mLockedFluid.getFluid()
                    .getName());
        aNBT.setBoolean("voidExcessEnabled", voidExcessEnabled);
        aNBT.setBoolean("isFluidLocked", isFluidLocked);
        aNBT.setInteger("tickRate", tickRateUpdate.getValue());
        super.saveNBTData(aNBT);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        this.mEUt = 0;
        this.mMaxProgresstime = 20;
        return SimpleCheckRecipeResult.ofSuccess("");
    }

    public boolean getIsVoidExcessEnabled() {
        return voidExcessEnabled;
    }

    /**
     * Attempts to remove {@code amount} of fluid from the tank if possible, does not do partial removals.
     *
     * @param amount The millibucket amount of the fluid to remove
     * @return True if successfully removed amount, false if no fluid was removed.
     */
    public boolean reduceFluid(long amount) {
        final BigInteger bigAmount = BigInteger.valueOf(amount);
        if (mStorageCurrent.compareTo(bigAmount) < 0) {
            return false;
        } else {
            mStorageCurrent = mStorageCurrent.subtract(bigAmount);
            return true;
        }
    }

    /**
     * Attempts to put {@code amount} of fluid into the tank if possible, fails if there's not enough space for all of
     * it.
     *
     * @param amount The millibucket amount of the fluid to insert
     * @param doFill Whether to actually fill, or just simulate a fill
     * @return True if successfully added the given amount of fluid to the tank, false if failed.
     */
    public boolean addFluid(long amount, boolean doFill) {
        final BigInteger bigAmount = BigInteger.valueOf(amount);
        final BigInteger newTotal = mStorageCurrent.add(bigAmount);
        if (newTotal.compareTo(mStorage) > 0) {
            return false;
        } else {
            if (doFill) {
                mStorageCurrent = newTotal;
            }
            return true;
        }
    }

    // Avoid allocating a new array on every query
    private final FluidTankInfo[] tankInfoCache = new FluidTankInfo[1];

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        int fluidSize = mStorageCurrent.compareTo(MAX_INT_BIGINT) >= 0 ? Integer.MAX_VALUE : mStorageCurrent.intValue();
        int tankCapacity = mStorage.compareTo(MAX_INT_BIGINT) >= 0 ? Integer.MAX_VALUE : mStorage.intValue();
        final boolean cacheNeedsRecreation;
        if (tankInfoCache[0] == null || tankInfoCache[0].capacity != tankCapacity) {
            cacheNeedsRecreation = true;
        } else if (tankInfoCache[0].fluid == null) {
            cacheNeedsRecreation = (mFluid != null);
        } else {
            cacheNeedsRecreation = !tankInfoCache[0].fluid.isFluidEqual(mFluid);
        }
        if (cacheNeedsRecreation) {
            final FluidStack storedFluid = GTUtility.copyAmount(fluidSize, mFluid);
            tankInfoCache[0] = new FluidTankInfo(storedFluid, tankCapacity);
        } else if (mFluid != null) {
            tankInfoCache[0].fluid.amount = fluidSize;
        }
        return tankInfoCache;
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mYottaHatch.clear();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mStorage = BigInteger.ZERO;
        glassTier = -1;
        maxCell = 0;
        if (!structureCheck_EM(YOTTANK_BOTTOM, 2, 0, 0)) return false;
        int cnt = 0;
        while (structureCheck_EM(YOTTANK_MID, 2, cnt + 1, 0)) {
            cnt++;
        }
        if (cnt > 15 || cnt < 1) return false;
        if (!structureCheck_EM(YOTTANK_TOP, 2, cnt + 2, 0)) return false;
        // maxCell+1 = Tier of highest Cell. glassTier is the glass voltage tier
        if (maxCell + 3 <= glassTier) {
            if (mStorage.compareTo(mStorageCurrent) < 0) mStorageCurrent = mStorage;
            if (mFluid == null) {
                mStorageCurrent = BigInteger.ZERO;
            }
            return true;
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTEYottaFluidTank> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEYottaFluidTank>builder()
                .addShape(
                    YOTTANK_BOTTOM,
                    transpose(
                        new String[][] { { "MM~MM", "MCCCM", "MCCCM", "MCCCM", "MMMMM" },
                            { "     ", " OOO ", " OOO ", " OOO ", "     " } }))
                .addShape(YOTTANK_MID, transpose(new String[][] { { "GGGGG", "GRRRG", "GRRRG", "GRRRG", "GGGGG" } }))
                .addShape(
                    YOTTANK_TOP,
                    transpose(
                        new String[][] { { "FFFFF", "F   F", "F   F", "F   F", "FFFFF" },
                            { "CCCCC", "CIIIC", "CIIIC", "CIIIC", "CCCCC" } }))
                .addElement('C', ofBlock(Loaders.yottaFluidTankCasing, 0))
                .addElement('G', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('R', ofChain(cells(10)))
                .addElement('F', ofFrame(Materials.Steel))
                .addElement(
                    'I',
                    buildHatchAdder(MTEYottaFluidTank.class).atLeast(gregtech.api.enums.HatchElement.InputHatch)
                        .casingIndex(1537)
                        .dot(1)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .addElement(
                    'M',
                    buildHatchAdder(MTEYottaFluidTank.class).atLeast(gregtech.api.enums.HatchElement.Maintenance)
                        .casingIndex(1537)
                        .dot(2)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .addElement(
                    'O',
                    buildHatchAdder(MTEYottaFluidTank.class).atLeast(gregtech.api.enums.HatchElement.OutputHatch)
                        .adder(MTEYottaFluidTank::addOutput)
                        .casingIndex(1537)
                        .dot(1)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .build();
        }
        return multiDefinition;
    }

    public List<IStructureElement<MTEYottaFluidTank>> cells(int num) {
        List<IStructureElement<MTEYottaFluidTank>> out = new ArrayList<>();
        for (int i = 0; i < num; ++i) {
            int finalI = i;
            out.add(onElementPass(x -> {
                x.mStorage = x.mStorage.add(calStorage(finalI));
                x.maxCell = Math.max(x.maxCell, finalI);
            }, ofBlock(Loaders.yottaFluidTankCell, i)));
        }
        return out;
    }

    public final boolean addOutput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else {
                if (aMetaTileEntity instanceof MTEHatchOutput) {
                    ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mOutputHatches.add((MTEHatchOutput) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof MTEYOTTAHatch) {
                    // only one yothatch allowed
                    if (!this.mYottaHatch.isEmpty()) return false;
                    ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    ((MTEYOTTAHatch) aMetaTileEntity).setTank(this);
                    return this.mYottaHatch.add((MTEYOTTAHatch) aMetaTileEntity);
                }
            }
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        final ArrayList<String> info = new ArrayList<>();
        info.add(
            translateToLocal("scanner.info.YOTTank.1") + " "
                + translateToLocal(
                    EnumChatFormatting.YELLOW + formatNumber(getFluidName() + EnumChatFormatting.RESET)));
        info.add(
            translateToLocal("scanner.info.YOTTank.0") + " "
                + translateToLocal(
                    EnumChatFormatting.GREEN + formatNumber(getCap()) + EnumChatFormatting.RESET + " L"));
        info.add(
            translateToLocal("scanner.info.YOTTank.2") + " "
                + translateToLocal(
                    EnumChatFormatting.GREEN + formatNumber(getStored())
                        + EnumChatFormatting.RESET
                        + " L"
                        + " ("
                        + EnumChatFormatting.GREEN
                        + getPercent()
                        + "%"
                        + EnumChatFormatting.RESET
                        + ")"));
        info.add(getTimeTo());
        info.add(
            StatCollector.translateToLocal("scanner.info.YOTTank.3") + " "
                + EnumChatFormatting.YELLOW
                + formatNumber(getLockedFluidName()));
        final String[] a = new String[info.size()];
        return info.toArray(a);
    }

    private String getPercent() {
        if (mStorage.signum() == 0) return "0";
        return valueOf(
            mStorageCurrent.multiply(BigInteger.valueOf(10000))
                .divide(mStorage)
                .doubleValue() / 100);
    }

    private String getTimeTo() {
        double avgIn = fluidInputValues1m.avgLong();
        double avgOut = fluidOutputValues1m.avgLong();
        double cap = mStorage.doubleValue();
        double stored = mStorageCurrent.doubleValue();
        if (avgIn >= avgOut) {
            if (avgIn > 0) {
                double timeToFull = (cap - stored) / (avgIn - avgOut) / 20;
                return "Time to Full: " + formatTime(timeToFull, true);
            }
            return "Time to Something: Infinity years";
        } else {
            double timeToEmpty = stored / (avgOut - avgIn) / 20;
            return "Time to Empty: " + formatTime(timeToEmpty, false);
        }
    }

    private String formatTime(double time, boolean fill) {
        if (time < 1) {
            return "Completely " + (fill ? "full" : "empty");
        } else if (time < 60) {
            return String.format("%.2f seconds", time);
        } else if (time < 3600) {
            return String.format("%.2f minutes", time / 60);
        } else if (time < 86400) {
            return String.format("%.2f hours", time / 3600);
        } else if (time < 31536000) {
            return String.format("%.2f days", time / 86400);
        } else {
            double y = time / 31536000;
            return y < 9_000 ? String.format("%.2f years", y) : "It's over 9000 years!!";
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Tank")
            .addInfo("The max output speed is decided by the amount of stored liquid and the output hatch's capacity.")
            .addInfo("The max fluid cell tier is limited by the glass tier.")
            .addInfo("HV glass for T1, EV glass for T2, IV glass for T3. . .")
            .addInfo("The max height of the cell blocks is 15.")
            .beginVariableStructureBlock(5, 5, 1, 15, 5, 5, false)
            .addController("Front of the second layer")
            .addCasingInfoExactly("Steel Frame Box", 16, false)
            .addCasingInfoRange("Any Tiered Glass", 16, 240, true)
            .addCasingInfoRange("Fluid Cell Block", 9, 135, true)
            .addCasingInfoRange("YOTTank Casing", 25, 43, false)
            .addInputHatch("Hint block with dot 1")
            .addOutputHatch("Hint block with dot 3")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    private static final BigInteger[] storageWithCells;

    static {
        storageWithCells = new BigInteger[10];
        final BigInteger baseStorage = BigInteger.valueOf(1_000_000);
        final BigInteger storageMultiplier = BigInteger.valueOf(100);
        BigInteger currentStorage = baseStorage;
        for (int i = 0; i < 10; i++) {
            storageWithCells[i] = currentStorage;
            currentStorage = currentStorage.multiply(storageMultiplier);
        }
    }

    public BigInteger calStorage(int meta) {
        return storageWithCells[meta];
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        super.onRunningTick(aStack);

        long totalInput = 0;
        long totalOutput = 0;

        long tickRate = Math.min(100L, Math.max(1L, (long) tickRateUpdate.getValue()));
        ++workTickCounter;
        if (workTickCounter < tickRate) {
            fluidInputValues1m.update(totalInput);
            fluidOutputValues1m.update(totalOutput);
            return true;
        }
        workTickCounter = 0;

        List<FluidStack> tStore = getStoredFluids();
        for (FluidStack tFluid : tStore) {
            if (tFluid == null) continue;
            if (isFluidLocked) {
                if (mLockedFluid != null) {
                    if (!tFluid.isFluidEqual(mLockedFluid)) continue;
                } else {
                    mLockedFluid = tFluid.copy();
                    mLockedFluid.amount = 1;
                }
            }
            if (mFluid == null || tFluid.isFluidEqual(mFluid)) {
                if (mFluid == null) {
                    mFluid = tFluid.copy();
                    mFluid.amount = 1;
                }
                if (addFluid(tFluid.amount, true)) {
                    totalInput += tFluid.amount;
                    tFluid.amount = 0;
                } else {
                    if (voidExcessEnabled) {
                        tFluid.amount = 0;
                    } else {
                        final BigInteger delta = mStorage.subtract(mStorageCurrent);
                        tFluid.amount -= delta.intValueExact();
                    }
                    mStorageCurrent = mStorage;
                }
            }
        }

        if (mStorageCurrent.compareTo(BigInteger.ZERO) <= 0) {
            mFluid = null;
        }

        if (mFluid != null) {
            // Try to drain 1% of the tank per tick
            int outputAmount = mStorageCurrent.divide(ONE_HUNDRED)
                .min(MAX_INT_BIGINT)
                .max(BigInteger.ONE)
                .intValueExact();
            if (outputAmount != 1) outputAmount = (int) Math.min(Integer.MAX_VALUE, (long) outputAmount * tickRate);
            else outputAmount = Math.min(mStorageCurrent.intValueExact(), outputAmount * (int) tickRate);

            final int originalOutputAmount = outputAmount;

            for (final MTEHatch outputHatch : mOutputHatches) {
                final FluidStack fluidInHatch = outputHatch.mFluid;

                final int remainingHatchSpace;
                if (fluidInHatch != null) {
                    if (fluidInHatch.isFluidEqual(mFluid)) {
                        remainingHatchSpace = outputHatch.getCapacity() - fluidInHatch.amount;
                    } else {
                        continue;
                    }
                } else {
                    remainingHatchSpace = outputHatch.getCapacity();
                }

                final int amountToFillHatch = Math.min(remainingHatchSpace, outputAmount);
                if (amountToFillHatch <= 0) {
                    continue;
                }
                final FluidStack fillStack = mFluid.copy();
                fillStack.amount = amountToFillHatch;
                final int transferredAmount = outputHatch.fill(fillStack, true);
                totalOutput += transferredAmount;
                outputAmount -= transferredAmount;
            }

            final int totalDrainedAmount = originalOutputAmount - outputAmount;
            if (totalDrainedAmount > 0) {
                mStorageCurrent = mStorageCurrent.subtract(BigInteger.valueOf(totalDrainedAmount));
                if (mStorageCurrent.signum() < 0) {
                    throw new IllegalStateException(
                        "YOTTank drained beyond its fluid amount, indicating logic bug: " + mStorageCurrent);
                }
            }
        }
        fluidInputValues1m.update(totalInput);
        fluidOutputValues1m.update(totalOutput);
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(YOTTANK_BOTTOM, 2, 0, 0, stackSize, hintsOnly);
        int height = stackSize.stackSize;
        if (height > 15) height = 15;
        structureBuild_EM(YOTTANK_TOP, 2, height + 2, 0, stackSize, hintsOnly);
        while (height > 0) {
            structureBuild_EM(YOTTANK_MID, 2, height, 0, stackSize, hintsOnly);
            height--;
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack toolStack) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            voidExcessEnabled ^= true;
            aPlayer.addChatMessage(
                new ChatComponentTranslation(
                    voidExcessEnabled ? "yottank.chat.voidExcessEnabled" : "yottank.chat.voidExcessDisabled"));
            return true;
        }
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack toolStack) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            if (mLockedFluid == null) {
                if (mFluid != null) {
                    mLockedFluid = mFluid;
                    aPlayer.addChatMessage(new ChatComponentTranslation("yottank.chat.1", getFluidName()));
                } else {
                    aPlayer.addChatMessage(new ChatComponentTranslation("yottank.chat.2"));
                }
                isFluidLocked = true;
            } else {
                mLockedFluid = null;
                isFluidLocked = false;
                aPlayer.addChatMessage(new ChatComponentTranslation("yottank.chat.0"));
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("YOTTank.hint", 8);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEYottaFluidTank(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1537),
                TextureFactory.builder()
                    .addIcon(textureFontOn)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .extFacing()
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1537), TextureFactory.builder()
                .addIcon(textureFontOff)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
                    .extFacing()
                    .glow()
                    .build() };
        } else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1537) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        built += survivialBuildPiece(YOTTANK_BOTTOM, stackSize, 2, 0, 0, elementBudget, env, false, true);
        int height = stackSize.stackSize;
        if (height > 15) height = 15;
        built += survivialBuildPiece(YOTTANK_TOP, stackSize, 2, height + 2, 0, elementBudget - built, env, false, true);
        while (height > 0) {
            built += survivialBuildPiece(YOTTANK_MID, stackSize, 2, height, 0, elementBudget - built, env, false, true);
            height--;
        }
        return built;
    }

    @Override
    public boolean shouldDisplayCheckRecipeResult() {
        return false;
    }

    @Override
    public boolean forceUseMui2() {
        return true;
    }

    @Override
    protected int[] mainTerminalSize() {
        return new int[] { 135, 91 };
    }

    @Override
    protected int[] machineInfoSize() {
        return new int[] { 123, 85 };
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TRANSPARENT_FLUID_SLOT;
    }

    @Override
    public void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot, PanelSyncManager syncManager,
        ModularPanel parentPanel) {

        DoubleSyncValue maxStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("maxStorage:0");

        DoubleSyncValue currentStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("currentStorage:0");

        StringSyncValue percentageSyncer = (StringSyncValue) syncManager.getSyncHandler("percentage:0");

        StringSyncValue timeTo = new StringSyncValue(this::getTimeTo);
        syncManager.syncValue("timeTo", timeTo);

        machineInfo.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocal("gui.YOTTank.0") + " "
                    + NumberFormat.formatWithMaxDigits(maxStorageSyncer.getDoubleValue(), 3))
                .asWidget()
                .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                .color(COLOR_TEXT_WHITE.get())
                .widthRel(1)
                .marginBottom(2)
                .setEnabledIf(w -> getErrorDisplayID() == 0));

        machineInfo.child(
            IKey.dynamic(
                () -> StatCollector.translateToLocal("gui.YOTTank.2") + " "
                    + (currentStorageSyncer.getValue() == 0 ? "0"
                        : NumberFormat.formatWithMaxDigits(currentStorageSyncer.getValue(), 3))
                    + " ("
                    + EnumChatFormatting.GREEN
                    + percentageSyncer.getStringValue()
                    + "%"
                    + EnumChatFormatting.RESET
                    + ")")
                .asWidget()
                .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                .color(COLOR_TEXT_WHITE.get())
                .widthRel(1)
                .marginBottom(2)
                .setEnabledIf(w -> getErrorDisplayID() == 0));

        machineInfo.child(
            IKey.dynamic(timeTo::getStringValue)
                .asWidget()
                .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                .color(COLOR_TEXT_WHITE.get())
                .widthRel(1)
                .marginBottom(2)
                .setEnabledIf(w -> getErrorDisplayID() == 0));
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager) {

        DoubleSyncValue maxStorageSyncer = new DoubleSyncValue(() -> mStorage.doubleValue());
        syncManager.syncValue("maxStorage", maxStorageSyncer);

        DoubleSyncValue currentStorageSyncer = new DoubleSyncValue(() -> mStorageCurrent.doubleValue());
        syncManager.syncValue("currentStorage", currentStorageSyncer);

        StringSyncValue percentageSyncer = new StringSyncValue(this::getPercent);
        syncManager.syncValue("percentage", percentageSyncer);

        FluidDisplaySyncHandler storedFluid = new FluidDisplaySyncHandler(() -> mFluid);
        syncManager.syncValue("storedFluid", storedFluid);

        FluidDisplaySyncHandler lockedFluid = new FluidDisplaySyncHandler(
            () -> mLockedFluid,
            aFluid -> mLockedFluid = aFluid);
        syncManager.syncValue("lockedFluid", lockedFluid);

        BooleanSyncValue locked = new BooleanSyncValue(() -> isFluidLocked, bool -> isFluidLocked = bool);
        syncManager.syncValue("isLocked", locked);

        com.cleanroommc.modularui.drawable.UITexture bg = com.cleanroommc.modularui.drawable.UITexture.builder()
            .location(MODID, "gui/background/screen_blue")
            .adaptable(2)
            .imageSize(90, 72)
            .canApplyTheme(true)
            .build();
        com.cleanroommc.modularui.drawable.UITexture bgNoInv = com.cleanroommc.modularui.drawable.UITexture.builder()
            .location(MODID, "gui/background/screen_blue_no_inventory")
            .canApplyTheme(true)
            .build();
        com.cleanroommc.modularui.drawable.UITexture mesh = com.cleanroommc.modularui.drawable.UITexture.builder()
            .location(MODID, "gui/overlay_slot/mesh")
            .canApplyTheme(true)
            .build();
        com.cleanroommc.modularui.drawable.UITexture heatSinkSmall = com.cleanroommc.modularui.drawable.UITexture
            .builder()
            .location(MODID, "gui/picture/heat_sink_small")
            .canApplyTheme(true)
            .build();
        ModularPanel panel = new ModularPanel("tt_multiblock");
        int textBoxToInventoryGap = 26;
        panel.size(198, 181 + textBoxToInventoryGap)
            .padding(4);

        registerSyncValues(panel, syncManager);

        ListWidget<IWidget, ?> machineInfo = new ListWidget<>().size(machineInfoSize()[0], machineInfoSize()[1])
            .pos(6, 3);

        Flow panelColumn = new Column().sizeRel(1);
        Flow panelTopRow = new Row().widthRel(1)
            .height(91);
        panelColumn.child(panelTopRow);
        panelTopRow.child(
            new SingleChildWidget<>().size(mainTerminalSize()[0], mainTerminalSize()[1])
                .overlay(bg)
                .child(machineInfo)
                .alignX(0));
        final ItemStackHandler invSlot = new ItemStackHandler(1);
        Flow inventoryRow = new Row().widthRel(1)
            .height(90)
            .alignX(0);
        Flow buttonColumn = new Column().width(18)
            .leftRel(1, -2, 1);
        if (doesBindPlayerInventory()) {
            inventoryRow.child(
                SlotGroupWidget.playerInventory(0)
                    .leftRel(0)
                    .marginLeft(4));
        }

        Flow panelGap = new Row().widthRel(1)
            .paddingRight(6)
            .height(textBoxToInventoryGap);
        insertThingsInGap(panelGap, syncManager, panel);
        panelColumn.child(panelGap);

        FluidSlotDisplayOnly fluidDisplay = new FluidSlotDisplayOnly(
            () -> Double.parseDouble(percentageSyncer.getStringValue()) / 100) {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!locked.getBoolValue()) {
                    if (storedFluid.getValue() != null) {
                        lockedFluid.setValue(storedFluid.getValue());
                    }
                    locked.setBoolValue(true);
                } else {
                    lockedFluid.setValue(null);
                    locked.setBoolValue(false);
                }
                return Result.SUCCESS;
            }

        }.syncHandler("storedFluid")
            .tooltipBuilder(t -> {
                FluidStack fluidStack = storedFluid.getValue();
                FluidStack lockedStack = lockedFluid.getValue();
                if (locked.getBoolValue() && fluidStack == null && lockedStack != null) {
                    t.clearText();
                    t.add(
                        lockedStack.getFluid()
                            .getLocalizedName())
                        .newLine()
                        .add(
                            EnumChatFormatting.BLUE
                                + (currentStorageSyncer.getValue() == 0 ? "0"
                                    : NumberFormat.formatWithMaxDigits(currentStorageSyncer.getValue(), 3))
                                + "/"
                                + NumberFormat.formatWithMaxDigits(maxStorageSyncer.getValue(), 3));
                } else if (fluidStack == null) {
                    t.clearText();
                    t.add("Empty");
                } else {
                    t.clearText();
                    t.add(
                        fluidStack.getFluid()
                            .getLocalizedName())
                        .newLine()
                        .add(
                            EnumChatFormatting.BLUE
                                + NumberFormat.formatWithMaxDigits(currentStorageSyncer.getValue(), 3)
                                + "/"
                                + NumberFormat.formatWithMaxDigits(maxStorageSyncer.getValue(), 3)
                                + EnumChatFormatting.RESET
                                + " ("
                                + EnumChatFormatting.GREEN
                                + percentageSyncer.getStringValue()
                                + "%"
                                + EnumChatFormatting.RESET
                                + ")");
                }
                if (locked.getBoolValue()) t.newLine()
                    .add("" + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + "Locked");
                else t.newLine()
                    .add("" + EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC + "Click to Lock Fluid!");
            });

        panelTopRow.child(
            new SingleChildWidget<>()
                .overlay(
                    com.cleanroommc.modularui.drawable.UITexture.fullImage(GregTech.ID, "gui/picture/yottank_overlay"))
                .size(48, 88)
                .pos(139, 1));

        panelTopRow.child(
            fluidDisplay.pos(146, 8)
                .size(34, 72));

        panelTopRow.child(
            new TransparentSingleChildWidget()
                .overlay(
                    com.cleanroommc.modularui.drawable.UITexture
                        .fullImage(GregTech.ID, "gui/picture/yottank_overlay_lines"))
                .size(32, 72)
                .pos(147, 9));

        insertTexts(machineInfo, invSlot, syncManager, panel);
        addTitleTextStyle(panel, this.getLocalName());

        if (shouldMakePowerPassButton()) addPowerPassButton(buttonColumn, textBoxToInventoryGap);
        if (shouldMakeEditParametersButtonEnabled()) addEditParametersButton(panel, syncManager, buttonColumn);
        if (shouldMakePowerSwitchButtonEnabled()) addPowerSwitchButtton(buttonColumn);

        panelTopRow.child(
            new SingleChildWidget<>()
                .overlay(com.cleanroommc.modularui.drawable.UITexture.fullImage(MODID, "gui/picture/tectech_logo_dark"))
                .size(18, 18)
                .pos(134 - 18 - 2, 90 - 18 - 2));

        if (doesBindPlayerInventory()) {
            buttonColumn.child(
                new ItemSlot().slot(
                    SyncHandlers.itemSlot(invSlot, 0)
                        .singletonSlotGroup())
                    .marginTop(4)
                    .background(new DrawableArray(GuiTextures.SLOT_ITEM, mesh)));
            buttonColumn.child(
                new SingleChildWidget<>().size(18, 6)
                    .overlay(heatSinkSmall));
        }
        inventoryRow.child(buttonColumn);
        panelColumn.child(inventoryRow);

        return panel.child(panelColumn);
    }

    @Override
    public void insertThingsInGap(Flow panelGap, PanelSyncManager syncManager, ModularPanel parent) {}

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
