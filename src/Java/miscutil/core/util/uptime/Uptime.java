package miscutil.core.util.uptime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

import miscutil.core.commands.CommandUptime;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.google.common.io.Files;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class Uptime {
	private static MinecraftServer server;
	//public static CommonProxy proxy;
	private static Timer updater;
	private static UptimeData uptime_data;
	private static WorldInfo world_info;
	private File uptime_file;
	private File uptime_backup;

	@SuppressWarnings("static-method")
	public void preInit()
	{
		UptimeLog.log("INFO", "Conjumbobulating Time Synthesis.");
		//UptimeLog.initLogger();
	}

	
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

	}

	
	public void serverStarting()
	{
		uptime_data = null;


		server = MinecraftServer.getServer();


		world_info = server.worldServers[0].getWorldInfo();


		File minecraft_folder = server.getFile("");
		String world_folder = server.getFolderName();
		String file_path = minecraft_folder + "/saves/" + world_folder;
		if (!MinecraftServer.getServer().isSinglePlayer()) {
			file_path = minecraft_folder + "/" + world_folder;
		}
		this.uptime_backup = new File(file_path + "/uptime.bk");
		this.uptime_file = new File(file_path + "/uptime.txt");
		if (this.uptime_backup.exists())
		{
			UptimeLog.log("INFO","Uptime: Uptime has found a backup of the uptime file, trying to restore it.", new Object[0]);
			try
			{
				this.uptime_file.delete();
				this.uptime_file.createNewFile();

				Files.copy(this.uptime_backup, this.uptime_file);

				uptime_data = new UptimeData(this.uptime_file, false);

				UptimeLog.log("INFO","Uptime: Uptime restored the backup.", new Object[0]);
			}
			catch (IOException ioe)
			{
				UptimeLog.log("WARNING","Uptime: Failed to restore backup of uptime file, uptime has been reset and the backup removed.", new Object[] { ioe });
				this.uptime_backup.delete();
				uptime_data = new UptimeData(this.uptime_file, true);
			}
			catch (NumberFormatException npe)
			{
				UptimeLog.log("WARNING","Uptime: Backup file is corrupt, uptime has been reset and the backup removed.", new Object[0]);
				this.uptime_backup.delete();
				uptime_data = new UptimeData(this.uptime_file, true);
			}
		}
		else if (this.uptime_file.exists())
		{
			UptimeLog.log("INFO","Uptime: Found uptime file, restoring it.", new Object[0]);
			if (this.uptime_file.length() > 1L)
			{
				try
				{
					uptime_data = new UptimeData(this.uptime_file, false);
				}
				catch (NumberFormatException npe)
				{
					UptimeLog.log("WARNING","Uptime: Uptime file is corrupt, uptime has been reset.", new Object[0]);
					uptime_data = new UptimeData(this.uptime_file, true);
				}
			}
			else
			{
				UptimeLog.log("WARNING","Uptime: Uptime file is empty, uptime has been reset.", new Object[0]);
				uptime_data = new UptimeData(this.uptime_file, true);
			}
		}
		else
		{
			UptimeLog.log("INFO","Uptime: No uptime or backup found, creating a new one.", new Object[0]);


			uptime_data = new UptimeData(this.uptime_file, true);
		}
		updater = new Timer(60000, (ActionListener) this);
		updater.setRepeats(true);
		updater.start();


		ICommandManager command_manager = server.getCommandManager();
		ServerCommandManager server_command_manager = (ServerCommandManager)command_manager;
		server_command_manager.registerCommand(new CommandUptime());


		saveUptime();
	}

	
	public void serverStopping()
	{
		UptimeLog.log("INFO", "upTime is going to stop.");
		if (updater.isRunning())
		{
			updater.stop();
			UptimeLog.log("INFO", "Saving Time Stats.");
			saveUptime();
		}
	}

	public static void updateUptime()
	{
		uptime_data.updateUptime(world_info.getWorldTime());
	}

	public static UptimeData getUptimeData()
	{
		return uptime_data;
	}

	private void saveUptime()
	{
		updateUptime();

		FMLLog.finer("Uptime: Uptime is currently saving its data.", new Object[0]);
		try
		{
			this.uptime_backup.delete();
			this.uptime_backup.createNewFile();
			Files.copy(this.uptime_file, this.uptime_backup);


			this.uptime_file.delete();
			this.uptime_file.createNewFile();

			uptime_data.saveUptime();


			this.uptime_backup.delete();
		}
		catch (IOException ioe)
		{
			UptimeLog.log("WARNING","Uptime: Failed while perpairing to save uptime file.", new Object[] { ioe });
		}
		FMLLog.finer("Uptime: Uptime has finished saving it's data.", new Object[0]);
	}

	@SuppressWarnings("static-method")
	public void onPlayerDeath(LivingDeathEvent lde)
	{
		if ((lde.entity instanceof EntityPlayerMP))
		{
			EntityPlayerMP player = (EntityPlayerMP)lde.entity;


			uptime_data.playerDied(player.getCommandSenderName());

			updateUptime();

			PlayerUptimeData player_data = uptime_data.getPlayerUptimeData(player.getCommandSenderName());


			player.addChatMessage(new ChatComponentText("You died on day " + uptime_data.getWorldDays() + "."));
			player.addChatMessage(new ChatComponentText("This took you " + player_data.player_ondays + " real days."));

			long remainder = player_data.last_life / 1000L;
			int days = (int)remainder / 86400;
			remainder %= 86400L;
			int hours = (int)remainder / 3600;
			remainder %= 3600L;
			int minutes = (int)remainder / 60;
			remainder %= 60L;
			int seconds = (int)remainder;
			player.addChatMessage(new ChatComponentText("In-game time taken: " + days + "d " + hours + "h " + minutes + "m " + seconds + "s."));
			if (player_data.last_life <= 10000L) {
				player.addChatMessage(new ChatComponentText("It takes someone very special to die that quickly."));
			} else if (player_data.last_life <= 20000L) {
				player.addChatMessage(new ChatComponentText("It could of been worse, you could of been the idiot that died in 10 seconds."));
			} else if (player_data.last_life <= 30000L) {
				player.addChatMessage(new ChatComponentText("Might I advise you learn how to run away and hide, and then just stay in there, and don't come out, you'll live longer that way."));
			} else if (player_data.last_life <= 40000L) {
				player.addChatMessage(new ChatComponentText("My grandma could survive longer in Minecraft that then."));
			} else if (player_data.last_life <= 50000L) {
				player.addChatMessage(new ChatComponentText("I would taunt you, but honestly, I think what just happened speaks for itself."));
			} else if (player_data.last_life <= 60000L) {
				player.addChatMessage(new ChatComponentText("Well, that didn't quite go to plan did it now, dust yourself off and try again."));
			} else if (player_data.last_life <= 120000L) {
				player.addChatMessage(new ChatComponentText("Ohhh, you was doing so well, right up to that point at the end there when you died."));
			}
			if (player_data.number_of_deaths == 1)
			{
				player.addChatMessage(new ChatComponentText("That was your first death."));
			}
			else
			{
				Date death_date = new Date(player_data.this_death);
				player.addChatMessage(new ChatComponentText("That was death number " + player_data.number_of_deaths + ". You last died on the " + new SimpleDateFormat("d MMM yyyy 'at' HH:mm:ss z").format(death_date) + "."));
				if (player_data.last_life == player_data.longest_life) {
					player.addChatMessage(new ChatComponentText("That was your longest life, congratulations."));
				} else if (player_data.last_life == player_data.shortest_life) {
					player.addChatMessage(new ChatComponentText("That was your shortest life, really good job there."));
				}
			}
			//Log Readable stats from dying players.
			System.out.println(player.getDisplayName() + " has died.");
			System.out.println("In-game time taken: " + days + "d " + hours + "h " + minutes + "m " + seconds + "s.");
			System.out.println("This took " + player_data.player_ondays + " real days.");
			System.out.println("That was death number " + player_data.number_of_deaths + ".");

		}
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == updater) {
			saveUptime();
		}
	}

	@SuppressWarnings("static-method")
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		uptime_data.playerLoggedOn(event.player.getCommandSenderName());
	}

	@SuppressWarnings("static-method")
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		uptime_data.playerLoggedOff(event.player.getCommandSenderName());
	}

	@SuppressWarnings("static-method")
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		uptime_data.playerRespawned(event.player.getCommandSenderName());
	}
	
	
}
