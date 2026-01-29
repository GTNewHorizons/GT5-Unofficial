package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;

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
                () -> connected.getBoolValue() ? EnumChatFormatting.GREEN + "Connected to Main Complex"
                    : EnumChatFormatting.RED + "Disconnected from Main Complex")
                .asWidget());
        super.createTerminalTextWidget(syncManager, parent).getChildren()
            .forEach(widget::child);

        return widget;
    }

}
