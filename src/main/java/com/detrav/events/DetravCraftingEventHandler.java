package com.detrav.events;

import com.detrav.utils.DetravRepairRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import gregtech.api.util.GT_Utility;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Detrav on 02.10.2016.
 */
public class DetravCraftingEventHandler {
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent e) {
        if(DetravRepairRecipe.INSTANCE == null) return;
        if(!(e.craftMatrix instanceof InventoryCrafting)) return;
        if(DetravRepairRecipe.INSTANCE.matches((InventoryCrafting) e.craftMatrix,null))
        {
            if(!GT_Utility.areStacksEqual(e.crafting, e.craftMatrix.getStackInSlot(4),true)) return;
            for(int i =0; i<e.craftMatrix.getSizeInventory(); i++) {
                ItemStack stack = e.craftMatrix.getStackInSlot(i);
                if(stack == null) continue;
                if (stack.stackSize < 2)
                    e.craftMatrix.setInventorySlotContents(i, null);
                else {
                    //stack.stackSize -= 1;
                    //e.craftMatrix.setInventorySlotContents(i,stack);
                }
            }
        }
    }

    static boolean inited = false;

    public static void register() {
        if (!inited) {
            inited = true;
            DetravCraftingEventHandler handler = new DetravCraftingEventHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
