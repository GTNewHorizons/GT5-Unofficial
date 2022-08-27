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

package com.github.bartimaeusnek.bartworks.util;

import static net.minecraft.util.EnumChatFormatting.*;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.util.StatCollector;

public class BW_Tooltip_Reference {
    public static final String BW_NO_RESET = ChatColorHelper.DARKGREEN + "BartWorks";
    public static final String TT_NO_RESET = BLUE + "Tec" + DARK_BLUE + "Tech";
    public static final String BW = BW_NO_RESET + GRAY;
    public static final String TT = TT_NO_RESET + GRAY;

    public static final Supplier<String> ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS =
            () -> StatCollector.translateToLocal("tooltip.bw.1.name") + " " + BW;
    public static final Supplier<String> ADDED_BY_BARTWORKS =
            () -> StatCollector.translateToLocal("tooltip.bw.0.name") + " " + BW;
    public static final Function<String, String> ADDED_VIA_BARTWORKS =
            owner -> String.format(StatCollector.translateToLocal("tooltip.bw.via.name"), owner);
    public static final String MULTIBLOCK_ADDED_BY_BARTWORKS = BW;
    public static final Function<String, String> MULTIBLOCK_ADDED_VIA_BARTWORKS =
            owner -> String.format(StatCollector.translateToLocal("tooltip.bw.mb_via.name"), owner);
    public static final String MULTIBLOCK_ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS =
            MULTIBLOCK_ADDED_VIA_BARTWORKS.apply(GREEN + "bartimaeusnek");

    public static final String ADV_STR_CHECK = "Uses an advanced " + TT + " structure check, due to " + BW;
    public static final String TT_BLUEPRINT = "To see the structure, use a " + TT + " Blueprint on the Controller!";

    public static String[] getTranslatedBrandedTooltip(String key) {
        String[] dsc = StatCollector.translateToLocal(key).split(";");
        String[] fdsc = new String[dsc.length + 1];
        System.arraycopy(dsc, 0, fdsc, 0, dsc.length);
        fdsc[dsc.length] = ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get();
        return fdsc;
    }
}
