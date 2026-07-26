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
        add(Materials2Materials.Arcanite,
            of(Materials2Materials.Thorium232, 4), of(Materials2Materials.EnergyCrystal, 4),
            of(Materials2Materials.InfusedOrder, 1), of(Materials2Materials.InfusedEntropy, 1));
        add(Materials2Materials.BabbitAlloy,
            of(Materials2Materials.Tin, 5), of(Materials2Materials.Lead, 36),
            of(Materials2Materials.Antimony, 8), of(Materials2Materials.Arsenic, 1));
        add(Materials2Materials.BlackMetal,
            of(Materials2Materials.Lead, 3), of(Materials2Materials.Manganese, 5), of(Materials2Materials.Carbon, 12));
        add(Materials2Materials.BloodSteel,
            of(Materials2Materials.Steel, 1), of(Materials2Materials.InfusedFire, 1));
        add(Materials2Materials.Botmium,
            of(Materials2Materials.Nitinol60, 1), of(Materials2Materials.Osmium, 6),
            of(Materials2Materials.Ruthenium, 6), of(Materials2Materials.Thallium, 3));
        add(Materials2Materials.EglinSteel,
            of(Materials2Materials.EglinSteelBaseCompound, 10), of(Materials2Materials.Sulfur, 1),
            of(Materials2Materials.Silicon, 4), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.EglinSteelBaseCompound,
            of(Materials2Materials.Iron, 4), of(Materials2Materials.Kanthal, 1), of(Materials2Materials.Invar, 5));
        add(Materials2Materials.EnergyCrystal,
            of(Materials2Materials.InfusedAir, 1), of(Materials2Materials.InfusedEarth, 1),
            of(Materials2Materials.InfusedFire, 1), of(Materials2Materials.InfusedWater, 1));
        add(Materials2Materials.Incoloy020,
            of(Materials2Materials.Iron, 10), of(Materials2Materials.Copper, 1),
            of(Materials2Materials.Chrome, 5), of(Materials2Materials.Nickel, 9));
        add(Materials2Materials.IncoloyDS,
            of(Materials2Materials.Iron, 23), of(Materials2Materials.Cobalt, 9),
            of(Materials2Materials.Chrome, 9), of(Materials2Materials.Nickel, 9));
        add(Materials2Materials.IncoloyMA956,
            of(Materials2Materials.Iron, 16), of(Materials2Materials.Aluminium, 3),
            of(Materials2Materials.Chrome, 5), of(Materials2Materials.Yttrium, 1));
        add(Materials2Materials.Inconel690,
            of(Materials2Materials.Chrome, 1), of(Materials2Materials.Niobium, 2),
            of(Materials2Materials.Molybdenum, 2), of(Materials2Materials.Nichrome, 3));
        add(Materials2Materials.Inconel792,
            of(Materials2Materials.Nickel, 2), of(Materials2Materials.Niobium, 1),
            of(Materials2Materials.Aluminium, 2), of(Materials2Materials.Nichrome, 1));
        add(Materials2Materials.NiobiumCarbide,
            of(Materials2Materials.Niobium, 1), of(Materials2Materials.Carbon, 1));
        add(Materials2Materials.Nitinol60,
            of(Materials2Materials.Nickel, 2), of(Materials2Materials.Titanium, 3));
        add(Materials2Materials.Potin,
            of(Materials2Materials.Lead, 2), of(Materials2Materials.Bronze, 2), of(Materials2Materials.Tin, 1));
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
        add(Materials2Materials.WoodsGlass,
            of(Materials2Materials.Silicon, 4), of(Materials2Materials.Barium, 3),
            of(Materials2Materials.Sodium, 2), of(Materials2Materials.Nickel, 1));
        add(Materials2Materials.ZirconiumCarbide,
            of(Materials2Materials.Zirconium, 1), of(Materials2Materials.Carbon, 1));
    }
    // spotless:on
}
