package gtPlusPlus.core.util.player;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class PlayerUtils {

	public static void messagePlayer(EntityPlayer P, String S){
		gregtech.api.util.GT_Utility.sendChatToPlayer(P, S);
	}

	public static EntityPlayer getPlayer(String name){
		List<EntityPlayer> i = new ArrayList<EntityPlayer>();
		Iterator<EntityPlayer> crunchifyIterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
		while (crunchifyIterator.hasNext()) {
			i.add((crunchifyIterator.next()));
		}		
		try{
			for (EntityPlayer temp : i) {
				if (temp.getDisplayName().toLowerCase().equals(name.toLowerCase())){
					return temp;
				}
			}
		}
		catch(NullPointerException e){}
		return null;
	}
	
	public static EntityPlayer getPlayerOnServerFromUUID(UUID parUUID){
		if (parUUID == null) 
		{
			return null;
		}
		List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayerMP player : allPlayers) 
		{
			if (player.getUniqueID().equals(parUUID)) 
			{
				return player;
			}
		}
		return null;
	}

	//Not Clientside
	public static EntityPlayer getPlayerInWorld(World world, String Name){
			List<EntityPlayer> i = world.playerEntities;
			Minecraft mc = Minecraft.getMinecraft();
			try{
				for (EntityPlayer temp : i) {
					if (temp.getDisplayName().toLowerCase().equals(Name.toLowerCase())){
						return temp;
					}
				}
			}
			catch(NullPointerException e){}
			return null;
		}

	public static boolean isPlayerOP(EntityPlayer player){
		if (player.canCommandSenderUseCommand(2, "")){
			return true;
		}
		return false;
	}

	//Not Clientside
	public static ItemStack getItemStackInPlayersHand(World world, String Name){
		EntityPlayer thePlayer = getPlayer(Name);
		ItemStack heldItem = null;
		try{heldItem = thePlayer.getHeldItem();
		}catch(NullPointerException e){return null;}
		if (heldItem != null){
			return heldItem;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static ItemStack getItemStackInPlayersHand(){
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack heldItem = null;
		try{heldItem = mc.thePlayer.getHeldItem();
		}catch(NullPointerException e){return null;}
		if (heldItem != null){
			return heldItem;
		}
		return null;
	}
	
	@SideOnly(Side.SERVER)
	public static ItemStack getItemStackInPlayersHand(EntityPlayer player){
		ItemStack heldItem = null;
		try{heldItem = player.getHeldItem();
		}catch(NullPointerException e){return null;}
		if (heldItem != null){
			return heldItem;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static Item getItemInPlayersHand(){
		Minecraft mc = Minecraft.getMinecraft();
		Item heldItem = null;
	
		try{heldItem = mc.thePlayer.getHeldItem().getItem();
		}catch(NullPointerException e){return null;}
	
		if (heldItem != null){
			return heldItem;
		}
	
		return null;
	}

}
