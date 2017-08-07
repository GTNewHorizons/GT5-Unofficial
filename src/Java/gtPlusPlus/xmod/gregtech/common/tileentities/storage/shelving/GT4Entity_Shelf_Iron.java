package gregtechmod.common.tileentities.storage;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;

public class GT_MetaTileEntity_Shelf_Iron
  extends GT_MetaTileEntity_Shelf
{
  public GT_MetaTileEntity_Shelf_Iron(int aID, String aName, String aNameRegional)
  {
    super(aID, aName, aNameRegional);
  }
  
  public GT_MetaTileEntity_Shelf_Iron() {}
  
  public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
  {
    return new GT_MetaTileEntity_Shelf_Iron();
  }
  
  public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
  {
    if (aSide == aFacing) {
      return 216 + this.mType;
    }
    if (aSide == 0) {
      return 32;
    }
    if (aSide == 1) {
      return 29;
    }
    return 40;
  }
}
