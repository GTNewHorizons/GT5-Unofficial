package gtPlusPlus.core.item;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.common.compat.COMPAT_Baubles;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.BaseItemDamageable;
import gtPlusPlus.core.item.base.BaseItemTCShard;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.foil.BaseItemFoil;
import gtPlusPlus.core.item.base.gears.BaseItemSmallGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot_OLD;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.CoalTar;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.chemistry.MilledOreProcessing;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.item.chemistry.StandardBaseParticles;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.food.BaseItemMetaFood;
import gtPlusPlus.core.item.general.BufferCore;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.item.general.ItemEmpty;
import gtPlusPlus.core.item.general.ItemGenericToken;
import gtPlusPlus.core.item.general.ItemHalfCompleteCasings;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.core.item.general.ItemMagicFeather;
import gtPlusPlus.core.item.general.books.ItemBaseBook;
import gtPlusPlus.core.item.general.chassis.ItemBoilerChassis;
import gtPlusPlus.core.item.general.chassis.ItemDehydratorCoil;
import gtPlusPlus.core.item.general.chassis.ItemDehydratorCoilWire;
import gtPlusPlus.core.item.general.spawn.ItemCustomSpawnEgg;
import gtPlusPlus.core.item.init.ItemsFoods;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.item.tool.misc.DebugScanner;
import gtPlusPlus.core.item.tool.misc.GregtechPump;
import gtPlusPlus.core.item.wearable.WearableLoader;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.everglades.GTplusplus_Everglades;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;

public final class ModItems {

    public static Item ZZZ_Empty;
    public static Item AAA_Broken;

    public static Item itemAlkalusDisk;
    public static ItemCustomSpawnEgg itemCustomSpawnEgg;

    public static Item itemIngotBatteryAlloy;

    public static Item itemHydrogenBlob;

    public static Item itemBedLocator_Base;
    public static Item itemBaseItemWithCharge;

    public static Item itemPersonalCloakingDevice;
    public static Item itemPersonalHealingDevice;
    public static Item itemSupremePizzaGloves;

    public static ItemBlueprint itemBlueprintBase;

    public static Item dustLithiumCarbonate;
    public static Item dustLithiumHydroxide;
    public static Item dustLithiumPeroxide;

    public static Item dustQuicklime;
    public static Item dustCalciumHydroxide;
    public static Item dustCalciumCarbonate;
    public static Item dustLi2CO3CaOH2;
    public static Item dustLi2BeF4;

    public static Item dustTumbagaMix;

    public static Item dustAer;
    public static Item dustIgnis;
    public static Item dustTerra;
    public static Item dustAqua;

    public static Item cellHydrogenChlorideMix;

    public static Item shardAer;
    public static Item shardIgnis;
    public static Item shardTerra;
    public static Item shardAqua;

    // Zirconium
    public static Item itemZirconiumChlorideCinterPellet;
    public static Item dustZrCl4;
    public static Item dustCookedZrCl4;

    public static Item dustCalciumSulfate;

    public static Item dustFertUN18;
    public static Item dustFertUN32;
    public static Fluid fluidNuclearWaste;

    // Possibly missing base items that GT may be missing.

    public static Item itemSmallWroughtIronGear;
    public static Item itemPlateRawMeat;
    public static Item itemPlateClay;
    public static Item itemPlateLithium;
    public static Item itemPlateEuropium;
    public static Item itemPlateVanadium;
    public static Item itemDoublePlateClay;
    public static Item itemDoublePlateEuropium;
    public static Item itemFoilUranium235;
    public static Item itemDustIndium;
    public static BlockBaseModular blockRawMeat;

    public static Item itemBoilerChassis;
    public static Item itemDehydratorCoilWire;
    public static Item itemDehydratorCoil;

    public static Item itemLavaFilter;
    public static Item itemAirFilter;

    public static Item itemCoalCoke;
    public static Item itemCactusCharcoal;
    public static Item itemSugarCharcoal;
    public static Item itemCactusCoke;
    public static Item itemSugarCoke;

    public static Item itemCircuitLFTR;
    public static Item itemBasicTurbine;

    public static Item itemHalfCompleteCasings;

    public static Item itemCustomBook;

    // Unstable Elements & Related Content
    public static Item dustNeptunium238;
    public static Item dustDecayedRadium226;
    public static Item dustRadium226;
    public static Item dustProtactinium233;

    public static GregtechPump toolGregtechPump;

    public static ItemGenericToken itemGenericToken;

    public static ItemStack itemHotTitaniumIngot;

    public static Fluid fluidZrF4;
    public static Fluid fluidFertBasic;
    public static Fluid fluidFertUN32;
    public static Fluid fluidFertUN18;

    public static DustDecayable dustMolybdenum99;
    public static DustDecayable dustTechnetium99;
    public static DustDecayable dustTechnetium99M;

    public static IonParticles itemIonParticleBase;
    public static StandardBaseParticles itemStandarParticleBase;

    public static BatteryPackBaseBauble itemChargePack_Low_1;
    public static BatteryPackBaseBauble itemChargePack_Low_2;
    public static BatteryPackBaseBauble itemChargePack_Low_3;
    public static BatteryPackBaseBauble itemChargePack_Low_4;
    public static BatteryPackBaseBauble itemChargePack_Low_5;
    public static BatteryPackBaseBauble itemChargePack_High_1;
    public static BatteryPackBaseBauble itemChargePack_High_2;
    public static BatteryPackBaseBauble itemChargePack_High_3;
    public static BatteryPackBaseBauble itemChargePack_High_4;

