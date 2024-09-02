package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.common.WirelessDataStore;
import tectech.mechanics.dataTransport.InventoryDataPacket;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

public class MTEHatchWirelessDataItemsOutput extends MTEHatch {

    public InventoryDataPacket dataPacket = null;

    public boolean uploadedSinceReset = false;

    public MTEHatchWirelessDataItemsOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.hatch.wirelessdataoutass.desc.0"),
                translateToLocal("gt.blockmachines.hatch.wirelessdataoutass.desc.1"), });
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchWirelessDataItemsOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessDataItemsOutput(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (dataPacket != null) {
            aNBT.setTag("eDATA", dataPacket.toNbt());
        }
        aNBT.setBoolean("uploadedSinceReset", uploadedSinceReset);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("eDATA")) {
            dataPacket = new InventoryDataPacket(aNBT.getCompoundTag("eDATA"));
        }
        if (aNBT.hasKey("uploadedSinceReset")) {
            uploadedSinceReset = aNBT.getBoolean("uploadedSinceReset");
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Upload data packet and mark it as uploaded, so it will not be uploaded again
            // until the data bank resets the wireless network
            if (dataPacket != null && !uploadedSinceReset) {
                WirelessDataStore wirelessDataStore = WirelessDataStore
                    .getWirelessDataSticks(getBaseMetaTileEntity().getOwnerUuid());
                wirelessDataStore.uploadData(Arrays.asList(dataPacket.getContent()));
                uploadedSinceReset = true;
            }
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                EM_D_ACTIVE,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                EM_D_SIDES,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(EM_D_CONN) };
    }
}
