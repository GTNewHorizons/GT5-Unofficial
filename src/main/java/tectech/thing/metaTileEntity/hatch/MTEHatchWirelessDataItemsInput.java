package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.render.TextureFactory;
import gregtech.common.WirelessDataStore;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

public class MTEHatchWirelessDataItemsInput extends MTEHatchDataAccess {

    private String clientLocale = "en_US";

    private List<ItemStack> dataItems = null;

    public MTEHatchWirelessDataItemsInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchWirelessDataItemsInput(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public MTEHatchWirelessDataItemsInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessDataItemsInput(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory
            .of(EM_D_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(EM_D_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
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
        if (aPlayer instanceof EntityPlayerMPAccessor) {
            clientLocale = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
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
        if (this.dataItems == null) return ImmutableList.of();
        return this.dataItems.stream()
            .filter(stack -> stack != null && filter.test(stack))
            .collect(Collectors.toList());
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Upload data packet and mark it as uploaded, so it will not be uploaded again
            // until the data bank resets the wireless network
            if (aTick % WirelessDataStore.DOWNLOAD_TICK == 0) {
                WirelessDataStore wirelessDataStore = WirelessDataStore
                    .getWirelessDataSticks(getBaseMetaTileEntity().getOwnerUuid());
                this.dataItems = wirelessDataStore.downloadData(aTick);
            }
        }
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
