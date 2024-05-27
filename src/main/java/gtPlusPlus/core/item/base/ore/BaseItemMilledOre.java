package gtPlusPlus.core.item.base.ore;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.item.Item;

public class BaseItemMilledOre extends BaseOreComponent {

    public BaseItemMilledOre(final Material material, int aMaterialEU) {
        super(material, BaseOreComponent.ComponentTypes.MILLED);
        CORE.RA.addMillingRecipe(material, aMaterialEU);
    }

    public static Item generate(Materials aMat, int aMaterialEU) {
        return generate(MaterialUtils.generateMaterialFromGtENUM(aMat), aMaterialEU);
    }

    public static Item generate(Material aMat, int aMaterialEU) {
        return new BaseItemMilledOre(aMat, aMaterialEU);
    }
}
