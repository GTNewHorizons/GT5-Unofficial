package com.github.technus.tectech;

import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 11.01.2017.
 */
public final class CommonValues {
    public static final String TEC_MARK_GENERAL =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Interdimensional";
    public static final String TEC_MARK_EM =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Elemental Matter";
    public static final String BASS_MARK =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Theta Movement";

    public static final byte DECAY_AT = 0;// hatches compute decays
    public static final byte MULTI_PURGE_1_AT = 2;// multiblocks clean their hatches 1
    public static final byte MOVE_AT = 4;// move stuff around
    public static final byte RECIPE_AT = 6;// move stuff around
    public static final byte MULTI_PURGE_2_AT = 8;// multiblocks clean their hatches 2
    public static final byte OVERFLOW_AT = 10;// then hatches clean themselves
    // - in case some hatches are not in multiblock structure
    public static final byte MULTI_CHECK_AT = 12;// multiblock checks it's state
    public static final byte DISPERSE_AT = 14;// overflow hatches perform disperse

    private CommonValues() {}
}
