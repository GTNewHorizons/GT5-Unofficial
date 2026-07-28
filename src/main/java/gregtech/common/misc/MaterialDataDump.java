package gregtech.common.misc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.ShapeItem;
import com.ruling_0.materiallib.api.StandardProperties;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.AspectRefStack;
import gregtech.api.material.FluidNames;
import gregtech.api.material.FluidRef;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialGenerationFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialRef;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.client.iconContainers.blocks.GTBlockIconContainer;
import gregtech.common.blocks.BlockMetal;
import gregtech.common.fluid.GTFluid;

/// Dumps `OrePrefixes` and bartworks-origin materials, plus the resolved MaterialLib registry view of the
/// `Materials2` port, to JSON, for consumption by the material unification tooling.
///
/// Triggered from `GTMod`'s `FMLLoadCompleteEvent` handler when the `gt.dumpMaterialData` system property is
/// set, so a headless server run can produce the dumps non-interactively.
public final class MaterialDataDump {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .disableHtmlEscaping()
        .serializeNulls()
        .serializeSpecialFloatingPointValues()
        .create();

    /// Used only for `recipe-census.json`: pretty-printing tens of thousands of recipe digests inflates the file
    /// without aiding the tooling that consumes it (a straight text diff), so this instance omits it.
    private static final Gson COMPACT_GSON = new GsonBuilder().disableHtmlEscaping()
        .serializeNulls()
        .serializeSpecialFloatingPointValues()
        .create();

    private MaterialDataDump() {}

    public static void writeAll(File directory) {
        directory.mkdirs();
        write(new File(directory, "oreprefixes.json"), dumpOrePrefixes());
        write(new File(directory, "werkstoff.json"), dumpWerkstoff());
        write(new File(directory, "ml-materials.json"), dumpMlMaterials());
        write(new File(directory, "legacy-variants.json"), dumpLegacyVariants());
        write(new File(directory, "fluid-textures.json"), dumpFluidTextures());
        write(new File(directory, "legacy-blocks.json"), dumpLegacyBlocks());
        write(new File(directory, "recipe-census.json"), dumpRecipeCensus(), COMPACT_GSON);
    }

    private static void write(File file, Object data) {
        write(file, data, GSON);
    }

