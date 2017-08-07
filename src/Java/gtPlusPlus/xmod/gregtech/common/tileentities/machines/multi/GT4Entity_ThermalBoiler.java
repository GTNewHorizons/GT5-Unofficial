package gregtechmod.common.tileentities.machines.multi;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_Recipe;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Multi_ThermalBoiler
  extends GT_MetaTileEntity_MultiBlockBase
{
  public boolean isFacingValid(byte aFacing)
  {
    return aFacing > 1;
  }
  
  public void onRightclick(EntityPlayer aPlayer)
  {
    getBaseMetaTileEntity().openGUI(aPlayer, 158, GregTech_API.gregtechmod);
  }
  
  public GT_MetaTileEntity_Multi_ThermalBoiler(int aID, String aName, String aNameRegional)
  {
    super(aID, aName, aNameRegional);
  }
  
  public GT_MetaTileEntity_Multi_ThermalBoiler() {}
  
  public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
  {
    return new GT_MetaTileEntity_Multi_ThermalBoiler();
  }
  
  public boolean isCorrectMachinePart(ItemStack aStack)
  {
    return true;
  }
  
  public int getDamageToComponent(ItemStack aStack)
  {
    return GT_Utility.areStacksEqual(aStack, GT_Items.Component_LavaFilter.getWildcard(1L, new Object[0])) ? 1 : 0;
  }
  
  public boolean checkRecipe(ItemStack aStack)
  {
    for (GT_Recipe tRecipe : GT_Recipe.sHotFuels) {
      if (depleteInput(tRecipe.getRepresentativeInput(0)))
      {
        this.mEUt = 400;
        this.mMaxProgresstime = (tRecipe.mStartEU * 2 / 5);
        this.mEfficiencyIncrease = (this.mMaxProgresstime * 30);
        if (tRecipe.getOutput(0) != null) {
          this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(0) }) };
        }
        if (GT_Utility.areStacksEqual(aStack, GT_Items.Component_LavaFilter.getWildcard(1L, new Object[0]))) {
          if ((tRecipe.getOutput(1) != null) && (getBaseMetaTileEntity().getRandomNumber(1000) < 100)) {
            this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(1) }) };
          } else if ((tRecipe.getOutput(2) != null) && (getBaseMetaTileEntity().getRandomNumber(900) < 50)) {
            this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(2) }) };
          } else if ((tRecipe.getOutput(3) != null) && (getBaseMetaTileEntity().getRandomNumber(850) < 25)) {
            this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(3) }) };
          }
        }
        return true;
      }
    }
    return false;
  }
  
  public boolean onRunningTick(ItemStack aStack)
  {
    if (this.mEUt > 0)
    {
      int tGeneratedEU = (int)(this.mEUt * 2L * this.mEfficiency / 10000L);
      if ((tGeneratedEU > 0) && (depleteInput(GT_ModHandler.getWater((tGeneratedEU + 160) / 160)))) {
        addOutput(GT_ModHandler.getSteam(tGeneratedEU));
      }
      return true;
    }
    return true;
  }
  
  public boolean checkMachine(ItemStack aStack)
  {
    byte tSide = getBaseMetaTileEntity().getBackFacing();
    if (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1))
    {
      if (((getBaseMetaTileEntity().getBlockAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != GregTech_API.sBlockList[0]) || (getBaseMetaTileEntity().getMetaIDAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != 14)) && 
        (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2)))) {
        return false;
      }
      int tX = getBaseMetaTileEntity().getXCoord();int tY = getBaseMetaTileEntity().getYCoord();int tZ = getBaseMetaTileEntity().getZCoord();
      for (byte i = -1; i < 2; i = (byte)(i + 1)) {
        for (byte j = -1; j < 2; j = (byte)(j + 1)) {
          if ((i != 0) || (j != 0)) {
            for (byte k = 0; k < 3; k = (byte)(k + 1)) {
              if (((i == 0) || (j == 0)) && (k == 1))
              {
                if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == GregTech_API.sBlockList[0])
                {
                  if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == 14) {}
                }
                else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)))) {
                  return false;
                }
              }
              else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == GregTech_API.sBlockList[0])
              {
                if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == 14) {}
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
    return "Converts Heat into Steam";
  }
}
