package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Utility;

public class Behaviour_Switch_Mode extends Behaviour_None {

    public Behaviour_Switch_Mode() {}

    @Override
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aStack != null && (aPlayer == null || aPlayer.isSneaking()) && !aWorld.isRemote) {

            GT_MetaGenerated_Tool itemTool = (GT_MetaGenerated_Tool) aItem;
            final byte maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode == 1) {
                return aStack;
            }

            MovingObjectPosition mop = GT_Utility.getPlayerLookingTarget();
            if (mop == null) {
                byte currentMode = itemTool.getToolMode(aStack);
                currentMode = (byte) ((currentMode + 1) % maxMode);
                itemTool.setToolMode(aStack, currentMode);
            }
            return aStack;
        }
        return aStack;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        super.getAdditionalToolTips(aItem, aList, aStack);
        if ((aItem instanceof GT_MetaGenerated_Tool)) {
            GT_MetaGenerated_Tool itemTool = (GT_MetaGenerated_Tool) aItem;
            final int maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode > 1) {
                aList.add("Shift+Rclick to change mode");
            }
        }

        return aList;
    }

}
