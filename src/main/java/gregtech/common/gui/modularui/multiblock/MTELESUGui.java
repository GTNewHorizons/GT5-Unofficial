package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import bartworks.common.configs.Configuration;
import bartworks.common.tileentities.multis.MTELESU;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTELESUGui extends MTEMultiBlockBaseGui<MTELESU> {

    public MTELESUGui(MTELESU multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue storedEUSyncer = syncManager.findSyncHandler("storedEU", LongSyncValue.class);
        LongSyncValue maxStoredEUSyncer = syncManager.findSyncHandler("maxStoredEU", LongSyncValue.class);
        LongSyncValue maxInputVoltageSyncer = syncManager.findSyncHandler("maxInputVoltage", LongSyncValue.class);
        LongSyncValue maxOutputVoltageSyncer = syncManager.findSyncHandler("maxOutputVoltage", LongSyncValue.class);
        LongSyncValue maxInputAmperageSyncer = syncManager.findSyncHandler("maxInputAmperage", LongSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.lang("tooltip.LESU.1.name")
                .color(Color.RED.main)
                .asWidget()
                .setEnabledIf(_ -> !baseMetaTileEntity.isActive())
                .marginBottom(4))
            .child(createTerminalLines("BW.gui.text.lesu.eu", storedEUSyncer))
            .child(createTerminalLines("BW.gui.text.lesu.max", maxStoredEUSyncer))
            .child(createTerminalLines("BW.gui.text.lesu.max_in", maxInputVoltageSyncer))
            .child(createTerminalLines("BW.gui.text.lesu.eu_out", maxOutputVoltageSyncer))
            .child(createTerminalLines("BW.gui.text.lesu.amp_io", maxInputAmperageSyncer))
            .child(
                IKey.lang("tooltip.LESU.0.name")
                    .color(Color.YELLOW.main)
                    .asWidget()
                    .setEnabledIf(_ -> storedEUSyncer.getLongValue() >= Long.MAX_VALUE - 1))

            .child(
                new ProgressWidget()
                    .value(
                        new DoubleSyncValue(
                            () -> (double) storedEUSyncer.getLongValue() / maxStoredEUSyncer.getLongValue()))
                    .texture(GTGuiTextures.PROGRESSBAR_STORED_EU, 147)
                    .marginLeft(2)
                    .marginTop(4)
                    .size(147, 5)
                    .tooltipAutoUpdate(true)
                    .addTooltipLine(
                        IKey.dynamic(
                            () -> formatNumber(
                                (double) storedEUSyncer.getLongValue() / maxStoredEUSyncer.getLongValue() * 100) + "%"))
                    .background(
                        GTGuiTextures.BW_PICTURE_STORED_EU_FRAME.asIcon()
                            .size(149, 7)));
    }

    private TextWidget<?> createTerminalLines(String langKey, LongSyncValue syncValue) {
        return IKey
            .dynamic(() -> StatCollector.translateToLocalFormatted(langKey, formatNumber(syncValue.getLongValue())))
            .color(Color.WHITE.main)
            .asWidget();
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(
            new ItemSlotGridBuilder(multiblock.getCircuitsInventoryHandler(), syncManager).size(4, 1)
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT))
                .build()
                .marginRight(18 + 4));
    }

    @Override
    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return ((Flow) super.createInventoryRow(panel, syncManager)).childPadding(4);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        LongSyncValue maxInputVoltageSyncer = syncManager.findSyncHandler("maxInputVoltage", LongSyncValue.class);

        return Flow.column()
            .coverChildren()
            .reverseLayout(true)
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.dechargerSlotStartIndex())
                            .singletonSlotGroup())
                    .marginTop(4)
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD)
                    .tooltipAutoUpdate(true)
                    .tooltipBuilder(
                        t -> t.addLine(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.machines.battery_slot.tooltip.1",
                                GTUtility.getColoredTierNameFromVoltage(maxInputVoltageSyncer.getLongValue())))))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager))
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.rechargerSlotStartIndex())
                            .singletonSlotGroup())
                    .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CHARGER)
                    .tooltipAutoUpdate(true)
                    .tooltipBuilder(
                        t -> t.addLine(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.machines.battery_slot.tooltip.2",
                                GTUtility.getColoredTierNameFromVoltage(maxInputVoltageSyncer.getLongValue())))));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("storedEU", new LongSyncValue(baseMetaTileEntity::getStoredEU));
        syncManager.syncValue(
            "maxStoredEU",
            new LongSyncValue(
                () -> baseMetaTileEntity.isActive()
                    ? baseMetaTileEntity.getOutputVoltage() * Configuration.multiblocks.energyPerCell
                    : 0));
        syncManager.syncValue("maxInputVoltage", new LongSyncValue(baseMetaTileEntity::getInputVoltage));
        syncManager.syncValue("maxOutputVoltage", new LongSyncValue(baseMetaTileEntity::getOutputVoltage));
        syncManager.syncValue("maxInputAmperage", new LongSyncValue(baseMetaTileEntity::getInputAmperage));
        syncManager.syncValue("storage", new LongSyncValue(multiblock::getStorage, multiblock::setStorage));
    }
}
