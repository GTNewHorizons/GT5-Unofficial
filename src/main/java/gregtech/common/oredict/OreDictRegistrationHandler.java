package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import gregtech.api.enums.ManualOreDictTweaks;
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
import gregtech.common.OreDictEventContainer;

public final class OreDictRegistrationHandler {

    private final GTProxy proxy;

    public OreDictRegistrationHandler(GTProxy proxy) {
        this.proxy = proxy;
    }

    public final HashSet<ItemStack> mRegisteredOres = new HashSet<>(10000);
    private final Collection<OreDictEventContainer> oreDictEvents = new HashSet<>();
    private final Collection<String> mIgnoredItems = new HashSet<>(
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
    private final Collection<String> mIgnoredNames = new HashSet<>(
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
    private final Collection<String> mInvalidNames = new HashSet<>(
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
    private boolean mOreDictActivated = false;

    public void registerOre(OreDictionary.OreRegisterEvent aEvent) {
        ModContainer tContainer = Loader.instance()
            .activeModContainer();
        String aMod = tContainer == null ? "UNKNOWN" : tContainer.getModId();
        String aOriginalMod = aMod;
        if (GTOreDictUnificator.isRegisteringOres()) {
            aMod = GregTech.ID;
        } else if (aMod.equals(GregTech.ID)) {
            aMod = "UNKNOWN";
        }
        if ((aEvent == null) || (aEvent.Ore == null)
            || (aEvent.Ore.getItem() == null)
            || (aEvent.Name == null)
            || (aEvent.Name.isEmpty())
            || (aEvent.Name.replace("_", "")
                .length() - aEvent.Name.length() == 9)) {
            if (aOriginalMod.equals(GregTech.ID)) {
                aOriginalMod = "UNKNOWN";
            }
            GTLoggers.GT_ORE_DICT_LOGGER.info(
                "{} did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.",
                aOriginalMod);
            throw new IllegalArgumentException(
                aOriginalMod
                    + " did something very bad! The registration is too invalid to even be shown properly. This happens only if you register null, invalid Items, empty Strings or even nonexisting Events to the OreDict.");
        }
        try {
            aEvent.Ore.stackSize = 1;

            // skipping TinkerConstruct ore registration except for blocks
            if (proxy.mIgnoreTcon && aOriginalMod.equals(TinkerConstruct.ID)
                && !(aEvent.Ore.getItem() instanceof ItemBlock)) {
                return;
            }
            String tModToName = aMod + " -> " + aEvent.Name;
            if (this.mOreDictActivated || GregTechAPI.sPostloadStarted || GregTechAPI.sLoadFinished) {
                tModToName = aOriginalMod + " --Late--> " + aEvent.Name;
            }
            if (((aEvent.Ore.getItem() instanceof ItemBlock))
                || (GTUtility.getBlockFromStack(aEvent.Ore) != Blocks.air)) {
                GTOreDictUnificator.addToBlacklist(aEvent.Ore);
            }
            this.mRegisteredOres.add(aEvent.Ore);
            if (this.mIgnoredItems.contains(aEvent.Name)) {
                if ((aEvent.Name.startsWith("item"))) {
                    GTLoggers.GT_ORE_DICT_LOGGER.info(tModToName);
                    if (aEvent.Name.equals("itemCopperWire")) {
                        GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                    }
                    if (aEvent.Name.equals("itemRubber")) {
                        GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Rubber, aEvent.Ore);
                    }
                    return;
                }
            } else if (this.mIgnoredNames.contains(aEvent.Name)) {
                GTLoggers.GT_ORE_DICT_LOGGER.info("{} is getting ignored via hardcode.", tModToName);
                return;
            } else if (aEvent.Name.equals("stone")) {
                GTOreDictUnificator.registerOre("stoneSmooth", aEvent.Ore);
                return;
            } else if (aEvent.Name.equals("cobblestone")) {
                GTOreDictUnificator.registerOre("stoneCobble", aEvent.Ore);
                return;
            } else if ((aEvent.Name.contains("|")) || (aEvent.Name.contains("*"))
                || (aEvent.Name.contains(":"))
                || (aEvent.Name.contains("."))
                || (aEvent.Name.contains("$"))) {
                    GTLoggers.GT_ORE_DICT_LOGGER
                        .info("{} is using a private Prefix and is therefor getting ignored properly.", tModToName);
                    return;
                } else if (aEvent.Name.equals("copperWire")) {
                    GTOreDictUnificator.registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                } else if (aEvent.Name.equals("oreHeeEndrium")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ore, Materials.Endium, aEvent.Ore);
                } else if (aEvent.Name.equals("sheetPlastic")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, aEvent.Ore);
                } else if (aEvent.Name.startsWith("shard")) {
                    switch (aEvent.Name) {
                        case "shardAir" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedAir, aEvent.Ore);
                            return;
                        }
                        case "shardWater" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedWater, aEvent.Ore);
                            return;
                        }
                        case "shardFire" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedFire, aEvent.Ore);
                            return;
                        }
                        case "shardEarth" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEarth, aEvent.Ore);
                            return;
                        }
                        case "shardOrder" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedOrder, aEvent.Ore);
                            return;
                        }
                        case "shardEntropy" -> {
                            GTOreDictUnificator.registerOre(OrePrefixes.gem, Materials.InfusedEntropy, aEvent.Ore);
                            return;
                        }
                    }
                } else if (aEvent.Name.equals("fieryIngot")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.FierySteel, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("ironwood")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.IronWood, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("steeleaf")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Steeleaf, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("knightmetal")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.ingot, Materials.Knightmetal, aEvent.Ore);
                    return;
                } else if (aEvent.Name.equals("compressedAluminum")) {
                    GTOreDictUnificator.registerOre(OrePrefixes.compressed, Materials.Aluminium, aEvent.Ore);
                    return;
                } else if (aEvent.Name.contains(" ")) {
                    GTLoggers.GT_ORE_DICT_LOGGER.info(
                        "{} is getting re-registered because the OreDict Name containing invalid spaces.",
                        tModToName);
                    GTOreDictUnificator.registerOre(aEvent.Name.replace(" ", ""), GTUtility.copyAmount(1, aEvent.Ore));
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                } else if (this.mInvalidNames.contains(aEvent.Name)) {
                    GTLoggers.GT_ORE_DICT_LOGGER
                        .info("{} is wrongly registered and therefor getting ignored.", tModToName);

                    return;
                }
            OrePrefixes aPrefix = OrePrefixes.getOrePrefix(aEvent.Name);
            Materials aMaterial = Materials._NULL;
            if ((aPrefix == OrePrefixes.nugget) && (aMod.equals(Thaumcraft.ID))
                && (aEvent.Ore.getItem()
                    .getUnlocalizedName()
                    .contains("ItemResource"))) {
                return;
            }
            if (aPrefix == null) {
                if (aEvent.Name.toLowerCase()
                    .equals(aEvent.Name)) {
                    GTLoggers.GT_ORE_DICT_LOGGER.info("{} is invalid due to being solely lowercased.", tModToName);
                    return;
                } else if (aEvent.Name.toUpperCase()
                    .equals(aEvent.Name)) {
                        GTLoggers.GT_ORE_DICT_LOGGER.info("{} is invalid due to being solely uppercased.", tModToName);
                        return;
                    } else if (Character.isUpperCase(aEvent.Name.charAt(0))) {
                        GTLoggers.GT_ORE_DICT_LOGGER
                            .info("{} is invalid due to the first character being uppercased.", tModToName);
                    }
            } else {
                if (aPrefix.skipActiveUnification()) {
                    GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                }
                String tName = aEvent.Name.substring(
                    aPrefix.getName()
                        .length());
                if (aPrefix != aPrefix.mPrefixInto) {
                    String tNewName = aPrefix.mPrefixInto.getName() + tName;
                    if (!GTOreDictUnificator.isRegisteringOres()) {
                        GTLoggers.GT_ORE_DICT_LOGGER.info(
                            "{} uses a depricated Prefix, and is getting re-registered as {}",
                            tModToName,
                            tNewName);
                    }
                    GTOreDictUnificator.registerOre(tNewName, aEvent.Ore);
                    return;
                }
                if (!tName.isEmpty()) {
                    char firstChar = tName.charAt(0);
                    if (Character.isUpperCase(firstChar) || Character.isLowerCase(firstChar)
                        || firstChar == '_'
                        || Character.isDigit(firstChar)) {
                        if (aPrefix.isMaterialBased()) {
                            aMaterial = Materials.get(tName);
                            if (aMaterial != aMaterial.mMaterialInto) {
                                GTOreDictUnificator.registerOre(aPrefix, aMaterial.mMaterialInto, aEvent.Ore);
                                if (!GTOreDictUnificator.isRegisteringOres()) {
                                    GTLoggers.GT_ORE_DICT_LOGGER.info(
                                        "{} uses a deprecated Material and is getting re-registered as {}",
                                        tModToName,
                                        aPrefix.get(aMaterial.mMaterialInto));
                                }
                                return;
                            }
                            if (!aPrefix.isIgnored(aMaterial)) {
                                aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                            }
                            if (aMaterial != Materials._NULL) {
                                Materials tReRegisteredMaterial;
                                for (Iterator<Materials> i$ = aMaterial.mOreReRegistrations.iterator(); i$
                                    .hasNext(); GTOreDictUnificator
                                        .registerOre(aPrefix, tReRegisteredMaterial, aEvent.Ore)) {
                                    tReRegisteredMaterial = i$.next();
                                }
                                aMaterial.add(GTUtility.copyAmount(1, aEvent.Ore));

                                if (GregTechAPI.sThaumcraftCompat != null && aPrefix.doGenerateItem(aMaterial)
                                    && !aPrefix.isIgnored(aMaterial)) {
                                    List<TCAspects.TC_AspectStack> tAspects = new ArrayList<>();
                                    for (TCAspects.TC_AspectStack tAspect : aPrefix.mAspects)
                                        tAspect.addToAspectList(tAspects);
                                    if (aPrefix.getMaterialAmount() >= 3628800 || aPrefix.getMaterialAmount() < 0)
                                        for (TCAspects.TC_AspectStack tAspect : aMaterial.mAspects)
                                            tAspect.addToAspectList(tAspects);
                                    GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(
                                        GTUtility.copyAmount(1, aEvent.Ore),
                                        tAspects,
                                        aEvent.Name);
                                }

                                switch (aPrefix.getName()) {
                                    case "crystal" -> {
                                        if ((aMaterial == Materials.CertusQuartz)
                                            || (aMaterial == Materials.NetherQuartz)
                                            || (aMaterial == Materials.Fluix)) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.gem, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case "gem" -> {
                                        if (aMaterial == Materials.Lapis || aMaterial == Materials.Sodalite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else
                                            if (aMaterial == Materials.InfusedAir || aMaterial == Materials.InfusedWater
                                                || aMaterial == Materials.InfusedFire
                                                || aMaterial == Materials.InfusedEarth
                                                || aMaterial == Materials.InfusedOrder
                                                || aMaterial == Materials.InfusedEntropy) {
                                                    GTOreDictUnificator.registerOre(
                                                        aMaterial.mName.replaceFirst("Infused", "shard"),
                                                        aEvent.Ore);
                                                } else if (aMaterial == Materials.Chocolate) {
                                                    GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                                } else if (aMaterial == Materials.CertusQuartz
                                                    || aMaterial == Materials.NetherQuartz) {
                                                        GTOreDictUnificator
                                                            .registerOre(OrePrefixes.item.get(aMaterial), aEvent.Ore);
                                                        GTOreDictUnificator
                                                            .registerOre(OrePrefixes.crystal, aMaterial, aEvent.Ore);
                                                        GTOreDictUnificator
                                                            .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                    } else
                                                    if (aMaterial == Materials.Fluix || aMaterial == Materials.Quartz
                                                        || aMaterial == Materials.Quartzite) {
                                                            GTOreDictUnificator.registerOre(
                                                                OrePrefixes.crystal,
                                                                aMaterial,
                                                                aEvent.Ore);
                                                            GTOreDictUnificator
                                                                .registerOre(OreDictNames.craftingQuartz, aEvent.Ore);
                                                        }
                                    }
                                    case "cableGt01" -> {
                                        if (aMaterial == Materials.Tin) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireTin, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyCopper) {
                                            GTOreDictUnificator
                                                .registerOre(OreDictNames.craftingWireCopper, aEvent.Ore);
                                        } else if (aMaterial == Materials.Gold) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireGold, aEvent.Ore);
                                        } else if (aMaterial == Materials.AnyIron) {
                                            GTOreDictUnificator.registerOre(OreDictNames.craftingWireIron, aEvent.Ore);
                                        }
                                    }
                                    case "lens" -> {
                                        if ((aMaterial.contains(SubTag.TRANSPARENT))
                                            && (aMaterial.mColor != Dyes._NULL)) {
                                            GTOreDictUnificator.registerOre(
                                                "craftingLens" + aMaterial.mColor.toString()
                                                    .replaceFirst("dye", ""),
                                                aEvent.Ore);
                                        }
                                    }
                                    case "plate" -> {
                                        if ((aMaterial == Materials.Polyethylene) || (aMaterial == Materials.Rubber)) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.sheet, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Silicon) {
                                            GTOreDictUnificator.registerOre(OrePrefixes.item, aMaterial, aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                            GTOreDictUnificator.registerOre(OrePrefixes.plank, aMaterial, aEvent.Ore);
                                        }
                                    }
                                    case "cell" -> {
                                        if (aMaterial == Materials.Empty) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        }
                                    }
                                    case "gearGt" -> GTOreDictUnificator
                                        .registerOre(OrePrefixes.gear, aMaterial, aEvent.Ore);
                                    case "stick" -> {
                                        if (!GTRecipeRegistrator.sRodMaterialList.contains(aMaterial)) {
                                            GTRecipeRegistrator.sRodMaterialList.add(aMaterial);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        } else if ((aMaterial == Materials.Tin) || (aMaterial == Materials.Lead)
                                            || (aMaterial == Materials.SolderingAlloy)) {
                                                GTOreDictUnificator
                                                    .registerOre(ToolDictNames.craftingToolSolderingMetal, aEvent.Ore);
                                            }
                                    }
                                    case "dust" -> {
                                        if (aMaterial == Materials.Salt) {
                                            GTOreDictUnificator.registerOre("itemSalt", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wood) {
                                            GTOreDictUnificator.registerOre("pulpWood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Wheat) {
                                            GTOreDictUnificator.registerOre("foodFlour", aEvent.Ore);
                                        } else if (aMaterial == Materials.Lapis) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Lazurite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeCyan, aEvent.Ore);
                                        } else if (aMaterial == Materials.Sodalite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBlue, aEvent.Ore);
                                        } else if (aMaterial == Materials.Cocoa) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                            GTOreDictUnificator.registerOre("foodCocoapowder", aEvent.Ore);
                                        } else if (aMaterial == Materials.Coffee) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.BrownLimonite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeBrown, aEvent.Ore);
                                        } else if (aMaterial == Materials.YellowLimonite) {
                                            GTOreDictUnificator.registerOre(Dyes.dyeYellow, aEvent.Ore);
                                        }
                                    }
                                    case "ingot" -> {
                                        if (aMaterial == Materials.Rubber) {
                                            GTOreDictUnificator.registerOre("itemRubber", aEvent.Ore);
                                        } else if (aMaterial == Materials.FierySteel) {
                                            GTOreDictUnificator.registerOre("fieryIngot", aEvent.Ore);
                                        } else if (aMaterial == Materials.IronWood) {
                                            GTOreDictUnificator.registerOre("ironwood", aEvent.Ore);
                                        } else if (aMaterial == Materials.Steeleaf) {
                                            GTOreDictUnificator.registerOre("steeleaf", aEvent.Ore);
                                        } else if (aMaterial == Materials.Knightmetal) {
                                            GTOreDictUnificator.registerOre("knightmetal", aEvent.Ore);
                                        } else if ((aMaterial == Materials.Brass) && (aEvent.Ore.getItemDamage() == 2)
                                            && (aEvent.Ore.getUnlocalizedName()
                                                .equals("item.ingotBrass"))
                                            && (new ItemStack(aEvent.Ore.getItem(), 1, 0).getUnlocalizedName()
                                                .contains("red"))) {
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.RedAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 0));
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.BlueAlloy,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 1));
                                                    GTOreDictUnificator.set(
                                                        OrePrefixes.ingot,
                                                        Materials.Brass,
                                                        new ItemStack(aEvent.Ore.getItem(), 1, 2));

                                                    GTValues.RA.stdBuilder()
                                                        .itemInputs(new ItemStack(aEvent.Ore.getItem(), 1, 3))
                                                        .itemOutputs(new ItemStack(aEvent.Ore.getItem(), 16, 4))
                                                        .duration(20 * SECONDS)
                                                        .eut(TierEU.RECIPE_ULV)
                                                        .addTo(cutterRecipes);
                                                }
                                    }
                                    default -> {}
                                }
                                if (aPrefix.isUnifiable() && !aMaterial.mUnifiable) {
                                    return;
                                }
                            } else {
                                for (Dyes tDye : Dyes.VALUES) {
                                    if (aEvent.Name.endsWith(
                                        tDye.name()
                                            .replaceFirst("dye", ""))) {
                                        GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                                        GTLoggers.GT_ORE_DICT_LOGGER.info(
                                            "{} Oh man, why the fuck would anyone need a OreDictified Color for this, that is even too much for GregTech... do not report this, this is just a random Comment about how ridiculous this is.",
                                            tModToName);
                                        return;
                                    }
                                }
                                // GT_FML_LOGGER.info("Material Name: "+aEvent.Name+ "
                                // !!!Unknown Material detected!!! Please report to GregTech Intergalactical for
                                // additional compatiblity. This is not an Error, an Issue nor a Lag Source, it is just
                                // an Information, which you should pass to me.");
                                // GTLoggers.GT_ORE_DICT_LOGGER.info(tModToName + " uses an unknown
                                // Material. Report this to GregTech.");
                                return;
                            }
                        } else {
                            aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                        }
                    }
                } else if (aPrefix.isSelfReferencing()) {
                    aPrefix.add(GTUtility.copyAmount(1, aEvent.Ore));
                } else {
                    GTLoggers.GT_ORE_DICT_LOGGER
                        .info("{} uses a Prefix as full OreDict Name, and is therefor invalid.", tModToName);
                    aEvent.Ore.setStackDisplayName("Invalid OreDictionary Tag");
                    return;
                }
                switch (aPrefix.getName()) {
                    case "dye" -> {
                        if (GTUtility.isStringValid(tName)) {
                            GTOreDictUnificator.registerOre(OrePrefixes.dye, aEvent.Ore);
                        }
                    }
                    case "stoneSmooth" -> GTOreDictUnificator.registerOre("stone", aEvent.Ore);
                    case "stoneCobble" -> GTOreDictUnificator.registerOre("cobblestone", aEvent.Ore);
                    case "plank" -> {
                        if (tName.equals("Wood")) {
                            GTOreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 3628800L));
                        }
                    }
                    case "slab" -> {
                        if (tName.equals("Wood")) {
                            GTOreDictUnificator.addItemData(aEvent.Ore, new ItemData(Materials.Wood, 1814400L));
                        }
                    }
                    case "sheet" -> {
                        if (tName.equals("Plastic")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Polyethylene, aEvent.Ore);
                        } else if (tName.equals("Rubber")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.plate, Materials.Rubber, aEvent.Ore);
                        }
                    }
                    case "crafting" -> {
                        switch (tName) {
                            case "ToolSolderingMetal" -> GregTechAPI.registerSolderingMetal(aEvent.Ore);
                            case "IndustrialDiamond" -> GTOreDictUnificator.addToBlacklist(aEvent.Ore);
                            case "WireCopper" -> GTOreDictUnificator
                                .registerOre(OrePrefixes.wire, Materials.Copper, aEvent.Ore);
                        }
                    }
                    case "wood" -> {
                        if (tName.equals("Rubber")) {
                            GTOreDictUnificator.registerOre("logRubber", aEvent.Ore);
                        }
                    }
                    case "food" -> {
                        if (tName.equals("Cocoapowder")) {
                            GTOreDictUnificator.registerOre(OrePrefixes.dust, Materials.Cocoa, aEvent.Ore);
                        }
                    }
                    default -> {}
                }
            }
            GTLoggers.GT_ORE_DICT_LOGGER.info(tModToName);

            OreDictEventContainer tOre = new OreDictEventContainer(aEvent, aPrefix, aMaterial, aMod);
            if ((!this.mOreDictActivated) || (!GregTechAPI.sUnificationEntriesRegistered)) {
                this.oreDictEvents.add(tOre);
            } else {
                this.oreDictEvents.clear();
            }
            if (this.mOreDictActivated) {
                OreDictEventContainer.registerRecipes(tOre);
            }
        } catch (Exception e) {
            GT_FML_LOGGER.error("Could not register ore (oredict name={}, item stack={})", aEvent.Name, aEvent.Ore, e);
        }
    }

    @SuppressWarnings("deprecation")
    public void activateOreDictHandler() {
        this.mOreDictActivated = true;

        ProgressManager.ProgressBar progressBar = proxy.isClientSide()
            ? ProgressManager.push("Register materials", oreDictEvents.size())
            : null;

        int size = 5;
        int sizeStep = oreDictEvents.size() / 20 - 1;

        for (OreDictEventContainer event : oreDictEvents) {
            sizeStep--;
            if (sizeStep == 0) {
                GT_FML_LOGGER.info("Baking : {}%", size);
                sizeStep = oreDictEvents.size() / 20 - 1;
                size += 5;
            }

            if (progressBar != null) {
                progressBar.step(event.mMaterial == null ? "" : event.mMaterial.getLocalizedName());
            }

            OreDictEventContainer.registerRecipes(event);
        }

        if (progressBar != null) {
            ProgressManager.pop(progressBar);
        }
    }

    public void registerUnificationEntries() {
        GTOreDictUnificator.resetUnificationEntries();
        for (OreDictEventContainer tOre : this.oreDictEvents) {
            if ((tOre.mPrefix != null) && (tOre.mPrefix.isUnifiable()) && (tOre.mMaterial != null)) {
                if (GTOreDictUnificator.isBlacklisted(tOre.mEvent.Ore)) {
                    GTOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, true);
                } else {
                    GTOreDictUnificator.addAssociation(tOre.mPrefix, tOre.mMaterial, tOre.mEvent.Ore, false);
                    GTOreDictUnificator.set(
                        tOre.mPrefix,
                        tOre.mMaterial,
                        tOre.mEvent.Ore,
                        (tOre.mModID != null)
                            && ManualOreDictTweaks.shouldOredictBeOverwritten(tOre.mModID, tOre.mEvent.Name),
                        true);
                }
            }
        }
        GregTechAPI.sUnificationEntriesRegistered = true;
        GTRecipe.reInit();
    }

    public boolean isRegisteredOre(ItemStack stack) {
        return mRegisteredOres.contains(stack);
    }
}
