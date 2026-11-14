package kubatech.loaders;

import static gregtech.api.enums.Mods.ThaumicBases;

import kubatech.api.eig.EIGBucketFactoryRegistry;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGFlowerBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGRainbowCactusBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGSeedBucket;
import kubatech.tileentity.gregtech.multiblock.eigbuckets.EIGStemBucket;

public class EIGBucketLoader {

    public static void LoadEIGBuckets() {
        if (ThaumicBases.isModLoaded()) {
            EIGBucketFactoryRegistry.addLowPriorityFactory(EIGRainbowCactusBucket.factory);
        }
        EIGBucketFactoryRegistry.addLowPriorityFactory(EIGFlowerBucket.factory);
        EIGBucketFactoryRegistry.addLowPriorityFactory(EIGStemBucket.factory);
        EIGBucketFactoryRegistry.addLowPriorityFactory(EIGSeedBucket.factory);
    }

}
