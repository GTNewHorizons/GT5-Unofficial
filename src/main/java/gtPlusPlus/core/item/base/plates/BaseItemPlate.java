package gtPlusPlus.core.item.base.plates;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemPlate extends BaseItemComponent {

    public BaseItemPlate(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.PLATE);
    }
}
