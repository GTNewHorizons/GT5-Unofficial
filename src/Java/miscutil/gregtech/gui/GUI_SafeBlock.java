package miscutil.gregtech.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import miscutil.core.lib.CORE;
import miscutil.core.util.PlayerCache;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_SafeBlock
        extends GT_GUIContainerMetaTile_Machine {
    public GUI_SafeBlock(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new CONTAINER_SafeBlock(aInventoryPlayer, aTileEntity), CORE.MODID + ":" + "textures/gui/" + "SafeBlock.png");
    }
    
    String UUID = ((CONTAINER_SafeBlock)this.mContainer).ownerUUID;
    boolean blockStatus = ((CONTAINER_SafeBlock)this.mContainer).blockStatus;
    private String tempPlayer = PlayerCache.lookupPlayerByUUID(UUID);
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
      {
    	
    	UUID = ((CONTAINER_SafeBlock)this.mContainer).ownerUUID;
        blockStatus = ((CONTAINER_SafeBlock)this.mContainer).blockStatus;
        tempPlayer = PlayerCache.lookupPlayerByUUID(UUID);
    	
    	if (blockStatus){
    		this.fontRendererObj.drawString("Safe Status: Locked", 64, 62, 4210752);
    	}
    	else {
    		this.fontRendererObj.drawString("Safe Status: Unlocked", 64, 62, 4210752);
    	}
        
    	this.fontRendererObj.drawString("Owner: "+ tempPlayer, 64, 72, 4210752);
    	
        if (!CORE.DEBUG){
            
        }
      }
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        /*String UUID = ((CONTAINER_SafeBlock)this.mContainer).UUID;
        this.fontRendererObj.drawString("Owner UUID: "+ UUID, 8, 12, 4210752);*/
        
    }
}
