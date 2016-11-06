package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import static gregtech.api.enums.GT_Values.V;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SolarGenerator;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SolarGenerator;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechMetaSolarGenerator;
import net.minecraft.entity.player.InventoryPlayer;

public class GregtechMetaTileEntitySolarGenerator extends GregtechMetaSolarGenerator {

	public GregtechMetaTileEntitySolarGenerator(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, "Feasts on the power of the Sun!", new ITexture[0]);
		onConfigLoad();
	}

	public GregtechMetaTileEntitySolarGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		onConfigLoad();
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide == getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_SolarGenerator(aPlayerInventory, aBaseMetaTileEntity, 16000);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SolarGenerator(aPlayerInventory, aBaseMetaTileEntity, "SolarBoiler.png", 16000);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntitySolarGenerator(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public void onConfigLoad() {
		this.mEfficiency = GregTech_API.sMachineFile.get(ConfigCategories.machineconfig, "SunAbsorber.efficiency.tier." + this.mTier, 100 - this.mTier * 10);

	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
				&& aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() + aBaseMetaTileEntity.getEUCapacity()) {

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

			if (aTick % 10L == 0L) {

				Utils.LOG_WARNING("getUniversalEnergyStored: "+aBaseMetaTileEntity.getUniversalEnergyStored() + "    maxEUOutput * 20 + getMinimumStoredEU: " + (maxEUOutput() * 20 + getMinimumStoredEU()));

				if ((this.mSolarCharge > 100) && (aBaseMetaTileEntity.isAllowedToWork()) && 
						(!aBaseMetaTileEntity.getWorld().isThundering()) && 
						aBaseMetaTileEntity.getUniversalEnergyStored() < (maxEUStore() - getMinimumStoredEU())) {
					getBaseMetaTileEntity().increaseStoredEnergyUnits(sEnergyPerTick * getEfficiency(), true);
				}
			}			 

			if ((this.mSolarCharge < 500) && (this.mProcessingEnergy != 0) && (aTick % 32L == 0L)) {
				Utils.LOG_WARNING("Adding Solar Charge. Currently "+mSolarCharge);
				this.mProcessingEnergy -= 1;
				this.mSolarCharge += 1;
			}

			if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 64L == 0L) && (!aBaseMetaTileEntity.getWorld().isThundering())) {
				Utils.LOG_WARNING("Adding Processing Energy. Currently "+mProcessingEnergy);
				boolean bRain = aBaseMetaTileEntity.getWorld().isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F;
				mProcessingEnergy += bRain && aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4 || !aBaseMetaTileEntity.getSkyAtSide((byte) 1) ? 0 : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 : 1;
			}

			if (aBaseMetaTileEntity.isServerSide()){
				//Utils.LOG_WARNING("6.");
				aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU());
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
		return Math.max(getEUVar(), V[mTier] * 16000 + getMinimumStoredEU());
	}


	ITexture SolarArray[] = {new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_8V), 
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_MV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_HV),
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_EV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_IV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LuV),
			new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_ZPM), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_UV), new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};


	@Override
	public ITexture[] getFront(byte aColor) {
		return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	@Override
	public ITexture[] getBack(byte aColor) {
		return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getBottom(byte aColor) {
		return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getTop(byte aColor) {
		return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};
	}

	@Override
	public ITexture[] getSides(byte aColor) {
		return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
	}

	@Override
	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT_ACTIVE),
				Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]};
	}

	@Override
	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

	@Override
	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

	@Override
	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL)};
	}

	@Override
	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
	}

}