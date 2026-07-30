package gregtech.api.enums.materials;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.Material;

/// gtPlusPlus's curated mixer-recipe composite table (component material + ratio parts), pinning the exact
/// entries the retired `gtPlusPlus.xmod.gregtech.loaders.RecipeGenDustGeneration` mixer generator emitted a
/// recipe for: a material with 1-4 composites (the retired gtPlusPlus `Material#getComposites`), reduced to
/// gtpp's smallest-ratio form (`gtPlusPlus.core.util.math.MathUtils#simplifyNumbersToSmallestForm`), reached
/// through one of the construction paths that builds `RecipeGenDustGeneration` with `disableOptional=false` --
/// `MaterialGenerator.generate`/`generateOreMaterialWithAllExcessComponents` (both construct it unconditionally),
/// or `gtPlusPlus.core.util.minecraft.ItemUtils#generateSpecialUseDusts`/`gtPlusPlus.core.util.minecraft.
/// MaterialUtils#generateSpecialDustAndAssignToAMaterial` called with mixer recipes left enabled (e.g.
/// `WoodsGlass`, which reaches the mixer block through `ItemUtils#generateSpecialUseDusts` rather than
/// `generate`). This is narrower than MaterialLib's own [gregtech.api.material.GTMaterialProperties#COMPOSITION],
/// which records a material's chemical makeup -- a broader, differently-shaped dataset than the crafting ratio
/// these recipes mix.
///
/// A composite-bearing material reachable only through `generateNuclearDusts` (which always disables optional
/// recipes, including the mixer block), `generateOreMaterial` (which never constructs
/// `RecipeGenDustGeneration` at all), or the above `ItemUtils`/`MaterialUtils` helpers called with mixer
/// recipes explicitly disabled, never reached the legacy mixer block, so it has no entry here despite
/// superficially qualifying by composite count alone -- likewise a `PURE_LIQUID`/`PURE_GAS`-state material
/// registered through `MaterialGenerator.generate` (which returns before constructing
/// `RecipeGenDustGeneration` for those states), though that state check does not apply to a material reached
/// through one of the other construction paths instead. `EglinSteelBaseCompound` is the one entry generated
/// through `addMixerRecipe_Standalone` instead of the ordinary mixer block -- see
/// [gregtech.loaders.oreprocessing.ProcessingMixerGtpp]'s circuit handling.
///
/// [gregtech.loaders.oreprocessing.ProcessingAlloyBlastSmelter] reuses these same ratios for the retired
/// `gtPlusPlus.xmod.gregtech.loaders.RecipeGenBlastSmelter`'s composite-dust-to-molten-fluid recipe -- that
/// generator read the identical `Material#getComposites`/`vSmallestRatio` fields the mixer did, so a shared
/// entry's parts are valid for both consumers. Its own eligibility is neither a subset nor a superset of the
/// mixer's: `BloodSteel`, `Nitinol60`, and `Botmium` are mixer-eligible entries the blast smelter's own
/// material blacklist/`generateBlastSmelterRecipes=false` calls exclude, while `AbyssalAlloy`, `AncientGranite`,
/// `ArceusAlloy2B`, `BlackTitanium`, `CinobiteA243`, `Grisium`, `HS188A`, `HastelloyC276`, `HastelloyN`,
/// `HastelloyW`, `HastelloyX`, `HeLiCoPtEr`, `Inconel625`, `LafiumCompound`, `Laurenium`, `MaragingSteel250`,
/// `MaragingSteel300`, `MaragingSteel350`, `Octiron`, `Pikyonium64B`, `Quantum`, `WatertightSteel`, and
/// `Zeron100` are blast-smelter-eligible entries the mixer generator's own (undocumented, not reproduced here)
/// filtering never reached. See [gregtech.loaders.oreprocessing.ProcessingAlloyBlastSmelter] for the blast
/// smelter's own declared eligibility.
public final class LegacyGTPPComposites {

    @Desugar
    public record Component(Material material, int parts) {}

    private static final Map<Material, List<Component>> TABLE = new LinkedHashMap<>();

