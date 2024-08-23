package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Holder extends GT_MetaTileEntity_Hatch implements IAddGregtechLogo {

    private static Textures.BlockIcons.CustomIcon EM_H;
    private static Textures.BlockIcons.CustomIcon EM_H_ACTIVE;

    public GT_MetaTileEntity_Hatch_Holder(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.holder.desc.0"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.holder.desc.1") });
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_Holder(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_H_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_HOLDER_ACTIVE");
        EM_H = new Textures.BlockIcons.CustomIcon("iconsets/EM_HOLDER");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(EM_H_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(EM_H) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Holder(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return facing.offsetY == 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        // if(aBaseMetaTileEntity.isActive())
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        // else if(heat>0)
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        // else
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO)
                .setSize(18, 18)
                .setPos(151, 63));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK)
                .setPos(46, 17)
                .setSize(84, 60))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_RACK_LARGE)
                    .setPos(68, 27)
                    .setSize(40, 40))
            .widget(new SlotWidget(new BaseSlot(inventoryHandler, 0) {

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isEnabled() {
                    return !getBaseMetaTileEntity().isActive();
                }
            }).setPos(79, 38))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BUTTON_STANDARD_LIGHT_16x16)
                    .setPos(152, 24)
                    .setSize(16, 16))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> getBaseMetaTileEntity().isActive() ? TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON
                            : TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED)
                    .setPos(152, 24)
                    .setSize(16, 16))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)));
    }
}
