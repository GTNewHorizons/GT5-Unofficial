package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import crazypants.enderio.Log;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.widget.CoverCycleButtonWidget;

public class MTENeutronSensor extends MTEHatch {

    private static final IIconContainer textureFont = new Textures.BlockIcons.CustomIcon("icons/NeutronSensorFont");
    private static final IIconContainer textureFont_Glow = new Textures.BlockIcons.CustomIcon(
        "icons/NeutronSensorFont_GLOW");

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
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build()
            .child(
                Flow.column()
                    .child(createInvertButtonRow())
                    .child(createThresholdFieldRow())
                    .coverChildren()
                    .crossAxisAlignment(com.cleanroommc.modularui.utils.Alignment.CrossAxis.START)
                    .childPadding(2)
                    .pos(8, 6));
    }

    public Flow createInvertButtonRow() {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(() -> inverted, val -> inverted = val);
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16, 16))
            .child(
                IKey.dynamic(
                    () -> invertedSyncer.getValue() ? translateToLocal("gt.interact.desc.inverted")
                        : translateToLocal("gt.interact.desc.normal"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    public Flow createThresholdFieldRow() {
        return Flow.row()
            .child(
                new TextFieldWidget().setFormatAsInteger(true)
                    .setNumbers(0, 1200000000)
                    .size(77, 12)
                    .value(new IntSyncValue(() -> threshold, val -> threshold = val))
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gui.NeutronSensor.4")
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new CoverCycleButtonWidget().setToggle(() -> inverted, (val) -> inverted = val)
                .setTextureGetter(
                    (state) -> state == 1 ? GTUITextures.OVERLAY_BUTTON_REDSTONE_ON
                        : GTUITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                .addTooltip(0, translateToLocal("gt.interact.desc.normal.tooltip"))
                .addTooltip(1, translateToLocal("gt.interact.desc.inverted.tooltip"))
                .setPos(10, 8))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> inverted ? translateToLocal("gt.interact.desc.inverted")
                            : translateToLocal("gt.interact.desc.normal"))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(28, 12))
            .widget(
                new NumericWidget().setBounds(0, 1200000000)
                    .setGetter(() -> threshold)
                    .setSetter((value) -> threshold = (int) value)
                    .setScrollValues(1000, 1, 1_000_000)
                    .setTextColor(Color.WHITE.dark(1))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setFocusOnGuiOpen(true)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                    .setPos(10, 28)
                    .setSize(77, 12))
            .widget(
                new TextWidget(translateToLocal("gui.NeutronSensor.4")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(90, 30));
    }
}
