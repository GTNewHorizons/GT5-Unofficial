package gtPlusPlus.core.handler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayerMP;

import io.netty.buffer.ByteBuf;

public class PacketHandler {

	public static SimpleNetworkWrapper packetLightning;
	
	public PacketHandler(){
		packetLightning = NetworkRegistry.INSTANCE.newSimpleChannel("gtpp_Lightning");
		packetLightning.registerMessage(Packet_Lightning_Handler.class, Packet_Lightning.class, 0, Side.SERVER);
	}
	
	
	
	
	/**
	 * Internal Packet Handlers
	 * @author Alkalus
	 *
	 */
	
	private class Packet_Lightning implements IMessage{

		public void sendTo(IMessage msg, EntityPlayerMP player){
			packetLightning.sendTo(msg, player);
		}
		
		public void sendToServer(String string){
			packetLightning.sendToServer(new Packet_Lightning(string));
		}		
		
		 private String text;

		    public Packet_Lightning(String text) {
		        this.text = text;
		    }

		    @Override
		    public void fromBytes(ByteBuf buf) {
		        text = ByteBufUtils.readUTF8String(buf); // this class is very useful in general for writing more complex objects
		    }

		    @Override
		    public void toBytes(ByteBuf buf) {
		        ByteBufUtils.writeUTF8String(buf, text);
		    }
		
	}
	
	private class Packet_Lightning_Handler implements IMessageHandler<Packet_Lightning, IMessage>{

		@Override
        public IMessage onMessage(Packet_Lightning message, MessageContext ctx) {
            System.out.println(String.format("Received %s from %s", message.text, ctx.getServerHandler().playerEntity.getDisplayName()));
            return null; // no response in this case
        }
		
	}
	
	
}
