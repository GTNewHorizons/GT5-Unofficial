package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;

public class MTENanochipAssemblyModuleBaseGui<T extends MTENanochipAssemblyModuleBase<?>>
    extends MTEMultiBlockBaseGui<T> {

    public MTENanochipAssemblyModuleBaseGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue connected = new BooleanSyncValue(this.multiblock::isConnected);
        syncManager.syncValue("connected", connected);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue connected = syncManager.findSyncHandler("connected", BooleanSyncValue.class);
        ListWidget<IWidget, ?> widget = new ListWidget<>().crossAxisAlignment(Alignment.CrossAxis.START);
        widget.child(
            IKey.dynamic(
                () -> connected.getBoolValue()
                    ? EnumChatFormatting.GREEN + translateToLocal("GT5U.gui.text.nac.module.status.connected")
                    : EnumChatFormatting.RED + translateToLocal("GT5U.gui.text.nac.module.status.disconnected"))
                .asWidget()
                .widthRel(1)
                .marginBottom(2));

        // Standard status (running/idle/errors) is only meaningful when connected to the NAC,
        // since the module's onPostTick is skipped when disconnected.
        Flow statusWrapper = Flow.column()
            .widthRel(1)
            .coverChildrenHeight(0)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .collapseDisabledChild()
            .setEnabledIf(_ -> connected.getBoolValue());
        super.createTerminalTextWidget(syncManager, parent).getChildren()
            .forEach(statusWrapper::child);
        widget.child(statusWrapper);

        return widget;
    }

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler infoPanel = syncManager
            .syncedPanel("infoPanel", true, (_, _) -> MTENanochipAssemblyComplexGui.openInfoPanel(parent));

        return new ButtonWidget<>().size(24)
            .marginTop(4)
            .overlay(IDrawable.EMPTY)
            .tooltip(t -> t.addLine(translateToLocal("GT5U.gui.text.nac.info.open")))
            .background(GTGuiTextures.PICTURE_NANOCHIP_LOGO)
            .disableHoverBackground()
            .onMousePressed(_ -> {
                if (!infoPanel.isPanelOpen()) {
                    infoPanel.openPanel();
                } else {
                    infoPanel.closePanel();
                }
                return true;
            });
    }

}
