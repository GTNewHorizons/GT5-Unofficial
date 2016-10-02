package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GT_MetaGenerated_Item_X01;
import ic2.core.Ic2Items;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.Random;

/**
 * Created by Detrav on 02.10.2016.
 */
public class DetravEntityDropEvent {

    final static int baseShance = 40;
    static Random random = new Random();

    @SubscribeEvent
    public void onLivingDropsEvent(LivingDropsEvent event) {
        if(event.entity.isCreatureType(EnumCreatureType.monster, false)) {
            float shance = (baseShance + event.entityLiving.getMaxHealth());
            int count = 0;
            while (shance > 100) {
                count++;
                shance -= 100;
            }

            if(count>0)
            {
                count = (int)Math.pow(count,event.lootingLevel+1) + 1;

                while (count>Ic2Items.coin.getMaxStackSize()) {
                    ItemStack itemStackToDrop = Ic2Items.coin.copy();
                    itemStackToDrop.stackSize = itemStackToDrop.getMaxStackSize();
                    event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX,
                            event.entity.posY, event.entity.posZ, itemStackToDrop));
                    count -= itemStackToDrop.getMaxStackSize();
                }
                ItemStack itemStackToDrop = Ic2Items.coin.copy();
                itemStackToDrop.stackSize = count;
                event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX,
                        event.entity.posY, event.entity.posZ, itemStackToDrop));
            }

            count = 0;

            shance = shance*(event.lootingLevel +1);
            while (shance > 100) {
                count++;
                shance -= 100;
            }
            if(random.nextInt(100) < shance) count ++;
            ItemStack itemStackToDrop = ItemList.Credit_Copper.get(count);
            event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX,
                    event.entity.posY, event.entity.posZ, itemStackToDrop));
        }
        /*if(event.source.getEntity() instanceof EntityPlayer)
        {

            ((EntityPlayer)event.source.getEntity()).addChatComponentMessage(
                    new ChatComponentText(event.entity.toString())
            );
        }*/
    }


    static boolean inited = false;

    public static void register() {
        if (!inited) {
            inited = true;
            DetravEntityDropEvent handler = new DetravEntityDropEvent();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
