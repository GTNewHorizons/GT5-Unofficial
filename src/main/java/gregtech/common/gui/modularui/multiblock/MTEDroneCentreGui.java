package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTLog;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

public class MTEDroneCentreGui extends MTEMultiBlockBaseGui<MTEDroneCentre> {
    public MTEDroneCentreGui(MTEDroneCentre mteDroneCentre) {
        super(mteDroneCentre);
    }
    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        GenericListSyncHandler<DroneConnection> connectionListSyncer = new GenericListSyncHandler<>(
            multiblock::getConnectionList,
            val -> {
                multiblock.getConnectionList().clear();
                multiblock.getConnectionList().addAll(val);
            },
            buffer -> {
                try {
                    return new DroneConnection(buffer.readNBTTagCompoundFromBuffer());
                } catch (IOException e) {
                    GTLog.err.println(e.getCause());
                }
                return null;
            },
            (buffer, j) -> {
                try {
                    buffer.writeNBTTagCompoundToBuffer(j.transConnectionToNBT());
                } catch (IOException e) {
                    GTLog.err.println(e.getCause());
                }
            },
            DroneConnection::equals,
            null);
        syncManager.syncValue("droneConnectionList", connectionListSyncer);
        }
    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager)
            .child(createInfoButton(syncManager))
            .child(createOnButton(syncManager))
            .child(createOffButton(syncManager));
    }
    protected IWidget createInfoButton(PanelSyncManager syncManager) {
        IPanelHandler machineListPanel = syncManager
            .panel("machineListPanel", this::openMachineListPanel, true);
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD,GTGuiTextures.OVERLAY_BUTTON_WHITELIST))
            .onMousePressed(mouseButton->{
                machineListPanel.openPanel();
                return true;
            })
            .tooltipBuilder(
                t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_open_list")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .setEnabledIf(widget->baseMetaTileEntity.isActive());
    }
    protected IWidget createOnButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD,GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON))
            .onMousePressed(mouseButton -> {
                return true;
                }
            )
            .tooltipBuilder(
                t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_poweron_all")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .setEnabledIf(widget->baseMetaTileEntity.isActive());
    }
    protected IWidget createOffButton(PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(18, 18)
            .playClickSound(true)
            .overlay(new DrawableStack(GTGuiTextures.BUTTON_STANDARD,GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF))
            .onMousePressed(mouseButton -> {
                    return true;
                }
            )
            .tooltipBuilder(
                t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.drone_poweroff_all")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .setEnabledIf(widget->baseMetaTileEntity.isActive());
    }
    private ModularPanel openMachineListPanel(@NotNull PanelSyncManager p_syncManager, @NotNull IPanelHandler syncHandler) {
        int heightCoff = p_syncManager.isClient() ? Minecraft.getMinecraft().currentScreen.height - 40 : 0;
        ModularPanel machinePanel = new ModularPanel("machineListPanel").size(260, heightCoff)
            .background(GTGuiTextures.BACKGROUND_STANDARD);
        return machinePanel;
    }
}
