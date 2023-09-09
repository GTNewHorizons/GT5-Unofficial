package gregtech.api.gui.widgets;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import gregtech.api.gui.widgets.GT_GuiTabLine.GT_GuiTabIconSet;
import gregtech.api.gui.widgets.GT_GuiTabLine.GT_ITabRenderer;
import gregtech.api.interfaces.IGuiIcon;

/**
 * A tab to be attached to a tab line
 */
public class GT_GuiTab {

    private static final int SLOT_SIZE = 18;

    public boolean visible = true, mousedOver, enabled = true;

    private Rectangle bounds;
    private final GT_GuiTabIconSet tabBackground;
    private final ItemStack item;
    private final GT_ITabRenderer gui;
    private GT_GuiTooltip tooltip;
    private final IGuiIcon overlay;
    private final boolean flipHorizontally;

    /**
     * A tab to be attached to a tab line
     *
     * @param gui              IGregTechTileEntity the tab line this tab belongs to is attached to
     * @param id               both the ID and position in the tab line of this tab. Not used, kept for compatibility.
     * @param bounds           bounds of this tab
     * @param tabBackground    set of background textures
     * @param item             item to draw atop the background texture, not colored
     * @param overlay          texture to draw atop the background texture, not colored
     * @param tooltipText      tooltip of this tab
     * @param flipHorizontally whether to draw this tab on the right side of the IGregTechTileEntity
     */
    public GT_GuiTab(GT_ITabRenderer gui, int id, Rectangle bounds, GT_GuiTabIconSet tabBackground, ItemStack item,
        IGuiIcon overlay, String[] tooltipText, boolean flipHorizontally) {
        this.gui = gui;
        this.bounds = bounds;
        this.item = item;
        this.tabBackground = tabBackground;
        this.overlay = overlay;
        if (tooltipText != null) {
            setTooltipText(tooltipText);
        }
        this.flipHorizontally = flipHorizontally;
    }

    public GT_GuiTab(GT_ITabRenderer gui, int id, Rectangle bounds, GT_GuiTabIconSet tabBackground) {
        this(gui, id, bounds, tabBackground, null, null, null, false);
    }

    /**
     * Set this tab's tooltip text
     *
     * @param text text to set
     * @return This tab for chaining
     */
    public GT_GuiTab setTooltipText(String... text) {
        if (tooltip == null) {
            tooltip = new GT_GuiTooltip(bounds, text);
            gui.addToolTip(tooltip);
        } else {
            tooltip.setToolTipText(text);
        }
        return this;
    }

    /**
     * @return This tab's tooltip object
     */
    public GT_GuiTooltip getTooltip() {
        return tooltip;
    }

    /**
     * Draw the background texture for this tab
     *
     * @param mouseX   not used, likely kept for backward compatibility
     * @param mouseY   not used, likely kept for backward compatibility
     * @param parTicks not used, likely kept for backward compatibility
     */
    public void drawBackground(int mouseX, int mouseY, float parTicks) {
        if (this.visible) {
            GT_GuiIcon.render(
                getBackgroundTexture(),
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height,
                1,
                true,
                this.flipHorizontally);
        }
    }

    /**
     * Draw overlay textures and items atop the background texture
     *
     * @param mouseX   X mouse coordinate
     * @param mouseY   Y mouse coordinate
     * @param parTicks not used, likely kept for backward compatibility
     */
    public void drawOverlays(int mouseX, int mouseY, float parTicks) {
        this.mousedOver = bounds.contains(mouseX, mouseY);

        if (this.tooltip != null) {
            this.tooltip.enabled = this.visible;
        }

        if (this.visible) {
            if (overlay != null) {
                GL11.glColor4f(1, 1, 1, 1);
                GT_GuiIcon.render(overlay, bounds.x, bounds.y, bounds.width, bounds.height, 1, true);
            }
            if (item != null) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

                if (item.getItem() instanceof ItemBlock) {
                    GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                }
                int margin = (bounds.height - SLOT_SIZE);
                gui.getItemRenderer()
                    .renderItemAndEffectIntoGUI(
                        gui.getFontRenderer(),
                        Minecraft.getMinecraft()
                            .getTextureManager(),
                        item,
                        bounds.x + (this.flipHorizontally ? 0 : margin),
                        bounds.y + margin);

                if (item.getItem() instanceof ItemBlock) GL11.glPopAttrib();

                GL11.glPopAttrib();
            }
        }
    }

    /**
     * @return the texture this tab should currently use as it's background
     */
    protected IGuiIcon getBackgroundTexture() {
        if (!enabled) return tabBackground.disabled;

        return mousedOver ? tabBackground.highlight : tabBackground.normal;
    }

    /**
     * @return the screen space occupied by this tab
     */
    public Rectangle getBounds() {
        return this.bounds;
    }

    /**
     * Reposition this tab on the screen
     *
     * @param xPos X tab coordinate
     * @param yPos Y tab coordinate
     */
    public void setPosition(int xPos, int yPos) {
        this.bounds = new Rectangle(xPos, yPos, bounds.width, bounds.height);
    }
}
