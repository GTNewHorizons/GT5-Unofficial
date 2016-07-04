package miscutil.core.multiblock.abstracts;

import miscutil.core.multiblock.base.interfaces.IBaseMultiblockSizeLimits;

public class AbstractMultiblockSizeLimits implements IBaseMultiblockSizeLimits {
	
	  public static final AbstractMultiblockSizeLimits instance = new AbstractMultiblockSizeLimits(3,3,3,3,3,3,27);
	  
	  private short min_X;
	  private short min_Y;
	  private short min_Z;
	  private short max_X;
	  private short max_Y;
	  private short max_Z;
	  private short totalBlocks;
	  
	  public AbstractMultiblockSizeLimits(int min_x, int min_y, int min_z, int max_x, int max_y, int max_z, int totalSize){
		  this.min_X = (short) min_x;
		  this.min_Y = (short) min_y;
		  this.min_Z = (short) min_z;	
		  this.max_X = (short) max_x;
		  this.max_Y = (short) max_y;
		  this.max_Z = (short) max_z;		  
		  this.totalBlocks = (short) totalSize;
	  }
	  
	  
	  @Override
	public int getMinNumberOfBlocksForAssembledMachine()
	  {
	    return totalBlocks;
	  }
	  
	  @Override
	public int getMaxXSize()
	  {
	    return max_X;
	  }
	  
	  @Override
	public int getMaxZSize()
	  {
	    return max_Y;
	  }
	  
	  @Override
	public int getMaxYSize()
	  {
	    return max_Z;
	  }
	  
	  @Override
	public int getMinXSize()
	  {
	    return min_X;
	  }
	  
	  @Override
	public int getMinYSize()
	  {
	    return min_Y;
	  }
	  
	  @Override
	public int getMinZSize()
	  {
	    return min_Z;
	  }
}
