package goodgenerator.blocks.tileEntity.gui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
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

public class AntimatterForgeGui extends MTEMultiBlockBaseGui {

    private final AntimatterForge base;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    protected static DecimalFormat standardFormat;
    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setExponentSeparator("e");
        standardFormat = new DecimalFormat("0.00E0", dfs);
    }

    public AntimatterForgeGui(AntimatterForge base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("ContainedAM", new LongSyncValue(base::getAntimatterAmount));
        syncManager.syncValue("PassiveCons", new LongSyncValue(base::getPassiveConsumption));
        syncManager.syncValue("ActiveCons", new LongSyncValue(base::getActiveConsumption));
        syncManager.syncValue("AMChange", new LongSyncValue(base::getAntimatterChange));
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
        LongSyncValue containedSync = (LongSyncValue) syncManager.getSyncHandler("ContainedAM:0");
        LongSyncValue passiveConsSync = (LongSyncValue) syncManager.getSyncHandler("PassiveCons:0");
        LongSyncValue activeConsSync = (LongSyncValue) syncManager.getSyncHandler("ActiveCons:0");
        LongSyncValue amChangeSync = (LongSyncValue) syncManager.getSyncHandler("AMChange:0");

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.0")
                            + ": "
                            + EnumChatFormatting.BLUE
                            + numberFormat.format(containedSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " L")))
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.1")
                            + ": "
                            + EnumChatFormatting.RED
                            + standardFormat.format(passiveConsSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU/t")))
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.2")
                            + ": "
                            + EnumChatFormatting.LIGHT_PURPLE
                            + standardFormat.format(activeConsSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " EU/t")))
            .child(
                new TextWidget(
                    IKey.dynamic(
                        () -> EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.AntimatterForge.3")
                            + ": "
                            + EnumChatFormatting.AQUA
                            + numberFormat.format(amChangeSync.getLongValue())
                            + EnumChatFormatting.WHITE
                            + " L")));

    }
}
