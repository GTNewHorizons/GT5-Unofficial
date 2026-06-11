package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.WirelessComputationPacket;
import gregtech.common.gui.modularui.hatch.MTEHatchWirelessComputationInputGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.dataTransport.QuantumDataPacket;

public class MTEHatchWirelessComputationInput extends MTEHatchDataInput implements IDataCopyable {

    public long requiredComputation = 10000;
    public static final String COPIED_DATA_IDENTIFIER = "cloudComputationInput";

    public MTEHatchWirelessComputationInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchWirelessComputationInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public long getRequiredComputation() {
        return requiredComputation;
    }

    public void setRequiredComputation(long requiredComputation) {
        this.requiredComputation = requiredComputation;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessComputationInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!aPlayer.isUsingItem()) {
            openGui(aPlayer);
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q == null) {
            long received = WirelessComputationPacket.downloadData(
                aBaseMetaTileEntity.getOwnerUuid(),
                requiredComputation,
                MinecraftServer.getServer()
                    .getTickCounter());
            if (received > 0) {
                q = new QuantumDataPacket(received);
                updateComputationHistory(received);
            } else {
                updateComputationHistory(0);
            }
        }

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        requiredComputation = aNBT.getLong("requiredComputation");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("requiredComputation", requiredComputation);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!aBaseMetaTileEntity.isServerSide()) {
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        }
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);

        if (!pasteCopiedData(aPlayer, dataStick.stackTagCompound)) return false;

        aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.gui.text.data_stick.loaded"));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        if (!baseMetaTileEntity.isServerSide()) {
            return;
        }
        ItemStack dataStick = player.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;
        dataStick.stackTagCompound = getCopiedData(player);
        dataStick.setStackDisplayName("Cloud Computation Input");
        player.addChatMessage(new ChatComponentTranslation("GT5U.gui.text.data_stick.saved"));
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setLong("computation", requiredComputation);
        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) return false;
        requiredComputation = nbt.getLong("computation");
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("requiredComputation", requiredComputation);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            translateToLocalFormatted(
                "GT5U.machines.computation_hatch.computation.amount",
                formatNumber(
                    accessor.getNBTData()
                        .getLong("requiredComputation"))));
    }

    private static String[] tooltips;

    @Override
    public String[] getDescription() {
        if (tooltips == null) {
            tooltips = new String[] { "Wireless Computation Data Input for Multiblocks" };
        }
        return tooltips;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchWirelessComputationInputGui(this).build(guiData, syncManager, uiSettings);
    }
}
