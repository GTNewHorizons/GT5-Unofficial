package gtPlusPlus.xmod.gregtech.api.gui.hatches.charge;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.xmod.gregtech.api.gui.hatches.GUI_4by4;

public class GUI_Electric_4by4 extends GUI_4by4{

	public GUI_Electric_4by4(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
		super(new CONTAINER_Electric_4by4(aInventoryPlayer, aTileEntity), aInventoryPlayer, aTileEntity, aName);
	}
	
	public GUI_Electric_4by4(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
        super(new CONTAINER_Electric_4by4(aInventoryPlayer, aTileEntity), aInventoryPlayer, aTileEntity, RES_PATH_GUI + aBackground + "4by4.png");

    }

}
