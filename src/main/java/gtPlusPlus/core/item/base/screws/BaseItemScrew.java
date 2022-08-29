package gtPlusPlus.core.item.base.screws;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemScrew extends BaseItemComponent {

    public BaseItemScrew(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.SCREW);
    }
}
