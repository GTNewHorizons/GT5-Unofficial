package kubatech.loaders;

import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.ThaumicTinkerer;

import kubatech.api.enums.EIGModes;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGFlowerBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGIC2Bucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGInfusedSeedBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGRainbowCactusBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGSeedBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGStemBucket;

public class EIGBucketLoader {

    public static void LoadEIGBuckets() {
        // IC2 buckets
        EIGModes.IC2.addLowPriorityFactory(EIGIC2Bucket.factory);

        // Regular Mode Buckets
        if (ThaumicTinkerer.isModLoaded()) {
            EIGModes.Normal.addLowPriorityFactory(EIGInfusedSeedBucket.factory);
        }
        if (ThaumicBases.isModLoaded()) {
            EIGModes.Normal.addLowPriorityFactory(EIGRainbowCactusBucket.factory);
        }
        EIGModes.Normal.addLowPriorityFactory(EIGFlowerBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGStemBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGSeedBucket.factory);
    }

}
