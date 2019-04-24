package gtPlusPlus.xmod.gregtech.common.helpers.treefarm;

import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.FakeBlockPos;
import gtPlusPlus.api.objects.minecraft.FakeWorld;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenerator {

	private final FakeTreeInFakeWorldGenerator mTreeData;
	
	public TreeGenerator() {
		Logger.INFO("Created Fake Tree Generator.");
		mTreeData = new FakeTreeInFakeWorldGenerator();		
		if (!mTreeData.hasGenerated) {
			mTreeData.generate(null, CORE.RANDOM, 0, 0, 0);
    	}		
	}
	
	public AutoMap<ItemStack> generateOutput(int aTreeSize){
		AutoMap<ItemStack> aTemp = new AutoMap<ItemStack>();
		AutoMap<ItemStack> aOutputMap = mTreeData.getOutputFromTree();
		if (aOutputMap != null && aOutputMap.size() > 0) {
			return aOutputMap;
		}		
		return aTemp;
	}
	
	public class FakeTreeInFakeWorldGenerator extends WorldGenAbstractTree
	{
	    /** The minimum height of a generated tree. */
	    private final int minTreeHeight;
	    /** True if this tree should grow Vines. */
	    private final boolean vinesGrow;
	    /** The metadata value of the wood to use in tree generation. */
	    private final int metaWood;
	    /** The metadata value of the leaves to use in tree generation. */
	    private final int metaLeaves;
	    
	    private final AutoMap<FakeWorld> mFakeWorld;
	    private final int mTreesToGenerate;
	    
	    private int mCurrentGeneratorIteration = 0;
	    
	    private boolean hasGenerated = false;
	    private AutoMap<ItemStack> aOutputsFromGenerator = new AutoMap<ItemStack>();

	    public FakeTreeInFakeWorldGenerator()
	    {
	        this(4, 0, 0, false, 5000);
	    }

	    public FakeTreeInFakeWorldGenerator(int aMinHeight, int aWoodMeta, int aLeafMeta, boolean aVines, int aTreeCount)
	    {
	        super(false);
	        this.minTreeHeight = aMinHeight;
	        this.metaWood = aWoodMeta;
	        this.metaLeaves = aLeafMeta;
	        this.vinesGrow = aVines;
	        this.mFakeWorld = new AutoMap<FakeWorld>();
	        this.mTreesToGenerate = aTreeCount;
			Logger.INFO("Created Fake Tree In Fake World Instance.");
	    }

	    
	    public AutoMap<ItemStack> getOutputFromTree(){
	    	if (!hasGenerated) {
	    		generate(null, CORE.RANDOM, 0, 0, 0);
	    	}	    	
	    	AutoMap<ItemStack> aOutputMap = new AutoMap<ItemStack>();		
			int aRandomTreeID = MathUtils.randInt(0, this.mFakeWorld.size()-1);
			FakeWorld aWorld = this.mFakeWorld.get(aRandomTreeID);
			if (aWorld != null) {
				aOutputMap = aWorld.getAllBlocksStoredInFakeWorld();				
			}	
			return aOutputMap;
	    	
	    }
	    
	    @Override
	    protected boolean func_150523_a(Block p_150523_1_)
	    {
	        return p_150523_1_.getMaterial() == Material.air || p_150523_1_.getMaterial() == Material.leaves || p_150523_1_ == Blocks.grass || p_150523_1_ == Blocks.dirt || p_150523_1_ == Blocks.log || p_150523_1_ == Blocks.log2 || p_150523_1_ == Blocks.sapling || p_150523_1_ == Blocks.vine;
	    }

	    @Override
	    protected boolean isReplaceable(World world, int x, int y, int z)
	    {
	    	FakeWorld aWorld = getWorld();
	        Block block = aWorld.getBlock(x, y, z);
	        return block.isAir(null, x, y, z) || block.isLeaves(null, x, y, z) || block.isWood(null, x, y, z) || func_150523_a(block);
	    }	    
	    
	    @Override
	    public boolean generate(World world, Random aRand, int aWorldX, int aWorldRealY, int aWorldZ){	    	
	    	//Only Generate Once - This object is Cached
	    	if (hasGenerated) {
	    		return hasGenerated;
	    	}	    	
	    	else {	    		
	    		for (int yy=0;yy<mTreesToGenerate;yy++) {	    			
	    			generateTree(0, 0, 0);
	    	        mCurrentGeneratorIteration++;
	    		}
	    		hasGenerated = true;
	    		if (this.mFakeWorld.size() > 0) {	    			
	    			for (FakeWorld aWorld : this.mFakeWorld) {
	    				for (ItemStack aBlockInFakeWorld : aWorld.getAllBlocksStoredInFakeWorld()) {
	    					aOutputsFromGenerator.add(aBlockInFakeWorld);
	    				}
	    			}	    			
	    			return true;
	    		}
	    		else {
	    			return false;
	    		}	    		
	    	}   	
	    }
	    
	    public FakeWorld getWorld() {
	    	FakeWorld aWorld = this.mFakeWorld.get(mCurrentGeneratorIteration);
	    	if (aWorld == null) {
	    		this.mFakeWorld.set(mCurrentGeneratorIteration, new FakeWorld(200));
	    		aWorld = this.mFakeWorld.get(mCurrentGeneratorIteration);
	    	}
	    	return aWorld;
	    }
	    
	    public boolean generateTree(int aWorldX, int aWorldRealY, int aWorldZ) {
	    	FakeWorld aWorld = getWorld();
	    	
	    	//Set some static values
	    	
	    	//Dummy Value
	    	int aWorldY = 10;
	    	
	        int l = CORE.RANDOM.nextInt(3) + this.minTreeHeight;
	        boolean flag = true;

	        if (aWorldY >= 1 && aWorldY + l + 1 <= 256)
	        {
	            byte b0;
	            int k1;
	            Block block;

	            for (int i1 = aWorldY; i1 <= aWorldY + 1 + l; ++i1)
	            {
	                b0 = 1;

	                if (i1 == aWorldY)
	                {
	                    b0 = 0;
	                }

	                if (i1 >= aWorldY + 1 + l - 2)
	                {
	                    b0 = 2;
	                }

	                for (int j1 = aWorldX - b0; j1 <= aWorldX + b0 && flag; ++j1)
	                {
	                    for (k1 = aWorldZ - b0; k1 <= aWorldZ + b0 && flag; ++k1)
	                    {
	                        if (i1 >= 0 && i1 < 256)
	                        {
	                            block = aWorld.getBlock(j1, i1, k1);

	                            if (!this.isReplaceable(null, j1, i1, k1))
	                            {
	                                flag = false;
	                            }
	                        }
	                        else
	                        {
	                            flag = false;
	                        }
	                    }
	                }
	            }

	            if (!flag)
	            {
	                return false;
	            }
	            else
	            {
	                Block block2 = aWorld.getBlock(aWorldX, aWorldY - 1, aWorldZ);
	                FakeBlockPos aBlockToGrowPlantOn = aWorld.getBlockAtCoords(aWorldX, aWorldY-1, aWorldZ);

	                boolean isSoil = block2.canSustainPlant(aWorld, aWorldX, aWorldY - 1, aWorldZ, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
	                if (isSoil && aWorldY < 256 - l - 1)
	                {
	                	aBlockToGrowPlantOn.onPlantGrow(aWorld, aWorldX, aWorldY - 1, aWorldZ, aWorldX, aWorldY, aWorldZ);
	                    b0 = 3;
	                    byte b1 = 0;
	                    int l1;
	                    int i2;
	                    int j2;
	                    int i3;

	                    for (k1 = aWorldY - b0 + l; k1 <= aWorldY + l; ++k1)
	                    {
	                        i3 = k1 - (aWorldY + l);
	                        l1 = b1 + 1 - i3 / 2;

	                        for (i2 = aWorldX - l1; i2 <= aWorldX + l1; ++i2)
	                        {
	                            j2 = i2 - aWorldX;

	                            for (int k2 = aWorldZ - l1; k2 <= aWorldZ + l1; ++k2)
	                            {
	                                int l2 = k2 - aWorldZ;

	                                if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || CORE.RANDOM.nextInt(2) != 0 && i3 != 0)
	                                {
	                                    Block block1 = aWorld.getBlock(i2, k1, k2);

	                                    if (block1.isAir(null, i2, k1, k2) || block1.isLeaves(null, i2, k1, k2))
	                                    {
	                                        this.setBlockAndNotifyAdequately(aWorld, i2, k1, k2, Blocks.leaves, this.metaLeaves);
	                                    }
	                                }
	                            }
	                        }
	                    }

	                    for (k1 = 0; k1 < l; ++k1)
	                    {
	                        block = aWorld.getBlock(aWorldX, aWorldY + k1, aWorldZ);

	                        if (block.isAir(null, aWorldX, aWorldY + k1, aWorldZ) || block.isLeaves(null, aWorldX, aWorldY + k1, aWorldZ))
	                        {
	                            this.setBlockAndNotifyAdequately(aWorld, aWorldX, aWorldY + k1, aWorldZ, Blocks.log, this.metaWood);

	                            if (this.vinesGrow && k1 > 0)
	                            {
	                                if (CORE.RANDOM.nextInt(3) > 0 && aWorld.isAirBlock(aWorldX - 1, aWorldY + k1, aWorldZ))
	                                {
	                                    this.setBlockAndNotifyAdequately(aWorld, aWorldX - 1, aWorldY + k1, aWorldZ, Blocks.vine, 8);
	                                }

	                                if (CORE.RANDOM.nextInt(3) > 0 && aWorld.isAirBlock(aWorldX + 1, aWorldY + k1, aWorldZ))
	                                {
	                                    this.setBlockAndNotifyAdequately(aWorld, aWorldX + 1, aWorldY + k1, aWorldZ, Blocks.vine, 2);
	                                }

	                                if (CORE.RANDOM.nextInt(3) > 0 && aWorld.isAirBlock(aWorldX, aWorldY + k1, aWorldZ - 1))
	                                {
	                                    this.setBlockAndNotifyAdequately(aWorld, aWorldX, aWorldY + k1, aWorldZ - 1, Blocks.vine, 1);
	                                }

	                                if (CORE.RANDOM.nextInt(3) > 0 && aWorld.isAirBlock(aWorldX, aWorldY + k1, aWorldZ + 1))
	                                {
	                                    this.setBlockAndNotifyAdequately(aWorld, aWorldX, aWorldY + k1, aWorldZ + 1, Blocks.vine, 4);
	                                }
	                            }
	                        }
	                    }

	                    if (this.vinesGrow)
	                    {
	                        for (k1 = aWorldY - 3 + l; k1 <= aWorldY + l; ++k1)
	                        {
	                            i3 = k1 - (aWorldY + l);
	                            l1 = 2 - i3 / 2;

	                            for (i2 = aWorldX - l1; i2 <= aWorldX + l1; ++i2)
	                            {
	                                for (j2 = aWorldZ - l1; j2 <= aWorldZ + l1; ++j2)
	                                {
	                                    if (aWorld.getBlock(i2, k1, j2).isLeaves(null, i2, k1, j2))
	                                    {
	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2 - 1, k1, j2).isAir(null, i2 - 1, k1, j2))
	                                        {
	                                            this.growVines(aWorld, i2 - 1, k1, j2, 8);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2 + 1, k1, j2).isAir(null, i2 + 1, k1, j2))
	                                        {
	                                            this.growVines(aWorld, i2 + 1, k1, j2, 2);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2, k1, j2 - 1).isAir(null, i2, k1, j2 - 1))
	                                        {
	                                            this.growVines(aWorld, i2, k1, j2 - 1, 1);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2, k1, j2 + 1).isAir(null, i2, k1, j2 + 1))
	                                        {
	                                            this.growVines(aWorld, i2, k1, j2 + 1, 4);
	                                        }
	                                    }
	                                }
	                            }
	                        }

	                        if (CORE.RANDOM.nextInt(5) == 0 && l > 5)
	                        {
	                            for (k1 = 0; k1 < 2; ++k1)
	                            {
	                                for (i3 = 0; i3 < 4; ++i3)
	                                {
	                                    if (CORE.RANDOM.nextInt(4 - k1) == 0)
	                                    {
	                                        l1 = CORE.RANDOM.nextInt(3);
	                                        this.setBlockAndNotifyAdequately(aWorld, aWorldX + Direction.offsetX[Direction.rotateOpposite[i3]], aWorldY + l - 5 + k1, aWorldZ + Direction.offsetZ[Direction.rotateOpposite[i3]], Blocks.cocoa, l1 << 2 | i3);
	                                    }
	                                }
	                            }
	                        }
	                    }
	                    return true;
	                }
	                else
	                {
	                    return false;
	                }
	            }
	        }
	        else
	        {
	            return false;
	        }
	    }

		/**
	     * Grows vines downward from the given block for a given length. Args: World, x, starty, z, vine-length
	     */
	    private void growVines(FakeWorld aWorld, int aX, int aY, int aZ, int aMeta)
	    {
	        int aLoopSize = vinesGrow ? MathUtils.randInt(0, 4) : 0;	        
	        for (int i=0;i<aLoopSize;i++) {
	            this.setBlockAndNotifyAdequately(aWorld, aX, aY, aZ, Blocks.vine, aMeta);	        	
	        }
	    }

		@Override
		protected void setBlockAndNotifyAdequately(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta) {	
			setBlockAndNotifyAdequately(getWorld(), aX, aY, aZ, aBlock, aMeta);			
		}
		
		protected void setBlockAndNotifyAdequately(FakeWorld aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta) {			
			if (aBlock != null && (aMeta >= 0 && aMeta <= Short.MAX_VALUE)) {
				aWorld.setBlockAtCoords(aX, aY, aZ, aBlock, aMeta);
				//aOutputsFromGenerator.put(ItemUtils.simpleMetaStack(aBlock, aMeta, 1));
			}			
		}
	}
	
	
}
