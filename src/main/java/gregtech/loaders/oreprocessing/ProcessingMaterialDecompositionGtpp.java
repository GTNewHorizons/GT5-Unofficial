package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.LegacyGTPPComposites;
import gregtech.api.enums.materials2.LegacyGTPPComposites.Component;
import gregtech.api.material.MaterialUtils;

/// Reproduces the retired gtPlusPlus `RecipeGenMaterialProcessing` for every material in [#ELIGIBLE] that
/// carries a [LegacyGTPPComposites] entry: a centrifuge (2-6 parts) or chemical-dehydrator (7-9 parts)
/// recipe decomposing the material's dust back into its composite parts, each part a cell when
/// [ProcessingOreMachine#gtppState] resolves it to anything but `SOLID`, else a dust.
///
/// This reuses the same [LegacyGTPPComposites] ratio table [ProcessingMixerGtpp] and
/// [ProcessingAlloyBlastSmelter] read, but is not restricted to either of their own eligibility -- the retired
/// generator's `material.getMaterialComposites().length > 1` gate is membership in this table alone, checked
/// here at runtime rather than pre-filtered into [#ELIGIBLE] itself, since [#ELIGIBLE] must still cover
/// materials the retired generator constructed unconditionally regardless of composite count.
///
/// [#run] is called from `CompatHandler#startLoadingGregAPIBasedRecipes` -- see
/// [ProcessingDustGeneration]'s class javadoc for why that timing matters.
public class ProcessingMaterialDecompositionGtpp {

    private ProcessingMaterialDecompositionGtpp() {}

    /// Every material the retired `RecipeGenMaterialProcessing` reached: `MaterialGenerator#generate` (any
    /// `generateEverything` value), `#generateOreMaterialWithAllExcessComponents`, or `#generateDusts` (all of
    /// which construct it with `disableOptional=false`, the only value any call site ever passes) --
    /// excluding a `PURE_GAS`/`PURE_LIQUID`-state material reached only through `generate`, since it returns
    /// before constructing this generator for those two states (`Bromine`, `Krypton`). No
    /// `generateNuclearMaterial` call ever passes `disableOptionalRecipes=false`, the only path that would
    /// have added a nuclear material here too.
    // spotless:off
    private static final Set<Material> ELIGIBLE = Set.of(
        Materials.Selenium, Materials.Iodine, Materials.Rhenium,
        Materials.Thallium, Materials.Germanium, Materials.Technetium,
        Materials.Lithium7, Materials.Uranium232, Materials.Uranium233,
        Materials.AdvancedNitinol, Materials.AstralTitanium, Materials.CelestialTungsten,
        Materials.Hypogen, Materials.ChromaticGlass, Materials.BlackMetal,
        Materials.Dragonblood, Materials.SiliconCarbide, Materials.ZirconiumCarbide,
        Materials.TantalumCarbide, Materials.NiobiumCarbide, Materials.TungstenTitaniumCarbide,
        Materials.EnergyCrystal, Materials.BloodSteel, Materials.Zeron100,
        Materials.Tumbaga, Materials.Potin, Materials.Staballoy,
        Materials.Tantalloy60, Materials.Tantalloy61, Materials.Inconel625,
        Materials.Inconel690, Materials.Inconel792, Materials.EglinSteel,
        Materials.MaragingSteel250, Materials.MaragingSteel300, Materials.MaragingSteel350,
        Materials.WatertightSteel, Materials.Nitinol60, Materials.Stellite,
        Materials.Talonite, Materials.HastelloyW, Materials.HastelloyX,
        Materials.HastelloyC276, Materials.HastelloyN, Materials.Incoloy020,
        Materials.IncoloyDS, Materials.IncoloyMA956, Materials.Grisium,
        Materials.HG1223, Materials.TriniumTitaniumAlloy, Materials.TriniumNaquadahAlloy,
        Materials.TriniumNaquadahCarbonite, Materials.ArceusAlloy2B, Materials.HeLiCoPtEr,
        Materials.LafiumCompound, Materials.CinobiteA243, Materials.Pikyonium64B,
        Materials.AbyssalAlloy, Materials.Laurenium, Materials.Botmium,
        Materials.HS188A, Materials.Titansteel, Materials.Arcanite,
        Materials.Octiron, Materials.BabbitAlloy, Materials.BlackTitanium,
        Materials.Indalloy140, Materials.Rhugnor, Materials.Quantum,
        Materials.AncientGranite, Materials.Runite, Materials.EglinSteelBaseCompound);
    // spotless:on

