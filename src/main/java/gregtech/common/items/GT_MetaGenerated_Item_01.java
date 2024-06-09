package gregtech.common.items;

import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Textures.BlockIcons.COVER_WOOD_PLATE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ACTIVITYDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ACTIVITYDETECTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ARM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CONTROLLER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CONVEYOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_CRAFTING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DRAIN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGYDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUIDDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FLUID_STORAGE_MONITOR0;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ITEMDETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MAINTENANCE_DETECTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PUMP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_REDSTONE_RECEIVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_REDSTONE_TRANSMITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SHUTTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VALVE;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_8V;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_EV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_HV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_IV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_LV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_LuV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_MV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_UV;
import static gregtech.api.enums.Textures.BlockIcons.SOLARPANEL_ZPM;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.client.GT_TooltipHandler.Tier.ERV;
import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.Tier.HV;
import static gregtech.client.GT_TooltipHandler.Tier.IV;
import static gregtech.client.GT_TooltipHandler.Tier.LV;
import static gregtech.client.GT_TooltipHandler.Tier.LuV;
import static gregtech.client.GT_TooltipHandler.Tier.MAX;
import static gregtech.client.GT_TooltipHandler.Tier.MV;
import static gregtech.client.GT_TooltipHandler.Tier.ULV;
import static gregtech.client.GT_TooltipHandler.Tier.UMV;
import static gregtech.client.GT_TooltipHandler.Tier.UV;
import static gregtech.client.GT_TooltipHandler.Tier.UXV;
import static gregtech.client.GT_TooltipHandler.Tier.ZPM;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Item_X32;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_FoodStat;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.GT_Cover_Arm;
import gregtech.common.covers.GT_Cover_ControlsWork;
import gregtech.common.covers.GT_Cover_Conveyor;
import gregtech.common.covers.GT_Cover_Crafting;
import gregtech.common.covers.GT_Cover_DoesWork;
import gregtech.common.covers.GT_Cover_Drain;
import gregtech.common.covers.GT_Cover_EUMeter;
import gregtech.common.covers.GT_Cover_FluidLimiter;
import gregtech.common.covers.GT_Cover_FluidRegulator;
import gregtech.common.covers.GT_Cover_FluidStorageMonitor;
import gregtech.common.covers.GT_Cover_Fluidfilter;
import gregtech.common.covers.GT_Cover_ItemFilter;
import gregtech.common.covers.GT_Cover_ItemMeter;
import gregtech.common.covers.GT_Cover_LiquidMeter;
import gregtech.common.covers.GT_Cover_NeedMaintainance;
import gregtech.common.covers.GT_Cover_PlayerDetector;
import gregtech.common.covers.GT_Cover_Pump;
import gregtech.common.covers.GT_Cover_RedstoneReceiverExternal;
import gregtech.common.covers.GT_Cover_RedstoneReceiverInternal;
import gregtech.common.covers.GT_Cover_RedstoneTransmitterExternal;
import gregtech.common.covers.GT_Cover_RedstoneTransmitterInternal;
import gregtech.common.covers.GT_Cover_Screen;
import gregtech.common.covers.GT_Cover_Shutter;
import gregtech.common.covers.GT_Cover_SolarPanel;
import gregtech.common.covers.GT_Cover_SteamRegulator;
import gregtech.common.covers.GT_Cover_SteamValve;
import gregtech.common.items.behaviors.Behaviour_Arrow_Potion;
import gregtech.common.items.behaviors.Behaviour_Cover_Tool;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.common.items.behaviors.Behaviour_DataStick;
import gregtech.common.items.behaviors.Behaviour_Lighter;
import gregtech.common.items.behaviors.Behaviour_PrintedPages;
import gregtech.common.items.behaviors.Behaviour_Scanner;
import gregtech.common.items.behaviors.Behaviour_SensorKit;
import gregtech.common.items.behaviors.Behaviour_Sonictron;
import gregtech.common.items.behaviors.Behaviour_Spray_Color;
import gregtech.common.items.behaviors.Behaviour_Spray_Color_Remover;
import gregtech.common.items.behaviors.Behaviour_WrittenBook;

public class GT_MetaGenerated_Item_01 extends GT_MetaGenerated_Item_X32 {

    public static GT_MetaGenerated_Item_01 INSTANCE;
    private final String mToolTipPurify = GT_LanguageManager
        .addStringLocalization("metaitem.01.tooltip.purify", "Throw into Cauldron to get clean Dust");
    private static final String aTextArrow = "  A";
    private static final String aTextStick = " S ";
    private static final String aTextFeather = "F  ";
    private static final String aTextEmptyRow = "   ";
    private static final String aTextShape = " P ";
    private static final String PartCoverText = " L/t (";
    private static final String PartCoverText2 = " L/s) as Cover";
    private static final String PartNotCoverText = "Cannot be used as a Cover";
    private static final String RAText = "Grabs from and inserts into specific slots";
    private static final String FRText1 = "Configurable up to ";
    private static final String FRText2 = " L/sec (as Cover)/n Rightclick/Screwdriver-rightclick/Shift-screwdriver-rightclick/n to adjust the pump speed by 1/16/256 L/sec per click";

    public GT_MetaGenerated_Item_01() {
        super(
            "metaitem.01",
            OrePrefixes.dustTiny,
            OrePrefixes.dustSmall,
            OrePrefixes.dust,
            OrePrefixes.dustImpure,
            OrePrefixes.dustPure,
            OrePrefixes.crushed,
            OrePrefixes.crushedPurified,
            OrePrefixes.crushedCentrifuged,
            OrePrefixes.gem,
            OrePrefixes.nugget,
            null,
            OrePrefixes.ingot,
            OrePrefixes.ingotHot,
            OrePrefixes.ingotDouble,
            OrePrefixes.ingotTriple,
            OrePrefixes.ingotQuadruple,
            OrePrefixes.ingotQuintuple,
            OrePrefixes.plate,
            OrePrefixes.plateDouble,
            OrePrefixes.plateTriple,
            OrePrefixes.plateQuadruple,
            OrePrefixes.plateQuintuple,
            OrePrefixes.plateDense,
            OrePrefixes.stick,
            OrePrefixes.lens,
            OrePrefixes.round,
            OrePrefixes.bolt,
            OrePrefixes.screw,
            OrePrefixes.ring,
            OrePrefixes.foil,
            OrePrefixes.cell,
            OrePrefixes.cellPlasma);
        INSTANCE = this;

        int tLastID = 0;

        setBurnValue(17000 + Materials.Wood.mMetaItemSubID, 1600);
        GT_OreDictUnificator.addToBlacklist(new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID));

