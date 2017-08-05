package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntity_RTG_Generator extends GT_MetaTileEntity_BasicGenerator {
	public int mEfficiency;

	//Generates fuel value based on MC days
	public static int convertDaysToTicks(float days){
		int value = 0;
		value = MathUtils.roundToClosestInt(20*86400*days);
		return value;
	}
	
	
	public GregtechMetaTileEntity_RTG_Generator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, "Requires RTG Pellets", new ITexture[0]);
	}

	public GregtechMetaTileEntity_RTG_Generator(String aName, int aTier, String aDescription,
			ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public boolean isOutputFacing(byte aSide) {
		return ((aSide > 1) && (aSide != getBaseMetaTileEntity().getFrontFacing())
				&& (aSide != getBaseMetaTileEntity().getBackFacing()));
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_RTG_Generator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public GT_Recipe.GT_Recipe_Map getRecipes() {
		return Recipe_GT.Gregtech_Recipe_Map.sRTGFuels;
	}

	public int getCapacity() {
		return 0;
	}

	public int getEfficiency() {
		return this.mEfficiency = 100;
	}

	public ITexture[] getFront(byte aColor) {
		return new ITexture[] { super.getFront(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT) };
	}

	public ITexture[] getBack(byte aColor) {
		return new ITexture[] { super.getBack(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK) };
	}

	public ITexture[] getBottom(byte aColor) {
		return new ITexture[] { super.getBottom(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM) };
	}

	public ITexture[] getTop(byte aColor) {
		return new ITexture[] { super.getTop(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
	}

	public ITexture[] getSides(byte aColor) {
		return new ITexture[] { super.getSides(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE) };
	}

	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[] { super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE) };
	}

	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[] { super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK_ACTIVE) };
	}

	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[] { super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE) };
	}

	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[] { super.getTopActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
	}

	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[] { super.getSidesActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE) };
	}

	public int getPollution() {
		return 0;
	}
}