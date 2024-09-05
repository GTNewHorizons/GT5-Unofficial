/*
 * MIT License Copyright (c) 2021 Glease Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package goodgenerator.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;
import tectech.TecTech;

public abstract class MessageMTEBase implements IMessage {

    protected int w;
    protected int x;
    protected short y;
    protected int z;

    public MessageMTEBase() {}

    public MessageMTEBase(IGregTechTileEntity tile) {
        this.w = tile.getWorld().provider.dimensionId;
        this.x = tile.getXCoord();
        this.y = tile.getYCoord();
        this.z = tile.getZCoord();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readShort();
        z = buf.readInt();
        w = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeShort(y);
        buf.writeInt(z);
        buf.writeInt(w);
    }

    public abstract static class Handler<REQ extends MessageMTEBase, REPLY extends IMessage>
        implements IMessageHandler<REQ, REPLY> {

        protected abstract REPLY onError(REQ message, MessageContext ctx);

        protected abstract REPLY onSuccess(REQ message, MessageContext ctx, IMetaTileEntity mte);

        @Override
        public REPLY onMessage(REQ message, MessageContext ctx) {
            World world;
            if (ctx.side == Side.SERVER) {
                world = DimensionManager.getWorld(message.w);
            } else {
                world = TecTech.proxy.getClientWorld();
                if (world.provider.dimensionId != message.w) return onError(message, ctx);
            }
            if (world == null) return onError(message, ctx);
            if (world.blockExists(message.x, message.y, message.z)) {
                TileEntity te = world.getTileEntity(message.x, message.y, message.z);
                if (te instanceof IGregTechTileEntity && !((IGregTechTileEntity) te).isInvalidTileEntity()) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte != null) return onSuccess(message, ctx, mte);
                }
            }
            return onError(message, ctx);
        }
    }
}
