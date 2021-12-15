package gtPlusPlus.xmod.gregtech.api.gui.hatches;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.gui.GT_GUIContainer;

public class GUI_HatchNbtConsumable extends GT_GUIContainer {

    private final String mName;
	private final int mTotalSlotCount;
    public final CONTAINER_HatchNbtConsumable mContainer;
    
    public GUI_HatchNbtConsumable(CONTAINER_HatchNbtConsumable aContainer, String aName) {
    	super(aContainer, RES_PATH_GUI + getTextureForGUI(aContainer.mInputslotCount*2)+".png");
        mContainer = aContainer;
        mName = aName;
		mTotalSlotCount = aContainer.mInputslotCount*2;
	}

	private static final String getTextureForGUI(int aTotalSlotCOunt) {
    	if (aTotalSlotCOunt == 18) {
        	return "HatchNbtConsumable_3By3";    		
    	}
    	else if (aTotalSlotCOunt == 32) {
        	return "HatchNbtConsumable_4By4";    		
    	}
    	else {
        	return "HatchNbtConsumable_2By2";    		
    	}    	
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	if (mTotalSlotCount == 18) {
            fontRendererObj.drawString(mName, 8, 4, 4210752);
            fontRendererObj.drawString("Stock", 25, 14, 4210752);
            fontRendererObj.drawString("Active", 115, 14, 4210752);  		
    	}
    	else if (mTotalSlotCount == 32) {
            //fontRendererObj.drawString("Slots: "+mTotalSlotCount, 8, 4, 4210752);
            //fontRendererObj.drawString(mName, 8, 4, 4210752);
            //fontRendererObj.drawString("Stock", 25, 16, 4210752);
            //fontRendererObj.drawString("Active", 115, 16, 4210752);  		
    	}
    	else {
            fontRendererObj.drawString(mName, 8, 4, 4210752);
            fontRendererObj.drawString("Stock", 25, 16, 4210752);
            fontRendererObj.drawString("Active", 115, 16, 4210752); 		
    	}   
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
