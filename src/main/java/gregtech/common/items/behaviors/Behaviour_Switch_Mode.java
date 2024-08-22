package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import gregtech.api.enums.GT_Values;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.net.Packet_ToolSwitchMode;
import gregtech.api.util.GT_Utility;

public class Behaviour_Switch_Mode extends Behaviour_None {

    public Behaviour_Switch_Mode() {}

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aStack != null && (aPlayer == null || aPlayer.isSneaking()) && aWorld.isRemote) {

            MetaGeneratedTool itemTool = (MetaGeneratedTool) aItem;
            final byte maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode == 1) {
                return aStack;
            }

            MovingObjectPosition mop = GT_Utility.getPlayerLookingTarget(aPlayer);
            if (mop == null) {
                GT_Values.NW.sendToServer(new Packet_ToolSwitchMode());
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
