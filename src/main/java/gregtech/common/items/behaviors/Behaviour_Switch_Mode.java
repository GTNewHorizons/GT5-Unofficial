package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Util;

public class Behaviour_Switch_Mode extends Behaviour_None {

    public Behaviour_Switch_Mode() {}

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float aHitX, float aHitY, float aHitZ) {
        if (aStack != null && (aPlayer == null || aPlayer.isSneaking()) && !aWorld.isRemote) {

            if (GT_Util.getTileEntity(aWorld, aX, aY, aZ, true) != null) {
                return false;
            }

            if (!(aItem instanceof GT_MetaGenerated_Tool)) {
                return false;
            }

            GT_MetaGenerated_Tool itemTool = (GT_MetaGenerated_Tool) aItem;
            final byte maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode == 1) {
                return false;
            }

            byte currentMode = itemTool.getToolMode(aStack);
            currentMode = (currentMode + 1) % maxMode;
            itemTool.setToolMode(aStack, currentMode);
            System.out.println(currentMode);
            return true;
        }
        return false;
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
