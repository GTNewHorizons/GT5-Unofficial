package miscutil.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;

import miscutil.core.util.Utils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntityIndustrialPlatePress
extends GT_MetaTileEntity_MultiBlockBase {
	public GregtechMetaTileEntityIndustrialPlatePress(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityIndustrialPlatePress(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityIndustrialPlatePress(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Vacuum Freezer", "Size: 3x3x3 (Hollow)", "Controller (front centered)", "1x Input (anywhere)", "1x Output (anywhere)", "1x Energy Hatch (anywhere)", "1x Maintenance Hatch (anywhere)", "Frost Proof Casings for the rest (16 at least!)"};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBenderRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		for (ItemStack tInput : tInputList) {
			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
			if (tRecipe != null) {
				if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
					this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
					this.mEfficiencyIncrease = 10000;
					if (tRecipe.mEUt <= 16) {
						this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
						this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
					} else {
						this.mEUt = tRecipe.mEUt;
						this.mMaxProgresstime = tRecipe.mDuration;
						while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
							this.mEUt *= 4;
							this.mMaxProgresstime /= 2;
						}
					}
					if (this.mEUt > 0) {
						this.mEUt = (-this.mEUt);
					}
					this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
					this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
					updateSlots();
					return true;
				}
			}
		}
		return false;
	}

	/*public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int stepX = 0;
		int stepY = 0;
		int stepZ = 0;

		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			stepX=+i;
			for (int j = -1; j < 2; j++) {
				stepZ=+j;
				for (int h = -1; h < 2; h++) {
					stepY=+h;
					Utils.LOG_INFO("X:"+stepX);
					Utils.LOG_INFO("Y:"+stepY);
					Utils.LOG_INFO("Z:"+stepZ);
					Utils.LOG_INFO("Block Facing - X:"+xDir+"    Z:"+zDir);
					Utils.LOG_INFO("(h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))");
					Utils.LOG_INFO("  "+(h != 0)+"   ||       "+(((xDir + i != 0)+"       ||       "+(zDir + j != 0))+"       &&    "+((i != 0)+"   ||   "+(j != 0))));
					try {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);						
						Utils.LOG_INFO("IPP - RESULT - ? - ["+tTileEntity.getXCoord()+"]  y ["+tTileEntity.getYCoord()+"]  z ["+tTileEntity.getZCoord()+"] || i ["+i+"]  j ["+j+"]  h ["+h+"]");
					} catch(Throwable t){Utils.LOG_INFO("Bad move");				
					}
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, 17)) && (!addInputToMachineList(tTileEntity, 17)) && (!addOutputToMachineList(tTileEntity, 17)) && (!addEnergyInputToMachineList(tTileEntity, 17))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings2) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
								return false;
							}
							tAmount++;
						}
					}		

				}
			}
		}
		return tAmount >= 16;
	}*/


	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int stepX = aBaseMetaTileEntity.getXCoord();
		int stepY = aBaseMetaTileEntity.getYCoord();
		int stepZ = aBaseMetaTileEntity.getZCoord();
		int temp = 0;

		Utils.LOG_INFO("Starting Block located @ "+"[X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	

		int tAmount = 0;
		switch (xDir) {
		case -1:
			stepX++;
			Utils.LOG_INFO("Modifying stepX + accomodate a "+xDir+" xDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;

		case 1:
			stepX--;
			Utils.LOG_INFO("Modifying stepX - accomodate a "+xDir+" xDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;
		}
		switch (zDir) {
		case -1:
			stepZ++;
			Utils.LOG_INFO("Modifying stepZ + accomodate a "+zDir+" zDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;

		case 1:
			stepZ--;
			Utils.LOG_INFO("Modifying stepZ - accomodate a "+zDir+" zDir - [X:"+stepX+"][Y:"+stepY+"][Z:"+stepZ+"]");	
			break;
		}

		for (int i = stepX-1; i <= stepX+1; i++){
			for (int j = stepZ-1; j <= stepZ+1; j++){
				for (int h = stepY-1; h <= stepY+1; h++){	


					Utils.LOG_INFO("Block Facing - X:"+xDir+"    Z:"+zDir);
					Utils.LOG_INFO("(h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))");
					Utils.LOG_INFO("  "+(h != 0)+"   ||       "+(((xDir + i != 0)+"       ||       "+(zDir + j != 0))+"       &&    "+((i != 0)+"   ||   "+(j != 0))));

					try {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);						
						Utils.LOG_INFO("IPP - RESULT - ? - ["+tTileEntity.getXCoord()+"]  y ["+tTileEntity.getYCoord()+"]  z ["+tTileEntity.getZCoord()+"] || i ["+i+"]  j ["+j+"]  h ["+h+"]");
					} catch(Throwable t){Utils.LOG_INFO("Checking Non-Hatch/Bus Block/Casing");				
					}
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, 17)) && (!addInputToMachineList(tTileEntity, 17)) && (!addOutputToMachineList(tTileEntity, 17)) && (!addEnergyInputToMachineList(tTileEntity, 17))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings2) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
								return false;
							}
							tAmount++;
						}
					}	
				}
			}
		}


		return tAmount >= 16;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
}
