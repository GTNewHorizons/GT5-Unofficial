package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.HoverableIcon;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELocalityLockingLattice;

public class LockalityLockingLatticeGui extends MTEMultiBlockBaseGui<MTELocalityLockingLattice> {

    public LockalityLockingLatticeGui(MTELocalityLockingLattice base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("UnstableEnder", new LongSyncValue(multiblock::getUnstableAmount));
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        LongSyncValue UnstableEnderStoredSync = syncManager.findSyncHandler("UnstableEnder", LongSyncValue.class);

        Widget<?> UnstableEnderHexagonCell = new HoverableIcon(GTGuiTextures.UNSTABLE_ENDER_HEX_CAPSULE.asIcon())
            .asWidget()
            .size(43, 39)
            .tooltipBuilder(
                t -> {
                    t.add(EnumChatFormatting.GREEN + "Unstable Ender: " + UnstableEnderStoredSync.getStringValue());
                })
            .tooltipAutoUpdate(true);

        ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);
        return panel.child(
            Flow.column()
                .padding(4)
                .childPadding(2)
                .child(new TextWidget<>(IKey.dynamic(UnstableEnderStoredSync::getStringValue)))
                .child(UnstableEnderHexagonCell));

    }

}
