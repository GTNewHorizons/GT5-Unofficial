/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.server.EventHandler;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.net.OreDictCachePacket;
import com.github.bartimaeusnek.bartworks.common.net.ServerJoinedPackage;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ServerEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event == null || !(event.entity instanceof EntityPlayerMP) || !FMLCommonHandler.instance().getSide().isServer())
            return;
        MainMod.BW_Network_instance.sendToPlayer(new OreDictCachePacket(OreDictHandler.getNonBWCache()), (EntityPlayerMP) event.entity);
        MainMod.BW_Network_instance.sendToPlayer(new ServerJoinedPackage(null),(EntityPlayerMP) event.entity);
    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public void onPlayerTickEventServer(TickEvent.PlayerTickEvent event) {
//        if (!BWUnificationEnforcer.isEnabled() || event == null || !(event.player instanceof EntityPlayerMP) || !FMLCommonHandler.instance().getSide().isServer())
//            return;
//
//        for (int i = 0; i < event.player.inventory.mainInventory.length; i++) {
//           ItemStack stack = event.player.inventory.mainInventory[i];
//           for (int id : OreDictionary.getOreIDs(stack))
//               if (BWUnificationEnforcer.getUnificationTargets().contains(OreDictionary.getOreName(id))){
//                   ArrayList<ItemStack> stacks = OreDictionary.getOres(OreDictionary.getOreName(id));
//                   for (int j = 0; j < stacks.size(); j++) {
//                       GameRegistry.UniqueIdentifier UI = GameRegistry.findUniqueIdentifierFor(stacks.get(j).getItem());
//                       if (UI.modId.equals(MainMod.MOD_ID)){
//                           event.player.inventory.mainInventory[i] = stacks.get(j).copy().splitStack(stack.stackSize);
//                       }
//                   }
//           }
//       }
//    }
}
