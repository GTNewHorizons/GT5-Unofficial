package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import javax.annotation.Nullable;

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
import gregtech.common.tileentities.machines.ISmartInputHatch;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeInputHatch;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.util.CommonValues;

/// The AL recipe input hatch that receives its recipes from a databank (via a [MTEHatchDataItemsOutput] + data pipe
/// chain).
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDataItemsInput extends MTEHatchDataBase
    implements ISmartInputHatch, IALRecipeInputHatch, IConnectsToDataPipe<ALRecipeDataPacket> {

    public boolean delDelay = true;
    protected ALRecipeDataPacket cachedRecipes = null;

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
    public void setContent(@Nullable ALRecipeDataPacket newRecipes) {
        ALRecipeDataPacket oldRecipes = this.cachedRecipes;

        if (newRecipes.isEmpty()) {
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
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagCompound stacksTag = getAssemblyLineRecipes().toNBT();
        aNBT.setTag("data_stacks", stacksTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTTagCompound stacksTag = aNBT.getCompoundTag("data_stacks");
        this.cachedRecipes = new ALRecipeDataPacket(stacksTag);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (CommonValues.MOVE_AT == aTick % 20) {
            if (this.cachedRecipes.isEmpty()) {
                getBaseMetaTileEntity().setActive(false);
            } else {
                getBaseMetaTileEntity().setActive(true);
                if (delDelay) {
                    delDelay = false;
                } else {
                    setContent(null);
                }
            }
        }
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
