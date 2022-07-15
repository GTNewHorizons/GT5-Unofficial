package goodgenerator.network;

import com.github.technus.tectech.TecTech;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.NeutronSensor;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;

public class MessageSetNeutronSensorData extends MessageMTEBase {
    protected String data;

    public MessageSetNeutronSensorData() {}

    public MessageSetNeutronSensorData(IGregTechTileEntity tile, String data) {
        super(tile);
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        data = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, data);
    }

    public static class ServerHandler extends MessageMTEBase.Handler<MessageSetNeutronSensorData, IMessage> {
        @Override
        protected IMessage onError(MessageSetNeutronSensorData message, MessageContext ctx) {
            return null;
        }

        @Override
        protected IMessage onSuccess(MessageSetNeutronSensorData message, MessageContext ctx, IMetaTileEntity mte) {
            if (mte instanceof NeutronSensor) {
                ((NeutronSensor) mte).setText(message.data);
                if (ctx.side == Side.CLIENT) mte.getBaseMetaTileEntity().openGUI(TecTech.proxy.getPlayer());
            }
            return null;
        }
    }
}
