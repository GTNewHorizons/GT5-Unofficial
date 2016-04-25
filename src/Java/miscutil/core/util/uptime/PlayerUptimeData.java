package miscutil.core.util.uptime;

import java.util.Properties;

public class PlayerUptimeData
{
	public long last_update;
	public long first_joined;
	public long last_active;
	public long played_time;
	public long player_lastday;
	public int player_ondays;
	public long this_death;
	public long last_death;
	public long alive_time;
	public int number_of_deaths;
	public long last_life;
	public long shortest_life;
	public long longest_life;
	public boolean online;

	public PlayerUptimeData()
	{
		long temp_time = System.currentTimeMillis();

		this.first_joined = temp_time;
		this.last_active = temp_time;
		this.played_time = 0L;

		this.player_lastday = temp_time;
		this.player_ondays = 1;

		this.this_death = temp_time;
		this.last_death = temp_time;
		this.alive_time = 0L;
		this.number_of_deaths = 0;
		this.last_life = 0L;
		this.shortest_life = 0L;
		this.longest_life = 0L;

		this.online = true;
		this.last_update = temp_time;
	}

	public PlayerUptimeData(int player_number, Properties properties)
			throws NumberFormatException
	{
		long temp_time = System.currentTimeMillis();

		this.first_joined = Long.valueOf(properties.getProperty("first_joined_" + player_number)).longValue();
		this.last_active = Long.valueOf(properties.getProperty("last_active_" + player_number)).longValue();
		this.played_time = Long.valueOf(properties.getProperty("played_time_" + player_number)).longValue();

		this.player_lastday = Long.valueOf(properties.getProperty("player_lastday_" + player_number)).longValue();
		this.player_ondays = Integer.valueOf(properties.getProperty("player_ondays_" + player_number)).intValue();

		this.this_death = Long.valueOf(properties.getProperty("this_death_" + player_number)).longValue();
		this.last_death = Long.valueOf(properties.getProperty("last_death_" + player_number)).longValue();
		this.alive_time = Long.valueOf(properties.getProperty("alive_time_" + player_number)).longValue();
		this.number_of_deaths = Integer.valueOf(properties.getProperty("number_of_deaths_" + player_number)).intValue();
		this.last_life = Long.valueOf(properties.getProperty("last_life_" + player_number)).longValue();
		this.shortest_life = Long.valueOf(properties.getProperty("shortest_life_" + player_number)).longValue();
		this.longest_life = Long.valueOf(properties.getProperty("longest_life_" + player_number)).longValue();

		this.online = false;
		this.last_update = temp_time;
	}

	public void savePlayerUptimeData(int player_number, Properties player_properties)
	{
		player_properties.setProperty("first_joined_" + player_number, String.valueOf(this.first_joined));
		player_properties.setProperty("last_active_" + player_number, String.valueOf(this.last_active));
		player_properties.setProperty("played_time_" + player_number, String.valueOf(this.played_time));
		player_properties.setProperty("player_lastday_" + player_number, String.valueOf(this.player_lastday));
		player_properties.setProperty("player_ondays_" + player_number, String.valueOf(this.player_ondays));
		player_properties.setProperty("this_death_" + player_number, String.valueOf(this.this_death));
		player_properties.setProperty("last_death_" + player_number, String.valueOf(this.last_death));
		player_properties.setProperty("alive_time_" + player_number, String.valueOf(this.alive_time));
		player_properties.setProperty("number_of_deaths_" + player_number, String.valueOf(this.number_of_deaths));
		player_properties.setProperty("last_life_" + player_number, String.valueOf(this.last_life));
		player_properties.setProperty("shortest_life_" + player_number, String.valueOf(this.shortest_life));
		player_properties.setProperty("longest_life_" + player_number, String.valueOf(this.longest_life));
	}

	public void playerLoggedOn()
	{
		long temp_time = System.currentTimeMillis();
		if (!this.online)
		{
			this.last_active = temp_time;


			this.online = true;
		}
		this.last_update = temp_time;
	}

	public void playerLoggedOff()
	{
		long temp_time = System.currentTimeMillis();
		if (this.online)
		{
			this.last_active = temp_time;
			this.played_time += temp_time - this.last_update;


			this.alive_time += temp_time - this.last_update;


			this.online = false;
		}
		this.last_update = temp_time;
	}

	public void playerDied()
	{
		long temp_time = System.currentTimeMillis();


		updateUptime();


		this.this_death = temp_time;
		this.number_of_deaths += 1;


		this.last_life = this.alive_time;
		if (this.number_of_deaths == 1)
		{
			this.shortest_life = this.last_life;
			this.longest_life = this.last_life;
		}
		else if (this.last_life < this.shortest_life)
		{
			this.shortest_life = this.last_life;
		}
		else if (this.last_life > this.longest_life)
		{
			this.longest_life = this.last_life;
		}
	}

	public void playerRespawned()
	{
		long temp_time = System.currentTimeMillis();

		this.last_active = temp_time;
		this.played_time += temp_time - this.last_update;

		this.alive_time = 0L;
		this.last_death = this.this_death;

		this.last_update = temp_time;
	}

	public void updateUptime()
	{
		long temp_time = System.currentTimeMillis();
		if (this.online)
		{
			this.last_active = temp_time;
			this.played_time += temp_time - this.last_update;


			this.alive_time += temp_time - this.last_update;


			long current_day = temp_time / 86400000L;
			current_day *= 86400000L;
			if (current_day > this.player_lastday)
			{
				this.player_ondays += 1;
				this.player_lastday = current_day;
			}
		}
		this.last_update = temp_time;
	}

	public void resetUptime()
	{
		long temp_time = System.currentTimeMillis();

		this.played_time = 0L;

		this.player_lastday = temp_time;
		this.player_ondays = 1;

		this.this_death = temp_time;
		this.last_death = temp_time;
		this.alive_time = 0L;
		this.number_of_deaths = 0;
		this.last_life = 0L;
		this.shortest_life = 0L;
		this.longest_life = 0L;

		this.online = true;
		this.last_update = temp_time;
	}
}
