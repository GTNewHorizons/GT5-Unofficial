package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.loaders.materials.LegacyMaterials;
import gregtech.loaders.materials.RecognitionMaterials.RecognitionMarker;
import gtPlusPlus.core.material.MaterialReconstruction;

/// Bridges legacy [OrePrefixes]/[Materials] pairs to their cutover MaterialLib [Shape]/[Material]
/// equivalents.
///
/// The prefix-to-shape map reflects [Materials2Shapes]'s, [Materials2CellShapes]'s, [Materials2BlockShapes]'s,
/// [Materials2OreShapes]'s, and [Materials2PipeShapes]'s [Shape] fields (each named identically to the
/// [OrePrefixes] it cuts over to) instead of hand-listing the cutover prefixes, so it always matches whatever
/// those declare. A prefix
/// normally maps to exactly one shape; the exceptions map to an ordered candidate list that [#stack] resolves
/// per material: `cellPlasma` (see [Materials2CellShapes]), and the five `pipeTiny`..`pipeHuge` prefix
/// strings, which the fluid and item pipe families share for disjoint material sets (see
/// [Materials2PipeShapes] -- the item shapes' field names deliberately differ from the prefix names, so they
/// and the `pipeRestrictive*` item shapes are folded under their prefix keys explicitly). The material lookup
/// is keyed by legacy
/// name (`Materials#mName`), preferring [GTMaterialProperties#LEGACY_NAME] over [Material#getName] because
/// MaterialLib sanitizes registration names that contain characters `Names#validate` rejects.
public class MU {

    private static Map<String, List<Shape>> prefixToShapes;
    private static Map<String, Material> legacyNameToMaterial;

    private MU() {}

    /// The MaterialLib shape a legacy item [OrePrefixes] cuts over to, or null if that prefix is not part of
    /// the cutover (e.g. a not-yet-cut-over container prefix). For a multi-candidate prefix (`cellPlasma`,
    /// `pipeTiny`..`pipeHuge`), the shape a specific material actually generates may differ -- see [#stack];
    /// callers that must see every candidate use [#shapes].
    public static @Nullable Shape shape(OrePrefixes prefix) {
        if (prefix == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? null : shapes.get(0);
    }

    /// Every candidate shape a legacy [OrePrefixes] cuts over to, in resolution order ([#stack] uses the
    /// first one a material generates); empty when the prefix is not part of the cutover.
    public static List<Shape> shapes(OrePrefixes prefix) {
        if (prefix == null) return Collections.emptyList();
        List<Shape> shapes = prefixShapes().get(prefix.name());
        return shapes == null ? Collections.emptyList() : shapes;
    }

    /// The MaterialLib material a legacy [Materials] cuts over to, or null if it has none (materials without
    /// a MaterialLib counterpart never had generated items in the legacy system either). Falls back to the
    /// MaterialLib registry for materials that are not [Materials2Materials] fields, such as the shapeless
    /// wildcard backings registered by [gregtech.loaders.materials.LegacyMarkerMaterials].
    public static @Nullable Material material(Materials material) {
        if (material == null) return null;
        Material found = legacyNamedMaterials().get(material.mName);
        if (found != null) return found;
        return MaterialLibAPI.getMaterial("gregtech", material.mName);
    }

    /// The MaterialLib [Material] backing a transitional legacy-family material object -- a [Materials], a
    /// `Werkstoff`, a gtPlusPlus `Material`, or a [RecognitionMarker] -- for migrating the plumbing off the
    /// legacy families onto [Material]. A legacy [Materials]
    /// (including the `Any*` wildcard facades) resolves through [#material]; anything else by its internal
    /// name, which for a marker finds its registered shapeless backing or the real material its name unifies
    /// into (e.g. `Ammonium`). A [Material] passes through unchanged. Null when nothing backs it.
    /// TRANSITIONAL -- removed once every call site passes a [Material] directly.
    public static @Nullable Material toMaterial(@Nullable Object material) {
        if (material == null) return null;
        if (material instanceof Material ml) return ml;
        if (material instanceof Materials legacy) {
            Material ml = material(legacy);
            if (ml != null) return ml;
        }
        String name = internalNameOf(material);
        return name == null ? null : MaterialLibAPI.getMaterial("gregtech", name);
    }

    /// The legacy-family material object (a [Materials], a [Werkstoff], or a gtPlusPlus [gtPlusPlus.core.
    /// material.Material]) that owns worldgen placement for a MaterialLib material, or null when none does --
    /// the inverse of [#toMaterial] for the worldgen spine, which stores [Material] but places and reads ore
    /// blocks through the family-dispatched ore adapters. A live, id-backed [Materials] counterpart wins
    /// first: a merged werkstoff+gregtech declaration (Salt, RockSalt, Spodumene carry
    /// [GTMaterialProperties#WERKSTOFF_IDS] alongside a live id) was always declared into veins via its
    /// `Materials` constant, so placement must keep the gregtech adapter's stone-validity gates for it. A
    /// remaining [GTMaterialProperties#WERKSTOFF_IDS] carrier belongs to the werkstoff that reconstructed it;
    /// a reconstruction-owned [GTMaterialProperties#GTPP_STATE] carrier resolves to its gtPlusPlus material
    /// (`GTPPOreAdapter`'s own exclusion rule); an id-less bridge [Materials] is the last resort.
    /// TRANSITIONAL -- dies with the legacy families.
    public static @Nullable Object legacyMaterialOf(@Nullable Material material) {
        if (material == null) return null;
        Materials gt = materialOf(material);
        if (gt != null && gt.mMetaItemSubID >= 0) return gt;
        List<Integer> werkstoffIds = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
        if (werkstoffIds != null) return WerkstoffReconstruction.byId(werkstoffIds.get(0));
        if (material.getProperty(GTMaterialProperties.GTPP_STATE) != null
            && MaterialReconstruction.isReconstructed(material.getName())) {
            return MaterialReconstruction.byName(material.getName());
        }
        return gt;
    }

    /// The MaterialLib material a legacy `Materials.get(name)` lookup cuts over to, or null on a miss --
    /// replacing the `_NULL` sentinel with null. Resolves through [#material]'s exact chain, keyed by the
    /// name directly: the [Materials2Materials]-field map (keyed by legacy internal name), then the
    /// MaterialLib registry for the shapeless marker backings without a declared field
    /// ([gregtech.loaders.materials.LegacyMarkerMaterials], [gregtech.loaders.materials.
    /// RecognitionMaterials]). `Materials.get` keys its map by `Materials#mName` -- the same string
    /// [#internalName] yields -- so any name it resolves to a facade resolves here to that facade's
    /// [#material]. Marker names `Materials.get` cannot resolve (the superconductors kept out of
    /// `getMaterialsMap`, the `RecognitionMarker`-typed fields) resolve here to their registered backing.
    public static @Nullable Material byLegacyName(@Nullable String name) {
        if (name == null) return null;
        Material found = legacyNamedMaterials().get(name);
        if (found != null) return found;
        return MaterialLibAPI.getMaterial("gregtech", name);
    }

    /// The cutover MaterialLib stack for a legacy (prefix, material) pair, or null when either side has no
    /// cutover mapping. When a prefix maps to more than one candidate shape (`cellPlasma`), the first one
    /// `material` actually generates is used.
    public static @Nullable ItemStack stack(OrePrefixes prefix, Materials material, long amount) {
        return stack(prefix, material(material), amount);
    }

    /// [#stack] for callers that already hold the MaterialLib [Material] directly instead of a legacy
    /// [Materials] enum constant -- e.g. gtPlusPlus material reconstruction, whose ~200 non-merged materials
    /// have no [Materials] counterpart to look up by.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        if (prefix == null || material == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return null;
        for (Shape shape : shapes) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    /// Whether a legacy (prefix, material) pair has a MaterialLib equivalent (see [#stack]). Unlike [#shape],
    /// which answers whether a prefix has cut over at all, this answers per material -- needed because a
    /// fluid-in-container shape's membership does not always mirror every material with a real legacy slot: a
    /// material can hold a legacy `cell` item generated purely from its `CELL` capability flag while never
    /// having a fluid to put in it (MaterialLib's container contract requires a material to also generate one
    /// of the container's fluid shapes, so such a material is left off `cell`'s membership and keeps its
    /// legacy item instead). Legacy construction code should skip a (prefix, material) pair exactly when this
    /// is true, not merely when [#shape] is non-null.
    public static boolean isCutOver(OrePrefixes prefix, Materials material) {
        return stack(prefix, material, 1) != null;
    }

    /// [#isCutOver] for a MaterialLib [Material] held directly -- see [#stack]'s raw-[Material] overload.
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return stack(prefix, material, 1) != null;
    }

