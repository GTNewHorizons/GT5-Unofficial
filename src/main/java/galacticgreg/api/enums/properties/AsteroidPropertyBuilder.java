package galacticgreg.api.enums.properties;

public class AsteroidPropertyBuilder {

    public boolean enabled = true;
    public int probability;
    public int sizeMin, sizeMax;
    public int specialBlockChance;
    public float oreDensityMultiplier = 1f;
    public int smallOreChance = 10;
    public LootPropertyBuilder loot;
    public int positiveEllipsoids = 2, negativeEllipsoids = 2;
    public int asteroidMinY = 50, asteroidMaxY = 100;

    public static class LootPropertyBuilder {

        public int lootChestChance;
        public int lootChestItemCount;
        public int lootChestTable;
        public boolean randomizeLootItemCount;

        public LootPropertyBuilder lootChestChance(int lootChestChance) {
            this.lootChestChance = lootChestChance;
            return this;
        }

        public LootPropertyBuilder lootChestItemCount(int lootChestItemCount) {
            this.lootChestItemCount = lootChestItemCount;
            return this;
        }

        public LootPropertyBuilder lootChestTable(int lootChestTable) {
            this.lootChestTable = lootChestTable;
            return this;
        }

        public LootPropertyBuilder isLootItemCountRandomized(boolean randomizeLootItemCount) {
            this.randomizeLootItemCount = randomizeLootItemCount;
            return this;
        }

    }

    public AsteroidPropertyBuilder() {
        loot = new LootPropertyBuilder();
    }

    public AsteroidPropertyBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public AsteroidPropertyBuilder probability(int probability) {
        this.probability = probability;
        return this;
    }

    public AsteroidPropertyBuilder sizeRange(int sizeMin, int sizeMax) {
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        return this;
    }

    public AsteroidPropertyBuilder specialBlockChance(int specialBlockChance) {
        this.specialBlockChance = specialBlockChance;
        return this;
    }

    public AsteroidPropertyBuilder oreDensityMultiplier(float mult) {
        this.oreDensityMultiplier = mult;
        return this;
    }

    public AsteroidPropertyBuilder smallOreChance(int smallOreChance) {
        this.smallOreChance = smallOreChance;
        return this;
    }

    public AsteroidPropertyBuilder positiveEllipsoids(int positiveEllipsoids) {
        this.positiveEllipsoids = positiveEllipsoids;
        return this;
    }

    public AsteroidPropertyBuilder negativeEllipsoids(int negativeEllipsoids) {
        this.negativeEllipsoids = negativeEllipsoids;
        return this;
    }

    public AsteroidPropertyBuilder asteroidYBounds(int minY, int maxY) {
        this.asteroidMinY = minY;
        this.asteroidMaxY = maxY;
        return this;
    }

    public AsteroidPropertyBuilder loot(LootPropertyBuilder lootPropertyBuilder) {
        this.loot = lootPropertyBuilder;
        return this;
    }
}
