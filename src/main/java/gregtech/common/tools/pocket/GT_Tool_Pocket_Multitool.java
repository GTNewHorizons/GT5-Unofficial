package gregtech.common.tools.pocket;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.behaviors.Behaviour_Switch_Metadata;
import gregtech.common.tools.GT_Tool;

public class GT_Tool_Pocket_Multitool extends GT_Tool {

    public final int mSwitchIndex;

    public GT_Tool_Pocket_Multitool(int aSwitchIndex) {
        mSwitchIndex = aSwitchIndex;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.POCKET_MULTITOOL_CLOSED : Textures.ItemIcons.VOID;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        super.onStatsAddedToTool(aItem, aID);
        aItem.addItemBehavior(aID, new Behaviour_Switch_Metadata(mSwitchIndex, true, true));
    }

    @Override
    public float getBaseDamage() {
        return 0;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return false;
    }
}
