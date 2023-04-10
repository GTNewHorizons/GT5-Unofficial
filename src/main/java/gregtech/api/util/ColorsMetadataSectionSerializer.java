package gregtech.api.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;

@SideOnly(Side.CLIENT)
public class ColorsMetadataSectionSerializer extends BaseMetadataSectionSerializer {

    public ColorsMetadataSection deserialize(JsonElement metadataColors, Type type,
        JsonDeserializationContext context) {
        // Default values
        boolean enableGuiTint = GregTech_API.sColoredGUI;
        Map<String, String> hexGuiTintMap = new HashMap<>();
        Map<String, String> hexTextColorMap = new HashMap<>();

        JsonObject jsonObject = JsonUtils.getJsonElementAsJsonObject(metadataColors, "metadata section");
        if (jsonObject.has("textColor")) {
            JsonObject textColors = JsonUtils.func_152754_s(jsonObject, "textColor");
            for (Map.Entry<String, JsonElement> entry : textColors.entrySet()) {
                if (entry.getValue()
                    .isJsonPrimitive()) {
                    hexTextColorMap.put(
                        entry.getKey(),
                        entry.getValue()
                            .getAsString());
                } else {
                    GT_Mod.GT_FML_LOGGER.warn("ColorOverride expects primitive value for key `textColor`");
                }
            }
        }

        if (jsonObject.has("guiTint")) {
            JsonObject guiTints = JsonUtils.func_152754_s(jsonObject, "guiTint");
            enableGuiTint = JsonUtils
                .getJsonObjectBooleanFieldValueOrDefault(guiTints, "enableGuiTintWhenPainted", true);

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
