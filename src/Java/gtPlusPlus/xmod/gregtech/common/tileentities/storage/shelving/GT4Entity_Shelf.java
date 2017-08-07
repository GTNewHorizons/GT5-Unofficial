package gregtechmod.common.tileentities.storage;

import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.enums.OrePrefixes;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GT_MetaTileEntity_Shelf
  extends MetaTileEntity
{
  public byte mType = 0;
  
  public GT_MetaTileEntity_Shelf(int aID, String aName, String aNameRegional)
  {
    super(aID, aName, aNameRegional);
  }
  
  public GT_MetaTileEntity_Shelf() {}
  
  public boolean isSimpleMachine()
  {
    return true;
  }
  
  public int getInvSize()
  {
    return 1;
  }
  
  public boolean isFacingValid(byte aFacing)
  {
    return aFacing > 1;
  }
  
  public boolean isAccessAllowed(EntityPlayer aPlayer)
  {
    return true;
  }
  
  public boolean ownerControl()
  {
    return false;
  }
  
  public boolean isEnetOutput()
  {
    return false;
  }
  
  public boolean isEnetInput()
  {
    return false;
  }
  
  public boolean isOutputFacing(byte aSide)
  {
    return false;
  }
  
  public boolean isInputFacing(byte aSide)
  {
    return false;
  }
  
  public void onRightclick(EntityPlayer aPlayer)
  {
    ItemStack tStack = aPlayer.field_71071_by.func_70301_a(aPlayer.field_71071_by.field_70461_c);
    if (tStack == null)
    {
      if ((this.mInventory[0] != null) && (this.mInventory[0].field_77994_a > 0))
      {
        aPlayer.field_71071_by.func_70299_a(aPlayer.field_71071_by.field_70461_c, this.mInventory[0]);
        getBaseMetaTileEntity().func_70299_a(0, null);
        this.mType = 0;
      }
    }
    else if (this.mInventory[0] == null) {
      if (OrePrefixes.paper.contains(tStack))
      {
        aPlayer.field_71071_by.func_70299_a(aPlayer.field_71071_by.field_70461_c, null);
        getBaseMetaTileEntity().func_70299_a(0, tStack);
        this.mType = 1;
      }
      else if (OrePrefixes.book.contains(tStack))
      {
        aPlayer.field_71071_by.func_70299_a(aPlayer.field_71071_by.field_70461_c, null);
        getBaseMetaTileEntity().func_70299_a(0, tStack);
        this.mType = 2;
      }
      else if ((GT_Items.IC2_Food_Can_Filled.isStackEqual(tStack, true, true)) || (GT_Items.IC2_Food_Can_Spoiled.isStackEqual(tStack, true, true)) || (GT_Items.IC2_Food_Can_Empty.isStackEqual(tStack, false, true)))
      {
        aPlayer.field_71071_by.func_70299_a(aPlayer.field_71071_by.field_70461_c, null);
        getBaseMetaTileEntity().func_70299_a(0, tStack);
        this.mType = 3;
      }
    }
  }
  
  public void onLeftclick(EntityPlayer aPlayer)
  {
    if ((this.mInventory[0] != null) && (this.mInventory[0].field_77994_a > 0))
    {
      ItemStack tOutput = GT_Utility.copy(new Object[] { this.mInventory[0] });
      if (!aPlayer.func_70093_af()) {
        tOutput.field_77994_a = 1;
      }
      getBaseMetaTileEntity().func_70298_a(0, tOutput.field_77994_a);
      EntityItem tEntity = new EntityItem(getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, tOutput);
      tEntity.field_70159_w = 0.0D;
      tEntity.field_70181_x = 0.0D;
      tEntity.field_70179_y = 0.0D;
      getBaseMetaTileEntity().getWorld().func_72838_d(tEntity);
      if (this.mInventory[0] == null) {
        this.mType = 0;
      }
    }
  }
  
  public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
  {
    return new GT_MetaTileEntity_Shelf();
  }
  
  public void saveNBTData(NBTTagCompound aNBT)
  {
    aNBT.func_74768_a("mType", this.mType);
  }
  
  public void loadNBTData(NBTTagCompound aNBT)
  {
    this.mType = ((byte)aNBT.func_74762_e("mType"));
  }
  
  public void onValueUpdate(byte aValue)
  {
    this.mType = aValue;
  }
  
  public byte getUpdateData()
  {
    return this.mType;
  }
  
  public boolean allowCoverOnSide(byte aSide, int aCoverID)
  {
    return aSide != getBaseMetaTileEntity().getFrontFacing();
  }
  
  public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
  {
    if (aSide == aFacing) {
      return 208 + this.mType;
    }
    return 10;
  }
  
  public String getDescription()
  {
    return "Decorative Item Storage";
  }
  
  public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack)
  {
    return false;
  }
  
  public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack)
  {
    return false;
  }
}
