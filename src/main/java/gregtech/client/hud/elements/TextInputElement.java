package gregtech.client.hud.elements;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class TextInputElement extends WidgetElement<TextInputElement> implements Configurable {

    public String text = "";
    private boolean focused;
    private final Consumer<String> onChange;
    private int cursorPos;
    private long lastBlinkTime;

    public TextInputElement(Consumer<String> onChange) {
        this.onChange = onChange;
        this.cursorPos = 0;
        this.lastBlinkTime = Minecraft.getSystemTime();
        this.width = HUDManager.UIConstants.TEXT_INPUT_WIDTH;
        this.height = HUDManager.UIConstants.TEXT_INPUT_HEIGHT;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.BG_R,
            HUDManager.UIConstants.BG_G,
            HUDManager.UIConstants.BG_B,
            HUDManager.UIConstants.BG_ALPHA,
            HUDManager.UIConstants.HOVER_R,
            HUDManager.UIConstants.HOVER_G,
            HUDManager.UIConstants.HOVER_B,
            HUDManager.UIConstants.BG_ALPHA);

        String displayText = text;
        if (focused && (Minecraft.getSystemTime() - lastBlinkTime) % HUDManager.UIConstants.CURSOR_BLINK_MS * 2
            < HUDManager.UIConstants.CURSOR_BLINK_MS) {
            if (cursorPos < text.length()) {
                displayText = text.substring(0, cursorPos) + "_" + text.substring(cursorPos);
            } else {
                displayText = text + "_";
            }
        }

        HUDManager.UIConstants.renderAlignedText(
            fr,
            displayText,
            absLeft(baseX) + HUDManager.UIConstants.TEXT_INPUT_OFFSET_X,
            absTop(baseY) + HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST,
            width,
            CompositeWidget.HudAlignment.LEFT,
            HUDManager.UIConstants.TEXT_COLOR);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        if (button == 0 && isMouseWithinBounds(mouseX, mouseY, baseX, baseY)) {
            focused = true;
            cursorPos = text.length();
            Keyboard.enableRepeatEvents(true);
            return true;
        }
        return super.onMouseClick(mouseX, mouseY, button, baseX, baseY);
    }

    public void unfocus() {
        focused = false;
        Keyboard.enableRepeatEvents(false);
        onChange.accept(text);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!focused) return;

        if (keyCode == Keyboard.KEY_BACK) {
            if (cursorPos > 0) {
                text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                cursorPos--;
            }
        } else if (keyCode == Keyboard.KEY_DELETE) {
            if (cursorPos < text.length()) {
                text = text.substring(0, cursorPos) + text.substring(cursorPos + 1);
            }
        } else if (keyCode == Keyboard.KEY_LEFT) {
            if (cursorPos > 0) cursorPos--;
        } else if (keyCode == Keyboard.KEY_RIGHT) {
            if (cursorPos < text.length()) cursorPos++;
        } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_ESCAPE) {
            unfocus();
        } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            text = text.substring(0, cursorPos) + typedChar + text.substring(cursorPos);
            cursorPos++;
        }

        lastBlinkTime = Minecraft.getSystemTime();
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
