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
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Argentia;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Aurelia;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Bauxite;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_BobsYerUncleRanks;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Chilly;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Coppon;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Cucumber;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Ferru;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Grapes;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Ilmenite;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Indigo;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Iridium;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Lemon;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_MTomato;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Manganese;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Mica;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_MilkWart;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Naquadah;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Nickel;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_OilBerry;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Onion;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Osmium;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Pitchblende;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Platinum;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Plumbilia;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Rape;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Scheelite;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_TeaLeaf;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Thorium;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Tine;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Tomato;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_UUABerry;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_UUMBerry;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Uraninite;
import static gregtech.common.items.IDMetaItem02.Crop_Drop_Zinc;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
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
import gregtech.api.util.GTLog;
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
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;

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
            addItem(
                ThermosCan_Coffee.ID,
                "Coffee",
                "Coffee, dark, without anything else",
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
            addItem(
                ThermosCan_Sweet_Coffee.ID,
                "Sweet coffee",
                "Keeping you awake the whole night",
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
            addItem(
                ThermosCan_Latte.ID,
                "Latte",
                "Just the regular morning Coffee, with milk",
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
            addItem(
                ThermosCan_Sweet_Latte.ID,
                "Sweet Latte",
                "A little bit of sugar goes a long way",
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
            addItem(
                ThermosCan_Sweet_Jesus_Latte.ID,
                "Sweet Jesus Latte",
                "You want some Coffee to your Sugar?",
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
            addItem(
                ThermosCan_Dark_Chocolate_Milk.ID,
                "Dark Chocolate Milk",
                "A bit bitter, better add a bit Sugar",
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
            addItem(
                ThermosCan_Chocolate_Milk.ID,
                "Chocolate Milk",
                "Sweet Goodness",
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
            addItem(
                ThermosCan_Tea.ID,
                "Tea",
                "Keep calm and carry on",
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
            addItem(
                ThermosCan_Sweet_Tea.ID,
                "Sweet Tea",
                "How about a Tea Party? In Boston?",
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
            addItem(
                ThermosCan_Ice_Tea.ID,
                "Ice Tea",
                "Better than this purple Junk Drink from failed Potions",
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

        ItemList.GelledToluene.set(addItem(GelledToluene.ID, "Gelled Toluene", "Raw Explosive"));

        ItemList.Bottle_Purple_Drink.set(
            addItem(
                Bottle_Purple_Drink.ID,
                "Purple Drink",
                "How about Lemonade. Or some Ice Tea? I got Purple Drink!",
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
            addItem(
                Bottle_Grape_Juice.ID,
                "Grape Juice",
                "This has a cleaning effect on your internals.",
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
            addItem(
                Bottle_Wine.ID,
                "Wine",
                "Ordinary",
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
            addItem(
                Bottle_Vinegar.ID,
                "Vinegar",
                "Exquisite",
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
            addItem(
                Bottle_Potato_Juice.ID,
                "Potato Juice",
                "Ever seen Potato Juice in stores? No? That has a reason.",
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
            addItem(
                Bottle_Vodka.ID,
                "Vodka",
                "Not to confuse with Water",
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
            addItem(
                Bottle_Leninade.ID,
                "Leninade",
                "Let the Communism flow through you!",
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
            addItem(
                Bottle_Mineral_Water.ID,
                "Mineral Water",
                "The best Stuff you can drink to stay healthy",
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
            addItem(
                Bottle_Salty_Water.ID,
                "Salty Water",
                "Like Sea Water but less dirty",
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
            addItem(
                Bottle_Reed_Water.ID,
                "Reed Water",
                "I guess this tastes better when fermented",
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
            addItem(
                Bottle_Rum.ID,
                "Rum",
                "A buddle o' rum",
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
            addItem(
                Bottle_Pirate_Brew.ID,
                "Pirate Brew",
                "Set the Sails, we are going to Torrentuga!",
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
            addItem(
                Bottle_Hops_Juice.ID,
                "Hops Juice",
                "Every Beer has a start",
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
            addItem(
                Bottle_Dark_Beer.ID,
                "Dark Beer",
                "Dark Beer, for the real Men",
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
            addItem(
                Bottle_Dragon_Blood.ID,
                "Dragon Blood",
                "FUS RO DAH!",
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
            addItem(
                Bottle_Wheaty_Juice.ID,
                "Wheaty Juice",
                "Is this liquefied Bread or what?",
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
            addItem(
                Bottle_Scotch.ID,
                "Scotch",
                "Technically this is just a Whisky",
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
            addItem(
                Bottle_Glen_McKenner.ID,
                "Glen McKenner",
                "Don't hand to easily surprised people, they will shatter it.",
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
            addItem(
                Bottle_Wheaty_Hops_Juice.ID,
                "Wheaty Hops Juice",
                "Also known as 'Duff-Lite'",
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
            addItem(
                Bottle_Beer.ID,
                "Beer",
                "Good old Beer",
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
            addItem(
                Bottle_Chilly_Sauce.ID,
                "Chilly Sauce",
                "Spicy",
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
            addItem(
                Bottle_Hot_Sauce.ID,
                "Hot Sauce",
                "Very Spicy, I guess?",
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
            addItem(
                Bottle_Diabolo_Sauce.ID,
                "Diabolo Sauce",
                "As if the Devil made this Sauce",
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
            addItem(
                Bottle_Diablo_Sauce.ID,
                "Diablo Sauce",
                "Diablo always comes back!",
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
            addItem(
                Bottle_Snitches_Glitch_Sauce.ID,
                "Old Man Snitches glitched Diablo Sauce",
                "[Missing No]",
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
            addItem(
                Bottle_Apple_Juice.ID,
                "Apple Juice",
                "Made of the Apples from our best Oak Farms",
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
            addItem(
                Bottle_Cider.ID,
                "Cider",
                "If you have nothing better to do with your Apples",
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
            addItem(
                Bottle_Golden_Apple_Juice.ID,
                "Golden Apple Juice",
                "A golden Apple in liquid form",
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
            addItem(
                Bottle_Golden_Cider.ID,
                "Golden Cider",
                "More Resistance, less Regeneration",
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
            addItem(
                Bottle_Iduns_Apple_Juice.ID,
                "Idun's Apple Juice",
                "So you got the Idea of using Notch Apples for a drink?",
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
            addItem(
                Bottle_Notches_Brew.ID,
                "Notches Brew",
                "This is just overpowered",
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
            addItem(
                Bottle_Lemon_Juice.ID,
                "Lemon Juice",
                "Maybe adding Sugar will make it less sour",
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
            addItem(
                Bottle_Limoncello.ID,
                "Limoncello",
                "An alcoholic Drink which tastes like Lemons",
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
            addItem(
                Bottle_Lemonade.ID,
                "Lemonade",
                "Cold and refreshing Lemonade",
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
            addItem(
                Bottle_Alcopops.ID,
                "Alcopops",
                "Don't let your Children drink this junk!",
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
            addItem(
                Bottle_Cave_Johnsons_Grenade_Juice.ID,
                "Cave Johnson's Grenade Juice",
                "When life gives you Lemons, make Life take them Lemons back!",
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
            addItem(
                Bottle_Milk.ID,
                "Milk",
                "Got Milk?",
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
            addItem(
                Bottle_Holy_Water.ID,
                "Holy Water",
                "May the holy Planks be with you",
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
            addItem(
                Food_Potato_On_Stick.ID,
                "Potato on a Stick",
                "Totally looks like a Crab Claw",
                new GTFoodStat(1, 0.3F, EnumAction.eat, new ItemStack(Items.stick, 1), false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Food_Potato_On_Stick_Roasted.set(
            addItem(
                Food_Potato_On_Stick_Roasted.ID,
                "Roasted Potato on a Stick",
                "Still looks like a Crab Claw",
                new GTFoodStat(6, 0.6F, EnumAction.eat, new ItemStack(Items.stick, 1), false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Fries.set(
            addItem(
                Food_Raw_Fries.ID,
                "Potato Strips",
                "It's Potato in Stripe Form",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Fries.set(
            addItem(
                Food_Fries.ID,
                "Fries",
                "Not to confuse with Fry the Delivery Boy",
                new GTFoodStat(7, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Packaged_Fries.set(
            addItem(
                Food_Packaged_Fries.ID,
                "Fries",
                "Ketchup not included",
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
            addItem(
                Food_Raw_PotatoChips.ID,
                "Potato Chips (Raw)",
                "Just like a Potato",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_PotatoChips.set(
            addItem(
                Food_PotatoChips.ID,
                "Potato Chips",
                "Crunchy",
                new GTFoodStat(7, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_ChiliChips.set(
            addItem(
                Food_ChiliChips.ID,
                "Chili Chips",
                "Spicy",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Packaged_PotatoChips.set(
            addItem(
                Food_Packaged_PotatoChips.ID,
                "Bag of Potato Chips",
                "Full of delicious Air",
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
            addItem(
                Food_Packaged_ChiliChips.ID,
                "Bag of Chili Chips",
                "Stop making noises Baj!",
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
            addItem(
                Food_Chum.ID,
                "Chum",
                "Chum is Fum!",
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
            addItem(
                Food_Chum_On_Stick.ID,
                "Chum on a Stick",
                "Don't forget to try our Chum-balaya",
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
            addItem(
                Food_Dough_Sugar.ID,
                "Sugary Dough",
                "Don't eat the Dough before it is baken",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Dough_Chocolate.set(
            addItem(
                Food_Dough_Chocolate.ID,
                "Chocolate Dough",
                "I said don't eat the Dough!",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Cookie.set(
            addItem(
                Food_Raw_Cookie.ID,
                "Cookie shaped Dough",
                "For baking Cookies",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));

        ItemList.Food_Sliced_Buns.set(
            addItem(
                Food_Sliced_Buns.ID,
                "Buns",
                "Pre Sliced",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Veggie.set(
            addItem(
                Food_Burger_Veggie.ID,
                "Veggieburger",
                "No matter how you call this, this is NOT a Burger!",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Cheese.set(
            addItem(
                Food_Burger_Cheese.ID,
                "Cheeseburger",
                "Cheesy!",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new ItemData(Materials.Cheese, 907200L)));
        ItemList.Food_Burger_Meat.set(
            addItem(
                Food_Burger_Meat.ID,
                "Hamburger",
                "The Mc Burger Queen Burger",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Burger_Chum.set(
            addItem(
                Food_Burger_Chum.ID,
                "Chumburger",
                "Fum is Chum!",
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
            addItem(
                Food_Sliced_Breads.ID,
                "Breads",
                "Pre Sliced",
                new GTFoodStat(5, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Veggie.set(
            addItem(
                Food_Sandwich_Veggie.ID,
                "Veggie Sandwich",
                "Meatless",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Cheese.set(
            addItem(
                Food_Sandwich_Cheese.ID,
                "Cheese Sandwich",
                "Say Cheese!",
                new GTFoodStat(7, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Bacon.set(
            addItem(
                Food_Sandwich_Bacon.ID,
                "Bacon Sandwich",
                "The best Sandwich ever!",
                new GTFoodStat(10, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sandwich_Steak.set(
            addItem(
                Food_Sandwich_Steak.ID,
                "Steak Sandwich",
                "Not a 'Steam Sandwich'",
                new GTFoodStat(10, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Baguettes.set(
            addItem(
                Food_Sliced_Baguettes.ID,
                "Baguettes",
                "Pre Sliced",
                new GTFoodStat(8, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Veggie.set(
            addItem(
                Food_Large_Sandwich_Veggie.ID,
                "Large Veggie Sandwich",
                "Just not worth it",
                new GTFoodStat(15, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 3L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Cheese.set(
            addItem(
                Food_Large_Sandwich_Cheese.ID,
                "Large Cheese Sandwich",
                "I need another cheesy tooltip for this",
                new GTFoodStat(15, 0.8F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Bacon.set(
            addItem(
                Food_Large_Sandwich_Bacon.ID,
                "Large Bacon Sandwich",
                "For Men! (and manly Women)",
                new GTFoodStat(20, 1.0F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Large_Sandwich_Steak.set(
            addItem(
                Food_Large_Sandwich_Steak.ID,
                "Large Steak Sandwich",
                "Yes, I once accidentially called it 'Steam Sandwich'",
                new GTFoodStat(20, 1.0F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Veggie.set(
            addItem(
                Food_Raw_Pizza_Veggie.ID,
                "Raw Veggie Pizza",
                "Into the Oven with it!",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Cheese.set(
            addItem(
                Food_Raw_Pizza_Cheese.ID,
                "Raw Cheese Pizza",
                "Into the Oven with it!",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Pizza_Meat.set(
            addItem(
                Food_Raw_Pizza_Meat.ID,
                "Raw Mince Meat Pizza",
                "Into the Oven with it!",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));

        ItemList.Food_Baked_Pizza_Veggie.set(
            addItem(
                Food_Baked_Pizza_Veggie.ID,
                "Veggie Pizza",
                "The next they want is Gluten Free Pizzas...",
                new GTFoodStat(3, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Pizza_Cheese.set(
            addItem(
                Food_Baked_Pizza_Cheese.ID,
                "Cheese Pizza",
                "Pizza Magarita",
                new GTFoodStat(4, 0.4F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Pizza_Meat.set(
            addItem(
                Food_Baked_Pizza_Meat.ID,
                "Mince Meat Pizza",
                "Emo Pizza, it cuts itself!",
                new GTFoodStat(5, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));

        ItemList.Dye_Indigo.set(
            addItem(
                Dye_Indigo.ID,
                "Indigo Dye",
                "Blue Dye",
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L),
                Dyes.dyeBlue));

        int[] Dye_Colors = new int[] { Dye_Color_00.ID, Dye_Color_01.ID, Dye_Color_02.ID, Dye_Color_03.ID,
            Dye_Color_04.ID, Dye_Color_05.ID, Dye_Color_06.ID, Dye_Color_07.ID, Dye_Color_08.ID, Dye_Color_09.ID,
            Dye_Color_10.ID, Dye_Color_11.ID, Dye_Color_12.ID, Dye_Color_13.ID, Dye_Color_14.ID, Dye_Color_15.ID };
        for (int i = 0; i < 16; i = i + 1) {
            ItemList.DYE_ONLY_ITEMS[i].set(
                addItem(
                    Dye_Colors[i],
                    Dyes.get(i).mName + " Dye",
                    "",
                    Dyes.get(i)
                        .name(),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L)));
        }

        ItemList.Plank_Oak
            .set(addItem(Plank_Oak.ID, "Oak Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Spruce.set(
            addItem(Plank_Spruce.ID, "Spruce Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Birch
            .set(addItem(Plank_Birch.ID, "Birch Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Jungle.set(
            addItem(Plank_Jungle.ID, "Jungle Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Acacia.set(
            addItem(Plank_Acacia.ID, "Acacia Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_DarkOak.set(
            addItem(Plank_DarkOak.ID, "Dark Oak Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Larch
            .set(addItem(Plank_Larch.ID, "Larch Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Teak
            .set(addItem(Plank_Teak.ID, "Teak Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Acacia_Green.set(
            addItem(
                Plank_Acacia_Green.ID,
                "Green Acacia Plank",
                aTextCover,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Lime
            .set(addItem(Plank_Lime.ID, "Lime Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Chestnut.set(
            addItem(
                Plank_Chestnut.ID,
                "Chestnut Plank",
                aTextCover,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Wenge
            .set(addItem(Plank_Wenge.ID, "Wenge Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Baobab.set(
            addItem(Plank_Baobab.ID, "Baobab Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Sequoia.set(
            addItem(Plank_Sequoia.ID, "Sequoia Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Kapok
            .set(addItem(Plank_Kapok.ID, "Kapok Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Ebony
            .set(addItem(Plank_Ebony.ID, "Ebony Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Mahagony.set(
            addItem(
                Plank_Mahagony.ID,
                "Mahagony Plank",
                aTextCover,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Balsa
            .set(addItem(Plank_Balsa.ID, "Balsa Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Willow.set(
            addItem(Plank_Willow.ID, "Willow Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Walnut.set(
            addItem(Plank_Walnut.ID, "Walnut Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Greenheart.set(
            addItem(
                Plank_Greenheart.ID,
                "Greenheart Plank",
                aTextCover,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Cherry.set(
            addItem(Plank_Cherry.ID, "Cherry Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Mahoe
            .set(addItem(Plank_Mahoe.ID, "Mahoe Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Poplar.set(
            addItem(Plank_Poplar.ID, "Poplar Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Palm
            .set(addItem(Plank_Palm.ID, "Palm Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Papaya.set(
            addItem(Plank_Papaya.ID, "Papaya Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Pine
            .set(addItem(Plank_Pine.ID, "Pine Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Plum
            .set(addItem(Plank_Plum.ID, "Plum Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Maple
            .set(addItem(Plank_Maple.ID, "Maple Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Citrus.set(
            addItem(Plank_Citrus.ID, "Citrus Plank", aTextCover, new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.Plank_Cherry_EFR.set(
            addItem(
                Plank_Cherry_EFR.ID,
                "Cherry Plank",
                aTextCover,
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));
        ItemList.SFMixture.set(addItem(SFMixture.ID, "Super Fuel Binder", "Raw Material"));
        ItemList.MSFMixture.set(addItem(MSFMixture.ID, "Magic Super Fuel Binder", "Raw Material"));

        ItemList.Crop_Drop_Plumbilia.set(
            addItem(
                Crop_Drop_Plumbilia.ID,
                "Plumbilia Leaf",
                "Source of Lead",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L)));
        ItemList.Crop_Drop_Argentia.set(
            addItem(
                Crop_Drop_Argentia.ID,
                "Argentia Leaf",
                "Source of Silver",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Crop_Drop_Indigo.set(
            addItem(
                Crop_Drop_Indigo.ID,
                "Indigo Blossom",
                "Used for making Blue Dye",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1L)));
        ItemList.Crop_Drop_Ferru.set(
            addItem(
                Crop_Drop_Ferru.ID,
                "Ferru Leaf",
                "Source of Iron",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L)));
        ItemList.Crop_Drop_Aurelia.set(
            addItem(
                Crop_Drop_Aurelia.ID,
                "Aurelia Leaf",
                "Source of Gold",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Crop_Drop_TeaLeaf.set(
            addItem(
                Crop_Drop_TeaLeaf.ID,
                "Tea Leaf",
                "Source of Tea",
                "cropTea",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));

        ItemList.Crop_Drop_OilBerry.set(
            addItem(
                Crop_Drop_OilBerry.ID,
                "Oil Berry",
                "Oil in Berry form",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));
        ItemList.Crop_Drop_BobsYerUncleRanks.set(
            addItem(
                Crop_Drop_BobsYerUncleRanks.ID,
                "Bobs-Yer-Uncle-Berry",
                "Source of Emeralds",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Crop_Drop_UUMBerry.set(
            addItem(
                Crop_Drop_UUMBerry.ID,
                "UUM Berry",
                "UUM in Berry form",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));
        ItemList.Crop_Drop_UUABerry.set(
            addItem(
                Crop_Drop_UUABerry.ID,
                "UUA Berry",
                "UUA in Berry form",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));

        ItemList.Crop_Drop_MilkWart.set(
            addItem(
                Crop_Drop_MilkWart.ID,
                "Milk Wart",
                "Source of Milk",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 1L)));

        ItemList.Crop_Drop_Coppon.set(
            addItem(
                Crop_Drop_Coppon.ID,
                "Coppon Fiber",
                "ORANGE WOOOOOOOL!!!",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 1L)));

        ItemList.Crop_Drop_Tine.set(
            addItem(
                Crop_Drop_Tine.ID,
                "Tine Twig",
                "Source of Tin",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));

        ItemList.Crop_Drop_Mica.set(
            addItem(
                Crop_Drop_Mica.ID,
                "Micadia Twig",
                "Source of Mica",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L),
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 1L)));

        ItemList.Crop_Drop_Bauxite.set(addItem(Crop_Drop_Bauxite.ID, "Bauxia Leaf", "Source of Aluminium"));
        ItemList.Crop_Drop_Ilmenite.set(addItem(Crop_Drop_Ilmenite.ID, "Titania Leaf", "Source of Titanium"));
        ItemList.Crop_Drop_Pitchblende.set(addItem(Crop_Drop_Pitchblende.ID, "Reactoria Leaf", "Source of Uranium"));
        ItemList.Crop_Drop_Uraninite.set(addItem(Crop_Drop_Uraninite.ID, "Uranium Leaf", "Source of Uranite"));
        ItemList.Crop_Drop_Thorium.set(addItem(Crop_Drop_Thorium.ID, "Thunder Leaf", "Source of Thorium"));
        ItemList.Crop_Drop_Nickel.set(addItem(Crop_Drop_Nickel.ID, "Nickelback Leaf", "Source of Nickel"));
        ItemList.Crop_Drop_Zinc.set(addItem(Crop_Drop_Zinc.ID, "Galvania Leaf", "Source of Zinc"));
        ItemList.Crop_Drop_Manganese.set(addItem(Crop_Drop_Manganese.ID, "Pyrolusium Leaf", "Source of Manganese"));
        ItemList.Crop_Drop_Scheelite.set(addItem(Crop_Drop_Scheelite.ID, "Scheelinium Leaf", "Source of Tungsten"));
        ItemList.Crop_Drop_Platinum.set(addItem(Crop_Drop_Platinum.ID, "Platina Leaf", "Source of Platinum"));
        ItemList.Crop_Drop_Iridium.set(addItem(Crop_Drop_Iridium.ID, "Quantaria Leaf", "Source of Iridium"));
        ItemList.Crop_Drop_Osmium.set(addItem(Crop_Drop_Osmium.ID, "Quantaria Leaf", "Source of Osmium"));
        ItemList.Crop_Drop_Naquadah.set(addItem(Crop_Drop_Naquadah.ID, "Stargatium Leaf", "Source of Naquadah"));

        ItemList.Crop_Drop_Chilly.set(
            addItem(
                Crop_Drop_Chilly.ID,
                "Chilly Pepper",
                "It is red and hot",
                "cropChilipepper",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false, Potion.confusion.id, 200, 1, 40),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Lemon.set(
            addItem(
                Crop_Drop_Lemon.ID,
                "Lemon",
                "Don't make Lemonade",
                "cropLemon",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Tomato.set(
            addItem(
                Crop_Drop_Tomato.ID,
                "Tomato",
                "Solid Ketchup",
                "cropTomato",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_MTomato.set(
            addItem(
                Crop_Drop_MTomato.ID,
                "Max Tomato",
                "Full Health in one Tomato",
                "cropTomato",
                new GTFoodStat(
                    9,
                    1.0F,
                    EnumAction.eat,
                    null,
                    false,
                    true,
                    false,
                    Potion.regeneration.id,
                    100,
                    100,
                    100),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Grapes.set(
            addItem(
                Crop_Drop_Grapes.ID,
                "Grapes",
                "Source of Wine",
                "cropGrape",
                new GTFoodStat(2, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Onion.set(
            addItem(
                Crop_Drop_Onion.ID,
                "Onion",
                "Taking over the whole Taste",
                "cropOnion",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Cucumber.set(
            addItem(
                Crop_Drop_Cucumber.ID,
                "Cucumber",
                "Not a Sea Cucumber!",
                "cropCucumber",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Crop_Drop_Rape.set(
            addItem(
                Crop_Drop_Rape.ID,
                "Rape",
                "Also known as Canola.",
                new TCAspects.TC_AspectStack(TCAspects.MESSIS, 1L),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 1L)));

        ItemList.Food_Cheese.set(
            addItem(
                Food_Cheese.ID,
                "Cheese",
                "Click the Cheese",
                "foodCheese",
                new GTFoodStat(3, 0.6F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 2L)));
        ItemList.Food_Dough.set(
            addItem(
                Food_Dough.ID,
                "Dough",
                "For making Breads",
                "foodDough",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Flat_Dough.set(
            addItem(
                Food_Flat_Dough.ID,
                "Flattened Dough",
                "For making Pizza",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Bread.set(
            addItem(
                Food_Raw_Bread.ID,
                "Dough",
                "In Bread Shape",
                new GTFoodStat(1, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Bun.set(
            addItem(
                Food_Raw_Bun.ID,
                "Dough",
                "In Bun Shape",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Raw_Baguette.set(
            addItem(
                Food_Raw_Baguette.ID,
                "Dough",
                "In Baguette Shape",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Baked_Bun.set(
            addItem(
                Food_Baked_Bun.ID,
                "Bun",
                "Do not teleport Bread!",
                new GTFoodStat(3, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Baked_Baguette.set(
            addItem(
                Food_Baked_Baguette.ID,
                "Baguette",
                "I teleported nothing BUT Bread!!!",
                new GTFoodStat(8, 0.5F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Bread.set(
            addItem(
                Food_Sliced_Bread.ID,
                "Sliced Bread",
                "Just half a Bread",
                new GTFoodStat(2, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Bun.set(
            addItem(
                Food_Sliced_Bun.ID,
                "Sliced Bun",
                "Just half a Bun",
                new GTFoodStat(1, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Sliced_Baguette.set(
            addItem(
                Food_Sliced_Baguette.ID,
                "Sliced Baguette",
                "Just half a Baguette",
                new GTFoodStat(4, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L)));
        ItemList.Food_Raw_Cake.set(
            addItem(
                Food_Raw_Cake.ID,
                "Cake Bottom",
                "For making Cake",
                new GTFoodStat(2, 0.2F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Baked_Cake.set(
            addItem(
                Food_Baked_Cake.ID,
                "Baked Cake Bottom",
                "I know I promised you an actual Cake, but well...",
                new GTFoodStat(3, 0.3F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));
        ItemList.Food_Sliced_Lemon.set(
            addItem(
                Food_Sliced_Lemon.ID,
                "Lemon Slice",
                "Ideal to put on your Drink",
                new GTFoodStat(1, 0.075F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Tomato.set(
            addItem(
                Food_Sliced_Tomato.ID,
                "Tomato Slice",
                "Solid Ketchup",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Onion.set(
            addItem(
                Food_Sliced_Onion.ID,
                "Onion Slice",
                "ONIONS, UNITE!",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));
        ItemList.Food_Sliced_Cucumber.set(
            addItem(
                Food_Sliced_Cucumber.ID,
                "Cucumber Slice",
                "QUEWWW-CUMMM-BERRR!!!",
                new GTFoodStat(1, 0.05F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.HERBA, 1L)));

        ItemList.Food_Sliced_Cheese.set(
            addItem(
                Food_Sliced_Cheese.ID,
                "Cheese Slice",
                "ALIEN ATTACK!!!, throw the CHEEEEESE!!!",
                new GTFoodStat(1, 0.1F, EnumAction.eat, null, false, true, false),
                new TCAspects.TC_AspectStack(TCAspects.FAMES, 1L)));

        ItemList.Cover_AdvancedRedstoneTransmitter.set(
            addItem(
                Cover_AdvancedRedstoneTransmitter.ID,
                "Advanced Redstone Transmitter",
                "Transfers Redstone signals wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneTransmitterInternal.set(
            addItem(
                Cover_AdvancedRedstoneTransmitterInternal.ID,
                "Advanced Redstone Transmitter (Internal)",
                "Transfers Redstone signals wirelessly/n Can only connect with advanced wireless covers/n §cDEPRECATED! This will be removed in the next major update.",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneReceiver.set(
            addItem(
                Cover_AdvancedRedstoneReceiver.ID,
                "Advanced Redstone Receiver",
                "Transfers Redstone signals wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));
        ItemList.Cover_AdvancedRedstoneReceiverInternal.set(
            addItem(
                Cover_AdvancedRedstoneReceiverInternal.ID,
                "Advanced Redstone Receiver (Internal)",
                "Transfers Redstone signals wirelessly/n Can only connect with advanced wireless covers/n §cDEPRECATED! This will be removed in the next major update.",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L)));

        ItemList.Cover_WirelessFluidDetector.set(
            addItem(
                Cover_WirelessFluidDetector.ID,
                "Wireless Fluid Detector Cover",
                "Transfers Fluid Amount as Redstone wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L)));
        ItemList.Cover_WirelessItemDetector.set(
            addItem(
                Cover_WirelessItemDetector.ID,
                "Wireless Item Detector Cover",
                "Transfers Item Amount as Redstone wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));

        ItemList.Cover_WirelessNeedsMaintainance.set(
            addItem(
                Cover_WirelessNeedsMaintainance.ID,
                "Wireless Needs Maintenance Cover",
                "Transfers Maintenance Issues as Redstone wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 3L)));

        ItemList.Cover_WirelessActivityDetector.set(
            addItem(
                Cover_WirelessActivityDetector.ID,
                "Wireless Activity Detector Cover",
                "Transfers Activity as Redstone wirelessly/n Can only connect with advanced wireless covers",
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.SENSUS, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L)));

        ItemList.Display_ITS_FREE.set(
            addItem(
                Display_ITS_FREE.ID,
                "ITS FREE",
                "(or at least almost free)",
                SubTag.INVISIBLE,
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L)));
        ItemList.Item_Redstone_Sniffer
            .set(new ItemRedstoneSniffer("Item_Redstone_Sniffer", "Redstone Sniffer", "What are these frequencies?!"));
        ItemList.Vajra_Core.set(addItem(Vajra_Core.ID, "Vajra Core", ""));
        ItemList.Magnetron.set(addItem(Magnetron.ID, "Magnetron", ""));
        ItemList.ChaosLocator
            .set(new ItemChaosLocator("Item_Chaos_Locator", "Chaos Locator", "Warps to areas with extreme entropy"));

        try {
            CropCard tCrop;
            GTUtility.getField(tCrop = Crops.instance.getCropList()[13], "mDrop")
                .set(tCrop, ItemList.Crop_Drop_Ferru.get(1L));
            GTUtility.getField(tCrop = Crops.instance.getCropList()[14], "mDrop")
                .set(tCrop, ItemList.Crop_Drop_Aurelia.get(1L));
        } catch (Exception e) {
            if (GTValues.D1) {
                e.printStackTrace(GTLog.err);
            }
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
                NBTTagCompound tNBT = GTUtility.ItemNBT.getNBT(aStack);
                if (!tNBT.getBoolean("GT.HasBeenUpdated")) {
                    tNBT.setBoolean("GT.HasBeenUpdated", true);
                    GTUtility.ItemNBT.setNBT(aStack, tNBT);
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
        setBurnValue(32000 + Crop_Drop_Tine.ID, 100);
        setBurnValue(32000 + Crop_Drop_Mica.ID, 240);
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
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Plumbilia.get(9L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Argentia.get(9L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Indigo.get(1L))
            .itemOutputs(ItemList.Dye_Indigo.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_MilkWart.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Coppon.get(9L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(extractorRecipes);
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Tine.get(9L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(extractorRecipes);

    }

    public void initCompressorRecipes() {
        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Coppon.get(4L))
            .itemOutputs(new ItemStack(Blocks.wool, 1, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Plumbilia.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Argentia.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Indigo.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Ferru.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Aurelia.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_OilBerry.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_BobsYerUncleRanks.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Tine.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Rape.get(4L))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

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
            .itemInputs(ItemList.Crop_Drop_Tine.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L))
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
