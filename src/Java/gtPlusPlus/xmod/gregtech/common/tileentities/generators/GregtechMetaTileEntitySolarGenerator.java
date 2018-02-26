package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SolarGenerator;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SolarGenerator;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechMetaSolarGenerator;

public class GregtechMetaTileEntitySolarGenerator extends GregtechMetaSolarGenerator {

	public GregtechMetaTileEntitySolarGenerator(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, "Feasts on the power of the Sun!", new ITexture[0]);
		this.onConfigLoad();
	}

	public GregtechMetaTileEntitySolarGenerator(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		this.onConfigLoad();
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide == this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_SolarGenerator(aPlayerInventory, aBaseMetaTileEntity, 16000);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SolarGenerator(aPlayerInventory, aBaseMetaTileEntity, "SolarBoiler.png", 16000);
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntitySolarGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "SunAbsorber.efficiency.tier." + this.mTier, 100 - (this.mTier * 10));

	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
				&& (aBaseMetaTileEntity.getUniversalEnergyStored() < (this.maxEUOutput() + aBaseMetaTileEntity.getEUCapacity()))) {

			if (this.mSolarCharge <= 20) {
				//Utils.LOG_WARNING("1.");
				this.mSolarCharge = 20;
				this.mLossTimer = 0;
			}
			if (++this.mLossTimer > 45) {
				//Utils.LOG_WARNING("2.");
				this.mSolarCharge -= 1;
				this.mLossTimer = 0;
			}

			if ((aTick % 10L) == 0L) {

				Logger.WARNING("getUniversalEnergyStored: "+aBaseMetaTileEntity.getUniversalEnergyStored() + "    maxEUOutput * 20 + getMinimumStoredEU: " + ((this.maxEUOutput() * 20) + this.getMinimumStoredEU()));

				if ((this.mSolarCharge > 100) && (aBaseMetaTileEntity.isAllowedToWork()) &&
						(!aBaseMetaTileEntity.getWorld().isThundering()) &&
						(aBaseMetaTileEntity.getUniversalEnergyStored() < (this.maxEUStore() - this.getMinimumStoredEU()))) {
					this.getBaseMetaTileEntity().increaseStoredEnergyUnits(sEnergyPerTick * this.getEfficiency(), true);
				}
			}

			if ((this.mSolarCharge < 500) && (this.mProcessingEnergy != 0) && ((aTick % 32L) == 0L)) {
				Logger.WARNING("Adding Solar Charge. Currently "+this.mSolarCharge);
				this.mProcessingEnergy -= 1;
				this.mSolarCharge += 1;
			}

			if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && ((aTick % 64L) == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering())) {
				Logger.WARNING("Adding Processing Energy. Currently "+this.mProcessingEnergy);
				final boolean bRain = aBaseMetaTileEntity.getWorld().isRaining() && (aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
				this.mProcessingEnergy += (bRain && (aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4)) || !aBaseMetaTileEntity.getSkyAtSide((byte) 1) ? 0 : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 : 1;
			}

			if (aBaseMetaTileEntity.isServerSide()){
				//Utils.LOG_WARNING("6.");
				aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork() && (aBaseMetaTileEntity.getUniversalEnergyStored() >= (this.maxEUOutput() + this.getMinimumStoredEU())));
			}
		}
	}

	@Override
	public void inValidate() {

	}

	@Override
	public int getEfficiency() {
		return this.mEfficiency;
	}

	@Override
	public long maxEUStore() {
		return Math.max(this.getEUVar(), (V[this.mTier] * 16000) + this.getMinimumStoredEU());
	}


	ITexture SolarArray[] = {new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_8V),
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_MV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_HV),
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_EV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_IV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LuV),
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_ZPM), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_UV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};


	@Override
	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};
	}

	@Override
	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT_ACTIVE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]};
	}

	@Override
	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

	@Override
	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

	@Override
	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};
	}

	@Override
	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

}