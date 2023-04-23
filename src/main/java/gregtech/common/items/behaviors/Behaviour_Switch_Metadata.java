package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.items.GT_Generic_Block;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

public class Behaviour_Switch_Metadata extends Behaviour_None {

    public final int mSwitchIndex;
    public final boolean mCheckTarget, mShowModeSwitchTooltip;

    public Behaviour_Switch_Metadata(int aSwitchIndex) {
        this(aSwitchIndex, false);
    }

    public Behaviour_Switch_Metadata(int aSwitchIndex, boolean aCheckTarget) {
        this(aSwitchIndex, aCheckTarget, false);
    }

    public Behaviour_Switch_Metadata(int aSwitchIndex, boolean aCheckTarget, boolean aShowModeSwitchTooltip) {
        mSwitchIndex = aSwitchIndex;
        mCheckTarget = aCheckTarget;
        mShowModeSwitchTooltip = aShowModeSwitchTooltip;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        if (mShowModeSwitchTooltip) aList.add(GT_Utility.trans("330", "Sneak Rightclick to switch Mode"));
        return aList;
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float aHitX, float aHitY, float aHitZ) {
        if (aStack != null && (aPlayer == null || aPlayer.isSneaking()) && !aWorld.isRemote) {
            if (mCheckTarget) {
                Block aBlock = aWorld.blockExists(aX, aY, aZ) ? aWorld.getBlock(aX, aY, aZ) : Blocks.air;
                if (aBlock instanceof GT_Generic_Block) {
                    Items.feather.setDamage(aStack, (short) mSwitchIndex);
                    GT_Utility.updateItemStack(aStack);
                    return true;
                }
                if (GT_Util.getTileEntity(aWorld, aX, aY, aZ, true) == null) {
                    Items.feather.setDamage(aStack, (short) mSwitchIndex);
                    GT_Utility.updateItemStack(aStack);
                    return true;
                }
                return false;
            }
            Items.feather.setDamage(aStack, (short) mSwitchIndex);
            GT_Utility.updateItemStack(aStack);
            return true;
        }
        return false;
    }
}
