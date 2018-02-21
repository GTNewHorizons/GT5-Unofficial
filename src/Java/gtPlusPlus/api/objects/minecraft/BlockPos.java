package gtPlusPlus.api.objects.minecraft;

import java.io.Serializable;

public class BlockPos implements Serializable{

	private static final long serialVersionUID = -7271947491316682006L;
	public final int xPos;
	public final int yPos;
	public final int zPos;
	public final int dim;

	public BlockPos(int x, int y, int z){
		this(x, y, z, 0);
	}

	public BlockPos(int x, int y, int z, int dim){
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		this.dim = dim;
	}

	public String getLocationString() {
		return "[X: "+this.xPos+"][Y: "+this.yPos+"][Z: "+this.zPos+"][Dim: "+this.dim+"]";
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

}
