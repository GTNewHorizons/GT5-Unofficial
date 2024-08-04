package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_StructureUtility.*;
import static net.minecraft.util.StatCollector.translateToLocal;

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

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import goodgenerator.blocks.tileEntity.GTMetaTileEntity.YOTTAHatch;
import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.client.GUI.GG_UITextures;
import goodgenerator.loader.Loaders;
import goodgenerator.util.CharExchanger;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class YottaFluidTank extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QTANK");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QTANK_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_QCHEST");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_QCHEST_GLOW");

    protected IStructureDefinition<YottaFluidTank> multiDefinition = null;
    protected final ArrayList<YOTTAHatch> mYottaHatch = new ArrayList<>();

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
    protected int glassMeta;
    protected int maxCell;
    protected final String YOTTANK_BOTTOM = mName + "buttom";
    protected final String YOTTANK_MID = mName + "mid";
    protected final String YOTTANK_TOP = mName + "top";
    protected final NumberFormatMUI numberFormat = new NumberFormatMUI();
    private int workTickCounter = 0;

    public static final BigInteger MAX_INT_BIGINT = BigInteger.valueOf(Integer.MAX_VALUE);

    protected boolean voidExcessEnabled = false;

    protected Parameters.Group.ParameterIn tickRateSettings;

    /** Name of the tick rate setting */
    private static final INameFunction<YottaFluidTank> TICK_RATE_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.YottaFluidTank.cfgi.0");
    /** Status of the tick rate setting */
    private static final IStatusFunction<YottaFluidTank> TICK_RATE_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 100, 100);

    @Override
    protected void parametersInstantiation_EM() {
        tickRateSettings = parametrization.getGroup(9, true)
            .makeInParameter(1, 20, TICK_RATE_SETTING_NAME, TICK_RATE_STATUS);
    }

    public YottaFluidTank(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public YottaFluidTank(String name) {
        super(name);
    }

    public int getMeta() {
        return glassMeta;
    }

    public void setMeta(int meta) {
        glassMeta = meta;
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
            final FluidStack storedFluid = mFluid.copy();
            storedFluid.amount = fluidSize;
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
        glassMeta = 0;
        maxCell = 0;
        if (!structureCheck_EM(YOTTANK_BOTTOM, 2, 0, 0)) return false;
        int cnt = 0;
        while (structureCheck_EM(YOTTANK_MID, 2, cnt + 1, 0)) {
            cnt++;
        }
        if (cnt > 15 || cnt < 1) return false;
        if (!structureCheck_EM(YOTTANK_TOP, 2, cnt + 2, 0)) return false;
        // maxCell+1 = Tier of highest Cell. glassMeta is the glass voltage tier
        if (maxCell + 3 <= glassMeta) {
            if (mStorage.compareTo(mStorageCurrent) < 0) mStorageCurrent = mStorage;
            if (mFluid == null) {
                mStorageCurrent = BigInteger.ZERO;
            }
            return true;
        }
        return false;
    }

    @Override
    public IStructureDefinition<YottaFluidTank> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<YottaFluidTank>builder()
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
                .addElement(
                    'G',
                    withChannel(
                        "glass",
                        BorosilicateGlass.ofBoroGlass(
                            (byte) 0,
                            (byte) 1,
                            Byte.MAX_VALUE,
                            YottaFluidTank::setMeta,
                            te -> (byte) te.getMeta())))
                .addElement('R', ofChain(cells(10)))
                .addElement('F', ofFrame(Materials.Steel))
                .addElement(
                    'I',
                    buildHatchAdder(YottaFluidTank.class).atLeast(GT_HatchElement.InputHatch)
                        .casingIndex(1537)
                        .dot(1)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .addElement(
                    'M',
                    buildHatchAdder(YottaFluidTank.class).atLeast(GT_HatchElement.Maintenance)
                        .casingIndex(1537)
                        .dot(2)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .addElement(
                    'O',
                    buildHatchAdder(YottaFluidTank.class).atLeast(GT_HatchElement.OutputHatch)
                        .adder(YottaFluidTank::addOutput)
                        .casingIndex(1537)
                        .dot(1)
                        .buildAndChain(Loaders.yottaFluidTankCasing, 0))
                .build();
        }
        return multiDefinition;
    }

    public List<IStructureElement<YottaFluidTank>> cells(int num) {
        List<IStructureElement<YottaFluidTank>> out = new ArrayList<>();
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
                if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
                } else if (aMetaTileEntity instanceof YOTTAHatch) {
                    // only one yothatch allowed
                    if (!this.mYottaHatch.isEmpty()) return false;
                    ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                    ((YOTTAHatch) aMetaTileEntity).setTank(this);
                    return this.mYottaHatch.add((YOTTAHatch) aMetaTileEntity);
                }
            }
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocal("scanner.info.YOTTank.1"),
            StatCollector.translateToLocal(
                EnumChatFormatting.YELLOW + CharExchanger.formatNumber(getFluidName() + EnumChatFormatting.RESET)),

            StatCollector.translateToLocal("scanner.info.YOTTank.0"),
            StatCollector.translateToLocal(
                EnumChatFormatting.GREEN + CharExchanger.formatNumber(getCap()) + EnumChatFormatting.RESET + " L"),

            StatCollector.translateToLocal("scanner.info.YOTTank.2"),
            StatCollector.translateToLocal(
                EnumChatFormatting.GREEN + CharExchanger.formatNumber(getStored())
                    + EnumChatFormatting.RESET
                    + " L"
                    + " ("
                    + EnumChatFormatting.GREEN
                    + getPercent()
                    + "%"
                    + EnumChatFormatting.RESET
                    + ")"),

            StatCollector.translateToLocal("scanner.info.YOTTank.3"),
            StatCollector.translateToLocal(
                EnumChatFormatting.YELLOW + CharExchanger.formatNumber(getLockedFluidName())
                    + EnumChatFormatting.RESET) };
    }

    private String getPercent() {
        if (mStorage.signum() == 0) return "0";
        return mStorageCurrent.multiply(ONE_HUNDRED)
            .divide(mStorage)
            .toString();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid Tank")
            .addInfo("Controller block for the YOTTank.")
            .addInfo("The max output speed is decided by the amount of stored liquid and the output hatch's capacity.")
            .addInfo("The max fluid cell tier is limited by the glass tier.")
            .addInfo("HV glass for T1, EV glass for T2, IV glass for T3. . .")
            .addInfo("The max height of the cell blocks is 15.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginVariableStructureBlock(5, 5, 1, 15, 5, 5, false)
            .addController("Front of the second layer")
            .addCasingInfoExactly("Steel Frame Box", 16, false)
            .addCasingInfoRange("Glass (HV+)", 16, 240, true)
            .addCasingInfoRange("Fluid Cell Block", 9, 135, true)
            .addCasingInfoRange("YOTTank Casing", 25, 43, false)
            .addInputHatch("Hint block with dot 1")
            .addOutputHatch("Hint block with dot 3")
            .toolTipFinisher("Good Generator");
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
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            long tickRate = Math.min(100L, Math.max(1L, (long) tickRateSettings.get()));
            ++workTickCounter;
            if (workTickCounter < tickRate) {
                return true;
            }
            workTickCounter = 0;

            List<FluidStack> tStore = getStoredFluids();
            for (FluidStack tFluid : tStore) {
                if (tFluid == null) continue;
                if (isFluidLocked) {
                    if (mLockedFluid != null) {
                        if (!tFluid.isFluidEqual(mLockedFluid)) continue;;
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

                for (final GT_MetaTileEntity_Hatch outputHatch : mOutputHatches) {
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
        }
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
        return new YottaFluidTank(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1537),
                TextureFactory.of(textureFontOn), TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1537),
                TextureFactory.of(textureFontOff), TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
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
    protected boolean shouldDisplayCheckRecipeResult() {
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget().setStringSupplier(
                    () -> StatCollector.translateToLocal("gui.YOTTank.0") + " " + numberFormat.format(mStorage) + " L")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.BigIntegerSyncer(() -> mStorage, val -> mStorage = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> StatCollector.translateToLocal("gui.YOTTank.1") + " " + getFluidName())
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.FluidStackSyncer(() -> mFluid, val -> mFluid = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.YOTTank.2") + " "
                            + numberFormat.format(mStorageCurrent)
                            + " L")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.BigIntegerSyncer(() -> mStorageCurrent, val -> mStorageCurrent = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.YOTTank.3") + " " + getLockedFluidName())
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.FluidStackSyncer(() -> mLockedFluid, val -> mLockedFluid = val))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> isFluidLocked, val -> isFluidLocked = val))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> voidExcessEnabled, val -> voidExcessEnabled = val));
    }

    @Override
    protected ButtonWidget createSafeVoidButton() {
        return (ButtonWidget) new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            voidExcessEnabled = !voidExcessEnabled;
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                ret.add(
                    voidExcessEnabled ? TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_ON
                        : TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_OFF);
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 132 : 156)
            .setSize(16, 16)
            .addTooltip(StatCollector.translateToLocal("gui.YOTTank.button.void"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
    }

    @Override
    protected ButtonWidget createPowerPassButton() {
        return (ButtonWidget) new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            isFluidLocked = !isFluidLocked;
            if (!widget.getContext()
                .isClient()) mLockedFluid = isFluidLocked ? mFluid : null;
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                ret.add(isFluidLocked ? GG_UITextures.OVERLAY_BUTTON_LOCK_ON : GG_UITextures.OVERLAY_BUTTON_LOCK_OFF);
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 116 : 140)
            .setSize(16, 16)
            .addTooltip(StatCollector.translateToLocal("gui.YOTTank.button.locking"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
