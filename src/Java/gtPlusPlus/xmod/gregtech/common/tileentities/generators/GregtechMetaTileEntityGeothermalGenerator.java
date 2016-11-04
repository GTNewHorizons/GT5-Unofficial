package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntityGeothermalGenerator extends GT_MetaTileEntity_BasicGenerator {

	public int mEfficiency;

	public GregtechMetaTileEntityGeothermalGenerator(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier, "Requires Pahoehoe Lava or Normal Lava as Fuel", new ITexture[0]);
		this.onConfigLoad();
	}

	public GregtechMetaTileEntityGeothermalGenerator(final String aName, final int aTier, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		this.onConfigLoad();
	}

	@Override
	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] {
				super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK),
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Vertical)
		};
	}

	@Override
	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[] {
				super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BACK_ACTIVE),
				new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Diesel_Vertical_Active)
		};
	}

	@Override
	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] {
				super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM)
		};
	}

	@Override
	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[] {
				super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_BOTTOM_ACTIVE)
		};
	}

	@Override
	public int getCapacity() {
		// return MathUtils.roundToClosestMultiple(32000*(this.mTier/2), 25000);
		return 5000 * this.mTier;
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Generates power from Lava/Pahoehoe at " + this.getEfficiency() + "% Efficiency per tick"
		};
	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}

	@Override
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] {
				super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
		};
	}

	@Override
	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[] {
				super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
		};
	}

	@Override
	public int getFuelValue(final ItemStack aStack) {
		int rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
		if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
			rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3);
		}
		return rValue;
	}

	public int getPollution() {
		return 100;
	}

	@Override
	public GT_Recipe_Map getRecipes() {
		return GT_Recipe_Map.sHotFuels;
	}

	@Override
	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] {
				super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.BOILER_LAVA_FRONT)
		};
	}

	@Override
	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[] {
				super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE)
		};
	}

	@Override
	public ITexture[] getTop(final byte aColor) {
		return new ITexture[] {
				super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
				new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER)
		};
	}

	@Override
	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[] {
				super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.DIESEL_GENERATOR_SIDE),
				new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE)
		};
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityGeothermalGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig,
				"ThermalGenerator.efficiency.tier." + this.mTier, 100 - this.mTier * 7);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}
}