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
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.common.misc.WirelessTeamData;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import tectech.util.CommonValues;

public class MTEHatchWirelessDataItemsInput extends MTEHatchDataAccess {

    private boolean forceUpdate = false;

    public MTEHatchWirelessDataItemsInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
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
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM,
            translateToLocal("gt.blockmachines.hatch.datainasswireless.desc.0"),
            translateToLocal("gt.blockmachines.hatch.datainasswireless.desc.1"), };
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Upload data packet and mark it as uploaded, so it will not be uploaded again
            // until the data bank resets the wireless network
            aTick = MinecraftServer.getServer()
                .getTickCounter();
            if (aTick % WirelessTeamData.IO_TICK_RATE == WirelessTeamData.DOWNLOAD_TICK_OFFSET) {

                Team team = TeamManager.getTeamByPlayer(getBaseMetaTileEntity().getOwnerUuid());
                var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);

                if (!data.hasDirtySticks() && !forceUpdate) return;

                if (forceUpdate) {
                    forceUpdate = false;
                }

                ObjectOpenHashSet<RecipeAssemblyLine> oldRecipes = cachedRecipes;
                this.cachedRecipes = data.downloadDatasticks();
                // Only notify when the available recipe set changed (by content, not count, so a same-size swap of
                // wireless data sticks still fires), to avoid re-checking every download cycle.
                if (recipesChanged(oldRecipes, cachedRecipes)) notifyWatchers();
            }
        }
    }

    @Override
    public List<RecipeAssemblyLine> getAssemblyLineRecipes() {
        if (cachedRecipes == null) return Collections.emptyList();

        return cachedRecipes.stream()
            .toList();
    }

    @Override
    protected String getWailaDataI18nKey() {
        return "tt.keyphrase.AL_Recipe_Receiving";
    }

    @Override
    public void onFirstTick(IGregTechTileEntity bmte) {
        super.onFirstTick(bmte);

        if (!bmte.isServerSide()) return;

        Team team = TeamManager.getTeamByPlayer(bmte.getOwnerUuid());
        var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
        data.registerDataOutput();
        forceUpdate = true;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();

        if (!getBaseMetaTileEntity().isServerSide()) return;

        Team team = TeamManager.getTeamByPlayer(getBaseMetaTileEntity().getOwnerUuid());
        var data = (WirelessTeamData) team.getData(WirelessTeamData.DATA_KEY);
        data.unregisterDataOutput();
    }
}
