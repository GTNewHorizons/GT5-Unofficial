package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
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
import gregtech.common.tileentities.machines.ISmartInputHatch;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeInputHatch;
import tectech.mechanics.pipe.IConnectsToDataPipe;

/// The AL recipe input hatch that receives its recipes from a databank (via a [MTEHatchDataItemsOutput] + data pipe
/// chain).
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDataItemsInput extends MTEHatchDataBase
    implements ISmartInputHatch, IALRecipeInputHatch, IConnectsToDataPipe<ALRecipeDataPacket> {

    public boolean delDelay = true;
    protected ALRecipeDataPacket cachedRecipes = ALRecipeDataPacket.EMPTY_PACKET;

    public MTEHatchDataItemsInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, GTSplit.splitLocalized("gt.blockmachines.hatch.datainass.desc"));
    }

    public MTEHatchDataItemsInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataItemsInput(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public void setContent(@Nullable ALRecipeDataPacket newRecipes) {
        ALRecipeDataPacket oldRecipes = this.cachedRecipes;

        if (newRecipes == null || newRecipes.isEmpty()) {
            this.cachedRecipes = ALRecipeDataPacket.EMPTY_PACKET;
        } else {
            this.cachedRecipes = newRecipes;
            this.delDelay = true;
        }

        // The upstream re-pushes the packet every cycle as a keep-alive, so only notify when the available recipe set
        // actually changed - compared by content (not just count) so a same-size data-stick swap still fires.
        if (recipesChanged(oldRecipes, this.cachedRecipes)) notifyWatchers();
    }

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        return cachedRecipes;
    }

    @Override
    public void setActive(boolean active) {
        // do nothing
    }

    @Override
    public IConnectsToDataPipe<ALRecipeDataPacket> getLastConnected() {
        return null;
    }

    @Override
    public void setLastConnected(IConnectsToDataPipe<ALRecipeDataPacket> connected) {}

    @Override
    public ALRecipeDataPacket getQueued() {
        return null;
    }

    @Override
    public boolean canClear() {
        return true;
    }

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Receiving";
    }
}
