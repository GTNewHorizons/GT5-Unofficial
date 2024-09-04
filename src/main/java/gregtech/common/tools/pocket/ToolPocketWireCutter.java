package gregtech.common.tools.pocket;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.behaviors.BehaviourSwitchMetadata;
import gregtech.common.tools.ToolWireCutter;

public class ToolPocketWireCutter extends ToolWireCutter {

    public final int mSwitchIndex;

    public ToolPocketWireCutter(int aSwitchIndex) {
        mSwitchIndex = aSwitchIndex;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.POCKET_MULTITOOL_WIRECUTTER : Textures.ItemIcons.VOID;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        super.onStatsAddedToTool(aItem, aID);
        aItem.addItemBehavior(aID, new BehaviourSwitchMetadata(mSwitchIndex, true, true));
    }
}
