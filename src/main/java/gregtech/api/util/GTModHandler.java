package gregtech.api.util;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.B;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.DW;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Mods.Gendustry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.recipes.BlastFurnaceRecipes;
import ganymedes01.etfuturum.recipes.SmokerRecipes;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.interfaces.internal.IGTCraftingRecipe;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.objects.GTHashSet;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeCategories;
import ic2.api.item.IBoxable;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.core.item.ItemToolbox;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the Interface I use for interacting with other Mods.
 * <p/>
 * Due to the many imports, this File can cause compile Problems if not all the APIs are installed
 */
public class GTModHandler {

    public static final List<IRecipe> sSingleNonBlockDamagableRecipeList = new ArrayList<>(1000);
    private static final Map<String, ItemStack> sIC2ItemMap = new HashMap<>();

    // public for bartworks
    public static final List<IRecipe> sAllRecipeList = new ArrayList<>(5000), sBufferRecipeList = new ArrayList<>(1000);
    private static final List<ItemStack> delayedRemovalByOutput = new ArrayList<>();
    private static final List<InventoryCrafting> delayedRemovalByRecipe = new ArrayList<>();

    public static Collection<String> sNativeRecipeClasses = new HashSet<>(), sSpecialRecipeClasses = new HashSet<>();
    public static GTHashSet sNonReplaceableItems = new GTHashSet();
    public static Object sBoxableWrapper = new GTIBoxableWrapper();
    public static Collection<GTItemStack> sBoxableItems = new ArrayList<>();
    private static Set<GTUtility.ItemId> recyclerWhitelist;
    private static Set<GTUtility.ItemId> recyclerBlacklist;

    private static boolean sBufferCraftingRecipes = true;
    public static List<Integer> sSingleNonBlockDamagableRecipeList_list = new ArrayList<>(100);
    public static List<Integer> sSingleNonBlockDamagableRecipeList_warntOutput = new ArrayList<>(50);
    public static List<Integer> sVanillaRecipeList_warntOutput = new ArrayList<>(50);
    public static List<Integer> sAnySteamFluidIDs = new ArrayList<>();
    public static List<Integer> sSuperHeatedSteamFluidIDs = new ArrayList<>();

    static {
        sNativeRecipeClasses.add(ShapedRecipes.class.getName());
        sNativeRecipeClasses.add(ShapedOreRecipe.class.getName());
        sNativeRecipeClasses.add(GTShapedRecipe.class.getName());
        sNativeRecipeClasses.add(ShapelessRecipes.class.getName());
        sNativeRecipeClasses.add(ShapelessOreRecipe.class.getName());
        sNativeRecipeClasses.add(GTShapelessRecipe.class.getName());
        sNativeRecipeClasses.add(ic2.core.AdvRecipe.class.getName());
        sNativeRecipeClasses.add(ic2.core.AdvShapelessRecipe.class.getName());
        sNativeRecipeClasses.add("appeng.recipes.game.ShapedRecipe");
        sNativeRecipeClasses.add("appeng.recipes.game.ShapelessRecipe");
        sNativeRecipeClasses.add("forestry.core.utils.ShapedRecipeCustom");

        // Recipe Classes, which should never be removed.
        sSpecialRecipeClasses.add(net.minecraft.item.crafting.RecipeFireworks.class.getName());
        sSpecialRecipeClasses.add(net.minecraft.item.crafting.RecipesArmorDyes.class.getName());
        sSpecialRecipeClasses.add(net.minecraft.item.crafting.RecipeBookCloning.class.getName());
        sSpecialRecipeClasses.add(net.minecraft.item.crafting.RecipesMapCloning.class.getName());
        sSpecialRecipeClasses.add(net.minecraft.item.crafting.RecipesMapExtending.class.getName());
        sSpecialRecipeClasses.add("jds.bibliocraft.BiblioSpecialRecipes");
        sSpecialRecipeClasses.add("dan200.qcraft.shared.EntangledQBlockRecipe");
        sSpecialRecipeClasses.add("dan200.qcraft.shared.EntangledQuantumComputerRecipe");
        sSpecialRecipeClasses.add("dan200.qcraft.shared.QBlockRecipe");
        sSpecialRecipeClasses.add("appeng.recipes.game.FacadeRecipe");
        sSpecialRecipeClasses.add("appeng.recipes.game.DisassembleRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.carts.LocomotivePaintingRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.util.crafting.RotorRepairRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.util.crafting.RoutingTableCopyRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.util.crafting.RoutingTicketCopyRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.util.crafting.TankCartFilterRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.emblems.LocomotiveEmblemRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.emblems.EmblemPostColorRecipe");
        sSpecialRecipeClasses.add("mods.railcraft.common.emblems.EmblemPostEmblemRecipe");
        sSpecialRecipeClasses.add("mods.immibis.redlogic.interaction.RecipeDyeLumarButton");
        sSpecialRecipeClasses.add("thaumcraft.common.items.armor.RecipesRobeArmorDyes");
        sSpecialRecipeClasses.add("thaumcraft.common.items.armor.RecipesVoidRobeArmorDyes");
        sSpecialRecipeClasses.add("thaumcraft.common.lib.crafting.ShapelessNBTOreRecipe");
        sSpecialRecipeClasses.add("twilightforest.item.TFMapCloningRecipe");
        sSpecialRecipeClasses.add("forestry.lepidopterology.MatingRecipe");
        sSpecialRecipeClasses.add("micdoodle8.mods.galacticraft.planets.asteroids.recipe.CanisterRecipes");
        sSpecialRecipeClasses.add("shedar.mods.ic2.nuclearcontrol.StorageArrayRecipe");
    }

    /**
     * Returns if that Liquid is Water or Distilled Water
     */
    public static boolean isWater(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(Materials.Water.getFluid(1)) || aFluid.isFluidEqual(getDistilledWater(1));
    }

    /**
     * Returns a Liquid Stack with given amount of Water.
     *
     * @deprecated Use {@link gregtech.api.enums.Materials} instead.
     */
    public static FluidStack getWater(long aAmount) {
        return Materials.Water.getFluid(aAmount);
    }

    /**
     * Returns a Liquid Stack with given amount of distilled Water.
     */
    public static FluidStack getDistilledWater(long aAmount) {
        FluidStack tFluid = FluidRegistry.getFluidStack("ic2distilledwater", (int) aAmount);
        if (tFluid == null) tFluid = Materials.Water.getFluid(aAmount);
        return tFluid;
    }

    /**
     * Returns a Liquid Stack with given amount of IC2 Coolant, falling back to water if not available.
     */
    public static FluidStack getIC2Coolant(long aAmount) {
        if (IndustrialCraft2.isModLoaded()) return FluidRegistry.getFluidStack("ic2coolant", (int) aAmount);
        else return Materials.Water.getFluid(aAmount);
    }

    /**
     * Returns a Liquid Stack with given amount of Liquid DNA, falling back to Biomass if not available.
     */
    public static FluidStack getLiquidDNA(long aAmount) {
        if (Gendustry.isModLoaded()) return FluidRegistry.getFluidStack("liquiddna", (int) aAmount);
        else return Materials.Biomass.getFluid(aAmount);
    }

    /**
     * Returns if that Liquid is Lava
     */
    public static boolean isLava(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(Materials.Lava.getFluid(1));
    }

    /**
     * Returns a Liquid Stack with given amount of Lava.
     *
     * @deprecated Use {@link gregtech.api.enums.Materials} instead.
     */
    public static FluidStack getLava(long aAmount) {
        return Materials.Lava.getFluid(aAmount);
    }

    /**
     * Returns if that Liquid is Steam
     */
    public static boolean isSteam(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(Materials.Steam.getGas(1));
    }

    /**
     * Returns if that Liquid is Any Steam (including other mods)
     */
    public static boolean isAnySteam(FluidStack aFluid) {
        return (aFluid != null && (isSteam(aFluid) || sAnySteamFluidIDs.contains(aFluid.getFluidID())));
    }

    /**
     * Returns if that Liquid is Super Heated Steam (including other mods)
     */
    public static boolean isSuperHeatedSteam(FluidStack aFluid) {
        return (aFluid != null && sSuperHeatedSteamFluidIDs.contains(aFluid.getFluidID()));
    }

    /**
     * Returns a Liquid Stack with given amount of Steam.
     *
     * @deprecated Use {@link gregtech.api.enums.Materials} instead.
     */
    @Deprecated
    public static FluidStack getSteam(long aAmount) {
        return Materials.Steam.getGas(aAmount);
    }

    /**
     * Returns a Liquid Stack with given amount of Milk.
     *
     * @deprecated Use {@link gregtech.api.enums.Materials} instead.
     */
    @Deprecated
    public static FluidStack getMilk(long aAmount) {
        return Materials.Milk.getFluid(aAmount);
    }

    /**
     * Returns a Liquid Stack with given amount of IC2 Hot Water
     */
    public static FluidStack getHotWater(final int amount) {
        return FluidRegistry.getFluidStack("ic2hotwater", amount);
    }

    /**
     * Returns a Liquid Stack with given amount of IC2 Pahoehoe Lava
     */
    public static FluidStack getPahoehoeLava(final int amount) {
        return FluidRegistry.getFluidStack("ic2pahoehoelava", amount);
    }

    /**
     * Returns a Liquid Stack with given amount of IC2 Superheated Steam
     */
    public static FluidStack getSuperHeatedSteam(final int amount) {
        return FluidRegistry.getFluidStack("ic2superheatedsteam", amount);
    }

    /**
     * Returns a Liquid Stack with given amount of IC2 Hot Coolant
     */
    public static FluidStack getHotCoolant(final int amount) {
        return FluidRegistry.getFluidStack("ic2hotcoolant", amount);
    }

    /**
     * @return the Value of this Stack, when burning inside a Furnace (200 = 1 Burn Process = 500 EU, max = 32767 (that
     *         is 81917.5 EU)), limited to Short because the vanilla Furnace otherwise can't handle it properly, stupid
     *         Mojang...
     */
    public static int getFuelValue(ItemStack aStack) {
        return TileEntityFurnace.getItemBurnTime(aStack);
    }

    /**
     * Gets an Item from IndustrialCraft, and returns a Replacement Item if not possible
     */
    public static ItemStack getIC2Item(String aItem, long aAmount, ItemStack aReplacement) {
        if (GTUtility.isStringInvalid(aItem) || !GregTechAPI.sPreloadStarted) return null;
        // if (D1) GTLog.out.println("Requested the Item '" + aItem + "' from the IC2-API");
        if (!sIC2ItemMap.containsKey(aItem)) {
            ItemStack tStack = IC2Items.getItem(aItem);
            sIC2ItemMap.put(aItem, tStack);
            if (tStack == null && D1) GTLog.err.println(aItem + " is not found in the IC2 Items!");
        }
        return GTUtility.copyAmount(aAmount, sIC2ItemMap.get(aItem), aReplacement);
    }

