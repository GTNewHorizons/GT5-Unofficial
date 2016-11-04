package gtPlusPlus.core.item.base.gears;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemGear extends BaseItemComponent {

	public BaseItemGear(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.GEAR);
	}
}
