package gregtech.common.tileentities.machines.multi.purification;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchPHSensorGui;

public class MTEHatchPHSensor extends MTEHatch {

    // This implementation was largely copied from the neutron sensor hatch

    protected double threshold = 0;
    protected boolean inverted = false;
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
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection Side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
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
        inverted = aNBT.getBoolean("mInverted");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mThreshold", threshold);
        aNBT.setBoolean("mInverted", inverted);
        super.saveNBTData(aNBT);
    }

    /**
     * Updates redstone output strength based on the pH of the multiblock.
     */
    public void updateRedstoneOutput(double pH) {
        isOn = (pH > threshold) ^ inverted;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (isOn) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, (byte) 15);
            }
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, (byte) 0);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
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

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchPHSensorGui(this).build(data, syncManager, uiSettings);
    }

}
