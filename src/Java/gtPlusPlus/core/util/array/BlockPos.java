package gtPlusPlus.core.util.array;

public class BlockPos {

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
	
}
