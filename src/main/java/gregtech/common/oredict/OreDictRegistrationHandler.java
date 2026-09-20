package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.MaterialLib;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTLoggers;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.RecognitionMaterials;
import gregtech.api.material.AspectRefStack;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.config.OPStuff;
import gregtech.loaders.materials.DeclaredMaterialNameTable;

/// Turns every `OreDictionary.OreRegisterEvent` into GregTech's view of that stack: the prefix it belongs to,
/// the material it stands for, its aliases, its unification association, and (buffered until
/// [#processBufferedRegistrations]) the ore-processing recipes for it.
///
/// A name's material side resolves in three tiers. The legacy name domain ([LegacyNameDomain]) is the
/// declared-materials table; a name outside it may still be a recognition marker ([RecognitionMaterials]),
/// which unifies and dispatches through `OrePrefixes#processRecognitionOre` rather than through a material;
/// and a werkstoff- or gtPlusPlus-reconstructed MaterialLib material ([#reconstructedMaterial]) is kept
/// rather than dropped as unknown. What the buffered registration finally carries is decided by
/// [#resolveCensusMaterial], MaterialLib-registry-first.
public final class OreDictRegistrationHandler {

    private OreDictRegistrationHandler() {}

    private static final HashSet<String> IGNORED_NAMES = new HashSet<>(
        Arrays.asList(
            // These would otherwise be interpreted as OrePrefixes.item + a valid GT material.
            "itemFlint",
            "itemClay",
            "itemRawRubber",
            "itemLazurite",
            "itemIridium",
            "itemTar",
            "itemForcicium",
            "itemForcillium",
            "itemHoney",
            "itemSulfuricAcid",
            "itemPotash",
            "itemBitumen",
            "itemMercury",
            "itemOsmium",
            "itemCoal",
            "itemSuperconductor",

            // These would otherwise resolve to a valid GT material form.
            "blockSand",
            "blockConcrete",
            "blockMarble",
            "blockRubber",
            "blockHoney",
            "blockCocoa",
            "blockPeat",
            "blockLeather",
            "blockHeeEndium",
            "ingotUnstable",
            "gemFluorite",
            "shardAir",
            "shardWater",
            "shardCrystal",
            "shardNether",
            "stickObsidian",

            // These match a prefix exactly, but that prefix is not self-referencing.
            // Without the hardcoded ignore, GT would treat them as invalid full-prefix
            // names and rename the stack to "Invalid OreDictionary Tag".
            "glowstone",
            "gravel",
            "soulsand",
            "grass",
            "obsidian",
            "chest",
            "dirt",
            "paper",
            "brick",

            // These resolve to non-material prefixes.
            // Without the hardcoded ignore, GT would add the stack directly to the corresponding OrePrefixes list.
            "wireMill",
            "cableRedNet",
            "dyeCrystal",
            "sandstone",
            "brickXyEngineering",
            "chunkLazurite",
            "breederUranium",
            "brSmallMachineCyaniteProcessor",
            "crafterWood"));

    private static final HashSet<String> INVALID_NAMES = new HashSet<>(
        Arrays.asList(
            // These match a prefix exactly.
            // stick and wood would otherwise be treated as invalid full-prefix names
            // and have their stacks renamed to "Invalid OreDictionary Tag".
            "stick",
            "wood",
            // blockWool is self-referencing and would otherwise be processed normally.
            "blockWool",

            // These resolve to non-material prefixes and would otherwise be added
            // directly to the corresponding OrePrefixes list.
            "alloyPlateEnergized",
            "alloyPlateEnergizedHardened",
            "bronzeRod",
            "bronzeStick",
            "bronzeTube",
            "obsidianRod",
            "obsidianStick",
            "planks",
            "woodRod"));

    private static final HashSet<ItemStack> registeredOres = new HashSet<>(32768);
    private static final ArrayList<OreDictRegistration> registrations = new ArrayList<>();
    private static boolean bufferRegistrationProcessing = true;

    public static void registerOre(OreDictionary.OreRegisterEvent event) {
        ModContainer container = Loader.instance()
            .activeModContainer();
        registerOre(event, container == null ? "UNKNOWN" : container.getModId());
    }

    /// Replays a registration that happened before this handler was listening, credited to the mod owning
    /// the stack's item. MaterialLib-owned stacks are GregTech's material items, so they count as GregTech's
    /// for [OreDictUnificationOverrides].
    public static void registerPreExistingOre(OreDictionary.OreRegisterEvent event) {
        GameRegistry.UniqueIdentifier id = event.Ore == null || event.Ore.getItem() == null ? null
            : GameRegistry.findUniqueIdentifierFor(event.Ore.getItem());
        String modId = id == null ? "UNKNOWN" : id.modId;
        registerOre(event, MaterialLib.MODID.equals(modId) ? Mods.GregTech.ID : modId);
    }

