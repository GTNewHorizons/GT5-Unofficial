package miscutil.core.intermod.thermalfoundation.item;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.intermod.thermalfoundation.block.TF_Blocks;
import miscutil.core.intermod.thermalfoundation.fluid.TF_Fluids;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cofh.core.item.ItemBase;
import cofh.core.item.ItemBucket;
import cofh.core.util.energy.FurnaceFuelHandler;
import cofh.core.util.fluid.BucketHandler;
import cofh.lib.util.helpers.ItemHelper;

public class TF_Items {

	public static ItemBase itemMaterial;
	public static ItemStack rodBlizz;
	public static ItemStack dustBlizz;
	public static ItemStack dustPyrotheum;
	public static ItemStack dustCryotheum;
	public static ItemBucket itemBucket;
	public static ItemStack bucketPyrotheum;
	public static ItemStack bucketCryotheum;
	
	public static ItemStack itemDustBlizz;
	public static ItemStack itemDustPyrotheum;
	public static ItemStack itemDustCryotheum;
	public static ItemStack itemRodBlizz;

	public static void preInit(){


		itemBucket = (ItemBucket)new ItemBucket("MiscUtils").setUnlocalizedName("bucket").setCreativeTab(AddToCreativeTab.tabMisc);
	    itemMaterial = (ItemBase)new ItemBase("MiscUtils").setUnlocalizedName("material").setCreativeTab(AddToCreativeTab.tabMisc);
	    

		bucketPyrotheum = itemBucket.addOreDictItem(1, "bucketPyrotheum");
		bucketCryotheum = itemBucket.addOreDictItem(2, "bucketCryotheum");
		rodBlizz = itemMaterial.addOreDictItem(1, "rodBlizz");
		dustBlizz = itemMaterial.addOreDictItem(2, "dustBlizz");
		dustPyrotheum = itemMaterial.addOreDictItem(3, "dustPyrotheum");
		dustCryotheum = itemMaterial.addOreDictItem(4, "dustCryotheum");
		
		FurnaceFuelHandler.registerFuel(dustPyrotheum, 2400);


		itemRodBlizz = UtilsItems.simpleMetaStack(itemMaterial, 1, 1);
		itemDustBlizz = UtilsItems.simpleMetaStack(itemMaterial, 2, 1);
		itemDustPyrotheum = UtilsItems.simpleMetaStack(itemMaterial, 3, 1);
		itemDustCryotheum = UtilsItems.simpleMetaStack(itemMaterial, 4, 1);



	}

	public static void init(){

		BucketHandler.registerBucket(TF_Blocks.blockFluidPyrotheum, 0, bucketPyrotheum);
		BucketHandler.registerBucket(TF_Blocks.blockFluidCryotheum, 0, bucketCryotheum);
		FluidContainerRegistry.registerFluidContainer(TF_Fluids.fluidPyrotheum, bucketPyrotheum, FluidContainerRegistry.EMPTY_BUCKET);
		FluidContainerRegistry.registerFluidContainer(TF_Fluids.fluidCryotheum, bucketCryotheum, FluidContainerRegistry.EMPTY_BUCKET);


	}

	public static void postInit(){

		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(dustPyrotheum, 1), new Object[] { "dustCoal", "dustSulfur", "dustRedstone", "dustBlaze" }));
		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(dustCryotheum, 1), new Object[] { Items.snowball, "dustSaltpeter", "dustRedstone", "dustBlizz" }));
		ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(dustCryotheum, 1), new Object[] { Items.snowball, "dustNitor", "dustRedstone", "dustBlizz" }));
		//ItemHelper.addRecipe(ItemHelper.ShapelessRecipe(ItemHelper.cloneStack(dustBlizz, 2), new Object[] { "rodBlizz" }));



	}

}
