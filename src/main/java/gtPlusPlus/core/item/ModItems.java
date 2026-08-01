package gtPlusPlus.core.item;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.NotEnoughItems;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;

import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.StringUtils;
import gregtech.client.GTTooltipHandler;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemDamageable;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.bauble.FireProtectionBauble;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.chemistry.StandardBaseParticles;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.item.general.ItemBufferCore;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemGenericToken;
import gtPlusPlus.core.item.general.ItemHalfCompleteCasings;
import gtPlusPlus.core.item.general.ItemHealingDevice;
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
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;

public final class ModItems {

    public static Item itemCustomBook;
    public static IonParticles itemIonParticleBase;
    public static StandardBaseParticles itemStandarParticleBase;
    public static ItemDummyResearch itemDummyResearch;

    public static void init() {
        MetaGeneratedGregtechItems.INSTANCE.generateMetaItems();
        WearableLoader.run();
        ItemsFoods.load();

        Item[] tumbagaMix = ItemUtils
            .generateSpecialUseDusts("MixTumbaga", "Tumbaga Mix", "Au2Cu", Utils.rgbtoHexValue(255, 150, 80));
        GregtechItemList.TumbagaMixDust.set(tumbagaMix[0]);
        GregtechItemList.SmallTumbagaMixDust.set(tumbagaMix[1]);
        GregtechItemList.TinyTumbagaMixDust.set(tumbagaMix[2]);

        runCustomDustGenerator();

        itemDummyResearch = new ItemDummyResearch();
        new ItemCustomSpawnEgg();
        itemCustomBook = new ItemBaseBook();
        itemIonParticleBase = new IonParticles();
        itemStandarParticleBase = new StandardBaseParticles();

        GregtechItemList.MagicFeather.set(new ItemMagicFeather());

        GregtechItemList.AlkalusDisk.set(
            new BaseItemDamageable("itemAlkalusDisk", AddToCreativeTab.tabMisc, "Unknown Use", EnumRarity.rare, false));

        GregtechItemList.BlueprintBase.set(new ItemBlueprint("itemBlueprint"));

        // Half Complete Casings
        Item halfCompleteCasings = new ItemHalfCompleteCasings(
            "itemHalfCompleteCasings",
            AddToCreativeTab.tabMisc,
            32,
            0,
            "This isn't quite finished yet.",
            EnumRarity.common,
            EnumChatFormatting.GRAY,
            false,
            Utils.rgbtoHexValue(255, 255, 255)).setTextureName(GregTech.ID + ":" + "gt.metaitem.01/" + "761");
        GregtechItemList.HalfCompleteCasing_I.set(new ItemStack(halfCompleteCasings, 1, 0));
        GregtechItemList.HalfCompleteCasing_II.set(new ItemStack(halfCompleteCasings, 1, 1));
        GregtechItemList.HalfCompleteCasing_III.set(new ItemStack(halfCompleteCasings, 1, 2));
        GregtechItemList.HalfCompleteCasing_IV.set(new ItemStack(halfCompleteCasings, 1, 3));

        // LFTR Control Circuit
        GregtechItemList.LFTRControlCircuit.set(
            new CoreItem(
                "itemCircuitLFTR",
                AddToCreativeTab.tabMisc,
                1,
                0,
                new String[] { "Keeps Multiblocks Stable" },
                EnumRarity.epic,
                false,
                null));

        // Scrubber Turbines
        Item basicTurbine = new ItemBasicScrubberTurbine();
        GregtechItemList.BasicIronTurbine.set(new ItemStack(basicTurbine));
        GregtechItemList.BasicBronzeTurbine.set(new ItemStack(basicTurbine, 1, 1));
        GregtechItemList.BasicSteelTurbine.set(new ItemStack(basicTurbine, 1, 2));

        // Hand Pumps
        ItemGregtechPump toolHandPump = new ItemGregtechPump();
        toolHandPump.registerPumpType(0, "Simple Hand Pump", 0, 0);
        toolHandPump.registerPumpType(1, "Advanced Hand Pump", 32000, 1);
        toolHandPump.registerPumpType(2, "Super Hand Pump", 128000, 2);
        toolHandPump.registerPumpType(3, "Ultimate Hand Pump", 512000, 3);
        toolHandPump.registerPumpType(4, "Expandable Hand Pump", 0, 4);

        GregtechItemList.SimpleHandPump.set(new ItemStack(toolHandPump, 1, 1000));
        GregtechItemList.AdvancedHandPump.set(new ItemStack(toolHandPump, 1, 1001));
        GregtechItemList.SuperHandPump.set(new ItemStack(toolHandPump, 1, 1002));
        GregtechItemList.UltimateHandPump.set(new ItemStack(toolHandPump, 1, 1003));
        GregtechItemList.ExpandableHandPump.set(new ItemStack(toolHandPump, 1, 1004));

        // Volumetric Flasks
        GregtechItemList.VOLUMETRIC_FLASK_8k
            .set(VolumetricFlaskHelper.generateNewFlask("Volumetric_Flask_8k", "Large Volumetric Flask", 16000));
        GregtechItemList.VOLUMETRIC_FLASK_32k
            .set(VolumetricFlaskHelper.generateNewFlask("Volumetric_Flask_32k", "Gigantic Volumetric Flask", 256000));
        GregtechItemList.KLEIN_BOTTLE.set(
            VolumetricFlaskHelper.generateNewFlask("Volumetric_Flask_Infinite", "Klein Bottle", Integer.MAX_VALUE));

        Item boilerChassis = new ItemBoilerChassis();
        GregtechItemList.BoilerChassis_Tier0.set(new ItemStack(boilerChassis, 1, 0));
        GregtechItemList.BoilerChassis_Tier1.set(new ItemStack(boilerChassis, 1, 1));
        GregtechItemList.BoilerChassis_Tier2.set(new ItemStack(boilerChassis, 1, 2));

        Item dehydratorCoil = new ItemDehydratorCoil();
        GregtechItemList.DehydratorCoilEV.set(new ItemStack(dehydratorCoil, 1, 0));
        GregtechItemList.DehydratorCoilIV.set(new ItemStack(dehydratorCoil, 1, 1));
        GregtechItemList.DehydratorCoilLuV.set(new ItemStack(dehydratorCoil, 1, 2));
        GregtechItemList.DehydratorCoilZPM.set(new ItemStack(dehydratorCoil, 1, 3));

        Item dehydratorCoilWire = new ItemDehydratorCoilWire();
        GregtechItemList.DehydratorCoilWireEV.set(new ItemStack(dehydratorCoilWire, 1, 0));
        GregtechItemList.DehydratorCoilWireIV.set(new ItemStack(dehydratorCoilWire, 1, 1));
        GregtechItemList.DehydratorCoilWireLuV.set(new ItemStack(dehydratorCoilWire, 1, 2));
        GregtechItemList.DehydratorCoilWireZPM.set(new ItemStack(dehydratorCoilWire, 1, 3));

        Item airFilter = new ItemAirFilter();
        GregtechItemList.AirFilter_Tier1.set(new ItemStack(airFilter, 1, 0));
        GregtechItemList.AirFilter_Tier2.set(new ItemStack(airFilter, 1, 1));
        GregtechItemList.LavaFilter.set(new ItemLavaFilter());

        // Agrichem Items
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

        // Generic Chem Items
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
        GregtechItemList.AlgagenicGrowthPromoterCatalyst.set(new ItemStack(genericChemItem, 1, 33))
            .registerOre("catalystAlgagenicGrowthPromoter");
        GregtechItemList.HellishForceCatalyst.set(new ItemStack(genericChemItem, 1, 34))
            .registerOre("catalystHellishForce");
        GregtechItemList.CrystalColorizationCatalyst.set(new ItemStack(genericChemItem, 1, 35))
            .registerOre("catalystCrystalColorization");
        GregtechItemList.ChlorinationCatalyst.set(new ItemStack(genericChemItem, 1, 36))
            .registerOre("catalystChlorination");

        // Milled Ore Processing
        GregtechItemList.MilledSphalerite.set(BaseItemMilledOre.generate(Materials.Sphalerite, TierEU.RECIPE_LuV));
        GregtechItemList.MilledChalcopyrite.set(BaseItemMilledOre.generate(Materials.Chalcopyrite, TierEU.RECIPE_IV));
        GregtechItemList.MilledNickel.set(BaseItemMilledOre.generate(Materials.Nickel, TierEU.RECIPE_IV));
        GregtechItemList.MilledPlatinum.set(BaseItemMilledOre.generate(Materials.Platinum, TierEU.RECIPE_LuV));
        GregtechItemList.MilledPentlandite.set(BaseItemMilledOre.generate(Materials.Pentlandite, TierEU.RECIPE_LuV));
        GregtechItemList.MilledRedstone.set(BaseItemMilledOre.generate(Materials.Redstone, TierEU.RECIPE_IV));
        GregtechItemList.MilledSpessartine.set(BaseItemMilledOre.generate(Materials.Spessartine, TierEU.RECIPE_LuV));
        GregtechItemList.MilledGrossular.set(BaseItemMilledOre.generate(Materials.Grossular, TierEU.RECIPE_LuV));
        GregtechItemList.MilledAlmandine.set(BaseItemMilledOre.generate(Materials.Almandine, TierEU.RECIPE_LuV));
        GregtechItemList.MilledPyrope.set(BaseItemMilledOre.generate(Materials.Pyrope, TierEU.RECIPE_EV));
        GregtechItemList.MilledMonazite.set(BaseItemMilledOre.generate(Materials.Monazite, TierEU.RECIPE_ZPM));
        GregtechItemList.MilledNetherite.set(
            BaseItemMilledOre.generate(Materials.Netherrack, TierEU.RECIPE_IV, new ItemStack(Blocks.netherrack, 256)));

        // These items stay registered for save/item-ID stability (BaseOreComponent's oredict skip already
        // defers the milled<Material> oredict name to MaterialLib once cut over), so only their NEI visibility
        // needs to follow the cutover.
        if (NotEnoughItems.isModLoaded()) {
            hideMilledIfCutOver(Materials.Sphalerite, GregtechItemList.MilledSphalerite);
            hideMilledIfCutOver(Materials.Chalcopyrite, GregtechItemList.MilledChalcopyrite);
            hideMilledIfCutOver(Materials.Nickel, GregtechItemList.MilledNickel);
            hideMilledIfCutOver(Materials.Platinum, GregtechItemList.MilledPlatinum);
            hideMilledIfCutOver(Materials.Pentlandite, GregtechItemList.MilledPentlandite);
            hideMilledIfCutOver(Materials.Redstone, GregtechItemList.MilledRedstone);
            hideMilledIfCutOver(Materials.Spessartine, GregtechItemList.MilledSpessartine);
            hideMilledIfCutOver(Materials.Grossular, GregtechItemList.MilledGrossular);
            hideMilledIfCutOver(Materials.Almandine, GregtechItemList.MilledAlmandine);
            hideMilledIfCutOver(Materials.Pyrope, GregtechItemList.MilledPyrope);
            hideMilledIfCutOver(Materials.Monazite, GregtechItemList.MilledMonazite);
            hideMilledIfCutOver(Materials.Netherrack, GregtechItemList.MilledNetherite);
        }

        // Baubles
        GregtechItemList.PersonalCloakingDevice.set(new ItemCloakingDevice(0));
        GregtechItemList.PersonalHealingDevice.set(new ItemHealingDevice());
        GregtechItemList.SupremePizzaGloves.set(new FireProtectionBauble());

        GregtechItemList.ChargePack_LV.set(registerChargePack(1));
        GregtechItemList.ChargePack_MV.set(registerChargePack(2));
        GregtechItemList.ChargePack_HV.set(registerChargePack(3));
        GregtechItemList.ChargePack_EV.set(registerChargePack(4));
        GregtechItemList.ChargePack_IV.set(registerChargePack(5));
        GregtechItemList.ChargePack_LuV.set(registerChargePack(6));
        GregtechItemList.ChargePack_ZPM.set(registerChargePack(7));
        GregtechItemList.ChargePack_UV.set(registerChargePack(8));
        GregtechItemList.ChargePack_UHV.set(registerChargePack(9));

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

        // Custom Tokens
        ItemGenericToken genericToken = new ItemGenericToken();
        genericToken.register(0, "BitCoin", 16, "Can be used on the dark web");

        GregtechItemList.BitCoin.set(new ItemStack(genericToken, 1, 0));
    }

