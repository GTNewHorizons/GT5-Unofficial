package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.preloader.CORE_Preloader;
import ic2.api.crops.ICropTile;

public abstract class BaseHarvestableCrop extends BaseCrop {

    @Override
    public int tier() {
        return 2;
    }

    @Override
    public int stat(int n) {
        return switch (n) {
            case 0 -> 0;
            case 1 -> 4;
            case 2 -> 0;
            case 3 -> 4;
            case 4 -> 0;
            default -> 0;
        };
    }

    @Override
    public boolean canGrow(ICropTile crop) {
        return crop.getSize() < 3;
    }

    @Override
    public int getOptimalHavestSize(ICropTile crop) {
        return 3;
    }

    @Override
    public boolean canBeHarvested(ICropTile crop) {
        return crop.getSize() == 3;
    }

    @Override
    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air) {
        return (int) ((double) humidity * 1.2D + (double) nutrients * 0.9D + (double) air * 0.9D);
    }

    @Override
    public int growthDuration(ICropTile crop) {
        short r;
        if (CORE_Preloader.DEBUG_MODE) {
            r = 1;
        } else if (crop.getSize() == 2) {
            r = 200;
        } else {
            r = 700;
        }

        return r;
    }

    @Override
    public byte getSizeAfterHarvest(ICropTile crop) {
        return 2;
    }

    @Override
    public int maxSize() {
        return 3;
    }

    @Override
    public String discoveredBy() {
        return "Alkalus";
    }
}
