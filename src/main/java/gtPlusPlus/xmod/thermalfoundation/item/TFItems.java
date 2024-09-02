package gtPlusPlus.xmod.thermalfoundation.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

import cofh.core.item.ItemBase;
import cofh.core.item.ItemBucket;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.core.util.fluid.BucketHandler;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.thermalfoundation.block.TFBlocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class TFItems {

    public static ItemBase itemMaterial;
    public static ItemStack rodBlizz;
    public static ItemStack dustBlizz;
    public static ItemStack dustPyrotheum;
    public static ItemStack dustCryotheum;
    public static ItemBucket itemBucket;
    public static ItemStack bucketPyrotheum;
    public static ItemStack bucketCryotheum;
    public static ItemStack bucketEnder;

    public static ItemStack itemDustBlizz;
    public static ItemStack itemDustPyrotheum;
    public static ItemStack itemDustCryotheum;
    public static ItemStack itemRodBlizz;

    public static void preInit() {

        itemBucket = (ItemBucket) new ItemBucket("MiscUtils").setUnlocalizedName("bucket")
            .setCreativeTab(AddToCreativeTab.tabMisc);
        itemMaterial = (ItemBase) new ItemBase("MiscUtils").setUnlocalizedName("material")
            .setCreativeTab(AddToCreativeTab.tabMisc);

        bucketPyrotheum = itemBucket.addOreDictItem(1, "bucketPyrotheum");
        bucketCryotheum = itemBucket.addOreDictItem(2, "bucketCryotheum");
        bucketEnder = itemBucket.addOreDictItem(3, "bucketEnder", 1);
        rodBlizz = itemMaterial.addOreDictItem(1, "rodBlizz");
        dustBlizz = itemMaterial.addOreDictItem(2, "dustBlizz");
        dustPyrotheum = itemMaterial.addOreDictItem(3, "dustPyrotheum");
        dustCryotheum = itemMaterial.addOreDictItem(4, "dustCryotheum");

        if (ReflectionUtils.doesClassExist("cofh.core.util.energy.FurnaceFuelHandler")) {
            FurnaceFuelHandler.registerFuel(dustPyrotheum, 2400); // cofh.core.util.energy.FurnaceFuelHandler.registerFuel(ItemStack,
                                                                  // int)
        }

        ItemUtils.addItemToOreDictionary(rodBlizz, "stickBlizz");

        itemRodBlizz = ItemUtils.simpleMetaStack(itemMaterial, 1, 1);
        itemDustBlizz = ItemUtils.simpleMetaStack(itemMaterial, 2, 1);
        itemDustPyrotheum = ItemUtils.simpleMetaStack(itemMaterial, 3, 1);
        itemDustCryotheum = ItemUtils.simpleMetaStack(itemMaterial, 4, 1);
    }

    public static void init() {

        BucketHandler.registerBucket(TFBlocks.blockFluidPyrotheum, 0, bucketPyrotheum);
        BucketHandler.registerBucket(TFBlocks.blockFluidCryotheum, 0, bucketCryotheum);
        BucketHandler.registerBucket(TFBlocks.blockFluidEnder, 0, bucketEnder);
        FluidContainerRegistry
            .registerFluidContainer(TFFluids.fluidPyrotheum, bucketPyrotheum, FluidContainerRegistry.EMPTY_BUCKET);
        FluidContainerRegistry
            .registerFluidContainer(TFFluids.fluidCryotheum, bucketCryotheum, FluidContainerRegistry.EMPTY_BUCKET);
        FluidContainerRegistry
            .registerFluidContainer(TFFluids.fluidEnder, bucketEnder, FluidContainerRegistry.EMPTY_BUCKET);
    }

    public static void postInit() {}
}
