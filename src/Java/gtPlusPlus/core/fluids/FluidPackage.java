package gtPlusPlus.core.fluids;

import static gtPlusPlus.core.fluids.FluidFactory.*;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class FluidPackage {


	public final int mID;
	public final String mName;
	private final Fluid mFluid;
	public final ItemStack mBucket;
	public final Block mBlock;

	public FluidPackage(int aID, String aName, Fluid aFluid, ItemStack aBucket, Block aBlock) {

		mNameToFluidMap.put(aName, aFluid);
		mNameToBucketMap.put(aName, aBucket);
		mNameToBlockMap.put(aName, aBlock);
		mNameToMetaMap.put(aName, aID);

		mFluidToNameMap.put(aFluid, aName);
		mFluidToBucketMap.put(aFluid, aBucket);
		mFluidToBlockMap.put(aFluid, aBlock);
		mFluidToMetaMap.put(aFluid, aID);

		mBucketToFluidMap.put(aBucket, aFluid);
		mBucketToNameMap.put(aBucket, aName);
		mBucketToBlockMap.put(aBucket, aBlock);
		mBucketToMetaMap.put(aBucket, aID);

		mBlockToNameMap.put(aBlock, aName);
		mBlockToFluidMap.put(aBlock, aFluid);
		mBlockToBucketMap.put(aBlock, aBucket);
		mBlockToMetaMap.put(aBlock, aID);	

		mMetaToNameMap.put(aID, aName);
		mMetaToFluidMap.put(aID, aFluid);
		mMetaToBucketMap.put(aID, aBucket);
		mMetaToBlockMap.put(aID, aBlock);
		
		
		mID = aID;
		mName = aName;
		mFluid = aFluid;
		mBucket = aBucket;
		mBlock = aBlock;

	}
	
	public Fluid get() {
		return mFluid;
	}
	
	public boolean valid() {
		return mName != null && mName.length() > 0 && mFluid != null && mBucket != null && mBlock != null;
	}

}
