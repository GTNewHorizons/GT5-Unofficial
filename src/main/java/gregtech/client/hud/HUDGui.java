package gregtech.client.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;

import gregtech.client.hud.HUDManager.UIConstants;
import gregtech.client.hud.elements.*;

public class HUDGui {

    // ----- GUI Widget Manager -----

    public static class GuiWidgetManager extends GuiScreen {

        private final CompositeWidget target;
        private CompositeWidget configPanel;

        public GuiWidgetManager(CompositeWidget target) {
            this.target = target;
        }

        @Override
        public void initGui() {
            Mouse.setGrabbed(false);
            configPanel = new CompositeWidget(
                width / 2 - HUDManager.UIConstants.CONFIG_PANEL_WIDTH / 2,
                height / 2 - HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT - HUDManager.UIConstants.CONFIG_PANEL_PADDING)
                    .addElement(
                        new RectElement()
                            .size(
                                HUDManager.UIConstants.CONFIG_PANEL_WIDTH,
                                HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT * 2
                                    + HUDManager.UIConstants.CONFIG_PANEL_PADDING * 2)
                            .colorRGBA(
                                HUDManager.UIConstants.MODAL_BG_R,
                                HUDManager.UIConstants.MODAL_BG_G,
                                HUDManager.UIConstants.MODAL_BG_B,
                                HUDManager.UIConstants.MODAL_BG_ALPHA));

            if (target.getIsConfigurable()) {
                configPanel.addElement(
                    new ButtonElement("Configure", () -> mc.displayGuiScreen(new GuiConfigureElement(target)))
                        .pos(
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X,
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_Y)
                        .size(
                            HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2,
                            HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT));
            }
            if (target.getIsDeletable()) {
                configPanel.addElement(
                    new ButtonElement("Delete", target::removeSelf)
                        .pos(
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X
                                + HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2,
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_Y)
                        .size(
                            HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2,
                            HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT));
            }
            configPanel
                .addElement(
                    new ButtonElement("Close", () -> mc.displayGuiScreen(null))
                        .pos(
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X,
                            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_Y
                                + HUDManager.UIConstants.CONFIG_BUTTON_SPACING)
                        .size(
                            HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2,
                            HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT));
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            HUDManager.HUDUtils.drawHudRect(
                0,
                0,
                width,
                height,
                HUDManager.UIConstants.OVERLAY_BG_R,
                HUDManager.UIConstants.OVERLAY_BG_G,
                HUDManager.UIConstants.OVERLAY_BG_B,
                HUDManager.UIConstants.OVERLAY_BG_ALPHA);
            configPanel.render(mc, mc.fontRenderer, mouseX, mouseY);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int button) {
            if (configPanel.elements.stream()
                .noneMatch(e -> e.onMouseClick(mouseX, mouseY, button, configPanel.x, configPanel.y))) {
                mc.displayGuiScreen(null);
            }
        }

        @Override
        public void onGuiClosed() {
            Mouse.setGrabbed(true);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }
    }

    // ----- GUI Configure Element -----

    public static class GuiConfigureElement extends GuiScreen {

        private final Object target;
        private final List<Object> navigationStack;
        private CompositeWidget configPanel;
        private final List<TextInputElement> allInputs = new ArrayList<>();

