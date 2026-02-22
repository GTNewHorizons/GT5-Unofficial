package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverFacadeBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class FacadeBaseUIFactory extends CoverUIFactory<CoverFacadeBase> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public FacadeBaseUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverFacadeBase adaptCover(Cover cover) {
        if (cover instanceof CoverFacadeBase adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        builder
            .widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    this::isEnabled,
                    (id, coverData) -> coverData.setFlags(getNewCoverVariable(id, coverData.getFlags())),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                        .setPos(startX, startY))
            .widget(new ItemDrawable(() -> {
                CoverFacadeBase cover = getCover();
                return cover != null ? cover.getStack() : null;
            }).asWidget()
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(TextWidget.dynamicString(() -> {
                CoverFacadeBase cover = getCover();
                return cover != null ? cover.getStack()
                    .getDisplayName() : "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TITLE.get())
                .setPos(25, 9))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.facade.Redstone"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.facade.Energy"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.facade.Fluids"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.facade.Items"))
                    .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
    }

    @Override
    protected void addTitleToUI(ModularWindow.Builder builder) {}

    private int getNewCoverVariable(int id, int flags) {
        return switch (id) {
            case 0 -> flags ^ 0x1;
            case 1 -> flags ^ 0x2;
            case 2 -> flags ^ 0x4;
            case 3 -> flags ^ 0x8;
            default -> flags;
        };
    }

    private boolean isEnabled(int id, CoverFacadeBase coverVariable) {
        return switch (id) {
            case 0 -> (coverVariable.getFlags() & 0x1) > 0;
            case 1 -> (coverVariable.getFlags() & 0x2) > 0;
            case 2 -> (coverVariable.getFlags() & 0x4) > 0;
            case 3 -> (coverVariable.getFlags() & 0x8) > 0;
            default -> false;
        };
    }
}
