package gregtech.common.items;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ADVANCED_REDSTONE_RECEIVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ADVANCED_REDSTONE_TRANSMITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_ACTIVITYDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_CONTROLLER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_FLUID_DETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_ITEM_DETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_WIRELESS_MAINTENANCE_DETECTOR;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;
import static gregtech.common.items.IDMetaItem02.Bottle_Alcopops;
import static gregtech.common.items.IDMetaItem02.Bottle_Apple_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Beer;
import static gregtech.common.items.IDMetaItem02.Bottle_Cave_Johnsons_Grenade_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Chilly_Sauce;
import static gregtech.common.items.IDMetaItem02.Bottle_Cider;
import static gregtech.common.items.IDMetaItem02.Bottle_Dark_Beer;
import static gregtech.common.items.IDMetaItem02.Bottle_Diablo_Sauce;
import static gregtech.common.items.IDMetaItem02.Bottle_Diabolo_Sauce;
import static gregtech.common.items.IDMetaItem02.Bottle_Dragon_Blood;
import static gregtech.common.items.IDMetaItem02.Bottle_Glen_McKenner;
import static gregtech.common.items.IDMetaItem02.Bottle_Golden_Apple_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Golden_Cider;
import static gregtech.common.items.IDMetaItem02.Bottle_Grape_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Holy_Water;
import static gregtech.common.items.IDMetaItem02.Bottle_Hops_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Hot_Sauce;
import static gregtech.common.items.IDMetaItem02.Bottle_Iduns_Apple_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Lemon_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Lemonade;
import static gregtech.common.items.IDMetaItem02.Bottle_Leninade;
import static gregtech.common.items.IDMetaItem02.Bottle_Limoncello;
import static gregtech.common.items.IDMetaItem02.Bottle_Milk;
import static gregtech.common.items.IDMetaItem02.Bottle_Mineral_Water;
import static gregtech.common.items.IDMetaItem02.Bottle_Notches_Brew;
import static gregtech.common.items.IDMetaItem02.Bottle_Pirate_Brew;
import static gregtech.common.items.IDMetaItem02.Bottle_Potato_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Purple_Drink;
import static gregtech.common.items.IDMetaItem02.Bottle_Reed_Water;
import static gregtech.common.items.IDMetaItem02.Bottle_Rum;
import static gregtech.common.items.IDMetaItem02.Bottle_Salty_Water;
import static gregtech.common.items.IDMetaItem02.Bottle_Scotch;
import static gregtech.common.items.IDMetaItem02.Bottle_Snitches_Glitch_Sauce;
import static gregtech.common.items.IDMetaItem02.Bottle_Vinegar;
import static gregtech.common.items.IDMetaItem02.Bottle_Vodka;
import static gregtech.common.items.IDMetaItem02.Bottle_Wheaty_Hops_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Wheaty_Juice;
import static gregtech.common.items.IDMetaItem02.Bottle_Wine;
import static gregtech.common.items.IDMetaItem02.Cover_AdvancedRedstoneReceiver;
import static gregtech.common.items.IDMetaItem02.Cover_AdvancedRedstoneReceiverInternal;
import static gregtech.common.items.IDMetaItem02.Cover_AdvancedRedstoneTransmitter;
import static gregtech.common.items.IDMetaItem02.Cover_AdvancedRedstoneTransmitterInternal;
import static gregtech.common.items.IDMetaItem02.Cover_WirelessActivityDetector;
import static gregtech.common.items.IDMetaItem02.Cover_WirelessFluidDetector;
import static gregtech.common.items.IDMetaItem02.Cover_WirelessItemDetector;
import static gregtech.common.items.IDMetaItem02.Cover_WirelessNeedsMaintainance;
import static gregtech.common.items.IDMetaItem02.Display_ITS_FREE;
import static gregtech.common.items.IDMetaItem02.Dye_Color_00;
import static gregtech.common.items.IDMetaItem02.Dye_Color_01;
import static gregtech.common.items.IDMetaItem02.Dye_Color_02;
import static gregtech.common.items.IDMetaItem02.Dye_Color_03;
import static gregtech.common.items.IDMetaItem02.Dye_Color_04;
import static gregtech.common.items.IDMetaItem02.Dye_Color_05;
import static gregtech.common.items.IDMetaItem02.Dye_Color_06;
import static gregtech.common.items.IDMetaItem02.Dye_Color_07;
import static gregtech.common.items.IDMetaItem02.Dye_Color_08;
import static gregtech.common.items.IDMetaItem02.Dye_Color_09;
import static gregtech.common.items.IDMetaItem02.Dye_Color_10;
import static gregtech.common.items.IDMetaItem02.Dye_Color_11;
import static gregtech.common.items.IDMetaItem02.Dye_Color_12;
import static gregtech.common.items.IDMetaItem02.Dye_Color_13;
import static gregtech.common.items.IDMetaItem02.Dye_Color_14;
import static gregtech.common.items.IDMetaItem02.Dye_Color_15;
import static gregtech.common.items.IDMetaItem02.Dye_Indigo;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Baguette;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Bun;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Cake;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Pizza_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Pizza_Meat;
import static gregtech.common.items.IDMetaItem02.Food_Baked_Pizza_Veggie;
import static gregtech.common.items.IDMetaItem02.Food_Burger_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Burger_Chum;
import static gregtech.common.items.IDMetaItem02.Food_Burger_Meat;
import static gregtech.common.items.IDMetaItem02.Food_Burger_Veggie;
import static gregtech.common.items.IDMetaItem02.Food_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_ChiliChips;
import static gregtech.common.items.IDMetaItem02.Food_Chum;
import static gregtech.common.items.IDMetaItem02.Food_Chum_On_Stick;
import static gregtech.common.items.IDMetaItem02.Food_Dough;
import static gregtech.common.items.IDMetaItem02.Food_Dough_Chocolate;
import static gregtech.common.items.IDMetaItem02.Food_Dough_Sugar;
import static gregtech.common.items.IDMetaItem02.Food_Flat_Dough;
import static gregtech.common.items.IDMetaItem02.Food_Fries;
import static gregtech.common.items.IDMetaItem02.Food_Large_Sandwich_Bacon;
import static gregtech.common.items.IDMetaItem02.Food_Large_Sandwich_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Large_Sandwich_Steak;
import static gregtech.common.items.IDMetaItem02.Food_Large_Sandwich_Veggie;
import static gregtech.common.items.IDMetaItem02.Food_Packaged_ChiliChips;
import static gregtech.common.items.IDMetaItem02.Food_Packaged_Fries;
import static gregtech.common.items.IDMetaItem02.Food_Packaged_PotatoChips;
import static gregtech.common.items.IDMetaItem02.Food_PotatoChips;
import static gregtech.common.items.IDMetaItem02.Food_Potato_On_Stick;
import static gregtech.common.items.IDMetaItem02.Food_Potato_On_Stick_Roasted;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Baguette;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Bread;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Bun;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Cake;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Cookie;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Fries;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Pizza_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Pizza_Meat;
import static gregtech.common.items.IDMetaItem02.Food_Raw_Pizza_Veggie;
import static gregtech.common.items.IDMetaItem02.Food_Raw_PotatoChips;
import static gregtech.common.items.IDMetaItem02.Food_Sandwich_Bacon;
import static gregtech.common.items.IDMetaItem02.Food_Sandwich_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Sandwich_Steak;
import static gregtech.common.items.IDMetaItem02.Food_Sandwich_Veggie;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Baguette;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Baguettes;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Bread;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Breads;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Bun;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Buns;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Cheese;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Cucumber;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Lemon;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Onion;
import static gregtech.common.items.IDMetaItem02.Food_Sliced_Tomato;
import static gregtech.common.items.IDMetaItem02.GelledToluene;
import static gregtech.common.items.IDMetaItem02.MSFMixture;
import static gregtech.common.items.IDMetaItem02.Magnetron;
import static gregtech.common.items.IDMetaItem02.Plank_Acacia;
import static gregtech.common.items.IDMetaItem02.Plank_Acacia_Green;
import static gregtech.common.items.IDMetaItem02.Plank_Balsa;
import static gregtech.common.items.IDMetaItem02.Plank_Baobab;
import static gregtech.common.items.IDMetaItem02.Plank_Birch;
import static gregtech.common.items.IDMetaItem02.Plank_Cherry;
import static gregtech.common.items.IDMetaItem02.Plank_Cherry_EFR;
import static gregtech.common.items.IDMetaItem02.Plank_Chestnut;
import static gregtech.common.items.IDMetaItem02.Plank_Citrus;
import static gregtech.common.items.IDMetaItem02.Plank_DarkOak;
import static gregtech.common.items.IDMetaItem02.Plank_Ebony;
import static gregtech.common.items.IDMetaItem02.Plank_Greenheart;
import static gregtech.common.items.IDMetaItem02.Plank_Jungle;
import static gregtech.common.items.IDMetaItem02.Plank_Kapok;
import static gregtech.common.items.IDMetaItem02.Plank_Larch;
import static gregtech.common.items.IDMetaItem02.Plank_Lime;
import static gregtech.common.items.IDMetaItem02.Plank_Mahagony;
import static gregtech.common.items.IDMetaItem02.Plank_Mahoe;
import static gregtech.common.items.IDMetaItem02.Plank_Maple;
import static gregtech.common.items.IDMetaItem02.Plank_Oak;
import static gregtech.common.items.IDMetaItem02.Plank_Palm;
import static gregtech.common.items.IDMetaItem02.Plank_Papaya;
import static gregtech.common.items.IDMetaItem02.Plank_Pine;
import static gregtech.common.items.IDMetaItem02.Plank_Plum;
import static gregtech.common.items.IDMetaItem02.Plank_Poplar;
import static gregtech.common.items.IDMetaItem02.Plank_Sequoia;
import static gregtech.common.items.IDMetaItem02.Plank_Spruce;
import static gregtech.common.items.IDMetaItem02.Plank_Teak;
import static gregtech.common.items.IDMetaItem02.Plank_Walnut;
import static gregtech.common.items.IDMetaItem02.Plank_Wenge;
import static gregtech.common.items.IDMetaItem02.Plank_Willow;
import static gregtech.common.items.IDMetaItem02.SFMixture;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Chocolate_Milk;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Coffee;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Dark_Chocolate_Milk;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Ice_Tea;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Latte;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Sweet_Coffee;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Sweet_Jesus_Latte;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Sweet_Latte;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Sweet_Tea;
import static gregtech.common.items.IDMetaItem02.ThermosCan_Tea;
import static gregtech.common.items.IDMetaItem02.Vajra_Core;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TierEU;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTFoodStat;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverExternal;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverInternal;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterExternal;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterInternal;
import gregtech.common.covers.redstone.CoverWirelessDoesWorkDetector;
import gregtech.common.covers.redstone.CoverWirelessFluidDetector;
import gregtech.common.covers.redstone.CoverWirelessItemDetector;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector;