    /// `HG1223`'s composites, in the order the retired generator's own `Material#getComposites` carried them
    /// (cell parts before dust parts is coincidental -- the retired generator preserves declaration order and
    /// this is that order, not a separate grouping step) -- not [GTMaterialProperties#COMPOSITION]'s own list
    /// order, which differs (`Mercury, Barium, Calcium, Copper, Oxygen`). `HG1223` has no
    /// [LegacyGTPPComposites] entry: `generate(matInfo, false)` dispatches it, so its
    /// `RecipeGenDustGeneration` mixer block (that table's own source) never ran for it, but the retired
    /// `RecipeGenMaterialProcessing` reads composition directly and unconditionally, so it still emitted this
    /// material's decomposition recipe. Sourced here rather than by widening [LegacyGTPPComposites], which
    /// [ProcessingMixerGtpp] and [ProcessingAlloyBlastSmelter] also read and whose own eligibility this material
    /// does not belong to.
    private static final Map<Material, List<Component>> COMPOSITION_FALLBACK = Map.of(
        Materials.HG1223,
        List.of(
            new Component(Materials.Mercury, 1),
            new Component(Materials.Oxygen, 8),
            new Component(Materials.Calcium, 2),
            new Component(Materials.Copper, 3),
            new Component(Materials.Barium, 2)));

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        List<Component> composites = LegacyGTPPComposites.composites(material);
        if (composites.isEmpty()) composites = COMPOSITION_FALLBACK.getOrDefault(material, List.of());
        if (composites.size() <= 1) return;

        long voltage = MaterialUtils.voltageMultiplier(material);
        ItemStack[] outputs = new ItemStack[composites.size()];
        int cellCount = 0;
        int totalCount = 0;
        for (int i = 0; i < composites.size(); i++) {
            Component component = composites.get(i);
            if (!"SOLID".equals(ProcessingOreMachine.gtppState(component.material()))) {
                outputs[i] = ProcessingDustGeneration
                    .stackOf(OrePrefixes.cell, component.material(), component.parts());
                cellCount += component.parts();
            } else {
                outputs[i] = ProcessingDustGeneration
                    .stackOf(OrePrefixes.dust, component.material(), component.parts());
            }
            totalCount += component.parts();
        }

        int[] chances = new int[outputs.length];
        for (int i = 0; i < outputs.length; i++) {
            chances[i] = outputs[i] != null ? 10000 : 0;
        }

        List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(outputs));
        internalOutputs.removeIf(Objects::isNull);
        int[] strippedChances = new int[internalOutputs.size()];
        System.arraycopy(chances, 0, strippedChances, 0, internalOutputs.size());

        ItemStack mainDust = ProcessingDustGeneration.stackOf(OrePrefixes.dust, material, totalCount);
        if (mainDust == null) return;

        ItemStack emptyCell = cellCount > 0 ? ItemList.Cell_Empty.get(cellCount) : null;
        ItemStack[] inputs = emptyCell == null ? new ItemStack[] { mainDust } : new ItemStack[] { mainDust, emptyCell };

        // mainDust's amount is the composites' part sum, routinely above 64 (e.g. BlackTitanium's 100) --
        // itemInputsUnsafe is required so GTOreDictUnificator's unification pass does not clamp it to 64.
        if (composites.size() <= 6) {
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(inputs)
                .itemOutputs(internalOutputs.toArray(new ItemStack[0]))
                .outputChances(strippedChances)
                .eut(voltage)
                .duration((int) (voltage / 10) * SECONDS)
                .addTo(centrifugeRecipes);
        } else if (composites.size() <= 9) {
            GTValues.RA.stdBuilder()
                .itemInputsUnsafe(inputs)
                .itemOutputs(internalOutputs.toArray(new ItemStack[0]))
                .outputChances(strippedChances)
                .eut(voltage)
                .duration(20 * (int) (voltage / 10))
                .addTo(chemicalDehydratorRecipes);
        }
    }
}
