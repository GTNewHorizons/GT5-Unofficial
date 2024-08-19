package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_LASER_TT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Pollution;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugPollutor extends GT_MetaTileEntity_TieredMachineBlock
    implements IAddUIWidgets, IAddGregtechLogo {

    private static GT_RenderedTexture POLLUTOR;
    public int pollution = 0;
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public GT_MetaTileEntity_DebugPollutor(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.2") });
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_DebugPollutor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugPollutor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        POLLUTOR = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/POLLUTOR"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1],
            (side == facing) ? POLLUTOR : OVERLAYS_ENERGY_OUT_LASER_TT[mTier] };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("ePollution", pollution);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        pollution = aNBT.getInteger("ePollution");
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (pollution > 0) {
                GT_Pollution.addPollution(aBaseMetaTileEntity, pollution);
            }
        } else if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (side != aBaseMetaTileEntity.getFrontFacing()) {
                    TecTech.proxy.em_particle(aBaseMetaTileEntity, side);
                    TecTech.proxy.pollutor_particle(aBaseMetaTileEntity, side);
                }
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
    public boolean isElectric() {
        return false;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY)
                .setSize(17, 17)
                .setPos(113, 56));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
            .widget(
                new TextWidget().setStringSupplier(() -> "Pollution: " + numberFormat.format(pollution))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 8));

        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE,
            val -> pollution -= val,
            512,
            64,
            7,
            4);
        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE,
            val -> pollution /= val,
            512,
            64,
            7,
            22);

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> pollution -= val, 16, 1, 25, 4);
        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL,
            val -> pollution /= val,
            16,
            2,
            25,
            22);

        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> pollution += val, 16, 1, 133, 4);
        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL,
            val -> pollution *= val,
            16,
            2,
            133,
            22);

        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE,
            val -> pollution += val,
            512,
            64,
            151,
            4);
        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE,
            val -> pollution *= val,
            512,
            64,
            151,
            22);

    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
        int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(
            new ButtonWidget()
                .setOnClick((clickData, widget) -> setter.accept(clickData.shift ? changeNumberShift : changeNumber))
                .setBackground(GT_UITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, yPos));
    }
}