        // Compressor recipe
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 8L))
                .itemOutputs(new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
        }

        GregTech_API.registerCover(
            new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID),
            TextureFactory.of(COVER_WOOD_PLATE),
            null);

        ItemStack tStack = new ItemStack(this, 1, 17000 + Materials.Wood.mMetaItemSubID);
        tStack.setStackDisplayName("The holy Planks of Sengir");
        GT_Utility.ItemNBT.addEnchantment(tStack, Enchantment.smite, 10);
        GT_ModHandler.addCraftingRecipe(
            tStack,
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XXX", "XDX", "XXX", 'X', OrePrefixes.gem.get(Materials.NetherStar), 'D',
                new ItemStack(Blocks.dragon_egg, 1, 32767) });

        ItemList.Credit_Greg_Copper.set(addItem(tLastID = 0, "Copper GT Credit", "0.125 Credits"));
        ItemList.Credit_Greg_Cupronickel.set(
            addItem(tLastID = 1, "Cupronickel GT Credit", "1 Credit", new ItemData(Materials.Cupronickel, 907200L)));
        ItemList.Credit_Greg_Silver.set(
            addItem(
                tLastID = 2,
                "Silver GT Credit",
                "8 Credits",
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Credit_Greg_Gold.set(addItem(tLastID = 3, "Gold GT Credit", "64 Credits"));
        ItemList.Credit_Greg_Platinum.set(addItem(tLastID = 4, "Platinum GT Credit", "512 Credits"));
        ItemList.Credit_Greg_Osmium.set(addItem(tLastID = 5, "Osmium GT Credit", "4,096 Credits"));
        ItemList.Credit_Greg_Naquadah.set(addItem(tLastID = 6, "Naquadah GT Credit", "32,768 Credits"));
        ItemList.Credit_Greg_Neutronium.set(addItem(tLastID = 7, "Neutronium GT Credit", "262,144 Credits"));
        ItemList.Coin_Gold_Ancient.set(
            addItem(
                tLastID = 8,
                "Ancient Gold Coin",
                "Found in ancient Ruins",
                new ItemData(Materials.Gold, 907200L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 8L)));
        ItemList.Coin_Doge.set(
            addItem(
                tLastID = 9,
                "Doge Coin",
                "wow much coin how money so crypto plz mine v rich very currency wow",
                new ItemData(Materials.Brass, 907200L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Coin_Chocolate.set(
            addItem(
                tLastID = 10,
                "Chocolate Coin",
                "Wrapped in Gold",
                new ItemData(Materials.Gold, OrePrefixes.foil.mMaterialAmount),
                new GT_FoodStat(
                    1,
                    0.1F,
                    EnumAction.eat,
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 1L),
                    true,
                    false,
                    false,
                    Potion.moveSpeed.id,
                    200,
                    1,
                    100)));
        ItemList.Credit_Copper.set(addItem(tLastID = 11, "Industrial Copper Credit", "0.125 Credits"));

        ItemList.Credit_Silver.set(
            addItem(
                tLastID = 13,
                "Industrial Silver Credit",
                "8 Credits",
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)));
        ItemList.Credit_Gold.set(addItem(tLastID = 14, "Industrial Gold Credit", "64 Credits"));
        ItemList.Credit_Platinum.set(addItem(tLastID = 15, "Industrial Platinum Credit", "512 Credits"));
        ItemList.Credit_Osmium.set(addItem(tLastID = 16, "Industrial Osmium Credit", "4096 Credits"));

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Coin_Chocolate.get(1L),
            new Object[] { OrePrefixes.dust.get(Materials.Cocoa), OrePrefixes.dust.get(Materials.Milk),
                OrePrefixes.dust.get(Materials.Sugar), OrePrefixes.foil.get(Materials.Gold) });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Copper.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Osmium });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Iron.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper, ItemList.Credit_Copper,
                ItemList.Credit_Copper });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Silver.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron,
                ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron, ItemList.Credit_Iron });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Gold.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver, ItemList.Credit_Silver,
                ItemList.Credit_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Platinum.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold,
                ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold, ItemList.Credit_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Osmium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum, ItemList.Credit_Platinum,
                ItemList.Credit_Platinum });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Copper.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(8L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Neutronium });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Cupronickel.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper,
                ItemList.Credit_Greg_Copper, ItemList.Credit_Greg_Copper });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Silver.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel,
                ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel, ItemList.Credit_Greg_Cupronickel });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Gold.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver,
                ItemList.Credit_Greg_Silver, ItemList.Credit_Greg_Silver });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Platinum.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold,
                ItemList.Credit_Greg_Gold, ItemList.Credit_Greg_Gold });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Osmium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum,
                ItemList.Credit_Greg_Platinum, ItemList.Credit_Greg_Platinum });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Naquadah.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium,
                ItemList.Credit_Greg_Osmium, ItemList.Credit_Greg_Osmium });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Credit_Greg_Neutronium.get(1L),
            GT_ModHandler.RecipeBits.KEEPNBT | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah,
                ItemList.Credit_Greg_Naquadah, ItemList.Credit_Greg_Naquadah });

        ItemList.Component_Minecart_Wheels_Iron.set(
            addItem(
                tLastID = 100,
                "Iron Minecart Wheels",
                "To get things rolling",
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L)));
        ItemList.Component_Minecart_Wheels_Steel.set(
            addItem(
                tLastID = 101,
                "Steel Minecart Wheels",
                "To get things rolling",
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Iron.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.AnyIron), 'S',
                OrePrefixes.stick.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Minecart_Wheels_Steel.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " h ", "RSR", " w ", 'R', OrePrefixes.ring.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel) });

        ItemList.CompressedFireclay.set(addItem(tLastID = 110, "Compressed Fireclay", "Brick-shaped"));
        ItemList.Firebrick.set(addItem(tLastID = 111, "Firebrick", "Heat resistant"));

        ItemList.Arrow_Head_Glass_Emtpy.set(
            addItem(
                tLastID = 200,
                "Empty Glass Arrow Head",
                "Fill with Potions before use",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Head_Glass_Poison.set(
            addItem(
                tLastID = 201,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Poison_Long.set(
            addItem(
                tLastID = 202,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with stretched Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Poison_Strong.set(
            addItem(
                tLastID = 203,
                "Poison Glass Arrow Head",
                "Glass Arrow filled with strong Poison",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Slowness.set(
            addItem(
                tLastID = 204,
                "Slowness Glass Arrow Head",
                "Glass Arrow filled with Laming Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Slowness_Long.set(
            addItem(
                tLastID = 205,
                "Slowness Glass Arrow Head",
                "Glass Arrow filled with stretched Laming Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Weakness.set(
            addItem(
                tLastID = 206,
                "Weakness Glass Arrow Head",
                "Glass Arrow filled with Weakening Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Weakness_Long.set(
            addItem(
                tLastID = 207,
                "Weakness Glass Arrow Head",
                "Glass Arrow filled with stretched Weakening Brew",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Head_Glass_Holy_Water.set(
            addItem(
                tLastID = 208,
                "Holy Water Glass Arrow Head",
                "Glass Arrow filled with Holy Water",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        ItemList.Arrow_Wooden_Glass_Emtpy.set(
            addItem(
                tLastID = 225,
                "Regular Glass Vial Arrow",
                "Empty Glass Arrow",
                new Behaviour_Arrow_Potion(1.0F, 6.0F),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison.set(
            addItem(
                tLastID = 226,
                "Regular Poison Arrow",
                "Glass Arrow filled with Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 450, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison_Long.set(
            addItem(
                tLastID = 227,
                "Regular Poison Arrow",
                "Glass Arrow filled with stretched Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Poison_Strong.set(
            addItem(
                tLastID = 228,
                "Regular Poison Arrow",
                "Glass Arrow filled with strong Poison",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.poison.id, 450, 1, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Slowness.set(
            addItem(
                tLastID = 229,
                "Regular Slowness Arrow",
                "Glass Arrow filled with Laming Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.moveSlowdown.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Slowness_Long.set(
            addItem(
                tLastID = 230,
                "Regular Slowness Arrow",
                "Glass Arrow filled with stretched Laming Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.moveSlowdown.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Weakness.set(
            addItem(
                tLastID = 231,
                "Regular Weakness Arrow",
                "Glass Arrow filled with Weakening Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.weakness.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Weakness_Long.set(
            addItem(
                tLastID = 232,
                "Regular Weakness Arrow",
                "Glass Arrow filled with stretched Weakening Brew",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Potion.weakness.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Wooden_Glass_Holy_Water.set(
            addItem(
                tLastID = 233,
                "Regular Holy Water Arrow",
                "Glass Arrow filled with Holy Water",
                new Behaviour_Arrow_Potion(1.0F, 6.0F, Enchantment.smite, 10),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        ItemList.Arrow_Plastic_Glass_Emtpy.set(
            addItem(
                tLastID = 250,
                "Light Glass Vial Arrow",
                "Empty Glass Arrow",
                new Behaviour_Arrow_Potion(1.5F, 6.0F),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison.set(
            addItem(
                tLastID = 251,
                "Light Poison Arrow",
                "Glass Arrow filled with Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 450, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison_Long.set(
            addItem(
                tLastID = 252,
                "Light Poison Arrow",
                "Glass Arrow filled with stretched Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Poison_Strong.set(
            addItem(
                tLastID = 253,
                "Light Poison Arrow",
                "Glass Arrow filled with strong Poison",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.poison.id, 450, 1, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Slowness.set(
            addItem(
                tLastID = 254,
                "Light Slowness Arrow",
                "Glass Arrow filled with Laming Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.moveSlowdown.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Slowness_Long.set(
            addItem(
                tLastID = 255,
                "Light Slowness Arrow",
                "Glass Arrow filled with stretched Laming Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.moveSlowdown.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Weakness.set(
            addItem(
                tLastID = 256,
                "Light Weakness Arrow",
                "Glass Arrow filled with Weakening Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.weakness.id, 900, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Weakness_Long.set(
            addItem(
                tLastID = 257,
                "Light Weakness Arrow",
                "Glass Arrow filled with stretched Weakening Brew",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Potion.weakness.id, 1800, 0, 100),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)));
        ItemList.Arrow_Plastic_Glass_Holy_Water.set(
            addItem(
                tLastID = 258,
                "Light Holy Water Arrow",
                "Glass Arrow filled with Holy Water",
                new Behaviour_Arrow_Potion(1.5F, 6.0F, Enchantment.smite, 10),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 1L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Emtpy.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Emtpy, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Poison_Strong.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Strong, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Slowness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Slowness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Weakness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Weakness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Wooden_Glass_Holy_Water.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Holy_Water, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Wood) });

        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Emtpy.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Emtpy, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Poison_Strong.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Poison_Strong, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Slowness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Slowness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Slowness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Weakness.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Weakness_Long.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Weakness_Long, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Arrow_Plastic_Glass_Holy_Water.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { aTextArrow, aTextStick, aTextFeather, 'A', ItemList.Arrow_Head_Glass_Holy_Water, 'F',
                OreDictNames.craftingFeather, 'S', OrePrefixes.stick.get(Materials.Plastic) });

        ItemList.Shape_Empty.set(
            addItem(
                tLastID = 300,
                "Empty Shape Plate",
                "Raw Plate to make Molds and Extruder Shapes",
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)));

        ItemList.Shape_Mold_Plate.set(addItem(tLastID = 301, "Mold (Plate)", "Mold for making Plates"));
        ItemList.Shape_Mold_Casing.set(addItem(tLastID = 302, "Mold (Casing)", "Mold for making Item Casings"));
        ItemList.Shape_Mold_Gear.set(addItem(tLastID = 303, "Mold (Gear)", "Mold for making Gears"));
        ItemList.Shape_Mold_Credit
            .set(addItem(tLastID = 304, "Mold (Coinage)", "Secure Mold for making Coins (Don't lose it!)"));
        ItemList.Shape_Mold_Bottle.set(addItem(tLastID = 305, "Mold (Bottle)", "Mold for making Bottles"));
        ItemList.Shape_Mold_Ingot.set(addItem(tLastID = 306, "Mold (Ingot)", "Mold for making Ingots"));
        ItemList.Shape_Mold_Ball.set(addItem(tLastID = 307, "Mold (Ball)", "Mold for making Balls"));
        ItemList.Shape_Mold_Block.set(addItem(tLastID = 308, "Mold (Block)", "Mold for making Blocks"));
        ItemList.Shape_Mold_Nugget.set(addItem(tLastID = 309, "Mold (Nuggets)", "Mold for making Nuggets"));
        ItemList.Shape_Mold_Bun.set(addItem(tLastID = 310, "Mold (Buns)", "Mold for shaping Buns"));
        ItemList.Shape_Mold_Bread.set(addItem(tLastID = 311, "Mold (Bread)", "Mold for shaping Breads"));
        ItemList.Shape_Mold_Baguette.set(addItem(tLastID = 312, "Mold (Baguette)", "Mold for shaping Baguettes"));
        ItemList.Shape_Mold_Cylinder.set(addItem(tLastID = 313, "Mold (Cylinder)", "Mold for shaping Cylinders"));
        ItemList.Shape_Mold_Anvil.set(addItem(tLastID = 314, "Mold (Anvil)", "Mold for shaping Anvils"));
        ItemList.Shape_Mold_Name
            .set(addItem(tLastID = 315, "Mold (Name)", "Mold for naming Items (rename Mold with Anvil)"));
        ItemList.Shape_Mold_Arrow.set(addItem(tLastID = 316, "Mold (Arrow Head)", "Mold for making Arrow Heads"));
        ItemList.Shape_Mold_Gear_Small.set(addItem(tLastID = 317, "Mold (Small Gear)", "Mold for making small Gears"));
        ItemList.Shape_Mold_Rod.set(addItem(tLastID = 318, "Mold (Rod)", "Mold for making Rods"));
        ItemList.Shape_Mold_Bolt.set(addItem(tLastID = 319, "Mold (Bolt)", "Mold for making Bolts"));
        ItemList.Shape_Mold_Round.set(addItem(tLastID = 320, "Mold (Round)", "Mold for making Rounds"));
        ItemList.Shape_Mold_Screw.set(addItem(tLastID = 321, "Mold (Screw)", "Mold for making Screws"));
        ItemList.Shape_Mold_Ring.set(addItem(tLastID = 322, "Mold (Ring)", "Mold for making Rings"));
        ItemList.Shape_Mold_Rod_Long.set(addItem(tLastID = 323, "Mold (Long Rod)", "Mold for making Long Rods"));
        ItemList.Shape_Mold_Rotor.set(addItem(tLastID = 324, "Mold (Rotor)", "Mold for making a Rotor"));
        ItemList.Shape_Mold_Turbine_Blade
            .set(addItem(tLastID = 325, "Mold (Turbine Blade)", "Mold for making a Turbine Blade"));
        ItemList.Shape_Mold_Pipe_Tiny.set(addItem(tLastID = 326, "Mold (Tiny Pipe)", "Mold for making tiny Pipes"));
        ItemList.Shape_Mold_Pipe_Small.set(addItem(tLastID = 327, "Mold (Small Pipe)", "Mold for making small Pipes"));
        ItemList.Shape_Mold_Pipe_Medium.set(addItem(tLastID = 328, "Mold (Normal Pipe)", "Mold for making Pipes"));
        ItemList.Shape_Mold_Pipe_Large.set(addItem(tLastID = 329, "Mold (Large Pipe)", "Mold for making large Pipes"));
        ItemList.Shape_Mold_Pipe_Huge
            .set(addItem(tLastID = 330, "Mold (Huge Pipe)", "Mold for making full Block Pipes"));
        ItemList.Shape_Mold_ToolHeadDrill
            .set(addItem(tLastID = 331, "Mold (Drill Head)", "Mold for making Drill Heads"));
        GT_ModHandler.removeRecipe(
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass),
            null,
            new ItemStack(Blocks.glass));

        ItemList.Shape_Extruder_Plate
            .set(addItem(tLastID = 350, "Extruder Shape (Plate)", "Extruder Shape for making Plates"));
        ItemList.Shape_Extruder_Rod
            .set(addItem(tLastID = 351, "Extruder Shape (Rod)", "Extruder Shape for making Rods"));
        ItemList.Shape_Extruder_Bolt
            .set(addItem(tLastID = 352, "Extruder Shape (Bolt)", "Extruder Shape for making Bolts"));
        ItemList.Shape_Extruder_Ring
            .set(addItem(tLastID = 353, "Extruder Shape (Ring)", "Extruder Shape for making Rings"));
        ItemList.Shape_Extruder_Cell
            .set(addItem(tLastID = 354, "Extruder Shape (Cell)", "Extruder Shape for making Cells"));
        ItemList.Shape_Extruder_Ingot.set(
            addItem(tLastID = 355, "Extruder Shape (Ingot)", "Extruder Shape for, wait, can't we just use a Furnace?"));
        ItemList.Shape_Extruder_Wire
            .set(addItem(tLastID = 356, "Extruder Shape (Wire)", "Extruder Shape for making Wires"));
        ItemList.Shape_Extruder_Casing
            .set(addItem(tLastID = 357, "Extruder Shape (Casing)", "Extruder Shape for making Item Casings"));
        ItemList.Shape_Extruder_Pipe_Tiny
            .set(addItem(tLastID = 358, "Extruder Shape (Tiny Pipe)", "Extruder Shape for making tiny Pipes"));
        ItemList.Shape_Extruder_Pipe_Small
            .set(addItem(tLastID = 359, "Extruder Shape (Small Pipe)", "Extruder Shape for making small Pipes"));
        ItemList.Shape_Extruder_Pipe_Medium
            .set(addItem(tLastID = 360, "Extruder Shape (Normal Pipe)", "Extruder Shape for making Pipes"));
        ItemList.Shape_Extruder_Pipe_Large
            .set(addItem(tLastID = 361, "Extruder Shape (Large Pipe)", "Extruder Shape for making large Pipes"));
        ItemList.Shape_Extruder_Pipe_Huge
            .set(addItem(tLastID = 362, "Extruder Shape (Huge Pipe)", "Extruder Shape for making full Block Pipes"));
        ItemList.Shape_Extruder_Block
            .set(addItem(tLastID = 363, "Extruder Shape (Block)", "Extruder Shape for making Blocks"));
        ItemList.Shape_Extruder_Sword
            .set(addItem(tLastID = 364, "Extruder Shape (Sword Blade)", "Extruder Shape for making Swords"));
        ItemList.Shape_Extruder_Pickaxe
            .set(addItem(tLastID = 365, "Extruder Shape (Pickaxe Head)", "Extruder Shape for making Pickaxes"));
        ItemList.Shape_Extruder_Shovel
            .set(addItem(tLastID = 366, "Extruder Shape (Shovel Head)", "Extruder Shape for making Shovels"));
        ItemList.Shape_Extruder_Axe
            .set(addItem(tLastID = 367, "Extruder Shape (Axe Head)", "Extruder Shape for making Axes"));
        ItemList.Shape_Extruder_Hoe
            .set(addItem(tLastID = 368, "Extruder Shape (Hoe Head)", "Extruder Shape for making Hoes"));
        ItemList.Shape_Extruder_Hammer
            .set(addItem(tLastID = 369, "Extruder Shape (Hammer Head)", "Extruder Shape for making Hammers"));
        ItemList.Shape_Extruder_File
            .set(addItem(tLastID = 370, "Extruder Shape (File Head)", "Extruder Shape for making Files"));
        ItemList.Shape_Extruder_Saw
            .set(addItem(tLastID = 371, "Extruder Shape (Saw Blade)", "Extruder Shape for making Saws"));
        ItemList.Shape_Extruder_Gear
            .set(addItem(tLastID = 372, "Extruder Shape (Gear)", "Extruder Shape for making Gears"));
        ItemList.Shape_Extruder_Bottle
            .set(addItem(tLastID = 373, "Extruder Shape (Bottle)", "Extruder Shape for making Bottles"));
        ItemList.Shape_Extruder_Rotor
            .set(addItem(tLastID = 374, "Extruder Shape (Rotor)", "Extruder Shape for a Rotor"));
        ItemList.Shape_Extruder_Small_Gear
            .set(addItem(tLastID = 375, "Extruder Shape (Small Gear)", "Extruder Shape for a Small Gear"));
        ItemList.Shape_Extruder_Turbine_Blade
            .set(addItem(tLastID = 376, "Extruder Shape (Turbine Blade)", "Extruder Shape for a Turbine Blade"));
        ItemList.Shape_Extruder_ToolHeadDrill
            .set(addItem(tLastID = 377, "Extruder Shape (Drill Head)", "Extruder Shape for a Drill Head"));

        ItemList.Shape_Slicer_Flat.set(addItem(tLastID = 398, "Slicer Blade (Flat)", "Slicer Blade for cutting Flat"));
        ItemList.Shape_Slicer_Stripes
            .set(addItem(tLastID = 399, "Slicer Blade (Stripes)", "Slicer Blade for cutting Stripes"));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Flat.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", aTextShape, "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Shape_Slicer_Stripes.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "hXS", "XPX", "fXd", 'P', ItemList.Shape_Extruder_Block, 'X',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel) });

        ItemList.Fuel_Can_Plastic_Empty.set(
            addItem(
                tLastID = 400,
                "Empty Plastic Fuel Can",
                "Used to store Fuels",
                new ItemData(Materials.Plastic, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));
        ItemList.Fuel_Can_Plastic_Filled.set(
            addItem(
                tLastID = 401,
                "Plastic Fuel Can",
                "Burns well in Diesel Generators",
                new ItemData(Materials.Plastic, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Fuel_Can_Plastic_Empty.get(7L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " PP", "P P", "PPP", 'P', OrePrefixes.plate.get(Materials.Plastic) });

        ItemList.Spray_Empty.set(
            addItem(
                tLastID = 402,
                "Empty Spray Can",
                "Used for making Sprays",
                new ItemData(
                    Materials.Tin,
                    OrePrefixes.plate.mMaterialAmount * 2L,
                    Materials.Redstone,
                    OrePrefixes.dust.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Spray_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        ItemList.Crate_Empty.set(
            addItem(
                tLastID = 403,
                "Empty Crate",
                "To Package lots of Material",
                new ItemData(Materials.Wood, 3628800L, Materials.Iron, OrePrefixes.screw.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Crate_Empty.get(4L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWS", "WdW", "SWS", 'W', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.screw.get(Materials.AnyIron) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Crate_Empty.get(4L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "SWS", "WdW", "SWS", 'W', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.screw.get(Materials.Steel) });

        ItemList.ThermosCan_Empty.set(
            addItem(
                tLastID = 404,
                "Empty Thermos Can",
                "Keeping hot things hot and cold things cold",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 1L + 2L * OrePrefixes.ring.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Aluminium, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ThermosCan_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Steel.set(
            addItem(
                tLastID = 405,
                "Large Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.Steel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Bronze, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        setFluidContainerStats(32000 + tLastID, 8000L, 64L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Steel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnyBronze, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Steel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_TungstenSteel.set(
            addItem(
                tLastID = 406,
                "Large Tungstensteel Fluid Cell",
                "",
                new ItemData(
                    Materials.TungstenSteel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Platinum, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 9L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 7L)));
        setFluidContainerStats(32000 + tLastID, 512000L, 32L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Platinum, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Aluminium.set(
            addItem(
                tLastID = 407,
                "Large Aluminium Fluid Cell",
                "",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Silver, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 5L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)));
        setFluidContainerStats(32000 + tLastID, 32000L, 64L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Aluminium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Silver, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Aluminium.get(1L))
            .duration(10 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_StainlessSteel.set(
            addItem(
                tLastID = 408,
                "Large Stainless Steel Fluid Cell",
                "",
                new ItemData(
                    Materials.StainlessSteel,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Electrum, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        setFluidContainerStats(32000 + tLastID, 64000L, 64L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Electrum, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_StainlessSteel.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Titanium.set(
            addItem(
                tLastID = 409,
                "Large Titanium Fluid Cell",
                "",
                new ItemData(
                    Materials.Titanium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.RoseGold, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 7L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 5L)));
        setFluidContainerStats(32000 + tLastID, 128000L, 64L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Titanium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.RoseGold, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Titanium.get(1L))
            .duration(10 * SECONDS)
            .eut(256)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Chrome.set(
            addItem(
                tLastID = 410,
                "Large Chrome Fluid Cell",
                "",
                new ItemData(
                    Materials.Chrome,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Palladium, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 6L)));
        setFluidContainerStats(32000 + tLastID, 2048000L, 8L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Chrome, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Palladium, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Chrome.get(1L))
            .duration(10 * SECONDS)
            .eut(1024)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Iridium.set(
            addItem(
                tLastID = 411,
                "Large Iridium Fluid Cell",
                "",
                new ItemData(
                    Materials.Iridium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Naquadah, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        setFluidContainerStats(32000 + tLastID, 8192000L, 2L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Naquadah, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Iridium.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Osmium.set(
            addItem(
                tLastID = 412,
                "Large Osmium Fluid Cell",
                "",
                new ItemData(
                    Materials.Osmium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.ElectrumFlux, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 11L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 9L)));
        setFluidContainerStats(32000 + tLastID, 32768000L, 1L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.ElectrumFlux, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Osmium.get(1L))
            .duration(10 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);

        ItemList.Large_Fluid_Cell_Neutronium.set(
            addItem(
                tLastID = 413,
                "Large Neutronium Fluid Cell",
                "",
                new ItemData(
                    Materials.Neutronium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    new MaterialStack(Materials.Draconium, OrePrefixes.ring.mMaterialAmount * 4L)),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 12L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 10L)));
        setFluidContainerStats(32000 + tLastID, 131072000L, 1L);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Draconium, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Large_Fluid_Cell_Neutronium.get(1L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            ItemList.SPRAY_CAN_DYES[i].set(
                addItem(
                    tLastID = 430 + 2 * i,
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Full",
                    new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
            ItemList.SPRAY_CAN_DYES_USED[i].set(
                addItem(
                    tLastID + 1,
                    "Spray Can (" + Dyes.get(i).mName + ")",
                    "Used",
                    new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 3L),
                    SubTag.INVISIBLE));
            IItemBehaviour<GT_MetaBase_Item> tBehaviour = new Behaviour_Spray_Color(
                ItemList.Spray_Empty.get(1L),
                ItemList.SPRAY_CAN_DYES_USED[i].get(1L),
                ItemList.SPRAY_CAN_DYES[i].get(1L),
                512L,
                i);
            addItemBehavior(32000 + tLastID, tBehaviour);
            addItemBehavior(32001 + tLastID, tBehaviour);
        }

        ItemList.Spray_Color_Remover.set(
            addItem(
                tLastID = 465,
                "Spray Can Solvent",
                "Full",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 8L)));
        ItemList.Spray_Color_Used_Remover.set(
            addItem(
                tLastID = 466,
                "Spray Can Solvent",
                "Used",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 3L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 6L),
                SubTag.INVISIBLE));
        IItemBehaviour<GT_MetaBase_Item> tBehaviour = new Behaviour_Spray_Color_Remover(
            ItemList.Spray_Empty.get(1L),
            ItemList.Spray_Color_Used_Remover.get(1L),
            ItemList.Spray_Color_Remover.get(1L),
            1024L);
        addItemBehavior(32000 + 465, tBehaviour);
        addItemBehavior(32000 + 466, tBehaviour);

        ItemList.Spray_Color_Remover_Empty.set(
            addItem(
                tLastID = 467,
                "Empty Spray Can Solvent Cannister",
                "Used for making Spray Can Solvent",
                new ItemData(
                    Materials.Aluminium,
                    OrePrefixes.plateDouble.mMaterialAmount * 4L,
                    Materials.Redstone,
                    OrePrefixes.dust.mMaterialAmount),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                ItemList.Large_Fluid_Cell_Aluminium.get(1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Spray_Color_Remover_Empty.get(1L))
            .duration(40 * SECONDS)
            .eut(1)
            .addTo(assemblerRecipes);

        ItemList.Tool_Matches.set(
            addItem(
                tLastID = 471,
                "Match",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Tool_MatchBox_Used.set(
            addItem(
                tLastID = 472,
                "Match Box",
                "This is not a Car",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_MatchBox_Full.set(
            addItem(
                tLastID = 473,
                "Match Box (Full)",
                "This is not a Car",
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        tBehaviour = new Behaviour_Lighter(null, ItemList.Tool_Matches.get(1L), ItemList.Tool_Matches.get(1L), 1L);
        addItemBehavior(32471, tBehaviour);
        tBehaviour = new Behaviour_Lighter(
            null,
            ItemList.Tool_MatchBox_Used.get(1L),
            ItemList.Tool_MatchBox_Full.get(1L),
            16L);
        addItemBehavior(32472, tBehaviour);
        addItemBehavior(32473, tBehaviour);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(1L))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.TricalciumPhosphate, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(1L))
            .duration(16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(4L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Wood, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Matches.get(4L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_Matches.get(16L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L))
            .itemOutputs(ItemList.Tool_MatchBox_Full.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Tool_MatchBox_Full.get(1L))
            .itemOutputs(ItemList.Tool_Matches.get(16L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(16)
            .addTo(packagerRecipes);

        ItemList.Tool_Lighter_Invar_Empty.set(
            addItem(
                tLastID = 474,
                "Lighter (Empty)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Invar_Used.set(
            addItem(
                tLastID = 475,
                "Lighter",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Invar_Full.set(
            addItem(
                tLastID = 476,
                "Lighter (Full)",
                "",
                new ItemData(Materials.Invar, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        tBehaviour = new Behaviour_Lighter(
            ItemList.Tool_Lighter_Invar_Empty.get(1L),
            ItemList.Tool_Lighter_Invar_Used.get(1L),
            ItemList.Tool_Lighter_Invar_Full.get(1L),
            100L);
        addItemBehavior(32475, tBehaviour);
        addItemBehavior(32476, tBehaviour);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 2L),
                new ItemStack(Items.flint, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Lighter_Invar_Empty.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        ItemList.Tool_Lighter_Platinum_Empty.set(
            addItem(
                tLastID = 477,
                "Platinum Lighter (Empty)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Tool_Lighter_Platinum_Used.set(
            addItem(
                tLastID = 478,
                "Platinum Lighter",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                SubTag.INVISIBLE));
        ItemList.Tool_Lighter_Platinum_Full.set(
            addItem(
                tLastID = 479,
                "Platinum Lighter (Full)",
                "A known Prank Master is engraved on it",
                new ItemData(Materials.Platinum, OrePrefixes.plate.mMaterialAmount * 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));

        tBehaviour = new Behaviour_Lighter(
            ItemList.Tool_Lighter_Platinum_Empty.get(1L),
            ItemList.Tool_Lighter_Platinum_Used.get(1L),
            ItemList.Tool_Lighter_Platinum_Full.get(1L),
            1000L);
        addItemBehavior(32478, tBehaviour);
        addItemBehavior(32479, tBehaviour);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Platinum, 2L),
                new ItemStack(Items.flint, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Tool_Lighter_Platinum_Empty.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(256)
            .addTo(assemblerRecipes);

        if (GalacticraftMars.isModLoaded()) {
            ItemList.Ingot_Heavy1
                .set(addItem(tLastID = 462, "Heavy Duty Alloy Ingot T1", "Used to make Heavy Duty Plates T1"));
            ItemList.Ingot_Heavy2
                .set(addItem(tLastID = 463, "Heavy Duty Alloy Ingot T2", "Used to make Heavy Duty Plates T2"));
            ItemList.Ingot_Heavy3
                .set(addItem(tLastID = 464, "Heavy Duty Alloy Ingot T3", "Used to make Heavy Duty Plates T3"));

        }
        ItemList.Ingot_IridiumAlloy.set(
            addItem(
                tLastID = 480,
                "Iridium Alloy Ingot",
                "Used to make Iridium Plates",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)));

        ItemList.Paper_Printed_Pages.set(
            addItem(
                tLastID = 481,
                "Printed Pages",
                "Used to make written Books",
                new ItemData(Materials.Paper, 10886400L),
                new Behaviour_PrintedPages(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Paper_Magic_Empty.set(
            addItem(
                tLastID = 482,
                "Magic Paper",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 1L)));
        ItemList.Paper_Magic_Page.set(
            addItem(
                tLastID = 483,
                "Enchanted Page",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 3628800L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2L)));
        ItemList.Paper_Magic_Pages.set(
            addItem(
                tLastID = 484,
                "Enchanted Pages",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 10886400L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 4L)));
        ItemList.Paper_Punch_Card_Empty.set(
            addItem(
                tLastID = 485,
                "Punch Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L)));
        ItemList.Paper_Punch_Card_Encoded.set(
            addItem(
                tLastID = 486,
                "Punched Card",
                "",
                SubTag.INVISIBLE,
                new ItemData(Materials.Paper, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_01.set(
            addItem(
                tLastID = 487,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_02.set(
            addItem(
                tLastID = 488,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Book_Written_03.set(
            addItem(
                tLastID = 489,
                "Book",
                "",
                new ItemData(Materials.Paper, 10886400L),
                "bookWritten",
                OreDictNames.craftingBook,
                new Behaviour_WrittenBook(),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));

        ItemList.Schematic.set(
            addItem(
                tLastID = 490,
                "Schematic",
                "EMPTY",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 1L)));
        ItemList.Schematic_Crafting.set(
            addItem(
                tLastID = 491,
                "Schematic (Crafting)",
                "Crafts the Programmed Recipe",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_1by1.set(
            addItem(
                tLastID = 495,
                "Schematic (1x1)",
                "Crafts 1 Items as 1x1 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_2by2.set(
            addItem(
                tLastID = 496,
                "Schematic (2x2)",
                "Crafts 4 Items as 2x2 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_3by3.set(
            addItem(
                tLastID = 497,
                "Schematic (3x3)",
                "Crafts 9 Items as 3x3 (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));
        ItemList.Schematic_Dust.set(
            addItem(
                tLastID = 498,
                "Schematic (Dusts)",
                "Combines Dusts (use in Packager)",
                new ItemData(Materials.Steel, 7257600L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_1by1.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "d  ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_2by2.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " d ", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_3by3.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  d", aTextShape, aTextEmptyRow, 'P', ItemList.Schematic });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Schematic_Dust.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { aTextEmptyRow, aTextShape, "  d", 'P', ItemList.Schematic });

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Crafting });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_1by1 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_2by2 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_3by3 });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Schematic.get(1L),
            GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Schematic_Dust });

        ItemList.Battery_Hull_LV.set(
            addItem(
                500,
                "Small Battery Hull",
                "An empty LV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Battery_Hull_MV.set(
            addItem(
                501,
                "Medium Battery Hull",
                "An empty MV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 3L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));
        ItemList.Battery_Hull_HV.set(
            addItem(
                502,
                "Large Battery Hull",
                "An empty HV Battery Hull",
                new ItemData(Materials.BatteryAlloy, OrePrefixes.plate.mMaterialAmount * 9L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 1L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Battery_Hull_LV.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "Cf ", "Ph ", "Ps ", 'P', OrePrefixes.plate.get(Materials.BatteryAlloy), 'C',
                OreDictNames.craftingWireTin });
        // GT_ModHandler.addCraftingRecipe(ItemList.Battery_Hull_MV.get(1L), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new
        // Object[]{"C C", "PPP", "PPP", 'P', OrePrefixes.plate.get(Materials.BatteryAlloy), 'C',
        // OreDictNames.craftingWireCopper});

        // ULV Batteries
        ItemList.Battery_RE_ULV_Tantalum.set(
            addItem(
                tLastID = 499,
                "Tantalum Capacitor",
                "Reusable",
                "batteryULV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L)));
        setElectricStats(32000 + tLastID, 1000L, GT_Values.V[0], 0L, -3L, false);
        registerTieredTooltip(ItemList.Battery_RE_ULV_Tantalum.get(1), ULV);

        // LV Batteries
        ItemList.Battery_SU_LV_SulfuricAcid.set(
            addItem(
                tLastID = 510,
                "Small Acid Battery",
                "Single Use",
                "batteryLV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));
        setElectricStats(32000 + tLastID, 18000L, GT_Values.V[1], 1L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_LV_SulfuricAcid.get(1), LV);

        ItemList.Battery_SU_LV_Mercury.set(
            addItem(
                tLastID = 511,
                "Small Mercury Battery",
                "Single Use",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));
        setElectricStats(32000 + tLastID, 32000L, GT_Values.V[1], 1L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_LV_Mercury.get(1), LV);

        ItemList.Battery_RE_LV_Cadmium.set(
            addItem(
                tLastID = 517,
                "Small Cadmium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));
        setElectricStats(32000 + tLastID, 75000L, GT_Values.V[1], 1L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_LV_Cadmium.get(1), LV);

        ItemList.Battery_RE_LV_Lithium.set(
            addItem(
                tLastID = 518,
                "Small Lithium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));
        setElectricStats(32000 + tLastID, 100000L, GT_Values.V[1], 1L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_LV_Lithium.get(1), LV);

        ItemList.Battery_RE_LV_Sodium.set(
            addItem(
                tLastID = 519,
                "Small Sodium Battery",
                "Reusable",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                "batteryLV"));
        setElectricStats(32000 + tLastID, 50000L, GT_Values.V[1], 1L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_LV_Sodium.get(1), LV);

        // MV Batteries
        ItemList.Battery_SU_MV_SulfuricAcid.set(
            addItem(
                tLastID = 520,
                "Medium Acid Battery",
                "Single Use",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));
        setElectricStats(32000 + tLastID, 72000L, GT_Values.V[2], 2L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_MV_SulfuricAcid.get(1), MV);

        ItemList.Battery_SU_MV_Mercury.set(
            addItem(
                tLastID = 521,
                "Medium Mercury Battery",
                "Single Use",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));
        setElectricStats(32000 + tLastID, 128000L, GT_Values.V[2], 2L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_MV_Mercury.get(1), MV);

        ItemList.Battery_RE_MV_Cadmium.set(
            addItem(
                tLastID = 527,
                "Medium Cadmium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));
        setElectricStats(32000 + tLastID, 300000L, GT_Values.V[2], 2L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_MV_Cadmium.get(1), MV);

        ItemList.Battery_RE_MV_Lithium.set(
            addItem(
                tLastID = 528,
                "Medium Lithium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));
        setElectricStats(32000 + tLastID, 400000L, GT_Values.V[2], 2L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_MV_Lithium.get(1), MV);

        ItemList.Battery_RE_MV_Sodium.set(
            addItem(
                tLastID = 529,
                "Medium Sodium Battery",
                "Reusable",
                "batteryMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L)));
        setElectricStats(32000 + tLastID, 200000L, GT_Values.V[2], 2L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_MV_Sodium.get(1), MV);

        // HV Batteries
        ItemList.Battery_SU_HV_SulfuricAcid.set(
            addItem(
                tLastID = 530,
                "Large Acid Battery",
                "Single Use",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L)));
        setElectricStats(32000 + tLastID, 288000L, GT_Values.V[3], 3L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_HV_SulfuricAcid.get(1), HV);

        ItemList.Battery_SU_HV_Mercury.set(
            addItem(
                tLastID = 531,
                "Large Mercury Battery",
                "Single Use",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L)));
        setElectricStats(32000 + tLastID, 512000L, GT_Values.V[3], 3L, -2L, true);
        registerTieredTooltip(ItemList.Battery_SU_HV_Mercury.get(1), HV);

        ItemList.Battery_RE_HV_Cadmium.set(
            addItem(
                tLastID = 537,
                "Large Cadmium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));
        setElectricStats(32000 + tLastID, 1200000L, GT_Values.V[3], 3L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_HV_Cadmium.get(1), HV);

        ItemList.Battery_RE_HV_Lithium.set(
            addItem(
                tLastID = 538,
                "Large Lithium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));
        setElectricStats(32000 + tLastID, 1600000L, GT_Values.V[3], 3L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_HV_Lithium.get(1), HV);

        ItemList.Battery_RE_HV_Sodium.set(
            addItem(
                tLastID = 539,
                "Large Sodium Battery",
                "Reusable",
                "batteryHV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L)));
        setElectricStats(32000 + tLastID, 800000L, GT_Values.V[3], 3L, -3L, true);
        registerTieredTooltip(ItemList.Battery_RE_HV_Sodium.get(1), HV);

        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_LV_SulfuricAcid.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_LV_Mercury.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_MV_SulfuricAcid.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_MV_Mercury.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler
            .addExtractionRecipe(ItemList.Battery_SU_HV_SulfuricAcid.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_SU_HV_Mercury.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Cadmium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Lithium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_LV_Sodium.get(1L), ItemList.Battery_Hull_LV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Cadmium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Lithium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_MV_Sodium.get(1L), ItemList.Battery_Hull_MV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Cadmium.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Lithium.get(1L), ItemList.Battery_Hull_HV.get(1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Battery_RE_HV_Sodium.get(1L), ItemList.Battery_Hull_HV.get(1L));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Cadmium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Lithium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 2L),
                ItemList.Battery_Hull_LV.get(1L))
            .itemOutputs(ItemList.Battery_RE_LV_Sodium.get(1L))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Cadmium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Lithium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 8L),
                ItemList.Battery_Hull_MV.get(1L))
            .itemOutputs(ItemList.Battery_RE_MV_Sodium.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cadmium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Cadmium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Lithium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 32L),
                ItemList.Battery_Hull_HV.get(1L))
            .itemOutputs(ItemList.Battery_RE_HV_Sodium.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(cannerRecipes);

        // IV Battery
        ItemList.Energy_LapotronicOrb.set(
            addItem(
                tLastID = 597,
                "Lapotronic Energy Orb",
                "Reusable battery",
                "batteryIV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));
        setElectricStats(32000 + tLastID, 100000000L, GT_Values.V[5], 5L, -3L, true);
        registerTieredTooltip(ItemList.Energy_LapotronicOrb.get(1), IV);

        // ZPM Module
        ItemList.ZPM.set(
            addItem(
                tLastID = 598,
                "Zero Point Module",
                "Single use battery",
                "batteryZPM",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, 2000000000000L, GT_Values.V[7], 7L, -2L, true);
        registerTieredTooltip(ItemList.ZPM.get(1), ZPM);

        // LuV Lapotron orb cluster battery
        ItemList.Energy_LapotronicOrb2.set(
            addItem(
                tLastID = 599,
                "Lapotronic Energy Orb Cluster",
                "Reusable battery",
                "batteryLuV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));
        setElectricStats(32000 + tLastID, 1000000000L, GT_Values.V[6], 6L, -3L, true);
        registerTieredTooltip(ItemList.Energy_LapotronicOrb2.get(1), LuV);

        // UV Battery
        ItemList.ZPM2.set(
            addItem(
                tLastID = 605,
                "Ultimate Battery",
                "Fill this to win minecraft",
                "batteryUV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, Long.MAX_VALUE, GT_Values.V[8], 8L, -3L, true);
        registerTieredTooltip(ItemList.ZPM2.get(1), UV);

        // UMV Battery
        ItemList.ZPM3.set(
            addItem(
                tLastID = 609,
                "Really Ultimate Battery",
                "Fill this to be way older",
                "batteryUMV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, Long.MAX_VALUE, GT_Values.V[12], 12L, -3L, true);
        registerTieredTooltip(ItemList.ZPM3.get(1), UMV);

        // UXV Battery
        ItemList.ZPM4.set(
            addItem(
                tLastID = 594,
                "Extremely Ultimate Battery",
                "Fill this to be older",
                "batteryUXV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, Long.MAX_VALUE, GT_Values.V[13], 13L, -3L, true);
        registerTieredTooltip(ItemList.ZPM4.get(1), UXV);

        // MAX Battery
        ItemList.ZPM5.set(
            addItem(
                tLastID = 145,
                "Insanely Ultimate Battery",
                "Fill this for fun",
                "batteryMAX",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, Long.MAX_VALUE, GT_Values.V[14], 14L, -3L, true);
        registerTieredTooltip(ItemList.ZPM5.get(1), MAX);

        // ERROR Battery
        ItemList.ZPM6.set(
            addItem(
                tLastID = 146,
                "Mega Ultimate Battery",
                "Fill the capacitor to reach enlightenment",
                "batteryERV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, Long.MAX_VALUE, GT_Values.V[15], 15L, -3L, true);
        registerTieredTooltip(ItemList.ZPM6.get(1), ERV);

        // ZPM Cluster
        ItemList.Energy_Module.set(
            addItem(
                tLastID = 736,
                "Energy Module",
                "Reusable battery",
                "batteryZPM",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                OrePrefixes.battery.get(Materials.Ultimate)));
        setElectricStats(32000 + tLastID, 10000000000L, GT_Values.V[7], 7L, -3L, true);
        registerTieredTooltip(ItemList.Energy_Module.get(1), ZPM);

        // UV Cluster
        ItemList.Energy_Cluster.set(
            addItem(
                tLastID = 737,
                "Energy Cluster",
                "Reusable battery",
                "batteryUV",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L)));
        setElectricStats(32000 + tLastID, 100000000000L, GT_Values.V[8], 8L, -3L, true);
        registerTieredTooltip(ItemList.Energy_Cluster.get(1), UV);

        // UIV, UMV, UXV and MAX component textures backported from gregicality.
        ItemList.Electric_Motor_LV.set(
            addItem(
                600,
                "Electric Motor (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.Electric_Motor_MV.set(
            addItem(
                601,
                "Electric Motor (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L)));
        ItemList.Electric_Motor_HV.set(
            addItem(
                602,
                "Electric Motor (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L)));
        ItemList.Electric_Motor_EV.set(
            addItem(
                603,
                "Electric Motor (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L)));
        ItemList.Electric_Motor_IV.set(
            addItem(
                604,
                "Electric Motor (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L)));
        ItemList.Electric_Motor_LuV.set(
            addItem(
                606,
                "Electric Motor (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L)));
        ItemList.Electric_Motor_ZPM.set(
            addItem(
                607,
                "Electric Motor (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L)));
        ItemList.Electric_Motor_UV.set(
            addItem(
                608,
                "Electric Motor (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L)));
        ItemList.Electric_Motor_UHV.set(
            addItem(
                596,
                "Electric Motor (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L)));
        ItemList.Electric_Motor_UEV.set(
            addItem(
                595,
                "Electric Motor (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UIV.set(
            addItem(
                17,
                "Electric Motor (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UMV.set(
            addItem(
                18,
                "Electric Motor (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_UXV.set(
            addItem(
                19,
                "Electric Motor (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Motor_MAX.set(
            addItem(
                20,
                "Electric Motor (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.IronMagnetic), 'R',
                OrePrefixes.stick.get(Materials.AnyIron), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Steel), 'W', OrePrefixes.wireGt01.get(Materials.AnyCopper), 'C',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Aluminium), 'W', OrePrefixes.wireGt02.get(Materials.Cupronickel), 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.SteelMagnetic), 'R',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'W', OrePrefixes.wireGt04.get(Materials.Electrum), 'C',
                OrePrefixes.cableGt02.get(Materials.Silver) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.Titanium), 'W', OrePrefixes.wireGt04.get(Materials.BlackSteel), 'C',
                OrePrefixes.cableGt02.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Motor_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CWR", "WIW", "RWC", 'I', OrePrefixes.stick.get(Materials.NeodymiumMagnetic), 'R',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'W', OrePrefixes.wireGt04.get(Materials.Graphene), 'C',
                OrePrefixes.cableGt02.get(Materials.Tungsten) });

        ItemList.ElectronicsLump.set(
            addItem(
                414,
                "Lump of Electronics",
                "How did they even produce this?",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.Tesseract.set(
            addItem(
                415,
                "Raw Tesseract",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.GigaChad.set(
            addItem(
                416,
                "Giga Chad Token",
                "You are worthy",
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1000L)));
        ItemList.EnergisedTesseract.set(
            addItem(
                417,
                "Energised Tesseract",
                "Higher dimensional engineering",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));

        ItemList.Electric_Piston_LV.set(
            addItem(
                640,
                "Electric Piston (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L)));
        ItemList.Electric_Piston_MV.set(
            addItem(
                641,
                "Electric Piston (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L)));
        ItemList.Electric_Piston_HV.set(
            addItem(
                642,
                "Electric Piston (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L)));
        ItemList.Electric_Piston_EV.set(
            addItem(
                643,
                "Electric Piston (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L)));
        ItemList.Electric_Piston_IV.set(
            addItem(
                644,
                "Electric Piston (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L)));
        ItemList.Electric_Piston_LuV.set(
            addItem(
                645,
                "Electric Piston (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L)));
        ItemList.Electric_Piston_ZPM.set(
            addItem(
                646,
                "Electric Piston (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L)));
        ItemList.Electric_Piston_UV.set(
            addItem(
                647,
                "Electric Piston (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L)));
        ItemList.Electric_Piston_UHV.set(
            addItem(
                648,
                "Electric Piston (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L)));
        ItemList.Electric_Piston_UEV.set(
            addItem(
                649,
                "Electric Piston (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UIV.set(
            addItem(
                21,
                "Electric Piston (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UMV.set(
            addItem(
                22,
                "Electric Piston (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_UXV.set(
            addItem(
                23,
                "Electric Piston (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));
        ItemList.Electric_Piston_MAX.set(
            addItem(
                24,
                "Electric Piston (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Steel), 'S',
                OrePrefixes.stick.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium), 'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.Titanium), 'S',
                OrePrefixes.stick.get(Materials.Titanium), 'G', OrePrefixes.gearGtSmall.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Piston_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PPP", "CSS", "CMG", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'S',
                OrePrefixes.stick.get(Materials.TungstenSteel), 'G',
                OrePrefixes.gearGtSmall.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        ItemList.Electric_Pump_LV.set(
            addItem(
                610,
                "Electric Pump (LV)",
                GT_Utility.formatNumbers(32) + PartCoverText + GT_Utility.formatNumbers(32 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Electric_Pump_MV.set(
            addItem(
                611,
                "Electric Pump (MV)",
                GT_Utility.formatNumbers(128) + PartCoverText + GT_Utility.formatNumbers(128 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Electric_Pump_HV.set(
            addItem(
                612,
                "Electric Pump (HV)",
                GT_Utility.formatNumbers(512) + PartCoverText + GT_Utility.formatNumbers(512 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Electric_Pump_EV.set(
            addItem(
                613,
                "Electric Pump (EV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Electric_Pump_IV.set(
            addItem(
                614,
                "Electric Pump (IV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));
        ItemList.Electric_Pump_LuV.set(
            addItem(
                615,
                "Electric Pump (LuV)",
                GT_Utility.formatNumbers(32768) + PartCoverText + GT_Utility.formatNumbers(32768 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 32L)));
        ItemList.Electric_Pump_ZPM.set(
            addItem(
                616,
                "Electric Pump (ZPM)",
                GT_Utility.formatNumbers(131072) + PartCoverText
                    + GT_Utility.formatNumbers(131072 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 64L)));
        ItemList.Electric_Pump_UV.set(
            addItem(
                617,
                "Electric Pump (UV)",
                GT_Utility.formatNumbers(524288) + PartCoverText
                    + GT_Utility.formatNumbers(524288 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128L)));
        ItemList.Electric_Pump_UHV.set(
            addItem(
                618,
                "Electric Pump (UHV)",
                GT_Utility.formatNumbers(1048576) + PartCoverText
                    + GT_Utility.formatNumbers(1048576 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 256L)));
        ItemList.Electric_Pump_UEV.set(
            addItem(
                619,
                "Electric Pump (UEV)",
                GT_Utility.formatNumbers(2097152) + PartCoverText
                    + GT_Utility.formatNumbers(2097152 * 20)
                    + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UIV.set(
            addItem(
                25,
                "Electric Pump (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UMV.set(
            addItem(
                26,
                "Electric Pump (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_UXV.set(
            addItem(
                27,
                "Electric Pump (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));
        ItemList.Electric_Pump_MAX.set(
            addItem(
                28,
                "Electric Pump (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 512L)));

        GregTech_API.registerCover(
            ItemList.Electric_Pump_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(32, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(128, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(512, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(2048, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(8192, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(32768, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(131072, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(524288, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(1048576, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.Electric_Pump_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_Pump(2097152, TextureFactory.of(OVERLAY_PUMP)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_LV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Tin), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'P',
                OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_MV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Bronze), 'S',
                OrePrefixes.screw.get(Materials.Bronze), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P',
                OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_HV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.Steel), 'S',
                OrePrefixes.screw.get(Materials.Steel), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'P',
                OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_EV, 'O',
                OrePrefixes.ring.get(Materials.AnyRubber), 'X', OrePrefixes.rotor.get(Materials.StainlessSteel), 'S',
                OrePrefixes.screw.get(Materials.StainlessSteel), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium),
                'P', OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Electric_Pump_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SXO", "dPw", "OMW", 'M', ItemList.Electric_Motor_IV, 'O',
                OrePrefixes.ring.get(Materials.AnySyntheticRubber), 'X', OrePrefixes.rotor.get(Materials.TungstenSteel),
                'S', OrePrefixes.screw.get(Materials.TungstenSteel), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten),
                'P', OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });

        ItemList.Steam_Valve_LV.set(
            addItem(
                620,
                "Steam Valve (LV)",
                GT_Utility.formatNumbers(1024) + PartCoverText + GT_Utility.formatNumbers(1024 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Steam_Valve_MV.set(
            addItem(
                621,
                "Steam Valve (MV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Steam_Valve_HV.set(
            addItem(
                622,
                "Steam Valve (HV)",
                GT_Utility.formatNumbers(4096) + PartCoverText + GT_Utility.formatNumbers(4096 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Steam_Valve_EV.set(
            addItem(
                623,
                "Steam Valve (EV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Steam_Valve_IV.set(
            addItem(
                624,
                "Steam Valve (IV)",
                GT_Utility.formatNumbers(16384) + PartCoverText + GT_Utility.formatNumbers(16384 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));

        GregTech_API.registerCover(
            ItemList.Steam_Valve_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(1024, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(2048, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(4096, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(8192, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Valve_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamValve(16384, TextureFactory.of(OVERLAY_VALVE)));

        ItemList.FluidRegulator_LV
            .set(addItem(tLastID = 660, "Fluid Regulator (LV)", FRText1 + GT_Utility.formatNumbers(640) + FRText2));
        ItemList.FluidRegulator_MV
            .set(addItem(tLastID = 661, "Fluid Regulator (MV)", FRText1 + GT_Utility.formatNumbers(2560) + FRText2));
        ItemList.FluidRegulator_HV
            .set(addItem(tLastID = 662, "Fluid Regulator (HV)", FRText1 + GT_Utility.formatNumbers(10240) + FRText2));
        ItemList.FluidRegulator_EV
            .set(addItem(tLastID = 663, "Fluid Regulator (EV)", FRText1 + GT_Utility.formatNumbers(40960) + FRText2));
        ItemList.FluidRegulator_IV
            .set(addItem(tLastID = 664, "Fluid Regulator (IV)", FRText1 + GT_Utility.formatNumbers(163840) + FRText2));
        ItemList.FluidRegulator_LuV
            .set(addItem(tLastID = 665, "Fluid Regulator (LuV)", FRText1 + GT_Utility.formatNumbers(655360) + FRText2));
        ItemList.FluidRegulator_ZPM.set(
            addItem(tLastID = 666, "Fluid Regulator (ZPM)", FRText1 + GT_Utility.formatNumbers(2621440) + FRText2));
        ItemList.FluidRegulator_UV.set(
            addItem(tLastID = 667, "Fluid Regulator (UV)", FRText1 + GT_Utility.formatNumbers(10485760) + FRText2));

        GregTech_API.registerCover(
            ItemList.FluidRegulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(32, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(128, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(512, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(2048, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(8192, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(32768, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(131072, TextureFactory.of(OVERLAY_PUMP)));
        GregTech_API.registerCover(
            ItemList.FluidRegulator_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_PUMP)),
            new GT_Cover_FluidRegulator(524288, TextureFactory.of(OVERLAY_PUMP)));

        ItemList.FluidFilter
            .set(addItem(669, "Fluid Filter Cover", "Set with Fluid Container to only accept one Fluid Type"));
        GregTech_API.registerCover(
            ItemList.FluidFilter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_Fluidfilter(TextureFactory.of(OVERLAY_SHUTTER)));
        ItemList.ItemFilter_Export.set(
            addItem(
                270,
                "Item Filter Cover (Export)",
                "Right click with an item to set filter (Only supports Export Mode)"));
        GregTech_API.registerCover(
            ItemList.ItemFilter_Export.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_ItemFilter(true, TextureFactory.of(OVERLAY_CONVEYOR)));
        ItemList.ItemFilter_Import.set(
            addItem(
                271,
                "Item Filter Cover (Import)",
                "Right click with an item to set filter (Only supports Import Mode)"));
        GregTech_API.registerCover(
            ItemList.ItemFilter_Import.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_ItemFilter(false, TextureFactory.of(OVERLAY_CONVEYOR)));
        ItemList.Cover_FluidLimiter
            .set(addItem(272, "Fluid Limiter Cover", "Limits fluid input depending on fill level"));
        GregTech_API.registerCover(
            ItemList.Cover_FluidLimiter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_FluidLimiter(TextureFactory.of(OVERLAY_SHUTTER)));

        ItemList.Conveyor_Module_LV.set(
            addItem(
                630,
                "Conveyor Module (LV)",
                "1 stack every 20 secs (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));
        ItemList.Conveyor_Module_MV.set(
            addItem(
                631,
                "Conveyor Module (MV)",
                "1 stack every 5 secs (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L)));
        ItemList.Conveyor_Module_HV.set(
            addItem(
                632,
                "Conveyor Module (HV)",
                "1 stack every 1 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L)));
        ItemList.Conveyor_Module_EV.set(
            addItem(
                633,
                "Conveyor Module (EV)",
                "1 stack every 1/5 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L)));
        ItemList.Conveyor_Module_IV.set(
            addItem(
                634,
                "Conveyor Module (IV)",
                "1 stack every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L)));
        ItemList.Conveyor_Module_LuV.set(
            addItem(
                635,
                "Conveyor Module (LuV)",
                "2 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 32L)));
        ItemList.Conveyor_Module_ZPM.set(
            addItem(
                636,
                "Conveyor Module (ZPM)",
                "4 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 64L)));
        ItemList.Conveyor_Module_UV.set(
            addItem(
                637,
                "Conveyor Module (UV)",
                "8 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 128L)));
        ItemList.Conveyor_Module_UHV.set(
            addItem(
                638,
                "Conveyor Module (UHV)",
                "16 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 256L)));
        ItemList.Conveyor_Module_UEV.set(
            addItem(
                639,
                "Conveyor Module (UEV)",
                "32 stacks every 1/20 sec (as Cover)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UIV.set(
            addItem(
                29,
                "Conveyor Module (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UMV.set(
            addItem(
                30,
                "Conveyor Module (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_UXV.set(
            addItem(
                31,
                "Conveyor Module (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));
        ItemList.Conveyor_Module_MAX.set(
            addItem(
                32,
                "Conveyor Module (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_LV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_MV, 'C',
                OrePrefixes.cableGt01.get(Materials.AnyCopper), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_HV, 'C',
                OrePrefixes.cableGt01.get(Materials.Gold), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_EV, 'C',
                OrePrefixes.cableGt01.get(Materials.Aluminium), 'R', OrePrefixes.plate.get(Materials.AnyRubber) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Conveyor_Module_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "RRR", "MCM", "RRR", 'M', ItemList.Electric_Motor_IV, 'C',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'R',
                OrePrefixes.plate.get(Materials.AnySyntheticRubber) });

        GregTech_API.registerCover(
            ItemList.Conveyor_Module_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(400, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(100, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(20, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(4, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 1, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_LuV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[6][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 2, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_ZPM.get(1L),
            TextureFactory.of(MACHINE_CASINGS[7][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 4, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[8][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 8, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UHV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[9][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 16, TextureFactory.of(OVERLAY_CONVEYOR)));
        GregTech_API.registerCover(
            ItemList.Conveyor_Module_UEV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[10][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new GT_Cover_Conveyor(1, 32, TextureFactory.of(OVERLAY_CONVEYOR)));

        ItemList.Robot_Arm_LV.set(
            addItem(
                650,
                "Robot Arm (LV)",
                "1 stack every 20 secs (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L)));
        ItemList.Robot_Arm_MV.set(
            addItem(
                651,
                "Robot Arm (MV)",
                "1 stack every 5 secs (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L)));
        ItemList.Robot_Arm_HV.set(
            addItem(
                652,
                "Robot Arm (HV)",
                "1 stack every 1 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 4L)));
        ItemList.Robot_Arm_EV.set(
            addItem(
                653,
                "Robot Arm (EV)",
                "1 stack every 1/5 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 8L)));
        ItemList.Robot_Arm_IV.set(
            addItem(
                654,
                "Robot Arm (IV)",
                "1 stack every 1/20 sec (as Cover)/n " + RAText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 16L)));
        ItemList.Robot_Arm_LuV.set(
            addItem(
                655,
                "Robot Arm (LuV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 32L)));
        ItemList.Robot_Arm_ZPM.set(
            addItem(
                656,
                "Robot Arm (ZPM)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 64L)));
        ItemList.Robot_Arm_UV.set(
            addItem(
                657,
                "Robot Arm (UV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 128L)));
        ItemList.Robot_Arm_UHV.set(
            addItem(
                658,
                "Robot Arm (UHV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 256L)));
        ItemList.Robot_Arm_UEV.set(
            addItem(
                659,
                "Robot Arm (UEV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UIV.set(
            addItem(
                33,
                "Robot Arm (UIV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UMV.set(
            addItem(
                34,
                "Robot Arm (UMV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_UXV.set(
            addItem(
                35,
                "Robot Arm (UXV)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));
        ItemList.Robot_Arm_MAX.set(
            addItem(
                36,
                "Robot Arm (MAX)",
                PartNotCoverText,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Steel), 'M',
                ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                OrePrefixes.circuit.get(Materials.Basic), 'C', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Aluminium), 'M',
                ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                OrePrefixes.circuit.get(Materials.Good), 'C', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.StainlessSteel), 'M',
                ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                OrePrefixes.circuit.get(Materials.Advanced), 'C', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.Titanium), 'M',
                ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                OrePrefixes.circuit.get(Materials.Data), 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Robot_Arm_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "CCC", "MSM", "PES", 'S', OrePrefixes.stick.get(Materials.TungstenSteel), 'M',
                ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                OrePrefixes.circuit.get(Materials.Elite), 'C', OrePrefixes.cableGt01.get(Materials.Tungsten) });

        GregTech_API.registerCover(
            ItemList.Robot_Arm_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(400, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(100, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(20, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(4, TextureFactory.of(OVERLAY_ARM)));
        GregTech_API.registerCover(
            ItemList.Robot_Arm_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_ARM)),
            new GT_Cover_Arm(1, TextureFactory.of(OVERLAY_ARM)));

        ItemList.QuantumEye.set(addItem(tLastID = 724, "Quantum Eye", "Improved Ender Eye"));
        ItemList.QuantumStar.set(addItem(tLastID = 725, "Quantum Star", "Improved Nether Star"));
        ItemList.Gravistar.set(addItem(tLastID = 726, "Gravi Star", "Ultimate Nether Star"));

        ItemList.Emitter_LV.set(
            addItem(
                680,
                "Emitter (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 1L)));
        ItemList.Emitter_MV.set(
            addItem(
                681,
                "Emitter (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 2L)));
        ItemList.Emitter_HV.set(
            addItem(
                682,
                "Emitter (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 4L)));
        ItemList.Emitter_EV.set(
            addItem(
                683,
                "Emitter (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 8L)));
        ItemList.Emitter_IV.set(
            addItem(
                684,
                "Emitter (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 16L)));
        ItemList.Emitter_LuV.set(
            addItem(
                685,
                "Emitter (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 32L)));
        ItemList.Emitter_ZPM.set(
            addItem(
                686,
                "Emitter (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 64L)));
        ItemList.Emitter_UV.set(
            addItem(
                687,
                "Emitter (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 128L)));
        ItemList.Emitter_UHV.set(
            addItem(
                688,
                "Emitter (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 256L)));
        ItemList.Emitter_UEV.set(
            addItem(
                689,
                "Emitter (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UIV.set(
            addItem(
                37,
                "Emitter (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UMV.set(
            addItem(
                38,
                "Emitter (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_UXV.set(
            addItem(
                39,
                "Emitter (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));
        ItemList.Emitter_MAX.set(
            addItem(
                40,
                "Emitter (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderPearl), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Emitter_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "SSC", "WQS", "CWS", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten) });

        ItemList.Sensor_LV.set(
            addItem(
                690,
                "Sensor (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 1L)));
        ItemList.Sensor_MV.set(
            addItem(
                691,
                "Sensor (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L)));
        ItemList.Sensor_HV.set(
            addItem(
                692,
                "Sensor (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
        ItemList.Sensor_EV.set(
            addItem(
                693,
                "Sensor (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 8L)));
        ItemList.Sensor_IV.set(
            addItem(
                694,
                "Sensor (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 16L)));
        ItemList.Sensor_LuV.set(
            addItem(
                695,
                "Sensor (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 32L)));
        ItemList.Sensor_ZPM.set(
            addItem(
                696,
                "Sensor (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 64L)));
        ItemList.Sensor_UV.set(
            addItem(
                697,
                "Sensor (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 128L)));
        ItemList.Sensor_UHV.set(
            addItem(
                698,
                "Sensor (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 256L)));
        ItemList.Sensor_UEV.set(
            addItem(
                699,
                "Sensor (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UIV.set(
            addItem(
                41,
                "Sensor (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UMV.set(
            addItem(
                42,
                "Sensor (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_UXV.set(
            addItem(
                43,
                "Sensor (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));
        ItemList.Sensor_MAX.set(
            addItem(
                44,
                "Sensor (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 512L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_LV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.CertusQuartz), 'S',
                OrePrefixes.stick.get(Materials.Brass), 'P', OrePrefixes.plate.get(Materials.Steel), 'C',
                OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_MV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gemFlawless.get(Materials.Emerald), 'S',
                OrePrefixes.stick.get(Materials.Electrum), 'P', OrePrefixes.plate.get(Materials.Aluminium), 'C',
                OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_HV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', OrePrefixes.gem.get(Materials.EnderEye), 'S',
                OrePrefixes.stick.get(Materials.Chrome), 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'C',
                OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumEye, 'S',
                OrePrefixes.stick.get(Materials.Platinum), 'P', OrePrefixes.plate.get(Materials.Titanium), 'C',
                OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Sensor_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "P Q", "PS ", "CPP", 'Q', ItemList.QuantumStar, 'S',
                OrePrefixes.stick.get(Materials.Iridium), 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'C',
                OrePrefixes.circuit.get(Materials.Elite) });

        ItemList.Field_Generator_LV.set(
            addItem(
                670,
                "Field Generator (LV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 1L)));
        ItemList.Field_Generator_MV.set(
            addItem(
                671,
                "Field Generator (MV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 2L)));
        ItemList.Field_Generator_HV.set(
            addItem(
                672,
                "Field Generator (HV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L)));
        ItemList.Field_Generator_EV.set(
            addItem(
                673,
                "Field Generator (EV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 8L)));
        ItemList.Field_Generator_IV.set(
            addItem(
                674,
                "Field Generator (IV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 16L)));
        ItemList.Field_Generator_LuV.set(
            addItem(
                675,
                "Field Generator (LuV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 32L)));
        ItemList.Field_Generator_ZPM.set(
            addItem(
                676,
                "Field Generator (ZPM)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 64L)));
        ItemList.Field_Generator_UV.set(
            addItem(
                677,
                "Field Generator (UV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 128L)));
        ItemList.Field_Generator_UHV.set(
            addItem(
                678,
                "Field Generator (UHV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 256L)));
        ItemList.Field_Generator_UEV.set(
            addItem(
                679,
                "Field Generator (UEV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UIV.set(
            addItem(
                45,
                "Field Generator (UIV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UMV.set(
            addItem(
                46,
                "Field Generator (UMV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_UXV.set(
            addItem(
                47,
                "Field Generator (UXV)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));
        ItemList.Field_Generator_MAX.set(
            addItem(
                48,
                "Field Generator (MAX)",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1024L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 512L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 512L)));

        ItemList.StableAdhesive.set(
            addItem(
                427,
                "Hyper-Stable Self-Healing Adhesive",
                "Complete and selective adhesion, even when torn or damaged",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 30L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 20L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 10L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VINCULUM, 5L)));
        ItemList.SuperconductorComposite.set(
            addItem(
                428,
                "Superconductor Rare-Earth Composite",
                "Zero resistance to electrical and quantum flow, regardless of temperature",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 50L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 25L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 15L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 10L)));
        ItemList.NaquadriaSupersolid.set(
            addItem(
                429,
                "Black Body Naquadria Supersolid",
                "Flows like a fluid and reflects nothing, perfect absorption and transfer",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 100L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 60L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 40L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 20L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 10L)));

        // Circuits ULV - LuV.
        ItemList.Circuit_Primitive.set(
            addItem(
                tLastID = 700,
                "Vacuum Tube",
                "A very simple Circuit",
                OrePrefixes.circuit.get(Materials.Primitive),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Primitive.get(1), ULV);
        ItemList.Circuit_Basic.set(
            addItem(
                tLastID = 701,
                "Integrated Logic Circuit",
                "A Basic Circuit",
                OrePrefixes.circuit.get(Materials.Basic),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Basic.get(1), LV);
        ItemList.Circuit_Good.set(
            addItem(
                tLastID = 702,
                "Good Electronic Circuit",
                "A Good Circuit",
                OrePrefixes.circuit.get(Materials.Good),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Good.get(1), MV);
        ItemList.Circuit_Advanced.set(
            addItem(
                tLastID = 703,
                "Processor Assembly",
                "An Advanced Circuit",
                OrePrefixes.circuit.get(Materials.Advanced),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Advanced.get(1), HV);
        ItemList.Circuit_Data.set(
            addItem(
                tLastID = 704,
                "Workstation",
                "An Extreme Circuit",
                OrePrefixes.circuit.get(Materials.Data),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Data.get(1), EV);
        ItemList.Circuit_Elite.set(
            addItem(
                tLastID = 705,
                "Mainframe",
                "An Elite Circuit",
                OrePrefixes.circuit.get(Materials.Elite),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Elite.get(1), IV);
        ItemList.Circuit_Master.set(
            addItem(
                tLastID = 706,
                "Nanoprocessor Mainframe",
                "A Master Circuit",
                OrePrefixes.circuit.get(Materials.Master),
                SubTag.NO_UNIFICATION));
        registerTieredTooltip(ItemList.Circuit_Master.get(1), LuV);

        // Backwards compatibility.
        ItemList.Circuit_Parts_Vacuum_Tube.set(ItemList.Circuit_Primitive.get(1));
        ItemList.Circuit_Computer.set(ItemList.Circuit_Advanced.get(1));

        ItemList.Tool_DataOrb.set(
            addItem(
                tLastID = 707,
                "Data Orb",
                "A High Capacity Data Storage",
                SubTag.NO_UNIFICATION,
                new Behaviour_DataOrb()));
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataOrb.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataOrb.get(1L) });
        ItemList.Tool_DataStick.set(
            addItem(
                tLastID = 708,
                "Data Stick",
                "A Low Capacity Data Storage",
                SubTag.NO_UNIFICATION,
                new Behaviour_DataStick()));
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Tool_DataStick.get(1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ItemList.Tool_DataStick.get(1L) });
        ItemList.Tool_Cover_Copy_Paste.set(
            addItem(
                tLastID = 709,
                "Cover Copy/Paste tool",
                "Set Cover Massively.",
                Behaviour_Cover_Tool.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 6L)));
        setElectricStats(32000 + tLastID, 400000L, GT_Values.V[2], 2L, -1L, false);

        ItemList.Circuit_Board_Basic.set(addItem(tLastID = 710, "Coated Circuit Board", "A Basic Board"));
        ItemList.Circuit_Board_Coated.set(ItemList.Circuit_Board_Basic.get(1));
        ItemList.Circuit_Board_Advanced.set(addItem(tLastID = 711, "Epoxy Circuit Board", "An Advanced Board"));
        ItemList.Circuit_Board_Epoxy.set(ItemList.Circuit_Board_Advanced.get(1));
        ItemList.Circuit_Board_Elite
            .set(addItem(tLastID = 712, "Multilayer Fiber-Reinforced Circuit Board", "An Elite Board"));
        ItemList.Circuit_Board_Multifiberglass.set(ItemList.Circuit_Board_Elite.get(1));
        ItemList.Circuit_Parts_Crystal_Chip_Elite
            .set(addItem(tLastID = 713, "Engraved Crystal Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Crystal_Chip_Master
            .set(addItem(tLastID = 714, "Engraved Lapotron Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Crystal_Chip_Wetware
            .set(addItem(tLastID = 668, "Living Crystal Chip", "Needed for Circuits"));
        ItemList.Circuit_Parts_Advanced.set(addItem(tLastID = 715, "Diode", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Diode.set(ItemList.Circuit_Parts_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Basic.set(addItem(tLastID = 716, "Resistor", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Resistor.set(ItemList.Circuit_Parts_Wiring_Basic.get(1));
        ItemList.Circuit_Parts_Wiring_Advanced.set(addItem(tLastID = 717, "Transistor", "Basic Electronic Component"));
        ItemList.Circuit_Parts_Transistor.set(ItemList.Circuit_Parts_Wiring_Advanced.get(1));
        ItemList.Circuit_Parts_Wiring_Elite.set(addItem(tLastID = 718, "Capacitor", "Electronic Component"));
        ItemList.Circuit_Parts_Capacitor.set(ItemList.Circuit_Parts_Wiring_Elite.get(1));
        ItemList.Empty_Board_Basic.set(addItem(tLastID = 719, "Phenolic Circuit Board", "A Good Board"));
        ItemList.Circuit_Board_Phenolic.set(ItemList.Empty_Board_Basic.get(1));
        ItemList.Empty_Board_Elite.set(addItem(tLastID = 720, "Fiber-Reinforced Circuit Board", "An Extreme Board"));
        ItemList.Circuit_Board_Fiberglass.set(ItemList.Empty_Board_Elite.get(1));

        ItemList.Component_Sawblade_Diamond.set(
            addItem(
                tLastID = 721,
                "Diamond Sawblade",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L),
                OreDictNames.craftingDiamondBlade));
        ItemList.Component_Grinder_Diamond.set(
            addItem(
                tLastID = 722,
                "Diamond Grinding Head",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 6L),
                OreDictNames.craftingGrinder));
        ItemList.Component_Grinder_Tungsten.set(
            addItem(
                tLastID = 723,
                "Tungsten Grinding Head",
                "",
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 6L),
                OreDictNames.craftingGrinder));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Sawblade_Diamond.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { " D ", "DGD", " D ", 'D', OrePrefixes.dustSmall.get(Materials.Diamond), 'G',
                OrePrefixes.gearGt.get(Materials.CobaltBrass) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Diamond.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "DSD", "SIS", "DSD", 'I', OrePrefixes.gem.get(Materials.Diamond), 'D',
                OrePrefixes.dust.get(Materials.Diamond), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
            ItemList.Component_Grinder_Tungsten.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "TST", "SIS", "TST", 'I', OreDictNames.craftingIndustrialDiamond, 'T',
                OrePrefixes.plate.get(Materials.Tungsten), 'S', OrePrefixes.plateDouble.get(Materials.Steel) });

        ItemList.Upgrade_Muffler.set(
            addItem(
                tLastID = 727,
                "Muffler Upgrade",
                "Makes Machines silent",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L)));
        ItemList.Upgrade_Lock.set(
            addItem(
                tLastID = 728,
                "Lock Upgrade",
                "Protects your Machines",
                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 4L)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Muffler.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Upgrade_Lock.get(1L))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        ItemList.Component_Filter.set(
            addItem(
                tLastID = 729,
                "Item Filter",
                "",
                new ItemData(Materials.Zinc, OrePrefixes.foil.mMaterialAmount * 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                OreDictNames.craftingFilter));

        ItemList.Cover_Controller.set(
            addItem(
                tLastID = 730,
                "Machine Controller Cover",
                "Turns Machines ON/OFF",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_ActivityDetector.set(
            addItem(
                tLastID = 731,
                "Activity Detector Cover",
                "Gives out Activity as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_FluidDetector.set(
            addItem(
                tLastID = 732,
                "Fluid Detector Cover",
                "Gives out Fluid Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Cover_ItemDetector.set(
            addItem(
                tLastID = 733,
                "Item Detector Cover",
                "Gives out Item Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1L)));
        ItemList.Cover_EnergyDetector.set(
            addItem(
                tLastID = 734,
                "Energy Detector Cover",
                "Gives out Energy Amount as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L)));
        ItemList.Cover_PlayerDetector.set(
            addItem(
                tLastID = 735,
                "Player Detector Cover",
                "Gives out close Players as Redstone",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_FluidStorageMonitor.set(
            addItem(
                tLastID = 577,
                "Fluid Storage Monitor Cover",
                "Displays the fluid stored in the Tank",
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Sensor_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_PlayerDetector.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GregTech_API.registerCover(
            ItemList.Cover_Controller.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_CONTROLLER)),
            new GT_Cover_ControlsWork(TextureFactory.of(OVERLAY_CONTROLLER)));

        final ITexture doesWorkCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_ACTIVITYDETECTOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_ACTIVITYDETECTOR_GLOW)
                .glow()
                .build());
        GregTech_API.registerCover(
            ItemList.Cover_ActivityDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], doesWorkCoverTexture),
            new GT_Cover_DoesWork(doesWorkCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_FluidDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_FLUIDDETECTOR)),
            new GT_Cover_LiquidMeter(TextureFactory.of(OVERLAY_FLUIDDETECTOR)));
        GregTech_API.registerCover(
            ItemList.Cover_ItemDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ITEMDETECTOR)),
            new GT_Cover_ItemMeter(TextureFactory.of(OVERLAY_ITEMDETECTOR)));
        GregTech_API.registerCover(
            ItemList.Cover_EnergyDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_ENERGYDETECTOR)),
            new GT_Cover_EUMeter(TextureFactory.of(OVERLAY_ENERGYDETECTOR)));

        final ITexture playerDectectorCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_ACTIVITYDETECTOR),
            TextureFactory.builder()
                .addIcon(OVERLAY_ACTIVITYDETECTOR_GLOW)
                .glow()
                .build());
        GregTech_API.registerCover(
            ItemList.Cover_PlayerDetector.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], playerDectectorCoverTexture),
            new GT_Cover_PlayerDetector(playerDectectorCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_FluidStorageMonitor.get(1L),
            TextureFactory.of(OVERLAY_FLUID_STORAGE_MONITOR0),
            new GT_Cover_FluidStorageMonitor());

        ItemList.Cover_Screen.set(
            addItem(
                tLastID = 740,
                "Computer Monitor Cover",
                "Displays Data and GUI",
                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LUX, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L)));
        ItemList.Cover_Crafting.set(
            addItem(
                tLastID = 744,
                "Crafting Table Cover",
                "Better than a wooden Workbench",
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 4L)));
        ItemList.Cover_Drain.set(
            addItem(
                tLastID = 745,
                "Drain Module Cover",
                "Absorbs Fluids and collects Rain",
                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));

        ItemList.Cover_Shutter.set(
            addItem(
                tLastID = 749,
                "Shutter Module Cover",
                "Blocks Inventory/Tank Side. Use together with Machine Controller.",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L)));

        GT_ModHandler.addCraftingRecipe(
            ItemList.Cover_Screen.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "AGA", "RPB", "ALA", 'A', OrePrefixes.plate.get(Materials.Aluminium), 'L',
                OrePrefixes.dust.get(Materials.Glowstone), 'R', Dyes.dyeRed, 'G', Dyes.dyeLime, 'B', Dyes.dyeBlue, 'P',
                OrePrefixes.plate.get(Materials.Glass) });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                ItemList.Cover_Drain.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Shutter.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 2L),
                new ItemStack(Blocks.iron_bars, 2),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Drain.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                new ItemStack(Blocks.crafting_table, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_Crafting.get(1L))
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_Shutter.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.FluidFilter.get(1L))
            .duration(40 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Cover_Screen.get(1L),
                ItemList.Cover_FluidDetector.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_FluidStorageMonitor.get(1L))
            .duration(40 * SECONDS)
            .eut(4)
            .addTo(assemblerRecipes);

        final ITexture screenCoverTexture = TextureFactory.of(
            TextureFactory.of(OVERLAY_SCREEN),
            TextureFactory.builder()
                .addIcon(OVERLAY_SCREEN_GLOW)
                .glow()
                .build());
        GregTech_API.registerCover(
            ItemList.Cover_Screen.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], screenCoverTexture),
            new GT_Cover_Screen(screenCoverTexture));
        GregTech_API.registerCover(
            ItemList.Cover_Crafting.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CRAFTING)),
            new GT_Cover_Crafting(TextureFactory.of(OVERLAY_CRAFTING)));
        GregTech_API.registerCover(
            ItemList.Cover_Drain.get(1L),
            TextureFactory.of(MACHINE_CASINGS[0][0], TextureFactory.of(OVERLAY_DRAIN)),
            new GT_Cover_Drain(TextureFactory.of(OVERLAY_DRAIN)));
        GregTech_API.registerCover(
            ItemList.Cover_Shutter.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_SHUTTER)),
            new GT_Cover_Shutter(TextureFactory.of(OVERLAY_SHUTTER)));

        ItemList.Cover_SolarPanel.set(
            addItem(
                tLastID = 750,
                "Solar Panel",
                "May the Sun be with you (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 1L)));
        ItemList.Cover_SolarPanel_8V.set(
            addItem(
                tLastID = 751,
                "Solar Panel (8V)",
                "8 Volt Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 2L)));
        ItemList.Cover_SolarPanel_LV.set(
            addItem(
                tLastID = 752,
                "Solar Panel (LV)",
                "Low Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 4L)));
        ItemList.Cover_SolarPanel_MV.set(
            addItem(
                tLastID = 753,
                "Solar Panel (MV)",
                "Medium Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 8L)));
        ItemList.Cover_SolarPanel_HV.set(
            addItem(
                tLastID = 754,
                "Solar Panel (HV)",
                "High Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 16L)));
        ItemList.Cover_SolarPanel_EV.set(
            addItem(
                tLastID = 755,
                "Solar Panel (EV)",
                "Extreme Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 32L)));
        ItemList.Cover_SolarPanel_IV.set(
            addItem(
                tLastID = 756,
                "Solar Panel (IV)",
                "Insane Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_LuV.set(
            addItem(
                tLastID = 757,
                "Solar Panel (LuV)",
                "Ludicrous Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_ZPM.set(
            addItem(
                tLastID = 758,
                "Solar Panel (ZPM)",
                "ZPM Voltage Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));
        ItemList.Cover_SolarPanel_UV.set(
            addItem(
                tLastID = 759,
                "Solar Panel (UV)",
                "Ultimate Solar Panel (Needs cleaning with right click)",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 64L)));

        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel.get(1L),
            TextureFactory.of(SOLARPANEL),
            new GT_Cover_SolarPanel(1));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_8V.get(1L),
            TextureFactory.of(SOLARPANEL_8V),
            new GT_Cover_SolarPanel(8));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_LV.get(1L),
            TextureFactory.of(SOLARPANEL_LV),
            new GT_Cover_SolarPanel(32));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_MV.get(1L),
            TextureFactory.of(SOLARPANEL_MV),
            new GT_Cover_SolarPanel(128));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_HV.get(1L),
            TextureFactory.of(SOLARPANEL_HV),
            new GT_Cover_SolarPanel(512));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_EV.get(1L),
            TextureFactory.of(SOLARPANEL_EV),
            new GT_Cover_SolarPanel(2048));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_IV.get(1L),
            TextureFactory.of(SOLARPANEL_IV),
            new GT_Cover_SolarPanel(8192));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_LuV.get(1L),
            TextureFactory.of(SOLARPANEL_LuV),
            new GT_Cover_SolarPanel(32768));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_ZPM.get(1L),
            TextureFactory.of(SOLARPANEL_ZPM),
            new GT_Cover_SolarPanel(131072));
        GregTech_API.registerCover(
            ItemList.Cover_SolarPanel_UV.get(1L),
            TextureFactory.of(SOLARPANEL_UV),
            new GT_Cover_SolarPanel(524288));

        ItemList.Tool_Sonictron.set(
            addItem(
                tLastID = 760,
                "Sonictron",
                "Bring your Music with you",
                Behaviour_Sonictron.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 4L)));
        ItemList.Tool_Cheat.set(
            addItem(
                tLastID = 761,
                "Debug Scanner",
                "Also an Infinite Energy Source",
                Behaviour_Scanner.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 64L)));
        setElectricStats(32000 + tLastID, -2000000000L, 1000000000L, -1L, -3L, false);
        ItemList.Tool_Scanner.set(
            addItem(
                tLastID = 762,
                "Portable Scanner",
                "Tricorder",
                Behaviour_Scanner.INSTANCE,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 6L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 6L)));
        setElectricStats(32000 + tLastID, 400000L, GT_Values.V[2], 2L, -1L, false);
        GT_ModHandler.addCraftingRecipe(
            ItemList.Tool_Scanner.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "EPR", "CSC", "PBP", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                OrePrefixes.plate.get(Materials.Aluminium), 'E', ItemList.Emitter_MV, 'R', ItemList.Sensor_MV, 'S',
                ItemList.Cover_Screen, 'B', ItemList.Battery_RE_MV_Lithium });
        ItemList.NC_SensorKit.set(addItem(tLastID = 763, "GregTech Sensor Kit", "", new Behaviour_SensorKit()));
        ItemList.Duct_Tape.set(
            addItem(
                tLastID = 764,
                "BrainTech Aerospace Advanced Reinforced Duct Tape FAL-84",
                "If you can't fix it with this, use more of it!",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                OreDictNames.craftingDuctTape));
        ItemList.McGuffium_239.set(
            addItem(
                tLastID = 765,
                "Mc Guffium 239",
                "42% better than Phlebotnium",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.SPIRITUS, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.VITIUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L)));

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Sensor_LV.get(1L), ItemList.Emitter_LV.get(1L), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.NC_SensorKit.get(1L))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(2)
            .addTo(assemblerRecipes);

        ItemList.Cover_RedstoneTransmitterExternal.set(
            addItem(
                tLastID = 741,
                "Redstone Transmitter (External)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneTransmitterInternal.set(
            addItem(
                tLastID = 742,
                "Redstone Transmitter (Internal)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiverExternal.set(
            addItem(
                tLastID = 746,
                "Redstone Receiver (External)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        ItemList.Cover_RedstoneReceiverInternal.set(
            addItem(
                tLastID = 747,
                "Redstone Receiver (Internal)",
                "Transfers Redstone signals wireless",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));

        GregTech_API.registerCover(
            ItemList.Cover_RedstoneTransmitterExternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            new GT_Cover_RedstoneTransmitterExternal(TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneTransmitterInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)),
            new GT_Cover_RedstoneTransmitterInternal(TextureFactory.of(OVERLAY_REDSTONE_TRANSMITTER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneReceiverExternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            new GT_Cover_RedstoneReceiverExternal(TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)));
        GregTech_API.registerCover(
            ItemList.Cover_RedstoneReceiverInternal.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)),
            new GT_Cover_RedstoneReceiverInternal(TextureFactory.of(OVERLAY_REDSTONE_RECEIVER)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Emitter_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_RedstoneTransmitterExternal.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Sensor_EV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_RedstoneReceiverExternal.get(1L))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneTransmitterInternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneTransmitterExternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneReceiverInternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneReceiverExternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneTransmitterExternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneTransmitterInternal.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.Cover_RedstoneReceiverExternal.get(1L),
            new Object[] { ItemList.Cover_RedstoneReceiverInternal.get(1L) });

        ItemList.Cover_NeedsMaintainance.set(
            addItem(
                tLastID = 748,
                "Needs Maintenance Cover",
                "Attach to Multiblock Controller. Emits Redstone Signal if needs Maintenance",
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L)));
        GregTech_API.registerCover(
            ItemList.Cover_NeedsMaintainance.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)),
            new GT_Cover_NeedMaintainance(TextureFactory.of(OVERLAY_MAINTENANCE_DETECTOR)));

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Emitter_MV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Cover_NeedsMaintainance.get(1L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { "SPS", "dIC", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });
        GT_ModHandler.addCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { "SPS", "CId", "SPS", 'P', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.screw.get(Materials.Iron), 'I', ItemList.Component_Filter, 'C',
                ItemList.Conveyor_Module_LV });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Export.get(1L),
            new Object[] { ItemList.ItemFilter_Import.get(1L) });
        GT_ModHandler.addShapelessCraftingRecipe(
            ItemList.ItemFilter_Import.get(1L),
            new Object[] { ItemList.ItemFilter_Export.get(1L) });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                ItemList.Component_Filter.get(1L),
                ItemList.Conveyor_Module_LV.get(1L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.ItemFilter_Export.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2L),
                ItemList.Component_Filter.get(1L),
                ItemList.Conveyor_Module_LV.get(1L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.ItemFilter_Import.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        GT_ModHandler.addCraftingRecipe(
            ItemList.Tool_Cover_Copy_Paste.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
            new Object[] { "PSP", "PCP", "PBP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'S',
                ItemList.Tool_DataStick.get(1L), 'C', ItemList.Cover_Screen.get(1L), 'B',
                ItemList.Battery_RE_MV_Lithium.get(1L) });

        ItemList.Steam_Regulator_LV.set(
            addItem(
                tLastID = 332,
                "Steam Regulator (LV)",
                GT_Utility.formatNumbers(1024) + PartCoverText + GT_Utility.formatNumbers(1024 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)));
        ItemList.Steam_Regulator_MV.set(
            addItem(
                tLastID = 333,
                "Steam Regulator (MV)",
                GT_Utility.formatNumbers(2048) + PartCoverText + GT_Utility.formatNumbers(2048 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 2L)));
        ItemList.Steam_Regulator_HV.set(
            addItem(
                tLastID = 334,
                "Steam Regulator (HV)",
                GT_Utility.formatNumbers(4096) + PartCoverText + GT_Utility.formatNumbers(4096 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)));
        ItemList.Steam_Regulator_EV.set(
            addItem(
                tLastID = 335,
                "Steam Regulator (EV)",
                GT_Utility.formatNumbers(8192) + PartCoverText + GT_Utility.formatNumbers(8192 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 8L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 8L)));
        ItemList.Steam_Regulator_IV.set(
            addItem(
                tLastID = 336,
                "Steam Regulator (IV)",
                GT_Utility.formatNumbers(16384) + PartCoverText + GT_Utility.formatNumbers(16384 * 20) + PartCoverText2,
                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 16L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 16L)));

        GregTech_API.registerCover(
            ItemList.Steam_Regulator_LV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(1024, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_MV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[2][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(2048, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_HV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[3][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(4096, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_EV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[4][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(8192, TextureFactory.of(OVERLAY_VALVE)));
        GregTech_API.registerCover(
            ItemList.Steam_Regulator_IV.get(1L),
            TextureFactory.of(MACHINE_CASINGS[5][0], TextureFactory.of(OVERLAY_VALVE)),
            new GT_Cover_SteamRegulator(16384, TextureFactory.of(OVERLAY_VALVE)));

    }

    private static final Map<Materials, Materials> cauldronRemap = new HashMap<>();

    public static void registerCauldronCleaningFor(Materials in, Materials out) {
        cauldronRemap.put(in, out);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        int aDamage = aItemEntity.getEntityItem()
            .getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0) && (!aItemEntity.worldObj.isRemote)) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                int tX = MathHelper.floor_double(aItemEntity.posX);
                int tY = MathHelper.floor_double(aItemEntity.posY);
                int tZ = MathHelper.floor_double(aItemEntity.posZ);
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {

                        aMaterial = cauldronRemap.getOrDefault(aMaterial, aMaterial);

                        aItemEntity.setEntityItemStack(
                            GT_OreDictUnificator
                                .get(OrePrefixes.dust, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.crushed) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(
                            GT_OreDictUnificator
                                .get(OrePrefixes.crushedPurified, aMaterial, aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                } else if (aPrefix == OrePrefixes.dust && aMaterial == Materials.Wheat) {
                    Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                    byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                    if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                        aItemEntity.setEntityItemStack(ItemList.Food_Dough.get(aItemEntity.getEntityItem().stackSize));
                        aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        int aDamage = aStack.getItemDamage();
        if ((aDamage < 32000) && (aDamage >= 0)) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[(aDamage % 1000)];
            if ((aMaterial != null) && (aMaterial != Materials.Empty) && (aMaterial != Materials._NULL)) {
                OrePrefixes aPrefix = this.mGeneratedPrefixList[(aDamage / 1000)];
                if ((aPrefix == OrePrefixes.dustImpure) || (aPrefix == OrePrefixes.dustPure)) {
                    aList.add(this.mToolTipPurify);
                }
            }
        }
    }

    public boolean isPlasmaCellUsed(OrePrefixes aPrefix, Materials aMaterial) {
        Collection<GT_Recipe> fusionRecipes = RecipeMaps.fusionRecipes.getAllRecipes();
        if (aPrefix == OrePrefixes.cellPlasma && aMaterial.getPlasma(1L) != null) { // Materials has a plasma fluid
            for (GT_Recipe recipe : fusionRecipes) { // Loop through fusion recipes
                if (recipe.getFluidOutput(0) != null) { // Make sure fluid output can't be null (not sure if possible)
                    if (recipe.getFluidOutput(0)
                        .isFluidEqual(aMaterial.getPlasma(1L))) return true; // Fusion recipe
                                                                             // output matches
                                                                             // current plasma
                                                                             // cell fluid
                }
            }
        }
        return false;
    }

    @Override
    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return (aDoShowAllItems) || (((aPrefix != OrePrefixes.gem) || (!aMaterial.mName.startsWith("Infused")))
            && (aPrefix != OrePrefixes.dustTiny)
            && (aPrefix != OrePrefixes.dustSmall)
            && (aPrefix != OrePrefixes.dustImpure)
            && (aPrefix != OrePrefixes.dustPure)
            && (aPrefix != OrePrefixes.crushed)
            && (aPrefix != OrePrefixes.crushedPurified)
            && (aPrefix != OrePrefixes.crushedCentrifuged)
            && (aPrefix != OrePrefixes.ingotHot)
            && !(aPrefix == OrePrefixes.cellPlasma && !isPlasmaCellUsed(aPrefix, aMaterial)));
    }

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (((aDamage >= 32430) && (aDamage <= 32461)) || (aDamage == 32465 || aDamage == 32466)) {
            return ItemList.Spray_Empty.get(1L);
        }
        if ((aDamage == 32479) || (aDamage == 32476)) {
            return new ItemStack(this, 1, aDamage - 2);
        }
        if (aDamage == 32401) {
            return new ItemStack(this, 1, aDamage - 1);
        }
        return super.getContainerItem(aStack);
    }

    @Override
    public boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial) {
        return (super.doesMaterialAllowGeneration(aPrefix, aMaterial));
    }
}
