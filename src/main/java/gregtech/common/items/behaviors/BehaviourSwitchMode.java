package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import gregtech.api.enums.GTValues;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.net.GTPacketToolSwitchMode;
import gregtech.api.util.GTUtility;

public class BehaviourSwitchMode extends BehaviourNone {

    public BehaviourSwitchMode() {}

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aStack != null && (aPlayer == null || aPlayer.isSneaking()) && aWorld.isRemote) {

            MetaGeneratedTool itemTool = (MetaGeneratedTool) aItem;
            final byte maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode == 1) {
                return aStack;
            }

            MovingObjectPosition mop = GTUtility.getPlayerLookingTarget(aPlayer);
            if (mop == null) {
                GTValues.NW.sendToServer(new GTPacketToolSwitchMode());
            }
            return aStack;
        }
        return aStack;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        super.getAdditionalToolTips(aItem, aList, aStack);
        if ((aItem instanceof MetaGeneratedTool)) {
            MetaGeneratedTool itemTool = (MetaGeneratedTool) aItem;
            final int maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode > 1) {
                aList.add("Shift+Rclick to change mode");
            }
        }

        return aList;
    }

}
