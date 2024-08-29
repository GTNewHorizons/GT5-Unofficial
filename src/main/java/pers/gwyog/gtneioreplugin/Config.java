package pers.gwyog.gtneioreplugin;

@com.gtnewhorizon.gtnhlib.config.Config(
    modid = GTNEIOrePlugin.MODID,
    category = "general",
    filename = GTNEIOrePlugin.MODID)
public class Config {

    @com.gtnewhorizon.gtnhlib.config.Config.Comment("if true, generate both csv files.")
    @com.gtnewhorizon.gtnhlib.config.Config.DefaultBoolean(false)
    @com.gtnewhorizon.gtnhlib.config.Config.RequiresMcRestart
    public static boolean printCsv;

    @com.gtnewhorizon.gtnhlib.config.Config.Comment("the name of the file you want for the ore sheet, it'll appear at the root of your instance.")
    @com.gtnewhorizon.gtnhlib.config.Config.DefaultString("GTNH-Oresheet.csv")
    @com.gtnewhorizon.gtnhlib.config.Config.RequiresMcRestart
    public static String CSVName;

    @com.gtnewhorizon.gtnhlib.config.Config.Comment("the name of the file you want for the small ore sheet, it'll appear at the root of your instance.")
    @com.gtnewhorizon.gtnhlib.config.Config.DefaultString("GTNH-Small-Ores-Sheet.csv")
    @com.gtnewhorizon.gtnhlib.config.Config.RequiresMcRestart
    public static String CSVnameSmall;

    @com.gtnewhorizon.gtnhlib.config.Config.Comment("Maximum number of lines the dimension names tooltip can have before it wraps around.")
    @com.gtnewhorizon.gtnhlib.config.Config.DefaultInt(11)
    @com.gtnewhorizon.gtnhlib.config.Config.RequiresMcRestart
    public static int maxTooltipLines;
}
