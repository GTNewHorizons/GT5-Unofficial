package net.glease.ggfab;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public enum ConfigurationHandler {

    INSTANCE;

    private Configuration config;
    private float laserOCPenaltyFactor;

    void init(File f) {
        config = new Configuration(f);
        loadConfig();
        setLanguageKeys();
    }

    private void setLanguageKeys() {
        for (String categoryName : config.getCategoryNames()) {
            ConfigCategory category = config.getCategory(categoryName);
            category.setLanguageKey("ggfab.config." + categoryName);
            for (Map.Entry<String, Property> entry : category.entrySet()) {
                entry.getValue().setLanguageKey(String.format("%s.%s", category.getLanguagekey(), entry.getKey()));
            }
        }
    }

    private void loadConfig() {
        laserOCPenaltyFactor = config.getFloat(
                "advasslinePenaltyFactor",
                "common.balancing",
                0.3f,
                0f,
                10f,
                "Laser overclock penalty factor. This will incredibly change the game balance. Even a small step from 0.2 to 0.3 can have very significant impact. Tweak with caution!");
        config.save();
    }

    public Configuration getConfig() {
        return config;
    }

    public float getLaserOCPenaltyFactor() {
        return laserOCPenaltyFactor;
    }
}
