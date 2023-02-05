package gregtech.api.gui.widgets;

import java.awt.Rectangle;

import gregtech.api.util.GT_TooltipDataCache.TooltipData;

public class GT_GuiSmartTooltip extends GT_GuiTooltip {

    public interface TooltipVisibilityProvider {

        boolean shouldShowTooltip();
    }

    private final TooltipVisibilityProvider visibilityProvider;

    public GT_GuiSmartTooltip(Rectangle bounds, TooltipVisibilityProvider visibilityProvider, TooltipData data) {
        super(bounds, data);
        this.visibilityProvider = visibilityProvider;
    }

    @Override
    protected void onTick() {
        super.onTick();
        // If disabled by super, stay disabled.
        this.enabled = this.enabled && this.visibilityProvider.shouldShowTooltip();
    }
}
