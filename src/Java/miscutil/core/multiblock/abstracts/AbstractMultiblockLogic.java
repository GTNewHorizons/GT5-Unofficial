package miscutil.core.multiblock.abstracts;

import miscutil.core.multiblock.abstracts.interfaces.IAbstractControllerInternal;
import miscutil.core.multiblock.abstracts.interfaces.IAbstractMultiblockLogic;
import miscutil.core.multiblock.base.BaseMultiblockLogic;
import net.minecraft.world.World;

public class AbstractMultiblockLogic extends BaseMultiblockLogic<IAbstractControllerInternal> implements IAbstractMultiblockLogic
{
  public AbstractMultiblockLogic()
  {
    super(IAbstractControllerInternal.class);
  }
  
  @Override
public IAbstractControllerInternal getController()
  {
    if (super.isConnected()) {
      return (IAbstractControllerInternal)this.controller;
    }
    return AbstractFakeMultiblockController.instance;
  }
  
  @Override
public IAbstractControllerInternal createNewController(World world)
  {
    return new AbstractMultiblockController(world);
  }


}
