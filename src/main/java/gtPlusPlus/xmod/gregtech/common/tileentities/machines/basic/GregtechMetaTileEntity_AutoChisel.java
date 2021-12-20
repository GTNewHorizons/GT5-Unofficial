package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.List;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import team.chisel.carving.Carving;

public class GregtechMetaTileEntity_AutoChisel extends GT_MetaTileEntity_BasicMachine {

	private ItemStack mInputCache;
	private ItemStack mOutputCache;
	
	public GregtechMetaTileEntity_AutoChisel(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, "Chisels things, Gregtech style", 1, 1, "Compressor.png", "", 
				new ITexture[]{
						new GT_RenderedTexture(BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
						new GT_RenderedTexture(BlockIcons.OVERLAY_SIDE_MASSFAB),
						new GT_RenderedTexture(BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE),
						new GT_RenderedTexture(BlockIcons.OVERLAY_FRONT_MULTI_SMELTER),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab),
						new GT_RenderedTexture(BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
						new GT_RenderedTexture(BlockIcons.OVERLAY_BOTTOM_MASSFAB)
		});
	}

	public GregtechMetaTileEntity_AutoChisel(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_AutoChisel(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
	}

	@Override
	public String[] getDescription() {
		String[] A = new String[]{
				this.mDescription,
				"What you want to chisel goes in slot 1",
				"What you want to get goes in the special slot (bottom right)",
				"If special slot is empty, first chisel result is used"
		};		
		return A;
	}
	
    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return null;
    }
    
    private boolean hasValidCache(ItemStack mItem, boolean mClearOnFailure) {
        if (mInputCache != null
                && mOutputCache != null
                && mInputCache.isItemEqual(mItem)
                && ItemStack.areItemStackTagsEqual(mItem, mInputCache)) {
        	return true;
        }
        // clear cache if it was invalid
        if (mClearOnFailure) {
            mInputCache = null;
            mOutputCache = null;
        }
        return false;
    }
    
    private void cacheItem(ItemStack mInputItem, ItemStack mOutputItem) {
        mOutputCache = mOutputItem.copy();
        mInputCache = mInputItem.copy();
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return hasValidCache(aStack, false) ? true : super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack) && hasChiselResults(aStack);
    }
    
	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private static boolean canBeMadeFrom(ItemStack from, ItemStack to) {
		List<ItemStack> results = getItemsForChiseling(from);
		for (ItemStack s : results) {
			if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
				return true;
			}
		}
		return false;
	}
	
	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private static boolean hasChiselResults(ItemStack from) {
		List<ItemStack> results = getItemsForChiseling(from);		
		return results.size() > 0;
	}
	
	private static List<ItemStack> getItemsForChiseling(ItemStack aStack){
		return Carving.chisel.getItemsForChiseling(aStack);
	}
	
	private static ItemStack getChiselOutput(ItemStack aInput, ItemStack aTarget) {
		ItemStack tOutput = null;
		if (aTarget != null && canBeMadeFrom(aInput, aTarget)) {
			tOutput = aTarget;
		}
		else {
			tOutput = getItemsForChiseling(aInput).get(0);
		}
		return tOutput;
	}
	
    @Override
    public int checkRecipe() {
    	ItemStack tOutput = null;
    	ItemStack aInput = getInputAt(0);
    	ItemStack aTarget = getSpecialSlot();
        boolean tIsCached = hasValidCache(aInput, true);
    	if (aInput != null && hasChiselResults(aInput) && aInput.stackSize > 0) {
    		tOutput = tIsCached ? mOutputCache.copy() : getChiselOutput(aInput, aTarget);    		
    		if (tOutput != null) {
    			// We can chisel this
    			if (canOutput(tOutput)) {
        			getInputAt(0).stackSize -= 1;
        			calculateOverclockedNess(16, 20);
        			//In case recipe is too OP for that machine
        			if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) {
        				return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        			}
        			if (!tIsCached) {
        				cacheItem(aInput, tOutput);
        			}
        			this.mOutputItems[0] = tOutput.copy();
        			return FOUND_AND_SUCCESSFULLY_USED_RECIPE;	
        		}
    			else {
    	            mOutputBlocked++;
    	            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
    	        }
    		}
    	}
    	return DID_NOT_FIND_RECIPE;
    }
	
}