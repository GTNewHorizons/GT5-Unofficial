package gregtech.common.tools.pocket;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.behaviors.Behaviour_Switch_Metadata;
import gregtech.common.tools.GT_Tool_File;

public class GT_Tool_Pocket_File extends GT_Tool_File {

    public final int mSwitchIndex;

    public GT_Tool_Pocket_File(int aSwitchIndex) {
        mSwitchIndex = aSwitchIndex;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.POCKET_MULTITOOL_FILE : Textures.ItemIcons.VOID;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        super.onStatsAddedToTool(aItem, aID);
        aItem.addItemBehavior(aID, new Behaviour_Switch_Metadata(mSwitchIndex, true, true));
    }
}
