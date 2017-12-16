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
    public int extra = 0;
    public int data1 = 0;
    public int data0 = 0;
    public int param = -1;
    public float value0f = 0;
    public float value1f = 0;
    public float input1f = 0;
    public float input2f = 0;
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
        int temp;
        if(mTier>=10){
            return new String[]{
                    "Parametrizer ID: " + EnumChatFormatting.GREEN + param,
                    "Value 0|F: " + EnumChatFormatting.AQUA + value0f,
                    "Value 0|I: " + EnumChatFormatting.AQUA + (temp=Float.floatToIntBits(value0f)),
                    "Value 0|B: " + EnumChatFormatting.AQUA + Util.intToString(temp,8),
                    "Value 1|F: " + EnumChatFormatting.BLUE + value1f,
                    "Value 1|I: " + EnumChatFormatting.BLUE + (temp=Float.floatToIntBits(value1f)),
                    "Value 1|B: " + EnumChatFormatting.BLUE + Util.intToString(temp,8),
                    "Input 0|F: " + EnumChatFormatting.GOLD   + input1f,
                    "Input 0|I: " + EnumChatFormatting.GOLD   + (temp=Float.floatToIntBits(input1f)),
                    "Input 0|B: " + EnumChatFormatting.GOLD   + Util.intToString(temp,8),
                    "Input 1|F: " + EnumChatFormatting.YELLOW + input2f,
                    "Input 1|I: " + EnumChatFormatting.YELLOW + (temp=Float.floatToIntBits(input2f)),
                    "Input 1|B: " + EnumChatFormatting.YELLOW + Util.intToString(temp,8),
                    "Data 0|I" + data0,
                    "Data 1|I" + data1,
                    "Data x|I" + extra,
            };
        }
        return new String[]{
                "Parametrizer ID: " + EnumChatFormatting.GREEN + param,
                "Value 0|F: " + EnumChatFormatting.AQUA + value0f,
                "Value 1|F: " + EnumChatFormatting.BLUE + value1f,
                "Input 0|F: " + EnumChatFormatting.GOLD   + input1f,
                "Input 1|F: " + EnumChatFormatting.YELLOW + input2f,
                "Data 0|I" + data0,
                "Data 1|I" + data1,
                "Data x|I" + extra,
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

    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mEXP", extra);
        if(mTier>=10) {
            aNBT.setInteger("mV2", Float.floatToIntBits(value1f));
            aNBT.setInteger("mV1", Float.floatToIntBits(value0f));
        }else {
            aNBT.setInteger("mV2", data1);
            aNBT.setInteger("mV1", data0);
        }
        aNBT.setInteger("mParam", param);
    }

    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        extra = aNBT.getInteger("mEXP");
        data1 = aNBT.getInteger("mV2");
        data0 = aNBT.getInteger("mV1");
        param = aNBT.getInteger("mParam");
        if(mTier>=10) {
            value0f =Float.intBitsToFloat(data0);
            value1f =Float.intBitsToFloat(data1);
        }else{
            value0f = (float) (data0 * Math.pow(2, extra));
            value1f = (float) (data1 * Math.pow(2, extra));
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
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                mDescription,
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "E=mine*craft^2"
        };
    }
}
