package gregtech.api.multitileentity.data;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.mutecore.api.item.TooltipAssigner;

import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class TooltipComponent implements TooltipAssigner {

    private GT_Multiblock_Tooltip_Builder tooltip;

    public TooltipComponent(GT_Multiblock_Tooltip_Builder tooltip) {
        this.tooltip = tooltip;
    }

    public GT_Multiblock_Tooltip_Builder getTooltip() {
        return tooltip;
    }

    @Override
    public void assignTooltip(List<String> tooltip) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.addAll(Arrays.asList(this.tooltip.getStructureInformation()));
        } else {
            tooltip.addAll(Arrays.asList(this.tooltip.getInformation()));
        }
    }
}
