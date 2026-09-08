package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public final class OreDictRegistrationHandler {

    private final GTProxy proxy;

    public OreDictRegistrationHandler(GTProxy proxy) {
        this.proxy = proxy;
    }

    private static final HashSet<String> IGNORED_ITEMS = new HashSet<>(
        Arrays.asList(
            "itemGhastTear",
            "itemFlint",
            "itemClay",
            "itemBucketSaltWater",
            "itemBucketFreshWater",
            "itemBucketWater",
            "itemRock",
            "itemReed",
            "itemArrow",
            "itemSaw",
            "itemKnife",
            "itemHammer",
            "itemChisel",
            "itemRubber",
            "itemEssence",
            "itemIlluminatedPanel",
            "itemSkull",
            "itemRawRubber",
            "itemBacon",
            "itemJetpackAccelerator",
            "itemLazurite",
            "itemIridium",
            "itemTear",
            "itemClaw",
            "itemFertilizer",
            "itemTar",
            "itemSlimeball",
            "itemCoke",
            "itemBeeswax",
            "itemBeeQueen",
            "itemForcicium",
            "itemForcillium",
            "itemRoyalJelly",
            "itemHoneydew",
            "itemHoney",
            "itemPollen",
            "itemReedTypha",
            "itemSulfuricAcid",
            "itemPotash",
            "itemCompressedCarbon",
            "itemBitumen",
            "itemCokeSugar",
            "itemCokeCactus",
            "itemCharcoalSugar",
            "itemCharcoalCactus",
            "itemSludge",
            "itemEnrichedAlloy",
            "itemQuicksilver",
            "itemMercury",
            "itemOsmium",
            "itemUltimateCircuit",
            "itemEnergizedStar",
            "itemAntimatterMolecule",
            "itemAntimatterGlob",
            "itemCoal",
            "itemBoat",
            "itemHerbalMedicineCake",
            "itemCakeSponge",
            "itemFishandPumpkinCakeSponge",
            "itemSoulCleaver",
            "itemInstantCake",
            "itemWhippingCream",
            "itemGlisteningWhippingCream",
            "itemCleaver",
            "itemHerbalMedicineWhippingCream",
            "itemStrangeWhippingCream",
            "itemBlazeCleaver",
            "itemBakedCakeSponge",
            "itemMagmaCake",
            "itemGlisteningCake",
            "itemOgreCleaver",
            "itemFishandPumpkinCake",
            "itemMagmaWhippingCream",
            "itemMultimeter",
            "itemSuperconductor"));

    private static final HashSet<String> IGNORED_NAMES = new HashSet<>(
        Arrays.asList(
            "grubBee",
            "chainLink",
            "candyCane",
            "bRedString",
            "bVial",
            "bFlask",
            "anorthositeSmooth",
            "migmatiteSmooth",
            "slateSmooth",
            "travertineSmooth",
            "limestoneSmooth",
            "orthogneissSmooth",
            "marbleSmooth",
            "honeyDrop",
            "lumpClay",
            "honeyEqualssugar",
            "flourEqualswheat",
            "bluestoneInsulated",
            "blockWaterstone",
            "blockSand",
            "blockTorch",
            "blockPumpkin",
            "blockClothRock",
            "blockStainedHardenedClay",
            "blockQuartzPillar",
            "blockQuartzChiselled",
            "blockSpawner",
            "blockCloth",
            "mobHead",
            "mobEgg",
            "enderFlower",
            "enderChest",
            "clayHardened",
            "dayGemMaterial",
            "nightGemMaterial",
            "snowLayer",
            "bPlaceholder",
            "hardenedClay",
            "eternalLifeEssence",
            "sandstone",
            "wheatRice",
            "transdimBlock",
            "bambooBasket",
            "lexicaBotania",
            "livingwoodTwig",
            "redstoneCrystal",
            "pestleAndMortar",
            "glowstone",
            "whiteStone",
            "stoneSlab",
            "transdimBlock",
            "clayBowl",
            "clayPlate",
            "ceramicBowl",
            "ceramicPlate",
            "ovenRack",
            "clayCup",
            "ceramicCup",
            "batteryBox",
            "transmutationStone",
            "torchRedstoneActive",
            "coal",
            "charcoal",
            "cloth",
            "cobblestoneSlab",
            "stoneBrickSlab",
            "cobblestoneWall",
            "stoneBrickWall",
            "cobblestoneStair",
            "stoneBrickStair",
            "blockCloud",
            "blockDirt",
            "blockTyrian",
            "blockCarpet",
            "blockFft",
            "blockLavastone",
            "blockHolystone",
            "blockConcrete",
            "sunnariumPart",
            "brSmallMachineCyaniteProcessor",
            "meteoriteCoal",
            "blockCobble",
            "pressOreProcessor",
            "crusherOreProcessor",
            "grinderOreProcessor",
            "blockRubber",
            "blockHoney",
            "blockHoneydew",
            "blockPeat",
            "blockRadioactive",
            "blockSlime",
            "blockCocoa",
            "blockSugarCane",
            "blockLeather",
            "blockClayBrick",
            "solarPanelHV",
            "cableRedNet",
            "stoneBowl",
            "crafterWood",
            "taintedSoil",
            "brickXyEngineering",
            "breederUranium",
            "wireMill",
            "chunkLazurite",
            "aluminumNatural",
            "aluminiumNatural",
            "naturalAluminum",
            "naturalAluminium",
            "antimatterMilligram",
            "antimatterGram",
            "strangeMatter",
            "coalGenerator",
            "electricFurnace",
            "unfinishedTank",
            "valvePart",
            "aquaRegia",
            "leatherSeal",
            "leatherSlimeSeal",
            "hambone",
            "slimeball",
            "clay",
            "enrichedUranium",
            "camoPaste",
            "antiBlock",
            "burntQuartz",
            "salmonRaw",
            "blockHopper",
            "blockEnderObsidian",
            "blockIcestone",
            "blockMagicWood",
            "blockEnderCore",
            "blockHeeEndium",
            "oreHeeEndPowder",
            "oreHeeStardust",
            "oreHeeIgneousRock",
            "oreHeeInstabilityOrb",
            "crystalPureFluix",
            "shardNether",
            "gemFluorite",
            "stickObsidian",
            "caveCrystal",
            "shardCrystal",
            "dyeCrystal",
            "shardFire",
            "shardWater",
            "shardAir",
            "shardEarth",
            "ingotRefinedIron",
            "blockMarble",
            "ingotUnstable",
            "obsidian",
            "dirt",
            "gravel",
            "grass",
            "soulsand",
            "paper",
            "brick",
            "chest"));

    private static final HashSet<String> INVALID_NAMES = new HashSet<>(
        Arrays.asList(
            "diamondShard",
            "redstoneRoot",
            "obsidianStick",
            "bloodstoneOre",
            "universalCable",
            "bronzeTube",
            "ironTube",
            "netherTube",
            "obbyTube",
            "infiniteBattery",
            "eliteBattery",
            "advancedBattery",
            "10kEUStore",
            "blueDye",
            "MonazitOre",
            "quartzCrystal",
            "whiteLuminiteCrystal",
            "darkStoneIngot",
            "invisiumIngot",
            "demoniteOrb",
            "enderGem",
            "starconiumGem",
            "osmoniumIngot",
            "tapaziteGem",
            "zectiumIngot",
            "foolsRubyGem",
            "rubyGem",
            "meteoriteGem",
            "adamiteShard",
            "sapphireGem",
            "copperIngot",
            "ironStick",
            "goldStick",
            "diamondStick",
            "reinforcedStick",
            "draconicStick",
            "emeraldStick",
            "copperStick",
            "tinStick",
            "silverStick",
            "bronzeStick",
            "steelStick",
            "leadStick",
            "manyullynStick",
            "arditeStick",
            "cobaltStick",
            "aluminiumStick",
            "alumiteStick",
            "oilsandsOre",
            "copperWire",
            "superconductorWire",
            "sulfuricAcid",
            "conveyorBelt",
            "ironWire",
            "aluminumWire",
            "aluminiumWire",
            "silverWire",
            "tinWire",
            "dustSiliconSmall",
            "AluminumOre",
            "plateHeavyT2",
            "blockWool",
            "alloyPlateEnergizedHardened",
            "gasWood",
            "alloyPlateEnergized",
            "SilverOre",
            "LeadOre",
            "TinOre",
            "CopperOre",
            "silverOre",
            "leadOre",
            "tinOre",
            "copperOre",
            "bauxiteOre",
            "HSLivingmetalIngot",
            "oilMoving",
            "oilStill",
            "oilBucket",
            "petroleumOre",
            "dieselFuel",
            "diamondNugget",
            "planks",
            "wood",
            "stick",
            "sticks",
            "naquadah",
            "obsidianRod",
            "stoneRod",
            "thaumiumRod",
            "steelRod",
            "netherrackRod",
            "woodRod",
            "ironRod",
            "cactusRod",
            "flintRod",
            "copperRod",
            "cobaltRod",
            "alumiteRod",
            "blueslimeRod",
            "arditeRod",
            "manyullynRod",
            "bronzeRod",
            "boneRod",
            "slimeRod",
            "redalloyBundled",
            "bluestoneBundled",
            "infusedteslatiteInsulated",
            "redalloyInsulated",
            "infusedteslatiteBundled"));

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

            if (stack.getItem() instanceof ItemBlock || GTUtility.getBlockFromStack(stack) != Blocks.air) {
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
        if (IGNORED_ITEMS.contains(oreName)) {
            if (!oreName.startsWith("item")) {
                return false;
            }

            GTLoggers.GT_ORE_DICT_LOGGER.info(oreOriginPath);

            if (oreName.equals("itemCopperWire")) {
                GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, stack);
            }
            if (oreName.equals("itemRubber")) {
                GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Rubber, stack);
            }
            return true;
        }

        if (IGNORED_NAMES.contains(oreName)) {
            GTLoggers.GT_ORE_DICT_LOGGER.info("{} is getting ignored via hardcode.", oreOriginPath);
            return true;
        }

        if (oreName.equals("stone")) {
            GTOreDictUnificator.registerOre("stoneSmooth", stack);
            return true;
        }

        if (oreName.equals("cobblestone")) {
            GTOreDictUnificator.registerOre("stoneCobble", stack);
            return true;
        }

        if (oreName.contains("|") || oreName.contains("*")
            || oreName.contains(":")
            || oreName.contains(".")
            || oreName.contains("$")) {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} is using a private Prefix and is therefor getting ignored properly.", oreOriginPath);
            return true;
        }

        if (oreName.equals("copperWire")) {
            GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, stack);
            return false;
        }

        if (oreName.equals("oreHeeEndrium")) {
            GTOreDictUnificator.registerOre(OrePrefixes.ore, Materials.Endium, stack);
            return false;
        }

        if (oreName.equals("sheetPlastic")) {
            GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, stack);
            return false;
        }

        if (oreName.startsWith("shard")) {
            switch (oreName) {
                case "shardAir" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedAir, stack);
                case "shardWater" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedWater, stack);
                case "shardFire" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedFire, stack);
                case "shardEarth" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEarth, stack);
                case "shardOrder" -> GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedOrder, stack);
                case "shardEntropy" -> GTOreDictUnificator
                    .registerOre(OrePrefixes.gem, Materials.InfusedEntropy, stack);
                default -> {
                    return false;
                }
            }
            return true;
        }

        if (oreName.equals("fieryIngot")) {
            GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.FierySteel, stack);
            return true;
        }

        if (oreName.equals("ironwood")) {
            GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.IronWood, stack);
            return true;
        }

        if (oreName.equals("steeleaf")) {
            GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Steeleaf, stack);
            return true;
        }

        if (oreName.equals("knightmetal")) {
            GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Knightmetal, stack);
            return true;
        }

        if (oreName.equals("compressedAluminum")) {
            GTOreDictUnificator.registerOre(OrePrefixes.compressed, Materials.Aluminium, stack);
            return true;
        }

        if (oreName.contains(" ")) {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} is getting re-registered because the OreDict Name containing invalid spaces.", oreOriginPath);
            GTOreDictUnificator.registerOre(oreName.replace(" ", ""), GTUtility.copyAmount(1, stack));
            stack.setStackDisplayName("Invalid OreDictionary Tag");
            return true;
        }

        if (INVALID_NAMES.contains(oreName)) {
            GTLoggers.GT_ORE_DICT_LOGGER.info("{} is wrongly registered and therefor getting ignored.", oreOriginPath);
            return true;
        }

        return false;
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

        Materials material = Materials._NULL;
        String materialName = oreName.substring(
            prefix.getName()
                .length());

        if (prefix != prefix.mPrefixInto) {
            String newName = prefix.mPrefixInto.getName() + materialName;
            if (!GTOreDictUnificator.isRegisteringOres()) {
                GTLoggers.GT_ORE_DICT_LOGGER
                    .info("{} uses a deprecated Prefix, and is getting re-registered as {}", oreOriginPath, newName);
            }
            GTOreDictUnificator.registerOre(newName, stack);
            return;
        }

        if (!materialName.isEmpty()) {
            char firstChar = materialName.charAt(0);
            boolean validFirstChar = Character.isUpperCase(firstChar) || Character.isLowerCase(firstChar)
                || firstChar == '_'
                || Character.isDigit(firstChar);

            if (validFirstChar) {
                if (prefix.isMaterialBased()) {
                    material = Materials.get(materialName);
                    if (processMaterialRegistration(oreName, stack, prefix, material, oreOriginPath)) {
                        return;
                    }
                } else {
                    prefix.add(GTUtility.copyAmount(1, stack));
                }
            }
        } else if (prefix.isSelfReferencing()) {
            prefix.add(GTUtility.copyAmount(1, stack));
        } else {
            GTLoggers.GT_ORE_DICT_LOGGER
                .info("{} uses a Prefix as full OreDict Name, and is therefor invalid.", oreOriginPath);
            stack.setStackDisplayName("Invalid OreDictionary Tag");
            return;
        }

        registerPrefixAliases(stack, prefix, materialName);

        GTLoggers.GT_ORE_DICT_LOGGER.info(oreOriginPath);

        OreDictRegistration registration = new OreDictRegistration(oreName, stack, prefix, material, modId);
        if (bufferRegistrationProcessing) {
            registrations.add(registration);
        } else {
            registration.registerRecipes();
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

        if (!prefix.isIgnored(material)) {
            prefix.add(GTUtility.copyAmount(1, stack));
        }

        if (material == Materials._NULL) {
            for (Dyes dye : Dyes.VALUES) {
                if (oreName.endsWith(
                    dye.name()
                        .substring("dye".length()))) {
                    GTOreDictUnificator.addToBlacklist(stack);
                    GTLoggers.GT_ORE_DICT_LOGGER.info(
                        "{} Oh man, why the fuck would anyone need a OreDictified Color for this, that is even too much for GregTech... do not report this, this is just a random Comment about how ridiculous this is.",
                        oreOriginPath);
                    return true;
                }
            }

            return true;
        }

        for (Materials reRegisteredMaterial : material.mOreReRegistrations) {
            GTOreDictUnificator.registerOre(prefix, reRegisteredMaterial, stack);
        }

        material.add(GTUtility.copyAmount(1, stack));

        registerThaumcraftAspects(oreName, stack, prefix, material);
        registerMaterialAliases(stack, prefix, material);

        return prefix.isUnifiable() && !material.mUnifiable;
    }

    private static void registerThaumcraftAspects(String oreName, ItemStack stack, OrePrefixes prefix,
        Materials material) {

        if (GregTechAPI.sThaumcraftCompat == null || !prefix.doGenerateItem(material) || prefix.isIgnored(material)) {
            return;
        }

        List<TCAspects.TC_AspectStack> aspects = new ArrayList<>();
        for (TCAspects.TC_AspectStack aspect : prefix.mAspects) {
            aspect.addToAspectList(aspects);
        }

        long materialAmount = prefix.getMaterialAmount();
        if (materialAmount >= GTValues.M || materialAmount < 0) {
            for (TCAspects.TC_AspectStack aspect : material.mAspects) {
                aspect.addToAspectList(aspects);
            }
        }

        GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(GTUtility.copyAmount(1, stack), aspects, oreName);
    }

    private static void registerMaterialAliases(ItemStack stack, OrePrefixes prefix, Materials material) {
        switch (prefix.getName()) {
            case "crystal" -> {
                if (material == Materials.CertusQuartz || material == Materials.NetherQuartz
                    || material == Materials.Fluix) {
                    GTOreDictUnificator.registerOre(OrePrefixes.gem, material, stack);
                }
            }
            case "gem" -> {
                if (material == Materials.Lapis || material == Materials.Sodalite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBlue, stack);
                } else if (material == Materials.Lazurite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeCyan, stack);
                } else if (material == Materials.InfusedAir || material == Materials.InfusedWater
                    || material == Materials.InfusedFire
                    || material == Materials.InfusedEarth
                    || material == Materials.InfusedOrder
                    || material == Materials.InfusedEntropy) {
                        GTOreDictUnificator.registerOre("shard" + material.mName.substring("Infused".length()), stack);
                    } else if (material == Materials.Chocolate) {
                        GTOreDictUnificator.registerOre(Dyes.dyeBrown, stack);
                    } else if (material == Materials.CertusQuartz || material == Materials.NetherQuartz) {
                        GTOreDictUnificator.registerOre(OrePrefixes.item.get(material), stack);
                        GTOreDictUnificator.registerOre(OrePrefixes.crystal, material, stack);
                        GTOreDictUnificator.registerOre(OreDictNames.craftingQuartz, stack);
                    } else if (material == Materials.Fluix || material == Materials.Quartz
                        || material == Materials.Quartzite) {
                            GTOreDictUnificator.registerOre(OrePrefixes.crystal, material, stack);
                            GTOreDictUnificator.registerOre(OreDictNames.craftingQuartz, stack);
                        }
            }
            case "cableGt01" -> {
                if (material == Materials.Tin) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireTin, stack);
                } else if (material == Materials.AnyCopper) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, stack);
                } else if (material == Materials.Gold) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireGold, stack);
                } else if (material == Materials.AnyIron) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireIron, stack);
                }
            }
            case "lens" -> {
                if (material.contains(SubTag.TRANSPARENT) && material.mColor != Dyes._NULL) {
                    String color = material.mColor.name();
                    if (color.startsWith("dye")) color = color.substring(3);
                    GTOreDictUnificator.registerOre("craftingLens" + color, stack);
                }
            }
            case "plate" -> {
                if (material == Materials.Polyethylene || material == Materials.Rubber) {
                    GTOreDictUnificator.registerOre(OrePrefixes.sheet, material, stack);
                } else if (material == Materials.Silicon) {
                    GTOreDictUnificator.registerOre(OrePrefixes.item, material, stack);
                } else if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                    GTOreDictUnificator.registerOre(OrePrefixes.plank, material, stack);
                }
            }
            case "cell" -> {
                if (material == Materials.Empty) {
                    GTOreDictUnificator.addToBlacklist(stack);
                }
            }
            case "gearGt" -> GTOreDictUnificator.registerOre(OrePrefixes.gear, material, stack);
            case "stick" -> {
                if (!GTRecipeRegistrator.sRodMaterialList.contains(material)) {
                    GTRecipeRegistrator.sRodMaterialList.add(material);
                } else if (material == Materials.Wood) {
                    GTOreDictUnificator.addToBlacklist(stack);
                } else if (material == Materials.Tin || material == Materials.Lead
                    || material == Materials.SolderingAlloy) {
                        GTOreDictUnificator.registerOre(ToolDictNames.craftingToolSolderingMetal, stack);
                    }
            }
            case "dust" -> {
                if (material == Materials.Salt) {
                    GTOreDictUnificator.registerOre("itemSalt", stack);
                } else if (material == Materials.Wood) {
                    GTOreDictUnificator.registerOre("pulpWood", stack);
                } else if (material == Materials.Wheat) {
                    GTOreDictUnificator.registerOre("foodFlour", stack);
                } else if (material == Materials.Lapis) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBlue, stack);
                } else if (material == Materials.Lazurite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeCyan, stack);
                } else if (material == Materials.Sodalite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBlue, stack);
                } else if (material == Materials.Cocoa) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBrown, stack);
                    GTOreDictUnificator.registerOre("foodCocoapowder", stack);
                } else if (material == Materials.Coffee) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBrown, stack);
                } else if (material == Materials.BrownLimonite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeBrown, stack);
                } else if (material == Materials.YellowLimonite) {
                    GTOreDictUnificator.registerOre(Dyes.dyeYellow, stack);
                }
            }
            case "ingot" -> {
                if (material == Materials.Rubber) {
                    GTOreDictUnificator.registerOre("itemRubber", stack);
                } else if (material == Materials.FierySteel) {
                    GTOreDictUnificator.registerOre("fieryIngot", stack);
                } else if (material == Materials.IronWood) {
                    GTOreDictUnificator.registerOre("ironwood", stack);
                } else if (material == Materials.Steeleaf) {
                    GTOreDictUnificator.registerOre("steeleaf", stack);
                } else if (material == Materials.Knightmetal) {
                    GTOreDictUnificator.registerOre("knightmetal", stack);
                } else if (material == Materials.Brass && stack.getItemDamage() == 2
                    && stack.getUnlocalizedName()
                        .equals("item.ingotBrass")
                    && new ItemStack(stack.getItem(), 1, 0).getUnlocalizedName()
                        .contains("red")) {
                            GTOreDictUnificator
                                .set(OrePrefixes.ingot, Materials.RedAlloy, new ItemStack(stack.getItem(), 1, 0));
                            GTOreDictUnificator
                                .set(OrePrefixes.ingot, Materials.BlueAlloy, new ItemStack(stack.getItem(), 1, 1));
                            GTOreDictUnificator
                                .set(OrePrefixes.ingot, Materials.Brass, new ItemStack(stack.getItem(), 1, 2));

                            GTValues.RA.stdBuilder()
                                .itemInputs(new ItemStack(stack.getItem(), 1, 3))
                                .itemOutputs(new ItemStack(stack.getItem(), 16, 4))
                                .duration(20 * SECONDS)
                                .eut(TierEU.RECIPE_ULV)
                                .addTo(cutterRecipes);
                        }
            }
            default -> {}
        }
    }

    private static void registerPrefixAliases(ItemStack stack, OrePrefixes prefix, String materialName) {
        switch (prefix.getName()) {
            case "dye" -> {
                if (GTUtility.isStringValid(materialName)) {
                    GTOreDictUnificator.registerOre(OrePrefixes.dye, stack);
                }
            }
            case "stoneSmooth" -> GTOreDictUnificator.registerOre("stone", stack);
            case "stoneCobble" -> GTOreDictUnificator.registerOre("cobblestone", stack);
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
            case "sheet" -> {
                if (materialName.equals("Plastic")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, stack);
                } else if (materialName.equals("Rubber")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Rubber, stack);
                }
            }
            case "crafting" -> {
                switch (materialName) {
                    case "ToolSolderingMetal" -> GregTechAPI.registerSolderingMetal(stack);
                    case "IndustrialDiamond" -> GTOreDictUnificator.addToBlacklist(stack);
                    case "WireCopper" -> GTOreDictUnificator.registerOre(OrePrefixes.wire, Materials.Copper, stack);
                }
            }
            case "wood" -> {
                if (materialName.equals("Rubber")) {
                    GTOreDictUnificator.registerOre("logRubber", stack);
                }
            }
            case "food" -> {
                if (materialName.equals("Cocoapowder")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.dust, Materials.Cocoa, stack);
                }
            }
            default -> {}
        }
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

    public void registerUnificationEntries() {
        GTOreDictUnificator.resetUnificationEntries();

        for (OreDictRegistration registration : registrations) {
            if (!registration.prefix.isUnifiable()) {
                continue;
            }

            boolean blacklisted = GTOreDictUnificator.isBlacklisted(registration.stack);
            GTOreDictUnificator
                .addAssociation(registration.prefix, registration.material, registration.stack, blacklisted);

            if (blacklisted) {
                continue;
            }

            boolean overwrite = registration.modId != null
                && OreDictUnificationOverrides.contains(registration.modId, registration.oreName);

            GTOreDictUnificator.set(registration.prefix, registration.material, registration.stack, overwrite, true);
        }

        GTRecipe.reInit();
    }

    public boolean isRegisteredOre(ItemStack stack) {
        return registeredOres.contains(stack);
    }
}
