package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import static gregtech.api.enums.GT_Values.V;

import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechMetaSolarGenerator;

public class GregtechMetaTileEntitySolarGenerator extends GregtechMetaSolarGenerator {

    public GregtechMetaTileEntitySolarGenerator(final int aID, final String aName, final String aNameRegional,
            final int aTier) {
        super(aID, aName, aNameRegional, aTier, "Feasts on the power of the Sun!", new ITexture[0]);
        this.onConfigLoad();
    }

    public GregtechMetaTileEntitySolarGenerator(final String aName, final int aTier, final String[] aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.onConfigLoad();
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
                this.mDescriptionArray,
                "Generates power at " + this.getEfficiency() + "% Efficiency per tick",
                "Output Voltage: " + this.getOutputTier() + " EU/t",
                CORE.GT_Tooltip.get());
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntitySolarGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile.get(
                ConfigCategories.machineconfig,
                "SunAbsorber.efficiency.tier." + this.mTier,
                100 - (this.mTier * 10));
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork()
                && (aBaseMetaTileEntity.getUniversalEnergyStored()
                        < (this.maxEUOutput() + aBaseMetaTileEntity.getEUCapacity()))) {

            if (this.mSolarCharge <= 20) {
                // Utils.LOG_WARNING("1.");
                this.mSolarCharge = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > 45) {
                // Utils.LOG_WARNING("2.");
                this.mSolarCharge -= 1;
                this.mLossTimer = 0;
            }

            if ((aTick % 10L) == 0L) {

                Logger.WARNING(
                        "getUniversalEnergyStored: " + aBaseMetaTileEntity.getUniversalEnergyStored()
                                + "    maxEUOutput * 20 + getMinimumStoredEU: "
                                + ((this.maxEUOutput() * 20) + this.getMinimumStoredEU()));

                if ((this.mSolarCharge > 100) && (aBaseMetaTileEntity.isAllowedToWork())
                        && (!aBaseMetaTileEntity.getWorld().isThundering())
                        && (aBaseMetaTileEntity.getUniversalEnergyStored()
                                < (this.maxEUStore() - this.getMinimumStoredEU()))) {
                    this.getBaseMetaTileEntity().increaseStoredEnergyUnits(sEnergyPerTick * this.getEfficiency(), true);
                }
            }

            if ((this.mSolarCharge < 500) && (this.mProcessingEnergy != 0) && ((aTick % 32L) == 0L)) {
                Logger.WARNING("Adding Solar Charge. Currently " + this.mSolarCharge);
                this.mProcessingEnergy -= 1;
                this.mSolarCharge += 1;
            }

            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork())
                    && ((aTick % 64L) == 0L)
                    && (!aBaseMetaTileEntity.getWorld().isThundering())) {
                Logger.WARNING("Adding Processing Energy. Currently " + this.mProcessingEnergy);
                final boolean bRain = aBaseMetaTileEntity.getWorld().isRaining()
                        && (aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
                this.mProcessingEnergy += (bRain && (aBaseMetaTileEntity.getWorld().skylightSubtracted >= 4))
                        || !aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP) ? 0
                                : !bRain && aBaseMetaTileEntity.getWorld().isDaytime() ? 8 : 1;
            }

            if (aBaseMetaTileEntity.isServerSide()) {
                // Utils.LOG_WARNING("6.");
                aBaseMetaTileEntity.setActive(
                        aBaseMetaTileEntity.isAllowedToWork() && (aBaseMetaTileEntity.getUniversalEnergyStored()
                                >= (this.maxEUOutput() + this.getMinimumStoredEU())));
            }
        }
    }

    @Override
    public void inValidate() {}

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public long maxEUStore() {
        return Math.max(this.getEUVar(), (V[this.mTier] * 16000) + this.getMinimumStoredEU());
    }

    ITexture SolarArray[] = { new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_8V),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_MV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_HV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_EV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_IV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LuV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_ZPM),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_UV),
            new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL) };

    @Override
    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier] };
    }

    @Override
    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC) };
    }

    @Override
    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC) };
    }

    @Override
    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL) };
    }

    @Override
    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC) };
    }

    @Override
    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT_ACTIVE),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE) };
    }

    @Override
    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE) };
    }

    @Override
    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL) };
    }

    @Override
    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
                new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE) };
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new ProgressBar().setProgress(() -> (float) mProcessingEnergy / 1000)
                        .setTexture(
                                GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM.get(getSteamVariant()),
                                GT_UITextures.PROGRESSBAR_BOILER_STEAM,
                                10)
                        .setDirection(ProgressBar.Direction.UP).setPos(70, 25).setSize(10, 54))
                .widget(
                        new ProgressBar().setProgress(() -> (float) getBaseMetaTileEntity().getStoredEU())
                                .setTexture(
                                        GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM.get(getSteamVariant()),
                                        GT_UITextures.PROGRESSBAR_BOILER_WATER,
                                        10)
                                .setDirection(ProgressBar.Direction.UP).setPos(83, 25).setSize(10, 54))
                .widget(
                        new ProgressBar().setProgress(() -> (float) mSolarCharge / maxProgresstime())
                                .setTexture(
                                        GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM.get(getSteamVariant()),
                                        GT_UITextures.PROGRESSBAR_BOILER_HEAT,
                                        10)
                                .setDirection(ProgressBar.Direction.UP).setPos(96, 25).setSize(10, 54))
                .widget(
                        new ProgressBar().setProgress(() -> (float) mProcessingEnergy / 1000)
                                .setTexture(GT_UITextures.PROGRESSBAR_FUEL_STEAM.get(getSteamVariant()), 14)
                                .setDirection(ProgressBar.Direction.UP).setPos(116, 45).setSize(14, 14));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(GT_UITextures.BACKGROUND_STEAM.get(getSteamVariant()))
                .setItemSlot(GT_UITextures.SLOT_ITEM_STEAM.get(getSteamVariant()))
                .setCoverTab(
                        GT_UITextures.TAB_COVER_STEAM_NORMAL.get(getSteamVariant()),
                        GT_UITextures.TAB_COVER_STEAM_HIGHLIGHT.get(getSteamVariant()),
                        GT_UITextures.TAB_COVER_STEAM_DISABLED.get(getSteamVariant()))
                .setTitleTab(
                        GT_UITextures.TAB_TITLE_STEAM.getAdaptable(getSteamVariant()),
                        GT_UITextures.TAB_TITLE_DARK_STEAM.getAdaptable(getSteamVariant()),
                        GT_UITextures.TAB_TITLE_ANGULAR_STEAM.getAdaptable(getSteamVariant()))
                .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_STEAM.get(getSteamVariant()));
    }
}
