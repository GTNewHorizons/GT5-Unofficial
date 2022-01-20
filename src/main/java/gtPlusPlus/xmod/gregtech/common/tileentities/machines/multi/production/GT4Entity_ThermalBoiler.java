package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GT4Entity_ThermalBoiler extends GregtechMeta_MultiBlockBase<GT4Entity_ThermalBoiler> {
	
	private int mCasing;
	private IStructureDefinition<GT4Entity_ThermalBoiler> STRUCTURE_DEFINITION = null;
	private int mSuperEfficencyIncrease = 0;

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
	private static Fluid mLava = null;
	private static Fluid mPahoehoe = null;
	private static Fluid mSolarSaltHot = null;

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		this.mSuperEfficencyIncrease=0;
		
		if (mLavaFilter == null) {
			mLavaFilter = ItemList.Component_LavaFilter.getItem();
		}
		if (mLava == null) {
			mLava = FluidRegistry.LAVA;
		}
		if (mPahoehoe == null) {
			mPahoehoe = FluidUtils.getPahoehoeLava(1).getFluid();
		}
		if (mSolarSaltHot == null) {
			mSolarSaltHot = MISC_MATERIALS.SOLAR_SALT_HOT.getFluid();
		}
		
		
		
		//Try reload new Lava Filter
		if (aStack == null) {
			ItemStack uStack = this.findItemInInventory(mLavaFilter);
			if (uStack != null) {				
				this.setGUIItemStack(uStack);
				aStack = this.getGUIItemStack();
			}
		}
		

		for (GT_Recipe tRecipe : GTPP_Recipe.GTPP_Recipe_Map.sThermalFuels.mRecipeList) {
			FluidStack tFluid = tRecipe.mFluidInputs[0];
			if (tFluid != null) {
				
				if (tFluid.getFluid() == mLava || tFluid.getFluid() == mPahoehoe) {
					if (depleteInput(tFluid)) {
						this.mMaxProgresstime = Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2));
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
						if (tFluid.getFluid() == mLava){
							if ((tRecipe.getOutput(6) != null) && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE) < tRecipe.getOutputChance(6))) {
								this.mOutputItems = new ItemStack[] { GT_Utility.copy(new Object[] { tRecipe.getOutput(6) }) };
							}
						}
						return true;
					}
				}
				else if (tFluid.getFluid() == mSolarSaltHot) {
					if (depleteInput(tFluid)) {
						this.mMaxProgresstime = tRecipe.mDuration;
						this.mEfficiency = 10000;						
						for (FluidStack aOutput : tRecipe.mFluidOutputs) {
							this.addOutput(FluidUtils.getFluidStack(aOutput, aOutput.amount));
						}
						return true;
					}
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
	public int getPollutionPerSecond(ItemStack aStack)
	{
		return CORE.ConfigSwitches.pollutionPerSecondMultiThermalBoiler;
	}

	public int getAmountOfOutputs()
	{
		return 7;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Thermal Boiler Controller")
				.addInfo("Converts Water & Heat into Steam")
				.addInfo("Consult user manual for more information")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front Center")
				.addCasingInfo("Thermal Containment Casings", 10)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addOutputHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(1)),
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(1))};
	}

	@Override
	public IStructureDefinition<GT4Entity_ThermalBoiler> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GT4Entity_ThermalBoiler>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"C~C", "C-C", "CCC"},
							{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GT4Entity_ThermalBoiler::addThermalBoilerList, TAE.getIndexFromPage(0, 1), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings2Misc, 11
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
	}

	public final boolean addThermalBoilerList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	public void damageFilter(){
		ItemStack filter = this.mInventory[1];
		if (filter != null){
			if (filter.getItem() instanceof ItemLavaFilter){

				long currentUse = ItemLavaFilter.getFilterDamage(filter);

				//Remove broken Filter
				if (currentUse >= 100-1){			
					this.mInventory[1] = null;
				}
				else {
					//Do Damage
					ItemLavaFilter.setFilterDamage(filter, currentUse+1);
				}
			}		
		}

	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()){
			// Reload Lava Filter
			if (this.getGUIItemStack() == null) {
				if (this.mInputBusses.size() > 0) {
					for (GT_MetaTileEntity_Hatch_InputBus aBus : this.mInputBusses) {
						for (ItemStack aStack : aBus.mInventory) {
							if (aStack != null && aStack.getItem() instanceof ItemLavaFilter) {
								this.setGUIItemStack(aStack);								
							}
						}						
					}
				}
			}			
			
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
