package miscutil.core.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import miscutil.core.util.uptime.PlayerUptimeData;
import miscutil.core.util.uptime.Uptime;
import miscutil.core.util.uptime.UptimeData;
import miscutil.core.util.uptime.UptimeLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentText;

public class CommandUptime
extends CommandBase
{
	public String getCommandName()
	{
		return "uptime";
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "/" + getCommandName() + " [player_name | reset | winners]";
	}

	@SuppressWarnings("rawtypes")
	public List getCommandAliases()
	{
		return null;
	}

	public void processCommand(ICommandSender sender, String[] args)
	{

		try{
			EntityPlayerMP player = (EntityPlayerMP)sender;


			Uptime.updateUptime();
			UptimeData data = Uptime.getUptimeData();
			if (args.length == 0)
			{
				player.addChatMessage(new ChatComponentText("World is on day " + data.getWorldDays() + "."));

				player.addChatMessage(new ChatComponentText("Server has been running for " + data.getServerOndays() + " days."));

				long remainder = data.getServerUptime() / 1000L;
				int days = (int)remainder / 86400;
				remainder %= 86400L;
				int hours = (int)remainder / 3600;
				remainder %= 3600L;
				int minutes = (int)remainder / 60;
				remainder %= 60L;
				int seconds = (int)remainder;
				player.addChatMessage(new ChatComponentText("Server has been up for " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds."));
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("reset"))
				{
					if (player.canCommandSenderUseCommand(2, ""))
					{
						data.resetUptime();

						@SuppressWarnings("unchecked")
						List<EntityPlayerMP> players = net.minecraft.server.MinecraftServer.getServer().worldServers[0].playerEntities;
						for (int t = 0; t < players.size(); t++) {
							((EntityPlayerMP)players.get(t)).addChatMessage(new ChatComponentText("All player statistics have been reset."));
						}
					}
					else
					{
						player.addChatMessage(new ChatComponentText("You must be an OP to use this subcommand."));
					}
				}
				else if (!args[0].equalsIgnoreCase("winners"))
				{
					PlayerUptimeData player_data = data.getPlayerUptimeData(args[0]);
					if (player_data != null)
					{
						player.addChatMessage(new ChatComponentText(args[0] + " has played for " + player_data.player_ondays + " days."));

						long remainder = player_data.played_time / 1000L;
						int days = (int)remainder / 86400;
						remainder %= 86400L;
						int hours = (int)remainder / 3600;
						remainder %= 3600L;
						int minutes = (int)remainder / 60;
						remainder %= 60L;
						int seconds = (int)remainder;
						player.addChatMessage(new ChatComponentText(args[0] + " has played for " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds."));

						Date first_joined = new Date(player_data.first_joined);
						player.addChatMessage(new ChatComponentText(args[0] + " first joined on the " + new SimpleDateFormat("d MMM yyyy 'at' HH:mm:ss z").format(first_joined) + "."));

						Date last_active = new Date(player_data.last_active);
						player.addChatMessage(new ChatComponentText(args[0] + " was last active on the " + new SimpleDateFormat("d MMM yyyy 'at' HH:mm:ss z").format(last_active) + "."));
						if (player_data.number_of_deaths != 0)
						{
							player.addChatMessage(new ChatComponentText(args[0] + " has died " + player_data.number_of_deaths + " times."));
							Date death_date = new Date(player_data.last_death);
							player.addChatMessage(new ChatComponentText(args[0] + " last died on the " + new SimpleDateFormat("d MMM yyyy 'at' HH:mm:ss z").format(death_date) + "."));

							remainder = player_data.shortest_life / 1000L;
							days = (int)remainder / 86400;
							remainder %= 86400L;
							hours = (int)remainder / 3600;
							remainder %= 3600L;
							minutes = (int)remainder / 60;
							remainder %= 60L;
							seconds = (int)remainder;
							player.addChatMessage(new ChatComponentText(args[0] + "'s shortest life was " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds."));

							remainder = player_data.longest_life / 1000L;
							days = (int)remainder / 86400;
							remainder %= 86400L;
							hours = (int)remainder / 3600;
							remainder %= 3600L;
							minutes = (int)remainder / 60;
							remainder %= 60L;
							seconds = (int)remainder;
							player.addChatMessage(new ChatComponentText(args[0] + "'s longest life was " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds."));
						}
						else
						{
							player.addChatMessage(new ChatComponentText(args[0] + " has not died yet."));
						}
						remainder = player_data.alive_time / 1000L;
						days = (int)remainder / 86400;
						remainder %= 86400L;
						hours = (int)remainder / 3600;
						remainder %= 3600L;
						minutes = (int)remainder / 60;
						remainder %= 60L;
						seconds = (int)remainder;
						player.addChatMessage(new ChatComponentText(args[0] + " has been alive for " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds."));
					}
					else
					{
						player.addChatMessage(new ChatComponentText("No uptime data exists for that player."));
					}
				}
			}
			else
			{
				player.addChatMessage(new ChatComponentText("Invalid usage, correct usage is /" + getCommandName() + " [player_name | all]"));
			}
		}
		catch (ClassCastException e) {
            //do something clever with the exception
			UptimeLog.log("SEVERE", e.getMessage());
			UptimeLog.log("WARNING", "You cannot run this command from console");
        }
	}

	public boolean canCommandSenderUseCommand(ICommandSender sender)
	{
		return true;
	}
}
