package miscutil.core.util.uptime;

import cpw.mods.fml.common.FMLLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class UptimeData
{
	private long last_update;
	private long server_uptime;
	private long server_lastday;
	private int server_ondays;
	private long last_world_time;
	private int world_days;
	private HashMap<String, PlayerUptimeData> player_data;
	private File uptime_file;

	public UptimeData(File uptime_file, boolean new_world)
			throws NumberFormatException
	{
		this.uptime_file = uptime_file;
		this.player_data = new HashMap<String, PlayerUptimeData>();
		if (new_world)
		{
			FMLLog.info("Uptime: No uptime file, estimating uptime from worlds total time.", new Object[0]);
			long temp_time = System.currentTimeMillis();

			long world_total_time = net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTotalTime();
			this.last_update = temp_time;
			this.server_uptime = (world_total_time * 50L);
			this.server_lastday = temp_time;
			this.server_ondays = ((int)(this.server_uptime / 86400000L) + 1);
			this.last_world_time = world_total_time;
			this.world_days = ((int)(world_total_time / 24000L) + 1);
			try
			{
				uptime_file.delete();
				uptime_file.createNewFile();
			}
			catch (IOException ioe)
			{
				FMLLog.warning("Uptime: Failed to create uptime file.", new Object[] { ioe });
			}
		}
		else
		{
			Properties properties = new Properties();
			try
			{
				FileInputStream in = new FileInputStream(uptime_file);
				properties.load(in);
				this.last_update = System.currentTimeMillis();
				this.server_uptime = Long.valueOf(properties.getProperty("server_uptime")).longValue();
				this.server_lastday = Long.valueOf(properties.getProperty("server_lastday")).longValue();
				this.server_ondays = Integer.valueOf(properties.getProperty("server_ondays")).intValue();
				this.last_world_time = Long.valueOf(properties.getProperty("last_world_time")).longValue();
				this.world_days = Integer.valueOf(properties.getProperty("world_days")).intValue();
				int players = Integer.valueOf(properties.getProperty("players")).intValue();
				for (int t = 0; t < players; t++)
				{
					String player = properties.getProperty("name_" + t);
					PlayerUptimeData new_data = new PlayerUptimeData(t, properties);
					this.player_data.put(player, new_data);
				}
			}
			catch (IOException ioe)
			{
				FMLLog.warning("Uptime: Failed to read to the uptime file, estimating uptime from worlds total time.", new Object[] { ioe });
				long temp_time = System.currentTimeMillis();

				long world_total_time = net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTotalTime();
				this.last_update = temp_time;
				this.server_uptime = (world_total_time * 50L);
				this.server_lastday = temp_time;
				this.server_ondays = ((int)(this.server_uptime / 86400000L) + 1);
				this.last_world_time = world_total_time;
				this.world_days = ((int)(world_total_time / 24000L) + 1);
			}
		}
	}

	public void saveUptime()
	{
		try
		{
			Properties properties = new Properties();
			properties.setProperty("server_uptime", String.valueOf(this.server_uptime));
			properties.setProperty("server_lastday", String.valueOf(this.server_lastday));
			properties.setProperty("server_ondays", String.valueOf(this.server_ondays));
			properties.setProperty("last_world_time", String.valueOf(this.last_world_time));
			properties.setProperty("world_days", String.valueOf(this.world_days));
			properties.setProperty("players", String.valueOf(this.player_data.size()));


			int player_number = 0;
			for (String player : this.player_data.keySet())
			{
				properties.setProperty("name_" + player_number, player);
				((PlayerUptimeData)this.player_data.get(player)).savePlayerUptimeData(player_number, properties);
				player_number++;
			}
			FileOutputStream out = new FileOutputStream(this.uptime_file);
			properties.store(out, "Server Uptime");
		}
		catch (IOException ioe)
		{
			FMLLog.warning("Uptime: Failed to write to the uptime file.", new Object[] { ioe });
		}
	}

	public void updateUptime(long current_world_time)
	{
		long temp_time = System.currentTimeMillis();
		if (this.last_world_time / 24000L < current_world_time / 24000L)
		{
			this.world_days += 1;
		}
		else if (this.last_world_time > current_world_time)
		{
			this.world_days += 1;
			FMLLog.info("Uptime: World time has gone backwards, assuming /time set was used and increasing world day.", new Object[0]);
		}
		this.last_world_time = current_world_time;


		this.server_uptime += temp_time - this.last_update;


		long current_day = temp_time / 86400000L;
		current_day *= 86400000L;
		if (current_day > this.server_lastday)
		{
			this.server_ondays += 1;
			this.server_lastday = current_day;
		}
		for (PlayerUptimeData player : this.player_data.values()) {
			player.updateUptime();
		}
		this.last_update = temp_time;
	}

	public long getServerUptime()
	{
		return this.server_uptime;
	}

	public int getServerOndays()
	{
		return this.server_ondays;
	}

	public int getWorldDays()
	{
		return this.world_days;
	}

	public void playerLoggedOn(String username)
	{
		if (this.player_data.get(username) == null) {
			this.player_data.put(username, new PlayerUptimeData());
		} else {
			((PlayerUptimeData)this.player_data.get(username)).playerLoggedOn();
		}
	}

	public void playerLoggedOff(String username)
	{
		if (this.player_data.get(username) != null) {
			((PlayerUptimeData)this.player_data.get(username)).playerLoggedOff();
		}
	}

	public void playerDied(String username)
	{
		if (this.player_data.get(username) != null) {
			((PlayerUptimeData)this.player_data.get(username)).playerDied();
		}
	}

	public void playerRespawned(String username)
	{
		if (this.player_data.get(username) != null) {
			((PlayerUptimeData)this.player_data.get(username)).playerRespawned();
		}
	}

	public PlayerUptimeData getPlayerUptimeData(String username)
	{
		if (this.player_data.get(username) != null) {
			return (PlayerUptimeData)this.player_data.get(username);
		}
		return null;
	}

	public void resetUptime()
	{
		for (PlayerUptimeData player : this.player_data.values()) {
			player.resetUptime();
		}
	}
}
