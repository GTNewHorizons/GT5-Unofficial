package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GT4Entity_ThermalBoiler
extends GregtechMeta_MultiBlockBase
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
	public String getMachineType() {
		return "Boiler";
	}

	@Override
	public int getDamageToComponent(ItemStack aStack){
		//log("Trying to damage component.");
		return ItemList.Component_LavaFilter.get(1L).getClass().isInstance(aStack) ? 1 : 0;
	}
	
	private static Item mLavaFilter;

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		this.mSuperEfficencyIncrease=0;
		
		if (mLavaFilter == null) {
			mLavaFilter = ItemList.Component_LavaFilter.getItem();
		}
		
		//Try reload new Lava Filter
		if (aStack == null) {
			ItemStack uStack = this.findItemInInventory(mLavaFilter);
			if (uStack != null) {				
				this.setGUIItemStack(uStack);
				aStack = this.getGUIItemStack();
			}
		}
		

		for (GT_Recipe tRecipe : Recipe_GT.Gregtech_Recipe_Map.sThermalFuels.mRecipeList) {
			FluidStack tFluid = tRecipe.mFluidInputs[0];
			if (tFluid != null) {
				if (depleteInput(tFluid)) {
					this.mMaxProgresstime = Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2));
					this.mEUt = getEUt();
					this.mEfficiencyIncrease = (this.mMaxProgresstime * getEfficiencyIncrease());

					int loot_MAXCHANCE = 100000;
					if (mLavaFilter.getClass().isInstance(aStack.getItem())) {

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

					}
					//Give Obsidian without Lava Filter
					if (tFluid.isFluidEqual(GT_ModHandler.getLava(86))){
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
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
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
		return 400;
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
		return 35;
	}

	public int getAmountOfOutputs()
	{
		return 7;
	}

	@Override
	public String[] getTooltip()
	{
		return new String[]{
				"Thermal Boiler Controller",
				"Converts Water & Heat into Steam",
				"Consult user manual for more information",
				"Size: 3x3x3 (Hollow)",
				"Thermal Containment Casings (10 at least!)",
				"Controller (front middle)",
				"2x Input Hatch (Water/Thermal Fluid)",
				"1x Output Hatch (Steam)",
				"1x Input Bus (Supplies controller with Lava Filters, optional)",
				"1x Output Bus (Filter results, optional)",
				};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(1)]};
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack arg1) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int tAmount = 0;

		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		} else {
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					for (int h = -1; h < 2; ++h) {
						if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
									h, zDir + j);
							Block aBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							int aMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

							if (!isValidBlockForStructure(tTileEntity, 1, true, aBlock, aMeta,
									ModBlocks.blockCasings2Misc, 11)) {
								log("Bad Thermal Boiler casing");
								return false;
							}
							++tAmount;

						}
					}
				}
			}
			return tAmount >= 10;
		}
	}

	public boolean damageFilter(){
		ItemStack filter = this.mInventory[1];
		if (filter != null){
			if (filter.getItem() instanceof ItemLavaFilter){

				long currentUse = ItemLavaFilter.getFilterDamage(filter);

				//Remove broken Filter
				if (currentUse >= 100-1){			
					this.mInventory[1] = null;
					return false;				
				}	
				else {
					//Do Damage
					ItemLavaFilter.setFilterDamage(filter, currentUse+1);
					return true;
				}			
			}		
		}

		return false;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()){
			//Utils.LOG_INFO("tick: "+aTick);
			if (this.mEUt > 0){
				if (aTick % 600L == 0L){
					damageFilter();
				}
			}
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "Generic3By3";
	}	

}
