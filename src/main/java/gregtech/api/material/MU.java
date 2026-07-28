package gregtech.api.material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2ArcSmelting;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2IDIndex;
import gregtech.api.enums.materials2.Materials2Markers;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.enums.materials2.Materials2Textures;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.config.Client;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import gregtech.loaders.materials.LegacyNameDomain;

/// Bridges legacy [OrePrefixes] to their cutover MaterialLib [Shape]/[Material] equivalents, and provides
/// [Material]-side accessors for the data legacy code once read off `gregtech.api.enums.Materials`.
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
/// name, preferring [GTMaterialProperties#LEGACY_NAME] over [Material#getName] because MaterialLib sanitizes
/// registration names that contain characters `Names#validate` rejects.
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

    /// The MaterialLib [Material] backing a legacy-family material object -- a gtPlusPlus `Material` -- for
    /// migrating the plumbing off the legacy families onto [Material]. Resolved by internal name. A [Material]
    /// passes through unchanged. Null when nothing backs it.
    public static @Nullable Material toMaterial(@Nullable Object material) {
        if (material == null) return null;
        if (material instanceof Material ml) return ml;
        String name = internalNameOf(material);
        return name == null ? null : MaterialLibAPI.getMaterial("gregtech", name);
    }

    /// The MaterialLib material a legacy `Materials.get(name)` lookup cuts over to, or null on a miss --
    /// replacing the `_NULL` sentinel with null. Resolves through [#material]'s exact chain, keyed by the
    /// name directly: the [Materials2Materials]-field map (keyed by legacy internal name), then the
    /// MaterialLib registry for the shapeless marker backings without a declared field
    /// ([gregtech.loaders.materials.LegacyMarkerMaterials], [gregtech.loaders.materials.
    /// RecognitionMaterials]). `Materials.get` keys its map by `Materials#mName` -- the same string
    /// [#internalName] yields -- so any name it resolves to a facade resolves here to that facade's
    /// [#material]. Marker names `Materials.get` cannot resolve (the superconductors kept out of
    /// `getMaterialsMap`, the recognition-marker fields) resolve here to their registered backing.
    public static @Nullable Material byLegacyName(@Nullable String name) {
        if (name == null) return null;
        Material found = legacyNamedMaterials().get(name);
        if (found != null) return found;
        return MaterialLibAPI.getMaterial("gregtech", name);
    }

    /// The material occupying a legacy generated-material id slot ([Materials2IDIndex]). Null for an empty slot
    /// or an out-of-range id.
    public static @Nullable Material byId(int id) {
        return Materials2IDIndex.get(id);
    }

    /// The cutover MaterialLib stack for a (prefix, material) pair, or null when either side has no cutover
    /// mapping. When a prefix maps to more than one candidate shape (`cellPlasma`), the first one `material`
    /// actually generates is used.
    public static @Nullable ItemStack stack(OrePrefixes prefix, @Nullable Material material, long amount) {
        if (prefix == null || material == null) return null;
        List<Shape> shapes = prefixShapes().get(prefix.name());
        if (shapes == null) return null;
        for (Shape shape : shapes) {
            if (material.hasShape(shape)) return MaterialLibAPI.getStack(material, shape, (int) amount);
        }
        return null;
    }

    /// A material's `cell` item, falling back to `cellMolten` when the plain `cell` shape does not resolve --
    /// unlike `cellPlasma` (whose [#stack] candidate list already includes `cellPlasmaLight`), `cell` has no
    /// built-in fallback: a gtpp material whose single fluid claimed [Materials2FluidShapes#fluidMolten]
    /// instead of a liquid/gas cell-eligible slot carries its full cell only under `cellMolten`.
    public static @Nullable ItemStack cellStack(@Nullable Material material, long amount) {
        ItemStack cell = stack(OrePrefixes.cell, material, amount);
        return cell != null ? cell : stack(OrePrefixes.cellMolten, material, amount);
    }

    /// Whether a (prefix, material) pair has a MaterialLib equivalent (see [#stack]). Unlike [#shape], which
    /// answers whether a prefix has cut over at all, this answers per material -- needed because a
    /// fluid-in-container shape's membership does not always mirror every material with a real legacy slot: a
    /// material can hold a legacy `cell` item generated purely from its `CELL` capability flag while never
    /// having a fluid to put in it (MaterialLib's container contract requires a material to also generate one
    /// of the container's fluid shapes, so such a material is left off `cell`'s membership and keeps its
    /// legacy item instead). Legacy construction code should skip a (prefix, material) pair exactly when this
    /// is true, not merely when [#shape] is non-null.
    public static boolean isCutOver(OrePrefixes prefix, @Nullable Material material) {
        return stack(prefix, material, 1) != null;
    }

    /// Whether `stack`'s unification association ([GTOreDictUnificator#getAssociation]) names `material` as
    /// its primary material, compared by identity.
    public static boolean isPartOf(@Nullable ItemStack stack, @Nullable Material material) {
        if (material == null) return false;
        ItemData association = GTOreDictUnificator.getAssociation(stack);
        return association != null && association.mMaterial.mMaterial == material;
    }

    /// The dust [ItemStack] a [GTMaterialProperties#COMPOSITION] entry contributes to a recipe, sized by the
    /// entry's amount, or null when the referenced material carries no `dust` shape (a gas/fluid-only
    /// component -- see [#compositionGas]) or fails to resolve. A composition entry always names a MaterialLib
    /// material directly ([MaterialRef#resolve]), so unlike [#stack] this needs no bartworks fallback.
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

    /// Whether a material has a resolvable molten fluid (see [#molten]) -- for callers that need the presence
    /// check independent of a specific fluid amount, such as one gate guarding several [#molten] calls of
    /// different amounts. True whenever the material carries the [Materials2FluidShapes#fluidMolten]
    /// MaterialLib shape (byte-equal to a legacy-named counterpart's `mStandardMoltenFluid != null`: the
    /// field is wired from the [GTMaterialProperties#LEGACY_FLUIDS] `molten` slot whose presence is exactly
    /// this shape's membership) or a [#recordSlotFluid]-stored MOLTEN fluid (a `GTFluid#configureMaterials`
    /// direct write). This is not restricted to [#isLegacyNamed] materials: every solid/liquid gtPlusPlus-only
    /// material whose single fluid was ported carries the same shape (`scripts/mu/gen_materials.py`'s
    /// `gtpp_fluid_and_cell_shape_lines` gave it to the whole set, `Water` the sole exception -- a
    /// vanilla-fluid special case resolved through its own legacy field instead), so gating the shape/stored
    /// checks behind [#isLegacyNamed] previously left every gtPlusPlus-only material's mold/fluid-consuming
    /// recipes ungenerated. A material with neither falls back to whether
    /// [GTMaterialProperties#WERKSTOFF_PREFIXES] contains `cellMolten` -- the bartworks part-generation ground
    /// truth recorded for the material. NOT `cellMolten` shape membership: a
    /// dual-nature element reconstructed from both a bartworks and a gtPlusPlus population (e.g.
    /// `Zirconium`/`Hafnium`/`Thorium232`) can carry the shape from its gtPlusPlus fluid capture while the
    /// werkstoff side never generated the item -- this fallback only runs once the shape/stored checks above
    /// already missed, so it never overrides either the legacy-named or the gtPlusPlus-only cases.
    public static boolean hasMolten(@Nullable Material material) {
        if (material == null) return false;
        if (material.hasShape(Materials2FluidShapes.fluidMolten) || storedFluid(material, FluidState.MOLTEN) != null) {
            return true;
        }
        if (isLegacyNamed(material)) return false;
        List<String> werkstoffPrefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        return werkstoffPrefixes != null && werkstoffPrefixes.contains(OrePrefixes.cellMolten.name());
    }

    /// The legacy `Materials#mStandardMoltenFluid`-backed `Materials#getMolten` stack for a material, or null
    /// when [#hasMolten] is false -- mirrors `getMolten`'s own null-on-absent behavior. The
    /// [Materials2FluidShapes#fluidMolten] Forge fluid this resolves is the same one every population's
    /// `mStandardMoltenFluid` was set from (`LegacyMaterials#build`'s `wireFluids` resolves it by
    /// [GTMaterialProperties#LEGACY_FLUIDS]'s `molten` slot name; both bridge loaders set their field from a
    /// `Material#getFluid` gated on this same shape), so once [#hasMolten] is true this is byte-identical to
    /// `getMolten`'s own stack.
    public static @Nullable FluidStack molten(@Nullable Material material, long amount) {
        Fluid stored = storedFluid(material, FluidState.MOLTEN);
        if (stored != null) return new FluidStack(stored, (int) amount);
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
        return resolveSlotFluid(material, FluidState.LIQUID, FluidNames::fluid);
    }

    /// [#fluid], for `Materials#mGas`/`Materials#getGas` -- the `gas()` slot. Wherever the former
    /// [Materials2FluidShapes#fluidGas] shape lookup succeeded this resolves the identical fluid: a material
    /// only generates that shape when it carries the slot ([Materials2FluidShapes]'s `requireRef` fails fluid
    /// registration otherwise), and the shape registers its Forge fluid under the slot's own name. The slot
    /// additionally covers materials whose legacy gas fluid never became an ML shape.
    public static @Nullable FluidStack gas(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// [#fluid], for `Materials#mPlasma`/`Materials#getPlasma` -- the `plasma()` slot.
    public static @Nullable FluidStack plasma(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.PLASMA, FluidNames::plasma);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// The legacy `Materials#hasPlasma()`/`mGeneratePlasma` flag for a material -- whether
    /// [GTMaterialProperties#GENERATION_FLAGS] carries [GTMaterialGenerationFlag#PLASMA], the exact flag
    /// `LegacyMaterials#build` feeds `MaterialBuilder#addPlasma` (which sets `mGeneratePlasma`) from. Absent
    /// flags mirror `mGeneratePlasma`'s own `false` default.
    public static boolean hasPlasma(@Nullable Material material) {
        if (material == null) return false;
        EnumSet<GTMaterialGenerationFlag> flags = material.getProperty(GTMaterialProperties.GENERATION_FLAGS);
        return flags != null && flags.contains(GTMaterialGenerationFlag.PLASMA);
    }

    /// The legacy `Materials#hasMetalItems()`/`mGenerateMetalItems` flag for a material -- whether
    /// [GTMaterialProperties#GENERATION_FLAGS] carries [GTMaterialGenerationFlag#METAL], the exact flag
    /// `LegacyMaterials#build` feeds `MaterialBuilder#addMetalItems` (which sets `mGenerateMetalItems`) from.
    /// Absent flags mirror the field's own `false` default. Backs [gregtech.api.enums.OrePrefixes#sheetmetal]'s
    /// `mCondition`, whose legacy lambda read `hasMetalItems()` off the `Materials` facade the condition can no
    /// longer see once it evaluates against a [MaterialSubTagView].
    public static boolean hasMetalItems(@Nullable Material material) {
        if (material == null) return false;
        EnumSet<GTMaterialGenerationFlag> flags = material.getProperty(GTMaterialProperties.GENERATION_FLAGS);
        return flags != null && flags.contains(GTMaterialGenerationFlag.METAL);
    }

    /// The legacy `Materials#hasGemItems()`/`mGenerateGemItems` flag for a material -- whether
    /// [GTMaterialProperties#GENERATION_FLAGS] carries [GTMaterialGenerationFlag#GEM], the exact flag
    /// `LegacyMaterials#build` feeds `MaterialBuilder#addGemItems` (which sets `mGenerateGemItems`) from.
    /// Absent flags mirror the field's own `false` default.
    public static boolean hasGemItems(@Nullable Material material) {
        if (material == null) return false;
        EnumSet<GTMaterialGenerationFlag> flags = material.getProperty(GTMaterialProperties.GENERATION_FLAGS);
        return flags != null && flags.contains(GTMaterialGenerationFlag.GEM);
    }

    /// [#fluid], for `Materials#mSolid`/`Materials#getSolid` -- the `solid()` slot.
    public static @Nullable FluidStack solid(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.SOLID, FluidNames::solid);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// A gtPlusPlus-only material's single registered legacy fluid, resolved by the name its
    /// [GTMaterialProperties#LEGACY_FLUIDS] slots declare ([FluidNames#legacyGtppFluidName]) -- unlike [#molten]/
    /// [#fluid]/[#gas], which read a legacy `Materials` field or a [Shape], neither of which a gtPlusPlus-only
    /// material carries, so those return null for exactly the materials this resolves. Mirrors the retired
    /// gtPlusPlus `Material#getFluidStack`: null exactly when gtpp itself registered no fluid for the material.
    public static @Nullable FluidStack legacyGtppFluid(@Nullable Material material, long amount) {
        Fluid fluid = legacyGtppFluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// Materials whose fluid is registered directly by name rather than through their own declaration, so
    /// [GTMaterialProperties#LEGACY_FLUIDS] never captured it.
    private static final Map<String, String> UNDECLARED_FLUID_NAMES = Map
        .of("ZirconiumTetrafluoride", "zirconiumtetrafluoride");

    /// [#legacyGtppFluid]'s raw [Fluid], for a bare `Material#getFluid()` read (no stack size).
    public static @Nullable Fluid legacyGtppFluidOf(@Nullable Material material) {
        if (material == null) return null;
        FluidNames fluids = material.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
        String name = fluids == null ? null : fluids.legacyGtppFluidName();
        if (name == null) name = UNDECLARED_FLUID_NAMES.get(material.getName());
        return name == null ? null : FluidRegistry.getFluid(name);
    }

    /// A gtPlusPlus-only material's registered plasma fluid, from [GTMaterialProperties#GTPP_PLASMA_NAME] --
    /// never from this class's own [GTMaterialProperties#LEGACY_FLUIDS]-based [#plasmaOf], so this is null for
    /// every gtPlusPlus-originated material outside the small set that property actually carries -- exactly
    /// mirroring the retired `Material#getPlasma`.
    public static @Nullable Fluid legacyGtppPlasmaOf(@Nullable Material material) {
        if (material == null) return null;
        String name = material.getProperty(GTMaterialProperties.GTPP_PLASMA_NAME);
        return name == null ? null : FluidRegistry.getFluid(name);
    }

    /// [#fluidOf]'s raw [Fluid] for the `gas()` slot -- the legacy `Materials#mGas` field value itself.
    public static @Nullable Fluid gasOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
    }

    /// [#fluidOf]'s raw [Fluid] for the `plasma()` slot -- the legacy `Materials#mPlasma` field value itself.
    public static @Nullable Fluid plasmaOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.PLASMA, FluidNames::plasma);
    }

    /// [#fluidOf]'s raw [Fluid] for the `molten()` slot -- the legacy `Materials#mStandardMoltenFluid` field
    /// value itself, resolved from the same [GTMaterialProperties#LEGACY_FLUIDS] `molten` slot (or
    /// [#recordSlotFluid] store) `LegacyMaterials#wireFluids` and `GTFluid#configureMaterials` assign the field
    /// from. Unlike [#molten], resolves the raw field rather than the [Materials2FluidShapes#fluidMolten] shape
    /// fallback, so it is null exactly when the legacy field is -- what the fluid autogen loop gates on.
    public static @Nullable Fluid moltenOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.MOLTEN, FluidNames::molten);
    }

    /// The legacy `Materials#hasCorrespondingFluid()` flag for a material, from
    /// [GTMaterialProperties#HAS_CORRESPONDING_FLUID] -- the `MaterialBuilder#addFluid` flag
    /// `LegacyMaterials#build` feeds it, mirroring the field's own `false` default when absent.
    public static boolean hasCorrespondingFluid(@Nullable Material material) {
        return material != null
            && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_FLUID));
    }

    /// [#hasCorrespondingFluid], for `Materials#hasCorrespondingGas()`/[GTMaterialProperties#HAS_CORRESPONDING_GAS].
    public static boolean hasCorrespondingGas(@Nullable Material material) {
        return material != null
            && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_GAS));
    }

    /// The five legacy fluid fields a material can carry, keying [#recordSlotFluid]'s store: `mFluid`
    /// (LIQUID), `mGas`, `mStandardMoltenFluid` (MOLTEN), `mSolid`, and `mPlasma`.
    public enum FluidState {
        LIQUID,
        GAS,
        MOLTEN,
        SOLID,
        PLASMA
    }

    private static final Map<Material, EnumMap<FluidState, Fluid>> slotFluids = new HashMap<>();

    /// Records the Forge fluid backing one of `material`'s legacy fluid states -- the [Material]-side twin of
    /// the loader-time legacy field writes (`GTFluid#configureMaterials` and `gregtech.loaders.preload.
    /// LoaderGTBlockFluid`'s direct `mFluid`/`mGas`/`mSolid` assignments), for materials whose fluids are
    /// configured at registration time rather than ported as [GTMaterialProperties#LEGACY_FLUIDS] data. A
    /// recorded fluid takes precedence over the property resolution in [#fluidOf]/[#gas]/[#molten]/[#solid]/
    /// [#plasma].
    public static void recordSlotFluid(Material material, FluidState state, Fluid fluid) {
        slotFluids.computeIfAbsent(material, k -> new EnumMap<>(FluidState.class))
            .put(state, fluid);
    }

    private static @Nullable Fluid storedFluid(@Nullable Material material, FluidState state) {
        if (material == null) return null;
        EnumMap<FluidState, Fluid> fluids = slotFluids.get(material);
        return fluids == null ? null : fluids.get(state);
    }

    /// Whether a fluid has been recorded into a material's slot store via [#recordSlotFluid] for `state` --
    /// the raw twin presence, without the [GTMaterialProperties#LEGACY_FLUIDS] fallback the public slot
    /// getters apply. Backs [gregtech.common.fluid.GTFluid]'s dump-mode already-wired guard, which must ask
    /// whether a slot was already stamped rather than whether one can be resolved.
    public static boolean hasStoredSlotFluid(@Nullable Material material, FluidState state) {
        return storedFluid(material, state) != null;
    }

    private static @Nullable Fluid resolveSlotFluid(@Nullable Material material, FluidState state,
        Function<FluidNames, FluidRef> slot) {
        Fluid stored = storedFluid(material, state);
        return stored != null ? stored : slotFluid(material, slot);
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

    /// The two cracking families a material's autogenerated cracked fluids belong to -- the legacy
    /// `Materials#hydroCrackedFluids`/`steamCrackedFluids` arrays, keyed by [#recordCrackedFluid].
    public enum CrackType {
        HYDRO,
        STEAM
    }

    private static final Map<Material, EnumMap<CrackType, Fluid[]>> crackedFluids = new HashMap<>();

    /// Records the Forge fluid `GTProxy#addAutoGeneratedHydroCrackedFluids`/`addAutoGeneratedSteamCrackedFluids`
    /// built for one of `material`'s three cracking severities (`severity` 0/1/2 = light/moderate/severe,
    /// matching the legacy array index) -- the [Material]-side twin of the facade
    /// `Materials#setHydroCrackedFluids`/`setSteamCrackedFluids` array write, for the materials whose cracked
    /// fluids GT autogenerates rather than MaterialLib pre-registering as [GTMaterialProperties#CRACKED_HYDRO_FLUIDS]/
    /// [#CRACKED_STEAM_FLUIDS] data. A recorded fluid takes precedence over that property resolution in
    /// [#crackedFluid].
    public static void recordCrackedFluid(Material material, CrackType type, int severity, Fluid fluid) {
        crackedFluids.computeIfAbsent(material, k -> new EnumMap<>(CrackType.class))
            .computeIfAbsent(type, k -> new Fluid[3])[severity] = fluid;
    }

    /// The cracked Forge fluid for `material`'s `type` cracking family at `severity` (0/1/2 =
    /// light/moderate/severe), or null when the material carries none. A [#recordCrackedFluid] autogenerated
    /// fluid wins; otherwise resolves the [GTMaterialProperties#CRACKED_HYDRO_FLUIDS]/[#CRACKED_STEAM_FLUIDS]
    /// [FluidRef] MaterialLib pre-registered, by the same Forge-fluid-name lookup `LegacyMaterials#resolveFluids`
    /// assigned the facade `Materials#getHydroCrackedFluids`/`getSteamCrackedFluids` array from -- so this is
    /// byte-identical to the legacy array element.
    public static @Nullable Fluid crackedFluid(@Nullable Material material, CrackType type, int severity) {
        if (material == null) return null;
        EnumMap<CrackType, Fluid[]> byType = crackedFluids.get(material);
        if (byType != null) {
            Fluid[] recorded = byType.get(type);
            if (recorded != null && recorded[severity] != null) return recorded[severity];
        }
        List<FluidRef> refs = material.getProperty(
            type == CrackType.HYDRO ? GTMaterialProperties.CRACKED_HYDRO_FLUIDS
                : GTMaterialProperties.CRACKED_STEAM_FLUIDS);
        if (refs == null || severity >= refs.size()) return null;
        FluidRef ref = refs.get(severity);
        return ref == null ? null : FluidRegistry.getFluid(ref.name());
    }

    /// Whether `material` belongs to the legacy name domain -- the [Material]-side existence predicate the
    /// control-flow gates use when they only need to know whether a material has a legacy [Materials]
    /// counterpart, not the counterpart object itself (the reverse-recipe, element-scan, and
    /// plasma/molten conversion loops that must skip reconstructed werkstoff/gtpp materials). Backed by
    /// [gregtech.loaders.materials.LegacyNameDomain]'s frozen membership set.
    public static boolean isLegacyNamed(@Nullable Material material) {
        return LegacyNameDomain.contains(material);
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

    /// [#craftIngredient(OrePrefixes,Material)] without the material association: the [ItemData] only names
    /// the ore-dictionary entry, so a reversible recipe derives no recycling output from this ingredient.
    /// The superconductor marker ingredients use this form -- their wires unify under the marker name but
    /// are not composed of the marker.
    public static @Nullable ItemData namedIngredient(OrePrefixes prefix, @Nullable Material material) {
        return prefix == null || material == null ? null : new ItemData(prefix, internalName(material));
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

    /// The legacy `Materials#mMoltenRGBa`-format `[r, g, b, a]` short array for a material -- the exact value
    /// the molten/plasma fluid autogen passes to `withColorRGBA`. Unpacks [GTMaterialProperties#MOLTEN_ARGB]
    /// when present (the `argbMolten` `MaterialBuilder#setMoltenARGB` was given); otherwise defaults to
    /// [#rgba] (`mMoltenRGBa` mirrors `mRGBa` when no separate molten color was set), and to the legacy field's
    /// own `{255, 255, 255, 0}` default when neither color is present. Never null, unlike [#rgba].
    public static short[] moltenRgba(@Nullable Material material) {
        Integer moltenArgb = material == null ? null : material.getProperty(GTMaterialProperties.MOLTEN_ARGB);
        if (moltenArgb == null) {
            short[] rgba = rgba(material);
            return rgba != null ? rgba : new short[] { 255, 255, 255, 0 };
        }
        return new short[] { (short) ((moltenArgb >>> 16) & 0xFF), (short) ((moltenArgb >>> 8) & 0xFF),
            (short) (moltenArgb & 0xFF), (short) ((moltenArgb >>> 24) & 0xFF) };
    }

    /// The legacy `Materials#mIconSet` texture set for a material, resolved by the same TEXTURE_SET-name lookup
    /// [Materials2Textures#iconSetOf] performs -- byte-identical for every population that reaches a MaterialLib
    /// [Material]: `LegacyMaterials#build` resolves `mIconSet` through it, and a bartworks-origin material's
    /// icon set is captured from the identical [Materials2Textures#iconSetOf] result.
    /// Null when `material` is null.
    public static @Nullable TextureSet iconSet(@Nullable Material material) {
        return material == null ? null : Materials2Textures.iconSetOf(material);
    }

    /// The legacy `Materials#mBlastFurnaceRequired` flag for a material, mirroring its own `= false` default.
    /// Ported byte-identically to [GTMaterialProperties#BLAST_REQUIRED]: `LegacyMaterials#build` sets it from
    /// this exact `Boolean.TRUE.equals` check, and a bartworks-origin material's blast-furnace requirement is
    /// computed from the identical expression against the same property.
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

    /// The legacy `Materials#canBeCracked()`/`mCanBeCracked` flag for a material, from
    /// [GTMaterialProperties#CAN_BE_CRACKED] -- absent mirrors `mCanBeCracked`'s own `false` default.
    /// `LegacyMaterials#build` feeds this exact property through `MaterialBuilder#addCrackingRecipes` (which
    /// sets `mCanBeCracked`) when present.
    public static boolean canBeCracked(@Nullable Material material) {
        return material != null && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.CAN_BE_CRACKED));
    }

    /// The legacy `Materials#mColor` [Dyes] for a material, from [GTMaterialProperties#DYE] -- or [Dyes#_NULL]
    /// when absent, mirroring `mColor`'s own default (and, since [Dyes#_NULL]'s name never matches a real lens
    /// ore-dict suffix, its practical never-generates behavior).
    public static Dyes dye(@Nullable Material material) {
        if (material == null) return Dyes._NULL;
        String dye = material.getProperty(GTMaterialProperties.DYE);
        return dye == null ? nearestDye(material) : Dyes.valueOf(dye);
    }

    /// The vanilla [Dyes] nearest a material's [GTMaterialProperties#ARGB] by squared RGB distance, or
    /// [Dyes#_NULL] when it has no color. Used as the [#dye] fallback for gem materials carrying no explicit
    /// `DYE` property (all werkstoff-derived gems reach GT this way) so their laser-engraver upgrade recipes and
    /// lens-gem oredict registration -- both keyed on `craftingLens<Dye>` -- still resolve a color, reproducing
    /// what bartworks classified from the werkstoff RGBA at recipe-registration time.
    private static Dyes nearestDye(@Nullable Material material) {
        short[] rgba = rgba(material);
        if (rgba == null) return Dyes._NULL;
        Dyes best = Dyes._NULL;
        long bestDistance = Long.MAX_VALUE;
        for (int i = 0; i <= 15; i++) {
            Dyes candidate = Dyes.get(i);
            short[] c = candidate.getRGBA();
            long dr = rgba[0] - c[0], dg = rgba[1] - c[1], db = rgba[2] - c[2];
            long distance = dr * dr + dg * dg + db * db;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
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

    /// The legacy `Materials#mAutoGenerateRecycleRecipes` flag for a material, from
    /// [GTMaterialProperties#AUTO_RECYCLE_RECIPES] -- `true` when absent, mirroring the field's own default
    /// (`LegacyMaterials#build` only overrides it when the property is present).
    /// [GTMaterialProperties#AUTO_RECYCLE_RECIPES] is a gregtech-dump-only property, so any material carrying
    /// it also has a legacy [Materials] counterpart;
    /// a reverse-recipe gate that skipped only counterpart-bearing materials with the flag unset therefore
    /// stays byte-identical when it reads this predicate directly.
    public static boolean autoGenerateRecycleRecipes(@Nullable Material material) {
        if (material == null) return true;
        Boolean value = material.getProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES);
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

    /// The legacy `Materials#getGasTemperature()` value for a material: room temperature (295 K) when
    /// [GTMaterialProperties#GAS_TEMP] is unset or zero, otherwise the material's [#meltingPoint] -- the
    /// legacy accessor reads `mMeltingPoint`, not `mGasTemp`, whenever the gas temperature is set
    /// (`Materials#getGasTemperature`), and the fluid registration temperatures built from it depend on that
    /// exact value.
    public static int gasTemperature(@Nullable Material material) {
        Integer gasTemp = material == null ? null : material.getProperty(GTMaterialProperties.GAS_TEMP);
        return gasTemp == null || gasTemp == 0 ? 295 : meltingPoint(material);
    }

    /// The legacy `Materials#getLiquidTemperature()` value for a material: room temperature (295 K) when its
    /// [#meltingPoint] is zero, otherwise the melting point -- the exact `mMeltingPoint == 0 ? 295 :
    /// mMeltingPoint` the corresponding-fluid autogen registers its fluid at.
    public static int liquidTemperature(@Nullable Material material) {
        int meltingPoint = meltingPoint(material);
        return meltingPoint == 0 ? 295 : meltingPoint;
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

    /// The legacy `Materials#getProcessingMaterialTierEU()` value for a material, or `0` if unset -- mirrors
    /// `Materials#processingMaterialTierEU`'s own default. Ported byte-identically to
    /// [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU]: `LegacyMaterials.build` and the gtpp bridge loader
    /// (`GtppBridgeMaterialsLoader`) feed this exact property through `setProcessingMaterialTierEU` when
    /// present, and otherwise leave the `0` default every population shares.
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

    /// The legacy `Materials#getToolEnchantment()`/`mToolEnchantment` for a material -- the [Enchantment]
    /// named by [GTMaterialProperties#TOOL_ENCHANTMENT], resolved by the same unlocalized-name lookup
    /// `LegacyMaterials#findEnchantment` performs against `Enchantment#enchantmentsList` (the property stores
    /// the `Enchantment#getName` string `MaterialDataDump` captured). Null when the material has no tool
    /// enchantment, mirroring `mToolEnchantment`'s own null default.
    public static @Nullable Enchantment getToolEnchantment(@Nullable Material material) {
        if (material == null) return null;
        return findEnchantment(material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT));
    }

    /// The legacy `Materials#getToolEnchantmentLevel()`/`mToolEnchantmentLevel` for a material, from
    /// [GTMaterialProperties#TOOL_ENCHANTMENT_LEVEL]. `LegacyMaterials#build` reads the level only alongside a
    /// [GTMaterialProperties#TOOL_ENCHANTMENT] name (defaulting `1` there), leaving `MaterialBuilder`'s own
    /// `1` default when no enchantment is set -- so this returns `1` whenever the name is absent, matching the
    /// field every material carries.
    public static int getToolEnchantmentLevel(@Nullable Material material) {
        if (material == null || material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT) == null) return 1;
        Integer level = material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT_LEVEL);
        return level == null ? 1 : level;
    }

    /// [#getToolEnchantment], for `Materials#getArmorEnchantment()`/`mArmorEnchantment` /
    /// [GTMaterialProperties#ARMOR_ENCHANTMENT].
    public static @Nullable Enchantment getArmorEnchantment(@Nullable Material material) {
        if (material == null) return null;
        return findEnchantment(material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT));
    }

    /// [#getToolEnchantmentLevel], for `Materials#getArmorEnchantmentLevel()`/`mArmorEnchantmentLevel` /
    /// [GTMaterialProperties#ARMOR_ENCHANTMENT_LEVEL].
    public static int getArmorEnchantmentLevel(@Nullable Material material) {
        if (material == null || material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT) == null) return 1;
        Integer level = material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT_LEVEL);
        return level == null ? 1 : level;
    }

    private static @Nullable Enchantment findEnchantment(@Nullable String unlocalizedName) {
        if (unlocalizedName == null) return null;
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment != null && unlocalizedName.equals(enchantment.getName())) return enchantment;
        }
        return null;
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

    /// The legacy `Materials#mFuelType` `MaterialBuilder.FuelType` ordinal for a material, or `0`
    /// (`MaterialBuilder.FuelType#Diesel`) if unset -- mirrors `MaterialBuilder`'s own default, see
    /// [#fuelPower(Material)].
    public static int fuelType(@Nullable Material material) {
        if (material == null) return 0;
        Integer fuelType = material.getProperty(GTMaterialProperties.FUEL_TYPE);
        return fuelType == null ? 0 : fuelType;
    }

    /// The smelting target for a material, resolved from [GTMaterialProperties#SMELT_INTO]: an unset property
    /// means the material smelts into itself, and a set one is chased one more hop through the target's own
    /// property (the legacy `setSmeltingInto` indirection).
    public static @Nullable Material smeltInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.SMELT_INTO);
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#MACERATE_INTO].
    public static @Nullable Material macerateInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.MACERATE_INTO);
    }

    /// The legacy `Materials#mMaterialInto` unification target for a material: the material itself.
    /// `mMaterialInto` is a proven universal self-reference -- every `Materials` constructor assigns `this`
    /// and nothing else ever writes it -- so no property backs it and this returns its argument, preserving
    /// that self-reference default.
    public static @Nullable Material materialInto(@Nullable Material material) {
        return material;
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#ARC_SMELT_INTO].
    public static @Nullable Material arcSmeltInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.ARC_SMELT_INTO);
    }

    /// The legacy `Materials#mArcSmeltIntoWithGas` gas-conditional arc-smelting mapping for a material
    /// (gas -> result), from [Materials2ArcSmelting]'s declared table; empty when the material has none.
    public static Map<Material, Material> arcSmeltIntoWithGas(@Nullable Material material) {
        return Materials2ArcSmelting.withGas(material);
    }

    private static Map<Material, List<Material>> oreReRegistrations;

    /// The wildcard marker materials an ore registered for `material` is also re-registered under -- the
    /// [Material]-side replacement for the legacy `Materials#mOreReRegistrations` wildcard wiring (an Iron ore
    /// entry also registers as AnyIron, etc.), consulted by the ore-registration dispatch. Empty for a
    /// material with no wildcard aliases.
    public static List<Material> oreReRegistrationsOf(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        return oreReRegistrations().getOrDefault(material, Collections.emptyList());
    }

    private static Map<Material, List<Material>> oreReRegistrations() {
        if (oreReRegistrations == null) {
            Map<Material, List<Material>> m = new HashMap<>();
            m.put(Materials2Materials.Iron, Collections.singletonList(Materials2Markers.AnyIron));
            m.put(Materials2Materials.PigIron, Collections.singletonList(Materials2Markers.AnyIron));
            m.put(Materials2Materials.CastIron, Collections.singletonList(Materials2Markers.AnyIron));
            m.put(Materials2Materials.Copper, Collections.singletonList(Materials2Markers.AnyCopper));
            m.put(Materials2Materials.AnnealedCopper, Collections.singletonList(Materials2Markers.AnyCopper));
            m.put(Materials2Materials.Bronze, Collections.singletonList(Materials2Markers.AnyBronze));
            m.put(Materials2Materials.Rubber, Collections.singletonList(Materials2Markers.AnyRubber));
            m.put(
                Materials2Materials.StyreneButadieneRubber,
                Arrays.asList(Materials2Markers.AnyRubber, Materials2Markers.AnySyntheticRubber));
            m.put(
                Materials2Materials.Silicone,
                Arrays.asList(Materials2Markers.AnyRubber, Materials2Markers.AnySyntheticRubber));
            m.put(Materials2Materials.Carbon, Collections.singletonList(Materials2Markers.AnyCarbon));
            m.put(Materials2Materials.Coal, Collections.singletonList(Materials2Markers.AnyCarbon));
            m.put(Materials2Materials.Charcoal, Collections.singletonList(Materials2Markers.AnyCarbon));
            m.put(Materials2Materials.Lignite, Collections.singletonList(Materials2Markers.AnyCarbon));
            oreReRegistrations = m;
        }
        return oreReRegistrations;
    }

    /// [#smeltInto(Material)], for [GTMaterialProperties#DIRECT_SMELTING].
    public static @Nullable Material directSmelting(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.DIRECT_SMELTING);
    }

    /// The tool-handle material a material's part recipes pair it with, defaulting to the material itself. A
    /// material outside the legacy name domain resolves the [#recordHandleMaterial] override its bridge loader
    /// pushed; [GTMaterialProperties#HANDLE_MATERIAL] is the fallback, one hop only -- the property never chains
    /// through another material's own handle, so there is nothing further to chase.
    public static @Nullable Material handleMaterial(@Nullable Material material) {
        if (material == null) return null;
        Material override = reconstructedHandles.get(material);
        if (override != null) return override;
        MaterialRef ref = material.getProperty(GTMaterialProperties.HANDLE_MATERIAL);
        if (ref == null) return material;
        Material resolved = ref.resolve();
        return resolved != null ? resolved : material;
    }

    private static final Map<Material, Material> reconstructedHandles = new HashMap<>();

    /// Records the tool-handle material `material`'s retired bartworks bridge facade would have carried in
    /// `Materials#mHandleMaterial` (see [#handleMaterial(Material)]). Called by `LoaderWerkstoffRegistrations`
    /// at the exact point in loading the facade write would have happened.
    public static void recordHandleMaterial(Material material, Material handle) {
        reconstructedHandles.put(material, handle);
    }

    private static final java.util.Set<Material> reconstructedBridgeRegistrations = new java.util.HashSet<>();

    /// Records that `material`'s retired bartworks bridge facade would have entered `Materials#getMaterialsMap`
    /// by this point in loading. Called by `LoaderWerkstoffRegistrations` at the exact point in loading the
    /// facade would have minted the material.
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
    /// werkstoff facade's raw SubTag list, captured separately from FLAGS -- see that property's
    /// javadoc) names `flag`: a werkstoff-backed material's FLAGS reflects only what its dumped bridge
    /// `Materials` carried, which never included the bartworks material's own SubTags, so this reads SUB_TAGS as a
    /// second source for the same 1:1 name mapping FLAGS already uses. Mirrors legacy
    /// `Materials#contains(SubTag)`/`mSubTags`.
    public static boolean hasFlag(@Nullable Material material, GTMaterialFlag flag) {
        if (material == null) return false;
        EnumSet<GTMaterialFlag> flags = material.getProperty(GTMaterialProperties.FLAGS);
        if (flags != null && flags.contains(flag)) return true;
        List<String> subTags = material.getProperty(GTMaterialProperties.SUB_TAGS);
        return subTags != null && subTags.contains(flag.name());
    }

    /// Whether a material's [GTMaterialProperties#SUB_TAGS] names `subTag`. For the werkstoff SubTags that have
    /// no [GTMaterialFlag] counterpart (`"NoBlast"`, `"AnaerobeSmelting"`, `"NobleGasSmelting"`) and so cannot be
    /// reached through [#hasFlag].
    public static boolean hasSubTag(@Nullable Material material, String subTag) {
        if (material == null) return false;
        List<String> subTags = material.getProperty(GTMaterialProperties.SUB_TAGS);
        return subTags != null && subTags.contains(subTag);
    }

    /// The legacy internal name of a MaterialLib material -- [GTMaterialProperties#LEGACY_NAME] when present
    /// (MaterialLib sanitizes registration names), otherwise the registration name. The [Material]-side
    /// equivalent of `Materials#getInternalName`, used to build ore-dictionary names and lang keys.
    public static String internalName(Material material) {
        String legacyName = material.getProperty(GTMaterialProperties.LEGACY_NAME);
        return legacyName != null ? legacyName : material.getName();
    }

    /// The exact legacy `Materials#mName` string for a material -- an alias for [#internalName], which already
    /// resolves the [GTMaterialProperties#LEGACY_NAME] override before the registration name.
    public static String legacyName(Material material) {
        return internalName(material);
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

    // Union dispatch helpers: the transitional two-family union (Materials / bartworks-origin materials, plus
    // the recognition markers) previously flowed through legacy-interface-typed slots; those slots are now
    // Object-typed (or MaterialLib Material-typed) and these helpers reproduce the exact per-family member
    // behavior at the read sites. TRANSITIONAL -- each dies with its last union call site.

    /// The legacy internal name of a [Material] (see [#internalName]); null for null or a non-[Material] value.
    public static @Nullable String internalNameOf(@Nullable Object material) {
        if (material instanceof Material ml) return internalName(ml);
        return null;
    }

    /// The lang key for a [Material]'s localized name; null for null or a non-[Material] value.
    public static @Nullable String localizedNameKeyOf(@Nullable Object material) {
        if (material instanceof Material ml) return "Material." + internalName(ml).toLowerCase();
        return null;
    }

    /// The localized display name of a [Material], translated from its [#localizedNameKeyOf] lang key; null for
    /// null or a non-[Material] value.
    public static @Nullable String localizedNameOf(@Nullable Object material) {
        if (material instanceof Material ml) {
            return StatCollector.translateToLocal("Material." + internalName(ml).toLowerCase());
        }
        return null;
    }

    /// The default display name of a [Material] (see [#localName]); null for null or a non-[Material] value.
    public static @Nullable String defaultLocalNameOf(@Nullable Object material) {
        if (material instanceof Material ml) return localName(ml);
        return null;
    }

    /// The [TextureSet] of a [Material] (see [#iconSet]); null for null or a non-[Material] value.
    public static @Nullable TextureSet textureSetOf(@Nullable Object material) {
        if (material instanceof Material ml) return iconSet(ml);
        return null;
    }

    /// The `[r, g, b, a]` color of a [Material] (see [#rgba]); null for null or a non-[Material] value.
    public static @Nullable short[] rgbaOf(@Nullable Object material) {
        if (material instanceof Material ml) return rgba(ml);
        return null;
    }

    /// The stone types a [Material]'s ore can generate in -- [StoneType#ICES] when it carries
    /// [GTMaterialFlag#ICE_ORE], otherwise [StoneType#STONES]. Empty for null or a non-[Material] value.
    public static List<IStoneType> validStonesOf(@Nullable Object material) {
        if (material instanceof Material ml) {
            return hasFlag(ml, GTMaterialFlag.ICE_ORE) ? StoneType.ICES : StoneType.STONES;
        }
        return Collections.emptyList();
    }

    /// Whether a [Material] generates `prefix` -- either through gregtech's own part autogen (see
    /// [OrePrefixes#doGenerateItem(Material)]) or the werkstoff part set (see
    /// [Materials2WerkstoffIndex#generatesPrefix]). False for null or a non-[Material] value.
    public static boolean generatesPrefix(@Nullable Object material, OrePrefixes prefix) {
        if (material instanceof Material ml) {
            return prefix.doGenerateItem(ml) || Materials2WerkstoffIndex.generatesPrefix(ml, prefix);
        }
        return false;
    }

    /// Whether a [Material] carries the [GTMaterialFlag] equivalent of a legacy [SubTag] (see
    /// [#flagForSubTag]); false for null or a non-[Material] value.
    public static boolean hasSubTag(@Nullable Object material, SubTag subTag) {
        if (material instanceof Material ml) {
            GTMaterialFlag flag = flagForSubTag(subTag);
            return flag != null && hasFlag(ml, flag);
        }
        return false;
    }

    /// The ore-dictionary-unified [ItemStack] for a [Material] at `prefix` and `amount` (see
    /// [GTOreDictUnificator#get]); null for null or a non-[Material] value.
    public static @Nullable ItemStack partOf(@Nullable Object material, OrePrefixes prefix, int amount) {
        if (material instanceof Material ml) return GTOreDictUnificator.get(prefix, ml, amount);
        return null;
    }

    private static final Map<Material, GeneratedMaterialRenderer> materialRenderers = new HashMap<>();

    /// Registers `renderer` as the special item renderer for `material`, keyed by the MaterialLib [Material].
    /// Mirrors the client-side renderer assignments in `Materials#initClient`; populated once from the client
    /// proxy (`GTClient#onPreInitialization`) after `initClient` has constructed the renderer instances, so the
    /// GT-owned store, `MaterialLibClient`'s registry, and the legacy `Materials#renderer` field all hold the
    /// same instances.
    public static void recordRenderer(Material material, GeneratedMaterialRenderer renderer) {
        materialRenderers.put(material, renderer);
    }

    /// The [GeneratedMaterialRenderer] [#recordRenderer] registered for `material`, or null when it has no
    /// special renderer. The [Material]-keyed replacement for the legacy `Materials#getRenderer`/`renderer`
    /// facade read that the generated-item, fluid-display, and electrode renderers used.
    public static @Nullable GeneratedMaterialRenderer rendererOf(@Nullable Material material) {
        return material == null ? null : materialRenderers.get(material);
    }

    /// The block-form metadata index of a [Material] (see [#oldSubId]); `0` for null or a non-[Material] value.
    public static int idOf(@Nullable Object material) {
        if (material instanceof Material ml) return oldSubId(ml);
        return 0;
    }

    /// Appends a [Material]'s chemical-formula tooltip (see [#chemicalTooltip]) to `list` when
    /// `Client.tooltip.showFormula` is enabled; a no-op for null or a non-[Material] value.
    public static void addTooltipsOf(@Nullable Object material, List<String> list) {
        if (material instanceof Material ml && Client.tooltip.showFormula) {
            String tooltip = chemicalTooltip(ml, false);
            if (tooltip != null && !tooltip.isEmpty()) list.add(tooltip);
        }
    }

    /// The [GTMaterialFlag] whose enum-constant name equals `subTag`'s name, or null when none does -- the
    /// [Material]-side lookup [#hasSubTag] uses to translate a legacy [SubTag] query into the flag its [Material]
    /// carries.
    private static @Nullable GTMaterialFlag flagForSubTag(SubTag subTag) {
        // Inverts LegacyMaterials#legacySubTagName, whose two flags carry a name no GTMaterialFlag constant spells.
        switch (subTag.mName) {
            case "AnaerobeGas":
                return GTMaterialFlag.ANAEROBE_GAS;
            case "NobleGas":
                return GTMaterialFlag.NOBLE_GAS;
            default:
                break;
        }
        try {
            return GTMaterialFlag.valueOf(subTag.mName);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
