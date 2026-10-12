package gregtech.api.enums.materials;

import java.util.EnumSet;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialEdit;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;
import com.ruling_0.materiallib.api.TextureSet;

import gregtech.api.enums.Mods;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// Materials whose items all belong to another mod. GregTech always declares the material, so its oredict names
/// and save identity stay stable, and takes the items from the owning mod through [ForeignShape] rows.
///
/// A row applies only when its mod is loaded. Without the mod the material has no shapes, so call sites check
/// `Material#hasShape` or go through `MaterialParts` instead of fetching a part directly.
///
/// `crystalFluix`, an oredict name with no MaterialLib shape, reaches the material through
/// [gregtech.common.oredict.OreDictAliases].
public class XModMaterials {

    public static Material Fluix;

    private record ForeignShape(Shape shape, Mods mod, String registryName, int meta) {}

    private XModMaterials() {}

    public static void init() {
        Fluix = MaterialLibAPI.newMaterial("gregtech", "Fluix", TextureSet.of("gregtech", "NONE"))
            .setTint(0xFFFFFFFF)
            .setProperty(GTMaterialProperties.LOCAL_NAME, "Fluix")
            .setProperty(StandardProperties.DISPLAY_NAME_KEY, "Material.fluix")
            .setProperty(
                GTMaterialProperties.FLAGS,
                EnumSet.of(
                    GTMaterialFlag.CRYSTAL,
                    GTMaterialFlag.CRYSTALLISABLE,
                    GTMaterialFlag.NO_SMASHING,
                    GTMaterialFlag.NO_SMELTING))
            .build();
        applyForeignShapes(
            Fluix,
            new ForeignShape(Shapes.gem, Mods.AppliedEnergistics2, "item.ItemMultiMaterial", 7),
            new ForeignShape(Shapes.dust, Mods.AppliedEnergistics2, "item.ItemMultiMaterial", 8));
    }

    private static void applyForeignShapes(Material material, ForeignShape... rows) {
        MaterialEdit edit = MaterialLibAPI.editMaterial(material.getModId(), material.getName());
        for (ForeignShape row : rows) {
            if (!row.mod()
                .isModLoaded()) continue;
            edit.generateShape(row.shape())
                .addShapeOverride(row.shape(), row.mod().ID, row.registryName(), row.meta());
        }
    }
}
