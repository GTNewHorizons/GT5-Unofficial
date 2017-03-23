package com.github.technus.tectech.elementalMatter;

import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 11.01.2017.
 */
public final class CommonValues {
    public final static String tecMark =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Elemental Matter";
    public final static byte decayAt = 0;// hatches compute dacays
    public final static byte multiPurge1At = 2;// multiblocks clean their hatches 1
    public final static byte moveAt = 4;// move stuff around
    public final static byte recipeAt = 6;// move stuff around
    public final static byte multiPurge2At = 8;// multiblocks clean their hatches 2
    public final static byte overflowAt = 10;// then hatches clean themselves
    // - in case some hatches are not in multiblock structure
    public final static byte multiCheckAt = 12;// multiblock checks it's state
    public final static byte disperseAt = 14;// overflow hatches perform disperse
}