public class MetaGeneratedItem02 extends MetaGeneratedItemX32 {

    public static MetaGeneratedItem02 INSTANCE;
    private static final String aTextCover = "Usable as Cover";

    public MetaGeneratedItem02() {
        super(
            "metaitem.02",
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.toolHeadHammer,
            OrePrefixes.toolHeadFile,
            OrePrefixes.toolHeadSaw,
            OrePrefixes.toolHeadDrill,
            OrePrefixes.toolHeadChainsaw,
            OrePrefixes.toolHeadWrench,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.toolHeadBuzzSaw,
            OrePrefixes.turbineBlade,
            null,
            OrePrefixes.itemCasing,
            OrePrefixes.wireFine,
            OrePrefixes.gearGtSmall,
            OrePrefixes.rotor,
            OrePrefixes.stickLong,
            OrePrefixes.springSmall,
            OrePrefixes.spring,
            OrePrefixes.___placeholder___,
            OrePrefixes.___placeholder___,
            OrePrefixes.gemChipped,
            OrePrefixes.gemFlawed,
            OrePrefixes.gemFlawless,
            OrePrefixes.gemExquisite,
            OrePrefixes.gearGt);
        INSTANCE = this;

        ItemList.ThermosCan_Coffee.set(
            addItemWithLocalizationKeys(
                ThermosCan_Coffee.ID,
                "gt.item.thermos_can.coffee.name",
                "gt.item.thermos_can.coffee.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    400,
                    1,
                    70,
                    Potion.digSpeed.id,
                    400,
                    1,
                    70),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.ThermosCan_Sweet_Coffee.set(
            addItemWithLocalizationKeys(
                ThermosCan_Sweet_Coffee.ID,
                "gt.item.thermos_can.sweet_coffee.name",
                "gt.item.thermos_can.sweet_coffee.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    400,
                    2,
                    90,
                    Potion.digSpeed.id,
                    400,
                    2,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 3L)));
        ItemList.ThermosCan_Latte.set(
            addItemWithLocalizationKeys(
                ThermosCan_Latte.ID,
                "gt.item.thermos_can.latte.name",
                "gt.item.thermos_can.latte.tooltip",
                new GTFoodStat(
                    3,
                    0.4F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    400,
                    0,
                    50,
                    Potion.digSpeed.id,
                    400,
                    0,
                    50),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 1L)));
        ItemList.ThermosCan_Sweet_Latte.set(
            addItemWithLocalizationKeys(
                ThermosCan_Sweet_Latte.ID,
                "gt.item.thermos_can.sweet_latte.name",
                "gt.item.thermos_can.sweet_latte.tooltip",
                new GTFoodStat(
                    3,
                    0.4F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    400,
                    1,
                    70,
                    Potion.digSpeed.id,
                    400,
                    1,
                    70),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2L)));
        ItemList.ThermosCan_Sweet_Jesus_Latte.set(
            addItemWithLocalizationKeys(
                ThermosCan_Sweet_Jesus_Latte.ID,
                "gt.item.thermos_can.sweet_jesus_latte.name",
                "gt.item.thermos_can.sweet_jesus_latte.tooltip",
                new GTFoodStat(
                    3,
                    0.4F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    400,
                    2,
                    90,
                    Potion.digSpeed.id,
                    400,
                    2,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MOTUS, 3L)));
        ItemList.ThermosCan_Dark_Chocolate_Milk.set(
            addItemWithLocalizationKeys(
                ThermosCan_Dark_Chocolate_Milk.ID,
                "gt.item.thermos_can.dark_chocolate_milk.name",
                "gt.item.thermos_can.dark_chocolate_milk.tooltip",
                new GTFoodStat(
                    3,
                    0.4F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.regeneration.id,
                    50,
                    1,
                    60),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.ThermosCan_Chocolate_Milk.set(
            addItemWithLocalizationKeys(
                ThermosCan_Chocolate_Milk.ID,
                "gt.item.thermos_can.chocolate_milk.name",
                "gt.item.thermos_can.chocolate_milk.tooltip",
                new GTFoodStat(
                    3,
                    0.4F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.regeneration.id,
                    50,
                    1,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 2L)));
        ItemList.ThermosCan_Tea.set(
            addItemWithLocalizationKeys(
                ThermosCan_Tea.ID,
                "gt.item.thermos_can.tea.name",
                "gt.item.thermos_can.tea.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSlowdown.id,
                    300,
                    0,
                    50),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.ThermosCan_Sweet_Tea.set(
            addItemWithLocalizationKeys(
                ThermosCan_Sweet_Tea.ID,
                "gt.item.thermos_can.sweet_tea.name",
                "gt.item.thermos_can.sweet_tea.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));
        ItemList.ThermosCan_Ice_Tea.set(
            addItemWithLocalizationKeys(
                ThermosCan_Ice_Tea.ID,
                "gt.item.thermos_can.ice_tea.name",
                "gt.item.thermos_can.ice_tea.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.ThermosCan_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSlowdown.id,
                    300,
                    0,
                    50),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L)));

        ItemList.GelledToluene.set(
            addItemWithLocalizationKeys(
                GelledToluene.ID,
                "gt.item.gelled_toluene.name",
                "gt.item.gelled_toluene.tooltip"));

        ItemList.Bottle_Purple_Drink.set(
            addItemWithLocalizationKeys(
                Bottle_Purple_Drink.ID,
                "gt.item.bottle.purple_drink.name",
                "gt.item.bottle.purple_drink.tooltip",
                new GTFoodStat(
                    8,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.moveSlowdown.id,
                    400,
                    1,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VINCULUM, 1L)));
        ItemList.Bottle_Grape_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Grape_Juice.ID,
                "gt.item.bottle.grape_juice.name",
                "gt.item.bottle.grape_juice.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.hunger.id,
                    400,
                    1,
                    60),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Wine.set(
            addItemWithLocalizationKeys(
                Bottle_Wine.ID,
                "gt.item.bottle.wine.name",
                "gt.item.bottle.wine.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    1,
                    60,
                    Potion.heal.id,
                    0,
                    0,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Vinegar.set(
            addItemWithLocalizationKeys(
                Bottle_Vinegar.ID,
                "gt.item.bottle.vinegar.name",
                "gt.item.bottle.vinegar.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    1,
                    90,
                    Potion.heal.id,
                    0,
                    1,
                    90,
                    Potion.poison.id,
                    200,
                    2,
                    10,
                    Potion.harm.id,
                    0,
                    2,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Potato_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Potato_Juice.ID,
                "gt.item.bottle.potato_juice.name",
                "gt.item.bottle.potato_juice.tooltip",
                new GTFoodStat(
                    3,
                    0.3F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L)));
        ItemList.Bottle_Vodka.set(
            addItemWithLocalizationKeys(
                Bottle_Vodka.ID,
                "gt.item.bottle.vodka.name",
                "gt.item.bottle.vodka.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    500,
                    0,
                    60,
                    Potion.damageBoost.id,
                    500,
                    1,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 1L)));
        ItemList.Bottle_Leninade.set(
            addItemWithLocalizationKeys(
                Bottle_Leninade.ID,
                "gt.item.bottle.leninade.name",
                "gt.item.bottle.leninade.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    500,
                    1,
                    90,
                    Potion.damageBoost.id,
                    500,
                    2,
                    90,
                    Potion.poison.id,
                    200,
                    2,
                    10,
                    Potion.harm.id,
                    0,
                    2,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L)));
        ItemList.Bottle_Mineral_Water.set(
            addItemWithLocalizationKeys(
                Bottle_Mineral_Water.ID,
                "gt.item.bottle.mineral_water.name",
                "gt.item.bottle.mineral_water.tooltip",
                new GTFoodStat(
                    1,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.regeneration.id,
                    100,
                    1,
                    10),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Salty_Water.set(
            addItemWithLocalizationKeys(
                Bottle_Salty_Water.ID,
                "gt.item.bottle.salty_water.name",
                "gt.item.bottle.salty_water.tooltip",
                SubTag.INVISIBLE,
                new GTFoodStat(
                    1,
                    0.0F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.hunger.id,
                    400,
                    2,
                    95),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TEMPESTAS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Bottle_Reed_Water.set(
            addItemWithLocalizationKeys(
                Bottle_Reed_Water.ID,
                "gt.item.bottle.reed_water.name",
                "gt.item.bottle.reed_water.tooltip",
                new GTFoodStat(
                    1,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Bottle_Rum.set(
            addItemWithLocalizationKeys(
                Bottle_Rum.ID,
                "gt.item.bottle.rum.name",
                "gt.item.bottle.rum.tooltip",
                new GTFoodStat(
                    4,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    300,
                    0,
                    60,
                    Potion.damageBoost.id,
                    300,
                    1,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Bottle_Pirate_Brew.set(
            addItemWithLocalizationKeys(
                Bottle_Pirate_Brew.ID,
                "gt.item.bottle.pirate_brew.name",
                "gt.item.bottle.pirate_brew.tooltip",
                new GTFoodStat(
                    4,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    300,
                    1,
                    90,
                    Potion.damageBoost.id,
                    300,
                    2,
                    90,
                    Potion.poison.id,
                    200,
                    2,
                    10,
                    Potion.harm.id,
                    0,
                    2,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 2L)));
        ItemList.Bottle_Hops_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Hops_Juice.ID,
                "gt.item.bottle.hops_juice.name",
                "gt.item.bottle.hops_juice.tooltip",
                new GTFoodStat(
                    1,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L)));
        ItemList.Bottle_Dark_Beer.set(
            addItemWithLocalizationKeys(
                Bottle_Dark_Beer.ID,
                "gt.item.bottle.dark_beer.name",
                "gt.item.bottle.dark_beer.tooltip",
                new GTFoodStat(
                    4,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    300,
                    1,
                    60,
                    Potion.damageBoost.id,
                    300,
                    1,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));
        ItemList.Bottle_Dragon_Blood.set(
            addItemWithLocalizationKeys(
                Bottle_Dragon_Blood.ID,
                "gt.item.bottle.dragon_blood.name",
                "gt.item.bottle.dragon_blood.tooltip",
                new GTFoodStat(
                    4,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    300,
                    2,
                    90,
                    Potion.damageBoost.id,
                    300,
                    2,
                    90,
                    Potion.poison.id,
                    200,
                    2,
                    10,
                    Potion.harm.id,
                    0,
                    2,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2L)));
        ItemList.Bottle_Wheaty_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Wheaty_Juice.ID,
                "gt.item.bottle.wheaty_juice.name",
                "gt.item.bottle.wheaty_juice.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L)));
        ItemList.Bottle_Scotch.set(
            addItemWithLocalizationKeys(
                Bottle_Scotch.ID,
                "gt.item.bottle.scotch.name",
                "gt.item.bottle.scotch.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    0,
                    60,
                    Potion.resistance.id,
                    400,
                    1,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L)));
        ItemList.Bottle_Glen_McKenner.set(
            addItemWithLocalizationKeys(
                Bottle_Glen_McKenner.ID,
                "gt.item.bottle.glen_mckenner.name",
                "gt.item.bottle.glen_mckenner.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    1,
                    90,
                    Potion.resistance.id,
                    400,
                    2,
                    90,
                    Potion.poison.id,
                    200,
                    2,
                    10,
                    Potion.harm.id,
                    0,
                    2,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 2L)));
        ItemList.Bottle_Wheaty_Hops_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Wheaty_Hops_Juice.ID,
                "gt.item.bottle.wheaty_hops_juice.name",
                "gt.item.bottle.wheaty_hops_juice.tooltip",
                new GTFoodStat(
                    1,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 2L)));
        ItemList.Bottle_Beer.set(
            addItemWithLocalizationKeys(
                Bottle_Beer.ID,
                "gt.item.bottle.beer.name",
                "gt.item.bottle.beer.tooltip",
                new GTFoodStat(
                    6,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    0,
                    60,
                    Potion.digSpeed.id,
                    400,
                    2,
                    60,
                    Potion.poison.id,
                    100,
                    0,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 1L)));
        ItemList.Bottle_Chilly_Sauce.set(
            addItemWithLocalizationKeys(
                Bottle_Chilly_Sauce.ID,
                "gt.item.bottle.chilly_sauce.name",
                "gt.item.bottle.chilly_sauce.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    1000,
                    0,
                    10,
                    Potion.fireResistance.id,
                    1000,
                    0,
                    60),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Bottle_Hot_Sauce.set(
            addItemWithLocalizationKeys(
                Bottle_Hot_Sauce.ID,
                "gt.item.bottle.hot_sauce.name",
                "gt.item.bottle.hot_sauce.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    2000,
                    0,
                    30,
                    Potion.fireResistance.id,
                    2000,
                    0,
                    70),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2L)));
        ItemList.Bottle_Diabolo_Sauce.set(
            addItemWithLocalizationKeys(
                Bottle_Diabolo_Sauce.ID,
                "gt.item.bottle.diabolo_sauce.name",
                "gt.item.bottle.diabolo_sauce.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    3000,
                    1,
                    50,
                    Potion.fireResistance.id,
                    3000,
                    0,
                    80),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)));
        ItemList.Bottle_Diablo_Sauce.set(
            addItemWithLocalizationKeys(
                Bottle_Diablo_Sauce.ID,
                "gt.item.bottle.diablo_sauce.name",
                "gt.item.bottle.diablo_sauce.tooltip",
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    4000,
                    1,
                    70,
                    Potion.fireResistance.id,
                    4000,
                    0,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 4L)));
        ItemList.Bottle_Snitches_Glitch_Sauce.set(
            addItemWithLocalizationKeys(
                Bottle_Snitches_Glitch_Sauce.ID,
                "gt.item.bottle.snitches_glitch_sauce.name",
                "gt.item.bottle.snitches_glitch_sauce.tooltip",
                SubTag.INVISIBLE,
                new GTFoodStat(
                    2,
                    0.1F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    9999,
                    2,
                    999,
                    Potion.fireResistance.id,
                    9999,
                    9,
                    999),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 5L)));
        ItemList.Bottle_Apple_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Apple_Juice.ID,
                "gt.item.bottle.apple_juice.name",
                "gt.item.bottle.apple_juice.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.hunger.id,
                    400,
                    0,
                    20),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Bottle_Cider.set(
            addItemWithLocalizationKeys(
                Bottle_Cider.ID,
                "gt.item.bottle.cider.name",
                "gt.item.bottle.cider.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    0,
                    60,
                    Potion.resistance.id,
                    400,
                    1,
                    60,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L)));
        ItemList.Bottle_Golden_Apple_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Golden_Apple_Juice.ID,
                "gt.item.bottle.golden_apple_juice.name",
                "gt.item.bottle.golden_apple_juice.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.hunger.id,
                    400,
                    0,
                    20,
                    Potion.field_76444_x.id,
                    2400,
                    0,
                    100,
                    Potion.regeneration.id,
                    100,
                    1,
                    100),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Golden_Cider.set(
            addItemWithLocalizationKeys(
                Bottle_Golden_Cider.ID,
                "gt.item.bottle.golden_cider.name",
                "gt.item.bottle.golden_cider.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.confusion.id,
                    400,
                    0,
                    60,
                    Potion.field_76444_x.id,
                    2400,
                    1,
                    95,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L)));
        ItemList.Bottle_Iduns_Apple_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Iduns_Apple_Juice.ID,
                "gt.item.bottle.iduns_apple_juice.name",
                "gt.item.bottle.iduns_apple_juice.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.regeneration.id,
                    600,
                    4,
                    100,
                    Potion.field_76444_x.id,
                    2400,
                    0,
                    100,
                    Potion.resistance.id,
                    6000,
                    0,
                    100,
                    Potion.fireResistance.id,
                    6000,
                    0,
                    100),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 9L)));
        ItemList.Bottle_Notches_Brew.set(
            addItemWithLocalizationKeys(
                Bottle_Notches_Brew.ID,
                "gt.item.bottle.notches_brew.name",
                "gt.item.bottle.notches_brew.tooltip",
                new GTFoodStat(
                    4,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.regeneration.id,
                    700,
                    4,
                    95,
                    Potion.field_76444_x.id,
                    3000,
                    1,
                    95,
                    Potion.resistance.id,
                    7000,
                    1,
                    95,
                    Potion.fireResistance.id,
                    7000,
                    0,
                    95,
                    Potion.harm.id,
                    0,
                    2,
                    20),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 9L)));
        ItemList.Bottle_Lemon_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Lemon_Juice.ID,
                "gt.item.bottle.lemon_juice.name",
                "gt.item.bottle.lemon_juice.tooltip",
                new GTFoodStat(
                    2,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.digSpeed.id,
                    1200,
                    0,
                    60),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 1L)));
        ItemList.Bottle_Limoncello.set(
            addItemWithLocalizationKeys(
                Bottle_Limoncello.ID,
                "gt.item.bottle.limoncello.name",
                "gt.item.bottle.limoncello.tooltip",
                new GTFoodStat(
                    2,
                    0.4F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.digSpeed.id,
                    1200,
                    0,
                    90,
                    Potion.poison.id,
                    200,
                    1,
                    5),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 1L)));
        ItemList.Bottle_Lemonade.set(
            addItemWithLocalizationKeys(
                Bottle_Lemonade.ID,
                "gt.item.bottle.lemonade.name",
                "gt.item.bottle.lemonade.tooltip",
                new GTFoodStat(
                    4,
                    0.3F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.digSpeed.id,
                    900,
                    1,
                    90),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 1L)));
        ItemList.Bottle_Alcopops.set(
            addItemWithLocalizationKeys(
                Bottle_Alcopops.ID,
                "gt.item.bottle.alcopops.name",
                "gt.item.bottle.alcopops.tooltip",
                new GTFoodStat(
                    2,
                    0.2F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.digSpeed.id,
                    900,
                    1,
                    90,
                    Potion.poison.id,
                    300,
                    2,
                    20),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VINCULUM, 1L)));
        ItemList.Bottle_Cave_Johnsons_Grenade_Juice.set(
            addItemWithLocalizationKeys(
                Bottle_Cave_Johnsons_Grenade_Juice.ID,
                "gt.item.bottle.cave_johnsons_grenade_juice.name",
                "gt.item.bottle.cave_johnsons_grenade_juice.tooltip",
                new GTFoodStat(
                    0,
                    0.0F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false).setExplosive(),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MORTUUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 1L)));
        ItemList.Bottle_Milk.set(
            addItemWithLocalizationKeys(
                Bottle_Milk.ID,
                "gt.item.bottle.milk.name",
                "gt.item.bottle.milk.tooltip",
                OrePrefixes.bottle.get(Materials.Milk),
                new GTFoodStat(
                    0,
                    0.0F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false).setMilk(),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));
        ItemList.Bottle_Holy_Water.set(
            addItemWithLocalizationKeys(
                Bottle_Holy_Water.ID,
                "gt.item.bottle.holy_water.name",
                "gt.item.bottle.holy_water.tooltip",
                OrePrefixes.bottle.get(Materials.HolyWater),
                new GTFoodStat(
                    0,
                    0.0F,
                    EnumAction.drink,
                    ItemList.Bottle_Empty.get(1L),
                    GregTechAPI.sDrinksAlwaysDrinkable,
                    false,
                    false,
                    Potion.poison.id,
                    100,
                    1,
                    100).setMilk(),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AURAM, 1L)));

        ItemList.Food_Potato_On_Stick.set(
            addItemWithLocalizationKeys(
                Food_Potato_On_Stick.ID,
                "gt.item.food.potato_on_stick.name",
                "gt.item.food.potato_on_stick.tooltip",
                new GTFoodStat(1, 0.3F, EnumAction.eat, new ItemStack(Items.stick, 1), false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Food_Potato_On_Stick_Roasted.set(
            addItemWithLocalizationKeys(
                Food_Potato_On_Stick_Roasted.ID,
                "gt.item.food.potato_on_stick_roasted.name",
                "gt.item.food.potato_on_stick_roasted.tooltip",
                new GTFoodStat(6, 0.6F, EnumAction.eat, new ItemStack(Items.stick, 1), false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Fries.set(
            addItemWithLocalizationKeys(
                Food_Raw_Fries.ID,
                "gt.item.food.raw_fries.name",
                "gt.item.food.raw_fries.tooltip",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Fries.set(
            addItemWithLocalizationKeys(
                Food_Fries.ID,
                "gt.item.food.fries.name",
                "gt.item.food.fries.tooltip",
                new GTFoodStat(7, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Packaged_Fries.set(
            addItemWithLocalizationKeys(
                Food_Packaged_Fries.ID,
                "gt.item.food.packaged_fries.name",
                "gt.item.food.packaged_fries.tooltip",
                new GTFoodStat(
                    7,
                    0.5F,
                    EnumAction.eat,
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 1L),
                    false,
                    true,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_PotatoChips.set(
            addItemWithLocalizationKeys(
                Food_Raw_PotatoChips.ID,
                "gt.item.food.raw_potato_chips.name",
                "gt.item.food.raw_potato_chips.tooltip",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_PotatoChips.set(
            addItemWithLocalizationKeys(
                Food_PotatoChips.ID,
                "gt.item.food.potato_chips.name",
                "gt.item.food.potato_chips.tooltip",
                new GTFoodStat(7, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_ChiliChips.set(
            addItemWithLocalizationKeys(
                Food_ChiliChips.ID,
                "gt.item.food.chili_chips.name",
                "gt.item.food.chili_chips.tooltip",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Packaged_PotatoChips.set(
            addItemWithLocalizationKeys(
                Food_Packaged_PotatoChips.ID,
                "gt.item.food.packaged_potato_chips.name",
                "gt.item.food.packaged_potato_chips.tooltip",
                new GTFoodStat(
                    7,
                    0.5F,
                    EnumAction.eat,
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                    false,
                    true,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Packaged_ChiliChips.set(
            addItemWithLocalizationKeys(
                Food_Packaged_ChiliChips.ID,
                "gt.item.food.packaged_chili_chips.name",
                "gt.item.food.packaged_chili_chips.tooltip",
                new GTFoodStat(
                    7,
                    0.6F,
                    EnumAction.eat,
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                    false,
                    true,
                    false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Chum.set(
            addItemWithLocalizationKeys(
                Food_Chum.ID,
                "gt.item.food.chum.name",
                "gt.item.food.chum.tooltip",
                new GTFoodStat(
                    5,
                    0.2F,
                    EnumAction.eat,
                    null,
                    true,
                    false,
                    true,
                    Potion.hunger.id,
                    1000,
                    4,
                    100,
                    Potion.confusion.id,
                    300,
                    1,
                    80),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Chum_On_Stick.set(
            addItemWithLocalizationKeys(
                Food_Chum_On_Stick.ID,
                "gt.item.food.chum_on_stick.name",
                "gt.item.food.chum_on_stick.tooltip",
                new GTFoodStat(
                    5,
                    0.2F,
                    EnumAction.eat,
                    new ItemStack(Items.stick, 1),
                    true,
                    false,
                    true,
                    Potion.hunger.id,
                    1000,
                    4,
                    100,
                    Potion.confusion.id,
                    300,
                    1,
                    80),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Dough_Sugar.set(
            addItemWithLocalizationKeys(
                Food_Dough_Sugar.ID,
                "gt.item.food.dough_sugar.name",
                "gt.item.food.dough_sugar.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Dough_Chocolate.set(
            addItemWithLocalizationKeys(
                Food_Dough_Chocolate.ID,
                "gt.item.food.dough_chocolate.name",
                "gt.item.food.dough_chocolate.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Cookie.set(
            addItemWithLocalizationKeys(
                Food_Raw_Cookie.ID,
                "gt.item.food.raw_cookie.name",
                "gt.item.food.raw_cookie.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));

        ItemList.Food_Sliced_Buns.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Buns.ID,
                "gt.item.food.sliced_buns.name",
                "gt.item.food.sliced_buns.tooltip",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Veggie.set(
            addItemWithLocalizationKeys(
                Food_Burger_Veggie.ID,
                "gt.item.food.burger_veggie.name",
                "gt.item.food.burger_veggie.tooltip",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Burger_Cheese.ID,
                "gt.item.food.burger_cheese.name",
                "gt.item.food.burger_cheese.tooltip",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new ItemData(Materials.Cheese, 907200L)));
        ItemList.Food_Burger_Meat.set(
            addItemWithLocalizationKeys(
                Food_Burger_Meat.ID,
                "gt.item.food.burger_meat.name",
                "gt.item.food.burger_meat.tooltip",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Chum.set(
            addItemWithLocalizationKeys(
                Food_Burger_Chum.ID,
                "gt.item.food.burger_chum.name",
                "gt.item.food.burger_chum.tooltip",
                new GTFoodStat(
                    5,
                    0.2F,
                    EnumAction.eat,
                    null,
                    true,
                    false,
                    true,
                    Potion.hunger.id,
                    1000,
                    4,
                    100,
                    Potion.confusion.id,
                    300,
                    1,
                    80),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));

        ItemList.Food_Sliced_Breads.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Breads.ID,
                "gt.item.food.sliced_breads.name",
                "gt.item.food.sliced_breads.tooltip",
                new GTFoodStat(5, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Veggie.set(
            addItemWithLocalizationKeys(
                Food_Sandwich_Veggie.ID,
                "gt.item.food.sandwich_veggie.name",
                "gt.item.food.sandwich_veggie.tooltip",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Sandwich_Cheese.ID,
                "gt.item.food.sandwich_cheese.name",
                "gt.item.food.sandwich_cheese.tooltip",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Bacon.set(
            addItemWithLocalizationKeys(
                Food_Sandwich_Bacon.ID,
                "gt.item.food.sandwich_bacon.name",
                "gt.item.food.sandwich_bacon.tooltip",
                new GTFoodStat(10, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Steak.set(
            addItemWithLocalizationKeys(
                Food_Sandwich_Steak.ID,
                "gt.item.food.sandwich_steak.name",
                "gt.item.food.sandwich_steak.tooltip",
                new GTFoodStat(10, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Baguettes.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Baguettes.ID,
                "gt.item.food.sliced_baguettes.name",
                "gt.item.food.sliced_baguettes.tooltip",
                new GTFoodStat(8, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Veggie.set(
            addItemWithLocalizationKeys(
                Food_Large_Sandwich_Veggie.ID,
                "gt.item.food.large_sandwich_veggie.name",
                "gt.item.food.large_sandwich_veggie.tooltip",
                new GTFoodStat(15, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 3L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Large_Sandwich_Cheese.ID,
                "gt.item.food.large_sandwich_cheese.name",
                "gt.item.food.large_sandwich_cheese.tooltip",
                new GTFoodStat(15, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Bacon.set(
            addItemWithLocalizationKeys(
                Food_Large_Sandwich_Bacon.ID,
                "gt.item.food.large_sandwich_bacon.name",
                "gt.item.food.large_sandwich_bacon.tooltip",
                new GTFoodStat(20, 1.0F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Steak.set(
            addItemWithLocalizationKeys(
                Food_Large_Sandwich_Steak.ID,
                "gt.item.food.large_sandwich_steak.name",
                "gt.item.food.large_sandwich_steak.tooltip",
                new GTFoodStat(20, 1.0F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Veggie.set(
            addItemWithLocalizationKeys(
                Food_Raw_Pizza_Veggie.ID,
                "gt.item.food.raw_pizza_veggie.name",
                "gt.item.food.raw_pizza_veggie.tooltip",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Raw_Pizza_Cheese.ID,
                "gt.item.food.raw_pizza_cheese.name",
                "gt.item.food.raw_pizza_cheese.tooltip",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Meat.set(
            addItemWithLocalizationKeys(
                Food_Raw_Pizza_Meat.ID,
                "gt.item.food.raw_pizza_meat.name",
                "gt.item.food.raw_pizza_meat.tooltip",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));

        ItemList.Food_Baked_Pizza_Veggie.set(
            addItemWithLocalizationKeys(
                Food_Baked_Pizza_Veggie.ID,
                "gt.item.food.baked_pizza_veggie.name",
                "gt.item.food.baked_pizza_veggie.tooltip",
                new GTFoodStat(3, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Pizza_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Baked_Pizza_Cheese.ID,
                "gt.item.food.baked_pizza_cheese.name",
                "gt.item.food.baked_pizza_cheese.tooltip",
                new GTFoodStat(4, 0.4F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Pizza_Meat.set(
            addItemWithLocalizationKeys(
                Food_Baked_Pizza_Meat.ID,
                "gt.item.food.baked_pizza_meat.name",
                "gt.item.food.baked_pizza_meat.tooltip",
                new GTFoodStat(5, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));

        ItemList.Dye_Indigo.set(
            addItemWithLocalizationKeys(
                Dye_Indigo.ID,
                "gt.item.dye.indigo.name",
                "gt.item.dye.indigo.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L),
                Dyes.dyeBlue));

        int[] Dye_Colors = new int[] { Dye_Color_00.ID, Dye_Color_01.ID, Dye_Color_02.ID, Dye_Color_03.ID,
            Dye_Color_04.ID, Dye_Color_05.ID, Dye_Color_06.ID, Dye_Color_07.ID, Dye_Color_08.ID, Dye_Color_09.ID,
            Dye_Color_10.ID, Dye_Color_11.ID, Dye_Color_12.ID, Dye_Color_13.ID, Dye_Color_14.ID, Dye_Color_15.ID };
        for (int i = 0; i < 16; i = i + 1) {
            String dyeKey = "gt.item.dye." + Dyes.get(i).mName.toLowerCase()
                .replace(" ", "_") + ".name";
            ItemList.DYE_ONLY_ITEMS[i].set(
                addItemWithLocalizationKeys(
                    Dye_Colors[i],
                    dyeKey,
                    "",
                    Dyes.get(i)
                        .name(),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L)));
        }
        final String plankOredict = "coverPlank";
        ItemList.Plank_Oak.set(
            addItemWithLocalizationKeys(
                Plank_Oak.ID,
                "gt.item.plank_cover.oak.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Spruce.set(
            addItemWithLocalizationKeys(
                Plank_Spruce.ID,
                "gt.item.plank_cover.spruce.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Birch.set(
            addItemWithLocalizationKeys(
                Plank_Birch.ID,
                "gt.item.plank_cover.birch.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Jungle.set(
            addItemWithLocalizationKeys(
                Plank_Jungle.ID,
                "gt.item.plank_cover.jungle.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Acacia.set(
            addItemWithLocalizationKeys(
                Plank_Acacia.ID,
                "gt.item.plank_cover.acacia.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_DarkOak.set(
            addItemWithLocalizationKeys(
                Plank_DarkOak.ID,
                "gt.item.plank_cover.dark_oak.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Larch.set(
            addItemWithLocalizationKeys(
                Plank_Larch.ID,
                "gt.item.plank_cover.larch.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Teak.set(
            addItemWithLocalizationKeys(
                Plank_Teak.ID,
                "gt.item.plank_cover.teak.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Acacia_Green.set(
            addItemWithLocalizationKeys(
                Plank_Acacia_Green.ID,
                "gt.item.plank_cover.green_acacia.name",
                plankOredict,
                "gt.item.plank_cover.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Lime.set(
            addItemWithLocalizationKeys(
                Plank_Lime.ID,
                "gt.item.plank_cover.lime.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Chestnut.set(
            addItemWithLocalizationKeys(
                Plank_Chestnut.ID,
                "gt.item.plank_cover.chestnut.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Wenge.set(
            addItemWithLocalizationKeys(
                Plank_Wenge.ID,
                "gt.item.plank_cover.wenge.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Baobab.set(
            addItemWithLocalizationKeys(
                Plank_Baobab.ID,
                "gt.item.plank_cover.baobab.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Sequoia.set(
            addItemWithLocalizationKeys(
                Plank_Sequoia.ID,
                "gt.item.plank_cover.sequoia.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Kapok.set(
            addItemWithLocalizationKeys(
                Plank_Kapok.ID,
                "gt.item.plank_cover.kapok.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Ebony.set(
            addItemWithLocalizationKeys(
                Plank_Ebony.ID,
                "gt.item.plank_cover.ebony.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Mahagony.set(
            addItemWithLocalizationKeys(
                Plank_Mahagony.ID,
                "gt.item.plank_cover.mahagony.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Balsa.set(
            addItemWithLocalizationKeys(
                Plank_Balsa.ID,
                "gt.item.plank_cover.balsa.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Willow.set(
            addItemWithLocalizationKeys(
                Plank_Willow.ID,
                "gt.item.plank_cover.willow.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Walnut.set(
            addItemWithLocalizationKeys(
                Plank_Walnut.ID,
                "gt.item.plank_cover.walnut.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Greenheart.set(
            addItemWithLocalizationKeys(
                Plank_Greenheart.ID,
                "gt.item.plank_cover.greenheart.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Cherry.set(
            addItemWithLocalizationKeys(
                Plank_Cherry.ID,
                "gt.item.plank_cover.cherry.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Mahoe.set(
            addItemWithLocalizationKeys(
                Plank_Mahoe.ID,
                "gt.item.plank_cover.mahoe.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Poplar.set(
            addItemWithLocalizationKeys(
                Plank_Poplar.ID,
                "gt.item.plank_cover.poplar.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Palm.set(
            addItemWithLocalizationKeys(
                Plank_Palm.ID,
                "gt.item.plank_cover.palm.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Papaya.set(
            addItemWithLocalizationKeys(
                Plank_Papaya.ID,
                "gt.item.plank_cover.papaya.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Pine.set(
            addItemWithLocalizationKeys(
                Plank_Pine.ID,
                "gt.item.plank_cover.pine.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Plum.set(
            addItemWithLocalizationKeys(
                Plank_Plum.ID,
                "gt.item.plank_cover.plum.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Maple.set(
            addItemWithLocalizationKeys(
                Plank_Maple.ID,
                "gt.item.plank_cover.maple.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Citrus.set(
            addItemWithLocalizationKeys(
                Plank_Citrus.ID,
                "gt.item.plank_cover.citrus.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Cherry_EFR.set(
            addItemWithLocalizationKeys(
                Plank_Cherry_EFR.ID,
                "gt.item.plank_cover.cherry_efr.name",
                "gt.item.plank_cover.tooltip",
                plankOredict,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.SFMixture
            .set(addItemWithLocalizationKeys(SFMixture.ID, "gt.item.sf_mixture.name", "gt.item.sf_mixture.tooltip"));
        ItemList.MSFMixture
            .set(addItemWithLocalizationKeys(MSFMixture.ID, "gt.item.msf_mixture.name", "gt.item.msf_mixture.tooltip"));

        ItemList.Food_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Cheese.ID,
                "gt.item.food.cheese.name",
                "gt.item.food.cheese.tooltip",
                "foodCheese",
                new GTFoodStat(3, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L)));
        ItemList.Food_Dough.set(
            addItemWithLocalizationKeys(
                Food_Dough.ID,
                "gt.item.food.dough.name",
                "gt.item.food.dough.tooltip",
                "foodDough",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Flat_Dough.set(
            addItemWithLocalizationKeys(
                Food_Flat_Dough.ID,
                "gt.item.food.flat_dough.name",
                "gt.item.food.flat_dough.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Bread.set(
            addItemWithLocalizationKeys(
                Food_Raw_Bread.ID,
                "gt.item.food.raw_bread.name",
                "gt.item.food.raw_bread.tooltip",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Bun.set(
            addItemWithLocalizationKeys(
                Food_Raw_Bun.ID,
                "gt.item.food.raw_bun.name",
                "gt.item.food.raw_bun.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Baguette.set(
            addItemWithLocalizationKeys(
                Food_Raw_Baguette.ID,
                "gt.item.food.raw_baguette.name",
                "gt.item.food.raw_baguette.tooltip",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Baked_Bun.set(
            addItemWithLocalizationKeys(
                Food_Baked_Bun.ID,
                "gt.item.food.baked_bun.name",
                "gt.item.food.baked_bun.tooltip",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Baguette.set(
            addItemWithLocalizationKeys(
                Food_Baked_Baguette.ID,
                "gt.item.food.baked_baguette.name",
                "gt.item.food.baked_baguette.tooltip",
                new GTFoodStat(8, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Bread.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Bread.ID,
                "gt.item.food.sliced_bread.name",
                "gt.item.food.sliced_bread.tooltip",
                new GTFoodStat(2, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Bun.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Bun.ID,
                "gt.item.food.sliced_bun.name",
                "gt.item.food.sliced_bun.tooltip",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Baguette.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Baguette.ID,
                "gt.item.food.sliced_baguette.name",
                "gt.item.food.sliced_baguette.tooltip",
                new GTFoodStat(4, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Cake.set(
            addItemWithLocalizationKeys(
                Food_Raw_Cake.ID,
                "gt.item.food.raw_cake.name",
                "gt.item.food.raw_cake.tooltip",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Baked_Cake.set(
            addItemWithLocalizationKeys(
                Food_Baked_Cake.ID,
                "gt.item.food.baked_cake.name",
                "gt.item.food.baked_cake.tooltip",
                new GTFoodStat(3, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Sliced_Lemon.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Lemon.ID,
                "gt.item.food.sliced_lemon.name",
                "gt.item.food.sliced_lemon.tooltip",
                new GTFoodStat(1, 0.075F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Tomato.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Tomato.ID,
                "gt.item.food.sliced_tomato.name",
                "gt.item.food.sliced_tomato.tooltip",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Onion.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Onion.ID,
                "gt.item.food.sliced_onion.name",
                "gt.item.food.sliced_onion.tooltip",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Cucumber.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Cucumber.ID,
                "gt.item.food.sliced_cucumber.name",
                "gt.item.food.sliced_cucumber.tooltip",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));

        ItemList.Food_Sliced_Cheese.set(
            addItemWithLocalizationKeys(
                Food_Sliced_Cheese.ID,
                "gt.item.food.sliced_cheese.name",
                "gt.item.food.sliced_cheese.tooltip",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));

        ItemList.Cover_AdvancedRedstoneTransmitter.set(
            addItemWithLocalizationKeys(
                Cover_AdvancedRedstoneTransmitter.ID,
                "gt.item.cover.advanced_redstone_transmitter.name",
                "gt.item.cover.advanced_redstone_transmitter.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneTransmitterInternal.set(
            addItemWithLocalizationKeys(
                Cover_AdvancedRedstoneTransmitterInternal.ID,
                "gt.item.cover.advanced_redstone_transmitter_internal.name",
                "gt.item.cover.advanced_redstone_transmitter_internal.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneReceiver.set(
            addItemWithLocalizationKeys(
                Cover_AdvancedRedstoneReceiver.ID,
                "gt.item.cover.advanced_redstone_receiver.name",
                "gt.item.cover.advanced_redstone_receiver.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneReceiverInternal.set(
            addItemWithLocalizationKeys(
                Cover_AdvancedRedstoneReceiverInternal.ID,
                "gt.item.cover.advanced_redstone_receiver_internal.name",
                "gt.item.cover.advanced_redstone_receiver_internal.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));

        ItemList.Cover_WirelessFluidDetector.set(
            addItemWithLocalizationKeys(
                Cover_WirelessFluidDetector.ID,
                "gt.item.cover.wireless_fluid_detector.name",
                "gt.item.cover.wireless_fluid_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_WirelessItemDetector.set(
            addItemWithLocalizationKeys(
                Cover_WirelessItemDetector.ID,
                "gt.item.cover.wireless_item_detector.name",
                "gt.item.cover.wireless_item_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));

        ItemList.Cover_WirelessNeedsMaintainance.set(
            addItemWithLocalizationKeys(
                Cover_WirelessNeedsMaintainance.ID,
                "gt.item.cover.wireless_needs_maintenance.name",
                "gt.item.cover.wireless_needs_maintenance.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 3L)));

        ItemList.Cover_WirelessActivityDetector.set(
            addItemWithLocalizationKeys(
                Cover_WirelessActivityDetector.ID,
                "gt.item.cover.wireless_activity_detector.name",
                "gt.item.cover.wireless_activity_detector.tooltip",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));

        ItemList.Display_ITS_FREE.set(
            addItemWithLocalizationKeys(
                Display_ITS_FREE.ID,
                "gt.item.display.its_free.name",
                "gt.item.display.its_free.tooltip",
                SubTag.INVISIBLE,
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Item_Redstone_Sniffer.set(
            new ItemRedstoneSniffer(
                "Item_Redstone_Sniffer",
                "gt.item.redstone_sniffer.name",
                "gt.item.redstone_sniffer.tooltip"));
        ItemList.DroneRemoteInterface.set(
            new ItemDroneRemoteInterface(
                "Item_Drone_Remote_Interface",
                "gt.item.drone_remote_interface.name",
                "gt.item.drone_remote_interface.tooltip"));
        ItemList.Vajra_Core.set(addItemWithLocalizationKeys(Vajra_Core.ID, "gt.item.vajra_core.name", ""));
        ItemList.Magnetron.set(addItemWithLocalizationKeys(Magnetron.ID, "gt.item.magnetron.name", ""));
        ItemList.ChaosLocator.set(
            new ItemChaosLocator("Item_Chaos_Locator", "gt.item.chaos_locator.name", "gt.item.chaos_locator.tooltip"));

        final ItemGTToolbox itemToolbox = new ItemGTToolbox(
            "Item_Toolbox",
            "Field Engineer's Toolbox",
            "A container to hold your tools!");
        ItemList.ToolBox.set(itemToolbox);

        if (GregTechAPI.sThaumcraftCompat != null) {
            GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(
                new ItemStack(itemToolbox),
                ImmutableList.of(
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 4),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 4),
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 2)),
                false);
        }

        setAllFluidContainerStats();
        setBurnValues();
        registerCovers();
        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
        initAssemblerRecipes();
        initExtractorRecipes();
        initMaceratorRecipes();
        initCompressorRecipes();
    }

    @Override
    public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        super.onLeftClickEntity(aStack, aPlayer, aEntity);
        return false;
    }

    @Override
    public boolean hasProjectile(SubTag aProjectileType, ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        return ((aDamage >= 25000) && (aDamage < 27000)) || (super.hasProjectile(aProjectileType, aStack));
    }

    @Override
    public boolean isItemStackUsable(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        Materials aMaterial = GregTechAPI.sGeneratedMaterials[(aDamage % 1000)];
        if ((aDamage >= 25000) && (aDamage < 27000) && (aMaterial != null) && (aMaterial.mToolEnchantment != null)) {
            Enchantment tEnchant = aMaterial.mToolEnchantment == Enchantment.fortune ? Enchantment.looting
                : aMaterial.mToolEnchantment;
            if (tEnchant.type == EnumEnchantmentType.weapon) {
                if (!ItemStackNBT.getBoolean(aStack, "GT.HasBeenUpdated")) {
                    ItemStackNBT.setBoolean(aStack, "GT.HasBeenUpdated", true);
                    GTUtility.ItemNBT.addEnchantment(aStack, tEnchant, aMaterial.mToolEnchantmentLevel);
                }
            }
        }
        return super.isItemStackUsable(aStack);
    }

    private void setAllFluidContainerStats() {
        setFluidContainerStats(32000 + Food_Raw_Fries.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Fries.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Raw_PotatoChips.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_PotatoChips.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_ChiliChips.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Sandwich_Veggie.ID, 0L, 32L);
        setFluidContainerStats(32000 + Food_Sandwich_Cheese.ID, 0L, 32L);
        setFluidContainerStats(32000 + Food_Sandwich_Bacon.ID, 0L, 32L);
        setFluidContainerStats(32000 + Food_Sandwich_Steak.ID, 0L, 32L);
        setFluidContainerStats(32000 + Food_Large_Sandwich_Veggie.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Large_Sandwich_Cheese.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Large_Sandwich_Bacon.ID, 0L, 16L);
        setFluidContainerStats(32000 + Food_Large_Sandwich_Steak.ID, 0L, 16L);
    }

    private void setBurnValues() {
        setBurnValue(32000 + Plank_Oak.ID, 75);
        setBurnValue(32000 + Plank_Spruce.ID, 75);
        setBurnValue(32000 + Plank_Birch.ID, 75);
        setBurnValue(32000 + Plank_Jungle.ID, 75);
        setBurnValue(32000 + Plank_Acacia.ID, 75);
        setBurnValue(32000 + Plank_DarkOak.ID, 75);
        setBurnValue(32000 + Plank_Larch.ID, 75);
        setBurnValue(32000 + Plank_Teak.ID, 75);
        setBurnValue(32000 + Plank_Acacia_Green.ID, 75);
        setBurnValue(32000 + Plank_Lime.ID, 75);
        setBurnValue(32000 + Plank_Chestnut.ID, 75);
        setBurnValue(32000 + Plank_Wenge.ID, 75);
        setBurnValue(32000 + Plank_Baobab.ID, 75);
        setBurnValue(32000 + Plank_Sequoia.ID, 75);
        setBurnValue(32000 + Plank_Kapok.ID, 75);
        setBurnValue(32000 + Plank_Ebony.ID, 75);
        setBurnValue(32000 + Plank_Mahagony.ID, 75);
        setBurnValue(32000 + Plank_Balsa.ID, 75);
        setBurnValue(32000 + Plank_Willow.ID, 75);
        setBurnValue(32000 + Plank_Walnut.ID, 75);
        setBurnValue(32000 + Plank_Greenheart.ID, 75);
        setBurnValue(32000 + Plank_Cherry.ID, 75);
        setBurnValue(32000 + Plank_Mahoe.ID, 75);
        setBurnValue(32000 + Plank_Poplar.ID, 75);
        setBurnValue(32000 + Plank_Palm.ID, 75);
        setBurnValue(32000 + Plank_Papaya.ID, 75);
        setBurnValue(32000 + Plank_Pine.ID, 75);
        setBurnValue(32000 + Plank_Plum.ID, 75);
        setBurnValue(32000 + Plank_Maple.ID, 75);
        setBurnValue(32000 + Plank_Citrus.ID, 75);
        setBurnValue(32000 + Plank_Cherry_EFR.ID, 75);
    }

    public void initCraftingShapedRecipes() {
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_Oak.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 0) });
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_Spruce.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_Birch.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 2) });
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_Jungle.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 3) });
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_Acacia.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 4) });
        GTModHandler.addCraftingRecipe(
            ItemList.Plank_DarkOak.get(2L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "s ", " P", 'P', new ItemStack(Blocks.wooden_slab, 1, 5) });
    }

    public void initAssemblerRecipes() {
        RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_RedstoneTransmitter.get(1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_AdvancedRedstoneTransmitter.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_RedstoneReceiver.get(1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_AdvancedRedstoneReceiver.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Cover_Controller.get(1L), ItemList.Sensor_EV.get(1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_WirelessController.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Cover_FluidDetector.get(1L), ItemList.Emitter_EV.get(1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_WirelessFluidDetector.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Cover_ItemDetector.get(1L), ItemList.Emitter_EV.get(1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_WirelessItemDetector.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Cover_NeedsMaintainance.get(1L), ItemList.Emitter_EV.get(1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_WirelessNeedsMaintainance.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Cover_ActivityDetector.get(1L), ItemList.Emitter_EV.get(1L))
            .circuit(1)
            .itemOutputs(ItemList.Cover_WirelessActivityDetector.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    public void initCraftingShapelessRecipes() {

    }

    public void initExtractorRecipes() {
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 0))
            .itemOutputs(new ItemStack(Items.dye, 2, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 1))
            .itemOutputs(new ItemStack(Items.dye, 2, 12))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 2))
            .itemOutputs(new ItemStack(Items.dye, 2, 13))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 3))
            .itemOutputs(new ItemStack(Items.dye, 2, 7))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 4))
            .itemOutputs(new ItemStack(Items.dye, 2, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 5))
            .itemOutputs(new ItemStack(Items.dye, 2, 14))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 6))
            .itemOutputs(new ItemStack(Items.dye, 2, 7))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 7))
            .itemOutputs(new ItemStack(Items.dye, 2, 9))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 1, 8))
            .itemOutputs(new ItemStack(Items.dye, 2, 7))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.yellow_flower, 1, 0))
            .itemOutputs(new ItemStack(Items.dye, 2, 11))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.double_plant, 1, 0))
            .itemOutputs(new ItemStack(Items.dye, 3, 11))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.double_plant, 1, 1))
            .itemOutputs(new ItemStack(Items.dye, 3, 13))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.double_plant, 1, 4))
            .itemOutputs(new ItemStack(Items.dye, 3, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.double_plant, 1, 5))
            .itemOutputs(new ItemStack(Items.dye, 3, 9))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

    }

    public void initCompressorRecipes() {
        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_flower, 8, 32767))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.yellow_flower, 8, 32767))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);
    }

    public void initMaceratorRecipes() {
        RA.stdBuilder()
            .itemInputs(ItemList.Food_Sliced_Cheese.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Cheese, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Dye_Cocoa.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.pumpkin, 1, 0))
            .itemOutputs(new ItemStack(Items.pumpkin_seeds, 4, 0))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon, 1, 0))
            .itemOutputs(new ItemStack(Items.melon_seeds, 1, 0))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("crop", 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Items.stick, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.wool, 1, WILDCARD))
            .itemOutputs(new ItemStack(Items.string, 2), new ItemStack(Items.string, 1))
            .outputChances(10000, 5000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.TranscendentMetal, 8L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(maceratorRecipes);
    }

    public void registerCovers() {
        CoverRegistry.registerDecorativeCover(ItemList.Plank_Oak.get(1L), TextureFactory.of(Blocks.planks, 0));
        CoverRegistry.registerDecorativeCover(ItemList.Plank_Spruce.get(1L), TextureFactory.of(Blocks.planks, 1));
        CoverRegistry.registerDecorativeCover(ItemList.Plank_Birch.get(1L), TextureFactory.of(Blocks.planks, 2));
        CoverRegistry.registerDecorativeCover(ItemList.Plank_Jungle.get(1L), TextureFactory.of(Blocks.planks, 3));
        CoverRegistry.registerDecorativeCover(ItemList.Plank_Acacia.get(1L), TextureFactory.of(Blocks.planks, 4));
        CoverRegistry.registerDecorativeCover(ItemList.Plank_DarkOak.get(1L), TextureFactory.of(Blocks.planks, 5));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Larch.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 0, new ItemStack(Blocks.planks, 1, 0))),
                0));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Teak.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 1, new ItemStack(Blocks.planks, 1, 0))),
                1));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Acacia_Green.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 2, new ItemStack(Blocks.planks, 1, 0))),
                2));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Lime.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 3, new ItemStack(Blocks.planks, 1, 0))),
                3));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Chestnut.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 4, new ItemStack(Blocks.planks, 1, 0))),
                4));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Wenge.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 5, new ItemStack(Blocks.planks, 1, 0))),
                5));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Baobab.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 6, new ItemStack(Blocks.planks, 1, 0))),
                6));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Sequoia.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 7, new ItemStack(Blocks.planks, 1, 0))),
                7));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Kapok.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 8, new ItemStack(Blocks.planks, 1, 0))),
                8));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Ebony.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 9, new ItemStack(Blocks.planks, 1, 0))),
                9));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Mahagony.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 10, new ItemStack(Blocks.planks, 1, 0))),
                10));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Balsa.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 11, new ItemStack(Blocks.planks, 1, 0))),
                11));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Willow.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 12, new ItemStack(Blocks.planks, 1, 0))),
                12));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Walnut.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 13, new ItemStack(Blocks.planks, 1, 0))),
                13));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Greenheart.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 14, new ItemStack(Blocks.planks, 1, 0))),
                14));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Cherry.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 15, new ItemStack(Blocks.planks, 1, 0))),
                15));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Mahoe.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 16, new ItemStack(Blocks.planks, 1, 0))),
                0));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Poplar.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 17, new ItemStack(Blocks.planks, 1, 0))),
                1));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Palm.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 18, new ItemStack(Blocks.planks, 1, 0))),
                2));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Papaya.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 19, new ItemStack(Blocks.planks, 1, 0))),
                3));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Pine.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 20, new ItemStack(Blocks.planks, 1, 0))),
                4));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Plum.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 21, new ItemStack(Blocks.planks, 1, 0))),
                5));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Maple.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 22, new ItemStack(Blocks.planks, 1, 0))),
                6));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Citrus.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler.getModItem(Forestry.ID, "planks", 1L, 23, new ItemStack(Blocks.planks, 1, 0))),
                7));
        CoverRegistry.registerDecorativeCover(
            ItemList.Plank_Cherry_EFR.get(1L),
            TextureFactory.of(
                GTUtility.getBlockFromStack(
                    GTModHandler
                        .getModItem(EtFuturumRequiem.ID, "wood_planks", 1L, 3, new ItemStack(Blocks.planks, 1, 0))),
                0));

        CoverRegistry.registerCover(
            ItemList.Cover_AdvancedRedstoneTransmitter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_TRANSMITTER)),
            context -> new CoverAdvancedRedstoneTransmitterExternal(
                context,
                TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_TRANSMITTER)));

        CoverRegistry.registerCover(
            ItemList.Cover_AdvancedRedstoneTransmitterInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_TRANSMITTER)),
            context -> new CoverAdvancedRedstoneTransmitterInternal(
                context,
                TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_TRANSMITTER)));

        CoverRegistry.registerCover(
            ItemList.Cover_AdvancedRedstoneReceiver.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_RECEIVER)),
            context -> new CoverAdvancedRedstoneReceiverExternal(
                context,
                TextureFactory.of(OVERLAY_ADVANCED_REDSTONE_RECEIVER)));

        CoverRegistry.registerCover(
            ItemList.Cover_AdvancedRedstoneReceiverInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_CONTROLLER)),
            context -> new CoverAdvancedRedstoneReceiverInternal(
                context,
                TextureFactory.of(OVERLAY_WIRELESS_CONTROLLER)));

        CoverRegistry.registerCover(
            ItemList.Cover_WirelessFluidDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_FLUID_DETECTOR)),
            context -> new CoverWirelessFluidDetector(context, TextureFactory.of(OVERLAY_WIRELESS_FLUID_DETECTOR)));

        CoverRegistry.registerCover(
            ItemList.Cover_WirelessItemDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_ITEM_DETECTOR)),
            context -> new CoverWirelessItemDetector(context, TextureFactory.of(OVERLAY_WIRELESS_ITEM_DETECTOR)));

        CoverRegistry.registerCover(
            ItemList.Cover_WirelessActivityDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_ACTIVITYDETECTOR)),
            context -> new CoverWirelessDoesWorkDetector(
                context,
                TextureFactory.of(OVERLAY_WIRELESS_ACTIVITYDETECTOR)));

        CoverRegistry.registerCover(
            ItemList.Cover_WirelessNeedsMaintainance.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_WIRELESS_MAINTENANCE_DETECTOR)),
            context -> new CoverWirelessMaintenanceDetector(
                context,
                TextureFactory.of(OVERLAY_WIRELESS_MAINTENANCE_DETECTOR)));
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return (aDoShowAllItems) || (!aPrefix.getName()
            .startsWith("toolHead"));
    }

    @Override
    public final ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (aDamage < 32000) {
            return null;
        }
        if (aDamage < 32100) {
            return ItemList.ThermosCan_Empty.get(1L);
        }
        if (aDamage < 32200) {
            return ItemList.Bottle_Empty.get(1L);
        }
        return null;
    }
}
