package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBase;

public class MTELargeBoilerGui extends MTEMultiBlockBaseGui<MTELargeBoilerBase> {

    private final MTELargeBoilerBase base;
    private static final int DISPLAY_ROW_HEIGHT = 15;
    private static final int DISPLAY_ROW_CHAR_LIMIT = 46;

    public MTELargeBoilerGui(MTELargeBoilerBase base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("superheated", new BooleanSyncValue(base::isSuperheated));
        syncManager.syncValue("fluidBurnTime", new IntSyncValue(base::getFluidBurnTime));
        syncManager.syncValue("solidBurnTime", new IntSyncValue(base::getSolidBurnTime));
        syncManager.syncValue("burnDecrease", new IntSyncValue(base::getBurnDecrease));
        syncManager.syncValue("currentWaterConsumption", new DoubleSyncValue(base::getCurrentWaterConsumptionPerTick));
        syncManager.syncValue("currentSteamProduction", new DoubleSyncValue(base::getCurrentSteamOutputPerTick));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue isMachineActive = syncManager.findSyncHandler("machineActive", BooleanSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .childIf(isMachineActive.getBoolValue(), () -> createSteamProductionTextWidget(syncManager))
            .childIf(isMachineActive.getBoolValue(), () -> createFluidTimerTextWidget(syncManager))
            .childIf(isMachineActive.getBoolValue(), () -> createSolidTimerTextWidget(syncManager));
    }

    private IWidget createSteamProductionTextWidget(PanelSyncManager syncManager) {
        BooleanSyncValue superheatedSync = syncManager.findSyncHandler("superheated", BooleanSyncValue.class);
        FluidStack fluidStack = superheatedSync.getBoolValue() ? FluidRegistry.getFluidStack("ic2superheatedsteam", 1)
            : FluidRegistry.getFluidStack("steam", 1);
        Flow column = Flow.column()
            .coverChildren(0);
        column.child(
            Flow.row()
                .fullWidth()
                .height(DISPLAY_ROW_HEIGHT)
                .child(createFluidDrawable(fluidStack))
                .child(createHoverableTextForFluid(fluidStack, syncManager)));
        return column;
    }

    private IWidget createFluidTimerTextWidget(PanelSyncManager syncManager) {
        IntSyncValue fluidBurnTimeSync = syncManager.findSyncHandler("fluidBurnTime", IntSyncValue.class);
        IntSyncValue burnDecreaseSync = syncManager.findSyncHandler("burnDecrease", IntSyncValue.class);
        return new TextWidget<>(
            IKey.dynamic(
                () -> translateToLocalFormatted(
                    "GT5U.machines.large_boiler.gui.fluid_burn",
                    String.format(
                        "%.2f", fluidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D))));
    }

    private IWidget createSolidTimerTextWidget(PanelSyncManager syncManager) {
        IntSyncValue solidBurnTimeSync = syncManager.findSyncHandler("solidBurnTime", IntSyncValue.class);
        IntSyncValue burnDecreaseSync = syncManager.findSyncHandler("burnDecrease", IntSyncValue.class);
        return new TextWidget<>(
            IKey.dynamic(
                () -> translateToLocalFormatted(
                    "GT5U.machines.large_boiler.gui.solid_burn",
                    String.format(
                        "%.2f", solidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D))));
    }

    private TextWidget<?> createHoverableTextForFluid(FluidStack fluidStack, PanelSyncManager syncManager) {
        String fluidName = EnumChatFormatting.AQUA + fluidStack.getLocalizedName() + EnumChatFormatting.RESET;
        DoubleSyncValue currentSteamProductionSync = syncManager
            .findSyncHandler("currentSteamProduction", DoubleSyncValue.class);
        // spotless:off
        return new TextWidget<>(
            IKey.dynamic(
                () -> getFluidTextLine(
                    fluidName,
                    currentSteamProductionSync.getValue().longValue())))
            .height(DISPLAY_ROW_HEIGHT)
            .scale(0.75f)
            .tooltipBuilder(
                t -> t.addLine(
                EnumChatFormatting.AQUA + fluidName
                    + "\n"
                    + GTUtility.appendRate(false,
                    currentSteamProductionSync.getValue().longValue(),
                    false,
                    1)))
            .tooltipAutoUpdate(true);
        //spotless:on
    }

    private @NotNull String getFluidTextLine(String fluidName, long amount) {
        String amountString = EnumChatFormatting.WHITE + " x "
            + EnumChatFormatting.GOLD
            + GTUtility.formatShortenedLong(amount)
            + "L"
            + EnumChatFormatting.WHITE
            + (GTUtility.appendRate(false, amount, true, 1));
        String truncatedFluidName = GTUtility.truncateText(fluidName, DISPLAY_ROW_CHAR_LIMIT - amountString.length());
        String localizedLine = StatCollector.translateToLocal("gt.interact.desc.mb.FluidTextLine");
        if (!localizedLine.equals("gt.interact.desc.mb.FluidTextLine")) {
            String amountShort = GTUtility.formatShortenedLong(amount);
            String rateText = EnumChatFormatting
                .getTextWithoutFormattingCodes(GTUtility.appendRate(false, amount, true, 1));
            String trimmedRate = rateText.trim();
            String rateInner = trimmedRate;
            if (trimmedRate.length() >= 2 && trimmedRate.startsWith("(") && trimmedRate.endsWith(")")) {
                rateInner = trimmedRate.substring(1, trimmedRate.length() - 1);
            }
            return StatCollector.translateToLocalFormatted(
                "gt.interact.desc.mb.FluidTextLine",
                truncatedFluidName,
                amountShort,
                "(",
                rateInner,
                ")");
        }
        return EnumChatFormatting.AQUA + truncatedFluidName + amountString;
    }

    private FluidDisplayWidget createFluidDrawable(FluidStack fluidStack) {
        return new FluidDisplayWidget().disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .displayAmount(false)
            .value(fluidStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }
}
