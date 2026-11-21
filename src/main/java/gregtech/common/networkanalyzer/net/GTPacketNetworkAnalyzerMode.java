package gregtech.common.networkanalyzer.net;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.networkanalyzer.NetworkAnalyzerData.AnalyzerModes;
import gregtech.common.networkanalyzer.events.NetworkAnalyzerPlayerTracker;
import gregtech.common.tools.ItemNetworkAnalyzer;
import io.netty.buffer.ByteBuf;

public class GTPacketNetworkAnalyzerMode extends GTPacket {

    private AnalyzerModes mode;
    private EntityPlayerMP player;

    public GTPacketNetworkAnalyzerMode() {
        super();
    }

    public GTPacketNetworkAnalyzerMode(AnalyzerModes mode) {
        super();
        this.mode = mode;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.NETWORK_ANALYZER_MODE.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        try {
            aOut.writeInt(this.mode.ordinal());
        } catch (Exception ex) {
            GT_FML_LOGGER.error(ex);;
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        final AnalyzerModes mode = GTDataUtils.getIndexSafe(AnalyzerModes.VALUES, aData.readInt());
        return new GTPacketNetworkAnalyzerMode(mode);
    }

    @Override
    public void setINetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer handlerPlayServer) {
            this.player = handlerPlayServer.playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (this.player == null) {
            return;
        }

        final ItemStack currentItem = this.player.inventory.getCurrentItem();

        if (currentItem != null && currentItem.getItem() instanceof ItemNetworkAnalyzer) {
            final String modeKey = "gt.NetworkAnalyzer.mode." + mode.name()
                .toLowerCase();

            ItemNetworkAnalyzer.setMode(currentItem, mode);
            NetworkAnalyzerPlayerTracker.reset(player);
            GTUtility.sendChatTrans(this.player, "gt.NetworkAnalyzer.set", new ChatComponentTranslation(modeKey));
        }
    }
}
