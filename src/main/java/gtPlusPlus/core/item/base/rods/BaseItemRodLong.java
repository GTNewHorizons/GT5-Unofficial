package gtPlusPlus.core.item.base.rods;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import net.minecraft.item.ItemStack;

public class BaseItemRodLong extends BaseItemComponent{

	public BaseItemRodLong(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.RODLONG);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return ("Long "+this.materialName+ " Rod");
	}

}
