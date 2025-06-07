package gtPlusPlus.core.item;

import static codechicken.nei.api.API.hideItem;
import static gregtech.api.enums.Mods.Baubles;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.common.compat.CompatBaubles;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.BaseItemDamageable;
import gtPlusPlus.core.item.base.BaseItemTCShard;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.foil.BaseItemFoil;
import gtPlusPlus.core.item.base.gears.BaseItemSmallGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.chemistry.StandardBaseParticles;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.item.circuit.GTPPIntegratedCircuitItem;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.food.BaseItemMetaFood;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.item.general.ItemBufferCore;
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
import gtPlusPlus.core.item.tool.misc.ItemGregtechPump;
import gtPlusPlus.core.item.wearable.WearableLoader;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOther;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import toxiceverglades.GTPPEverglades;

public final class ModItems {

    public static ItemCustomSpawnEgg itemCustomSpawnEgg;

    public static Item itemIngotBatteryAlloy;

    public static Item itemBedLocator_Base;
    public static Item itemBaseItemWithCharge;

    public static Item itemPersonalCloakingDevice;
    public static Item itemPersonalHealingDevice;
    public static Item itemSupremePizzaGloves;

    public static Item dustLithiumCarbonate;
    public static Item dustLithiumHydroxide;
    public static Item dustLithiumPeroxide;

    public static Item dustQuicklime;
    public static Item dustCalciumHydroxide;
    public static Item dustCalciumCarbonate;
    public static Item dustLi2CO3CaOH2;
    public static Item dustLi2BeF4;

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

    public static Item itemCircuitLFTR;

    public static Item itemHalfCompleteCasings;

    public static Item itemCustomBook;

    // Unstable Elements & Related Content
    public static Item dustNeptunium238;
    public static Item dustNeptunium239;
    public static Item dustDecayedRadium226;
    public static Item dustRadium226;
    public static Item dustProtactinium233;

    public static ItemGregtechPump toolGregtechPump;

    public static ItemGenericToken itemGenericToken;

    public static ItemStack itemHotTitaniumIngot;

    public static Fluid fluidZrF4;
    public static Fluid fluidFertBasic;
    public static Fluid fluidFertUN32;
    public static Fluid fluidFertUN18;

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

    public static ItemDummyResearch itemDummyResearch;

    public static BaseItemMetaFood itemMetaFood;

