package gtPlusPlus.core.handler.events;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerCache;

import java.util.UUID;

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

				//Populates player cache
				if (!localPlayerRef.worldObj.isRemote){
				PlayerCache.appendParamChanges(localPlayersName, localPlayersUUID.toString());
				
				if (!CORE.isModUpToDate){
					Utils.LOG_INFO("You're not using the latest recommended version of GT++, consider updating.");
					Utils.LOG_INFO("Latest version is: "+CORE.MASTER_VERSION);
					Utils.LOG_INFO("You currently have: "+CORE.VERSION);
					Utils.messagePlayer(localPlayerRef, "You're not using the latest recommended version of GT++, consider updating.");
				}
				else {
					Utils.LOG_INFO("You're using the latest recommended version of GT++.");					
				}
				
				}


				/*if (localPlayerRef.getCommandSenderName().toLowerCase().equalsIgnoreCase("ImQ009") || localPlayerRef.getCommandSenderName().toLowerCase().contains("player")){
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


				}*/


			}			
		} catch (Throwable errr){
			Utils.LOG_INFO("Login Handler encountered an error.");

		}
	}

}