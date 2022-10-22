package gregtech.api.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;

@SideOnly(Side.CLIENT)
public class ColorsMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer {
    public ColorsMetadataSection deserialize(
            JsonElement metadataColors, Type type, JsonDeserializationContext context) {
        // Default values
        boolean enableGuiTint = GregTech_API.sColoredGUI;
        Map<String, String> hexGuiTintMap = new HashMap<String, String>();
        Map<String, String> hexTextColorMap = new HashMap<String, String>() {
            {
                put("title", "");
                put("text", "");
                put("value", "");
                put("nei", "");
            }
        };

        JsonObject jsonObject = JsonUtils.getJsonElementAsJsonObject(metadataColors, "metadata section");
        if (jsonObject.has("textColor")) {
            JsonObject textColors = JsonUtils.func_152754_s(jsonObject, "textColor");
            for (String key : hexTextColorMap.keySet()) {
                hexTextColorMap.replace(key, JsonUtils.getJsonObjectStringFieldValueOrDefault(textColors, key, ""));
            }
        }

        if (jsonObject.has("guiTint")) {
            JsonObject guiTints = JsonUtils.func_152754_s(jsonObject, "guiTint");
            enableGuiTint =
                    JsonUtils.getJsonObjectBooleanFieldValueOrDefault(guiTints, "enableGuiTintWhenPainted", true);

            for (Dyes dye : Dyes.values()) {
                hexGuiTintMap.put(dye.mName, GT_Util.toHexString(dye.getRGBA()));
            }

            for (String key : hexGuiTintMap.keySet()) {
                if (enableGuiTint) {
                    hexGuiTintMap.replace(
                            key,
                            JsonUtils.getJsonObjectStringFieldValueOrDefault(guiTints, key, hexGuiTintMap.get(key)));
                } else {
                    hexGuiTintMap.replace(key, GT_Util.toHexString(Dyes.dyeWhite.getRGBA()));
                }
            }
        }

        return new ColorsMetadataSection(hexTextColorMap, hexGuiTintMap, enableGuiTint);
    }

    public JsonElement serialize(ColorsMetadataSection colorsMetaSection, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        return jsonObject;
    }

    public String getSectionName() {
        return "colors";
    }

    public JsonElement serialize(Object object, Type type, JsonSerializationContext context) {
        return this.serialize((ColorsMetadataSection) object, type, context);
    }
}
