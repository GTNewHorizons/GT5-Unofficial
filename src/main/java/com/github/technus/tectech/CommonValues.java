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
    public static final String COSMIC_MARK =
            EnumChatFormatting.BLUE + "Tec" +
                    EnumChatFormatting.DARK_BLUE + "Tech" +
                    EnumChatFormatting.BLUE + ": Cosmic";//TODO get a better name than cosmic for *UNDEFINED* thing

    public static final byte DECAY_AT = 0;// hatches compute decays
    public static final byte MULTI_PURGE_1_AT = 2;// multiblocks clean their hatches 1
    public static final byte MOVE_AT = 4;// move stuff around
    public static final byte RECIPE_AT = 6;// move stuff around
    public static final byte MULTI_PURGE_2_AT = 8;// multiblocks clean their hatches 2
    public static final byte OVERFLOW_AT = 10;// then hatches clean themselves
    // - in case some hatches are not in multiblock structure
    public static final byte MULTI_CHECK_AT = 12;// multiblock checks it's state
    public static final byte DISPERSE_AT = 14;// overflow hatches perform disperse
    public static final byte TRANSFER_AT = 16;

    public static final long[] AatV = new long[]{268435455,67108863,16777215,4194303,1048575,262143,65535,16383,4095,1023,255,63,15,3,1,1};
    public static final String[] VOLTAGE_NAMES = new String[]{"Ultra Low Voltage", "Low Voltage", "Medium Voltage", "High Voltage", "Extreme Voltage", "Insane Voltage", "Ludicrous Voltage", "ZPM Voltage", "Ultimate Voltage", "Ultimate High Voltage", "Ultimate Extreme Voltage", "Ultimate Insane Voltage", "Ultimate Mega Voltage", "Ultimate Extended Mega Voltage", "Overpowered Voltage", "Maximum Voltage"};
    public static final String[] VN = new String[]{"ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV", "UIV", "UMV", "UXV", "OpV", "MAX"};
    public static final long[] V = new long[]{8L, 32L, 128L, 512L, 2048L, 8192L, 32768L, 131072L, 524288L, 2097152L, 8388608L, 33554432L, 134217728L, 536870912L, 1073741824L, Integer.MAX_VALUE - 7};

    private CommonValues() {}
}
