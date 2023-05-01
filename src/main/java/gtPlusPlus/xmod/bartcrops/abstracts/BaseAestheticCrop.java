package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.preloader.CORE_Preloader;
import ic2.api.crops.ICropTile;

public abstract class BaseAestheticCrop extends BaseHarvestableCrop {

    @Override
    public int tier() {
        return 1;
    }

    @Override
    public int stat(int n) {
        switch (n) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 4;
            case 4:
                return 0;
            default:
                return 0;
        }
    }

    @Override
    public int growthDuration(ICropTile crop) {
        return CORE_Preloader.DEBUG_MODE ? 1 : 225;
    }

    @Override
    public byte getSizeAfterHarvest(ICropTile crop) {
        return 1;
    }
}
