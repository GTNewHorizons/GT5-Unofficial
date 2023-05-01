package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import goodgenerator.blocks.tileEntity.NeutronActivator;
import goodgenerator.util.CharExchanger;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;

public class NeutronSensor extends GT_MetaTileEntity_Hatch {

    private static final IIconContainer textureFont = new Textures.BlockIcons.CustomIcon("icons/NeutronSensorFont");
    private static final IIconContainer textureFont_Glow = new Textures.BlockIcons.CustomIcon(
            "icons/NeutronSensorFont_GLOW");

    protected String texts = "";
    boolean isOn = false;

    public NeutronSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Detect Neutron Kinetic Energy.");
    }

    public NeutronSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        texts = aNBT.getString("mBoxContext");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mBoxContext", texts);
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
            float aX, float aY, float aZ) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public void setText(String text) {
        texts = text == null ? "" : text;
    }

    public String getText() {
        return texts == null ? "" : texts;
    }

    public void outputRedstoneSignal() {
        isOn = true;
    }

    public void stopOutputRedstoneSignal() {
        isOn = false;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont),
                TextureFactory.builder().addIcon(textureFont_Glow).glow().build() };
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
        return new NeutronSensor(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        TextFieldWidget textField = new TextFieldWidget();
        builder.widget(
                textField.setGetter(this::getText).setSetter(this::setText)
                        .setValidator(
                                str -> isValidExpression(str) ? str
                                        : textField.getLastText().size() > 0 ? textField.getLastText().get(0) : "")
                        .setFocusOnGuiOpen(true).setTextColor(Color.WHITE.dark(1))
                        .setTextAlignment(Alignment.CenterLeft)
                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2)).setPos(8, 48)
                        .setSize(100, 18))
                .widget(
                        new TextWidget(StatCollector.translateToLocal("gui.NeutronSensor.0"))
                                .setDefaultColor(COLOR_TEXT_GRAY.get()).setTextAlignment(Alignment.CenterLeft)
                                .setPos(8, 8))
                .widget(
                        new TextWidget(StatCollector.translateToLocal("gui.NeutronSensor.1"))
                                .setDefaultColor(COLOR_TEXT_GRAY.get()).setPos(8, 32))
                .widget(
                        TextWidget.dynamicText(
                                () -> isValidExpression(textField.getText())
                                        ? new Text(StatCollector.translateToLocal("gui.NeutronSensor.2"))
                                                .color(0x077d02)
                                        : new Text(StatCollector.translateToLocal("gui.NeutronSensor.3"))
                                                .color(COLOR_TEXT_RED.get()))
                                .setSynced(false).setPos(120, 53));
    }

    private boolean isValidExpression(String exp) {
        return isValidSuffix(exp) && CharExchanger.isValidCompareExpress(NeutronActivator.rawProcessExp(exp));
    }

    private boolean isValidSuffix(String exp) {
        int index;
        index = exp.length() - 1;
        if (index < 0) return false;
        if (exp.charAt(index) != 'V' && exp.charAt(index) != 'v') return false;
        index = exp.length() - 2;
        if (index < 0) return false;
        if (exp.charAt(index) != 'E' && exp.charAt(index) != 'e') return false;
        index = exp.length() - 3;
        if (index < 0) return false;
        return exp.charAt(index) == 'M' || exp.charAt(index) == 'm'
                || exp.charAt(index) == 'K'
                || exp.charAt(index) == 'k'
                || Character.isDigit(exp.charAt(index));
    }
}
