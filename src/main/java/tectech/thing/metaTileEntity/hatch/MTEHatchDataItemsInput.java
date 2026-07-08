package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.util.CommonValues;

public class MTEHatchDataItemsInput extends MTEHatchDataAccess implements IConnectsToDataPipe {

    public boolean delDelay = true;

    public MTEHatchDataItemsInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDataItemsInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return true;
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
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return null;
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return false;
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
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source) {
        return null;
    }

    public void setContents(ALRecipeDataPacket iIn) {
        ObjectOpenHashSet<RecipeAssemblyLine> oldRecipes = cachedRecipes;
        if (iIn == null) {
            cachedRecipes = null;
        } else {
            if (iIn.getContent().length > 0) {
                cachedRecipes = new ObjectOpenHashSet<>(iIn.getContent());
                delDelay = true;
            } else {
                cachedRecipes = null;
            }
        }
        // The upstream re-pushes the packet every cycle as a keep-alive, so only notify when the available recipe set
        // actually changed - compared by content (not just count) so a same-size data-stick swap still fires.
        if (recipesChanged(oldRecipes, cachedRecipes)) notifyWatchers();
    }

    @Override
    public List<RecipeAssemblyLine> getAssemblyLineRecipes() {
        if (cachedRecipes == null) return Collections.emptyList();

        return cachedRecipes.stream()
            .toList();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (CommonValues.MOVE_AT == aTick % 20) {
            getBaseMetaTileEntity().setActive(cachedRecipes != null);
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.datainass.desc.0"),
            translateToLocal("gt.blockmachines.hatch.datainass.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.datainass.desc.2") };
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }

    @Override
    protected String getWailaDataI18nKey() {
        return "tt.keyphrase.AL_Recipe_Receiving";
    }
}
