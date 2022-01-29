package gtPlusPlus.xmod.bartcrops.crops;

import gtPlusPlus.core.material.ELEMENT.STANDALONE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.bartcrops.abstracts.BaseAestheticCrop;
import ic2.api.crops.ICropTile;
import net.minecraft.item.ItemStack;

public class Crop_Force extends BaseAestheticCrop {
	
	public int tier() {
		return 4;
	}

	public String name() {
		return "Force";
	}

	public String discoveredBy() {
		return "Alkalus";
	}

	public int growthDuration(ICropTile crop) {
		int ret = 800;
		
		/*if (crop.isBlockBelow(Blocks.dirt) || crop.isBlockBelow(Blocks.flowing_water)) {
			ret = 225;
		}*/

		if (CORE_Preloader.DEBUG_MODE) {
			ret = 1;
		}

		return ret;
	}

	public String[] attributes() {
		return new String[]{"Power", "Soil", "Yellow", "Gold"};
	}

	public ItemStack getGain(ICropTile crop) {		
		ItemStack ret = this.getDisplayItem();
		if (MathUtils.randInt(0, 10) > 8) {
			ret = STANDALONE.FORCE.getNugget(MathUtils.randInt(4, 8));
		}
		return ret;
	}

	public ItemStack getDisplayItem() {
		return STANDALONE.FORCE.getNugget(0);
	}
}