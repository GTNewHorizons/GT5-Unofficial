package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import gregtech.GTLoggers;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public final class OreDictRegistrationHandler {

    private final GTProxy proxy;

    public OreDictRegistrationHandler(GTProxy proxy) {
        this.proxy = proxy;
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

    public final HashSet<ItemStack> registeredOres = new HashSet<>(32768);
    private final HashSet<OreDictRegistration> registrations = new HashSet<>();
    private boolean bufferRegistrationProcessing = true;

    public void registerOre(OreDictionary.OreRegisterEvent event) {
        ModContainer container = Loader.instance()
            .activeModContainer();
        String modId = container == null ? "UNKNOWN" : container.getModId();
        String originalModId = modId;

        if (GTOreDictUnificator.isRegisteringOres()) {
            modId = GregTech.ID;
        } else if (modId.equals(GregTech.ID)) {
            modId = "UNKNOWN";
        }

        if (event == null || event.Ore == null
            || event.Ore.getItem() == null
            || event.Name == null
            || event.Name.isEmpty()) {

            String reportingModId = originalModId.equals(GregTech.ID) ? "UNKNOWN" : originalModId;
            String message = reportingModId
                + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.";

            GTLoggers.GT_ORE_DICT_LOGGER.info(message);
            throw new IllegalArgumentException(message);
        }

        try {
            ItemStack stack = event.Ore;
            String oreName = event.Name;

            stack.stackSize = 1;

            // Skip Tinker Construct ore registrations except for blocks
            if (proxy.mIgnoreTcon && originalModId.equals(TinkerConstruct.ID)
                && !(stack.getItem() instanceof ItemBlock)) {
                return;
            }

            String oreOriginPath = modId + " -> " + oreName;
            if (!bufferRegistrationProcessing) {
                oreOriginPath = originalModId + " --Late--> " + oreName;
            }

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

    private boolean handleSpecialOreRegistration(String oreName, ItemStack stack, String oreOriginPath) {
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

    private void processOreRegistration(String oreName, ItemStack stack, String modId, String oreOriginPath) {
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

        Materials material = prefix.isMaterialBased() ? Materials.get(materialName) : Materials._NULL;

        OreDictAliases.registerAliases(oreName, stack);
        handleSpecialRegistration(stack, prefix, material, materialName);

        if (!prefix.isIgnored(material)) {
            prefix.add(GTUtility.copyAmount(1, stack));
        }

        if (prefix.isMaterialBased() && processMaterialRegistration(oreName, stack, prefix, material, oreOriginPath)) {
            return;
        }

        GTLoggers.GT_ORE_DICT_LOGGER.info(oreOriginPath);

        OreDictRegistration registration = new OreDictRegistration(oreName, stack, prefix, material, modId);

        if (registration.prefix.isUnifiable()) {
            GTOreDictUnificator.addAssociation(
                registration.prefix,
                registration.material,
                registration.stack,
                GTOreDictUnificator.isBlacklisted(registration.stack));
        }

        OreDictUnificationOverrides.capture(registration);

        if (bufferRegistrationProcessing) {
            registrations.add(registration);
        } else {
            registration.registerRecipes();
        }
    }

    private static void handleSpecialRegistration(ItemStack stack, OrePrefixes prefix, Materials material,
        String materialName) {

        if (prefix.isMaterialBased() && material == Materials._NULL) {
            return;
        }

        switch (prefix.getName()) {
            case "dye" -> GTOreDictUnificator.registerOre(OrePrefixes.dye, stack);
            case "gearGt" -> GTOreDictUnificator.registerOre(OrePrefixes.gear, material, stack);
            case "lens" -> {
                if (material.contains(SubTag.TRANSPARENT) && material.mColor != Dyes._NULL) {
                    String color = material.mColor.name();
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
            case "crafting" -> {
                if (materialName.equals("IndustrialDiamond")) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
        }
    }

    private static boolean processMaterialRegistration(String oreName, ItemStack stack, OrePrefixes prefix,
        Materials material, String oreOriginPath) {

        if (material != material.mMaterialInto) {
            GTOreDictUnificator.registerOre(prefix, material.mMaterialInto, stack);
            if (!GTOreDictUnificator.isRegisteringOres()) {
                GTLoggers.GT_ORE_DICT_LOGGER.info(
                    "{} uses a deprecated Material and is getting re-registered as {}",
                    oreOriginPath,
                    prefix.get(material.mMaterialInto));
            }
            return true;
        }

        if (material == Materials._NULL) {
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

        for (Materials reRegisteredMaterial : material.mOreReRegistrations) {
            GTOreDictUnificator.registerOre(prefix, reRegisteredMaterial, stack);
        }

        material.add(GTUtility.copyAmount(1, stack));

        if (GregTechAPI.sThaumcraftCompat != null && prefix.doGenerateItem(material) && !prefix.isIgnored(material)) {
            ArrayList<TCAspects.TC_AspectStack> aspects = new ArrayList<>();
            for (TCAspects.TC_AspectStack aspect : prefix.mAspects) {
                aspect.addToAspectList(aspects);
            }

            long materialAmount = prefix.getMaterialAmount();
            if (materialAmount >= GTValues.M || materialAmount < 0) {
                for (TCAspects.TC_AspectStack aspect : material.mAspects) {
                    aspect.addToAspectList(aspects);
                }
            }

            GregTechAPI.sThaumcraftCompat
                .registerThaumcraftAspectsToItem(GTUtility.copyAmount(1, stack), aspects, oreName);
        }

        return prefix.isUnifiable() && !material.mUnifiable;
    }

    @SuppressWarnings("deprecation")
    public void processBufferedRegistrations() {
        bufferRegistrationProcessing = false;

        ProgressManager.ProgressBar progressBar = proxy.isClientSide()
            ? ProgressManager.push("Register materials", registrations.size())
            : null;

        int progress = 5;
        int eventsUntilProgressLog = registrations.size() / 20 - 1;

        for (OreDictRegistration registration : registrations) {
            if (--eventsUntilProgressLog == 0) {
                GT_FML_LOGGER.info("Baking : {}%", progress);
                eventsUntilProgressLog = registrations.size() / 20 - 1;
                progress += 5;
            }

            if (progressBar != null) {
                progressBar.step(registration.material.getLocalizedName());
            }

            registration.registerRecipes();
        }

        if (progressBar != null) {
            ProgressManager.pop(progressBar);
        }

        registrations.clear();
    }

    public boolean isRegisteredOre(ItemStack stack) {
        return registeredOres.contains(stack);
    }
}
