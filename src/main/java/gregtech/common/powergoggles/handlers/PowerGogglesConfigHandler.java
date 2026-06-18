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
    public static boolean showMeasurements;
    public static boolean hideWhenChatOpen;

    public static int gradientBadColor;
    public static int gradientOkColor;
    public static int gradientGoodColor;

    public static int chartBackgroundColor;
    public static int chartBorderColor;
    public static int chartMinTextColor;
    public static int chartMaxTextColor;
    public static int chartManualScaleIndicatorColor;
    public static int measurementsBackgroundColor;

    public static int textBadColor;
    public static int textOkColor;
    public static int textGoodColor;
    public static final Logger GOGGLES_LOGGER = LogManager.getLogger("GT5U Power Goggles");

    private static final int DEFAULT_GRADIENT_BAD_COLOR = Color.rgb(255, 50, 50);
    private static final int DEFAULT_GRADIENT_OK_COLOR = Color.rgb(50, 255, 50);
    private static final int DEFAULT_GRADIENT_GOOD_COLOR = Color.rgb(50, 50, 255);
    private static final int DEFAULT_CHART_BACKGROUND_COLOR = Color.argb(19, 14, 91, (int) (255 * 0.75f));
    private static final int DEFAULT_CHART_BORDER_COLOR = Color.rgb(81, 79, 104);
    private static final int DEFAULT_CHART_MIN_TEXT_COLOR = Color.rgb(237, 2, 158);
    private static final int DEFAULT_CHART_MAX_TEXT_COLOR = Color.rgb(237, 2, 158);
    private static final int DEFAULT_CHART_MANUAL_SCALE_INDICATOR_COLOR = Color.rgb(255, 210, 80);
    private static final int DEFAULT_MEASUREMENTS_BACKGROUND_COLOR = Color.argb(47, 20, 76, (int) (255 * 0.85));
    private static final int DEFAULT_TEXT_BAD_COLOR = Color.rgb(255, 0, 0);
    private static final int DEFAULT_TEXT_OK_COLOR = Color.rgb(255, 255, 255);
    private static final int DEFAULT_TEXT_GOOD_COLOR = Color.rgb(0, 255, 0);

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
            showMeasurements = config.get(Configuration.CATEGORY_GENERAL, "Show Measurements Section", true, "")
                .getBoolean(true);
            hudScale = config.get(Configuration.CATEGORY_GENERAL, "HUD Scale", 1.0, "")
                .getDouble(1.0);
            hideWhenChatOpen = config
                .get(Configuration.CATEGORY_GENERAL, "Hide HUD", true, "Hide the HUD when the in-game chat is open")
                .getBoolean(true);

            gradientBadColor = getColorSetting("Bad Gradient", DEFAULT_GRADIENT_BAD_COLOR);
            gradientOkColor = getColorSetting("Ok Gradient", DEFAULT_GRADIENT_OK_COLOR);
            gradientGoodColor = getColorSetting("Good Gradient", DEFAULT_GRADIENT_GOOD_COLOR);
            chartBackgroundColor = getColorSetting("Chart Background Color", DEFAULT_CHART_BACKGROUND_COLOR);
            chartBorderColor = getColorSetting("Chart Border Color", DEFAULT_CHART_BORDER_COLOR);
            chartMinTextColor = getColorSetting("Chart Min Text Color", DEFAULT_CHART_MIN_TEXT_COLOR);
            chartMaxTextColor = getColorSetting("Chart Max Text Color", DEFAULT_CHART_MAX_TEXT_COLOR);
            chartManualScaleIndicatorColor = getColorSetting(
                "Chart Manual Scale Indicator Color",
                DEFAULT_CHART_MANUAL_SCALE_INDICATOR_COLOR);
            measurementsBackgroundColor = getColorSetting(
                "Measurements Background Color",
                DEFAULT_MEASUREMENTS_BACKGROUND_COLOR);
            textBadColor = getColorSetting("Bad Text", DEFAULT_TEXT_BAD_COLOR);
            textOkColor = getColorSetting("Ok Text", DEFAULT_TEXT_OK_COLOR);
            textGoodColor = getColorSetting("Good Text", DEFAULT_TEXT_GOOD_COLOR);

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
        showMeasurements = true;
        hudScale = 1.0;
        hideWhenChatOpen = true;

        gradientBadColor = DEFAULT_GRADIENT_BAD_COLOR;
        gradientOkColor = DEFAULT_GRADIENT_OK_COLOR;
        gradientGoodColor = DEFAULT_GRADIENT_GOOD_COLOR;
        chartBackgroundColor = DEFAULT_CHART_BACKGROUND_COLOR;
        chartBorderColor = DEFAULT_CHART_BORDER_COLOR;
        chartMinTextColor = DEFAULT_CHART_MIN_TEXT_COLOR;
        chartMaxTextColor = DEFAULT_CHART_MAX_TEXT_COLOR;
        chartManualScaleIndicatorColor = DEFAULT_CHART_MANUAL_SCALE_INDICATOR_COLOR;
        measurementsBackgroundColor = DEFAULT_MEASUREMENTS_BACKGROUND_COLOR;
        textBadColor = DEFAULT_TEXT_BAD_COLOR;
        textOkColor = DEFAULT_TEXT_OK_COLOR;
        textGoodColor = DEFAULT_TEXT_GOOD_COLOR;

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
        config.get(Configuration.CATEGORY_GENERAL, "Show Measurements Section", true, "")
            .set(showMeasurements);
        config.get(Configuration.CATEGORY_GENERAL, "HUD Scale", 1.0, "")
            .set(hudScale);
        config.get(Configuration.CATEGORY_GENERAL, "Hide HUD", true, "Hide the HUD when the in-game chat is open")
            .set(hideWhenChatOpen);

        setColorSetting("Bad Gradient", gradientBadColor);
        setColorSetting("Ok Gradient", gradientOkColor);
        setColorSetting("Good Gradient", gradientGoodColor);
        setColorSetting("Chart Background Color", chartBackgroundColor);
        setColorSetting("Chart Border Color", chartBorderColor);
        setColorSetting("Chart Min Text Color", chartMinTextColor);
        setColorSetting("Chart Max Text Color", chartMaxTextColor);
        setColorSetting("Chart Manual Scale Indicator Color", chartManualScaleIndicatorColor);
        setColorSetting("Measurements Background Color", measurementsBackgroundColor);
        setColorSetting("Bad Text", textBadColor);
        setColorSetting("Ok Text", textOkColor);
        setColorSetting("Good Text", textGoodColor);

        config.save();
    }

    private static int getColorSetting(String key, int defaultValue) {
        return config.get(Configuration.CATEGORY_GENERAL, key, defaultValue, "", 0, Integer.MAX_VALUE)
            .getInt();
    }

    private static void setColorSetting(String key, int value) {
        config.get(Configuration.CATEGORY_GENERAL, key, value, "", 0, Integer.MAX_VALUE)
            .set(value);
    }
}
