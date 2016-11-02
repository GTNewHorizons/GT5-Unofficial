package gtPlusPlus.core.item.base.plates;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.materials.MaterialUtils;

public class BaseItemPlate extends BaseItemComponent{

	public BaseItemPlate(Material material) {
		super(material, BaseItemComponent.ComponentTypes.PLATE);
	}
	
	public BaseItemPlate(String unlocalizedName, String materialName, short[] colour, int tier, int sRadioactivity) {
		this(MaterialUtils.generateQuickMaterial(materialName, new short[]{colour[0], colour[1], colour[2], 0}, sRadioactivity));
	}

	

}
