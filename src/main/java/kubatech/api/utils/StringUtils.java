/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
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
 * spotless:on
 */

package kubatech.api.utils;

import net.minecraft.util.EnumChatFormatting;

public class StringUtils {

    private static final String[] rainbow = new String[] { EnumChatFormatting.DARK_RED.toString(),
        EnumChatFormatting.RED.toString(), EnumChatFormatting.GOLD.toString(), EnumChatFormatting.YELLOW.toString(),
        EnumChatFormatting.DARK_GREEN.toString(), EnumChatFormatting.GREEN.toString(),
        EnumChatFormatting.AQUA.toString(), EnumChatFormatting.DARK_AQUA.toString(),
        EnumChatFormatting.DARK_BLUE.toString(), EnumChatFormatting.BLUE.toString(),
        EnumChatFormatting.LIGHT_PURPLE.toString(), EnumChatFormatting.DARK_PURPLE.toString(),
        EnumChatFormatting.WHITE.toString(), EnumChatFormatting.GRAY.toString(),
        EnumChatFormatting.DARK_GRAY.toString(), EnumChatFormatting.BLACK.toString(), };

    public static String applyRainbow(String str, int offset, String additional) {
        StringBuilder final_string = new StringBuilder();
        int i = offset;
        for (char c : str.toCharArray()) final_string.append(rainbow[i++ % rainbow.length])
            .append(additional)
            .append(c);
        return final_string.toString();
    }

    public static String applyRainbow(String str, int offset) {
        return applyRainbow(str, offset, "");
    }

    public static String applyRainbow(String str) {
        return applyRainbow(str, 0, "");
    }
}
