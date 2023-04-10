/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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

package kubatech.api;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import kubatech.api.utils.StringUtils;

import net.minecraft.util.EnumChatFormatting;

public class Variables {

    public static final String Author = "Author: "
        + StringUtils.applyRainbow("kuba6000", 0, EnumChatFormatting.BOLD.toString());

    public static String buildAuthorList(String... authors) {
        if (authors.length == 0) return "Author: Unknown";
        StringBuilder b = new StringBuilder("Author: ")
            .append(StringUtils.applyRainbow(authors[0], 0, EnumChatFormatting.BOLD.toString()));
        for (int i = 1; i < authors.length; i++) {
            String author = authors[i];
            b.append(EnumChatFormatting.RESET)
                .append(" & ")
                .append(EnumChatFormatting.GOLD)
                .append(author);
        }
        return b.toString();
    }

    public static final String StructureHologram = "To see the structure, use a " + EnumChatFormatting.BLUE
        + "Structure"
        + EnumChatFormatting.DARK_BLUE
        + "Lib"
        + EnumChatFormatting.RESET
        + ""
        + EnumChatFormatting.GRAY
        + " Hologram Projector on the Controller!";

    public static final double ln4 = Math.log(4d);
    public static final double ln2 = Math.log(2d);

    public static final NumberFormat numberFormatScientific = new DecimalFormat("0.00E0");
    public static final NumberFormat numberFormat = NumberFormat.getInstance();
}
