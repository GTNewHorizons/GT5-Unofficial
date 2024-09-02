package galacticgreg.api.enums.properties;

import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.ModContainers;

public enum Asteroids {

    // spotless : off
    EndAsteroids(ModContainers.Vanilla, DimensionDef.EndAsteroids, new AsteroidPropertyBuilder().probability(200)
        .sizeRange(5, 15)
        .specialBlockChance(5)
        .oreSpawn(
            new AsteroidPropertyBuilder.OreSpawnPropertyBuilder().baseOreChance(5)
                .doesObeyingHeightLimits(false)
                .AreOresOnlyInsideAsteroids(false)
                .primaryToRareOreOffset(5)
                .smallOreChance(10))
        .loot(
            new AsteroidPropertyBuilder.LootPropertyBuilder().lootChestChance(1)
                .lootChestItemCount(10)
                .lootChestTable(3)
                .isLootItemCountRandomized(true))),
    KuiperBelt(ModContainers.GalaxySpace, DimensionDef.KuiperBelt, new AsteroidPropertyBuilder().probability(200)
        .sizeRange(5, 15)
        .specialBlockChance(5)
        .oreSpawn(
            new AsteroidPropertyBuilder.OreSpawnPropertyBuilder().baseOreChance(5)
                .doesObeyingHeightLimits(false)
                .AreOresOnlyInsideAsteroids(false)
                .primaryToRareOreOffset(5)
                .smallOreChance(10))
        .loot(
            new AsteroidPropertyBuilder.LootPropertyBuilder().lootChestChance(1)
                .lootChestItemCount(10)
                .lootChestTable(3)
                .isLootItemCountRandomized(true))),
    MehenBelt(ModContainers.AmunRa, DimensionDef.MehenBelt, new AsteroidPropertyBuilder().probability(200)
        .sizeRange(5, 15)
        .specialBlockChance(5)
        .oreSpawn(
            new AsteroidPropertyBuilder.OreSpawnPropertyBuilder().baseOreChance(5)
                .doesObeyingHeightLimits(false)
                .AreOresOnlyInsideAsteroids(false)
                .primaryToRareOreOffset(5)
                .smallOreChance(10))
        .loot(
            new AsteroidPropertyBuilder.LootPropertyBuilder().lootChestChance(1)
                .lootChestItemCount(10)
                .lootChestTable(3)
                .isLootItemCountRandomized(true))),
    Asteroids(ModContainers.GalacticraftMars, DimensionDef.Asteroids, new AsteroidPropertyBuilder().probability(200)
        .sizeRange(5, 15)
        .specialBlockChance(5)
        .oreSpawn(
            new AsteroidPropertyBuilder.OreSpawnPropertyBuilder().baseOreChance(5)
                .doesObeyingHeightLimits(false)
                .AreOresOnlyInsideAsteroids(false)
                .primaryToRareOreOffset(5)
                .smallOreChance(10))
        .loot(
            new AsteroidPropertyBuilder.LootPropertyBuilder().lootChestChance(1)
                .lootChestItemCount(10)
                .lootChestTable(3)
                .isLootItemCountRandomized(true))),;

    // spotless : on

    public ModContainers modContainers;
    public DimensionDef dimensionDef;
    public AsteroidPropertyBuilder asteroidPropertyBuilder;

    private Asteroids(ModContainers modContainers, DimensionDef dimensionDef,
        AsteroidPropertyBuilder asteroidPropertyBuilder) {
        this.modContainers = modContainers;
        this.dimensionDef = dimensionDef;
        this.asteroidPropertyBuilder = asteroidPropertyBuilder;
    }
}
