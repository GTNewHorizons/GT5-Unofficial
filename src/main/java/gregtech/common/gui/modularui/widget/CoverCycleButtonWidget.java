package gregtech.common.gui.modularui.widget;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.modularui.GTUITextures;

/**
 * Fires click action on mouse release, not on press. Draws different backgrounds depending on whether the mouse is
 * being pressed or the widget is hovered.
 */
public class CoverCycleButtonWidget extends CycleButtonWidget {

    private static final UITexture BUTTON_NORMAL_NOT_PRESSED = GTUITextures.BUTTON_COVER_NORMAL
        .getSubArea(0, 0, 1, 0.5f);
    private static final UITexture BUTTON_NORMAL_PRESSED = GTUITextures.BUTTON_COVER_NORMAL.getSubArea(0, 0.5f, 1, 1);
    private static final UITexture BUTTON_HOVERED_NOT_PRESSED = GTUITextures.BUTTON_COVER_NORMAL_HOVERED
        .getSubArea(0, 0, 1, 0.5f);
    private static final UITexture BUTTON_HOVERED_PRESSED = GTUITextures.BUTTON_COVER_NORMAL_HOVERED
        .getSubArea(0, 0.5f, 1, 1);

    private boolean clickPressed;

    private static final int TOOLTIP_DELAY = 5;

    public CoverCycleButtonWidget() {
        setSize(16, 16);
        setTooltipShowUpDelay(TOOLTIP_DELAY);
    }

    @Override
    public ClickResult onClick(int buttonId, boolean doubleClick) {
        updateState();
        if (!canClick()) return ClickResult.REJECT;
        clickPressed = true;
        return ClickResult.SUCCESS;
    }

    @Override
    public boolean onClickReleased(int buttonId) {
        clickPressed = false;
        updateState();
        if (!isHovering() || !canClick()) return false;
        return onClickImpl(buttonId);
    }

    protected boolean onClickImpl(int buttonId) {
        super.onClick(buttonId, false);
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canClick() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    protected void updateState() {}

    public boolean isClickPressed() {
        return clickPressed;
    }

    @Override
    public void drawBackground(float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        super.drawBackground(partialTicks);
    }

    @Override
    public IDrawable[] getBackground() {
        if (isHovering()) {
            if (clickPressed) {
                return new IDrawable[] { BUTTON_HOVERED_PRESSED };
            } else {
                return new IDrawable[] { BUTTON_HOVERED_NOT_PRESSED };
            }
        } else {
            if (clickPressed) {
                return new IDrawable[] { BUTTON_NORMAL_PRESSED };
            } else {
                return new IDrawable[] { BUTTON_NORMAL_NOT_PRESSED };
            }
        }
    }
}
