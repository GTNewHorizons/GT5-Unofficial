package gregtech.client;

import static gregtech.api.enums.ModIDs.GregTech;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import gregtech.api.GregTech_API;

public class GT_GUI_ClientConfig extends GuiConfig {

    public GT_GUI_ClientConfig(GuiScreen parentScreen) {
        super(
                parentScreen,
                getConfigElements(),
                GregTech.modID,
                "client",
                false,
                false,
                getAbridgedConfigPath(GregTech_API.sClientDataFile.mConfig.toString()));
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigElements() {
        final Configuration config = GregTech_API.sClientDataFile.mConfig;
        setLanguageKeys(config);
        return config.getCategoryNames()
                     .stream()
                     .filter(name -> name.indexOf('.') == -1)
                     .map(name -> new ConfigElement(config.getCategory(name)))
                     .collect(Collectors.toList());
    }

    private static void setLanguageKeys(Configuration config) {
        for (String categoryName : config.getCategoryNames()) {
            ConfigCategory category = config.getCategory(categoryName);
            category.setLanguageKey("GT5U.config." + categoryName);
            for (Map.Entry<String, Property> entry : category.entrySet()) {
                // drop the default value in name
                String name = entry.getKey();
                int defaultStart = name.lastIndexOf('_');
                String realName = defaultStart >= 0 ? name.substring(0, defaultStart) : name;
                entry.getValue()
                     .setLanguageKey(String.format("%s.%s", category.getLanguagekey(), realName));
            }
        }
    }
}
