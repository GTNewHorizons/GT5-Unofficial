package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemStackData;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GMTE_AmazonPackager extends GregtechMeta_MultiBlockBase {

	private long mVoltage;
	private byte mTier;
	private int mCasing;
	private IStructureDefinition<GMTE_AmazonPackager> STRUCTURE_DEFINITION = null;

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GMTE_AmazonPackager(mName);
	}

	public GMTE_AmazonPackager(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GMTE_AmazonPackager(String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Packager";
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "Generic3By3";
	}

	@Override
	public IStructureDefinition<GMTE_AmazonPackager> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GMTE_AmazonPackager>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"C~C", "C-C", "CCC"},
							{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GMTE_AmazonPackager::addAmazonPackagerList, TAE.getIndexFromPage(2, 9), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings3Misc, 9
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}



	public final boolean addAmazonPackagerList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Amazon Warehouse")
				.addInfo("This Multiblock is used for EXTREME packaging requirements")
				.addInfo("Dust Schematics are inserted into the input busses")
				.addInfo("If inserted into the controller, it is shared across all busses")
				.addInfo("1x, 2x, 3x & Other Schematics are to be placed into the controller GUI slot")
				.addInfo("Uncomparably fast compared to a single packager of the same tier")
				.addInfo("Only uses 75% of the eu/t normally required")
				.addInfo("Processes five items per voltage tier")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front center")
				.addCasingInfo("Supply Depot Casings", 10)
				.addInputBus("Any casing", 1)
				.addOutputBus("Any casing", 1)
				.addEnergyHatch("Any casing", 1)
				.addMaintenanceHatch("Any casing", 1)
				.addMufflerHatch("Any casing", 1)
				.toolTipFinisher("GT++");
		return tt;
	}

	private final void initFields() {
		mVoltage = getMaxInputVoltage();
		mTier = (byte) Math.max(1, GT_Utility.getTier(mVoltage));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(2, 1)), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(2, 1))};
	}



	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes;
	}

	public void sortInputBusses() {
		for (GT_MetaTileEntity_Hatch_InputBus h : this.mInputBusses) {
			h.updateSlots();
		}
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {	
		
		//Just the best place to check this~
		initFields();
		
		ArrayList<ItemStack> tItems = getStoredInputs();
		if (this.getGUIItemStack() != null) {
			tItems.add(this.getGUIItemStack());		
		}
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
		boolean state = checkRecipeGeneric(tItemInputs, tFluidInputs, 5 * GT_Utility.getTier(this.getMaxInputVoltage()), 75, 500, 10000);


		if (state) {
			return true;
		}
		else {
			tItems = getStoredInputs();
			AutoMap<ItemStackData> mCompleted = new AutoMap<ItemStackData>();
			AutoMap<ItemStackData> mSchematics = new AutoMap<ItemStackData>();
			for (ItemStack tInputItem : tItems) {
				if (tInputItem != null) {					
					if (ItemList.Schematic_1by1.isStackEqual((Object) tInputItem) || ItemList.Schematic_2by2.isStackEqual((Object) tInputItem) || ItemList.Schematic_3by3.isStackEqual((Object) tInputItem)) {
						mSchematics.put(new ItemStackData(tInputItem));
					}
				}
			}
			if (mSchematics.size() > 0) {
				for (ItemStackData g : mSchematics) {
					for (ItemStack tInputItem : tItems) {
						if (tInputItem != null) {							
							mCompleted.put(new ItemStackData(tInputItem));
							checkRecipe(tInputItem, g.getStack());	
						}
					}
				}
			}
			
			return mCompleted != null && mCompleted.size() > 0;
		}		
	}

	public boolean checkRecipe(ItemStack inputStack, ItemStack schematicStack) {		
		if (GT_Utility.isStackValid((Object) inputStack) && GT_Utility.isStackValid((Object) schematicStack)
				&& GT_Utility.getContainerItem(inputStack, true) == null) {			
			ItemStack tOutputStack;			
			if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)&& inputStack.stackSize >= 1) {				
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input = inputStack;
					--input.stackSize;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 16 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 2;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			} else if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 4) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, null, inputStack, inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input2 = inputStack;
					input2.stackSize -= 4;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 32 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 4;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			} else if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 9) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, inputStack, inputStack, inputStack,
						inputStack, inputStack, inputStack, inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input3 = inputStack;
					input3.stackSize -= 9;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 64 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 6;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean allowPutStack(final ItemStack aStack, ItemStack schematicStack) {
		//If Schematic Static is not 1x1, 2x2, 3x3
		if (!ItemList.Schematic_1by1.isStackEqual((Object) schematicStack) && !ItemList.Schematic_2by2.isStackEqual((Object) schematicStack) && !ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)) {
			return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.containsInput(aStack);
		}
		//Something
		if (GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.findRecipe((IHasWorldObjectAndCoords) this.getBaseMetaTileEntity(), true, GT_Values.V[this.mTier],
				(FluidStack[]) null, new ItemStack[]{GT_Utility.copyAmount(64L, new Object[]{aStack}), schematicStack}) != null) {
			return true;
		}
		//1x1
		if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)
				&& GT_ModHandler.getRecipeOutput(new ItemStack[]{aStack}) != null) {
			return true;
		}
		//2x2
		if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
				&& GT_ModHandler.getRecipeOutput(new ItemStack[]{aStack, aStack, null, aStack, aStack}) != null) {
			return true;
		}
		//3x3
		if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack) && GT_ModHandler.getRecipeOutput(
				new ItemStack[]{aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack}) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(ItemStack arg0){
		return CORE.ConfigSwitches.pollutionPerSecondMultiPackager;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 9;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
	}
}
