package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class MTEHatchDataInput extends MTEHatchDataConnector<QuantumDataPacket> {

    private boolean delDelay = true;

    private long history;

    protected void updateComputationHistory(long value) {
        this.history = value;
    }

    public MTEHatchDataInput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.datain.desc.0"),
                translateToLocal("gt.blockmachines.hatch.datain.desc.1"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.datain.desc.2") });
    }

    public MTEHatchDataInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected QuantumDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new QuantumDataPacket(nbt);
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

    public void setContents(QuantumDataPacket qIn) {
        if (qIn == null) {
            this.q = null;
        } else {
            if (qIn.getContent() > 0) {
                this.q = qIn;
                delDelay = true;
            } else {
                this.q = null;
            }

            history = q == null ? 0 : q.getContent();
        }
    }

    @Override
    protected void resetHistory() {
        history = 0;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        if (delDelay) {
            delDelay = false;
        } else {
            setContents(null);
        }
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
        currenttip
            .add(translate("tt.keyphrase.Computation_Receiving", GTUtility.formatNumbers(tag.getLong("computation"))));
    }
}
