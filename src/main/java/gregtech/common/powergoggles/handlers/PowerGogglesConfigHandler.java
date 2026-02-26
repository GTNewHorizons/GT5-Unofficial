package gregtech.common.powergoggles.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizons.modularui.api.math.Color;

public class PowerGogglesConfigHandler {

    public static Configuration config;
    public static double hudScale;
    public static int mainOffsetX;
    public static int mainOffsetY;
    public static int rectangleWidth;
    public static int rectangleHeight;
    public static double mainTextScaling;
    public static double subTextScaling;
    public static String gradientPreset;
    public static int formatIndex;
    public static int readingIndex;
    public static int gradientIndex;
    public static boolean showPowerChart;
    public static boolean showPowerBar;
    public static boolean hideWhenChatOpen;

    public static int gradientBadColor;
    public static int gradientOkColor;
    public static int gradientGoodColor;

    public static int chartBackgroundColor;
    public static int chartBorderColor;
    public static int masurementsBackgroundColor;

    public static int textBadColor;
    public static int textOkColor;
    public static int textGoodColor;
    public static final Logger GOGGLES_LOGGER = LogManager.getLogger("GT5U Power Goggles");

    public static void init(File confFile) {
        config = new Configuration(confFile);
        config.load();
        syncConfig();
    }

    public static void syncConfig() {

        try {
            mainOffsetX = config.get(Configuration.CATEGORY_GENERAL, "Render Offset X", 10, "")
                .getInt(10);
            mainOffsetY = config.get(Configuration.CATEGORY_GENERAL, "Render Offset Y", 37, "")
                .getInt(40);
            rectangleWidth = config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Width", 120, "")
                .getInt(120);
            rectangleHeight = config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Height", 4, "")
                .getInt(4);
            mainTextScaling = config
                .get(Configuration.CATEGORY_GENERAL, "Storage Text Scale", 1, "Text size of the storage EU reading")
                .getDouble(1);
            subTextScaling = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Timed Reading Text Scale",
                    0.75,
                    "Text size of the 5m and 1h readings")
                .getDouble(0.75);
            gradientPreset = config
                .get(Configuration.CATEGORY_GENERAL, "Gradient Preset", "NORMAL", "Available options: NORMAL")
                .getString();
            formatIndex = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Format Index",
                    0,
                    "Available options: SI, SCIENTIFIC, ENGINEERING")
                .getInt(0);
            readingIndex = config
                .get(Configuration.CATEGORY_GENERAL, "Reading Index", 0, "Available options: TOTAL, EUT, BOTH")
                .getInt(0);
            gradientIndex = config
                .get(Configuration.CATEGORY_GENERAL, "Gradient Index", 0, "Available options: NORMAL, DEUTERANOPIA")
                .getInt(0);
            showPowerChart = config.get(Configuration.CATEGORY_GENERAL, "Show Power Chart", false, "")
                .getBoolean(false);
            showPowerBar = config.get(Configuration.CATEGORY_GENERAL, "Show Power Bar", true, "")
                .getBoolean(true);
            hudScale = config.get(Configuration.CATEGORY_GENERAL, "HUD Scale", 1.0, "")
                .getDouble(1.0);
            hideWhenChatOpen = config
                .get(Configuration.CATEGORY_GENERAL, "Hide HUD", true, "Hide the HUD when the in-game chat is open")
                .getBoolean(true);

            gradientBadColor = config
                .get(Configuration.CATEGORY_GENERAL, "Bad Gradient", Color.rgb(255, 50, 50), "", 0, Integer.MAX_VALUE)
                .getInt();
            gradientOkColor = config
                .get(Configuration.CATEGORY_GENERAL, "Ok Gradient", Color.rgb(50, 255, 50), "", 0, Integer.MAX_VALUE)
                .getInt();
            gradientGoodColor = config
                .get(Configuration.CATEGORY_GENERAL, "Good Gradient", Color.rgb(50, 50, 255), "", 0, Integer.MAX_VALUE)
                .getInt();

            chartBackgroundColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Chart Background Color",
                    Color.argb(19, 14, 91, (int) (255 * 0.75f)),
                    "",
                    0,
                    Integer.MAX_VALUE)
                .getInt();
            chartBorderColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Chart Border Color",
                    Color.rgb(81, 79, 104),
                    "",
                    0,
                    Integer.MAX_VALUE)
                .getInt();
            masurementsBackgroundColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Measurements Background Color",
                    Color.argb(47, 20, 76, (int) (255 * 0.85)),
                    "",
                    0,
                    Integer.MAX_VALUE)
                .getInt();
            textBadColor = config
                .get(Configuration.CATEGORY_GENERAL, "Bad Text", Color.rgb(255, 0, 0), "", 0, Integer.MAX_VALUE)
                .getInt();
            textOkColor = config
                .get(Configuration.CATEGORY_GENERAL, "Ok Text", Color.rgb(255, 255, 255), "", 0, Integer.MAX_VALUE)
                .getInt();
            textGoodColor = config
                .get(Configuration.CATEGORY_GENERAL, "Good Text", Color.rgb(0, 255, 0), "", 0, Integer.MAX_VALUE)
                .getInt();

        } catch (Exception e) {
            GOGGLES_LOGGER.error("Couldn't load goggles config.");
            GOGGLES_LOGGER.debug(e);
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
}
