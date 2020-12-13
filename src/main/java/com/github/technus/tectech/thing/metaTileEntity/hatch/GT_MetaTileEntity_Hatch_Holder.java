package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Holder;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Holder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Holder extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon EM_H;
    private static Textures.BlockIcons.CustomIcon EM_H_ACTIVE;

    public GT_MetaTileEntity_Hatch_Holder(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_Holder(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
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
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_H_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_H)};
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Holder(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
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
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Holder(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Holder(aPlayerInventory, aBaseMetaTileEntity, translateToLocal("gt.blockmachines.hatch.holder.tier.09.name"));//Object Holder
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        //if(aBaseMetaTileEntity.isActive())
        //    aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        //else if(heat>0)
        //    aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        //else
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.hatch.holder.desc.0"),//For Research Station
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.holder.desc.1")//Advanced Holding Mechanism!
        };
    }
}
