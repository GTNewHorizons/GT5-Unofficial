package goodgenerator.blocks.tileEntity.gui;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Row;

import goodgenerator.blocks.tileEntity.MTEYottaFluidTank;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.widget.FluidDisplaySyncHandler;
import gregtech.common.gui.modularui.widget.FluidSlotDisplayOnly;
import gregtech.common.modularui2.widget.TransparentSingleChildWidget;
import tectech.thing.metaTileEntity.multi.base.gui.TTMultiBlockBaseGui;

public class MTEYottaFluidTankGui extends TTMultiBlockBaseGui {

    private final MTEYottaFluidTank yottaBase;

    public MTEYottaFluidTankGui(MTEMultiBlockBase base) {
        super(base);
        this.yottaBase = (MTEYottaFluidTank) base;
    }

    @Override
    protected IWidget createTopRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(machineInfoSize()[0] + 4, machineInfoSize()[1] + 3)
            .child(
                new ParentWidget<>().size(machineInfoSize()[0] - 48 - 4, 88)
                    .padding(3)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel).size(machineInfoSize()[0] - 48 - 10, 88 - 5)
                            .collapseDisabledChild())
                    .child(
                        new SingleChildWidget<>().bottomRel(0, 10, 0)
                            .rightRel(0, 10, 0)
                            .size(18, 18)
                            .widgetTheme(GTWidgetThemes.PICTURE_LOGO)))
            .child(createYottankDisplay(syncManager));
    }

    private IWidget createYottankDisplay(PanelSyncManager syncManager) {
        return new ParentWidget<>().size(48, 88)
            .align(Alignment.CenterRight)
            .child(
                new SingleChildWidget<>().overlay(UITexture.fullImage(GregTech.ID, "gui/picture/yottank_overlay"))
                    .size(48, 88))
            .child(
                createFluidDisplay(syncManager).size(34, 72)
                    .align(Alignment.Center))
            .child(
                new TransparentSingleChildWidget()
                    .overlay(UITexture.fullImage(GregTech.ID, "gui/picture/yottank_overlay_lines"))
                    .size(32, 72)
                    .align(Alignment.Center));
    }

    private FluidSlotDisplayOnly createFluidDisplay(PanelSyncManager syncManager) {
        StringSyncValue percentageSyncer = (StringSyncValue) syncManager.getSyncHandler("percentage:0");
        FluidDisplaySyncHandler storedFluid = (FluidDisplaySyncHandler) syncManager.getSyncHandler("storedFluid:0");
        FluidDisplaySyncHandler lockedFluid = (FluidDisplaySyncHandler) syncManager.getSyncHandler("lockedFluid:0");
        BooleanSyncValue locked = (BooleanSyncValue) syncManager.getSyncHandler("isLocked:0");

        return new FluidSlotDisplayOnly(() -> Double.parseDouble(percentageSyncer.getStringValue()) / 100) {

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
            .tooltipBuilder(t -> getFluidDisplayTooltip(t, syncManager));
    }

    private void getFluidDisplayTooltip(RichTooltip t, PanelSyncManager syncManager) {
        DoubleSyncValue maxStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("maxStorage:0");
        DoubleSyncValue currentStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("currentStorage:0");
        StringSyncValue percentageSyncer = (StringSyncValue) syncManager.getSyncHandler("percentage:0");
        FluidDisplaySyncHandler storedFluid = (FluidDisplaySyncHandler) syncManager.getSyncHandler("storedFluid:0");
        FluidDisplaySyncHandler lockedFluid = (FluidDisplaySyncHandler) syncManager.getSyncHandler("lockedFluid:0");
        BooleanSyncValue locked = (BooleanSyncValue) syncManager.getSyncHandler("isLocked:0");

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
                            : NumberFormat.format(currentStorageSyncer.getValue(), NumberFormat.DEFAULT))
                        + "/"
                        + NumberFormat.format(maxStorageSyncer.getValue(), NumberFormat.DEFAULT));
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
                    EnumChatFormatting.BLUE + NumberFormat.format(currentStorageSyncer.getValue(), NumberFormat.DEFAULT)
                        + "/"
                        + NumberFormat.format(maxStorageSyncer.getValue(), NumberFormat.DEFAULT)
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
    }

    @Override
    public IWidget createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        DoubleSyncValue maxStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("maxStorage:0");
        DoubleSyncValue currentStorageSyncer = (DoubleSyncValue) syncManager.getSyncHandler("currentStorage:0");
        StringSyncValue percentageSyncer = (StringSyncValue) syncManager.getSyncHandler("percentage:0");
        StringSyncValue timeTo = (StringSyncValue) syncManager.getSyncHandler("timeTo:0");

        return new ListWidget<>().padding(2)
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocal("gui.YOTTank.0") + " "
                        + NumberFormat.format(maxStorageSyncer.getDoubleValue(), NumberFormat.DEFAULT))
                    .asWidget()
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(w -> yottaBase.getErrorDisplayID() == 0))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocal("gui.YOTTank.2") + " "
                        + (currentStorageSyncer.getValue() == 0 ? "0"
                            : NumberFormat.format(currentStorageSyncer.getValue(), NumberFormat.DEFAULT))
                        + " ("
                        + EnumChatFormatting.GREEN
                        + percentageSyncer.getStringValue()
                        + "%"
                        + EnumChatFormatting.RESET
                        + ")")
                    .asWidget()
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(w -> yottaBase.getErrorDisplayID() == 0))
            .child(
                IKey.dynamic(timeTo::getStringValue)
                    .asWidget()
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .color(Color.WHITE.main)
                    .widthRel(1)
                    .marginBottom(2)
                    .setEnabledIf(w -> yottaBase.getErrorDisplayID() == 0));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        DoubleSyncValue maxStorageSyncer = new DoubleSyncValue(() -> yottaBase.mStorage.doubleValue());
        syncManager.syncValue("maxStorage", maxStorageSyncer);

        DoubleSyncValue currentStorageSyncer = new DoubleSyncValue(() -> yottaBase.mStorageCurrent.doubleValue());
        syncManager.syncValue("currentStorage", currentStorageSyncer);

        StringSyncValue percentageSyncer = new StringSyncValue(yottaBase::getPercent);
        syncManager.syncValue("percentage", percentageSyncer);

        FluidDisplaySyncHandler storedFluid = new FluidDisplaySyncHandler(() -> yottaBase.mFluid);
        syncManager.syncValue("storedFluid", storedFluid);

        FluidDisplaySyncHandler lockedFluid = new FluidDisplaySyncHandler(
            () -> yottaBase.mLockedFluid,
            aFluid -> yottaBase.mLockedFluid = aFluid);
        syncManager.syncValue("lockedFluid", lockedFluid);

        BooleanSyncValue locked = new BooleanSyncValue(
            () -> yottaBase.isFluidLocked,
            bool -> yottaBase.isFluidLocked = bool);
        syncManager.syncValue("isLocked", locked);

        StringSyncValue timeTo = new StringSyncValue(yottaBase::getTimeTo);
        syncManager.syncValue("timeTo", timeTo);
    }
}
