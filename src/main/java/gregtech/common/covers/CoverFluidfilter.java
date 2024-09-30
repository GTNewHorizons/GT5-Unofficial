package gregtech.common.covers;

import static gregtech.api.enums.GTValues.E;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui2.EnumRowBuilder;
import io.netty.buffer.ByteBuf;

public class CoverFluidfilter extends CoverBehaviorBase<CoverFluidfilter.FluidFilterData> {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0; // 000
    private final int INVERT_INPUT_DENY_OUTPUT = 1; // 001
    private final int FILTER_INPUT_ANY_OUTPUT = 2; // 010
    private final int INVERT_INPUT_ANY_OUTPUT = 3; // 011
    private final int DENY_INPUT_FILTER_OUTPUT = 4; // 100
    private final int DENY_INPUT_INVERT_OUTPUT = 5; // 101
    private final int ANY_INPUT_FILTER_OUTPUT = 6; // 110
    private final int ANY_INPUT_INVERT_OUTPUT = 7; // 111

    public CoverFluidfilter(ITexture coverTexture) {
        super(FluidFilterData.class, coverTexture);
    }

    @Override
    public FluidFilterData createDataObject() {
        return new FluidFilterData(-1, 0);
    }

    @Override
    public FluidFilterData createDataObject(int aLegacyData) {
        return new FluidFilterData(aLegacyData >>> 3, aLegacyData & 0x7);
    }

