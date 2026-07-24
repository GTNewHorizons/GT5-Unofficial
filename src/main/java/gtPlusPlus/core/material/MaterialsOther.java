package gtPlusPlus.core.material;

import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class MaterialsOther {

    // Soul Sand
    public static final Material SOULSAND = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.SoulSand);

    // Redstone
    public static final Material REDSTONE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Redstone);

    // Glowstone Dust
    public static final Material GLOWSTONE = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Glowstone);

    // Enderpearl
    public static final Material ENDERPEARL = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.EnderPearl);

    // Raw Flesh
    public static final Material MEAT = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.MeatRaw);

    // Clay
    public static final Material CLAY = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.Clay);

    // Cast Iron
    public static final Material CAST_IRON = MaterialUtils.generateMaterialFromGtENUM(Materials2Materials.CastIron);

    // PTFE
    public static final Material PTFE = MaterialUtils
        .generateMaterialFromGtENUM(MU.material(MaterialUtils.getMaterial("Polytetrafluoroethylene", "Plastic")));

    // Plastic
    public static final Material PLASTIC = MaterialUtils
        .generateMaterialFromGtENUM(MU.material(MaterialUtils.getMaterial("Plastic", "Rubber")));

    static {
        MEAT.setTextureSet(TextureSet.SET_ROUGH);
        CLAY.setTextureSet(TextureSet.SET_ROUGH);
    }
}
