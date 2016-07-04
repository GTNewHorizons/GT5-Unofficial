package miscutil.core.multiblock.abstracts.interfaces;

import miscutil.core.multiblock.base.interfaces.IBaseMultiblockControllerInternal;

public interface IAbstractControllerInternal extends IAbstractController, IBaseMultiblockControllerInternal {
	  //public abstract IInventoryAdapter getInternalInventory();
	  
	  public abstract int getHealthScaled(int paramInt);
	}