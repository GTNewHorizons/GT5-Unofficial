package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
import tectech.util.CommonValues;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDataItemsOutput extends MTEHatchDataBase
    implements IALRecipeOutputHatch, IConnectsToDataPipe<ALRecipeDataPacket> {

    public ALRecipeDataPacket previousPacket = null;
    private ALRecipeDataPacket q = null;
    private IConnectsToDataPipe<ALRecipeDataPacket> lastConnected = null;

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
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    // region IConnectsToDataPipe

    @Override
    public @Nullable IConnectsToDataPipe<ALRecipeDataPacket> getLastConnected() {
        return lastConnected;
    }

    @Override
    public void setLastConnected(@Nullable IConnectsToDataPipe<ALRecipeDataPacket> connected) {
        lastConnected = connected;
    }

    @Override
    public ALRecipeDataPacket getQueued() {
        return q;
    }

    @Override
    public void setContent(@Nullable ALRecipeDataPacket content) {
        if (content == null) content = ALRecipeDataPacket.EMPTY_PACKET;
        this.q = content;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isOutputFacing(side);
    }

    @Override
    public CheckState moveAround(IGregTechTileEntity aBaseMetaTileEntity, CheckState checkState) {
        var result = IConnectsToDataPipe.super.moveAround(aBaseMetaTileEntity, checkState);

        switch (result) {
            // these states should not modify this hatch or the connected one
            case CONNECTION, DISCONNECTED, UNKNOWN -> {}
            case NEW_DATA -> {
                previousPacket = q;
                q = ALRecipeDataPacket.EMPTY_PACKET;
            }
            case CONNECTED -> {} // ((MTEHatchDataItemsInput) connected).setContents(q);
            default -> {
                // TODO: log this new state
            }
        }
        return result;
    }
    // endregion

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Transmitting";
    }

    // region IALRecipeOutputHatch

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        return previousPacket == null ? ALRecipeDataPacket.EMPTY_PACKET : previousPacket;
    }

    @Override
    public void setAssemblyLineRecipes(ALRecipeDataPacket packet) {
        enqueueAndMove(packet);
    }

    // endregion

    // 5s connection polling, in case of pipes being broken, until pipes update hatches instantly
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide()) return;

        aTick = MinecraftServer.getServer()
            .getTickCounter();

        if (CommonValues.MOVE_AT == aTick % IConnectsToDataPipe.TICKS_CONNECTION_POLLING) {
            moveAround(aBaseMetaTileEntity, CheckState.CONNECTION);
        }
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (lastConnected == null) return;
        // when "outputs" are broken, update the connected input's data
        moveAround(this.getBaseMetaTileEntity(), CheckState.CONNECTION);
    }
}
