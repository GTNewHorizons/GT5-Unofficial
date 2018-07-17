package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_FastNeutronReactor
extends GregtechMeta_MultiBlockBase
{

	private int mSuperEfficencyIncrease = 0;


	public GregtechMTE_FastNeutronReactor(int aID, String aName, String aNameRegional)
	{
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_FastNeutronReactor(String mName) {
		super(mName);
	}

	@Override
	public String getMachineType() {
		return "Reactor";
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GregtechMTE_FastNeutronReactor(this.mName);
	}
	@Override
	public boolean isFacingValid(byte aFacing)
	{
		return aFacing > 1;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack)
	{
		return true;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack){
		return 0;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		this.mSuperEfficencyIncrease=0;
		if (processing_Stage_1()) {
			if (processing_Stage_2()) {
				if (processing_Stage_3()) {
					if (processing_Stage_4()) {
						
					}
					else {
						//Stage 4
					}
				}
				else {
					//Stage 3
				}
			}
			else {
				//Stage 2
			}
		}
		else {
			//Stage 1
		}
		return false;
	}

	public boolean processing_Stage_1() { //Deplete Water, Add More Progress Time
		for (GT_MetaTileEntity_Hatch_Input tRecipe : this.mInputHatches) {
			if (tRecipe.getFluid() != null){
				FluidStack tFluid = FluidUtils.getFluidStack(tRecipe.getFluid(), 200);
				if (tFluid != null) {					
					if (tFluid == GT_ModHandler.getDistilledWater(1)) {
						if (depleteInput(tFluid)) {
							this.mMaxProgresstime = Math.max(1, runtimeBoost(8 * 2));
							this.mEUt = getEUt();
							this.mEfficiencyIncrease = (this.mMaxProgresstime * getEfficiencyIncrease());
							return true;
						}
					}				
					
				}
			}		
	}
	this.mMaxProgresstime = 0;
	this.mEUt = 0;
	return false;
	}
	public boolean processing_Stage_2() {
		return false;
	}
	public boolean processing_Stage_3() {
		return false;
	}
	public boolean processing_Stage_4() {
		return false;
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		if (this.mEUt > 0) {
			if(this.mSuperEfficencyIncrease>0){
				this.mEfficiency = Math.min(10000, this.mEfficiency + this.mSuperEfficencyIncrease);
			}
			int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 10000L);
			if (tGeneratedEU > 0) {
				long amount = (tGeneratedEU + 160) / 160;
				if (!depleteInput(GT_ModHandler.getDistilledWater(amount))) {
					explodeMultiblock();
				} else {
					addOutput(GT_ModHandler.getSteam(tGeneratedEU));
				}
			}
			return true;
		}
		return true;
	}
	
	public int getEUt() {
		return 0; //Default 400
	}
	
	public int getEfficiencyIncrease() {
		return 0; //Default 12
	}

	int runtimeBoost(int mTime) {
		return mTime * 150 / 100;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack)
	{
		return true;
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
				"Fukushima-Daiichi Reactor No. 6",
				"------------------------------------------",
				"Boiling Water Reactor",
				"Harness the power of Nuclear Fission",
				"------------------------------------------",
				"Consult user manual for more information",
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)]};
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack arg1) {
		return true;
	}

	public boolean damageFilter(){		
		return false;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

}
