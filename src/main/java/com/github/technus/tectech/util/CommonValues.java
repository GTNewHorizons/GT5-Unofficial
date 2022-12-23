package com.github.technus.tectech.util;

import gregtech.api.enums.GT_Values;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 11.01.2017.
 */
@SuppressWarnings("SpellCheckingInspection")
public final class CommonValues {
    public static final String TEC_MARK_SHORT = EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech";
    public static final String TEC_MARK_GENERAL = TEC_MARK_SHORT + EnumChatFormatting.BLUE + ": Interdimensional";
    public static final String TEC_MARK_EM = TEC_MARK_SHORT + EnumChatFormatting.BLUE + ": Elemental Matter";
    public static final String THETA_MOVEMENT = TEC_MARK_SHORT + EnumChatFormatting.BLUE + ": Theta Movement";
    public static final String COSMIC_MARK = TEC_MARK_SHORT + EnumChatFormatting.BLUE + ": Cosmic";

    public static final byte DECAY_AT = 0; // hatches compute decays
    public static final byte MULTI_PURGE_1_AT = 2; // multiblocks clean their hatches 1
    public static final byte MOVE_AT = 4; // move stuff around
    public static final byte RECIPE_AT = 6; // move stuff around
    public static final byte MULTI_PURGE_2_AT = 8; // multiblocks clean their hatches 2
    public static final byte OVERFLOW_AT = 10; // then hatches clean themselves
    // - in case some hatches are not in multiblock structure
    public static final byte MULTI_CHECK_AT = 12; // multiblock checks its state
    public static final byte DISPERSE_AT = 14; // overflow hatches perform disperse
    public static final byte TRANSFER_AT = 16;

    public static final long[] AatV = new long[] {
        268435455, 67108863, 16777215, 4194303, 1048575, 262143, 65535, 16383, 4095, 1023, 255, 63, 15, 3, 1, 1
    };
    public static final String[] VN = GT_Values.VN;
    public static final long[] V = GT_Values.V;

    public static final String[] EOH_TIER_FANCY_NAMES = {
        "Crude",
        "Primitive",
        "Stable",
        "Advanced",
        "Superb",
        "Exotic",
        "Perfect",
        "Tipler",
        EnumChatFormatting.BOLD + "Gallifreyan"
    };

    private CommonValues() {}
}
