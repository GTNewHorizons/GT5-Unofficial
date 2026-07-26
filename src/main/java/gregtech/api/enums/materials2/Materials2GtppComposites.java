package gregtech.api.enums.materials2;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.bsideup.jabel.Desugar;
import com.ruling_0.materiallib.api.Material;

/// gtPlusPlus's curated mixer-recipe composite table (component material + ratio parts), pinning the exact
/// entries the retired `gtPlusPlus.xmod.gregtech.loaders.RecipeGenDustGeneration` mixer generator emitted a
/// recipe for: a material with 1-4 composites (`gtPlusPlus.core.material.Material#getComposites`), reduced to
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
public final class Materials2GtppComposites {

    @Desugar
    public record Component(Material material, int parts) {}

    private static final Map<Material, List<Component>> TABLE = new LinkedHashMap<>();

    private Materials2GtppComposites() {}

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
        add(Materials2Materials.AbyssalAlloy,
            of(Materials2Materials.StainlessSteel, 5), of(Materials2Materials.TungstenCarbide, 5),
            of(Materials2Materials.Nichrome, 5), of(Materials2Materials.Bronze, 5),
            of(Materials2Materials.IncoloyMA956, 5), of(Materials2Materials.Iodine, 1),
            of(Materials2Materials.Radon, 1), of(Materials2Materials.Germanium, 1));
        add(Materials2Materials.AncientGranite,
            of(Materials2Materials.Oxygen, 8), of(Materials2Materials.Iron, 5),
            of(Materials2Materials.SiliconDioxide, 5), of(Materials2Materials.Alumina, 3),
            of(Materials2Materials.Potassium, 3), of(Materials2Materials.Calcium, 2),
            of(Materials2Materials.Sodium, 2), of(Materials2Materials.Ytterbium, 1));
        add(Materials2Materials.Arcanite,
            of(Materials2Materials.Thorium232, 4), of(Materials2Materials.EnergyCrystal, 4),
            of(Materials2Materials.InfusedOrder, 1), of(Materials2Materials.InfusedEntropy, 1));
        add(Materials2Materials.ArceusAlloy2B,
            of(Materials2Materials.Trinium, 3), of(Materials2Materials.MaragingSteel350, 4),
            of(Materials2Materials.TungstenSteel, 2), of(Materials2Materials.Osmiridium, 1),
            of(Materials2Materials.Strontium, 1));
        add(Materials2Materials.BabbitAlloy,
            of(Materials2Materials.Tin, 5), of(Materials2Materials.Lead, 36),
            of(Materials2Materials.Antimony, 8), of(Materials2Materials.Arsenic, 1));
        add(Materials2Materials.BlackMetal,
            of(Materials2Materials.Lead, 3), of(Materials2Materials.Manganese, 5), of(Materials2Materials.Carbon, 12));
        add(Materials2Materials.BlackTitanium,
            of(Materials2Materials.Titanium, 55), of(Materials2Materials.Lanthanum, 12),
            of(Materials2Materials.Tungsten, 8), of(Materials2Materials.Cobalt, 6),
            of(Materials2Materials.Manganese, 4), of(Materials2Materials.Phosphorus, 4),
            of(Materials2Materials.Palladium, 4), of(Materials2Materials.Niobium, 2),
            of(Materials2Materials.Argon, 5));
        add(Materials2Materials.BloodSteel,
            of(Materials2Materials.Steel, 1), of(Materials2Materials.InfusedFire, 1));
        add(Materials2Materials.Botmium,
            of(Materials2Materials.Nitinol60, 1), of(Materials2Materials.Osmium, 6),
            of(Materials2Materials.Ruthenium, 6), of(Materials2Materials.Thallium, 3));
        add(Materials2Materials.CinobiteA243,
            of(Materials2Materials.Zeron100, 16), of(Materials2Materials.Naquadria, 7),
            of(Materials2Materials.Gadolinium, 5), of(Materials2Materials.Aluminium, 3),
            of(Materials2Materials.Mercury, 2), of(Materials2Materials.Tin, 2),
            of(Materials2Materials.Titanium, 12), of(Materials2Materials.Osmiridium, 6));
        add(Materials2Materials.EglinSteel,
            of(Materials2Materials.EglinSteelBaseCompound, 10), of(Materials2Materials.Sulfur, 1),
            of(Materials2Materials.Silicon, 4), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.EglinSteelBaseCompound,
            of(Materials2Materials.Iron, 4), of(Materials2Materials.Kanthal, 1), of(Materials2Materials.Invar, 5));
        add(Materials2Materials.EnergyCrystal,
            of(Materials2Materials.InfusedAir, 1), of(Materials2Materials.InfusedEarth, 1),
            of(Materials2Materials.InfusedFire, 1), of(Materials2Materials.InfusedWater, 1));
        add(Materials2Materials.Grisium,
            of(Materials2Materials.Titanium, 9), of(Materials2Materials.Carbon, 9),
            of(Materials2Materials.Potassium, 9), of(Materials2Materials.Lithium, 9),
            of(Materials2Materials.Sulfur, 9), of(Materials2Materials.Hydrogen, 5));
        add(Materials2Materials.HastelloyC276,
            of(Materials2Materials.Cobalt, 1), of(Materials2Materials.Molybdenum, 8),
            of(Materials2Materials.Tungsten, 1), of(Materials2Materials.Copper, 1),
            of(Materials2Materials.Chrome, 7), of(Materials2Materials.Nickel, 32));
        add(Materials2Materials.HastelloyN,
            of(Materials2Materials.Yttrium, 2), of(Materials2Materials.Molybdenum, 4),
            of(Materials2Materials.Chrome, 2), of(Materials2Materials.Titanium, 2),
            of(Materials2Materials.Nickel, 15));
        add(Materials2Materials.HastelloyW,
            of(Materials2Materials.Iron, 3), of(Materials2Materials.Cobalt, 1),
            of(Materials2Materials.Molybdenum, 12), of(Materials2Materials.Chrome, 3),
            of(Materials2Materials.Nickel, 31));
        add(Materials2Materials.HastelloyX,
            of(Materials2Materials.Iron, 9), of(Materials2Materials.Manganese, 1),
            of(Materials2Materials.Silicon, 1), of(Materials2Materials.Molybdenum, 4),
            of(Materials2Materials.Chrome, 11), of(Materials2Materials.Nickel, 24));
        add(Materials2Materials.HeLiCoPtEr,
            of(Materials2Materials.Helium, 1), of(Materials2Materials.Lithium, 1),
            of(Materials2Materials.Cobalt, 1), of(Materials2Materials.Platinum, 1),
            of(Materials2Materials.Erbium, 1));
        add(Materials2Materials.HS188A,
            of(Materials2Materials.Cobalt, 10), of(Materials2Materials.Hafnium, 10),
            of(Materials2Materials.Talonite, 8), of(Materials2Materials.Rhenium, 5),
            of(Materials2Materials.NiobiumCarbide, 5), of(Materials2Materials.HastelloyX, 4),
            of(Materials2Materials.TungstenSteel, 4), of(Materials2Materials.ZirconiumCarbide, 4));
        add(Materials2Materials.Incoloy020,
            of(Materials2Materials.Iron, 10), of(Materials2Materials.Copper, 1),
            of(Materials2Materials.Chrome, 5), of(Materials2Materials.Nickel, 9));
        add(Materials2Materials.IncoloyDS,
            of(Materials2Materials.Iron, 23), of(Materials2Materials.Cobalt, 9),
            of(Materials2Materials.Chrome, 9), of(Materials2Materials.Nickel, 9));
        add(Materials2Materials.IncoloyMA956,
            of(Materials2Materials.Iron, 16), of(Materials2Materials.Aluminium, 3),
            of(Materials2Materials.Chrome, 5), of(Materials2Materials.Yttrium, 1));
        add(Materials2Materials.Inconel625,
            of(Materials2Materials.Nickel, 3), of(Materials2Materials.Chrome, 7),
            of(Materials2Materials.Molybdenum, 10), of(Materials2Materials.Invar, 10),
            of(Materials2Materials.Nichrome, 13));
        add(Materials2Materials.Inconel690,
            of(Materials2Materials.Chrome, 1), of(Materials2Materials.Niobium, 2),
            of(Materials2Materials.Molybdenum, 2), of(Materials2Materials.Nichrome, 3));
        add(Materials2Materials.Inconel792,
            of(Materials2Materials.Nickel, 2), of(Materials2Materials.Niobium, 1),
            of(Materials2Materials.Aluminium, 2), of(Materials2Materials.Nichrome, 1));
        add(Materials2Materials.LafiumCompound,
            of(Materials2Materials.HastelloyN, 4), of(Materials2Materials.Naquadah, 2),
            of(Materials2Materials.Samarium, 1), of(Materials2Materials.Tungsten, 2),
            of(Materials2Materials.Argon, 1), of(Materials2Materials.Aluminium, 3),
            of(Materials2Materials.Nickel, 4), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.Laurenium,
            of(Materials2Materials.EglinSteel, 8), of(Materials2Materials.Indium, 2),
            of(Materials2Materials.Chrome, 4), of(Materials2Materials.Dysprosium, 1),
            of(Materials2Materials.Rhenium, 1));
        add(Materials2Materials.MaragingSteel250,
            of(Materials2Materials.Steel, 16), of(Materials2Materials.Molybdenum, 1),
            of(Materials2Materials.Titanium, 1), of(Materials2Materials.Nickel, 4),
            of(Materials2Materials.Cobalt, 2));
        add(Materials2Materials.MaragingSteel300,
            of(Materials2Materials.Steel, 16), of(Materials2Materials.Titanium, 1),
            of(Materials2Materials.Aluminium, 1), of(Materials2Materials.Nickel, 4),
            of(Materials2Materials.Cobalt, 2));
        add(Materials2Materials.MaragingSteel350,
            of(Materials2Materials.Steel, 16), of(Materials2Materials.Aluminium, 1),
            of(Materials2Materials.Molybdenum, 1), of(Materials2Materials.Nickel, 4),
            of(Materials2Materials.Cobalt, 2));
        add(Materials2Materials.NiobiumCarbide,
            of(Materials2Materials.Niobium, 1), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.Nitinol60,
            of(Materials2Materials.Nickel, 2), of(Materials2Materials.Titanium, 3));
        add(Materials2Materials.Octiron,
            of(Materials2Materials.Arcanite, 6), of(Materials2Materials.Titansteel, 6),
            of(Materials2Materials.EnergyCrystal, 1), of(Materials2Materials.BlackSteel, 2),
            of(Materials2Materials.Thaumium, 5));
        add(Materials2Materials.Pikyonium64B,
            of(Materials2Materials.Inconel792, 8), of(Materials2Materials.EglinSteel, 5),
            of(Materials2Materials.NaquadahEnriched, 4), of(Materials2Materials.Cerium, 3),
            of(Materials2Materials.Antimony, 2), of(Materials2Materials.Platinum, 2),
            of(Materials2Materials.Ytterbium, 1), of(Materials2Materials.TungstenSteel, 4));
        add(Materials2Materials.Potin,
            of(Materials2Materials.Lead, 2), of(Materials2Materials.Bronze, 2), of(Materials2Materials.Tin, 1));
        add(Materials2Materials.Quantum,
            of(Materials2Materials.Stellite, 3), of(Materials2Materials.EnergyCrystal, 1),
            of(Materials2Materials.SiliconCarbide, 1), of(Materials2Materials.Gallium, 1),
            of(Materials2Materials.Americium, 1), of(Materials2Materials.Palladium, 1),
            of(Materials2Materials.Bismuth, 1), of(Materials2Materials.Germanium, 1));
        add(Materials2Materials.SiliconCarbide,
            of(Materials2Materials.Silicon, 1), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.Staballoy,
            of(Materials2Materials.Uranium, 9), of(Materials2Materials.Titanium, 1));
        add(Materials2Materials.Stellite,
            of(Materials2Materials.Cobalt, 7), of(Materials2Materials.Chrome, 7),
            of(Materials2Materials.Manganese, 4), of(Materials2Materials.Titanium, 2));
        add(Materials2Materials.Talonite,
            of(Materials2Materials.Cobalt, 4), of(Materials2Materials.Chrome, 3),
            of(Materials2Materials.Phosphorus, 2), of(Materials2Materials.Molybdenum, 1));
        add(Materials2Materials.Tantalloy60,
            of(Materials2Materials.Tungsten, 2), of(Materials2Materials.Tantalum, 23));
        add(Materials2Materials.Tantalloy61,
            of(Materials2Materials.Tantalloy60, 1), of(Materials2Materials.Titanium, 6), of(Materials2Materials.Yttrium, 4));
        add(Materials2Materials.TantalumCarbide,
            of(Materials2Materials.Tantalum, 1), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.Titansteel,
            of(Materials2Materials.TungstenTitaniumCarbide, 3), of(Materials2Materials.InfusedFire, 1),
            of(Materials2Materials.InfusedEarth, 1), of(Materials2Materials.InfusedEntropy, 1));
        add(Materials2Materials.TriniumNaquadahAlloy,
            of(Materials2Materials.Trinium, 5), of(Materials2Materials.Naquadah, 9));
        add(Materials2Materials.TriniumNaquadahCarbonite,
            of(Materials2Materials.TriniumNaquadahAlloy, 9), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.TriniumTitaniumAlloy,
            of(Materials2Materials.Trinium, 3), of(Materials2Materials.Titanium, 7));
        add(Materials2Materials.Tumbaga,
            of(Materials2Materials.Gold, 7), of(Materials2Materials.Copper, 3));
        add(Materials2Materials.TungstenTitaniumCarbide,
            of(Materials2Materials.TungstenCarbide, 7), of(Materials2Materials.Titanium, 3));
        add(Materials2Materials.WatertightSteel,
            of(Materials2Materials.Steel, 12), of(Materials2Materials.Carbon, 2),
            of(Materials2Materials.Manganese, 1), of(Materials2Materials.Silicon, 2),
            of(Materials2Materials.Phosphorus, 1), of(Materials2Materials.Sulfur, 1),
            of(Materials2Materials.Aluminium, 1));
        add(Materials2Materials.WoodsGlass,
            of(Materials2Materials.Silicon, 4), of(Materials2Materials.Barium, 3),
            of(Materials2Materials.Sodium, 2), of(Materials2Materials.Nickel, 1));
        add(Materials2Materials.Zeron100,
            of(Materials2Materials.Chrome, 13), of(Materials2Materials.Nickel, 3),
            of(Materials2Materials.Molybdenum, 2), of(Materials2Materials.Copper, 10),
            of(Materials2Materials.Tungsten, 2), of(Materials2Materials.Steel, 20));
        add(Materials2Materials.ZirconiumCarbide,
            of(Materials2Materials.Zirconium, 1), of(Materials2Materials.Carbon, 1));
    }
    // spotless:on
}
