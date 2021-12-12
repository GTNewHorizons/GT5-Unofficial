package gtPlusPlus.core.item.base.bolts;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemBolt extends BaseItemComponent{

	public BaseItemBolt(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.BOLT);
	}
}
