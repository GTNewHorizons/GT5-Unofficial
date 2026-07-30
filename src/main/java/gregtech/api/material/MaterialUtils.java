package gregtech.api.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import gregtech.api.enums.materials2.Shapes;
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
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.MaterialFluidNames;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Textures;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.MaterialStack;
import gregtech.common.config.Client;

/// GregTech's read layer over a MaterialLib [Material]: the accessors for everything GregTech attaches to a
/// material through [GTMaterialProperties], grouped by domain (fluids, ores, tools, names, flags, colour).
///
/// Every accessor tolerates a null material and answers with the same value an unset property would give, so
/// callers reading a material resolved from block metadata or item NBT -- which can legitimately miss -- need
/// no guard of their own. Values whose default is a plain constant are declared on the [Property] key itself
/// (see [gregtech.api.material.GTMaterialProperties]); the accessors here carry only the null-material case and
/// the defaults a constant cannot express.
///
/// Computation lives in the purpose-built classes this delegates to -- [MaterialAtomics], [MaterialFormulas],
/// [Materials2Textures] -- which are implementation detail; this is the surface callers use.
public class MaterialUtils {

    private MaterialUtils() {}

    /// The MaterialLib material whose [#internalName] is `name`, or null on a miss. Deliberately wider than
    /// [gregtech.loaders.materials.LegacyNameDomain#lookup], whose frozen table does not cover every material
    /// whose registration name MaterialLib had to sanitize.
    public static @Nullable Material byLegacyName(@Nullable String name) {
        if (name == null) return null;
        if (legacyNameToMaterial == null) {
            Map<String, Material> map = new HashMap<>();
            for (Material material : MaterialLibAPI.getMaterials()) {
                if ("gregtech".equals(material.getModId())) map.putIfAbsent(internalName(material), material);
            }
            legacyNameToMaterial = map;
        }
        Material found = legacyNameToMaterial.get(name);
        return found != null ? found : MaterialLibAPI.getMaterial("gregtech", name);
    }

    private static Map<String, Material> legacyNameToMaterial;

