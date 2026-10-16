package gregtech.common.tileentities.machines;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDataCopyable;
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

public class MTEHeatSensor extends MTEHatch implements IDataCopyable {

    public static final String COPIED_DATA_IDENTIFIER = "heatSensor";

    protected static final IIconContainer TEXTURE_FRONT = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    protected static final IIconContainer TEXTURE_FRONT_GLOW = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

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
        if (!(aPlayer instanceof EntityPlayerMP)) {
            openGui(aPlayer);
            return true;
        }

        final ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            openGui(aPlayer);
            return true;
        }

        if (!pasteCopiedData(aPlayer, dataStick.stackTagCompound)) {
            aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.heat_sensor.invalid"));
            return false;
        }

        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.heat_sensor.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) {
            return;
        }
        final ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return;
        }

        dataStick.stackTagCompound = getCopiedData(aPlayer);
        dataStick.setStackDisplayName("Heat Sensor Hatch Configuration");
        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.heat_sensor.saved"));
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
                    .setOutputRedstoneSignal(direction, isOn && direction == facing ? (byte) 15 : (byte) 0);
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

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setDouble("threshold", getThreshold());
        tag.setBoolean("inverted", isInverted());
        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) return false;
        setThreshold(nbt.getDouble("threshold"));
        setInverted(nbt.getBoolean("inverted"));
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
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

        HeatSensor("GT5U.MBTT.HeatSensorHatch", IHeatProducer::addHeatSensorHatchToMachineList, MTEHeatSensor.class);

        private final String name;
        private final IGTHatchAdder<IHeatProducer> adder;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;

        HeatSensorHatchElement(String name, IGTHatchAdder<IHeatProducer> adder,
            Class<? extends IMetaTileEntity> mteClasse) {
            this.name = name;
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

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public String getDescriptionLangKey() {
            return name;
        }
    }

}
