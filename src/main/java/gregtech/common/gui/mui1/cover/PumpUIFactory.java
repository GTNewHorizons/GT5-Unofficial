package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Collections;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.CoverLegacyData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class PumpUIFactory extends CoverLegacyDataUIFactory {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    private CoverDataFollowerToggleButtonWidget<CoverLegacyData> mBlockWidget = null;
    private CoverDataFollowerToggleButtonWidget<CoverLegacyData> mAllowWidget = null;

    public PumpUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder.widget(
            new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                this::getCover,
                (id, coverData) -> !getClickable(id, coverData.getVariable()),
                (id, coverData) -> coverData.setVariable(getNewCoverVariable(id, coverData.getVariable())),
                getUIBuildContext())
                    .addToggleButton(
                        0,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                            .addTooltip(translateToLocal("gt.interact.desc.export.tooltip"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addToggleButton(
                        1,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(translateToLocal("gt.interact.desc.import.tooltip"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addToggleButton(
                        2,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.Pump.AlwaysOn"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addToggleButton(
                        3,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.Pump.MachProcState"))
                            .setPos(spaceX * 1, spaceY * 1))
                    .addToggleButton(
                        4,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.Pump.InvertedMachProcState"))
                            .setPos(spaceX * 2, spaceY * 1))
                    .addToggleButton(5, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                        mAllowWidget = widget;
                        widget
                            .setTextureGetter(
                                i -> coverMatches(PumpUIFactory::isExportModeSelected)
                                    ? GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT
                                    : GTUITextures.OVERLAY_BUTTON_ALLOW_OUTPUT)
                            .dynamicTooltip(() -> {
                                CoverLegacyData cover = getCover();
                                return Collections.singletonList(
                                    cover == null || cover.getVariable() % 2 == 0
                                        ? StatCollector.translateToLocal("gt.interact.desc.Pump.AllowIn")
                                        : StatCollector.translateToLocal("gt.interact.desc.Pump.AllowOut"));
                            })
                            .setPos(spaceX * 0, spaceY * 2);
                    })
                    .addToggleButton(6, CoverDataFollowerToggleButtonWidget.ofDisableable(), widget -> {
                        mBlockWidget = widget;
                        widget
                            .setTextureGetter(
                                i -> coverMatches(PumpUIFactory::isExportModeSelected)
                                    ? GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT
                                    : GTUITextures.OVERLAY_BUTTON_BLOCK_OUTPUT)
                            .dynamicTooltip(
                                () -> Collections.singletonList(
                                    coverMatches(PumpUIFactory::isExportModeSelected)
                                        ? StatCollector.translateToLocal("gt.interact.desc.Pump.BlockIn")
                                        : StatCollector.translateToLocal("gt.interact.desc.Pump.BlockOut")))
                            .setPos(spaceX * 1, spaceY * 2);
                    })
                    .setPos(startX, startY))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Pump.ExpImp"))
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Pump.Conditional"))
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 1))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> coverMatches(PumpUIFactory::isExportModeSelected)
                            ? StatCollector.translateToLocal("gt.interact.desc.Pump.InputBlock")
                            : StatCollector.translateToLocal("gt.interact.desc.Pump.OutputBlock"))
                    .setSynced(false)
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 2));
    }

    private static boolean isExportModeSelected(CoverLegacyData cover) {
        return cover.getVariable() % 2 == 0;
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        switch (id) {
            case 0 -> {
                if (mBlockWidget != null) {
                    mBlockWidget.notifyTooltipChange();
                }
                if (mAllowWidget != null) {
                    mAllowWidget.notifyTooltipChange();
                }
                return coverVariable & ~0x1;
            }
            case 1 -> {
                if (mBlockWidget != null) {
                    mBlockWidget.notifyTooltipChange();
                }
                if (mAllowWidget != null) {
                    mAllowWidget.notifyTooltipChange();
                }
                return coverVariable | 0x1;
            }
            case 2 -> {
                if (coverVariable > 5) return 0x6 | (coverVariable & ~0xE);
                return (coverVariable & ~0xE);
            }
            case 3 -> {
                if (coverVariable > 5) return 0x8 | (coverVariable & ~0xE);
                return 0x2 | (coverVariable & ~0xE);
            }
            case 4 -> {
                if (coverVariable > 5) return 0xA | (coverVariable & ~0xE);
                return (0x4 | (coverVariable & ~0xE));
            }
            case 5 -> {
                if (coverVariable <= 5) return coverVariable + 6;
            }
            case 6 -> {
                if (coverVariable > 5) return coverVariable - 6;
            }
        }
        return coverVariable;
    }

    private boolean getClickable(int id, int coverVariable) {
        if (coverVariable < 0 | 11 < coverVariable) return false;
        return switch (id) {
            case 0, 1 -> (0x1 & coverVariable) != id;
            case 2 -> (coverVariable % 6) >= 2;
            case 3 -> (coverVariable % 6) < 2 | 4 <= (coverVariable % 6);
            case 4 -> (coverVariable % 6) < 4;
            case 5 -> coverVariable < 6;
            case 6 -> coverVariable >= 6;
            default -> false;
        };
    }
}
