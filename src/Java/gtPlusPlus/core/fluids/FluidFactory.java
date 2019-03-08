package gtPlusPlus.core.fluids;

import java.util.LinkedHashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.GregtechException;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.base.itemblock.FluidItemBlock;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidFactory {

	public static final Map<String, Fluid> mNameToFluidMap = new LinkedHashMap<String, Fluid>();
	public static final Map<String, ItemStack> mNameToBucketMap = new LinkedHashMap<String, ItemStack>();
	public static final Map<String, Block> mNameToBlockMap = new LinkedHashMap<String, Block>();
	public static final Map<String, Integer> mNameToMetaMap = new LinkedHashMap<String, Integer>();

	public static final Map<Fluid, String> mFluidToNameMap = new LinkedHashMap<Fluid, String>();
	public static final Map<Fluid, ItemStack> mFluidToBucketMap = new LinkedHashMap<Fluid, ItemStack>();
	public static final Map<Fluid, Block> mFluidToBlockMap = new LinkedHashMap<Fluid, Block>();
	public static final Map<Fluid, Integer> mFluidToMetaMap = new LinkedHashMap<Fluid, Integer>();

	public static final Map<ItemStack, Fluid> mBucketToFluidMap = new LinkedHashMap<ItemStack, Fluid>();
	public static final Map<ItemStack, String> mBucketToNameMap = new LinkedHashMap<ItemStack, String>();
	public static final Map<ItemStack, Block> mBucketToBlockMap = new LinkedHashMap<ItemStack, Block>();
	public static final Map<ItemStack, Integer> mBucketToMetaMap = new LinkedHashMap<ItemStack, Integer>();

	public static final Map<Block, String> mBlockToNameMap = new LinkedHashMap<Block, String>();
	public static final Map<Block, Fluid> mBlockToFluidMap = new LinkedHashMap<Block, Fluid>();
	public static final Map<Block, ItemStack> mBlockToBucketMap = new LinkedHashMap<Block, ItemStack>();
	public static final Map<Block, Integer> mBlockToMetaMap = new LinkedHashMap<Block, Integer>();

	public static final Map<Integer, String> mMetaToNameMap = new LinkedHashMap<Integer, String>();
	public static final Map<Integer, Fluid> mMetaToFluidMap = new LinkedHashMap<Integer, Fluid>();
	public static final Map<Integer, ItemStack> mMetaToBucketMap = new LinkedHashMap<Integer, ItemStack>();
	public static final Map<Integer, Block> mMetaToBlockMap = new LinkedHashMap<Integer, Block>();
	
	
	//Special Colour Handling
	public static final Map<Integer, Integer> mMetaToColourMap = new LinkedHashMap<Integer, Integer>();


	public static Item mGenericBucket;
	private static FluidPackage mErrorFluid;
	private static AutoMap<FluidPackage> mGeneratedFluids = new AutoMap<FluidPackage>();

	public static void preInit() {

	}

	public static void init() {		
		mGenericBucket = new ItemGenericFluidBucket(Blocks.air);		
		GameRegistry.registerItem(mGenericBucket, "gtpp.bucket.generic");
		for (FluidPackage y : mGeneratedFluids) {			
			if (!y.valid()) {
				continue;
			}			
			GameRegistry.registerBlock(y.mBlock, FluidItemBlock.class, "gtpp_" + y.mName);
			FluidContainerRegistry.registerFluidContainer(y.get(), y.mBucket, new ItemStack(Items.bucket));
		}
		Utils.registerEvent(BucketHandler.INSTANCE);
	}

	public static void postInit() {

	}

	/**
	 * Generates a 'Water' type fluid.
	 * @param aID - The Fluid ID (Must be unique)
	 * @param aUnlocalName - Unlocalized Fluid Name
	 * @param aRGB - a {@link Short[]} containing the RGB of the FluidPackage.
	 * @return - A fully constructed & registered {@linkplain FluidPackage}
	 */
	public static FluidPackage generate(int aID, String aUnlocalName, short[] aRGB) {
		return generate(aID, aUnlocalName, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE, aRGB);
	}
	
	/**
	 * Generate a {@link FluidPackage} from the data provided. This FluidPackage is automatically registered and handled internally.
	 * Pass in {@link Short}.MIN_VALUE for any of the Fluid Fields (Besides ID, Name or RGB) and it will default to water values.
	 * @param aID - The Fluid ID (Must be unique)
	 * @param aUnlocalName - Unlocalized Fluid Name
	 * @param luminosity - How bright is the fluid.
	 * @param density - completely arbitrary; negative density indicates that the fluid is
     * lighter than air. Default value is approximately the real-life density of water in kg/m^3.
	 * @param temp - completely arbitrary; higher temperature indicates that the fluid is
     * hotter than air. Default value is approximately the real-life room temperature of water in degrees Kelvin
	 * @param viscosity - completely arbitrary; negative values are not
     * permissible. Default value is approximately the real-life density of water in m/s^2 (x10^-3).     *
     * Higher viscosity means that a fluid flows more slowly, like molasses.
     * Lower viscosity means that a fluid flows more quickly, like helium.
	 * @param aRGB - a {@link Short[]} containing the RGB of the FluidPackage.
	 * @return - A fully constructed & registered {@linkplain FluidPackage}
	 */
	public static FluidPackage generate(int aID, String aUnlocalName, int luminosity, int density, int temp,
			int viscosity, short[] aRGB) {

		FluidPackage aFluidToGenerate = null;

		// Check Load Phase for some Safety, only allow this to be called in Pre-Init.
		if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.PRE_INIT) {
			try {
				throw new GregtechException("Cannot generate Fluid Packages outside of Pre-Init!");
			} catch (GregtechException e) {
				System.exit(0);
			}
		}
		
		Fluid aGenFluid = fluid(aUnlocalName, luminosity, density, temp, viscosity, aRGB);
		Block aGenBlock = block(aGenFluid, aRGB);
		ItemStack aGenBucket = bucket(aID);

		aFluidToGenerate = new FluidPackage(aID, aUnlocalName, aGenFluid, aGenBucket, aGenBlock);

		if (aFluidToGenerate != null && aFluidToGenerate.valid()) {
			FluidRegistry.registerFluid(aFluidToGenerate.get());	
			mGeneratedFluids.put(aFluidToGenerate);		
		}
		else {
			// Handle Bad generation
			if (mErrorFluid == null) {
				mErrorFluid = new FluidPackage(0, "", FluidUtils.getWater(1).getFluid(), ItemUtils.getSimpleStack(Items.water_bucket), Blocks.water);
			}
			return mErrorFluid;
		}
		

		return aFluidToGenerate;
	}
	
	
	
	private static Fluid fluid(String aUnlocalName, int luminosity, int density, int temp,
			int viscosity, short[] aRGB) {
		return new FactoryFluid(aUnlocalName, luminosity, density, temp, viscosity, aRGB);
	}
	
	private static ItemStack bucket(int aID) {
		return ItemGenericFluidBucket.registerFluidForBucket(aID);
	}
	
	private static Block block(Fluid aFluidForBlock, short[] aRGB) {
		if (aFluidForBlock != null) {
			FluidRegistry.registerFluid(aFluidForBlock);
			return new BlockFluidBase(aFluidForBlock, aRGB);
		}
		return Blocks.dirt;	
	}

	/**
	 * Copyright © SpaceToad, 2011 http://www.mod-buildcraft.com BuildCraft is
	 * distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
	 * Please check the contents of the license located in
	 * http://www.mod-buildcraft.com/MMPL-1.0.txt
	 * 
	 * Modified version of the BC BucketHandler, except using ItemStacks > Items
	 * (Why?)
	 * 
	 * @author Alkalus
	 */

	public static class BucketHandler {

		public static BucketHandler INSTANCE = new BucketHandler();

		private BucketHandler() {

		}

		@SubscribeEvent
		public void onBucketFill(FillBucketEvent event) {
			ItemStack result = fillCustomBucket(event.world, event.target);
			if (result == null) {
				return;
			}
			event.result = result;
			event.setResult(Result.ALLOW);
		}

		private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
			Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			ItemStack bucket = mBlockToBucketMap.get(block);
			if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
				world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
				return ItemUtils.getSimpleStack(bucket, 1);
			} else {
				return null;
			}
		}

	}

}
