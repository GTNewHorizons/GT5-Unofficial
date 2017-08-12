package gtPlusPlus.preloader;

import gregtech.common.items.GT_MetaGenerated_Item_01;
import net.minecraft.item.ItemStack;

public class Preloader_GT_OreDict {
	
	public static boolean shouldPreventRegistration(String string, ItemStack circuit) {
		int damageValue = circuit.getItemDamage() - 32000;
		if (circuit.getItem() instanceof GT_MetaGenerated_Item_01) { // 700-720
			if (damageValue >= 700 && damageValue <= 720) {
				return true;
			}
		}
		else {
			try {
				if (Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03") != null) { // 6/11/12/14/16/20/30-57/69-73/79-96
					Class<?> MetaItem03 = Class.forName("gregtech.common.items.GT_MetaGenerated_Item_03");
					if (isInstanceOf(MetaItem03, circuit.getItem())) {
						if (damageValue == 6 || damageValue == 11 || damageValue == 12 || damageValue == 14
								|| damageValue == 16 || damageValue == 20) {
							return true;
						}
						else if (damageValue >= 30 && damageValue <= 57) {
							return true;
						}
						else if (damageValue >= 69 && damageValue <= 73) {
							return true;
						}
						else if (damageValue >= 79 && damageValue <= 96) {
							return true;
						}
					}
				}
			}
			catch (ClassNotFoundException e) {
			}
		}
		return false;
	}

	// Simplification of Life.
	private static boolean isInstanceOf(Class<?> clazz, Object obj) {
		return clazz.isInstance(obj);
	}
	
}
