package gregtech.client.hud.elements;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import gregtech.client.hud.HUDManager;

public abstract class WidgetElement<T extends WidgetElement<T>> {

    public int offsetX;
    public int offsetY;
    public int width;
    public int height;
    public int offsetHorizontal;
    public int offsetVertical;
    protected float red = 1.0f, green = 1.0f, blue = 1.0f, alpha = 1.0f;
    protected boolean hasColor, hoverHighlight = true;
    public final List<WidgetElement<?>> children = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public T pos(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T size(int width, int height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T offset(int horizontal, int vertical) {
        this.offsetHorizontal = horizontal;
        this.offsetVertical = vertical;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T offsetHorizontal(int horizontal) {
        this.offsetHorizontal = horizontal;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T offsetVertical(int vertical) {
        this.offsetVertical = vertical;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T colorRGBA(float r, float g, float b, float a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        this.hasColor = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T hoverHighlight(boolean highlight) {
        this.hoverHighlight = highlight;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addElement(WidgetElement<?> child) {
        children.add(child);
        child.updateLayout();
        return (T) this;
    }

    public void render(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderSelf(mc, fr, baseX, baseY, mouseX, mouseY);
        children.forEach(child -> child.render(mc, fr, absLeft(baseX), absTop(baseY), mouseX, mouseY));
    }

    protected abstract void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX,
        double mouseY);

    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        return children.stream()
            .anyMatch(child -> child.onMouseClick(mouseX, mouseY, button, absLeft(baseX), absTop(baseY)));
    }

    public void onMouseRelease(int button) {
        children.forEach(child -> child.onMouseRelease(button));
    }

    public void handleMouseDrag(double mouseX, double mouseY, int baseX, int baseY) {
        children.forEach(child -> child.handleMouseDrag(mouseX, mouseY, absLeft(baseX), absTop(baseY)));
    }

    protected int absLeft(int baseX) {
        return baseX + offsetX + offsetHorizontal;
    }

    protected int absTop(int baseY) {
        return baseY + offsetY + offsetVertical;
    }

    protected int absRight(int baseX) {
        return baseX + offsetX + offsetHorizontal + width;
    }

    protected int absBottom(int baseY) {
        return baseY + offsetY + offsetVertical + height;
    }

    public boolean isMouseWithin(double mouseX, double mouseY, int baseX, int baseY) {
        return isMouseWithinBounds(mouseX, mouseY, baseX, baseY) || children.stream()
            .anyMatch(child -> child.isMouseWithin(mouseX, mouseY, absLeft(baseX), absTop(baseY)));
    }

    public boolean isMouseWithinBounds(double mouseX, double mouseY, int baseX, int baseY) {
        return mouseX >= absLeft(baseX) && mouseX <= absRight(baseX)
            && mouseY >= absTop(baseY)
            && mouseY <= absBottom(baseY);
    }

    protected void renderBackground(int baseX, int baseY, double mouseX, double mouseY, float normalR, float normalG,
        float normalB, float normalA, float hoverR, float hoverG, float hoverB, float hoverA) {
        boolean hovered = hoverHighlight && isMouseWithinBounds(mouseX, mouseY, baseX, baseY);
        float r = hasColor ? red : (hovered ? hoverR : normalR);
        float g = hasColor ? green : (hovered ? hoverG : normalG);
        float b = hasColor ? blue : (hovered ? hoverB : normalB);
        float a = hasColor ? alpha : (hovered ? hoverA : normalA);
        HUDManager.HUDUtils.drawHudRect(absLeft(baseX), absTop(baseY), absRight(baseX), absBottom(baseY), r, g, b, a);
    }

    public boolean acceptsMouseOutside(double mouseX, double mouseY, int baseX, int baseY) {
        return children.stream()
            .anyMatch(child -> child.acceptsMouseOutside(mouseX, mouseY, absLeft(baseX), absTop(baseY)));
    }

    protected TextInputElement createWidthInput(int yOff) {
        return new TextInputElement(text -> {
            try {
                width = Integer.parseInt(text);
                updateAfterConfigChange();
            } catch (NumberFormatException ignored) {}
        }).pos(70, yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
            .size(HUDManager.UIConstants.TEXT_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);
    }

    protected TextInputElement createOffsetInput(int yOff, boolean isHorizontal) {
        return new TextInputElement(text -> {
            try {
                if (isHorizontal) {
                    offsetHorizontal = Integer.parseInt(text);
                } else {
                    offsetVertical = Integer.parseInt(text);
                }
                updateAfterConfigChange();
            } catch (NumberFormatException ignored) {}
        }).pos(
            isHorizontal ? HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X
                : HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X + 40,
            yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
            .size(HUDManager.UIConstants.MARGIN_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);
    }

    protected void updateAfterConfigChange() {
        updateLayout();
    }

    public void updateLayout() {
        children.forEach(WidgetElement::updateLayout);
    }
}
