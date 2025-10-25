package gregtech.client.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDManager.UIConstants;
import gregtech.client.hud.elements.ButtonElement;
import gregtech.client.hud.elements.CheckboxElement;
import gregtech.client.hud.elements.Configurable;
import gregtech.client.hud.elements.RectElement;
import gregtech.client.hud.elements.TextElement;
import gregtech.client.hud.elements.TextInputElement;
import gregtech.client.hud.elements.WidgetElement;

@SideOnly(Side.CLIENT)
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
                new TextElement(label).colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            CheckboxElement checkbox = new CheckboxElement(label, currentValue, setter)
                .pos(100, yOff - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(UIConstants.TEXT_INPUT_HEIGHT, UIConstants.TEXT_INPUT_HEIGHT);

            gui.addConfigElement(checkbox);

            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createStringConfig(HUDGui.GuiConfigureElement gui, String label, String currentValue,
            Consumer<String> setter, int yOff) {
            gui.addConfigElement(
                new TextElement(label).colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            TextInputElement input = new TextInputElement(setter).pos(100, yOff - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(UIConstants.TEXT_INPUT_WIDTH, UIConstants.TEXT_INPUT_HEIGHT);

            input.text = String.valueOf(currentValue);
            gui.addConfigElement(input);
            gui.addInput(input);

            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        public static int createColorConfig(HUDGui.GuiConfigureElement gui, String label, float red, float green,
            float blue, float alpha, DoubleConsumer redSetter, DoubleConsumer greenSetter, DoubleConsumer blueSetter,
            DoubleConsumer alphaSetter, int yOff) {
            // Add label for color components
            gui.addConfigElement(
                new TextElement(label + " (R G B A)").colorSupplier(() -> UIConstants.TEXT_COLOR)
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));

            final int inputWidth = UIConstants.TEXT_INPUT_WIDTH / 5 * 3;
            final int inputSpacing = 2;

            // Add text input for red
            TextInputElement redInput = new TextInputElement(text -> {
                try {
                    redSetter.accept(Float.parseFloat(text));
                } catch (NumberFormatException ignored) {}
            }).pos(
                UIConstants.CONFIG_BUTTON_OFFSET_X,
                yOff + UIConstants.CONFIG_BUTTON_SPACING - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(inputWidth, UIConstants.TEXT_INPUT_HEIGHT);
            redInput.text = String.valueOf(red);
            gui.addConfigElement(redInput);
            gui.addInput(redInput);

            // Add text input for green
            TextInputElement greenInput = new TextInputElement(text -> {
                try {
                    greenSetter.accept(Float.parseFloat(text));
                } catch (NumberFormatException ignored) {}
            }).pos(
                UIConstants.CONFIG_BUTTON_OFFSET_X + inputWidth + inputSpacing,
                yOff + UIConstants.CONFIG_BUTTON_SPACING - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(inputWidth, UIConstants.TEXT_INPUT_HEIGHT);
            greenInput.text = String.valueOf(green);
            gui.addConfigElement(greenInput);
            gui.addInput(greenInput);

            // Add text input for blue
            TextInputElement blueInput = new TextInputElement(text -> {
                try {
                    blueSetter.accept(Float.parseFloat(text));
                } catch (NumberFormatException ignored) {}
            }).pos(
                UIConstants.CONFIG_BUTTON_OFFSET_X + (inputWidth + inputSpacing) * 2,
                yOff + UIConstants.CONFIG_BUTTON_SPACING - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(inputWidth, UIConstants.TEXT_INPUT_HEIGHT);
            blueInput.text = String.valueOf(blue);
            gui.addConfigElement(blueInput);
            gui.addInput(blueInput);

            // Add text input for alpha
            TextInputElement alphaInput = new TextInputElement(text -> {
                try {
                    alphaSetter.accept(Float.parseFloat(text));
                } catch (NumberFormatException ignored) {}
            }).pos(
                UIConstants.CONFIG_BUTTON_OFFSET_X + (inputWidth + inputSpacing) * 3,
                yOff + UIConstants.CONFIG_BUTTON_SPACING - UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
                .size(inputWidth, UIConstants.TEXT_INPUT_HEIGHT);
            alphaInput.text = String.valueOf(alpha);
            gui.addConfigElement(alphaInput);
            gui.addInput(alphaInput);

            return yOff + UIConstants.CONFIG_BUTTON_SPACING * 2;
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

            int panelHeight = calculateConfigPanelHeight();

            configPanel = new CompositeWidget(
                width / 2 - UIConstants.CONFIG_PANEL_WIDTH / 2,
                height / 2 - panelHeight / 2).addElement(
                    new RectElement().size(UIConstants.CONFIG_PANEL_WIDTH, panelHeight)
                        .colorRGBA(
                            UIConstants.MODAL_BG_R,
                            UIConstants.MODAL_BG_G,
                            UIConstants.MODAL_BG_B,
                            UIConstants.MODAL_BG_ALPHA));

            int yOff = UIConstants.CONFIG_PANEL_PADDING;

            if (!navigationStack.isEmpty()) {
                yOff = addBackButton(yOff);
            }

            yOff = addCloseButton(yOff);

            yOff = addSubElements(yOff);

            if (target instanceof Configurable configurable) {
                yOff = configurable.configure(this, yOff);
            }
        }

        private int addBackButton(int yOff) {
            configPanel.addElement(
                new ButtonElement(
                    "Back",
                    () -> mc.displayGuiScreen(
                        new GuiConfigureElement(
                            navigationStack.get(navigationStack.size() - 1),
                            navigationStack.subList(0, navigationStack.size() - 1))))
                                .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff)
                                .size(UIConstants.CONFIG_BUTTON_WIDTH / 2 - 2, UIConstants.CONFIG_BUTTON_HEIGHT));
            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        private int addCloseButton(int yOff) {
            configPanel.addElement(
                new ButtonElement("Close", () -> mc.displayGuiScreen(null))
                    .pos(UIConstants.CONFIG_BUTTON_OFFSET_X + UIConstants.CONFIG_BUTTON_WIDTH / 2 + 2, yOff)
                    .size(UIConstants.CONFIG_BUTTON_WIDTH / 2 - 2, UIConstants.CONFIG_BUTTON_HEIGHT));
            return yOff + UIConstants.CONFIG_BUTTON_SPACING;
        }

        private int addSubElements(int yOff) {
            List<WidgetElement<?>> subElements = null;
            if (target instanceof CompositeWidget widget) {
                subElements = widget.elements;
            } else if (target instanceof WidgetElement<?>element) {
                subElements = element.children;
            }

            int subCount = subElements != null ? subElements.size() : 0;
            if (subCount > 0) {
                configPanel.addElement(
                    new TextElement("Sub-elements:").colorSupplier(() -> UIConstants.TEXT_COLOR)
                        .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff));
                yOff += UIConstants.CONFIG_BUTTON_SPACING;

                for (int i = 0; i < subCount; i++) {
                    WidgetElement<?> child = subElements.get(i);
                    List<Object> newStack = new ArrayList<>(navigationStack);
                    if (target instanceof CompositeWidget) {
                        newStack.add(target);
                    } else {
                        newStack.addAll(navigationStack);
                        newStack.add(target);
                    }
                    configPanel.addElement(
                        new ButtonElement(
                            child.getClass()
                                .getSimpleName() + " ["
                                + i
                                + "]",
                            () -> mc.displayGuiScreen(new GuiConfigureElement(child, newStack)))
                                .pos(UIConstants.CONFIG_BUTTON_OFFSET_X, yOff)
                                .size(UIConstants.CONFIG_BUTTON_WIDTH, UIConstants.CONFIG_BUTTON_HEIGHT));
                    yOff += UIConstants.CONFIG_BUTTON_SPACING;
                }
            }
            return yOff;
        }

        private int calculateConfigPanelHeight() {
            int height = UIConstants.CONFIG_PANEL_PADDING * 2
                + UIConstants.CONFIG_BUTTON_SPACING * (navigationStack.isEmpty() ? 1 : 2);

            int subCount = 0;
            if (target instanceof CompositeWidget widget) {
                subCount = widget.elements.size();
            } else if (target instanceof WidgetElement<?>element) {
                subCount = element.children.size();
            }

            if (subCount > 0) {
                height += subCount * HUDManager.UIConstants.CONFIG_BUTTON_SPACING
                    + HUDManager.UIConstants.CONFIG_BUTTON_SPACING;
            }

            int configHeight = 0;
            if (target instanceof Configurable configurable) {
                GuiConfigureElement dummy = new DummyConfigure(target, navigationStack);
                configHeight = configurable.configure(dummy, 0);
            }
            height += configHeight;

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

    private static class DummyConfigure extends GuiConfigureElement {

        protected DummyConfigure(Object target, List<Object> navigationStack) {
            super(target, navigationStack);
        }

        @Override
        public void addConfigElement(WidgetElement<?> element) {
            // Do nothing
        }

        @Override
        public void addInput(TextInputElement input) {
            // Do nothing
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
