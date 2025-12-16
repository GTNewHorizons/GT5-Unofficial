package tectech.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Created by danie_000 on 11.01.2017.
 */
@SuppressWarnings("SpellCheckingInspection")
public final class CommonValues {

    public static final String TEC_MARK_SHORT = EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech";
    public static final String TEC_MARK_GENERAL = TEC_MARK_SHORT + EnumChatFormatting.BLUE
        + StatCollector.translateToLocal("tt.tooltip.mark_general");
    public static final String TEC_MARK_EM = TEC_MARK_SHORT + EnumChatFormatting.BLUE
        + StatCollector.translateToLocal("tt.tooltip.mark_em");
    public static final String THETA_MOVEMENT = TEC_MARK_SHORT + EnumChatFormatting.BLUE
        + StatCollector.translateToLocal("tt.tooltip.theta_movement");
    public static final String COSMIC_MARK = TEC_MARK_SHORT + EnumChatFormatting.BLUE
        + StatCollector.translateToLocal("tt.tooltip.cosmic_mark");
    public static final String GODFORGE_MARK = TEC_MARK_SHORT + EnumChatFormatting.BLUE
        + StatCollector.translateToLocal("tt.tooltip.godforge_mark");

    public static final byte MOVE_AT = 4; // move stuff around
    public static final byte RECIPE_AT = 6; // move stuff around
    // - in case some hatches are not in multiblock structure
    public static final byte MULTI_CHECK_AT = 12; // multiblock checks its state
    public static final byte TRANSFER_AT = 16;

    public static final String[] EOH_TIER_FANCY_NAMES = { "Crude", "Primitive", "Stable", "Advanced", "Superb",
        "Exotic", "Perfect", "Tipler", EnumChatFormatting.BOLD + "Gallifreyan" };

    public static String getLocalizedEohTierFancyNames(int tier) {
        if (tier < 0 || tier >= EOH_TIER_FANCY_NAMES.length) {
            return StatCollector.translateToLocal("tt.eoh.fancy_names.unknown");
        }
        String unlocalizedName = "tt.eoh.fancy_names."
            + EOH_TIER_FANCY_NAMES[tier].replace(EnumChatFormatting.BOLD.toString(), "")
                .toLowerCase();
        if (StatCollector.canTranslate(unlocalizedName)) {
            String localizedName = StatCollector.translateToLocal(unlocalizedName);
            if (EOH_TIER_FANCY_NAMES[tier].contains(EnumChatFormatting.BOLD.toString())) {
                return EnumChatFormatting.BOLD + localizedName;
            } else {
                return localizedName;
            }
        }
        return EOH_TIER_FANCY_NAMES[tier];
    }

    private CommonValues() {}
}
