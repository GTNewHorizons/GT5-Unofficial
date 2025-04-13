package gregtech.common.gui.mui1.cover;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
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
        ItemStackHandler filterInvHandler = new ItemStackHandler(1);
        CoverItemFilter cover = getCover();
        if (cover != null) {
            filterInvHandler.setStackInSlot(0, setStackSize1(cover.getFilter()));
        }
        builder
            .widget(
                new CoverDataControllerWidget<>(this::getCover, getUIBuildContext()).addFollower(
                    new CoverDataFollowerToggleButtonWidget<>(),
                    CoverItemFilter::isWhitelist,
                    CoverItemFilter::setWhitelist,
                    widget -> widget
                        .setToggleTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST, GTUITextures.OVERLAY_BUTTON_WHITELIST)
                        .addTooltip(0, GTUtility.trans("125.1", "Whitelist Mode"))
                        .addTooltip(1, GTUtility.trans("124.1", "Blacklist Mode"))
                        .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        new CoverDataFollowerSlotWidget<>(filterInvHandler, 0, true),
                        coverData -> setStackSize1(coverData.getFilter()),
                        (coverData, stack) -> coverData.setFilter(setStackSize1(stack)),
                        widget -> widget.setBackground(GTUITextures.SLOT_DARK_GRAY)
                            .setPos(spaceX * 0, spaceY * 2))
                    .setPos(startX, startY))
            .widget(
                new TextWidget(GTUtility.trans("317", "Filter: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 0, 3 + startY + spaceY * 1))
            .widget(
                new TextWidget(GTUtility.trans("318", "Check Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0));
    }

    private ItemStack setStackSize1(ItemStack stack) {
        if (stack != null) {
            stack.stackSize = 1;
        }
        return stack;
    }
}
