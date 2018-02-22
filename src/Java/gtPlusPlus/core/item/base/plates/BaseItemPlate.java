package gtPlusPlus.core.item.base.plates;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class BaseItemPlate extends BaseItemComponent{

	public BaseItemPlate(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.PLATE);
	}

	public BaseItemPlate(final String unlocalizedName, final String materialName, final MaterialState state, final short[] colour, final int tier, final int sRadioactivity) {
		this(MaterialUtils.generateQuickMaterial(materialName, state, new short[]{colour[0], colour[1], colour[2], 0}, sRadioactivity));
	}



}
