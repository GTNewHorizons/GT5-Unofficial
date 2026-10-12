package gtnhintergalactic.recipe;

import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtnhintergalactic.recipe.IGRecipeMaps.spaceMiningRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import akka.japi.Pair;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.GTOreAdapter;
import gtnhintergalactic.item.ItemMiningDrones;

/**
 * Available asteroids for space mining are defined here
 *
 * @author minecraft7771
 */
public class SpaceMiningRecipes {

    // Asteroids for drone tier -> Asteroids at given distance (and their index on the unique asteroid list)
    public static final List<Map<Integer, List<Pair<Integer, GTRecipe>>>> asteroidDistanceMap = new ArrayList<>();
    public static final List<AsteroidData> uniqueAsteroidList = new ArrayList<>();
    /** List of mining drones to be used in recipe creation */
    public static final ItemStack[] MINING_DRONES = new ItemStack[ItemMiningDrones.DroneTiers.values().length];
    /** Drills used for recipe creation */
    public static final ItemStack[] MINING_DRILLS = new ItemStack[ItemMiningDrones.DroneMaterials.values().length];
    /** Rods used for recipe creation */
    public static final ItemStack[] MINING_RODS = new ItemStack[ItemMiningDrones.DroneMaterials.values().length];
    /** Map from mining drones back to tiers */
    private static final Map<GTUtility.ItemId, Integer> DRONE_TIERS = new HashMap<>();

    static {
        for (ItemMiningDrones.DroneTiers droneTier : ItemMiningDrones.DroneTiers.values()) {
            int i = droneTier.ordinal();
            ItemStack drone = droneTier.get(0);
            MINING_DRONES[i] = drone;
            DRONE_TIERS.put(GTUtility.ItemId.createWithoutNBT(drone), i);
        }
        for (ItemMiningDrones.DroneMaterials mat : ItemMiningDrones.DroneMaterials.values()) {
            MINING_DRILLS[mat.ordinal()] = MaterialLibAPI.getStack(mat.getMaterial(), Shapes.toolHeadDrill, 4);
            MINING_RODS[mat.ordinal()] = MaterialLibAPI.getStack(mat.getMaterial(), Shapes.stick, 4);
        }
    }

