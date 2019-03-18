package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.core.lib.CORE;
import ic2.api.crops.ICropTile;

public abstract class BaseHarvestableCrop extends BaseCrop {
	
	public int tier() {
		return 2;
	}

	public int stat(int n) {
		switch (n) {
			case 0 :
				return 0;
			case 1 :
				return 4;
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

	public boolean canGrow(ICropTile crop) {
		return crop.getSize() < 3;
	}

	public int getOptimalHavestSize(ICropTile crop) {
		return 3;
	}

	public boolean canBeHarvested(ICropTile crop) {
		return crop.getSize() == 3;
	}

	public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air) {
		return (int) ((double) humidity * 1.2D + (double) nutrients * 0.9D + (double) air * 0.9D);
	}

	public int growthDuration(ICropTile crop) {
		short r;
		if (CORE.DEBUG) {
			r = 1;
		} else if (crop.getSize() == 2) {
			r = 200;
		} else {
			r = 700;
		}

		return r;
	}

	public byte getSizeAfterHarvest(ICropTile crop) {
		return 2;
	}

	public int maxSize() {
		return 3;
	}

	public String discoveredBy() {
		return "Alkalus";
	}
}