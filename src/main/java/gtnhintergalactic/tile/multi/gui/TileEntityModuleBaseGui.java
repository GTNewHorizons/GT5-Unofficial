package gtnhintergalactic.tile.multi.gui;

import static gtnhintergalactic.GTNHIntergalactic.ASSET_PREFIX;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.numberFormat;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import tectech.thing.metaTileEntity.multi.base.gui.TTMultiBlockBaseGui;

public class TileEntityModuleBaseGui extends TTMultiBlockBaseGui {

    public TileEntityModuleBaseGui(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    protected void initCustomIcons() {
        super.initCustomIcons();
        this.customIcons.put("logo", UITexture.fullImage(ASSET_PREFIX, "gui/picture/space_elevator_logo_dark.png"));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue euVarSyncer = new LongSyncValue(base::getEUVar);
        syncManager.syncValue("euVar", euVarSyncer);
        return super.createTerminalTextWidget(syncManager, parent).child(
            IKey.dynamic(() -> "Stored Energy: " + numberFormat.format(euVarSyncer.getValue()) + " EU")
                .asWidget()
                .setEnabledIf(
                    w -> base.getBaseMetaTileEntity()
                        .isAllowedToWork()
                        || base.getBaseMetaTileEntity()
                            .isActive())
                .color(Color.WHITE.main)
                .widthRel(1)
                .marginBottom(2));
    }
}
