package toxiceverglades.biome;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.config.Configuration;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import toxiceverglades.dimension.DimensionEverglades;

public class BiomeEverglades {

    public static BiomeGenEverglades biome = new BiomeGenEverglades();

    public Object instance;

    public BiomeEverglades() {}

    public void load() {
        BiomeDictionary.registerBiomeType(biome, BiomeDictionary.Type.DEAD);
    }

    static class BiomeGenEverglades extends BiomeGenBase {

        public BiomeGenEverglades() {
            super(Configuration.worldgen.EVERGLADESBIOME_ID);
            this.theBiomeDecorator = new CustomBiomeGenerator();
            this.theBiomeDecorator.treesPerChunk = 2;
            this.setBiomeName("Toxic Everglades");
            this.topBlock = DimensionEverglades.blockTopLayer;
            this.fillerBlock = DimensionEverglades.blockSecondLayer;
            this.enableRain = true;
            this.enableSnow = false;
            this.rainfall = 0.7F;
            this.setHeight(new BiomeGenBase.Height(-0.25F, 0.4F));
            this.waterColorMultiplier = 0x17290A;

            this.spawnableMonsterList.clear();
            this.spawnableCreatureList.clear();
            this.spawnableWaterCreatureList.clear();
            this.spawnableCaveCreatureList.clear();

            // Enemies
            this.spawnableMonsterList.add(new SpawnListEntry(EntitySickBlaze.class, 40, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 35, 2, 4));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityStaballoyConstruct.class, 10, 1, 1));

            // Animals
            this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 1, 1, 6));
            this.spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 10, 8, 8));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int getSkyColorByTemp(float par1) {
            return 0xF67A14;
        }
    }
}
