package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchEnergyDebugGui;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchEnergyDebug extends MTEHatchEnergy {

    public MTEHatchEnergyDebug(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchEnergyDebug(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public long getInputTier() {
        return voltageTier;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchEnergyDebug(mName, mTier, new String[] { "" }, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("debugAmperage", amperage);
        aNBT.setInteger("debugVTier", voltageTier);
        aNBT.setInteger("refillInterval", refillInterval);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        amperage = aNBT.getInteger("debugAmperage");
        voltageTier = aNBT.getInteger("debugVTier");
        refillInterval = aNBT.getInteger("refillInterval");
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedWithSuffix(
            "gt.blockmachines.energy_hatch_debug.desc",
            GTAuthors.buildAuthorsWithFormat(GTAuthors.AuthorChrom));
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    // 0 = ulv -> 14 MAX+
    private int voltageTier = 15;
    private int amperage = 2;

    public int getRefillInterval() {
        return refillInterval;
    }

    public void setRefillInterval(int refillInterval) {
        this.refillInterval = refillInterval;
    }

    public int getAmperage() {
        return amperage;
    }

    public void setAmperage(int amperage) {
        this.amperage = amperage;
    }

    public int getVoltageTier() {
        return voltageTier;
    }

    public void setVoltageTier(int voltageTier) {
        this.voltageTier = voltageTier;
    }

    // Refills every [refill interval] ticks, default 600 (30 seconds)
    private int refillInterval = 600;

    @Override
    public long getMinimumStoredEU() {
        return amperage * V[voltageTier];
    }

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public long maxEUInput() {
        return V[voltageTier];
    }

    @Override
    public long maxAmperesIn() {
        return amperage;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        if (baseMetaTileEntity.isServerSide()) fetchEnergy();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        return;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // refill entirely every [refillInterval] ticks, default is 600/30 seconds
            // minimum value of 1, to avoid div by 0
            if (aTick % ((long) Math.max(1, refillInterval)) == 0L) {
                fetchEnergy();
            }
        }
    }

    private void fetchEnergy() {
        setEUVar(Long.MAX_VALUE);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchEnergyDebugGui(this).build(data, syncManager, uiSettings);
    }

}
