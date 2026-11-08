package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataBase;
import gregtech.api.render.TextureFactory;
import gregtech.common.WirelessDataStore;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeOutputHatch;

public class MTEHatchWirelessDataItemsOutput extends MTEHatchDataBase implements IALRecipeOutputHatch {

    @NotNull
    public ALRecipeDataPacket dataPacket = ALRecipeDataPacket.EMPTY_PACKET;
    protected boolean packetValid = false;

    public MTEHatchWirelessDataItemsOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { StatCollector.translateToLocal("gt.blockmachines.hatch.wirelessdataoutass.desc.0"),
                StatCollector.translateToLocal("gt.blockmachines.hatch.wirelessdataoutass.desc.1"), });
    }

    public MTEHatchWirelessDataItemsOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessDataItemsOutput(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        if (!this.dataPacket.isEmpty()) {
            aNBT.setTag("eDATA", this.dataPacket.toNBT());
        }

        aNBT.setBoolean("packetValid", packetValid);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey("eDATA")) {
            this.dataPacket = new ALRecipeDataPacket(aNBT.getCompoundTag("eDATA"));
        }

        this.packetValid = aNBT.getBoolean("packetValid");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Upload data packet and mark it as uploaded, so it will not be uploaded again
            // until the data bank resets the wireless network
            aTick = MinecraftServer.getServer()
                .getTickCounter();
            if (aTick % WirelessDataStore.IO_TICK_RATE == 0) {
                WirelessDataStore wirelessDataStore = WirelessDataStore
                    .getWirelessDataSticks(getBaseMetaTileEntity().getOwnerUuid());

                wirelessDataStore.uploadData(this);

                if (packetValid) {
                    packetValid = false;
                } else {
                    dataPacket = ALRecipeDataPacket.EMPTY_PACKET;
                }
            }
        }
    }

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Transmitting";
    }

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        return dataPacket;
    }

    @Override
    public void setAssemblyLineRecipes(ALRecipeDataPacket packet) {
        this.dataPacket = packet;
        this.packetValid = true;
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
}
