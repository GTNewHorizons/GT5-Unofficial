package goodgenerator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import goodgenerator.blocks.tileEntity.PreciseAssembler;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;

public class MessageResetTileTexture extends MessageMTEBase {

    protected int index;

    public MessageResetTileTexture(){
    }

    public MessageResetTileTexture(IGregTechTileEntity tile, int index) {
        super(tile);
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(index);
    }

    public static class ClientHandler extends MessageMTEBase.Handler<MessageResetTileTexture, IMessage> {

        @Override
        protected IMessage onError(MessageResetTileTexture message, MessageContext ctx) {
            return null;
        }

        @Override
        protected IMessage onSuccess(MessageResetTileTexture message, MessageContext ctx, IMetaTileEntity mte) {
            if (mte instanceof PreciseAssembler) {
                ((PreciseAssembler) mte).setCasingTier(message.index);
                mte.markDirty();
            }
            return null;
        }
    }

}
