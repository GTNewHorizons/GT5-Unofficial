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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.ShapeItem;
import com.ruling_0.materiallib.api.StandardProperties;

import bartworks.system.material.Werkstoff;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.AspectRefStack;
import gregtech.api.material.FluidNames;
import gregtech.api.material.FluidRef;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialGenerationFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.GTWerkstoffFlag;
import gregtech.api.material.GTppData;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialRef;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.material.WerkstoffData;
import gregtech.api.material.WerkstoffRefStack;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.client.iconContainers.blocks.GTBlockIconContainer;
import gregtech.common.blocks.BlockMetal;
import gregtech.common.fluid.GTFluid;
import gtPlusPlus.core.material.Material;

/// Dumps the four legacy material systems -- GregTech `Materials`, `OrePrefixes`, bartworks `Werkstoff`, and
/// gtPlusPlus `Material` -- plus the resolved MaterialLib registry view of the stage-03 `Materials2` port, to
/// JSON, for consumption by the material unification tooling.
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
        write(new File(directory, "gt-materials.json"), dumpGtMaterials());
        write(new File(directory, "oreprefixes.json"), dumpOrePrefixes());
        write(new File(directory, "werkstoff.json"), dumpWerkstoff());
        write(new File(directory, "gtpp-materials.json"), dumpGtppMaterials());
        write(new File(directory, "ml-materials.json"), dumpMlMaterials());
        write(new File(directory, "legacy-variants.json"), dumpLegacyVariants());
        write(new File(directory, "fluid-textures.json"), dumpFluidTextures());
        write(new File(directory, "legacy-blocks.json"), dumpLegacyBlocks());
        write(new File(directory, "gtpp-ores.json"), dumpGtppOres());
        write(new File(directory, "werkstoff-fields.json"), dumpWerkstoffFields());
        write(new File(directory, "recipe-census.json"), dumpRecipeCensus(), COMPACT_GSON);
    }

    /// Maps every public static `Werkstoff` field of the pool declaration classes to its werkstoff id, so the
    /// stage-10 reconstruction flip can rewrite each field to a `byId(...)` lookup without parsing the
    /// declaration source (field names do not reliably match werkstoff names, e.g. `Bismuthinit` vs
    /// "Bismuthinite").
    private static List<Map<String, Object>> dumpWerkstoffFields() {
        List<Map<String, Object>> out = new ArrayList<>();
        Class<?>[] pools = { bartworks.system.material.WerkstoffLoader.class, goodgenerator.items.GGMaterial.class,
            gtnhlanth.common.register.WerkstoffMaterialPool.class,
            gtnhlanth.common.register.BotWerkstoffMaterialPool.class };
        for (Class<?> pool : pools) {
            for (java.lang.reflect.Field field : pool.getFields()) {
                if (field.getType() != Werkstoff.class) continue;
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
                try {
                    Werkstoff werkstoff = (Werkstoff) field.get(null);
                    Map<String, Object> json = new LinkedHashMap<>();
                    json.put("class", pool.getName());
                    json.put("field", field.getName());
                    json.put("id", werkstoff != null ? (int) werkstoff.getmID() : null);
                    out.add(json);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return out;
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

    // region gt-materials.json

    private static List<Map<String, Object>> dumpGtMaterials() {
        LinkedHashSet<Materials> materials = new LinkedHashSet<>();
        for (Materials material : GregTechAPI.sGeneratedMaterials) {
            if (material != null) materials.add(material);
        }
        materials.addAll(Materials.getAll());

        List<Map<String, Object>> out = new ArrayList<>();
        for (Materials material : materials) out.add(dumpGtMaterial(material));
        return out;
    }

    private static Map<String, Object> dumpGtMaterial(Materials material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", material.mName);
        json.put("localName", material.mDefaultLocalName);
        json.put("subId", material.mMetaItemSubID);
        json.put("rgba", toIntArray(material.mRGBa));
        json.put("moltenRgba", toIntArray(material.mMoltenRGBa));
        json.put("iconSet", material.mIconSet != null ? material.mIconSet.mSetName : null);
        json.put("color", material.mColor != null && material.mColor != Dyes._NULL ? material.mColor.name() : null);
        json.put("element", material.mElement != null ? material.mElement.name() : null);
        json.put("meltingPoint", material.mMeltingPoint);
        json.put("blastTemp", (int) material.mBlastFurnaceTemp);
        json.put("blastRequired", material.mBlastFurnaceRequired);
        json.put("autoBlast", material.mAutoGenerateBlastFurnaceRecipes);
        json.put("autoVacuum", material.mAutoGenerateVacuumFreezerRecipes);
        json.put("autoRecycle", material.mAutoGenerateRecycleRecipes);
        json.put("gasTemp", material.mGasTemp);
        json.put("fuelPower", material.mFuelPower);
        json.put("fuelType", material.mFuelType);
        json.put("heatDamage", material.mHeatDamage);
        json.put("toolSpeed", material.mToolSpeed);
        json.put("toolDurability", material.mDurability);
        json.put("toolQuality", material.mToolQuality);
        json.put("unifiable", material.mUnifiable);
        json.put("densityMultiplier", material.mDensityMultiplier);
        json.put("densityDivider", material.mDensityDivider);
        json.put("steamMultiplier", material.mSteamMultiplier);
        json.put("gasMultiplier", material.mGasMultiplier);
        json.put("plasmaMultiplier", material.mPlasmaMultiplier);
        json.put("generationFlags", dumpGenerationFlags(material));
        json.put("hasCorrespondingFluid", material.hasCorrespondingFluid());
        json.put("hasCorrespondingGas", material.hasCorrespondingGas());
        json.put("hasElectrolyzerRecipe", (material.mExtraData & 1) != 0);
        json.put("hasCentrifugeRecipe", (material.mExtraData & 2) != 0);
        json.put("canBeCracked", material.canBeCracked());
        json.put("hasGlowingOre", material.hasGlowingOre());
        json.put("processingMaterialTierEU", material.getProcessingMaterialTierEU());
        json.put("enchants", dumpEnchants(material));
        json.put("subTags", dumpSubTags(material));
        json.put("composition", dumpComposition(material.mMaterialList));
        json.put("smeltInto", selfOrNull(material, material.mSmeltInto));
        json.put("macerateInto", selfOrNull(material, material.mMacerateInto));
        json.put("arcSmeltInto", selfOrNull(material, material.mArcSmeltInto));
        json.put("directSmelting", selfOrNull(material, material.mDirectSmelting));
        json.put("handleMaterial", selfOrNull(material, material.mHandleMaterial));
        json.put("materialInto", selfOrNull(material, material.mMaterialInto));
        json.put("oreByProducts", dumpMaterialNames(material.mOreByProducts));
        json.put("oreMultiplier", material.mOreMultiplier);
        json.put("byProductMultiplier", material.mByProductMultiplier);
        json.put("smeltingMultiplier", material.mSmeltingMultiplier);
        json.put("aspects", dumpAspects(material.mAspects));
        json.put("fluids", dumpFluids(material));
        json.put("crackedFluids", dumpCrackedFluids(material));
        json.put("hasGas", material.mHasGas);
        json.put("generatedPrefixes", dumpGeneratedPrefixes(material));
        json.put("addedPrefixes", dumpAddedPrefixes(material));
        json.put("removedPrefixes", dumpRemovedPrefixes(material));
        json.put("prefixLocalNameOverrides", dumpPrefixLocalNameOverrides(material));
        return json;
    }

    /// Cutover shapes (see [MU]) whose legacy display name differs from what MaterialLib's plain `%s`
    /// substitution would produce, so `gen_lang.py` can emit a lang override to keep display names identical
    /// after the item cutover. The legacy name goes through [OrePrefixes#getLocalizedNameForItem], which,
    /// unlike MaterialLib's default substitution, can apply an exact per-material lang override or an
    /// inflection (`.phrase`) key. Where the `gt.oreprefix.*` format key is absent from the lang files
    /// loaded in this environment (the translate call falls back to the raw key), the intended English
    /// default is computed from [OrePrefixes#getDefaultLocalNameForItem]'s pure-Java format instead.
    private static List<Map<String, Object>> dumpPrefixLocalNameOverrides(Materials material) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (MU.shape(prefix) == null || !prefix.doGenerateItem(material)) continue;
            String legacyName = prefix.getLocalizedNameForItem(material);
            if (legacyName.startsWith("gt.oreprefix.")) {
                legacyName = prefix.getDefaultLocalNameForItem(material);
            }
            String mlDefaultName = String
                .format(prefix.getMaterialPrefix() + "%s" + prefix.getMaterialPostfix(), material.mDefaultLocalName);
            if (legacyName.equals(mlDefaultName)) continue;
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("prefix", prefix.getName());
            json.put("legacyName", legacyName);
            json.put("mlDefaultName", mlDefaultName);
            out.add(json);
        }
        return out;
    }

    /// `MaterialBuilder#addOrePrefix` exceptions: prefixes this material generates regardless of its
    /// {@link #dumpGenerationFlags generation flags}, e.g. Iron's `nanite`.
    private static List<String> dumpAddedPrefixes(Materials material) {
        List<String> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.mGeneratedItems.contains(material)) out.add(prefix.getName());
        }
        return out;
    }

    /// `MaterialBuilder#removeOrePrefix` exceptions: prefixes this material never generates despite its
    /// {@link #dumpGenerationFlags generation flags}, e.g. Iron's `ingot` (vanilla already supplies one).
    private static List<String> dumpRemovedPrefixes(Materials material) {
        List<String> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.mNotGeneratedItems.contains(material)) out.add(prefix.getName());
        }
        return out;
    }

    private static String selfOrNull(Materials owner, Materials value) {
        return value == null || value == owner ? null : value.mName;
    }

    private static Map<String, Object> dumpEnchants(Materials material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("tool", dumpEnchant(material.mToolEnchantment, material.mToolEnchantmentLevel));
        json.put("armor", dumpEnchant(material.mArmorEnchantment, material.mArmorEnchantmentLevel));
        return json;
    }

    private static Map<String, Object> dumpEnchant(Enchantment enchantment, int level) {
        if (enchantment == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", enchantment.getName());
        json.put("level", level);
        return json;
    }

    private static List<String> dumpGenerationFlags(Materials material) {
        List<String> flags = new ArrayList<>();
        if (material.hasDustItems()) flags.add("DUST");
        if (material.hasMetalItems()) flags.add("METAL");
        if (material.hasGemItems()) flags.add("GEM");
        if (material.hasOresItems()) flags.add("ORE");
        if (material.hasCell()) flags.add("CELL");
        if (material.hasPlasma()) flags.add("PLASMA");
        if (material.hasToolHeadItems()) flags.add("TOOL_HEAD");
        if (material.hasGearItems()) flags.add("GEAR");
        if (material.hasEmpty()) flags.add("EMPTY");
        return flags;
    }

    private static List<String> dumpSubTags(Materials material) {
        List<String> tags = new ArrayList<>();
        for (SubTag tag : SubTag.sSubTags.values()) {
            if (material.contains(tag)) tags.add(tag.mName);
        }
        Collections.sort(tags);
        return tags;
    }

    private static List<Map<String, Object>> dumpComposition(List<MaterialStack> stacks) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (MaterialStack stack : stacks) out.add(dumpMaterialStack(stack));
        return out;
    }

    private static Map<String, Object> dumpMaterialStack(MaterialStack stack) {
        if (stack == null || stack.mMaterial == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("material", stack.mMaterial.mName);
        json.put("amount", stack.mAmount);
        return json;
    }

    private static List<String> dumpMaterialNames(List<Materials> materials) {
        List<String> out = new ArrayList<>();
        for (Materials material : materials) if (material != null) out.add(material.mName);
        return out;
    }

    private static List<Map<String, Object>> dumpAspects(List<TCAspects.TC_AspectStack> aspects) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (TCAspects.TC_AspectStack aspect : aspects) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("name", aspect.mAspect.name());
            json.put("amount", aspect.mAmount);
            out.add(json);
        }
        return out;
    }

    private static Map<String, Object> dumpFluids(Materials material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("solid", dumpFluid(material.mSolid));
        json.put("fluid", dumpFluid(material.mFluid));
        json.put("gas", dumpFluid(material.mGas));
        json.put("plasma", dumpFluid(material.mPlasma));
        json.put("molten", dumpFluid(material.mStandardMoltenFluid));
        return json;
    }

    private static Map<String, Object> dumpFluid(Fluid fluid) {
        if (fluid == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("name", fluid.getName());
        json.put("temperature", fluid.getTemperature());
        return json;
    }

    /// The material's hydro/steam-cracked fluids (light/moderate/severe severity each), captured while dumping
    /// since [GTProxy#addAutoGeneratedHydroCrackedFluids]/`addAutoGeneratedSteamCrackedFluids` populate them
    /// during preload, well before this load-complete dump runs. Null for a material that was never cracked
    /// (its uncracked source fluid was absent when GT generated cracked fluids).
    private static Map<String, Object> dumpCrackedFluids(Materials material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("hydroCracked", dumpCrackedFluidArray(material.getHydroCrackedFluids()));
        json.put("steamCracked", dumpCrackedFluidArray(material.getSteamCrackedFluids()));
        return json;
    }

    private static List<Map<String, Object>> dumpCrackedFluidArray(Fluid[] fluids) {
        if (fluids == null || fluids[0] == null) return null;
        List<Map<String, Object>> out = new ArrayList<>();
        for (Fluid fluid : fluids) out.add(dumpFluid(fluid));
        return out;
    }

    private static List<String> dumpGeneratedPrefixes(Materials material) {
        List<String> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.doGenerateItem(material)) out.add(prefix.getName());
        }
        return out;
    }

    private static int[] toIntArray(short[] values) {
        int[] out = new int[values.length];
        for (int i = 0; i < values.length; i++) out[i] = values[i];
        return out;
    }

    // endregion

    // region oreprefixes.json

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

    /// Keyed by [MaterialIconRegistry.IconType] ordinal. The plan for this dump assumed 192 slots sourced
    /// directly from `TextureSet`; the actual per-slot suffixes live on `MaterialIconRegistry.IconType`, which
    /// currently declares 158 entries -- see the stage-01 report for details.
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
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) out.add(dumpWerkstoff(werkstoff));
        return out;
    }

    private static Map<String, Object> dumpWerkstoff(Werkstoff werkstoff) {
        int id = werkstoff.getId();
        short[] rgba = werkstoff.getRGBA();
        Werkstoff.Stats stats = werkstoff.getStats();
        Materials bridge = werkstoff.getBridgeMaterial();

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("id", id);
        json.put("name", werkstoff.getDefaultName());
        json.put("varName", werkstoff.getVarName());
        json.put("rgb", new int[] { rgba[0], rgba[1], rgba[2] });
        json.put("texSet", werkstoff.getTexSet() != null ? werkstoff.getTexSet().mSetName : null);
        json.put(
            "type",
            werkstoff.getType()
                .name());
        json.put("pool", werkstoffPool(id));
        json.put("isProxy", stats.isProxy());
        json.put("bridgeMaterial", bridge != null ? bridge.mName : null);
        json.put("meltingPoint", stats.getMeltingPoint());
        json.put("boilingPoint", stats.getBoilingPoint());
        json.put("protons", stats.getProtons());
        json.put("neutrons", stats.getNeutrons());
        json.put("mass", stats.getMass());
        json.put("meltingVoltage", stats.getMeltingVoltage());
        json.put("durability", stats.getDurOverride());
        json.put("speed", stats.getSpeedOverride());
        json.put("quality", stats.getQualityOverride());
        json.put("sublimation", stats.isSublimation());
        json.put("toxic", stats.isToxic());
        json.put("radioactive", stats.isRadioactive());
        json.put("blastFurnace", stats.isBlastFurnace());
        json.put("elektrolysis", stats.isElektrolysis());
        json.put("centrifuge", stats.isCentrifuge());
        json.put("gas", stats.isGas());
        json.put("contents", dumpWerkstoffContents(werkstoff));
        json.put("oreByProducts", dumpWerkstoffOreByProducts(werkstoff));
        json.put("generatedPrefixes", dumpWerkstoffGeneratedPrefixes(werkstoff));

        Werkstoff.GenerationFeatures features = werkstoff.getGenerationFeatures();
        json.put("enforceUnification", features.enforceUnification);
        json.put("chemicalRecipes", features.hasChemicalRecipes());
        json.put("metalCraftingSolidifierRecipes", features.hasMetalCraftingSolidifierRecipes());
        json.put("metalSolidifierRecipes", features.hasMetaSolidifierRecipes());
        json.put("mixerRecipes", features.hasMixerRecipes());
        json.put("sifterRecipes", features.hasSifterRecipes());
        json.put("mixCircuit", features.mixCircuit);
        json.put("ebfGasTimeMultiplier", stats.getEbfGasRecipeTimeMultiplier());
        json.put("ebfGasAmountMultiplier", stats.getEbfGasRecipeConsumedAmountMultiplier());
        json.put("durabilityModifier", stats.getDurMod());
        json.put("enchantmentLevel", stats.getEnchantmentlvl());
        json.put("autoBlastFurnaceRecipes", stats.autoGenerateBlastFurnaceRecipes());
        json.put("autoVacuumFreezerRecipes", stats.autoGenerateVacuumFreezerRecipes());
        json.put(
            "additionalOredict",
            werkstoff.getAdditionalOredict()
                .stream()
                .sorted()
                .collect(Collectors.toList()));
        json.put(
            "subTags",
            werkstoff.getExplicitSubTags()
                .stream()
                .map(tag -> tag.mName)
                .sorted()
                .collect(Collectors.toList()));
        json.put("formula", werkstoff.getFormulaTooltip());
        json.put("formulaLocalized", werkstoff.isFormulaNeededLocalized());
        return json;
    }

    /// Buckets a `Werkstoff` id into its owning pool by id range. The 11500..11599 block belongs to bartworks
    /// (`WerkstoffLoader` declares 11500-11503 directly, past the gtnhlanth block); ids outside every known
    /// range fall back to `"unknown"`.
    private static String werkstoffPool(int id) {
        if (id > 31765) return "gt-bridge-proxy";
        if (id >= 29900 && id <= 29999) return "gtnhlanth-bot";
        if (id >= 11500 && id <= 11599) return "bartworks";
        if (id >= 11000 && id <= 11499) return "gtnhlanth";
        if (id >= 10001 && id <= 10999) return "goodgenerator";
        if (id >= 1 && id <= 9999) return "bartworks";
        return "unknown";
    }

    private static List<Map<String, Object>> dumpWerkstoffContents(Werkstoff werkstoff) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Pair<ISubTagContainer, Integer> entry : werkstoff.getContents()
            .getValue()) {
            ISubTagContainer key = entry.getKey();
            Map<String, Object> json = new LinkedHashMap<>();
            if (key instanceof Materials material) {
                json.put("name", material.mName);
                json.put("kind", "material");
            } else if (key instanceof Werkstoff other) {
                json.put("name", other.getDefaultName());
                json.put("kind", "werkstoff");
            } else {
                json.put("name", String.valueOf(key));
                json.put("kind", "unknown");
            }
            json.put("amount", entry.getValue());
            out.add(json);
        }
        return out;
    }

    private static List<String> dumpWerkstoffOreByProducts(Werkstoff werkstoff) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < werkstoff.getNoOfByProducts(); i++) {
            ISubTagContainer byProduct = werkstoff.getOreByProductRaw(i);
            if (byProduct instanceof Materials material) out.add(material.mName);
            else if (byProduct instanceof Werkstoff other) out.add(other.getDefaultName());
        }
        return out;
    }

    /// PINNED-CAPTURE TRAP: the committed `scripts/mu/dumps/werkstoff.json` is a pre-stage-10 capture and
    /// must not be refreshed from a post-fold boot. `hasItemType` reflects `addItemsForGeneration`'s reroute
    /// loop, which removes any prefix whose item the oredict already provides -- on a post-fold tree that
    /// includes MaterialLib's own unified shapes (generated FROM this dump), so a refresh would shrink the
    /// ground truth toward whatever is already ported. The pinned capture is the reroute result under
    /// legacy-only conditions, i.e. what bartworks actually generated.
    private static List<String> dumpWerkstoffGeneratedPrefixes(Werkstoff werkstoff) {
        List<String> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (werkstoff.hasItemType(prefix)) out.add(prefix.getName());
        }
        return out;
    }

    // endregion

    // region gtpp-materials.json

    /// `Material.mMaterialMap` is a `HashSet`, so iteration order is not stable across launches; sort by
    /// unlocalized name to make the dump byte-identical between runs (see the `gtpp-materials.json` ordering
    /// note in the material-unification tooling).
    ///
    /// PINNED-CAPTURE TRAP: the committed `scripts/mu/dumps/gtpp-materials.json` is pinned at its stage-11
    /// commit-1 capture and must not be refreshed from a later boot. `Material#setTextureSet`'s composition
    /// heuristic is registration-order-sensitive for 15 materials (AceticAnhydride, CopperIISulfate,
    /// CopperIISulfatePentahydrate, CyanoaceticAcid, EglinSteel, Grisium, HydrogenCyanide, Indalloy140,
    /// Laurenium, Octiron, PotassiumNitrate, SodiumCyanide, SodiumNitrate, SolidAcidCatalystMixture,
    /// ThoriumHexafluoride), so a re-dump could capture a different `textureSet` for those than the codegen
    /// already committed for -- legacy itself varied this run-to-run, so the pinned capture is exactly as
    /// legacy-faithful as any other.
    private static List<Map<String, Object>> dumpGtppMaterials() {
        List<Material> materials = new ArrayList<>(Material.mMaterialMap);
        materials.sort(java.util.Comparator.comparing(Material::getUnlocalizedName));
        List<Map<String, Object>> out = new ArrayList<>();
        for (Material material : materials) out.add(dumpGtppMaterial(material));
        return out;
    }

    private static Map<String, Object> dumpGtppMaterial(Material material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("unlocalizedName", material.getUnlocalizedName());
        json.put("localName", material.getDefaultLocalName());
        json.put(
            "state",
            material.getState() != null ? material.getState()
                .name() : null);
        json.put("rgba", toIntArray(material.getRGBA()));
        json.put("textureSet", material.getTextureSet() != null ? material.getTextureSet().mSetName : null);
        json.put("meltingPointC", material.getMeltingPointC());
        json.put("boilingPointC", material.getBoilingPointC());
        json.put("protons", material.getProtons());
        json.put("neutrons", material.getNeutrons());
        json.put("tier", material.vTier);
        json.put("voltageMultiplier", material.vVoltageMultiplier);
        json.put("chemicalFormula", material.vChemicalFormula);
        json.put("durability", material.vDurability);
        json.put("usesBlastFurnace", material.requiresBlastFurnace());
        json.put("isRadioactive", material.isRadioactive);
        json.put("radiationLevel", material.vRadiationLevel);
        json.put("hasOre", material.hasOre());
        json.put("werkstoffID", material.werkstoffID);
        json.put("composition", dumpGtppComposition(material.getComposites()));
        json.put("fluids", dumpGtppFluids(material));
        json.put("generatedParts", dumpGtppGeneratedParts(material));
        return json;
    }

    /// The authoritative per-material generated-part list, read from the live item registry
    /// (`Material.mComponentMap`, populated by every `BaseItemComponent`/`BaseOreComponent` constructor)
    /// rather than re-derived from `MaterialGenerator`'s procedural state/flag logic. Covers plates, ingots,
    /// dusts, and the rest of the part set, and -- for ore materials -- the crushed/raw ore chain, since both
    /// item families register into the same map. Sorted by ore-prefix name for determinism.
    private static List<Map<String, Object>> dumpGtppGeneratedParts(Material material) {
        List<Map<String, Object>> out = new ArrayList<>();
        Map<String, ItemStack> parts = Material.mComponentMap.get(material.getUnlocalizedName());
        if (parts == null) return out;
        List<String> prefixes = new ArrayList<>(parts.keySet());
        Collections.sort(prefixes);
        for (String prefix : prefixes) {
            ItemStack stack = parts.get(prefix);
            if (stack == null || stack.getItem() == null) continue;
            UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
            if (id == null) continue;
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("prefix", prefix);
            json.put("registryName", id.modId + ":" + id.name);
            json.put("meta", stack.getItemDamage());
            out.add(json);
        }
        return out;
    }

    private static List<Map<String, Object>> dumpGtppComposition(List<gtPlusPlus.core.material.MaterialStack> stacks) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (gtPlusPlus.core.material.MaterialStack stack : stacks) {
            Material inner = stack.getStackMaterial();
            if (inner == null) continue;
            Map<String, Object> json = new LinkedHashMap<>();
            json.put("name", inner.getUnlocalizedName());
            json.put("amount", stack.getPartsPerOneHundred());
            out.add(json);
        }
        return out;
    }

    private static Map<String, Object> dumpGtppFluids(Material material) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put(
            "fluid",
            material.getFluid() != null ? material.getFluid()
                .getName() : null);
        json.put(
            "plasma",
            material.getPlasma() != null ? material.getPlasma()
                .getName() : null);
        return json;
    }

    // endregion

    // region gtpp-ores.json

    /// The legacy `BlockBaseOre` registry name (`domain:name`) of every gtpp material with `hasOre() == true`
    /// -- not itself a `generatedParts` prefix (see `dumpGtppGeneratedParts`), since `BlockBaseOre` never
    /// calls `registerComponentForMaterial`/populates `Material.mComponentMap` the way every other legacy part
    /// item does. Sourced by scanning the live block registry for `BlockBaseOre` instances directly (not via
    /// `Material#getOre`/`getOreBlock`'s oredict lookup, which -- unlike `mComponentMap` -- resolves whichever
    /// stack currently WINS the canonical `ore<Name>` association, no longer the legacy block once cut over).
    /// A supplementary, non-pinned dump (unlike `gtpp-materials.json`) mirroring `dumpLegacyBlocks`'s role for
    /// GT's own storage blocks -- refresh freely alongside `GTPPOreAdapter`'s Postea migration table. Sorted
    /// by unlocalized name for determinism.
    private static List<Map<String, Object>> dumpGtppOres() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (net.minecraft.block.Block block : cpw.mods.fml.common.registry.GameData.getBlockRegistry()
            .typeSafeIterable()) {
            if (!(block instanceof gtPlusPlus.core.block.base.BlockBaseOre oreBlock)) continue;

            UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(net.minecraft.item.Item.getItemFromBlock(block));
            if (id == null) continue;

            Map<String, Object> json = new LinkedHashMap<>();
            json.put(
                "unlocalizedName",
                oreBlock.getMaterialEx()
                    .getUnlocalizedName());
            json.put("registryName", id.modId + ":" + id.name);
            out.add(json);
        }
        out.sort(java.util.Comparator.comparing(m -> (String) m.get("unlocalizedName")));
        return out;
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
        json.put("blastTemp", material.getProperty(GTMaterialProperties.BLAST_TEMP));
        json.put("blastRequired", material.getProperty(GTMaterialProperties.BLAST_REQUIRED));
        json.put("gasTemp", material.getProperty(GTMaterialProperties.GAS_TEMP));
        json.put("fuelPower", material.getProperty(GTMaterialProperties.FUEL_POWER));
        json.put("fuelType", material.getProperty(GTMaterialProperties.FUEL_TYPE));
        json.put("heatDamage", material.getProperty(GTMaterialProperties.HEAT_DAMAGE));
        json.put("toolSpeed", material.getProperty(GTMaterialProperties.TOOL_SPEED));
        json.put("toolDurability", material.getProperty(GTMaterialProperties.DURABILITY));
        json.put("toolQuality", material.getProperty(GTMaterialProperties.TOOL_QUALITY));
        json.put("subId", material.getProperty(GTMaterialProperties.OLD_SUB_ID));
        json.put("moltenTint", material.getProperty(GTMaterialProperties.MOLTEN_TINT));
        json.put("element", material.getProperty(GTMaterialProperties.ELEMENT));
        json.put("composition", dumpMlMaterialRefStacks(material.getProperty(GTMaterialProperties.COMPOSITION)));
        json.put("smeltInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.SMELT_INTO)));
        json.put("macerateInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.MACERATE_INTO)));
        json.put("arcSmeltInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.ARC_SMELT_INTO)));
        json.put("directSmelting", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.DIRECT_SMELTING)));
        json.put("handleMaterial", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.HANDLE_MATERIAL)));
        json.put("materialInto", dumpMlMaterialRef(material.getProperty(GTMaterialProperties.MATERIAL_INTO)));
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
        json.put("canBeCracked", material.getProperty(GTMaterialProperties.CAN_BE_CRACKED));
        json.put("hasGlowingOre", material.getProperty(GTMaterialProperties.HAS_GLOWING_ORE));
        json.put("processingMaterialTierEU", material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU));
        json.put("addedPrefixes", material.getProperty(GTMaterialProperties.ADDED_PREFIXES));
        json.put("removedPrefixes", material.getProperty(GTMaterialProperties.REMOVED_PREFIXES));
        json.put("werkstoff", dumpMlWerkstoff(material.getProperty(GTMaterialProperties.WERKSTOFF)));
        json.put("gtpp", dumpMlGtpp(material.getProperty(GTMaterialProperties.GTPP)));
        return json;
    }

    private static Map<String, Object> dumpMlGtpp(GTppData data) {
        if (data == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("tier", data.tier());
        json.put("voltageMultiplier", data.voltageMultiplier());
        json.put("meltingPointK", data.meltingPointK());
        json.put("boilingPointK", data.boilingPointK());
        json.put("durability", data.durability());
        json.put("usesBlastFurnace", data.usesBlastFurnace());
        json.put("isRadioactive", data.isRadioactive());
        json.put("radiationLevel", data.radiationLevel());
        json.put("hasOre", data.hasOre());
        json.put("chemicalFormula", data.chemicalFormula());
        json.put("protons", data.protons());
        json.put("neutrons", data.neutrons());
        json.put("state", data.state());
        json.put("generatesFluid", data.generatesFluid());
        json.put("generatesCells", data.generatesCells());
        json.put("composition", dumpMlMaterialRefStacks(data.composition()));
        json.put("fluidName", data.fluidName());
        json.put("plasmaName", data.plasmaName());
        return json;
    }

    private static Map<String, Object> dumpMlWerkstoff(WerkstoffData data) {
        if (data == null) return null;
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("ids", data.ids());
        json.put("type", data.type());
        json.put("pool", data.pool());
        json.put("meltingPoint", data.meltingPoint());
        json.put("boilingPoint", data.boilingPoint());
        json.put("protons", data.protons());
        json.put("neutrons", data.neutrons());
        json.put("mass", data.mass());
        json.put("meltingVoltage", data.meltingVoltage());
        json.put("durabilityOverride", data.durabilityOverride());
        json.put("speedOverride", data.speedOverride());
        json.put("qualityOverride", data.qualityOverride());
        json.put("durabilityModifier", data.durabilityModifier());
        json.put("enchantmentLevel", data.enchantmentLevel());
        json.put("ebfGasTimeMultiplier", data.ebfGasTimeMultiplier());
        json.put("ebfGasAmountMultiplier", data.ebfGasAmountMultiplier());
        json.put("mixCircuit", data.mixCircuit());
        List<String> flags = new ArrayList<>();
        for (GTWerkstoffFlag flag : data.flags()) flags.add(flag.name());
        Collections.sort(flags);
        json.put("flags", flags);
        json.put("prefixes", data.prefixes());
        json.put("contents", dumpMlWerkstoffRefStacks(data.contents()));
        json.put("oreByProducts", dumpMlWerkstoffRefStacks(data.oreByProducts()));
        json.put("subTags", data.subTags());
        json.put("additionalOreDict", data.additionalOreDict());
        json.put("formula", data.formula());
        return json;
    }

    private static List<Map<String, Object>> dumpMlWerkstoffRefStacks(List<WerkstoffRefStack> stacks) {
        List<Map<String, Object>> out = new ArrayList<>();
        if (stacks == null) return out;
        for (WerkstoffRefStack stack : stacks) {
            Map<String, Object> json = new LinkedHashMap<>();
            json.put(
                "material",
                stack.material()
                    .name());
            json.put("amount", stack.amount());
            json.put("werkstoff", stack.werkstoff());
            out.add(json);
        }
        return out;
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
    /// actually created, captured while `gt.dumpMaterialData` bypassed the stage-05 cutover skip. Ground
    /// truth for which shapes had a real legacy item, since [#dumpGeneratedPrefixes] alone overcounts:
    /// capability bits (`doGenerateItem`) can be set for a prefix that never held a constructor slot, and can
    /// drift between construction time and this dump.
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
    /// `material` by a naming rule, because the two hand-curated arrays (`Materials[]` in `LoaderGTBlockFluid`,
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
            json.put("material", metal.mMats[meta].mName);
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
    /// ground truth for stage 09's before/after parity check on the `Processing*` -> `ShapeConsumer` recipe
    /// generation port. Every recipe map's digest list is written pre-sorted lexicographically, which is what
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
