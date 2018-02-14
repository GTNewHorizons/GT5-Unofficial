package gtPlusPlus.core.util.array;

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
		return "[X: "+this.xPos+"][Y: "+this.yPos+"][Z: "+this.zPos+"]";
	}
	
}
