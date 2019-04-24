package gtPlusPlus.xmod.gregtech.common.helpers.treefarm;

import java.util.Random;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenerator {

	public static AutoMap<ItemStack> generateOutput(int aTreeSize){
		AutoMap<ItemStack> aOutputMap = new AutoMap<ItemStack>();
		
		
		return aOutputMap;
	}
	
	public class FakeWorldGenerator extends WorldGenAbstractTree
	{
	    /** The minimum height of a generated tree. */
	    private final int minTreeHeight;
	    /** True if this tree should grow Vines. */
	    private final boolean vinesGrow;
	    /** The metadata value of the wood to use in tree generation. */
	    private final int metaWood;
	    /** The metadata value of the leaves to use in tree generation. */
	    private final int metaLeaves;
	    
	    private boolean hasGenerated = false;
	    private AutoMap<ItemStack> aOutputsFromGenerator = new AutoMap<ItemStack>();

	    public FakeWorldGenerator()
	    {
	        this(4, 0, 0, false);
	    }

	    public FakeWorldGenerator(int aMinHeight, int aWoodMeta, int aLeafMeta, boolean aVines)
	    {
	        super(false);
	        this.minTreeHeight = aMinHeight;
	        this.metaWood = aWoodMeta;
	        this.metaLeaves = aLeafMeta;
	        this.vinesGrow = aVines;
	    }

	    
	    public AutoMap<ItemStack> getOutputFromTree(){
	    	if (!hasGenerated) {
	    		generate(null, CORE.RANDOM, 0, 0, 0);
	    	}	    	
	    	return aOutputsFromGenerator;
	    }
	    
	    
	    @Override
	    public boolean generate(World aWorld, Random aRand, int aWorldX, int aWorldRealY, int aWorldZ){
	    	
	    	//Only Generate Once - This object is Cached
	    	if (hasGenerated) {
	    		return hasGenerated;
	    	}
	    	
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

	                            if (!this.isReplaceable(aWorld, j1, i1, k1))
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

	                boolean isSoil = block2.canSustainPlant(aWorld, aWorldX, aWorldY - 1, aWorldZ, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
	                if (isSoil && aWorldY < 256 - l - 1)
	                {
	                    block2.onPlantGrow(aWorld, aWorldX, aWorldY - 1, aWorldZ, aWorldX, aWorldY, aWorldZ);
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

	                                    if (block1.isAir(aWorld, i2, k1, k2) || block1.isLeaves(aWorld, i2, k1, k2))
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

	                        if (block.isAir(aWorld, aWorldX, aWorldY + k1, aWorldZ) || block.isLeaves(aWorld, aWorldX, aWorldY + k1, aWorldZ))
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
	                                    if (aWorld.getBlock(i2, k1, j2).isLeaves(aWorld, i2, k1, j2))
	                                    {
	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2 - 1, k1, j2).isAir(aWorld, i2 - 1, k1, j2))
	                                        {
	                                            this.growVines(aWorld, i2 - 1, k1, j2, 8);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2 + 1, k1, j2).isAir(aWorld, i2 + 1, k1, j2))
	                                        {
	                                            this.growVines(aWorld, i2 + 1, k1, j2, 2);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2, k1, j2 - 1).isAir(aWorld, i2, k1, j2 - 1))
	                                        {
	                                            this.growVines(aWorld, i2, k1, j2 - 1, 1);
	                                        }

	                                        if (CORE.RANDOM.nextInt(4) == 0 && aWorld.getBlock(i2, k1, j2 + 1).isAir(aWorld, i2, k1, j2 + 1))
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

	    	            hasGenerated = true;
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
	    private void growVines(World aWorld, int aX, int aY, int aZ, int aMeta)
	    {
	        this.setBlockAndNotifyAdequately(aWorld, aX, aY, aZ, Blocks.vine, aMeta);
	        int i1 = 4;

	        while (true)
	        {
	            --aY;

	            if (!aWorld.getBlock(aX, aY, aZ).isAir(aWorld, aX, aY, aZ) || i1 <= 0)
	            {
	                return;
	            }

	            this.setBlockAndNotifyAdequately(aWorld, aX, aY, aZ, Blocks.vine, aMeta);
	            --i1;
	        }
	    }

		@Override
		protected void setBlockAndNotifyAdequately(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta) {
			
			super.setBlockAndNotifyAdequately(aWorld, aX, aY, aZ, aBlock, aMeta);
		}
	}
	
	
}
