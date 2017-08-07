package gregtechmod.common.tileentities.machines.multi;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Multi_AutoCrafter
  extends GT_MetaTileEntity_MultiBlockBase
{
  public boolean isFacingValid(byte aFacing)
  {
    return aFacing > 1;
  }
  
  public void onRightclick(EntityPlayer aPlayer) {}
  
  public GT_MetaTileEntity_Multi_AutoCrafter(int aID, String aName, String aNameRegional)
  {
    super(aID, aName, aNameRegional);
  }
  
  public GT_MetaTileEntity_Multi_AutoCrafter() {}
  
  public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
  {
    return new GT_MetaTileEntity_Multi_AutoCrafter();
  }
  
  public boolean isCorrectMachinePart(ItemStack aStack)
  {
    return true;
  }
  
  public int getDamageToComponent(ItemStack aStack)
  {
    return 0;
  }
  
  public boolean checkRecipe(ItemStack aStack)
  {
    return false;
  }
  
  public boolean onRunningTick(ItemStack aStack)
  {
    return true;
  }
  
  public boolean checkMachine(ItemStack aStack)
  {
    byte tSide = getBaseMetaTileEntity().getBackFacing();
    if (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1))
    {
      if (((getBaseMetaTileEntity().getBlockAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != gregtechmod.api.GregTech_API.sBlockList[0]) || (getBaseMetaTileEntity().getMetaIDAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != 15)) && 
        (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2)))) {
        return false;
      }
      int tX = getBaseMetaTileEntity().getXCoord();int tY = getBaseMetaTileEntity().getYCoord();int tZ = getBaseMetaTileEntity().getZCoord();
      for (byte i = -1; i < 2; i = (byte)(i + 1)) {
        for (byte j = -1; j < 2; j = (byte)(j + 1)) {
          if ((i != 0) || (j != 0)) {
            for (byte k = 0; k < 5; k = (byte)(k + 1)) {
              if (((i == 0) || (j == 0)) && (k > 0) && (k < 4))
              {
                if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == gregtechmod.api.GregTech_API.sBlockList[0])
                {
                  if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == 15) {}
                }
                else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)))) {
                  return false;
                }
              }
              else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == gregtechmod.api.GregTech_API.sBlockList[0])
              {
                if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == 15) {}
              }
              else {
                return false;
              }
            }
          }
        }
      }
    }
    else
    {
      return false;
    }
    return true;
  }
  
  public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
  {
    if (aSide == aFacing) {
      return aActive ? 84 : 83;
    }
    return super.getTextureIndex(aSide, aFacing, aActive, aRedstone);
  }
  
  public boolean explodesOnComponentBreak(ItemStack aStack)
  {
    return false;
  }
  
  public int getMaxEfficiency(ItemStack aStack)
  {
    return 10000;
  }
  
  public int getPollutionPerTick(ItemStack aStack)
  {
    return 0;
  }
  
  public int getAmountOfOutputs()
  {
    return 1;
  }
  
  public String getDescription()
  {
    return "Highly Advanced Autocrafter";
  }
}
