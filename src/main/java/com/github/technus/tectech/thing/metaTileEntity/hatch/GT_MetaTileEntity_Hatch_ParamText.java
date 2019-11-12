package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_ParamText;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_ParamText;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.reflect.FieldUtils;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_ParamText extends GT_MetaTileEntity_Hatch_Param {
    public String value0s="";
    public String value1s="";

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_ParamText(int aID, String aName, String aNameRegional, int aTier) {
        super(aID,aName,aNameRegional,aTier);
    }

    public GT_MetaTileEntity_Hatch_ParamText(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if(aPlayerInventory.player instanceof EntityPlayerMP) {
            NetworkDispatcher.INSTANCE.sendTo(new TextParametersMessage.ParametersTextData(this), (EntityPlayerMP) aPlayerInventory.player);
        }
        return new GT_Container_ParamText(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_ParamText(aPlayerInventory, aBaseMetaTileEntity);
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
        return new GT_MetaTileEntity_Hatch_ParamText(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                translateToLocalFormatted("tt.keyword.Parametrizer", clientLocale)+ " " + translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": " + EnumChatFormatting.GREEN + param,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 0S: " + EnumChatFormatting.DARK_AQUA + value0s,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 1S: " + EnumChatFormatting.DARK_BLUE + value1s,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 0D: " + EnumChatFormatting.AQUA + value0D,
                translateToLocalFormatted("tt.keyword.Value", clientLocale) + " 1D: " + EnumChatFormatting.BLUE + value1D,
                translateToLocalFormatted("tt.keyword.Input", clientLocale) + " 0D: " + EnumChatFormatting.GOLD   + input0D,
                translateToLocalFormatted("tt.keyword.Input", clientLocale) + " 1D: " + EnumChatFormatting.YELLOW + input1D,
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
        aNBT.setString("eIeValue0S", value0s);
        aNBT.setString("eIeValue1S", value1s);
        aNBT.removeTag("ePointer");
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        value0s = aNBT.getString("eIeValue0S");
        if (value0s==null){
            value0s="";
        }
        value1s = aNBT.getString("eIeValue1S");
        if(value1s==null){
            value1s="";
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
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.hatch.param.desc.0"),//For parametrization of Multiblocks
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.hatch.param.desc.1") +"\u00b2"//E=mine*craft
        };
    }
}
