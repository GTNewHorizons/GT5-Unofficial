package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GT_MetaTileEntity_Hatch_Reservoir extends GT_MetaTileEntity_Hatch_FluidGenerator {

	public GT_MetaTileEntity_Hatch_Reservoir(final int aID, final String aName, final String aNameRegional,	final int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_Hatch_Reservoir(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}


	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Reservoir(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public String[] getCustomTooltip() {
		String[] aTooltip = new String[3];
		aTooltip[0] = "Requires a Block of water facing the intake";
		aTooltip[1] = "Infinite water supply hatch";
		aTooltip[2] = "Creates 2000L of Water every 4 ticks";
		return aTooltip;
	}

	@Override
	public Fluid getFluidToGenerate() {
		return FluidRegistry.WATER;
	}

	@Override
	public int getAmountOfFluidToGenerate() {
		return 2000;
	}

	@Override
	public int getMaxTickTime() {
		return 4;
	}

	@Override
	public int getCapacity() {
		return 128000;
	}

	@Override
	public boolean doesHatchMeetConditionsToGenerate() {
		Block aWater = this.getBaseMetaTileEntity().getBlockAtSide(this.getBaseMetaTileEntity().getFrontFacing());
		return aWater == Blocks.water || aWater == Blocks.flowing_water;
	}

	@Override
	public void generateParticles(World aWorld, String name) {
		
	}
	
	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Water)};
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Water)};
	}

}
