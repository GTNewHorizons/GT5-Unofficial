package miscutil.core.multiblock.base.interfaces;

public abstract interface IBaseMultiblockSizeLimits {
	
	  public abstract int getMinNumberOfBlocksForAssembledMachine();
	  
	  public abstract int getMaxXSize();
	  
	  public abstract int getMaxZSize();
	  
	  public abstract int getMaxYSize();
	  
	  public abstract int getMinXSize();
	  
	  public abstract int getMinYSize();
	  
	  public abstract int getMinZSize();
	  
	}