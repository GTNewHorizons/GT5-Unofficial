package miscutil.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum UtilsChatFormatting
{
    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    OBFUSCATED('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),
    RESET('r');
    /** Maps a formatting code (e.g., 'f') to its corresponding enum value (e.g., WHITE). */
    private static final Map<Character, UtilsChatFormatting> formattingCodeMapping = new HashMap<Character, UtilsChatFormatting>();
    /** Maps a name (e.g., 'underline') to its corresponding enum value (e.g., UNDERLINE). */
    private static final Map<String, UtilsChatFormatting> nameMapping = new HashMap<String, UtilsChatFormatting>();
    /**
     * Matches formatting codes that indicate that the client should treat the following text as bold, recolored,
     * obfuscated, etc.
     */
    private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
    /** The formatting code that produces this format. */
    private final char formattingCode;
    private final boolean fancyStyling;
    /**
     * The control string (section sign + formatting code) that can be inserted into client-side text to display
     * subsequent text in this format.
     */
    private final String controlString;

    private UtilsChatFormatting(char p_i1336_3_)
    {
        this(p_i1336_3_, false);
    }

    private UtilsChatFormatting(char p_i1337_3_, boolean p_i1337_4_)
    {
        this.formattingCode = p_i1337_3_;
        this.fancyStyling = p_i1337_4_;
        this.controlString = "\u00a7" + p_i1337_3_;
    }

    /**
     * Gets the formatting code that produces this format.
     */
    public char getFormattingCode()
    {
        return this.formattingCode;
    }

    /**
     * False if this is just changing the color or resetting; true otherwise.
     */
    public boolean isFancyStyling()
    {
        return this.fancyStyling;
    }

    /**
     * Checks if typo is a color.
     */
    public boolean isColor()
    {
        return !this.fancyStyling && this != RESET;
    }

    /**
     * Gets the friendly name of this value.
     */
    public String getFriendlyName()
    {
        return this.name().toLowerCase();
    }

    @Override
	public String toString()
    {
        return this.controlString;
    }

    /**
     * Returns a copy of the given string, with formatting codes stripped away.
     */
    @SideOnly(Side.CLIENT)
    public static String getTextWithoutFormattingCodes(String p_110646_0_)
    {
        return p_110646_0_ == null ? null : formattingCodePattern.matcher(p_110646_0_).replaceAll("");
    }

    /**
     * Gets a value by its friendly name; null if the given name does not map to a defined value.
     */
    public static UtilsChatFormatting getValueByName(String p_96300_0_)
    {
        return p_96300_0_ == null ? null : (UtilsChatFormatting)nameMapping.get(p_96300_0_.toLowerCase());
    }

    /**
     * Gets all the valid values. Args: @param par0: Whether or not to include color values. @param par1: Whether or not
     * to include fancy-styling values (anything that isn't a color value or the "reset" value).
     */
    public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_)
    {
        ArrayList<String> arraylist = new ArrayList<String>();
        UtilsChatFormatting[] aenumchatformatting = values();
        int i = aenumchatformatting.length;

        for (int j = 0; j < i; ++j)
        {
            UtilsChatFormatting enumchatformatting = aenumchatformatting[j];

            if ((!enumchatformatting.isColor() || p_96296_0_) && (!enumchatformatting.isFancyStyling() || p_96296_1_))
            {
                arraylist.add(enumchatformatting.getFriendlyName());
            }
        }

        return arraylist;
    }

    static
    {
        UtilsChatFormatting[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            UtilsChatFormatting var3 = var0[var2];
            formattingCodeMapping.put(Character.valueOf(var3.getFormattingCode()), var3);
            nameMapping.put(var3.getFriendlyName(), var3);
        }
    }
}