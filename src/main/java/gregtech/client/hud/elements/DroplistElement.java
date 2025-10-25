package gregtech.client.hud.elements;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class DroplistElement extends WidgetElement<DroplistElement> implements Configurable {

    private final Supplier<List<String>> optionsSupplier;
    private int selectedIndex;
    private boolean expanded;
    private final Consumer<Integer> onSelect;
    private int itemHeight = HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;

    public DroplistElement(Supplier<List<String>> optionsSupplier, int initialIndex, Consumer<Integer> onSelect) {
        this.optionsSupplier = optionsSupplier;
        this.selectedIndex = initialIndex;
        this.onSelect = onSelect;
        this.width = HUDManager.UIConstants.CONFIG_BUTTON_WIDTH;
        this.height = HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setItemHeight(int height) {
        this.itemHeight = height;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        List<String> options = optionsSupplier.get();
        if (selectedIndex < 0 || selectedIndex >= options.size()) selectedIndex = 0;

        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.BUTTON_BG_R,
            HUDManager.UIConstants.BUTTON_BG_G,
            HUDManager.UIConstants.BUTTON_BG_B,
            HUDManager.UIConstants.BUTTON_ALPHA,
            HUDManager.UIConstants.BUTTON_HOVER_R,
            HUDManager.UIConstants.BUTTON_HOVER_G,
            HUDManager.UIConstants.BUTTON_HOVER_B,
            HUDManager.UIConstants.BUTTON_ALPHA);
        HUDManager.UIConstants.renderAlignedText(
            fr,
            options.get(selectedIndex),
            absLeft(baseX) + HUDManager.UIConstants.DROPLIST_TEXT_OFFSET_X,
            absTop(baseY) + HUDManager.UIConstants.DROPLIST_TEXT_OFFSET_Y,
            width,
            CompositeWidget.HudAlignment.CENTER,
            HUDManager.UIConstants.TEXT_COLOR);

        if (expanded) {
            HUDManager.HUDUtils.drawHudRect(
                absLeft(baseX),
                absBottom(baseY),
                absRight(baseX),
                absBottom(baseY) + itemHeight * options.size(),
                HUDManager.UIConstants.DROPLIST_PANE_R,
                HUDManager.UIConstants.DROPLIST_PANE_G,
                HUDManager.UIConstants.DROPLIST_PANE_B,
                HUDManager.UIConstants.DROPLIST_PANE_ALPHA);
            for (int i = 0; i < options.size(); i++) {
                int itemY = absBottom(baseY) + i * itemHeight;
                boolean itemHovered = mouseY >= itemY && mouseY <= itemY + itemHeight
                    && mouseX >= absLeft(baseX)
                    && mouseX <= absRight(baseX);
                if (itemHovered) {
                    HUDManager.HUDUtils.drawHudRect(
                        absLeft(baseX),
                        itemY,
                        absRight(baseX),
                        itemY + itemHeight,
                        HUDManager.UIConstants.DROPLIST_ITEM_HOVER_R,
                        HUDManager.UIConstants.DROPLIST_ITEM_HOVER_G,
                        HUDManager.UIConstants.DROPLIST_ITEM_HOVER_B,
                        HUDManager.UIConstants.DROPLIST_ITEM_ALPHA);
                }
                HUDManager.UIConstants.renderAlignedText(
                    fr,
                    options.get(i),
                    absLeft(baseX) + HUDManager.UIConstants.DROPLIST_TEXT_OFFSET_X,
                    itemY + HUDManager.UIConstants.DROPLIST_TEXT_OFFSET_Y,
                    width,
                    CompositeWidget.HudAlignment.LEFT,
                    HUDManager.UIConstants.TEXT_COLOR);
            }
        }
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        if (button == 0) {
            if (expanded) {
                List<String> options = optionsSupplier.get();
                for (int i = 0; i < options.size(); i++) {
                    int itemY = absBottom(baseY) + i * itemHeight;
                    if (mouseY >= itemY && mouseY <= itemY + itemHeight
                        && mouseX >= absLeft(baseX)
                        && mouseX <= absRight(baseX)) {
                        selectedIndex = i;
                        onSelect.accept(i);
                        expanded = false;
                        return true;
                    }
                }
                expanded = false;
            } else if (isMouseWithinBounds(mouseX, mouseY, baseX, baseY)) {
                expanded = true;
                return true;
            }
        }
        return super.onMouseClick(mouseX, mouseY, button, baseX, baseY);
    }

    @Override
    public boolean acceptsMouseOutside(double mouseX, double mouseY, int baseX, int baseY) {
        if (expanded) {
            List<String> options = optionsSupplier.get();
            return mouseY >= absBottom(baseY) && mouseY <= absBottom(baseY) + itemHeight * options.size()
                && mouseX >= absLeft(baseX)
                && mouseX <= absRight(baseX);
        }
        return false;
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", this.offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", this.offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }
}
