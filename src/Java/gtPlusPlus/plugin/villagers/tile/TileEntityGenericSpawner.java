package gtPlusPlus.plugin.villagers.tile;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityGenericSpawner extends TileEntityMobSpawner {
	
	/*
	 * Static Variables
	 */
	
	/**
	 * The Mob Spawner Map
	 */
	public static HashMap<Integer, Entity> mSpawners = new HashMap<Integer, Entity>();
	
	/**
	 * Registers a New Mob Spawner Type
	 * @param aID - the Spawner type ID
	 * @param aEntity - the Entity which you'd like to spawn
	 */
	public static boolean registerNewMobSpawner(int aID, Entity aEntity) {
		int registered = mSpawners.size();
		mSpawners.put(aID, aEntity);
		return mSpawners.size() > registered;
	}
	
	/*
	 * Instance Variables
	 */
	
	/**
	 * The {@link Entity} type which spawns.
	 */
	private final Entity mSpawnType;
	
	
	
	/*
	 * Constructors
	 */
	
	/**
	 * Constructs a new Spawner, based on an existing type registered.
	 * @param aID - The ID in the {@link mSpawners} map.
	 */
	public TileEntityGenericSpawner(int aID) {
		if (mSpawners.get(aID) != null) {
			mSpawnType = mSpawners.get(aID);			
		}
		else {
			mSpawnType = null;
		}
	}


	/**
	 * Constructs a new Spawner, then registers it.
	 * @param aID - The ID to be used in the {@link mSpawners} map.
	 * @param aEntity - The {@link Entity} type which will be spawned.
	 */
	public TileEntityGenericSpawner(int aID, Entity aEntity) {
		if (aEntity != null) {
			mSpawnType = aEntity;
			this.registerNewMobSpawner(aID, aEntity);
		}
		else {
			mSpawnType = null;
		}
	}
	

}
