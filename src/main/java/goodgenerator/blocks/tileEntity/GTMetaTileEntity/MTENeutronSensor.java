package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import crazypants.enderio.Log;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTENeutronSensorGui;

public class MTENeutronSensor extends MTEHatch {

    private static final IIconContainer textureFont = Textures.BlockIcons.CustomIcon.create("icons/NeutronSensorFont");
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.CustomIcon
        .create("icons/NeutronSensorFont_GLOW");

    protected int threshold = 0;
    protected boolean inverted = false;
    boolean isOn = false;

    public MTENeutronSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Detect Neutron Kinetic Energy.");
    }

    public MTENeutronSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Can be installed in Neutron Activator.",
            "Output Redstone Signal according to the Neutron Kinetic Energy.",
            "Right click to open the GUI and setting." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mBoxContext")) {
            // Convert legacy settings
            setThresholdFromString(aNBT.getString("mBoxContext"));
        } else {
            threshold = aNBT.getInteger("mThreshold");
            inverted = aNBT.getBoolean("mInverted");
        }
        super.loadNBTData(aNBT);
    }

    /**
     * Used to convert legacy setting where the sensor would use a string like ">200keV" to set its threshold. This
     * method updates the {@link #threshold} and {@link #inverted} fields based on the input string. The string is
     * assumed to be in format "(operator)(value)[suffix](ev)", where:
     * <ul>
     * <li>(operator) is one of "<", ">", "<=", ">=", "==", or "!="</li>
     * <li>(value) is a numeric value (sequence of decimal digits)</li>
     * <li>(suffix) is "k", "K", "m", or "M" (optional)</li>
     * <li>(ev) is the string "ev", case-insensitive.</li>
     * </ul>
     * Note that operators "==" and "!=" can not be converted exactly, as the new threshold supports only a binary
     * comparison (less than, or greater than or equal). Thus "==" is interpreted in the same way as "<=", and "!=" as
     * ">". This shouldn't be a big problem for real setups, because one should probably not be testing for strict
     * equality here anyway. The possible reasonable conditions "==0eV" and "!=0eV" will continue working as before.
     *
     * @param text String to convert.
     */
    private void setThresholdFromString(String text) {
        Matcher matcher = Pattern.compile("^(<|>|<=|>=|==|!=)([0-9]*)(|k|m)(ev)$", Pattern.CASE_INSENSITIVE)
            .matcher(text);

        if (!matcher.matches()) {
            Log.error("Failed to parse Neutron Sensor setting: \"" + text + "\"!");
            return;
        }

        String operator = matcher.group(1);
        String value = matcher.group(2);
        String suffix = matcher.group(3);

        int newThreshold = Integer.parseInt(value);

        switch (suffix) {
            case "k":
            case "K":
                newThreshold *= 1000;
                break;
            case "m":
            case "M":
                newThreshold *= 1_000_000;
                break;
        }

        switch (operator) {
            case "<" -> {
                threshold = newThreshold;
                inverted = true;
            }
            case ">", "!=" -> {
                threshold = newThreshold + 1;
                inverted = false;
            }
            case "<=", "==" -> {
                threshold = newThreshold + 1;
                inverted = true;
            }
            case ">=" -> {
                threshold = newThreshold;
                inverted = false;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mThreshold", threshold);
        aNBT.setBoolean("mInverted", inverted);
        super.saveNBTData(aNBT);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    /**
     * Updates redstone output strength based on the eV of the multiblock.
     *
     * @param eV Amount of eV to compare.
     */
    public void updateRedstoneOutput(int eV) {
        isOn = (eV >= threshold) ^ inverted;
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
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (isOn) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) 15);
            }
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) 0);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENeutronSensor(mName, mTier, mDescriptionArray, mTextures);
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
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTENeutronSensorGui(this).build(data, syncManager, uiSettings);
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

}
