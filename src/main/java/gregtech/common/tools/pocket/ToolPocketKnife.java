package gregtech.common.tools.pocket;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.MaterialUtils;
import gregtech.common.items.behaviors.BehaviourSwitchMetadata;
import gregtech.common.tools.ToolKnife;

public class ToolPocketKnife extends ToolKnife {

    public final int mSwitchIndex;

    public ToolPocketKnife(int aSwitchIndex) {
        mSwitchIndex = aSwitchIndex;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? GTMaterialIcons.item("pocketMultiToolKnife", MetaGeneratedTool.getPrimaryMaterialML(aStack))
            : Textures.GlobalIcons.VOID;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return MaterialUtils.rgba(MetaGeneratedTool.getPrimaryMaterialML(aStack));
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        super.onStatsAddedToTool(aItem, aID);
        aItem.addItemBehavior(aID, new BehaviourSwitchMetadata(mSwitchIndex, true, true));
    }
}
