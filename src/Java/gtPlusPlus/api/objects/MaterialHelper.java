package gtPlusPlus.api.objects;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

public class MaterialHelper {

	public static ItemStack getComponentFromMaterial(OrePrefixes oreprefix, Material material, int amount){
		return ItemUtils.getOrePrefixStack(oreprefix, material, amount);
	}
	
}
