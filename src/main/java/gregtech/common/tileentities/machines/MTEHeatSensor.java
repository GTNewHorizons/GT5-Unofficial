package gregtech.common.tileentities.machines;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHeatProducer;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.IGTHatchAdder;
import gregtech.common.gui.modularui.hatch.MTEHeatSensorGui;

public class MTEHeatSensor extends MTEHatch {

    protected static final IIconContainer TEXTURE_FRONT = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    protected static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

    protected double threshold = 0;
    protected boolean inverted = false;
    protected float heat = 0;

    public MTEHeatSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Reads heat from a machine.");
    }

    public MTEHeatSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        return new String[] { "Reads heat of a machine.",
            "Send redstone signal if the heat is greater than the threshold.",
            "Right click to open the GUI and change settings." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        threshold = aNBT.getDouble("mThreshold");
        inverted = aNBT.getBoolean("mInverted");
        heat = aNBT.getFloat("heat");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mThreshold", threshold);
        aNBT.setBoolean("mInverted", inverted);
        aNBT.setFloat("heat", heat);
        super.saveNBTData(aNBT);
    }

    public void setHeatValue(float heat) {
        this.heat = heat;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        ForgeDirection facing = getBaseMetaTileEntity().getFrontFacing();
        boolean isOn = (heat > threshold) ^ inverted;
        if (aBaseMetaTileEntity.isServerSide()) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity
                    .setStrongOutputRedstoneSignal(direction, isOn && direction == facing ? (byte) 15 : 0);
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHeatSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TEXTURE_FRONT), TextureFactory.builder()
            .addIcon(textureFont_Glow)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TEXTURE_FRONT) };
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
        return new MTEHeatSensorGui(this).build(data, syncManager, uiSettings);
    }

    public enum HeatSensorHatchElement implements IHatchElement<IHeatProducer> {

        HeatSensor(IHeatProducer::addHeatSensorHatchToMachineList, MTEHeatSensor.class);

        private final IGTHatchAdder<IHeatProducer> adder;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;

        HeatSensorHatchElement(IGTHatchAdder<IHeatProducer> adder, Class<? extends IMetaTileEntity> mteClasse) {
            this.adder = adder;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasse));
        }

        @Override
        public long count(IHeatProducer heatProducer) {
            return heatProducer.getHeatSensorHatchNum();
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super IHeatProducer> adder() {
            return adder;
        }

    }

}