    private LegacyGTPPComposites() {}

    public static boolean has(Material material) {
        return TABLE.containsKey(material);
    }

    public static List<Component> composites(Material material) {
        return TABLE.getOrDefault(material, List.of());
    }

    private static void add(Material output, Component... components) {
        TABLE.put(output, List.of(components));
    }

    private static Component of(Material material, int parts) {
        return new Component(material, parts);
    }

    // spotless:off
    static {
        add(Materials.AbyssalAlloy,
            of(Materials.StainlessSteel, 5), of(Materials.TungstenCarbide, 5),
            of(Materials.Nichrome, 5), of(Materials.Bronze, 5),
            of(Materials.IncoloyMA956, 5), of(Materials.Iodine, 1),
            of(Materials.Radon, 1), of(Materials.Germanium, 1));
        add(Materials.AncientGranite,
            of(Materials.Oxygen, 8), of(Materials.Iron, 5),
            of(Materials.SiliconDioxide, 5), of(Materials.Alumina, 3),
            of(Materials.Potassium, 3), of(Materials.Calcium, 2),
            of(Materials.Sodium, 2), of(Materials.Ytterbium, 1));
        add(Materials.Arcanite,
            of(Materials.Thorium232, 4), of(Materials.EnergyCrystal, 4),
            of(Materials.InfusedOrder, 1), of(Materials.InfusedEntropy, 1));
        add(Materials.ArceusAlloy2B,
            of(Materials.Trinium, 3), of(Materials.MaragingSteel350, 4),
            of(Materials.TungstenSteel, 2), of(Materials.Osmiridium, 1),
            of(Materials.Strontium, 1));
        add(Materials.BabbitAlloy,
            of(Materials.Tin, 5), of(Materials.Lead, 36),
            of(Materials.Antimony, 8), of(Materials.Arsenic, 1));
        add(Materials.BlackMetal,
            of(Materials.Lead, 3), of(Materials.Manganese, 5), of(Materials.Carbon, 12));
        add(Materials.BlackTitanium,
            of(Materials.Titanium, 55), of(Materials.Lanthanum, 12),
            of(Materials.Tungsten, 8), of(Materials.Cobalt, 6),
            of(Materials.Manganese, 4), of(Materials.Phosphorus, 4),
            of(Materials.Palladium, 4), of(Materials.Niobium, 2),
            of(Materials.Argon, 5));
        add(Materials.BloodSteel,
            of(Materials.Steel, 1), of(Materials.InfusedFire, 1));
        add(Materials.Botmium,
            of(Materials.Nitinol60, 1), of(Materials.Osmium, 6),
            of(Materials.Ruthenium, 6), of(Materials.Thallium, 3));
        add(Materials.CinobiteA243,
            of(Materials.Zeron100, 16), of(Materials.Naquadria, 7),
            of(Materials.Gadolinium, 5), of(Materials.Aluminium, 3),
            of(Materials.Mercury, 2), of(Materials.Tin, 2),
            of(Materials.Titanium, 12), of(Materials.Osmiridium, 6));
        add(Materials.EglinSteel,
            of(Materials.EglinSteelBaseCompound, 10), of(Materials.Sulfur, 1),
            of(Materials.Silicon, 4), of(Materials.Carbon, 1));
        add(Materials.EglinSteelBaseCompound,
            of(Materials.Iron, 4), of(Materials.Kanthal, 1), of(Materials.Invar, 5));
        add(Materials.EnergyCrystal,
            of(Materials.InfusedAir, 1), of(Materials.InfusedEarth, 1),
            of(Materials.InfusedFire, 1), of(Materials.InfusedWater, 1));
        add(Materials.Grisium,
            of(Materials.Titanium, 9), of(Materials.Carbon, 9),
            of(Materials.Potassium, 9), of(Materials.Lithium, 9),
            of(Materials.Sulfur, 9), of(Materials.Hydrogen, 5));
        add(Materials.HastelloyC276,
            of(Materials.Cobalt, 1), of(Materials.Molybdenum, 8),
            of(Materials.Tungsten, 1), of(Materials.Copper, 1),
            of(Materials.Chrome, 7), of(Materials.Nickel, 32));
        add(Materials.HastelloyN,
            of(Materials.Yttrium, 2), of(Materials.Molybdenum, 4),
            of(Materials.Chrome, 2), of(Materials.Titanium, 2),
            of(Materials.Nickel, 15));
        add(Materials.HastelloyW,
            of(Materials.Iron, 3), of(Materials.Cobalt, 1),
            of(Materials.Molybdenum, 12), of(Materials.Chrome, 3),
            of(Materials.Nickel, 31));
        add(Materials.HastelloyX,
            of(Materials.Iron, 9), of(Materials.Manganese, 1),
            of(Materials.Silicon, 1), of(Materials.Molybdenum, 4),
            of(Materials.Chrome, 11), of(Materials.Nickel, 24));
        add(Materials.HeLiCoPtEr,
            of(Materials.Helium, 1), of(Materials.Lithium, 1),
            of(Materials.Cobalt, 1), of(Materials.Platinum, 1),
            of(Materials.Erbium, 1));
        add(Materials.HS188A,
            of(Materials.Cobalt, 10), of(Materials.Hafnium, 10),
            of(Materials.Talonite, 8), of(Materials.Rhenium, 5),
            of(Materials.NiobiumCarbide, 5), of(Materials.HastelloyX, 4),
            of(Materials.TungstenSteel, 4), of(Materials.ZirconiumCarbide, 4));
        add(Materials.Incoloy020,
            of(Materials.Iron, 10), of(Materials.Copper, 1),
            of(Materials.Chrome, 5), of(Materials.Nickel, 9));
        add(Materials.IncoloyDS,
            of(Materials.Iron, 23), of(Materials.Cobalt, 9),
            of(Materials.Chrome, 9), of(Materials.Nickel, 9));
        add(Materials.IncoloyMA956,
            of(Materials.Iron, 16), of(Materials.Aluminium, 3),
            of(Materials.Chrome, 5), of(Materials.Yttrium, 1));
        add(Materials.Inconel625,
            of(Materials.Nickel, 3), of(Materials.Chrome, 7),
            of(Materials.Molybdenum, 10), of(Materials.Invar, 10),
            of(Materials.Nichrome, 13));
        add(Materials.Inconel690,
            of(Materials.Chrome, 1), of(Materials.Niobium, 2),
            of(Materials.Molybdenum, 2), of(Materials.Nichrome, 3));
        add(Materials.Inconel792,
            of(Materials.Nickel, 2), of(Materials.Niobium, 1),
            of(Materials.Aluminium, 2), of(Materials.Nichrome, 1));
        add(Materials.LafiumCompound,
            of(Materials.HastelloyN, 4), of(Materials.Naquadah, 2),
            of(Materials.Samarium, 1), of(Materials.Tungsten, 2),
            of(Materials.Argon, 1), of(Materials.Aluminium, 3),
            of(Materials.Nickel, 4), of(Materials.Carbon, 1));
        add(Materials.Laurenium,
            of(Materials.EglinSteel, 8), of(Materials.Indium, 2),
            of(Materials.Chrome, 4), of(Materials.Dysprosium, 1),
            of(Materials.Rhenium, 1));
        add(Materials.MaragingSteel250,
            of(Materials.Steel, 16), of(Materials.Molybdenum, 1),
            of(Materials.Titanium, 1), of(Materials.Nickel, 4),
            of(Materials.Cobalt, 2));
        add(Materials.MaragingSteel300,
            of(Materials.Steel, 16), of(Materials.Titanium, 1),
            of(Materials.Aluminium, 1), of(Materials.Nickel, 4),
            of(Materials.Cobalt, 2));
        add(Materials.MaragingSteel350,
            of(Materials.Steel, 16), of(Materials.Aluminium, 1),
            of(Materials.Molybdenum, 1), of(Materials.Nickel, 4),
            of(Materials.Cobalt, 2));
        add(Materials.NiobiumCarbide,
            of(Materials.Niobium, 1), of(Materials.Carbon, 1));
        add(Materials.Nitinol60,
            of(Materials.Nickel, 2), of(Materials.Titanium, 3));
        add(Materials.Octiron,
            of(Materials.Arcanite, 6), of(Materials.Titansteel, 6),
            of(Materials.EnergyCrystal, 1), of(Materials.BlackSteel, 2),
            of(Materials.Thaumium, 5));
        add(Materials.Pikyonium64B,
            of(Materials.Inconel792, 8), of(Materials.EglinSteel, 5),
            of(Materials.NaquadahEnriched, 4), of(Materials.Cerium, 3),
            of(Materials.Antimony, 2), of(Materials.Platinum, 2),
            of(Materials.Ytterbium, 1), of(Materials.TungstenSteel, 4));
        add(Materials.Potin,
            of(Materials.Lead, 2), of(Materials.Bronze, 2), of(Materials.Tin, 1));
        add(Materials.Quantum,
            of(Materials.Stellite, 3), of(Materials.EnergyCrystal, 1),
            of(Materials.SiliconCarbide, 1), of(Materials.Gallium, 1),
            of(Materials.Americium, 1), of(Materials.Palladium, 1),
            of(Materials.Bismuth, 1), of(Materials.Germanium, 1));
        add(Materials.SiliconCarbide,
            of(Materials.Silicon, 1), of(Materials.Carbon, 1));
        add(Materials.Staballoy,
            of(Materials.Uranium, 9), of(Materials.Titanium, 1));
        add(Materials.Stellite,
            of(Materials.Cobalt, 7), of(Materials.Chrome, 7),
            of(Materials.Manganese, 4), of(Materials.Titanium, 2));
        add(Materials.Talonite,
            of(Materials.Cobalt, 4), of(Materials.Chrome, 3),
            of(Materials.Phosphorus, 2), of(Materials.Molybdenum, 1));
        add(Materials.Tantalloy60,
            of(Materials.Tungsten, 2), of(Materials.Tantalum, 23));
        add(Materials.Tantalloy61,
            of(Materials.Tantalloy60, 1), of(Materials.Titanium, 6), of(Materials.Yttrium, 4));
        add(Materials.TantalumCarbide,
            of(Materials.Tantalum, 1), of(Materials.Carbon, 1));
        add(Materials.Titansteel,
            of(Materials.TungstenTitaniumCarbide, 3), of(Materials.InfusedFire, 1),
            of(Materials.InfusedEarth, 1), of(Materials.InfusedEntropy, 1));
        add(Materials.TriniumNaquadahAlloy,
            of(Materials.Trinium, 5), of(Materials.Naquadah, 9));
        add(Materials.TriniumNaquadahCarbonite,
            of(Materials.TriniumNaquadahAlloy, 9), of(Materials.Carbon, 1));
        add(Materials.TriniumTitaniumAlloy,
            of(Materials.Trinium, 3), of(Materials.Titanium, 7));
        add(Materials.Tumbaga,
            of(Materials.Gold, 7), of(Materials.Copper, 3));
        add(Materials.TungstenTitaniumCarbide,
            of(Materials.TungstenCarbide, 7), of(Materials.Titanium, 3));
        add(Materials.WatertightSteel,
            of(Materials.Steel, 12), of(Materials.Carbon, 2),
            of(Materials.Manganese, 1), of(Materials.Silicon, 2),
            of(Materials.Phosphorus, 1), of(Materials.Sulfur, 1),
            of(Materials.Aluminium, 1));
        add(Materials.WoodsGlass,
            of(Materials.Silicon, 4), of(Materials.Barium, 3),
            of(Materials.Sodium, 2), of(Materials.Nickel, 1));
        add(Materials.Zeron100,
            of(Materials.Chrome, 13), of(Materials.Nickel, 3),
            of(Materials.Molybdenum, 2), of(Materials.Copper, 10),
            of(Materials.Tungsten, 2), of(Materials.Steel, 20));
        add(Materials.ZirconiumCarbide,
            of(Materials.Zirconium, 1), of(Materials.Carbon, 1));
    }
    // spotless:on
}
