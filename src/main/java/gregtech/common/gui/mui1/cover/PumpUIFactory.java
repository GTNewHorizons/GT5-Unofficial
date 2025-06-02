package gregtech.common.gui.mui1.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Collections;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
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
                            .addTooltip(translateToLocal("gt.interact.desc.export"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addToggleButton(
                        1,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                            .addTooltip(translateToLocal("gt.interact.desc.import"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addToggleButton(
                        2,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                            .addTooltip(GTUtility.trans("224", "Always On"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addToggleButton(
                        3,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343", "Use Machine " + "Processing State"))
                            .setPos(spaceX * 1, spaceY * 1))
                    .addToggleButton(
                        4,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
                            .addTooltip(GTUtility.trans("343.1", "Use " + "Inverted Machine Processing State"))
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
                                        ? GTUtility.trans("314", "Allow Input")
                                        : GTUtility.trans("312", "Allow " + "Output"));
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
                                        ? GTUtility.trans("313", "Block Input")
                                        : GTUtility.trans("311", "Block " + "Output")))
                            .setPos(spaceX * 1, spaceY * 2);
                    })
                    .setPos(startX, startY))
            .widget(
                new TextWidget(GTUtility.trans("229", "Export/Import")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(GTUtility.trans("230", "Conditional")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(3 + startX + spaceX * 3, 4 + startY + spaceY * 1))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> coverMatches(PumpUIFactory::isExportModeSelected)
                            ? GTUtility.trans("344", "Input Blocking")
                            : GTUtility.trans("344.1", "Output Blocking"))
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
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
