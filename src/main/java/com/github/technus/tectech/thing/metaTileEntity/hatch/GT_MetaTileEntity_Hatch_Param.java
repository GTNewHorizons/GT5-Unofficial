package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_Param extends GT_MetaTileEntity_Hatch implements IAddGregtechLogo, IAddUIWidgets {

    public int pointer = 0;
    public int param = -1;
    public double value0D = 0;
    public double value1D = 0;
    public double input0D = 0;
    public double input1D = 0;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_Param(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                0,
                new String[] { CommonValues.TEC_MARK_GENERAL,
                        EnumChatFormatting.DARK_RED
                                + "Deprecated; Now you can set parameter by clicking LED on multiblock GUI.",
                        EnumChatFormatting.DARK_RED
                                + "If it doesn't work, try removing Parametrizer from multiblock structure." });
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_Param(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/PARAM");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/PARAM_ACTIVE");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(ScreenON) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(ScreenOFF) };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_Param(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("ePointer", pointer);
        aNBT.setDouble("eValue0D", value0D);
        aNBT.setDouble("eValue1D", value1D);
        aNBT.setDouble("eInput0D", input0D);
        aNBT.setDouble("eInput1D", input1D);
        aNBT.setInteger("eParam", param);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        pointer = aNBT.getInteger("ePointer");
        if (aNBT.hasKey("eFloats") || aNBT.hasKey("eValue0i")
                || aNBT.hasKey("eValue1i")
                || aNBT.hasKey("eInput0i")
                || aNBT.hasKey("eInput1i")) {
            boolean usesFloat = aNBT.getBoolean("eFloats");
            if (usesFloat) {
                value0D = Double.longBitsToDouble(aNBT.getLong("eValue0i"));
                value1D = Double.longBitsToDouble(aNBT.getLong("eValue1i"));
                input0D = Double.longBitsToDouble(aNBT.getLong("eInput0i"));
                input1D = Double.longBitsToDouble(aNBT.getLong("eInput1i"));
            } else {
                value0D = aNBT.getLong("eValue0i");
                value1D = aNBT.getLong("eValue1i");
                input0D = aNBT.getLong("eInput0i");
                input1D = aNBT.getLong("eInput1i");
            }
        } else {
            value0D = aNBT.getDouble("eValue0D");
            value1D = aNBT.getDouble("eValue1D");
            input0D = aNBT.getDouble("eInput0D");
            input1D = aNBT.getDouble("eInput1D");
        }
        param = aNBT.getInteger("eParam");
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
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
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
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO_DARK).setSize(18, 18)
                        .setPos(112, 55));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final boolean isAdvanced = mTier > 5;
        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE).setPos(43, 4)
                        .setSize(90, 72));

        addChangeParamButton(
                builder,
                (shift, columnPointer, secondRow) -> param -= shift ? 16 : 4,
                7,
                4,
                GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE,
                TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    secondRow.set(false);
                } else {
                    columnPointer.addAndGet(shift ? -16 : -4);
                }
            } else {
                value0D -= shift ? 4096 : 256;
            }
        }, 7, 22, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_0);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    columnPointer.addAndGet(shift ? -16 : -4);
                } else {
                    secondRow.set(true);
                }
            } else {
                value1D -= shift ? 4096 : 256;
            }
        }, 7, 40, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_1);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (shift) {
                    if (secondRow.get()) {
                        value1D = Double.longBitsToDouble(0xFFFF_FFFF_FFFF_FFFFL);
                    } else {
                        value0D = Double.longBitsToDouble(0xFFFF_FFFF_FFFF_FFFFL);
                    }
                } else {
                    if (secondRow.get()) {
                        long temp = Double.doubleToLongBits(value1D);
                        temp |= 1L << (long) columnPointer.get();
                        value1D = Double.longBitsToDouble(temp);
                    } else {
                        long temp = Double.doubleToLongBits(value0D);
                        temp |= 1L << (long) columnPointer.get();
                        value0D = Double.longBitsToDouble(temp);
                    }
                }
            } else {
                value0D /= shift ? 4096 : 256;
                value1D /= shift ? 4096 : 256;
            }
        },
                7,
                58,
                isAdvanced ? TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_S : GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE,
                isAdvanced ? null : TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);

        addChangeParamButton(
                builder,
                (shift, columnPointer, secondRow) -> param -= shift ? 2 : 1,
                25,
                4,
                GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL,
                TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    secondRow.set(false);
                } else {
                    columnPointer.addAndGet(shift ? -2 : -1);
                }
            } else {
                value0D -= shift ? 16 : 1;
            }
        }, 25, 22, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_0);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    columnPointer.addAndGet(shift ? -2 : -1);
                } else {
                    secondRow.set(true);
                }
            } else {
                value1D -= shift ? 16 : 1;
            }
        }, 25, 40, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_1);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (shift) {
                    if (secondRow.get()) {
                        value1D = Double.longBitsToDouble(0);
                    } else {
                        value0D = Double.longBitsToDouble(0);
                    }
                } else {
                    if (secondRow.get()) {
                        long temp = Double.doubleToLongBits(value1D);
                        temp &= ~(1L << (long) columnPointer.get());
                        value1D = Double.longBitsToDouble(temp);
                    } else {
                        long temp = Double.doubleToLongBits(value0D);
                        temp &= ~(1L << (long) columnPointer.get());
                        value0D = Double.longBitsToDouble(temp);
                    }
                }
            } else {
                value0D /= shift ? 16 : 2;
                value1D /= shift ? 16 : 2;
            }
        },
                25,
                58,
                isAdvanced ? TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_C : GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL,
                isAdvanced ? null : TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_X);

        addChangeParamButton(
                builder,
                (shift, columnPointer, secondRow) -> param += shift ? 2 : 1,
                133,
                4,
                GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL,
                TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    secondRow.set(false);
                } else {
                    columnPointer.addAndGet(shift ? 2 : 1);
                }
            } else {
                value0D += shift ? 16 : 1;
            }
        }, 133, 22, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_0);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    columnPointer.addAndGet(shift ? 2 : 1);
                } else {
                    secondRow.set(true);
                }
            } else {
                value1D += shift ? 16 : 1;
            }
        }, 133, 40, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_1);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (shift) {
                    if (secondRow.get()) {
                        value1D = Double.longBitsToDouble(~Double.doubleToLongBits(value1D));
                    } else {
                        value0D = Double.longBitsToDouble(~Double.doubleToLongBits(value1D));
                    }
                } else {
                    if (secondRow.get()) {
                        long temp = Double.doubleToLongBits(value1D);
                        temp ^= 1L << (long) columnPointer.get();
                        value1D = Double.longBitsToDouble(temp);
                    } else {
                        long temp = Double.doubleToLongBits(value0D);
                        temp ^= 1L << (long) columnPointer.get();
                        value0D = Double.longBitsToDouble(temp);
                    }
                }
            } else {
                value0D *= shift ? 16 : 2;
                value1D *= shift ? 16 : 2;
            }
        },
                133,
                58,
                isAdvanced ? TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_T : GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL,
                isAdvanced ? null : TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);

        addChangeParamButton(
                builder,
                (shift, columnPointer, secondRow) -> param += shift ? 16 : 4,
                151,
                4,
                GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE,
                TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    secondRow.set(false);
                } else {
                    columnPointer.addAndGet(shift ? 16 : 4);
                }
            } else {
                value0D += shift ? 4096 : 256;
            }
        }, 151, 22, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_0);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                if (secondRow.get()) {
                    columnPointer.addAndGet(shift ? 16 : 4);
                } else {
                    secondRow.set(true);
                }
            } else {
                value1D += shift ? 4096 : 256;
            }
        }, 151, 40, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_1);
        addChangeParamButton(builder, (shift, columnPointer, secondRow) -> {
            if (isAdvanced) {
                value0D = input0D;
                value1D = input1D;
            } else {
                value0D *= shift ? 4096 : 256;
                value1D *= shift ? 4096 : 256;
            }
        },
                151,
                58,
                isAdvanced ? TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_IF : GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE,
                isAdvanced ? null : TecTechUITextures.OVERLAY_BUTTON_PARAMETRIZER_ID);

        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> pointer, val -> pointer = val))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> param, val -> param = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> value0D, val -> value0D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> value1D, val -> value1D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> input0D, val -> input0D = val))
                .widget(new FakeSyncWidget.DoubleSyncer(() -> input1D, val -> input1D = val));

        final String CIRCLED_0 = "\u24EA";
        final String CIRCLED_1 = "\u2460";
        final String ARROW_DOWN = "\u2b07";
        final String ARROW_UP = "\u2b06";
        builder.widget(
                TextWidget
                        .dynamicString(
                                () -> (isAdvanced ? "Parameters X: " : "Parameters: ") + param)
                        .setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(46, 7))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> CIRCLED_0 + ARROW_DOWN + TT_Utility.formatNumberExp(input0D))
                                .setSynced(false).setDefaultColor(0x22ddff).setPos(46, 16))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> CIRCLED_1 + ARROW_DOWN + TT_Utility.formatNumberExp(input1D))
                                .setSynced(false).setDefaultColor(0x00ffff).setPos(46, 24))
                .widget(
                        TextWidget.dynamicString(() -> CIRCLED_0 + ARROW_UP + TT_Utility.formatNumberExp(value0D))
                                .setSynced(false).setDefaultColor(0x00bbff).setPos(46, 33))
                .widget(
                        TextWidget.dynamicString(() -> CIRCLED_1 + ARROW_UP + TT_Utility.formatNumberExp(value1D))
                                .setSynced(false).setDefaultColor(0x0077ff).setPos(46, 41))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> CIRCLED_0 + ARROW_UP
                                                + TT_Utility.longBitsToShortString(Double.doubleToLongBits(value0D)))
                                .setSynced(false).setDefaultColor(0x00bbff).setScale(.5f)
                                .setTextAlignment(Alignment.CenterLeft).setPos(46, 50))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> CIRCLED_1 + ARROW_UP
                                                + TT_Utility.longBitsToShortString(Double.doubleToLongBits(value1D)))
                                .setSynced(false).setDefaultColor(0x0077ff).setScale(.5f)
                                .setTextAlignment(Alignment.CenterLeft).setPos(46, 58));
        if (isAdvanced) {
            builder.widget(
                    TextWidget.dynamicString(() -> "Pointer " + Integer.toHexString(pointer | 0x10000).substring(1))
                            .setSynced(false).setDefaultColor(0x0033ff).setPos(46, 66));
        }
    }

    private void addChangeParamButton(ModularWindow.Builder builder, OnClick onClick, int xPos, int yPos,
            IDrawable overlay1, IDrawable overlay2) {
        final boolean isAdvanced = mTier > 5;
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            AtomicInteger columnPointer = new AtomicInteger(pointer & 0xff);
            AtomicBoolean secondRow = new AtomicBoolean((pointer & 0x0100) != 0);
            onClick.accept(clickData.shift, columnPointer, secondRow);
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (isAdvanced) {
                if (columnPointer.get() >= 64) {
                    columnPointer.set(63);
                } else if (columnPointer.get() < 0) {
                    columnPointer.set(0);
                }
                pointer = secondRow.get() ? columnPointer.get() + 0x100 : columnPointer.get();
            }
            if (param > 9) {
                param = 9;
            } else if (param < -1) {
                param = -1;
            }
        }).setPlayClickSound(false)
                .setBackground(
                        overlay2 != null ? new IDrawable[] { GT_UITextures.BUTTON_STANDARD, overlay1, overlay2 }
                                : new IDrawable[] { GT_UITextures.BUTTON_STANDARD, overlay1 })
                .setSize(18, 18).setPos(xPos, yPos));
    }

    @FunctionalInterface
    private interface OnClick {

        void accept(boolean shift, AtomicInteger columnPointer, AtomicBoolean secondRow);
    }
}
