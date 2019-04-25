package gtPlusPlus.api.objects.minecraft;

import java.util.HashMap;

import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class FakeWorld implements IBlockAccess {

	public static HashMap<String, FakeBlockPos> mStaticFakeWorldData;

	public HashMap<String, FakeBlockPos> mFakeWorldData = new HashMap<String, FakeBlockPos>();
	
	/**
	 * Must be an odd number
	 */
	private int aWorldSize = 99;
	private final int aDistanceFromOrigin;
	private final int aWorldHeight;
	
	public FakeWorld() {
		this(99);
	}
	
	public FakeWorld(int aSize) {
		if (MathUtils.isNumberEven(aSize)) {
			aSize++;
		}
		aWorldSize = aSize;
		aDistanceFromOrigin = ((aWorldSize-1)/2);
		aWorldHeight = aDistanceFromOrigin >= 255 ? 255 : aDistanceFromOrigin;
		Logger.INFO("Created a Fake World with data:");
		Logger.INFO("World Size: "+aWorldSize);
		Logger.INFO("Distance from Origin in each dir: "+aDistanceFromOrigin);
		Logger.INFO("World Height: "+aWorldHeight);
		init();
	}
	
	public void init() {
		
		/*if (mStaticFakeWorldData == null) {
			Logger.INFO("Setting all Blocks in Fake World to Air.");
			mStaticFakeWorldData = new HashMap<String, FakeBlockPos>();
			for (int y=0;y<=aWorldHeight;y++) {
				for (int x=-aDistanceFromOrigin;x<=aDistanceFromOrigin;x++) {
					for (int z=-aDistanceFromOrigin;z<=aDistanceFromOrigin;z++) {
						FakeBlockPos aTempPos = new FakeBlockPos(x, y, z, Blocks.air, 0);
						mStaticFakeWorldData.put(aTempPos.getLocationString(), aTempPos);
					}
				}
			}
		}*/
		//if (mStaticFakeWorldData != null) {
			//Logger.INFO(" Instancing static air world.");
			mFakeWorldData = new HashMap<String, FakeBlockPos>();
			//mFakeWorldData = (HashMap<String, FakeBlockPos>) mStaticFakeWorldData.clone();			
		//}
		
		
		//Logger.INFO("Initialisation of FakeWorld is now complete.");
	}
	
	public FakeBlockPos getBlockAtCoords(int x, int y, int z) {
		String S = ""+x+"@"+y+"@"+z;
		FakeBlockPos aBlock = mFakeWorldData.get(S);
		if (aBlock == null) {
			return new FakeBlockPos(x, y, z, Blocks.air, 0);
		}
		return aBlock;		
	}
	
	public void setBlockAtCoords(int x, int y, int z, Block aBlock, int aMeta) {
		FakeBlockPos aTempPos = new FakeBlockPos(x, y, z, aBlock, aMeta);
		Logger.INFO("Setting "+x+", "+y+", "+z+" to "+aBlock.getLocalizedName()+":"+aMeta);
		mFakeWorldData.put(aTempPos.getLocationString(), aTempPos);		
	}
	
	public AutoMap<ItemStack> getAllBlocksStoredInFakeWorld(){
		AutoMap<ItemStack> aOutput = new AutoMap<ItemStack>();		
		for (FakeBlockPos aPos : mFakeWorldData.values()) {
			if (aPos == null || aPos.getBlockAtPos() == Blocks.air) {
				continue;
			}
			else {
				ItemStack aTempStack = ItemUtils.simpleMetaStack(aPos.getBlockAtPos(), aPos.getMetaAtPos(), 1);
				if (ItemUtils.checkForInvalidItems(aTempStack)) {
					//Logger.INFO("Output: "+aTempStack.getDisplayName());
					aOutput.put(aTempStack);
				}
			}
		}		
		return aOutput;
	}

	public Block getBlock(int x, int y, int z) {
		FakeBlockPos aPos = getBlockAtCoords(x, y, z);
		return aPos.getBlockAtPos();
	}

	public boolean isAirBlock(int x, int y, int z) {
		Block aBlock = getBlock(x, y, z);
		return aBlock == Blocks.air || aBlock.getMaterial() == Material.air;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_) {
		return null;
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_) {
		return 0;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		return getBlockAtCoords(x, y, z).getMetaAtPos();
	}

	@Override
	public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_) {
		return 0;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_) {
		return BiomeGenBase.plains;
	}

	@Override
	public int getHeight() {
		return aWorldHeight;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return false;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
		if (!isAirBlock(x, y, z)) {
			return true;
		}
		return false;
	}
	
	
	
}