    public static void init() {

        Item magicFeather = new ItemMagicFeather();
        GregtechItemList.MagicFeather.set(magicFeather);
        MinecraftForge.EVENT_BUS.register(magicFeather);
        FMLCommonHandler.instance()
            .bus()
            .register(magicFeather);

        GregtechItemList.AlkalusDisk.set(
            new BaseItemDamageable(
                "itemAlkalusDisk",
                AddToCreativeTab.tabMisc,
                1,
                0,
                "Unknown Use",
                EnumRarity.rare,
                EnumChatFormatting.AQUA,
                false,
                null));

        itemGenericToken = new ItemGenericToken();
        itemDummyResearch = new ItemDummyResearch();
        itemCustomSpawnEgg = new ItemCustomSpawnEgg();

        // Register meta item, because we need them for everything.
        MetaGeneratedGregtechItems.INSTANCE.generateMetaItems();

        // Register Hydrogen Blobs first, so we can replace old helium blobs.
        // Register Old Helium Blob, this will be replaced when held by a player.

        // Load Wearable Items
        WearableLoader.run();

        GregtechItemList.BlueprintBase.set(new ItemBlueprint("itemBlueprint"));

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
            MaterialGenerator.generate(MaterialsElements.getInstance().SELENIUM); // LFTR byproduct
            MaterialGenerator.generate(MaterialsElements.getInstance().BROMINE);
            MaterialGenerator.generate(MaterialsElements.getInstance().KRYPTON); // LFTR byproduct
            MaterialGenerator.generate(MaterialsElements.getInstance().IODINE); // LFTR byproduct
            MaterialGenerator.generate(MaterialsElements.getInstance().RHENIUM);
            MaterialGenerator.generate(MaterialsElements.getInstance().THALLIUM);
            MaterialGenerator.generate(MaterialsElements.getInstance().GERMANIUM);

            // RADIOACTIVE ELEMENTS
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().POLONIUM, false);
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().RADIUM, false);
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().PROTACTINIUM, false);
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().CURIUM, false);
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().NEPTUNIUM, false);
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().FERMIUM, false);

            // Nuclear Isotopes

            // Lithium-7 is used as a part of the molten lithium fluoride in molten salt reactors: liquid-fluoride
            // nuclear reactors.
            // The large neutron-absorption cross-section of lithium-6 (about 940 barns[5]) as compared with the very
            // small
            // neutron cross-section of lithium-7 (about 45 millibarns) makes high separation of lithium-7 from natural
            // lithium a
            // strong requirement for the possible use in lithium fluoride reactors.
            MaterialGenerator.generate(MaterialsElements.getInstance().LITHIUM7, false);
            // Production of 233U (through the neutron irradiation of 232Th) invariably produces small amounts of 232U
            // as an impurity
            // because of parasitic (n,2n) reactions on uranium-233 itself, or on protactinium-233, or on thorium-232:
            MaterialGenerator.generate(MaterialsElements.getInstance().URANIUM232);
            // Uranium-233 is a fissile isotope of uranium that is bred from thorium-232 as part of the thorium fuel
            // cycle.
            MaterialGenerator.generate(MaterialsElements.getInstance().URANIUM233);
            // Plutonium-238 is a very powerful alpha emitter. This makes the plutonium-238 isotope suitable for usage
            // in radioisotope thermoelectric generators (RTGs)
            // and radioisotope heater units - one gram of plutonium-238 generates approximately 0.5 W of thermal power.
            MaterialGenerator.generateNuclearMaterial(MaterialsElements.getInstance().PLUTONIUM238, false);

            // Custom Materials that will have standalone refinery processes
            MaterialGenerator.generate(MaterialsElements.STANDALONE.ADVANCED_NITINOL, false);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.ASTRAL_TITANIUM);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.HYPOGEN);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS);

            // Custom Materials that are from Runescape
            MaterialGenerator.generate(MaterialsElements.STANDALONE.BLACK_METAL);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.WHITE_METAL);
            MaterialGenerator.generateOreMaterialWithAllExcessComponents(MaterialsElements.STANDALONE.GRANITE);
            MaterialGenerator.generateOreMaterialWithAllExcessComponents(MaterialsElements.STANDALONE.RUNITE);
            MaterialGenerator.generate(MaterialsElements.STANDALONE.DRAGON_METAL);

            MaterialMisc.run();

            MaterialGenerator.generate(MaterialsAlloy.SILICON_CARBIDE);
            MaterialGenerator.generate(MaterialsAlloy.ZIRCONIUM_CARBIDE);
            MaterialGenerator.generate(MaterialsAlloy.TANTALUM_CARBIDE);
            MaterialGenerator.generate(MaterialsAlloy.NIOBIUM_CARBIDE);
            MaterialGenerator.generate(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE);

            // LFTR Fuel components
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.AMMONIUM_BIFLUORIDE); // LFTR fuel component
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.BERYLLIUM_HYDROXIDE); // LFTR fuel component
            // MaterialGenerator.generateNuclearDusts(FLUORIDES.AMMONIUM_TETRAFLUOROBERYLLATE); // LFTR fuel component

            // Generate Fluorides
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.BERYLLIUM_FLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.LITHIUM_FLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.THORIUM_TETRAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.THORIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.URANIUM_TETRAFLUORIDE, false);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.URANIUM_HEXAFLUORIDE, false);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE);
            // LFTR Fluoride outputs
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.NEPTUNIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.TECHNETIUM_HEXAFLUORIDE);
            MaterialGenerator.generateNuclearDusts(MaterialsFluorides.SELENIUM_HEXAFLUORIDE);

            // Generate Reactor Fuel Salts
            MaterialGenerator.generateNuclearDusts(MaterialsNuclides.LiFBeF2ZrF4U235);
            MaterialGenerator.generateNuclearDusts(MaterialsNuclides.LiFBeF2ZrF4UF4);
            MaterialGenerator.generateNuclearDusts(MaterialsNuclides.LiFBeF2ThF4UF4);
            // MaterialGenerator.generateNuclearMaterial(NUCLIDE.Li2BeF4, false);

            // Generate some Alloys

            // Misc Alloys
            MaterialGenerator.generate(MaterialsAlloy.ENERGYCRYSTAL);
            MaterialGenerator.generate(MaterialsAlloy.BLOODSTEEL);

            MaterialGenerator.generate(MaterialsAlloy.ZERON_100);
            // Tumbaga was the name given by Spaniards to a non-specific alloy of gold and copper
            MaterialGenerator.generate(MaterialsAlloy.TUMBAGA);
            // Potin is traditionally an alloy of bronze, tin and lead, with varying quantities of each possible
            MaterialGenerator.generate(MaterialsAlloy.POTIN);

            // Staballoy & Tantalloy
            MaterialGenerator.generate(MaterialsAlloy.STABALLOY);
            MaterialGenerator.generate(MaterialsAlloy.TANTALLOY_60);
            MaterialGenerator.generate(MaterialsAlloy.TANTALLOY_61);

            // Inconel
            MaterialGenerator.generate(MaterialsAlloy.INCONEL_625);
            MaterialGenerator.generate(MaterialsAlloy.INCONEL_690);
            MaterialGenerator.generate(MaterialsAlloy.INCONEL_792);

            // Steels
            MaterialGenerator.generateDusts(MaterialsAlloy.EGLIN_STEEL_BASE);
            MaterialGenerator.generate(MaterialsAlloy.EGLIN_STEEL);
            MaterialGenerator.generate(MaterialsAlloy.MARAGING250);
            MaterialGenerator.generate(MaterialsAlloy.MARAGING300);
            MaterialGenerator.generate(MaterialsAlloy.MARAGING350);
            MaterialGenerator.generate(MaterialsAlloy.AQUATIC_STEEL);

            MaterialGenerator.generate(MaterialsAlloy.NITINOL_60, true);

            // Composite Alloys
            MaterialGenerator.generate(MaterialsAlloy.STELLITE);
            MaterialGenerator.generate(MaterialsAlloy.TALONITE);

            // Hastelloy
            MaterialGenerator.generate(MaterialsAlloy.HASTELLOY_W);
            MaterialGenerator.generate(MaterialsAlloy.HASTELLOY_X);
            MaterialGenerator.generate(MaterialsAlloy.HASTELLOY_C276);
            MaterialGenerator.generate(MaterialsAlloy.HASTELLOY_N);

            // Incoloy
            MaterialGenerator.generate(MaterialsAlloy.INCOLOY_020);
            MaterialGenerator.generate(MaterialsAlloy.INCOLOY_DS);
            MaterialGenerator.generate(MaterialsAlloy.INCOLOY_MA956);

            // Leagrisium
            MaterialGenerator.generate(MaterialsAlloy.LEAGRISIUM);

            // Super Conductor
            MaterialGenerator.generate(MaterialsAlloy.HG1223, false, false);

            // Generate Fictional Materials
            MaterialGenerator.generate(MaterialsAlloy.TRINIUM_TITANIUM);
            MaterialGenerator.generate(MaterialsAlloy.TRINIUM_NAQUADAH, false);
            MaterialGenerator.generate(MaterialsAlloy.TRINIUM_NAQUADAH_CARBON);
            MaterialGenerator.generate(MaterialsAlloy.TRINIUM_REINFORCED_STEEL);

            // Top Tier Alloys
            MaterialGenerator.generate(MaterialsAlloy.HELICOPTER);
            MaterialGenerator.generate(MaterialsAlloy.LAFIUM);
            MaterialGenerator.generate(MaterialsAlloy.CINOBITE);
            MaterialGenerator.generate(MaterialsAlloy.PIKYONIUM);
            MaterialGenerator.generate(MaterialsAlloy.ABYSSAL);
            MaterialGenerator.generate(MaterialsAlloy.LAURENIUM);

            // abs recipe in RECIPES_GREGTECH.java
            MaterialGenerator.generate(MaterialsAlloy.BOTMIUM, true, false);

            MaterialGenerator.generate(MaterialsAlloy.HS188A);

            MaterialGenerator.generate(MaterialsAlloy.TITANSTEEL);
            MaterialGenerator.generate(MaterialsAlloy.ARCANITE);
            MaterialGenerator.generate(MaterialsAlloy.OCTIRON);

            MaterialGenerator.generate(MaterialsAlloy.BABBIT_ALLOY, false);
            MaterialGenerator.generate(MaterialsAlloy.BLACK_TITANIUM, false);
            MaterialGenerator.generate(MaterialsAlloy.INDALLOY_140, false, false);

            // High Level Bioplastic
            MaterialGenerator.generate(MaterialsElements.STANDALONE.RHUGNOR, false, false);

            // Must be the final Alloy to Generate
            MaterialGenerator.generate(MaterialsAlloy.QUANTUM);

            // Ores
            MaterialGenerator.generateOreMaterial(MaterialsFluorides.FLUORITE);
            MaterialGenerator.generateOreMaterial(MaterialsAlloy.KOBOLDITE);
            GTPPEverglades.GenerateOreMaterials();

            // formula override
            MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.vChemicalFormula = StringUtils.subscript("(CW)7Ti3");

            // Werkstoff bridge
            MaterialsElements.getInstance().ZIRCONIUM.setWerkstoffID((short) 3);
            MaterialsElements.getInstance().THORIUM232.setWerkstoffID((short) 30);
            MaterialsElements.getInstance().RUTHENIUM.setWerkstoffID((short) 64);
            MaterialsElements.getInstance().HAFNIUM.setWerkstoffID((short) 11000);
            MaterialsElements.getInstance().IODINE.setWerkstoffID((short) 11012);

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
            shardAer = getModItem(Thaumcraft.ID, "ItemShard", 1, 0).getItem();
            shardIgnis = getModItem(Thaumcraft.ID, "ItemShard", 1, 1).getItem();
            shardAqua = getModItem(Thaumcraft.ID, "ItemShard", 1, 2).getItem();
            shardTerra = getModItem(Thaumcraft.ID, "ItemShard", 1, 3).getItem();
        }
        // Generates a set of four special dusts to be used in my recipes.
        dustAer = ItemUtils.generateSpecialUseDusts(MaterialsElements.getInstance().AER, true)[0];
        dustIgnis = ItemUtils.generateSpecialUseDusts(MaterialsElements.getInstance().IGNIS, true)[0];
        dustTerra = ItemUtils.generateSpecialUseDusts(MaterialsElements.getInstance().TERRA, true)[0];
        dustAqua = ItemUtils.generateSpecialUseDusts(MaterialsElements.getInstance().AQUA, true)[0];

        ItemUtils.generateSpecialUseDusts(MaterialMisc.WOODS_GLASS, false);
        cellHydrogenChlorideMix = MaterialMisc.HYDROGEN_CHLORIDE_MIX.getCell(1)
            .getItem();

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

        if (ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1) == null) {
            dustQuicklime = ItemUtils
                .generateSpecialUseDusts("Quicklime", "Quicklime", "CaO", Utils.rgbtoHexValue(255, 255, 175))[0]; // https://en.wikipedia.org/wiki/Calcium_oxide
        }
        dustCalciumHydroxide = ItemUtils.generateSpecialUseDusts(
            "CalciumHydroxide",
            "Hydrated Lime",
            "CaO2H2",
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
            GTOreDictUnificator.registerOre("dustCalciumSulfate", new ItemStack(dustCalciumSulfate));
        } else {
            GTOreDictUnificator
                .registerOre("dustCalciumSulfate", ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1));
        }
        dustLi2CO3CaOH2 = ItemUtils.generateSpecialUseDusts(
            "Li2CO3CaOH2",
            "Li2CO3 + CaO2H2 Compound",
            "Li2CO3CaO2H2",
            Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_carbonate
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(MaterialsFluorides.SODIUM_FLUORIDE, false);
        // FLiBe Fuel Compounds
        dustLi2BeF4 = ItemUtils.generateSpecialUseDusts(
            "Li2BeF4",
            "Lithium Tetrafluoroberyllate Fuel Compound",
            "Li2BeF4",
            Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/FLiBe
        Material.registerComponentForMaterial(MaterialsNuclides.Li2BeF4, OrePrefixes.dust, new ItemStack(dustLi2BeF4));

        Item[] phthalicAnhydride = ItemUtils.generateSpecialUseDusts(
            "PhthalicAnhydride",
            "Phthalic Anhydride",
            "C6H4(CO)2O",
            Utils.rgbtoHexValue(175, 175, 175));
        GregtechItemList.PhthalicAnhydrideDust.set(phthalicAnhydride[0]);
        GregtechItemList.SmallPhthalicAnhydrideDust.set(phthalicAnhydride[1]);
        GregtechItemList.TinyPhthalicAnhydrideDust.set(phthalicAnhydride[2]);

        Item[] lithiumHydroperoxide = ItemUtils.generateSpecialUseDusts(
            "LithiumHydroperoxide",
            "Lithium Hydroperoxide",
            "HLiO2",
            Utils.rgbtoHexValue(125, 125, 125));
        GregtechItemList.LithiumHydroperoxide.set(lithiumHydroperoxide[0]);
        GregtechItemList.SmallLithiumHydroperoxide.set(lithiumHydroperoxide[1]);
        GregtechItemList.TinyLithiumHydroperoxide.set(lithiumHydroperoxide[2]);

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
            ItemList.Cell_Empty.get(1),
            1000,
            true);

        // LFTR Control Circuit
        itemCircuitLFTR = new CoreItem(
            "itemCircuitLFTR",
            EnumChatFormatting.GREEN + "Control Circuit",
            AddToCreativeTab.tabMisc,
            1,
            0,
            new String[] { "Keeps Multiblocks Stable" },
            EnumRarity.epic,
            EnumChatFormatting.DARK_GREEN,
            false,
            null);

        Item basicTurbine = new ItemBasicScrubberTurbine();
        GregtechItemList.BasicIronTurbine.set(new ItemStack(basicTurbine));
        GregtechItemList.BasicBronzeTurbine.set(new ItemStack(basicTurbine, 1, 1));
        GregtechItemList.BasicSteelTurbine.set(new ItemStack(basicTurbine, 1, 2));

        // Zirconium
        // Cinter Pellet.
        itemZirconiumChlorideCinterPellet = new CoreItem(
            "itemZirconiumPellet",
            "Zirconium Pellet [" + StringUtils.subscript("ZrCl4") + "]",
            tabMisc).setTextureName(GTPlusPlus.ID + ":itemShard");
        GTOreDictUnificator.registerOre("pelletZirconium", new ItemStack(itemZirconiumChlorideCinterPellet));
        // Zirconium Chloride
        dustZrCl4 = ItemUtils.generateSpecialUseDusts("ZrCl4", "ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; // http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf
        dustCookedZrCl4 = ItemUtils
            .generateSpecialUseDusts("CookedZrCl4", "Cooked ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; // http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf

        // Zirconium Tetrafluoride
        /*
         * GTOreDictUnificator.registerOre("cellZrF4",
         * ItemUtils.getItemStackOfAmountFromOreDict("cellZirconiumTetrafluoride", 1));
         * GTOreDictUnificator.registerOre("dustZrF4",
         * ItemUtils.getItemStackOfAmountFromOreDict("dustZirconiumTetrafluoride", 1));
         */
        fluidZrF4 = FluidUtils.generateFluidNoPrefix(
            "ZirconiumTetrafluoride",
            "Zirconium Tetrafluoride",
            500,
            new short[] { 170, 170, 140, 100 }); // https://en.wikipedia.org/wiki/Zirconium_tetrafluoride
        MaterialsFluorides.ZIRCONIUM_TETRAFLUORIDE.setFluid(fluidZrF4);

        // Coolant Salt
        // NaBF4 - NaF - 621C
        // dustNaBF4NaF = ItemUtils.generateSpecialUseDusts("NaBF4NaF", "NaBF4NaF", Utils.rgbtoHexValue(45, 45, 90))[0];
        // //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride

        // Load Tree Farmer
        // https://en.wikipedia.org/wiki/UAN
        dustFertUN18 = ItemUtils
            .generateSpecialUseDusts("UN18Fertiliser", "UN-18 Fertiliser", Utils.rgbtoHexValue(60, 155, 60))[0];
        dustFertUN32 = ItemUtils
            .generateSpecialUseDusts("UN32Fertiliser", "UN-32 Fertiliser", Utils.rgbtoHexValue(55, 190, 55))[0];

        if (Forestry.isModLoaded()) {
            ItemStack temp1 = ItemList.IC2_Fertilizer.get(1);
            ItemStack temp2 = ItemList.FR_Fertilizer.get(1);
            if (temp1 != null && temp2 != null) {
                fluidFertBasic = FluidUtils.generateFluidNonMolten(
                    "Fertiliser",
                    "Fertiliser",
                    32,
                    new short[] { 45, 170, 45, 100 },
                    temp1,
                    null,
                    true);
                GTValues.RA.stdBuilder()
                    .itemInputs(temp2)
                    .fluidOutputs(new FluidStack(fluidFertBasic, 36))
                    .duration(5 * TICKS)
                    .eut(16)
                    .addTo(fluidExtractionRecipes);
            }
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
        toolGregtechPump = new ItemGregtechPump();
        toolGregtechPump.registerPumpType(0, "Simple Hand Pump", 0, 0);
        toolGregtechPump.registerPumpType(1, "Advanced Hand Pump", 32000, 1);
        toolGregtechPump.registerPumpType(2, "Super Hand Pump", 128000, 2);
        toolGregtechPump.registerPumpType(3, "Ultimate Hand Pump", 512000, 3);
        toolGregtechPump.registerPumpType(4, "Expandable Hand Pump", 0, 4);

        GregtechItemList.SimpleHandPump.set(new ItemStack(ModItems.toolGregtechPump, 1, 1000));
        GregtechItemList.AdvancedHandPump.set(new ItemStack(ModItems.toolGregtechPump, 1, 1001));
        GregtechItemList.SuperHandPump.set(new ItemStack(ModItems.toolGregtechPump, 1, 1002));
        GregtechItemList.UltimateHandPump.set(new ItemStack(ModItems.toolGregtechPump, 1, 1003));
        GregtechItemList.ExpandableHandPump.set(new ItemStack(ModItems.toolGregtechPump, 1, 1004));

        // Xp Fluids - Dev
        if (!FluidRegistry.isFluidRegistered("mobessence")) {
            FluidUtils.generateFluidNoPrefix("mobessence", "Mob Essence", 0, new short[] { 125, 175, 125, 100 });
        }

        dustNeptunium238 = new DustDecayable(
            "dustNeptunium238",
            0xAFF04B,
            50000,
            new String[] { StringUtils.superscript("238Np"),
                "Result: Plutonium 238 (" + StringUtils.superscript("238Pu") + ")" },
            MaterialsElements.getInstance().PLUTONIUM238.getDust(1),
            5,
            GTRecipeConstants.DecayType.BetaMinus);
        dustNeptunium239 = new DustDecayable(
            "dustNeptunium239",
            0x71F045,
            25000,
            new String[] { StringUtils.superscript("238Np"),
                "Result: Plutonium 239 (" + StringUtils.superscript("239Pu") + ")" },
            Materials.Plutonium.getDust(1),
            5,
            GTRecipeConstants.DecayType.BetaMinus);
        dustDecayedRadium226 = ItemUtils.generateSpecialUseDusts(
            "DecayedRadium226",
            "Decayed Radium-226",
            "Contains Radon (" + StringUtils.superscript("222Rn") + ")",
            MaterialsElements.getInstance().RADIUM.getRgbAsHex())[0];
        dustRadium226 = new DustDecayable(
            "dustRadium226",
            MaterialsElements.getInstance().RADIUM.getRgbAsHex(),
            90000,
            new String[] { StringUtils.superscript("226Ra"),
                "Result: Radon (" + StringUtils.superscript("222Rn") + ")" },
            new ItemStack(dustDecayedRadium226),
            5,
            GTRecipeConstants.DecayType.Alpha);
        dustProtactinium233 = new DustDecayable(
            "dustProtactinium233",
            MaterialsElements.getInstance().PROTACTINIUM.getRgbAsHex(),
            32000,
            new String[] { StringUtils.superscript("233Pa"),
                "Result: Uranium 233 (" + StringUtils.superscript("233U") + ")" },
            MaterialsElements.getInstance().URANIUM233.getDust(1),
            6,
            GTRecipeConstants.DecayType.BetaMinus);

        itemIonParticleBase = new IonParticles();
        itemStandarParticleBase = new StandardBaseParticles();

        Item a8kFlask = VolumetricFlaskHelper.generateNewFlask("Volumetric_Flask_8k", "Large Volumetric Flask", 16000);
        Item a64kFlask = VolumetricFlaskHelper
            .generateNewFlask("Volumetric_Flask_32k", "Gigantic Volumetric Flask", 256000);
        Item aMAXINTFlask = VolumetricFlaskHelper
            .generateNewFlask("Volumetric_Flask_Infinite", "Klein Bottle", 2147483647);
        GregtechItemList.VOLUMETRIC_FLASK_8k.set(a8kFlask);
        GregtechItemList.VOLUMETRIC_FLASK_32k.set(a64kFlask);
        GregtechItemList.KLEIN_BOTTLE.set(aMAXINTFlask);

        itemBoilerChassis = new ItemBoilerChassis();
        itemDehydratorCoilWire = new ItemDehydratorCoilWire();
        itemDehydratorCoil = new ItemDehydratorCoil();
        GregtechItemList.DehydratorCoilWireEV.set(new ItemStack(itemDehydratorCoilWire, 1, 0));
        GregtechItemList.DehydratorCoilWireIV.set(new ItemStack(itemDehydratorCoilWire, 1, 1));
        GregtechItemList.DehydratorCoilWireLuV.set(new ItemStack(itemDehydratorCoilWire, 1, 2));
        GregtechItemList.DehydratorCoilWireZPM.set(new ItemStack(itemDehydratorCoilWire, 1, 3));

        itemAirFilter = new ItemAirFilter();
        itemLavaFilter = new ItemLavaFilter();

        // Chemistry
        Item[] formaldehydeCatalyst = ItemUtils.generateSpecialUseDusts(
            "FormaldehydeCatalyst",
            "Formaldehyde Catalyst",
            "Fe16V1",
            Utils.rgbtoHexValue(25, 5, 25));
        GregtechItemList.FormaldehydeCatalystDust.set(formaldehydeCatalyst[0]);
        GregtechItemList.SmallFormaldehydeCatalystDust.set(formaldehydeCatalyst[1]);
        GregtechItemList.TinyFormaldehydeCatalystDust.set(formaldehydeCatalyst[2]);

        Item[] ammoniumNitrate = ItemUtils.generateSpecialUseDusts(
            "AmmoniumNitrate",
            "Ammonium Nitrate",
            "N2H4O3",
            Utils.rgbtoHexValue(150, 75, 150));
        GregtechItemList.AmmoniumNitrateDust.set(ammoniumNitrate[0]);
        GregtechItemList.SmallAmmoniumNitrateDust.set(ammoniumNitrate[1]);
        GregtechItemList.TinyAmmoniumNitrateDust.set(ammoniumNitrate[2]);

        // Farm Animal Fun
        // Nitrogen, Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
        Item[] manureByproducts = ItemUtils.generateSpecialUseDusts(
            "ManureByproducts",
            "Manure Byproduct",
            "(N2H4O3)N2P2Ca3CuC8",
            Utils.rgbtoHexValue(110, 75, 25));
        GregtechItemList.ManureByproductsDust.set(manureByproducts[0]);
        GregtechItemList.SmallManureByproductsDust.set(manureByproducts[1]);
        GregtechItemList.TinyManureByproductsDust.set(manureByproducts[2]);

        // Basically Guano
        Item[] organicFertilizer = ItemUtils.generateSpecialUseDusts(
            "OrganicFertilizer",
            "Organic Fertilizer",
            "Ca5(PO4)3(OH)",
            Utils.rgbtoHexValue(240, 240, 240));
        GregtechItemList.OrganicFertilizerDust.set(organicFertilizer[0]);
        GregtechItemList.SmallOrganicFertilizerDust.set(organicFertilizer[1]);
        GregtechItemList.TinyOrganicFertilizerDust.set(organicFertilizer[2]);

        // Dirt Dust :)
        Item[] dirt = ItemUtils.generateSpecialUseDusts("Dirt", "Dried Earth", Utils.rgbtoHexValue(65, 50, 15));
        GregtechItemList.DriedEarthDust.set(dirt[0]);
        GregtechItemList.SmallDriedEarthDust.set(dirt[1]);
        GregtechItemList.TinyDriedEarthDust.set(dirt[2]);

        GregtechItemList.Algae.set(new ItemAlgaeBase());

        // TODO Remove after 2.8
        Item bioSelector = new GTPPIntegratedCircuitItem("BioRecipeSelector", "bioscience/BioCircuit");
        hideItem(new ItemStack(bioSelector));

        Item agrichemItem = new ItemAgrichemBase();
        GregtechItemList.AlgaeBiomass.set(new ItemStack(agrichemItem));
        GregtechItemList.GreenAlgaeBiomass.set(new ItemStack(agrichemItem, 1, 1))
            .registerOre("biomassGreenAlgae");
        GregtechItemList.BrownAlgaeBiomass.set(new ItemStack(agrichemItem, 1, 2))
            .registerOre("biomassBrownAlgae");
        GregtechItemList.GoldenBrownAlgaeBiomass.set(new ItemStack(agrichemItem, 1, 3))
            .registerOre("biomassGoldenBrownAlgae");
        GregtechItemList.RedAlgaeBiomass.set(new ItemStack(agrichemItem, 1, 4))
            .registerOre("biomassRedAlgae");

        GregtechItemList.CelluloseFiber.set(new ItemStack(agrichemItem, 1, 5))
            .registerOre("fiberCellulose");
        GregtechItemList.GoldenBrownCelluloseFiber.set(new ItemStack(agrichemItem, 1, 6))
            .registerOre("fiberCellulose", "fiberGoldenBrownCellulose");
        GregtechItemList.RedCelluloseFiber.set(new ItemStack(agrichemItem, 1, 7))
            .registerOre("fiberCellulose", "fiberRedCellulose");

        GregtechItemList.Compost.set(new ItemStack(agrichemItem, 1, 8));
        GregtechItemList.WoodPellet.set(new ItemStack(agrichemItem, 1, 9))
            .registerOre("pelletWood");
        GregtechItemList.WoodBrick.set(new ItemStack(agrichemItem, 1, 10))
            .registerOre("brickWood");
        GregtechItemList.CellulosePulp.set(new ItemStack(agrichemItem, 1, 11))
            .registerOre("pulpCellulose");
        GregtechItemList.RawBioResin.set(new ItemStack(agrichemItem, 1, 12));

        GregtechItemList.EmptyCatalystCarrier.set(new ItemStack(agrichemItem, 1, 13))
            .registerOre("catalystEmpty");
        GregtechItemList.GreenMetalCatalyst.set(new ItemStack(agrichemItem, 1, 14))
            .registerOre("catalystAluminiumSilver");

        GregtechItemList.AlginicAcid.set(new ItemStack(agrichemItem, 1, 15))
            .registerOre("dustAlginicAcid");
        // metadata 16 no longer used
        GregtechItemList.AluminiumPellet.set(new ItemStack(agrichemItem, 1, 17))
            .registerOre("pelletAluminium");
        // metadata 18-21 no longer used
        GregtechItemList.Pellet_Mold.set(new ItemStack(agrichemItem, 1, 22));
        GregtechItemList.CleanAluminiumMix.set(new ItemStack(agrichemItem, 1, 23));

        GregtechItemList.Pinecone.set(new ItemStack(agrichemItem, 1, 24))
            .registerOre("pinecone");
        GregtechItemList.CrushedPineMaterials.set(new ItemStack(agrichemItem, 1, 25))
            .registerOre("crushedPineMaterial");

        // General Chemistry
        // TODO Remove after 2.8
        Item t3Selector = new GTPPIntegratedCircuitItem("T3RecipeSelector", "science/general/AdvancedCircuit");
        hideItem(new ItemStack(t3Selector));

        Item genericChemItem = new ItemGenericChemBase();

        GregtechItemList.RedMetalCatalyst.set(new ItemStack(genericChemItem))
            .registerOre("catalystIronCopper");
        GregtechItemList.YellowMetalCatalyst.set(new ItemStack(genericChemItem, 1, 1))
            .registerOre("catalystTungstenNickel");
        GregtechItemList.BlueMetalCatalyst.set(new ItemStack(genericChemItem, 1, 2))
            .registerOre("catalystCobaltTitanium");
        GregtechItemList.OrangeMetalCatalyst.set(new ItemStack(genericChemItem, 1, 3))
            .registerOre("catalystVanadiumPalladium");
        GregtechItemList.PurpleMetalCatalyst.set(new ItemStack(genericChemItem, 1, 4))
            .registerOre("catalystIridiumRuthenium");
        GregtechItemList.BrownMetalCatalyst.set(new ItemStack(genericChemItem, 1, 5))
            .registerOre("catalystNickelAluminium");
        GregtechItemList.PinkMetalCatalyst.set(new ItemStack(genericChemItem, 1, 6))
            .registerOre("catalystPlatinumRhodium");

        GregtechItemList.Milling_Ball_Alumina.set(new ItemStack(genericChemItem, 1, 7))
            .registerOre("millingballAlumina");
        GregtechItemList.Milling_Ball_Soapstone.set(new ItemStack(genericChemItem, 1, 8))
            .registerOre("millingballSoapstone");

        GregtechItemList.SodiumEthoxide.set(new ItemStack(genericChemItem, 1, 9))
            .registerOre("dustSodiumEthoxide");
        GregtechItemList.SodiumEthylXanthate.set(new ItemStack(genericChemItem, 1, 10))
            .registerOre("dustSodiumEthylXanthate");
        GregtechItemList.PotassiumEthylXanthate.set(new ItemStack(genericChemItem, 1, 11))
            .registerOre("dustPotassiumEthylXanthate");
        GregtechItemList.PotassiumHydroxide.set(new ItemStack(genericChemItem, 1, 12))
            .registerOre("dustPotassiumHydroxide");

        GregtechItemList.FormaldehydeCatalyst.set(new ItemStack(genericChemItem, 1, 13))
            .registerOre("catalystFormaldehyde");
        GregtechItemList.SolidAcidCatalyst.set(new ItemStack(genericChemItem, 1, 14))
            .registerOre("catalystSolidAcid");
        GregtechItemList.InfiniteMutationCatalyst.set(new ItemStack(genericChemItem, 1, 15))
            .registerOre("catalystInfiniteMutation");

        // QFT Catalysts
        GregtechItemList.PlatinumGroupCatalyst.set(new ItemStack(genericChemItem, 1, 16))
            .registerOre("catalystPlatinumGroup");
        GregtechItemList.PlasticPolymerCatalyst.set(new ItemStack(genericChemItem, 1, 17))
            .registerOre("catalystPlasticPolymer");
        GregtechItemList.RubberPolymerCatalyst.set(new ItemStack(genericChemItem, 1, 18))
            .registerOre("catalystRubberPolymer");
        GregtechItemList.AdhesionPromoterCatalyst.set(new ItemStack(genericChemItem, 1, 19))
            .registerOre("catalystAdhesionPromoter");
        GregtechItemList.TitaTungstenIndiumCatalyst.set(new ItemStack(genericChemItem, 1, 20))
            .registerOre("catalystTitaTungstenIndium");
        GregtechItemList.RadioactivityCatalyst.set(new ItemStack(genericChemItem, 1, 21))
            .registerOre("catalystRadioactivity");
        GregtechItemList.RareEarthGroupCatalyst.set(new ItemStack(genericChemItem, 1, 22))
            .registerOre("catalystRareEarthGroup");
        GregtechItemList.SimpleNaquadahCatalyst.set(new ItemStack(genericChemItem, 1, 23))
            .registerOre("catalystSimpleNaquadah");
        GregtechItemList.AdvancedNaquadahCatalyst.set(new ItemStack(genericChemItem, 1, 24))
            .registerOre("catalystAdvancedNaquadah");
        GregtechItemList.RawIntelligenceCatalyst.set(new ItemStack(genericChemItem, 1, 25))
            .registerOre("catalystRawIntelligence");
        GregtechItemList.UltimatePlasticCatalyst.set(new ItemStack(genericChemItem, 1, 26))
            .registerOre("catalystUltimatePlastic");
        GregtechItemList.BiologicalIntelligenceCatalyst.set(new ItemStack(genericChemItem, 1, 27))
            .registerOre("catalystBiologicalIntelligence");
        GregtechItemList.TemporalHarmonyCatalyst.set(new ItemStack(genericChemItem, 1, 28))
            .registerOre("catalystTemporalHarmony");
        GregtechItemList.ParticleAccelerationCatalyst.set(new ItemStack(genericChemItem, 1, 31))
            .registerOre("catalystParticleAcceleration");
        GregtechItemList.SynchrotronCapableCatalyst.set(new ItemStack(genericChemItem, 1, 32))
            .registerOre("catalystSynchrotronCapable");
        GregtechItemList.AlgagenicGrowthPromoterCatalyst.set(new ItemStack(genericChemItem, 1, 33))
            .registerOre("catalystAlgagenicGrowthPromoter");
        GregtechItemList.HellishForceCatalyst.set(new ItemStack(genericChemItem, 1, 34))
            .registerOre("catalystHellishForce");
        GregtechItemList.CrystalColorizationCatalyst.set(new ItemStack(genericChemItem, 1, 35))
            .registerOre("catalystCrystalColorization");

        // Milled Ore Processing
        GregtechItemList.MilledSphalerite
            .set(BaseItemMilledOre.generate(Materials.Sphalerite, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledChalcopyrite
            .set(BaseItemMilledOre.generate(Materials.Chalcopyrite, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledNickel.set(BaseItemMilledOre.generate(Materials.Nickel, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledPlatinum.set(BaseItemMilledOre.generate(Materials.Platinum, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledPentlandite
            .set(BaseItemMilledOre.generate(Materials.Pentlandite, (int) TierEU.RECIPE_LuV));

        GregtechItemList.MilledRedstone.set(BaseItemMilledOre.generate(Materials.Redstone, (int) TierEU.RECIPE_IV));
        GregtechItemList.MilledSpessartine
            .set(BaseItemMilledOre.generate(Materials.Spessartine, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledGrossular.set(BaseItemMilledOre.generate(Materials.Grossular, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledAlmandine.set(BaseItemMilledOre.generate(Materials.Almandine, (int) TierEU.RECIPE_LuV));
        GregtechItemList.MilledPyrope.set(BaseItemMilledOre.generate(Materials.Pyrope, (int) TierEU.RECIPE_EV));
        GregtechItemList.MilledMonazite.set(BaseItemMilledOre.generate(Materials.Monazite, (int) TierEU.RECIPE_ZPM));
        GregtechItemList.MilledNetherite.set(
            BaseItemMilledOre
                .generate(Materials.Netherrack, (int) TierEU.RECIPE_IV, new ItemStack(Blocks.netherrack, 256)));

        // Baubles
        if (Baubles.isModLoaded()) {
            CompatBaubles.run();
        }

        // Buffer Cores!
        GregtechItemList.Energy_Core_ULV.set(new ItemBufferCore("itemBufferCore", 1));
        GregtechItemList.Energy_Core_LV.set(new ItemBufferCore("itemBufferCore", 2));
        GregtechItemList.Energy_Core_MV.set(new ItemBufferCore("itemBufferCore", 3));
        GregtechItemList.Energy_Core_HV.set(new ItemBufferCore("itemBufferCore", 4));
        GregtechItemList.Energy_Core_EV.set(new ItemBufferCore("itemBufferCore", 5));
        GregtechItemList.Energy_Core_IV.set(new ItemBufferCore("itemBufferCore", 6));
        GregtechItemList.Energy_Core_LuV.set(new ItemBufferCore("itemBufferCore", 7));
        GregtechItemList.Energy_Core_ZPM.set(new ItemBufferCore("itemBufferCore", 8));
        GregtechItemList.Energy_Core_UV.set(new ItemBufferCore("itemBufferCore", 9));
        GregtechItemList.Energy_Core_UHV.set(new ItemBufferCore("itemBufferCore", 10));

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
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGadolinium", 1) == null) {
            ItemUtils.generateSpecialUseDusts(
                "Gadolinium",
                "Gadolinium",
                Materials.Gadolinium.mElement.name(),
                Utils.rgbtoHexValue(226, 172, 9));
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustYtterbium", 1) == null) {
            ItemUtils.generateSpecialUseDusts(
                "Ytterbium",
                "Ytterbium",
                Materials.Ytterbium.mElement.name(),
                Utils.rgbtoHexValue(
                    Materials.Yttrium.mRGBa[0] - 60,
                    Materials.Yttrium.mRGBa[1] - 60,
                    Materials.Yttrium.mRGBa[2] - 60));
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSamarium", 1) == null) {
            ItemUtils.generateSpecialUseDusts(
                "Samarium",
                "Samarium",
                Materials.Samarium.mElement.name(),
                Utils.rgbtoHexValue(161, 168, 114));
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustLanthanum", 1) == null) {
            ItemUtils.generateSpecialUseDusts(
                "Lanthanum",
                "Lanthanum",
                Materials.Lanthanum.mElement.name(),
                Utils.rgbtoHexValue(106, 127, 163));
        }

        // Just an unusual plate needed for some black magic.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateClay", 1) == null) {
            itemPlateClay = new BaseItemPlate(MaterialsOther.CLAY);
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleClay", 1) == null) {
            itemDoublePlateClay = new BaseItemPlateDouble(MaterialsOther.CLAY);
        }

        // Need this for Mutagenic Frames
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilUranium235", 1) == null) {
            itemFoilUranium235 = new BaseItemFoil(MaterialsElements.getInstance().URANIUM235);
        }

        // A small gear needed for wizardry.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gearGtSmallWroughtIron", 1) == null) {
            itemSmallWroughtIronGear = new BaseItemSmallGear(MaterialsOther.WROUGHT_IRON);
        }
        // Krypton Processing
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1) == null) {
            itemHotTitaniumIngot = new ItemStack(
                new BaseItemIngot(MaterialsElements.getInstance().TITANIUM, ComponentTypes.HOTINGOT));
        } else {
            itemHotTitaniumIngot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1);
        }

        // Need this for Laurenium
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustIndium", 1) == null) {
            itemDustIndium = new BaseItemDust(MaterialsElements.getInstance().INDIUM);
        }

        // Springs
        MaterialUtils.generateComponentAndAssignToAMaterial(
            ComponentTypes.SPRING,
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, MaterialsElements.STANDALONE.WHITE_METAL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, MaterialsAlloy.NITINOL_60);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, MaterialsAlloy.AQUATIC_STEEL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SPRING, MaterialsAlloy.EGLIN_STEEL);

        // Small Springs
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, MaterialsAlloy.MARAGING250);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, MaterialsAlloy.STABALLOY);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.SMALLSPRING, MaterialsAlloy.BLACK_TITANIUM);

        // Fine Wire
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsElements.STANDALONE.WHITE_METAL);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsElements.getInstance().ZIRCONIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsAlloy.LEAGRISIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsAlloy.BABBIT_ALLOY);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsAlloy.KOBOLDITE);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsAlloy.HG1223);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsAlloy.QUANTUM);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FINEWIRE, MaterialsElements.STANDALONE.HYPOGEN);
        MaterialUtils.generateComponentAndAssignToAMaterial(
            ComponentTypes.FINEWIRE,
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS);

        // Foil
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.BLACK_TITANIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.BOTMIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.TITANSTEEL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.NITINOL_60);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.QUANTUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.LAURENIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.HYPOGEN);
        MaterialUtils.generateComponentAndAssignToAMaterial(
            ComponentTypes.FOIL,
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.ASTRAL_TITANIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.RHUGNOR);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.WHITE_METAL);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.ADVANCED_NITINOL);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.PIKYONIUM);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.CINOBITE);
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.LAFIUM);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsAlloy.TRINIUM_REINFORCED_STEEL);
        MaterialUtils
            .generateComponentAndAssignToAMaterial(ComponentTypes.FOIL, MaterialsElements.STANDALONE.CHRONOMATIC_GLASS);

        // Gear
        MaterialUtils.generateComponentAndAssignToAMaterial(ComponentTypes.GEAR, MaterialsElements.STANDALONE.RHUGNOR);

        // Special Sillyness

        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateSodium", 1) == null) {
            new BaseItemPlate(MaterialsElements.getInstance().SODIUM);
        }

        // A Block of Meat.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("blockMeatRaw", 1) == null) {
            blockRawMeat = new BlockBaseModular(MaterialsOther.MEAT, BlockTypes.STANDARD);
            GTPPCore.burnables.add(Pair.of(900, new ItemStack(blockRawMeat)));
        }

        // A plate of Vanadium.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateVanadium", 1) == null) {
            itemPlateVanadium = new BaseItemPlate(MaterialsElements.getInstance().VANADIUM);
        }

        // A plate of Lithium.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateLithium", 1) == null) {
            itemPlateLithium = new BaseItemPlate(MaterialsElements.getInstance().LITHIUM);
        }

        // A plate of Europium.
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateEuropium", 1) == null) {
            itemPlateEuropium = new BaseItemPlate(MaterialsElements.getInstance().EUROPIUM);
        }
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleEuropium", 1) == null) {
            itemDoublePlateEuropium = new BaseItemPlateDouble(MaterialsElements.getInstance().EUROPIUM);
        }

        // Tumbaga Mix (For Simple Crafting)
        Item[] tumbagaMix = ItemUtils
            .generateSpecialUseDusts("MixTumbaga", "Tumbaga Mix", "Au2Cu", Utils.rgbtoHexValue(255, 150, 80));
        GregtechItemList.TumbagaMixDust.set(tumbagaMix[0]);
        GregtechItemList.SmallTumbagaMixDust.set(tumbagaMix[1]);
        GregtechItemList.TinyTumbagaMixDust.set(tumbagaMix[2]);
    }
}
