package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_Hatch_Reservoir extends GT_MetaTileEntity_Hatch_FluidGenerator {

	private static Block sBlock_EIO;
	private static Block sBlock_RIO;
	
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
	
	private static void setCrossModData() {
		if (LoadedMods.EnderIO && sBlock_EIO == null) {
			sBlock_EIO = GameRegistry.findBlock("EnderIO", "blockReservoir");
		}
		if (LoadedMods.RemoteIO && sBlock_RIO == null) {
			sBlock_RIO = GameRegistry.findBlock("RIO", "machine");
		}
	}
	
	public static boolean isTileValid(TileEntity aTile) {
		if (aTile != null) {
			if (aTile instanceof IFluidHandler) {
				IFluidHandler aFluidHandler = (IFluidHandler) aTile;
				return aFluidHandler.canDrain(ForgeDirection.UNKNOWN, FluidRegistry.WATER);
			}
		}
		return false;
	}

	@Override
	public boolean doesHatchMeetConditionsToGenerate() {
		Block aWater = this.getBaseMetaTileEntity().getBlockAtSide(this.getBaseMetaTileEntity().getFrontFacing());
		if (aWater != null && aWater != Blocks.air) {
			if (!this.canTankBeFilled()) {
				return false;
			}
			setCrossModData();
			if (LoadedMods.EnderIO) {
				if (aWater == sBlock_EIO) {
					return isTileValid(this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getFrontFacing()));				
				}			
			}
			if (LoadedMods.RemoteIO) {
				if (aWater == sBlock_RIO && this.getBaseMetaTileEntity().getMetaIDAtSide(this.getBaseMetaTileEntity().getFrontFacing()) == 0) {
					return isTileValid(this.getBaseMetaTileEntity().getTileEntityAtSide(this.getBaseMetaTileEntity().getFrontFacing()));
				}
			}
			return aWater == Blocks.water || aWater == Blocks.flowing_water;
		}
		return false;		
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
