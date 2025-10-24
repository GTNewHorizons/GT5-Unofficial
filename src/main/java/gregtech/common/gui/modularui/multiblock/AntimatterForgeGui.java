package gregtech.common.gui.modularui.multiblock;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import goodgenerator.blocks.tileEntity.AntimatterForge;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class AntimatterForgeGui extends MTEMultiBlockBaseGui<AntimatterForge> {

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    protected static DecimalFormat standardFormat;
    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        standardFormat = new DecimalFormat("0.00E0", dfs);
    }

    public AntimatterForgeGui(AntimatterForge base) {
        super(base);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("ContainedAM", new LongSyncValue(multiblock::getAntimatterAmount));
        syncManager.syncValue("PassiveCons", new LongSyncValue(multiblock::getPassiveConsumption));
        syncManager.syncValue("ActiveCons", new LongSyncValue(multiblock::getActiveConsumption));
        syncManager.syncValue("AMChange", new LongSyncValue(multiblock::getAntimatterChange));
    }

    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .padding(4)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild()))
            .child(createTerminalCornerColumn(panel, syncManager));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue containedSync = syncManager.findSyncHandler("ContainedAM", LongSyncValue.class);
        LongSyncValue passiveConsSync = syncManager.findSyncHandler("PassiveCons", LongSyncValue.class);
        LongSyncValue activeConsSync = syncManager.findSyncHandler("ActiveCons", LongSyncValue.class);
        LongSyncValue amChangeSync = syncManager.findSyncHandler("AMChange", LongSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.0")
                            + ": "
                            + EnumChatFormatting.BLUE
                            + numberFormat.format(containedSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " L")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.1")
                            + ": "
                            + EnumChatFormatting.RED
                            + standardFormat.format(passiveConsSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU/t")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.2")
                            + ": "
                            + EnumChatFormatting.LIGHT_PURPLE
                            + standardFormat.format(activeConsSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU/t")))
            .child(
                new TextWidget<>(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.3")
                            + ": "
                            + EnumChatFormatting.AQUA
                            + numberFormat.format(amChangeSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " L")));

    }
}