    /// The dust [ItemStack] a [GTMaterialProperties#COMPOSITION] entry contributes to a recipe, sized by the
    /// entry's amount, or null when the referenced material carries no `dust` shape (a gas/fluid-only
    /// component -- see [#compositionGas]) or fails to resolve. A composition entry always names a MaterialLib
    /// material directly ([MaterialRef#resolve]), so unlike [#stack] this needs no bartworks fallback.
    public static @Nullable ItemStack compositionDust(MaterialRefStack entry) {
        Material material = entry.material()
            .resolve();
        if (material == null || !material.hasShape(Shapes.dust)) return null;
        return MaterialLibAPI.getStack(material, Shapes.dust, (int) entry.amount());
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

    /// Whether a material has a resolvable molten fluid (see [#molten]), for callers needing the presence
    /// check independent of a fluid amount, such as one gate guarding several [#molten] calls of different
    /// amounts. True when the material carries the [Materials2FluidShapes#fluidMolten] shape or a
    /// [#recordSlotFluid]-stored MOLTEN fluid, which `GTFluid#configureMaterials` writes for the materials
    /// excluded from the shape-backed fluids.
    public static boolean hasMolten(@Nullable Material material) {
        if (material == null) return false;
        return material.hasShape(Materials2FluidShapes.fluidMolten) || storedFluid(material, FluidState.MOLTEN) != null;
    }

    /// The molten fluid stack for a material at `amount`, or null when [#hasMolten] is false. A
    /// [#recordSlotFluid]-stored MOLTEN fluid takes precedence; otherwise resolves the
    /// [Materials2FluidShapes#fluidMolten] MaterialLib shape stack directly.
    public static @Nullable FluidStack molten(@Nullable Material material, long amount) {
        Fluid stored = storedFluid(material, FluidState.MOLTEN);
        if (stored != null) return new FluidStack(stored, (int) amount);
        if (!hasMolten(material)) return null;
        return MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) amount);
    }

    /// [#fluidOf], as a sized [FluidStack]; null when the material carries no fluid slot.
    public static @Nullable FluidStack fluid(@Nullable Material material, long amount) {
        Fluid fluid = fluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// The raw [Fluid] for a material's liquid slot, for presence gates and callers building their own
    /// stacks. A [#recordSlotFluid]-stored LIQUID fluid takes precedence; otherwise resolved from
    /// [MaterialFluidNames]'s `fluid()` slot by Forge fluid name -- NOT from a
    /// [Materials2FluidShapes#fluidLiquid] shape lookup, since a material whose fluid is a vanilla or
    /// foreign Forge fluid (`Water` -> `water`, `Milk` -> `milk`) carries the slot without any ML fluid
    /// shape, which a shape-based lookup would miss.
    public static @Nullable Fluid fluidOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.LIQUID, FluidNames::fluid);
    }

    /// [#fluid], for a material's `gas()` slot; null when it carries none. Agrees with any
    /// [Materials2FluidShapes#fluidGas] shape the material generates -- that shape only registers when the
    /// material carries the slot ([Materials2FluidShapes]'s `requireRef` fails fluid registration
    /// otherwise), under the slot's own Forge fluid name -- but also resolves gas fluids that never became
    /// an ML shape.
    public static @Nullable FluidStack gas(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// [#fluid], for a material's `plasma()` slot.
    public static @Nullable FluidStack plasma(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.PLASMA, FluidNames::plasma);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// Whether a material's [GTMaterialProperties#GENERATION_FLAGS] carry `flag`, i.e. whether it generates
    /// that item set. False when the property is absent or when `material` is null.
    public static boolean generates(@Nullable Material material, GTMaterialGenerationFlag flag) {
        if (material == null) return false;
        EnumSet<GTMaterialGenerationFlag> flags = material.getProperty(GTMaterialProperties.GENERATION_FLAGS);
        return flags != null && flags.contains(flag);
    }

    /// [#fluid], for a material's `solid()` slot.
    public static @Nullable FluidStack solid(@Nullable Material material, long amount) {
        Fluid fluid = storedFluid(material, FluidState.SOLID);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// A gtPlusPlus-originated material's single fluid, whichever state backs it. Unlike
    /// [#molten]/[#fluid]/[#gas], which each answer for one state, this takes the first the material has, so
    /// callers porting a gtPlusPlus recipe do not have to know which state the material ended up in. Null when
    /// the material has no fluid at all.
    public static @Nullable FluidStack legacyGtppFluid(@Nullable Material material, long amount) {
        Fluid fluid = legacyGtppFluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// The shapes [#legacyGtppFluidOf] tries, in the order the legacy gtPlusPlus fluid name was derived:
    /// molten first, then liquid, then gas.
    private static final Shape[] GTPP_FLUID_SHAPES = { Materials2FluidShapes.fluidMolten,
        Materials2FluidShapes.fluidLiquid, Materials2FluidShapes.fluidGas };

    /// Materials whose fluid is registered directly by name rather than through a shape of their own, so no
    /// shape lookup can reach it.
    private static final Map<String, String> UNDECLARED_FLUID_NAMES = Map
        .of("ZirconiumTetrafluoride", "zirconiumtetrafluoride");

    /// [#legacyGtppFluid]'s raw [Fluid], for a bare fluid read with no stack size.
    public static @Nullable Fluid legacyGtppFluidOf(@Nullable Material material) {
        if (material == null) return null;
        for (Shape shape : GTPP_FLUID_SHAPES) {
            Fluid fluid = MaterialLibAPI.getFluid(material, shape);
            if (fluid != null) return fluid;
        }
        String name = UNDECLARED_FLUID_NAMES.get(material.getName());
        return name == null ? null : FluidRegistry.getFluid(name);
    }

    /// A gtPlusPlus-only material's registered plasma fluid, from [GTMaterialProperties#GTPP_PLASMA_NAME] --
    /// never from this class's own [MaterialFluidNames]-based [#plasmaOf], so this is
    /// null for
    /// every gtPlusPlus-originated material outside the small set that property actually carries -- exactly
    /// mirroring the retired `Material#getPlasma`.
    public static @Nullable Fluid legacyGtppPlasmaOf(@Nullable Material material) {
        if (material == null) return null;
        String name = material.getProperty(GTMaterialProperties.GTPP_PLASMA_NAME);
        return name == null ? null : FluidRegistry.getFluid(name);
    }

    /// [#fluidOf]'s raw [Fluid] for the `gas()` slot.
    public static @Nullable Fluid gasOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
    }

    /// [#fluidOf]'s raw [Fluid] for the `plasma()` slot.
    public static @Nullable Fluid plasmaOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.PLASMA, FluidNames::plasma);
    }

    /// [#fluidOf]'s raw [Fluid] for the `molten()` slot, resolved from the same
    /// [MaterialFluidNames] `molten` slot (or [#recordSlotFluid] store) as [#molten].
    /// Unlike
    /// [#molten], does not fall back to the [Materials2FluidShapes#fluidMolten] shape lookup -- this is null
    /// exactly when the slot itself is unset, which the fluid autogen loop gates on.
    public static @Nullable Fluid moltenOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.MOLTEN, FluidNames::molten);
    }

    /// Whether a material has a corresponding registered fluid -- [GTMaterialProperties#HAS_CORRESPONDING_FLUID],
    /// `false` when absent.
    public static boolean hasCorrespondingFluid(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_FLUID);
    }

    /// [#hasCorrespondingFluid], for [GTMaterialProperties#HAS_CORRESPONDING_GAS].
    public static boolean hasCorrespondingGas(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_GAS);
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
    /// configured at registration time rather than ported as [MaterialFluidNames] data.
    /// A
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

    /// The shapes that can back each fluid state, in resolution order. A state usually maps to its own shape,
    /// but a material declaring both a liquid and a gas registers only one MaterialLib fluid when the two
    /// share a Forge fluid name, so each of those two states falls back to the other's shape.
    private static final EnumMap<FluidState, Shape[]> STATE_SHAPES = new EnumMap<>(
        Map.of(
            FluidState.LIQUID,
            new Shape[] { Materials2FluidShapes.fluidLiquid, Materials2FluidShapes.fluidGas },
            FluidState.GAS,
            new Shape[] { Materials2FluidShapes.fluidGas, Materials2FluidShapes.fluidLiquid },
            FluidState.MOLTEN,
            new Shape[] { Materials2FluidShapes.fluidMolten },
            FluidState.PLASMA,
            new Shape[] { Materials2FluidShapes.fluidPlasma },
            FluidState.SOLID,
            new Shape[] {}));

    /// A [#recordSlotFluid]-stored fluid wins; otherwise the material must declare the
    /// [MaterialFluidNames] slot, and the fluid is the one MaterialLib registered for
    /// the
    /// first shape in [#STATE_SHAPES] that the material generates. The slot gate matters independently of the
    /// shapes: it is what distinguishes a material that has no fluid in this state from one whose fluid shares
    /// another state's registration.
    private static @Nullable Fluid resolveSlotFluid(@Nullable Material material, FluidState state,
        Function<FluidNames, FluidRef> slot) {
        Fluid stored = storedFluid(material, state);
        if (stored != null) return stored;
        if (material == null) return null;
        FluidNames legacyFluids = MaterialFluidNames.of(material.getName());
        if (legacyFluids == null || slot.apply(legacyFluids) == null) return null;
        for (Shape shape : STATE_SHAPES.get(state)) {
            Fluid fluid = MaterialLibAPI.getFluid(material, shape);
            if (fluid != null) return fluid;
        }
        return null;
    }

    private static final Map<Fluid, Material> fluidMaterials = new LinkedHashMap<>();

    /// Records that `fluid` was registered for `material`. Written from [gregtech.common.fluid.GTFluid]'s
    /// material-slot configuration and [gregtech.loaders.preload.LoaderGTBlockFluid]'s direct water
    /// registration -- see [#materialOfFluid] for the read side.
    public static void recordFluidMaterial(Fluid fluid, Material material) {
        fluidMaterials.put(fluid, material);
    }

    /// The material [#recordFluidMaterial] recorded for a fluid, or null when none owns it.
    public static @Nullable Material materialOfFluid(@Nullable Fluid fluid) {
        return fluid == null ? null : fluidMaterials.get(fluid);
    }

    /// The two cracking families a material's autogenerated cracked fluids belong to, keyed by
    /// [#recordCrackedFluid].
    public enum CrackType {
        HYDRO,
        STEAM
    }

    private static final Map<Material, EnumMap<CrackType, Fluid[]>> crackedFluids = new HashMap<>();

    /// Records the Forge fluid `GTProxy#addAutoGeneratedHydroCrackedFluids`/`addAutoGeneratedSteamCrackedFluids`
    /// built for one of `material`'s three cracking severities (`severity` 0/1/2 = light/moderate/severe),
    /// for materials whose cracked fluids GT autogenerates rather than MaterialLib pre-registering as
    /// [MaterialFluidNames] data. A recorded fluid takes
    /// precedence over that property resolution in [#crackedFluid].
    public static void recordCrackedFluid(Material material, CrackType type, int severity, Fluid fluid) {
        crackedFluids.computeIfAbsent(material, k -> new EnumMap<>(CrackType.class))
            .computeIfAbsent(type, k -> new Fluid[3])[severity] = fluid;
    }

    /// The six cracked-fluid shapes, indexed by [CrackType] then by severity.
    private static final EnumMap<CrackType, Shape[]> CRACKED_SHAPES = new EnumMap<>(
        Map.of(
            CrackType.HYDRO,
            new Shape[] { Materials2FluidShapes.fluidHydroCracked1, Materials2FluidShapes.fluidHydroCracked2,
                Materials2FluidShapes.fluidHydroCracked3 },
            CrackType.STEAM,
            new Shape[] { Materials2FluidShapes.fluidSteamCracked1, Materials2FluidShapes.fluidSteamCracked2,
                Materials2FluidShapes.fluidSteamCracked3 }));

    /// The cracked Forge fluid for `material`'s `type` cracking family at `severity` (0/1/2 =
    /// light/moderate/severe), or null when the material carries none. A [#recordCrackedFluid] autogenerated
    /// fluid wins; otherwise the fluid MaterialLib registered for the matching cracked shape.
    public static @Nullable Fluid crackedFluid(@Nullable Material material, CrackType type, int severity) {
        if (material == null) return null;
        EnumMap<CrackType, Fluid[]> byType = crackedFluids.get(material);
        if (byType != null) {
            Fluid[] recorded = byType.get(type);
            if (recorded != null && recorded[severity] != null) return recorded[severity];
        }
        return MaterialLibAPI.getFluid(material, CRACKED_SHAPES.get(type)[severity]);
    }

    /// The block-form metadata index a material was assigned (e.g. the frame and storage-block variant
    /// selector), from [GTMaterialProperties#OLD_SUB_ID], or -1 if unset. Callers reading block-form
    /// metadata (frame tiers, worldgen) use this accessor.
    /// `-1` is this accessor's own sentinel, not a [GTMaterialProperties#OLD_SUB_ID] default: absence is
    /// meaningful for that key, and [gregtech.api.enums.materials2.Materials2IDIndex] and
    /// [Materials2WerkstoffIndex#generatesPrefix] both branch on it.
    public static int oldSubId(@Nullable Material material) {
        if (material == null) return -1;
        Integer id = material.getProperty(GTMaterialProperties.OLD_SUB_ID);
        return id == null ? -1 : id;
    }

    /// The `[r, g, b, a]` short array for a material's [GTMaterialProperties#ARGB] color, or null if it has
    /// no ARGB property set (unported markers). Unpacks the packed int as `(argb >>> 16) & 0xFF` /
    /// `(argb >>> 8) & 0xFF` / `(argb) & 0xFF` / `(argb >>> 24) & 0xFF` for r/g/b/a respectively. Unlike
    /// [com.ruling_0.materiallib.api.StandardProperties#TINT], preserves alpha `0x00` (see
    /// [GTMaterialProperties#ARGB]'s javadoc) -- do not substitute this for TINT in ML-side rendering code.
    public static @Nullable short[] rgba(@Nullable Material material) {
        if (material == null) return null;
        Integer argb = material.getProperty(GTMaterialProperties.ARGB);
        if (argb == null) return null;
        return new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
    }

    /// The `[r, g, b, a]` short array for a material's molten/plasma fluid color -- the value the fluid
    /// autogen passes to `withColorRGBA`. Unpacks [GTMaterialProperties#MOLTEN_ARGB] when present;
    /// otherwise falls back to [#rgba], and to `{255, 255, 255, 0}` when neither is present. Never null,
    /// unlike [#rgba].
    public static short[] moltenRgba(@Nullable Material material) {
        Integer moltenArgb = material == null ? null : material.getProperty(GTMaterialProperties.MOLTEN_ARGB);
        if (moltenArgb == null) {
            short[] rgba = rgba(material);
            return rgba != null ? rgba : new short[] { 255, 255, 255, 0 };
        }
        return new short[] { (short) ((moltenArgb >>> 16) & 0xFF), (short) ((moltenArgb >>> 8) & 0xFF),
            (short) (moltenArgb & 0xFF), (short) ((moltenArgb >>> 24) & 0xFF) };
    }

    /// The texture set for a material, resolved via [Materials2Textures#iconSetOf]. Null when `material`
    /// is null.
    public static @Nullable TextureSet iconSet(@Nullable Material material) {
        return material == null ? null : Materials2Textures.iconSetOf(material);
    }

    /// Whether a material requires a blast furnace to smelt -- [GTMaterialProperties#BLAST_REQUIRED],
    /// `false` when absent.
    public static boolean blastFurnaceRequired(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.BLAST_REQUIRED);
    }

    /// A material's density -- [MaterialAtomics#density]. `GTValues.M` is returned for a null `material`,
    /// equivalent to the 1/1 default.
    public static long density(@Nullable Material material) {
        return material == null ? GTValues.M : MaterialAtomics.density(material);
    }

    /// A material's mass -- [MaterialAtomics#mass]: the linked [Element] mass when
    /// [GTMaterialProperties#ELEMENT] is present, else `Element.Tc`'s mass when
    /// [GTMaterialProperties#COMPOSITION] is empty or absent, else the density-weighted average of the
    /// composition's own values. `Element.Tc`'s mass is also returned for a null `material`.
    public static long mass(@Nullable Material material) {
        return material == null ? Element.Tc.getMass() : MaterialAtomics.mass(material);
    }

    /// [#mass], for [MaterialAtomics#protons].
    public static long protons(@Nullable Material material) {
        return material == null ? Element.Tc.getProtons() : MaterialAtomics.protons(material);
    }

    /// [#mass], for [MaterialAtomics#neutrons].
    public static long neutrons(@Nullable Material material) {
        return material == null ? Element.Tc.getNeutrons() : MaterialAtomics.neutrons(material);
    }

    /// The ore byproducts list for a material, resolved from [GTMaterialProperties#ORE_BYPRODUCTS] in
    /// declaration order; empty when absent. A reference that fails to resolve is skipped.
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

    /// Whether a material has an electrolyzer recipe -- [GTMaterialProperties#HAS_ELECTROLYZER_RECIPE],
    /// `false` when absent.
    public static boolean hasElectrolyzerRecipe(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE);
    }

    /// [#hasElectrolyzerRecipe], for [GTMaterialProperties#HAS_CENTRIFUGE_RECIPE].
    public static boolean hasCentrifugeRecipe(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE);
    }

    /// Whether a material can be cracked -- [GTMaterialProperties#CAN_BE_CRACKED], `false` when absent.
    public static boolean canBeCracked(@Nullable Material material) {
        return material != null && material.getProperty(GTMaterialProperties.CAN_BE_CRACKED);
    }

    /// A material's [Dyes] -- [GTMaterialProperties#DYE] when present, otherwise [#nearestDye]. [Dyes#_NULL]
    /// for a null `material`.
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

    /// Whether a material auto-generates blast furnace recipes --
    /// [GTMaterialProperties#AUTO_BLAST_FURNACE_RECIPES], `true` when absent.
    public static boolean autoGenerateBlastFurnaceRecipes(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES);
    }

    /// [#autoGenerateBlastFurnaceRecipes], for [GTMaterialProperties#AUTO_VACUUM_FREEZER_RECIPES].
    public static boolean autoGenerateVacuumFreezerRecipes(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES);
    }

    /// Whether a material auto-generates recycle recipes -- [GTMaterialProperties#AUTO_RECYCLE_RECIPES],
    /// `true` when absent. A gregtech-dump-only property: only materials in the legacy name domain (see
    /// [#isLegacyNamed]) ever carry it, so a reverse-recipe gate that only means to skip legacy-named
    /// materials with the flag unset can read this predicate directly without an additional domain check.
    public static boolean autoGenerateRecycleRecipes(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES);
    }

    /// The Kelvin melting point for a material -- [GTMaterialProperties#MELTING_POINT], or `0` if unset.
    public static int meltingPoint(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.MELTING_POINT);
    }

    /// A material's gas registration temperature in Kelvin: room temperature (295 K) when
    /// [GTMaterialProperties#GAS_TEMP] is unset or zero, otherwise the material's [#meltingPoint] -- not the
    /// `GAS_TEMP` property's own value -- since the fluid registration temperatures built from this depend
    /// on that exact value.
    public static int gasTemperature(@Nullable Material material) {
        Integer gasTemp = material == null ? null : material.getProperty(GTMaterialProperties.GAS_TEMP);
        return gasTemp == null || gasTemp == 0 ? 295 : meltingPoint(material);
    }

    /// A material's liquid registration temperature in Kelvin: room temperature (295 K) when its
    /// [#meltingPoint] is zero, otherwise the melting point -- the temperature the corresponding-fluid
    /// autogen registers its fluid at.
    public static int liquidTemperature(@Nullable Material material) {
        int meltingPoint = meltingPoint(material);
        return meltingPoint == 0 ? 295 : meltingPoint;
    }

    /// The Kelvin blast furnace temperature for a material -- [GTMaterialProperties#BLAST_TEMP], or `0` if
    /// unset.
    public static int blastFurnaceTemp(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.BLAST_TEMP);
    }

    /// The material tier -- [GTMaterialProperties#TIER], or `0` if unset.
    public static int tier(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.TIER);
    }

    /// The recipe voltage multiplier for a material -- [GTMaterialProperties#VOLTAGE_MULTIPLIER], or `16` if
    /// unset, which is the value every tier-0 material carries.
    public static long voltageMultiplier(@Nullable Material material) {
        return material == null ? 16L : material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
    }

    /// The processing material tier EU value for a material --
    /// [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU], or `0` if unset.
    public static int processingMaterialTierEU(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
    }

    /// The tool durability for a material -- [GTMaterialProperties#DURABILITY], or `0` if unset.
    public static int durability(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.DURABILITY);
    }

    /// [#durability], for [GTMaterialProperties#TOOL_QUALITY].
    public static int toolQuality(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.TOOL_QUALITY);
    }

    /// [#durability], for [GTMaterialProperties#TOOL_SPEED] -- absent defaults to `1.0f`.
    public static float toolSpeed(@Nullable Material material) {
        return material == null ? 1.0f : material.getProperty(GTMaterialProperties.TOOL_SPEED);
    }

    /// The tool [Enchantment] for a material, named by [GTMaterialProperties#TOOL_ENCHANTMENT] (an
    /// `Enchantment#getName` string) and resolved against `Enchantment#enchantmentsList`. Null when the
    /// material has no tool enchantment.
    public static @Nullable Enchantment getToolEnchantment(@Nullable Material material) {
        if (material == null) return null;
        return findEnchantment(material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT));
    }

    /// The tool enchantment level for a material -- [GTMaterialProperties#TOOL_ENCHANTMENT_LEVEL], `1` when
    /// absent. Also `1` whenever [GTMaterialProperties#TOOL_ENCHANTMENT] itself is absent, since a level
    /// with no named enchantment has nothing to apply to.
    public static int getToolEnchantmentLevel(@Nullable Material material) {
        if (material == null || material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT) == null) return 1;
        Integer level = material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT_LEVEL);
        return level == null ? 1 : level;
    }

    /// [#getToolEnchantment], for [GTMaterialProperties#ARMOR_ENCHANTMENT].
    public static @Nullable Enchantment getArmorEnchantment(@Nullable Material material) {
        if (material == null) return null;
        return findEnchantment(material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT));
    }

    /// [#getToolEnchantmentLevel], for [GTMaterialProperties#ARMOR_ENCHANTMENT_LEVEL].
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

    /// The heat damage for a material -- [GTMaterialProperties#HEAT_DAMAGE], or `0` if unset.
    public static float heatDamage(@Nullable Material material) {
        return material == null ? 0f : material.getProperty(GTMaterialProperties.HEAT_DAMAGE);
    }

    /// Whether a material's items unify under the ore dictionary -- `true` unless
    /// [GTMaterialProperties#UNIFIABLE] is explicitly `false`.
    public static boolean unifiable(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.UNIFIABLE);
    }

    /// The fuel power for a material -- [GTMaterialProperties#FUEL_POWER], or `0` if unset.
    public static int fuelPower(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.FUEL_POWER);
    }

    /// The fuel type ordinal for a material into [gregtech.api.util.GTRecipeConstants.FuelType], or `0`
    /// (`FuelType#DieselFuel`) if unset -- [GTMaterialProperties#FUEL_TYPE], see [#fuelPower(Material)].
    public static int fuelType(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.FUEL_TYPE);
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

    /// [#smeltInto(Material)], for [GTMaterialProperties#ARC_SMELT_INTO].
    public static @Nullable Material arcSmeltInto(@Nullable Material material) {
        return chaseRef(material, GTMaterialProperties.ARC_SMELT_INTO);
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

    /// Records the tool-handle material override `material` carries for [#handleMaterial(Material)] --
    /// pushed by `LoaderWerkstoffRegistrations` for materials reconstructed from a bartworks werkstoff, at
    /// the point in loading such a material's handle is decided.
    public static void recordHandleMaterial(Material material, Material handle) {
        reconstructedHandles.put(material, handle);
    }

    private static final java.util.Set<Material> reconstructedBridgeRegistrations = new java.util.HashSet<>();

    /// Records that `material` (a reconstructed bartworks-origin material) has reached this point in
    /// loading -- called by `LoaderWerkstoffRegistrations` once its other per-material registration steps
    /// have run, for [#hasBridgeRegistration]'s timing check.
    public static void recordBridgeRegistration(Material material) {
        reconstructedBridgeRegistrations.add(material);
    }

    /// Whether [#recordBridgeRegistration] has run for `material`. Keys `GTProxy#registerOre`'s
    /// reconstructed-name dispatch, so an ore-dictionary event registered before this point (notably
    /// GregTech's own preInit catch-up replay) is dropped.
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

    /// Whether a material carries a legacy [gregtech.api.enums.SubTag], ported 1:1 to [GTMaterialFlag] of
    /// the same name -- see [GTMaterialProperties#FLAGS]. Also true when [GTMaterialProperties#SUB_TAGS]
    /// (see that property's own javadoc) names `flag`: a werkstoff-backed material's FLAGS only reflects
    /// what a gregtech-side dump captured for it, which never included the werkstoff's own SubTags, so this
    /// reads SUB_TAGS as a second source for the same 1:1 name mapping FLAGS already uses.
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
    /// (MaterialLib sanitizes registration names), otherwise the registration name. Used to build
    /// ore-dictionary names and lang keys.
    public static @Nullable String internalName(@Nullable Material material) {
        if (material == null) return null;
        String legacyName = material.getProperty(GTMaterialProperties.LEGACY_NAME);
        return legacyName != null ? legacyName : material.getName();
    }

    /// The default display name for a material -- [GTMaterialProperties#LOCAL_NAME] when present, otherwise
    /// the registration name. Null when `material` is null.
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

    /// [MaterialFormulas#forSearch], with a missing formula reported as the empty string rather than null.
    public static String chemicalFormula(@Nullable Material material) {
        String formula = MaterialFormulas.forSearch(material);
        return formula == null ? "" : formula;
    }

    /// The lang key a material's display name and its derivatives are translated from. Stated once here
    /// because [MaterialFormulas] and the tooltip paths append their own suffixes to it.
    public static @Nullable String localizedNameKey(@Nullable Material material) {
        String name = internalName(material);
        return name == null ? null : "Material." + name.toLowerCase();
    }

    public static String chemicalTooltip(@Nullable Material material, boolean showQuestionMarks) {
        return chemicalTooltip(material, 1, showQuestionMarks);
    }

    /// The chemical-formula tooltip of a MaterialLib material: a bare `?` formula is hidden unless
    /// `showQuestionMarks`, and a composed material at two or more full units is parenthesized and suffixed
    /// with the multiplier.
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

    /// The Thaumcraft aspect list for a material, as [AspectRefStack]s. An explicit
    /// [GTMaterialProperties#ASPECTS] list is returned as-is; without one, a composed material derives its
    /// aspects from its [GTMaterialProperties#COMPOSITION]: each component's own aspects merged in
    /// first-seen order with amounts summed per aspect, then divided by the density-scaled component
    /// count, minimum `1` each. Empty when neither source applies.
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

    /// The localized display name of a [Material], translated from its [#localizedNameKey] lang key; null
    /// for a null material.
    public static @Nullable String localizedName(@Nullable Material material) {
        return material == null ? null : StatCollector.translateToLocal(localizedNameKey(material));
    }

    /// The stone types a [Material]'s ore can generate in -- [StoneType#ICES] when it carries
    /// [GTMaterialFlag#ICE_ORE], otherwise [StoneType#STONES]. Empty for a null material.
    public static List<IStoneType> validStones(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        return hasFlag(material, GTMaterialFlag.ICE_ORE) ? StoneType.ICES : StoneType.STONES;
    }

    /// Whether a [Material] carries the [GTMaterialFlag] equivalent of a legacy [SubTag] (see
    /// [#flagForSubTag]); false for a null material.
    public static boolean hasSubTag(@Nullable Material material, SubTag subTag) {
        if (material == null) return false;
        GTMaterialFlag flag = flagForSubTag(subTag);
        return flag != null && hasFlag(material, flag);
    }

    /// Appends a [Material]'s chemical-formula tooltip (see [#chemicalTooltip]) to `list` when
    /// `Client.tooltip.showFormula` is enabled; a no-op for a null material.
    public static void addTooltips(@Nullable Material material, List<String> list) {
        if (material == null || !Client.tooltip.showFormula) return;
        String tooltip = chemicalTooltip(material, false);
        if (tooltip != null && !tooltip.isEmpty()) list.add(tooltip);
    }

    /// The [GTMaterialFlag] whose enum-constant name equals `subTag`'s name, or null when none does -- the
    /// [Material]-side lookup [#hasSubTag] uses to translate a legacy [SubTag] query into the flag its [Material]
    /// carries.
    private static @Nullable GTMaterialFlag flagForSubTag(SubTag subTag) {
        // AnaerobeGas/NobleGas are the two GTMaterialFlag constants whose SubTag name does not match the enum
        // constant name directly, so they need an explicit mapping before the general valueOf fallback below.
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

    /// The crushed-ore yield multiplier for a material, or `1` if unset -- see [GTMaterialProperties#ORE_MULTIPLIER].
    public static int oreMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
    }

    /// [#oreMultiplier], for [GTMaterialProperties#SMELTING_MULTIPLIER].
    public static int smeltingMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER);
    }

    /// [#oreMultiplier], for [GTMaterialProperties#BYPRODUCT_MULTIPLIER].
    public static int byProductMultiplier(@Nullable Material material) {
        return material == null ? 1 : material.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER);
    }
}
