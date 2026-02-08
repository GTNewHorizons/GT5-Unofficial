package gregtech.common.gui.modularui.hatch;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate1by1;

import java.util.function.Supplier;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import bartworks.common.tileentities.tiered.MTERadioHatch;
import bartworks.util.MathUtils;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTERadioHatchGui extends MTEHatchBaseGui<MTERadioHatch> {

    public MTERadioHatchGui(MTERadioHatch base) {
        super(base);
    }

    // credit to purebluez
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        IPanelHandler popupPanel = syncManager
            .syncedPanel("popup", true, (manager, handler) -> createShutterUI(syncManager));
        syncManager.registerSlotGroup("item_inv", 1);

        IntSyncValue massSyncer = new IntSyncValue(hatch::getMass, value -> hatch.setMass((byte) value));
        IntSyncValue sievertSyncer = new IntSyncValue(hatch::getSievert, hatch::setSievert);
        IntSyncValue color1Syncer = new IntSyncValue(
            () -> hatch.getColorForGuiAtIndex(0),
            c -> hatch.setColorForGuiAtIndex((short) c, 0));
        IntSyncValue color2Syncer = new IntSyncValue(
            () -> hatch.getColorForGuiAtIndex(1),
            c -> hatch.setColorForGuiAtIndex((short) c, 1));
        IntSyncValue color3Syncer = new IntSyncValue(
            () -> hatch.getColorForGuiAtIndex(2),
            c -> hatch.setColorForGuiAtIndex((short) c, 2));
        IntSyncValue coverageSyncer = new IntSyncValue(hatch::getCoverage, value -> hatch.setCoverage((short) value));
        LongSyncValue timeSyncHandler = new LongSyncValue(hatch::getTimer, hatch::setTimer);
        LongSyncValue decayTimeSyncHandler = new LongSyncValue(hatch::getDecayTime, hatch::setDecayTime);

        syncManager.syncValue("decayTime", decayTimeSyncHandler);
        syncManager.syncValue("timer", timeSyncHandler);
        syncManager.syncValue("mass", massSyncer);
        syncManager.syncValue("sievert", sievertSyncer);
        syncManager.syncValue("color0", color1Syncer);
        syncManager.syncValue("color1", color2Syncer);
        syncManager.syncValue("color2", color3Syncer);
        syncManager.syncValue("coverage", 0, coverageSyncer);
        return GTGuis.mteTemplatePanelBuilder(hatch, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                gridTemplate1by1(
                    index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("item_inv"))))
            .child(
                GTGuiTextures.PICTURE_SIEVERT_CONTAINER.asWidget()
                    .pos(61, 9)
                    .size(56, 24))
            .child(
                new ProgressWidget().progress(() -> sievertSyncer.getIntValue() / 148f)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .texture(GTGuiTextures.PROGRESSBAR_SIEVERT, 24)
                    .pos(65, 13)
                    .size(48, 16))
            .child(
                GTGuiTextures.PICTURE_DECAY_TIME_INSIDE.asWidget()
                    .pos(124, 18)
                    .size(16, 48))
            .child(new IDrawable.DrawableWidget((context, x, y, width, height, widgetTheme) -> {
                if (decayTimeSyncHandler.getLongValue() > 0) {

                    int drawableHeight = MathUtils.ceilInt(
                        48 * ((decayTimeSyncHandler.getLongValue()
                            - timeSyncHandler.getLongValue() % decayTimeSyncHandler.getLongValue())
                            / (float) decayTimeSyncHandler.getLongValue()));

                    new com.cleanroommc.modularui.drawable.Rectangle().color(
                        com.cleanroommc.modularui.utils.Color
                            .rgb(color1Syncer.getIntValue(), color2Syncer.getIntValue(), color3Syncer.getIntValue()))
                        .draw(context, new Area(0, 48 - drawableHeight, 16, drawableHeight), widgetTheme);
                }
            }).pos(124, 18)
                .size(18, 48))
            .child(createDurationMeterContainer(syncManager))
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("BW.NEI.display.radhatch.1", massSyncer.getIntValue()))
                    .alignment(com.cleanroommc.modularui.utils.Alignment.Center)
                    .asWidget()
                    .pos(65, 62))
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("BW.NEI.display.radhatch.0", sievertSyncer.getIntValue()))
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .asWidget()
                    .pos(60, 72)
                    .size(80, 8))
            .child(new com.cleanroommc.modularui.widgets.ButtonWidget<>().onMousePressed(mouseButton -> {
                popupPanel.openPanel();
                return popupPanel.isPanelOpen();
            })
                .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.add("Radiation Shutter"))
                .pos(153, 5)
                .size(18, 18))
            .child(
                GTGuiTextures.PICTURE_BARTWORKS_LOGO_STANDARD.asWidget()
                    .pos(10, 53)
                    .size(47, 21))
            .bindPlayerInventory();
    }

    // annoying that it has to be done this way, maybe move out of MTE later.

    protected Supplier<Integer> COLOR_TITLE = () -> hatch.getTextColorOrDefault("title", 0x404040);

    private ModularPanel createShutterUI(PanelSyncManager syncManager) {
        IntSyncValue coverageSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("coverage:0");
        return createPopUpPanel("bw:radio_hatch_shutter", false, false).size(176, 107)
            .child(
                IKey.str("Radiation Shutter Control")
                    .color(this.COLOR_TITLE.get())
                    .asWidget()
                    .pos(10, 9))
            .child(
                GTGuiTextures.PICTURE_RADIATION_SHUTTER_FRAME.asWidget()
                    .pos(14, 27)
                    .size(55, 54))
            .child(
                new ProgressWidget().progress(() -> 1 - ((double) coverageSyncer.getValue() / 100D))
                    .texture(GTGuiTextures.PICTURE_TRANSPARENT, GTGuiTextures.PICTURE_RADIATION_SHUTTER_INSIDE, 50)
                    .direction(ProgressWidget.Direction.UP)
                    .pos(16, 29)
                    .size(51, 50))
            .child(
                new TextFieldWidget().setNumbers(0, 100)
                    .value(new StringSyncValue(coverageSyncer::getStringValue, coverageSyncer::setStringValue))
                    .setTextColor(com.cleanroommc.modularui.utils.Color.WHITE.darker(1))
                    .setTextAlignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .pos(86, 27)
                    .size(30, 12))
            .child(
                IKey.str("%")
                    .asWidget()
                    .pos(118, 31));
    }

    private IWidget createDurationMeterContainer(PanelSyncManager syncManager) {

        return GTGuiTextures.PICTURE_DECAY_TIME_CONTAINER.asWidget()
            .tooltipBuilder(
                tooltip -> tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.tile.radhatch.10.name",
                        hatch.getTimer() <= 1 ? 0 : (hatch.getDecayTime() - hatch.getTimer()) / 20,
                        hatch.getTimer() <= 1 ? 0 : hatch.getDecayTime() / 20)))
            .tooltipAutoUpdate(true)
            .pos(120, 14)
            .size(24, 56);
    }

}