    /**
     * Gets an Item from IndustrialCraft, but the Damage Value can be specified, and returns a Replacement Item with the
     * same Damage if not possible
     */
    public static ItemStack getIC2Item(String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
        ItemStack rStack = getIC2Item(aItem, aAmount, aReplacement);
        if (rStack == null) return null;
        Items.feather.setDamage(rStack, aMeta);
        return rStack;
    }

    /**
     * Gets an Item from IndustrialCraft, but the Damage Value can be specified
     */
    public static ItemStack getIC2Item(String aItem, long aAmount, int aMeta) {
        return getIC2Item(aItem, aAmount, aMeta, null);
    }

    /**
     * Gets an Item from IndustrialCraft
     */
    public static ItemStack getIC2Item(String aItem, long aAmount) {
        return getIC2Item(aItem, aAmount, null);
    }

    /**
     * Gets an Item from the specified mod
     */
    public static ItemStack getModItem(String aModID, String aItem) {
        return getModItem(aModID, aItem, 1, null);
    }

    /**
     * Gets an Item from the specified mod
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount) {
        return getModItem(aModID, aItem, aAmount, null);
    }

    /**
     * Gets an Item from the specified mod, and returns a Replacement Item if not possible
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, ItemStack aReplacement) {
        ItemStack result;
        if (GTUtility.isStringInvalid(aItem) || !GregTechAPI.sPreloadStarted) {
            result = null;
        } else {
            result = GTUtility
                .copyAmount(aAmount, GameRegistry.findItemStack(aModID, aItem, (int) aAmount), aReplacement);
        }

        if (result == null) {
            String reason;
            if (GTUtility.isStringInvalid(aItem)) {
                reason = "the name of the item is an invalid string";
            } else if (!GregTechAPI.sPreloadStarted) {
                reason = "the GT5U preloading phase has not yet started";
            } else {
                reason = "the item was not found in the game registry";
            }
            String log_message = "getModItem call: object \"" + aItem
                + "\" with mod id \""
                + aModID
                + "\" has returned null because "
                + reason;
            GTLog.out.println(log_message);
            new Exception().printStackTrace(GTLog.out);
        }
        return result;
    }

    /**
     * Gets an Item from the specified mod, but the Damage Value can be specified
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, int aMeta) {
        ItemStack rStack = getModItem(aModID, aItem, aAmount);
        if (rStack == null) return null;
        Items.feather.setDamage(rStack, aMeta);
        return rStack;
    }

    /**
     * Gets an Item from the specified mod, but the Damage Value can be specified, and returns a Replacement Item with
     * the same Damage if not possible
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
        ItemStack rStack = getModItem(aModID, aItem, aAmount, aReplacement);
        if (rStack == null) return null;
        Items.feather.setDamage(rStack, aMeta);
        return rStack;
    }

    /**
     * Adds a Scrapbox Drop. Fails at April first for the "suddenly Hoes"-Feature of IC2
     */
    public static boolean addScrapboxDrop(float aChance, ItemStack aOutput) {
        aOutput = GTOreDictUnificator.get(true, aOutput);
        if (aOutput == null || aChance <= 0) return false;
        aOutput.stackSize = 1;
        if (GTConfig.troll && !GTUtility.areStacksEqual(aOutput, new ItemStack(Items.wooden_hoe, 1, 0))) return false;
        Recipes.scrapboxDrops.addDrop(GTUtility.copyOrNull(aOutput), aChance);
        return true;
    }

    // temporary buffer list to fix NPE if you try to access the recyclerBlacklist too early
    private static List<RecipeInputItemStack> tempRecyclerBlackList = new ArrayList<>();

    /**
     * Adds an Item to the Recycler Blacklist
     */
    public static boolean addToRecyclerBlackList(ItemStack aRecycledStack) {
        if (aRecycledStack == null) return false;
        final RecipeInputItemStack iRecipeInput = new RecipeInputItemStack(aRecycledStack);
        if (Recipes.recyclerBlacklist == null) {
            tempRecyclerBlackList.add(iRecipeInput);
            return true;
        }
        if (tempRecyclerBlackList != null) {
            for (RecipeInputItemStack recipe : tempRecyclerBlackList) {
                Recipes.recyclerBlacklist.add(recipe);
            }
            tempRecyclerBlackList = null;
        }
        Recipes.recyclerBlacklist.add(iRecipeInput);
        return true;
    }

    /**
     * Just simple Furnace smelting. Unbelievable how Minecraft fails at making a simple ItemStack->ItemStack mapping...
     */
    public static boolean addSmeltingRecipe(ItemStack input, ItemStack output) {
        return addSmeltingRecipe(input, output, 0.0F);
    }

    /**
     * Just simple Furnace smelting. Unbelievable how Minecraft fails at making a simple ItemStack->ItemStack mapping...
     */
    public static boolean addSmeltingRecipe(ItemStack input, ItemStack output, float xp) {
        output = GTOreDictUnificator.get(true, output);
        if (input == null || output == null) return false;
        FurnaceRecipes.smelting()
            .func_151394_a(input, GTUtility.copyOrNull(output), xp);
        return true;
    }

