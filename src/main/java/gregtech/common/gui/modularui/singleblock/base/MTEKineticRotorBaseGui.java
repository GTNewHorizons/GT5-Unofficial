package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.enums.Mods.GregTech;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.HoverableIcon;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.*;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEKineticRotorBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import ic2.api.item.IKineticRotor;

public abstract class MTEKineticRotorBaseGui<T extends MTEKineticRotorBase> extends MTETieredMachineBlockBaseGui<T> {

    private static final int ICON_SIZE = 18;
    private static final Predicate<ItemStack> rotorCheck = stack -> (stack.getItem() instanceof IKineticRotor rotor
        && rotor.isAcceptedType(stack, IKineticRotor.GearboxType.WIND));

    protected static IDrawable kineticPanelButtonOverlay = UITexture
        .fullImage(GregTech.ID, "gui/overlay_button/information");

    public MTEKineticRotorBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        final ItemSlot rotorSlot = new ItemSlot().slot(
            new ModularSlot(machine.inventoryHandler, 0).singletonSlotGroup()
                .filter(rotorCheck))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_ROTOR);

        return Flow.row()
            .child(
                createInfoSide(syncManager).widthRel(0.75f)
                    .heightRel(1f))
            .child(createSlotSide(rotorSlot).widthRel(0.25f))
            .child(
                createKineticInfoPanelButton(syncManager, panel, kineticPanelButtonOverlay).bottom(0)
                    .right(21))
            .child(
                makeLogoWidget().bottom(0)
                    .right(0));
    }

    protected Flow createInfoSide(PanelSyncManager syncManager) {
        return Flow.col()
            .verticalCenter()
            .child(createTextDisplayWidget(syncManager).full());
    }

    protected ParentWidget<?> createTextDisplayWidget(PanelSyncManager syncManager) {
        BooleanSyncValue rotorBlockedSync = syncManager.findSyncHandler("rotorBlocked", BooleanSyncValue.class);
        IntSyncValue rotorIDSync = syncManager.findSyncHandler("rotorID", IntSyncValue.class);
        return new ParentWidget<>().widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(getTextList(syncManager))
            .childIf(
                (rotorBlockedSync.getBoolValue() && rotorIDSync.getIntValue() != -1),
                () -> new HoverableIcon(GTGuiTextures.OVERLAY_ROTOR.asIcon()).asWidget()
                    .size(ICON_SIZE)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.gui.text.rotor_blocked"))
                    .bottom(4)
                    .left(4));
    }

    protected ListWidget<IWidget, ?> getTextList(PanelSyncManager syncManager) {
        BooleanSyncValue rotorBlockedSync = syncManager.findSyncHandler("rotorBlocked", BooleanSyncValue.class);
        LongSyncValue kuOutSync = syncManager.findSyncHandler("kuOut", LongSyncValue.class);
        DoubleSyncValue obsMultiSync = syncManager.findSyncHandler("obsMulti", DoubleSyncValue.class);
        return new ListWidget<>().full()
            .padding(4)
            .reverseLayout(true)
            .childIf(
                (obsMultiSync.getDoubleValue() < 1d && !rotorBlockedSync.getBoolValue()),
                () -> IKey.dynamic(() -> {
                    double obsMulti = obsMultiSync.getDoubleValue();
                    return String.format(
                        StatCollector.translateToLocal("GT5U.gui.text.obstruction_multi"),
                        // done this way because translateToLocal turns %.2f in lang key to %s for some reason???
                        String.format("%.2f", obsMulti));
                })
                    .color(Color.YELLOW.main)
                    .asWidget()
                    .fullWidth())
            .collapseDisabledChild()
            .child(IKey.dynamic(() -> {
                long kuOut = kuOutSync.getLongValue();
                return String.format(StatCollector.translateToLocal("GT5U.gui.text.current_ku"), kuOut);
            })
                .color(Color.WHITE.main)
                .asWidget()
                .fullWidth());
    }

    protected Flow createSlotSide(ItemSlot rotorSlot) {
        return Flow.col()
            .child(rotorSlot.center());
    }

    protected ButtonWidget<?> createKineticInfoPanelButton(PanelSyncManager syncManager, ModularPanel parent,
        IDrawable buttonOverlay) {
        IPanelHandler kineticInfoPanel = syncManager
            .syncedPanel("kineticPanel", true, (_, _) -> openKineticInfoPanel(syncManager, parent));
        return new ButtonWidget<>().overlay(buttonOverlay)
            .onMousePressed(d -> {
                if (!kineticInfoPanel.isPanelOpen()) {
                    kineticInfoPanel.openPanel();
                } else {
                    kineticInfoPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.text.kinetic_panel")));
    }

    private ModularPanel openKineticInfoPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("kineticPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 130)
            .child(
                Flow.col()
                    .full()
                    .padding(6, 6, 3, 3)
                    .child(kineticPanelTitleText())
                    .child(kineticPanelInfo(syncManager)));
    }

    private TextWidget<?> kineticPanelTitleText() {
        return new TextWidget<>(
            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.kinetic_panel"))
                .textAlign(Alignment.CENTER)
                .fullWidth()
                .height(18);
    }

    private ParentWidget<?> kineticPanelInfo(PanelSyncManager syncManager) {
        return new ParentWidget<>().widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .fullWidth()
            .height(100)
            .padding(4)
            .child(getKineticInfoText(syncManager).full());
    }

    /**
     * For displaying extra info about the machine in a separate popout
     * to add more info, override and add children to super.getKineticTextInfo()
     */
    protected ListWidget<IWidget, ?> getKineticInfoText(PanelSyncManager syncManager) {
        IntSyncValue spaceFrontSync = syncManager.findSyncHandler("spaceFront", IntSyncValue.class);
        IntSyncValue spaceSidesSync = syncManager.findSyncHandler("spaceSides", IntSyncValue.class);
        IntSyncValue rotorIDSync = syncManager.findSyncHandler("rotorID", IntSyncValue.class);
        return new ListWidget<>().collapseDisabledChild()
            .child(
                new TextWidget<>(
                    EnumChatFormatting.WHITE + ""
                        + EnumChatFormatting.UNDERLINE
                        + StatCollector.translateToLocal("GT5U.gui.text.space_require")).textAlign(Alignment.CENTER)
                            .fullWidth()
                            .marginBottom(1))
            .childIf(
                rotorIDSync.getIntValue() == -1,
                () -> new TextWidget<>(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.gui.text.space_norotor"))
                        .textAlign(Alignment.CENTER)
                        .fullWidth()
                        .marginBottom(2))
            .childIf(
                rotorIDSync.getIntValue() != -1,
                () -> IKey.dynamic(
                    () -> String
                        .format(StatCollector.translateToLocal("GT5U.gui.text.front"), spaceFrontSync.getIntValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .fullWidth()
                    .marginBottom(1))
            .childIf(
                rotorIDSync.getIntValue() != -1,
                () -> IKey.dynamic(
                    () -> String
                        .format(StatCollector.translateToLocal("GT5U.gui.text.sides"), spaceSidesSync.getIntValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2));
    };

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        LongSyncValue kuOutSyncer = new LongSyncValue(machine::currentKU);
        DoubleSyncValue obsMultiSyncer = new DoubleSyncValue(machine::obstructedOutputMultiplier);
        BooleanSyncValue rotorBlockedSyncer = new BooleanSyncValue(machine::isRotorBlocked);
        IntSyncValue rotorIDSyncer = new IntSyncValue(machine::getRotorID);
        IntSyncValue spaceFrontSyncer = new IntSyncValue(machine::getSpaceRequiredFront);
        IntSyncValue spaceSideSyncer = new IntSyncValue(machine::getSpaceRequiredSides);
        syncManager.syncValue("kuOut", kuOutSyncer);
        syncManager.syncValue("obsMulti", obsMultiSyncer);
        syncManager.syncValue("rotorBlocked", rotorBlockedSyncer);
        syncManager.syncValue("rotorID", rotorIDSyncer);
        syncManager.syncValue("spaceFront", spaceFrontSyncer);
        syncManager.syncValue("spaceSides", spaceSideSyncer);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }
}
