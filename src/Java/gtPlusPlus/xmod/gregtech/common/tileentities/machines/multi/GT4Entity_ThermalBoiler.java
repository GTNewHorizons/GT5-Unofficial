package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT4Entity_ThermalBoiler
extends GT_MetaTileEntity_MultiBlockBase
{
	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return aFacing > 1;
	}

	public void onRightclick(EntityPlayer aPlayer)
	{
		getBaseMetaTileEntity().openGUI(aPlayer, 158);
	}

	public GT4Entity_ThermalBoiler(int aID, String aName, String aNameRegional)
	{
		super(aID, aName, aNameRegional);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT4Entity_ThermalBoiler(damageFactorLow, mNEI, mNEI);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack)
	{
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack)
	{
		return GT_Utility.areStacksEqual(aStack, ItemList.Component_LavaFilter.getWildcard(1L, new Object[0])) ? 1 : 0;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack)
	{
		Collection<GT_Recipe> hotFuels = GT_Recipe.GT_Recipe_Map.sHotFuels.mRecipeList;
		for (GT_Recipe tRecipe : hotFuels) {
			if (depleteInput(tRecipe.getRepresentativeInput(0)))
			{
				this.mEUt = 400;
				this.mMaxProgresstime = (tRecipe.mEUt * 2 / 5);
				this.mEfficiencyIncrease = (this.mMaxProgresstime * 30);
				if (tRecipe.getOutput(0) != null) {
					this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(0) }) };
				}
				if (GT_Utility.areStacksEqual(aStack, ItemList.Component_LavaFilter.getWildcard(1L, new Object[0]))) {
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

	@Override
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
			int META = 0;
			if (((getBaseMetaTileEntity().getBlockAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != GregTech_API.sBlockCasings1) || (getBaseMetaTileEntity().getMetaIDAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != META)) && 
					(!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2), 1))) {
				return false;
			}
			int tX = getBaseMetaTileEntity().getXCoord();int tY = getBaseMetaTileEntity().getYCoord();int tZ = getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte)(i + 1)) {
				for (byte j = -1; j < 2; j = (byte)(j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 3; k = (byte)(k + 1)) {
							if (((i == 0) || (j == 0)) && (k == 1))
							{
								if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == GregTech_API.sBlockCasings1)
								{
									if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == META) {}
								}
								else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)), 1)) {
									return false;
								}
							}
							else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == GregTech_API.sBlockCasings1)
							{
								if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == META) {}
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

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack)
	{
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack)
	{
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack)
	{
		return 0;
	}

	public int getAmountOfOutputs()
	{
		return 1;
	}

	@Override
	public String[] getDescription()
	{
		return new String[]{"Converts Heat into Steam"};
	}


	@Override
	public ITexture[] getTexture(IGregTechTileEntity arg0, byte arg1, byte arg2, byte arg3, boolean arg4, boolean arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
