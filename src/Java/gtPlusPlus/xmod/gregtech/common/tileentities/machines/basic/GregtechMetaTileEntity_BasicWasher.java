package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_BasicWasher extends GT_MetaTileEntity_BasicMachine {

	public GregtechMetaTileEntity_BasicWasher(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, 
				"It's like an automatic Cauldron for washing dusts.", 1, 1, "PotionBrewer.png", "",
				new ITexture[]{
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER)
		}
				);
	}

	public GregtechMetaTileEntity_BasicWasher(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	/*public GregtechMetaTileEntity_BasicWasher(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}*/

	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Grants no byproducts, but it is fast.", CORE.GT_Tooltip};
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_BasicWasher(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeList() {
		return Recipe_GT.Gregtech_Recipe_Map.sSimpleWasherRecipes;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (getRecipeList().containsInput(aStack));
	}

	@Override
	public boolean isFluidInputAllowed(FluidStack aFluid) {
		return (aFluid.getFluid().getName().equals("water")) || (super.isFluidInputAllowed(aFluid));
	}

	@Override
	public int getCapacity() {
		return 8000;
	}

}
