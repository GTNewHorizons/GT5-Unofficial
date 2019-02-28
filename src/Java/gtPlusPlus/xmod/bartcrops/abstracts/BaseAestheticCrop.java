package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.core.lib.CORE;
import ic2.api.crops.ICropTile;

public abstract class BaseAestheticCrop extends BaseHarvestableCrop {
	
	public int tier() {
		return 1;
	}

	public int stat(int n) {
		switch (n) {
			case 0 :
				return 0;
			case 1 :
				return 0;
			case 2 :
				return 0;
			case 3 :
				return 4;
			case 4 :
				return 0;
			default :
				return 0;
		}
	}

	public int growthDuration(ICropTile crop) {
		return CORE.DEBUG ? 1 : 225;
	}

	public byte getSizeAfterHarvest(ICropTile crop) {
		return 1;
	}
	
}