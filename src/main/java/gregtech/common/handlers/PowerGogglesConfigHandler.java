package gregtech.common.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

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
    public static boolean hideWhenChatOpen;

    public static int gradientBadColor;
    public static int gradientOkColor;
    public static int gradientGoodColor;

    public static void init(File confFile) {
        if (config == null) {
            config = new Configuration(confFile);
            config.load();
            syncConfig();
        }
    }

    public static void syncConfig() {

        try {
            mainOffsetX = config.get(Configuration.CATEGORY_GENERAL, "Render Offset X", 10, "")
                .getInt(10);
            mainOffsetY = config.get(Configuration.CATEGORY_GENERAL, "Render Offset Y", 40, "")
                .getInt(40);
            rectangleWidth = config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Width", 120, "")
                .getInt(120);
            rectangleHeight = config.get(Configuration.CATEGORY_GENERAL, "Power Rectangle Height", 10, "")
                .getInt(10);
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
            hudScale = config.get(Configuration.CATEGORY_GENERAL, "HUD Scale", 1.0, "")
                .getDouble(1.0);
            hideWhenChatOpen = config
                .get(Configuration.CATEGORY_GENERAL, "Hide HUD", false, "Hide the HUD when the in-game chat is open")
                .getBoolean(false);
            int gradientBadRed = config
                .get(Configuration.CATEGORY_GENERAL, "Bad Gradient Red", 255, "RGB values of the bad gradient", 0, 255)
                .getInt();
            int gradientBadGreen = config.get(Configuration.CATEGORY_GENERAL, "Bad Gradient Green", 50, "", 0, 255)
                .getInt();
            int gradientBadBlue = config.get(Configuration.CATEGORY_GENERAL, "Bad Gradient Blue", 50, "", 0, 255)
                .getInt();
            gradientBadColor = Color.rgb(gradientBadRed, gradientBadGreen, gradientBadBlue);

        } catch (Exception e) {
            System.out.println("Unable to load Config");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
}
