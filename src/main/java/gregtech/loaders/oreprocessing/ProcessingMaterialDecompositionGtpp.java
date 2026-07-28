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

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2GtppComposites;
import gregtech.api.enums.materials2.Materials2GtppComposites.Component;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;

/// Reproduces the retired gtPlusPlus `RecipeGenMaterialProcessing` for every material in [#ELIGIBLE] that
/// carries a [Materials2GtppComposites] entry: a centrifuge (2-6 parts) or chemical-dehydrator (7-9 parts)
/// recipe decomposing the material's dust back into its composite parts, each part a cell when
/// [ProcessingOreMachine#gtppState] resolves it to anything but `SOLID`, else a dust.
///
/// This reuses the same [Materials2GtppComposites] ratio table [ProcessingMixerGtpp] and
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
        Materials2Materials.Selenium, Materials2Materials.Iodine, Materials2Materials.Rhenium,
        Materials2Materials.Thallium, Materials2Materials.Germanium, Materials2Materials.Technetium,
        Materials2Materials.Lithium7, Materials2Materials.Uranium232, Materials2Materials.Uranium233,
        Materials2Materials.AdvancedNitinol, Materials2Materials.AstralTitanium, Materials2Materials.CelestialTungsten,
        Materials2Materials.Hypogen, Materials2Materials.ChromaticGlass, Materials2Materials.BlackMetal,
        Materials2Materials.Dragonblood, Materials2Materials.SiliconCarbide, Materials2Materials.ZirconiumCarbide,
        Materials2Materials.TantalumCarbide, Materials2Materials.NiobiumCarbide, Materials2Materials.TungstenTitaniumCarbide,
        Materials2Materials.EnergyCrystal, Materials2Materials.BloodSteel, Materials2Materials.Zeron100,
        Materials2Materials.Tumbaga, Materials2Materials.Potin, Materials2Materials.Staballoy,
        Materials2Materials.Tantalloy60, Materials2Materials.Tantalloy61, Materials2Materials.Inconel625,
        Materials2Materials.Inconel690, Materials2Materials.Inconel792, Materials2Materials.EglinSteel,
        Materials2Materials.MaragingSteel250, Materials2Materials.MaragingSteel300, Materials2Materials.MaragingSteel350,
        Materials2Materials.WatertightSteel, Materials2Materials.Nitinol60, Materials2Materials.Stellite,
        Materials2Materials.Talonite, Materials2Materials.HastelloyW, Materials2Materials.HastelloyX,
        Materials2Materials.HastelloyC276, Materials2Materials.HastelloyN, Materials2Materials.Incoloy020,
        Materials2Materials.IncoloyDS, Materials2Materials.IncoloyMA956, Materials2Materials.Grisium,
        Materials2Materials.HG1223, Materials2Materials.TriniumTitaniumAlloy, Materials2Materials.TriniumNaquadahAlloy,
        Materials2Materials.TriniumNaquadahCarbonite, Materials2Materials.ArceusAlloy2B, Materials2Materials.HeLiCoPtEr,
        Materials2Materials.LafiumCompound, Materials2Materials.CinobiteA243, Materials2Materials.Pikyonium64B,
        Materials2Materials.AbyssalAlloy, Materials2Materials.Laurenium, Materials2Materials.Botmium,
        Materials2Materials.HS188A, Materials2Materials.Titansteel, Materials2Materials.Arcanite,
        Materials2Materials.Octiron, Materials2Materials.BabbitAlloy, Materials2Materials.BlackTitanium,
        Materials2Materials.Indalloy140, Materials2Materials.Rhugnor, Materials2Materials.Quantum,
        Materials2Materials.AncientGranite, Materials2Materials.Runite, Materials2Materials.EglinSteelBaseCompound);
    // spotless:on

    /// `HG1223`'s composites, in the order the retired generator's own `Material#getComposites` carried them
    /// (cell parts before dust parts is coincidental -- the retired generator preserves declaration order and
    /// this is that order, not a separate grouping step) -- not [GTMaterialProperties#COMPOSITION]'s own list
    /// order, which differs (`Mercury, Barium, Calcium, Copper, Oxygen`). `HG1223` has no
    /// [Materials2GtppComposites] entry: `generate(matInfo, false)` dispatches it, so its
    /// `RecipeGenDustGeneration` mixer block (that table's own source) never ran for it, but the retired
    /// `RecipeGenMaterialProcessing` reads composition directly and unconditionally, so it still emitted this
    /// material's decomposition recipe. Sourced here rather than by widening [Materials2GtppComposites], which
    /// [ProcessingMixerGtpp] and [ProcessingAlloyBlastSmelter] also read and whose own eligibility this material
    /// does not belong to.
    private static final Map<Material, List<Component>> COMPOSITION_FALLBACK = Map.of(
        Materials2Materials.HG1223,
        List.of(
            new Component(Materials2Materials.Mercury, 1),
            new Component(Materials2Materials.Oxygen, 8),
            new Component(Materials2Materials.Calcium, 2),
            new Component(Materials2Materials.Copper, 3),
            new Component(Materials2Materials.Barium, 2)));

    public static void run() {
        for (Material material : ELIGIBLE) {
            generate(material);
        }
    }

    private static void generate(Material material) {
        List<Component> composites = Materials2GtppComposites.composites(material);
        if (composites.isEmpty()) composites = COMPOSITION_FALLBACK.getOrDefault(material, List.of());
        if (composites.size() <= 1) return;

        long voltage = MU.voltageMultiplier(material);
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
