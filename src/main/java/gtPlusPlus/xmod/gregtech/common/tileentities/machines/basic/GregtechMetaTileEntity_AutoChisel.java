package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.List;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import team.chisel.carving.Carving;

public class GregtechMetaTileEntity_AutoChisel extends GT_MetaTileEntity_BasicMachine {

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

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack) && hasChiselResults(aStack);
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
	
    @Override
    public int checkRecipe() {
    	ItemStack tOutput = null;
    	ItemStack aInput = getInputAt(0);
    	ItemStack aTarget = getSpecialSlot();
    	if (aInput != null && hasChiselResults(aInput) && aInput.stackSize > 0) {
    		Logger.INFO("Has Valid Input.");
    		if (aTarget != null && canBeMadeFrom(aInput, aTarget)) {
    			tOutput = aTarget;
        		Logger.INFO("Has Valid Target.");
    		}
    		else {
    			tOutput = getItemsForChiseling(aInput).get(0);
        		Logger.INFO("Using target(0)");
    		}
    		if (tOutput != null) {
        		Logger.INFO("Has Valid Output. "+tOutput.getDisplayName());
    			// We can chisel this
    			if (canOutput(tOutput)) {
    	    		Logger.INFO("Can Output");
        			getInputAt(0).stackSize -= 1;
    	    		Logger.INFO("Consuming 1 input");
        			calculateOverclockedNess(16, 20);
    	    		Logger.INFO("Did Overclock");
        			//In case recipe is too OP for that machine
        			if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1) {
        	    		Logger.INFO("Brrrrr");
        				return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        			}
    	    		Logger.INFO("Setting Output");
        			this.mOutputItems[0] = tOutput;
            		Logger.INFO("Recipe good.");
        			return FOUND_AND_SUCCESSFULLY_USED_RECIPE;	
        		}
    			else {
    	    		Logger.INFO("Cannot Output");
    	            mOutputBlocked++;
    	            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
    	        }
    		}
    	}
		Logger.INFO("Recipe bad.");
    	return DID_NOT_FIND_RECIPE;
    }
	
}