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
        return switch (n) {
            case 0 -> 0;
            case 1 -> 0;
            case 2 -> 0;
            case 3 -> 4;
            case 4 -> 0;
            default -> 0;
        };
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
