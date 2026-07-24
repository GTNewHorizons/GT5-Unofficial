package gregtech.common.networkanalyzer.net;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketTypes;
import gregtech.common.networkanalyzer.NetworkAnalyzerData;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer;
import io.netty.buffer.ByteBuf;

public class GTPacketNetworkAnalyzer extends GTPacket {

    private NetworkAnalyzerData data;

    public GTPacketNetworkAnalyzer() {
        super();
    }

    public GTPacketNetworkAnalyzer(NetworkAnalyzerData data) {
        super();
        this.data = data;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.NETWORK_ANALYZER.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        try {
            this.data.writeExternal(aOut);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();
        data.readExternal(aData);
        return new GTPacketNetworkAnalyzer(data);
    }

    @Override
    public void process(IBlockAccess aWorld) {
        WorldOverlayRenderer.nodes = this.data.nodes;
        WorldOverlayRenderer.edges = this.data.edges;
        WorldOverlayRenderer.needListRefresh = true;
    }
}
