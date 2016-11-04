package gtPlusPlus.xmod.thermalfoundation.item;

import cofh.core.item.ItemBase;
import cofh.core.item.ItemBucket;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.core.util.fluid.BucketHandler;
import cofh.lib.util.helpers.ItemHelper;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.thermalfoundation.block.TF_Blocks;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TF_Items {

	public static ItemBase		itemMaterial;
	public static ItemStack		rodBlizz;
	public static ItemStack		dustBlizz;
	public static ItemStack		dustPyrotheum;
	public static ItemStack		dustCryotheum;
	public static ItemBucket	itemBucket;
	public static ItemStack		bucketPyrotheum;
	public static ItemStack		bucketCryotheum;

	public static ItemStack		itemDustBlizz;
	public static ItemStack		itemDustPyrotheum;
	public static ItemStack		itemDustCryotheum;
	public static ItemStack		itemRodBlizz;

	public static void init() {

		BucketHandler.registerBucket(TF_Blocks.blockFluidPyrotheum, 0, TF_Items.bucketPyrotheum);
		BucketHandler.registerBucket(TF_Blocks.blockFluidCryotheum, 0, TF_Items.bucketCryotheum);
		FluidContainerRegistry.registerFluidContainer(TF_Fluids.fluidPyrotheum, TF_Items.bucketPyrotheum,
				FluidContainerRegistry.EMPTY_BUCKET);
		FluidContainerRegistry.registerFluidContainer(TF_Fluids.fluidCryotheum, TF_Items.bucketCryotheum,
				FluidContainerRegistry.EMPTY_BUCKET);

	}

	public static void postInit() {

		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(TF_Items.dustPyrotheum, 1), new Object[] {
				"dustCoal", "dustSulfur", "dustRedstone", "dustBlaze"
		}));
		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(TF_Items.dustCryotheum, 1), new Object[] {
				Items.snowball, "dustSaltpeter", "dustRedstone", "dustBlizz"
		}));
		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(TF_Items.dustCryotheum, 1), new Object[] {
				Items.snowball, "dustNitor", "dustRedstone", "dustBlizz"
		}));
		// ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(dustBlizz,
		// 2), new Object[] { "rodBlizz" }));

	}

	public static void preInit() {

		TF_Items.itemBucket = (ItemBucket) new ItemBucket("MiscUtils").setUnlocalizedName("bucket")
				.setCreativeTab(AddToCreativeTab.tabMisc);
		TF_Items.itemMaterial = (ItemBase) new ItemBase("MiscUtils").setUnlocalizedName("material")
				.setCreativeTab(AddToCreativeTab.tabMisc);

		TF_Items.bucketPyrotheum = TF_Items.itemBucket.addOreDictItem(1, "bucketPyrotheum");
		TF_Items.bucketCryotheum = TF_Items.itemBucket.addOreDictItem(2, "bucketCryotheum");
		TF_Items.rodBlizz = TF_Items.itemMaterial.addOreDictItem(1, "rodBlizz");
		TF_Items.dustBlizz = TF_Items.itemMaterial.addOreDictItem(2, "dustBlizz");
		TF_Items.dustPyrotheum = TF_Items.itemMaterial.addOreDictItem(3, "dustPyrotheum");
		TF_Items.dustCryotheum = TF_Items.itemMaterial.addOreDictItem(4, "dustCryotheum");

		FurnaceFuelHandler.registerFuel(TF_Items.dustPyrotheum, 2400);

		TF_Items.itemRodBlizz = ItemUtils.simpleMetaStack(TF_Items.itemMaterial, 1, 1);
		TF_Items.itemDustBlizz = ItemUtils.simpleMetaStack(TF_Items.itemMaterial, 2, 1);
		TF_Items.itemDustPyrotheum = ItemUtils.simpleMetaStack(TF_Items.itemMaterial, 3, 1);
		TF_Items.itemDustCryotheum = ItemUtils.simpleMetaStack(TF_Items.itemMaterial, 4, 1);

	}

}
