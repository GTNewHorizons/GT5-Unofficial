package tectech.thing.gui;

import java.util.UUID;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.IFluidHandler;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.mechanics.enderStorage.EnderLinkTag;
import tectech.mechanics.enderStorage.EnderWorldSavedData;
import tectech.thing.cover.CoverEnderFluidLink;

public class CoverEnderFluidLinkGui extends CoverBaseGui<CoverEnderFluidLink> {

    public CoverEnderFluidLinkGui(CoverEnderFluidLink cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(positionRow(createFrequencyRow(syncManager)))
            .child(positionRow(createChannelModeRow(syncManager)))
            .child(positionRow(createTransferModeRow(syncManager)));
    }

    private Flow createFrequencyRow(PanelSyncManager syncManager) {
        StringSyncValue frequencySyncer = new StringSyncValue(
            this::getFrequency,
            val -> this.setFrequency(val, syncManager));

        return Flow.row()
            .marginBottom(4)
            .child(
                new TextFieldWidget().height(10)
                    .width(88)
                    .value(frequencySyncer))
            .child(
                IKey.lang("gt.interact.desc.channel")
                    .asWidget());
    }

    private String getFrequency() {
        if (!(cover.getTile() instanceof IFluidHandler teTank)) {
            return "";
        }

        EnderLinkTag tag = EnderWorldSavedData.getEnderLinkTag(teTank);
        return tag == null ? "" : tag.getFrequency();
    }

    private void setFrequency(String val, PanelSyncManager syncManager) {
        if (!(cover.getTile() instanceof IFluidHandler tank)) {
            return;
        }
        UUID uuid = null;

        if (cover.isPrivateChannel()) {
            uuid = syncManager.getPlayer()
                .getUniqueID();
            if (!uuid.equals(CoverEnderFluidLink.getOwner(tank))) return;
        }

        EnderWorldSavedData.bindEnderLinkTag(tank, new EnderLinkTag(val, uuid));
    }

    private Flow createChannelModeRow(PanelSyncManager syncManager) {
        BooleanSyncValue privateChannelSyncer = new BooleanSyncValue(cover::isPrivateChannel, cover::setPrivateChannel);
        syncManager.syncValue("privateChannel", privateChannelSyncer);

        return Flow.row()
            .marginBottom(4)
            .child(
                createSelectButton(
                    privateChannelSyncer,
                    false,
                    GTGuiTextures.OVERLAY_BUTTON_WHITELIST,
                    "gt.interact.desc.public"))
            .child(
                createSelectButton(
                    privateChannelSyncer,
                    true,
                    GTGuiTextures.OVERLAY_BUTTON_BLACKLIST,
                    "gt.interact.desc.private"))
            .child(
                IKey.lang("gt.interact.desc.set_perm")
                    .asWidget());
    }

    private Flow createTransferModeRow(PanelSyncManager syncManager) {
        BooleanSyncValue exportSyncer = new BooleanSyncValue(cover::isExport, cover::setExport);
        syncManager.syncValue("export", exportSyncer);

        return Flow.row()
            .child(createSelectButton(exportSyncer, false, GTGuiTextures.OVERLAY_IMPORT, "gt.interact.desc.import"))
            .child(createSelectButton(exportSyncer, true, GTGuiTextures.OVERLAY_EXPORT, "gt.interact.desc.export"))
            .child(
                IKey.lang("gt.interact.desc.set_io")
                    .asWidget());
    }

    private IWidget createSelectButton(BooleanSyncValue syncValue, boolean targetValue, IDrawable overlay, String key) {
        return new SelectButton().value(LinkedBoolValue.of(syncValue, targetValue))
            .size(16)
            .marginRight(2)
            .overlay(overlay)
            .tooltipBuilder(t -> t.addLine(StatCollector.translateToLocal(key)));
    }

}
