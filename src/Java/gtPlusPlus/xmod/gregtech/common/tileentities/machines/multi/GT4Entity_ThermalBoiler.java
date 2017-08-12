package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT4Entity_ThermalBoiler
extends GT_MetaTileEntity_MultiBlockBase
{

	private int mSuperEfficencyIncrease = 0;

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

	public GT4Entity_ThermalBoiler(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT4Entity_ThermalBoiler(this.mName);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack)
	{
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack){
		Utils.LOG_INFO("Trying to damage component.");
		return ItemList.Component_LavaFilter.get(1L).getClass().isInstance(aStack) ? 1 : 0;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		this.mSuperEfficencyIncrease=0;

		for (GT_Recipe tRecipe : Recipe_GT.Gregtech_Recipe_Map.sThermalFuels.mRecipeList) {
			FluidStack tFluid = tRecipe.mFluidInputs[0];
			if (tFluid != null) {
				if (depleteInput(tFluid)) {
					this.mMaxProgresstime = Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2));
					this.mEUt = getEUt();
					this.mEfficiencyIncrease = (this.mMaxProgresstime * getEfficiencyIncrease());

					if (ItemList.Component_LavaFilter.get(1L).getClass().isInstance(aStack)) {
						int loot_MAXCHANCE = 100000;
						if ((tRecipe.getOutput(0) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(0))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(0) }) };
						} 
						if ((tRecipe.getOutput(1) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(1))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(1) }) };
						} 
						if ((tRecipe.getOutput(2) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(2))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(2) }) };
						} 
						if ((tRecipe.getOutput(3) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(3))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(3) }) };
						} 
						if ((tRecipe.getOutput(4) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(4))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(4) }) };
						} 
						if ((tRecipe.getOutput(5) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(5))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(5) }) };
						} 
						if ((tRecipe.getOutput(6) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(6))) {
							this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(6) }) };
						}
					}

					return true;
				}
			}
		}
		this.mMaxProgresstime = 0;
		this.mEUt = 0;
		return false;
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		if (this.mEUt > 0) {
			if(this.mSuperEfficencyIncrease>0)this.mEfficiency = Math.min(10000, this.mEfficiency + this.mSuperEfficencyIncrease);
			int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 10000L);
			if (tGeneratedEU > 0) {
				long amount = (tGeneratedEU + 160) / 160;
				if (depleteInput(Materials.Water.getFluid(amount)) || depleteInput(GT_ModHandler.getDistilledWater(amount))) {
					addOutput(GT_ModHandler.getSteam(tGeneratedEU));
				} else {
					explodeMultiblock();
				}
			}
			return true;
		}
		return true;
	}

	public int getEUt() {
		return 600;
	}

	public int getEfficiencyIncrease() {
		return 12;
	}

	int runtimeBoost(int mTime) {
		return mTime * 150 / 100;
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
		return 7;
	}

	@Override
	public String[] getDescription()
	{
		return new String[]{
				"Thermal Boiler Controller",
				"Converts Heat into Steam",
				"Size: 3x3x3 (Hollow)",
				"Controller (front middle)",
				"2x Output Hatch",
				"2x Input Hatch",
				"1x Maintenance Hatch (Centre of top)",
				"Thermal Containment Casings for the rest",
				"Input & Output hatches can go be configured how you wish",
				"Place them in the middle block of the Back, Bottom and Sides",
				"Consult user manual for more information",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)]};
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity arg0, ItemStack arg1) {
		byte tSide = getBaseMetaTileEntity().getBackFacing();
		if (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1))
		{
			int META = 11;
			int CASING = TAE.GTPP_INDEX(1);
			if (((getBaseMetaTileEntity().getBlockAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != ModBlocks.blockCasings2Misc) || (getBaseMetaTileEntity().getMetaIDAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2) != META)) && 
					(!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2), CASING))) {
				return false;
			}
			int tX = getBaseMetaTileEntity().getXCoord();int tY = getBaseMetaTileEntity().getYCoord();int tZ = getBaseMetaTileEntity().getZCoord();
			for (byte i = -1; i < 2; i = (byte)(i + 1)) {
				for (byte j = -1; j < 2; j = (byte)(j + 1)) {
					if ((i != 0) || (j != 0)) {
						for (byte k = 0; k < 3; k = (byte)(k + 1)) {
							if (((i == 0) || (j == 0)) && (k == 1))
							{
								if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == ModBlocks.blockCasings2Misc)
								{
									if (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == META) {}
								}
								else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)), CASING)) {
									return false;
								}
							}
							else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide < 4 ? i : -k), tY + j, tZ + (tSide < 4 ? -k : tSide == 3 ? k : i)) == ModBlocks.blockCasings2Misc)
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

	public boolean damageFilter(){
		ItemStack filter = this.mInventory[0];
		if (filter != null){
			if (filter.getItem() instanceof ItemLavaFilter){

				long currentUse = ItemLavaFilter.getFilterDamage(filter);

				//Remove broken Filter
				if (filter.getItemDamage() == 0 && currentUse >= 100-1){			
					this.mInventory[0] = null;
					return false;				
				}	
				else {
					//Do Damage
					ItemAirFilter.setFilterDamage(filter, currentUse+1);
					Utils.LOG_WARNING("Lava Filter Damage: "+currentUse);
					return true;
				}			
			}		
		}
		return false;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aTick % 600L == 0L){
			damageFilter();
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

}