    private static void write(File file, Object data, Gson gson) {
        try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write material dump " + file, e);
        }
        GTLog.out.println("MaterialDataDump: wrote " + file);
    }

    // region oreprefixes.json

    private static Map<String, Object> dumpMaterialStack(MaterialStack stack) {
        if (stack == null || stack.mMaterial == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("material", MU.internalName(stack.mMaterial));
        json.put("amount", stack.mAmount);
        return json;
    }

    private static Map<String, Object> dumpOrePrefixes() {
        List<Map<String, Object>> prefixes = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) prefixes.add(dumpOrePrefix(prefix));

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("prefixes", prefixes);
        root.put("textureSlots", dumpTextureSlots());
        return root;
    }

    private static Map<String, Object> dumpOrePrefix(OrePrefixes prefix) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", prefix.getName());
        json.put("localNameFormat", prefix.getMaterialPrefix() + "%s" + prefix.getMaterialPostfix());
        json.put("materialAmount", prefix.getMaterialAmount());
        json.put("generationBits", prefix.getMaterialGenerationBits());
        json.put("textureIndex", prefix.getTextureIndex());
        json.put("defaultStackSize", prefix.getDefaultStackSize());
        json.put("isUnifiable", prefix.isUnifiable());
        json.put("isMaterialBased", prefix.isMaterialBased());
        json.put("isSelfReferencing", prefix.isSelfReferencing());
        json.put("isContainer", prefix.isContainer());
        json.put("isRecyclable", prefix.isRecyclable());
        json.put("isEnchantable", prefix.isEnchantable());
        json.put("secondaryMaterial", dumpMaterialStack(prefix.mSecondaryMaterial));
        return json;
    }

    /// Keyed by [MaterialIconRegistry.IconType] ordinal -- the per-slot suffixes live on
    /// `MaterialIconRegistry.IconType` (currently 158 entries), not on `TextureSet`.
    private static Map<Integer, String> dumpTextureSlots() {
        Map<Integer, String> slots = new TreeMap<>();
        for (MaterialIconRegistry.IconType type : MaterialIconRegistry.IconType.values()) {
            slots.put(type.ordinal(), type.suffix);
        }
        return slots;
    }

    // endregion

    // region werkstoff.json

    private static List<Map<String, Object>> dumpWerkstoff() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (com.ruling_0.materiallib.api.Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            out.add(dumpWerkstoff(material));
        }
        return out;
    }

    /// One JSON record per MaterialLib material carrying [GTMaterialProperties#WERKSTOFF_IDS], sourced entirely
    /// from that material's own properties (the same ground truth [#dumpMlMaterial] reads) rather than a live
    /// bartworks material instance -- `id` from the first [GTMaterialProperties#WERKSTOFF_IDS] entry, `pool`
    /// bucketed from it by [#werkstoffPool], and `generatedPrefixes` from the pinned
    /// [GTMaterialProperties#WERKSTOFF_PREFIXES] capture (not re-derived from a live reroute loop, avoiding the
    /// staleness the legacy dump had to guard against). `additionalOredict`, `sublimation`,
    /// `durabilityModifier` and `enchantmentLevel` have no surviving property and are dropped.
    private static Map<String, Object> dumpWerkstoff(com.ruling_0.materiallib.api.Material material) {
        int id = Materials2WerkstoffIndex.idOf(material);
        short[] rgba = MU.rgba(material);
        TextureSet texSet = MU.iconSet(material);

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("id", id);
        json.put("name", material.getProperty(GTMaterialProperties.LOCAL_NAME));
        json.put("varName", MU.internalName(material));
        json.put("rgb", rgba != null ? new int[] { rgba[0], rgba[1], rgba[2] } : null);
        json.put("texSet", texSet != null ? texSet.mSetName : null);
        json.put("type", material.getProperty(GTMaterialProperties.WERKSTOFF_TYPE));
        json.put("pool", werkstoffPool(id));
        json.put("meltingPoint", material.getProperty(GTMaterialProperties.MELTING_POINT));
        json.put("boilingPoint", material.getProperty(GTMaterialProperties.BOILING_POINT));
        json.put("protons", MU.protons(material));
        json.put("neutrons", MU.neutrons(material));
        json.put("mass", MU.mass(material));
        json.put("meltingVoltage", material.getProperty(GTMaterialProperties.MELTING_VOLTAGE));
        json.put("durability", material.getProperty(GTMaterialProperties.DURABILITY));
        json.put("speed", material.getProperty(GTMaterialProperties.TOOL_SPEED));
        json.put("quality", material.getProperty(GTMaterialProperties.TOOL_QUALITY));
        json.put("toxic", material.getProperty(GTMaterialProperties.TOXIC));
        json.put("radioactive", material.getProperty(GTMaterialProperties.IS_RADIOACTIVE));
        json.put("blastFurnace", material.getProperty(GTMaterialProperties.BLAST_REQUIRED));
        json.put("elektrolysis", material.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE));
        json.put("centrifuge", material.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE));
        json.put("gas", material.getProperty(GTMaterialProperties.HAS_GAS));
        json.put("contents", dumpMlMaterialRefStacks(material.getProperty(GTMaterialProperties.COMPOSITION)));
        json.put("oreByProducts", dumpMlMaterialRefNames(material.getProperty(GTMaterialProperties.ORE_BYPRODUCTS)));
        json.put("generatedPrefixes", orEmpty(material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES)));
        json.put("enforceUnification", material.getProperty(GTMaterialProperties.ENFORCE_ORE_DICT_UNIFICATION));
        json.put("chemicalRecipes", material.getProperty(GTMaterialProperties.HAS_CHEMICAL_RECIPE));
        json.put(
            "metalCraftingSolidifierRecipes",
            MaterialDumpData.hasMetalCraftingSolidifierRecipe(material.getName()));
        json.put("metalSolidifierRecipes", MaterialDumpData.hasMetalSolidifierRecipe(material.getName()));
        json.put("mixerRecipes", material.getProperty(GTMaterialProperties.HAS_MIXER_RECIPE));
        json.put("sifterRecipes", material.getProperty(GTMaterialProperties.HAS_SIFTER_RECIPE));
        json.put("mixCircuit", material.getProperty(GTMaterialProperties.MIX_CIRCUIT));
        json.put("ebfGasTimeMultiplier", material.getProperty(GTMaterialProperties.EBF_GAS_TIME_MULTIPLIER));
        json.put("ebfGasAmountMultiplier", material.getProperty(GTMaterialProperties.EBF_GAS_AMOUNT_MULTIPLIER));
        json.put("autoBlastFurnaceRecipes", material.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES));
        json.put("autoVacuumFreezerRecipes", material.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES));
        json.put("subTags", orEmpty(material.getProperty(GTMaterialProperties.SUB_TAGS)));
        json.put("formula", material.getProperty(GTMaterialProperties.FORMULA));
        json.put("formulaLocalized", material.getProperty(GTMaterialProperties.FORMULA_LOCALIZED));
        return json;
    }

    /// Buckets a legacy werkstoff id into its owning pool by id range. The 11500..11599 block belongs to
    /// bartworks (11500-11503 sit past the gtnhlanth block); ids outside every known range fall back to
    /// `"unknown"`.
    private static String werkstoffPool(int id) {
        if (id > 31765) return "gt-bridge-proxy";
        if (id >= 29900 && id <= 29999) return "gtnhlanth-bot";
        if (id >= 11500 && id <= 11599) return "bartworks";
        if (id >= 11000 && id <= 11499) return "gtnhlanth";
        if (id >= 10001 && id <= 10999) return "goodgenerator";
        if (id >= 1 && id <= 9999) return "bartworks";
        return "unknown";
    }

    // endregion

    // region ml-materials.json

    private static List<Map<String, Object>> dumpMlMaterials() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (com.ruling_0.materiallib.api.Material material : MaterialLibAPI.getMaterials()) {
            if (!"gregtech".equals(material.getModId())) continue;
            out.add(dumpMlMaterial(material));
        }
        return out;
    }

    private static Map<String, Object> dumpMlMaterial(com.ruling_0.materiallib.api.Material material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", material.getName());
        json.put("legacyName", material.getProperty(GTMaterialProperties.LEGACY_NAME));
        json.put("tint", material.getProperty(StandardProperties.TINT));
        json.put("argb", material.getProperty(GTMaterialProperties.ARGB));
        json.put("moltenArgb", material.getProperty(GTMaterialProperties.MOLTEN_ARGB));
        json.put(
            "textureSet",
            material.getProperty(StandardProperties.TEXTURE_SET)
                .getName());
        json.put("shapes", dumpMlShapes(material));
        json.put("families", dumpMlFamilies(material));
        json.put("localName", material.getProperty(GTMaterialProperties.LOCAL_NAME));
        json.put("meltingPoint", material.getProperty(GTMaterialProperties.MELTING_POINT));
        json.put("boilingPoint", material.getProperty(GTMaterialProperties.BOILING_POINT));
        json.put("meltingVoltage", material.getProperty(GTMaterialProperties.MELTING_VOLTAGE));
        json.put("blastTemp", material.getProperty(GTMaterialProperties.BLAST_TEMP));
        json.put("blastRequired", material.getProperty(GTMaterialProperties.BLAST_REQUIRED));
        json.put("toxic", material.getProperty(GTMaterialProperties.TOXIC));
        json.put("isRadioactive", material.getProperty(GTMaterialProperties.IS_RADIOACTIVE));
        json.put("radiationLevel", material.getProperty(GTMaterialProperties.RADIATION_LEVEL));
        json.put("tier", material.getProperty(GTMaterialProperties.TIER));
        json.put("voltageMultiplier", material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER));
        json.put("mixCircuit", material.getProperty(GTMaterialProperties.MIX_CIRCUIT));
        json.put("ebfGasTimeMultiplier", material.getProperty(GTMaterialProperties.EBF_GAS_TIME_MULTIPLIER));
        json.put("ebfGasAmountMultiplier", material.getProperty(GTMaterialProperties.EBF_GAS_AMOUNT_MULTIPLIER));
        json.put("subTags", material.getProperty(GTMaterialProperties.SUB_TAGS));
        json.put("gasTemp", material.getProperty(GTMaterialProperties.GAS_TEMP));
        json.put("fuelPower", material.getProperty(GTMaterialProperties.FUEL_POWER));
        json.put("fuelType", material.getProperty(GTMaterialProperties.FUEL_TYPE));
        json.put("heatDamage", material.getProperty(GTMaterialProperties.HEAT_DAMAGE));
        json.put("toolSpeed", material.getProperty(GTMaterialProperties.TOOL_SPEED));
        json.put("toolDurability", material.getProperty(GTMaterialProperties.DURABILITY));
        json.put("toolQuality", material.getProperty(GTMaterialProperties.TOOL_QUALITY));
        json.put("subId", material.getProperty(GTMaterialProperties.OLD_SUB_ID));
        json.put("formula", material.getProperty(GTMaterialProperties.FORMULA));
        json.put("formulaLocalized", material.getProperty(GTMaterialProperties.FORMULA_LOCALIZED));
        json.put("moltenTint", material.getProperty(GTMaterialProperties.MOLTEN_TINT));
        json.put("element", material.getProperty(GTMaterialProperties.ELEMENT));
        json.put("composition", dumpMlMaterialRefStacks(material.getProperty(GTMaterialProperties.COMPOSITION)));
        json.put("smeltInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.SMELT_INTO)));
        json.put("macerateInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.MACERATE_INTO)));
        json.put("arcSmeltInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.ARC_SMELT_INTO)));
        json.put("directSmelting", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.DIRECT_SMELTING)));
        json.put("handleMaterial", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.HANDLE_MATERIAL)));
        json.put("oreByProducts", dumpMlMaterialRefNames(material.getProperty(GTMaterialProperties.ORE_BYPRODUCTS)));
        json.put("oreMultiplier", material.getProperty(GTMaterialProperties.ORE_MULTIPLIER));
        json.put("byProductMultiplier", material.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER));
        json.put("smeltingMultiplier", material.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER));
        json.put("flags", dumpMlFlags(material.getProperty(GTMaterialProperties.FLAGS)));
        json.put("aspects", dumpMlAspects(material.getProperty(GTMaterialProperties.ASPECTS)));
        json.put("fluids", dumpMlFluids(material.getProperty(GTMaterialProperties.LEGACY_FLUIDS)));
        json.put(
            "crackedHydroFluids",
            dumpMlFluidRefList(material.getProperty(GTMaterialProperties.CRACKED_HYDRO_FLUIDS)));
        json.put(
            "crackedSteamFluids",
            dumpMlFluidRefList(material.getProperty(GTMaterialProperties.CRACKED_STEAM_FLUIDS)));
        json.put("color", material.getProperty(GTMaterialProperties.DYE));
        json.put("autoBlast", material.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES));
        json.put("autoVacuum", material.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES));
        json.put("autoRecycle", material.getProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES));
        json.put("toolEnchantment", material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT));
        json.put("toolEnchantmentLevel", material.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT_LEVEL));
        json.put("armorEnchantment", material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT));
        json.put("armorEnchantmentLevel", material.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT_LEVEL));
        json.put("unifiable", material.getProperty(GTMaterialProperties.UNIFIABLE));
        json.put("densityMultiplier", material.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER));
        json.put("densityDivider", material.getProperty(GTMaterialProperties.DENSITY_DIVIDER));
        json.put("steamMultiplier", material.getProperty(GTMaterialProperties.STEAM_MULTIPLIER));
        json.put("gasMultiplier", material.getProperty(GTMaterialProperties.GAS_MULTIPLIER));
        json.put("plasmaMultiplier", material.getProperty(GTMaterialProperties.PLASMA_MULTIPLIER));
        json.put("generationFlags", dumpMlGenerationFlags(material.getProperty(GTMaterialProperties.GENERATION_FLAGS)));
        json.put("hasCorrespondingFluid", material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_FLUID));
        json.put("hasCorrespondingGas", material.getProperty(GTMaterialProperties.HAS_CORRESPONDING_GAS));
        json.put("hasElectrolyzerRecipe", material.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE));
        json.put("hasCentrifugeRecipe", material.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE));
        json.put("hasGas", material.getProperty(GTMaterialProperties.HAS_GAS));
        json.put("enforceOreDictUnification", material.getProperty(GTMaterialProperties.ENFORCE_ORE_DICT_UNIFICATION));
        json.put("hasChemicalRecipe", material.getProperty(GTMaterialProperties.HAS_CHEMICAL_RECIPE));
        json.put("hasMixerRecipe", material.getProperty(GTMaterialProperties.HAS_MIXER_RECIPE));
        json.put("hasSifterRecipe", material.getProperty(GTMaterialProperties.HAS_SIFTER_RECIPE));
        json.put(
            "hasMetalCraftingSolidifierRecipe",
            MaterialDumpData.hasMetalCraftingSolidifierRecipe(material.getName()));
        json.put("hasMetalSolidifierRecipe", MaterialDumpData.hasMetalSolidifierRecipe(material.getName()));
        json.put("canBeCracked", material.getProperty(GTMaterialProperties.CAN_BE_CRACKED));
        json.put("hasGlowingOre", material.getProperty(GTMaterialProperties.HAS_GLOWING_ORE));
        json.put("processingMaterialTierEU", material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU));
        json.put("addedPrefixes", material.getProperty(GTMaterialProperties.ADDED_PREFIXES));
        json.put("removedPrefixes", material.getProperty(GTMaterialProperties.REMOVED_PREFIXES));
        json.put("werkstoff", dumpMlWerkstoff(material));
        json.put("gtpp", dumpMlGtpp(material));
        return json;
    }

    /// Serializes the gtPlusPlus-specific data, or null when `material` carries none (see
    /// [GTMaterialProperties#GTPP_STATE]). Scalars shared with the other material origins -- tier, voltage
    /// multiplier, melting and boiling point, durability, blast-furnace use, radioactivity, composition and
    /// the chemical formula -- are canonical properties serialized at the top level instead, and the
    /// proton/neutron counts are computed (see [gregtech.api.material.MaterialAtomics]).
    private static Map<String, Object> dumpMlGtpp(com.ruling_0.materiallib.api.Material material) {
        String state = material.getProperty(GTMaterialProperties.GTPP_STATE);
        if (state == null) return null;

        FluidNames legacyFluids = material.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
        boolean generatesFluid = MaterialDumpData.gtppGeneratesFluid(material.getName());

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("state", state);
        json.put("generatesFluid", generatesFluid);
        json.put("generatesCells", MaterialDumpData.gtppGeneratesCells(material.getName()));
        json.put("fluidName", generatesFluid && legacyFluids != null ? legacyFluids.legacyGtppFluidName() : null);
        json.put("plasmaName", generatesFluid ? material.getProperty(GTMaterialProperties.GTPP_PLASMA_NAME) : null);
        return json;
    }

    /// Serializes the bartworks-specific data, or null when `material` carries none (see
    /// [GTMaterialProperties#WERKSTOFF_IDS]). Scalars shared with the other material origins -- melting and
    /// boiling point, melting voltage, tool-stat overrides, EBF gas multipliers, mix circuit, sub tags,
    /// contents, ore byproducts, the chemical formula, and the toxic/radioactive/blast-furnace/auto-recipe
    /// flags -- are canonical properties serialized at the top level instead, and the proton/mass counts are
    /// computed (see [gregtech.api.material.MaterialAtomics]).
    private static Map<String, Object> dumpMlWerkstoff(com.ruling_0.materiallib.api.Material material) {
        List<Integer> ids = material.getProperty(GTMaterialProperties.WERKSTOFF_IDS);
        if (ids == null) return null;

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("ids", ids);
        json.put("type", material.getProperty(GTMaterialProperties.WERKSTOFF_TYPE));
        json.put("pool", MaterialDumpData.werkstoffPool(material.getName()));
        // Always empty: every recipe-gen flag is a top-level canonical property. Emitted so the JSON shape
        // stays stable.
        json.put("flags", List.of());
        json.put("prefixes", orEmpty(material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES)));
        return json;
    }

    private static <T> List<T> orEmpty(List<T> value) {
        return value != null ? value : List.of();
    }

    private static int orDefault(Integer value, int fallback) {
        return value != null ? value : fallback;
    }

    private static long orDefault(Long value, long fallback) {
        return value != null ? value : fallback;
    }

    private static float orDefault(Float value, float fallback) {
        return value != null ? value : fallback;
    }

    private static double orDefault(Double value, double fallback) {
        return value != null ? value : fallback;
    }

    private static String orDefault(String value, String fallback) {
        return value != null ? value : fallback;
    }

    private static List<String> dumpMlGenerationFlags(EnumSet<GTMaterialGenerationFlag> flags) {
        List<String> out = new ArrayList<>();
        if (flags == null) return out;
        for (GTMaterialGenerationFlag flag : flags) out.add(flag.name());
        Collections.sort(out);
        return out;
    }

    private static List<String> dumpMlShapes(com.ruling_0.materiallib.api.Material material) {
        List<String> out = new ArrayList<>();
        for (Shape shape : material.getShapes()) out.add(shape.getName());
        Collections.sort(out);
        return out;
    }

    private static List<String> dumpMlFamilies(com.ruling_0.materiallib.api.Material material) {
        List<String> out = new ArrayList<>();
        for (Family family : material.getFamilies()) out.add(family.getName());
        Collections.sort(out);
        return out;
    }

    private static List<Map<String, Object>> dumpMlMaterialRefStacks(List<MaterialRefStack> stacks) {
        List<Map<String, Object>> out = new ArrayList<>();
        if (stacks == null) return out;
        for (MaterialRefStack stack : stacks) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put(
                "material",
                stack.material()
                    .name());
            json.put("amount", stack.amount());
            out.add(json);
        }
        return out;
    }

    private static String dumpMlMaterialRef(MaterialRef ref) {
        return ref != null ? ref.name() : null;
    }

    private static List<String> dumpMlMaterialRefNames(List<MaterialRefStack> stacks) {
        List<String> out = new ArrayList<>();
        if (stacks == null) return out;
        for (MaterialRefStack stack : stacks) out.add(
            stack.material()
                .name());
        return out;
    }

    private static List<String> dumpMlFlags(EnumSet<GTMaterialFlag> flags) {
        List<String> out = new ArrayList<>();
        if (flags == null) return out;
        for (GTMaterialFlag flag : flags) out.add(flag.name());
        Collections.sort(out);
        return out;
    }

    private static List<Map<String, Object>> dumpMlAspects(List<AspectRefStack> aspects) {
        List<Map<String, Object>> out = new ArrayList<>();
        if (aspects == null) return out;
        for (AspectRefStack aspect : aspects) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("name", aspect.name());
            json.put("amount", aspect.amount());
            out.add(json);
        }
        return out;
    }

    private static Map<String, Object> dumpMlFluids(FluidNames fluids) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("solid", dumpMlFluidRef(fluids != null ? fluids.solid() : null));
        json.put("fluid", dumpMlFluidRef(fluids != null ? fluids.fluid() : null));
        json.put("gas", dumpMlFluidRef(fluids != null ? fluids.gas() : null));
        json.put("plasma", dumpMlFluidRef(fluids != null ? fluids.plasma() : null));
        json.put("molten", dumpMlFluidRef(fluids != null ? fluids.molten() : null));
        return json;
    }

    private static List<Map<String, Object>> dumpMlFluidRefList(List<FluidRef> refs) {
        List<Map<String, Object>> out = new ArrayList<>();
        if (refs == null) return out;
        for (FluidRef ref : refs) out.add(dumpMlFluidRef(ref));
        return out;
    }

    private static Map<String, Object> dumpMlFluidRef(FluidRef ref) {
        if (ref == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", ref.name());
        json.put("temperature", ref.temperature());
        json.put("texture", ref.texture());
        return json;
    }

    // endregion

    // region fluid-textures.json

    /// The still-icon texture path each legacy fluid name registered, captured while `gt.dumpMaterialData`
    /// made every material's legacy fluid builder actually construct (see [GTFluid#DUMP_TEXTURES] and its
    /// `GTProxy`/`LoaderGTBlockFluid` dump-mode bypasses of the skip-when-already-wired checks) -- ground
    /// truth for [Materials2FluidShapes]'s per-material icon path override.
    private static Map<String, String> dumpFluidTextures() {
        return new TreeMap<>(GTFluid.DUMP_TEXTURES);
    }

    // endregion

    // region legacy-variants.json

    /// Every (metaItemName, prefixName, materialName, damage) tuple a [MetaGeneratedItemX32] constructor
    /// actually created, captured while `gt.dumpMaterialData` bypassed the item-cutover skip. Ground truth for
    /// which shapes had a real legacy item: capability bits (`doGenerateItem`) can be set for a prefix that
    /// never held a constructor slot, and can drift between construction time and this dump.
    private static List<Map<String, Object>> dumpLegacyVariants() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (MetaGeneratedItemX32.LegacyVariant variant : MetaGeneratedItemX32.DUMP_VARIANTS) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("metaItem", variant.metaItemName());
            json.put("prefix", variant.prefixName());
            json.put("material", variant.materialName());
            json.put("damage", variant.damage());
            out.add(json);
        }
        return out;
    }

    // endregion

    // region legacy-blocks.json

    /// Every (blockField, meta, materialName, iconName) entry the legacy `gregtech.common.blocks.BlockMetal`
    /// storage-block instances hold, read directly off their `mMats`/`mBlockIcons` arrays -- ground truth for
    /// `block` `OrePrefixes` membership, since (unlike every other prefix) `block` generates through this
    /// hand-curated per-instance array rather than the generic capability-bit pipeline (its dumped
    /// `generationBits` is `0`, so it is absent from every material's `generatedPrefixes`). Read directly off
    /// live fields rather than a dump-mode capture: the arrays are static source-code literals with no
    /// construction-time drift to guard against, unlike [MetaGeneratedItemX32]'s capability-bit-driven item
    /// shapes. `iconName` is each material's legacy per-material art (e.g. `gregtech:iconsets/BLOCK_ADAMANTIUM`),
    /// captured positionally off the same `mBlockIcons` array `BlockMetal#getIcon` reads rather than derived from
    /// `material` by a naming rule, because the two hand-curated arrays (`Material[]` in `LoaderGTBlockFluid`,
    /// `IIconContainer[]` in `Textures.BlockIcons`) drift out of alphabetical/token sync in dozens of entries
    /// (e.g. `Spinel` renders `BLOCK_FOOLSRUBY`, `GarnetRed` renders `BLOCK_REDGARNET`).
    private static List<Map<String, Object>> dumpLegacyBlocks() {
        List<Map<String, Object>> out = new ArrayList<>();
        dumpLegacyBlock(out, "sBlockMetal1", GregTechAPI.sBlockMetal1);
        dumpLegacyBlock(out, "sBlockMetal2", GregTechAPI.sBlockMetal2);
        dumpLegacyBlock(out, "sBlockMetal3", GregTechAPI.sBlockMetal3);
        dumpLegacyBlock(out, "sBlockMetal4", GregTechAPI.sBlockMetal4);
        dumpLegacyBlock(out, "sBlockMetal5", GregTechAPI.sBlockMetal5);
        dumpLegacyBlock(out, "sBlockMetal6", GregTechAPI.sBlockMetal6);
        dumpLegacyBlock(out, "sBlockMetal7", GregTechAPI.sBlockMetal7);
        dumpLegacyBlock(out, "sBlockMetal8", GregTechAPI.sBlockMetal8);
        dumpLegacyBlock(out, "sBlockMetal9", GregTechAPI.sBlockMetal9);
        dumpLegacyBlock(out, "sBlockMetal10", GregTechAPI.sBlockMetal10);
        dumpLegacyBlock(out, "sBlockGem1", GregTechAPI.sBlockGem1);
        dumpLegacyBlock(out, "sBlockGem2", GregTechAPI.sBlockGem2);
        dumpLegacyBlock(out, "sBlockGem3", GregTechAPI.sBlockGem3);
        return out;
    }

    private static void dumpLegacyBlock(List<Map<String, Object>> out, String blockField,
        net.minecraft.block.Block block) {
        BlockMetal metal = (BlockMetal) block;
        for (int meta = 0; meta < metal.mMats.length; meta++) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("blockField", blockField);
            json.put("meta", meta);
            json.put("material", MU.internalName(metal.mMats[meta]));
            json.put(
                "iconName",
                metal.mBlockIcons[meta] instanceof GTBlockIconContainer gtIcon ? gtIcon.getIconName() : null);
            out.add(json);
        }
    }

    // endregion

    // region recipe-census.json

    /// Per-[RecipeMap] recipe count plus a stable digest of every recipe's item/fluid inputs and outputs, and
    /// the number of vanilla crafting-table recipes with a MaterialLib [ShapeItem] among their ingredients --
    /// ground truth for before/after parity checks on recipe generation (e.g. the `Processing*` ->
    /// `ShapeConsumer` dispatch). Every recipe map's digest list is written pre-sorted lexicographically, which is what
    /// makes this deterministic across runs regardless of a given [RecipeMapBackend]'s internal (possibly
    /// hash-based) iteration order: only the multiset of digests is ground truth, never their order.
    private static Map<String, Object> dumpRecipeCensus() {
        List<Map<String, Object>> recipeMaps = new ArrayList<>();
        List<String> mapNames = new ArrayList<>(RecipeMap.ALL_RECIPE_MAPS.keySet());
        Collections.sort(mapNames);
        for (String name : mapNames) {
            RecipeMap<?> recipeMap = RecipeMap.ALL_RECIPE_MAPS.get(name);
            List<String> digests = new ArrayList<>();
            for (GTRecipe recipe : recipeMap.getAllRecipes()) digests.add(digestRecipe(recipe));
            Collections.sort(digests);

            Map<String, Object> json = new LinkedHashMap<>();
            json.put("name", name);
            json.put("count", digests.size());
            json.put("digests", digests);
            recipeMaps.add(json);
        }

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("recipeMaps", recipeMaps);
        root.put("craftingTableMlRecipes", dumpCraftingTableMlRecipeCount());
        return root;
    }

    private static String digestRecipe(GTRecipe recipe) {
        return "in=" + digestItems(recipe.mInputs)
            + "|out="
            + digestItems(recipe.mOutputs)
            + "|fin="
            + digestFluids(recipe.mFluidInputs)
            + "|fout="
            + digestFluids(recipe.mFluidOutputs);
    }

    private static String digestItems(ItemStack[] stacks) {
        if (stacks == null) return "";
        List<String> parts = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
            parts.add(
                (id != null ? id.toString() : "UNKNOWN") + ":"
                    + stack.getItemDamage()
                    + ":"
                    + stack.stackSize
                    + ":"
                    + stack.hasTagCompound());
        }
        Collections.sort(parts);
        return String.join(",", parts);
    }

    private static String digestFluids(FluidStack[] fluids) {
        if (fluids == null) return "";
        List<String> parts = new ArrayList<>();
        for (FluidStack fluid : fluids) {
            if (fluid == null) continue;
            parts.add(
                fluid.getFluid()
                    .getName() + ":"
                    + fluid.amount);
        }
        Collections.sort(parts);
        return String.join(",", parts);
    }

    /// Vanilla crafting-table recipes (the four common [IRecipe] implementations Forge and vanilla register:
    /// [ShapedOreRecipe], [ShapelessOreRecipe], [ShapedRecipes], [ShapelessRecipes]) that consume at least one
    /// MaterialLib [ShapeItem] -- a coarse parity signal, not a digest, since crafting-table recipes are not
    /// keyed by material the way [RecipeMap] recipes are.
    private static int dumpCraftingTableMlRecipeCount() {
        int count = 0;
        for (Object entry : CraftingManager.getInstance()
            .getRecipeList()) {
            if (entry instanceof IRecipe recipe && involvesMlItem(recipe)) count++;
        }
        return count;
    }

    private static boolean involvesMlItem(IRecipe recipe) {
        if (recipe instanceof ShapedOreRecipe shapedOre) {
            for (Object ingredient : shapedOre.getInput()) if (involvesMlItem(ingredient)) return true;
        } else if (recipe instanceof ShapelessOreRecipe shapelessOre) {
            for (Object ingredient : shapelessOre.getInput()) if (involvesMlItem(ingredient)) return true;
        } else if (recipe instanceof ShapedRecipes shaped) {
            for (ItemStack stack : shaped.recipeItems) if (isMlStack(stack)) return true;
        } else if (recipe instanceof ShapelessRecipes shapeless) {
            for (ItemStack stack : shapeless.recipeItems) if (isMlStack(stack)) return true;
        }
        return false;
    }

    private static boolean involvesMlItem(Object ingredient) {
        if (ingredient instanceof ItemStack stack) return isMlStack(stack);
        if (ingredient instanceof List<?>alternatives) {
            for (Object alternative : alternatives) {
                if (alternative instanceof ItemStack stack && isMlStack(stack)) return true;
            }
        }
        return false;
    }

    private static boolean isMlStack(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ShapeItem;
    }

    // endregion
}
