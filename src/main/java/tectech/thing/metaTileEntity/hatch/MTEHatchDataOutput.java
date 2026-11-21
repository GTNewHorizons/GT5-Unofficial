package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchDataOutputGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class MTEHatchDataOutput extends MTEHatchDataConnector<QuantumDataPacket> {

    private long history;

    public long requestedComputation;
    public boolean allowComputationConfiguring;

    public MTEHatchDataOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { translateToLocal("gt.blockmachines.hatch.dataout.desc.0"),
                translateToLocal("gt.blockmachines.hatch.dataout.desc.1"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.dataout.desc.2") });
    }

    public MTEHatchDataOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public long getRequestedComputation() {
        return requestedComputation;
    }

    public void setRequestedComputation(long requestedComputation) {
        this.requestedComputation = requestedComputation;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setLong("comp", requestedComputation);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        requestedComputation = aNBT.getLong("comp");
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isOutputFacing(side);
    }

    public void providePacket(QuantumDataPacket packet) {
        this.q = packet;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        IConnectsToDataPipe current = this, source = this, next;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof MTEHatchDataInput) {
                ((MTEHatchDataInput) next).setContents(q);
                break;
            }
            source = current;
            current = next;
        }

        history = q == null ? 0 : q.getContent();

        q = null;
    }

    @Override
    protected void resetHistory() {
        history = 0;
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source /* ==this */) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof MTEPipeData) {
            ((MTEPipeData) meta).markUsed();
            return (IConnectsToDataPipe) meta;
        } else if (meta instanceof MTEHatchDataInput && ((MTEHatchDataInput) meta).getColorization() == color
            && ((MTEHatchDataInput) meta).canConnectData(
                base.getFrontFacing()
                    .getOpposite())) {
                        return (IConnectsToDataPipe) meta;
                    }
        return null;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("computation", history);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(translate("tt.keyphrase.Computation_Transmitting", formatNumber(tag.getLong("computation"))));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {

        if (allowComputationConfiguring) {
            openGui(aPlayer);
        } else {
            GTUtility.sendChatToPlayer(aPlayer, "Cannot configure computation on this hatch");
        }

        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDataOutputGui(this).build(guiData, syncManager, uiSettings);
    }
}
