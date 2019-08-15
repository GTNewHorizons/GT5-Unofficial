package gtPlusPlus.core.commands;

import java.util.ArrayList;
import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.preloader.asm.AsmConfig;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class CommandEnableDebugWhileRunning implements ICommand
{
	private final List<String> aliases;

	public CommandEnableDebugWhileRunning(){
		this.aliases = new ArrayList<>();
	}

	@Override
	public int compareTo(final Object o){
		return 0;

	}

	@Override
	public String getCommandName(){
		return "debugmodegtpp";

	}

	
	// Use '/debugmodegtpp' along with 'logging' or 'debug' to toggle Debug mode and Logging.
	// Using nothing after the command toggles both to their opposite states respectively.	
	@Override
	public String getCommandUsage(final ICommandSender var1){
		return "/debugmodegtpp";

	}

	@Override
	public List<String> getCommandAliases(){
		return this.aliases;

	}

	@Override
	public void processCommand(final ICommandSender S, final String[] argString){
		
		if (argString == null || argString.length == 0 || argString.length > 1) {
			Logger.INFO("Toggling Debug Mode & Logging.");			
			final EntityPlayer P = CommandUtils.getPlayer(S);		
			if (PlayerUtils.isPlayerOP(P)) {
			    CORE.DEBUG = Utils.invertBoolean(CORE.DEBUG);
			    PlayerUtils.messagePlayer(P, "Toggled GT++ Debug Mode - Enabled: "+CORE.DEBUG);
			    AsmConfig.disableAllLogging = Utils.invertBoolean(AsmConfig.disableAllLogging);
			    PlayerUtils.messagePlayer(P, "Toggled GT++ Logging - Enabled: "+(!AsmConfig.disableAllLogging));
			}	
		}
		else {
			if (argString[0].toLowerCase().equals("debug")) {
				Logger.INFO("Toggling Debug Mode.");			
				final EntityPlayer P = CommandUtils.getPlayer(S);		
				if (PlayerUtils.isPlayerOP(P)) {
				    CORE.DEBUG = Utils.invertBoolean(CORE.DEBUG);
				    PlayerUtils.messagePlayer(P, "Toggled GT++ Debug Mode - Enabled: "+CORE.DEBUG);
				}
			}
			else if (argString[0].toLowerCase().equals("logging")) {
				Logger.INFO("Toggling Logging.");			
				final EntityPlayer P = CommandUtils.getPlayer(S);		
				if (PlayerUtils.isPlayerOP(P)) {
				    AsmConfig.disableAllLogging = Utils.invertBoolean(AsmConfig.disableAllLogging);
				    PlayerUtils.messagePlayer(P, "Toggled GT++ Logging - Enabled: "+(!AsmConfig.disableAllLogging));
				}
			}
			else {		
				final EntityPlayer P = CommandUtils.getPlayer(S);		
				if (PlayerUtils.isPlayerOP(P)) {
				    PlayerUtils.messagePlayer(P, "Invalid command, use 'debug' or 'logging'");
				}
			}
		}
		
		
			
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender var1){	    
	    if (var1 == null) {
	        return false;
	    }	    
		final EntityPlayer P = CommandUtils.getPlayer(var1);
		if (P != null && PlayerUtils.isPlayerOP(P)) {
		    return true;
		}
		return false;
	}

	@Override
	public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2){
		return null;
	}

	@Override
	public boolean isUsernameIndex(final String[] var1, final int var2){
		// TODO Auto-generated method stub
		return false;
	}

	public boolean playerUsesCommand(final World W, final EntityPlayer P, final int cost){
	    return true;
	}

}