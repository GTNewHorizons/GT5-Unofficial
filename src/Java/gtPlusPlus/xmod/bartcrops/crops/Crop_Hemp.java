package gtPlusPlus.xmod.bartcrops.crops;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bartcrops.abstracts.BaseAestheticCrop;
import ic2.api.crops.ICropTile;
import net.minecraft.item.ItemStack;

public class Crop_Hemp extends BaseAestheticCrop {
	
	public int tier() {
		return 2;
	}

	public String name() {
		return "Hemp";
	}

	public String discoveredBy() {
		return "Alkalus";
	}

	public int growthDuration(ICropTile crop) {
		int ret = 550;
		
		/*if (crop.isBlockBelow(Blocks.dirt) || crop.isBlockBelow(Blocks.flowing_water)) {
			ret = 225;
		}*/

		if (CORE.DEBUG) {
			ret = 1;
		}

		return ret;
	}

	public String[] attributes() {
		return new String[]{"Green", "Soil", "Orange"};
	}

	public ItemStack getGain(ICropTile crop) {
		
		ItemStack ret = this.getDisplayItem();
		if (MathUtils.randInt(0, 10) > 8) {
			ret = ItemUtils.getSimpleStack(ModItems.itemRope, MathUtils.randInt(1, 3));
		}

		return ret;
	}

	public ItemStack getDisplayItem() {
		return ItemUtils.getSimpleStack(ModItems.itemRope, 0);
	}
}