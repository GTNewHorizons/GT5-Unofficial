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

/// Materials whose items all belong to another mod. GregTech declares the material itself unconditionally, so
/// its ore-dictionary names keep resolving and its registration name stays stable in a saved world, and takes
/// the item side from the owning mod through [ForeignShape] rows applied with [MaterialLibAPI#editMaterial].
///
/// Every row is conditional on its mod: a generated shape with no override makes MaterialLib mint its own item,
/// which for a material GregTech does not itself produce would be a new item appearing whenever the mod is
/// absent. Without the mod the material therefore stays shapeless, and call sites must read it through
/// `MaterialParts` or `Material#hasShape` rather than fetching a part outright.
///
/// A prefix MaterialLib has no shape for cannot carry a row: those ore-dictionary names
/// (`crystalFluix`, `pearlFluix`) reach the material through [gregtech.common.oredict.OreDictAliases] and the
/// ordinary name parsing instead.
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
                    GTMaterialFlag.NO_SMELTING,
                    GTMaterialFlag.QUARTZ))
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
