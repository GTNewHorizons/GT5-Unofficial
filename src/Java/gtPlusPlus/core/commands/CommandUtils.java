package gtPlusPlus.core.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandUtils {

	public static EntityPlayer getPlayer(final ICommandSender icommandsender){
		EntityPlayer player;

		if(icommandsender instanceof EntityPlayer){
			player = (EntityPlayer)icommandsender;
			return player;
		}
		else {
			return null;
		}
	}

}
