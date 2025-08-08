package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import gregtech.GTMod;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;

public class BehaviourSwitchMode extends BehaviourNone {

    public BehaviourSwitchMode() {}

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        super.getAdditionalToolTips(aItem, aList, aStack);
        if ((aItem instanceof MetaGeneratedTool itemTool)) {
            final int maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode > 1) {
                int keyCode = GTMod.proxy.TOOL_MODE_SWITCH_KEYBIND.getKeyCode();
                String keyName;
                if (keyCode < 0) {
                    keyName = Mouse.getButtonName(keyCode + 101);
                } else {
                    keyName = Keyboard.getKeyName(keyCode);
                }
                aList.add(
                    EnumChatFormatting.DARK_GRAY
                        + StatCollector.translateToLocalFormatted("gt.behaviour.switch_mode.tooltip", keyName));
            }
        }

        return aList;
    }

}
