package gtPlusPlus.core.item.base.ingots;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemIngot extends BaseItemComponent {

    protected final String materialName;
    protected final String unlocalName;

    public BaseItemIngot(final Material material) {
        this(material, ComponentTypes.INGOT);
    }

    public BaseItemIngot(final Material material, final ComponentTypes type) {
        super(material, type);
        this.materialName = material.getLocalizedName();
        this.unlocalName = material.getUnlocalizedName();
    }
}
