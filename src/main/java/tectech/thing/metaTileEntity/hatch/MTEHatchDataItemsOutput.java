package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeOutputHatch;
import tectech.mechanics.pipe.IConnectsToDataPipe;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDataItemsOutput extends MTEHatchDataBase implements IALRecipeOutputHatch, IConnectsToDataPipe<ALRecipeDataPacket> {

    public ALRecipeDataPacket previousPacket = null;

    public MTEHatchDataItemsOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, GTSplit.splitLocalized("gt.blockmachines.hatch.dataoutass.desc"));
    }

    public MTEHatchDataItemsOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataItemsOutput(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(
                MTEHatchDataConnector.EM_D_ACTIVE,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(MTEHatchDataConnector.EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(
                MTEHatchDataConnector.EM_D_SIDES,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(MTEHatchDataConnector.EM_D_CONN) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        if (!this.previousPacket.isEmpty()) {
            aNBT.setTag("eDATA", this.previousPacket.toNBT());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        if (aNBT.hasKey("eDATA")) {
            this.previousPacket = new ALRecipeDataPacket(aNBT.getCompoundTag("eDATA"));
        }
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public ALRecipeDataPacket getQueued() {
        return q;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isOutputFacing(side);
    }

    @Override
    public CheckState moveAround(IGregTechTileEntity aBaseMetaTileEntity, CheckState checkState) {
        var result = super.moveAround(aBaseMetaTileEntity, checkState);

        switch (result) {
            // these states should not modify this hatch or the connected one
            case CONNECTION, DISCONNECTED, UNKNOWN -> {}
            case NEW_DATA -> {
                previousPacket = q;
                q = null;
            }
            case CONNECTED -> {} // ((MTEHatchDataItemsInput) connected).setContents(q);
            default -> {
                // TODO: log this new state
            }
        }
        return result;
    }

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Transmitting";
    }

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        return previousPacket == null ? ALRecipeDataPacket.EMPTY_PACKET : previousPacket;
    }

    @Override
    public void setAssemblyLineRecipes(ALRecipeDataPacket packet) {
        this.previousPacket = packet;
    }
}
