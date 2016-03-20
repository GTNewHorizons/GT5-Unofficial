package com.detrav.items.behaviours;

import gregtech.api.items.GT_MetaBase_Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolElectricProPick extends BehaviourDetravToolProPick
{

    public BehaviourDetravToolElectricProPick(int aCosts) {
        super(aCosts);
    }

    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {

        if (!aWorld.isRemote) {
            //aPlayer.openGui();

            aPlayer.addChatMessage(new ChatComponentText("Scanning Begin"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.onItemRightClick(aItem,aStack,aWorld,aPlayer);
    }
}