package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DataConnector.EM_D_ACTIVE;
import static com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DataConnector.EM_D_CONN;
import static com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DataConnector.EM_D_SIDES;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.WirelessDataStore;

public class GT_MetaTileEntity_Hatch_WirelessInputDataItems extends GT_MetaTileEntity_Hatch_DataAccess {

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_WirelessInputDataItems(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_WirelessInputDataItems(String aName, int aTier, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_WirelessInputDataItems(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_WirelessInputDataItems(
            this.mName,
            this.mTier,
            mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GT_RenderedTexture(
                EM_D_ACTIVE,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GT_RenderedTexture(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GT_RenderedTexture(
                EM_D_SIDES,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GT_RenderedTexture(EM_D_CONN) };
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
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM,
            translateToLocal("gt.blockmachines.hatch.datainasswireless.desc.0"),
            translateToLocal("gt.blockmachines.hatch.datainasswireless.desc.1"), };
    }

    @Override
    public List<ItemStack> getInventoryItems(Predicate<ItemStack> filter) {
        WirelessDataStore wirelessData = WirelessDataStore
            .getWirelessDataSticks(getBaseMetaTileEntity().getOwnerUuid());
        return wirelessData.downloadData()
            .stream()
            .filter(stack -> stack != null && filter.test(stack))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocalFormatted("tt.keyphrase.Content_Stack_Count", clientLocale) + ": "
            + getInventoryItems(_stack -> true).size() };
    }
}
