package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT4Entity_AutoCrafter
extends GT_MetaTileEntity_MultiBlockBase
{
	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return aFacing > 1;
	}

	public void onRightclick(EntityPlayer aPlayer) {}

	public GT4Entity_AutoCrafter(int aID, String aName, String aNameRegional)
	{
		super(aID, aName, aNameRegional);
	}


	public GT4Entity_AutoCrafter(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT4Entity_AutoCrafter(this.mName);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack)
	{
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack)
	{
		return 0;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack)
	{
		return false;
	}

	@Override
	public boolean onRunningTick(ItemStack aStack)
	{
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
		return new String[]{
				"Highly Advanced Autocrafter",
				CORE.GT_Tooltip
				};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity p0, byte p1, byte p2, byte p3, boolean p4, boolean p5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity p0, ItemStack p1) {
		byte tSide = getBaseMetaTileEntity().getBackFacing();
		int META = 2;
		if (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1))
		{
			if (((getBaseMetaTileEntity().getBlockAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != GregTech_API.sBlockCasings1) || (getBaseMetaTileEntity().getMetaIDAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != META)) && 
					(!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2), 2))) {
				return false;
			}
			int tX = getBaseMetaTileEntity().getXCoord();int tY = getBaseMetaTileEntity().getYCoord();int tZ = getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte)(i + 1)) {
				for (byte j = -1; j < 2; j = (byte)(j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 5; k = (byte)(k + 1)) {
							if (((i == 0) || (j == 0)) && (k > 0) && (k < 4))
							{
								if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == GregTech_API.sBlockCasings1)
								{
									if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == META) {}
								}
								else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)), 2)) {
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
}
