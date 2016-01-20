package miscutil.gregtech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class IMessage {

	public static void messageThePlayer(String s){
		if(Minecraft.getMinecraft().thePlayer.worldObj.isRemote){
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(s));
		}
		else if(!Minecraft.getMinecraft().thePlayer.worldObj.isRemote){
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(s));
		}
	}

	public void messageOtherPlayer(String s){
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(s));	
	}

}
