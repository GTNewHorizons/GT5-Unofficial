/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.net.OreDictCachePacket;
import com.github.bartimaeusnek.bartworks.common.net.ServerJoinedPackage;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ServerEventHandler {

    // MinecraftForge.EVENT_BUS
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event == null || !(event.entity instanceof EntityPlayerMP) || !SideReference.Side.Server) return;
        MainMod.BW_Network_instance.sendToPlayer(
                new OreDictCachePacket(OreDictHandler.getNonBWCache()), (EntityPlayerMP) event.entity);
        MainMod.BW_Network_instance.sendToPlayer(new ServerJoinedPackage(null), (EntityPlayerMP) event.entity);
    }

    // FMLCommonHandler.instance().bus()
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerTickEventServer(TickEvent.PlayerTickEvent event) {
        if (event == null || !(event.player instanceof EntityPlayerMP)) return;

        if (event.player.worldObj.getTotalWorldTime() % 20 != 0) return;

        boolean replace = false;
        ItemStack toReplace = null;
        for (int i = 0; i < event.player.inventory.mainInventory.length; i++) {
            ItemStack stack = event.player.inventory.mainInventory[i];
            if (stack == null) continue;
            int[] oreIDs = OreDictionary.getOreIDs(stack);

            if (oreIDs.length > 0) {
                loop:
                for (int oreID : oreIDs) {
                    String oreDictName = OreDictionary.getOreName(oreID);
                    for (Werkstoff e : Werkstoff.werkstoffHashSet) {
                        replace = e.getGenerationFeatures().enforceUnification;
                        if (replace) {
                            if (oreDictName.contains(e.getVarName())) {
                                String prefix = oreDictName.replace(e.getVarName(), "");
                                OrePrefixes prefixes = OrePrefixes.getPrefix(prefix);
                                if (prefixes == null) {
                                    continue;
                                }
                                toReplace = GT_OreDictUnificator.get(prefixes, e.getVarName(), stack.stackSize);
                                break loop;
                            } else {
                                for (String s : e.getADDITIONAL_OREDICT()) {
                                    if (oreDictName.contains(s)) {
                                        String prefix = oreDictName.replace(s, "");
                                        OrePrefixes prefixes = OrePrefixes.getPrefix(prefix);
                                        if (prefixes == null) {
                                            continue;
                                        }
                                        toReplace = GT_OreDictUnificator.get(prefixes, e.getVarName(), stack.stackSize);
                                        break loop;
                                    }
                                }
                            }
                        }
                        replace = false;
                    }
                }
            }
            if (replace && toReplace != null) {
                event.player.inventory.setInventorySlotContents(i, toReplace);
                replace = false;
            }
        }
    }
}
