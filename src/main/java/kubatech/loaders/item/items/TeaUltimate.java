/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package kubatech.loaders.item.items;

import kubatech.api.utils.ModUtils;
import kubatech.api.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class TeaUltimate extends TeaCollection {
    public TeaUltimate() {
        super("ultimate_tea");
    }

    private static String name = "";
    private static long timeCounter = 0;
    private static int colorCounter = 0;

    @Override
    public String getDisplayName(ItemStack stack) {
        if (!ModUtils.isClientSided) return super.getDisplayName(stack);
        if (stack.stackTagCompound == null
                || (!stack.stackTagCompound.hasKey("TeaOwner")
                        || stack.stackTagCompound
                                .getString("TeaOwner")
                                .equals(Minecraft.getMinecraft().thePlayer.getCommandSenderName()))) {
            long current = System.currentTimeMillis();
            if (current - timeCounter > 100) {
                timeCounter = current;
                name = StringUtils.applyRainbow(
                        "ULTIMATE", colorCounter++, EnumChatFormatting.BOLD.toString() + EnumChatFormatting.OBFUSCATED);
            }
            return String.format(super.getDisplayName(stack), name + EnumChatFormatting.RESET);
        }
        return EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + "???????";
    }
}
