package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
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
        syncManager.syncValue("eut", new IntSyncValue(base::getEUt));
        syncManager.syncValue("superheated", new BooleanSyncValue(base::isSuperheated));
        syncManager.syncValue("fluidBurnTime", new IntSyncValue(base::getFluidBurnTime));
        syncManager.syncValue("solidBurnTime", new IntSyncValue(base::getSolidBurnTime));
        syncManager.syncValue("burnDecrease", new IntSyncValue(base::getBurnDecrease));
        syncManager.syncValue("efficiency", new IntSyncValue(base::getCurrentEfficiency));
        syncManager.syncValue("currentWaterConsumption", new DoubleSyncValue(base::getCurrentWaterConsumptionPerTick));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue eutSync = syncManager.findSyncHandler("eut", IntSyncValue.class);
        IntSyncValue efficiencySync = syncManager.findSyncHandler("efficiency", IntSyncValue.class);
        BooleanSyncValue superheatedSync = syncManager.findSyncHandler("superheated", BooleanSyncValue.class);
        IntSyncValue fluidBurnTimeSync = syncManager.findSyncHandler("fluidBurnTime", IntSyncValue.class);
        IntSyncValue solidBurnTimeSync = syncManager.findSyncHandler("solidBurnTime", IntSyncValue.class);
        IntSyncValue burnDecreaseSync = syncManager.findSyncHandler("burnDecrease", IntSyncValue.class);
        DoubleSyncValue currentWaterConsumptionSync = syncManager
            .findSyncHandler("currentWaterConsumption", DoubleSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + translateToLocalFormatted(
                            "GT5U.machines.large_boiler.gui.production",
                            superheatedSync.getBoolValue() ? translateToLocal("GT5U.machines.large_boiler.gui.shsteam")
                                : translateToLocal("GT5U.machines.large_boiler.gui.steam"))
                            + ": "
                            + EnumChatFormatting.BLUE
                            + formatNumber(
                                efficiencySync.getIntValue() / 10000F
                                    * eutSync.getIntValue()
                                    * 2L
                                    / (superheatedSync.getBoolValue() ? 3 : 1))
                            + EnumChatFormatting.WHITE
                            + " L/t")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE
                            + translateToLocalFormatted("GT5U.machines.large_boiler.gui.consumption", "water")
                            + ": "
                            + EnumChatFormatting.BLUE
                            + formatNumber(currentWaterConsumptionSync.getDoubleValue())
                            + EnumChatFormatting.WHITE
                            + " L/t")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + translateToLocalFormatted(
                            "GT5U.machines.large_boiler.gui.burn",
                            "Fluid",
                            String.format(
                                "%.2f",
                                fluidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D)))))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + translateToLocalFormatted(
                            "GT5U.machines.large_boiler.gui.burn",
                            "Solid",
                            String.format(
                                "%.2f",
                                solidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D)))));
    }
}
