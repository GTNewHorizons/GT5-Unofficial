package gregtech.common.misc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bartworks.system.material.Werkstoff;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTLog;
import gtPlusPlus.core.material.Material;

/// Dumps the four legacy material systems -- GregTech `Materials`, `OrePrefixes`, bartworks `Werkstoff`, and
/// gtPlusPlus `Material` -- to JSON, for consumption by the material unification tooling.
///
/// Triggered from `GTMod`'s `FMLLoadCompleteEvent` handler when the `gt.dumpMaterialData` system property is
/// set, so a headless server run can produce the dumps non-interactively.
public final class MaterialDataDump {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .disableHtmlEscaping()
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
    }

    private static void write(File file, Object data) {
        try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(data, writer);
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
        json.put("hasGas", material.mHasGas);
        json.put("generatedPrefixes", dumpGeneratedPrefixes(material));
        return json;
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

    private static List<String> dumpWerkstoffGeneratedPrefixes(Werkstoff werkstoff) {
        List<String> out = new ArrayList<>();
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (werkstoff.hasItemType(prefix)) out.add(prefix.getName());
        }
        return out;
    }

    // endregion

    // region gtpp-materials.json

    private static List<Map<String, Object>> dumpGtppMaterials() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Material material : Material.mMaterialMap) out.add(dumpGtppMaterial(material));
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
        return json;
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
}
