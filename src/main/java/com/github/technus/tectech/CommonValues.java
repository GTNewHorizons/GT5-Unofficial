package com.github.technus.tectech;

import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 11.01.2017.
 */
public final class CommonValues {
    public final static String TEC_MARK_GENERAL =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Interdimensional";
    public final static String TEC_MARK_EM =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Elemental Matter";
    public final static String BASS_MARK =
            EnumChatFormatting.BLUE + "Bass" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Theta Movement";

    public final static byte DECAY_AT = 0;// hatches compute decays
    public final static byte MULTI_PURGE_1_AT = 2;// multiblocks clean their hatches 1
    public final static byte MOVE_AT = 4;// move stuff around
    public final static byte RECIPE_AT = 6;// move stuff around
    public final static byte MULTI_PURGE_2_AT = 8;// multiblocks clean their hatches 2
    public final static byte OVERFLOW_AT = 10;// then hatches clean themselves
    // - in case some hatches are not in multiblock structure
    public final static byte MULTI_CHECK_AT = 12;// multiblock checks it's state
    public final static byte DISPERSE_AT = 14;// overflow hatches perform disperse
}