        public static int createIntConfig(HUDGui.GuiConfigureElement gui, String label, int currentValue,
            IntConsumer onChange, int yOff) {

            gui.addConfigElement(
                new TextElement(label).colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            TextInputElement input = new TextInputElement(text -> {
                try {
                    int val = Integer.parseInt(text);
                    onChange.accept(val);
                } catch (NumberFormatException ignored) {}
            }).pos(100, yOff - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(UIConstants.TEXT_INPUT_WIDTH, UIConstants.TEXT_INPUT_HEIGHT);

            input.text = String.valueOf(currentValue);
            gui.addConfigElement(input);
            gui.addInput(input);

            return yOff + HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createFloatConfig(HUDGui.GuiConfigureElement gui, String label, float currentValue,
            DoubleConsumer setter, int yOff) {
            gui.addConfigElement(
                new TextElement(label).colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            TextInputElement input = new TextInputElement(text -> {
                try {
                    setter.accept(Float.parseFloat(text));
                } catch (NumberFormatException ignored) {}
            }).pos(100, yOff - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(UIConstants.TEXT_INPUT_WIDTH, UIConstants.TEXT_INPUT_HEIGHT);

            input.text = String.valueOf(currentValue);
            gui.addConfigElement(input);
            gui.addInput(input);

            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createDoubleConfig(HUDGui.GuiConfigureElement gui, String label, double currentValue,
            DoubleConsumer setter, int yOff) {
            gui.addConfigElement(
                new TextElement(label).colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            TextInputElement input = new TextInputElement(text -> {
                try {
                    setter.accept(Double.parseDouble(text));
                } catch (NumberFormatException ignored) {}
            }).pos(100, yOff - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(UIConstants.TEXT_INPUT_WIDTH, UIConstants.TEXT_INPUT_HEIGHT);

            input.text = String.valueOf(currentValue);
            gui.addConfigElement(input);
            gui.addInput(input);

            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createBooleanConfig(HUDGui.GuiConfigureElement gui, String label, boolean currentValue,
            Consumer<Boolean> setter, int yOff) {
            gui.addConfigElement(
                new CheckboxElement(label, currentValue, setter).pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff)
                    .size(12, 12));
            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createStringConfig(HUDGui.GuiConfigureElement gui, String label, String currentValue,
            Consumer<String> onChange, int yOff) {
            TextInputElement input = new TextInputElement(onChange)
                .pos(
                    HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X,
                    yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(HUDManager.UIConstants.TEXT_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);

            input.text = currentValue;
            gui.addConfigElement(input);
            gui.addInput(input);

            return yOff + HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
        }

        public GuiConfigureElement(Object target) {
            this(target, new ArrayList<>());
        }

        public GuiConfigureElement(Object target, List<Object> navigationStack) {
            this.target = target;
            this.navigationStack = navigationStack;
        }

        @Override
        public void initGui() {
            Mouse.setGrabbed(false);
            int panelHeight = calculatePanelHeight();
            configPanel = new CompositeWidget(
                width / 2 - HUDManager.UIConstants.CONFIG_PANEL_WIDTH / 2,
                height / 2 - panelHeight / 2).addElement(
                    new RectElement().size(HUDManager.UIConstants.CONFIG_PANEL_WIDTH, panelHeight)
                        .colorRGBA(
                            HUDManager.UIConstants.MODAL_BG_R,
                            HUDManager.UIConstants.MODAL_BG_G,
                            HUDManager.UIConstants.MODAL_BG_B,
                            HUDManager.UIConstants.MODAL_BG_ALPHA));

            int yOff = HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_Y;

            List<WidgetElement<?>> subElements;
            String title;
            if (target instanceof CompositeWidget) {
                subElements = ((CompositeWidget) target).elements;
                title = "Elements:";
            } else {
                subElements = ((WidgetElement<?>) target).children;
                title = "Child Elements:";
            }
            yOff = addSubElementButtons(yOff, title, subElements);

            if (target instanceof WidgetElement<?>element && element instanceof Configurable) {
                yOff = ((Configurable) element).configure(this, yOff);
            }

            configPanel.addElement(new ButtonElement("Back", () -> {
                if (navigationStack.isEmpty()) {
                    if (target instanceof WidgetElement<?> && HUDManager.getInstance()
                        .getWidgets()
                        .stream()
                        .anyMatch(widget -> widget.elements.contains(target) && widget.getIsConfigurable())) {
                        mc.displayGuiScreen(
                            new GuiWidgetManager(
                                HUDManager.getInstance()
                                    .getWidgets()
                                    .stream()
                                    .filter(w -> w.elements.contains(target))
                                    .findFirst()
                                    .orElse(null)));
                    } else {
                        mc.displayGuiScreen(null);
                    }
                } else {
                    Object parent = navigationStack.remove(navigationStack.size() - 1);
                    mc.displayGuiScreen(new GuiConfigureElement(parent, navigationStack));
                }
            }).pos(
                HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2,
                panelHeight - HUDManager.UIConstants.CONFIG_BUTTON_SPACING
                    - HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT / 2)
                .size(HUDManager.UIConstants.CONFIG_BUTTON_WIDTH / 2, HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT));
        }

        private int addSubElementButtons(int yOff, String title, List<WidgetElement<?>> subElements) {
            if (!subElements.isEmpty()) {
                addConfigElement(
                    new TextElement(title).colorSupplier(() -> HUDManager.UIConstants.TEXT_COLOR)
                        .pos(HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));
                yOff += HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
                for (int i = 0; i < subElements.size(); i++) {
                    WidgetElement<?> e = subElements.get(i);
                    String label = e.getClass()
                        .getSimpleName() + " ["
                        + i
                        + "]";
                    addConfigElement(new ButtonElement(label, () -> {
                        List<Object> newStack = new ArrayList<>(navigationStack);
                        newStack.add(target);
                        mc.displayGuiScreen(new GuiConfigureElement(e, newStack));
                    }).pos(HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X, yOff)
                        .size(HUDManager.UIConstants.CONFIG_BUTTON_WIDTH, HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT));
                    yOff += HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
                }
            }
            return yOff;
        }

        private int calculatePanelHeight() {
            int height = HUDManager.UIConstants.CONFIG_PANEL_PADDING * 2 + HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;
            int subCount = 0;
            int configSpacings = 0;
            if (target instanceof CompositeWidget widget) {
                subCount = widget.elements.size();
            } else if (target instanceof WidgetElement<?>element) {
                subCount = element.children.size();
                if (element instanceof Configurable) {
                    configSpacings = ((Configurable) element).getConfigSpacingCount();
                }
            }
            if (subCount > 0) {
                height += subCount * HUDManager.UIConstants.CONFIG_BUTTON_SPACING
                    + HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
            }
            height += configSpacings * HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
            return height + HUDManager.UIConstants.CONFIG_PANEL_HEIGHT_BUFFER;
        }

        public void addConfigElement(WidgetElement<?> element) {
            configPanel.addElement(element);
        }

        public void addInput(TextInputElement input) {
            allInputs.add(input);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            HUDManager.HUDUtils.drawHudRect(
                0,
                0,
                width,
                height,
                HUDManager.UIConstants.OVERLAY_BG_R,
                HUDManager.UIConstants.OVERLAY_BG_G,
                HUDManager.UIConstants.OVERLAY_BG_B,
                HUDManager.UIConstants.OVERLAY_BG_ALPHA);
            configPanel.render(mc, mc.fontRenderer, mouseX, mouseY);

            if (target instanceof CompositeWidget widget) {
                drawHoveredSubElementOutline(widget.elements, widget.x, widget.y, mouseX, mouseY);
            } else if (target instanceof WidgetElement<?>element) {
                int[] abs = getAbsolutePosition();
                HUDManager.HUDUtils.drawRectOutline(
                    abs[0] - HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                    abs[1] - HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                    abs[0] + element.width + HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                    abs[1] + element.height + HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                    HUDManager.UIConstants.TILE_SELECTED_R,
                    HUDManager.UIConstants.TILE_SELECTED_G,
                    HUDManager.UIConstants.TILE_SELECTED_B,
                    HUDManager.UIConstants.TILE_SELECTED_ALPHA,
                    HUDManager.UIConstants.TILE_OUTLINE_WIDTH);
                drawHoveredSubElementOutline(element.children, abs[0], abs[1], mouseX, mouseY);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private void drawHoveredSubElementOutline(List<WidgetElement<?>> subElements, int parentX, int parentY,
            int mouseX, int mouseY) {
            for (int i = 0; i < subElements.size(); i++) {
                WidgetElement<?> child = subElements.get(i);
                String expectedLabel = child.getClass()
                    .getSimpleName() + " ["
                    + i
                    + "]";
                ButtonElement hoveredButton = configPanel.elements.stream()
                    .filter(
                        el -> el instanceof ButtonElement && ((ButtonElement) el).getLabel()
                            .equals(expectedLabel))
                    .map(el -> (ButtonElement) el)
                    .filter(button -> button.isMouseWithinBounds(mouseX, mouseY, configPanel.x, configPanel.y))
                    .findFirst()
                    .orElse(null);
                if (hoveredButton != null) {
                    int childAbsX = parentX + child.offsetX + child.offsetHorizontal;
                    int childAbsY = parentY + child.offsetY + child.offsetVertical;
                    HUDManager.HUDUtils.drawRectOutline(
                        childAbsX - HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                        childAbsY - HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                        childAbsX + child.width + HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                        childAbsY + child.height + HUDManager.UIConstants.TILE_OUTLINE_WIDTH,
                        HUDManager.UIConstants.TILE_SELECTED_R,
                        HUDManager.UIConstants.TILE_SELECTED_G,
                        HUDManager.UIConstants.TILE_SELECTED_B,
                        HUDManager.UIConstants.TILE_SELECTED_ALPHA,
                        HUDManager.UIConstants.TILE_OUTLINE_WIDTH);
                }
            }
        }

        private int[] getAbsolutePosition() {
            if (!(target instanceof WidgetElement<?>element) || navigationStack.isEmpty()) {
                return new int[] { 0, 0 };
            }
            CompositeWidget widget = (CompositeWidget) navigationStack.get(0);
            int absX = widget.x;
            int absY = widget.y;
            for (int i = 1; i < navigationStack.size(); i++) {
                WidgetElement<?> parent = (WidgetElement<?>) navigationStack.get(i);
                absX += parent.offsetX + parent.offsetHorizontal;
                absY += parent.offsetY + parent.offsetVertical;
            }
            absX += element.offsetX + element.offsetHorizontal;
            absY += element.offsetY + element.offsetVertical;
            return new int[] { absX, absY };
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int button) {
            unfocusAll();
            if (configPanel.elements.stream()
                .anyMatch(e -> e.onMouseClick(mouseX, mouseY, button, configPanel.x, configPanel.y))) return;
            mc.displayGuiScreen(null);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            allInputs.forEach(input -> input.keyTyped(typedChar, keyCode));
        }

        private void unfocusAll() {
            allInputs.forEach(TextInputElement::unfocus);
        }

        @Override
        public void onGuiClosed() {
            Mouse.setGrabbed(true);
            HUDManager.getInstance()
                .getWidgets()
                .forEach(CompositeWidget::updateLayouts);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }
    }

    // ----- GUI HUD Edit -----

    public static class GuiHUDEdit extends GuiScreen {

        private final HUDManager hud;

        public GuiHUDEdit(HUDManager hud) {
            this.hud = hud;
        }

        @Override
        public void initGui() {
            Mouse.setGrabbed(false);
        }

        @Override
        public void onGuiClosed() {
            Mouse.setGrabbed(true);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if (hud.getWidgets()
                .isEmpty()) {
                mc.displayGuiScreen(null);
                return;
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int button) {
            hud.handleMouseClick(mouseX, mouseY, button, 1.0f);
        }

        @Override
        protected void mouseClickMove(int mouseX, int mouseY, int button, long time) {
            hud.handleMouseDrag(mouseX, mouseY, button, 1.0f);
        }

        @Override
        protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
            if (button >= 0) hud.handleMouseRelease(button);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) {
            hud.getWidgets()
                .forEach(
                    widget -> widget.elements.stream()
                        .filter(e -> e instanceof TextInputElement)
                        .forEach(e -> ((TextInputElement) e).keyTyped(typedChar, keyCode)));
        }
    }
}
