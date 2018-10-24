package gtPlusPlus.xmod.galacticraft.system.core.world.gen.biome;

import gtPlusPlus.xmod.galacticraft.system.core.world.gen.GalacticBiomeGenBase;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeGenGalaxy extends GalacticBiomeGenBase {
	public BiomeGenGalaxy(int var1) {
		super(var1);
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		if (!ConfigManagerCore.disableBiomeTypeRegistrations) {
			BiomeDictionary.registerBiomeType(this, new Type[]{Type.COLD, Type.DRY, Type.DEAD, Type.SPOOKY});
		}

	}
}