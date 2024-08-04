package goodgenerator.crossmod.ic2;

import ic2.api.crops.Crops;

public class CropsLoader {

    public static void registerCrops() {
        Crops.instance.registerCrop(new GGCropsSaltyRoot("saltroot"));
    }
}