    @Override
    protected String getDescriptionImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        final Fluid fluid = FluidRegistry.getFluid(aCoverVariable.mFluidID);
        if (fluid == null) return E;

        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return (String
            .format("Filtering Fluid: %s - %s", sFluid.getLocalizedName(), getFilterMode(aCoverVariable.mFilterMode)));
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected FluidFilterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        FluidFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    public String getFilterMode(int aFilterMode) {
        return switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT -> GTUtility.trans("043", "Filter input, Deny output");
            case INVERT_INPUT_DENY_OUTPUT -> GTUtility.trans("044", "Invert input, Deny output");
            case FILTER_INPUT_ANY_OUTPUT -> GTUtility.trans("045", "Filter input, Permit any output");
            case INVERT_INPUT_ANY_OUTPUT -> GTUtility.trans("046", "Invert input, Permit any output");
            case DENY_INPUT_FILTER_OUTPUT -> GTUtility.trans("307", "Deny input, Filter output");
            case DENY_INPUT_INVERT_OUTPUT -> GTUtility.trans("308", "Deny input, Invert output");
            case ANY_INPUT_FILTER_OUTPUT -> GTUtility.trans("309", "Permit any input, Filter output");
            case ANY_INPUT_INVERT_OUTPUT -> GTUtility.trans("310", "Permit any input, Invert output");
            default -> ("UNKNOWN");
        };
    }

    @Override
    protected FluidFilterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        FluidFilterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mFilterMode = (aCoverVariable.mFilterMode + (aPlayer.isSneaking() ? -1 : 1)) % 8;
        if (aCoverVariable.mFilterMode < 0) {
            aCoverVariable.mFilterMode = 7;
        }

        GTUtility.sendChatToPlayer(aPlayer, getFilterMode(aCoverVariable.mFilterMode));

        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == ForgeDirection.UNKNOWN) return false;
        if (((aX > 0.375D) && (aX < 0.625D)) || ((side.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))
            || (side.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0 && aZ > 0.375D && aZ < 0.625D
            || (side.offsetZ != 0)) {
            final ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            final FluidStack tFluid = GTUtility.getFluidForFilledItem(tStack, true);
            if (tFluid != null) {
                final int aFluid = tFluid.getFluidID();
                aCoverVariable.mFluidID = aFluid;
                aTileEntity.setCoverDataAtSide(side, aCoverVariable);
                final FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GTUtility
                    .sendChatToPlayer(aPlayer, GTUtility.trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsInImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT) return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT) return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, FluidFilterData aCoverVariable,
        ICoverable aTileEntity) {
        return 0;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidFilterUIFactory(buildContext).createWindow();
    }

    @Override
    protected String getGuiId() {
        return "cover.fluid_filter";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<IOMode> ioModeSyncValue = new EnumSyncValue<>(
            IOMode.class,
            () -> getFilterDirection(guiData),
            value -> setFilterDirection(value, guiData));
        syncManager.syncValue("io_mode", ioModeSyncValue);
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            () -> getFilterType(guiData),
            value -> setFilterType(value, guiData));
        syncManager.syncValue("filter_type", filterTypeSyncValue);
        EnumSyncValue<BlockMode> blockModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            () -> getBlockMode(guiData),
            value -> setBlockMode(value, guiData));
        syncManager.syncValue("block_mode", blockModeSyncValue);

        IFluidTank filterTank = new FluidStackTank(() -> {
            Fluid fluid = FluidRegistry.getFluid(getCoverData(guiData).mFluidID);
            if (fluid != null) {
                return new FluidStack(fluid, 1);
            }
            return null;
        }, fluidStack -> {
            int fluidId = fluidStack != null ? FluidRegistry.getFluidID(fluidStack.getFluid()) : -1;
            FluidFilterData coverData = getCoverData(guiData);
            coverData.mFluidID = fluidId;
            guiData.setCoverData(coverData);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }, 1);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(IOMode.class).value(ioModeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_IMPORT, GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                        .build(),
                    IKey.str(GTUtility.trans("238", "Filter Direction"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                        .build(),
                    IKey.str(GTUtility.trans("239", "Filter Type"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(BlockMode.class).value(blockModeSyncValue)
                        .overlay(
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT),
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT))
                        .tooltip(
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT ? GTUtility.trans("314", "Allow Input")
                                    : GTUtility.trans("312", "Allow Output")),
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT ? GTUtility.trans("313", "Block Input")
                                    : GTUtility.trans("311", "Block Output")))
                        .build(),
                    IKey.str(GTUtility.trans("240", "Block Flow"))
                        .asWidget()))
            .child(
                Flow.row()
                    .marginLeft(WIDGET_MARGIN)
                    .marginTop(WIDGET_MARGIN)
                    .coverChildren()
                    .childPadding(WIDGET_MARGIN)
                    .child(
                        new FluidSlot().syncHandler(
                            new FluidSlotSyncHandler(filterTank).phantom(true)
                                .controlsAmount(false)))
                    .child(IKey.dynamic(() -> {
                        FluidStack fluidStack = filterTank.getFluid();
                        if (fluidStack != null) {
                            return fluidStack.getLocalizedName();
                        }
                        return GTUtility.trans("315", "Filter Empty");
                    })
                        .asWidget()));
    }

    private enum IOMode implements KeyProvider {

        INPUT(IKey.str(GTUtility.trans("232", "Filter Input"))),
        OUTPUT(IKey.str(GTUtility.trans("233", "Filter Output")));

        private final IKey key;

        IOMode(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private IOMode getFilterDirection(CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        return (coverData.mFilterMode >> 2 & 0x1) == 0 ? IOMode.INPUT : IOMode.OUTPUT;
    }

    private void setFilterDirection(IOMode ioMode, CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        IOMode oldMode = getFilterDirection(guiData);
        if (ioMode == oldMode) return;

        if (ioMode == IOMode.INPUT) {
            coverData.mFilterMode &= 0x3;
        } else {
            coverData.mFilterMode |= 0x4;
        }
        guiData.setCoverData(coverData);
    }

    private enum FilterType implements KeyProvider {

        WHITELIST(IKey.str(GTUtility.trans("236", "Whitelist Fluid"))),
        BLACKLIST(IKey.str(GTUtility.trans("237", "Blacklist Fluid")));

        private final IKey key;

        FilterType(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private FilterType getFilterType(CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        return (coverData.mFilterMode & 0x1) == 0 ? FilterType.WHITELIST : FilterType.BLACKLIST;
    }

    private void setFilterType(FilterType filterType, CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        FilterType oldFilterType = getFilterType(guiData);
        if (filterType == oldFilterType) return;

        if (filterType == FilterType.WHITELIST) {
            coverData.mFilterMode &= 0x6;
        } else {
            coverData.mFilterMode |= 0x1;
        }
        guiData.setCoverData(coverData);
    }

    private enum BlockMode {
        BLOCK,
        ALLOW
    }

    private BlockMode getBlockMode(CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        return (coverData.mFilterMode >> 1 & 0x1) == 0 ? BlockMode.BLOCK : BlockMode.ALLOW;
    }

    private void setBlockMode(BlockMode blockMode, CoverGuiData guiData) {
        FluidFilterData coverData = getCoverData(guiData);
        BlockMode oldBlockMode = getBlockMode(guiData);
        if (blockMode == oldBlockMode) return;

        if (blockMode == BlockMode.BLOCK) {
            coverData.mFilterMode &= 0x5;
        } else {
            coverData.mFilterMode |= 0x2;
        }
        guiData.setCoverData(coverData);
    }

    private class FluidFilterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FluidFilterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCoverData,
                    this::setCoverData,
                    CoverFluidfilter.this,
                    (id, coverData) -> !getClickable(id, coverData),
                    (id, coverData) -> {
                        coverData.mFilterMode = getNewFilterMode(id, coverData);
                        return coverData;
                    }).addToggleButton(
                        0,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(GTUtility.trans("232", "Filter Input"))
                            .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                                .addTooltip(GTUtility.trans("233", "Filter Output"))
                                .setPos(spaceX * 1, spaceY * 0))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                .addTooltip(GTUtility.trans("234", "Block Output"))
                                .setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                .addTooltip(GTUtility.trans("235", "Allow Output"))
                                .setPos(spaceX * 1, spaceY * 2))
                        .addToggleButton(
                            4,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                                .addTooltip(GTUtility.trans("236", "Whitelist Fluid"))
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            5,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                                .addTooltip(GTUtility.trans("237", "Blacklist Fluid"))
                                .setPos(spaceX * 1, spaceY * 1))
                        .addFollower(new CoverDataFollowerSlotWidget<FluidFilterData>(new ItemStackHandler(), 0, true) {

                            @Override
                            protected void putClickedStack(ItemStack stack, int mouseButton) {
                                if (stack != null && GTUtility.getFluidFromContainerOrFluidDisplay(stack) == null)
                                    return;
                                super.putClickedStack(
                                    GTUtility.getFluidDisplayStack(
                                        GTUtility.getFluidFromContainerOrFluidDisplay(stack),
                                        false),
                                    mouseButton);
                            }
                        }, this::getFluidDisplayItem, (coverData, stack) -> {
                            if (stack == null) {
                                coverData.mFluidID = -1;
                            } else {
                                FluidStack fluid = GTUtility.getFluidFromDisplayStack(stack);
                                if (fluid != null && fluid.getFluid() != null) {
                                    coverData.mFluidID = fluid.getFluid()
                                        .getID();
                                }
                            }
                            return coverData;
                        },
                            widget -> widget.setBackground(ModularUITextures.FLUID_SLOT)
                                .setPos(0, spaceY * 3 + 2))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("238", "Filter Direction")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("239", "Filter Type")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("240", "Block Flow")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 2))
                .widget(TextWidget.dynamicString(() -> {
                    if (getCoverData() != null) {
                        ItemStack fluidDisplay = getFluidDisplayItem(getCoverData());
                        if (fluidDisplay != null) {
                            return fluidDisplay.getDisplayName();
                        }
                    }
                    return GTUtility.trans("315", "Filter Empty");
                })
                    .setSynced(false)
                    .setDefaultColor(COLOR_TITLE.get())
                    .setPos(startX + spaceX + 3, 4 + startY + spaceY * 3));
        }

        private int getNewFilterMode(int id, FluidFilterData coverVariable) {
            return switch (id) {
                case 0 -> (coverVariable.mFilterMode & 0x3);
                case 1 -> (coverVariable.mFilterMode | 0x4);
                case 2 -> (coverVariable.mFilterMode & 0x5);
                case 3 -> (coverVariable.mFilterMode | 0x2);
                case 4 -> (coverVariable.mFilterMode & 0x6);
                case 5 -> (coverVariable.mFilterMode | 0x1);
                default -> coverVariable.mFilterMode;
            };
        }

        private boolean getClickable(int id, FluidFilterData coverVariable) {
            return switch (id) {
                case 0, 1 -> (coverVariable.mFilterMode >> 2 & 0x1) != (id & 0x1);
                case 2, 3 -> (coverVariable.mFilterMode >> 1 & 0x1) != (id & 0x1);
                case 4, 5 -> (coverVariable.mFilterMode & 0x1) != (id & 0x1);
                default -> false;
            };
        }

        private ItemStack getFluidDisplayItem(FluidFilterData coverData) {
            Fluid fluid = FluidRegistry.getFluid(coverData.mFluidID);
            return GTUtility.getFluidDisplayStack(fluid);
        }
    }

    public static class FluidFilterData implements ISerializableObject {

        private int mFluidID;
        private int mFilterMode;

        public FluidFilterData(int mFluidID, int mFilterMode) {
            this.mFluidID = mFluidID;
            this.mFilterMode = mFilterMode;
        }

        @Override
        @Nonnull
        public ISerializableObject copy() {
            return new FluidFilterData(mFluidID, mFilterMode);
        }

        @Override
        @Nonnull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tNBT = new NBTTagCompound();
            tNBT.setInteger("mFilterMode", mFilterMode);
            if (mFluidID >= 0) tNBT.setString(
                "mFluid",
                FluidRegistry.getFluid(mFluidID)
                    .getName());
            return tNBT;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(mFilterMode)
                .writeInt(mFluidID);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound tNBT) {
                mFilterMode = tNBT.getInteger("mFilterMode");
                if (tNBT.hasKey("mFluid", NBT.TAG_STRING))
                    mFluidID = FluidRegistry.getFluidID(tNBT.getString("mFluid"));
                else mFluidID = -1;
            }
        }

        @Override
        @Nonnull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mFilterMode = aBuf.readByte();
            mFluidID = aBuf.readInt();
            return this;
        }
    }
}
