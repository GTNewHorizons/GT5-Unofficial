package kubatech.loaders;

import static gregtech.api.enums.Mods.ThaumicBases;

import kubatech.api.enums.EIGModes;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGFlowerBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGRainbowCactusBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGSeedBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGStemBucket;

public class EIGBucketLoader {

    public static void LoadEIGBuckets() {

        // Regular Mode Buckets
        if (ThaumicBases.isModLoaded()) {
            EIGModes.Normal.addLowPriorityFactory(EIGRainbowCactusBucket.factory);
        }
        EIGModes.Normal.addLowPriorityFactory(EIGFlowerBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGStemBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGSeedBucket.factory);
    }

}
