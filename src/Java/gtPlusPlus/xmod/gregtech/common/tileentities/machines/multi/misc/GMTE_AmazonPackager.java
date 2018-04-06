package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemStackData;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraftforge.fluids.FluidStack;

public class GMTE_AmazonPackager extends GregtechMeta_MultiBlockBase {

	private long mVoltage;
	private byte mTier;

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GMTE_AmazonPackager(mName);
	}

	public GMTE_AmazonPackager(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		initFields();
	}

	public GMTE_AmazonPackager(String aName) {
		super(aName);
		initFields();
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Amazon Warehouse",
				"This Multiblock is used for EXTREME packaging requirements",
				""
		};
	}

	private final void initFields() {
		mVoltage = getMaxInputVoltage();
		mTier = (byte) Math.max(1, GT_Utility.getTier(mVoltage));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(7)]};
	}




	public GT_Recipe.GT_Recipe_Map getRecipeList() {
		return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes;
	}


	@Override
	public boolean checkRecipe(ItemStack aStack) {		
		ArrayList<ItemStack> tItems = getStoredInputs();
		AutoMap<ItemStackData> mCompleted = new AutoMap<ItemStackData>();
		
		for (ItemStack tInputItem : tItems) {
			if (tInputItem != null) {
				mCompleted.put(new ItemStackData(tInputItem));
				checkRecipe(tInputItem, getGUIItemStack());
			}
		}
		
		return mCompleted != null && mCompleted.size() > 0;
	}

	public boolean checkRecipe(ItemStack inputStack, ItemStack schematicStack) {		
		if (GT_Utility.isStackValid((Object) inputStack) && GT_Utility.isStackValid((Object) schematicStack)
				&& GT_Utility.getContainerItem(inputStack, true) == null) {			
			ItemStack tOutputStack;			
			if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)&& inputStack.stackSize >= 1) {				
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack});
				if (tOutputStack != null && this.addOutput(tOutputStack)) {
					final ItemStack input = inputStack;
					--input.stackSize;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					this.mMaxProgresstime = 16 / (1 << this.mTier - 1);
					return true;
				}
				return false;

			} else if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 4) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, null, inputStack, inputStack});
				if (tOutputStack != null && this.addOutput(tOutputStack)) {
					final ItemStack input2 = inputStack;
					input2.stackSize -= 4;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					this.mMaxProgresstime = 32 / (1 << this.mTier - 1);
					return true;
				}
				return false;
			} else if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 9) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, inputStack, inputStack, inputStack,
						inputStack, inputStack, inputStack, inputStack});
				if (tOutputStack != null && this.addOutput(tOutputStack)) {
					final ItemStack input3 = inputStack;
					input3.stackSize -= 9;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					this.mMaxProgresstime = 64 / (1 << this.mTier - 1);
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,	final ItemStack aStack, ItemStack schematicStack) {
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
	public boolean checkMachine(IGregTechTileEntity p0, ItemStack p1) {
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		return 10000;
	}

}