    /**
     * Add all asteroid definitions to the recipe map
     */
    public static void addAsteroids() {
        for (int i = 0; i < MINING_DRONES.length; i++) {
            asteroidDistanceMap.add(new TreeMap<>());
        }
        // Coal Asteroid
        addRecipesToDrones(
            "coalAsteroid",
            null,
            null,
            new int[] { 7000, 1000, 2000 },
            new Material[] { Materials.Coal, Materials.Lignite, Materials.Graphite },
            StoneType.Endstone,
            30,
            120,
            1,
            40,
            20,
            1,
            10 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            200);
        // Salt Asteroid
        addRecipesToDrones(
            "saltAsteroid",
            null,
            null,
            new int[] { 4000, 2000, 4000 },
            new Material[] { Materials.Salt, Materials.RockSalt, Materials.Saltpeter },
            StoneType.Endstone,
            30,
            120,
            1,
            250,
            20,
            1,
            10 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            300);
        // Iron Asteroid
        addRecipesToDrones(
            "ironAsteroid",
            null,
            null,
            new int[] { 4000, 2000, 1000, 1000, 500, 500 },
            new Material[] { Materials.Iron, Materials.Gold, Materials.Magnetite, Materials.Pyrite,
                Materials.BasalticMineralSand, Materials.GraniticMineralSand },
            StoneType.Endstone,
            30,
            150,
            1,
            180,
            10,
            1,
            10 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            600);

        // Copper Asteroid
        addRecipesToDrones(
            "copperAsteroid",
            null,
            null,
            new int[] { 5000, 3000, 2000 },
            new Material[] { Materials.Copper, Materials.Chalcopyrite, Materials.Malachite },
            StoneType.Endstone,
            30,
            150,
            3,
            12,
            10,
            1,
            10 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            500);

        // Tin Asteroid
        addRecipesToDrones(
            "tinAsteroid",
            null,
            null,
            new int[] { 2000, 1500, 6000, 500 },
            new Material[] { Materials.Cassiterite, Materials.CassiteriteSand, Materials.Tin, Materials.Asbestos },
            StoneType.Endstone,
            50,
            200,
            2,
            100,
            10,
            1,
            50,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            400);

        // Aluminium Asteroid
        addRecipesToDrones(
            "aluminiumAsteroid",
            null,
            null,
            new int[] { 5000, 3500, 1500 },
            new Material[] { Materials.Aluminium, Materials.Bauxite, Materials.Rutile },
            StoneType.Endstone,
            10,
            20,
            5,
            20,
            20,
            1,
            50,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.MV.ordinal(),
            ItemMiningDrones.DroneTiers.EV.ordinal(),
            120);

        // Nickel Asteroid
        addRecipesToDrones(
            "nickelAsteroid",
            null,
            null,
            new int[] { 4000, 3000, 3000 },
            new Material[] { Materials.Nickel, Materials.Pentlandite, Materials.Garnierite },
            StoneType.Endstone,
            20,
            40,
            5,
            20,
            20,
            1,
            50,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            170);

        // Chrome Asteroid
        addRecipesToDrones(
            "chromeAsteroid",
            null,
            null,
            new int[] { 5000, 3000, 2000 },
            new Material[] { Materials.Chrome, Materials.Ruby, Materials.Chromite },
            StoneType.Endstone,
            16,
            32,
            10,
            20,
            40,
            1,
            50,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.MV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            100);

        // PlatLine Ore Asteroid
        addRecipesToDrones(
            "platlineOreAsteroid",
            null,
            null,
            new int[] { 6000, 2000, 1500, 500 },
            new Material[] { Materials.Platinum, Materials.Palladium, Materials.Iridium, Materials.Osmium },
            StoneType.Endstone,
            20,
            40,
            10,
            50,
            60,
            1,
            50,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            130);

        // Gem Asteroid
        addRecipesToDrones(
            "gemAsteroid",
            null,
            null,
            new int[] { 1500, 1500, 1500, 1500, 750, 750, 750, 1000, 500, 500, 400, 100 },
            new Material[] { Materials.Ruby, Materials.Emerald, Materials.Sapphire, Materials.GreenSapphire,
                Materials.Diamond, Materials.Opal, Materials.Amethyst, Materials.Topaz, Materials.BlueTopaz,
                Materials.Bauxite, Materials.Vinteum, Materials.NetherStar },
            StoneType.Endstone,
            30,
            160,
            17,
            40,
            60,
            1,
            100,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            180);

        // Indium Asteroid
        addRecipesToDrones(
            "indiumAsteroid",
            null,
            null,
            new int[] { 6000, 2000, 1000, 1000 },
            new Material[] { Materials.Indium, Materials.Sphalerite, Materials.Zinc, Materials.Cadmium },
            StoneType.Endstone,
            30,
            120,
            50,
            90,
            120,
            2,
            500,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UEV.ordinal(),
            170);

        // Thaumium Asteroid
        addRecipesToDrones(
            "thaumiumAsteroid",
            null,
            null,
            new int[] { 6000, 4000 },
            new Material[] { Materials.Thaumium, Materials.Void },
            null,
            20,
            50,
            10,
            70,
            120,
            1,
            30 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            150);

        // Basic Magic Asteroid
        addRecipesToDrones(
            "basicMagicAsteroid",
            null,
            null,
            new int[] { 3500, 3500, 500, 500, 500, 500, 500, 500 },
            new Material[] { Materials.InfusedGold, Materials.Shadow, Materials.InfusedAir, Materials.InfusedEarth,
                Materials.InfusedFire, Materials.InfusedWater, Materials.InfusedEntropy, Materials.InfusedOrder },
            StoneType.Endstone,
            24,
            60,
            8,
            24,
            120,
            1,
            100,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            200);

        // Niobium Asteroid
        addRecipesToDrones(
            "niobiumAsteroid",
            null,
            null,
            new int[] { 3000, 2000, 1500, 3500 },
            new Material[] { Materials.Niobium, Materials.Quantium, Materials.Ytterbium, Materials.Yttrium },
            StoneType.Endstone,
            30,
            120,
            30,
            160,
            120,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            160);

        // Quartz Asteroid
        addRecipesToDrones(
            "quartzAsteroid",
            null,
            null,
            new int[] { 3000, 2250, 2250, 2500 },
            new Material[] { Materials.Quartzite, Materials.CertusQuartz, Materials.NetherQuartz, Materials.Vanadium },
            StoneType.Endstone,
            20,
            80,
            20,
            120,
            50,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.MV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            230);

        // Lutetium Asteroid
        addRecipesToDrones(
            "lutetiumAsteroid",
            null,
            null,
            new int[] { 1500, 1000, 1500, 500, 5500 },
            new Material[] { Materials.Tellurium, Materials.Thulium, Materials.Tantalum, Materials.Lutetium,
                Materials.Redstone },
            StoneType.Endstone,
            20,
            80,
            40,
            240,
            90,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            100);

        // Silicon Asteroid
        addRecipesToDrones(
            "siliconAsteroid",
            null,
            null,
            new int[] { 2000, 4500, 2500 },
            new Material[] { Materials.Mica, Materials.Silicon, Materials.SiliconSolarGrade },
            StoneType.Endstone,
            20,
            80,
            50,
            250,
            60,
            2,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            200);

        // Phosphate Asteroid
        addRecipesToDrones(
            "phosphateAsteroid",
            null,
            null,
            new int[] { 4500, 2500, 3000, 3000 },
            new Material[] { Materials.Phosphate, Materials.TricalciumPhosphate, Materials.Sulfur, Materials.Apatite },
            StoneType.Endstone,
            20,
            150,
            60,
            250,
            60,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UIV.ordinal(),
            150);

        // Space Ore Asteroid
        addRecipesToDrones(
            "spaceOreAsteroid",
            null,
            null,
            new int[] { 2000, 2000, 2000, 1000, 2000, 2000, 3000, 3000, 3000, 3000, 3000, 3000 },
            new Material[] { Materials.MeteoricIron, Materials.DeepIron, Materials.Mytryl, Materials.BlackPlutonium,
                Materials.CallistoIce, Materials.Ledox, Materials.Alduorite, Materials.Rubracium, Materials.Vulcanite,
                Materials.Vyroxeres, Materials.Ceruclase, Materials.Orichalcum },
            StoneType.Endstone,
            20,
            50,
            30,
            100,
            60,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            150);

        // Lead Asteroid
        addRecipesToDrones(
            "leadAsteroid",
            null,
            null,
            new int[] { 3000, 2500, 2500, 2000 },
            new Material[] { Materials.Lead, Materials.Arsenic, Materials.Barium, Materials.Lepidolite },
            StoneType.Endstone,
            30,
            100,
            5,
            150,
            20,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            220);

        // Adamantium Asteroid
        addRecipesToDrones(
            "adamantiumAsteroid",
            null,
            null,
            new int[] { 2500, 2000, 2000, 2000, 1500 },
            new Material[] { Materials.Adamantium, Materials.Bismuth, Materials.Antimony, Materials.Gallium,
                Materials.Lithium },
            StoneType.Endstone,
            30,
            120,
            5,
            120,
            20,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_EV,
            ItemMiningDrones.DroneTiers.EV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            300);

        // Tungsten-Titanium Asteroid
        addRecipesToDrones(
            "tungstenTitaniumAsteroid",
            null,
            null,
            new int[] { 3000, 3000, 2000, 1500, 500 },
            new Material[] { Materials.Tungsten, Materials.Titanium, Materials.Neodymium, Materials.Molybdenum,
                Materials.Tungstate },
            StoneType.Endstone,
            30,
            70,
            60,
            200,
            120,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            100);

        // Blue Asteroid
        addRecipesToDrones(
            "blueAsteroid",
            null,
            null,
            new int[] { 6000, 2000, 1000, 1000 },
            new Material[] { Materials.Lapis, Materials.Calcite, Materials.Lazurite, Materials.Sodalite },
            StoneType.Endstone,
            10,
            50,
            20,
            200,
            60,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            250);

        // Aluminium-LanthLine Asteroid
        addRecipesToDrones(
            "aluminiumLanthlineAsteroid",
            null,
            null,
            new int[] { 3500, 1500, 2500, 2500 },
            new Material[] { Materials.Aluminium, Materials.Bauxite, Materials.Monazite, Materials.Bastnasite },
            StoneType.Endstone,
            10,
            80,
            40,
            120,
            60,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.MV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            250);

        // Everglades Asteroid
        addRecipesToDrones(
            "evergladesAsteroid",
            null,
            null,
            new int[] { 600, 400, 1500, 1500, 1000, 400, 1000, 600, 1000, 1000, 1000 },
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Koboldite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Crocoite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.GadoliniteY, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Lepersonnite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Zircon, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Lautarite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Honeaite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.Alburnite, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.RareEarthI, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.RareEarthII, OreShapes.ore, 64),
                MaterialLibAPI.getStack(Materials.RareEarthIII, OreShapes.ore, 64) },
            10,
            20,
            110,
            230,
            200,
            1,
            25 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            100);

        // Draconic Asteroid
        addRecipesToDrones(
            "draconicAsteroid",
            null,
            null,
            new int[] { 6500, 2500, 1000 },
            new Material[] { Materials.Draconium, Materials.DraconiumAwakened, Materials.ElectrumFlux },
            StoneType.Endstone,
            15,
            60,
            60,
            200,
            360,
            2,
            30 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            190);

        // CHEEEEEESEEE Asteroid
        addRecipesToDrones(
            "cheeseAsteroid",
            null,
            null,
            new int[] { 10000 },
            new Material[] { Materials.Cheese },
            StoneType.Endstone,
            1,
            30,
            90,
            200,
            240,
            2,
            50 * SECONDS,
            (int) TierEU.RECIPE_ZPM,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            10);

        // Ardite/Cobalt Asteroid
        addRecipesToDrones(
            "arditeCobaltAsteroid",
            null,
            null,
            new int[] { 3750, 3750, 2500 },
            new Material[] { Materials.Cobalt, Materials.Ardite, Materials.Manyullyn },
            StoneType.Stone,
            20,
            90,
            30,
            100,
            180,
            1,
            50 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.EV.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            150);

        // Nether Ore Asteroid
        addRecipesToDrones(
            "NetherOreAsteroid",
            null,
            null,
            new int[] { 3000, 3000, 2000, 1500, 500 },
            new Material[] { Materials.NetherQuartz, Materials.Sulfur, Materials.CertusQuartz, Materials.Quartzite,
                Materials.Firestone },
            StoneType.Netherrack,
            30,
            110,
            10,
            100,
            240,
            1,
            50 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.EV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            150);

        // Europium Asteroid
        addRecipesToDrones(
            "europiumAsteroid",
            null,
            null,
            new int[] { 4000, 4000, 1500, 500 },
            new Material[] { Materials.Ledox, Materials.CallistoIce, Materials.Borax, Materials.Europium },
            StoneType.Endstone,
            40,
            120,
            40,
            60,
            240,
            2,
            50 * SECONDS,
            (int) TierEU.RECIPE_ZPM,
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            150);

        // Cosmic Asteroid
        addRecipesToDrones(
            "cosmicAsteroid",
            null,
            null,
            new int[] { 2500, 2500, 2500, 2500 },
            new Material[] { Materials.CosmicNeutronium, Materials.Neutronium, Materials.BlackPlutonium,
                Materials.Bedrockium },
            StoneType.Endstone,
            10,
            70,
            60,
            100,
            240,
            2,
            500,
            (int) TierEU.RECIPE_UV,
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            170);

        // Infinity Catalyst Asteroid
        addRecipesToDrones(
            "infinityCatalystAsteroid",
            null,
            null,
            new int[] { 5000, 3000, 2000 },
            new Material[] { Materials.InfinityCatalyst, Materials.CosmicNeutronium, Materials.Neutronium },
            StoneType.Endstone,
            30,
            120,
            70,
            100,
            320,
            2,
            1000,
            (int) TierEU.RECIPE_UV,
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            150);

        // Ichorium Asteroid
        addRecipesToDrones(
            "ichoriumAsteroid",
            null,
            null,
            new int[] { 4500, 3000, 1500, 500, 500 },
            new Material[] { Materials.ShadowIron, Materials.MeteoricIron, Materials.Ichorium, Materials.Desh,
                Materials.Americium },
            StoneType.Endstone,
            30,
            120,
            70,
            100,
            320,
            3,
            1000,
            (int) TierEU.RECIPE_UV,
            ItemMiningDrones.DroneTiers.UEV.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            150);

        // Clay asteroid
        addRecipesToDrones(
            "clayAsteroid",
            null,
            null,
            new int[] { 10000 },
            new ItemStack[] { new ItemStack(Blocks.clay, 64) },
            30,
            60,
            20,
            100,
            30,
            1,
            40 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.LV.ordinal(),
            ItemMiningDrones.DroneTiers.LuV.ordinal(),
            200);

        if (HardcoreEnderExpansion.isModLoaded()) {
            // Mysterious Crystal Asteroid
            addRecipesToDrones(
                "mysteriousCrystalAsteroid",
                null,
                null,
                new int[] { 7400, 2000, 500, 98, 2 },
                new ItemStack[] {
                    GTUtility.copyAmount(
                        64,
                        GTOreAdapter.INSTANCE.getVariantStack(Materials.MysteriousCrystal, StoneType.Endstone, false)),
                    GTUtility.copyAmount(
                        64,
                        GTOreAdapter.INSTANCE.getVariantStack(Materials.Mytryl, StoneType.Endstone, false)),
                    GTUtility.copyAmount(
                        64,
                        GTOreAdapter.INSTANCE.getVariantStack(Materials.Oriharukon, StoneType.Endstone, false)),
                    GTModHandler.getModItem(HardcoreEnderExpansion.ID, "endium_ore", 64),
                    GTModHandler.getModItem(HardcoreEnderExpansion.ID, "end_powder_ore", 64) },
                30,
                60,
                65,
                120,
                300,
                1,
                500,
                (int) TierEU.RECIPE_ZPM,
                ItemMiningDrones.DroneTiers.IV.ordinal(),
                ItemMiningDrones.DroneTiers.UXV.ordinal(),
                220);
        }

        // Uranium-Plutonium Asteroid
        addRecipesToDrones(
            "uraniumPlutoniumAsteroid",
            null,
            null,
            new int[] { 3000, 2450, 2450, 2000, 100 },
            new Material[] { Materials.Uranium, Materials.Uranium235, Materials.Plutonium, Materials.Plutonium241,
                Materials.Thorianite },
            StoneType.Stone,
            40,
            180,
            30,
            70,
            120,
            1,
            20 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.HV.ordinal(),
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            150);

        // Holmium/Samarium Asteroid
        addRecipesToDrones(
            "holmiumSamariumAsteroid",
            null,
            null,
            new int[] { 2000, 3000, 3000, 2000 },
            new Material[] { Materials.Holmium, Materials.Samarium, Materials.Tiberium, Materials.Strontium },
            StoneType.Stone,
            15,
            50,
            40,
            80,
            260,
            2,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            75);

        // PlatLine Pure Asteroid
        addRecipesToDrones(
            "platlinePureAsteroid",
            null,
            null,
            new int[] { 3800, 2000, 1500, 500, 1200, 1000 },
            new Material[] { Materials.Platinum, Materials.Palladium, Materials.Iridium, Materials.Osmium,
                Materials.Ruthenium, Materials.Rhodium },
            null,
            10,
            30,
            25,
            200,
            360,
            2,
            500,
            (int) TierEU.RECIPE_ZPM,
            ItemMiningDrones.DroneTiers.ZPM.ordinal(),
            ItemMiningDrones.DroneTiers.UEV.ordinal(),
            60);

        // Magnesium Asteroid
        addRecipesToDrones(
            "magnesiumAsteroid",
            null,
            null,
            new int[] { 4000, 3000, 3000 },
            new Material[] { Materials.Magnesium, Materials.Manganese, Materials.Fluorspar },
            StoneType.Stone,
            10,
            80,
            10,
            200,
            60,
            1,
            20 * SECONDS,
            (int) TierEU.RECIPE_IV,
            ItemMiningDrones.DroneTiers.EV.ordinal(),
            ItemMiningDrones.DroneTiers.UHV.ordinal(),
            250);

        // Lanthanum Asteroid
        addRecipesToDrones(
            "lanthanumAsteroid",
            null,
            null,
            new int[] { 1500, 2000, 3000, 3500 },
            new Material[] { Materials.Trinium, Materials.Lanthanum, Materials.Orundum, Materials.Silver },
            StoneType.Stone,
            30,
            120,
            30,
            230,
            120,
            2,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UIV.ordinal(),
            150);

        // Tengam Asteroid
        addRecipesToDrones(
            "tengamAsteroid",
            null,
            null,
            new int[] { 100, 1650, 3500, 2250, 2500 },
            new Material[] { Materials.Dilithium, Materials.Orundum, Materials.Vanadium, Materials.Ytterbium,
                Materials.TengamRaw },
            StoneType.Stone,
            5,
            100,
            20,
            100,
            120,
            3,
            25 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.UEV.ordinal(),
            ItemMiningDrones.DroneTiers.UXV.ordinal(),
            50);

        // Naquadah Asteroid
        addRecipesToDrones(
            "naquadahAsteroid",
            null,
            null,
            new int[] { 4000, 3500, 2500 },
            new Material[] { Materials.NaquadahOxideMixture, Materials.EnrichedNaquadahOxideMixture,
                Materials.NaquadriaOxideMixture },
            StoneType.Stone,
            20,
            80,
            50,
            150,
            240,
            1,
            50 * SECONDS,
            (int) TierEU.RECIPE_LuV,
            ItemMiningDrones.DroneTiers.IV.ordinal(),
            ItemMiningDrones.DroneTiers.UV.ordinal(),
            200);

        // Draconic Core Ruin
        if (DraconicEvolution.isModLoaded()) {
            addRecipesToDrones(
                "draconicCoreRuin",
                null,
                null,
                new int[] { 100, 100, 9800 },
                new ItemStack[] { kubatech.api.enums.ItemList.DEFCDraconicSchematic.get(1),
                    GTModHandler.getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                    ItemList.ZPM.getWithCharge(1, Integer.MAX_VALUE - 1) },
                1,
                1,
                50,
                200,
                1000,
                3,
                100 * SECONDS,
                (int) TierEU.RECIPE_UEV,
                ItemMiningDrones.DroneTiers.UHV.ordinal(),
                ItemMiningDrones.DroneTiers.UIV.ordinal(),
                1);
        }

        // Get all asteroids
        WeightedAsteroidList asteroids = new WeightedAsteroidList(
            spaceMiningRecipes.getAllRecipes()
                .stream());
        // Find max distance
        int maxDistance = 0;
        for (GTRecipe recipe : asteroids.recipes) {
            maxDistance = Math.max(maxDistance, recipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA).maxDistance);
        }

        // Map every asteroid to a distance range
        int[][] visited = new int[MINING_DRONES.length][maxDistance + 1];
        for (int i = 0; i < asteroids.recipes.size(); i++) {
            GTRecipe recipe = asteroids.recipes.get(i);
            int droneTier = recipe.mInputs[0].getItemDamage();
            SpaceMiningData meta = recipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
            assert meta != null;
            for (int j = meta.minDistance; j <= meta.maxDistance; j++) {
                List<Pair<Integer, GTRecipe>> pairList = asteroidDistanceMap.get(droneTier)
                    .get(j);
                Pair<Integer, GTRecipe> pair = pairList.get(visited[droneTier][j]);
                pair = new Pair<>(pair.first(), recipe);
                pairList.set(visited[droneTier][j], pair);
                visited[droneTier][j]++;

            }
        }
    }

    /**
     * Returns the drone (not consumed) and other items used for mining operations with a given drone tier. NB: the
     * first entry in the result is the drone, which has stack size zero
     */
    public static ItemStack[] getTieredInputs(int tier) {
        return new ItemStack[] { MINING_DRONES[tier], MINING_DRILLS[tier], MINING_RODS[tier] };
    }

    public static Optional<Integer> getTierFromDrone(ItemStack drone) {
        return Optional.ofNullable(DRONE_TIERS.getOrDefault(GTUtility.ItemId.createWithoutNBT(drone), null));
    }

    private static void addRecipesToDrones(String asteroidName, ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
        int[] aChances, ItemStack[] aItemOutputs, int minSize, int maxSize, int minDistance, int maxDistance,
        int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int startDroneTier, int endDroneTier,
        int recipeWeight) {
        ItemStack[] tItemInputs;
        if (aItemInputs == null) {
            tItemInputs = new ItemStack[3];
        } else {
            tItemInputs = new ItemStack[aItemInputs.length + 3];
            System.arraycopy(aItemInputs, 0, tItemInputs, 3, aItemInputs.length);
        }
        for (int i = startDroneTier; i <= endDroneTier; i++) {
            for (int j = minDistance; j <= maxDistance; j++) {
                asteroidDistanceMap.get(i)
                    .computeIfAbsent(j, k -> new ArrayList<>())
                    .add(new Pair<>(uniqueAsteroidList.size(), null));
            }
            tItemInputs[0] = MINING_DRONES[i];
            tItemInputs[1] = MINING_DRILLS[i];
            tItemInputs[2] = MINING_RODS[i];
            IG_RecipeAdder.addSpaceMiningRecipe(
                asteroidName,
                tItemInputs,
                aFluidInputs,
                aChances,
                aItemOutputs,
                minSize + (int) GTUtility.powInt(2, i - startDroneTier) - 1,
                maxSize + (int) GTUtility.powInt(2, i - startDroneTier) - 1,
                minDistance,
                maxDistance,
                computationRequiredPerSec,
                minModuleTier,
                (int) Math.ceil(duration / Math.sqrt(i - startDroneTier + 1)),
                EUt,
                recipeWeight);
        }
        uniqueAsteroidList.add(
            new AsteroidData(
                asteroidName,
                minDistance,
                maxDistance,
                minSize,
                maxSize,
                computationRequiredPerSec,
                recipeWeight,
                startDroneTier,
                endDroneTier,
                aItemOutputs,
                aChances,
                minModuleTier,
                duration,
                EUt));
    }

    private static void addRecipesToDrones(String asteroidName, ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
        int[] aChances, Material[] ores, StoneType stoneType, int minSize, int maxSize, int minDistance,
        int maxDistance, int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int startDroneTier,
        int endDroneTier, int recipeWeight) {
        ItemStack[] tItemInputs;
        if (aItemInputs == null) {
            tItemInputs = new ItemStack[3];
        } else {
            tItemInputs = new ItemStack[aItemInputs.length + 3];
            System.arraycopy(aItemInputs, 0, tItemInputs, 3, aItemInputs.length);
        }
        for (int i = startDroneTier; i <= endDroneTier; i++) {
            for (int j = minDistance; j <= maxDistance; j++) {
                asteroidDistanceMap.get(i)
                    .computeIfAbsent(j, k -> new ArrayList<>())
                    .add(new Pair<>(uniqueAsteroidList.size(), null));
            }
            tItemInputs[0] = MINING_DRONES[i];
            tItemInputs[1] = MINING_DRILLS[i];
            tItemInputs[2] = MINING_RODS[i];
            IG_RecipeAdder.addSpaceMiningRecipe(
                asteroidName,
                tItemInputs,
                aFluidInputs,
                aChances,
                ores,
                stoneType,
                minSize + (int) GTUtility.powInt(2, i - startDroneTier) - 1,
                maxSize + (int) GTUtility.powInt(2, i - startDroneTier) - 1,
                minDistance,
                maxDistance,
                computationRequiredPerSec,
                minModuleTier,
                (int) Math.ceil(duration / Math.sqrt(i - startDroneTier + 1)),
                (int) Math.ceil(EUt * Math.sqrt(i - startDroneTier + 1)),
                recipeWeight);
        }
        uniqueAsteroidList.add(
            new AsteroidData(
                asteroidName,
                minDistance,
                maxDistance,
                minSize,
                maxSize,
                computationRequiredPerSec,
                recipeWeight,
                startDroneTier,
                endDroneTier,
                ores,
                stoneType,
                aChances,
                minModuleTier,
                duration,
                EUt));
    }

    /**
     * A list of space mining recipes with precomputed total weight and total "time density", usually cached in the
     * space mining module
     *
     * @author hacatu
     */
    public static class WeightedAsteroidList {

        public List<GTRecipe> recipes;
        public int totalWeight;
        public int totalTimedensity;

        public WeightedAsteroidList(Stream<GTRecipe> inRecipes) {
            recipes = new ArrayList<>();
            inRecipes.forEach(r -> {
                SpaceMiningData data = r.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                if (data != null) {
                    recipes.add(r);
                    totalWeight += data.recipeWeight;
                    totalTimedensity += data.recipeWeight * r.mDuration;
                }
            });
        }

        public GTRecipe getRandom() {
            int i = 0;
            double r = Math.random() * totalWeight;
            while (i < recipes.size() - 1) {
                SpaceMiningData data = recipes.get(i)
                    .getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                if (data == null) throw new IllegalStateException("Illegal space miner recipe found");
                r -= data.recipeWeight;
                if (r <= 0) {
                    break;
                }
                i++;
            }
            return recipes.get(i);
        }
    }
}
