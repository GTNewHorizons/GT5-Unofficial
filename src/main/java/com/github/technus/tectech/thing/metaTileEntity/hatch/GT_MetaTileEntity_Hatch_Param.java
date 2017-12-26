package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Param;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_ParamAdv;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Param;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_ParamAdv;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_Param extends GT_MetaTileEntity_Hatch {
    private boolean usesFloat = false;
    public int pointer = 0;
    public int param = -1;
    public int value0i = 0;
    public int value1i = 0;
    public int input0i = 0;
    public int input1i = 0;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;

    public GT_MetaTileEntity_Hatch_Param(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "For parametrization of Multiblocks");
    }

    public GT_MetaTileEntity_Hatch_Param(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
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
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if (mTier >= 10) return new GT_Container_ParamAdv(aPlayerInventory, aBaseMetaTileEntity);
        return new GT_Container_Param(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if (mTier >= 10) return new GT_GUIContainer_ParamAdv(aPlayerInventory, aBaseMetaTileEntity);
        return new GT_GUIContainer_Param(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(ScreenON)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(ScreenOFF)};
    }

    //@Override
    //public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
    //    if (aBaseMetaTileEntity.isClientSide() && (aTick % 20L == 0L)) {
    //        //refresh casing on state change
    //        int Xpos = aBaseMetaTileEntity.getXCoord();
    //        int Ypos = aBaseMetaTileEntity.getYCoord();
    //        int Zpos = aBaseMetaTileEntity.getZCoord();
    //        try {
    //            aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(Xpos , Ypos, Zpos , Xpos , Ypos, Zpos );
    //        } catch (Exception e) {}
    //    }
    //    super.onPostTick(aBaseMetaTileEntity, aTick);
    //}

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_Param(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if(mTier>=10)
            return new String[]{
                    "Parametrizer ID: " + EnumChatFormatting.GREEN + param,
                    "Value 0I: " + EnumChatFormatting.AQUA + value0i,
                    "Value 0FB: " + EnumChatFormatting.AQUA + Float.intBitsToFloat(value0i)+" "+ Util.intBitsToShortString(value0i),
                    "Value 1I: " + EnumChatFormatting.BLUE + value1i,
                    "Value 1FB: " + EnumChatFormatting.BLUE + Float.intBitsToFloat(value1i)+" "+ Util.intBitsToShortString(value1i),
                    "Input 0I: " + EnumChatFormatting.GOLD   + input0i,
                    "Input 0FB: " + EnumChatFormatting.GOLD   + Float.intBitsToFloat(input0i)+" "+ Util.intBitsToShortString(input0i),
                    "Input 1I: " + EnumChatFormatting.YELLOW + input1i,
                    "Input 1FB: " + EnumChatFormatting.YELLOW + Float.intBitsToFloat(input1i)+" "+ Util.intBitsToShortString(input1i),
            };
        return new String[]{
                "Parametrizer ID: " + EnumChatFormatting.GREEN + param,
                "Value 0I: " + EnumChatFormatting.AQUA + value0i,
                "Value 1I: " + EnumChatFormatting.BLUE + value1i,
                "Input 0I: " + EnumChatFormatting.GOLD   + input0i,
                "Input 1I: " + EnumChatFormatting.YELLOW + input1i,
        };
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
        aNBT.setBoolean("eFloats", usesFloat);
        aNBT.setInteger("ePointer", pointer);
        aNBT.setInteger("eValue0i", value0i);
        aNBT.setInteger("eValue1i", value1i);
        aNBT.setInteger("eInput0i", value0i);
        aNBT.setInteger("eInput1i", value1i);
        aNBT.setInteger("eParam", param);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        usesFloat = aNBT.getBoolean("eFloats");
        pointer = aNBT.getInteger("ePointer");
        value0i=aNBT.getInteger("eValue0i");
        value1i=aNBT.getInteger("eValue1i");
        value0i=aNBT.getInteger("eInput0i");
        value1i=aNBT.getInteger("eInput1i");
        param = aNBT.getInteger("eParam");
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
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                mDescription,
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "E=mine*craft\u00b2"
        };
    }

    public boolean isUsingFloats() {
        return mTier >= 10 && usesFloat;
    }

    //returns - succeded
    public boolean setUsingFloats(boolean value){
        if(mTier>=10){
            usesFloat=value;
            return true;
        }
        return !value;
    }
}
