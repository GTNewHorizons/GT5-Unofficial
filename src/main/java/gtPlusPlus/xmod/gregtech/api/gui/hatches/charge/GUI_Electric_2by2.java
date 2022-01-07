package gtPlusPlus.xmod.gregtech.api.gui.hatches.charge;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_2by2;

public class GUI_Electric_2by2 extends GUI_2by2{

	public GUI_Electric_2by2(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
		super(new CONTAINER_Electric_2by2(aInventoryPlayer, aTileEntity), aInventoryPlayer, aTileEntity, aName);
	}
	
	public GUI_Electric_2by2(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
       super(new CONTAINER_Electric_2by2(aInventoryPlayer, aTileEntity), aInventoryPlayer, aTileEntity, RES_PATH_GUI + aBackground + "2by2.png");
    }

}
