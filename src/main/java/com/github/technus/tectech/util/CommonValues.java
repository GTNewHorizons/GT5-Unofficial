package com.github.technus.tectech.util;

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
    public static final String GODFORGE_MARK = TEC_MARK_SHORT + EnumChatFormatting.BLUE + ": Project Godforge";

    public static final byte MOVE_AT = 4; // move stuff around
    public static final byte RECIPE_AT = 6; // move stuff around
    // - in case some hatches are not in multiblock structure
    public static final byte MULTI_CHECK_AT = 12; // multiblock checks its state
    public static final byte TRANSFER_AT = 16;

    public static final String[] EOH_TIER_FANCY_NAMES = { "Crude", "Primitive", "Stable", "Advanced", "Superb",
        "Exotic", "Perfect", "Tipler", EnumChatFormatting.BOLD + "Gallifreyan" };

    private CommonValues() {}
}