    public static DebugScanner itemDebugScanner;

    public static ItemDummyResearch itemDummyResearch;

    public static BaseItemMetaFood itemMetaFood;

    public static ItemMagicFeather itemMagicFeather;

    static {
        Logger.INFO("Items!");
        // Default item used when recipes fail, handy for debugging. Let's make sure they exist when this class is
        // called upon.
        AAA_Broken = new BaseItemIngot_OLD(
                "AAA_Broken",
                "Errors - Tell Alkalus",
                Utils.rgbtoHexValue(128, 128, 128),
                0);
        ZZZ_Empty = new ItemEmpty();
    }

    public static void init() {

        itemDebugScanner = new DebugScanner();
        itemMagicFeather = new ItemMagicFeather();

        itemAlkalusDisk = new BaseItemDamageable(
                "itemAlkalusDisk",
                AddToCreativeTab.tabMisc,
                1,
                0,
                "Unknown Use",
                EnumRarity.rare,
                EnumChatFormatting.AQUA,
                false,
                null);

        itemGenericToken = new ItemGenericToken();
        itemDummyResearch = new ItemDummyResearch();
        itemCustomSpawnEgg = new ItemCustomSpawnEgg();

        // Register meta item, because we need them for everything.
        MetaGeneratedGregtechItems.INSTANCE.generateMetaItems();

        // Register Hydrogen Blobs first, so we can replace old helium blobs.
        itemHydrogenBlob = new CoreItem("itemHydrogenBlob", "Mysterious Hydrogen Blob", tabMisc)
                .setTextureName(GTPlusPlus.ID + ":itemHeliumBlob");
        // Register Old Helium Blob, this will be replaced when held by a player.

        // Load Wearable Items
        WearableLoader.run();

        itemBlueprintBase = new ItemBlueprint("itemBlueprint");

        itemHalfCompleteCasings = new ItemHalfCompleteCasings(
                "itemHalfCompleteCasings",
                AddToCreativeTab.tabMisc,
                32,
                0,
                "This isn't quite finished yet.",
                EnumRarity.common,
                EnumChatFormatting.GRAY,
                false,
                Utils.rgbtoHexValue(255, 255, 255)).setTextureName(GregTech.ID + ":" + "gt.metaitem.01/" + "761");

        // Start meta Item Generation
        ItemsFoods.load();

        try {

            registerCustomMaterialComponents();

            // Elements generate first so they can be used in compounds.
            // Missing Elements
            MaterialGenerator.generate(ELEMENT.getInstance().SELENIUM); // LFTR byproduct
            MaterialGenerator.generate(ELEMENT.getInstance().BROMINE);
            MaterialGenerator.generate(ELEMENT.getInstance().KRYPTON); // LFTR byproduct
            MaterialGenerator.generate(ELEMENT.getInstance().STRONTIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().ZIRCONIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().RUTHENIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().IODINE); // LFTR byproduct
            MaterialGenerator.generate(ELEMENT.getInstance().HAFNIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().DYSPROSIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().ERBIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().PRASEODYMIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().TELLURIUM); // LFTR byproduct
            MaterialGenerator.generate(ELEMENT.getInstance().RHODIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().RHENIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().THALLIUM);
            MaterialGenerator.generate(ELEMENT.getInstance().GERMANIUM);

            // RADIOACTIVE ELEMENTS
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().POLONIUM, false);
            // MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().RADON, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().RADIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PROMETHIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PROTACTINIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().CURIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().CALIFORNIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().NEPTUNIUM, false);
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().FERMIUM, false);

            // Nuclear Isotopes

