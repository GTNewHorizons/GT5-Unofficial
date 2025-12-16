package gregtech.common.gui.mui1.cover;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class ItemFilterUIFactory extends CoverUIFactory<CoverItemFilter> {

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public ItemFilterUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected CoverItemFilter adaptCover(Cover cover) {
        if (cover instanceof CoverItemFilter adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        CoverItemFilter cover = getCover();
        assert cover != null;
        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    new CoverDataFollowerToggleButtonWidget<>(),
                    CoverItemFilter::isWhitelist,
                    CoverItemFilter::setWhitelist,
                    widget -> widget
                        .setToggleTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST, GTUITextures.OVERLAY_BUTTON_WHITELIST)
                        .addTooltip(0, StatCollector.translateToLocal("gt.interact.desc.Item_Filter.Whitelist_Mode"))
                        .addTooltip(1, StatCollector.translateToLocal("gt.interact.desc.Item_Filter.Blacklist_Mode"))
                        .setPos(spaceX * 0, spaceY * 0))
                    .setPos(startX, startY))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Item_Filter.Filter"))
                    .setPos(startX + spaceX * 0, 3 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.Item_Filter.CheckMode"))
                    .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
            .widget(
                SlotWidget.phantom(cover.getFilter(), 0)
                    .setPos(startX + spaceX * 0, startY + spaceY * 2));
    }
}
