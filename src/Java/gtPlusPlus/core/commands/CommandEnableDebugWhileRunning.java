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
		this.aliases.add("gtplusplus");
	}

	@Override
	public int compareTo(final Object o){
		if (o instanceof Comparable<?>) {
		 @SuppressWarnings("unchecked")
		Comparable<ICommand> a = (Comparable<ICommand>) o;	
		 if (a.equals(this)) {
			 return 0;
		 }
		 else {
			 return -1;
		 }
		}
		return -1;
	}

	@Override
	public String getCommandName(){
		return "gtpp";

	}


	// Use '/gtpp' along with 'logging' or 'debug' to toggle Debug mode and Logging.
	// Using nothing after the command toggles both to their opposite states respectively.	
	@Override
	public String getCommandUsage(final ICommandSender var1){
		return "/gtpp ?";
	}

	@Override
	public List<String> getCommandAliases(){
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender S, final String[] argString){
		int aMaxArgumentsAllowed = 1;

		if ((argString == null || argString.length == 0 || argString.length > aMaxArgumentsAllowed) || argString[0].toLowerCase().equals("?")) {
			Logger.INFO("Listing commands and their uses.");			
			final EntityPlayer P = CommandUtils.getPlayer(S);
			AsmConfig.disableAllLogging = Utils.invertBoolean(AsmConfig.disableAllLogging);
			PlayerUtils.messagePlayer(P, "The following are valid args for the '/gtpp' command:");
			PlayerUtils.messagePlayer(P, "?       - This help command.");
			PlayerUtils.messagePlayer(P, "logging - Toggles ALL GT++ logging for current session.");
			PlayerUtils.messagePlayer(P, "debug   - Toggles GT++ Debug Mode. Only use when advised, may break everything. (OP)");
		}
		else if (argString[0].toLowerCase().equals("debug")) {
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
			AsmConfig.disableAllLogging = Utils.invertBoolean(AsmConfig.disableAllLogging);
			PlayerUtils.messagePlayer(P, "Toggled GT++ Logging - Enabled: "+(!AsmConfig.disableAllLogging));
		}
		else {		
			final EntityPlayer P = CommandUtils.getPlayer(S);	
			PlayerUtils.messagePlayer(P, "Invalid command, use '?' as an argument for help.'");
		}

	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender var1){	    
		if (var1 == null || CommandUtils.getPlayer(var1) == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2){
		ArrayList<String> aTabCompletes = new ArrayList<String>();
		aTabCompletes.add("?");
		aTabCompletes.add("logging");
		aTabCompletes.add("debug");
		return aTabCompletes;
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