            // Lithium-7 is used as a part of the molten lithium fluoride in molten salt reactors: liquid-fluoride
            // nuclear reactors.
            // The large neutron-absorption cross-section of lithium-6 (about 940 barns[5]) as compared with the very
            // small
            // neutron cross-section of lithium-7 (about 45 millibarns) makes high separation of lithium-7 from natural
            // lithium a
            // strong requirement for the possible use in lithium fluoride reactors.
            MaterialGenerator.generate(ELEMENT.getInstance().LITHIUM7, false);
            // Thorium-232 is the most stable isotope of Thorium, purified for nuclear fuel use in this case.
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().THORIUM232);
            // Production of 233U (through the neutron irradiation of 232Th) invariably produces small amounts of 232U
            // as an impurity
            // because of parasitic (n,2n) reactions on uranium-233 itself, or on protactinium-233, or on thorium-232:
            MaterialGenerator.generate(ELEMENT.getInstance().URANIUM232);
            // Uranium-233 is a fissile isotope of uranium that is bred from thorium-232 as part of the thorium fuel
            // cycle.
            MaterialGenerator.generate(ELEMENT.getInstance().URANIUM233);
            // Plutonium-238 is a very powerful alpha emitter. This makes the plutonium-238 isotope suitable for usage
            // in radioisotope thermoelectric generators (RTGs)
            // and radioisotope heater units - one gram of plutonium-238 generates approximately 0.5 W of thermal power.
            MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PLUTONIUM238, false);

            // Custom Materials that will have standalone refinery processes
            MaterialGenerator.generate(ELEMENT.STANDALONE.ADVANCED_NITINOL, false);
            MaterialGenerator.generate(ELEMENT.STANDALONE.ASTRAL_TITANIUM);
            MaterialGenerator.generate(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN);
            MaterialGenerator.generate(ELEMENT.STANDALONE.HYPOGEN);
            MaterialGenerator.generate(ELEMENT.STANDALONE.CHRONOMATIC_GLASS);

            // Custom Materials that are from Runescape
            MaterialGenerator.generate(ELEMENT.STANDALONE.BLACK_METAL);
            MaterialGenerator.generate(ELEMENT.STANDALONE.WHITE_METAL);
            MaterialGenerator.generateOreMaterialWithAllExcessComponents(ELEMENT.STANDALONE.GRANITE);
            MaterialGenerator.generateOreMaterialWithAllExcessComponents(ELEMENT.STANDALONE.RUNITE);
            MaterialGenerator.generate(ELEMENT.STANDALONE.DRAGON_METAL, false);

            MISC_MATERIALS.run();

            MaterialGenerator.generate(ALLOY.SILICON_CARBIDE);
            MaterialGenerator.generate(ALLOY.ZIRCONIUM_CARBIDE);
            MaterialGenerator.generate(ALLOY.TANTALUM_CARBIDE);
            MaterialGenerator.generate(ALLOY.NIOBIUM_CARBIDE);
            MaterialGenerator.generate(ALLOY.TUNGSTEN_TITANIUM_CARBIDE);

            // LFTR Fuel components
            MaterialGenerator.generateNuclearDusts(FLUORIDES.AMMONIUM_BIFLUORIDE); // LFTR fuel component
            MaterialGenerator.generateNuclearDusts(FLUORIDES.BERYLLIUM_HYDROXIDE); // LFTR fuel component
            // MaterialGenerator.generateNuclearDusts(FLUORIDES.AMMONIUM_TETRAFLUOROBERYLLATE); // LFTR fuel component

            // Generate Fluorides
            MaterialGenerator.generateNuclearDusts(FLUORIDES.BERYLLIUM_FLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.LITHIUM_FLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.THORIUM_TETRAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.THORIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.URANIUM_TETRAFLUORIDE, false);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.URANIUM_HEXAFLUORIDE, false);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE);
            // LFTR Fluoride outputs
            MaterialGenerator.generateNuclearDusts(FLUORIDES.NEPTUNIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.TECHNETIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(FLUORIDES.SELENIUM_HEXAFLUORIDE);

            // Generate Reactor Fuel Salts
            MaterialGenerator.generateNuclearDusts(NUCLIDE.LiFBeF2ZrF4U235);
            MaterialGenerator.generateNuclearDusts(NUCLIDE.LiFBeF2ZrF4UF4);
            MaterialGenerator.generateNuclearDusts(NUCLIDE.LiFBeF2ThF4UF4);
            // MaterialGenerator.generateNuclearMaterial(NUCLIDE.Li2BeF4, false);

            // Generate some Alloys

            // Misc Alloys
            MaterialGenerator.generate(ALLOY.ENERGYCRYSTAL);
            MaterialGenerator.generate(ALLOY.BLOODSTEEL);

            MaterialGenerator.generate(ALLOY.ZERON_100);
            // Tumbaga was the name given by Spaniards to a non-specific alloy of gold and copper
            MaterialGenerator.generate(ALLOY.TUMBAGA);
            // Potin is traditionally an alloy of bronze, tin and lead, with varying quantities of each possible
            MaterialGenerator.generate(ALLOY.POTIN);

            // Staballoy & Tantalloy
            MaterialGenerator.generate(ALLOY.STABALLOY);
            MaterialGenerator.generate(ALLOY.TANTALLOY_60);
            MaterialGenerator.generate(ALLOY.TANTALLOY_61);

            // Inconel
            MaterialGenerator.generate(ALLOY.INCONEL_625);
            MaterialGenerator.generate(ALLOY.INCONEL_690);
            MaterialGenerator.generate(ALLOY.INCONEL_792);

            // Steels
            MaterialGenerator.generateDusts(ALLOY.EGLIN_STEEL_BASE);
            MaterialGenerator.generate(ALLOY.EGLIN_STEEL);
            MaterialGenerator.generate(ALLOY.MARAGING250);
            MaterialGenerator.generate(ALLOY.MARAGING300);
            MaterialGenerator.generate(ALLOY.MARAGING350);
            MaterialGenerator.generate(ALLOY.AQUATIC_STEEL);

            MaterialGenerator.generate(ALLOY.NITINOL_60, true);

            // Composite Alloys
            MaterialGenerator.generate(ALLOY.STELLITE);
            MaterialGenerator.generate(ALLOY.TALONITE);

            // Hastelloy
            MaterialGenerator.generate(ALLOY.HASTELLOY_W);
            MaterialGenerator.generate(ALLOY.HASTELLOY_X);
            MaterialGenerator.generate(ALLOY.HASTELLOY_C276);
            MaterialGenerator.generate(ALLOY.HASTELLOY_N);

            // Incoloy
            MaterialGenerator.generate(ALLOY.INCOLOY_020);
            MaterialGenerator.generate(ALLOY.INCOLOY_DS);
            MaterialGenerator.generate(ALLOY.INCOLOY_MA956);

            // Leagrisium
            MaterialGenerator.generate(ALLOY.LEAGRISIUM);

            // Super Conductor
            MaterialGenerator.generate(ALLOY.HG1223, false, false);

            // Generate Fictional Materials
            MaterialGenerator.generate(ALLOY.TRINIUM_TITANIUM);
            MaterialGenerator.generate(ALLOY.TRINIUM_NAQUADAH, false);
            MaterialGenerator.generate(ALLOY.TRINIUM_NAQUADAH_CARBON);
            MaterialGenerator.generate(ALLOY.TRINIUM_REINFORCED_STEEL);

            // Top Tier Alloys
            MaterialGenerator.generate(ALLOY.HELICOPTER);
            MaterialGenerator.generate(ALLOY.LAFIUM);
            MaterialGenerator.generate(ALLOY.CINOBITE);
            MaterialGenerator.generate(ALLOY.PIKYONIUM);
            MaterialGenerator.generate(ALLOY.ABYSSAL);
            MaterialGenerator.generate(ALLOY.LAURENIUM);

            // abs recipe in RECIPES_GREGTECH.java
            MaterialGenerator.generate(ALLOY.BOTMIUM, true, false);

            MaterialGenerator.generate(ALLOY.HS188A);

            MaterialGenerator.generate(ALLOY.TITANSTEEL);
            MaterialGenerator.generate(ALLOY.ARCANITE);
            MaterialGenerator.generate(ALLOY.OCTIRON);

            MaterialGenerator.generate(ALLOY.BABBIT_ALLOY, false);
            MaterialGenerator.generate(ALLOY.BLACK_TITANIUM, false);
            MaterialGenerator.generate(ALLOY.INDALLOY_140, false, false);

            // High Level Bioplastic
            MaterialGenerator.generate(ELEMENT.STANDALONE.RHUGNOR, false, false);

            // Must be the final Alloy to Generate
            MaterialGenerator.generate(ALLOY.QUANTUM);

            // Ores
            MaterialGenerator.generateOreMaterial(FLUORIDES.FLUORITE);
            MaterialGenerator.generateOreMaterial(ALLOY.KOBOLDITE);
            GTplusplus_Everglades.GenerateOreMaterials();

            // formula override
            ALLOY.TUNGSTEN_TITANIUM_CARBIDE.vChemicalFormula = StringUtils.subscript("(CW)7Ti3");
            ALLOY.TITANSTEEL.vChemicalFormula = StringUtils.subscript("((CW)7Ti3)3???");

            // Werkstoff bridge
            ELEMENT.getInstance().ZIRCONIUM.setWerkstoffID((short) 3);
            ELEMENT.getInstance().THORIUM232.setWerkstoffID((short) 30);
            ELEMENT.getInstance().RUTHENIUM.setWerkstoffID((short) 64);
            ELEMENT.getInstance().HAFNIUM.setWerkstoffID((short) 11000);
            ELEMENT.getInstance().IODINE.setWerkstoffID((short) 11012);

        } catch (final Throwable r) {
            Logger.INFO("Failed to Generated a Material. " + r.getMessage());
            r.printStackTrace();
        }

        // Generates four elemental shards when TC is not installed.
        if (!Thaumcraft.isModLoaded()) {
            shardAer = new BaseItemTCShard("Aer", Utils.rgbtoHexValue(225, 225, 5));
            shardIgnis = new BaseItemTCShard("Ignis", Utils.rgbtoHexValue(255, 5, 5));
            shardTerra = new BaseItemTCShard("Terra", Utils.rgbtoHexValue(5, 255, 5));
            shardAqua = new BaseItemTCShard("Aqua", Utils.rgbtoHexValue(5, 5, 255));
        } else {
            shardAer = ItemUtils
                    .getItemStackWithMeta(Thaumcraft.isModLoaded(), "Thaumcraft:ItemShard", "Air Shard", 0, 1)
                    .getItem();
            shardIgnis = ItemUtils
                    .getItemStackWithMeta(Thaumcraft.isModLoaded(), "Thaumcraft:ItemShard", "Fire Shard", 1, 1)
                    .getItem();
            shardAqua = ItemUtils
                    .getItemStackWithMeta(Thaumcraft.isModLoaded(), "Thaumcraft:ItemShard", "Warer Shard", 2, 1)
                    .getItem();
            shardTerra = ItemUtils
                    .getItemStackWithMeta(Thaumcraft.isModLoaded(), "Thaumcraft:ItemShard", "Earth Shard", 3, 1)
                    .getItem();
        }
        // Generates a set of four special dusts to be used in my recipes.
        dustAer = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().AER, true)[0];
        dustIgnis = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().IGNIS, true)[0];
        dustTerra = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().TERRA, true)[0];
        dustAqua = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().AQUA, true)[0];

        ItemUtils.generateSpecialUseDusts(MISC_MATERIALS.WOODS_GLASS, false);
        cellHydrogenChlorideMix = MISC_MATERIALS.HYDROGEN_CHLORIDE_MIX.getCell(1).getItem();

        // Nuclear Fuel Dusts
        dustLithiumCarbonate = ItemUtils.generateSpecialUseDusts(
                "LithiumCarbonate",
                "Lithium Carbonate",
                "Li2CO3",
                Utils.rgbtoHexValue(240, 240, 240))[0]; // https://en.wikipedia.org/wiki/Lithium_carbonate
        dustLithiumPeroxide = ItemUtils.generateSpecialUseDusts(
                "LithiumPeroxide",
                "Lithium Peroxide",
                "Li2O2",
                Utils.rgbtoHexValue(250, 250, 250))[0]; // https://en.wikipedia.org/wiki/Lithium_peroxide
        dustLithiumHydroxide = ItemUtils.generateSpecialUseDusts(
                "LithiumHydroxide",
                "Lithium Hydroxide",
                "LiOH",
                Utils.rgbtoHexValue(250, 250, 250))[0]; // https://en.wikipedia.org/wiki/Lithium_hydroxide

        if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1))) {
            dustQuicklime = ItemUtils
                    .generateSpecialUseDusts("Quicklime", "Quicklime", "CaO", Utils.rgbtoHexValue(255, 255, 175))[0]; // https://en.wikipedia.org/wiki/Calcium_oxide
        }
        dustCalciumHydroxide = ItemUtils.generateSpecialUseDusts(
                "CalciumHydroxide",
                "Hydrated Lime",
                "Ca(OH)2",
                Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_hydroxide
        dustCalciumCarbonate = ItemUtils.generateSpecialUseDusts(
                "CalciumCarbonate",
                "Calcium Carbonate",
                "CaCO3",
                Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_carbonate
        if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1) == null)
                || (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustCalciumSulfate", 1) == null)) {
            dustCalciumSulfate = ItemUtils.generateSpecialUseDusts(
                    "Gypsum",
                    "Calcium Sulfate (Gypsum)",
                    "CaSO4",
                    Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_sulfate
            GT_OreDictUnificator.registerOre("dustCalciumSulfate", ItemUtils.getSimpleStack(dustCalciumSulfate));
        } else {
            GT_OreDictUnificator.registerOre(
                    "dustCalciumSulfate",
                    ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1));
        }
        dustLi2CO3CaOH2 = ItemUtils.generateSpecialUseDusts(
                "Li2CO3CaOH2",
                "Li2CO3 + Ca(OH)2 Compound",
                "Li2CO3CaOH2",
                Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_carbonate
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(FLUORIDES.SODIUM_FLUORIDE, false);
        // FLiBe Fuel Compounds
        dustLi2BeF4 = ItemUtils.generateSpecialUseDusts(
                "Li2BeF4",
                "Lithium Tetrafluoroberyllate Fuel Compound",
                "Li2BeF4",
                Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/FLiBe
        Material.registerComponentForMaterial(NUCLIDE.Li2BeF4, OrePrefixes.dust, ItemUtils.getSimpleStack(dustLi2BeF4));
        // fluidFLiBeSalt = ("Li2BeF4", "Li2BeF4", 7430, new short[]{255, 255, 255, 100}, 0);
        // fluidFLiBeSalt = FluidUtils.addGTFluidNoPrefix("Li2BeF4", "Lithium Tetrafluoroberyllate", new short[]{255,
        // 255, 255, 100}, 0, 743, null, CI.emptyCells(1), 1000, true);
        // fluidFLiBeSaltBurnt = FluidUtils.addGTFluidNoPrefix("Li2BeF2UF4", "Li2BeF2UF4", new short[]{50, 255, 50,
        // 100}, 0, 743, null, CI.emptyCells(1), 1000, true);

        // LFTR Core Fluid Processing
        // fluidLftrCore1 = FluidUtils.addGTFluidNoPrefix("LiBeF2UF4FP", "LiBeF2UF4FP", new short[]{110, 255, 110, 100},
        // 0, 800, null, CI.emptyCells(1), 1000, true);
        // fluidLftrCore2 = FluidUtils.addGTFluidNoPrefix("UF6F2FP", "UF6F2FP", new short[]{150, 255, 150, 100}, 0, 800,
        // null, CI.emptyCells(1), 1000, true);
        // fluidLftrCore3 = FluidUtils.addGTFluidNoPrefix("LiFBeF2", "LiFBeF2", new short[]{100, 255, 50, 100}, 0, 800,
        // null, CI.emptyCells(1), 1000, true);
        // fluidLftrCore4 = FluidUtils.addGTFluidNoPrefix("LiFBeF2UF4", "LiFBeF2UF4", new short[]{50, 255, 100, 100}, 0,
        // 800, null, CI.emptyCells(1), 1000, true);
        // LFTR Blanket Fluid Processing
        fluidNuclearWaste = FluidUtils.addGTFluidNoPrefix(
                "nuclear.waste",
                "Nuclear Waste",
                new short[] { 10, 250, 10, 100 },
                0,
                1000,
                null,
                CI.emptyCells(1),
                1000,
                true);

        // LFTR Control Circuit
        itemCircuitLFTR = new CoreItem(
                "itemCircuitLFTR",
                "" + EnumChatFormatting.GREEN + "Control Circuit",
                AddToCreativeTab.tabMisc,
                1,
                0,
                new String[] { "Keeps Multiblocks Stable" },
                EnumRarity.epic,
                EnumChatFormatting.DARK_GREEN,
                false,
                null);

        if (CORE.ConfigSwitches.enableMachine_Pollution) {
            itemBasicTurbine = new ItemBasicScrubberTurbine();
        }

        // Zirconium
        // Cinter Pellet.
        itemZirconiumChlorideCinterPellet = new CoreItem(
                "itemZirconiumPellet",
                "Zirconium Pellet [" + StringUtils.subscript("ZrCl4") + "]",
                tabMisc).setTextureName(GTPlusPlus.ID + ":itemShard");
        GT_OreDictUnificator.registerOre("pelletZirconium", new ItemStack(itemZirconiumChlorideCinterPellet));
        // Zirconium Chloride
        dustZrCl4 = ItemUtils.generateSpecialUseDusts("ZrCl4", "ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; // http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf
        dustCookedZrCl4 = ItemUtils
                .generateSpecialUseDusts("CookedZrCl4", "Cooked ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; // http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf

        // Zirconium Tetrafluoride
        /*
         * GT_OreDictUnificator.registerOre("cellZrF4",
         * ItemUtils.getItemStackOfAmountFromOreDict("cellZirconiumTetrafluoride", 1));
         * GT_OreDictUnificator.registerOre("dustZrF4",
         * ItemUtils.getItemStackOfAmountFromOreDict("dustZirconiumTetrafluoride", 1));
         */
        fluidZrF4 = FluidUtils.generateFluidNoPrefix(
                "ZirconiumTetrafluoride",
                "Zirconium Tetrafluoride",
                500,
                new short[] { 170, 170, 140, 100 }); // https://en.wikipedia.org/wiki/Zirconium_tetrafluoride
        FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.setFluid(fluidZrF4);

        // Coolant Salt
        // NaBF4 - NaF - 621C
        // dustNaBF4NaF = ItemUtils.generateSpecialUseDusts("NaBF4NaF", "NaBF4NaF", Utils.rgbtoHexValue(45, 45, 90))[0];
        // //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride

        // Load Tree Farmer
        if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer) { // https://en.wikipedia.org/wiki/UAN
            dustFertUN18 = ItemUtils
                    .generateSpecialUseDusts("UN18Fertiliser", "UN-18 Fertiliser", Utils.rgbtoHexValue(60, 155, 60))[0];
            dustFertUN32 = ItemUtils
                    .generateSpecialUseDusts("UN32Fertiliser", "UN-32 Fertiliser", Utils.rgbtoHexValue(55, 190, 55))[0];

            ItemStack temp1 = ItemUtils.getCorrectStacktype("IC2:itemFertilizer", 1);
            ItemStack temp2 = null;

            if (Forestry.isModLoaded()) {
                temp2 = ItemUtils.getCorrectStacktype("Forestry:fertilizerCompound", 1);
            }
            if (temp1 != null) {
                fluidFertBasic = FluidUtils.generateFluidNonMolten(
                        "Fertiliser",
                        "Fertiliser",
                        32,
                        new short[] { 45, 170, 45, 100 },
                        temp1,
                        null,
                        true);
                GT_Values.RA.stdBuilder().itemInputs(temp2).fluidOutputs(new FluidStack(fluidFertBasic, 36))
                        .duration(5 * TICKS).eut(16).addTo(fluidExtractionRecipes);
            }
            fluidFertUN32 = FluidUtils.generateFluidNonMolten(
                    "UN32Fertiliser",
                    "UN-32 Fertiliser",
                    24,
                    new short[] { 55, 190, 55, 100 },
                    null,
                    null,
                    true);
            fluidFertUN18 = FluidUtils.generateFluidNonMolten(
                    "UN18Fertiliser",
                    "UN-18 Fertiliser",
                    22,
                    new short[] { 60, 155, 60, 100 },
                    null,
                    null,
                    true);

            /*
             * GT_Values.RA.addMixerRecipe( arg0, //Item In arg1, arg2, arg3, arg4, //Fluid in arg5, //Fluid Out arg6,
             * //Item out arg7, //Eu arg8); //Time
             */

        }

        // Juice
        FluidUtils.generateFluidNonMolten(
                "RaisinJuice",
                "Raisin Juice",
                2,
                new short[] { 51, 0, 51, 100 },
                ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foodRaisins", 1),
                ItemUtils.getItemStackOfAmountFromOreDictNoBroken("fruitRaisins", 1),
                50,
                true);

        // Test items
        toolGregtechPump = new GregtechPump();
        toolGregtechPump.registerPumpType(0, "Simple Hand Pump", 0, 0);
        toolGregtechPump.registerPumpType(1, "Advanced Hand Pump", 32000, 1);
        toolGregtechPump.registerPumpType(2, "Super Hand Pump", 128000, 2);
        toolGregtechPump.registerPumpType(3, "Ultimate Hand Pump", 512000, 3);

        // Xp Fluids - Dev
        if (!FluidRegistry.isFluidRegistered("mobessence")) {
            FluidUtils.generateFluidNoPrefix("mobessence", "mobessence", 0, new short[] { 125, 175, 125, 100 });
        }
        if (!FluidRegistry.isFluidRegistered("xpjuice")) {
            FluidUtils.generateFluidNoPrefix("xpjuice", "xpjuice", 0, new short[] { 50, 150, 50, 100 });
        }

        dustNeptunium238 = new DustDecayable(
                "dustNeptunium238",
                Utils.rgbtoHexValue(175, 240, 75),
                50640,
                new String[] { StringUtils.superscript("238Np"),
                        "Result: Plutonium 238 (" + StringUtils.superscript("238Pu") + ")" },
                ELEMENT.getInstance().PLUTONIUM238.getDust(1).getItem(),
                5);
        dustDecayedRadium226 = ItemUtils.generateSpecialUseDusts(
                "DecayedRadium226",
                "Decayed Radium-226",
                "Contains Radon (" + StringUtils.superscript("222Rn") + ")",
                ELEMENT.getInstance().RADIUM.getRgbAsHex())[0];
        dustRadium226 = new DustDecayable(
                "dustRadium226",
                ELEMENT.getInstance().RADIUM.getRgbAsHex(),
                90000,
                new String[] { StringUtils.superscript("226Ra"),
                        "Result: Radon (" + StringUtils.superscript("222Rn") + ")" },
                ItemUtils.getSimpleStack(dustDecayedRadium226).getItem(),
                5);
        dustProtactinium233 = new DustDecayable(
                "dustProtactinium233",
                ELEMENT.getInstance().PROTACTINIUM.getRgbAsHex(),
                32000,
                new String[] { StringUtils.superscript("233Pa"),
                        "Result: Uranium 233(" + StringUtils.superscript("233U") + ")" },
                ELEMENT.getInstance().URANIUM233.getDust(1).getItem(),
                6);
        dustMolybdenum99 = new DustDecayable(
                "dustMolybdenum99",
                ELEMENT.getInstance().MOLYBDENUM.getRgbAsHex(),
                16450,
                new String[] { StringUtils.superscript("99Mo"),
                        "Result: Technicium 99ᵐ (" + StringUtils.superscript("99ᵐTc") + ")" },
                dustTechnetium99M,
                4);

        itemIonParticleBase = new IonParticles();
        itemStandarParticleBase = new StandardBaseParticles();

        Item a8kFlask = VolumetricFlaskHelper.generateNewFlask("Volumetric_Flask_8k", "Large Volumetric Flask", 8000);
        Item a64kFlask = VolumetricFlaskHelper
                .generateNewFlask("Volumetric_Flask_32k", "Gigantic Volumetric Flask", 32000);
        GregtechItemList.VOLUMETRIC_FLASK_8k.set(a8kFlask);
        GregtechItemList.VOLUMETRIC_FLASK_32k.set(a64kFlask);

        itemBoilerChassis = new ItemBoilerChassis();
        itemDehydratorCoilWire = new ItemDehydratorCoilWire();
        itemDehydratorCoil = new ItemDehydratorCoil();

        itemAirFilter = new ItemAirFilter();
        itemLavaFilter = new ItemLavaFilter();

        // Chemistry
        new CoalTar();
        new RocketFuels();

        // Nuclear Processing
        new NuclearChem();

        // Farm Animal Fun
        new AgriculturalChem();

        // General Chemistry
        new GenericChem();

        // Milled Ore Processing
        new MilledOreProcessing();

        // IC2 Exp
        Logger.INFO("IndustrialCraft2 Found - Loading Resources.");

        // Baubles Mod Test
        try {
            final Class<?> baublesTest = ReflectionUtils.getClass("baubles.api.IBauble");
            if (baublesTest != null) {
                COMPAT_Baubles.run();
            } else {
                Logger.INFO("Baubles Not Found - Skipping Resources.");
            }
        } catch (final Throwable T) {
            Logger.INFO("Baubles Not Found - Skipping Resources.");
        }

        // Buffer Cores!
        Item itemBufferCore;
        for (int i = 1; i <= 10; i++) {
            itemBufferCore = new BufferCore("itemBufferCore", i).setCreativeTab(AddToCreativeTab.tabMachines);
            GameRegistry.registerItem(itemBufferCore, itemBufferCore.getUnlocalizedName());
        }

        itemCustomBook = new ItemBaseBook();
        registerCustomTokens();
    }

    public static void registerCustomTokens() {
        itemGenericToken.register(0, "BitCoin", 16, "Can be used on the dark web");
        itemGenericToken.register(1, "Hand Pump Trade Token I", 1, "Craft into a Tier I Hand pump");
        itemGenericToken.register(2, "Hand Pump Trade Token II", 1, "Craft into a Tier II Hand pump");
        itemGenericToken.register(3, "Hand Pump Trade Token III", 1, "Craft into a Tier III Hand pump");
        itemGenericToken.register(4, "Hand Pump Trade Token IV", 1, "Craft into a Tier IV Hand pump");
    }

    public static void registerCustomMaterialComponents() {
        // Custom GT++ Crafting Components

        /*
         * Try to generate dusts for missing rare earth materials if they don't exist
         */
        if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGadolinium", 1))) {
            ItemUtils.generateSpecialUseDusts(
                    "Gadolinium",
                    "Gadolinium",
                    Materials.Gadolinium.mElement.name(),
                    Utils.rgbtoHexValue(226, 172, 9));
        }
        if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustYtterbium", 1))) {
            ItemUtils.generateSpecialUseDusts(
                    "Ytterbium",
                    "Ytterbium",
                    Materials.Ytterbium.mElement.name(),
                    Utils.rgbtoHexValue(
                            Materials.Yttrium.mRGBa[0] - 60,
                            Materials.Yttrium.mRGBa[1] - 60,
                            Materials.Yttrium.mRGBa[2] - 60));
        }
        if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSamarium", 1))) {
            ItemUtils.generateSpecialUseDusts(
                    "Samarium",
                    "Samarium",
                    Materials.Samarium.mElement.name(),
                    Utils.rgbtoHexValue(161, 168, 114));
        }
        if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustLanthanum", 1))) {
            ItemUtils.generateSpecialUseDusts(
                    "Lanthanum",
                    "Lanthanum",
                    Materials.Lanthanum.mElement.name(),
                    Utils.rgbtoHexValue(106, 127, 163));
        }

        // Just an unusual plate needed for some black magic.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateClay", 1) == null) {
            itemPlateClay = new BaseItemPlate(NONMATERIAL.CLAY);
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleClay", 1) == null) {
            itemDoublePlateClay = new BaseItemPlateDouble(NONMATERIAL.CLAY);
        }

        // Need this for Mutagenic Frames
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilUranium235", 1) == null) {
            itemFoilUranium235 = new BaseItemFoil(ELEMENT.getInstance().URANIUM235);
        }

        // A small gear needed for wizardry.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gearGtSmallWroughtIron", 1) == null) {
            itemSmallWroughtIronGear = new BaseItemSmallGear(NONMATERIAL.WROUGHT_IRON);
        }
        // Krypton Processing
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1) == null) {
            itemHotTitaniumIngot = ItemUtils
                    .getSimpleStack(new BaseItemIngot(ELEMENT.getInstance().TITANIUM, ComponentTypes.HOTINGOT));
        } else {
            itemHotTitaniumIngot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1);
        }

        // Need this for Laurenium
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustIndium", 1) == null) {
            itemDustIndium = new BaseItemDust(ELEMENT.getInstance().INDIUM);
        }

        // Springs
        MaterialUtils
                .generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, ELEMENT.STANDALONE.WHITE_METAL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, ALLOY.NITINOL_60);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, ALLOY.AQUATIC_STEEL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, ALLOY.EGLIN_STEEL);

        // Small Springs
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, ALLOY.MARAGING250);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, ALLOY.NICHROME);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, ALLOY.STABALLOY);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, ALLOY.STEEL_BLACK);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, ALLOY.BLACK_TITANIUM);

        // Fine Wire
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ELEMENT.STANDALONE.WHITE_METAL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ELEMENT.getInstance().PALLADIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ELEMENT.getInstance().ZIRCONIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ALLOY.LEAGRISIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ALLOY.BABBIT_ALLOY);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ALLOY.KOBOLDITE);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ALLOY.HG1223);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ALLOY.QUANTUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ELEMENT.STANDALONE.HYPOGEN);
        MaterialUtils
                .generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, ELEMENT.STANDALONE.CHRONOMATIC_GLASS);

        // Foil
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.BLACK_TITANIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.BOTMIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.TITANSTEEL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.NITINOL_60);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.QUANTUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.LAURENIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.HYPOGEN);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.ASTRAL_TITANIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.RHUGNOR);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.WHITE_METAL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.ADVANCED_NITINOL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.PIKYONIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.CINOBITE);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.LAFIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ALLOY.TRINIUM_REINFORCED_STEEL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, ELEMENT.STANDALONE.CHRONOMATIC_GLASS);

        // Special Sillyness
        if (true) {

            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateSodium", 1) == null) {
                new BaseItemPlate(ELEMENT.getInstance().SODIUM);
            }

            Material meatRaw = NONMATERIAL.MEAT;
            // A plate of Meat.
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateMeatRaw", 1) == null) {
                itemPlateRawMeat = new BaseItemPlate(meatRaw);
                ItemUtils.registerFuel(ItemUtils.getSimpleStack(itemPlateRawMeat), 100);
            }
            // A Block of Meat.
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("blockMeatRaw", 1) == null) {
                blockRawMeat = new BlockBaseModular(meatRaw, BlockTypes.STANDARD);
                ItemUtils.registerFuel(ItemUtils.getSimpleStack(blockRawMeat), 900);
            }
        }

        // A plate of Vanadium.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateVanadium", 1) == null) {
            itemPlateVanadium = new BaseItemPlate(ELEMENT.getInstance().VANADIUM);
        }

        // A plate of Lithium.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateLithium", 1) == null) {
            itemPlateLithium = new BaseItemPlate(ELEMENT.getInstance().LITHIUM);
        }

        // A plate of Europium.
        if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateEuropium", 1) == null)
                && CORE.ConfigSwitches.enableCustom_Pipes) {
            itemPlateEuropium = new BaseItemPlate(ELEMENT.getInstance().EUROPIUM);
        }
        if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleEuropium", 1) == null)
                && CORE.ConfigSwitches.enableCustom_Pipes) {
            itemDoublePlateEuropium = new BaseItemPlateDouble(ELEMENT.getInstance().EUROPIUM);
        }

        // Tumbaga Mix (For Simple Crafting)
        dustTumbagaMix = ItemUtils
                .generateSpecialUseDusts("MixTumbaga", "Tumbaga Mix", "Au2Cu", Utils.rgbtoHexValue(255, 150, 80))[0];
    }
}
