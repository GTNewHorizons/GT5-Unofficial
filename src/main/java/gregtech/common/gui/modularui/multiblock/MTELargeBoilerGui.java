package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBase;

public class MTELargeBoilerGui extends MTEMultiBlockBaseGui<MTELargeBoilerBase> {

    private final MTELargeBoilerBase base;

    public MTELargeBoilerGui(MTELargeBoilerBase base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        // syncManager.syncValue("isActive", new BooleanSyncValue(base::isActive));
        syncManager.syncValue("eut", new IntSyncValue(base::getEUt));
        // syncManager.syncValue("efficiency", new IntSyncValue(base::getCurrentEfficiency));
        syncManager.syncValue("superheated", new BooleanSyncValue(base::isSuperheated));
    }

    // @Override
    // protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
    // return super.createPanelGap(parent, syncManager).child(createOverdriveButton(syncManager, parent));
    // }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue eutSync = syncManager.findSyncHandler("eut", IntSyncValue.class);
        IntSyncValue efficiencySync = syncManager.findSyncHandler("efficiency", IntSyncValue.class);
        BooleanSyncValue superheatedSync = syncManager.findSyncHandler("superheated", BooleanSyncValue.class);
        BooleanSyncValue isActive = syncManager.findSyncHandler("isActive", BooleanSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).childIf(
            isActive.getBoolValue(),
            new TextWidget<>(
                IKey.dynamic(
                    () -> EnumChatFormatting.WHITE
                        + translateToLocalFormatted(
                            "GT5U.machines.large_boiler.gui",
                            superheatedSync.getBoolValue() ? translateToLocal("GT5U.machines.large_boiler.gui.shsteam")
                                : translateToLocal("GT5U.machines.large_boiler.gui.steam"))
                        + ": "
                        + EnumChatFormatting.BLUE
                        + efficiencySync.getIntValue() / 10000F
                            * eutSync.getIntValue()
                            * 40L
                            / (superheatedSync.getBoolValue() ? 3 : 1)
                        + EnumChatFormatting.WHITE
                        + " L/s")));
    }
}
