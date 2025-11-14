package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.core.config.ASMConfiguration;
import ic2.api.crops.ICropTile;

public abstract class BaseAestheticCrop extends BaseHarvestableCrop {

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public int stat(int n) {
        if (n == 3) return 4;
        return 0;
    }

    @Override
    public int growthDuration(ICropTile crop) {
        return ASMConfiguration.debug.debugMode ? 1 : 225;
    }

    @Override
    public byte getSizeAfterHarvest(ICropTile crop) {
        return 1;
    }
}
