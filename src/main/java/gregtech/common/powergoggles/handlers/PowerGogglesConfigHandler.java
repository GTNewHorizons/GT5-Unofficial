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
    public static boolean manualGraphScale;
    public static String manualGraphMin;
    public static String manualGraphMax;
    public static boolean showPowerChart;
    public static boolean showPowerBar;
    public static boolean hideWhenChatOpen;

    public static int gradientBadColor;
    public static int gradientOkColor;
    public static int gradientGoodColor;

    public static int chartBackgroundColor;
    public static int chartBorderColor;
    public static int chartMinTextColor;
    public static int chartMaxTextColor;
    public static int chartManualScaleIndicatorColor;
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
            manualGraphScale = config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Scale", false, "")
                .getBoolean(false);
            manualGraphMin = config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Min", "0", "")
                .getString();
            manualGraphMax = config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Max", "1000", "")
                .getString();
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
            chartMinTextColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Chart Min Text Color",
                    Color.rgb(237, 2, 158),
                    "",
                    0,
                    Integer.MAX_VALUE)
                .getInt();
            chartMaxTextColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Chart Max Text Color",
                    Color.rgb(237, 2, 158),
                    "",
                    0,
                    Integer.MAX_VALUE)
                .getInt();
            chartManualScaleIndicatorColor = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Chart Manual Scale Indicator Color",
                    Color.rgb(255, 210, 80),
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

    public static void resetToDefaults() {
        if (config == null) return;

        mainOffsetX = 10;
        mainOffsetY = 40;
        rectangleWidth = 120;
        rectangleHeight = 4;
        mainTextScaling = 1;
        subTextScaling = 0.75;
        gradientPreset = "NORMAL";
        formatIndex = 0;
        readingIndex = 0;
        gradientIndex = 0;
        manualGraphScale = false;
        manualGraphMin = "0";
        manualGraphMax = "1000";
        showPowerChart = false;
        showPowerBar = true;
        hudScale = 1.0;
        hideWhenChatOpen = true;

        gradientBadColor = Color.rgb(255, 50, 50);
        gradientOkColor = Color.rgb(50, 255, 50);
        gradientGoodColor = Color.rgb(50, 50, 255);
        chartBackgroundColor = Color.argb(19, 14, 91, (int) (255 * 0.75f));
        chartBorderColor = Color.rgb(81, 79, 104);
        chartMinTextColor = Color.rgb(237, 2, 158);
        chartMaxTextColor = Color.rgb(237, 2, 158);
        chartManualScaleIndicatorColor = Color.rgb(255, 210, 80);
        masurementsBackgroundColor = Color.argb(47, 20, 76, (int) (255 * 0.85));
        textBadColor = Color.rgb(255, 0, 0);
        textOkColor = Color.rgb(255, 255, 255);
        textGoodColor = Color.rgb(0, 255, 0);

        config.get(Configuration.CATEGORY_GENERAL, "Render Offset X", 10, "")
            .set(mainOffsetX);
        config.get(Configuration.CATEGORY_GENERAL, "Render Offset Y", 37, "")
            .set(mainOffsetY);
        config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Width", 120, "")
            .set(rectangleWidth);
        config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Height", 4, "")
            .set(rectangleHeight);
        config.get(Configuration.CATEGORY_GENERAL, "Storage Text Scale", 1, "Text size of the storage EU reading")
            .set(mainTextScaling);
        config
            .get(
                Configuration.CATEGORY_GENERAL,
                "Timed Reading Text Scale",
                0.75,
                "Text size of the 5m and 1h readings")
            .set(subTextScaling);
        config.get(Configuration.CATEGORY_GENERAL, "Gradient Preset", "NORMAL", "Available options: NORMAL")
            .set(gradientPreset);
        config.get(Configuration.CATEGORY_GENERAL, "Format Index", 0, "Available options: SI, SCIENTIFIC, ENGINEERING")
            .set(formatIndex);
        config.get(Configuration.CATEGORY_GENERAL, "Reading Index", 0, "Available options: TOTAL, EUT, BOTH")
            .set(readingIndex);
        config.get(Configuration.CATEGORY_GENERAL, "Gradient Index", 0, "Available options: NORMAL, DEUTERANOPIA")
            .set(gradientIndex);
        config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Scale", false, "")
            .set(manualGraphScale);
        config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Min", "0", "")
            .set(manualGraphMin);
        config.get(Configuration.CATEGORY_GENERAL, "Manual Graph Max", "1000", "")
            .set(manualGraphMax);
        config.get(Configuration.CATEGORY_GENERAL, "Show Power Chart", false, "")
            .set(showPowerChart);
        config.get(Configuration.CATEGORY_GENERAL, "Show Power Bar", true, "")
            .set(showPowerBar);
        config.get(Configuration.CATEGORY_GENERAL, "HUD Scale", 1.0, "")
            .set(hudScale);
        config.get(Configuration.CATEGORY_GENERAL, "Hide HUD", true, "Hide the HUD when the in-game chat is open")
            .set(hideWhenChatOpen);

        config.get(Configuration.CATEGORY_GENERAL, "Bad Gradient", gradientBadColor, "", 0, Integer.MAX_VALUE)
            .set(gradientBadColor);
        config.get(Configuration.CATEGORY_GENERAL, "Ok Gradient", gradientOkColor, "", 0, Integer.MAX_VALUE)
            .set(gradientOkColor);
        config.get(Configuration.CATEGORY_GENERAL, "Good Gradient", gradientGoodColor, "", 0, Integer.MAX_VALUE)
            .set(gradientGoodColor);
        config
            .get(
                Configuration.CATEGORY_GENERAL,
                "Chart Background Color",
                chartBackgroundColor,
                "",
                0,
                Integer.MAX_VALUE)
            .set(chartBackgroundColor);
        config.get(Configuration.CATEGORY_GENERAL, "Chart Border Color", chartBorderColor, "", 0, Integer.MAX_VALUE)
            .set(chartBorderColor);
        config.get(Configuration.CATEGORY_GENERAL, "Chart Min Text Color", chartMinTextColor, "", 0, Integer.MAX_VALUE)
            .set(chartMinTextColor);
        config.get(Configuration.CATEGORY_GENERAL, "Chart Max Text Color", chartMaxTextColor, "", 0, Integer.MAX_VALUE)
            .set(chartMaxTextColor);
        config
            .get(
                Configuration.CATEGORY_GENERAL,
                "Chart Manual Scale Indicator Color",
                chartManualScaleIndicatorColor,
                "",
                0,
                Integer.MAX_VALUE)
            .set(chartManualScaleIndicatorColor);
        config
            .get(
                Configuration.CATEGORY_GENERAL,
                "Measurements Background Color",
                masurementsBackgroundColor,
                "",
                0,
                Integer.MAX_VALUE)
            .set(masurementsBackgroundColor);
        config.get(Configuration.CATEGORY_GENERAL, "Bad Text", textBadColor, "", 0, Integer.MAX_VALUE)
            .set(textBadColor);
        config.get(Configuration.CATEGORY_GENERAL, "Ok Text", textOkColor, "", 0, Integer.MAX_VALUE)
            .set(textOkColor);
        config.get(Configuration.CATEGORY_GENERAL, "Good Text", textGoodColor, "", 0, Integer.MAX_VALUE)
            .set(textGoodColor);

        config.save();
    }
}
