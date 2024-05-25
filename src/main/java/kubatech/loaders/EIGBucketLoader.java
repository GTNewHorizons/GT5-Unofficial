package kubatech.loaders;

import kubatech.api.LoaderReference;
import kubatech.api.enums.EIGModes;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGFlowerBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGIC2Bucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGRainbowCactusBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGSeedBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGStemBucket;

public class EIGBucketLoader {

    public static void LoadEIGBuckets() {
        // IC2 buckets
        EIGModes.IC2.addLowPriorityFactory(EIGIC2Bucket.factory);

        // Regular Mode Buckets
        if (LoaderReference.ThaumicBases) {
            EIGModes.Normal.addLowPriorityFactory(EIGRainbowCactusBucket.factory);
        }
        EIGModes.Normal.addLowPriorityFactory(EIGFlowerBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGStemBucket.factory);
        EIGModes.Normal.addLowPriorityFactory(EIGSeedBucket.factory);
    }

}