    private static void registerOre(OreDictionary.OreRegisterEvent event, String modId) {
        if (!GTMod.sMaterialsReady) return;

        if (event == null || event.Ore == null
            || event.Ore.getItem() == null
            || event.Name == null
            || event.Name.isEmpty()) {

            String message = modId
                + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Enum Constants! (well, the registration is too invalid to even be shown properly)";

            GTLoggers.GT_ORE_DICT_LOGGER.info(message);
            throw new IllegalArgumentException(message);
        }

        try {
            ItemStack stack = event.Ore;
            String oreName = event.Name;

            stack.stackSize = 1;

            // Skip Tinker Construct ore registrations except for blocks
            if (OPStuff.ignoreTinkerConstruct && modId.equals(TinkerConstruct.ID)
                && !(stack.getItem() instanceof ItemBlock)) {
                return;
            }

            String oreOriginPath = modId + (bufferRegistrationProcessing ? " -> " : " --Late--> ") + oreName;

            if (GTUtility.getBlockFromStack(stack) != Blocks.air) {
                GTOreDictUnificator.addToBlacklist(stack);
            }

            registeredOres.add(stack);

            if (handleSpecialOreRegistration(oreName, stack, oreOriginPath)) {
                return;
            }

            processOreRegistration(oreName, stack, modId, oreOriginPath);
        } catch (Exception e) {
            GT_FML_LOGGER.error("Could not register ore (oredict name={}, item stack={})", event.Name, event.Ore, e);
        }
    }

    private static boolean handleSpecialOreRegistration(String oreName, ItemStack stack, String oreOriginPath) {
        if (IGNORED_NAMES.contains(oreName)) {
            GTLoggers.GT_ORE_DICT_LOGGER.info("{} is getting ignored via hardcode.", oreOriginPath);
            return true;
        }

        if (INVALID_NAMES.contains(oreName)) {
            GTLoggers.GT_ORE_DICT_LOGGER.info("{} is wrongly registered and therefore getting ignored.", oreOriginPath);
            return true;
        }

        if (oreName.contains(" ")) {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} is getting re-registered because the OreDict Name containing invalid spaces.", oreOriginPath);
            GTOreDictUnificator.registerOre(oreName.replace(" ", ""), stack);
            stack.setStackDisplayName("Invalid OreDictionary Tag");
            return true;
        }

