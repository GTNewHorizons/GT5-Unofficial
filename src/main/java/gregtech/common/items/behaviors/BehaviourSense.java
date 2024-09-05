package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import ic2.api.crops.ICropTile;

public class BehaviourSense extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.sense", "Rightclick to harvest Crop Sticks");

    public BehaviourSense(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof ICropTile)) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    for (int k = -2; k < 3; k++) {
                        if ((aStack.stackSize > 0)
                            && (((tTileEntity = aWorld.getTileEntity(aX + i, aY + j, aZ + k)) instanceof ICropTile))
                            && (((ICropTile) tTileEntity).harvest(true))
                            && (!aPlayer.capabilities.isCreativeMode)) {
                            ((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts / 20);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
