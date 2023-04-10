package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.minecraft.client.renderer.texture.IIconRegister;
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
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
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
public class GT_MetaTileEntity_Hatch_Uncertainty extends GT_MetaTileEntity_Hatch
        implements IAddGregtechLogo, IAddUIWidgets {

    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    public short[] matrix = new short[] { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500 };
    public byte selection = -1, mode = 0, status = -128; // all 8 bits set

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_Uncertainty(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
        TT_Utility.setTier(aTier, this);
        regenerate();
    }

    public GT_MetaTileEntity_Hatch_Uncertainty(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        regenerate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/UC");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/UC_ACTIVE");
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && (aTick & 15) == 0) {
            if (mode == 0) {
                aBaseMetaTileEntity.setActive(false);
                status = -128;
            } else {
                aBaseMetaTileEntity.setActive(true);
                shift();
                compute();
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_Uncertainty(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocalFormatted("tt.keyword.Status", clientLocale) + ": "
                + EnumChatFormatting.GOLD
                + status };
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
        aNBT.setByte("mSel", selection);
        aNBT.setByte("mMode", mode);
        aNBT.setByte("mStatus", status);
        NBTTagCompound mat = new NBTTagCompound();
        for (int i = 0; i < 16; i++) {
            mat.setShort(Integer.toString(i), matrix[i]);
        }
        aNBT.setTag("mMat", mat);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        selection = aNBT.getByte("mSel");
        mode = aNBT.getByte("mMode");
        status = aNBT.getByte("mStatus");
        NBTTagCompound mat = aNBT.getCompoundTag("mMat");
        for (int i = 0; i < 16; i++) {
            matrix[i] = mat.getShort(Integer.toString(i));
        }
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
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.certain.desc.0"), // Feeling
                                                                                                                   // certain,
                                                                                                                   // or
                                                                                                                   // not?
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockmachines.hatch.certain.desc.1") // Schrödinger equation in a box
        };
    }

    private boolean balanceCheck(int sideLenY, short... masses) {
        float inequality = 0;
        for (int i = 0; i < masses.length >> 1; i++) {
            inequality += Math.abs(masses[i] - masses[masses.length - i - 1]);
        }
        return inequality < masses.length << 7;
    }

    public void regenerate() {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = (short) TecTech.RANDOM.nextInt(1000);
        }
    }

    public byte compute() {
        int result = 0;
        switch (mode) {
            case 1: // ooo oxo ooo
                result = balanceCheck(4, matrix) ? 0 : 1;
                break;
            case 2: // ooo xox ooo
                result += balanceCheck(
                        4,
                        matrix[0],
                        matrix[4],
                        matrix[1],
                        matrix[5],
                        matrix[2],
                        matrix[6],
                        matrix[3],
                        matrix[7]) ? 0 : 1;
                result += balanceCheck(
                        4,
                        matrix[8],
                        matrix[12],
                        matrix[9],
                        matrix[13],
                        matrix[10],
                        matrix[14],
                        matrix[11],
                        matrix[15]) ? 0 : 2;
                break;
            case 3: // oxo xox oxo
                result += balanceCheck(
                        2,
                        matrix[0],
                        matrix[4],
                        matrix[8],
                        matrix[12],
                        matrix[1],
                        matrix[5],
                        matrix[9],
                        matrix[13]) ? 0 : 1;
                result += balanceCheck(
                        4,
                        matrix[0],
                        matrix[4],
                        matrix[1],
                        matrix[5],
                        matrix[2],
                        matrix[6],
                        matrix[3],
                        matrix[7]) ? 0 : 2;
                result += balanceCheck(
                        4,
                        matrix[8],
                        matrix[12],
                        matrix[9],
                        matrix[13],
                        matrix[10],
                        matrix[14],
                        matrix[11],
                        matrix[15]) ? 0 : 4;
                result += balanceCheck(
                        2,
                        matrix[2],
                        matrix[6],
                        matrix[10],
                        matrix[14],
                        matrix[3],
                        matrix[7],
                        matrix[11],
                        matrix[15]) ? 0 : 8;
                break;
            case 4: // xox ooo xox
                result += balanceCheck(2, matrix[0], matrix[4], matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2, matrix[8], matrix[12], matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(2, matrix[2], matrix[6], matrix[3], matrix[7]) ? 0 : 4;
                result += balanceCheck(2, matrix[10], matrix[14], matrix[11], matrix[15]) ? 0 : 8;
                break;
            case 5: // xox oxo xox
                result += balanceCheck(2, matrix[0], matrix[4], matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2, matrix[8], matrix[12], matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(4, matrix) ? 0 : 4;
                result += balanceCheck(2, matrix[2], matrix[6], matrix[3], matrix[7]) ? 0 : 8;
                result += balanceCheck(2, matrix[10], matrix[14], matrix[11], matrix[15]) ? 0 : 16;
                break;
        }
        return status = (byte) result;
    }

    private void shift() {
        int i = TecTech.RANDOM.nextInt(16), j = TecTech.RANDOM.nextInt(128);
        matrix[i] += ((matrix[i] & 1) == 0 ? 2 : -2) * j >> 5;
        matrix[i] += j == 0 ? 1 : 0;
        if (matrix[i] < 0) {
            matrix[i] = 0;
        } else if (matrix[i] > 1000) {
            matrix[i] = 999;
        }
    }

    public byte update(int newMode) {
        if (newMode == mode) {
            return status;
        }
        if (newMode < 0 || newMode > 5) {
            newMode = 0;
        }
        mode = (byte) newMode;
        regenerate();
        compute();
        return status;
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
        final boolean isAdvanced = mTier > 7;

        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE).setPos(43, 4)
                        .setSize(90, 72))
                .widget(
                        new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_UNCERTAINTY_MONITOR).setPos(46, 27)
                                .setSize(46, 46));

        int[] xPositions = new int[] { 7, 25, 133, 151 };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int index = i * 4 + j;
                builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                    if (selection == -1) {
                        selection = (byte) index;
                    } else {
                        short temp = matrix[selection];
                        matrix[selection] = matrix[index];
                        matrix[index] = temp;
                        selection = -1;
                    }
                    compute();
                }).setPlayClickSound(false)
                        .setBackground(
                                GT_UITextures.BUTTON_STANDARD,
                                TecTechUITextures.OVERLAY_BUTTON_UNCERTAINTY[index])
                        .setPos(xPositions[i], 4 + j * 18).setSize(18, 18))
                        .widget(new FakeSyncWidget.ShortSyncer(() -> matrix[index], val -> matrix[index] = val));
            }
        }
        builder.widget(new FakeSyncWidget.ByteSyncer(() -> selection, val -> selection = val))
                .widget(new FakeSyncWidget.ByteSyncer(() -> mode, val -> mode = val))
                .widget(new FakeSyncWidget.ByteSyncer(() -> status, val -> status = val));

        builder.widget(
                TextWidget.dynamicString(() -> "Status: " + (status == 0 ? "OK" : "NG")).setSynced(false)
                        .setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(46, 7));

        for (int i = 0; i < 9; i++) {
            final int index = i;
            builder.widget(new DrawableWidget().setDrawable(() -> {
                UITexture valid = TecTechUITextures.PICTURE_UNCERTAINTY_VALID[index];
                UITexture invalid = TecTechUITextures.PICTURE_UNCERTAINTY_INVALID[index];
                switch (mode) {
                    case 1: // ooo oxo ooo
                        if (index == 4) return status == 0 ? valid : invalid;
                        break;
                    case 2: // ooo xox ooo
                        if (index == 3) return (status & 1) == 0 ? valid : invalid;
                        if (index == 5) return (status & 2) == 0 ? valid : invalid;
                        break;
                    case 3: // oxo xox oxo
                        if (index == 1) return (status & 1) == 0 ? valid : invalid;
                        if (index == 3) return (status & 2) == 0 ? valid : invalid;
                        if (index == 5) return (status & 4) == 0 ? valid : invalid;
                        if (index == 7) return (status & 8) == 0 ? valid : invalid;
                        break;
                    case 4: // xox ooo xox
                        if (index == 0) return (status & 1) == 0 ? valid : invalid;
                        if (index == 2) return (status & 2) == 0 ? valid : invalid;
                        if (index == 6) return (status & 4) == 0 ? valid : invalid;
                        if (index == 8) return (status & 8) == 0 ? valid : invalid;
                        break;
                    case 5: // xox oxo xox
                        if (index == 0) return (status & 1) == 0 ? valid : invalid;
                        if (index == 2) return (status & 2) == 0 ? valid : invalid;
                        if (index == 4) return (status & 4) == 0 ? valid : invalid;
                        if (index == 6) return (status & 8) == 0 ? valid : invalid;
                        if (index == 8) return (status & 16) == 0 ? valid : invalid;
                        break;
                }
                return null;
            }).setPos(55 + (index % 3) * 12, 36 + (index / 3) * 12).setSize(4, 4));
        }

        for (int i = 0; i < 16; i++) {
            final int index = i;
            builder.widget(new DrawableWidget() {

                @Override
                public void draw(float partialTicks) {
                    if (isAdvanced) {
                        glEnable(GL_BLEND);
                        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                        glColor4f(1f, 1f, 1f, (float) matrix[index] / 1000f);

                        // super.draw but without disabling blend
                        GlStateManager.pushMatrix();
                        getDrawable().draw(Pos2d.ZERO, getSize(), partialTicks);
                        GlStateManager.popMatrix();

                        glDisable(GL_BLEND);
                        glColor4f(1f, 1f, 1f, 1f);
                    } else {
                        if (TecTech.RANDOM.nextInt(1000) < matrix[index]) {
                            super.draw(partialTicks);
                        }
                    }
                }
            }.setDrawable(TecTechUITextures.PICTURE_UNCERTAINTY_INDICATOR).setPos(47 + (i / 4) * 12, 28 + (i % 4) * 12)
                    .setSize(8, 8)).widget(
                            new DrawableWidget().setDrawable(
                                    () -> selection == index ? TecTechUITextures.PICTURE_UNCERTAINTY_SELECTED : null)
                                    .setPos(46 + (i / 4) * 12, 27 + (i % 4) * 12).setSize(10, 10));
        }
    }
}
