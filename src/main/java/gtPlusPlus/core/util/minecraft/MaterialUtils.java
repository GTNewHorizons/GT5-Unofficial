package gtPlusPlus.core.util.minecraft;

import net.minecraft.util.StatCollector;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.util.GTLanguageManager;
import gregtech.loaders.materials.LegacyNameDomain;
import gtPlusPlus.core.util.math.MathUtils;

public class MaterialUtils {

    public static boolean hasValidRGBA(final short[] rgba) {
        return rgba != null && rgba.length >= 3 && rgba.length <= 4;
    }

    public static int getTierOfMaterial(final double aMeltingPoint) {
        return aMeltingPoint < 1000 ? 0 : (MathUtils.roundToClosestInt(aMeltingPoint / 1000f));
    }

    @Deprecated // use TierEU enum
    public static int getVoltageForTier(int aTier) {
        // aTier += 1; - Probably some logic to this, idk.

        return switch (aTier) {
            case 0 -> 16;
            case 1 -> 30;
            case 2 -> 120;
            case 3 -> 480;
            case 4 -> 1920;
            case 5 -> 7680;
            case 6 -> 30720;
            case 7 -> 122880;
            case 8 -> 491520;
            case 9 -> 1966080;
            case 10 -> 7864320;
            case 11 -> 31457280;
            case 12 -> 125829120;
            case 13 -> 503316480;
            case 14 -> 2013265920;
            default -> Integer.MAX_VALUE;
        };

    }

    private static Material getMaterialByName(String materialName) {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (!LegacyNameDomain.contains(ml)) {
                continue;
            }
            if (MaterialUtils.getMaterialName(ml)
                .equalsIgnoreCase(materialName)) {
                return ml;
            }
        }
        return null;
    }

    public static String getMaterialName(Material mat) {
        String mName = gregtech.api.material.MaterialUtils.localName(mat);
        if (mName == null || mName.isEmpty()) {
            mName = gregtech.api.material.MaterialUtils.internalName(mat);
        }
        return mName;
    }

    public static Material getMaterial(String aMaterialName, String aFallbackMaterialName) {
        Material g = getMaterial(aMaterialName);
        if (g == null) {
            g = getMaterial(aFallbackMaterialName);
        }
        if (g == null) {
            throw new IllegalStateException();
        }
        return g;
    }

    public static Material getMaterial(String aMaterialName) {
        Material m = LegacyNameDomain.lookup(aMaterialName);
        if (m == null) {
            m = getMaterialByName(aMaterialName);
        }
        if (m == null) {
            m = Materials2Materials.NULL;
        }
        return m;
    }

    public static boolean isNullGregtechMaterial(Material aGregtechMaterial) {
        return aGregtechMaterial == Materials2Materials.NULL;
    }

    public static void generateMaterialLocalizedName(String materialNameForKey, String materialDefaultLocalName) {
        GTLanguageManager
            .addStringLocalization(getMaterialLocalizedNameKey(materialNameForKey), materialDefaultLocalName);
    }

    public static void generateMaterialLocalizedName(String name) {
        generateMaterialLocalizedName(name, name);
    }

    public static String getMaterialLocalizedName(String defaultName) {
        return StatCollector.translateToLocal(getMaterialLocalizedNameKey(defaultName));
    }

    public static String getMaterialLocalizedNameKey(String materialName) {
        return "Material." + materialName.toLowerCase()
            .replaceAll("[^a-zA-Z0-9]", "");
    }
}
