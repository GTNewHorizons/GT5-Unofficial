package gtPlusPlus.xmod.gregtech.api.gui.computer;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.computer.GT_Computercube_Description;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GT_GUIContainer_ComputerCube extends GT_GUIContainerMetaTile_Machine {
	public GT_GUIContainer_ComputerCube(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aBaseMetaTileEntity, int aID) {
		super(new GT_Container_ComputerCube(aInventoryPlayer, aBaseMetaTileEntity, aID), CORE.RES_PATH_GUI + "computer/"+aID+".png");
		GT_Container_ComputerCube tContainer = (GT_Container_ComputerCube) this.mContainer;
		Logger.INFO("1 GUI Mode: "+aID);
		Logger.INFO("2 GUI Mode: "+tContainer.mID);
		if (tContainer.mID == 5) {
			this.xSize += 50;
		}
	}
	

    public static ResourceLocation[] mGUIbackground = new ResourceLocation[8];
    
    static {
    	mGUIbackground[0] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/0.png");
    	mGUIbackground[1] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/1.png");
    	mGUIbackground[2] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/2.png");
    	mGUIbackground[3] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/3.png");
    	mGUIbackground[4] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/4.png");
    	mGUIbackground[5] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/5.png");
    	mGUIbackground[6] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/6.png");
    	mGUIbackground[7] = new ResourceLocation(CORE.RES_PATH_GUI + "computer/Redstone.png");
    	
    }

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		GT_Container_ComputerCube tContainer = (GT_Container_ComputerCube) this.mContainer;
		//Logger.INFO("3 GUI Mode: "+xSize);
		//GT_TileEntity_ComputerCube tTileEntity = (GT_TileEntity_ComputerCube) tContainer.mTileEntity;
		if (tContainer != null)
			switch (tContainer.mID) {
				case 0 :
					this.fontRendererObj.drawString("G.L.A.D.-OS", 64, 61, 16448255);
					this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
					break;
				case 1 :
					this.fontRendererObj.drawString("Reactorstats:", 7, 108, 16448255);
					this.fontRendererObj.drawString(toNumber(tContainer.mEU) + "EU at " + tContainer.mEUOut + "EU/t", 7, 120, 16448255);
					this.fontRendererObj.drawString("HEM: " + (tContainer.mHEM / 10000.0F), 7, 128, 16448255);
					this.fontRendererObj.drawString(toNumber(tContainer.mHeat) + "/" + toNumber(tContainer.mMaxHeat) + "Heat", 7, 136, 16448255);
					this.fontRendererObj.drawString("Explosionpower: " + (tContainer.mExplosionStrength / 100.0F), 7, 144, 16448255);
					this.fontRendererObj.drawString("Runtime: " + ((tContainer.mProgress > 0) ? (/*(tContainer.mEU / tContainer.mEUOut)*/ tContainer.mProgress / 20) : 0) + "secs", 7, 152, 16448255);
					break;
				case 2 :
					this.fontRendererObj.drawString("Scanner", 51, 7, 16448255);
					if (tContainer.mProgress == 0) {
						this.fontRendererObj.drawString("Can be used to", 51, 24, 16448255);
						this.fontRendererObj.drawString("scan Seedbags", 51, 32, 16448255);
					}
					else {
						this.fontRendererObj.drawString("Progress:", 51, 24, 16448255);
						this.fontRendererObj.drawString(tContainer.mProgress + "%", 51, 32, 16448255);
					}
					this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
					break;
				case 3 :
					this.fontRendererObj.drawString("Centrifuge", 7, 7, 16448255);
					this.fontRendererObj.drawString("Recipe: " + (tContainer.mMaxHeat + 1) + "/" + GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.size(), 7, 23, 16448255);
					this.fontRendererObj.drawString("EU: " + toNumber(tContainer.mEU), 7, 31, 16448255);
					break;
				case 4 :
					this.fontRendererObj.drawString("Fusionreactor", 7, 7, 16448255);
					this.fontRendererObj.drawString("Recipe: " + (tContainer.mMaxHeat + 1) + "/" + GT_Recipe_Map.sFusionRecipes.mRecipeList.size(), 7, 23, 16448255);
					this.fontRendererObj.drawString("Start: " + toNumber(tContainer.mEU) + "EU", 7, 31, 16448255);
					this.fontRendererObj.drawString("EU/t: " + toNumber(tContainer.mEUOut), 7, 39, 16448255);
					this.fontRendererObj.drawString(toNumber(tContainer.mHeat) + " Ticks", 7, 47, 16448255);
					if (tContainer.mEUOut < 0) {
						this.fontRendererObj.drawString("IN: " + toNumber(-tContainer.mEUOut * tContainer.mHeat) + "EU", 7, 55, 16448255);
						break;
					}
					this.fontRendererObj.drawString("OUT: " + toNumber(tContainer.mEUOut * tContainer.mHeat) + "EU", 7, 55, 16448255);
					break;
				case 5 :
					if (tContainer.mID == 5 && this.xSize == 176) {
						this.xSize += 50;
					}
					if (tContainer.mMaxHeat >= 0 && tContainer.mMaxHeat < GT_Computercube_Description.sDescriptions.size())
						for (int i = 0; i < ((GT_Computercube_Description) GT_Computercube_Description.sDescriptions.get(tContainer.mMaxHeat)).mDescription.length; i++) {
							if (i == 0) {
								this.fontRendererObj.drawString(((GT_Computercube_Description) GT_Computercube_Description.sDescriptions.get(tContainer.mMaxHeat)).mDescription[i], 7, 7, 16448255);
							}
							else {
								this.fontRendererObj.drawString(((GT_Computercube_Description) GT_Computercube_Description.sDescriptions.get(tContainer.mMaxHeat)).mDescription[i], 7, 7
										+ 8 * i, 16448255);
							}
						}
					break;
				case 6 :
					this.fontRendererObj.drawString("Electrolyzer", 7, 7, 16448255);
					this.fontRendererObj.drawString("Recipe: " + (tContainer.mMaxHeat + 1) + "/" + GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList.size(), 7, 23, 16448255);
					this.fontRendererObj.drawString("EU: " + toNumber(tContainer.mEU), 7, 31, 16448255);
					break;
			}
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		if (mContainer != null) {
			GT_Container_ComputerCube tContainer = (GT_Container_ComputerCube) this.mContainer;	
	        mc.renderEngine.bindTexture(mGUIbackground[((GT_Container_ComputerCube) this.mContainer).mID]);
			int x = (width - xSize) / 2;
			int y = (height - ySize) / 2;
			drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
			switch (tContainer.mID) {
				case 5 :
					if (tContainer.mExplosionStrength != 0)
						drawTexturedModalRect(x + 152, y + 6, 0, 166, 50, 50);
					break;
			}
		}		
	}

	public String toNumber(long mEU) {
		String tString = "";
		boolean temp = true, negative = false;
		if (mEU < 0) {
			mEU *= -1;
			negative = true;
		}
		int i;
		for (i = 1000000000; i > 0; i /= 10) {
			long tDigit = mEU / i % 10;
			if (temp && tDigit != 0)
				temp = false;
			if (!temp) {
				tString = tString + tDigit;
				if (i != 1) {
					int j;
					for (j = i; j > 0;) {
						if (j == 1)
							tString = tString + ",";
						j /= 1000;
					}
				}
			}
		}
		if (tString.equals(""))
			tString = "0";
		return negative ? ("-" + tString) : tString;
	}
}
