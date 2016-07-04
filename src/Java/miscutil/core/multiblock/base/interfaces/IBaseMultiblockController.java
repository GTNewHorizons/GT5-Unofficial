package miscutil.core.multiblock.base.interfaces;

import java.util.Collection;

import javax.annotation.Nonnull;

import raisintoast.core.multiblock.IMultiblockPart;

public interface IBaseMultiblockController {
	  public abstract boolean isAssembled();
	  
	  public abstract void reassemble();
	  
	  public abstract String getLastValidationError();
	  
	  @Nonnull
	  public abstract Collection<IMultiblockPart> getComponents();
	}