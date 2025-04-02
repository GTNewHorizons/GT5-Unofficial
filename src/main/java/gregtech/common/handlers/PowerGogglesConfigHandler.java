package gregtech.common.handlers;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class PowerGogglesConfigHandler {

    public static Configuration config;

    public static int mainOffsetX;
    public static int mainOffsetY;
    public static int rectangleWidth;
    public static int rectangleHeight;
    public static double mainTextScaling;
    public static double subTextScaling;
    public static String gradientPreset;
    public static String numberFormatting;

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
            mainOffsetY = config.get(Configuration.CATEGORY_GENERAL, "Render Offset Y", 10, "")
                .getInt(10);
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
                    "Time Change Text Scale",
                    1 / 1.35,
                    "Text size of the 5m and 1h readings")
                .getDouble(1 / 1.35);
            gradientPreset = config
                .get(Configuration.CATEGORY_GENERAL, "Gradient Preset", "NORMAL", "Available options: NORMAL")
                .getString();
            numberFormatting = config
                .get(
                    Configuration.CATEGORY_GENERAL,
                    "Number Formatting",
                    "SCIENTIFIC",
                    "Available options: SI, SCIENTIFIC, ENGINEERING")
                .getString();

        } catch (Exception e) {
            System.out.println("Unable to load Config");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
}
