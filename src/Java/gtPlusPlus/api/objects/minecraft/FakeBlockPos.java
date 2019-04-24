package gtPlusPlus.api.objects.minecraft;

import java.util.HashSet;
import java.util.Set;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class FakeBlockPos extends BlockPos {

	private static final long serialVersionUID = -6442245826092414593L;
	private Block aBlockAtPos;
	private int aBlockMetaAtPos = 0;
	
	public static FakeBlockPos generateBlockPos(String sUUID) {
		String[] s2 = sUUID.split("@");
		return new FakeBlockPos(s2);
	}
	
	public FakeBlockPos(String[] s){
		this(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[0]));
	}

	public FakeBlockPos(int x, int y, int z, Block aBlock, int aMeta){
		this(x, y, z, 0);
		aBlockAtPos = aBlock;
		aBlockMetaAtPos = aMeta;
	}

	private FakeBlockPos(int x, int y, int z, int dim){
		this(x, y, z, DimensionManager.getWorld(dim));
	}

	private FakeBlockPos(int x, int y, int z, World dim){
		super(x, y, z, null);
	}

	public String getLocationString() {
		String S = ""+this.xPos+"@"+this.yPos+"@"+this.zPos;
		return S;
	}
	
	public String getUniqueIdentifier() {
		String S = ""+this.xPos+"@"+this.yPos+"@"+this.zPos+this.aBlockAtPos.getLocalizedName()+"@"+this.aBlockMetaAtPos;
		return S;
	}	

	@Override
	public  int hashCode() {
		int hash = 5;
		hash += (13 * this.xPos);
		hash += (19 * this.yPos);
		hash += (31 * this.zPos);
		hash += (17 * this.dim);
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if(!(other instanceof FakeBlockPos)) {
			return false;
		}
		FakeBlockPos otherPoint = (FakeBlockPos) other;
		return this.xPos == otherPoint.xPos && this.yPos == otherPoint.yPos && this.zPos == otherPoint.zPos;
	}

	public int distanceFrom(FakeBlockPos target) {
		if (target.dim != this.dim) {
			return Short.MIN_VALUE;
		}    	
		return distanceFrom(target.xPos, target.yPos, target.zPos);
	}

	/**
	 *
	 * @param x X coordinate of target.
	 * @param y Y coordinate of target.
	 * @param z Z coordinate of target.
	 * @return square of distance
	 */
	public int distanceFrom(int x, int y, int z) {
		int distanceX = this.xPos - x;
		int distanceY = this.yPos - y;
		int distanceZ = this.zPos - z;
		return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
	}

	public boolean isWithinRange(FakeBlockPos target, int range) {    	
		if (target.dim != this.dim) {
			return false;
		}    	
		return isWithinRange(target.xPos, target.yPos, target.zPos, range);
	}

	public boolean isWithinRange(int x, int y, int z, int range) {
		return distanceFrom(x, y, z) <= (range * range);
	}

	
	public FakeBlockPos getUp() {
		return new FakeBlockPos(this.xPos, this.yPos+1, this.zPos, this.dim);
	}
	
	public FakeBlockPos getDown() {
		return new FakeBlockPos(this.xPos, this.yPos-1, this.zPos, this.dim);
	}	

	public FakeBlockPos getXPos() {
		return new FakeBlockPos(this.xPos+1, this.yPos, this.zPos, this.dim);
	}
	
	public FakeBlockPos getXNeg() {
		return new FakeBlockPos(this.xPos-1, this.yPos, this.zPos, this.dim);
	}
	
	public FakeBlockPos getZPos() {
		return new FakeBlockPos(this.xPos, this.yPos, this.zPos+1, this.dim);
	}

	public FakeBlockPos getZNeg() {
		return new FakeBlockPos(this.xPos, this.yPos, this.zPos-1, this.dim);
	}
	
	public AutoMap<BlockPos> getSurroundingBlocks(){
		AutoMap<BlockPos> sides = new AutoMap<BlockPos>();
		sides.put(getUp());
		sides.put(getDown());
		sides.put(getXPos());
		sides.put(getXNeg());
		sides.put(getZPos());
		sides.put(getZNeg());		
		return sides;
	}
	
	public Block getBlockAtPos() {
		return getBlockAtPos(this);
	}
	
	public Block getBlockAtPos(FakeBlockPos pos) {
		return getBlockAtPos(world, pos);
	}
	
	public Block getBlockAtPos(World world, FakeBlockPos pos) {
		return aBlockAtPos;
	}
	
	public int getMetaAtPos() {
		return getMetaAtPos(this);
	}
	
	public int getMetaAtPos(FakeBlockPos pos) {
		return getMetaAtPos(world, pos);
	}
	
	public int getMetaAtPos(World world, FakeBlockPos pos) {
		return aBlockMetaAtPos;
	}
	
	public boolean hasSimilarNeighbour() {
		return hasSimilarNeighbour(false);
	}	
	
	/**
	 * @param strict - Does this check Meta Data?
	 * @return - Does this block have a neighbour that is the same?
	 */
	public boolean hasSimilarNeighbour(boolean strict) {		
		for (BlockPos g : getSurroundingBlocks().values()) {
			if (getBlockAtPos(g) == getBlockAtPos()) {
				if (!strict) {
					return true;
				}
				else {
					if (getMetaAtPos() == getMetaAtPos(g)) {
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	public AutoMap<BlockPos> getSimilarNeighbour() {
		return getSimilarNeighbour(false);
	}	
	
	/**
	 * @param strict - Does this check Meta Data?
	 * @return - Does this block have a neighbour that is the same?
	 */
	public AutoMap<BlockPos> getSimilarNeighbour(boolean strict) {
		AutoMap<BlockPos> sides = new AutoMap<BlockPos>();		
		for (BlockPos g : getSurroundingBlocks().values()) {
			if (getBlockAtPos(g) == getBlockAtPos()) {
				if (!strict) {
					sides.put(g);
				}
				else {
					if (getMetaAtPos() == getMetaAtPos(g)) {
						sides.put(g);
					}
				}
			}
		}		
		return sides;
	}
	
	public Set<BlockPos> getValidNeighboursAndSelf(){
		AutoMap<BlockPos> h = getSimilarNeighbour(true);
		h.put(this);		
		Set<BlockPos> result = new HashSet<BlockPos>();
		for (BlockPos f : h.values()) {
			result.add(f);
		}		
		return result;
	}
	
    /**
     * Called when a plant grows on this block, only implemented for saplings using the WorldGen*Trees classes right now.
     * Modder may implement this for custom plants.
     * This does not use ForgeDirection, because large/huge trees can be located in non-representable direction,
     * so the source location is specified.
     * Currently this just changes the block to dirt if it was grass.
     *
     * Note: This happens DURING the generation, the generation may not be complete when this is called.
     *
     * @param world Current world
     * @param x Soil X
     * @param y Soil Y
     * @param z Soil Z
     * @param sourceX Plant growth location X
     * @param sourceY Plant growth location Y
     * @param sourceZ Plant growth location Z
     */
    public void onPlantGrow(FakeWorld world, int x, int y, int z, int sourceX, int sourceY, int sourceZ)
    {
        if (getBlockAtPos() == Blocks.grass || getBlockAtPos() == Blocks.farmland)
        {
            this.aBlockAtPos = Blocks.dirt;
            this.aBlockMetaAtPos = 0;
        }
    }
	
}
