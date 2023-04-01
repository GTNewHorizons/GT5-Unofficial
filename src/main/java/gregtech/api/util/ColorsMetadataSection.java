package gregtech.api.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.data.IMetadataSection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorsMetadataSection implements IMetadataSection {

    private final Map<String, Integer> textColors;
    private final Map<String, String> hexTextColors;
    private final Map<String, Integer> guiTints;
    private final Map<String, String> hexGuiTints;
    private final boolean guiTintEnabled;

    public ColorsMetadataSection(Map<String, String> hexTextColorMap, Map<String, String> hexGuiTintMap,
            boolean guiTintEnabled) {
        this.hexTextColors = hexTextColorMap;
        this.textColors = convertHexMapToIntMap(hexTextColorMap);

        this.hexGuiTints = hexGuiTintMap;
        this.guiTints = convertHexMapToIntMap(hexGuiTintMap);

        this.guiTintEnabled = guiTintEnabled;
    }

    private Map<String, Integer> convertHexMapToIntMap(Map<String, String> hexMap) {
        Map<String, Integer> intMap = new HashMap<>();

        for (String key : hexMap.keySet()) {
            int colorValue = -1;
            String hex = hexMap.get(key);
            try {
                if (!hex.isEmpty()) colorValue = Integer.parseUnsignedInt(hex, 16);
            } catch (final NumberFormatException e) {
                GT_Log.err.println("Couldn't format color correctly of " + key + " -> " + hex);
            }
            intMap.put(key, colorValue);
        }
        return intMap;
    }

    public int getTextColorOrDefault(String key, int defaultColor) {
        return isColorInMap(key, this.hexTextColors) ? this.textColors.get(key) : defaultColor;
    }

    public int getGuiTintOrDefault(String key, int defaultColor) {
        return isColorInMap(key, this.hexGuiTints) ? this.guiTints.get(key) : defaultColor;
    }

    private boolean isColorInMap(String key, Map<String, String> hexMap) {
        return hexMap.containsKey(key) && !hexMap.get(key)
                                                 .isEmpty();
    }

    public boolean sGuiTintingEnabled() {
        return this.guiTintEnabled;
    }
}
