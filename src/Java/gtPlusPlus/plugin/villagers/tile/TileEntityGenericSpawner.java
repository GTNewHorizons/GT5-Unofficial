package gtPlusPlus.plugin.villagers.tile;

import java.util.HashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityGenericSpawner extends TileEntityMobSpawner {

	/*
	 * Static Variables
	 */

	/** A HashMap storing string names of classes mapping to the actual java.lang.Class type. */
	private static Map<?, ?> nameToClassMap_Ex = new HashMap<Object, Object>();
	/** A HashMap storing the classes and mapping to the string names (reverse of nameToClassMap). */
	private static Map<?, ?> classToNameMap_Ex = new HashMap<Object, Object>();

	/**
	 * The Mob Spawner Map
	 */
	public static HashMap<Integer, Class<Entity>> mSpawners = new HashMap<Integer, Class<Entity>>();

	/**
	 * Registers a New Mob Spawner Type
	 * 
	 * @param aID
	 *            - the Spawner type ID
	 * @param aEntity
	 *            - the Entity which you'd like to spawn
	 */
	public static boolean registerNewMobSpawner(int aID, Class<Entity> aEntity) {
		int registered = mSpawners.size();
		Logger.INFO("Currently "+registered+" spawners are registered.");
		mSpawners.put(aID, aEntity);
		return mSpawners.size() > registered;
	}

	/*
	 * Instance Variables
	 */

	/**
	 * The {@link Entity} type which spawns.
	 */
	protected int mID;
	private final Class mSpawnType;
	private MobSpawnerCustomLogic spawnerLogic;

	/*
	 * Constructors
	 */

	/**
	 * Constructs a new Spawner, based on an existing type registered.
	 * 
	 * @param aID
	 *            - The ID in the {@link mSpawners} map.
	 */
	public TileEntityGenericSpawner(int aID) {
		mID = aID;
		if (mSpawners.get(aID) != null) {
			mSpawnType = mSpawners.get(aID);
		} else {
			mSpawnType = null;
		}

		// Last thing to Init
		generateLogicObject();
	}

	/**
	 * Constructs a new Spawner, then registers it.
	 * 
	 * @param aID
	 *            - The ID to be used in the {@link mSpawners} map.
	 * @param aEntity
	 *            - The {@link Entity} type which will be spawned.
	 */
	public TileEntityGenericSpawner(int aID, Entity aEntity) {
		mID = aID;
		if (aEntity != null) {
			mSpawnType = aEntity.getClass();
			if (mSpawners.containsKey(aID)) {
				registerNewMobSpawner(aID, mSpawnType);
			}
		} else {
			mSpawnType = null;
		}

		// Last thing to Init
		generateLogicObject();
	}

	public final MobSpawnerCustomLogic getLogic() {
		if (spawnerLogic == null || spawnerLogic.getSpawnerWorld() == null) {
			generateLogicObject();
		}
		return spawnerLogic;
	}

	private final void generateLogicObject() {
		spawnerLogic = new MobSpawnerCustomLogic(this);
	}

	public int getID() {
		return this.mID;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		if (hasInternalFieldBeenSet()) {
			this.getLogic().readFromNBT(p_145839_1_);
			this.xCoord = p_145839_1_.getInteger("x");
			this.yCoord = p_145839_1_.getInteger("y");
			this.zCoord = p_145839_1_.getInteger("z");
			this.mID = p_145839_1_.getInteger("mID");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		if (hasInternalFieldBeenSet()) {
			String s = (String) classToNameMap_Ex.get(mSpawners.get(this.mID));
			if (s == null){
				//throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
				s = mSpawners.containsKey(this.mID) ? mSpawners.get(this.mID).getSimpleName() : "bad.class.name";  
				p_145841_1_.setString("id", s);
				Logger.WARNING(this.getClass() + " is missing a mapping! This is a bug! Used key: "+s);
				p_145841_1_.setInteger("x", this.xCoord);
				p_145841_1_.setInteger("y", this.yCoord);
				p_145841_1_.setInteger("z", this.zCoord);
				p_145841_1_.setInteger("mID", this.mID);
			}
			else {
				Logger.WARNING(this.getClass() + " is not missing a mapping! Used key: "+s);
				p_145841_1_.setString("id", s);
				p_145841_1_.setInteger("x", this.xCoord);
				p_145841_1_.setInteger("y", this.yCoord);
				p_145841_1_.setInteger("z", this.zCoord);
				p_145841_1_.setInteger("mID", this.mID);
			}
			this.getLogic().writeToNBT(p_145841_1_);
		}
	}

	@Override
	public void updateEntity() {
		if (Utils.isServer()) {
			mTicks++;
			if (hasInternalFieldBeenSet()) {
				this.getLogic().updateSpawner();
			}
		}
	}

	private boolean isReady = false;
	private long mTicks = 0;

	private boolean hasInternalFieldBeenSet() {
		if (isReady && mTicks % 200 != 0) {
			return true;
		} else {

			if (Utils.isServer()) {
				try {				
					Map<?, ?> a1 = (Map<?, ?>) ReflectionUtils.getField(TileEntity.class, "nameToClassMap").get(this);
					Map<?, ?> a2 = (Map<?, ?>) ReflectionUtils.getField(TileEntity.class, "classToNameMap").get(this);				
					if (a1 != null) {
						if (nameToClassMap_Ex == null) {
							nameToClassMap_Ex = a1;
						}
						if (nameToClassMap_Ex != null) {
							if (nameToClassMap_Ex.size() != a1.size()) {
								nameToClassMap_Ex = a1;
							}					
						}
					}
					if (a2 != null) {
						if (classToNameMap_Ex == null) {
							classToNameMap_Ex = a2;			
						}
						if (classToNameMap_Ex != null) {
							if (classToNameMap_Ex.size() != a2.size()) {
								classToNameMap_Ex = a2;
							}			
						}
						if (nameToClassMap_Ex != null && classToNameMap_Ex != null) {
							//Logger.INFO("nameToClassMap_Ex has a size of "+nameToClassMap_Ex.size()+".");
							//Logger.INFO("a1 has a size of "+a1.size()+".");
							//Logger.INFO("classToNameMap_Ex has a size of "+classToNameMap_Ex.size()+".");
							//Logger.INFO("a2 has a size of "+a2.size()+".");
							isReady = true;
							return true;
						}
					}				

					/*Field mInternalLogicField = ReflectionUtils.getField(getClass(), "field_145882_a");
				if (mInternalLogicField != null) {
					MobSpawnerBaseLogic a = (MobSpawnerBaseLogic) mInternalLogicField.get(this);
					if (a != null) {
						ReflectionUtils.setField(this, "field_145882_a", getLogic());
						if (a.equals(getLogic())) {
							isReady = true;
							return true;
						}
					}
				}*/
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
				}
			}
			return false;
		}
	}

	/**
	 * Called when a client event is received with the event number and argument,
	 * see World.sendClientEvent
	 */
	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		return this.spawnerLogic.setDelayToMin(p_145842_1_) ? true : super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public MobSpawnerBaseLogic func_145881_a() {
		return this.spawnerLogic;
	}

}
