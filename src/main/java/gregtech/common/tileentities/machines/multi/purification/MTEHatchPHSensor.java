package gregtech.common.tileentities.machines.multi.purification;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchPHSensorGui;
import gregtech.common.tileentities.machines.multi.MTEHatchRedstoneBase;

public class MTEHatchPHSensor extends MTEHatchRedstoneBase {

    // This implementation was largely copied from the neutron sensor hatch

    protected double threshold = 0;
    private boolean isOn = false;

    private static final IIconContainer textureFont = Textures.BlockIcons.OVERLAY_HATCH_PH_SENSOR;
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_PH_SENSOR_GLOW;

    public MTEHatchPHSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Detects pH value.");
    }

    public MTEHatchPHSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Can be installed in the pH Neutralization Purification Unit.",
            "Outputs Redstone Signal according to the current pH value.",
            "Right click to open the GUI and change settings." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        threshold = aNBT.getDouble("mThreshold");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mThreshold", threshold);
        super.saveNBTData(aNBT);
    }

    /**
     * Updates redstone output strength based on the pH of the multiblock.
     */
    public void updateRedstoneOutput(double pH) {
        setRedstoneSignal(pH > threshold);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchPHSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont), TextureFactory.builder()
            .addIcon(textureFont_Glow)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont) };
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchPHSensorGui(this).build(data, syncManager, uiSettings);
    }

}
