package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.XModMaterials;

/// The items of other mods, and of MaterialLib under another prefix's name, that [ForeignPartRecipes#registerTable]
/// generates part recipes, covers and material data for. A row whose item is absent is skipped.
final class ForeignPartRecipeTable {

    /// One item, processed as `oreName` would be under `prefix` and `material`.
    record ForeignPart(OrePrefixes prefix, Material material, String oreName, Supplier<ItemStack> stack) {}

    static List<ForeignPart> parts() {
        return List.of(
            // appliedenergistics2
            part(OrePrefixes.crystal, XModMaterials.Fluix, "appliedenergistics2", "item.ItemMultiMaterial", 7),
            ore(Materials.CertusQuartz, "appliedenergistics2", "tile.OreQuartz", 0),
            ore(Materials.ChargedCertusQuartz, "appliedenergistics2", "tile.OreQuartzCharged", 0),
            part(
                OrePrefixes.crystal,
                Materials.ChargedCertusQuartz,
                "appliedenergistics2",
                "item.ItemMultiMaterial",
                1),
            // BiomesOPlenty
            ore(Materials.Amber, "BiomesOPlenty", "gemOre", 14),
            ore(Materials.Ruby, "BiomesOPlenty", "gemOre", 2),
            ore(Materials.Topaz, "BiomesOPlenty", "gemOre", 6),
            ore(Materials.Tanzanite, "BiomesOPlenty", "gemOre", 8),
            ore(Materials.Malachite, "BiomesOPlenty", "gemOre", 10),
            ore(Materials.Sapphire, "BiomesOPlenty", "gemOre", 12),
            // BuildCraft|Silicon
            part(OrePrefixes.crystal, Materials.Redstone, "BuildCraft|Silicon", "redstoneCrystal", 0),
            // DraconicEvolution
            ore(Materials.Draconium, "DraconicEvolution", "draconiumOre", 0),
            // dreamcraft
            ore(Materials.Zinc, "dreamcraft", "ZincGravelOre", 0),
            part(OrePrefixes.compressed, Materials.Mytryl, "dreamcraft", "MytrylCompressedPlate", 0),
            part(OrePrefixes.compressed, Materials.CallistoIce, "dreamcraft", "CallistoIceCompressedPlate", 0),
            part(OrePrefixes.compressed, Materials.Ledox, "dreamcraft", "LedoxCompressedPlate", 0),
            part(OrePrefixes.plate, Materials.Stone, "dreamcraft", "StonePlate", 0),
            part(OrePrefixes.compressed, Materials.Quantium, "dreamcraft", "QuantinumCompressedPlate", 0),
            part(
                OrePrefixes.compressed,
                Materials.MysteriousCrystal,
                "dreamcraft",
                "MysteriousCrystalCompressedPlate",
                0),
            // EnderIO
            part(OrePrefixes.item, Materials.Silicon, "EnderIO", "itemMaterial", 0),
            // etfuturum
            ore(Materials.Coal, "etfuturum", "deepslate_coal_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Coal, "etfuturum", "deepslate_coal_ore", 0),
            ore(Materials.Lapis, "etfuturum", "deepslate_lapis_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Lapis, "etfuturum", "deepslate_lapis_ore", 0),
            ore(Materials.Diamond, "etfuturum", "deepslate_diamond_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Diamond, "etfuturum", "deepslate_diamond_ore", 0),
            ore(Materials.Emerald, "etfuturum", "deepslate_emerald_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Emerald, "etfuturum", "deepslate_emerald_ore", 0),
            ore(Materials.Copper, "etfuturum", "deepslate_copper_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Copper, "etfuturum", "deepslate_copper_ore", 0),
            ore(Materials.Iron, "etfuturum", "deepslate_iron_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Iron, "etfuturum", "deepslate_iron_ore", 0),
            ore(Materials.Gold, "etfuturum", "deepslate_gold_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Gold, "etfuturum", "deepslate_gold_ore", 0),
            ore(Materials.Redstone, "etfuturum", "deepslate_redstone_ore", 0),
            part(OrePrefixes.oreDeepslate, Materials.Redstone, "etfuturum", "deepslate_redstone_ore", 0),
            // ExtraUtilities
            part(OrePrefixes.nugget, Materials.Unstable, "ExtraUtilities", "unstableingot", 1),
            // ForbiddenMagic
            part(OrePrefixes.nugget, Materials.Emerald, "ForbiddenMagic", "FMResource", 0),
            // Forestry
            ore(Materials.Copper, "Forestry", "resources", 1),
            ore(Materials.Tin, "Forestry", "resources", 2),
            ore(Materials.Apatite, "Forestry", "resources", 0),
            // GalacticraftAmunRa
            ore(Materials.Gold, "GalacticraftAmunRa", "tile.basaltMultiOre", 0),
            ore(Materials.Gold, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 4),
            ore(Materials.Iron, "GalacticraftAmunRa", "tile.basaltMultiOre", 6),
            ore(Materials.Iron, "GalacticraftAmunRa", "tile.hardClayMultiOre", 5),
            ore(Materials.Lapis, "GalacticraftAmunRa", "tile.basaltMultiOre", 7),
            ore(Materials.Lapis, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 5),
            ore(Materials.Diamond, "GalacticraftAmunRa", "tile.obsidianMultiOre", 0),
            ore(Materials.Diamond, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 2),
            ore(Materials.Emerald, "GalacticraftAmunRa", "tile.obsidianMultiOre", 9),
            ore(Materials.Emerald, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 1),
            ore(Materials.Coal, "GalacticraftAmunRa", "tile.hardClayMultiOre", 0),
            ore(Materials.Copper, "GalacticraftAmunRa", "tile.basaltMultiOre", 2),
            ore(Materials.Copper, "GalacticraftAmunRa", "tile.hardClayMultiOre", 3),
            ore(Materials.Copper, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 8),
            ore(Materials.Tin, "GalacticraftAmunRa", "tile.basaltMultiOre", 3),
            ore(Materials.Tin, "GalacticraftAmunRa", "tile.hardClayMultiOre", 4),
            ore(Materials.Uranium, "GalacticraftAmunRa", "tile.basaltMultiOre", 10),
            ore(Materials.Uranium, "GalacticraftAmunRa", "tile.obsidianMultiOre", 2),
            ore(Materials.Uranium, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 7),
            ore(Materials.Lead, "GalacticraftAmunRa", "tile.obsidianMultiOre", 3),
            ore(Materials.Lead, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 6),
            ore(Materials.Aluminium, "GalacticraftAmunRa", "tile.basaltMultiOre", 1),
            ore(Materials.Aluminium, "GalacticraftAmunRa", "tile.hardClayMultiOre", 2),
            ore(Materials.Silicon, "GalacticraftAmunRa", "tile.basaltMultiOre", 8),
            ore(Materials.Silicon, "GalacticraftAmunRa", "tile.hardClayMultiOre", 1),
            ore(Materials.Ruby, "GalacticraftAmunRa", "tile.obsidianMultiOre", 1),
            ore(Materials.Ruby, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 0),
            ore(Materials.Spodumene, "GalacticraftAmunRa", "tile.basaltMultiOre", 5),
            ore(Materials.Spodumene, "GalacticraftAmunRa", "tile.asteroidMultiOre1", 3),
            ore(Materials.Titanium, "GalacticraftAmunRa", "tile.basaltMultiOre", 9),
            ore(Materials.Steel, "GalacticraftAmunRa", "tile.oldConcreteOre", 0),
            part(OrePrefixes.compressed, Materials.Gold, "GalacticraftAmunRa", "item.baseItem", 19),
            // GalacticraftCore
            ore(Materials.Copper, "GalacticraftCore", "tile.gcBlockCore", 5),
            ore(Materials.Copper, "GalacticraftCore", "tile.moonBlock", 0),
            ore(Materials.Tin, "GalacticraftCore", "tile.gcBlockCore", 6),
            ore(Materials.Tin, "GalacticraftCore", "tile.moonBlock", 1),
            ore(Materials.Aluminium, "GalacticraftCore", "tile.gcBlockCore", 7),
            ore(Materials.Silicon, "GalacticraftCore", "tile.gcBlockCore", 8),
            ore(Materials.Cheese, "GalacticraftCore", "tile.moonBlock", 2),
            part(OrePrefixes.compressed, Materials.Copper, "GalacticraftCore", "item.basicItem", 6),
            part(OrePrefixes.compressed, Materials.Tin, "GalacticraftCore", "item.basicItem", 7),
            part(OrePrefixes.compressed, Materials.Aluminium, "GalacticraftCore", "item.basicItem", 8),
            part(OrePrefixes.compressed, Materials.Steel, "GalacticraftCore", "item.basicItem", 9),
            part(OrePrefixes.compressed, Materials.Bronze, "GalacticraftCore", "item.basicItem", 10),
            part(OrePrefixes.compressed, Materials.Iron, "GalacticraftCore", "item.basicItem", 11),
            part(OrePrefixes.compressed, Materials.MeteoricIron, "GalacticraftCore", "item.meteoricIronIngot", 1),
            part(OrePrefixes.item, Materials.Silicon, "GalacticraftCore", "item.basicItem", 2),
            // GalacticraftMars
            ore(Materials.Iron, "GalacticraftMars", "tile.asteroidsBlock", 5),
            ore(Materials.Iron, "GalacticraftMars", "tile.mars", 3),
            ore(Materials.Copper, "GalacticraftMars", "tile.mars", 0),
            ore(Materials.Tin, "GalacticraftMars", "tile.mars", 1),
            ore(Materials.Aluminium, "GalacticraftMars", "tile.asteroidsBlock", 3),
            ore(Materials.Ilmenite, "GalacticraftMars", "tile.asteroidsBlock", 4),
            part(OrePrefixes.compressed, Materials.Titanium, "GalacticraftMars", "item.itemBasicAsteroids", 6),
            part(OrePrefixes.shard, Materials.Titanium, "GalacticraftMars", "item.itemBasicAsteroids", 4),
            part(OrePrefixes.shard, Materials.Iron, "GalacticraftMars", "item.itemBasicAsteroids", 3),
            ore(Materials.Desh, "GalacticraftMars", "tile.mars", 2),
            part(OrePrefixes.compressed, Materials.Desh, "GalacticraftMars", "item.null", 5),
            // GalaxySpace
            ore(Materials.Lead, "GalaxySpace", "leadore", 0),
            ore(Materials.Lead, "GalaxySpace", "mercuryblocks", 3),
            ore(Materials.Iron, "GalaxySpace", "mercuryblocks", 4),
            ore(Materials.Nickel, "GalaxySpace", "mercuryblocks", 5),
            ore(Materials.Diamond, "GalaxySpace", "venusblocks", 3),
            ore(Materials.Sulfur, "GalaxySpace", "venusblocks", 2),
            ore(Materials.Sulfur, "GalaxySpace", "ioblocks", 6),
            ore(Materials.MeteoricIron, "GalaxySpace", "ceresblocks", 3),
            ore(Materials.Cobalt, "GalaxySpace", "phobosblocks", 4),
            ore(Materials.Desh, "GalaxySpace", "phobosblocks", 3),
            ore(Materials.Oriharukon, "GalaxySpace", "deimosblocks", 2),
            ore(Materials.Tin, "GalaxySpace", "deimosblocks", 3),
            ore(Materials.Tungsten, "GalaxySpace", "ioblocks", 5),
            ore(Materials.Tungsten, "GalaxySpace", "proteusblocks", 5),
            ore(Materials.Copper, "GalaxySpace", "ioblocks", 4),
            ore(Materials.Magnesium, "GalaxySpace", "ganymedeblocks", 2),
            ore(Materials.Ilmenite, "GalaxySpace", "ganymedeblocks", 3),
            ore(Materials.Platinum, "GalaxySpace", "proteusblocks", 4),
            ore(Materials.Mithril, "GalaxySpace", "tritonblocks", 3),
            ore(Materials.Iron, "GalaxySpace", "mirandablocks", 3),
            ore(Materials.Iron, "GalaxySpace", "barnardaEoreiron", 0),
            ore(Materials.Gold, "GalaxySpace", "barnardaEoregold", 0),
            ore(Materials.Adamantium, "GalaxySpace", "oberonblocks", 3),
            // gregtech
            alias(OrePrefixes.crystal, Materials.CertusQuartz, Shapes.gem),
            alias(OrePrefixes.crystal, Materials.Quartzite, Shapes.gem),
            alias(OrePrefixes.item, Materials.Silicon, Shapes.plate),
            named(OrePrefixes.plateAlloy, "plateAlloyAdvanced", () -> ItemList.AdvancedAlloyPlate.get(1)),
            named(OrePrefixes.plateAlloy, "plateAlloyCarbon", () -> ItemList.CarbonAlloyPlate.get(1)),
            // HardcoreEnderExpansion
            ore(Materials.HeeEndium, "HardcoreEnderExpansion", "endium_ore", 0),
            // harvestcraft
            ore(Materials.Salt, "harvestcraft", "salt", 0),
            // IC2
            ore(Materials.Copper, "IC2", "blockOreCopper", 0),
            ore(Materials.Tin, "IC2", "blockOreTin", 0),
            ore(Materials.Uranium, "IC2", "blockOreUran", 0),
            ore(Materials.Lead, "IC2", "blockOreLead", 0),
            part(OrePrefixes.plateDense, Materials.Lapis, "IC2", "itemDensePlates", 8),
            part(OrePrefixes.plateDense, Materials.Obsidian, "IC2", "itemDensePlates", 7),
            named(OrePrefixes.plateAlloy, "plateAlloyAdvanced", "IC2", "itemPartAlloy", 0),
            named(OrePrefixes.plateAlloy, "plateAlloyCarbon", "IC2", "itemPartCarbonPlate", 0),
            // MagicBees
            part(OrePrefixes.nugget, Materials.Emerald, "MagicBees", "beeNugget", 6),
            part(OrePrefixes.nugget, Materials.Diamond, "MagicBees", "beeNugget", 5),
            part(OrePrefixes.nugget, Materials.Apatite, "MagicBees", "beeNugget", 7),
            // minecraft
            ore(Materials.Gold, "minecraft", "gold_ore", 0),
            ore(Materials.Iron, "minecraft", "iron_ore", 0),
            ore(Materials.Lapis, "minecraft", "lapis_ore", 0),
            ore(Materials.Diamond, "minecraft", "diamond_ore", 0),
            ore(Materials.Redstone, "minecraft", "redstone_ore", 0),
            ore(Materials.Emerald, "minecraft", "emerald_ore", 0),
            ore(Materials.Coal, "minecraft", "coal_ore", 0),
            part(OrePrefixes.crystal, Materials.NetherQuartz, "minecraft", "quartz", 0),
            ore(Materials.NetherQuartz, "minecraft", "quartz_ore", 0),
            part(OrePrefixes.item, Materials.Wheat, "minecraft", "wheat", WILDCARD),
            // ProjRed|Exploration
            ore(Materials.Ruby, "ProjRed|Exploration", "projectred.exploration.ore", 0),
            ore(Materials.Sapphire, "ProjRed|Exploration", "projectred.exploration.ore", 1),
            ore(Materials.Copper, "ProjRed|Exploration", "projectred.exploration.ore", 3),
            ore(Materials.Tin, "ProjRed|Exploration", "projectred.exploration.ore", 4),
            ore(Materials.Silver, "ProjRed|Exploration", "projectred.exploration.ore", 5),
            ore(Materials.Electrotine, "ProjRed|Exploration", "projectred.exploration.ore", 6),
            // Railcraft
            ore(Materials.Lapis, "Railcraft", "ore", 4),
            ore(Materials.Diamond, "Railcraft", "ore", 2),
            ore(Materials.Emerald, "Railcraft", "ore", 3),
            ore(Materials.Firestone, "Railcraft", "ore", 5),
            // SGCraft
            ore(Materials.Naquadah, "SGCraft", "naquadahOre", 0),
            // TaintedMagic
            ore(Materials.Shadow, "TaintedMagic", "BlockShadowOre", 0),
            // TConstruct
            ore(Materials.Gold, "TConstruct", "GravelOre", 1),
            ore(Materials.Iron, "TConstruct", "GravelOre", 0),
            ore(Materials.Copper, "TConstruct", "SearedBrick", 3),
            ore(Materials.Copper, "TConstruct", "GravelOre", 2),
            ore(Materials.Tin, "TConstruct", "SearedBrick", 4),
            ore(Materials.Tin, "TConstruct", "GravelOre", 3),
            ore(Materials.Cobalt, "TConstruct", "SearedBrick", 1),
            ore(Materials.Cobalt, "TConstruct", "GravelOre", 5),
            ore(Materials.Ardite, "TConstruct", "SearedBrick", 2),
            ore(Materials.Aluminium, "TConstruct", "SearedBrick", 5),
            ore(Materials.Aluminium, "TConstruct", "GravelOre", 4),
            // Thaumcraft
            ore(Materials.Cinnabar, "Thaumcraft", "blockCustomOre", 0),
            ore(Materials.InfusedAir, "Thaumcraft", "blockCustomOre", 1),
            ore(Materials.InfusedFire, "Thaumcraft", "blockCustomOre", 2),
            ore(Materials.InfusedWater, "Thaumcraft", "blockCustomOre", 3),
            ore(Materials.InfusedEarth, "Thaumcraft", "blockCustomOre", 4),
            ore(Materials.InfusedOrder, "Thaumcraft", "blockCustomOre", 5),
            ore(Materials.InfusedEntropy, "Thaumcraft", "blockCustomOre", 6),
            ore(Materials.Amber, "Thaumcraft", "blockCustomOre", 7),
            // Translocator
            part(OrePrefixes.nugget, Materials.Diamond, "Translocator", "diamondNugget", 0),
            // TwilightForest
            ore(Materials.Knightmetal, "TwilightForest", "item.shardCluster", 0));
    }

    private static ForeignPart ore(Material material, String modId, String name, int meta) {
        return part(OrePrefixes.ore, material, modId, name, meta);
    }

    private static ForeignPart part(OrePrefixes prefix, Material material, String modId, String name, int meta) {
        return new ForeignPart(
            prefix,
            material,
            prefix.getName() + material.getName(),
            () -> modStack(modId, name, meta));
    }

    private static ForeignPart alias(OrePrefixes prefix, Material material, Shape shape) {
        return new ForeignPart(
            prefix,
            material,
            prefix.getName() + material.getName(),
            () -> MaterialLibAPI.getStack(material, shape, 1));
    }

    private static ForeignPart named(OrePrefixes prefix, String oreName, String modId, String name, int meta) {
        return named(prefix, oreName, () -> modStack(modId, name, meta));
    }

    private static ForeignPart named(OrePrefixes prefix, String oreName, Supplier<ItemStack> stack) {
        return new ForeignPart(prefix, Materials.NULL, oreName, stack);
    }

    private static ItemStack modStack(String modId, String name, int meta) {
        Item item = GameRegistry.findItem(modId, name);
        return item == null ? null : new ItemStack(item, 1, meta);
    }

    private ForeignPartRecipeTable() {}
}
