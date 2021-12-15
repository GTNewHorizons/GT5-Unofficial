package gtPlusPlus.core.util.minecraft.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.network.CustomPacket.PacketType;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class PacketHandler {
	public static final PacketHandler INSTANCE = new PacketHandler();
	private static final PacketType[] packetTypes = PacketType.values();
	final FMLEventChannel channel;

	private PacketHandler() {
		this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("GTPP");
		this.channel.register(this);
	}

	public static void init() {
	}

	@SubscribeEvent
	public void onPacket(ServerCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
		event.packet.payload().readBytes(data);
		this.onPacketData(data, ((NetHandlerPlayServer) event.handler).playerEntity);
	}

	@SubscribeEvent
	public void onPacket(ClientCustomPacketEvent event) {
		byte[] data = new byte[event.packet.payload().readableBytes()];
		event.packet.payload().readBytes(data);
		this.onPacketData(data, (EntityPlayerMP) null);
	}

	public void onPacketData(byte[] bData, EntityPlayerMP player) {
      DataInputStream data = new DataInputStream(new ByteArrayInputStream(bData));

      try {
         byte packetID = data.readByte();
         if (packetID < 0) {
            return;
         }
         PacketType type = packetTypes[packetID];
         Object pkt;  
         
         switch(type.ordinal()) {
         case 0:
            pkt = new PacketTileEntity();
            break;
         default:
            return;
         }

         if (pkt != null) {
            ((CustomPacket)pkt).readData(data);
         }
      } catch (IOException var7) {
         Logger.ERROR("Exception in PacketHandler.onPacketData: {0}"+ var7 + new Object[]{Arrays.toString(bData)});
      }

   }
}