package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTLoggers;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.CircuitComponents;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Wildcard;
import gregtech.api.material.AspectRefStack;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OreDictUtils;
import gregtech.common.config.OPStuff;

/// Turns every `OreDictionary.OreRegisterEvent` into GregTech's view of that stack: the prefix it belongs to,
/// the material it stands for, its aliases, its unification association, and (buffered until
/// [#processBufferedRegistrations]) the ore-processing recipes for it.
///
/// A material-based name resolves its material side through [MaterialUtils#byLegacyName]. A name naming no
/// material gets no association or recipes, except a name-only association for the names in
/// [#NAME_ONLY_MATERIALS]. Every transparent material with a dye joins that dye's `craftingLens` group through its
/// lens.
public final class OreDictRegistrationHandler {

    private OreDictRegistrationHandler() {}

    /// Material names of the tiered and circuit-component entries (`circuitBasic`, `batteryGood`,
    /// `componentCircuitResistor`), which name no material. Every item under such a name gets a name-only
    /// association, so recipes accept any of them.
    private static final Set<String> NAME_ONLY_MATERIALS = new HashSet<>();

    static {
        for (Circuits circuit : Circuits.values()) NAME_ONLY_MATERIALS.add(circuit.materialName());
        for (CircuitComponents component : CircuitComponents.values()) {
            NAME_ONLY_MATERIALS.add(component.materialName());
        }
    }

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
    /// the stack's item.
    public static void registerPreExistingOre(OreDictionary.OreRegisterEvent event) {
        GameRegistry.UniqueIdentifier id = event.Ore == null || event.Ore.getItem() == null ? null
            : GameRegistry.findUniqueIdentifierFor(event.Ore.getItem());
        String modId = id == null ? "UNKNOWN" : id.modId;
        registerOre(event, modId);
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

            registeredOres.add(stack);

            if (handleSpecialOreRegistration(oreName, stack, oreOriginPath)) {
                return;
            }

            processOreRegistration(oreName, stack, modId, oreOriginPath);
        } catch (Exception e) {
            GT_FML_LOGGER.error("Could not register ore (oredict name={}, item stack={})", event.Name, event.Ore, e);
        }
    }

    /// Whether GregTech ignores registrations under `oreName` and generates no recipes for its items.
    public static boolean isIgnoredName(String oreName) {
        return IGNORED_NAMES.contains(oreName);
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
            OreDictUtils.registerOre(oreName.replace(" ", ""), stack);
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
            case "stone" -> OreDictUtils.registerOre("stoneSmooth", stack);
            case "cobblestone" -> OreDictUtils.registerOre("stoneCobble", stack);
            case "shardOrder" -> OreDictUtils.registerOre(OrePrefixes.gem, Materials.InfusedOrder, stack);
            case "shardEntropy" -> OreDictUtils.registerOre(OrePrefixes.gem, Materials.InfusedEntropy, stack);
            case "itemRubber" -> OreDictUtils.registerOre(OrePrefixes.ingot, Materials.Rubber, stack);
            case "compressedAluminum" -> OreDictUtils.registerOre(OrePrefixes.compressed, Materials.Aluminium, stack);
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
        if (prefix.isMaterialBased() && !materialName.isEmpty() && !NAME_ONLY_MATERIALS.contains(materialName)) {
            material = MaterialUtils.byLegacyName(materialName);
        }

        OreDictAliases.registerAliases(oreName, stack, prefix, materialName);
        handleSpecialRegistration(stack, prefix, material, materialName);
        String aliasKey = material == null ? null : material.getProperty(GTMaterialProperties.ALIAS_KEY);
        if (aliasKey != null) {
            API.setAliases(stack, aliasKey);
        }
        if (!prefix.isIgnored(material)) {
            prefix.add(GTUtility.copyAmount(1, stack));
        }

        if (prefix.isMaterialBased() && processMaterialRegistration(oreName, stack, prefix, material, materialName)) {
            return;
        }

        GTLoggers.GT_ORE_DICT_LOGGER.info(oreOriginPath);

        Material registeredMaterial = material == null ? Materials._NULL : material;

        // GT++ registers cells for these materials that contain different fluids from their BartWorks counterparts.
        // They share OreDict names but must not unify. Remove this once the duplicate cells are removed.
        boolean excludeGtppCells = Mods.GTPlusPlus.ID.equals(modId)
            && (oreName.equals("cellIodine") || oreName.equals("cellNeon")
                || oreName.equals("cellKrypton")
                || oreName.equals("cellXenon"));

        if (prefix.isUnifiable() && !excludeGtppCells) {
            MaterialData.addAssociation(prefix, registeredMaterial, stack);
        }

        OreDictRegistration registration = new OreDictRegistration(oreName, stack, prefix, registeredMaterial);

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
            case "dye" -> OreDictUtils.registerOre(OrePrefixes.dye, stack);
            case "gearGt" -> {
                OreDictUtils.registerOre(OrePrefixes.gear, material, stack);
                for (Wildcard wildcard : Wildcard.containing(material)) {
                    OreDictUtils.registerOre(wildcard.oreDictName(OrePrefixes.gear), stack);
                }
            }
            case "lens" -> {
                if (MaterialUtils.hasFlag(material, GTMaterialFlag.TRANSPARENT)
                    && MaterialUtils.dye(material) != Dyes._NULL) {
                    String color = MaterialUtils.dye(material)
                        .name();
                    if (color.startsWith("dye")) color = color.substring(3);
                    OreDictUtils.registerOre("craftingLens" + color, stack);
                }
            }

            case "plank" -> {
                if (materialName.equals("Wood")) {
                    MaterialData.add(stack, new ItemData(Materials.Wood, GTValues.M));
                }
            }
            case "slab" -> {
                if (materialName.equals("Wood")) {
                    MaterialData.add(stack, new ItemData(Materials.Wood, GTValues.M / 2));
                }
            }

            case "stick" -> {
                if (!GTRecipeRegistrator.sRodMaterialList.contains(material)) {
                    GTRecipeRegistrator.sRodMaterialList.add(material);
                } else if (material == Materials.Wood) {
                    OreDictUtils.addToBlacklist(stack);
                }
            }
        }
    }

    /// Returns true when the registration stops here: an unknown material name or a material that never unifies.
    private static boolean processMaterialRegistration(String oreName, ItemStack stack, OrePrefixes prefix,
        @Nullable Material material, String materialName) {

        if (material == null) {
            if (NAME_ONLY_MATERIALS.contains(materialName)) {
                MaterialData.addAssociation(prefix, materialName, stack);
            }
            return true;
        }

        for (Wildcard wildcard : Wildcard.containing(material)) {
            OreDictUtils.registerOre(wildcard.oreDictName(prefix), stack);
        }

        if (GregTechAPI.sThaumcraftCompat != null && carriesAspects(prefix, material) && !prefix.isIgnored(material)) {
            ArrayList<TC_AspectStack> aspects = new ArrayList<>();
            for (TC_AspectStack aspect : prefix.aspects) {
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

    /// Whether items under `prefix` and `material` get Thaumcraft aspects: MaterialLib serves the pair, or the
    /// prefix names an ore on one stone type and the material has ores.
    private static boolean carriesAspects(OrePrefixes prefix, Material material) {
        if (material == null) return false;
        if (MaterialParts.isCutOver(prefix, material)) return true;
        if (!material.hasShape(OreShapes.ore)) return false;
        for (StoneType stoneType : StoneType.VALUES) {
            if (stoneType.getPrefix() == prefix) return true;
        }
        return false;
    }
}
