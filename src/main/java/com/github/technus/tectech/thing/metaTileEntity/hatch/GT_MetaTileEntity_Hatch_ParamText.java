package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_ParamText extends GT_MetaTileEntity_Hatch_Param {

    public String value0s = "";
    public String value1s = "";

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_ParamText(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_ParamText(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_ParamText(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
                translateToLocalFormatted("tt.keyword.Parametrizer", clientLocale) + " "
                        + translateToLocalFormatted("tt.keyword.ID", clientLocale)
                        + ": "
                        + EnumChatFormatting.GREEN
                        + param,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 0S: "
                        + EnumChatFormatting.DARK_AQUA
                        + value0s,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 1S: "
                        + EnumChatFormatting.DARK_BLUE
                        + value1s,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 0D: "
                        + EnumChatFormatting.AQUA
                        + value0D,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 1D: "
                        + EnumChatFormatting.BLUE
                        + value1D,
                translateToLocalFormatted("tt.keyword.Input", clientLocale) + " 0D: "
                        + EnumChatFormatting.GOLD
                        + input0D,
                translateToLocalFormatted("tt.keyword.Input", clientLocale) + " 1D: "
                        + EnumChatFormatting.YELLOW
                        + input1D, };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("eIeValue0S", value0s);
        aNBT.setString("eIeValue1S", value1s);
        aNBT.removeTag("ePointer");
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        value0s = aNBT.getString("eIeValue0S");
        value1s = aNBT.getString("eIeValue1S");
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_GENERAL,
                EnumChatFormatting.DARK_RED
                        + "Deprecated; Now you can set parameter by clicking LED on multiblock GUI.",
                EnumChatFormatting.DARK_RED
                        + "If it doesn't work, try removing Parametrizer from multiblock structure.", };
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO_DARK).setSize(18, 18)
                        .setPos(148, 55));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE_PARAMETRIZER_TXT).setPos(7, 4)
                        .setSize(162, 72));

        addChangeNumberButton(builder, -16, -4, 7, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE);
        addChangeNumberButton(builder, -2, -1, 25, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL);
        addChangeNumberButton(builder, 2, 1, 133, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL);
        addChangeNumberButton(builder, 16, 4, 151, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE);

        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> param, val -> param = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> value0D, val -> value0D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> value1D, val -> value1D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> input0D, val -> input0D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> input1D, val -> input1D = val));
        // .widget(new FakeSyncWidget.StringSyncer(() -> value0s, val -> value0s = val))
        // .widget(new FakeSyncWidget.StringSyncer(() -> value1s, val -> value1s = val));

        final String CIRCLED_0 = "\u24EA";
        final String CIRCLED_1 = "\u2460";
        final String ARROW_DOWN = "\u2b07";
        final String ARROW_UP = "\u2b06";
        builder.widget(
                TextWidget.dynamicString(() -> "Parameters: " + param).setSynced(false)
                        .setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(46, 7))
                .widget(new TextWidget(CIRCLED_0 + ARROW_UP).setDefaultColor(0x00bbff).setPos(10, 29))
                .widget(new TextWidget(CIRCLED_1 + ARROW_UP).setDefaultColor(0x0077ff).setPos(10, 44))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> CIRCLED_0 + ARROW_DOWN + TT_Utility.formatNumberExp(input0D))
                                .setSynced(false).setDefaultColor(0x22ddff).setPos(10, 56))
                .widget(
                        TextWidget.dynamicString(() -> CIRCLED_1 + ARROW_DOWN + TT_Utility.formatNumberExp(input1D))
                                .setSynced(false).setDefaultColor(0x00ffff).setPos(10, 65));

        addTextField(builder, true);
        addTextField(builder, false);
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, int changeNumberShift, int changeNumber, int xPos,
            IDrawable overlay) {
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            param += clickData.shift ? changeNumberShift : changeNumber;
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (param > 9) {
                param = 9;
            } else if (param < -1) {
                param = -1;
            }
        }).setPlayClickSound(false)
                .setBackground(GT_UITextures.BUTTON_STANDARD, overlay, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID)
                .setSize(18, 18).setPos(xPos, 4));
    }

    private void addTextField(ModularWindow.Builder builder, boolean isIndex0) {
        TextFieldWidget widget = new TextFieldWidget();
        builder.widget(widget.setGetter(() -> isIndex0 ? value0s : value1s).setSetter(str -> {
            double val;
            try {
                val = parse(str);
            } catch (Exception e) {
                // This shouldn't happen as long as validator works
                str = "";
                val = 0;
            }
            if (isIndex0) {
                value0s = str;
                value0D = val;
            } else {
                value1s = str;
                value1D = val;
            }
        }).setValidator(str -> {
            try {
                parse(str);
                return str;
            } catch (Exception e) {
                return widget.getLastText().size() > 0 ? widget.getLastText().get(0) : "";
            }
        }).setTextColor(Color.WHITE.dark(1)).setTextAlignment(Alignment.CenterLeft)
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                .setPos(26, isIndex0 ? 26 : 41).setSize(138, 12));
    }

    private double parse(String str) {
        double val;
        if (str.contains("b")) {
            String[] split = str.split("b");
            val = TT_Utility.bitStringToInt(split[0].replaceAll("[^-]", "") + split[1].replaceAll("_", ""));
        } else if (str.contains("x")) {
            String[] split = str.split("x");
            val = TT_Utility.hexStringToInt(split[0].replaceAll("[^-]", "") + split[1].replaceAll("_", ""));
        } else {
            val = TT_Utility.stringToDouble(str);
        }
        return val;
    }
}
