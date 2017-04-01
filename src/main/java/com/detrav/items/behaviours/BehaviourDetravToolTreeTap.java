package com.detrav.items.behaviours;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.common.items.behaviors.Behaviour_None;
import ic2.core.block.BlockRubWood;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Detrav on 01.04.2017.
 */
public class BehaviourDetravToolTreeTap extends Behaviour_None {

    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        Block b = aWorld.getBlock(aX,aY,aZ);
        if(b instanceof BlockRubWood)
        {
            int startPos = aY;
            for(int i = aY; i> aY-10 && i> 1 && aWorld.getBlock(aX,i,aZ) instanceof  BlockRubWood; i--)
                startPos = i;

            for(int i = startPos; i< aY + 10; i++)
            {
                if(!(aWorld.getBlock(aX,i,aZ) instanceof  BlockRubWood))
                    break;
                if(i == aY)
                {
                    aWorld.setBlockMetadataWithNotify(aX,i,aZ,aSide,2);
                }
                else
                {
                    aWorld.setBlockMetadataWithNotify(aX,i,aZ,1,2);
                }
            }

            ((DetravMetaGeneratedTool01)aItem).doDamage(aStack, 100);
            return  true;
        }
        return false;
    }
}