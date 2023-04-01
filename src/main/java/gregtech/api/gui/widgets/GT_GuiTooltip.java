package gregtech.api.gui.widgets;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import gregtech.api.util.GT_TooltipDataCache.TooltipData;

public class GT_GuiTooltip {

    protected Rectangle bounds;
    protected TooltipData data;
    private List<String> displayedText;
    public boolean enabled = true;

    /**
     * Used to create a tooltip that will appear over the specified bounds. This will initially be a "static" tooltip
     * that doesn't respect verbosity levels or respond to the shift key.
     *
     * @param bounds
     * @param text
     */
    public GT_GuiTooltip(Rectangle bounds, String... text) {
        this.bounds = bounds;
        setToolTipText(text);
    }

    /**
     * Used to create a tooltip that will appear over the specified bounds. This will initially be a "dynamic" tooltip
     * that respects verbosity levels and responds to the shift key.
     *
     * @param bounds
     * @param data
     */
    public GT_GuiTooltip(Rectangle bounds, TooltipData data) {
        this.bounds = bounds;
        // Trust that the tooltips have already been formatted and colored, just make sure it has no nulls
        this.data = sanitizeTooltipData(data);
    }

    private TooltipData sanitizeTooltipData(TooltipData data) {
        if (data.text == null) {
            data.text = List.of();
        }
        if (data.shiftText == null) {
            data.shiftText = List.of();
        }
        return data;
    }

    /**
     * Called before the tooltip manager checks whether this tooltip is enabled
     */
    protected void onTick() {
        // Switch which of our 2 stored texts we're displaying now.
        this.displayedText = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.data.shiftText : this.data.text;
        // If this text is empty, let's not display a tooltip at all.
        this.enabled = this.displayedText.size() != 0;
    }

    /**
     * Called once this tooltip has been determined to be enabled
     */
    protected void updateText() {}

    /**
     * Used to set a "static" tooltip that doesn't respect verbosity levels or respond to the shift key
     *
     * @param text
     */
    public void setToolTipText(String... text) {
        this.data = formatTooltip(text);
        this.displayedText = data.text;
    }

    /**
     * Used to set a "dynamic" tooltip that respects verbosity levels and responds to the shift key
     *
     * @param data
     */
    public void setToolTipText(TooltipData data) {
        // Trust that the tooltips have already been formatted and colored, just make sure it has no nulls
        this.data = sanitizeTooltipData(data);
    }

    /**
     * Apply tooltip colors in case the text doesn't contain them and return as tooltip data
     *
     * @param text
     * @return colored tooltip lines as list
     */
    protected TooltipData formatTooltip(String[] text) {
        List<String> list;
        if (text != null) {
            list = new ArrayList<>(text.length);
            for (String s : text) {
                if (s == null) continue;
                if (list.isEmpty()) list.add("\u00a7f" + s);
                else list.add("\u00a77" + s);
            }
        } else {
            list = Collections.emptyList();
        }
        return new TooltipData(list, list);
    }

    public List<String> getToolTipText() {
        return this.displayedText;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDelayed() {
        return true;
    }
}
