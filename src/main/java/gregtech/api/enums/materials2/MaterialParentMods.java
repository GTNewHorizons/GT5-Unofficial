package gregtech.api.enums.materials2;

import java.util.Map;

import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.Mods;

/// The parent-mod availability gate for the `BlockMetal` storage-block materials as declared rows:
/// material -> the [Mods] entry whose absence disables the material. The data declares exactly one row --
/// `HSLA` is disabled when RotaryCraft is absent -- so every other material passes unconditionally.
public class MaterialParentMods {

    private static Map<Material, Mods> table;

    private MaterialParentMods() {}

    /// Whether `material`'s parent mod is present (or it has none).
    public static boolean hasParentMod(Material material) {
        Mods parent = table().get(material);
        return parent == null || parent.isModLoaded() || GTMod.proxy.mEnableAllMaterials;
    }

    private static Map<Material, Mods> table() {
        if (table == null) {
            if (Materials.HSLA == null) {
                throw new IllegalStateException("Parent-mod table consulted before Materials2.init");
            }
            table = Map.of(Materials.HSLA, Mods.RotaryCraft);
        }
        return table;
    }
}
