package gtPlusPlus.xmod.thermalfoundation.item;

import net.minecraftforge.fluids.FluidContainerRegistry;

import cofh.core.item.ItemBase;
import cofh.core.item.ItemBucket;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.core.util.fluid.BucketHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.block.TFBlocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class TFItems {

    public static void preInit() {
        ItemBucket bucket = (ItemBucket) new ItemBucket("MiscUtils").setUnlocalizedName("bucket")
            .setCreativeTab(AddToCreativeTab.tabMisc);

        GregtechItemList.PyrotheumBucket.set(bucket.addOreDictItem(1, "bucketPyrotheum"));
        GregtechItemList.CryotheumBucket.set(bucket.addOreDictItem(2, "bucketCryotheum"));
        GregtechItemList.EnderBucket.set(bucket.addOreDictItem(3, "bucketEnder", 1));

        ItemBase material = (ItemBase) new ItemBase("MiscUtils").setUnlocalizedName("material")
            .setCreativeTab(AddToCreativeTab.tabMisc);

        GregtechItemList.BlizzRod.set(material.addOreDictItem(1, "rodBlizz"));
        GregtechItemList.BlizzPowder.set(material.addOreDictItem(2, "dustBlizz"));
        GregtechItemList.PyrotheumDust.set(material.addOreDictItem(3, "dustPyrotheum"));
        GregtechItemList.CryotheumDust.set(material.addOreDictItem(4, "dustCryotheum"));

        FurnaceFuelHandler.registerFuel(GregtechItemList.PyrotheumDust.get(1), 2400);

        GTOreDictUnificator.registerOre("stickBlizz", GregtechItemList.BlizzRod.get(1));
    }

    public static void init() {
        BucketHandler.registerBucket(TFBlocks.blockFluidPyrotheum, 0, GregtechItemList.PyrotheumBucket.get(1));
        BucketHandler.registerBucket(TFBlocks.blockFluidCryotheum, 0, GregtechItemList.CryotheumBucket.get(1));
        BucketHandler.registerBucket(TFBlocks.blockFluidEnder, 0, GregtechItemList.EnderBucket.get(1));
        FluidContainerRegistry.registerFluidContainer(
            TFFluids.fluidPyrotheum,
            GregtechItemList.PyrotheumBucket.get(1),
            FluidContainerRegistry.EMPTY_BUCKET);
        FluidContainerRegistry.registerFluidContainer(
            TFFluids.fluidCryotheum,
            GregtechItemList.CryotheumBucket.get(1),
            FluidContainerRegistry.EMPTY_BUCKET);
        FluidContainerRegistry.registerFluidContainer(
            TFFluids.fluidEnder,
            GregtechItemList.EnderBucket.get(1),
            FluidContainerRegistry.EMPTY_BUCKET);
    }
}
