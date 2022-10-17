package gtPlusPlus.core.item.base.plates;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemPlateDouble extends BaseItemComponent {

    public BaseItemPlateDouble(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.PLATEDOUBLE);
        this.setMaxStackSize(32);
    }
}
