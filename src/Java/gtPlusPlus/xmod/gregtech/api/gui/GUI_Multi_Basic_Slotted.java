package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GUI_Multi_Basic_Slotted extends GT_GUIContainerMetaTile_Machine {
	String mName = "";

	public GUI_Multi_Basic_Slotted(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
			String aTextureFile) {
		super(new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
		this.mName = aName;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(this.mName, 10, 8, 16448255);
		if (this.mContainer != null) {
			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 1) != 0) {
				this.fontRendererObj.drawString(this.trans("132", "Pipe is loose."), 10, 16, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 2) != 0) {
				this.fontRendererObj.drawString(this.trans("133", "Screws are missing."), 10, 24, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 4) != 0) {
				this.fontRendererObj.drawString(this.trans("134", "Something is stuck."), 10, 32, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 8) != 0) {
				this.fontRendererObj.drawString(this.trans("135", "Platings are dented."), 10, 40, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 16) != 0) {
				this.fontRendererObj.drawString(this.trans("136", "Circuitry burned out."), 10, 48, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 32) != 0) {
				this.fontRendererObj.drawString(this.trans("137", "That doesn\'t belong there."), 10, 56, 16448255);
			}

			if ((((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode & 64) != 0) {
				this.fontRendererObj.drawString(this.trans("138", "Incomplete Structure."), 10, 64, 16448255);
			}

			if (((GT_Container_MultiMachine) this.mContainer).mDisplayErrorCode == 0) {
				if (((GT_Container_MultiMachine) this.mContainer).mActive == 0) {
					this.fontRendererObj.drawString(this.trans("139", "Hit with Soft Hammer"), 10, 16, 16448255);
					this.fontRendererObj.drawString(this.trans("140", "to (re-)start the Machine"), 10, 24, 16448255);
					this.fontRendererObj.drawString(this.trans("141", "if it doesn\'t start."), 10, 32, 16448255);
				} else {
					this.fontRendererObj.drawString(this.trans("142", "Running perfectly."), 10, 16, 16448255);
				}

				ItemStack tItem;
				if (this.mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DrillerBase) {
					tItem = this.mContainer.mTileEntity.getMetaTileEntity().getStackInSlot(1);
					if (tItem == null
							|| !GT_Utility.areStacksEqual(tItem, GT_ModHandler.getIC2Item("miningPipe", 1L))) {
						this.fontRendererObj.drawString(this.trans("143", "Missing Mining Pipe"), 10,
								((GT_Container_MultiMachine) this.mContainer).mActive == 0 ? 40 : 24, 16448255);
					}
				} else if (this.mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbine) {
					tItem = this.mContainer.mTileEntity.getMetaTileEntity().getStackInSlot(1);
					if (tItem == null || tItem.getItem() != GT_MetaGenerated_Tool_01.INSTANCE
							|| tItem.getItemDamage() < 170 || tItem.getItemDamage() > 177) {
						this.fontRendererObj.drawString(this.trans("144", "Missing Turbine Rotor"), 10,
								((GT_Container_MultiMachine) this.mContainer).mActive == 0 ? 40 : 24, 16448255);
					}
				}
			}
		}

	}

	public String trans(String aKey, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}