    private static void hideMilledIfCutOver(com.ruling_0.materiallib.api.Material material,
        GregtechItemList legacyItem) {
        if (MaterialParts.isCutOver(OrePrefixes.milled, material)) {
            codechicken.nei.api.API.hideItem(legacyItem.get(1));
        }
    }

    private static Item registerChargePack(int tier) {
        Item item = new BatteryPackBaseBauble(tier);
        registerTieredTooltip(new ItemStack(item, 1, GTRecipeBuilder.WILDCARD), GTTooltipHandler.Tier.values()[tier]);
        return item;
    }

    private static void runCustomDustGenerator() {
        // Nuclear Fuel Dusts
        Item[] lithiumCarbonate = ItemUtils.generateSpecialUseDusts(
            "LithiumCarbonate",
            "Lithium Carbonate",
            "Li2CO3",
            Utils.rgbtoHexValue(240, 240, 240));
        GregtechItemList.LithiumCarbonateDust.set(lithiumCarbonate[0]);
        GregtechItemList.SmallLithiumCarbonateDust.set(lithiumCarbonate[1]);
        GregtechItemList.TinyLithiumCarbonateDust.set(lithiumCarbonate[2]);

        Item[] lithiumPeroxide = ItemUtils.generateSpecialUseDusts(
            "LithiumPeroxide",
            "Lithium Peroxide",
            "Li2O2",
            Utils.rgbtoHexValue(250, 250, 250));
        GregtechItemList.LithiumPeroxideDust.set(lithiumPeroxide[0]);
        GregtechItemList.SmallLithiumPeroxideDust.set(lithiumPeroxide[1]);
        GregtechItemList.TinyLithiumPeroxideDust.set(lithiumPeroxide[2]);

        Item[] lithiumHydroxide = ItemUtils.generateSpecialUseDusts(
            "LithiumHydroxide",
            "Lithium Hydroxide",
            "LiOH",
            Utils.rgbtoHexValue(250, 250, 250));
        GregtechItemList.LithiumHydroxideDust.set(lithiumHydroxide[0]);
        GregtechItemList.SmallLithiumHydroxideDust.set(lithiumHydroxide[1]);
        GregtechItemList.TinyLithiumHydroxideDust.set(lithiumHydroxide[2]);

        Item[] calciumHydroxide = ItemUtils
            .generateSpecialUseDusts("CalciumHydroxide", "Hydrated Lime", "CaO2H2", Utils.rgbtoHexValue(255, 255, 255));
        GregtechItemList.CalciumHydroxideDust.set(calciumHydroxide[0]);
        GregtechItemList.SmallCalciumHydroxideDust.set(calciumHydroxide[1]);
        GregtechItemList.TinyCalciumHydroxideDust.set(calciumHydroxide[2]);

        Item[] calciumCarbonate = ItemUtils.generateSpecialUseDusts(
            "CalciumCarbonate",
            "Calcium Carbonate",
            "CaCO3",
            Utils.rgbtoHexValue(255, 255, 255));
        GregtechItemList.CalciumCarbonateDust.set(calciumCarbonate[0]);
        GregtechItemList.SmallCalciumCarbonateDust.set(calciumCarbonate[1]);
        GregtechItemList.TinyCalciumCarbonateDust.set(calciumCarbonate[2]);

        Item calciumSulfate = ItemUtils.generateSpecialUseDusts(
            "Gypsum",
            "Calcium Sulfate (Gypsum)",
            "CaSO4",
            Utils.rgbtoHexValue(255, 255, 255))[0];
        GTOreDictUnificator.registerOre("dustCalciumSulfate", new ItemStack(calciumSulfate));

        Item[] Li2CO3CaOH2 = ItemUtils.generateSpecialUseDusts(
            "Li2CO3CaOH2",
            "Li2CO3 + CaO2H2 Compound",
            "Li2CO3CaO2H2",
            Utils.rgbtoHexValue(255, 255, 255));
        GregtechItemList.Li2CO3CaOH2Dust.set(Li2CO3CaOH2[0]);
        GregtechItemList.SmallLi2CO3CaOH2Dust.set(Li2CO3CaOH2[1]);
        GregtechItemList.TinyLi2CO3CaOH2Dust.set(Li2CO3CaOH2[2]);

        ItemUtils.generateSpecialUseDusts(
            "Li2BeF4",
            "Lithium Tetrafluoroberyllate Fuel Compound",
            "Li2BeF4",
            Utils.rgbtoHexValue(255, 255, 255));

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

        // Zirconium
        // Cinter Pellet.
        GregtechItemList.ZirconiumPellet.set(
            new CoreItem("itemZirconiumPellet", AddToCreativeTab.tabMisc, 64, 0, GTValues.emptyStringArray)
                .setTextureName(GTPlusPlus.ID + ":itemShard"));
        GTOreDictUnificator.registerOre("pelletZirconium", GregtechItemList.ZirconiumPellet.get(1));

        // Zirconium Chloride
        Item[] ZrCl4 = ItemUtils.generateSpecialUseDusts("ZrCl4", "ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180));
        GregtechItemList.ZrCl4Dust.set(ZrCl4[0]);
        GregtechItemList.SmallZrCl4Dust.set(ZrCl4[1]);
        GregtechItemList.TinyZrCl4Dust.set(ZrCl4[2]);

        Item[] cookedZrCl4 = ItemUtils
            .generateSpecialUseDusts("CookedZrCl4", "Cooked ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180));
        GregtechItemList.CookedZrCl4Dust.set(cookedZrCl4[0]);
        GregtechItemList.SmallCookedZrCl4Dust.set(cookedZrCl4[1]);
        GregtechItemList.TinyCookedZrCl4Dust.set(cookedZrCl4[2]);

        GregtechItemList.Neptunium238Dust.set(
            new DustDecayable(
                "dustNeptunium238",
                0xAFF04B,
                50000,
                new String[] { StringUtils.superscript("238Np"),
                    "Result: Plutonium 238 (" + StringUtils.superscript("238Pu") + ")" },
                MaterialLibAPI.getStack(Materials.Plutonium238, Shapes.dust, 1),
                5,
                GTRecipeConstants.DecayType.BetaMinus));

        GregtechItemList.Neptunium239Dust.set(
            new DustDecayable(
                "dustNeptunium239",
                0x71F045,
                25000,
                new String[] { StringUtils.superscript("238Np"),
                    "Result: Plutonium 239 (" + StringUtils.superscript("239Pu") + ")" },
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 1),
                5,
                GTRecipeConstants.DecayType.BetaMinus));

        GregtechItemList.DecayedRadium226Dust.set(
            ItemUtils.generateSpecialUseDusts(
                "DecayedRadium226",
                "Decayed Radium-226",
                "Contains Radon (" + StringUtils.superscript("222Rn") + ")",
                MathUtils.getRgbAsHex(MaterialUtils.rgba(Materials.Radium)))[0]);

        GregtechItemList.Radium226Dust.set(
            new DustDecayable(
                "dustRadium226",
                MathUtils.getRgbAsHex(MaterialUtils.rgba(Materials.Radium)),
                90000,
                new String[] { StringUtils.superscript("226Ra"),
                    "Result: Radon (" + StringUtils.superscript("222Rn") + ")" },
                GregtechItemList.DecayedRadium226Dust.get(1),
                5,
                GTRecipeConstants.DecayType.Alpha));

        GregtechItemList.Protactinium233Dust.set(
            new DustDecayable(
                "dustProtactinium233",
                MathUtils.getRgbAsHex(MaterialUtils.rgba(Materials.Protactinium)),
                32000,
                new String[] { StringUtils.superscript("233Pa"),
                    "Result: Uranium 233 (" + StringUtils.superscript("233U") + ")" },
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 1),
                6,
                GTRecipeConstants.DecayType.BetaMinus));
    }
}
