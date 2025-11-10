package gregtech.api.enums;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.*;
import static gregtech.api.util.CustomGlyphs.*;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.objects.XSTR;
import gregtech.api.util.CustomGlyphs;
import gregtech.api.util.StringUtils;

/**
 * Pretty formatting for author names.
 */
public class GTAuthors {

    private static final String[] formattingCodes;
    static {
        // a list specifically for random selection of formatting codes.
        formattingCodes = new String[] { DARK_GREEN, DARK_AQUA, DARK_PURPLE, GOLD, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE,
            YELLOW, WHITE, OBFUSCATED, UNDERLINE };
    }

    public static final String AuthorColen = "" + EnumChatFormatting.DARK_RED
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "C"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "o"
        + EnumChatFormatting.GREEN
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "l"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "e"
        + EnumChatFormatting.DARK_PURPLE
        + EnumChatFormatting.BOLD
        + EnumChatFormatting.ITALIC
        + EnumChatFormatting.UNDERLINE
        + "n";
    public static final String AuthorKuba = "" + EnumChatFormatting.DARK_RED
        + EnumChatFormatting.BOLD
        + "k"
        + EnumChatFormatting.RED
        + EnumChatFormatting.BOLD
        + "u"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + "b"
        + EnumChatFormatting.YELLOW
        + EnumChatFormatting.BOLD
        + "a"
        + EnumChatFormatting.DARK_GREEN
        + EnumChatFormatting.BOLD
        + "6"
        + EnumChatFormatting.GREEN
        + EnumChatFormatting.BOLD
        + "0"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "0"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + "0";
    public static final String AuthorPxx500 = "" + EnumChatFormatting.DARK_BLUE + EnumChatFormatting.BOLD + "Pxx500";
    public static final String AuthorBlueWeabo = "" + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + "Blue"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "Weabo";
    public static final String Authorguid118 = "" + EnumChatFormatting.WHITE
        + EnumChatFormatting.BOLD
        + "gu"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "id"
        + EnumChatFormatting.DARK_AQUA
        + EnumChatFormatting.BOLD
        + "118";
    public static final String Authorminecraft7771 = "" + EnumChatFormatting.BLUE
        + EnumChatFormatting.LIGHT_PURPLE
        + "minecraft7771";
    public static final Supplier<String> AuthorCloud = chain(
        text("" + EnumChatFormatting.AQUA + EnumChatFormatting.BOLD),
        animatedText(
            "C",
            1,
            500,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + "\u238B" + RESET + AQUA + BOLD,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + "\u0B83" + RESET + AQUA + BOLD,
            DARK_AQUA + OBFUSCATED + BOLD + "X" + RESET + AQUA + BOLD,
            DARK_AQUA + BOLD + "\u29BC" + RESET + AQUA + BOLD),
        text(EnumChatFormatting.AQUA + EnumChatFormatting.BOLD.toString() + "loud" + EnumChatFormatting.RESET),
        animatedText(
            " ",
            1,
            500,
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + "\u238B",
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + "\u0B83",
            DARK_AQUA + OBFUSCATED + BOLD + "X",
            DARK_AQUA + BOLD + "\u29BC"));
    public static final Supplier<String> AuthorCloudSupplier = () -> StatCollector
        .translateToLocalFormatted("gt.author", AuthorCloud.get());
    public static final String AuthorQuerns = EnumChatFormatting.RED + "Querns";
    public static final String AuthorSilverMoon = EnumChatFormatting.AQUA + "SilverMoon";
    public static final String AuthorTheEpicGamer274 = EnumChatFormatting.DARK_AQUA + "TheEpicGamer274";
    public static final String Ollie = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Ollie";
    public static final String authorBaps = "" + EnumChatFormatting.GOLD
        + "Ba"
        + EnumChatFormatting.LIGHT_PURPLE
        + "ps";
    public static final String AuthorOmdaCZ = "" + EnumChatFormatting.BLUE + "Omda" + EnumChatFormatting.RED + "CZ";
    public static final String AuthorEvgenWarGold = "" + EnumChatFormatting.RED
        + EnumChatFormatting.BOLD
        + "Evgen"
        + EnumChatFormatting.BLUE
        + EnumChatFormatting.BOLD
        + "War"
        + EnumChatFormatting.GOLD
        + EnumChatFormatting.BOLD
        + "Gold";
    public static final String AuthorVolence = EnumChatFormatting.AQUA + "Volence";
    public static final String AuthorEigenRaven = EnumChatFormatting.DARK_PURPLE + "Eigen"
        + EnumChatFormatting.BOLD
        + "Raven";
    public static final String AuthorNotAPenguin = "" + EnumChatFormatting.WHITE
        + EnumChatFormatting.BOLD
        + "Not"
        + EnumChatFormatting.AQUA
        + EnumChatFormatting.BOLD
        + "APenguin";
    public static final String AuthorPineapple = EnumChatFormatting.BLUE + "Recursive Pineapple";
    public static final Supplier<String> AuthorNoc = chain(
        animatedText(
            "Noc",
            0,
            500,
            EnumChatFormatting.GOLD + BOLD,
            EnumChatFormatting.DARK_GREEN + BOLD,
            EnumChatFormatting.GOLD + BOLD,
            EnumChatFormatting.DARK_GREEN + BOLD,
            EnumChatFormatting.DARK_GREEN + OBFUSCATED + BOLD));
    public static final String AuthorJulia =
        // spotless:off
        EnumChatFormatting.BOLD.toString() +
            EnumChatFormatting.GOLD            + CustomGlyphs.SPARKLES +
            EnumChatFormatting.AQUA            + "J"                   +
            EnumChatFormatting.LIGHT_PURPLE    + "u"                   +
            EnumChatFormatting.WHITE           + "l"                   +
            EnumChatFormatting.LIGHT_PURPLE    + "i"                   +
            EnumChatFormatting.AQUA            + "a"                   +
            EnumChatFormatting.GOLD            + CustomGlyphs.SPARKLES ;
    // spotless:on
    public static final String AuthorPureBluez = EnumChatFormatting.WHITE + "Pure"
        + EnumChatFormatting.AQUA
        + "B"
        + EnumChatFormatting.DARK_AQUA
        + "l"
        + EnumChatFormatting.BLUE
        + "u"
        + EnumChatFormatting.DARK_BLUE
        + "ez";
    public static final Supplier<String> fancyAuthorChrom = chain(
        createChromLetter("C", ORDER),
        createChromLetter("h", EARTH),
        createChromLetter("r", CHAOS),
        createChromLetter("o", AIR),
        createChromLetter("m", STAR));
    public static final String AuthorChrom = "" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD + "Chrom";
    public static final Supplier<String> AuthorThree = chain(
        animatedText(
            "Three",
            0,
            1000,
            EnumChatFormatting.BLUE + BOLD,
            EnumChatFormatting.RED + BOLD,
            EnumChatFormatting.YELLOW + BOLD));
    public static final String AuthorQuetz4l = EnumChatFormatting.AQUA + "Quetz4l";