    /**
     * Adds to Furnace AND Alloy Smelter
     */
    public static boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput, boolean hidden) {
        if (aInput == null || aOutput == null) {
            return false;
        }
        boolean temp = aInput.stackSize == 1 && addSmeltingRecipe(aInput, aOutput);
        ItemStack input2 = OrePrefixes.ingot.contains(aOutput) ? ItemList.Shape_Mold_Ingot.get(0)
            : OrePrefixes.block.contains(aOutput) ? ItemList.Shape_Mold_Block.get(0)
                : OrePrefixes.nugget.contains(aOutput) ? ItemList.Shape_Mold_Nugget.get(0) : null;
        if (Materials.Graphite.contains(aInput)) {
            return false;
        }
        if ((input2 == null) && ((OrePrefixes.ingot.contains(aInput)) || (OrePrefixes.dust.contains(aInput))
            || (OrePrefixes.gem.contains(aInput)))) {
            return false;
        }
        GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
        if (input2 == null) {
            recipeBuilder.itemInputs(aInput);
        } else {
            recipeBuilder.itemInputs(aInput, input2);
        }
        recipeBuilder.itemOutputs(aOutput)
            .duration(6 * SECONDS + 10 * TICKS)
            .eut(3)
            .recipeCategory(RecipeCategories.alloySmelterRecycling);
        if (hidden) {
            recipeBuilder.hidden();
        }
        recipeBuilder.addTo(alloySmelterRecipes);
        return true;
    }

    /**
     * Removes IC2 recipes.
     */
    public static void removeAllIC2Recipes() {
        getMaceratorRecipeList().clear();
        getCompressorRecipeList().clear();
        getExtractorRecipeList().clear();
        getOreWashingRecipeList().clear();
        getThermalCentrifugeRecipeList().clear();
    }

    public static Map<IRecipeInput, RecipeOutput> getExtractorRecipeList() {
        return Recipes.extractor.getRecipes();
    }

    public static Map<IRecipeInput, RecipeOutput> getCompressorRecipeList() {
        return Recipes.compressor.getRecipes();
    }

    public static Map<IRecipeInput, RecipeOutput> getMaceratorRecipeList() {
        return Recipes.macerator.getRecipes();
    }

    public static Map<IRecipeInput, RecipeOutput> getThermalCentrifugeRecipeList() {
        return Recipes.centrifuge.getRecipes();
    }

    public static Map<IRecipeInput, RecipeOutput> getOreWashingRecipeList() {
        return Recipes.oreWashing.getRecipes();
    }

    public static void stopBufferingCraftingRecipes() {
        sBufferCraftingRecipes = false;

        bulkRemoveRecipeByOutput(delayedRemovalByOutput);
        bulkRemoveByRecipe(delayedRemovalByRecipe);
        sBufferRecipeList.forEach(GameRegistry::addRecipe);

        delayedRemovalByOutput.clear();
        delayedRemovalByRecipe.clear();
        sBufferRecipeList.clear();
    }

    /**
     * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
     */
    public static boolean addCraftingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
        int[] aEnchantmentLevelsAdded, Object[] aRecipe) {
        return addCraftingRecipe(
            aResult,
            aEnchantmentsAdded,
            aEnchantmentLevelsAdded,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            aRecipe);
    }

    /**
     * Regular Crafting Recipes. Deletes conflicting Recipes too.
     * <p/>
     * You can insert instances of IItemContainer into the Recipe Input Array directly without having to call "get(1)"
     * on them.
     * <p/>
     * Enums are automatically getting their "name()"-Method called in order to deliver an OreDict String.
     * <p/>
     * Lowercase Letters are reserved for Tools. They are as follows:
     * <p/>
     * 'b' ToolDictNames.craftingToolBlade 'c' ToolDictNames.craftingToolCrowbar, 'd'
     * ToolDictNames.craftingToolScrewdriver, 'f' ToolDictNames.craftingToolFile, 'h'
     * ToolDictNames.craftingToolHardHammer, 'i' ToolDictNames.craftingToolSolderingIron, 'j'
     * ToolDictNames.craftingToolSolderingMetal, 'k' ToolDictNames.craftingToolKnive 'm'
     * ToolDictNames.craftingToolMortar, 'p' ToolDictNames.craftingToolDrawplate, 'r'
     * ToolDictNames.craftingToolSoftMallet, 's' ToolDictNames.craftingToolSaw, 'w' ToolDictNames.craftingToolWrench,
     * 'x' ToolDictNames.craftingToolWireCutter,
     */
    public static boolean addCraftingRecipe(ItemStack aResult, Object[] aRecipe) {
        return addCraftingRecipe(aResult, 0, aRecipe);
    }

    /**
     * Regular Crafting Recipes. Deletes conflicting Recipes too.
     * <p/>
     * You can insert instances of IItemContainer into the Recipe Input Array directly without having to call "get(1)"
     * on them.
     * <p/>
     * Enums are automatically getting their "name()"-Method called in order to deliver an OreDict String.
     * <p/>
     * Lowercase Letters are reserved for Tools. They are as follows:
     * <p/>
     * 'b' ToolDictNames.craftingToolBlade 'c' ToolDictNames.craftingToolCrowbar, 'd'
     * ToolDictNames.craftingToolScrewdriver, 'f' ToolDictNames.craftingToolFile, 'h'
     * ToolDictNames.craftingToolHardHammer, 'i' ToolDictNames.craftingToolSolderingIron, 'j'
     * ToolDictNames.craftingToolSolderingMetal, 'k' ToolDictNames.craftingToolKnive 'm'
     * ToolDictNames.craftingToolMortar, 'p' ToolDictNames.craftingToolDrawplate, 'r'
     * ToolDictNames.craftingToolSoftMallet, 's' ToolDictNames.craftingToolSaw, 'w' ToolDictNames.craftingToolWrench,
     * 'x' ToolDictNames.craftingToolWireCutter,
     */
    public static boolean addCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
        return addCraftingRecipe(
            aResult,
            null,
            null,
            (aBitMask & RecipeBits.MIRRORED) != 0,
            (aBitMask & RecipeBits.BUFFERED) != 0,
            (aBitMask & RecipeBits.KEEPNBT) != 0,
            (aBitMask & RecipeBits.DISMANTLEABLE) != 0,
            (aBitMask & RecipeBits.NOT_REMOVABLE) == 0,
            (aBitMask & RecipeBits.REVERSIBLE) != 0,
            (aBitMask & RecipeBits.DELETE_ALL_OTHER_RECIPES) != 0,
            (aBitMask & RecipeBits.DELETE_ALL_OTHER_RECIPES_IF_SAME_NBT) != 0,
            (aBitMask & RecipeBits.DELETE_ALL_OTHER_SHAPED_RECIPES) != 0,
            (aBitMask & RecipeBits.DELETE_ALL_OTHER_NATIVE_RECIPES) != 0,
            (aBitMask & RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS) == 0,
            (aBitMask & RecipeBits.ONLY_ADD_IF_THERE_IS_ANOTHER_RECIPE_FOR_IT) != 0,
            (aBitMask & RecipeBits.ONLY_ADD_IF_RESULT_IS_NOT_NULL) != 0,
            aRecipe);
    }

    public static void addMachineCraftingRecipe(ItemStack aResult, Object @Nullable [] aRecipe, int machineTier) {
        addMachineCraftingRecipe(aResult, RecipeBits.BITSD, aRecipe, machineTier);
    }

    public static void addMachineCraftingRecipe(ItemStack aResult, long aBitMask, Object @Nullable [] aRecipe,
        int machineTier) {
        if (aRecipe == null) return;

        for (int i = 3; i < aRecipe.length; i++) {
            if (!(aRecipe[i] instanceof MTEBasicMachineWithRecipe.X)) continue;

            // spotless:off
            aRecipe[i] = switch ((MTEBasicMachineWithRecipe.X) aRecipe[i]) {
                case CIRCUIT -> switch (machineTier) {
                    case  0 -> OrePrefixes.circuit.get(Materials.ULV);
                    case  1 -> OrePrefixes.circuit.get(Materials.LV);
                    case  2 -> OrePrefixes.circuit.get(Materials.MV);
                    case  3 -> OrePrefixes.circuit.get(Materials.HV);
                    case  4 -> OrePrefixes.circuit.get(Materials.EV);
                    case  5 -> OrePrefixes.circuit.get(Materials.IV);
                    case  6 -> OrePrefixes.circuit.get(Materials.LuV);
                    case  7 -> OrePrefixes.circuit.get(Materials.ZPM);
                    case  8 -> OrePrefixes.circuit.get(Materials.UV);
                    default -> OrePrefixes.circuit.get(Materials.UHV);
                };

                case BETTER_CIRCUIT -> switch (machineTier) {
                    case  0 -> OrePrefixes.circuit.get(Materials.LV);
                    case  1 -> OrePrefixes.circuit.get(Materials.MV);
                    case  2 -> OrePrefixes.circuit.get(Materials.HV);
                    case  3 -> OrePrefixes.circuit.get(Materials.EV);
                    case  4 -> OrePrefixes.circuit.get(Materials.IV);
                    case  5 -> OrePrefixes.circuit.get(Materials.LuV);
                    case  6 -> OrePrefixes.circuit.get(Materials.ZPM);
                    case  7 -> OrePrefixes.circuit.get(Materials.UV);
                    default -> OrePrefixes.circuit.get(Materials.UHV);
                };

                case HULL -> switch (machineTier) {
                    case  0 -> ItemList.Hull_ULV;
                    case  1 -> ItemList.Hull_LV;
                    case  2 -> ItemList.Hull_MV;
                    case  3 -> ItemList.Hull_HV;
                    case  4 -> ItemList.Hull_EV;
                    case  5 -> ItemList.Hull_IV;
                    case  6 -> ItemList.Hull_LuV;
                    case  7 -> ItemList.Hull_ZPM;
                    case  8 -> ItemList.Hull_UV;
                    default -> ItemList.Hull_MAX;
                };

                case WIRE -> switch (machineTier) {
                    case  0 -> OrePrefixes.cableGt01.get(Materials.Lead);
                    case  1 -> OrePrefixes.cableGt01.get(Materials.Tin);
                    case  2 -> OrePrefixes.cableGt01.get(Materials.AnyCopper);
                    case  3 -> OrePrefixes.cableGt01.get(Materials.Gold);
                    case  4 -> OrePrefixes.cableGt01.get(Materials.Aluminium);
                    case  5 -> OrePrefixes.cableGt01.get(Materials.Platinum);
                    case  6 -> OrePrefixes.cableGt01.get(Materials.NiobiumTitanium);
                    case  7 -> OrePrefixes.cableGt01.get(Materials.Naquadah);
                    case  8 -> OrePrefixes.cableGt04.get(Materials.NaquadahAlloy);
                    default -> OrePrefixes.wireGt01.get(Materials.SuperconductorUHV);
                };

                case WIRE4 -> switch (machineTier) {
                    case  0 -> OrePrefixes.cableGt04.get(Materials.Lead);
                    case  1 -> OrePrefixes.cableGt04.get(Materials.Tin);
                    case  2 -> OrePrefixes.cableGt04.get(Materials.AnyCopper);
                    case  3 -> OrePrefixes.cableGt04.get(Materials.Gold);
                    case  4 -> OrePrefixes.cableGt04.get(Materials.Aluminium);
                    case  5 -> OrePrefixes.cableGt04.get(Materials.Platinum);
                    case  6 -> OrePrefixes.cableGt04.get(Materials.NiobiumTitanium);
                    case  7 -> OrePrefixes.cableGt04.get(Materials.Naquadah);
                    case  8 -> OrePrefixes.wireGt01.get(Materials.SuperconductorUHV);
                    default -> OrePrefixes.wireGt04.get(Materials.SuperconductorUHV);
                };

                case STICK_DISTILLATION -> OrePrefixes.stick.get(Materials.Blaze);

                case GLASS -> switch (machineTier) {
                    case 0, 1, 2, 3    -> new ItemStack(Blocks.glass, 1, WILDCARD);
                    case 4, 5, 6, 7, 8 -> "blockGlass" + VN[machineTier];
                    default            -> "blockGlass" + VN[8];
                };

                case PLATE -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.plate.get(Materials.Steel);
                    case 2    -> OrePrefixes.plate.get(Materials.Aluminium);
                    case 3    -> OrePrefixes.plate.get(Materials.StainlessSteel);
                    case 4    -> OrePrefixes.plate.get(Materials.Titanium);
                    case 5    -> OrePrefixes.plate.get(Materials.TungstenSteel);
                    case 6    -> OrePrefixes.plate.get(Materials.HSSG);
                    case 7    -> OrePrefixes.plate.get(Materials.HSSE);
                    default   -> OrePrefixes.plate.get(Materials.Neutronium);
                };

                case PIPE -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.pipeMedium.get(Materials.Bronze);
                    case 2    -> OrePrefixes.pipeMedium.get(Materials.Steel);
                    case 3    -> OrePrefixes.pipeMedium.get(Materials.StainlessSteel);
                    case 4    -> OrePrefixes.pipeMedium.get(Materials.Titanium);
                    case 5    -> OrePrefixes.pipeMedium.get(Materials.TungstenSteel);
                    case 6    -> OrePrefixes.pipeSmall.get(Materials.ZPM);
                    case 7    -> OrePrefixes.pipeMedium.get(Materials.ZPM);
                    case 8    -> OrePrefixes.pipeLarge.get(Materials.ZPM);
                    default   -> OrePrefixes.pipeHuge.get(Materials.ZPM);
                };

                case COIL_HEATING -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.wireGt02.get(Materials.AnyCopper);
                    case 2    -> OrePrefixes.wireGt02.get(Materials.Cupronickel);
                    case 3    -> OrePrefixes.wireGt02.get(Materials.Kanthal);
                    case 4    -> OrePrefixes.wireGt02.get(Materials.Nichrome);
                    case 5    -> OrePrefixes.wireGt02.get(Materials.TPV);
                    case 6    -> OrePrefixes.wireGt02.get(Materials.HSSG);
                    case 7    -> OrePrefixes.wireGt02.get(Materials.Naquadah);
                    case 8    -> OrePrefixes.wireGt02.get(Materials.NaquadahAlloy);
                    case 9    -> OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                    default   -> OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                };

                case COIL_HEATING_DOUBLE -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.wireGt04.get(Materials.AnyCopper);
                    case 2    -> OrePrefixes.wireGt04.get(Materials.Cupronickel);
                    case 3    -> OrePrefixes.wireGt04.get(Materials.Kanthal);
                    case 4    -> OrePrefixes.wireGt04.get(Materials.Nichrome);
                    case 5    -> OrePrefixes.wireGt04.get(Materials.TPV);
                    case 6    -> OrePrefixes.wireGt04.get(Materials.HSSG);
                    case 7    -> OrePrefixes.wireGt04.get(Materials.Naquadah);
                    case 8    -> OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                    case 9    -> OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                    default   -> OrePrefixes.wireGt16.get(Materials.NaquadahAlloy);
                };

                case STICK_MAGNETIC -> switch (machineTier) {
                    case 0, 1       -> OrePrefixes.stick.get(Materials.IronMagnetic);
                    case 2, 3       -> OrePrefixes.stick.get(Materials.SteelMagnetic);
                    case 4, 5       -> OrePrefixes.stick.get(Materials.NeodymiumMagnetic);
                    case 6, 7, 8, 9 -> OrePrefixes.stick.get(Materials.SamariumMagnetic);
                    default         -> OrePrefixes.stick.get(Materials.TengamAttuned);
                };

                case STICK_ELECTROMAGNETIC -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.stick.get(Materials.AnyIron);
                    case 2, 3 -> OrePrefixes.stick.get(Materials.Steel);
                    case 4    -> OrePrefixes.stick.get(Materials.Neodymium);
                    default   -> OrePrefixes.stick.get(Materials.VanadiumGallium);
                };

                case COIL_ELECTRIC -> switch (machineTier) {
                    case 0  -> OrePrefixes.wireGt01.get(Materials.Lead);
                    case 1  -> OrePrefixes.wireGt02.get(Materials.Tin);
                    case 2  -> OrePrefixes.wireGt02.get(Materials.AnyCopper);
                    case 3  -> OrePrefixes.wireGt04.get(Materials.AnyCopper);
                    case 4  -> OrePrefixes.wireGt08.get(Materials.AnnealedCopper);
                    case 5  -> OrePrefixes.wireGt16.get(Materials.AnnealedCopper);
                    case 6  -> OrePrefixes.wireGt04.get(Materials.YttriumBariumCuprate);
                    case 7  -> OrePrefixes.wireGt08.get(Materials.Iridium);
                    default -> OrePrefixes.wireGt16.get(Materials.Osmium);
                };

                case ROBOT_ARM -> switch (machineTier) {
                    case 0, 1 -> ItemList.Robot_Arm_LV;
                    case 2    -> ItemList.Robot_Arm_MV;
                    case 3    -> ItemList.Robot_Arm_HV;
                    case 4    -> ItemList.Robot_Arm_EV;
                    case 5    -> ItemList.Robot_Arm_IV;
                    case 6    -> ItemList.Robot_Arm_LuV;
                    case 7    -> ItemList.Robot_Arm_ZPM;
                    case 8    -> ItemList.Robot_Arm_UV;
                    case 9    -> ItemList.Robot_Arm_UHV;
                    case 10   -> ItemList.Robot_Arm_UEV;
                    case 11   -> ItemList.Robot_Arm_UIV;
                    case 12   -> ItemList.Robot_Arm_UMV;
                    case 13   -> ItemList.Robot_Arm_UXV;
                    default   ->  ItemList.Robot_Arm_MAX;
                };

                case PUMP -> switch (machineTier) {
                    case 0, 1 -> ItemList.Electric_Pump_LV;
                    case 2    -> ItemList.Electric_Pump_MV;
                    case 3    -> ItemList.Electric_Pump_HV;
                    case 4    -> ItemList.Electric_Pump_EV;
                    case 5    -> ItemList.Electric_Pump_IV;
                    case 6    -> ItemList.Electric_Pump_LuV;
                    case 7    -> ItemList.Electric_Pump_ZPM;
                    case 8    -> ItemList.Electric_Pump_UV;
                    case 9    -> ItemList.Electric_Pump_UHV;
                    case 10   -> ItemList.Electric_Pump_UEV;
                    case 11   -> ItemList.Electric_Pump_UIV;
                    case 12   -> ItemList.Electric_Pump_UMV;
                    case 13   -> ItemList.Electric_Pump_UXV;
                    default   -> ItemList.Electric_Pump_MAX;
                };

                case MOTOR -> switch (machineTier) {
                    case 0, 1 -> ItemList.Electric_Motor_LV;
                    case 2    -> ItemList.Electric_Motor_MV;
                    case 3    -> ItemList.Electric_Motor_HV;
                    case 4    -> ItemList.Electric_Motor_EV;
                    case 5    -> ItemList.Electric_Motor_IV;
                    case 6    -> ItemList.Electric_Motor_LuV;
                    case 7    -> ItemList.Electric_Motor_ZPM;
                    case 8    -> ItemList.Electric_Motor_UV;
                    case 9    -> ItemList.Electric_Motor_UHV;
                    case 10   -> ItemList.Electric_Motor_UEV;
                    case 11   -> ItemList.Electric_Motor_UIV;
                    case 12   -> ItemList.Electric_Motor_UMV;
                    case 13   -> ItemList.Electric_Motor_UXV;
                    default   -> ItemList.Electric_Motor_MAX;
                };

                case PISTON -> switch (machineTier) {
                    case 0, 1 -> ItemList.Electric_Piston_LV;
                    case 2    -> ItemList.Electric_Piston_MV;
                    case 3    -> ItemList.Electric_Piston_HV;
                    case 4    -> ItemList.Electric_Piston_EV;
                    case 5    -> ItemList.Electric_Piston_IV;
                    case 6    -> ItemList.Electric_Piston_LuV;
                    case 7    -> ItemList.Electric_Piston_ZPM;
                    case 8    -> ItemList.Electric_Piston_UV;
                    case 9    -> ItemList.Electric_Piston_UHV;
                    case 10   -> ItemList.Electric_Piston_UEV;
                    case 11   -> ItemList.Electric_Piston_UIV;
                    case 12   -> ItemList.Electric_Piston_UMV;
                    case 13   -> ItemList.Electric_Piston_UXV;
                    default   -> ItemList.Electric_Piston_MAX;
                };

                case CONVEYOR -> switch (machineTier) {
                    case 0, 1 -> ItemList.Conveyor_Module_LV;
                    case 2    -> ItemList.Conveyor_Module_MV;
                    case 3    -> ItemList.Conveyor_Module_HV;
                    case 4    -> ItemList.Conveyor_Module_EV;
                    case 5    -> ItemList.Conveyor_Module_IV;
                    case 6    -> ItemList.Conveyor_Module_LuV;
                    case 7    -> ItemList.Conveyor_Module_ZPM;
                    case 8    -> ItemList.Conveyor_Module_UV;
                    case 9    -> ItemList.Conveyor_Module_UHV;
                    case 10   -> ItemList.Conveyor_Module_UEV;
                    case 11   -> ItemList.Conveyor_Module_UIV;
                    case 12   -> ItemList.Conveyor_Module_UMV;
                    case 13   -> ItemList.Conveyor_Module_UXV;
                    default   -> ItemList.Conveyor_Module_MAX;
                };

                case EMITTER -> switch (machineTier) {
                    case 0, 1 -> ItemList.Emitter_LV;
                    case 2    -> ItemList.Emitter_MV;
                    case 3    -> ItemList.Emitter_HV;
                    case 4    -> ItemList.Emitter_EV;
                    case 5    -> ItemList.Emitter_IV;
                    case 6    -> ItemList.Emitter_LuV;
                    case 7    -> ItemList.Emitter_ZPM;
                    case 8    -> ItemList.Emitter_UV;
                    case 9    -> ItemList.Emitter_UHV;
                    case 10   -> ItemList.Emitter_UEV;
                    case 11   -> ItemList.Emitter_UIV;
                    case 12   -> ItemList.Emitter_UMV;
                    case 13   -> ItemList.Emitter_UXV;
                    default   -> ItemList.Emitter_MAX;
                };

                case SENSOR -> switch (machineTier) {
                    case 0, 1 -> ItemList.Sensor_LV;
                    case 2    -> ItemList.Sensor_MV;
                    case 3    -> ItemList.Sensor_HV;
                    case 4    -> ItemList.Sensor_EV;
                    case 5    -> ItemList.Sensor_IV;
                    case 6    -> ItemList.Sensor_LuV;
                    case 7    -> ItemList.Sensor_ZPM;
                    case 8    -> ItemList.Sensor_UV;
                    case 9    -> ItemList.Sensor_UHV;
                    case 10   -> ItemList.Sensor_UEV;
                    case 11   -> ItemList.Sensor_UIV;
                    case 12   -> ItemList.Sensor_UMV;
                    case 13   -> ItemList.Sensor_UXV;
                    default   -> ItemList.Sensor_MAX;
                };

                case FIELD_GENERATOR -> switch (machineTier) {
                    case 0, 1 -> ItemList.Field_Generator_LV;
                    case 2    -> ItemList.Field_Generator_MV;
                    case 3    -> ItemList.Field_Generator_HV;
                    case 4    -> ItemList.Field_Generator_EV;
                    case 5    -> ItemList.Field_Generator_IV;
                    case 6    -> ItemList.Field_Generator_LuV;
                    case 7    -> ItemList.Field_Generator_ZPM;
                    case 8    -> ItemList.Field_Generator_UV;
                    case 9    -> ItemList.Field_Generator_UHV;
                    case 10   -> ItemList.Field_Generator_UEV;
                    case 11   -> ItemList.Field_Generator_UIV;
                    case 12   -> ItemList.Field_Generator_UMV;
                    case 13   -> ItemList.Field_Generator_UXV;
                    default   -> ItemList.Field_Generator_MAX;
                };

                case ROTOR -> switch (machineTier) {
                    case 0, 1 -> OrePrefixes.rotor.get(Materials.Tin);
                    case 2    -> OrePrefixes.rotor.get(Materials.Bronze);
                    case 3    -> OrePrefixes.rotor.get(Materials.Steel);
                    case 4    -> OrePrefixes.rotor.get(Materials.StainlessSteel);
                    case 5    -> OrePrefixes.rotor.get(Materials.TungstenSteel);
                    case 6    -> OrePrefixes.rotor.get(ExternalMaterials.getRhodiumPlatedPalladium());
                    case 7    -> OrePrefixes.rotor.get(Materials.Iridium);
                    default   -> OrePrefixes.rotor.get(Materials.Osmium);
                };

                default -> throw new IllegalArgumentException("MISSING TIER MAPPING FOR: " + aRecipe[i] + " AT TIER " + machineTier);
            };
            // spotless:on
        }

        if (!GTModHandler.addCraftingRecipe(aResult, aBitMask, aRecipe)) {
            throw new IllegalArgumentException("INVALID CRAFTING RECIPE FOR: " + aResult.getDisplayName());
        }
    }

    /**
     * Internal realisation of the Crafting Recipe adding Process.
     */
    private static boolean addCraftingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
        int[] aEnchantmentLevelsAdded, boolean aMirrored, boolean aBuffered, boolean aKeepNBT, boolean aDismantleable,
        boolean aRemovable, boolean aReversible, boolean aRemoveAllOthersWithSameOutput,
        boolean aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT, boolean aRemoveAllOtherShapedsWithSameOutput,
        boolean aRemoveAllOtherNativeRecipes, boolean aCheckForCollisions,
        boolean aOnlyAddIfThereIsAnyRecipeOutputtingThis, boolean aOnlyAddIfResultIsNotNull, Object[] aRecipe) {

        aResult = GTOreDictUnificator.get(true, aResult);
        if (aOnlyAddIfResultIsNotNull && aResult == null) return false;
        if (aResult != null && Items.feather.getDamage(aResult) == WILDCARD) Items.feather.setDamage(aResult, 0);
        if (aRecipe == null || aRecipe.length == 0) return false;

        // The renamed variable clarifies what's happening
        // noinspection UnnecessaryLocalVariable
        boolean tDoWeCareIfThereWasARecipe = aOnlyAddIfThereIsAnyRecipeOutputtingThis;
        boolean tThereWasARecipe = false;

        for (byte i = 0; i < aRecipe.length; i++) {
            if (aRecipe[i] instanceof IItemContainer) aRecipe[i] = ((IItemContainer) aRecipe[i]).get(1);
            else if (aRecipe[i] instanceof Enum) aRecipe[i] = ((Enum<?>) aRecipe[i]).name();
            else if (!(aRecipe[i] == null || aRecipe[i] instanceof ItemStack
                || aRecipe[i] instanceof ItemData
                || aRecipe[i] instanceof String
                || aRecipe[i] instanceof Character)) aRecipe[i] = aRecipe[i].toString();
        }

        StringBuilder shape = new StringBuilder(E);
        int idx = 0;
        if (aRecipe[idx] instanceof Boolean) {
            throw new IllegalArgumentException();
        }

        ArrayList<Object> tRecipeList = new ArrayList<>(Arrays.asList(aRecipe));

        while (aRecipe[idx] instanceof String) {
            StringBuilder s = new StringBuilder((String) aRecipe[idx++]);
            shape.append(s);
            while (s.length() < 3) s.append(" ");
            if (s.length() > 3) throw new IllegalArgumentException();

            for (char c : s.toString()
                .toCharArray()) {
                switch (c) {
                    case 'b' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolBlade.name());
                    }
                    case 'c' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolCrowbar.name());
                    }
                    case 'd' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolScrewdriver.name());
                    }
                    case 'f' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolFile.name());
                    }
                    case 'h' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolHardHammer.name());
                    }
                    case 'i' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolSolderingIron.name());
                    }
                    case 'j' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolSolderingMetal.name());
                    }
                    case 'k' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolKnife.name());
                    }
                    case 'm' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolMortar.name());
                    }
                    case 'p' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolDrawplate.name());
                    }
                    case 'r' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolSoftMallet.name());
                    }
                    case 's' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolSaw.name());
                    }
                    case 'w' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolWrench.name());
                    }
                    case 'x' -> {
                        tRecipeList.add(c);
                        tRecipeList.add(ToolDictNames.craftingToolWireCutter.name());
                    }
                }
            }
        }

        aRecipe = tRecipeList.toArray();

        if (aRecipe[idx] instanceof Boolean) {
            idx++;
        }
        Map<Character, ItemStack> tItemStackMap = new HashMap<>();
        Map<Character, ItemData> tItemDataMap = new HashMap<>();
        tItemStackMap.put(' ', null);

        boolean tRemoveRecipe = true;

        for (; idx < aRecipe.length; idx += 2) {
            if (aRecipe[idx] == null || aRecipe[idx + 1] == null) {
                if (D1) {
                    GTLog.err.println(
                        "WARNING: Missing Item for shaped Recipe: "
                            + (aResult == null ? "null" : aResult.getDisplayName()));
                    for (Object tContent : aRecipe) GTLog.err.println(tContent);
                }
                return false;
            }
            Character chr = (Character) aRecipe[idx];
            Object in = aRecipe[idx + 1];
            if (in instanceof ItemStack is) {
                tItemStackMap.put(chr, GTUtility.copyOrNull(is));
                tItemDataMap.put(chr, GTOreDictUnificator.getItemData(is));
            } else if (in instanceof ItemData) {
                String tString = in.toString();
                switch (tString) {
                    case "plankWood" -> tItemDataMap.put(chr, new ItemData(Materials.Wood, M));
                    case "stoneNetherrack" -> tItemDataMap.put(chr, new ItemData(Materials.Netherrack, M));
                    case "stoneObsidian" -> tItemDataMap.put(chr, new ItemData(Materials.Obsidian, M));
                    case "stoneEndstone" -> tItemDataMap.put(chr, new ItemData(Materials.Endstone, M));
                    default -> tItemDataMap.put(chr, (ItemData) in);
                }
                ItemStack tStack = GTOreDictUnificator.getFirstOre(in, 1);
                if (tStack == null) tRemoveRecipe = false;
                else tItemStackMap.put(chr, tStack);
                in = aRecipe[idx + 1] = in.toString();
            } else if (in instanceof String) {
                if (in.equals(OreDictNames.craftingChest.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Wood, M * 8));
                else if (in.equals(OreDictNames.craftingBook.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Paper, M * 3));
                else if (in.equals(OreDictNames.craftingPiston.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Stone, M * 4, Materials.Wood, M * 3));
                else if (in.equals(OreDictNames.craftingFurnace.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Stone, M * 8));
                else if (in.equals(OreDictNames.craftingIndustrialDiamond.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Diamond, M));
                else if (in.equals(OreDictNames.craftingAnvil.toString()))
                    tItemDataMap.put(chr, new ItemData(Materials.Iron, M * 10));
                ItemStack tStack = GTOreDictUnificator.getFirstOre(in, 1);
                if (tStack == null) tRemoveRecipe = false;
                else tItemStackMap.put(chr, tStack);
            } else {
                throw new IllegalArgumentException();
            }
        }

        if (aReversible && aResult != null) {
            ItemData[] tData = new ItemData[9];
            int x = -1;
            for (char chr : shape.toString()
                .toCharArray()) tData[++x] = tItemDataMap.get(chr);
            if (GTUtility.arrayContainsNonNull(tData)) GTOreDictUnificator.addItemData(aResult, new ItemData(tData));
        }

        if (aCheckForCollisions && tRemoveRecipe) {
            ItemStack[] tRecipe = new ItemStack[9];
            int x = -1;
            for (char chr : shape.toString()
                .toCharArray()) {
                tRecipe[++x] = tItemStackMap.get(chr);
                if (tRecipe[x] != null && Items.feather.getDamage(tRecipe[x]) == WILDCARD)
                    Items.feather.setDamage(tRecipe[x], 0);
            }
            if (tDoWeCareIfThereWasARecipe || !aBuffered) tThereWasARecipe = removeRecipe(tRecipe) != null;
            else removeRecipeDelayed(tRecipe);
        }

        if (aResult == null || aResult.stackSize <= 0) return false;

        if (aRemoveAllOthersWithSameOutput || aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT
            || aRemoveAllOtherShapedsWithSameOutput
            || aRemoveAllOtherNativeRecipes) {
            if (tDoWeCareIfThereWasARecipe || !aBuffered) tThereWasARecipe = removeRecipeByOutput(
                aResult,
                !aRemoveAllOthersWithSameOutputIfTheyHaveSameNBT,
                aRemoveAllOtherShapedsWithSameOutput,
                aRemoveAllOtherNativeRecipes) || tThereWasARecipe;
            else removeRecipeByOutputDelayed(aResult);
        }

        if (aOnlyAddIfThereIsAnyRecipeOutputtingThis && !tDoWeCareIfThereWasARecipe && !tThereWasARecipe) {
            ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
                .getRecipeList();
            int tList_sS = tList.size();
            for (int i = 0; i < tList_sS && !tThereWasARecipe; i++) {
                IRecipe tRecipe = tList.get(i);
                if (sSpecialRecipeClasses.contains(
                    tRecipe.getClass()
                        .getName()))
                    continue;
                if (GTUtility.areStacksEqual(GTOreDictUnificator.get(tRecipe.getRecipeOutput()), aResult, true)) {
                    tList.remove(i--);
                    tList_sS = tList.size();
                    tThereWasARecipe = true;
                }
            }
        }

        if (Items.feather.getDamage(aResult) == WILDCARD || Items.feather.getDamage(aResult) < 0)
            Items.feather.setDamage(aResult, 0);

        GTUtility.updateItemStack(aResult);

        if (tThereWasARecipe || !aOnlyAddIfThereIsAnyRecipeOutputtingThis) {
            if (sBufferCraftingRecipes && aBuffered) sBufferRecipeList.add(
                new GTShapedRecipe(
                    GTUtility.copyOrNull(aResult),
                    aRemovable,
                    aKeepNBT,
                    aEnchantmentsAdded,
                    aEnchantmentLevelsAdded,
                    aRecipe).setMirrored(aMirrored));
            else GameRegistry.addRecipe(
                new GTShapedRecipe(
                    GTUtility.copyOrNull(aResult),
                    aRemovable,
                    aKeepNBT,
                    aEnchantmentsAdded,
                    aEnchantmentLevelsAdded,
                    aRecipe).setMirrored(aMirrored));
        }
        return true;
    }

    /**
     * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
     */
    public static boolean addShapelessCraftingRecipe(ItemStack aResult, Object[] aRecipe) {
        return addShapelessCraftingRecipe(
            aResult,
            RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | RecipeBits.BUFFERED,
            aRecipe);
    }

    /**
     * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
     */
    public static boolean addShapelessCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
        return addShapelessCraftingRecipe(
            aResult,
            null,
            null,
            (aBitMask & RecipeBits.BUFFERED) != 0,
            (aBitMask & RecipeBits.KEEPNBT) != 0,
            (aBitMask & RecipeBits.DISMANTLEABLE) != 0,
            (aBitMask & RecipeBits.NOT_REMOVABLE) == 0,
            (aBitMask & RecipeBits.OVERWRITE_NBT) != 0,
            aRecipe);
    }

    /**
     * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
     */
    private static boolean addShapelessCraftingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
        int[] aEnchantmentLevelsAdded, boolean aBuffered, boolean aKeepNBT, boolean aDismantleable, boolean aRemovable,
        boolean overwriteNBT, Object[] aRecipe) {
        aResult = GTOreDictUnificator.get(true, aResult);
        if (aRecipe == null || aRecipe.length == 0) return false;
        for (byte i = 0; i < aRecipe.length; i++) {
            if (aRecipe[i] instanceof IItemContainer) aRecipe[i] = ((IItemContainer) aRecipe[i]).get(1);
            else if (aRecipe[i] instanceof Enum) aRecipe[i] = ((Enum<?>) aRecipe[i]).name();
            else if (!(aRecipe[i] == null || aRecipe[i] instanceof ItemStack
                || aRecipe[i] instanceof String
                || aRecipe[i] instanceof Character)) aRecipe[i] = aRecipe[i].toString();
        }

        ItemStack[] tRecipe = new ItemStack[9];
        int i = 0;
        for (Object tObject : aRecipe) {
            if (tObject == null) {
                if (D1) GTLog.err.println(
                    "WARNING: Missing Item for shapeless Recipe: "
                        + (aResult == null ? "null" : aResult.getDisplayName()));
                for (Object tContent : aRecipe) GTLog.err.println(tContent);
                return false;
            }
            if (tObject instanceof ItemStack) {
                tRecipe[i] = (ItemStack) tObject;
            } else if (tObject instanceof String) {
                tRecipe[i] = GTOreDictUnificator.getFirstOre(tObject, 1);
                if (tRecipe[i] == null) break;
            }
            i++;
        }
        if (sBufferCraftingRecipes && aBuffered) removeRecipeDelayed(tRecipe);
        else removeRecipe(tRecipe);

        if (aResult == null || aResult.stackSize <= 0) return false;

        if (Items.feather.getDamage(aResult) == WILDCARD || Items.feather.getDamage(aResult) < 0)
            Items.feather.setDamage(aResult, 0);

        GTUtility.updateItemStack(aResult);

        if (sBufferCraftingRecipes && aBuffered) sBufferRecipeList.add(
            new GTShapelessRecipe(
                GTUtility.copyOrNull(aResult),
                aRemovable,
                aKeepNBT,
                overwriteNBT,
                aEnchantmentsAdded,
                aEnchantmentLevelsAdded,
                aRecipe));
        else GameRegistry.addRecipe(
            new GTShapelessRecipe(
                GTUtility.copyOrNull(aResult),
                aRemovable,
                aKeepNBT,
                overwriteNBT,
                aEnchantmentsAdded,
                aEnchantmentLevelsAdded,
                aRecipe));
        return true;
    }

    /**
     * Removes a Smelting Recipe
     */
    public static boolean removeFurnaceSmelting(ItemStack aInput) {
        if (aInput != null) {
            for (ItemStack tInput : FurnaceRecipes.smelting()
                .getSmeltingList()
                .keySet()) {
                if (GTUtility.isStackValid(tInput) && GTUtility.areStacksEqual(aInput, tInput, true)) {
                    FurnaceRecipes.smelting()
                        .getSmeltingList()
                        .remove(tInput);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a Crafting Recipe and gives you the former output of it.
     *
     * @param aRecipe The content of the Crafting Grid as ItemStackArray with length 9
     * @return the output of the old Recipe or null if there was nothing.
     */
    public static ItemStack removeRecipe(ItemStack... aRecipe) {
        if (aRecipe == null) return null;
        if (isAllNulls(aRecipe)) return null;

        ItemStack rReturn = null;
        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < aRecipe.length && i < 9; i++) {
            aCrafting.setInventorySlotContents(i, aRecipe[i]);
        }
        ArrayList<IRecipe> allRecipes = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        int size = allRecipes.size();
        for (int i = 0; i < size; i++) {
            for (; i < size; i++) {
                if ((!(allRecipes.get(i) instanceof IGTCraftingRecipe)
                    || ((IGTCraftingRecipe) allRecipes.get(i)).isRemovable()) && allRecipes.get(i)
                        .matches(aCrafting, DW)) {
                    rReturn = allRecipes.get(i)
                        .getCraftingResult(aCrafting);
                    if (rReturn != null) allRecipes.remove(i--);
                    size = allRecipes.size();
                }
            }
        }
        return rReturn;
    }

    public static void removeRecipeDelayed(ItemStack... aRecipe) {
        if (!sBufferCraftingRecipes) {
            removeRecipe(aRecipe);
            return;
        }

        if (aRecipe == null) return;
        if (isAllNulls(aRecipe)) return;

        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < aRecipe.length && i < 9; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        delayedRemovalByRecipe.add(aCrafting);
    }

    public static void bulkRemoveByRecipe(List<InventoryCrafting> toRemove) {
        ArrayList<IRecipe> allRecipes = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        GT_FML_LOGGER.info("BulkRemoveByRecipe: allRecipes: " + allRecipes.size() + " toRemove: " + toRemove.size());

        Set<IRecipe> tListToRemove = allRecipes.parallelStream()
            .filter(tRecipe -> {
                if ((tRecipe instanceof IGTCraftingRecipe) && !((IGTCraftingRecipe) tRecipe).isRemovable())
                    return false;
                return toRemove.stream()
                    .anyMatch(aCrafting -> tRecipe.matches(aCrafting, DW));
            })
            .collect(Collectors.toSet());

        allRecipes.removeIf(tListToRemove::contains);
    }

    public static boolean removeRecipeByOutputDelayed(ItemStack aOutput) {
        if (sBufferCraftingRecipes) return delayedRemovalByOutput.add(aOutput);
        else return removeRecipeByOutput(aOutput);
    }

    public static boolean removeRecipeByOutputDelayed(ItemStack aOutput, boolean aIgnoreNBT,
        boolean aNotRemoveShapelessRecipes, boolean aOnlyRemoveNativeHandlers) {
        if (sBufferCraftingRecipes && (aIgnoreNBT && !aNotRemoveShapelessRecipes && !aOnlyRemoveNativeHandlers))
            // Too lazy to handle deferred versions of the parameters that aren't used very often
            return delayedRemovalByOutput.add(aOutput);
        else return removeRecipeByOutput(aOutput, aIgnoreNBT, aNotRemoveShapelessRecipes, aOnlyRemoveNativeHandlers);
    }

    public static boolean removeRecipeByOutput(ItemStack aOutput) {
        return removeRecipeByOutput(aOutput, true, false, false);
    }

    /**
     * Removes a Crafting Recipe.
     *
     * @param output The output of the Recipe.
     * @return if it has removed at least one Recipe.
     */
    public static boolean removeRecipeByOutput(ItemStack output, boolean aIgnoreNBT, boolean aNotRemoveShapelessRecipes,
        boolean aOnlyRemoveNativeHandlers) {
        if (output == null) return false;
        boolean rReturn = false;
        final ArrayList<IRecipe> allRecipes = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        output = GTOreDictUnificator.get(output);
        int size = allRecipes.size();
        for (int i = 0; i < size; i++) {
            final IRecipe recipe = allRecipes.get(i);
            if (aNotRemoveShapelessRecipes
                && (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe)) {
                continue;
            }
            if (aOnlyRemoveNativeHandlers) {
                if (!sNativeRecipeClasses.contains(
                    recipe.getClass()
                        .getName()))
                    continue;
            } else {
                if (sSpecialRecipeClasses.contains(
                    recipe.getClass()
                        .getName()))
                    continue;
            }
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (!(recipe instanceof IGTCraftingRecipe) || ((IGTCraftingRecipe) recipe).isRemovable()) {
                if (GTUtility.areStacksEqual(GTOreDictUnificator.get_nocopy(recipeOutput), output, aIgnoreNBT)) {
                    allRecipes.remove(i--);
                    size = allRecipes.size();
                    rReturn = true;
                }
            }
        }
        return rReturn;
    }

    public static boolean bulkRemoveRecipeByOutput(List<ItemStack> toRemove) {
        ArrayList<IRecipe> allRecipes = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();

        Set<ItemStack> setToRemove = toRemove.parallelStream()
            .map(GTOreDictUnificator::get_nocopy)
            .collect(Collectors.toSet());

        GT_FML_LOGGER
            .info("BulkRemoveRecipeByOutput: allRecipes: " + allRecipes.size() + " setToRemove: " + setToRemove.size());

        Set<IRecipe> tListToRemove = allRecipes.parallelStream()
            .filter(tRecipe -> {
                if ((tRecipe instanceof IGTCraftingRecipe) && !((IGTCraftingRecipe) tRecipe).isRemovable())
                    return false;
                if (sSpecialRecipeClasses.contains(
                    tRecipe.getClass()
                        .getName()))
                    return false;
                final ItemStack tStack = GTOreDictUnificator.get_nocopy(tRecipe.getRecipeOutput());
                return setToRemove.stream()
                    .anyMatch(aOutput -> GTUtility.areStacksEqual(tStack, aOutput, true));
            })
            .collect(Collectors.toSet());

        allRecipes.removeIf(tListToRemove::contains);
        return true;
    }

    /**
     * Checks all Crafting Handlers for Recipe Output Used for the Autocrafting Table
     */
    public static ItemStack getAllRecipeOutput(World aWorld, ItemStack... aRecipe) {
        if (aRecipe == null || aRecipe.length == 0) return null;

        if (aWorld == null) aWorld = DW;

        boolean temp = false;
        for (ItemStack itemStack : aRecipe) {
            if (itemStack != null) {
                temp = true;
                break;
            }
        }
        if (!temp) return null;
        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        List<IRecipe> allRecipes = CraftingManager.getInstance()
            .getRecipeList();
        synchronized (sAllRecipeList) {
            if (sAllRecipeList.size() != allRecipes.size()) {
                sAllRecipeList.clear();
                sAllRecipeList.addAll(allRecipes);
            }
            for (int i = 0, j = sAllRecipeList.size(); i < j; i++) {
                IRecipe tRecipe = sAllRecipeList.get(i);
                if (tRecipe.matches(aCrafting, aWorld)) {
                    if (i > 10) {
                        sAllRecipeList.remove(i);
                        sAllRecipeList.add(i - 10, tRecipe);
                    }
                    return tRecipe.getCraftingResult(aCrafting);
                }
            }
        }

        int tIndex = 0;
        ItemStack tStack1 = null, tStack2 = null;
        for (int i = 0, j = aCrafting.getSizeInventory(); i < j; i++) {
            ItemStack tStack = aCrafting.getStackInSlot(i);
            if (tStack != null) {
                if (tIndex == 0) tStack1 = tStack;
                if (tIndex == 1) tStack2 = tStack;
                tIndex++;
            }
        }

        if (tIndex == 2 && tStack2 != null) {
            if (tStack1.getItem() == tStack2.getItem() && tStack1.stackSize == 1
                && tStack2.stackSize == 1
                && tStack1.getItem()
                    .isRepairable()) {
                int tNewDamage = tStack1.getMaxDamage() + tStack1.getItemDamage()
                    - tStack2.getItemDamage()
                    + tStack1.getMaxDamage() / 20;
                return new ItemStack(tStack1.getItem(), 1, Math.max(tNewDamage, 0));
            }
        }

        return null;
    }

    /**
     * Gives you a copy of the Output from a Crafting Recipe Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(ItemStack... aRecipe) {
        return getRecipeOutput(false, false, aRecipe);
    }

    /**
     * Gives you a copy of the Output from a Crafting Recipe Used for Recipe Detection. If available, will choose a
     * recipe that wasn't auto generated during OreDictionary registration. The OreDict recipe is still chosen if it is
     * the only one that exists.
     * <p>
     * For example: Many Planks -> Slab recipes may have a recipe to the corresponding slab, but also have another that
     * results in Minecraft's default Oak Slab. This second recipe is generated automatically when the plank is
     * registered as 'plankWood' in the OreDict. This method will select the former, regardless of the order they appear
     * on the list.
     */
    public static ItemStack getRecipeOutputPreferNonOreDict(ItemStack... aRecipe) {
        return getRecipeOutput(false, true, aRecipe);
    }

    public static ItemStack getRecipeOutput(boolean aUncopiedStack, ItemStack... aRecipe) {
        return getRecipeOutput(aUncopiedStack, false, aRecipe);
    }

    /**
     * Gives you a copy of the Output from a Crafting Recipe Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(boolean aUncopiedStack, boolean aPreferNonOreDict, ItemStack... aRecipe) {
        if (aRecipe == null || isAllNulls(aRecipe)) return null;

        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();

        boolean tOreDictRecipeFound = false;
        ItemStack tOreDictOutput = null;

        for (IRecipe iRecipe : tList) {
            if (iRecipe.matches(aCrafting, DW)) {
                ItemStack tOutput = aUncopiedStack ? iRecipe.getRecipeOutput() : iRecipe.getCraftingResult(aCrafting);

                if (aPreferNonOreDict && iRecipe instanceof ShapedOreRecipe) {
                    if (!tOreDictRecipeFound) {
                        tOreDictOutput = tOutput;
                        tOreDictRecipeFound = true;
                    }
                    continue;
                }

                if (tOutput == null || tOutput.stackSize <= 0) {
                    // Seriously, who would ever do that shit?
                    if (!GregTechAPI.sPostloadFinished) throw new GTItsNotMyFaultException(
                        "Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
                }

                if (aUncopiedStack) return tOutput;
                return GTUtility.copyOrNull(tOutput);
            }
        }

        if (!tOreDictRecipeFound) return null;

        if (tOreDictOutput == null || tOreDictOutput.stackSize <= 0) {
            // Seriously, who would ever do that shit?
            if (!GregTechAPI.sPostloadFinished) throw new GTItsNotMyFaultException(
                "Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
        }

        if (aUncopiedStack) return tOreDictOutput;
        return GTUtility.copyOrNull(tOreDictOutput);
    }

    private static List<IRecipe> bufferedRecipes = null;

    /**
     * Gives you a list of the Outputs from a Crafting Recipe If you have multiple Mods, which add Bronze Armor for
     * example Buffers a List which only has armor-alike crafting in it
     */
    public static List<ItemStack> getRecipeOutputsBuffered(ItemStack... aRecipe) {

        if (bufferedRecipes == null) bufferedRecipes = CraftingManager.getInstance()
            .getRecipeList()
            .stream()
            .filter(
                tRecipe -> !(tRecipe instanceof ShapelessRecipes) && !(tRecipe instanceof ShapelessOreRecipe)
                    && !(tRecipe instanceof IGTCraftingRecipe))
            .filter(tRecipe -> {
                try {
                    ItemStack tOutput = tRecipe.getRecipeOutput();
                    if (tOutput.stackSize == 1 && tOutput.getMaxDamage() > 0 && tOutput.getMaxStackSize() == 1) {
                        return true;
                    }
                } catch (Exception ignored) {}
                return false;
            })
            .collect(Collectors.toList());
        return getRecipeOutputs(bufferedRecipes, false, aRecipe);
    }

    /**
     * Gives you a list of the Outputs from a Crafting Recipe If you have multiple Mods, which add Bronze Armor for
     * example
     */
    public static List<ItemStack> getRecipeOutputs(List<IRecipe> aList, boolean aDeleteFromList, ItemStack... aRecipe) {
        List<ItemStack> rList = new ArrayList<>();
        if (aRecipe == null || isAllNulls(aRecipe)) return rList;
        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        if (!aDeleteFromList) {
            HashSet<ItemStack> stacks = new HashSet<>();
            aList.stream()
                .filter(tRecipe -> {
                    if (tRecipe instanceof ShapelessRecipes || tRecipe instanceof ShapelessOreRecipe
                        || tRecipe instanceof IGTCraftingRecipe) return false;
                    return tRecipe.matches(aCrafting, DW);
                })
                .forEach(tRecipe -> stacks.add(tRecipe.getCraftingResult(aCrafting)));
            rList = stacks.stream()
                .filter(
                    tOutput -> tOutput.stackSize == 1 && tOutput.getMaxDamage() > 0 && tOutput.getMaxStackSize() == 1)
                .collect(Collectors.toList());
        } else for (Iterator<IRecipe> iterator = aList.iterator(); iterator.hasNext();) {
            IRecipe tRecipe = iterator.next();

            if (tRecipe.matches(aCrafting, DW)) {
                ItemStack tOutput = tRecipe.getCraftingResult(aCrafting);

                if (tOutput == null || tOutput.stackSize <= 0) {
                    // Seriously, who would ever do that shit?
                    if (!GregTechAPI.sPostloadFinished) throw new GTItsNotMyFaultException(
                        "Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
                    continue;
                }
                if (tOutput.stackSize != 1) continue;
                if (tOutput.getMaxDamage() <= 0) continue;
                if (tOutput.getMaxStackSize() != 1) continue;
                if (tRecipe instanceof ShapelessRecipes) continue;
                if (tRecipe instanceof ShapelessOreRecipe) continue;
                if (tRecipe instanceof IGTCraftingRecipe) continue;
                rList.add(GTUtility.copyOrNull(tOutput));
                iterator.remove();
            }
        }
        return rList;
    }

    private static boolean isAllNulls(ItemStack[] aRecipe) {
        for (ItemStack stack : aRecipe) {
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used in my own Furnace.
     */
    public static ItemStack getSmeltingOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
        if (aInput == null || aInput.stackSize < 1) return null;
        ItemStack rStack = GTOreDictUnificator.get(
            FurnaceRecipes.smelting()
                .getSmeltingResult(aInput));

        if (rStack != null && (aOutputSlot == null || (GTUtility.areStacksEqual(rStack, aOutputSlot)
            && rStack.stackSize + aOutputSlot.stackSize <= aOutputSlot.getMaxStackSize()))) {
            if (aRemoveInput) aInput.stackSize--;
            return rStack;
        }
        return null;
    }

    public static ItemStack getEFRBlastingOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
        if (aInput == null || aInput.stackSize < 1) return null;
        ItemStack rStack = GTOreDictUnificator.get(
            BlastFurnaceRecipes.smelting()
                .getSmeltingResult(aInput));

        if (rStack != null && (aOutputSlot == null || (GTUtility.areStacksEqual(rStack, aOutputSlot)
            && rStack.stackSize + aOutputSlot.stackSize <= aOutputSlot.getMaxStackSize()))) {
            if (aRemoveInput) aInput.stackSize--;
            return rStack;
        }
        return null;
    }

    public static ItemStack getEFRSmokingOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
        if (aInput == null || aInput.stackSize < 1) return null;
        ItemStack rStack = GTOreDictUnificator.get(
            SmokerRecipes.smelting()
                .getSmeltingResult(aInput));

        if (rStack != null && (aOutputSlot == null || (GTUtility.areStacksEqual(rStack, aOutputSlot)
            && rStack.stackSize + aOutputSlot.stackSize <= aOutputSlot.getMaxStackSize()))) {
            if (aRemoveInput) aInput.stackSize--;
            return rStack;
        }
        return null;
    }

    /**
     * Used in my own Recycler.
     * <p/>
     * Only produces Scrap if aScrapChance == 0. aScrapChance is usually the random Number I give to the Function If you
     * directly insert 0 as aScrapChance then you can check if its Recycler-Blacklisted or similar
     */
    @Nullable
    public static ItemStack getRecyclerOutput(ItemStack aInput, int aScrapChance) {
        if (aInput == null || aScrapChance != 0) return null;
        if (recyclerWhitelist == null) {
            generateRecyclerCache();
        }

        if (recyclerWhitelist.isEmpty()) {
            if (searchRecyclerCache(aInput, recyclerBlacklist)) {
                return null;
            } else {
                return ItemList.IC2_Scrap.get(1);
            }
        } else {
            if (searchRecyclerCache(aInput, recyclerWhitelist)) {
                return ItemList.IC2_Scrap.get(1);
            } else {
                return null;
            }
        }
    }

    private static void generateRecyclerCache() {
        recyclerWhitelist = new HashSet<>();
        for (IRecipeInput input : Recipes.recyclerWhitelist) {
            for (ItemStack stack : input.getInputs()) {
                recyclerWhitelist.add(GTUtility.ItemId.create(stack.getItem(), stack.getItemDamage(), null));
            }
        }
        recyclerBlacklist = new HashSet<>();
        for (IRecipeInput input : Recipes.recyclerBlacklist) {
            for (ItemStack stack : input.getInputs()) {
                recyclerBlacklist.add(GTUtility.ItemId.create(stack.getItem(), stack.getItemDamage(), null));
            }
        }
    }

    private static boolean searchRecyclerCache(ItemStack stack, Set<GTUtility.ItemId> set) {
        if (set.contains(GTUtility.ItemId.createWithoutNBT(stack))) {
            return true;
        }
        // ic2.api.recipe.RecipeInputItemStack#matches expects item with wildcard meta to accept arbitrary meta
        return set.contains(GTUtility.ItemId.createAsWildcard(stack));
    }

    /**
     * For the Scrapboxinator
     */
    public static ItemStack getRandomScrapboxDrop() {
        return Recipes.scrapboxDrops.getDrop(ItemList.IC2_Scrapbox.get(1), false);
    }

    /**
     * Charges an Electric Item. Only if it's a valid Electric Item of course. This forces the Usage of proper Voltages
     * (so not the transfer limits defined by the Items) unless you ignore the Transfer Limit. If aTier is
     * Integer.MAX_VALUE it will ignore Tier based Limitations.
     *
     * @return the actually used Energy.
     */
    public static int chargeElectricItem(ItemStack aStack, int aCharge, int aTier, boolean aIgnoreLimit,
        boolean aSimulate) {
        if (isElectricItem(aStack)) {
            int tTier = ((ic2.api.item.IElectricItem) aStack.getItem()).getTier(aStack);
            if (tTier < 0 || tTier == aTier || aTier == Integer.MAX_VALUE) {
                if (!aIgnoreLimit && tTier >= 0)
                    aCharge = (int) Math.min(aCharge, V[Math.max(0, Math.min(V.length - 1, tTier))]);
                if (aCharge > 0) {
                    int rCharge = (int) Math
                        .max(0.0, ic2.api.item.ElectricItem.manager.charge(aStack, aCharge, tTier, true, aSimulate));
                    return rCharge + (rCharge * 4 > aTier ? aTier : 0);
                }
            }
        }
        return 0;
    }

    /**
     * Discharges an Electric Item. Only if it's a valid Electric Item for that of course. This forces the Usage of
     * proper Voltages (so not the transfer limits defined by the Items) unless you ignore the Transfer Limit. If aTier
     * is Integer.MAX_VALUE it will ignore Tier based Limitations.
     *
     * @return the Energy got from the Item.
     */
    public static int dischargeElectricItem(ItemStack aStack, int aCharge, int aTier, boolean aIgnoreLimit,
        boolean aSimulate, boolean aIgnoreDischargability) {
        if (isElectricItem(aStack)) {
            int tTier = ((ic2.api.item.IElectricItem) aStack.getItem()).getTier(aStack);
            if (tTier < 0 || tTier == aTier || aTier == Integer.MAX_VALUE) {
                if (!aIgnoreLimit && tTier >= 0) {
                    int tier = Math.max(0, Math.min(V.length - 1, tTier));
                    aCharge = (int) Math.min(aCharge, V[tier] + B[tier]);
                }
                if (aCharge > 0) {
                    int rCharge = (int) Math.max(
                        0,
                        ic2.api.item.ElectricItem.manager.discharge(
                            aStack,
                            aCharge + (aCharge * 4 > aTier ? aTier : 0),
                            tTier,
                            true,
                            !aIgnoreDischargability,
                            aSimulate));
                    return rCharge - (rCharge * 4 > aTier ? aTier : 0);
                }
            }
        }
        return 0;
    }

    /**
     * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
     *
     * @return if the action was successful
     */
    public static boolean canUseElectricItem(ItemStack aStack, int aCharge) {
        if (isElectricItem(aStack)) {
            return ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge);
        }
        return false;
    }

    /**
     * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
     *
     * @return if the action was successful
     */
    public static boolean useElectricItem(ItemStack aStack, int aCharge, EntityPlayer aPlayer) {
        if (isElectricItem(aStack)) {
            ic2.api.item.ElectricItem.manager.use(aStack, 0, aPlayer);
            if (ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge)) {
                return ic2.api.item.ElectricItem.manager.use(aStack, aCharge, aPlayer);
            }
        }
        return false;
    }

    /**
     * Uses an Item. Tries to discharge in case of Electric Items
     */
    public static boolean damageOrDechargeItem(ItemStack aStack, int aDamage, int aDecharge, EntityLivingBase aPlayer) {
        if (GTUtility.isStackInvalid(aStack) || (aStack.getMaxStackSize() <= 1 && aStack.stackSize > 1)) return false;
        if (aPlayer instanceof EntityPlayer && ((EntityPlayer) aPlayer).capabilities.isCreativeMode) return true;
        if (aStack.getItem() instanceof IDamagableItem) {
            return ((IDamagableItem) aStack.getItem()).doDamageToItem(aStack, aDamage);
        } else if (GTModHandler.isElectricItem(aStack)) {
            if (canUseElectricItem(aStack, aDecharge)) {
                if (aPlayer instanceof EntityPlayer) {
                    return GTModHandler.useElectricItem(aStack, aDecharge, (EntityPlayer) aPlayer);
                }
                return GTModHandler.dischargeElectricItem(aStack, aDecharge, Integer.MAX_VALUE, true, false, true)
                    >= aDecharge;
            }
        } else if (aStack.getItem()
            .isDamageable()) {
                if (aPlayer == null) {
                    aStack.setItemDamage(aStack.getItemDamage() + aDamage);
                } else {
                    aStack.damageItem(aDamage, aPlayer);
                }
                if (aStack.getItemDamage() >= aStack.getMaxDamage()) {
                    aStack.setItemDamage(aStack.getMaxDamage() + 1);
                    ItemStack tStack = GTUtility.getContainerItem(aStack, true);
                    if (tStack != null) {
                        aStack.func_150996_a(tStack.getItem());
                        aStack.setItemDamage(tStack.getItemDamage());
                        aStack.stackSize = tStack.stackSize;
                        aStack.setTagCompound(tStack.getTagCompound());
                    } else if (aStack.stackSize > 0) {
                        aStack.stackSize--;
                    }
                }
                return true;
            }
        return false;
    }

    /**
     * Uses a Soldering Iron from player or external inventory
     */
    public static boolean useSolderingIron(ItemStack aStack, EntityLivingBase aPlayer, IInventory aExternalInventory) {
        if (aPlayer == null || aStack == null) return false;
        if (GTUtility.isStackInList(aStack, GregTechAPI.sSolderingToolList)) {
            if (aPlayer instanceof EntityPlayer tPlayer) {
                if (tPlayer.capabilities.isCreativeMode) return true;
                if (isElectricItem(aStack) && ic2.api.item.ElectricItem.manager.getCharge(aStack) > 1000.0d) {
                    if (consumeSolderingMaterial(tPlayer)
                        || (aExternalInventory != null && consumeSolderingMaterial(aExternalInventory))) {
                        if (canUseElectricItem(aStack, 10000)) {
                            return GTModHandler.useElectricItem(aStack, 10000, (EntityPlayer) aPlayer);
                        }
                        GTModHandler.useElectricItem(
                            aStack,
                            (int) ic2.api.item.ElectricItem.manager.getCharge(aStack),
                            (EntityPlayer) aPlayer);
                        return false;
                    } else {
                        GTUtility.sendChatTrans((EntityPlayer) aPlayer, "GT5U.chat.soldering_iron.not_enough");
                    }
                }
            } else {
                damageOrDechargeItem(aStack, 1, 1000, aPlayer);
                return true;
            }
        }
        return false;
    }

    public static boolean useSolderingIron(ItemStack aStack, EntityLivingBase aPlayer) {
        return useSolderingIron(aStack, aPlayer, null);
    }

    public static boolean consumeSolderingMaterial(EntityPlayer aPlayer) {
        if (aPlayer.capabilities.isCreativeMode) return true;
        IInventory tInventory = aPlayer.inventory;
        if (consumeSolderingMaterial(tInventory)) {
            if (aPlayer.inventoryContainer != null) {
                aPlayer.inventoryContainer.detectAndSendChanges();
            }
            return true;
        }
        for (int i = 0; i < tInventory.getSizeInventory(); i++) {
            ItemStack tStack = tInventory.getStackInSlot(i);
            if (tStack != null && tStack.getItem() instanceof ItemToolbox) {
                IInventory tToolboxInventory = ((ItemToolbox) tStack.getItem()).getInventory(aPlayer, tStack);
                if (consumeSolderingMaterial(tToolboxInventory)) {
                    tInventory.markDirty();
                    if (aPlayer.inventoryContainer != null) {
                        aPlayer.inventoryContainer.detectAndSendChanges();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Consumes soldering material from given inventory
     */
    public static boolean consumeSolderingMaterial(IInventory aInventory) {
        for (int i = 0; i < aInventory.getSizeInventory(); i++) {
            ItemStack tStack = aInventory.getStackInSlot(i);
            if (GTUtility.isStackInList(tStack, GregTechAPI.sSolderingMetalList)) {
                if (tStack.stackSize < 1) return false;
                if (tStack.stackSize == 1) {
                    tStack = null;
                } else {
                    tStack.stackSize--;
                }
                aInventory.setInventorySlotContents(i, tStack);
                aInventory.markDirty();
                return true;
            }
        }
        return false;
    }

    /**
     * Is this an electric Item, which can charge other Items?
     */
    public static boolean isChargerItem(ItemStack aStack) {
        if (isElectricItem(aStack)) {
            return ((ic2.api.item.IElectricItem) aStack.getItem()).canProvideEnergy(aStack);
        }
        return false;
    }

    /**
     * Is this an electric Item?
     */
    public static boolean isElectricItem(ItemStack aStack) {
        return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem electricItem
            && electricItem.getTier(aStack) < Integer.MAX_VALUE;
    }

    public static boolean isElectricItem(ItemStack aStack, byte aTier) {
        return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem electricItem
            && electricItem.getTier(aStack) == aTier;
    }

    /**
     * Returns the current charge and maximum charge of an ItemStack.
     *
     * @param aStack Any ItemStack.
     * @return Optional.empty() if the stack is null or not an electric item, or an Optional containing a payload of an
     * array containing [ current_charge, maximum_charge ] on success.
     */
    public static Optional<Long[]> getElectricItemCharge(ItemStack aStack) {
        if (aStack == null || !isElectricItem(aStack)) {
            return Optional.empty();
        }

        final Item item = aStack.getItem();

        if (item instanceof final MetaBaseItem metaBaseItem) {
            final Long[] stats = metaBaseItem.getElectricStats(aStack);
            if (stats != null && stats.length > 0) {
                return Optional.of(new Long[]{metaBaseItem.getRealCharge(aStack), stats[0]});
            }

        } else if (item instanceof final IElectricItem ic2ElectricItem) {
            return Optional.of(new Long[]{(long) ic2.api.item.ElectricItem.manager.getCharge(aStack), (long) ic2ElectricItem.getMaxCharge(aStack)});
        }

        return Optional.empty();
    }

    /**
     * Allow item to be inserted into ic2 toolbox
     */
    public static void registerBoxableItemToToolBox(ItemStack aStack) {
        if (aStack != null) {
            ic2.api.item.ItemWrapper.registerBoxable(aStack.getItem(), (IBoxable) sBoxableWrapper);
            sBoxableItems.add(new GTItemStack(aStack));
        }
    }

    public static int getCapsuleCellContainerCountMultipliedWithStackSize(ItemStack... aStacks) {
        int rAmount = 0;
        for (ItemStack tStack : aStacks)
            if (tStack != null) rAmount += getCapsuleCellContainerCount(tStack) * tStack.stackSize;
        return rAmount;
    }

    public static int getCapsuleCellContainerCount(ItemStack aStack) {
        if (aStack == null) return 0;

        if (GTUtility.areStacksEqual(GTUtility.getContainerForFilledItem(aStack, true), ItemList.Cell_Empty.get(1))) {
            return 1;
        }

        if (GTUtility.areStacksEqual(aStack, getIC2Item("waterCell", 1, WILDCARD))) {
            return 1;
        }

        for (OrePrefixes cellType : OrePrefixes.CELL_TYPES) {
            if (cellType.contains(aStack)) {
                return 1;
            }
        }

        return 0;
    }

    public static class RecipeBits {

        /**
         * Mirrors the Recipe
         */
        public static final long MIRRORED = B[0];
        /**
         * Buffers the Recipe for later addition. This makes things more efficient.
         */
        public static final long BUFFERED = B[1];
        /**
         * This is a special Tag I used for crafting Coins up and down. If all the input items have the same NBT, keep
         * it in the output item.
         */
        public static final long KEEPNBT = B[2];
        /**
         * Makes the Recipe Reverse Craftable in the Disassembler.
         */
        public static final long DISMANTLEABLE = B[3];
        /**
         * Prevents the Recipe from accidentally getting removed by my own Handlers.
         */
        public static final long NOT_REMOVABLE = B[4];
        /**
         * Reverses the Output of the Recipe for smelting and pulverising.
         */
        public static final long REVERSIBLE = B[5];
        /**
         * Removes all Recipes with the same Output Item regardless of NBT, unless another Recipe Deletion Bit is added
         * too.
         */
        public static final long DELETE_ALL_OTHER_RECIPES = B[6];
        /**
         * Removes all Recipes with the same Output Item limited to the same NBT.
         */
        public static final long DELETE_ALL_OTHER_RECIPES_IF_SAME_NBT = B[7];
        /**
         * Removes all Recipes with the same Output Item limited to Shaped Recipes.
         */
        public static final long DELETE_ALL_OTHER_SHAPED_RECIPES = B[8];
        /**
         * Removes all Recipes with the same Output Item limited to native Recipe Handlers.
         */
        public static final long DELETE_ALL_OTHER_NATIVE_RECIPES = B[9];
        /**
         * Disables the check for colliding Recipes.
         */
        public static final long DO_NOT_CHECK_FOR_COLLISIONS = B[10];
        /**
         * Only adds the Recipe if there is another Recipe having that Output
         */
        public static final long ONLY_ADD_IF_THERE_IS_ANOTHER_RECIPE_FOR_IT = B[11];
        /**
         * Only adds the Recipe if it has an Output
         */
        public static final long ONLY_ADD_IF_RESULT_IS_NOT_NULL = B[12];
        /**
         * Don't remove shapeless recipes with this output
         */
        public static final long DONT_REMOVE_SHAPELESS = B[13];
        /**
         * Keep input item's NBT if the input item is the same as output item, and try to overwrite input item's NBT
         * tags with output item's NBT tags if exists
         */
        public static final long OVERWRITE_NBT = B[14];
        /**
         * Combination of common bits. NOT_REMOVABLE, REVERSIBLE, and BUFFERED
         */
        public static final long BITS = NOT_REMOVABLE | REVERSIBLE | BUFFERED;
        /**
         * Combination of common bits. NOT_REMOVABLE, REVERSIBLE, BUFFERED, and DISMANTLEABLE
         */
        public static final long BITSD = BITS | DISMANTLEABLE;
        /**
         * Combination of common bits. DO_NOT_CHECK_FOR_COLLISIONS, BUFFERED, ONLY_ADD_IF_RESULT_IS_NOT_NULL,
         * NOT_REMOVABLE
         */
        public static final long BITS_STD = DO_NOT_CHECK_FOR_COLLISIONS | BUFFERED
            | ONLY_ADD_IF_RESULT_IS_NOT_NULL
            | NOT_REMOVABLE;
    }
}
