package miscutil.gregtech.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import miscutil.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_SteamCondenser extends GT_GUIContainerMetaTile_Machine
{
	long tickTime = 0;
	
  public GUI_SteamCondenser(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aTextureName, int aSteamCapacity)
  {
    super(new CONTAINER_SteamCondenser(aInventoryPlayer, aTileEntity, aSteamCapacity), CORE.MODID + ":" + "textures/gui/" + aTextureName);
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    this.fontRendererObj.drawString("Condenser", 8, 4, 4210752);
    if (!CORE.DEBUG){
        tickTime = ((CONTAINER_SteamCondenser)this.mContainer).mTickingTime;
        this.fontRendererObj.drawString("Tick Time: "+tickTime, 8, 12, 4210752);
    }
  }
  
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
  {
    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    if (this.mContainer != null)
    {
    	
      int tScale = ((CONTAINER_SteamCondenser)this.mContainer).mSteamAmount;
      if (tScale > 0) {
        drawTexturedModalRect(x + 70, y + 25 + 54 - tScale, 194, 54 - tScale, 10, tScale);
      }
      tScale = ((CONTAINER_SteamCondenser)this.mContainer).mWaterAmount;
      if (tScale > 0) {
        drawTexturedModalRect(x + 83, y + 25 + 54 - tScale, 204, 54 - tScale, 10, tScale);
      }
      tScale = ((CONTAINER_SteamCondenser)this.mContainer).mTemperature;
      if (tScale > 0) {
        drawTexturedModalRect(x + 96, y + 25 + 54 - tScale, 214, 54 - tScale, 10, tScale);
      }
      tScale = ((CONTAINER_SteamCondenser)this.mContainer).mProcessingEnergy;
      if (tScale > 0) {
        drawTexturedModalRect(x + 115, y + 44 + 2/* - tScale*/, 177, 14 - tScale, 15, 1+tScale);
      }
    }
  }
}