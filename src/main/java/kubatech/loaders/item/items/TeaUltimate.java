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

import java.util.Random;
import kubatech.api.utils.FastRandom;
import kubatech.api.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class TeaUltimate extends Tea {
    public TeaUltimate() {
        super("ultimate_tea");
    }

    private static final String[] Colors = new String[] {
        "\u00a70", "\u00a71", "\u00a72", "\u00a73", "\u00a74", "\u00a75", "\u00a76", "\u00a77", "\u00a78", "\u00a79",
        "\u00a7a", "\u00a7b", "\u00a7c", "\u00a7d", "\u00a7e", "\u00a7f",
    };
    private static final Random rnd = new FastRandom();
    private static String name = "";
    private static long timeCounter = 0;

    private static String rndColor() {
        return Colors[rnd.nextInt(Colors.length)] + EnumChatFormatting.BOLD + "" + EnumChatFormatting.OBFUSCATED;
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        if (!ModUtils.isClientSided) return super.getDisplayName(stack);
        if (stack.stackTagCompound == null
                || (!stack.stackTagCompound.hasKey("TeaOwner")
                        || stack.stackTagCompound
                                .getString("TeaOwner")
                                .equals(Minecraft.getMinecraft()
                                        .thePlayer
                                        .getUniqueID()
                                        .toString()))) {
            long current = System.currentTimeMillis();
            if (current - timeCounter > 200) {
                timeCounter = current;
                name = rndColor() + "U" + rndColor() + "L" + rndColor() + "T" + rndColor() + "I" + rndColor() + "M"
                        + rndColor() + "A" + rndColor() + "T" + rndColor() + "E";
            }
            return String.format(super.getDisplayName(stack), name + EnumChatFormatting.RESET);
        }
        return EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + "???????";
    }
}
