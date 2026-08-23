package gregtech.common.tileentities.machines;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.IGTHatchAdder;
import gregtech.common.gui.modularui.hatch.MTEHeatSensorGui;
import gregtech.common.tileentities.machines.multi.MTEHatchRedstoneBase;

public class MTEHatchHeatSensor extends MTEHatchRedstoneBase {

    protected static final IIconContainer TEXTURE_FRONT = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    protected static final IIconContainer TEXTURE_FRONT_GLOW = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

    protected double threshold = 0;

    public MTEHatchHeatSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Reads heat from a machine.");
    }

    public MTEHatchHeatSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
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
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble("mThreshold", threshold);
        super.saveNBTData(aNBT);
    }

    public void updateRedstoneOutput(float heat) {
        setRedstoneSignal(heat > threshold);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchHeatSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TEXTURE_FRONT), TextureFactory.builder()
            .addIcon(TEXTURE_FRONT_GLOW)
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

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHeatSensorGui(this).build(data, syncManager, uiSettings);
    }

    public enum HeatSensorHatchElement implements IHatchElement<IHeatProducer> {

        HeatSensor(IHeatProducer::addHeatSensorHatchToMachineList, MTEHatchHeatSensor.class);

        private final IGTHatchAdder<IHeatProducer> adder;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;

        HeatSensorHatchElement(IGTHatchAdder<IHeatProducer> adder, Class<? extends IMetaTileEntity> mteClasse) {
            this.adder = adder;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasse));
        }

        @Override
        public long count(IHeatProducer heatProducer) {
            return heatProducer.getHeatSensorHatches()
                .size();
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