    /// The dust [ItemStack] a [GTMaterialProperties#COMPOSITION] entry contributes to a recipe, sized by the
    /// entry's amount, or null when the referenced material carries no `dust` shape (a gas/fluid-only
    /// component -- see [#compositionGas]) or fails to resolve. A composition entry always names a MaterialLib
    /// material directly ([MaterialRef#resolve]), so unlike [#stack] this needs no legacy-[Materials]/
    /// bartworks fallback.
    public static @Nullable ItemStack compositionDust(MaterialRefStack entry) {
        Material material = entry.material()
            .resolve();
        return material == null ? null : stack(OrePrefixes.dust, material, entry.amount());
    }

    /// The gas [FluidStack] a [GTMaterialProperties#COMPOSITION] entry contributes when its material has no
    /// `dust` shape, at 1000 mB per unit of the entry's amount -- the only non-dust composition backing
    /// [gregtech.loaders.materialrecipes.LoaderMixerRecipes] and [gregtech.loaders.materialrecipes.
    /// LoaderChemicalRecipes]'s carriers reference. Null when the material resolves but carries neither a
    /// `dust` nor a `fluidGas` shape.
    public static @Nullable FluidStack compositionGas(MaterialRefStack entry) {
        Material material = entry.material()
            .resolve();
        if (material == null || !material.hasShape(Materials2FluidShapes.fluidGas)) return null;
        return MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidGas, (int) (1000 * entry.amount()));
    }

    /// Whether a material carries a legacy `Materials#mStandardMoltenFluid` (see [#molten]) -- for callers that
    /// need the presence check independent of a specific fluid amount, such as one gate guarding several
    /// [#molten] calls of different amounts. HYBRID: while a material still has a live legacy [Materials]
    /// counterpart ([#materialOf] non-null), delegates to that facade's field. A material without one answers
    /// per reconstructed population, reproducing the field the retired bridge facade carried:
    ///
    /// - Werkstoff-reconstructed ([GTMaterialProperties#WERKSTOFF_PREFIXES] present): whether the prefix list
    /// contains `cellMolten` -- the `Werkstoff#hasItemType(cellMolten)` ground truth the bartworks bridge
    /// set its field under. NOT `cellMolten` shape membership: the dual-nature elements (e.g. `Zirconium`/
    /// `Hafnium`/`Thorium232`) carry the shape from their gtpp fluid capture while the werkstoff side never
    /// generated the item.
    /// - gtpp-reconstructed ([GTMaterialProperties#GTPP_STATE] present): whether
    /// `MaterialReconstruction#build` [#recordLegacyMolten]-ed the material -- the gtpp bridge facade held a
    /// molten fluid only when the material's own fluid had already resolved at bridge-construction time
    /// (materials constructed before gtpp's `Material#registerAllPending` gate opened deferred their fluid,
    /// so their facades stayed molten-less even with a `cellMolten` cutover). A dual werkstoff+gtpp material
    /// answers true when either side's writer fired, matching the facade both loaders mutated in turn.
    /// - Otherwise: [#isCutOver] of [OrePrefixes#cellMolten]. Merely having a
    /// [Materials2FluidShapes#fluidMolten] shape, without the container shape, would be wider than any
    /// population's gate -- [#isCutOver] checks the container, not the bare fluid.
    public static boolean hasMolten(@Nullable Material material) {
        Materials legacy = materialOf(material);
        if (legacy != null) return legacy.mStandardMoltenFluid != null;
        if (material == null) return false;
        List<String> werkstoffPrefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        boolean gtpp = material.getProperty(GTMaterialProperties.GTPP_STATE) != null;
        if (werkstoffPrefixes != null || gtpp) {
            return (werkstoffPrefixes != null && werkstoffPrefixes.contains(OrePrefixes.cellMolten.name()))
                || reconstructedLegacyMolten.contains(material);
        }
        return isCutOver(OrePrefixes.cellMolten, material);
    }

    private static final java.util.Set<Material> reconstructedLegacyMolten = new java.util.HashSet<>();

    /// Records that `material`'s retired gtpp bridge facade would have carried a `mStandardMoltenFluid` (see
    /// [#hasMolten]). Called by `MaterialReconstruction#build` at the exact point the facade assignment used
    /// to run, with the same gate.
    public static void recordLegacyMolten(Material material) {
        reconstructedLegacyMolten.add(material);
    }

    /// The legacy `Materials#mStandardMoltenFluid`-backed `Materials#getMolten` stack for a material, or null
    /// when [#hasMolten] is false -- mirrors `getMolten`'s own null-on-absent behavior. The
    /// [Materials2FluidShapes#fluidMolten] Forge fluid this resolves is the same one every population's
    /// `mStandardMoltenFluid` was set from (`LegacyMaterials#build`'s `wireFluids` resolves it by
    /// [GTMaterialProperties#LEGACY_FLUIDS]'s `molten` slot name; both bridge loaders set their field from a
    /// `Material#getFluid` gated on this same shape), so once [#hasMolten] is true this is byte-identical to
    /// `getMolten`'s own stack.
    public static @Nullable FluidStack molten(@Nullable Material material, long amount) {
        if (!hasMolten(material)) return null;
        return MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) amount);
    }

    /// The legacy `Materials#mFluid`-backed `Materials#getFluid` stack for a material, or null when it carries
    /// no fluid slot. Resolved from [GTMaterialProperties#LEGACY_FLUIDS]'s `fluid()` slot by Forge fluid name
    /// -- the exact resolution `LegacyMaterials#wireFluids` assigns `mFluid` from -- NOT from a
    /// [Materials2FluidShapes#fluidLiquid] shape lookup: a material whose legacy fluid is a vanilla or foreign
    /// Forge fluid (`Water` -> `water`, `Milk` -> `milk`) carries the slot without any ML fluid shape, so a
    /// shape-based lookup would miss it.
    public static @Nullable FluidStack fluid(@Nullable Material material, long amount) {
        Fluid fluid = fluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// [#fluid]'s raw [Fluid] -- the legacy `Materials#mFluid` field value itself, for presence gates and
    /// callers building their own stacks.
    public static @Nullable Fluid fluidOf(@Nullable Material material) {
        return slotFluid(material, FluidNames::fluid);
    }

    /// [#fluid], for `Materials#mGas`/`Materials#getGas` -- the `gas()` slot. Wherever the former
    /// [Materials2FluidShapes#fluidGas] shape lookup succeeded this resolves the identical fluid: a material
    /// only generates that shape when it carries the slot ([Materials2FluidShapes]'s `requireRef` fails fluid
    /// registration otherwise), and the shape registers its Forge fluid under the slot's own name. The slot
    /// additionally covers materials whose legacy gas fluid never became an ML shape.
    public static @Nullable FluidStack gas(@Nullable Material material, long amount) {
        Fluid fluid = slotFluid(material, FluidNames::gas);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// [#fluid], for `Materials#mPlasma`/`Materials#getPlasma` -- the `plasma()` slot.
    public static @Nullable FluidStack plasma(@Nullable Material material, long amount) {
        Fluid fluid = slotFluid(material, FluidNames::plasma);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// [#fluid], for `Materials#mSolid`/`Materials#getSolid` -- the `solid()` slot.
    public static @Nullable FluidStack solid(@Nullable Material material, long amount) {
        Fluid fluid = slotFluid(material, FluidNames::solid);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    private static @Nullable Fluid slotFluid(@Nullable Material material, Function<FluidNames, FluidRef> slot) {
        if (material == null) return null;
        FluidNames legacyFluids = material.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
        if (legacyFluids == null) return null;
        FluidRef ref = slot.apply(legacyFluids);
        return ref == null ? null : FluidRegistry.getFluid(ref.name());
    }

    private static final Map<Fluid, Material> fluidMaterials = new LinkedHashMap<>();

    /// Records that `fluid` was registered for `material` -- the [Material]-side twin of
    /// `Materials#FLUID_MAP`, written at the same two points (`LegacyMaterials`'s fluid wiring and
    /// `GTFluid#configureMaterials`). A `FLUID_MAP` entry whose legacy material has no MaterialLib
    /// counterpart is not mirrored here, so readers null-check exactly as they do against `FLUID_MAP`.
    public static void recordFluidMaterial(Fluid fluid, Material material) {
        fluidMaterials.put(fluid, material);
    }

    /// The material [#recordFluidMaterial] recorded for a fluid -- the `Materials#getGtMaterialFromFluid`/
    /// `FLUID_MAP` lookup on the [Material] side. Null when no recorded material owns the fluid.
    public static @Nullable Material materialOfFluid(@Nullable Fluid fluid) {
        return fluid == null ? null : fluidMaterials.get(fluid);
    }

    /// The legacy [Materials] a MaterialLib material was ported from, or null if it has none.
    public static @Nullable Materials materialOf(Material material) {
        if (material == null) return null;
        return Materials.getMaterialsMap()
            .get(internalName(material));
    }

    /// The crafting-table ingredient a legacy `OrePrefixes.get(Materials)` call produced, built directly from the
    /// MaterialLib [Material] rather than routed through its legacy [Materials] counterpart. Returns the
    /// [ItemData] that [gregtech.api.util.GTModHandler#addCraftingRecipe] resolves to an ore-dictionary name
    /// (through [ItemData#toString]) so the ingredient still accepts any matching item, while also carrying the
    /// material association that drives a reversible recipe's auto-generated recycling recipes. A bare
    /// ore-dictionary [String] ingredient supplies only the name, not that association, so a reversible recipe
    /// built from one silently loses its recycling; this preserves both. Null when `prefix` or `material` is
    /// null -- a marker such as `AnyIron` carries no MaterialLib [Material] to pass here in the first place, so
    /// callers building its ingredient use `OrePrefixes#ingredient(Materials)` instead.
    public static @Nullable ItemData craftIngredient(OrePrefixes prefix, @Nullable Material material) {
        return prefix == null || material == null ? null : new ItemData(prefix, material);
    }

    /// The legacy `mMetaItemSubID` a material was assigned (block-form metadata index, e.g. the frame and
    /// storage-block variant selector), or -1 if unset -- mirrors legacy `Materials#mMetaItemSubID`'s own
    /// unset default. Ported byte-identically to [GTMaterialProperties#OLD_SUB_ID]; callers reading
    /// block-form metadata (frame tiers, worldgen) use this instead of the legacy field.
    public static int oldSubId(@Nullable Material material) {
        if (material == null) return -1;
        Integer id = material.getProperty(GTMaterialProperties.OLD_SUB_ID);
        return id == null ? -1 : id;
    }

    /// The legacy `Materials#mRGBa`-format `[r, g, b, a]` short array for a material, or null if it has no
    /// [GTMaterialProperties#ARGB] (unported markers). Unpacks the property with the identical shift/mask math
    /// `Materials`'s own constructor uses on the same value -- `LegacyMaterials.build` feeds this exact
    /// property through that constructor via `MaterialBuilder#setARGB`, so this is byte-identical to the
    /// legacy field for every ported material, not merely observationally equal. Unlike
    /// [com.ruling_0.materiallib.api.StandardProperties#TINT], preserves alpha `0x00` (see
    /// [GTMaterialProperties#ARGB]'s javadoc) -- do not substitute this for TINT in ML-side rendering code.
    public static @Nullable short[] rgba(@Nullable Material material) {
        if (material == null) return null;
        Integer argb = material.getProperty(GTMaterialProperties.ARGB);
        if (argb == null) return null;
        return new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
    }

    /// The legacy `Materials#mIconSet` texture set for a material, resolved by the same TEXTURE_SET-name lookup
    /// [LegacyMaterials#iconSetOf] performs -- byte-identical for every population that reaches a MaterialLib
    /// [Material]: `LegacyMaterials#build` calls it directly to set `mIconSet`; `BridgeMaterialsLoader` sets a
    /// werkstoff bridge's `mIconSet` from `Werkstoff#getTexSet`, which the werkstoff's own constructor was built
    /// with [LegacyMaterials#iconSetOf]'s result (`WerkstoffReconstruction`); and `GtppBridgeMaterialsLoader`
    /// sets a gtpp bridge's `mIconSet` from the same [LegacyMaterials#iconSetOf] call made directly in
    /// `MaterialReconstruction#build`. Null when `material` is null.
    public static @Nullable TextureSet iconSet(@Nullable Material material) {
        return material == null ? null : LegacyMaterials.iconSetOf(material);
    }

    /// The legacy `Materials#mBlastFurnaceRequired` flag for a material, mirroring its own `= false` default.
    /// Ported byte-identically to [GTMaterialProperties#BLAST_REQUIRED]: `LegacyMaterials#build` sets it from
    /// this exact `Boolean.TRUE.equals` check, and both `BridgeMaterialsLoader` (via
    /// `Werkstoff.Stats#isBlastFurnace`) and `GtppBridgeMaterialsLoader` (via
    /// `Material.GtppScalars#usesBlastFurnace`) compute their bridge's flag from the identical expression
    /// against the same property.
    public static boolean blastFurnaceRequired(@Nullable Material material) {
        return material != null && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.BLAST_REQUIRED));
    }

    /// The legacy `Materials#getDensity()` value for a material -- `(M * densityMultiplier) / densityDivider`,
    /// from [GTMaterialProperties#DENSITY_MULTIPLIER]/[#DENSITY_DIVIDER] (each `1` when absent, mirroring
    /// `MaterialBuilder`'s own default), the exact integer math `Materials`'s constructor performs on the ported
    /// data (`LegacyMaterials.build`'s `builder.setDensity`). `null` mirrors the same 1/1 default.
    public static long density(@Nullable Material material) {
        if (material == null) return GTValues.M;
        Integer multiplier = material.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER);
        Integer divider = material.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        return (GTValues.M * (multiplier == null ? 1 : multiplier)) / (divider == null ? 1 : divider);
    }

    /// The legacy `Materials#getMass()` value for a material -- [MaterialAtomics#mass], which reproduces the
    /// identical formula (linked [Element] mass when [GTMaterialProperties#ELEMENT] is present, else
    /// `Element.Tc`'s mass when [GTMaterialProperties#COMPOSITION] is empty or absent, else the
    /// density-weighted average of the composition's own values). `null` mirrors the composition-absent
    /// default.
    public static long mass(@Nullable Material material) {
        return material == null ? Element.Tc.getMass() : MaterialAtomics.mass(material);
    }

    /// [#mass], for `Materials#getProtons()`/[MaterialAtomics#protons].
    public static long protons(@Nullable Material material) {
        return material == null ? Element.Tc.getProtons() : MaterialAtomics.protons(material);
    }

    /// [#mass], for `Materials#getNeutrons()`/[MaterialAtomics#neutrons].
    public static long neutrons(@Nullable Material material) {
        return material == null ? Element.Tc.getNeutrons() : MaterialAtomics.neutrons(material);
    }

    /// The legacy `Materials#mOreByProducts` list for a material, resolved from
    /// [GTMaterialProperties#ORE_BYPRODUCTS] in declaration order; empty when absent. A reference that fails to
    /// resolve is skipped. The [Material]-side equivalent of the legacy field -- `Materials#setOreByproducts`'s
    /// `.mMaterialInto` remap is a proven no-op (every `Materials` instance's `mMaterialInto` is itself), so
    /// unlike that method this does not repeat it.
    public static List<Material> oreByProducts(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<MaterialRefStack> oreByProducts = material.getProperty(GTMaterialProperties.ORE_BYPRODUCTS);
        if (oreByProducts == null || oreByProducts.isEmpty()) return Collections.emptyList();
        List<Material> list = new ArrayList<>(oreByProducts.size());
        for (MaterialRefStack entry : oreByProducts) {
            Material resolved = entry.material()
                .resolve();
            if (resolved != null) list.add(resolved);
        }
        return list;
    }

    /// The legacy `(Materials#mExtraData & 1) != 0` electrolyzer-recipe gate for a material, from
    /// [GTMaterialProperties#HAS_ELECTROLYZER_RECIPE] -- absent mirrors `mExtraData`'s `0` default (bit unset).
    public static boolean hasElectrolyzerRecipe(@Nullable Material material) {
        return material != null
            && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE));
    }

    /// [#hasElectrolyzerRecipe], for the legacy `(Materials#mExtraData & 2) != 0` centrifuge-recipe gate /
    /// [GTMaterialProperties#HAS_CENTRIFUGE_RECIPE].
    public static boolean hasCentrifugeRecipe(@Nullable Material material) {
        return material != null
            && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE));
    }

    /// The legacy `Materials#mColor` [Dyes] for a material, from [GTMaterialProperties#DYE] -- or [Dyes#_NULL]
    /// when absent, mirroring `mColor`'s own default (and, since [Dyes#_NULL]'s name never matches a real lens
    /// ore-dict suffix, its practical never-generates behavior).
    public static Dyes dye(@Nullable Material material) {
        if (material == null) return Dyes._NULL;
        String dye = material.getProperty(GTMaterialProperties.DYE);
        return dye == null ? Dyes._NULL : Dyes.valueOf(dye);
    }

    /// The legacy `Materials#mAutoGenerateBlastFurnaceRecipes` flag for a material, from
    /// [GTMaterialProperties#AUTO_BLAST_FURNACE_RECIPES] -- `true` when absent, mirroring the field's own
    /// default (`LegacyMaterials.build` only overrides it when the property is present).
    public static boolean autoGenerateBlastFurnaceRecipes(@Nullable Material material) {
        if (material == null) return true;
        Boolean value = material.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES);
        return value == null || value;
    }

    /// [#autoGenerateBlastFurnaceRecipes], for `Materials#mAutoGenerateVacuumFreezerRecipes`/
    /// [GTMaterialProperties#AUTO_VACUUM_FREEZER_RECIPES].
    public static boolean autoGenerateVacuumFreezerRecipes(@Nullable Material material) {
        if (material == null) return true;
        Boolean value = material.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES);
        return value == null || value;
    }

    /// The legacy `Materials#mMeltingPoint` Kelvin melting point for a material, or `0` if unset -- mirrors
    /// `MaterialBuilder`'s own default. Ported byte-identically to [GTMaterialProperties#MELTING_POINT]:
    /// `LegacyMaterials.build` feeds this exact property through `MaterialBuilder#setMeltingPoint` when
    /// present, and otherwise leaves the builder's `0` default that `mMeltingPoint` itself falls back to.
    public static int meltingPoint(@Nullable Material material) {
        if (material == null) return 0;
        Integer meltingPoint = material.getProperty(GTMaterialProperties.MELTING_POINT);
        return meltingPoint == null ? 0 : meltingPoint;
    }

    /// [#meltingPoint(Material)] for callers still holding the legacy [Materials] enum constant. Falls back to
    /// a direct legacy `Materials#mMeltingPoint` read when [#material] has no MaterialLib counterpart -- see
    /// [#hasFlag(Materials, GTMaterialFlag)]'s javadoc for why marker materials need this fallback.
    public static int meltingPoint(@Nullable Materials material) {
        if (material == null) return 0;
        Material ml = material(material);
        return ml != null ? meltingPoint(ml) : material.mMeltingPoint;
    }

    /// The legacy `Materials#mBlastFurnaceTemp` Kelvin blast furnace temperature for a material, or `0` if
    /// unset. Ported byte-identically to [GTMaterialProperties#BLAST_TEMP]: `MaterialDataDump` captured the
    /// property already truncated to `mBlastFurnaceTemp`'s `short` range, so `LegacyMaterials.build`'s
    /// `MaterialBuilder#setBlastFurnaceTemp` re-truncation is a no-op and this `int` widening loses nothing.
    public static int blastFurnaceTemp(@Nullable Material material) {
        if (material == null) return 0;
        Integer blastTemp = material.getProperty(GTMaterialProperties.BLAST_TEMP);
        return blastTemp == null ? 0 : blastTemp;
    }

    /// [#blastFurnaceTemp(Material)] for callers still holding the legacy [Materials] enum constant. Falls
    /// back to a direct legacy `Materials#mBlastFurnaceTemp` read when [#material] has no MaterialLib
    /// counterpart -- see [#hasFlag(Materials, GTMaterialFlag)]'s javadoc for why marker materials need this
    /// fallback.
    public static int blastFurnaceTemp(@Nullable Materials material) {
        if (material == null) return 0;
        Material ml = material(material);
        return ml != null ? blastFurnaceTemp(ml) : material.mBlastFurnaceTemp;
    }

    /// The legacy `Materials#getProcessingMaterialTierEU()` value for a material, or `0` if unset -- mirrors
    /// `Materials#processingMaterialTierEU`'s own default. Ported byte-identically to
    /// [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU]: `LegacyMaterials.build` and both bridge loaders
    /// (`WerkstoffReconstruction`, `GtppBridgeMaterialsLoader`) feed this exact property through
    /// `setProcessingMaterialTierEU` when present, and otherwise leave the `0` default every population shares.
    public static int processingMaterialTierEU(@Nullable Material material) {
        if (material == null) return 0;
        Integer tierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        return tierEU == null ? 0 : tierEU;
    }

    /// The legacy `Materials#mDurability` tool durability for a material, or `0` if unset -- the same default
    /// `LegacyMaterials#build` feeds `MaterialBuilder#setTool` when [GTMaterialProperties#DURABILITY] is
    /// absent, itself mirroring `mDurability`'s own field default.
    public static int durability(@Nullable Material material) {
        if (material == null) return 0;
        Integer durability = material.getProperty(GTMaterialProperties.DURABILITY);
        return durability == null ? 0 : durability;
    }

    /// [#durability], for `Materials#mToolQuality`/[GTMaterialProperties#TOOL_QUALITY].
    public static int toolQuality(@Nullable Material material) {
        if (material == null) return 0;
        Integer toolQuality = material.getProperty(GTMaterialProperties.TOOL_QUALITY);
        return toolQuality == null ? 0 : toolQuality;
    }

    /// [#durability], for `Materials#mToolSpeed`/[GTMaterialProperties#TOOL_SPEED] -- absent defaults to
    /// `1.0f`, the `setTool` default `LegacyMaterials#build` passes and `mToolSpeed`'s own field default.
    public static float toolSpeed(@Nullable Material material) {
        if (material == null) return 1.0f;
        Float toolSpeed = material.getProperty(GTMaterialProperties.TOOL_SPEED);
        return toolSpeed == null ? 1.0f : toolSpeed;
    }

    /// The legacy `Materials#mHeatDamage` for a material, or `0` if unset -- mirrors `MaterialBuilder`'s own
    /// default (`LegacyMaterials#build` only calls `setHeatDamage` when [GTMaterialProperties#HEAT_DAMAGE] is
    /// present).
    public static float heatDamage(@Nullable Material material) {
        if (material == null) return 0f;
        Float heatDamage = material.getProperty(GTMaterialProperties.HEAT_DAMAGE);
        return heatDamage == null ? 0f : heatDamage;
    }

    /// The legacy `Materials#mUnifiable` flag for a material -- `true` unless [GTMaterialProperties#UNIFIABLE]
    /// is explicitly `false`, mirroring `MaterialBuilder`'s `true` default (`LegacyMaterials#build` only calls
    /// `setUnifiable(false)` on an explicit `false`).
    public static boolean unifiable(@Nullable Material material) {
        return material == null || !Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE));
    }

    /// The legacy `Materials#mFuelPower` fuel value for a material, or `0` if unset -- mirrors
    /// `MaterialBuilder`'s own default (`LegacyMaterials.build` only calls `MaterialBuilder#setFuel` when
    /// [GTMaterialProperties#FUEL_TYPE] or [GTMaterialProperties#FUEL_POWER] is present).
    public static int fuelPower(@Nullable Material material) {
        if (material == null) return 0;
        Integer fuelPower = material.getProperty(GTMaterialProperties.FUEL_POWER);
        return fuelPower == null ? 0 : fuelPower;
    }

    /// [#fuelPower(Material)] for callers still holding the legacy [Materials] enum constant. Falls back to a
    /// direct legacy `Materials#mFuelPower` read when [#material] has no MaterialLib counterpart -- see
    /// [#hasFlag(Materials, GTMaterialFlag)]'s javadoc for why marker materials need this fallback.
    public static int fuelPower(@Nullable Materials material) {
        if (material == null) return 0;
        Material ml = material(material);
        return ml != null ? fuelPower(ml) : material.mFuelPower;
    }

    /// The legacy `Materials#mFuelType` `MaterialBuilder.FuelType` ordinal for a material, or `0`
    /// (`MaterialBuilder.FuelType#Diesel`) if unset -- mirrors `MaterialBuilder`'s own default, see
    /// [#fuelPower(Material)].
    public static int fuelType(@Nullable Material material) {
        if (material == null) return 0;
        Integer fuelType = material.getProperty(GTMaterialProperties.FUEL_TYPE);
        return fuelType == null ? 0 : fuelType;
    }

    /// [#fuelType(Material)] for callers still holding the legacy [Materials] enum constant. Falls back to a
    /// direct legacy `Materials#mFuelType` read when [#material] has no MaterialLib counterpart -- see
    /// [#hasFlag(Materials, GTMaterialFlag)]'s javadoc for why marker materials need this fallback.
    public static int fuelType(@Nullable Materials material) {
        if (material == null) return 0;
        Material ml = material(material);
        return ml != null ? fuelType(ml) : material.mFuelType;
    }

    /// The legacy `Materials#mSmeltInto` smelting target, resolved from [GTMaterialProperties#SMELT_INTO] --
    /// or `material` itself when the property is absent, mirroring `mSmeltInto`'s own `= this` default.
    /// `LegacyMaterials.build` only ever populates the deferred supplier this reads (via `addDeferredRef`),
    /// never the final field; the actual `mSmeltInto` assignment happens once, for every material, in
    /// `Materials`'s static initializer (`setSmeltingInto`) -- unconditionally before bartworks' bridge ever
    /// runs (bartworks mutates its bridge materials no earlier than its own `FMLInitializationEvent`, well
    /// after `Materials`'s static initializer has already completed for every mod). `mSmeltInto` is never
    /// reassigned after that, so unlike [GTMaterialProperties#HANDLE_MATERIAL] this needs no bridge-timing
    /// guard. Chases the referenced material's own `mSmeltInto` once more, mirroring `setSmeltingInto`'s
    /// `.mMaterialInto.mSmeltInto` indirection (the `mMaterialInto` hop is a proven no-op -- every `Materials`
    /// constructor sets it to `this`, and bartworks' own bridge mutation reassigns it to itself too).
    public static @Nullable Materials smeltInto(@Nullable Materials material) {
        if (material == null) return null;
        Material ml = material(material);
        if (ml == null) return material.mSmeltInto;
        MaterialRef ref = ml.getProperty(GTMaterialProperties.SMELT_INTO);
        if (ref == null) return material;
        Materials resolved = resolveLegacyRef(ref);
        return resolved != null ? resolved.mSmeltInto : material;
    }

    /// [#smeltInto], for `Materials#mMacerateInto`/[GTMaterialProperties#MACERATE_INTO].
    public static @Nullable Materials macerateInto(@Nullable Materials material) {
        if (material == null) return null;
        Material ml = material(material);
        if (ml == null) return material.mMacerateInto;
        MaterialRef ref = ml.getProperty(GTMaterialProperties.MACERATE_INTO);
        if (ref == null) return material;
        Materials resolved = resolveLegacyRef(ref);
        return resolved != null ? resolved.mMacerateInto : material;
    }

    /// [#smeltInto], for `Materials#mArcSmeltInto`/[GTMaterialProperties#ARC_SMELT_INTO].
    public static @Nullable Materials arcSmeltInto(@Nullable Materials material) {
        if (material == null) return null;
        Material ml = material(material);
        if (ml == null) return material.mArcSmeltInto;
        MaterialRef ref = ml.getProperty(GTMaterialProperties.ARC_SMELT_INTO);
        if (ref == null) return material;
        Materials resolved = resolveLegacyRef(ref);
        return resolved != null ? resolved.mArcSmeltInto : material;
    }

    /// [#smeltInto], for `Materials#mDirectSmelting`/[GTMaterialProperties#DIRECT_SMELTING].
    public static @Nullable Materials directSmelting(@Nullable Materials material) {
        if (material == null) return null;
        Material ml = material(material);
        if (ml == null) return material.mDirectSmelting;
        MaterialRef ref = ml.getProperty(GTMaterialProperties.DIRECT_SMELTING);
        if (ref == null) return material;
        Materials resolved = resolveLegacyRef(ref);
        return resolved != null ? resolved.mDirectSmelting : material;
    }

    /// The legacy `Materials#mHandleMaterial` tool-handle material -- the live field itself, not
    /// [GTMaterialProperties#HANDLE_MATERIAL]. That property is a MaterialLib load-time snapshot, while
    /// `mHandleMaterial` is only finalized later, at recipe-registration time, once the bartworks/gtpp bridge
    /// loaders' `FMLInitializationEvent` handle-material writes have run; the two can disagree for materials
    /// whose bridge-computed handle differs from the snapshot, so every recipe-registration call site reads
    /// this live field, matching what it always read directly before this accessor existed.
    public static @Nullable Materials handleMaterial(@Nullable Materials material) {
        if (material == null) return null;
        return material.mHandleMaterial;
    }

    /// [#smeltInto(Materials)] for a MaterialLib [Material] held directly, mirroring the same semantics on
    /// [GTMaterialProperties#SMELT_INTO]: an unset property means the material smelts into itself, and a set
    /// one is chased one more hop through the target's own property (the legacy `setSmeltingInto` indirection).
    public static @Nullable Material smeltInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.SMELT_INTO);
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#MACERATE_INTO].
    public static @Nullable Material macerateInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.MACERATE_INTO);
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#ARC_SMELT_INTO].
    public static @Nullable Material arcSmeltInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.ARC_SMELT_INTO);
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#DIRECT_SMELTING].
    public static @Nullable Material directSmelting(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.DIRECT_SMELTING);
    }

    /// [#handleMaterial(Materials)] for a MaterialLib [Material] held directly. HYBRID: when `material` has a
    /// legacy [Materials] counterpart, mirrors [#handleMaterial(Materials)] by reading the live
    /// `Materials#mHandleMaterial` field and converting the result back through [#material] -- the property
    /// path below is a load-time snapshot of that field and can diverge from it, same as the `Materials`
    /// overload. Materials without a legacy counterpart (werkstoffe, gtpp materials) have no live field left,
    /// so those resolve the [#recordHandleMaterial] override the bridge loaders now push in place of their
    /// retired facade write; [GTMaterialProperties#HANDLE_MATERIAL] remains the fallback for a reconstructed
    /// material neither loader touched, one hop only -- the property never chains through another material's
    /// own handle, so there is nothing further to chase.
    public static @Nullable Material handleMaterial(@Nullable Material material) {
        if (material == null) return null;
        Materials legacy = materialOf(material);
        if (legacy != null) {
            Material handle = material(legacy.mHandleMaterial);
            return handle != null ? handle : material;
        }
        Material override = reconstructedHandles.get(material);
        if (override != null) return override;
        MaterialRef ref = material.getProperty(GTMaterialProperties.HANDLE_MATERIAL);
        if (ref == null) return material;
        Material resolved = ref.resolve();
        return resolved != null ? resolved : material;
    }

    private static final Map<Material, Material> reconstructedHandles = new HashMap<>();

    /// Records the tool-handle material `material`'s retired bridge facade would have carried in
    /// `Materials#mHandleMaterial` (see [#handleMaterial(Material)]). Called by the bridge loaders
    /// (`MaterialReconstruction#build`, bartworks' `BridgeMaterialsLoader`) at the exact points the facade
    /// writes used to run; a later write overrides an earlier one, matching the facade both loaders mutated
    /// in turn for a dual werkstoff+gtpp material.
    public static void recordHandleMaterial(Material material, Material handle) {
        reconstructedHandles.put(material, handle);
    }

    private static final java.util.Set<Material> reconstructedBridgeRegistrations = new java.util.HashSet<>();

    /// Records that `material`'s retired bridge facade would have entered `Materials#getMaterialsMap` by this
    /// point in loading. Called by the bridge loaders (`MaterialReconstruction#build`, bartworks'
    /// `BridgeMaterialsLoader`) at the exact former minting sites; recording twice for a dual werkstoff+gtpp
    /// material is a no-op, matching the single facade both loaders shared.
    public static void recordBridgeRegistration(Material material) {
        reconstructedBridgeRegistrations.add(material);
    }

    /// Whether [#recordBridgeRegistration] has run for `material` -- the reconstructed-material equivalent of
    /// the bridge era's `Materials.get(name) != _NULL`, which flipped true at the same moment. Keys
    /// `GTProxy#registerOre`'s reconstructed-name dispatch, so an ore-dictionary event registered before this
    /// point (notably GregTech's own preInit catch-up replay) is dropped exactly as the bridge era dropped it.
    public static boolean hasBridgeRegistration(@Nullable Material material) {
        return material != null && reconstructedBridgeRegistrations.contains(material);
    }

    /// Resolves a [GTMaterialProperties] `MaterialRef` property to its legacy [Materials] counterpart through
    /// [#materialOf], which keys off [GTMaterialProperties#LEGACY_NAME] -- not `Materials.get(ref.name())`,
    /// which breaks whenever MaterialLib sanitized the target's registration name away from its true legacy
    /// name (see `gregtech.loaders.materials.MaterialsLegacyBridge#ML_NAME_TO_FIELD_OVERRIDES`, e.g. ML `NULL`
    /// vs legacy `_NULL`) and would otherwise silently land on `Materials#_NULL` instead of the real target.
    private static @Nullable Materials resolveLegacyRef(@Nullable MaterialRef ref) {
        if (ref == null) return null;
        Material resolved = ref.resolve();
        return resolved == null ? null : materialOf(resolved);
    }

    private static @Nullable Material chaseRef(@Nullable Material material,
        com.ruling_0.materiallib.api.Property<MaterialRef> property) {
        if (material == null) return null;
        MaterialRef ref = material.getProperty(property);
        if (ref == null) return material;
        Material target = ref.resolve();
        if (target == null) return material;
        MaterialRef hop = target.getProperty(property);
        Material hopped = hop == null ? null : hop.resolve();
        return hopped != null ? hopped : target;
    }

    /// Whether a material carries a legacy [gregtech.api.enums.SubTag], ported 1:1 to [GTMaterialFlag] of the
    /// same name -- see [GTMaterialProperties#FLAGS]. Also true when [GTMaterialProperties#SUB_TAGS] (the
    /// werkstoff facade's raw `Werkstoff` SubTag list, captured separately from FLAGS -- see that property's
    /// javadoc) names `flag`: a werkstoff-backed material's FLAGS reflects only what its dumped bridge
    /// `Materials` carried, which never included its own `Werkstoff`'s SubTags, so this reads SUB_TAGS as a
    /// second source for the same 1:1 name mapping FLAGS already uses. Mirrors legacy
    /// `Materials#contains(SubTag)`/`mSubTags`.
    public static boolean hasFlag(@Nullable Material material, GTMaterialFlag flag) {
        if (material == null) return false;
        EnumSet<GTMaterialFlag> flags = material.getProperty(GTMaterialProperties.FLAGS);
        if (flags != null && flags.contains(flag)) return true;
        List<String> subTags = material.getProperty(GTMaterialProperties.SUB_TAGS);
        return subTags != null && subTags.contains(flag.name());
    }

    /// [#hasFlag(Material, GTMaterialFlag)] for callers still holding the legacy [Materials] enum constant.
    /// Falls back to a direct legacy `Materials#contains(SubTag)` read when [#material] has no MaterialLib
    /// counterpart -- the ~291 marker materials `LegacyMarkerMaterials` builds directly (e.g. `AnyBronze`,
    /// `AnyCopper`) still carry real legacy SubTags of their own despite never being ML-backed, so treating an
    /// unmapped material as flagless (like the raw [Material] overload does for a genuinely absent property)
    /// would be wrong here. `GTMaterialFlag` names match `SubTag` names 1:1 for every flag this fallback can
    /// reach (mirrors `LegacyMaterials`'s own `legacySubTagName`; the two dynamic bartworks-only exceptions,
    /// `ANAEROBE_GAS`/`NOBLE_GAS`, never apply to a plain `Materials` instance).
    public static boolean hasFlag(@Nullable Materials material, GTMaterialFlag flag) {
        if (material == null) return false;
        Material ml = material(material);
        if (ml != null) return hasFlag(ml, flag);
        return material.contains(SubTag.getNewSubTag(flag.name()));
    }

    /// [#hasFlag(Materials, GTMaterialFlag)] for a recognition marker, consulted through its own sub-tag set
    /// for the [SubTag] whose name matches `flag` 1:1.
    public static boolean hasFlag(@Nullable RecognitionMarker material, GTMaterialFlag flag) {
        return material != null && material.contains(SubTag.getNewSubTag(flag.name()));
    }

    /// The legacy internal name of a MaterialLib material -- [GTMaterialProperties#LEGACY_NAME] when present
    /// (MaterialLib sanitizes registration names), otherwise the registration name. The [Material]-side
    /// equivalent of `Materials#getInternalName`, used to build ore-dictionary names and lang keys.
    public static String internalName(Material material) {
        String legacyName = material.getProperty(GTMaterialProperties.LEGACY_NAME);
        return legacyName != null ? legacyName : material.getName();
    }

    /// The legacy `Materials#mDefaultLocalName` display name for a material --
    /// [GTMaterialProperties#LOCAL_NAME] when present, otherwise the registration name, the exact fallback
    /// `LegacyMaterials#build` feeds `setDefaultLocalName`. Null when `material` is null.
    public static @Nullable String localName(@Nullable Material material) {
        if (material == null) return null;
        String localName = material.getProperty(GTMaterialProperties.LOCAL_NAME);
        return localName != null ? localName : material.getName();
    }

    /// The [Element] a MaterialLib material's [GTMaterialProperties#ELEMENT] names, or null when it carries none.
    public static @Nullable Element element(@Nullable Material material) {
        if (material == null) return null;
        String elementName = material.getProperty(GTMaterialProperties.ELEMENT);
        return elementName == null ? null : Element.get(elementName);
    }

    /// The chemical formula of a MaterialLib material ([GTMaterialProperties#FORMULA], localized through the
    /// material's lang key when [GTMaterialProperties#FORMULA_LOCALIZED] is set), or the empty string when it
    /// carries none.
    public static String chemicalFormula(@Nullable Material material) {
        if (material == null) return "";
        String formula = material.getProperty(GTMaterialProperties.FORMULA);
        if (formula == null) return "";
        if (Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.FORMULA_LOCALIZED))) {
            return StatCollector
                .translateToLocal("Material." + internalName(material).toLowerCase() + ".ChemicalFormula");
        }
        return formula;
    }

    public static String chemicalTooltip(@Nullable Material material, boolean showQuestionMarks) {
        return chemicalTooltip(material, 1, showQuestionMarks);
    }

    /// The chemical-formula tooltip of a MaterialLib material, matching the legacy `Materials` rendering: a
    /// bare `?` formula is hidden unless `showQuestionMarks`, and a composed material at two or more full
    /// units is parenthesized and suffixed with the multiplier.
    public static String chemicalTooltip(@Nullable Material material, long multiplier, boolean showQuestionMarks) {
        String formula = chemicalFormula(material);
        if (!showQuestionMarks && formula.equals("?")) return "";
        List<MaterialStack> list = materialList(material);
        if (multiplier >= 2 * GTValues.M && !list.isEmpty()) {
            return ((material.getProperty(GTMaterialProperties.ELEMENT) != null
                || (list.size() < 2 && list.get(0).mAmount == 1)) ? formula : "(" + formula + ")") + multiplier;
        }
        return formula;
    }

    /// The composition of a MaterialLib material as [MaterialStack]s, resolved from
    /// [GTMaterialProperties#COMPOSITION]; empty when it carries none.
    public static List<MaterialStack> materialList(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return Collections.emptyList();
        List<MaterialStack> list = new ArrayList<>(composition.size());
        for (MaterialRefStack entry : composition) {
            Material resolved = entry.material()
                .resolve();
            if (resolved != null) list.add(new MaterialStack(resolved, entry.amount()));
        }
        return list;
    }

    /// The legacy `Materials#mAspects` Thaumcraft aspect list for a material, as [AspectRefStack]s. An
    /// explicit [GTMaterialProperties#ASPECTS] list is returned as-is (the legacy builder's `addAspect`
    /// path); without one, a composed material derives its aspects exactly as `Materials`'s constructor
    /// does -- each component's own aspects merged in first-seen order with amounts summed per aspect, then
    /// divided by the density-scaled component count, minimum `1` each. Empty when neither source applies,
    /// mirroring the legacy field's empty-list default.
    public static List<AspectRefStack> aspects(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<AspectRefStack> aspects = material.getProperty(GTMaterialProperties.ASPECTS);
        if (aspects != null) return aspects;
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return Collections.emptyList();
        Map<String, Long> merged = new LinkedHashMap<>();
        long components = 0;
        for (MaterialRefStack entry : composition) {
            components += entry.amount();
            Material component = entry.material()
                .resolve();
            if (component == null) continue;
            for (AspectRefStack aspect : aspects(component)) {
                if (aspect.amount() == 0) continue;
                merged.merge(aspect.name(), (long) aspect.amount(), Long::sum);
            }
        }
        Integer multiplier = material.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER);
        Integer divider = material.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        components = components * (multiplier == null ? 1 : multiplier) / (divider == null ? 1 : divider);
        long divisor = Math.max(1, components);
        List<AspectRefStack> list = new ArrayList<>(merged.size());
        for (Map.Entry<String, Long> entry : merged.entrySet()) {
            list.add(new AspectRefStack(entry.getKey(), (int) Math.max(1, entry.getValue() / divisor)));
        }
        return list;
    }

    // Union dispatch helpers: the transitional three-family union (Materials / Werkstoff / gtPlusPlus
    // Material, plus the recognition markers) previously flowed through legacy-interface-typed slots; those
    // slots are now Object-typed (or MaterialLib Material-typed) and these helpers reproduce the exact
    // per-family member behavior at the read sites. TRANSITIONAL -- each dies with its last union call site.

    /// The legacy `findMaterial` lookup for the transitional union: a werkstoff by var name first, then a
    /// gtPlusPlus material, then a legacy [Materials]. Null on a miss.
    public static @Nullable Object findLegacyMaterial(String name) {
        Werkstoff bw = Werkstoff.werkstoffVarNameHashMap.get(name);
        if (bw != null) return bw;
        gtPlusPlus.core.material.Material gtpp = gtPlusPlus.core.material.Material.mMaterialsByName.get(name);
        if (gtpp != null) return gtpp;
        return Materials.getMaterialsMap()
            .get(name);
    }

    /// `getInternalName` across the union; [#internalName] for a [Material]; null for null or a foreign type.
    public static @Nullable String internalNameOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getInternalName();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getInternalName();
        if (material instanceof Materials legacy) return legacy.getInternalName();
        if (material instanceof RecognitionMarker marker) return marker.getInternalName();
        if (material instanceof Material ml) return internalName(ml);
        return null;
    }

    /// `getLocalizedNameKey` across the union; null for null or a foreign type.
    public static @Nullable String localizedNameKeyOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getLocalizedNameKey();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getLocalizedNameKey();
        if (material instanceof Materials legacy) return legacy.getLocalizedNameKey();
        if (material instanceof RecognitionMarker marker) return marker.getLocalizedNameKey();
        if (material instanceof Material ml) return localizedNameKeyOf(legacyMaterialOf(ml));
        return null;
    }

    /// `getLocalizedName` across the union; null for null or a foreign type.
    public static @Nullable String localizedNameOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getLocalizedName();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getLocalizedName();
        if (material instanceof Materials legacy) return legacy.getLocalizedName();
        if (material instanceof Material ml) return localizedNameOf(legacyMaterialOf(ml));
        return null;
    }

    /// `getDefaultLocalName` across the union; [#localName] for a [Material]; null for null or a foreign type.
    public static @Nullable String defaultLocalNameOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getDefaultLocalName();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getDefaultLocalName();
        if (material instanceof Materials legacy) return legacy.getDefaultLocalName();
        if (material instanceof Material ml) return localName(ml);
        return null;
    }

    /// `getTextureSet` across the union; null for null or a foreign type.
    public static @Nullable TextureSet textureSetOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getTextureSet();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getTextureSet();
        if (material instanceof Materials legacy) return legacy.getTextureSet();
        if (material instanceof Material ml) return textureSetOf(legacyMaterialOf(ml));
        return null;
    }

    /// `getRGBA` across the union; null for null or a foreign type.
    public static @Nullable short[] rgbaOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getRGBA();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getRGBA();
        if (material instanceof Materials legacy) return legacy.getRGBA();
        if (material instanceof RecognitionMarker marker) return marker.getRGBA();
        if (material instanceof Material ml) return rgbaOf(legacyMaterialOf(ml));
        return null;
    }

    /// `getValidStones` across the union; empty for null or a foreign type.
    public static List<IStoneType> validStonesOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getValidStones();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getValidStones();
        if (material instanceof Materials legacy) return legacy.getValidStones();
        if (material instanceof Material ml) return validStonesOf(legacyMaterialOf(ml));
        return Collections.emptyList();
    }

    /// `generatesPrefix` across the union; false for null or a foreign type.
    public static boolean generatesPrefix(@Nullable Object material, OrePrefixes prefix) {
        if (material instanceof Werkstoff w) return w.generatesPrefix(prefix);
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.generatesPrefix(prefix);
        if (material instanceof Materials legacy) return legacy.generatesPrefix(prefix);
        if (material instanceof Material ml) return generatesPrefix(legacyMaterialOf(ml), prefix);
        return false;
    }

    /// `ISubTagContainer#contains` across the union (a gtPlusPlus material carries no sub tags and is always
    /// false, matching its own `contains`); false for null or a foreign type.
    public static boolean hasSubTag(@Nullable Object material, SubTag subTag) {
        if (material instanceof Werkstoff w) return w.contains(subTag);
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.contains(subTag);
        if (material instanceof Materials legacy) return legacy.contains(subTag);
        if (material instanceof Material ml) return hasSubTag(legacyMaterialOf(ml), subTag);
        return false;
    }

    /// `getPart` across the union (a gtPlusPlus material reproduces `Materials#getPart` -- through
    /// its bridge [Materials] when it has one, by ore-dictionary name otherwise); null for null or a foreign
    /// type.
    public static @Nullable ItemStack partOf(@Nullable Object material, OrePrefixes prefix, int amount) {
        if (material instanceof Werkstoff w) return w.getPart(prefix, amount);
        if (material instanceof gtPlusPlus.core.material.Material gtpp) {
            Materials gt = gtpp.getGTMaterial();
            if (gt != null) return GTOreDictUnificator.get(prefix, gt, amount);
            return GTOreDictUnificator.get(prefix.oreDictName(gtpp.getInternalName()), amount);
        }
        if (material instanceof Materials legacy) return legacy.getPart(prefix, amount);
        if (material instanceof Material ml) return partOf(legacyMaterialOf(ml), prefix, amount);
        return null;
    }

    /// `getGTMaterial` across the union -- the bridge/equivalent [Materials] of a werkstoff or gtPlusPlus
    /// material, a [Materials] itself, or null.
    public static @Nullable Materials gtMaterialOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getGTMaterial();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getGTMaterial();
        if (material instanceof Materials legacy) return legacy.getGTMaterial();
        if (material instanceof Material ml) return gtMaterialOf(legacyMaterialOf(ml));
        return null;
    }

    /// `getId` across the union; `0` for null or a foreign type.
    public static int idOf(@Nullable Object material) {
        if (material instanceof Werkstoff w) return w.getId();
        if (material instanceof gtPlusPlus.core.material.Material gtpp) return gtpp.getId();
        if (material instanceof Materials legacy) return legacy.getId();
        if (material instanceof Material ml) return idOf(legacyMaterialOf(ml));
        return 0;
    }

    /// `addTooltips` across the union; a no-op for null or a foreign type.
    public static void addTooltipsOf(@Nullable Object material, List<String> list) {
        if (material instanceof Werkstoff w) w.addTooltips(list);
        else if (material instanceof gtPlusPlus.core.material.Material gtpp) gtpp.addTooltips(list);
        else if (material instanceof Materials legacy) legacy.addTooltips(list);
        else if (material instanceof Material ml) addTooltipsOf(legacyMaterialOf(ml), list);
    }

    private static Map<String, List<Shape>> prefixShapes() {
        if (prefixToShapes == null) {
            Map<String, List<Shape>> map = new HashMap<>();
            collectShapes(map, Materials2Shapes.class);
            collectShapes(map, Materials2CellShapes.class);
            collectShapes(map, Materials2BlockShapes.class);
            collectShapes(map, Materials2OreShapes.class);
            collectShapes(map, Materials2PipeShapes.class);
            // cellPlasmaLight is a second candidate shape for the cellPlasma prefix, not a prefix of its own
            // (see Materials2CellShapes); its field name deliberately does not match an OrePrefixes name, so
            // fold it into "cellPlasma"'s candidate list instead of collecting it under its own key.
            if (Materials2CellShapes.cellPlasmaLight != null) {
                map.get("cellPlasma")
                    .add(Materials2CellShapes.cellPlasmaLight);
            }
            // The item-pipe shapes' field names likewise differ from their oredict prefixes (fluid and item
            // pipes share the pipeTiny..pipeHuge prefix strings, see Materials2PipeShapes); fold each under
            // its prefix key, after the fluid shape where one exists.
            foldPipeShapes(map, "itemPipeTiny", "pipeTiny");
            foldPipeShapes(map, "itemPipeSmall", "pipeSmall");
            foldPipeShapes(map, "itemPipeMedium", "pipeMedium");
            foldPipeShapes(map, "itemPipeLarge", "pipeLarge");
            foldPipeShapes(map, "itemPipeHuge", "pipeHuge");
            foldPipeShapes(map, "itemPipeRestrictiveTiny", "pipeRestrictiveTiny");
            foldPipeShapes(map, "itemPipeRestrictiveSmall", "pipeRestrictiveSmall");
            foldPipeShapes(map, "itemPipeRestrictiveMedium", "pipeRestrictiveMedium");
            foldPipeShapes(map, "itemPipeRestrictiveLarge", "pipeRestrictiveLarge");
            foldPipeShapes(map, "itemPipeRestrictiveHuge", "pipeRestrictiveHuge");
            prefixToShapes = map;
        }
        return prefixToShapes;
    }

    private static void foldPipeShapes(Map<String, List<Shape>> map, String fieldName, String prefixName) {
        List<Shape> folded = map.remove(fieldName);
        if (folded == null) return;
        map.computeIfAbsent(prefixName, k -> new ArrayList<>())
            .addAll(folded);
    }

    private static void collectShapes(Map<String, List<Shape>> map, Class<?> shapesClass) {
        for (Field field : shapesClass.getFields()) {
            if (field.getType() != Shape.class) continue;
            Shape shape = readStatic(field);
            if (shape == null) continue;
            map.computeIfAbsent(field.getName(), k -> new ArrayList<>())
                .add(shape);
        }
    }

    private static Map<String, Material> legacyNamedMaterials() {
        if (legacyNameToMaterial == null) {
            Map<String, Material> map = new HashMap<>();
            for (Field field : Materials2Materials.class.getFields()) {
                if (field.getType() != Material.class) continue;
                Material material = readStatic(field);
                if (material != null) map.put(internalName(material), material);
            }
            legacyNameToMaterial = map;
        }
        return legacyNameToMaterial;
    }

    @SuppressWarnings("unchecked")
    private static <T> T readStatic(Field field) {
        try {
            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
