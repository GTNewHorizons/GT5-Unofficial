package gtPlusPlus.xmod.galacticraft.system.objects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import gtPlusPlus.api.objects.data.Pair;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import net.minecraft.block.Block;

public class PlanetGenerator {

	private final Planet mPlanet;
	private final IPlanetBlockRegister mTask;
	private final Map<Integer, Pair<String, Block>> mPlanetBlocks;
	private final Thread mTaskThread;
	
	public static final Map<String, PlanetGenerator> mGlobalPlanetCache = new HashMap<String, PlanetGenerator>();
	
	public PlanetGenerator(Planet aPlanet, IPlanetBlockRegister aBlockRegistrationTask) {
		mPlanet = aPlanet;
		mTask = aBlockRegistrationTask;
		mPlanetBlocks = new LinkedHashMap<Integer, Pair<String, Block>>();
		for (int i=0;i<4;i++) {
			Block b = aBlockRegistrationTask.getBlocks().get(i);
			if (b != null)
			mPlanetBlocks.put(i, new Pair<String, Block>(b.getUnlocalizedName(), b));
		}
		if (mGlobalPlanetCache.get(mPlanet.getName().toUpperCase()) == null) {
			mGlobalPlanetCache.put(mPlanet.getName().toUpperCase(), this);
		}
		else {
			try {
				this.finalize();
			} catch (Throwable e) {
			}
		}
		mTaskThread = new Thread(aBlockRegistrationTask);
		mTaskThread.start();
	}

	public synchronized final Planet getPlanet() {
		return mPlanet;
	}

	public synchronized final IPlanetBlockRegister getTask() {
		return mTask;
	}

	public synchronized final Map<Integer, Pair<String, Block>> getPlanetBlocks() {
		return mPlanetBlocks;
	}
	
}
