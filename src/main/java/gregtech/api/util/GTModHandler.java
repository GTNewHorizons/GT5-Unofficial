package gregtech.api.util;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.B;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.DW;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.GTValues.W;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Tier;
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
import gregtech.api.recipe.RecipeMap;
import ic2.api.item.IBoxable;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
import ic2.api.reactor.IReactorComponent;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeOutput;
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
    public static GTHashSet<GTItemStack> sNonReplaceableItems = new GTHashSet<>();
    public static Object sBoxableWrapper = new GTIBoxableWrapper();
    public static Collection<GTItemStack> sBoxableItems = new ArrayList<>();
    private static final Map<IRecipeInput, RecipeOutput> emptyRecipeMap = new HashMap<>();
    private static Set<GTUtility.ItemId> recyclerWhitelist;
    private static Set<GTUtility.ItemId> recyclerBlacklist;

    private static boolean sBufferCraftingRecipes = true;
    public static List<Integer> sSingleNonBlockDamagableRecipeList_list = new ArrayList<>(100);
    private static final boolean sSingleNonBlockDamagableRecipeList_create = true;
    private static final ItemStack sMt1 = new ItemStack(Blocks.dirt, 1, 0), sMt2 = new ItemStack(Blocks.dirt, 1, 0);
    private static final String s_H = "h", s_F = "f", s_I = "I", s_P = "P", s_R = "R";
    private static final ItemStack[][] sShapes1 = new ItemStack[][] {
        { sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1, null },
        { sMt1, null, sMt1, sMt1, null, sMt1, sMt1, sMt1, sMt1 },
        { null, sMt1, null, sMt1, sMt1, sMt1, sMt1, null, sMt1 },
        { sMt1, sMt1, sMt1, sMt1, null, sMt1, null, null, null },
        { sMt1, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1 },
        { sMt1, sMt1, sMt1, sMt1, null, sMt1, sMt1, null, sMt1 },
        { null, null, null, sMt1, null, sMt1, sMt1, null, sMt1 },
        { null, sMt1, null, null, sMt1, null, null, sMt2, null },
        { sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2, null },
        { null, sMt1, null, null, sMt2, null, null, sMt2, null },
        { sMt1, sMt1, null, sMt1, sMt2, null, null, sMt2, null },
        { null, sMt1, sMt1, null, sMt2, sMt1, null, sMt2, null },
        { sMt1, sMt1, null, null, sMt2, null, null, sMt2, null },
        { null, sMt1, sMt1, null, sMt2, null, null, sMt2, null },
        { null, sMt1, null, sMt1, null, null, null, sMt1, sMt2 },
        { null, sMt1, null, null, null, sMt1, sMt2, sMt1, null },
        { null, sMt1, null, sMt1, null, sMt1, null, null, sMt2 },
        { null, sMt1, null, sMt1, null, sMt1, sMt2, null, null },
        { null, sMt2, null, null, sMt1, null, null, sMt1, null },
        { null, sMt2, null, null, sMt2, null, sMt1, sMt1, sMt1 },
        { null, sMt2, null, null, sMt2, null, null, sMt1, null },
        { null, sMt2, null, sMt1, sMt2, null, sMt1, sMt1, null },
        { null, sMt2, null, null, sMt2, sMt1, null, sMt1, sMt1 },
        { null, sMt2, null, null, sMt2, null, sMt1, sMt1, null },
        { sMt1, null, null, null, sMt2, null, null, null, sMt2 },
        { null, null, sMt1, null, sMt2, null, sMt2, null, null },
        { sMt1, null, null, null, sMt2, null, null, null, null },
        { null, null, sMt1, null, sMt2, null, null, null, null },
        { sMt1, sMt2, null, null, null, null, null, null, null },
        { sMt2, sMt1, null, null, null, null, null, null, null },
        { sMt1, null, null, sMt2, null, null, null, null, null },
        { sMt2, null, null, sMt1, null, null, null, null, null },
        { sMt1, sMt1, sMt1, sMt1, sMt1, sMt1, null, sMt2, null },
        { sMt1, sMt1, null, sMt1, sMt1, sMt2, sMt1, sMt1, null },
        { null, sMt1, sMt1, sMt2, sMt1, sMt1, null, sMt1, sMt1 },
        { null, sMt2, null, sMt1, sMt1, sMt1, sMt1, sMt1, sMt1 },
        { sMt1, sMt1, sMt1, sMt1, sMt2, sMt1, null, sMt2, null },
        { sMt1, sMt1, null, sMt1, sMt2, sMt2, sMt1, sMt1, null },
        { null, sMt1, sMt1, sMt2, sMt2, sMt1, null, sMt1, sMt1 },
        { null, sMt2, null, sMt1, sMt2, sMt1, sMt1, sMt1, sMt1 },
        { sMt1, null, null, null, sMt1, null, null, null, null },
        { null, sMt1, null, sMt1, null, null, null, null, null },
        { sMt1, sMt1, null, sMt2, null, sMt1, sMt2, null, null },
        { null, sMt1, sMt1, sMt1, null, sMt2, null, null, sMt2 } };
    public static List<Integer> sSingleNonBlockDamagableRecipeList_validsShapes1 = new ArrayList<>(44);
    public static boolean sSingleNonBlockDamagableRecipeList_validsShapes1_update = false;
    public static List<Integer> sSingleNonBlockDamagableRecipeList_warntOutput = new ArrayList<>(50);
    public static List<Integer> sVanillaRecipeList_warntOutput = new ArrayList<>(50);
    public static final List<IRecipe> sSingleNonBlockDamagableRecipeList_verified = new ArrayList<>(1000);
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
        return aFluid.isFluidEqual(getWater(1)) || aFluid.isFluidEqual(getDistilledWater(1));
    }

    /**
     * Returns a Liquid Stack with given amount of Water.
     */
    public static FluidStack getWater(long aAmount) {
        return FluidRegistry.getFluidStack("water", (int) aAmount);
    }

    /**
     * Returns a Liquid Stack with given amount of distilled Water.
     */
    public static FluidStack getDistilledWater(long aAmount) {
        FluidStack tFluid = FluidRegistry.getFluidStack("ic2distilledwater", (int) aAmount);
        if (tFluid == null) tFluid = getWater(aAmount);
        return tFluid;
    }

    /**
     * Returns if that Liquid is Lava
     */
    public static boolean isLava(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(getLava(1));
    }

    /**
     * Returns a Liquid Stack with given amount of Lava.
     */
    public static FluidStack getLava(long aAmount) {
        return FluidRegistry.getFluidStack("lava", (int) aAmount);
    }

    /**
     * Returns if that Liquid is Steam
     */
    public static boolean isSteam(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(getSteam(1));
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
     */
    public static FluidStack getSteam(long aAmount) {
        return FluidRegistry.getFluidStack("steam", (int) aAmount);
    }

    /**
     * Returns if that Liquid is Milk
     */
    public static boolean isMilk(FluidStack aFluid) {
        if (aFluid == null) return false;
        return aFluid.isFluidEqual(getMilk(1));
    }

    /**
     * Returns a Liquid Stack with given amount of Milk.
     */
    public static FluidStack getMilk(long aAmount) {
        return FluidRegistry.getFluidStack("milk", (int) aAmount);
    }

    public static ItemStack getEmptyCell(long aAmount) {
        return ItemList.Cell_Empty.get(aAmount);
    }

    public static ItemStack getAirCell(long aAmount) {
        return ItemList.Cell_Air.get(aAmount);
    }

    public static ItemStack getWaterCell(long aAmount) {
        return ItemList.Cell_Water.get(aAmount);
    }

    public static ItemStack getLavaCell(long aAmount) {
        return ItemList.Cell_Lava.get(aAmount);
    }

    /**
     * @param aValue the Value of this Stack, when burning inside a Furnace (200 = 1 Burn Process = 500 EU, max = 32767
     *               (that is 81917.5 EU)), limited to Short because the vanilla Furnace otherwise can't handle it
     *               properly, stupid Mojang...
     */
    public static ItemStack setFuelValue(ItemStack aStack, short aValue) {
        aStack.setTagCompound(GTUtility.getNBTContainingShort(aStack.getTagCompound(), "GT.ItemFuelValue", aValue));
        return aStack;
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
        if (!sIC2ItemMap.containsKey(aItem)) try {
            ItemStack tStack = IC2Items.getItem(aItem);
            sIC2ItemMap.put(aItem, tStack);
            if (tStack == null && D1) GTLog.err.println(aItem + " is not found in the IC2 Items!");
        } catch (Throwable e) {
            /* Do nothing */
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
     * OUT OF ORDER
     */
    @Deprecated
    public static boolean getModeKeyDown(EntityPlayer aPlayer) {
        return false;
    }

    /**
     * OUT OF ORDER
     */
    @Deprecated
    public static boolean getBoostKeyDown(EntityPlayer aPlayer) {
        return false;
    }

    /**
     * OUT OF ORDER
     */
    @Deprecated
    public static boolean getJumpKeyDown(EntityPlayer aPlayer) {
        return false;
    }

    /**
     * Adds a Valuable Ore to the Miner
     */
    public static boolean addValuableOre(Block aBlock, int aMeta, int aValue) {
        if (aValue <= 0) return false;
        try {
            Class.forName("ic2.core.IC2")
                .getMethod("addValuableOre", IRecipeInput.class, int.class)
                .invoke(null, new RecipeInputItemStack(new ItemStack(aBlock, 1, aMeta)), aValue);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return true;
    }

    /**
     * Adds a Scrapbox Drop. Fails at April first for the "suddenly Hoes"-Feature of IC2
     */
    public static boolean addScrapboxDrop(float aChance, ItemStack aOutput) {
        aOutput = GTOreDictUnificator.get(true, aOutput);
        if (aOutput == null || aChance <= 0) return false;
        aOutput.stackSize = 1;
        if (GTConfig.troll && !GTUtility.areStacksEqual(aOutput, new ItemStack(Items.wooden_hoe, 1, 0))) return false;
        try {
            ic2.api.recipe.Recipes.scrapboxDrops.addDrop(GTUtility.copyOrNull(aOutput), aChance);
        } catch (Throwable e) {
            /* Do nothing */
        }
        return true;
    }

    /**
     * Adds an Item to the Recycler Blacklist
     */
    public static boolean addToRecyclerBlackList(ItemStack aRecycledStack) {
        if (aRecycledStack == null) return false;
        try {
            ic2.api.recipe.Recipes.recyclerBlacklist.add(new RecipeInputItemStack(aRecycledStack));
        } catch (Throwable e) {
            /* Do nothing */
        }
        return true;
    }

    /**
     * Just simple Furnace smelting. Unbelievable how Minecraft fails at making a simple ItemStack->ItemStack mapping...
     */
    public static boolean addSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
        aOutput = GTOreDictUnificator.get(true, aOutput);
        if (aInput == null || aOutput == null) return false;
        FurnaceRecipes.smelting()
            .func_151394_a(aInput, GTUtility.copyOrNull(aOutput), 0.0F);
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
        getMaceratorRecipeList().entrySet()
            .clear();
        getCompressorRecipeList().entrySet()
            .clear();
        getExtractorRecipeList().entrySet()
            .clear();
        getOreWashingRecipeList().entrySet()
            .clear();
        getThermalCentrifugeRecipeList().entrySet()
            .clear();
    }

    /**
     * Adds GT versions of the IC2 recipes from the supplied IC2RecipeList. Deprecated because all IC2 recipes
     * have been manually added to GT.
     */
    @Deprecated
    public static void addIC2RecipesToGT(Map<IRecipeInput, RecipeOutput> aIC2RecipeList, RecipeMap<?> aGTRecipeMap,
        boolean aAddGTRecipe, boolean aRemoveIC2Recipe, boolean aExcludeGTIC2Items) {
        Map<ItemStack, ItemStack> aRecipesToRemove = new HashMap<>();
        for (Entry<IRecipeInput, RecipeOutput> iRecipeInputRecipeOutputEntry : aIC2RecipeList.entrySet()) {
            if (iRecipeInputRecipeOutputEntry.getValue().items.isEmpty()) {
                continue;
            }

            for (ItemStack tStack : (iRecipeInputRecipeOutputEntry.getKey()).getInputs()) {
                if (!GTUtility.isStackValid(tStack)) {
                    continue;
                }

                if (aAddGTRecipe) {
                    try {
                        if (aExcludeGTIC2Items && ((tStack.getUnlocalizedName()
                            .contains("gt.metaitem.01")
                            || tStack.getUnlocalizedName()
                                .contains("gt.blockores")
                            || tStack.getUnlocalizedName()
                                .contains("ic2.itemCrushed")
                            || tStack.getUnlocalizedName()
                                .contains("ic2.itemPurifiedCrushed"))))
                            continue;
                        switch (aGTRecipeMap.unlocalizedName) {
                            case "gt.recipe.macerator", "gt.recipe.extractor", "gt.recipe.compressor" -> GTValues.RA
                                .stdBuilder()
                                .itemInputs(
                                    GTUtility.copyAmount(
                                        iRecipeInputRecipeOutputEntry.getKey()
                                            .getAmount(),
                                        tStack))
                                .itemOutputs(iRecipeInputRecipeOutputEntry.getValue().items.toArray(new ItemStack[0]))
                                .duration(15 * SECONDS)
                                .eut(2)
                                .addTo(aGTRecipeMap);
                            case "gt.recipe.thermalcentrifuge" -> GTValues.RA.stdBuilder()
                                .itemInputs(
                                    GTUtility.copyAmount(
                                        iRecipeInputRecipeOutputEntry.getKey()
                                            .getAmount(),
                                        tStack))
                                .itemOutputs(iRecipeInputRecipeOutputEntry.getValue().items.toArray(new ItemStack[0]))
                                .duration(25 * SECONDS)
                                .eut(48)
                                .addTo(aGTRecipeMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace(GTLog.err);
                    }
                }
                if (aRemoveIC2Recipe) {
                    aRecipesToRemove.put(tStack, iRecipeInputRecipeOutputEntry.getValue().items.get(0));
                }

            }

        }
        GTUtility.bulkRemoveSimpleIC2MachineRecipe(aRecipesToRemove, aIC2RecipeList);
    }

    public static Map<IRecipeInput, RecipeOutput> getExtractorRecipeList() {
        try {
            return ic2.api.recipe.Recipes.extractor.getRecipes();
        } catch (Throwable e) {
            /* Do nothing */
        }
        return emptyRecipeMap;
    }

    public static Map<IRecipeInput, RecipeOutput> getCompressorRecipeList() {
        try {
            return ic2.api.recipe.Recipes.compressor.getRecipes();
        } catch (Throwable e) {
            /* Do nothing */
        }
        return emptyRecipeMap;
    }

    public static Map<IRecipeInput, RecipeOutput> getMaceratorRecipeList() {
        try {
            return ic2.api.recipe.Recipes.macerator.getRecipes();
        } catch (Throwable e) {
            /* Do nothing */
        }
        return emptyRecipeMap;
    }

    public static Map<IRecipeInput, RecipeOutput> getThermalCentrifugeRecipeList() {
        try {
            return ic2.api.recipe.Recipes.centrifuge.getRecipes();
        } catch (Throwable e) {
            /* Do nothing */
        }
        return emptyRecipeMap;
    }

    public static Map<IRecipeInput, RecipeOutput> getOreWashingRecipeList() {
        try {
            return ic2.api.recipe.Recipes.oreWashing.getRecipes();
        } catch (Throwable e) {
            /* Do nothing */
        }
        return emptyRecipeMap;
    }

    /**
     * IC2-OreWasher Recipe. Overloads old Recipes automatically
     */
    @Deprecated
    public static boolean addOreWasherRecipe(ItemStack aInput, int[] aChances, int aWaterAmount, Object... aOutput) {
        if (aInput == null || aOutput == null || aOutput.length == 0 || aOutput[0] == null) return false;
        RA.stdBuilder()
            .itemInputs(aInput)
            .itemOutputs((ItemStack) aOutput[0], (ItemStack) aOutput[1], (ItemStack) aOutput[2])
            .outputChances(aChances)
            .fluidInputs(GTModHandler.getWater(aWaterAmount))
            .duration(25 * SECONDS)
            .eut(16)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(aInput)
            .itemOutputs((ItemStack) aOutput[0], (ItemStack) aOutput[1], (ItemStack) aOutput[2])
            .outputChances(aChances)
            .fluidInputs(GTModHandler.getDistilledWater(aWaterAmount / 5))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(oreWasherRecipes);
        return true;
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
     * ToolDictNames.craftingToolSoftHammer, 's' ToolDictNames.craftingToolSaw, 'w' ToolDictNames.craftingToolWrench,
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
     * ToolDictNames.craftingToolSoftHammer, 's' ToolDictNames.craftingToolSaw, 'w' ToolDictNames.craftingToolWrench,
     * 'x' ToolDictNames.craftingToolWireCutter,
     */
    public static boolean addCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
        return addCraftingRecipe(
            aResult,
            new Enchantment[0],
            new int[0],
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

    public static boolean addMachineCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe,
        int machineTier) {
        if (aRecipe != null) {
            for (int i = 3; i < aRecipe.length; i++) {
                if (!(aRecipe[i] instanceof MTEBasicMachineWithRecipe.X)) continue;

                // spotless:off
                aRecipe[i] = switch ((MTEBasicMachineWithRecipe.X) aRecipe[i]) {
                    case CIRCUIT            -> Tier.ELECTRIC[machineTier].mManagingObject;
                    case BETTER_CIRCUIT     -> Tier.ELECTRIC[machineTier].mBetterManagingObject;
                    case HULL               -> Tier.ELECTRIC[machineTier].mHullObject;
                    case WIRE               -> Tier.ELECTRIC[machineTier].mConductingObject;
                    case WIRE4              -> Tier.ELECTRIC[machineTier].mLargerConductingObject;
                    case STICK_DISTILLATION -> OrePrefixes.stick.get(Materials.Blaze);

                    case GLASS -> switch (machineTier) {
                        case 0, 1, 2, 3    -> new ItemStack(Blocks.glass, 1, W);
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
                        case 6    -> OrePrefixes.pipeSmall.get(Materials.Ultimate);
                        case 7    -> OrePrefixes.pipeMedium.get(Materials.Ultimate);
                        case 8    -> OrePrefixes.pipeLarge.get(Materials.Ultimate);
                        default   -> OrePrefixes.pipeHuge.get(Materials.Ultimate);
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

            if (!GTModHandler.addCraftingRecipe(
                aResult,
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.BUFFERED
                    | GTModHandler.RecipeBits.NOT_REMOVABLE
                    | GTModHandler.RecipeBits.REVERSIBLE,
                aRecipe)) {
                throw new IllegalArgumentException("INVALID CRAFTING RECIPE FOR: " + aResult.getDisplayName());
            }
        }
        return true;
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
        if (aResult != null && Items.feather.getDamage(aResult) == W) Items.feather.setDamage(aResult, 0);
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

        try {
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
                            tRecipeList.add(ToolDictNames.craftingToolSoftHammer.name());
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
                if (GTUtility.arrayContainsNonNull(tData))
                    GTOreDictUnificator.addItemData(aResult, new ItemData(tData));
            }

            if (aCheckForCollisions && tRemoveRecipe) {
                ItemStack[] tRecipe = new ItemStack[9];
                int x = -1;
                for (char chr : shape.toString()
                    .toCharArray()) {
                    tRecipe[++x] = tItemStackMap.get(chr);
                    if (tRecipe[x] != null && Items.feather.getDamage(tRecipe[x]) == W)
                        Items.feather.setDamage(tRecipe[x], 0);
                }
                if (tDoWeCareIfThereWasARecipe || !aBuffered) tThereWasARecipe = removeRecipe(tRecipe) != null;
                else removeRecipeDelayed(tRecipe);
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
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

        if (Items.feather.getDamage(aResult) == W || Items.feather.getDamage(aResult) < 0)
            Items.feather.setDamage(aResult, 0);

        GTUtility.updateItemStack(aResult);

        if (tThereWasARecipe || !aOnlyAddIfThereIsAnyRecipeOutputtingThis) {
            if (sBufferCraftingRecipes && aBuffered) sBufferRecipeList.add(
                new GTShapedRecipe(
                    GTUtility.copyOrNull(aResult),
                    aDismantleable,
                    aRemovable,
                    aKeepNBT,
                    aEnchantmentsAdded,
                    aEnchantmentLevelsAdded,
                    aRecipe).setMirrored(aMirrored));
            else GameRegistry.addRecipe(
                new GTShapedRecipe(
                    GTUtility.copyOrNull(aResult),
                    aDismantleable,
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
    public static boolean addShapelessEnchantingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
        int[] aEnchantmentLevelsAdded, Object[] aRecipe) {
        return addShapelessCraftingRecipe(
            aResult,
            aEnchantmentsAdded,
            aEnchantmentLevelsAdded,
            true,
            false,
            false,
            false,
            aRecipe);
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
            new Enchantment[0],
            new int[0],
            (aBitMask & RecipeBits.BUFFERED) != 0,
            (aBitMask & RecipeBits.KEEPNBT) != 0,
            (aBitMask & RecipeBits.DISMANTLEABLE) != 0,
            (aBitMask & RecipeBits.NOT_REMOVABLE) == 0,
            aRecipe);
    }

    /**
     * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
     */
    private static boolean addShapelessCraftingRecipe(ItemStack aResult, Enchantment[] aEnchantmentsAdded,
        int[] aEnchantmentLevelsAdded, boolean aBuffered, boolean aKeepNBT, boolean aDismantleable, boolean aRemovable,
        Object[] aRecipe) {
        aResult = GTOreDictUnificator.get(true, aResult);
        if (aRecipe == null || aRecipe.length == 0) return false;
        for (byte i = 0; i < aRecipe.length; i++) {
            if (aRecipe[i] instanceof IItemContainer) aRecipe[i] = ((IItemContainer) aRecipe[i]).get(1);
            else if (aRecipe[i] instanceof Enum) aRecipe[i] = ((Enum<?>) aRecipe[i]).name();
            else if (!(aRecipe[i] == null || aRecipe[i] instanceof ItemStack
                || aRecipe[i] instanceof String
                || aRecipe[i] instanceof Character)) aRecipe[i] = aRecipe[i].toString();
        }
        try {
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
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }

        if (aResult == null || aResult.stackSize <= 0) return false;

        if (Items.feather.getDamage(aResult) == W || Items.feather.getDamage(aResult) < 0)
            Items.feather.setDamage(aResult, 0);

        GTUtility.updateItemStack(aResult);

        if (sBufferCraftingRecipes && aBuffered) sBufferRecipeList.add(
            new GTShapelessRecipe(
                GTUtility.copyOrNull(aResult),
                aDismantleable,
                aRemovable,
                aKeepNBT,
                aEnchantmentsAdded,
                aEnchantmentLevelsAdded,
                aRecipe));
        else GameRegistry.addRecipe(
            new GTShapelessRecipe(
                GTUtility.copyOrNull(aResult),
                aDismantleable,
                aRemovable,
                aKeepNBT,
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
     * Removes all matching Smelting Recipes by output
     */
    public static boolean removeFurnaceSmeltingByOutput(ItemStack aOutput) {
        if (aOutput != null) {
            return FurnaceRecipes.smelting()
                .getSmeltingList()
                .values()
                .removeIf(
                    tOutput -> GTUtility.isStackValid(tOutput) && GTUtility.areStacksEqual(aOutput, tOutput, true));
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
        if (Arrays.stream(aRecipe)
            .noneMatch(Objects::nonNull)) return null;

        ItemStack rReturn = null;
        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < aRecipe.length && i < 9; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        int tList_sS = tList.size();
        try {
            for (int i = 0; i < tList_sS; i++) {
                for (; i < tList_sS; i++) {
                    if ((!(tList.get(i) instanceof IGTCraftingRecipe)
                        || ((IGTCraftingRecipe) tList.get(i)).isRemovable()) && tList.get(i)
                            .matches(aCrafting, DW)) {
                        rReturn = tList.get(i)
                            .getCraftingResult(aCrafting);
                        if (rReturn != null) tList.remove(i--);
                        tList_sS = tList.size();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GTLog.err);
        }
        return rReturn;
    }

    public static void removeRecipeDelayed(ItemStack... aRecipe) {
        if (!sBufferCraftingRecipes) {
            removeRecipe(aRecipe);
            return;
        }

        if (aRecipe == null) return;
        if (Arrays.stream(aRecipe)
            .noneMatch(Objects::nonNull)) return;

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
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        GT_FML_LOGGER.info("BulkRemoveByRecipe: tList: " + tList.size() + " toRemove: " + toRemove.size());

        Set<IRecipe> tListToRemove = tList.parallelStream()
            .filter(tRecipe -> {
                if ((tRecipe instanceof IGTCraftingRecipe) && !((IGTCraftingRecipe) tRecipe).isRemovable())
                    return false;
                return toRemove.stream()
                    .anyMatch(aCrafting -> tRecipe.matches(aCrafting, DW));
            })
            .collect(Collectors.toSet());

        tList.removeIf(tListToRemove::contains);
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
     * @param aOutput The output of the Recipe.
     * @return if it has removed at least one Recipe.
     */
    public static boolean removeRecipeByOutput(ItemStack aOutput, boolean aIgnoreNBT,
        boolean aNotRemoveShapelessRecipes, boolean aOnlyRemoveNativeHandlers) {
        if (aOutput == null) return false;
        boolean rReturn = false;
        final ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        aOutput = GTOreDictUnificator.get(aOutput);
        int tList_sS = tList.size();
        for (int i = 0; i < tList_sS; i++) {
            final IRecipe tRecipe = tList.get(i);
            if (aNotRemoveShapelessRecipes
                && (tRecipe instanceof ShapelessRecipes || tRecipe instanceof ShapelessOreRecipe)) continue;
            if (aOnlyRemoveNativeHandlers) {
                if (!sNativeRecipeClasses.contains(
                    tRecipe.getClass()
                        .getName()))
                    continue;
            } else {
                if (sSpecialRecipeClasses.contains(
                    tRecipe.getClass()
                        .getName()))
                    continue;
            }
            ItemStack tStack = tRecipe.getRecipeOutput();
            if ((!(tRecipe instanceof IGTCraftingRecipe) || ((IGTCraftingRecipe) tRecipe).isRemovable())
                && GTUtility.areStacksEqual(GTOreDictUnificator.get(tStack), aOutput, aIgnoreNBT)) {
                tList.remove(i--);
                tList_sS = tList.size();
                rReturn = true;
            }
        }
        return rReturn;
    }

    public static boolean bulkRemoveRecipeByOutput(List<ItemStack> toRemove) {
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();

        Set<ItemStack> setToRemove = toRemove.parallelStream()
            .map(GTOreDictUnificator::get_nocopy)
            .collect(Collectors.toSet());

        GT_FML_LOGGER.info("BulkRemoveRecipeByOutput: tList: " + tList.size() + " setToRemove: " + setToRemove.size());

        Set<IRecipe> tListToRemove = tList.parallelStream()
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

        tList.removeIf(tListToRemove::contains);
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
        List<IRecipe> tList = CraftingManager.getInstance()
            .getRecipeList();
        synchronized (sAllRecipeList) {
            if (sAllRecipeList.size() != tList.size()) {
                sAllRecipeList.clear();
                sAllRecipeList.addAll(tList);
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
        return getRecipeOutput(false, true, aRecipe);
    }

    public static ItemStack getRecipeOutputNoOreDict(ItemStack... aRecipe) {
        return getRecipeOutput(false, false, aRecipe);
    }

    public static ItemStack getRecipeOutput(boolean aUncopiedStack, ItemStack... aRecipe) {
        return getRecipeOutput(aUncopiedStack, true, aRecipe);
    }

    /**
     * Gives you a copy of the Output from a Crafting Recipe Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(boolean aUncopiedStack, boolean allowOreDict, ItemStack... aRecipe) {
        if (aRecipe == null || Arrays.stream(aRecipe)
            .noneMatch(Objects::nonNull)) return null;

        InventoryCrafting aCrafting = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 3, 3);
        for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
        ArrayList<IRecipe> tList = (ArrayList<IRecipe>) CraftingManager.getInstance()
            .getRecipeList();
        boolean found = false;

        for (IRecipe iRecipe : tList) {
            found = false;
            if (!allowOreDict && iRecipe instanceof ShapedOreRecipe) continue;

            try {
                found = iRecipe.matches(aCrafting, DW);
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
            if (found) {
                ItemStack tOutput = aUncopiedStack ? iRecipe.getRecipeOutput() : iRecipe.getCraftingResult(aCrafting);
                if (tOutput == null || tOutput.stackSize <= 0) {
                    // Seriously, who would ever do that shit?
                    if (!GregTechAPI.sPostloadFinished) throw new GTItsNotMyFaultException(
                        "Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
                } else {
                    if (aUncopiedStack) return tOutput;
                    return GTUtility.copyOrNull(tOutput);
                }
            }
        }
        return null;
    }

    /**
     * Gives you a list of the Outputs from a Crafting Recipe If you have multiple Mods, which add Bronze Armor for
     * example This also removes old Recipes from the List.
     */
    public static List<ItemStack> getVanillyToolRecipeOutputs(ItemStack... aRecipe) {
        if (!GregTechAPI.sPostloadStarted || GregTechAPI.sPostloadFinished) sSingleNonBlockDamagableRecipeList.clear();
        if (sSingleNonBlockDamagableRecipeList.isEmpty()) {
            for (IRecipe tRecipe : CraftingManager.getInstance()
                .getRecipeList()) {
                ItemStack tStack = tRecipe.getRecipeOutput();
                if (GTUtility.isStackValid(tStack) && tStack.getMaxStackSize() == 1
                    && tStack.getMaxDamage() > 0
                    && !(tStack.getItem() instanceof ItemBlock)
                    && !(tStack.getItem() instanceof IReactorComponent)
                    && !isElectricItem(tStack)
                    && !GTUtility.isStackInList(tStack, sNonReplaceableItems)) {
                    if (!(tRecipe instanceof ShapelessRecipes || tRecipe instanceof ShapelessOreRecipe)) {
                        if (tRecipe instanceof ShapedOreRecipe) {
                            boolean temp = true;
                            for (Object tObject : ((ShapedOreRecipe) tRecipe).getInput()) if (tObject != null) {
                                if (tObject instanceof ItemStack && (((ItemStack) tObject).getItem() == null
                                    || ((ItemStack) tObject).getMaxStackSize() < 2
                                    || ((ItemStack) tObject).getMaxDamage() > 0
                                    || ((ItemStack) tObject).getItem() instanceof ItemBlock)) {
                                    temp = false;
                                    break;
                                }
                                if (tObject instanceof List && ((List<?>) tObject).isEmpty()) {
                                    temp = false;
                                    break;
                                }
                            }
                            if (temp) sSingleNonBlockDamagableRecipeList.add(tRecipe);
                        } else if (tRecipe instanceof ShapedRecipes) {
                            boolean temp = true;
                            for (ItemStack tObject : ((ShapedRecipes) tRecipe).recipeItems) {
                                if (tObject != null && (tObject.getItem() == null || tObject.getMaxStackSize() < 2
                                    || tObject.getMaxDamage() > 0
                                    || tObject.getItem() instanceof ItemBlock)) {
                                    temp = false;
                                    break;
                                }
                            }
                            if (temp) sSingleNonBlockDamagableRecipeList.add(tRecipe);
                        } else {
                            sSingleNonBlockDamagableRecipeList.add(tRecipe);
                        }
                    }
                }
            }
            GTLog.out.println(
                "GTMod: Created a List of Tool Recipes containing " + sSingleNonBlockDamagableRecipeList.size()
                    + " Recipes for recycling."
                    + (sSingleNonBlockDamagableRecipeList.size() > 1024
                        ? " Scanning all these Recipes is the reason for the startup Lag you receive right now."
                        : E));
        }
        List<ItemStack> rList = getRecipeOutputs(sSingleNonBlockDamagableRecipeList, true, aRecipe);
        if (!GregTechAPI.sPostloadStarted || GregTechAPI.sPostloadFinished) sSingleNonBlockDamagableRecipeList.clear();
        return rList;
    }

    /**
     * Gives you a list of the Outputs from a Crafting Recipe If you have multiple Mods, which add Bronze Armor for
     * example
     */
    public static List<ItemStack> getRecipeOutputs(ItemStack... aRecipe) {
        return getRecipeOutputs(
            CraftingManager.getInstance()
                .getRecipeList(),
            false,
            aRecipe);
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
        if (aRecipe == null || Arrays.stream(aRecipe)
            .noneMatch(Objects::nonNull)) return rList;
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
                    try {
                        return tRecipe.matches(aCrafting, DW);
                    } catch (Throwable e) {
                        e.printStackTrace(GTLog.err);
                        return false;
                    }
                })
                .forEach(tRecipe -> stacks.add(tRecipe.getCraftingResult(aCrafting)));
            rList = stacks.stream()
                .filter(
                    tOutput -> tOutput.stackSize == 1 && tOutput.getMaxDamage() > 0 && tOutput.getMaxStackSize() == 1)
                .collect(Collectors.toList());
        } else for (Iterator<IRecipe> iterator = aList.iterator(); iterator.hasNext();) {
            IRecipe tRecipe = iterator.next();
            boolean matched = false;

            try {
                matched = tRecipe.matches(aCrafting, DW);
            } catch (Throwable e) {
                e.printStackTrace(GTLog.err);
            }
            if (matched) {
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

    /**
     * Used in my own Machines. Decreases StackSize of the Input if wanted.
     * <p/>
     * Checks also if there is enough Space in the Output Slots.
     */
    public static ItemStack[] getMachineOutput(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList,
        boolean aRemoveInput, NBTTagCompound rRecipeMetaData, ItemStack... aOutputSlots) {
        if (aOutputSlots == null || aOutputSlots.length == 0) return new ItemStack[0];
        if (aInput == null) return new ItemStack[aOutputSlots.length];
        try {
            for (Entry<IRecipeInput, RecipeOutput> tEntry : aRecipeList.entrySet()) {
                if (tEntry.getKey()
                    .matches(aInput)) {
                    if (tEntry.getKey()
                        .getAmount() <= aInput.stackSize) {
                        ItemStack[] tList = tEntry.getValue().items.toArray(new ItemStack[0]);
                        if (tList.length == 0) break;
                        ItemStack[] rList = new ItemStack[aOutputSlots.length];
                        rRecipeMetaData.setTag("return", tEntry.getValue().metadata);
                        for (byte i = 0; i < aOutputSlots.length && i < tList.length; i++) {
                            if (tList[i] != null) {
                                if (aOutputSlots[i] == null || (GTUtility.areStacksEqual(tList[i], aOutputSlots[i])
                                    && tList[i].stackSize + aOutputSlots[i].stackSize
                                        <= aOutputSlots[i].getMaxStackSize())) {
                                    rList[i] = GTUtility.copyOrNull(tList[i]);
                                } else {
                                    return new ItemStack[aOutputSlots.length];
                                }
                            }
                        }

                        if (aRemoveInput) aInput.stackSize -= tEntry.getKey()
                            .getAmount();
                        return rList;
                    }
                    break;
                }
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GTLog.err);
        }
        return new ItemStack[aOutputSlots.length];
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
        for (IRecipeInput input : ic2.api.recipe.Recipes.recyclerWhitelist) {
            for (ItemStack stack : input.getInputs()) {
                recyclerWhitelist.add(GTUtility.ItemId.create(stack.getItem(), stack.getItemDamage(), null));
            }
        }
        recyclerBlacklist = new HashSet<>();
        for (IRecipeInput input : ic2.api.recipe.Recipes.recyclerBlacklist) {
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
        return ic2.api.recipe.Recipes.scrapboxDrops.getDrop(ItemList.IC2_Scrapbox.get(1), false);
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
        try {
            if (isElectricItem(aStack)) {
                int tTier = ((ic2.api.item.IElectricItem) aStack.getItem()).getTier(aStack);
                if (tTier < 0 || tTier == aTier || aTier == Integer.MAX_VALUE) {
                    if (!aIgnoreLimit && tTier >= 0)
                        aCharge = (int) Math.min(aCharge, V[Math.max(0, Math.min(V.length - 1, tTier))]);
                    if (aCharge > 0) {
                        int rCharge = (int) Math.max(
                            0.0,
                            ic2.api.item.ElectricItem.manager.charge(aStack, aCharge, tTier, true, aSimulate));
                        return rCharge + (rCharge * 4 > aTier ? aTier : 0);
                    }
                }
            }
        } catch (Throwable e) {
            /* Do nothing */
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
        try {
            // if (isElectricItem(aStack) && (aIgnoreDischargability ||
            // ((ic2.api.item.IElectricItem)aStack.getItem()).canProvideEnergy(aStack))) {
            if (isElectricItem(aStack)) {
                int tTier = ((ic2.api.item.IElectricItem) aStack.getItem()).getTier(aStack);
                if (tTier < 0 || tTier == aTier || aTier == Integer.MAX_VALUE) {
                    if (!aIgnoreLimit && tTier >= 0) aCharge = (int) Math.min(
                        aCharge,
                        V[Math.max(0, Math.min(V.length - 1, tTier))] + B[Math.max(0, Math.min(V.length - 1, tTier))]);
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
        } catch (Throwable e) {
            /* Do nothing */
        }
        return 0;
    }

    /**
     * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
     *
     * @return if the action was successful
     */
    public static boolean canUseElectricItem(ItemStack aStack, int aCharge) {
        try {
            if (isElectricItem(aStack)) {
                return ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge);
            }
        } catch (Throwable e) {
            /* Do nothing */
        }
        return false;
    }

    /**
     * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
     *
     * @return if the action was successful
     */
    public static boolean useElectricItem(ItemStack aStack, int aCharge, EntityPlayer aPlayer) {
        try {
            if (isElectricItem(aStack)) {
                ic2.api.item.ElectricItem.manager.use(aStack, 0, aPlayer);
                if (ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge)) {
                    return ic2.api.item.ElectricItem.manager.use(aStack, aCharge, aPlayer);
                }
            }
        } catch (Throwable e) {
            /* Do nothing */
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
                        GTUtility.sendChatToPlayer(
                            (EntityPlayer) aPlayer,
                            GTUtility.trans("094.1", "Not enough soldering material!"));
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
        try {
            if (isElectricItem(aStack)) {
                return ((ic2.api.item.IElectricItem) aStack.getItem()).canProvideEnergy(aStack);
            }
        } catch (Throwable e) {
            /* Do nothing */
        }
        return false;
    }

    /**
     * Is this an electric Item?
     */
    public static boolean isElectricItem(ItemStack aStack) {
        try {
            return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem
                && ((IElectricItem) aStack.getItem()).getTier(aStack) < Integer.MAX_VALUE;
        } catch (Throwable e) {
            /* Do nothing */
        }
        return false;
    }

    public static boolean isElectricItem(ItemStack aStack, byte aTier) {
        try {
            return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem
                && ((IElectricItem) aStack.getItem()).getTier(aStack) == aTier;
        } catch (Throwable e) {
            /* Do nothing */
        }
        return false;
    }

    /**
     * Returns the current charge and maximum charge of an ItemStack.
     *
     * @param aStack Any ItemStack.
     * @return Optional.empty() if the stack is null or not an electric item, or an Optional containing a payload of an
     *         array containing [ current_charge, maximum_charge ] on success.
     */
    public static Optional<Long[]> getElectricItemCharge(ItemStack aStack) {
        if (aStack == null || !isElectricItem(aStack)) {
            return Optional.empty();
        }

        final Item item = aStack.getItem();

        if (item instanceof final MetaBaseItem metaBaseItem) {
            final Long[] stats = metaBaseItem.getElectricStats(aStack);
            if (stats != null && stats.length > 0) {
                return Optional.of(new Long[] { metaBaseItem.getRealCharge(aStack), stats[0] });
            }

        } else if (item instanceof final IElectricItem ic2ElectricItem) {
            return Optional.of(
                new Long[] { (long) ic2.api.item.ElectricItem.manager.getCharge(aStack),
                    (long) ic2ElectricItem.getMaxCharge(aStack) });
        }

        return Optional.empty();
    }

    /**
     * Allow item to be inserted into ic2 toolbox
     */
    public static void registerBoxableItemToToolBox(ItemStack aStack) {
        if (aStack != null) {
            try {
                ic2.api.item.ItemWrapper.registerBoxable(aStack.getItem(), (IBoxable) sBoxableWrapper);
            } catch (Throwable ignored) {
                /* Do nothing */
            }
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

        if (GTUtility.areStacksEqual(aStack, getIC2Item("waterCell", 1, W))) {
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
        public static long MIRRORED = B[0];
        /**
         * Buffers the Recipe for later addition. This makes things more efficient.
         */
        public static long BUFFERED = B[1];
        /**
         * This is a special Tag I used for crafting Coins up and down.
         */
        public static long KEEPNBT = B[2];
        /**
         * Makes the Recipe Reverse Craftable in the Disassembler.
         */
        public static long DISMANTLEABLE = B[3];
        /**
         * Prevents the Recipe from accidentally getting removed by my own Handlers.
         */
        public static long NOT_REMOVABLE = B[4];
        /**
         * Reverses the Output of the Recipe for smelting and pulverising.
         */
        public static long REVERSIBLE = B[5];
        /**
         * Removes all Recipes with the same Output Item regardless of NBT, unless another Recipe Deletion Bit is added
         * too.
         */
        public static long DELETE_ALL_OTHER_RECIPES = B[6];
        /**
         * Removes all Recipes with the same Output Item limited to the same NBT.
         */
        public static long DELETE_ALL_OTHER_RECIPES_IF_SAME_NBT = B[7];
        /**
         * Removes all Recipes with the same Output Item limited to Shaped Recipes.
         */
        public static long DELETE_ALL_OTHER_SHAPED_RECIPES = B[8];
        /**
         * Removes all Recipes with the same Output Item limited to native Recipe Handlers.
         */
        public static long DELETE_ALL_OTHER_NATIVE_RECIPES = B[9];
        /**
         * Disables the check for colliding Recipes.
         */
        public static long DO_NOT_CHECK_FOR_COLLISIONS = B[10];
        /**
         * Only adds the Recipe if there is another Recipe having that Output
         */
        public static long ONLY_ADD_IF_THERE_IS_ANOTHER_RECIPE_FOR_IT = B[11];
        /**
         * Only adds the Recipe if it has an Output
         */
        public static long ONLY_ADD_IF_RESULT_IS_NOT_NULL = B[12];
        /**
         * Don't remove shapeless recipes with this output
         */
        public static long DONT_REMOVE_SHAPELESS = B[13];
    }
}
