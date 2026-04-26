package gregtech.common.tileentities.machines.multi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
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
import gregtech.common.gui.modularui.hatch.MTEToxicResidueSensorGui;

public class MTEToxicResidueSensor extends MTEHatch {

    private int threshold = 0;
    private boolean inverted = false;
    private boolean isOn = false;
    private ThresholdType thresholdType;

    private static final IIconContainer textureFont = Textures.BlockIcons.OVERLAY_HATCH_TOXIC_RESIDUE_SENSOR;
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_TOXIC_RESIDUE_SENSOR_GLOW;

    public MTEToxicResidueSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Reads Toxic Residue from Large Neutralization Engine");
        thresholdType = ThresholdType.FLAT;
    }

    public MTEToxicResidueSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        thresholdType = ThresholdType.FLAT;
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
        return new String[] { "Reads Toxic Residue of the Large Neutralization Engine",
            "Right click to open the GUI and change settings." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        threshold = aNBT.getInteger("mThreshold");
        inverted = aNBT.getBoolean("mInverted");
        thresholdType = ThresholdType.values()[aNBT.getInteger("thresholdType")];
        isOn = aNBT.getBoolean("isOn");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mThreshold", threshold);
        aNBT.setBoolean("mInverted", inverted);
        aNBT.setInteger("thresholdType", thresholdType.ordinal());
        aNBT.setBoolean("isOn", isOn);
        super.saveNBTData(aNBT);
    }

    /**
     * Updates redstone output strength based on the toxic residue of the LNE.
     */
    public void updateRedstoneOutput(int toxicResidue, int capacity) {
        if (thresholdType == ThresholdType.PERCENTAGE) {
            toxicResidue = (int) (((float) toxicResidue / capacity) * 100);
        }
        isOn = (toxicResidue > threshold) ^ inverted;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, (byte) (isOn ? 15 : 0));
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
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

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEToxicResidueSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
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
        return new MTEToxicResidueSensorGui(this).build(data, syncManager, uiSettings);
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(ThresholdType thresholdType) {
        this.thresholdType = thresholdType;
        this.threshold = Math.min(this.threshold, this.thresholdType.getMaxCapacity());
    }

    public enum ThresholdType {

        FLAT(Integer.MAX_VALUE, "gt.interact.desc.toxic_residue_sensor.flat",
            "gt.interact.desc.toxic_residue_sensor.flat.tooltip"),
        PERCENTAGE(100, "gt.interact.desc.toxic_residue_sensor.percentage",
            "gt.interact.desc.toxic_residue_sensor.percentage.tooltip");

        final int maxCapacity;
        final String name;
        final String tooltip;

        ThresholdType(int maxCapacity, String nameUntranslated, String tooltipUntranslated) {
            this.maxCapacity = maxCapacity;
            this.name = StatCollector.translateToLocal(nameUntranslated);
            this.tooltip = StatCollector.translateToLocal(tooltipUntranslated);
        }

        @Override
        public String toString() {
            return name;
        }

        public String getTooltip() {
            return tooltip;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }
    }
}
