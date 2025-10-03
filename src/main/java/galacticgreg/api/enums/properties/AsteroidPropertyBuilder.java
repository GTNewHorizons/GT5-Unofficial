package galacticgreg.api.enums.properties;

import org.jetbrains.annotations.NotNull;

public class AsteroidPropertyBuilder {

    public int probability;
    public int sizeMin, sizeMax;
    public int specialBlockChance;
    public OreSpawnPropertyBuilder oreSpawn;
    public LootPropertyBuilder loot;

    public static class OreSpawnPropertyBuilder {

        public int baseOreChance;
        public boolean obeyHeighLimits;
        public boolean oresOnlyInsideAsteroids;
        public int primaryToRareOreOffset;
        public int smallOreChance;

        public @NotNull OreSpawnPropertyBuilder baseOreChance(int baseOreChance) {
            this.baseOreChance = baseOreChance;
            return this;
        }

        public @NotNull OreSpawnPropertyBuilder doesObeyingHeightLimits(boolean obeyHeighLimits) {
            this.obeyHeighLimits = obeyHeighLimits;
            return this;
        }

        public @NotNull OreSpawnPropertyBuilder AreOresOnlyInsideAsteroids(boolean oresOnlyInsideAsteroids) {
            this.oresOnlyInsideAsteroids = oresOnlyInsideAsteroids;
            return this;
        }

        public @NotNull OreSpawnPropertyBuilder primaryToRareOreOffset(int primaryToRareOreOffset) {
            this.primaryToRareOreOffset = primaryToRareOreOffset;
            return this;
        }

        public @NotNull OreSpawnPropertyBuilder smallOreChance(int smallOreChance) {
            this.smallOreChance = smallOreChance;
            return this;
        }

    }

    public static class LootPropertyBuilder {

        public int lootChestChance;
        public int lootChestItemCount;
        public int lootChestTable;
        public boolean randomizeLootItemCount;

        public @NotNull LootPropertyBuilder lootChestChance(int lootChestChance) {
            this.lootChestChance = lootChestChance;
            return this;
        }

        public @NotNull LootPropertyBuilder lootChestItemCount(int lootChestItemCount) {
            this.lootChestItemCount = lootChestItemCount;
            return this;
        }

        public @NotNull LootPropertyBuilder lootChestTable(int lootChestTable) {
            this.lootChestTable = lootChestTable;
            return this;
        }

        public @NotNull LootPropertyBuilder isLootItemCountRandomized(boolean randomizeLootItemCount) {
            this.randomizeLootItemCount = randomizeLootItemCount;
            return this;
        }

    }

    public AsteroidPropertyBuilder() {
        oreSpawn = new OreSpawnPropertyBuilder();
        loot = new LootPropertyBuilder();
    }

    public @NotNull AsteroidPropertyBuilder probability(int probability) {
        this.probability = probability;
        return this;
    }

    public @NotNull AsteroidPropertyBuilder sizeRange(int sizeMin, int sizeMax) {
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        return this;
    }

    public @NotNull AsteroidPropertyBuilder specialBlockChance(int specialBlockChance) {
        this.specialBlockChance = specialBlockChance;
        return this;
    }

    public @NotNull AsteroidPropertyBuilder oreSpawn(OreSpawnPropertyBuilder oreSpawnPropertyBuilder) {
        this.oreSpawn = oreSpawnPropertyBuilder;
        return this;
    }

    public @NotNull AsteroidPropertyBuilder loot(LootPropertyBuilder lootPropertyBuilder) {
        this.loot = lootPropertyBuilder;
        return this;
    }
}
