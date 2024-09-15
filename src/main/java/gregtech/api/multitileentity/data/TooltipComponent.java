package gregtech.api.multitileentity.data;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.mutecore.api.item.TooltipAssigner;

import gregtech.api.util.MultiblockTooltipBuilder;

public class TooltipComponent implements TooltipAssigner {

    private MultiblockTooltipBuilder tooltip;

    public TooltipComponent(MultiblockTooltipBuilder tooltip) {
        this.tooltip = tooltip;
    }

    public MultiblockTooltipBuilder getTooltip() {
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
