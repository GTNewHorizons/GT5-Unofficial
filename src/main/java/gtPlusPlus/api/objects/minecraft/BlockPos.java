package gtPlusPlus.api.objects.minecraft;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraftforge.common.DimensionManager;

public class BlockPos implements Serializable{

	private static final long serialVersionUID = -7271947491316682006L;
	public final int xPos;
	public final int yPos;
	public final int zPos;
	public final int dim;
	public final transient World world;
	
	public static BlockPos generateBlockPos(String sUUID) {
		String[] s2 = sUUID.split("@");
		return new BlockPos(s2);
	}
	
	public BlockPos(String[] s){
		this(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[0]));
	}

	public BlockPos(int x, int y, int z){
		this(x, y, z, 0);
	}

	public BlockPos(int x, int y, int z, int dim){
		this(x, y, z, DimensionManager.getWorld(dim));
	}

	public BlockPos(int x, int y, int z, World dim){
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		
		if (dim != null) {
			this.dim = dim.provider.dimensionId;
			this.world = dim;			
		}
		else {
			this.dim = 0;
			this.world = null;			
		}
		
	}
	
	public BlockPos(IGregTechTileEntity b) {
		this (b.getXCoord(), b.getYCoord(), b.getZCoord(), b.getWorld());
	}
	
	public BlockPos(TileEntity b) {
		this (b.xCoord, b.yCoord, b.zCoord, b.getWorldObj());
	}

	public String getLocationString() {
		return "[X: "+this.xPos+"][Y: "+this.yPos+"][Z: "+this.zPos+"][Dim: "+this.dim+"]";
	}
	
	public String getUniqueIdentifier() {
		String S = ""+this.dim+"@"+this.xPos+"@"+this.yPos+"@"+this.zPos;
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
		if(!(other instanceof BlockPos)) {
			return false;
		}
		BlockPos otherPoint = (BlockPos)other;
		return this.xPos == otherPoint.xPos && this.yPos == otherPoint.yPos && this.zPos == otherPoint.zPos && this.dim == otherPoint.dim;
	}

	public int distanceFrom(BlockPos target) {
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

	public boolean isWithinRange(BlockPos target, int range) {    	
		if (target.dim != this.dim) {
			return false;
		}    	
		return isWithinRange(target.xPos, target.yPos, target.zPos, range);
	}

	public boolean isWithinRange(int x, int y, int z, int range) {
		return distanceFrom(x, y, z) <= (range * range);
	}

	
	public BlockPos getUp() {
		return new BlockPos(this.xPos, this.yPos+1, this.zPos, this.dim);
	}
	
	public BlockPos getDown() {
		return new BlockPos(this.xPos, this.yPos-1, this.zPos, this.dim);
	}	

	public BlockPos getXPos() {
		return new BlockPos(this.xPos+1, this.yPos, this.zPos, this.dim);
	}
	
	public BlockPos getXNeg() {
		return new BlockPos(this.xPos-1, this.yPos, this.zPos, this.dim);
	}
	
	public BlockPos getZPos() {
		return new BlockPos(this.xPos, this.yPos, this.zPos+1, this.dim);
	}

	public BlockPos getZNeg() {
		return new BlockPos(this.xPos, this.yPos, this.zPos-1, this.dim);
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
	
	public Block getBlockAtPos(BlockPos pos) {
		return getBlockAtPos(world, pos);
	}
	
	public Block getBlockAtPos(World world, BlockPos pos) {
		return world.getBlock(pos.xPos, pos.yPos, pos.zPos);
	}
	
	public int getMetaAtPos() {
		return getMetaAtPos(this);
	}
	
	public int getMetaAtPos(BlockPos pos) {
		return getMetaAtPos(world, pos);
	}
	
	public int getMetaAtPos(World world, BlockPos pos) {
		return world.getBlockMetadata(pos.xPos, pos.yPos, pos.zPos);
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
	
}