        if (oreName.contains("|") || oreName.contains("*")
            || oreName.contains(":")
            || oreName.contains(".")
            || oreName.contains("$")) {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} is using a private Prefix and is therefore getting ignored properly.", oreOriginPath);
            return true;
        }

        switch (oreName) {
            case "stone" -> GTOreDictUnificator.registerOre("stoneSmooth", stack);
            case "cobblestone" -> GTOreDictUnificator.registerOre("stoneCobble", stack);
            case "shardOrder" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedOrder, stack);
            case "shardEntropy" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEntropy, stack);
            case "itemRubber" -> GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Rubber, stack);
            case "compressedAluminum" -> GTOreDictUnificator
                .registerOre(OrePrefixes.compressed, Materials.Aluminium, stack);
            default -> {
                return false;
            }
        }

        return true;
    }

    private static void processOreRegistration(String oreName, ItemStack stack, String modId, String oreOriginPath) {
        OrePrefixes prefix = OrePrefixes.getOrePrefix(oreName);

        if (prefix == null) {
            GTLoggers.GT_ORE_DICT_LOGGER.info("{} prefix is null and won't be processed", oreOriginPath);
            return;
        }

        if (prefix == OrePrefixes.nugget && modId.equals(Thaumcraft.ID)
            && stack.getItem()
                .getUnlocalizedName()
                .contains("ItemResource")) {
            return;
        }

        if (prefix.skipActiveUnification()) {
            GTOreDictUnificator.addToBlacklist(stack);
        }

        String materialName = oreName.substring(
            prefix.getName()
                .length());

        if (materialName.isEmpty() && !prefix.isSelfReferencing()) {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} uses a Prefix as full OreDict Name, and is therefor invalid.", oreOriginPath);
            stack.setStackDisplayName("Invalid OreDictionary Tag");
            return;
        }

        Material material = null;
        Material recognitionMarker = null;
        if (prefix.isMaterialBased() && !materialName.isEmpty()) {
            material = LegacyNameDomain.lookup(materialName);
            if (material == null) {
                recognitionMarker = RecognitionMaterials.getRecognitionMarker(materialName);
            }
        }

        OreDictAliases.registerAliases(oreName, stack);
        handleSpecialRegistration(stack, prefix, material, materialName);

        if (recognitionMarker != null) {
            registerRecognitionOre(prefix, recognitionMarker, stack);
        } else if (!prefix.isIgnored(material)) {
            prefix.add(GTUtility.copyAmount(1, stack));
        }

        if (prefix.isMaterialBased() && recognitionMarker == null
            && processMaterialRegistration(oreName, stack, prefix, material, materialName, oreOriginPath)) {
            return;
        }

        GTLoggers.GT_ORE_DICT_LOGGER.info(oreOriginPath);

        Material censusMaterial = resolveCensusMaterial(materialName, material, recognitionMarker);
        Material censusMarker = isCensusMarker(recognitionMarker) ? recognitionMarker : null;

        // GT++ registers cells for these materials that contain different fluids from their BartWorks counterparts.
        // They share OreDict names but must not unify. Remove this once the duplicate cells are removed.
        boolean excludeGtppCells = Mods.GTPlusPlus.ID.equals(modId)
            && (oreName.equals("cellIodine") || oreName.equals("cellNeon")
                || oreName.equals("cellKrypton")
                || oreName.equals("cellXenon"));

        if (prefix.isUnifiable() && !excludeGtppCells) {
            GTOreDictUnificator.addAssociation(prefix, censusMaterial, stack);
        }

        OreDictRegistration registration = new OreDictRegistration(
            oreName,
            stack,
            prefix,
            censusMaterial,
            censusMarker,
            modId);
        OreDictUnificationOverrides.handle(registration);

        if (bufferRegistrationProcessing) {
            registrations.add(registration);
        } else {
            registration.registerRecipes();
        }
    }

    private static void handleSpecialRegistration(ItemStack stack, OrePrefixes prefix, @Nullable Material material,
        String materialName) {

        if (prefix.isMaterialBased() && material == null) {
            return;
        }

        switch (prefix.getName()) {
            case "dye" -> GTOreDictUnificator.registerOre(OrePrefixes.dye, stack);
            case "gearGt" -> GTOreDictUnificator.registerOre(OrePrefixes.gear, material, stack);
            case "lens" -> {
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.TRANSPARENT)
                    && MaterialUtils.dye(material) != Dyes._NULL) {
                    String color = MaterialUtils.dye(material)
                        .name();
                    if (color.startsWith("dye")) color = color.substring(3);
                    GTOreDictUnificator.registerOre("craftingLens" + color, stack);
                }
            }

            case "plate" -> {
                if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            case "plank" -> {
                if (materialName.equals("Wood")) {
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Wood, GTValues.M));
                }
            }
            case "slab" -> {
                if (materialName.equals("Wood")) {
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Wood, GTValues.M / 2));
                }
            }

            case "cell" -> {
                if (material == Materials.Empty) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            case "stick" -> {
                if (!GTRecipeRegistrator.sRodMaterialList.contains(material)) {
                    GTRecipeRegistrator.sRodMaterialList.add(material);
                } else if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
        }
    }

    /// Unifies a foreign ore-dictionary entry whose name resolves to a recognition marker rather than to a
    /// material in the legacy name domain (see [RecognitionMaterials#getRecognitionMarker]). The marker's
    /// crystal/gem aliases come from [OreDictAliases]; this adds the prefix membership and the gear alias.
    private static void registerRecognitionOre(OrePrefixes prefix, Material marker, ItemStack stack) {
        if (MaterialUtils.unifiable(marker)) {
            prefix.add(GTUtility.copyAmount(1, stack));
        }
        if (prefix == OrePrefixes.gearGt) {
            GTOreDictUnificator.registerOre(OrePrefixes.gear, marker, stack);
        }
    }

    /// Returns true when the registration stops here: an unknown material name (unless it is a reconstructed
    /// MaterialLib material) or a material that never unifies.
    private static boolean processMaterialRegistration(String oreName, ItemStack stack, OrePrefixes prefix,
        @Nullable Material material, String materialName, String oreOriginPath) {

        if (material == null) {
            if (reconstructedMaterial(materialName) != null) {
                return false;
            }
            for (Dyes dye : Dyes.VALUES) {
                String dyeName = dye.name()
                    .substring("dye".length());
                if (oreName.endsWith(dyeName)) {
                    GTOreDictUnificator.addToBlacklist(stack);
                    GTLoggers.GT_ORE_DICT_LOGGER.info("{} is a colored ore and is being blacklisted", oreOriginPath);
                    return true;
                }
            }
            return true;
        }

        for (Material reRegisteredMaterial : MaterialFacades.oreReRegistrationsOf(material)) {
            GTOreDictUnificator.registerOre(prefix, reRegisteredMaterial, stack);
        }

        if (GregTechAPI.sThaumcraftCompat != null && prefix.doGenerateItem(material) && !prefix.isIgnored(material)) {
            ArrayList<TC_AspectStack> aspects = new ArrayList<>();
            for (TC_AspectStack aspect : prefix.mAspects) {
                aspect.addToAspectList(aspects);
            }

            long materialAmount = prefix.getMaterialAmount();
            if (materialAmount >= GTValues.M || materialAmount < 0) {
                for (AspectRefStack aspect : MaterialUtils.aspects(material)) {
                    new TC_AspectStack(TCAspects.valueOf(aspect.name()), aspect.amount()).addToAspectList(aspects);
                }
            }

            GregTechAPI.sThaumcraftCompat
                .registerThaumcraftAspectsToItem(GTUtility.copyAmount(1, stack), aspects, oreName);
        }

        return prefix.isUnifiable() && !MaterialUtils.unifiable(material);
    }

    private static boolean isCensusMarker(@Nullable Material recognitionMarker) {
        return recognitionMarker != null && MaterialUtils.unifiable(recognitionMarker)
            && MaterialUtils.hasFlag(recognitionMarker, GTMaterialFlag.CRYSTALLISABLE);
    }

    /// The MaterialLib material an ore-dictionary registration carries downstream. A recognition marker that
    /// [#isCensusMarker] resolves to itself. Otherwise `materialName` resolves ML-registry-first, falling back
    /// to `material` (or the `NULL` placeholder) when the registry holds no shaped material under that name, or
    /// when `materialName` is one [DeclaredMaterialNameTable] freezes.
    private static Material resolveCensusMaterial(@Nullable String materialName, @Nullable Material material,
        @Nullable Material recognitionMarker) {
        if (isCensusMarker(recognitionMarker)) {
            return recognitionMarker;
        }
        if (materialName != null && !materialName.isEmpty() && !hasDeclaredMaterialsField(materialName)) {
            Material ml = MaterialLibAPI.getMaterial("gregtech", materialName);
            // A shapeless registry hit is a recognition/wildcard backing; those names resolve through the
            // legacy path instead.
            if (ml != null && !ml.getShapes()
                .isEmpty()) return ml;
        }
        return material != null ? material : Materials.NULL;
    }

    /// The reconstructed (werkstoff- or gtpp-owned) MaterialLib material registered under `materialName`, or
    /// null when the name is dropped as unknown. A werkstoff-origin material qualifies once
    /// [MaterialUtils#hasBridgeRegistration] records it; an event processed before that record exists resolves
    /// to null. A gtPlusPlus-originated material never gets that record and qualifies instead on
    /// [GTMaterialProperties#GTPP_STATE] plus a shaped registry hit.
    private static @Nullable Material reconstructedMaterial(@Nullable String materialName) {
        if (materialName == null || materialName.isEmpty() || hasDeclaredMaterialsField(materialName)) return null;
        Material ml = MaterialLibAPI.getMaterial("gregtech", materialName);
        if (ml == null) return null;
        if (MaterialUtils.hasBridgeRegistration(ml)) return ml;
        if (ml.getProperty(GTMaterialProperties.GTPP_STATE) == null) return null;
        return ml.getShapes()
            .isEmpty() ? null : ml;
    }

    /// Whether `name` is in the frozen domain [DeclaredMaterialNameTable] backs.
    private static boolean hasDeclaredMaterialsField(String name) {
        return DeclaredMaterialNameTable.NAMES.contains(name);
    }

    @SuppressWarnings("deprecation")
    public static void processBufferedRegistrations() {
        bufferRegistrationProcessing = false;

        ProgressManager.ProgressBar progressBar = ProgressManager.push("Register materials", registrations.size());

        int progress = 5;
        int eventsUntilProgressLog = registrations.size() / 20 - 1;

        for (OreDictRegistration registration : registrations) {
            if (--eventsUntilProgressLog == 0) {
                GT_FML_LOGGER.info("Baking : {}%", progress);
                eventsUntilProgressLog = registrations.size() / 20 - 1;
                progress += 5;
            }

            String label = MaterialUtils.localizedName(registration.material);
            progressBar.step(label == null ? "" : label);
            registration.registerRecipes();
        }

        ProgressManager.pop(progressBar);
        registrations.clear();
    }

    public static boolean isRegisteredOre(ItemStack stack) {
        return registeredOres.contains(stack);
    }
}
