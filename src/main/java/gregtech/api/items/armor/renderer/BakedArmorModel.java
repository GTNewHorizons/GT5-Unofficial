package gregtech.api.items.armor.renderer;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BakedArmorModel {

    public final int textureWidth, textureHeight;
    public final Map<String, BakedBone> bonesByName = new LinkedHashMap<>();

    public BakedArmorModel(int textureWidth, int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }
}