    private static Supplier<String> createChromLetter(String letter, String... injectedUnicode) {

        XSTR random = XSTR.XSTR_INSTANCE;
        // calculates the amount of cycles
        int length = 4 + injectedUnicode.length * 2;
        String[] colorList = new String[length];

        int currentUnicodeIndex = 0;
        for (int i = 0; i < colorList.length; i++) {
            StringBuilder builder = new StringBuilder();
            int prependedFormattingCodes = 1 + random.nextInt(2);
            for (int codeStep = 0; codeStep < prependedFormattingCodes; codeStep++) {
                // adds fun formatting codes
                int randIndex = random.nextInt(formattingCodes.length);
                builder.append(formattingCodes[randIndex]);
            }
            // checks if its the correct positon to insert a special unicode character, injects if so, otherwise adds
            // the letter
            if (currentUnicodeIndex < injectedUnicode.length && ((i + 1) % injectedUnicode.length == 0)) {
                builder.append(injectedUnicode[currentUnicodeIndex]);
                currentUnicodeIndex += 1;
            } else {
                builder.append(letter);
            }
            colorList[i] = builder.toString();
        }
        return chromAnimatedText(" ", 1, 1000, colorList);
    }

    // special version of the animated text that strips the return value of spaces, don't bother using this elsewhere
    private static Supplier<String> chromAnimatedText(String text, int posstep, int delay, String... formattingArray) {
        if (text == null || formattingArray == null || formattingArray.length == 0) return () -> "";

        final int finalDelay = Math.max(delay, 1);
        final int finalPosstep = Math.max(posstep, 0);

        return () -> {
            StringBuilder sb = new StringBuilder(text.length() * 3);
            int offset = (int) ((System.currentTimeMillis() / finalDelay) % formattingArray.length);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int indexColorArray = (i * finalPosstep + formattingArray.length - offset) % formattingArray.length;
                sb.append(formattingArray[indexColorArray]);
                sb.append(c);
            }
            return sb.toString()
                .replaceAll("\\s", "");
        };
    }

    public static String formatAuthors(String... authors) {
        return StringUtils.formatList(
            Arrays.stream(authors)
                .map(author -> EnumChatFormatting.GREEN + author + EnumChatFormatting.RESET + EnumChatFormatting.GRAY)
                .toArray(String[]::new));
    }

    public static String buildAuthorsWithFormat(String... authors) {
        if (authors == null || authors.length == 0) return "";
        return StatCollector
            .translateToLocalFormatted("gt.author" + (authors.length == 1 ? "" : "s"), formatAuthors(authors));
    }
}
