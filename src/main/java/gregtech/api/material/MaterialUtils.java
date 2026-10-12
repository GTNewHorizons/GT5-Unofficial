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

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.FluidMaterialInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.MaterialRef;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.StoneCategory;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.MaterialFluidNames;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.MaterialStack;
import gregtech.common.config.Client;

/// Accessors for the GregTech data attached to a MaterialLib [Material] through [GTMaterialProperties], such as its
/// fluids, ore and tool stats, names, flags and color.
///
/// Most accessors tolerate a null material and answer what an unset property would. Plain-constant defaults live on the
/// [com.ruling_0.materiallib.api.Property] key itself.
public class MaterialUtils {

    private MaterialUtils() {}

    /// Returns the GregTech material whose [#internalName] or registration name is `name`. Null on a miss and for
    /// the `NULL` placeholder.
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
        if (found == null) found = MaterialLibAPI.getMaterial("gregtech", name);
        return found == Materials.NULL ? null : found;
    }

    private static Map<String, Material> legacyNameToMaterial;

    /// Returns the dust stack for a [GTMaterialProperties#COMPOSITION] entry, sized by the entry's amount. Null
    /// when the material carries no `dust` shape. For the gas form, see [#compositionGas].
    public static @Nullable ItemStack compositionDust(MaterialRefStack entry) {
        Material material = entry.material()
            .resolve();
        if (!material.hasShape(Shapes.dust)) return null;
        return MaterialLibAPI.getStack(material, Shapes.dust, (int) entry.amount());
    }

    /// Returns the gas stack for a [GTMaterialProperties#COMPOSITION] entry, at 1000 mB per unit of the entry's
    /// amount. Null when the material carries no `fluidGas` shape.
    public static @Nullable FluidStack compositionGas(MaterialRefStack entry) {
        Material material = entry.material()
            .resolve();
        if (!material.hasShape(FluidShapes.fluidGas)) return null;
        return MaterialLibAPI.getFluidStack(material, FluidShapes.fluidGas, (int) (1000 * entry.amount()));
    }

    /// Returns whether the material carries the [FluidShapes#fluidMolten] shape.
    public static boolean hasMolten(@Nullable Material material) {
        return material != null && material.hasShape(FluidShapes.fluidMolten);
    }

    /// Returns the molten fluid at `amount`, or null when [#hasMolten] is false.
    public static @Nullable FluidStack molten(@Nullable Material material, long amount) {
        if (!hasMolten(material)) return null;
        return MaterialLibAPI.getFluidStack(material, FluidShapes.fluidMolten, (int) amount);
    }

    /// Same as [#fluidOf], as a stack of `amount`.
    public static @Nullable FluidStack fluid(@Nullable Material material, long amount) {
        Fluid fluid = fluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// Returns the fluid for the material's liquid slot, or null when it has none. A material listed in
    /// [MaterialFluidNames] answers null unless it declares a `fluid()` slot.
    public static @Nullable Fluid fluidOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.LIQUID, FluidNames::fluid);
    }

    /// Same as [#fluid], for the `gas()` slot.
    public static @Nullable FluidStack gas(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// Same as [#fluid], for the `plasma()` slot.
    public static @Nullable FluidStack plasma(@Nullable Material material, long amount) {
        Fluid fluid = resolveSlotFluid(material, FluidState.PLASMA, FluidNames::plasma);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// Returns the material's [GTMaterialProperties#SOLID_FLUID] as a stack of `amount`, or null when it declares
    /// none.
    public static @Nullable FluidStack solid(@Nullable Material material, long amount) {
        String name = material == null ? null : material.getProperty(GTMaterialProperties.SOLID_FLUID);
        return name == null ? null : FluidRegistry.getFluidStack(name, (int) amount);
    }

    /// Returns the first of the material's molten, liquid and gas fluids as a stack of `amount`. Null when it
    /// carries none of the three shapes.
    public static @Nullable FluidStack anyFluid(@Nullable Material material, long amount) {
        Fluid fluid = anyFluidOf(material);
        return fluid == null ? null : new FluidStack(fluid, (int) amount);
    }

    /// The shapes [#anyFluidOf] tries, in fixed order: molten, then liquid, then gas.
    private static final Shape[] ANY_FLUID_SHAPES = { FluidShapes.fluidMolten, FluidShapes.fluidLiquid,
        FluidShapes.fluidGas };

    /// Same as [#anyFluid], as a bare [Fluid].
    public static @Nullable Fluid anyFluidOf(@Nullable Material material) {
        if (material == null) return null;
        for (Shape shape : ANY_FLUID_SHAPES) {
            Fluid fluid = MaterialLibAPI.getFluid(material, shape);
            if (fluid != null) return fluid;
        }
        return null;
    }

    /// Same as [#fluidOf], for the `gas()` slot.
    public static @Nullable Fluid gasOf(@Nullable Material material) {
        return resolveSlotFluid(material, FluidState.GAS, FluidNames::gas);
    }

    /// Same as [#fluidOf], for the `molten()` slot. Unlike [#molten], null when a material listed in
    /// [MaterialFluidNames] declares no `molten()` slot, even if it carries the [FluidShapes#fluidMolten] shape.
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

    /// The fluid states a material can carry.
    private enum FluidState {
        LIQUID,
        GAS,
        MOLTEN,
        PLASMA
    }

    /// The shapes that can back each fluid state, in resolution order. A material whose liquid and gas share a
    /// Forge fluid name registers only one MaterialLib fluid, so liquid and gas each fall back to the other's shape.
    private static final EnumMap<FluidState, Shape[]> STATE_SHAPES = new EnumMap<>(
        Map.of(
            FluidState.LIQUID,
            new Shape[] { FluidShapes.fluidLiquid, FluidShapes.fluidGas },
            FluidState.GAS,
            new Shape[] { FluidShapes.fluidGas, FluidShapes.fluidLiquid },
            FluidState.MOLTEN,
            new Shape[] { FluidShapes.fluidMolten },
            FluidState.PLASMA,
            new Shape[] { FluidShapes.fluidPlasma }));

    /// Returns the fluid backing `state`. A material listed in [MaterialFluidNames] must declare the slot, and the
    /// fluid is the one MaterialLib registered for the first shape in [#STATE_SHAPES] that the material generates.
    /// Any other material resolves through the state's own shape alone.
    private static @Nullable Fluid resolveSlotFluid(@Nullable Material material, FluidState state,
        Function<FluidNames, FluidRef> slot) {
        if (material == null) return null;
        Shape[] shapes = STATE_SHAPES.get(state);
        FluidNames legacyFluids = MaterialFluidNames.of(material.getName());
        if (legacyFluids == null) return MaterialLibAPI.getFluid(material, shapes[0]);
        if (slot.apply(legacyFluids) == null) return null;
        for (Shape shape : shapes) {
            Fluid fluid = MaterialLibAPI.getFluid(material, shape);
            if (fluid != null) return fluid;
        }
        return null;
    }

    /// Returns the material that owns `fluid` per [MaterialLibAPI#lookupFluid], or null when no material does.
    public static @Nullable Material materialOfFluid(@Nullable Fluid fluid) {
        FluidMaterialInfo info = MaterialLibAPI.lookupFluid(fluid);
        return info == null ? null : info.material();
    }

    /// The two cracking families of a material's cracked fluids.
    public enum CrackType {
        HYDRO,
        STEAM
    }

    /// The six cracked-fluid shapes, indexed by [CrackType] then by severity.
    private static final EnumMap<CrackType, Shape[]> CRACKED_SHAPES = new EnumMap<>(
        Map.of(
            CrackType.HYDRO,
            new Shape[] { FluidShapes.fluidHydroCracked1, FluidShapes.fluidHydroCracked2,
                FluidShapes.fluidHydroCracked3 },
            CrackType.STEAM,
            new Shape[] { FluidShapes.fluidSteamCracked1, FluidShapes.fluidSteamCracked2,
                FluidShapes.fluidSteamCracked3 }));

    /// Returns the cracked fluid of `material`'s `type` family at `severity` (0/1/2 = light/moderate/severe), or
    /// null when the material carries none.
    public static @Nullable Fluid crackedFluid(@Nullable Material material, CrackType type, int severity) {
        if (material == null) return null;
        return MaterialLibAPI.getFluid(material, CRACKED_SHAPES.get(type)[severity]);
    }

    /// The material's legacy generated-material id ([GTMaterialProperties#OLD_SUB_ID]), which old saves encode in
    /// its parts' metadata. `-1` if unset.
    public static int oldSubId(@Nullable Material material) {
        if (material == null) return -1;
        Integer id = material.getProperty(GTMaterialProperties.OLD_SUB_ID);
        return id == null ? -1 : id;
    }

    /// Returns a new `[r, g, b, a]` array for the material's [MaterialLibAPI#getTint], or null when it declares no
    /// [StandardProperties#TINT]. A value cached at load misses a resource pack's override.
    public static @Nullable short[] rgba(@Nullable Material material) {
        if (material == null || !material.hasProperty(StandardProperties.TINT)) return null;
        int argb = MaterialLibAPI.getTint(material);
        return new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
    }

    /// The name of a material's [StandardProperties#TEXTURE_SET], i.e. the folder its art lives in under
    /// `textures/{items,blocks}/materials/`. Null when `material` is null.
    public static @Nullable String textureSetName(@Nullable Material material) {
        if (material == null) return null;
        return material.getProperty(StandardProperties.TEXTURE_SET)
            .getName();
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

    /// A material's mass -- [MaterialAtomics#mass]. `Element.Tc`'s mass is returned for a null `material`.
    public static long mass(@Nullable Material material) {
        return material == null ? Element.Tc.getMass() : MaterialAtomics.mass(material);
    }

    /// [#mass], for [MaterialAtomics#protons].
    public static long protons(@Nullable Material material) {
        return material == null ? Element.Tc.getProtons() : MaterialAtomics.protons(material);
    }

    /// The ore byproducts list for a material, resolved from [GTMaterialProperties#ORE_BYPRODUCTS] in
    /// declaration order; empty when absent.
    public static List<Material> oreByProducts(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<MaterialRefStack> oreByProducts = material.getProperty(GTMaterialProperties.ORE_BYPRODUCTS);
        if (oreByProducts == null || oreByProducts.isEmpty()) return Collections.emptyList();
        List<Material> list = new ArrayList<>(oreByProducts.size());
        for (MaterialRefStack entry : oreByProducts) {
            list.add(
                entry.material()
                    .resolve());
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

    /// A material's [Dyes] -- [GTMaterialProperties#DYE], or [Dyes#_NULL] when absent or for a null `material`.
    public static Dyes dye(@Nullable Material material) {
        if (material == null) return Dyes._NULL;
        String dye = material.getProperty(GTMaterialProperties.DYE);
        return dye == null ? Dyes._NULL : Dyes.valueOf(dye);
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
    /// `true` when absent.
    public static boolean autoGenerateRecycleRecipes(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES);
    }

    /// The Kelvin melting point: the declared [GTMaterialProperties#MELTING_POINT], else the average over a
    /// [GTMaterialProperties#COMPOSITION] of more than one unit, where components with no melting point count as
    /// zero. `-1` when neither applies.
    public static int meltingPoint(@Nullable Material material) {
        if (material == null) return -1;
        int declared = material.getProperty(GTMaterialProperties.MELTING_POINT);
        if (declared >= 0) return declared;
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null) return declared;
        long total = 0;
        long sum = 0;
        for (MaterialRefStack stack : composition) {
            total += stack.amount();
            int componentPoint = meltingPoint(
                stack.material()
                    .resolve());
            if (componentPoint > 0) sum += componentPoint * stack.amount();
        }
        return total > 1 ? (int) (sum / total) : declared;
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

    /// The turbine steam-flow multiplier for a material -- [GTMaterialProperties#STEAM_MULTIPLIER], or `1` if
    /// unset.
    public static float steamMultiplier(@Nullable Material material) {
        Float multiplier = material == null ? null : material.getProperty(GTMaterialProperties.STEAM_MULTIPLIER);
        return multiplier == null ? 1.0f : multiplier;
    }

    /// [#steamMultiplier], for [GTMaterialProperties#GAS_MULTIPLIER].
    public static float gasMultiplier(@Nullable Material material) {
        Float multiplier = material == null ? null : material.getProperty(GTMaterialProperties.GAS_MULTIPLIER);
        return multiplier == null ? 1.0f : multiplier;
    }

    /// [#steamMultiplier], for [GTMaterialProperties#PLASMA_MULTIPLIER].
    public static float plasmaMultiplier(@Nullable Material material) {
        Float multiplier = material == null ? null : material.getProperty(GTMaterialProperties.PLASMA_MULTIPLIER);
        return multiplier == null ? 1.0f : multiplier;
    }

    /// The recipe voltage multiplier for a material -- [GTMaterialProperties#VOLTAGE_MULTIPLIER], or `16` if
    /// unset.
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

    /// The heat damage for a material -- [GTMaterialProperties#HEAT_DAMAGE], or `0` if unset.
    public static float heatDamage(@Nullable Material material) {
        return material == null ? 0f : material.getProperty(GTMaterialProperties.HEAT_DAMAGE);
    }

    /// Whether a material's items unify by oredict -- `true` unless
    /// [GTMaterialProperties#UNIFIABLE] is explicitly `false`.
    public static boolean unifiable(@Nullable Material material) {
        return material == null || material.getProperty(GTMaterialProperties.UNIFIABLE);
    }

    /// The fuel power for a material -- [GTMaterialProperties#FUEL_POWER], or `0` if unset.
    public static int fuelPower(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.FUEL_POWER);
    }

    /// The fuel type for a material -- [GTMaterialProperties#FUEL_TYPE], an ordinal into
    /// [gregtech.api.util.GTRecipeConstants.FuelType], or `0` (`DieselFuel`) if unset.
    public static int fuelType(@Nullable Material material) {
        return material == null ? 0 : material.getProperty(GTMaterialProperties.FUEL_TYPE);
    }

    /// Returns the material's [GTMaterialProperties#SMELT_INTO] target, or the material itself when unset. A
    /// target that sets the same property is followed one more hop.
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

    /// Returns the material's tool-handle [GTMaterialProperties#HANDLE_MATERIAL], or the material itself when
    /// unset. Unlike [#smeltInto], it follows only one hop.
    public static @Nullable Material handleMaterial(@Nullable Material material) {
        if (material == null) return null;
        MaterialRef ref = material.getProperty(GTMaterialProperties.HANDLE_MATERIAL);
        if (ref == null) return material;
        return ref.resolve();
    }

    private static @Nullable Material chaseRef(@Nullable Material material,
        com.ruling_0.materiallib.api.Property<MaterialRef> property) {
        if (material == null) return null;
        MaterialRef ref = material.getProperty(property);
        if (ref == null) return material;
        Material target = ref.resolve();
        MaterialRef hop = target.getProperty(property);
        return hop == null ? target : hop.resolve();
    }

    /// Whether a material's [GTMaterialProperties#FLAGS] carry `flag`.
    public static boolean hasFlag(@Nullable Material material, GTMaterialFlag flag) {
        if (material == null) return false;
        EnumSet<GTMaterialFlag> flags = material.getProperty(GTMaterialProperties.FLAGS);
        return flags != null && flags.contains(flag);
    }

    /// A material's internal name: [GTMaterialProperties#LEGACY_NAME] when present, otherwise the registration
    /// name.
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

    /// Same as [MaterialFormulas#forSearch], with the empty string in place of null.
    public static String chemicalFormula(@Nullable Material material) {
        String formula = MaterialFormulas.forSearch(material);
        return formula == null ? "" : formula;
    }

    /// Returns the lang key the material's display name is translated from: its
    /// [StandardProperties#DISPLAY_NAME_KEY], else `Material.` plus its lowercased [#internalName].
    public static @Nullable String localizedNameKey(@Nullable Material material) {
        if (material == null) return null;
        String key = material.getProperty(StandardProperties.DISPLAY_NAME_KEY);
        return key != null ? key : "Material." + internalName(material).toLowerCase();
    }

    /// Returns the translation of the material's `<localizedNameKey>.flavorText` key when the lang files carry
    /// one, else its English [GTMaterialProperties#FLAVOR_TEXT]. Null when it has neither.
    public static @Nullable String flavorText(@Nullable Material material) {
        String nameKey = localizedNameKey(material);
        if (nameKey == null) return null;
        String key = nameKey + ".flavorText";
        if (StatCollector.canTranslate(key)) return StatCollector.translateToLocal(key);
        return material.getProperty(GTMaterialProperties.FLAVOR_TEXT);
    }

    /// Returns the material's chemical formula tooltip. A bare `?` formula becomes the empty string unless
    /// `showQuestionMarks`.
    public static String chemicalTooltip(@Nullable Material material, boolean showQuestionMarks) {
        String formula = chemicalFormula(material);
        return !showQuestionMarks && formula.equals("?") ? "" : formula;
    }

    /// Returns the material's [GTMaterialProperties#COMPOSITION] with every amount divided by their greatest common
    /// divisor. Empty when it carries none. The result may be the property's own list and must not be modified.
    public static List<MaterialRefStack> reducedComposition(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return Collections.emptyList();
        long divisor = 0;
        for (MaterialRefStack entry : composition) divisor = gcd(divisor, entry.amount());
        if (divisor <= 1) return composition;
        List<MaterialRefStack> reduced = new ArrayList<>(composition.size());
        for (MaterialRefStack entry : composition) {
            reduced.add(new MaterialRefStack(entry.material(), entry.amount() / divisor));
        }
        return reduced;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /// The composition of a MaterialLib material as [MaterialStack]s, resolved from
    /// [GTMaterialProperties#COMPOSITION]; empty when it carries none.
    public static List<MaterialStack> materialList(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return Collections.emptyList();
        List<MaterialStack> list = new ArrayList<>(composition.size());
        for (MaterialRefStack entry : composition) {
            list.add(
                new MaterialStack(
                    entry.material()
                        .resolve(),
                    entry.amount()));
        }
        return list;
    }

    /// Returns the material's Thaumcraft aspects. A declared [GTMaterialProperties#ASPECTS] list is returned as
    /// is. Otherwise a material with a [GTMaterialProperties#COMPOSITION] sums its components' aspects and
    /// divides them by its density-scaled component count, at least `1` each. Empty when neither applies.
    public static List<AspectRefStack> aspects(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        List<AspectRefStack> aspects = material.getProperty(GTMaterialProperties.ASPECTS);
        if (aspects != null) return aspects;
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) return Collections.emptyList();
        Map<String, Long> merged = new LinkedHashMap<>();
        long components = 0;
        for (MaterialRefStack entry : composition) {
            Material component = entry.material()
                .resolve();
            components += entry.amount();
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

    /// The stone types a [Material]'s ore can generate in: the ice stones for an [GTMaterialFlag#ICE_ORE]
    /// material, every other stone otherwise.
    public static List<IStoneType> validStones(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        return hasFlag(material, GTMaterialFlag.ICE_ORE) ? StoneType.ICES : StoneType.STONES;
    }

    /// Whether a [Material]'s ore can generate in `stoneType`, per [#validStones].
    public static boolean isValidStone(Material material, StoneType stoneType) {
        StoneCategory category = hasFlag(material, GTMaterialFlag.ICE_ORE) ? StoneCategory.Ice : StoneCategory.Stone;
        return stoneType.getCategory() == category;
    }

    /// Appends the material's [#addHazardTooltips] lines and, when `Client.tooltip.showFormula` is on, its
    /// [#chemicalTooltip] to `list`.
    ///
    /// For items with their own `addInformation`. Stacks MaterialLib serves get these lines from
    /// [gregtech.client.MaterialFormulaTooltip].
    public static void addTooltips(@Nullable Material material, List<String> list) {
        if (material == null) return;
        addHazardTooltips(material, list);
        if (!Client.tooltip.showFormula) return;
        String tooltip = chemicalTooltip(material, false);
        if (tooltip != null && !tooltip.isEmpty()) list.add(tooltip);
    }

    /// Appends the material's [GTMaterialProperties#RADIATION_LEVEL] and [GTMaterialProperties#TOXIC] warnings to
    /// `list`, each behind its own `Client.tooltip` toggle, followed by one shared hazmat line.
    public static void addHazardTooltips(@Nullable Material material, List<String> list) {
        if (material == null) return;
        boolean radioactive = Client.tooltip.showRadioactiveText
            && material.getProperty(GTMaterialProperties.RADIATION_LEVEL) != null;
        boolean toxic = Client.tooltip.showToxicText
            && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.TOXIC));
        if (radioactive) list.add(StatCollector.translateToLocal("GTPP.core.GT_Tooltip_Radioactive"));
        if (toxic) list.add(StatCollector.translateToLocal("GTPP.core.GT_Tooltip_Toxic"));
        if (radioactive || toxic) list.add(StatCollector.translateToLocal("GTPP.core.GT_Tooltip_HazmatWarning"));
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
