package miscutil.core.handler.events;

import java.util.UUID;

import miscutil.core.item.ModItems;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class LoginEventHandler {

	public String localPlayersName;
	public UUID localPlayersUUID;
	private EntityPlayer localPlayerRef;

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

		this.localPlayerRef = event.player;
		this.localPlayersName = event.player.getDisplayName();
		this.localPlayersUUID = event.player.getUniqueID();

		try {


			if (localPlayerRef instanceof EntityPlayerMP && localPlayerRef != null){
				/*Utils.LOG_INFO("Engaging Log in protection.");
				localPlayerRef.*/

				if (localPlayerRef.getCommandSenderName().toLowerCase().equalsIgnoreCase("ImQ009")/* || localPlayerRef.getCommandSenderName().toLowerCase().contains("player")*/){


					Utils.LOG_INFO("Spawning a new Santa Thread.");

					Thread t = new Thread() {
						UUID threadHandlerIDthing = localPlayersUUID;
						@Override
						public void run() {
							while(true && Minecraft.getMinecraft().getIntegratedServer() != null) {
								try {
									if(localPlayerRef == null){
										localPlayerRef = Utils.getPlayerOnServerFromUUID(threadHandlerIDthing);
									}


									//ImQ009 is a legend.
									if (localPlayerRef.getCommandSenderName().toLowerCase().equalsIgnoreCase("ImQ009")){
										Utils.messagePlayer(localPlayerRef, "Enjoy some complimentary Raisin Bread.");
										localPlayerRef.inventory.addItemStackToInventory(UtilsItems.getSimpleStack(ModItems.itemIngotRaisinBread, MathUtils.randInt(1, 5)));
									}


									if (localPlayerRef.getCommandSenderName().toLowerCase().contains("player")){
										Utils.messagePlayer(localPlayerRef, "Enjoy some complimentary Raisin Bread.");
										localPlayerRef.inventory.addItemStackToInventory(UtilsItems.getSimpleStack(ModItems.itemIngotRaisinBread, MathUtils.randInt(1, 5)));
									}
									Thread.sleep(1000*60*MathUtils.randInt(15, 90));
								} catch (InterruptedException ie) {
									Utils.LOG_INFO("Santa Mode Disabled.");
								}
							}

							Utils.LOG_INFO("Thread Stopped. Handler Closed.");

						}
					};
					//t.start();		


				}


			}			
		} catch (Throwable errr){
			Utils.LOG_INFO("Login Handler encountered an error.");

		}
	}

}