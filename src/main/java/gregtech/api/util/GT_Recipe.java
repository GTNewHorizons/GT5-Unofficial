package gregtech.api.util;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.D2;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.L;
import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.NEICustomDiagrams;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.check.FindRecipeResult.*;
import static gregtech.api.util.GT_RecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUIDSTACK_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUIDSTACK_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUID_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OR_FLUID_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OR_FLUID_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.GT_RecipeTemplate;
import static gregtech.api.util.GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES;
import static gregtech.api.util.GT_RecipeMapUtil.asTemplate;
import static gregtech.api.util.GT_RecipeMapUtil.buildOrEmpty;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static gregtech.api.util.GT_Utility.isArrayEmptyOrNull;
import static gregtech.api.util.GT_Utility.isArrayOfLength;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.util.ReadableNumberConverter;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.SubTag;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.interfaces.IGT_RecipeMap;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_RecipeMapUtil.GT_RecipeTemplate;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.api.util.item.ItemHolder;
import gregtech.api.util.recipe.RecipeInputRequirements;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.items.GT_FluidDisplayItem;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.power.EUPower;
import gregtech.common.power.Power;
import gregtech.common.power.UnspecifiedEUPower;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Replicator;
import gregtech.nei.FusionSpecialValueFormatter;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.HeatingCoilSpecialValueFormatter;
import gregtech.nei.INEISpecialInfoFormatter;
import gregtech.nei.NEIRecipeInfo;
import ic2.core.Ic2Items;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it
 * ruins compatibility This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you
 * can access this File. Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the
 * correct Functions for these Constructors.
 * <p/>
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then
 * remove said erroreous Functions.
 */
public class GT_Recipe implements Comparable<GT_Recipe> {

    /**
     * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs,
     * please add a new Recipe, because of the HashMaps.
     */
    public ItemStack[] mInputs, mOutputs;
    /**
     * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs,
     * please add a new Recipe, because of the HashMaps.
     */
    public FluidStack[] mFluidInputs, mFluidOutputs;
    /**
     * If you changed the amount of Array-Items inside the Output Array then the length of this Array must be larger or
     * equal to the Output Array. A chance of 10000 equals 100%
     */
    public int[] mChances;
    /**
     * An Item that needs to be inside the Special Slot, like for example the Copy Slot inside the Printer. This is only
     * useful for Fake Recipes in NEI, since findRecipe() and containsInput() don't give a shit about this Field. Lists
     * are also possible.
     */
    public Object mSpecialItems;

    public int mDuration, mEUt, mSpecialValue;
    /**
     * Use this to just disable a specific Recipe, but the Configuration enables that already for every single Recipe.
     */
    public boolean mEnabled = true;
    /**
     * If this Recipe is hidden from NEI
     */
    public boolean mHidden = false;
    /**
     * If this Recipe is Fake and therefore doesn't get found by the findRecipe Function (It is still in the HashMaps,
     * so that containsInput does return T on those fake Inputs)
     */
    public boolean mFakeRecipe = false;
    /**
     * If this Recipe can be stored inside a Machine in order to make Recipe searching more Efficient by trying the
     * previously used Recipe first. In case you have a Recipe Map overriding things and returning one time use Recipes,
     * you have to set this to F.
     */
    public boolean mCanBeBuffered = true;
    /**
     * If this Recipe needs the Output Slots to be completely empty. Needed in case you have randomised Outputs
     */
    public boolean mNeedsEmptyOutput = false;
    /**
     * Used for describing recipes that do not fit the default recipe pattern (for example Large Boiler Fuels)
     */
    private String[] neiDesc = null;
    /**
     * Stores which mod added this recipe
     */
    public List<ModContainer> owners = new ArrayList<>();
    /**
     * Stores stack traces where this recipe was added
     */
    public List<List<StackTraceElement>> stackTraces = new ArrayList<>();

    private GT_Recipe(GT_Recipe aRecipe, boolean shallow) {
        mInputs = shallow ? aRecipe.mInputs : GT_Utility.copyItemArray(aRecipe.mInputs);
        mOutputs = shallow ? aRecipe.mOutputs : GT_Utility.copyItemArray(aRecipe.mOutputs);
        mSpecialItems = aRecipe.mSpecialItems;
        mChances = aRecipe.mChances;
        mFluidInputs = shallow ? aRecipe.mFluidInputs : GT_Utility.copyFluidArray(aRecipe.mFluidInputs);
        mFluidOutputs = shallow ? aRecipe.mFluidOutputs : GT_Utility.copyFluidArray(aRecipe.mFluidOutputs);
        mDuration = aRecipe.mDuration;
        mSpecialValue = aRecipe.mSpecialValue;
        mEUt = aRecipe.mEUt;
        mNeedsEmptyOutput = aRecipe.mNeedsEmptyOutput;
        mCanBeBuffered = aRecipe.mCanBeBuffered;
        mFakeRecipe = aRecipe.mFakeRecipe;
        mEnabled = aRecipe.mEnabled;
        mHidden = aRecipe.mHidden;
        owners = new ArrayList<>(aRecipe.owners);
        reloadOwner();
    }

    // only used for GT_RecipeBuilder. Should not be called otherwise
    GT_Recipe(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs, FluidStack[] mFluidOutputs,
        int[] mChances, Object mSpecialItems, int mDuration, int mEUt, int mSpecialValue, boolean mEnabled,
        boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered, boolean mNeedsEmptyOutput, String[] neiDesc) {
        this.mInputs = mInputs;
        this.mOutputs = mOutputs;
        this.mFluidInputs = mFluidInputs;
        this.mFluidOutputs = mFluidOutputs;
        this.mChances = mChances;
        this.mSpecialItems = mSpecialItems;
        this.mDuration = mDuration;
        this.mEUt = mEUt;
        this.mSpecialValue = mSpecialValue;
        this.mEnabled = mEnabled;
        this.mHidden = mHidden;
        this.mFakeRecipe = mFakeRecipe;
        this.mCanBeBuffered = mCanBeBuffered;
        this.mNeedsEmptyOutput = mNeedsEmptyOutput;
        this.neiDesc = neiDesc;

        reloadOwner();
    }

    public GT_Recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (aInputs == null) aInputs = new ItemStack[0];
        if (aOutputs == null) aOutputs = new ItemStack[0];
        if (aFluidInputs == null) aFluidInputs = new FluidStack[0];
        if (aFluidOutputs == null) aFluidOutputs = new FluidStack[0];
        if (aChances == null) aChances = new int[aOutputs.length];
        if (aChances.length < aOutputs.length) aChances = Arrays.copyOf(aChances, aOutputs.length);

        aInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        aOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        aFluidInputs = ArrayExt.withoutNulls(aFluidInputs, FluidStack[]::new);
        aFluidOutputs = ArrayExt.withoutNulls(aFluidOutputs, FluidStack[]::new);

        GT_OreDictUnificator.setStackArray(true, aInputs);
        GT_OreDictUnificator.setStackArray(true, aOutputs);

        for (ItemStack tStack : aOutputs) GT_Utility.updateItemStack(tStack);

        for (int i = 0; i < aChances.length; i++) if (aChances[i] <= 0) aChances[i] = 10000;
        for (int i = 0; i < aFluidInputs.length; i++) aFluidInputs[i] = aFluidInputs[i].copy();
        for (int i = 0; i < aFluidOutputs.length; i++) aFluidOutputs[i] = aFluidOutputs[i].copy();

        for (ItemStack aInput : aInputs)
            if (aInput != null && Items.feather.getDamage(aInput) != W) for (int j = 0; j < aOutputs.length; j++) {
                if (GT_Utility.areStacksEqual(aInput, aOutputs[j]) && aChances[j] >= 10000) {
                    if (aInput.stackSize >= aOutputs[j].stackSize) {
                        aInput.stackSize -= aOutputs[j].stackSize;
                        aOutputs[j] = null;
                    } else {
                        aOutputs[j].stackSize -= aInput.stackSize;
                    }
                }
            }

        if (aOptimize && aDuration >= 32) {
            ArrayList<ItemStack> tList = new ArrayList<>();
            tList.addAll(Arrays.asList(aInputs));
            tList.addAll(Arrays.asList(aOutputs));
            for (int i = 0; i < tList.size(); i++) if (tList.get(i) == null) tList.remove(i--);

            for (byte i = (byte) Math.min(64, aDuration / 16); i > 1; i--) if (aDuration / i >= 16) {
                boolean temp = true;
                for (ItemStack stack : tList) if (stack.stackSize % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack aFluidInput : aFluidInputs) if (aFluidInput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack aFluidOutput : aFluidOutputs) if (aFluidOutput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) {
                    for (ItemStack itemStack : tList) itemStack.stackSize /= i;
                    for (FluidStack aFluidInput : aFluidInputs) aFluidInput.amount /= i;
                    for (FluidStack aFluidOutput : aFluidOutputs) aFluidOutput.amount /= i;
                    aDuration /= i;
                }
            }
        }

        mInputs = aInputs;
        mOutputs = aOutputs;
        mSpecialItems = aSpecialItems;
        mChances = aChances;
        mFluidInputs = aFluidInputs;
        mFluidOutputs = aFluidOutputs;
        mDuration = aDuration;
        mSpecialValue = aSpecialValue;
        mEUt = aEUt;
        // checkCellBalance();
        reloadOwner();
    }

    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aFuelValue, int aType) {
        this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
    }

    private static FluidStack[] tryGetFluidInputsFromCells(ItemStack aInput) {
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
        return tFluid == null ? null : new FluidStack[] { tFluid };
    }

    // aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        int aSpecialValue, int aType) {
        this(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
            null,
            null,
            null,
            null,
            0,
            0,
            Math.max(1, aSpecialValue));

        if (mInputs.length > 0 && aSpecialValue > 0) {
            switch (aType) {
                // Diesel Generator
                case 0 -> {
                    GT_Recipe_Map.sDieselFuels.addRecipe(this);
                    GT_Recipe_Map.sLargeBoilerFakeFuels.addDieselRecipe(this);
                }
                // Gas Turbine
                case 1 -> GT_Recipe_Map.sTurbineFuels.addRecipe(this);

                // Thermal Generator
                case 2 -> GT_Recipe_Map.sHotFuels.addRecipe(this);

                // Plasma Generator
                case 4 -> GT_Recipe_Map.sPlasmaFuels.addRecipe(this);

                // Magic Generator
                case 5 -> GT_Recipe_Map.sMagicFuels.addRecipe(this);

                // Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
                default -> {
                    GT_Recipe_Map.sDenseLiquidFuels.addRecipe(this);
                    GT_Recipe_Map.sLargeBoilerFakeFuels.addDenseLiquidRecipe(this);
                }
            }
        }
    }

    public GT_Recipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aDuration, int aEUt,
        int aSpecialValue) {
        this(
            true,
            null,
            null,
            null,
            null,
            new FluidStack[] { aInput1, aInput2 },
            new FluidStack[] { aOutput1 },
            Math.max(aDuration, 1),
            aEUt,
            Math.max(Math.min(aSpecialValue, 160000000), 0));
        if (mInputs.length > 1) {
            GT_Recipe_Map.sFusionRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        this(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sLatheRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, int aCellAmount, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt) {
        this(
            true,
            new ItemStack[] { aInput1,
                aCellAmount > 0 ? ItemList.Cell_Empty.get(Math.min(64, Math.max(1, aCellAmount))) : null },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            Math.max(aEUt, 1),
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sDistillationRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
        this(
            true,
            new ItemStack[] { aInput1,
                GT_ModHandler.getIC2Item(
                    "industrialTnt",
                    aInput2 > 0 ? Math.min(aInput2, 64) : 1,
                    new ItemStack(Blocks.tnt, aInput2 > 0 ? Math.min(aInput2, 64) : 1)) },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            20,
            30,
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sImplosionRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aOutput1) {
        this(
            true,
            new ItemStack[] { aInput1, ItemList.Circuit_Integrated.getWithDamage(0, aInput1.stackSize) },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            Math.max(aEUt, 1),
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sBenderRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, ItemStack aInput2, int aEUt, int aDuration, ItemStack aOutput1) {
        this(
            true,
            aInput2 == null ? new ItemStack[] { aInput1 } : new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            Math.max(aEUt, 1),
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sAlloySmelterRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1,
        ItemStack aOutput2) {
        this(
            true,
            aInput2 == null ? new ItemStack[] { aInput1 } : new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            Math.max(aEUt, 1),
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sCannerRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
        this(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            120,
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sVacuumRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt, int VACUUM) {
        this(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            Math.max(aDuration, 1),
            aEUt,
            0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            GT_Recipe_Map.sVacuumRecipes.addRecipe(this);
        }
    }

    public GT_Recipe(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt) {
        this(
            false,
            null,
            null,
            null,
            null,
            new FluidStack[] { aInput1 },
            new FluidStack[] { aOutput1 },
            Math.max(aDuration, 1),
            aEUt,
            0);
        if (mFluidInputs.length > 0 && mFluidOutputs[0] != null) {
            GT_Recipe_Map.sVacuumRecipes.addRecipe(this);
        }
    }

    // Dummy GT_Recipe maker...
    public GT_Recipe(ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        this(
            true,
            aInputs,
            aOutputs,
            aSpecialItems,
            aChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
    }

    public static void reInit() {
        GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
        for (GT_Recipe_Map tMapEntry : GT_Recipe_Map.sMappings) tMapEntry.reInit();
    }

    // -----
    // Old Constructors, do not use!
    // -----

    public ItemStack getRepresentativeInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mInputs.length) return null;
        return GT_Utility.copyOrNull(mInputs[aIndex]);
    }

    public ItemStack getOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mOutputs.length) return null;
        return GT_Utility.copyOrNull(mOutputs[aIndex]);
    }

    public int getOutputChance(int aIndex) {
        if (mChances == null) return 10000;
        if (aIndex < 0 || aIndex >= mChances.length) return 10000;
        return mChances[aIndex];
    }

    public FluidStack getRepresentativeFluidInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidInputs.length || mFluidInputs[aIndex] == null) return null;
        return mFluidInputs[aIndex].copy();
    }

    public FluidStack getFluidOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidOutputs.length || mFluidOutputs[aIndex] == null) return null;
        return mFluidOutputs[aIndex].copy();
    }

    public void checkCellBalance() {
        if (!D2 || mInputs.length < 1) return;

        int tInputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInputs);
        int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutputs);

        if (tInputAmount < tOutputAmount) {
            if (!Materials.Tin.contains(mInputs)) {
                GT_Log.err.println("You get more Cells, than you put in? There must be something wrong.");
                new Exception().printStackTrace(GT_Log.err);
            }
        } else if (tInputAmount > tOutputAmount) {
            if (!Materials.Tin.contains(mOutputs)) {
                GT_Log.err.println("You get less Cells, than you put in? GT Machines usually don't destroy Cells.");
                new Exception().printStackTrace(GT_Log.err);
            }
        }
    }

    public GT_Recipe copy() {
        return new GT_Recipe(this, false);
    }

    public GT_Recipe copyShallow() {
        return new GT_Recipe(this, true);
    }

    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, FluidStack[] aFluidInputs,
        ItemStack... aInputs) {
        return isRecipeInputEqual(aDecreaseStacksizeBySuccess, false, 1, aFluidInputs, aInputs);
    }

    // For non-multiplied recipe amount values
    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes,
        FluidStack[] aFluidInputs, ItemStack... aInputs) {
        return isRecipeInputEqual(aDecreaseStacksizeBySuccess, aDontCheckStackSizes, 1, aFluidInputs, aInputs);
    }

    /**
     * Okay, did some code archeology to figure out what's going on here.
     *
     * <p>
     * This variable was added in <a
     * href=https://github.com/GTNewHorizons/GT5-Unofficial/commit/9959ab7443982a19ad329bca424ab515493432e9>this
     * commit,</a> in order to fix the issues mentioned in <a
     * href=https://github.com/GTNewHorizons/GT5-Unofficial/pull/183>the PR</a>.
     *
     * <p>
     * It looks like it controls checking NBT. At this point, since we are still using universal fluid cells which store
     * their fluids in NBT, it probably will not be safe to disable the NBT checks in the near future. Data sticks may
     * be another case. Anyway, we probably can't get rid of this without some significant changes to clean up recipe
     * inputs.
     */
    public static boolean GTppRecipeHelper;

    /**
     * WARNING: Do not call this method with both {@code aDecreaseStacksizeBySuccess} and {@code aDontCheckStackSizes}
     * set to {@code true}! You'll get weird behavior.
     */
    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes,
        int amountMultiplier, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (mInputs.length > 0 && aInputs == null) return false;
        if (mFluidInputs.length > 0 && aFluidInputs == null) return false;

        // We need to handle 0-size recipe inputs. These are for inputs that don't get consumed.
        boolean inputFound;
        int remainingCost;

        // Array tracking modified fluid amounts. For efficiency, we will lazily initialize this array.
        // We use Integer so that we can have null as the default value, meaning unchanged.
        Integer[] newFluidAmounts = null;
        if (aFluidInputs != null) {
            newFluidAmounts = new Integer[aFluidInputs.length];

            for (FluidStack recipeFluidCost : mFluidInputs) {
                if (recipeFluidCost != null) {
                    inputFound = false;
                    remainingCost = recipeFluidCost.amount * amountMultiplier;

                    for (int i = 0; i < aFluidInputs.length; i++) {
                        FluidStack providedFluid = aFluidInputs[i];
                        if (providedFluid != null && providedFluid.isFluidEqual(recipeFluidCost)) {
                            inputFound = true;
                            if (newFluidAmounts[i] == null) {
                                newFluidAmounts[i] = providedFluid.amount;
                            }

                            if (aDontCheckStackSizes || newFluidAmounts[i] >= remainingCost) {
                                newFluidAmounts[i] -= remainingCost;
                                remainingCost = 0;
                                break;
                            } else {
                                remainingCost -= newFluidAmounts[i];
                                newFluidAmounts[i] = 0;
                            }
                        }
                    }

                    if (remainingCost > 0 || !inputFound) {
                        // Cost not satisfied, or for non-consumed inputs, input not found.
                        return false;
                    }
                }
            }
        }

        // Array tracking modified item stack sizes. For efficiency, we will lazily initialize this array.
        // We use Integer so that we can have null as the default value, meaning unchanged.
        Integer[] newItemAmounts = null;
        if (aInputs != null) {
            newItemAmounts = new Integer[aInputs.length];

            for (ItemStack recipeItemCost : mInputs) {
                ItemStack unifiedItemCost = GT_OreDictUnificator.get_nocopy(true, recipeItemCost);
                if (unifiedItemCost != null) {
                    inputFound = false;
                    remainingCost = recipeItemCost.stackSize * amountMultiplier;

                    for (int i = 0; i < aInputs.length; i++) {
                        ItemStack providedItem = aInputs[i];
                        if (GT_OreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost)) {
                            if (GTppRecipeHelper) { // Please see JavaDoc on GTppRecipeHelper for why this is here.
                                if (GT_Utility.areStacksEqual(providedItem, Ic2Items.FluidCell.copy(), true)
                                    || GT_Utility.areStacksEqual(providedItem, ItemList.Tool_DataStick.get(1L), true)
                                    || GT_Utility.areStacksEqual(providedItem, ItemList.Tool_DataOrb.get(1L), true)) {
                                    if (!GT_Utility.areStacksEqual(providedItem, recipeItemCost, false)) continue;
                                }
                            }

                            inputFound = true;
                            if (newItemAmounts[i] == null) {
                                newItemAmounts[i] = providedItem.stackSize;
                            }

                            if (aDontCheckStackSizes || newItemAmounts[i] >= remainingCost) {
                                newItemAmounts[i] -= remainingCost;
                                remainingCost = 0;
                                break;
                            } else {
                                remainingCost -= newItemAmounts[i];
                                newItemAmounts[i] = 0;
                            }
                        }
                    }

                    if (remainingCost > 0 || !inputFound) {
                        // Cost not satisfied, or for non-consumed inputs, input not found.
                        return false;
                    }
                }
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            // Copy modified amounts into the input stacks.
            if (aFluidInputs != null) {
                for (int i = 0; i < aFluidInputs.length; i++) {
                    if (newFluidAmounts[i] != null) {
                        aFluidInputs[i].amount = newFluidAmounts[i];
                    }
                }
            }

            if (aInputs != null) {
                for (int i = 0; i < aInputs.length; i++) {
                    if (newItemAmounts[i] != null) {
                        aInputs[i].stackSize = newItemAmounts[i];
                    }
                }
            }
        }

        return true;
    }

    public boolean isRecipePossible(@Nullable ItemInventoryLogic itemInput, @Nullable FluidInventoryLogic fluidInput) {
        return getAmountOfRecipesDone(itemInput, fluidInput, 1, true) > 0;
    }

    public long getAmountOfRecipesDone(@Nullable ItemInventoryLogic itemInput, @Nullable FluidInventoryLogic fluidInput,
        long maxParallel, boolean simulate) {
        if (itemInput == null) {
            itemInput = new ItemInventoryLogic(0);
        }

        if (fluidInput == null) {
            fluidInput = new FluidInventoryLogic(0, 0);
        }

        itemInput.startRecipeCheck();
        Map<ItemHolder, Long> recipeItems = getItemInputsAsItemMap();
        for (Entry<ItemHolder, Long> entry : recipeItems.entrySet()) {
            maxParallel = Math
                .min(maxParallel, itemInput.calculateAmountOfTimesItemCanBeTaken(entry.getKey(), entry.getValue()));
        }

        for (FluidStack fluid : mFluidInputs) {
            if (fluid == null) continue;
            maxParallel = Math
                .min(maxParallel, fluidInput.calculateAmountOfTimesFluidCanBeTaken(fluid.getFluid(), fluid.amount));
        }

        if (simulate) {
            itemInput.stopRecipeCheck();
            return maxParallel;
        }

        for (Entry<ItemHolder, Long> entry : recipeItems.entrySet()) {
            itemInput.subtractItemAmount(entry.getKey(), entry.getValue() * maxParallel, false);
        }

        for (FluidStack fluid : mFluidInputs) {
            if (fluid == null) continue;
            fluidInput.drain(fluid.getFluid(), fluid.amount * maxParallel, false);
        }

        return maxParallel;
    }

    private Map<ItemHolder, Long> getItemInputsAsItemMap() {
        Map<ItemHolder, Long> items = new HashMap<>();
        for (ItemStack item : mInputs) {
            if (item == null) continue;
            ItemHolder itemHolder = new ItemHolder(item);
            items.put(itemHolder, items.getOrDefault(itemHolder, 0L) + item.stackSize);
        }
        return items;
    }

    @Override
    public int compareTo(GT_Recipe recipe) {
        // first lowest tier recipes
        // then fastest
        // then with lowest special value
        // then dry recipes
        // then with fewer inputs
        if (this.mEUt != recipe.mEUt) {
            return this.mEUt - recipe.mEUt;
        } else if (this.mDuration != recipe.mDuration) {
            return this.mDuration - recipe.mDuration;
        } else if (this.mSpecialValue != recipe.mSpecialValue) {
            return this.mSpecialValue - recipe.mSpecialValue;
        } else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
            return this.mFluidInputs.length - recipe.mFluidInputs.length;
        } else if (this.mInputs.length != recipe.mInputs.length) {
            return this.mInputs.length - recipe.mInputs.length;
        }
        return 0;
    }

    public String[] getNeiDesc() {
        return neiDesc;
    }

    /**
     * Sets description shown on NEI. <br>
     * If you have a large number of recipes for the recipemap, this is not efficient memory wise, so use
     * {@link GT_Recipe_Map#setNEISpecialInfoFormatter} instead.
     */
    protected void setNeiDesc(String... neiDesc) {
        this.neiDesc = neiDesc;
    }

    /**
     * Use {@link GT_Recipe_Map#getItemInputPositions} or {@link GT_Recipe_Map#getSpecialItemPosition} or
     * {@link GT_Recipe_Map#getFluidInputPositions} instead
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public ArrayList<PositionedStack> getInputPositionedStacks() {
        return null;
    }

    /**
     * Use {@link GT_Recipe_Map#getItemOutputPositions} or {@link GT_Recipe_Map#getFluidOutputPositions} instead
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public ArrayList<PositionedStack> getOutputPositionedStacks() {
        return null;
    }

    public void reloadOwner() {
        setOwner(
            Loader.instance()
                .activeModContainer());

        final List<String> excludedClasses = Arrays.asList(
            "java.lang.Thread",
            "gregtech.api.util.GT_Recipe",
            "gregtech.api.util.GT_RecipeBuilder",
            "gregtech.api.util.GT_Recipe$GT_Recipe_Map",
            "gregtech.common.GT_RecipeAdder");
        if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace) {
            List<StackTraceElement> toAdd = new ArrayList<>();
            for (StackTraceElement stackTrace : Thread.currentThread()
                .getStackTrace()) {
                if (excludedClasses.stream()
                    .noneMatch(
                        c -> stackTrace.getClassName()
                            .equals(c))) {
                    toAdd.add(stackTrace);
                }
            }
            stackTraces.add(toAdd);
        }
    }

    public void setOwner(ModContainer newOwner) {
        ModContainer oldOwner = owners.size() > 0 ? this.owners.get(owners.size() - 1) : null;
        if (newOwner != null && newOwner != oldOwner) {
            owners.add(newOwner);
        }
    }

    /**
     * Use in case {@link Loader#activeModContainer()} isn't helpful
     */
    public void setOwner(String modId) {
        for (ModContainer mod : Loader.instance()
            .getModList()) {
            if (mod.getModId()
                .equals(modId)) {
                setOwner(mod);
                return;
            }
        }
    }

    public GT_Recipe setInputs(ItemStack... aInputs) {
        // TODO determine if we need this without trailing nulls call
        this.mInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        return this;
    }

    public GT_Recipe setOutputs(ItemStack... aOutputs) {
        this.mOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        return this;
    }

    public GT_Recipe setFluidInputs(FluidStack... aInputs) {
        this.mFluidInputs = ArrayExt.withoutTrailingNulls(aInputs, FluidStack[]::new);
        return this;
    }

    public GT_Recipe setFluidOutputs(FluidStack... aOutputs) {
        this.mFluidOutputs = ArrayExt.withoutTrailingNulls(aOutputs, FluidStack[]::new);
        return this;
    }

    public GT_Recipe setDuration(int aDuration) {
        this.mDuration = aDuration;
        return this;
    }

    public GT_Recipe setEUt(int aEUt) {
        this.mEUt = aEUt;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof GT_Recipe recipe)) return false;
        for (int i = 0; i < Math.min(mInputs.length, recipe.mInputs.length); i++) {
            if (mInputs[i] == null && recipe.mInputs[i] == null) continue;
            if (mInputs[i] == null || recipe.mInputs[i] == null) return false;
            ItemHolder currentIH = new ItemHolder(mInputs[i]);
            ItemHolder otherIH = new ItemHolder(recipe.mInputs[i]);
            if (!currentIH.equals(otherIH)) return false;
        }
        for (int i = 0; i < Math.min(mFluidInputs.length, recipe.mFluidInputs.length); i++) {
            if (mFluidInputs[i] == null && recipe.mFluidInputs[i] == null) continue;
            if (mFluidInputs[i] == null || recipe.mFluidInputs[i] == null) return false;
            if (!FluidStack.areFluidStackTagsEqual(mFluidInputs[i], recipe.mFluidInputs[i])) return false;
        }
        return mDuration == recipe.mDuration && mEUt == recipe.mEUt && mSpecialValue == recipe.mSpecialValue;
    }

    public static class GT_Recipe_AssemblyLine {

        public static final ArrayList<GT_Recipe_AssemblyLine> sAssemblylineRecipes = new ArrayList<>();

        static {
            if (!Boolean.getBoolean("com.gtnh.gt5u.ignore-invalid-assline-recipe"))
                GregTech_API.sFirstWorldTick.add(GT_Recipe_AssemblyLine::checkInvalidRecipes);
            else GT_Log.out.println("NOT CHECKING INVALID ASSLINE RECIPE.");
        }

        private static void checkInvalidRecipes() {
            int invalidCount = 0;
            GT_Log.out.println("Started assline validation");
            for (GT_Recipe_AssemblyLine recipe : sAssemblylineRecipes) {
                if (recipe.getPersistentHash() == 0) {
                    invalidCount++;
                    GT_Log.err.printf("Invalid recipe: %s%n", recipe);
                }
            }
            if (invalidCount > 0) throw new RuntimeException(
                "There are " + invalidCount + " invalid assembly line recipe(s)! Check GregTech.log for details!");
        }

        public ItemStack mResearchItem;
        public int mResearchTime;
        public ItemStack[] mInputs;
        public FluidStack[] mFluidInputs;
        public ItemStack mOutput;
        public int mDuration;
        public int mEUt;
        public ItemStack[][] mOreDictAlt;
        private int mPersistentHash;

        /**
         * THIS CONSTRUCTOR DOES SET THE PERSISTENT HASH.
         * <p>
         * if you set one yourself, it will give you one of the RunetimeExceptions!
         */
        public GT_Recipe_AssemblyLine(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
            this(
                aResearchItem,
                aResearchTime,
                aInputs,
                aFluidInputs,
                aOutput,
                aDuration,
                aEUt,
                new ItemStack[aInputs.length][]);
            int tPersistentHash = 1;
            for (ItemStack tInput : aInputs)
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tInput, true, false);
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
            for (FluidStack tFluidInput : aFluidInputs)
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tFluidInput, true, false);
            tPersistentHash = tPersistentHash * 31 + aResearchTime;
            tPersistentHash = tPersistentHash * 31 + aDuration;
            tPersistentHash = tPersistentHash * 31 + aEUt;
            setPersistentHash(tPersistentHash);
        }

        /**
         * THIS CONSTRUCTOR DOES <b>NOT</b> SET THE PERSISTENT HASH.
         * <p>
         * if you don't set one yourself, it will break a lot of stuff!
         */
        public GT_Recipe_AssemblyLine(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt, ItemStack[][] aAlt) {
            mResearchItem = aResearchItem;
            mResearchTime = aResearchTime;
            mInputs = aInputs;
            mFluidInputs = aFluidInputs;
            mOutput = aOutput;
            mDuration = aDuration;
            mEUt = aEUt;
            mOreDictAlt = aAlt;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            GT_ItemStack[] thisInputs = new GT_ItemStack[this.mInputs.length];
            int totalInputStackSize = 0;
            for (int i = 0; i < this.mInputs.length; i++) {
                thisInputs[i] = new GT_ItemStack(this.mInputs[i]);
                totalInputStackSize += thisInputs[i].mStackSize;
            }
            int inputHash = Arrays.deepHashCode(thisInputs);
            int inputFluidHash = Arrays.deepHashCode(this.mFluidInputs);
            GT_ItemStack thisOutput = new GT_ItemStack(mOutput);
            GT_ItemStack thisResearch = new GT_ItemStack(mResearchItem);
            int miscRecipeDataHash = Arrays.deepHashCode(
                new Object[] { totalInputStackSize, mDuration, mEUt, thisOutput, thisResearch, mResearchTime });
            result = prime * result + inputFluidHash;
            result = prime * result + inputHash;
            result = prime * result + miscRecipeDataHash;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof GT_Recipe_AssemblyLine other)) {
                return false;
            }
            if (this.mInputs.length != other.mInputs.length) {
                return false;
            }
            if (this.mFluidInputs.length != other.mFluidInputs.length) {
                return false;
            }
            // Check Outputs Match
            GT_ItemStack output1 = new GT_ItemStack(this.mOutput);
            GT_ItemStack output2 = new GT_ItemStack(other.mOutput);
            if (!output1.equals(output2)) {
                return false;
            }
            // Check Scanned Item Match
            GT_ItemStack scan1 = new GT_ItemStack(this.mResearchItem);
            GT_ItemStack scan2 = new GT_ItemStack(other.mResearchItem);
            if (!scan1.equals(scan2)) {
                return false;
            }
            // Check Items Match
            GT_ItemStack[] thisInputs = new GT_ItemStack[this.mInputs.length];
            GT_ItemStack[] otherInputs = new GT_ItemStack[other.mInputs.length];
            for (int i = 0; i < thisInputs.length; i++) {
                thisInputs[i] = new GT_ItemStack(this.mInputs[i]);
                otherInputs[i] = new GT_ItemStack(other.mInputs[i]);
            }
            for (int i = 0; i < thisInputs.length; i++) {
                if (!thisInputs[i].equals(otherInputs[i]) || thisInputs[i].mStackSize != otherInputs[i].mStackSize) {
                    return false;
                }
            }
            // Check Fluids Match
            for (int i = 0; i < this.mFluidInputs.length; i++) {
                if (!this.mFluidInputs[i].isFluidStackIdentical(other.mFluidInputs[i])) {
                    return false;
                }
            }

            return this.mDuration == other.mDuration && this.mEUt == other.mEUt
                && this.mResearchTime == other.mResearchTime;
        }

        public int getPersistentHash() {
            if (mPersistentHash == 0)
                GT_Log.err.println("Assline recipe persistent hash has not been set! Recipe: " + mOutput);
            return mPersistentHash;
        }

        @Override
        public String toString() {
            return "GT_Recipe_AssemblyLine{" + "mResearchItem="
                + mResearchItem
                + ", mResearchTime="
                + mResearchTime
                + ", mInputs="
                + Arrays.toString(mInputs)
                + ", mFluidInputs="
                + Arrays.toString(mFluidInputs)
                + ", mOutput="
                + mOutput
                + ", mDuration="
                + mDuration
                + ", mEUt="
                + mEUt
                + ", mOreDictAlt="
                + Arrays.toString(mOreDictAlt)
                + '}';
        }

        /**
         * @param aPersistentHash the persistent hash. it should reflect the exact input used to generate this recipe If
         *                        0 is passed in, the actual persistent hash will be automatically remapped to 1
         *                        instead.
         * @throws IllegalStateException if the persistent hash has been set already
         */
        public void setPersistentHash(int aPersistentHash) {
            if (this.mPersistentHash != 0) throw new IllegalStateException("Cannot set persistent hash twice!");
            if (aPersistentHash == 0) this.mPersistentHash = 1;
            else this.mPersistentHash = aPersistentHash;
        }
    }

    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static class GT_Recipe_Map implements IGT_RecipeMap {

        /**
         * Contains all Recipe Maps
         */
        public static final Collection<GT_Recipe_Map> sMappings = new ArrayList<>();
        /**
         * All recipe maps indexed by their {@link #mUniqueIdentifier}.
         */
        public static final Map<String, GT_Recipe_Map> sIndexedMappings = new HashMap<>();

        static final String TEXTURES_GUI_BASICMACHINES = "textures/gui/basicmachines";
        public static final GT_Recipe_Map sOreWasherRecipes = new GT_Recipe_Map(
            new HashSet<>(500),
            "gt.recipe.orewasher",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "OreWasher"),
            1,
            3,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("orewasher", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW);
        public static final GT_Recipe_Map sThermalCentrifugeRecipes = new GT_Recipe_Map(
            new HashSet<>(1000),
            "gt.recipe.thermalcentrifuge",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ThermalCentrifuge"),
            1,
            3,
            1,
            0,
            2,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("thermalcentrifuge", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sCompressorRecipes = new GT_Recipe_Map(
            new HashSet<>(750),
            "gt.recipe.compressor",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Compressor"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_COMPRESSOR)
                .setRecipeConfigFile("compressor", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_COMPRESSOR_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_COMPRESS_STEAM);
        public static final GT_Recipe_Map sExtractorRecipes = new GT_Recipe_Map(
            new HashSet<>(250),
            "gt.recipe.extractor",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Extractor"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
                .setRecipeConfigFile("extractor", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_EXTRACT_STEAM);
        public static final GT_Recipe_Map sRecyclerRecipes = new GT_Recipe_Map_Recycler(
            new HashSet<>(0),
            "ic.recipe.recycler",
            null,
            "ic2.recycler",
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Recycler"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_RECYCLE)
                .setProgressBar(GT_UITextures.PROGRESSBAR_RECYCLE, ProgressBar.Direction.CIRCULAR_CW);
        public static final GT_Recipe_Map sFurnaceRecipes = new GT_Recipe_Map_Furnace(
            new HashSet<>(0),
            "mc.recipe.furnace",
            "Furnace",
            "smelting",
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM);
        public static final GT_Recipe_Map sMicrowaveRecipes = new GT_Recipe_Map_Microwave(
            new HashSet<>(0),
            "gt.recipe.microwave",
            null,
            "smelting",
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);

        public static final GT_Recipe_Map sScannerFakeRecipes = new GT_Recipe_Map(
            new HashSet<>(300),
            "gt.recipe.scanner",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Scanner"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MICROSCOPE)
                .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sRockBreakerFakeRecipes = new GT_Recipe_Map(
            new HashSet<>(200),
            "gt.recipe.rockbreaker",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "RockBreaker"),
            2,
            1,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
                .setProgressBar(GT_UITextures.PROGRESSBAR_MACERATE, ProgressBar.Direction.RIGHT);
        @Deprecated
        public static final GT_Recipe_Map sByProductList = new GT_Recipe_Map(
            new HashSet<>(1000),
            "gt.recipe.byproductlist",
            "Ore Byproduct List",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            6,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            false).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sReplicatorFakeRecipes = new ReplicatorFakeMap(
            new HashSet<>(100),
            "gt.recipe.replicator",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Replicator"),
            0,
            1,
            0,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_UUM)
                .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sAssemblylineVisualRecipes = new GT_Recipe_Map_AssemblyLineFake(
            new HashSet<>(110),
            "gt.recipe.fakeAssemblylineProcess",
            "Assemblyline Process",
            null,
            GregTech.getResourcePath("textures", "gui", "FakeAssemblyline"),
            16,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
                .setUsualFluidInputCount(4)
                .setDisableOptimize(true);
        /**
         * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
         */
        public static final GT_Recipe_Map sPlasmaArcFurnaceRecipes = new GT_Recipe_Map(
            new HashSet<>(20000),
            "gt.recipe.plasmaarcfurnace",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaArcFurnace"),
            1,
            9,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("arcfurnace", FIRST_ITEM_INPUT);
        /**
         * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
         */
        public static final GT_Recipe_Map sArcFurnaceRecipes = new GT_Recipe_Map(
            new HashSet<>(20000),
            "gt.recipe.arcfurnace",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ArcFurnace"),
            1,
            9,
            1,
            1,
            3,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("arcfurnace", FIRST_ITEM_INPUT);
        public static final GT_Recipe_Map sPrinterRecipes = new GT_Recipe_Map_Printer(
            new HashSet<>(5),
            "gt.recipe.printer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Printer"),
            1,
            1,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_PAGE_BLANK)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_PAGE_PRINTED)
                .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_STICK)
                .setRecipeConfigFile("printer", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sSifterRecipes = new GT_Recipe_Map(
            new HashSet<>(105),
            "gt.recipe.sifter",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Sifter"),
            1,
            9,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
                .setRecipeConfigFile("sifter", FIRST_ITEM_INPUT);
        public static final GT_Recipe_Map sPressRecipes = new GT_Recipe_Map_FormingPress(
            new HashSet<>(300),
            "gt.recipe.press",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Press3"),
            6,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_PRESS_1)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_PRESS_2)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_PRESS_3)
                .setRecipeConfigFile("press", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sLaserEngraverRecipes = new GT_Recipe_Map(
            new HashSet<>(810),
            "gt.recipe.laserengraver",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LaserEngraver2"),
            4,
            4,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_LENS)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("laserengraving", FIRST_ITEM_OUTPUT)
                .setUsualFluidInputCount(2)
                .setUsualFluidOutputCount(2);
        public static final GT_Recipe_Map sMixerRecipes = new GT_Recipe_Map(
            new HashSet<>(900),
            "gt.recipe.mixer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Mixer6"),
            9,
            4,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("mixer", FIRST_ITEM_OR_FLUID_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW);
        public static final GT_Recipe_Map sAutoclaveRecipes = new GT_Recipe_Map(
            new HashSet<>(300),
            "gt.recipe.autoclave",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Autoclave4"),
            2,
            4,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_GEM)
                .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("autoclave", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sElectroMagneticSeparatorRecipes = new GT_Recipe_Map(
            new HashSet<>(50),
            "gt.recipe.electromagneticseparator",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ElectromagneticSeparator"),
            1,
            3,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("electromagneticseparator", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_MAGNET, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sPolarizerRecipes = new GT_Recipe_Map(
            new HashSet<>(300),
            "gt.recipe.polarizer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Polarizer"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_MAGNET, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("polarizer", FIRST_ITEM_INPUT);
        public static final GT_Recipe_Map sMaceratorRecipes = new GT_Recipe_Map_Macerator(
            new HashSet<>(16600),
            "gt.recipe.macerator",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Macerator4"),
            1,
            4,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
                .setProgressBar(GT_UITextures.PROGRESSBAR_MACERATE, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("pulveriser", FIRST_ITEM_INPUT)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE_STEAM)
                .setSlotOverlaySteam(true, GT_UITextures.OVERLAY_SLOT_DUST_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_MACERATE_STEAM);
        public static final GT_Recipe_Map sChemicalBathRecipes = new GT_Recipe_Map(
            new HashSet<>(2550),
            "gt.recipe.chemicalbath",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ChemicalBath"),
            1,
            3,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
                .setRecipeConfigFile("chemicalbath", FIRST_ITEM_INPUT);
        public static final GT_Recipe_Map sFluidCannerRecipes = new GT_Recipe_Map_FluidCanner(
            new HashSet<>(2100),
            "gt.recipe.fluidcanner",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidCanner"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setRecipeConfigFile("canning", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_CANNER, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sBrewingRecipes = new GT_Recipe_Map(
            new HashSet<>(450),
            "gt.recipe.brewer",
            "Brewing Machine",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PotionBrewer"),
            1,
            0,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CAULDRON)
                .setRecipeConfigFile("brewing", FIRST_FLUIDSTACK_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sFluidHeaterRecipes = new GT_Recipe_Map(
            new HashSet<>(10),
            "gt.recipe.fluidheater",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidHeater"),
            1,
            0,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_HEATER_1)
                .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_HEATER_2)
                .setRecipeConfigFile("fluidheater", FIRST_FLUIDSTACK_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sDistilleryRecipes = new GT_Recipe_Map(
            new HashSet<>(400),
            "gt.recipe.distillery",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Distillery"),
            1,
            1,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_BEAKER_1)
                .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_BEAKER_2)
                .setRecipeConfigFile("distillery", FIRST_FLUIDSTACK_OUTPUT)
                .setRecipeSpecialHandler(r -> {
                    int aInput = r.mFluidInputs[0].amount, aOutput = r.mFluidOutputs[0].amount, aDuration = r.mDuration;

                    // reduce the batch size if fluid amount is exceeding
                    int tScale = (Math.max(aInput, aOutput) + 999) / 1000;
                    if (tScale <= 0) tScale = 1;
                    if (tScale > 1) {
                        // trying to find whether there is a better factor
                        for (int i = tScale; i <= 5; i++) {
                            if (aInput % i == 0 && aDuration % i == 0) {
                                tScale = i;
                                break;
                            }
                        }
                        for (int i = tScale; i <= 5; i++) {
                            if (aInput % i == 0 && aDuration % i == 0 && aOutput % i == 0) {
                                tScale = i;
                                break;
                            }
                        }
                        aInput = (aInput + tScale - 1) / tScale;
                        aOutput = aOutput / tScale;
                        if (!isArrayEmptyOrNull(r.mOutputs)) {
                            ItemData tData = GT_OreDictUnificator.getItemData(r.mOutputs[0]);
                            if (tData != null && (tData.mPrefix == OrePrefixes.dust
                                || OrePrefixes.dust.mFamiliarPrefixes.contains(tData.mPrefix))) {
                                r.mOutputs[0] = GT_OreDictUnificator.getDust(
                                    tData.mMaterial.mMaterial,
                                    tData.mMaterial.mAmount * r.mOutputs[0].stackSize / tScale);
                            } else {
                                if (r.mOutputs[0].stackSize / tScale == 0) r.mOutputs[0] = GT_Values.NI;
                                else r.mOutputs[0] = GT_Utility
                                    .copyAmount(r.mOutputs[0].stackSize / tScale, r.mOutputs[0]);
                            }
                        }
                        aDuration = (aDuration + tScale - 1) / tScale;
                        r.mFluidInputs[0] = GT_Utility.copyAmount(aInput, r.mFluidInputs[0]);
                        r.mFluidOutputs[0] = GT_Utility.copyAmount(aOutput, r.mFluidOutputs[0]);
                        r.mDuration = aDuration;
                    }
                })
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sFermentingRecipes = new GT_Recipe_Map(
            new HashSet<>(50),
            "gt.recipe.fermenter",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Fermenter"),
            0,
            0,
            0,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("fermenting", FIRST_FLUIDSTACK_OUTPUT);
        public static final GT_Recipe_Map sFluidSolidficationRecipes = new GT_Recipe_Map(
            new HashSet<>(35000),
            "gt.recipe.fluidsolidifier",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidSolidifier"),
            1,
            1,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MOLD)
                .setRecipeConfigFile("fluidsolidifier", FIRST_ITEM_OUTPUT)
                .setRecipeSpecialHandler(r -> {
                    if (ArrayUtils.isNotEmpty(r.mFluidInputs)) {
                        if (Materials.PhasedGold.getMolten(1)
                            .isFluidEqual(r.mFluidInputs[0]))
                            r.mFluidInputs = new FluidStack[] {
                                Materials.VibrantAlloy.getMolten(r.mFluidInputs[0].amount) };
                        else if (Materials.PhasedIron.getMolten(1)
                            .isFluidEqual(r.mFluidInputs[0]))
                            r.mFluidInputs = new FluidStack[] {
                                Materials.PulsatingIron.getMolten(r.mFluidInputs[0].amount) };
                    }
                })
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sFluidExtractionRecipes = new GT_Recipe_Map(
            new HashSet<>(15000),
            "gt.recipe.fluidextractor",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidExtractor"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
                .setRecipeConfigFile("fluidextractor", FIRST_ITEM_INPUT)
                .setRecipeSpecialHandler(r -> {
                    if (ArrayUtils.isNotEmpty(r.mFluidInputs)) {
                        if (Materials.PhasedGold.getMolten(1)
                            .isFluidEqual(r.mFluidInputs[0]))
                            r.mFluidInputs = new FluidStack[] {
                                Materials.VibrantAlloy.getMolten(r.mFluidInputs[0].amount) };
                        else if (Materials.PhasedIron.getMolten(1)
                            .isFluidEqual(r.mFluidInputs[0]))
                            r.mFluidInputs = new FluidStack[] {
                                Materials.PulsatingIron.getMolten(r.mFluidInputs[0].amount) };
                    }
                })
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sBoxinatorRecipes = new GT_Recipe_Map(
            new HashSet<>(2500),
            "gt.recipe.packager",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Packager"),
            2,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_BOX)
                .setRecipeConfigFile("boxing", FIRST_ITEM_OUTPUT)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_BOXED)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sUnboxinatorRecipes = new GT_Recipe_Map_Unboxinator(
            new HashSet<>(2500),
            "gt.recipe.unpackager",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Unpackager"),
            1,
            2,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BOXED)
                .setRecipeConfigFile("unboxing", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        /**
         * Usually, but not always, you should use {@link GT_RecipeConstants#Fusion} instead.
         */
        public static final GT_Recipe_Map sFusionRecipes = new GT_Recipe_Map_FluidOnly(
            new HashSet<>(50),
            "gt.recipe.fusionreactor",
            "Fusion Reactor",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FusionReactor"),
            0,
            0,
            0,
            2,
            1,
            "Start: ",
            1,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .useComparatorForNEI(true)
                .setUsualFluidInputCount(2)
                .setRecipeConfigFile("fusion", FIRST_FLUID_OUTPUT)
                .setDisableOptimize(true)
                .setNEISpecialInfoFormatter(FusionSpecialValueFormatter.INSTANCE);
        /**
         * Usually, but not always, you should use {@link GT_RecipeConstants#Fusion} instead.
         */
        public static final GT_Recipe_Map sComplexFusionRecipes = new GT_Recipe_Map_ComplexFusion(
            new HashSet<>(50),
            "gt.recipe.complexfusionreactor",
            "Complex Fusion Reactor",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ComplexFusionReactor"),
            3,
            0,
            0,
            2,
            1,
            "Start: ",
            1,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setUsualFluidInputCount(16)
                .setUsualFluidOutputCount(16)
                .setNEITransferRect(new Rectangle(79, 34, 18, 18))
                .setLogoPos(80, 61)
                .setNEISpecialInfoFormatter(FusionSpecialValueFormatter.INSTANCE)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map sCentrifugeRecipes = new GT_Recipe_Map(
            new HashSet<>(1200),
            "gt.recipe.centrifuge",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Centrifuge"),
            2,
            6,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
                .setRecipeConfigFile("centrifuge", FIRST_ITEM_OR_FLUID_INPUT)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_FLUID)
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sElectrolyzerRecipes = new GT_Recipe_Map(
            new HashSet<>(300),
            "gt.recipe.electrolyzer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Electrolyzer"),
            2,
            6,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CHARGER)
                .setRecipeConfigFile("electrolyzer", FIRST_ITEM_OR_FLUID_INPUT)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_CHARGER_FLUID)
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
        /**
         * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
         */
        public static final GT_Recipe_Map sBlastRecipes = new GT_Recipe_Map(
            new HashSet<>(800),
            "gt.recipe.blastfurnace",
            "Blast Furnace",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            6,
            6,
            1,
            0,
            1,
            "Heat Capacity: ",
            1,
            " K",
            false,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("blastfurnace", FIRST_ITEM_INPUT)
                .setNEISpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE);
        /**
         * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
         */
        public static final GT_Recipe_Map sPlasmaForgeRecipes = new GT_Recipe_Map_LargeNEI(
            new HashSet<>(20),
            "gt.recipe.plasmaforge",
            "DTPF",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
            9,
            9,
            0,
            0,
            1,
            "Heat Capacity: ",
            1,
            " K",
            false,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setUsualFluidInputCount(9)
                .setUsualFluidOutputCount(9)
                .setDisableOptimize(true)
                .setNEISpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE);

        public static final GT_Recipe_Map sTranscendentPlasmaMixerRecipes = new TranscendentPlasmaMixerRecipeMap(
            new HashSet<>(20),
            "gt.recipe.transcendentplasmamixerrecipes",
            "Transcendent Plasma Mixer",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
            1,
            0,
            0,
            0,
            1,
            "",
            0,
            "",
            false,
            true).setDisableOptimize(true);

        public static class GT_FakeSpaceProjectRecipe extends GT_Recipe {

            public final String projectName;

            public GT_FakeSpaceProjectRecipe(boolean aOptimize, ItemStack[] aInputs, FluidStack[] aFluidInputs,
                int aDuration, int aEUt, int aSpecialValue, String projectName) {
                super(aOptimize, aInputs, null, null, null, aFluidInputs, null, aDuration, aEUt, aSpecialValue);
                this.projectName = projectName;
            }
        }

        public static final GT_Recipe_Map sFakeSpaceProjectRecipes = new GT_Recipe_Map(
            new HashSet<>(20),
            "gt.recipe.fakespaceprojects",
            "Space Projects",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            12,
            0,
            0,
            0,
            1,
            translateToLocal("gt.specialvalue.stages") + " ",
            1,
            "",
            false,
            true) {

            IDrawable projectTexture;

            @Override
            public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
                IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
                IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
                Supplier<Float> progressSupplier, Pos2d windowOffset) {
                ModularWindow.Builder builder = super.createNEITemplate(
                    itemInputsInventory,
                    itemOutputsInventory,
                    specialSlotInventory,
                    fluidInputsInventory,
                    fluidOutputsInventory,
                    progressSupplier,
                    windowOffset);
                addRecipeSpecificDrawable(
                    builder,
                    windowOffset,
                    () -> projectTexture,
                    new Pos2d(124, 28),
                    new Size(18, 18));
                return builder;
            }

            @Override
            public List<Pos2d> getItemInputPositions(int itemInputCount) {
                return UIHelper.getGridPositions(itemInputCount, 16, 28, 3);
            }

            @Override
            public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
                return UIHelper.getGridPositions(fluidInputCount, 88, 28, 1);
            }

            @Override
            protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
                GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
                super.handleNEIItemOutputTooltip(currentTip, pStack);
                if (pStack.item != null && pStack.item.getItem() instanceof GT_FluidDisplayItem) return currentTip;
                currentTip.add(GRAY + translateToLocal("Item Count: ") + formatNumbers(pStack.realStackSize));
                return currentTip;
            }

            @Override
            public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
                for (PositionedStack stack : neiCachedRecipe.mInputs) {
                    if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack && stack.item != null
                        && !(stack.item.getItem() instanceof GT_FluidDisplayItem)) {
                        int stackSize = ((GT_NEI_DefaultHandler.FixedPositionedStack) stack).realStackSize;
                        String displayString;
                        if (stack.item.stackSize > 9999) {
                            displayString = ReadableNumberConverter.INSTANCE.toWideReadableForm(stackSize);
                        } else {
                            displayString = String.valueOf(stackSize);
                        }
                        drawNEIOverlayText(displayString, stack, 0xffffff, 0.5f, true, Alignment.BottomRight);
                    }
                }
                if (neiCachedRecipe.mRecipe instanceof GT_FakeSpaceProjectRecipe) {
                    ISpaceProject project = SpaceProjectManager
                        .getProject(((GT_FakeSpaceProjectRecipe) neiCachedRecipe.mRecipe).projectName);
                    if (project != null) {
                        projectTexture = project.getTexture();
                        GuiDraw
                            .drawStringC(EnumChatFormatting.BOLD + project.getLocalizedName(), 85, 0, 0x404040, false);
                    }
                }
            }

            @Override
            public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
                int bar1Width = 17;
                int bar2Width = 18;
                builder.widget(
                    new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                        .setDirection(ProgressBar.Direction.RIGHT)
                        .setProgress(() -> progressSupplier.get() * ((float) (bar1Width + bar2Width) / bar1Width))
                        .setSynced(false, false)
                        .setPos(new Pos2d(70, 28).add(windowOffset))
                        .setSize(bar1Width, 72));
                builder.widget(
                    new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                        .setDirection(ProgressBar.Direction.RIGHT)
                        .setProgress(
                            () -> (progressSupplier.get() - ((float) bar1Width / (bar1Width + bar2Width)))
                                * ((float) (bar1Width + bar2Width) / bar2Width))
                        .setSynced(false, false)
                        .setPos(new Pos2d(106, 28).add(windowOffset))
                        .setSize(bar2Width, 72));
            }
        }.useModularUI(true)
            .setRenderRealStackSizes(false)
            .setUsualFluidInputCount(4)
            .setNEIBackgroundOffset(2, 23)
            .setLogoPos(152, 83)
            .setDisableOptimize(true);

        public static class TranscendentPlasmaMixerRecipeMap extends GT_Recipe_Map {

            public TranscendentPlasmaMixerRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
                String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
                super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
                useModularUI(true);
                setUsualFluidInputCount(20);
                setUsualFluidOutputCount(1);
                setProgressBarPos(86, 44);
                setNEITransferRect(
                    new Rectangle(
                        progressBarPos.x - (16 / 2),
                        progressBarPos.y,
                        progressBarSize.width + 16,
                        progressBarSize.height));
                setLogoPos(87, 99);
                setNEIBackgroundSize(172, 118);
            }

            @Override
            public List<Pos2d> getItemInputPositions(int itemInputCount) {
                return UIHelper.getGridPositions(itemInputCount, 60, 8, 1);
            }

            @Override
            public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
                return UIHelper.getGridPositions(fluidInputCount, 6, 26, 4, 5);
            }

            @Override
            public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
                return UIHelper.getGridPositions(fluidOutputCount, 114, 44, 1);
            }

            @Override
            protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
                // These look odd because recipeInfo.recipe.mEUt is actually the EU per litre of fluid processed, not
                // the EU/t.
                drawNEIText(
                    recipeInfo,
                    GT_Utility.trans("152", "Total: ")
                        + formatNumbers(1000L * recipeInfo.recipe.mDuration / 100L * recipeInfo.recipe.mEUt)
                        + " EU");
                // 1000 / (20 ticks * 5 seconds) = 10L/t. 10L/t * x EU/L = 10 * x EU/t.
                long averageUsage = 10L * recipeInfo.recipe.mEUt;
                drawNEIText(
                    recipeInfo,
                    "Average: " + formatNumbers(averageUsage)
                        + " EU/t"
                        + GT_Utility.getTierNameWithParentheses(averageUsage));
            }
        }

        /**
         * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for coal/charcoal amount.
         */
        public static final GT_Recipe_Map sPrimitiveBlastRecipes = new GT_Recipe_Map(
            new HashSet<>(200),
            "gt.recipe.primitiveblastfurnace",
            "Primitive Blast Furnace",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            3,
            3,
            1,
            0,
            1,
            E,
            1,
            E,
            false,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeEmitter(builder -> {
                    Optional<GT_Recipe> rr = builder.validateInputCount(1, 2)
                        .validateOutputCount(1, 2)
                        .validateNoInputFluid()
                        .validateNoOutputFluid()
                        .noOptimize()
                        .build();
                    if (!rr.isPresent()) return Collections.emptyList();
                    ItemStack aInput1 = builder.getItemInputBasic(0);
                    ItemStack aInput2 = builder.getItemInputBasic(1);
                    ItemStack aOutput1 = builder.getItemOutput(0);
                    ItemStack aOutput2 = builder.getItemOutput(1);
                    if ((aInput1 == null && aInput2 == null) || (aOutput1 == null && aOutput2 == null))
                        return Collections.emptyList();
                    int aCoalAmount = builder.getMetadata(ADDITIVE_AMOUNT);
                    if (aCoalAmount <= 0) return Collections.emptyList();
                    GT_RecipeTemplate coll = asTemplate(rr.get());
                    for (Materials coal : new Materials[] { Materials.Coal, Materials.Charcoal }) {
                        coll.derive()
                            .setInputs(aInput1, aInput2, coal.getGems(aCoalAmount))
                            .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount));
                        coll.derive()
                            .setInputs(aInput1, aInput2, coal.getDust(aCoalAmount))
                            .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount));
                    }
                    int aDuration = builder.duration;
                    if (Railcraft.isModLoaded()) {
                        coll.derive()
                            .setInputs(aInput1, aInput2, RailcraftToolItems.getCoalCoke(aCoalAmount / 2))
                            .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount / 2))
                            .setDuration(aDuration * 2 / 3);
                    }
                    if (GTPlusPlus.isModLoaded()) {
                        ItemStack cactusCoke = GT_ModHandler
                            .getModItem(GTPlusPlus.ID, "itemCactusCoke", aCoalAmount * 2L);
                        ItemStack sugarCoke = GT_ModHandler
                            .getModItem(GTPlusPlus.ID, "itemSugarCoke", aCoalAmount * 2L);
                        coll.derive()
                            .setInputs(aInput1, aInput2, cactusCoke)
                            .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2))
                            .setDuration(aDuration * 2 / 3);
                        coll.derive()
                            .setInputs(aInput1, aInput2, sugarCoke)
                            .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2))
                            .setDuration(aDuration * 2 / 3);
                    }
                    if ((aInput1 == null || aInput1.stackSize <= 6) && (aInput2 == null || aInput2.stackSize <= 6)
                        && (aOutput1 == null || aOutput1.stackSize <= 6)
                        && (aOutput2 == null || aOutput2.stackSize <= 6)) {
                        // we don't use GT_Utility.mul() here. It does not have the truncating we need here.
                        aInput1 = GT_Utility.multiplyStack(10L, aInput1);
                        aInput2 = GT_Utility.multiplyStack(10L, aInput2);
                        aOutput1 = GT_Utility.multiplyStack(10L, aOutput1);
                        aOutput2 = GT_Utility.multiplyStack(10L, aOutput2);
                        for (Materials coal : new Materials[] { Materials.Coal, Materials.Charcoal }) {
                            coll.derive()
                                .setInputs(aInput1, aInput2, coal.getBlocks(aCoalAmount))
                                .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount))
                                .setDuration(aDuration * 10);
                            coll.derive()
                                .setInputs(aInput1, aInput2, coal.getBlocks(aCoalAmount))
                                .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount))
                                .setDuration(aDuration * 10);
                        }
                        if (Railcraft.isModLoaded()) {
                            coll.derive()
                                .setInputs(aInput1, aInput2, EnumCube.COKE_BLOCK.getItem(aCoalAmount / 2))
                                .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2))
                                .setDuration(aDuration * 20 / 3);
                        }
                    }
                    return coll.getAll();
                })
                .setRecipeConfigFile("primitiveblastfurnace", FIRST_ITEM_INPUT);
        /**
         * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for TNT/ITNT/... amount. Value is truncated to [0, 64]
         */
        public static final GT_Recipe_Map sImplosionRecipes = new GT_Recipe_Map(
            new HashSet<>(900),
            "gt.recipe.implosioncompressor",
            "Implosion Compressor",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            2,
            2,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_IMPLOSION)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_EXPLOSIVE)
                .setRecipeConfigFile("implosion", FIRST_ITEM_INPUT)
                .setRecipeEmitter(b -> {
                    switch (b.getItemInputsBasic().length) {
                        case 0:
                            return Collections.emptyList();
                        case 1:
                            break;
                        default:
                            return b.build()
                                .map(Collections::singletonList)
                                .orElse(Collections.emptyList());
                    }
                    Optional<GT_Recipe> t = b.noOptimize()
                        .duration(20)
                        .eut(30)
                        .validateInputCount(1, 1)
                        .validateOutputCount(1, 2)
                        .build();
                    if (!t.isPresent()) return Collections.emptyList();
                    ItemStack input = b.getItemInputBasic(0);
                    GT_RecipeTemplate coll = asTemplate(t.get());
                    int tExplosives = Math.min(b.getMetadata(ADDITIVE_AMOUNT), 64);
                    int tGunpowder = tExplosives << 1; // Worst
                    int tDynamite = Math.max(1, tExplosives >> 1); // good
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    int tTNT = tExplosives; // Slightly better
                    int tITNT = Math.max(1, tExplosives >> 2); // the best
                    if (tGunpowder < 65) coll.derive()
                        .setInputs(input, ItemList.Block_Powderbarrel.get(tGunpowder));
                    if (tDynamite < 17) coll.derive()
                        .setInputs(input, GT_ModHandler.getIC2Item("dynamite", tDynamite, null));
                    coll.derive()
                        .setInputs(input, new ItemStack(Blocks.tnt, tTNT));
                    coll.derive()
                        .setInputs(input, GT_ModHandler.getIC2Item("industrialTnt", tITNT, null));
                    return coll.getAll();
                })
                .setDisableOptimize(true)
                .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sVacuumRecipes = new GT_Recipe_Map(
            new HashSet<>(305),
            "gt.recipe.vacuumfreezer",
            "Vacuum Freezer",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            E,
            1,
            E,
            false,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setRecipeConfigFile("vacuumfreezer", FIRST_ITEM_INPUT)
                .setRecipeEmitter(b -> {
                    b.noOptimize();
                    FluidStack in, out;
                    if (isArrayOfLength(b.getItemInputsBasic(), 1) && isArrayOfLength(b.getItemOutputs(), 1)
                        && isArrayEmptyOrNull(b.getFluidInputs())
                        && isArrayEmptyOrNull(b.getFluidOutputs())
                        && (in = GT_Utility.getFluidForFilledItem(b.getItemInputBasic(0), true)) != null
                        && (out = GT_Utility.getFluidForFilledItem(b.getItemOutput(0), true)) != null) {
                        return Arrays.asList(
                            b.build()
                                .get(),
                            b.fluidInputs(in)
                                .fluidOutputs(out)
                                .noItemInputs()
                                .noItemOutputs()
                                .build()
                                .get());
                    }
                    return buildOrEmpty(b);
                })
                .setUsualFluidInputCount(2);
        /**
         * using {@code .addTo(sChemicalRecipes)} will cause the recipe to be added to single block recipe map ONLY!
         * use {@link GT_RecipeConstants#UniversalChemical} to add to both.
         */
        public static final GT_Recipe_Map sChemicalRecipes = new GT_Recipe_Map(
            new HashSet<>(1170),
            "gt.recipe.chemicalreactor",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ChemicalReactor"),
            2,
            2,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_MOLECULAR_1)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_2)
                .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_3)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_VIAL_1)
                .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_VIAL_2)
                .setRecipeConfigFile("chemicalreactor", FIRST_ITEM_OR_FLUID_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
                .setDisableOptimize(true);
        /**
         * using {@code .addTo(sMultiblockChemicalRecipes)} will cause the recipe to be added to multiblock recipe map
         * ONLY!
         * use {@link GT_RecipeConstants#UniversalChemical} to add to both.
         */
        public static final GT_Recipe_Map sMultiblockChemicalRecipes = //
            new GT_Recipe_Map_LargeChemicalReactor()
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
                .setUsualFluidInputCount(6)
                .setUsualFluidOutputCount(6)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map sDistillationRecipes = //
            new GT_Recipe_Map_DistillationTower().setRecipeConfigFile("distillation", FIRST_FLUIDSTACK_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
                .setUsualFluidOutputCount(11)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map_OilCracker sCrackingRecipes = (GT_Recipe_Map_OilCracker) //
        new GT_Recipe_Map_OilCracker().setRecipeConfigFile("cracking", FIRST_FLUIDSTACK_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
            .setUsualFluidInputCount(2);
        /**
         * @deprecated Use sCrackingRecipes instead
         */
        @Deprecated
        public static final GT_Recipe_Map sCrakingRecipes = sCrackingRecipes;

        public static final GT_Recipe_Map sPyrolyseRecipes = new GT_Recipe_Map(
            new HashSet<>(150),
            "gt.recipe.pyro",
            "Pyrolyse Oven",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            2,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setDisableOptimize(true)
                .setRecipeConfigFile("pyrolyse", FIRST_ITEM_INPUT);
        public static final GT_Recipe_Map sWiremillRecipes = new GT_Recipe_Map(
            new HashSet<>(450),
            "gt.recipe.wiremill",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Wiremill"),
            2,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_WIREMILL)
                .setRecipeConfigFile("wiremill", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_WIREMILL, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sBenderRecipes = new GT_Recipe_Map(
            new HashSet<>(5000),
            "gt.recipe.metalbender",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Bender"),
            2,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BENDER)
                .setRecipeConfigFile("bender", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_BENDING, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sAlloySmelterRecipes = new GT_Recipe_Map(
            new HashSet<>(12000),
            "gt.recipe.alloysmelter",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "AlloySmelter"),
            2,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
                .setRecipeEmitter(b -> {
                    if (Materials.Graphite.contains(b.getItemInputBasic(0))) return Collections.emptyList();
                    if (GT_Utility.isArrayOfLength(b.getItemInputsBasic(), 1)) {
                        ItemStack aInput1 = b.getItemInputBasic(0);
                        if (((OrePrefixes.ingot.contains(aInput1)) || (OrePrefixes.dust.contains(aInput1))
                            || (OrePrefixes.gem.contains(aInput1)))) return Collections.emptyList();
                    }
                    return buildOrEmpty(
                        b.validateNoInputFluid()
                            .validateNoOutputFluid()
                            .validateInputCount(1, 2)
                            .validateOutputCount(1, 1));
                })
                .setRecipeConfigFile(
                    "alloysmelting",
                    r -> GT_Config
                        .getStackConfigName(GT_Utility.isArrayOfLength(r.mInputs, 1) ? r.mInputs[0] : r.mOutputs[0]))
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM);
        public static final GT_Recipe_Map sAssemblerRecipes = new GT_Recipe_Map_Assembler(
            new HashSet<>(8200),
            "gt.recipe.assembler",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Assembler2"),
            9,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CIRCUIT)
                .setRecipeConfigFile("assembling", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map sCircuitAssemblerRecipes = new GT_Recipe_Map_Assembler(
            new HashSet<>(605),
            "gt.recipe.circuitassembler",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "CircuitAssembler"),
            6,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setNEIUnificateOutput(!NEICustomDiagrams.isModLoaded())
                .setRecipeConfigFile("circuitassembler", FIRST_ITEM_OUTPUT)
                .setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CIRCUIT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sCannerRecipes = new GT_Recipe_Map(
            new HashSet<>(900),
            "gt.recipe.canner",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Canner"),
            2,
            2,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CANNER)
                .setRecipeConfigFile("canning", FIRST_ITEM_INPUT)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
                .setProgressBar(GT_UITextures.PROGRESSBAR_CANNER, ProgressBar.Direction.RIGHT);
        @Deprecated
        public static final GT_Recipe_Map sCNCRecipes = new GT_Recipe_Map(
            new HashSet<>(100),
            "gt.recipe.cncmachine",
            "CNC Machine",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            2,
            1,
            2,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sLatheRecipes = new GT_Recipe_Map(
            new HashSet<>(1150),
            "gt.recipe.lathe",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Lathe"),
            1,
            2,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_ROD_1)
                .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_ROD_2)
                .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeConfigFile("lathe", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_LATHE, ProgressBar.Direction.RIGHT)
                .addSpecialTexture(5, 18, 98, 24, GT_UITextures.PROGRESSBAR_LATHE_BASE);
        public static final GT_Recipe_Map sCutterRecipes = new GT_Recipe_Map(
            new HashSet<>(5125),
            "gt.recipe.cuttingsaw",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Cutter4"),
            2,
            4,
            1,
            1,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BOX)
                .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_CUTTER_SLICED)
                .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
                .setRecipeEmitter(b -> {
                    b.validateInputCount(1, 2)
                        .validateOutputCount(1, 4)
                        .validateNoOutputFluid();
                    if (b.getFluidInputs() != null || !b.isValid())
                        return buildOrEmpty(b.validateInputFluidCount(1, 1));
                    int aDuration = b.getDuration(), aEUt = b.getEUt();
                    Collection<GT_Recipe> ret = new ArrayList<>();
                    b.copy()
                        .fluidInputs(Materials.Water.getFluid(GT_Utility.clamp(aDuration * aEUt / 320, 4, 1000)))
                        .duration(aDuration * 2)
                        .build()
                        .ifPresent(ret::add);
                    b.copy()
                        .fluidInputs(GT_ModHandler.getDistilledWater(GT_Utility.clamp(aDuration * aEUt / 426, 3, 750)))
                        .duration(aDuration * 2)
                        .build()
                        .ifPresent(ret::add);
                    b.fluidInputs(Materials.Lubricant.getFluid(GT_Utility.clamp(aDuration * aEUt / 1280, 1, 250)))
                        .duration(aDuration)
                        .build()
                        .ifPresent(ret::add);
                    return ret;
                })
                .setRecipeConfigFile("cutting", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_CUT, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sSlicerRecipes = new GT_Recipe_Map(
            new HashSet<>(20),
            "gt.recipe.slicer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Slicer"),
            2,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_SQUARE)
                .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_SLICE_SHAPE)
                .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_SLICER_SLICED)
                .setRecipeConfigFile("slicer", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_SLICE, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sExtruderRecipes = new GT_Recipe_Map(
            new HashSet<>(13000),
            "gt.recipe.extruder",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Extruder"),
            2,
            1,
            2,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_EXTRUDER_SHAPE)
                .setRecipeConfigFile("extruder", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRUDE, ProgressBar.Direction.RIGHT);

        public static final GT_Recipe_Map sHammerRecipes = new GT_Recipe_Map(
            new HashSet<>(3800),
            "gt.recipe.hammer",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Hammer"),
            2,
            2,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setUsualFluidInputCount(2)
                .setUsualFluidOutputCount(2)
                .setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_HAMMER)
                .setRecipeConfigFile("forgehammer", FIRST_ITEM_OUTPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
                .addSpecialTexture(20, 6, 78, 42, GT_UITextures.PROGRESSBAR_HAMMER_BASE)
                .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_HAMMER_STEAM)
                .setProgressBarSteam(GT_UITextures.PROGRESSBAR_HAMMER_STEAM)
                .addSpecialTextureSteam(20, 6, 78, 42, GT_UITextures.PROGRESSBAR_HAMMER_BASE_STEAM);
        public static final GT_Recipe_Map sAmplifiers = new GT_Recipe_Map(
            new HashSet<>(2),
            "gt.recipe.uuamplifier",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Amplifabricator"),
            1,
            0,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
                .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_UUA)
                .setRecipeConfigFile("amplifier", FIRST_ITEM_INPUT)
                .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sMassFabFakeRecipes = new GT_Recipe_Map(
            new HashSet<>(2),
            "gt.recipe.massfab",
            null,
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Massfabricator"),
            1,
            0,
            1,
            0,
            8,
            E,
            1,
            E,
            true,
            true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_UUA)
                .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_UUM)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sDieselFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(20),
            "gt.recipe.dieselgeneratorfuel",
            "Combustion Generator Fuels",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sExtremeDieselFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(20),
            "gt.recipe.extremedieselgeneratorfuel",
            "Extreme Diesel Engine Fuel",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sTurbineFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(25),
            "gt.recipe.gasturbinefuel",
            "Gas Turbine Fuel",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sHotFuels = new GT_Recipe_Map_Fuel(
            new HashSet<>(10),
            "gt.recipe.thermalgeneratorfuel",
            "Thermal Generator Fuels",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            false);
        public static final GT_Recipe_Map_Fuel sDenseLiquidFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(15),
            "gt.recipe.semifluidboilerfuels",
            "Semifluid Boiler Fuels",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sPlasmaFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(100),
            "gt.recipe.plasmageneratorfuels",
            "Plasma Generator Fuels",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sMagicFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(100),
            "gt.recipe.magicfuels",
            "Magic Energy Absorber Fuels",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sSmallNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.smallnaquadahreactor",
            "Naquadah Reactor MkI",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sLargeNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.largenaquadahreactor",
            "Naquadah Reactor MkII",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sHugeNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.fluidnaquadahreactor",
            "Naquadah Reactor MkIII",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sExtremeNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.hugenaquadahreactor",
            "Naquadah Reactor MkIV",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sUltraHugeNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.extrahugenaquadahreactor",
            "Naquadah Reactor MkV",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map_Fuel sFluidNaquadahReactorFuels = (GT_Recipe_Map_Fuel) new GT_Recipe_Map_Fuel(
            new HashSet<>(1),
            "gt.recipe.fluidfuelnaquadahreactor",
            "Fluid Naquadah Reactor",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            0,
            0,
            1,
            "Fuel Value: ",
            1000,
            " EU",
            true,
            true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        public static final GT_Recipe_Map sMultiblockElectrolyzerRecipes = new GT_Recipe_Map(
            new HashSet<>(300),
            "gt.recipe.largeelectrolyzer",
            "Large(PA) Electrolyzer",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            1,
            9,
            0,
            0,
            1,
            "",
            0,
            "",
            true,
            false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock);

        public static final GT_Recipe_Map sMultiblockCentrifugeRecipes = new GT_Recipe_Map(
            new HashSet<>(1200),
            "gt.recipe.largecentrifuge",
            "Large(PA) Centrifuge",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            1,
            9,
            0,
            0,
            1,
            "",
            0,
            "",
            true,
            false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map sMultiblockMixerRecipes = new GT_Recipe_Map(
            new HashSet<>(900),
            "gt.recipe.largemixer",
            "Large(PA) Mixer",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            9,
            3,
            0,
            0,
            1,
            "",
            0,
            "",
            true,
            false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblockNoCircuit)
                .setDisableOptimize(true);
        public static final GT_Recipe_Map_LargeBoilerFakeFuels sLargeBoilerFakeFuels = (GT_Recipe_Map_LargeBoilerFakeFuels) new GT_Recipe_Map_LargeBoilerFakeFuels()
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setDisableOptimize(true);

        public static final GT_Recipe_Map sNanoForge = new GT_Recipe_Map(
            new HashSet<>(10),
            "gt.recipe.nanoforge",
            "Nano Forge",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            6,
            2,
            2,
            1,
            1,
            "Tier: ",
            1,
            "",
            false,
            true).useModularUI(true)
                .setUsualFluidInputCount(3)
                .setDisableOptimize(true)
                .setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_LENS)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT);

        public static final GT_Recipe_Map sPCBFactory = new GT_Recipe_Map(
            new HashSet<>(10),
            "gt.recipe.pcbfactory",
            "PCB Factory",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            6,
            9,
            3,
            1,
            1,
            E,
            0,
            E,
            true,
            true).useModularUI(true)
                .setUsualFluidInputCount(3)
                .setUsualFluidOutputCount(0)
                .setDisableOptimize(true)
                .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
                .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                    List<String> result = new ArrayList<>();
                    int bitmap = recipeInfo.recipe.mSpecialValue;
                    if ((bitmap & 0b1) > 0) {
                        result.add(GT_Utility.trans("336", "PCB Factory Tier: ") + 1);
                    } else if ((bitmap & 0b10) > 0) {
                        result.add(GT_Utility.trans("336", "PCB Factory Tier: ") + 2);
                    } else if ((bitmap & 0b100) > 0) {
                        result.add(GT_Utility.trans("336", "PCB Factory Tier: ") + 3);
                    }
                    if ((bitmap & 0b1000) > 0) {
                        result.add(GT_Utility.trans("337", "Upgrade Required: ") + GT_Utility.trans("338", "Bio"));
                    }
                    return result;
                });

        public static final GT_Recipe_Map_IC2NuclearFake sIC2NuclearFakeRecipe = (GT_Recipe_Map_IC2NuclearFake) new GT_Recipe_Map_IC2NuclearFake()
            .setDisableOptimize(true);

        static {
            sCentrifugeRecipes.addDownstream(sMultiblockCentrifugeRecipes.deepCopyInput());
            sMixerRecipes.addDownstream(sMultiblockMixerRecipes.deepCopyInput());
            sElectrolyzerRecipes.addDownstream(sMultiblockElectrolyzerRecipes.deepCopyInput());
            sDieselFuels.addDownstream(
                IGT_RecipeMap.newRecipeMap(
                    b -> b.build()
                        .map(sLargeBoilerFakeFuels::addDieselRecipe)
                        .map(Collections::singletonList)
                        .orElse(Collections.emptyList())));
            sDenseLiquidFuels.addDownstream(
                IGT_RecipeMap.newRecipeMap(
                    b -> b.build()
                        .map(sLargeBoilerFakeFuels::addDenseLiquidRecipe)
                        .map(Collections::singletonList)
                        .orElse(Collections.emptyList())));
        }

        @Nullable
        public static GT_Recipe_Map findRecipeMap(@Nonnull String unlocalizedName) {
            return sMappings.stream()
                .filter(m -> unlocalizedName.equals(m.mUnlocalizedName))
                .findFirst()
                .orElse(null);
        }

        /**
         * HashMap of Recipes based on their Items
         */
        public final Map<ItemHolder, Collection<GT_Recipe>> mRecipeItemMap = new /* Concurrent */ HashMap<>();
        /**
         * HashMap of Recipes based on their Fluids
         */
        public final Map<String, Collection<GT_Recipe>> mRecipeFluidMap = new HashMap<>();

        public final HashSet<String> mRecipeFluidNameMap = new HashSet<>();
        /**
         * The List of all Recipes
         */
        public final Collection<GT_Recipe> mRecipeList;
        /**
         * String used as an unlocalised Name.
         */
        public final String mUnlocalizedName;
        /**
         * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
         */
        public final String mNEIName;
        /**
         * GUI used for NEI Display. Usually the GUI of the Machine itself
         */
        public final String mNEIGUIPath;

        public final String mNEISpecialValuePre, mNEISpecialValuePost;
        public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems,
            mMinimalInputFluids, mAmperage;
        public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

        /**
         * Whether to show oredict equivalent outputs when NEI is queried to show recipe
         */
        public boolean mNEIUnificateOutput = true;

        /**
         * Unique identifier for this recipe map. Generated from aUnlocalizedName and a few other parameters. See
         * constructor for details.
         */
        public final String mUniqueIdentifier;

        /**
         * Whether this recipe map contains any fluid outputs.
         */
        private boolean mHasFluidOutputs = false;

        /**
         * Whether this recipe map contains special slot inputs.
         */
        private boolean mUsesSpecialSlot = false;

        /**
         * Whether this recipemap checks for equality of special slot when searching recipe.
         */
        private boolean isSpecialSlotSensitive = false;

        /**
         * How many fluid inputs does this recipemap has at most. Currently used only for NEI slot placements and does
         * not actually restrict number of fluids used in the recipe.
         */
        private int usualFluidInputCount;

        /**
         * How many fluid outputs does this recipemap has at most. Currently used only for NEI slot placements and does
         * not actually restrict number of fluids used in the recipe.
         */
        private int usualFluidOutputCount;

        /**
         * Whether to use ModularUI for slot placements.
         */
        public boolean useModularUI = false;

        /**
         * Overlays used for GUI. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first slot in the same
         * section, e.g. first slot in the item output slots 8 = If it's special item slot.
         */
        private final TByteObjectMap<IDrawable> slotOverlays = new TByteObjectHashMap<>();

        /**
         * Overlays used for GUI on steam machine. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first
         * slot in the same section, e.g. first slot in the item output slots 8 = If it's special item slot.
         */
        private final TByteObjectMap<SteamTexture> slotOverlaysSteam = new TByteObjectHashMap<>();

        /**
         * Progressbar used for BasicMachine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
         * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
         * the bottom.
         */
        private FallbackableUITexture progressBarTexture;

        /**
         * Progressbar used for steam machine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
         * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
         * the bottom.
         */
        private FallbackableSteamTexture progressBarTextureSteam;

        public ProgressBar.Direction progressBarDirection = ProgressBar.Direction.RIGHT;

        public Size progressBarSize = new Size(20, 18);

        public Pos2d progressBarPos = new Pos2d(78, 24);

        public Rectangle neiTransferRect = new Rectangle(
            progressBarPos.x - (16 / 2),
            progressBarPos.y,
            progressBarSize.width + 16,
            progressBarSize.height);

        /**
         * Image size in direction of progress. Used for non-smooth rendering.
         */
        private int progressBarImageSize;

        /**
         * Additional textures shown on GUI.
         */
        public final List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures = new ArrayList<>();

        /**
         * Additional textures shown on steam machine GUI.
         */
        public final List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam = new ArrayList<>();

        public IDrawable logo = GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;

        public Pos2d logoPos = new Pos2d(152, 63);

        public Size logoSize = new Size(17, 17);

        public Pos2d neiBackgroundOffset = new Pos2d(2, 3);

        public Size neiBackgroundSize = new Size(172, 82);

        protected final GT_GUIColorOverride colorOverride;
        private int neiTextColorOverride = -1;

        private INEISpecialInfoFormatter neiSpecialInfoFormatter;

        private final boolean checkForCollision = true;
        private boolean allowNoInput;
        private boolean allowNoInputFluid;
        private boolean allowNoOutput;
        private boolean allowNoOutputFluid;
        private boolean disableOptimize = false;
        private Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> recipeEmitter = this::defaultBuildRecipe;
        private Function<? super GT_Recipe, ? extends GT_Recipe> specialHandler;
        private String recipeConfigCategory;
        private Function<? super GT_Recipe, String> recipeConfigKeyConvertor;
        private final List<IGT_RecipeMap> downstreams = new ArrayList<>(0);

        /**
         * Flag if a comparator should be used to search the recipe in NEI (which is defined in {@link Power}). Else
         * only the voltage will be used to find recipes
         */
        public boolean useComparatorForNEI;

        /**
         * Whether to render the actual size of stacks or a size of 1.
         */
        public boolean renderRealStackSizes = true;

        /**
         * Initialises a new type of Recipe Handler.
         *
         * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a
         *                                   pre-initialised Size.
         * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
         * @param aLocalName                 @deprecated the displayed Name inside the NEI Recipe GUI for optionally
         *                                   registering aUnlocalizedName
         *                                   with the language manager
         * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png"
         *                                   if forgotten.
         * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
         * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
         * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
         * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
         * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
         * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
         */
        public GT_Recipe_Map(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            sMappings.add(this);
            mNEIAllowed = aNEIAllowed;
            mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
            mRecipeList = aRecipeList;
            mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
            mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
            mNEISpecialValuePre = aNEISpecialValuePre;
            mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
            mNEISpecialValuePost = aNEISpecialValuePost;
            mAmperage = aAmperage;
            mUsualInputCount = aUsualInputCount;
            mUsualOutputCount = aUsualOutputCount;
            mMinimalInputItems = aMinimalInputItems;
            mMinimalInputFluids = aMinimalInputFluids;
            GregTech_API.sItemStackMappings.add(mRecipeItemMap);
            mUnlocalizedName = aUnlocalizedName;
            if (aLocalName != null) {
                GT_LanguageManager.addStringLocalization(mUnlocalizedName, aLocalName);
            }
            mUniqueIdentifier = String.format(
                "%s_%d_%d_%d_%d_%d",
                aUnlocalizedName,
                aAmperage,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputFluids,
                aMinimalInputItems);
            progressBarTexture = new FallbackableUITexture(
                UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
                GT_UITextures.PROGRESSBAR_ARROW);
            colorOverride = GT_GUIColorOverride.get(ModularUITextures.VANILLA_BACKGROUND.location);
            if (sIndexedMappings.put(mUniqueIdentifier, this) != null)
                throw new IllegalArgumentException("Duplicate recipe map registered: " + mUniqueIdentifier);
        }

        @Deprecated
        public GT_Recipe_Map(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed,
            boolean aNEIUnificateOutput) {
            this(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
            setNEIUnificateOutput(aNEIUnificateOutput);
        }

        public GT_Recipe_Map setDisableOptimize(boolean disableOptimize) {
            this.disableOptimize = disableOptimize;
            return this;
        }

        public GT_Recipe_Map setSpecialSlotSensitive(boolean isSpecialSlotSensitive) {
            this.isSpecialSlotSensitive = isSpecialSlotSensitive;
            return this;
        }

        public GT_Recipe_Map setNEIUnificateOutput(boolean mNEIUnificateOutput) {
            this.mNEIUnificateOutput = mNEIUnificateOutput;
            return this;
        }

        public GT_Recipe_Map useComparatorForNEI(boolean use) {
            this.useComparatorForNEI = use;
            return this;
        }

        public GT_Recipe_Map setRenderRealStackSizes(boolean renderRealStackSizes) {
            this.renderRealStackSizes = renderRealStackSizes;
            return this;
        }

        public GT_Recipe_Map useModularUI(boolean use) {
            this.useModularUI = use;
            return this;
        }

        public GT_Recipe_Map setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
            IDrawable slotOverlay) {
            useModularUI(true);
            this.slotOverlays.put(
                (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
                slotOverlay);
            return this;
        }

        public GT_Recipe_Map setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, IDrawable slotOverlay) {
            return setSlotOverlay(isFluid, isOutput, isFirst, false, slotOverlay);
        }

        public GT_Recipe_Map setSlotOverlay(boolean isFluid, boolean isOutput, IDrawable slotOverlay) {
            return setSlotOverlay(isFluid, isOutput, true, slotOverlay)
                .setSlotOverlay(isFluid, isOutput, false, slotOverlay);
        }

        public GT_Recipe_Map setSlotOverlaySteam(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
            SteamTexture slotOverlay) {
            useModularUI(true);
            this.slotOverlaysSteam.put(
                (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
                slotOverlay);
            return this;
        }

        public GT_Recipe_Map setSlotOverlaySteam(boolean isOutput, boolean isFirst, SteamTexture slotOverlay) {
            return setSlotOverlaySteam(false, isOutput, isFirst, false, slotOverlay);
        }

        public GT_Recipe_Map setSlotOverlaySteam(boolean isOutput, SteamTexture slotOverlay) {
            return setSlotOverlaySteam(false, isOutput, true, false, slotOverlay)
                .setSlotOverlaySteam(false, isOutput, false, false, slotOverlay);
        }

        public GT_Recipe_Map setProgressBar(UITexture progressBarTexture, ProgressBar.Direction progressBarDirection) {
            return setProgressBarWithFallback(
                new FallbackableUITexture(
                    UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
                    progressBarTexture),
                progressBarDirection);
        }

        public GT_Recipe_Map setProgressBar(UITexture progressBarTexture) {
            return setProgressBar(progressBarTexture, ProgressBar.Direction.RIGHT);
        }

        /**
         * Some resource packs want to use custom progress bar textures even for plain arrow. This method allows them to
         * add unique textures, yet other packs don't need to make textures for every recipemap.
         */
        public GT_Recipe_Map setProgressBarWithFallback(FallbackableUITexture progressBarTexture,
            ProgressBar.Direction progressBarDirection) {
            useModularUI(true);
            this.progressBarTexture = progressBarTexture;
            this.progressBarDirection = progressBarDirection;
            return this;
        }

        public GT_Recipe_Map setProgressBarSteam(SteamTexture progressBarTexture) {
            return setProgressBarSteamWithFallback(
                new FallbackableSteamTexture(
                    SteamTexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName + "_%s"),
                    progressBarTexture));
        }

        public GT_Recipe_Map setProgressBarSteamWithFallback(FallbackableSteamTexture progressBarTexture) {
            this.progressBarTextureSteam = progressBarTexture;
            return this;
        }

        public GT_Recipe_Map setProgressBarSize(int x, int y) {
            useModularUI(true);
            this.progressBarSize = new Size(x, y);
            return this;
        }

        public GT_Recipe_Map setProgressBarPos(int x, int y) {
            useModularUI(true);
            this.progressBarPos = new Pos2d(x, y);
            return this;
        }

        public GT_Recipe_Map setProgressBarImageSize(int progressBarImageSize) {
            useModularUI(true);
            this.progressBarImageSize = progressBarImageSize;
            return this;
        }

        public GT_Recipe_Map setNEITransferRect(Rectangle neiTransferRect) {
            useModularUI(true);
            this.neiTransferRect = neiTransferRect;
            return this;
        }

        public GT_Recipe_Map addSpecialTexture(int width, int height, int x, int y, IDrawable texture) {
            useModularUI(true);
            specialTextures
                .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
            return this;
        }

        public GT_Recipe_Map addSpecialTextureSteam(int width, int height, int x, int y, SteamTexture texture) {
            useModularUI(true);
            specialTexturesSteam
                .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
            return this;
        }

        public GT_Recipe_Map setUsualFluidInputCount(int usualFluidInputCount) {
            useModularUI(true);
            this.usualFluidInputCount = usualFluidInputCount;
            return this;
        }

        public GT_Recipe_Map setUsualFluidOutputCount(int usualFluidOutputCount) {
            useModularUI(true);
            this.usualFluidOutputCount = usualFluidOutputCount;
            return this;
        }

        public GT_Recipe_Map setLogo(IDrawable logo) {
            useModularUI(true);
            this.logo = logo;
            return this;
        }

        public GT_Recipe_Map setLogoPos(int x, int y) {
            useModularUI(true);
            this.logoPos = new Pos2d(x, y);
            return this;
        }

        public GT_Recipe_Map setLogoSize(int width, int height) {
            useModularUI(true);
            this.logoSize = new Size(width, height);
            return this;
        }

        public GT_Recipe_Map setNEIBackgroundOffset(int x, int y) {
            useModularUI(true);
            this.neiBackgroundOffset = new Pos2d(x, y);
            return this;
        }

        public GT_Recipe_Map setNEIBackgroundSize(int width, int height) {
            useModularUI(true);
            this.neiBackgroundSize = new Size(width, height);
            return this;
        }

        public GT_Recipe_Map setNEISpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
            this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
            return this;
        }

        /**
         * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
         */
        public GT_Recipe_Map setRecipeEmitter(
            Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
            this.recipeEmitter = func;
            return this;
        }

        /**
         * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
         * <p>
         * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
         */
        public GT_Recipe_Map combineRecipeEmitter(
            Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
            // move recipeEmitter to local variable, so lambda capture the function itself instead of this
            Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> cur = recipeEmitter;
            this.recipeEmitter = b -> Iterables.concat(cur.apply(b), func.apply(b));
            return this;
        }

        /**
         * Change how recipes are emitted by a particular recipe builder. Should not return null.
         */
        public GT_Recipe_Map setRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
            return setRecipeEmitter(func.andThen(Collections::singletonList));
        }

        /**
         * Change how recipes are emitted by a particular recipe builder. Effectively add a new recipe per recipe added.
         * func must not return null.
         * <p>
         * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
         */
        public GT_Recipe_Map combineRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
            return combineRecipeEmitter(func.andThen(Collections::singletonList));
        }

        private static <T> Function<? super T, ? extends T> withIdentityReturn(Consumer<T> func) {
            return r -> {
                func.accept(r);
                return r;
            };
        }

        /**
         * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
         * override {@link #doAdd(GT_RecipeBuilder)}
         *
         * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
         */
        public GT_Recipe_Map setRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
            this.specialHandler = func;
            return this;
        }

        /**
         * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior, create a subclass
         * and override {@link #doAdd(GT_RecipeBuilder)}
         *
         * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
         */
        public GT_Recipe_Map setRecipeSpecialHandler(Consumer<GT_Recipe> func) {
            return setRecipeSpecialHandler(withIdentityReturn(func));
        }

        /**
         * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
         * override {@link #doAdd(GT_RecipeBuilder)}.
         * <p>
         * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
         * <p>
         * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
         * The supplied function will be given the output of existing handler when a recipe is added.
         */
        public GT_Recipe_Map chainRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
            this.specialHandler = specialHandler == null ? func : specialHandler.andThen(func);
            return this;
        }

        /**
         * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
         * override {@link #doAdd(GT_RecipeBuilder)}.
         * <p>
         * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
         * <p>
         * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
         * The supplied function will be given the output of existing handler when a recipe is added.
         */
        public GT_Recipe_Map chainRecipeSpecialHandler(Consumer<GT_Recipe> func) {
            return chainRecipeSpecialHandler(withIdentityReturn(func));
        }

        public GT_Recipe_Map setRecipeConfigFile(String category, Function<? super GT_Recipe, String> keyConvertor) {
            if (StringUtils.isBlank(category) || keyConvertor == null) throw new IllegalArgumentException();
            this.recipeConfigCategory = category;
            this.recipeConfigKeyConvertor = keyConvertor;
            return this;
        }

        @Override
        public void addDownstream(IGT_RecipeMap downstream) {
            this.downstreams.add(downstream);
        }

        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
            int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            return addRecipe(
                new GT_Recipe(
                    aOptimize,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    aOutputChances,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue));
        }

        public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
            int aDuration, int aEUt, int aSpecialValue) {
            return addRecipe(
                new GT_Recipe(
                    false,
                    null,
                    null,
                    null,
                    aOutputChances,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue),
                false,
                false,
                false);
        }

        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
            FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
            return addRecipe(
                new GT_Recipe(
                    aOptimize,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    null,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue));
        }

        public GT_Recipe addRecipe(GT_Recipe aRecipe) {
            return addRecipe(aRecipe, true, false, false);
        }

        protected GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe,
            boolean aHidden) {
            aRecipe.mHidden = aHidden;
            aRecipe.mFakeRecipe = aFakeRecipe;
            if (aRecipe.mFluidInputs.length < mMinimalInputFluids && aRecipe.mInputs.length < mMinimalInputItems)
                return null;
            if (aCheckForCollisions
                && findRecipe(null, false, true, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)
                return null;
            return add(aRecipe);
        }

        /**
         * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
         * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
         */
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
            int aEUt, int aSpecialValue) {
            return addFakeRecipe(
                aCheckForCollisions,
                new GT_Recipe(
                    false,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    aOutputChances,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue));
        }

        /**
         * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
         * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
         */
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            return addFakeRecipe(
                aCheckForCollisions,
                new GT_Recipe(
                    false,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    null,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue));
        }

        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, boolean hidden) {
            return addFakeRecipe(
                aCheckForCollisions,
                new GT_Recipe(
                    false,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    null,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue),
                hidden);
        }

        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, ItemStack[][] aAlt, boolean hidden) {
            return addFakeRecipe(
                aCheckForCollisions,
                new GT_Recipe_WithAlt(
                    false,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    null,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue,
                    aAlt),
                hidden);
        }

        /**
         * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
         * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
         */
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
            return addRecipe(aRecipe, aCheckForCollisions, true, false);
        }

        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe, boolean hidden) {
            return addRecipe(aRecipe, aCheckForCollisions, true, hidden);
        }

        @Nonnull
        @Override
        public Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder) {
            Iterable<? extends GT_Recipe> recipes = recipeEmitter.apply(builder);
            Collection<GT_Recipe> ret = new ArrayList<>();
            for (GT_Recipe r : recipes) {
                if (recipeConfigCategory != null) {
                    String configKey = recipeConfigKeyConvertor.apply(r);
                    if (configKey != null
                        && (r.mDuration = GregTech_API.sRecipeFile.get(recipeConfigCategory, configKey, r.mDuration))
                            <= 0) {
                        continue;
                    }
                }
                if (r.mFluidInputs.length < mMinimalInputFluids && r.mInputs.length < mMinimalInputItems) return null;
                if (r.mSpecialValue == 0) {
                    // new style cleanroom/lowgrav handling
                    int specialValue = 0;
                    if (builder.getMetadata(GT_RecipeConstants.LOW_GRAVITY, false)) specialValue -= 100;
                    if (builder.getMetadata(GT_RecipeConstants.CLEANROOM, false)) specialValue -= 200;
                    for (GT_RecipeBuilder.MetadataIdentifier<Integer> ident : SPECIAL_VALUE_ALIASES) {
                        Integer metadata = builder.getMetadata(ident, null);
                        if (metadata != null) {
                            specialValue = metadata;
                            break;
                        }
                    }
                    r.mSpecialValue = specialValue;
                }
                if (specialHandler != null) r = specialHandler.apply(r);
                if (r == null) continue;
                if (checkForCollision
                    && findRecipe(null, false, true, Long.MAX_VALUE, r.mFluidInputs, r.mInputs) != null) {
                    StringBuilder errorInfo = new StringBuilder();
                    boolean hasAnEntry = false;
                    for (FluidStack fStack : r.mFluidInputs) {
                        if (fStack == null) {
                            continue;
                        }
                        String s = fStack.getLocalizedName();
                        if (s == null) {
                            continue;
                        }
                        if (hasAnEntry) {
                            errorInfo.append("+")
                                .append(s);
                        } else {
                            errorInfo.append(s);
                        }
                        hasAnEntry = true;
                    }
                    for (ItemStack iStack : r.mInputs) {
                        if (iStack == null) {
                            continue;
                        }
                        String s = iStack.getDisplayName();
                        if (hasAnEntry) {
                            errorInfo.append("+")
                                .append(s);
                        } else {
                            errorInfo.append(s);
                        }
                        hasAnEntry = true;
                    }
                    handleRecipeCollision(errorInfo.toString());
                    continue;
                }
                ret.add(add(r));
            }
            if (!ret.isEmpty()) {
                builder.clearInvalid();
                for (IGT_RecipeMap downstream : downstreams) {
                    downstream.doAdd(builder);
                }
            }
            return ret;
        }

        public final Iterable<? extends GT_Recipe> defaultBuildRecipe(GT_RecipeBuilder builder) {
            // TODO sensible validation
            GT_RecipeBuilder b = builder;
            if (disableOptimize && builder.optimize) {
                b = copy(builder, b).noOptimize();
            }
            return buildOrEmpty(b);
        }

        private static GT_RecipeBuilder copy(GT_RecipeBuilder original, GT_RecipeBuilder b) {
            return b == original ? b.copy() : b;
        }

        public GT_Recipe add(GT_Recipe aRecipe) {
            mRecipeList.add(aRecipe);
            for (FluidStack aFluid : aRecipe.mFluidInputs) {
                if (aFluid != null) {
                    Collection<GT_Recipe> tList = mRecipeFluidMap.computeIfAbsent(
                        aFluid.getFluid()
                            .getName(),
                        k -> new HashSet<>(1));
                    tList.add(aRecipe);
                    mRecipeFluidNameMap.add(
                        aFluid.getFluid()
                            .getName());
                }
            }
            if (aRecipe.mFluidOutputs.length != 0) {
                this.mHasFluidOutputs = true;
            }
            if (aRecipe.mSpecialItems != null) {
                this.mUsesSpecialSlot = true;
            }
            return addToItemMap(aRecipe);
        }

        public void reInit() {
            mRecipeItemMap.clear();
            for (GT_Recipe tRecipe : mRecipeList) {
                GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
                GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
                addToItemMap(tRecipe);
            }
        }

        /**
         * @return if this Item is a valid Input for any for the Recipes
         */
        public boolean containsInput(ItemStack aStack) {
            return aStack != null && (mRecipeItemMap.containsKey(new GT_ItemStack(aStack))
                || mRecipeItemMap.containsKey(new GT_ItemStack(aStack, true)));
        }

        /**
         * @return if this Fluid is a valid Input for any for the Recipes
         */
        public boolean containsInput(FluidStack aFluid) {
            return aFluid != null && containsInput(aFluid.getFluid());
        }

        /**
         * @return if this Fluid is a valid Input for any for the Recipes
         */
        public boolean containsInput(Fluid aFluid) {
            return aFluid != null && mRecipeFluidNameMap.contains(aFluid.getName());
        }

        @Nullable
        public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated, long aVoltage,
            FluidStack[] aFluids, ItemStack... aInputs) {
            return findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
        }

        @Nullable
        public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
            return findRecipe(
                aTileEntity,
                null,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                null,
                aInputs);
        }

        @Nullable
        public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe,
            boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
            return findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
        }

        @Nullable
        public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe,
            boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
            ItemStack... aInputs) {
            return findRecipe(
                aTileEntity,
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                null,
                aInputs);
        }

        @Nullable
        public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe,
            boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
            return findRecipe(aTileEntity, aRecipe, aNotUnificated, false, aVoltage, aFluids, aSpecialSlot, aInputs);
        }

        // TODO: make this final after migrating BW
        @SuppressWarnings("unused")
        @Nullable
        public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            FindRecipeResult result = findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            return result.isSuccessful() ? result.getRecipe() : null;
        }

        /**
         * finds a Recipe matching the aFluid and ItemStack Inputs.
         *
         * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things
         *                             up.
         * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
         * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with
         *                             the provided input
         * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
         * @param aFluids              the Fluid Inputs
         * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with
         *                             this, but some custom ones do.
         * @param aInputs              the Item Inputs
         * @return Result of the recipe search
         */
        @Nonnull
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            // No Recipes? Well, nothing to be found then.
            if (mRecipeList.isEmpty()) return NOT_FOUND;

            // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1
            // Stack" or "at least 2 Stacks" before they start searching for Recipes.
            // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in
            // their Machines to select Sub Recipes.
            if (GregTech_API.sPostloadFinished) {
                if (mMinimalInputFluids > 0) {
                    if (aFluids == null) return NOT_FOUND;
                    int tAmount = 0;
                    for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
                    if (tAmount < mMinimalInputFluids) return NOT_FOUND;
                }
                if (mMinimalInputItems > 0) {
                    if (aInputs == null) return NOT_FOUND;
                    int tAmount = 0;
                    for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                    if (tAmount < mMinimalInputItems) return NOT_FOUND;
                }
            }

            // Unification happens here in case the Input isn't already unificated.
            if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

            // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
            if (aRecipe != null) if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
                && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || GT_Utility.areStacksEqualOrNull((ItemStack) aRecipe.mSpecialItems, aSpecialSlot)) {
                        return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt
                            ? FindRecipeResult.ofSuccess(aRecipe)
                            : FindRecipeResult.ofInsufficientVoltage(aRecipe);
                    }
                }

            // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
            if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs) if (tStack != null) {
                Collection<GT_Recipe> tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
                if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes) if (!tRecipe.mFakeRecipe
                    && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        if (!isSpecialSlotSensitive
                            || GT_Utility.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt
                                ? FindRecipeResult.ofSuccess(tRecipe)
                                : FindRecipeResult.ofInsufficientVoltage(tRecipe);
                        }
                    }
                tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack, true));
                if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes) if (!tRecipe.mFakeRecipe
                    && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        if (!isSpecialSlotSensitive
                            || GT_Utility.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt
                                ? FindRecipeResult.ofSuccess(tRecipe)
                                : FindRecipeResult.ofInsufficientVoltage(tRecipe);
                        }
                    }
            }

            // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that
            // Map too.
            if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids) if (aFluid != null) {
                Collection<GT_Recipe> tRecipes = mRecipeFluidMap.get(
                    aFluid.getFluid()
                        .getName());
                if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes) if (!tRecipe.mFakeRecipe
                    && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        if (!isSpecialSlotSensitive
                            || GT_Utility.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt
                                ? FindRecipeResult.ofSuccess(tRecipe)
                                : FindRecipeResult.ofInsufficientVoltage(tRecipe);
                        }
                    }
            }

            // And nothing has been found.
            return NOT_FOUND;
        }

        @Nonnull
        public FindRecipeResult findRecipeWithResult(@Nullable GT_Recipe lastRecipe, long voltage,
            @Nullable ItemInventoryLogic itemInput, @Nullable FluidInventoryLogic fluidInput) {
            if (mRecipeList.isEmpty()) return NOT_FOUND;

            if (itemInput == null) {
                itemInput = new ItemInventoryLogic(0);
            }

            if (fluidInput == null) {
                fluidInput = new FluidInventoryLogic(0, 0);
            }

            if (mMinimalInputItems > itemInput.getStoredItems().length) {
                return NOT_FOUND;
            }

            if (mMinimalInputFluids > fluidInput.getStoredFluids().length) {
                return NOT_FOUND;
            }

            if (lastRecipe != null && lastRecipe.isRecipePossible(itemInput, fluidInput)) {
                return FindRecipeResult.ofSuccess(lastRecipe);
            }

            itemInput.startRecipeCheck();
            Map<ItemHolder, Long> items = itemInput.getMapOfStoredItems();
            Collection<GT_Recipe> recipesMatching = new ArrayList<>();
            for (ItemHolder item : items.keySet()) {
                Collection<GT_Recipe> recipesForItem = mRecipeItemMap.get(item);
                recipesMatching.addAll(
                    recipesForItem.stream()
                        .filter(recipe -> new RecipeInputRequirements(recipe).tryToFillItemRequirements(items))
                        .collect(Collectors.toList()));
            }

            Map<Fluid, Long> fluids = fluidInput.getMapOfStoredFluids();
            for (Fluid fluid : fluids.keySet()) {
                Collection<GT_Recipe> recipesForFluid = mRecipeFluidMap.get(fluid.getName());
                recipesMatching.addAll(
                    recipesForFluid.stream()
                        .filter(recipe -> new RecipeInputRequirements(recipe).tryToFillFluidRequirements(fluids))
                        .collect(Collectors.toList()));
            }
            recipesMatching = recipesMatching.stream()
                .distinct()
                .filter(recipe -> recipe.mEUt < voltage)
                .collect(Collectors.toList());
            if (recipesMatching.size() <= 0) return NOT_FOUND;

            return FindRecipeResult.ofSuccess(new ArrayList<>(recipesMatching));
        }

        protected GT_Recipe addToItemMap(GT_Recipe aRecipe) {
            for (ItemStack aStack : aRecipe.mInputs) if (aStack != null) {
                ItemHolder tStack = new ItemHolder(aStack);
                Collection<GT_Recipe> tList = mRecipeItemMap.computeIfAbsent(tStack, k -> new HashSet<>(1));
                tList.add(aRecipe);
            }
            return aRecipe;
        }

        /**
         * Whether this recipe map contains any fluid outputs.
         */
        public boolean hasFluidOutputs() {
            return mHasFluidOutputs;
        }

        /**
         * Whether this recipe map contains any fluid inputs.
         */
        public boolean hasFluidInputs() {
            return mRecipeFluidNameMap.size() != 0;
        }

        /**
         * Whether this recipe map contains special slot inputs.
         */
        public boolean usesSpecialSlot() {
            return mUsesSpecialSlot;
        }

        public int getUsualFluidInputCount() {
            return Math.max(usualFluidInputCount, hasFluidInputs() ? 1 : 0);
        }

        public int getUsualFluidOutputCount() {
            return Math.max(usualFluidOutputCount, hasFluidOutputs() ? 1 : 0);
        }

        @Nullable
        public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
            byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0)
                + (index == 0 ? 4 : 0)
                + (isSpecial ? 8 : 0));
            if (slotOverlays.containsKey(overlayKey)) {
                return slotOverlays.get(overlayKey);
            }
            return null;
        }

        @Nullable
        public SteamTexture getOverlayForSlotSteam(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
            byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0)
                + (index == 0 ? 4 : 0)
                + (isSpecial ? 8 : 0));
            if (slotOverlaysSteam.containsKey(overlayKey)) {
                return slotOverlaysSteam.get(overlayKey);
            }
            return null;
        }

        @Nullable
        public SteamTexture getOverlayForSlotSteam(boolean isOutput, boolean isFirst) {
            byte overlayKey = (byte) ((isOutput ? 2 : 0) + (isFirst ? 4 : 0));
            if (slotOverlaysSteam.containsKey(overlayKey)) {
                return slotOverlaysSteam.get(overlayKey);
            }
            return null;
        }

        public UITexture getProgressBarTexture() {
            return progressBarTexture.get();
        }

        public FallbackableUITexture getProgressBarTextureRaw() {
            return progressBarTexture;
        }

        public UITexture getProgressBarTextureSteam(SteamVariant steamVariant) {
            return progressBarTextureSteam.get(steamVariant);
        }

        public int getProgressBarImageSize() {
            if (progressBarImageSize != 0) {
                return progressBarImageSize;
            }
            return switch (progressBarDirection) {
                case UP, DOWN -> progressBarSize.height;
                case CIRCULAR_CW -> Math.max(progressBarSize.width, progressBarSize.height);
                default -> progressBarSize.width;
            };
        }

        /**
         * Adds slot backgrounds, progressBar, etc.
         */
        public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
            IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
            IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
            Supplier<Float> progressSupplier, Pos2d windowOffset) {
            ModularWindow.Builder builder = ModularWindow.builder(neiBackgroundSize)
                .setBackground(ModularUITextures.VANILLA_BACKGROUND);

            UIHelper.forEachSlots(
                (i, backgrounds, pos) -> builder.widget(
                    SlotWidget.phantom(itemInputsInventory, i)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18)),
                (i, backgrounds, pos) -> builder.widget(
                    SlotWidget.phantom(itemOutputsInventory, i)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18)),
                (i, backgrounds, pos) -> {
                    if (usesSpecialSlot()) builder.widget(
                        SlotWidget.phantom(specialSlotInventory, 0)
                            .setBackground(backgrounds)
                            .setPos(pos)
                            .setSize(18, 18));
                },
                (i, backgrounds, pos) -> builder.widget(
                    SlotWidget.phantom(fluidInputsInventory, i)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18)),
                (i, backgrounds, pos) -> builder.widget(
                    SlotWidget.phantom(fluidOutputsInventory, i)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18)),
                ModularUITextures.ITEM_SLOT,
                ModularUITextures.FLUID_SLOT,
                this,
                mUsualInputCount,
                mUsualOutputCount,
                getUsualFluidInputCount(),
                getUsualFluidOutputCount(),
                SteamVariant.NONE,
                windowOffset);

            addProgressBarUI(builder, progressSupplier, windowOffset);
            addGregTechLogoUI(builder, windowOffset);

            for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : specialTextures) {
                builder.widget(
                    new DrawableWidget().setDrawable(specialTexture.getLeft())
                        .setSize(
                            specialTexture.getRight()
                                .getLeft())
                        .setPos(
                            specialTexture.getRight()
                                .getRight()
                                .add(windowOffset)));
            }

            return builder;
        }

        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
            Pos2d windowOffset) {
            builder.widget(
                new ProgressBar().setTexture(getProgressBarTexture(), 20)
                    .setDirection(progressBarDirection)
                    .setProgress(progressSupplier)
                    .setSynced(false, false)
                    .setPos(progressBarPos.add(windowOffset))
                    .setSize(progressBarSize));
        }

        public void addGregTechLogoUI(ModularWindow.Builder builder, Pos2d windowOffset) {
            builder.widget(
                new DrawableWidget().setDrawable(logo)
                    .setSize(logoSize)
                    .setPos(logoPos.add(windowOffset)));
        }

        public void addRecipeSpecificDrawable(ModularWindow.Builder builder, Pos2d windowOffset,
            Supplier<IDrawable> supplier, Pos2d pos, Size size) {
            builder.widget(
                new DrawableWidget().setDrawable(supplier)
                    .setSize(size)
                    .setPos(pos.add(windowOffset)));
        }

        /**
         * Overriding this method allows custom NEI stack placement
         */
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getItemInputPositions(itemInputCount);
        }

        /**
         * Overriding this method allows custom NEI stack placement
         */
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper.getItemOutputPositions(itemOutputCount);
        }

        /**
         * Overriding this method allows custom NEI stack placement
         */
        public Pos2d getSpecialItemPosition() {
            return UIHelper.getSpecialItemPosition();
        }

        /**
         * Overriding this method allows custom NEI stack placement
         */
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getFluidInputPositions(fluidInputCount);
        }

        /**
         * Overriding this method allows custom NEI stack placement
         */
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getFluidOutputPositions(fluidOutputCount);
        }

        public void drawNEIDescription(NEIRecipeInfo recipeInfo) {
            drawNEIEnergyInfo(recipeInfo);
            drawNEIDurationInfo(recipeInfo);
            drawNEISpecialInfo(recipeInfo);
            drawNEIRecipeOwnerInfo(recipeInfo);
        }

        protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
            GT_Recipe recipe = recipeInfo.recipe;
            Power power = recipeInfo.power;
            if (power.getEuPerTick() > 0) {
                drawNEIText(recipeInfo, GT_Utility.trans("152", "Total: ") + power.getTotalPowerString());

                String amperage = power.getAmperageString();
                String powerUsage = power.getPowerUsageString();
                if (amperage == null || amperage.equals("unspecified") || powerUsage.contains("(OC)")) {
                    drawNEIText(recipeInfo, GT_Utility.trans("153", "Usage: ") + powerUsage);
                    if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
                        Power originalPower = getPowerFromRecipeMap();
                        if (!(originalPower instanceof UnspecifiedEUPower)) {
                            originalPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration);
                            drawNEIText(
                                recipeInfo,
                                GT_Utility.trans("275", "Original voltage: ") + originalPower.getVoltageString());
                        }
                    }
                    if (amperage != null && !amperage.equals("unspecified") && !amperage.equals("1")) {
                        drawNEIText(recipeInfo, GT_Utility.trans("155", "Amperage: ") + amperage);
                    }
                } else if (amperage.equals("1")) {
                    drawNEIText(recipeInfo, GT_Utility.trans("154", "Voltage: ") + power.getVoltageString());
                } else {
                    drawNEIText(recipeInfo, GT_Utility.trans("153", "Usage: ") + powerUsage);
                    drawNEIText(recipeInfo, GT_Utility.trans("154", "Voltage: ") + power.getVoltageString());
                    drawNEIText(recipeInfo, GT_Utility.trans("155", "Amperage: ") + amperage);
                }
            }
        }

        protected void drawNEIDurationInfo(NEIRecipeInfo recipeInfo) {
            Power power = recipeInfo.power;
            if (power.getDurationTicks() > 0) {
                String textToDraw = GT_Utility.trans("158", "Time: ");
                if (GT_Mod.gregtechproxy.mNEIRecipeSecondMode) {
                    textToDraw += power.getDurationStringSeconds();
                    if (power.getDurationSeconds() <= 1.0d) {
                        textToDraw += String.format(" (%s)", power.getDurationStringTicks());
                    }
                } else {
                    textToDraw += power.getDurationStringTicks();
                }
                drawNEIText(recipeInfo, textToDraw);
            }
        }

        protected void drawNEISpecialInfo(NEIRecipeInfo recipeInfo) {
            String[] recipeDesc = recipeInfo.recipe.getNeiDesc();
            if (recipeDesc != null) {
                for (String s : recipeDesc) {
                    drawOptionalNEIText(recipeInfo, s);
                }
            } else if (neiSpecialInfoFormatter != null) {
                drawNEITextMultipleLines(
                    recipeInfo,
                    neiSpecialInfoFormatter.format(recipeInfo, this::formatSpecialValue));
            } else {
                drawOptionalNEIText(recipeInfo, getNEISpecialInfo(recipeInfo.recipe.mSpecialValue));
            }
        }

        protected String getNEISpecialInfo(int specialValue) {
            if (specialValue == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
                return GT_Utility.trans("159", "Needs Low Gravity");
            } else if (specialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                return GT_Utility.trans("160", "Needs Cleanroom");
            } else if (specialValue == -201) {
                return GT_Utility.trans("206", "Scan for Assembly Line");
            } else if (specialValue == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                return GT_Utility.trans("160.1", "Needs Cleanroom & LowGrav");
            } else if (specialValue == -400) {
                return GT_Utility.trans("216", "Deprecated Recipe");
            } else if (hasSpecialValueFormat()) {
                return formatSpecialValue(specialValue);
            }
            return null;
        }

        private boolean hasSpecialValueFormat() {
            return (GT_Utility.isStringValid(mNEISpecialValuePre)) || (GT_Utility.isStringValid(mNEISpecialValuePost));
        }

        protected String formatSpecialValue(int specialValue) {
            return mNEISpecialValuePre + formatNumbers((long) specialValue * mNEISpecialValueMultiplier)
                + mNEISpecialValuePost;
        }

        protected void drawNEIRecipeOwnerInfo(NEIRecipeInfo recipeInfo) {
            GT_Recipe recipe = recipeInfo.recipe;
            if (GT_Mod.gregtechproxy.mNEIRecipeOwner) {
                if (recipe.owners.size() > 1) {
                    drawNEIText(
                        recipeInfo,
                        EnumChatFormatting.ITALIC + GT_Utility.trans("273", "Original Recipe by: ")
                            + recipe.owners.get(0)
                                .getName());
                    for (int i = 1; i < recipe.owners.size(); i++) {
                        drawNEIText(
                            recipeInfo,
                            EnumChatFormatting.ITALIC + GT_Utility.trans("274", "Modified by: ")
                                + recipe.owners.get(i)
                                    .getName());
                    }
                } else if (recipe.owners.size() > 0) {
                    drawNEIText(
                        recipeInfo,
                        EnumChatFormatting.ITALIC + GT_Utility.trans("272", "Recipe by: ")
                            + recipe.owners.get(0)
                                .getName());
                }
            }
            if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace && recipe.stackTraces != null
                && !recipe.stackTraces.isEmpty()) {
                drawNEIText(recipeInfo, "stackTrace:");
                // todo: good way to show all stacktraces
                for (StackTraceElement stackTrace : recipe.stackTraces.get(0)) {
                    drawNEIText(recipeInfo, stackTrace.toString());
                }
            }
        }

        protected void drawNEIText(NEIRecipeInfo recipeInfo, String text) {
            drawNEIText(recipeInfo, text, 10);
        }

        /**
         * Draws text on NEI recipe.
         *
         * @param yShift y position to shift after this text
         */
        @SuppressWarnings("SameParameterValue")
        protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int yShift) {
            drawNEIText(recipeInfo, text, 10, yShift);
        }

        /**
         * Draws text on NEI recipe.
         *
         * @param xStart x position to start drawing
         * @param yShift y position to shift after this text
         */
        @SuppressWarnings("SameParameterValue")
        protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int xStart, int yShift) {
            Minecraft.getMinecraft().fontRenderer.drawString(
                text,
                xStart,
                recipeInfo.yPos,
                neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
            recipeInfo.yPos += yShift;
        }

        protected void drawOptionalNEIText(NEIRecipeInfo recipeInfo, String text) {
            if (GT_Utility.isStringValid(text) && !text.equals("unspecified")) {
                drawNEIText(recipeInfo, text, 10);
            }
        }

        protected void drawNEITextMultipleLines(NEIRecipeInfo recipeInfo, List<String> texts) {
            for (String text : texts) {
                drawNEIText(recipeInfo, text, 10);
            }
        }

        public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
            GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
            for (PositionedStack pStack : neiCachedRecipe.mInputs) {
                if (stack == pStack.item) {
                    if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                        currentTip = handleNEIItemInputTooltip(
                            currentTip,
                            (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                    }
                    break;
                }
            }
            for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
                if (stack == pStack.item) {
                    if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                        currentTip = handleNEIItemOutputTooltip(
                            currentTip,
                            (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                    }
                    break;
                }
            }
            return currentTip;
        }

        protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
            GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
            if (pStack.isNotConsumed()) {
                currentTip.add(GRAY + GT_Utility.trans("151", "Does not get consumed in the process"));
            }
            return currentTip;
        }

        protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
            GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
            if (pStack.isChanceBased()) {
                currentTip.add(GRAY + GT_Utility.trans("150", "Chance: ") + pStack.getChanceText());
            }
            return currentTip;
        }

        public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
            for (PositionedStack stack : neiCachedRecipe.mInputs) {
                if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    drawNEIOverlayForInput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
                }
            }
            for (PositionedStack stack : neiCachedRecipe.mOutputs) {
                if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    drawNEIOverlayForOutput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
                }
            }
        }

        protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
            if (stack.isNotConsumed()) {
                drawNEIOverlayText("NC", stack);
            }
        }

        protected void drawNEIOverlayForOutput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
            if (stack.isChanceBased()) {
                drawNEIOverlayText(stack.getChanceText(), stack);
            }
        }

        @SuppressWarnings("SameParameterValue")
        protected void drawNEIOverlayText(String text, PositionedStack stack, int color, float scale, boolean shadow,
            Alignment alignment) {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            int width = fontRenderer.getStringWidth(text);
            int x = (int) ((stack.relx + 8 + 8 * alignment.x) / scale) - (width / 2 * (alignment.x + 1));
            int y = (int) ((stack.rely + 8 + 8 * alignment.y) / scale)
                - (fontRenderer.FONT_HEIGHT / 2 * (alignment.y + 1))
                - (alignment.y - 1) / 2;

            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, 1);
            fontRenderer.drawString(text, x, y, color, shadow);
            GlStateManager.popMatrix();
        }

        protected void drawNEIOverlayText(String text, PositionedStack stack) {
            drawNEIOverlayText(
                text,
                stack,
                colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
                0.5f,
                false,
                Alignment.TopLeft);
        }

        public void updateNEITextColorOverride() {
            neiTextColorOverride = colorOverride.getTextColorOrDefault("nei", -1);
        }

        public Power getPowerFromRecipeMap() {
            // By default, assume generic EU LV power with no overclocks
            Power power;
            if (mShowVoltageAmperageInNEI) {
                power = new EUPower((byte) 1, mAmperage);
            } else {
                power = new UnspecifiedEUPower((byte) 1, mAmperage);
            }
            return power;
        }

        /**
         * Use {@link #getItemInputPositions} or {@link #getSpecialItemPosition} or {@link #getFluidInputPositions}
         * instead
         */
        @Deprecated
        public ArrayList<PositionedStack> getInputPositionedStacks(GT_Recipe recipe) {
            return null;
        }

        /**
         * Use {@link #getItemOutputPositions} or {@link #getFluidOutputPositions} instead
         */
        @Deprecated
        public ArrayList<PositionedStack> getOutputPositionedStacks(GT_Recipe recipe) {
            return null;
        }

        public void addRecipe(Object o, FluidStack[] fluidInputArray, FluidStack[] fluidOutputArray) {}
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Here are a few Classes I use for Special Cases in some Machines without having to write a separate Machine Class.
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Nicely display NEI with many items and fluids. Remember to call {@link GT_Recipe_Map#setUsualFluidInputCount} and
     * {@link GT_Recipe_Map#setUsualFluidOutputCount}. If row count >= 6, it doesn't fit in 2 recipes per page, so
     * change it via IMC.
     */
    public static class GT_Recipe_Map_LargeNEI extends GT_Recipe_Map {

        private static final int xDirMaxCount = 3;
        private static final int yOrigin = 8;

        public GT_Recipe_Map_LargeNEI(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
            useModularUI(true);
            setLogoPos(80, 62);
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 16, yOrigin, xDirMaxCount);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper.getGridPositions(itemOutputCount, 106, yOrigin, xDirMaxCount);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 16, yOrigin + getItemRowCount() * 18, xDirMaxCount);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getGridPositions(fluidOutputCount, 106, yOrigin + getItemRowCount() * 18, xDirMaxCount);
        }

        @Override
        public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
            IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
            IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
            Supplier<Float> progressSupplier, Pos2d windowOffset) {
            // Delay setter so that calls to #setUsualFluidInputCount and #setUsualFluidOutputCount are considered
            setNEIBackgroundSize(172, 82 + (Math.max(getItemRowCount() + getFluidRowCount() - 4, 0)) * 18);
            return super.createNEITemplate(
                itemInputsInventory,
                itemOutputsInventory,
                specialSlotInventory,
                fluidInputsInventory,
                fluidOutputsInventory,
                progressSupplier,
                windowOffset);
        }

        private int getItemRowCount() {
            return (Math.max(mUsualInputCount, mUsualOutputCount) - 1) / xDirMaxCount + 1;
        }

        private int getFluidRowCount() {
            return (Math.max(getUsualFluidInputCount(), getUsualFluidOutputCount()) - 1) / xDirMaxCount + 1;
        }
    }

    /**
     * Display fluids where normally items are placed on NEI.
     */
    public static class GT_Recipe_Map_FluidOnly extends GT_Recipe_Map {

        public GT_Recipe_Map_FluidOnly(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
            useModularUI(true);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getItemInputPositions(fluidInputCount);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getItemOutputPositions(fluidOutputCount);
        }
    }

    /**
     * Abstract Class for general Recipe Handling of non GT Recipes
     */
    public abstract static class GT_Recipe_Map_NonGTRecipes extends GT_Recipe_Map {

        public GT_Recipe_Map_NonGTRecipes(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return false;
        }

        @Override
        public boolean containsInput(FluidStack aFluid) {
            return false;
        }

        @Override
        public boolean containsInput(Fluid aFluid) {
            return false;
        }

        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
            int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            return null;
        }

        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
            FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
            return null;
        }

        @Override
        public GT_Recipe addRecipe(GT_Recipe aRecipe) {
            return null;
        }

        @Override
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
            int aEUt, int aSpecialValue) {
            return null;
        }

        @Override
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            return null;
        }

        @Override
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, boolean hidden) {
            return null;
        }

        @Override
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
            return null;
        }

        @Override
        public GT_Recipe add(GT_Recipe aRecipe) {
            return null;
        }

        @Override
        public void reInit() {
            /**/
        }

        @Override
        protected GT_Recipe addToItemMap(GT_Recipe aRecipe) {
            return null;
        }
    }

    /**
     * Just a Recipe Map with Utility specifically for Fuels.
     */
    public static class GT_Recipe_Map_Fuel extends GT_Recipe_Map {

        private final Map<String, GT_Recipe> mRecipesByFluidInput = new HashMap<>();

        public GT_Recipe_Map_Fuel(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
            setDisableOptimize(true);
        }

        public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, int aFuelValueInEU) {
            return addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
        }

        public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, int aChance, int aFuelValueInEU) {
            return addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
        }

        public GT_Recipe addFuel(FluidStack aFluidInput, FluidStack aFluidOutput, int aFuelValueInEU) {
            return addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
        }

        public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput,
            int aFuelValueInEU) {
            return addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
        }

        public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput,
            int aChance, int aFuelValueInEU) {
            return addRecipe(
                true,
                new ItemStack[] { aInput },
                new ItemStack[] { aOutput },
                null,
                new int[] { aChance },
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                0,
                0,
                aFuelValueInEU);
        }

        @Override
        public GT_Recipe add(GT_Recipe aRecipe) {
            aRecipe = super.add(aRecipe);
            if (aRecipe.mInputs != null && GT_Utility.getNonnullElementCount(aRecipe.mInputs) == 1
                && (aRecipe.mFluidInputs == null || GT_Utility.getNonnullElementCount(aRecipe.mFluidInputs) == 0)) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(aRecipe.mInputs[0], true);
                if (tFluid != null) {
                    tFluid.amount = 0;
                    mRecipesByFluidInput.put(tFluid.getUnlocalizedName(), aRecipe);
                }
            } else if ((aRecipe.mInputs == null || GT_Utility.getNonnullElementCount(aRecipe.mInputs) == 0)
                && aRecipe.mFluidInputs != null
                && GT_Utility.getNonnullElementCount(aRecipe.mFluidInputs) == 1
                && aRecipe.mFluidInputs[0] != null) {
                    mRecipesByFluidInput.put(aRecipe.mFluidInputs[0].getUnlocalizedName(), aRecipe);
                }
            return aRecipe;
        }

        public GT_Recipe findFuel(FluidStack aFluidInput) {
            return mRecipesByFluidInput.get(aFluidInput.getUnlocalizedName());
        }
    }

    /**
     * Special Class for Furnace Recipe handling.
     */
    public static class GT_Recipe_Map_Furnace extends GT_Recipe_Map_NonGTRecipes {

        public GT_Recipe_Map_Furnace(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            if (aInputs == null || aInputs.length == 0 || aInputs[0] == null) return NOT_FOUND;
            if (aRecipe != null && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
                return FindRecipeResult.ofSuccess(aRecipe);
            ItemStack tOutput = GT_ModHandler.getSmeltingOutput(aInputs[0], false, null);
            return tOutput == null ? NOT_FOUND
                : FindRecipeResult.ofSuccess(
                    new GT_Recipe(
                        false,
                        new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                        new ItemStack[] { tOutput },
                        null,
                        null,
                        null,
                        null,
                        128,
                        4,
                        0));
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return GT_ModHandler.getSmeltingOutput(aStack, false, null) != null;
        }
    }

    /**
     * Special Class for Microwave Recipe handling.
     */
    public static class GT_Recipe_Map_Microwave extends GT_Recipe_Map_NonGTRecipes {

        public GT_Recipe_Map_Microwave(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            if (aInputs == null || aInputs.length == 0 || aInputs[0] == null) return NOT_FOUND;
            if (aRecipe != null && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
                return FindRecipeResult.ofSuccess(aRecipe);
            ItemStack tOutput = GT_ModHandler.getSmeltingOutput(aInputs[0], false, null);

            if (GT_Utility.areStacksEqual(aInputs[0], new ItemStack(Items.book, 1, W))) {
                return FindRecipeResult.ofSuccess(
                    new GT_Recipe(
                        false,
                        new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                        new ItemStack[] {
                            GT_Utility.getWrittenBook("Manual_Microwave", ItemList.Book_Written_03.get(1)) },
                        null,
                        null,
                        null,
                        null,
                        32,
                        4,
                        0));
            }

            // Check Container Item of Input since it is around the Input, then the Input itself, then Container Item of
            // Output and last check the Output itself
            for (ItemStack tStack : new ItemStack[] { GT_Utility.getContainerItem(aInputs[0], true), aInputs[0],
                GT_Utility.getContainerItem(tOutput, true), tOutput }) if (tStack != null) {
                    if (GT_Utility.areStacksEqual(tStack, new ItemStack(Blocks.netherrack, 1, W), true)
                        || GT_Utility.areStacksEqual(tStack, new ItemStack(Blocks.tnt, 1, W), true)
                        || GT_Utility.areStacksEqual(tStack, new ItemStack(Items.egg, 1, W), true)
                        || GT_Utility.areStacksEqual(tStack, new ItemStack(Items.firework_charge, 1, W), true)
                        || GT_Utility.areStacksEqual(tStack, new ItemStack(Items.fireworks, 1, W), true)
                        || GT_Utility.areStacksEqual(tStack, new ItemStack(Items.fire_charge, 1, W), true)) {
                        GT_Log.exp.println(
                            "Microwave Explosion due to TNT || EGG || FIREWORKCHARGE || FIREWORK || FIRE CHARGE");
                        return EXPLODE;
                    }
                    ItemData tData = GT_OreDictUnificator.getItemData(tStack);

                    if (tData != null) {
                        if (tData.mMaterial != null && tData.mMaterial.mMaterial != null) {
                            if (tData.mMaterial.mMaterial.contains(SubTag.METAL)
                                || tData.mMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
                                GT_Log.exp.println("Microwave Explosion due to METAL insertion");
                                return EXPLODE;
                            }
                            if (tData.mMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
                                GT_Log.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                                return ON_FIRE;
                            }
                        }
                        for (MaterialStack tMaterial : tData.mByProducts) if (tMaterial != null) {
                            if (tMaterial.mMaterial.contains(SubTag.METAL)
                                || tMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
                                GT_Log.exp.println("Microwave Explosion due to METAL insertion");
                                return EXPLODE;
                            }
                            if (tMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
                                GT_Log.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                                return ON_FIRE;
                            }
                        }
                    }
                    if (TileEntityFurnace.getItemBurnTime(tStack) > 0) {
                        GT_Log.exp.println("Microwave INFLAMMATION due to BURNABLE insertion");
                        return ON_FIRE;
                    }
                }

            return tOutput == null ? NOT_FOUND
                : FindRecipeResult.ofSuccess(
                    new GT_Recipe(
                        false,
                        new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                        new ItemStack[] { tOutput },
                        null,
                        null,
                        null,
                        null,
                        32,
                        4,
                        0));
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return GT_ModHandler.getSmeltingOutput(aStack, false, null) != null;
        }
    }

    /**
     * Special Class for Unboxinator handling.
     */
    public static class GT_Recipe_Map_Unboxinator extends GT_Recipe_Map {

        public GT_Recipe_Map_Unboxinator(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            if (aInputs == null || aInputs.length == 0 || !ItemList.IC2_Scrapbox.isStackEqual(aInputs[0], false, true))
                return super.findRecipeWithResult(
                    aRecipe,
                    aNotUnificated,
                    aDontCheckStackSizes,
                    aVoltage,
                    aFluids,
                    aSpecialSlot,
                    aInputs);
            ItemStack tOutput = GT_ModHandler.getRandomScrapboxDrop();
            if (tOutput == null) return super.findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            GT_Recipe rRecipe = new GT_Recipe(
                false,
                new ItemStack[] { ItemList.IC2_Scrapbox.get(1) },
                new ItemStack[] { tOutput },
                null,
                null,
                null,
                null,
                16,
                1,
                0);
            // It is not allowed to be buffered due to the random Output
            rRecipe.mCanBeBuffered = false;
            // Due to its randomness it is not good if there are Items in the Output Slot, because those Items could
            // manipulate the outcome.
            rRecipe.mNeedsEmptyOutput = true;
            return FindRecipeResult.ofSuccess(rRecipe);
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return ItemList.IC2_Scrapbox.isStackEqual(aStack, false, true) || super.containsInput(aStack);
        }
    }

    /**
     * Special Class for Fluid Canner handling.
     */
    public static class GT_Recipe_Map_FluidCanner extends GT_Recipe_Map {

        public GT_Recipe_Map_FluidCanner(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            FindRecipeResult result = super.findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            if (aInputs == null || aInputs.length == 0
                || aInputs[0] == null
                || result.isSuccessful()
                || !GregTech_API.sPostloadFinished) return result;

            if (aFluids != null && aFluids.length > 0 && aFluids[0] != null) {
                ItemStack tOutput = GT_Utility.fillFluidContainer(aFluids[0], aInputs[0], false, true);
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput, true);
                if (tFluid != null) {
                    GT_Recipe recipe = new GT_Recipe(
                        false,
                        new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                        new ItemStack[] { tOutput },
                        null,
                        null,
                        new FluidStack[] { tFluid },
                        null,
                        Math.max(tFluid.amount / 64, 16),
                        1,
                        0);
                    recipe.mCanBeBuffered = false;
                    return FindRecipeResult.ofSuccess(recipe);
                }
            }
            FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInputs[0], true);
            if (tFluid != null) {
                GT_Recipe recipe = new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    new ItemStack[] { GT_Utility.getContainerItem(aInputs[0], true) },
                    null,
                    null,
                    null,
                    new FluidStack[] { tFluid },
                    Math.max(tFluid.amount / 64, 16),
                    1,
                    0);
                recipe.mCanBeBuffered = false;
                return FindRecipeResult.ofSuccess(recipe);
            }
            return NOT_FOUND;
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return aStack != null && (super.containsInput(aStack) || (aStack.getItem() instanceof IFluidContainerItem
                && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0));
        }

        @Override
        public boolean containsInput(FluidStack aFluid) {
            return true;
        }

        @Override
        public boolean containsInput(Fluid aFluid) {
            return true;
        }
    }

    /**
     * Special Class for Recycler Recipe handling.
     */
    public static class GT_Recipe_Map_Recycler extends GT_Recipe_Map_NonGTRecipes {

        public GT_Recipe_Map_Recycler(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            if (aInputs == null || aInputs.length == 0 || aInputs[0] == null) return NOT_FOUND;
            if (aRecipe != null && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
                return FindRecipeResult.ofSuccess(aRecipe);
            return FindRecipeResult.ofSuccess(
                new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    new ItemStack[] { GT_ModHandler.getRecyclerOutput(aInputs[0], 0) },
                    null,
                    new int[] { 1250 },
                    null,
                    null,
                    45,
                    1,
                    0));
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return GT_ModHandler.getRecyclerOutput(aStack, 0) != null;
        }
    }

    /**
     * Special Class for Macerator/RockCrusher Recipe handling.
     */
    public static class GT_Recipe_Map_Macerator extends GT_Recipe_Map {

        public GT_Recipe_Map_Macerator(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            if (aInputs == null || aInputs.length == 0 || aInputs[0] == null || !GregTech_API.sPostloadFinished)
                return super.findRecipeWithResult(
                    aRecipe,
                    aNotUnificated,
                    aDontCheckStackSizes,
                    aVoltage,
                    aFluids,
                    aSpecialSlot,
                    aInputs);
            FindRecipeResult result = super.findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            if (result.isSuccessful()) return result;

            try {
                List<ItemStack> tRecipeOutputs = mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher
                    .getRecipe(GT_Utility.copyAmount(1, aInputs[0]))
                    .getRandomizedOuputs();
                if (tRecipeOutputs != null) {
                    GT_Recipe recipe = new GT_Recipe(
                        false,
                        new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                        tRecipeOutputs.toArray(new ItemStack[0]),
                        null,
                        null,
                        null,
                        null,
                        800,
                        2,
                        0);
                    recipe.mCanBeBuffered = false;
                    recipe.mNeedsEmptyOutput = true;
                    return FindRecipeResult.ofSuccess(recipe);
                }
            } catch (NoClassDefFoundError e) {
                if (D1) GT_Log.err.println("Railcraft Not loaded");
            } catch (NullPointerException e) {
                /**/
            }

            ItemStack tComparedInput = GT_Utility.copyOrNull(aInputs[0]);
            ItemStack[] tOutputItems = GT_ModHandler.getMachineOutput(
                tComparedInput,
                ic2.api.recipe.Recipes.macerator.getRecipes(),
                true,
                new NBTTagCompound(),
                null,
                null,
                null);
            if (tComparedInput != null && GT_Utility.arrayContainsNonNull(tOutputItems)) {
                return FindRecipeResult.ofSuccess(
                    new GT_Recipe(
                        false,
                        new ItemStack[] {
                            GT_Utility.copyAmount(aInputs[0].stackSize - tComparedInput.stackSize, aInputs[0]) },
                        tOutputItems,
                        null,
                        null,
                        null,
                        null,
                        400,
                        2,
                        0));
            }
            return NOT_FOUND;
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return super.containsInput(aStack) || GT_Utility.arrayContainsNonNull(
                GT_ModHandler.getMachineOutput(
                    GT_Utility.copyAmount(64, aStack),
                    ic2.api.recipe.Recipes.macerator.getRecipes(),
                    false,
                    new NBTTagCompound(),
                    null,
                    null,
                    null));
        }
    }

    /**
     * Special Class for Assembler handling.
     */
    public static class GT_Recipe_Map_Assembler extends GT_Recipe_Map {

        public GT_Recipe_Map_Assembler(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {

            FindRecipeResult result = super.findRecipeWithResult(
                aRecipe,
                true,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            /*
             * Doesnt work, keep it as a reminder tho if (rRecipe == null){ Set<ItemStack> aInputs2 = new
             * TreeSet<ItemStack>(); for (ItemStack aInput : aInputs) { aInputs2.add(aInput); } for (ItemStack aInput :
             * aInputs) { aInputs2.remove(aInput); int[] oredictIDs = OreDictionary.getOreIDs(aInput); if (
             * oredictIDs.length > 1){ for (final int i : oredictIDs){ final ItemStack[] oredictIS = (ItemStack[])
             * OreDictionary.getOres(OreDictionary.getOreName(i)).toArray(); if (oredictIS != null && oredictIS.length >
             * 1){ for (final ItemStack IS : oredictIS){ aInputs2.add(IS); ItemStack[] temp = (ItemStack[])
             * aInputs2.toArray(); rRecipe = super.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids,
             * aSpecialSlot,temp); if(rRecipe!= null){ break; } else { aInputs2.remove(IS); } } if(rRecipe!= null)
             * break; } } if(rRecipe!= null) break; }else aInputs2.add(aInput); if(rRecipe!= null) break; } }
             */
            if (aInputs == null || aInputs.length == 0
                || aInputs[0] == null
                || !result.isSuccessful()
                || !GregTech_API.sPostloadFinished) return result;

            GT_Recipe rRecipe = result.getRecipeNonNull();
            for (ItemStack aInput : aInputs) {
                if (ItemList.Paper_Printed_Pages.isStackEqual(aInput, false, true)) {
                    rRecipe = rRecipe.copy();
                    rRecipe.mCanBeBuffered = false;
                    rRecipe.mOutputs[0].setTagCompound(aInput.getTagCompound());
                }
            }
            return FindRecipeResult.ofSuccess(rRecipe);
        }
    }

    /**
     * Special Class for Forming Press handling.
     */
    public static class GT_Recipe_Map_FormingPress extends GT_Recipe_Map {

        public GT_Recipe_Map_FormingPress(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            FindRecipeResult result = super.findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            if (aInputs == null || aInputs.length < 2 || !GregTech_API.sPostloadFinished) return result;
            if (!result.isSuccessful()) {
                return findRenamingRecipe(aInputs);
            }
            for (ItemStack aMold : aInputs) {
                if (ItemList.Shape_Mold_Credit.isStackEqual(aMold, false, true)) {
                    NBTTagCompound tNBT = aMold.getTagCompound();
                    if (tNBT == null) tNBT = new NBTTagCompound();
                    if (!tNBT.hasKey("credit_security_id")) tNBT.setLong("credit_security_id", System.nanoTime());
                    aMold.setTagCompound(tNBT);

                    GT_Recipe rRecipe = result.getRecipeNonNull();
                    rRecipe = rRecipe.copy();
                    rRecipe.mCanBeBuffered = false;
                    rRecipe.mOutputs[0].setTagCompound(tNBT);
                    return FindRecipeResult.ofSuccess(rRecipe);
                }
            }
            return result;
        }

        private ItemStack findNameMoldIndex(ItemStack[] inputs) {
            for (ItemStack stack : inputs) {
                if (ItemList.Shape_Mold_Name.isStackEqual(stack, false, true)) return stack;
            }
            return null;
        }

        private ItemStack findStackToRename(ItemStack[] inputs, ItemStack mold) {
            for (ItemStack stack : inputs) {
                if (stack == mold || stack == null) continue;
                return stack;
            }
            return null;
        }

        @Nonnull
        private FindRecipeResult findRenamingRecipe(ItemStack[] inputs) {
            ItemStack mold = findNameMoldIndex(inputs);
            if (mold == null) return NOT_FOUND;
            ItemStack input = findStackToRename(inputs, mold);
            if (input == null) return NOT_FOUND;
            ItemStack output = GT_Utility.copyAmount(1, input);
            if (output == null) return NOT_FOUND;
            output.setStackDisplayName(mold.getDisplayName());
            GT_Recipe recipe = new GT_Recipe(
                false,
                new ItemStack[] { ItemList.Shape_Mold_Name.get(0), GT_Utility.copyAmount(1, input) },
                new ItemStack[] { output },
                null,
                null,
                null,
                null,
                128,
                8,
                0);
            recipe.mCanBeBuffered = false;
            return FindRecipeResult.ofSuccess(recipe);
        }
    }

    /**
     * Special Class for Printer handling.
     */
    public static class GT_Recipe_Map_Printer extends GT_Recipe_Map {

        public GT_Recipe_Map_Printer(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Nonnull
        @Override
        public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {
            FindRecipeResult result = super.findRecipeWithResult(
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
            if (aInputs == null || aInputs.length == 0
                || aInputs[0] == null
                || aFluids == null
                || aFluids.length == 0
                || aFluids[0] == null
                || !GregTech_API.sPostloadFinished) return result;

            Dyes aDye = null;
            for (Dyes tDye : Dyes.VALUES) if (tDye.isFluidDye(aFluids[0])) {
                aDye = tDye;
                break;
            }

            if (aDye == null) return result;

            if (!result.isSuccessful()) {
                ItemStack tOutput = GT_ModHandler.getAllRecipeOutput(
                    null,
                    aInputs[0],
                    aInputs[0],
                    aInputs[0],
                    aInputs[0],
                    ItemList.DYE_ONLY_ITEMS[aDye.mIndex].get(1),
                    aInputs[0],
                    aInputs[0],
                    aInputs[0],
                    aInputs[0]);
                if (tOutput != null) {
                    GT_Recipe recipe = addRecipe(
                        new GT_Recipe(
                            true,
                            new ItemStack[] { GT_Utility.copyAmount(8, aInputs[0]) },
                            new ItemStack[] { tOutput },
                            null,
                            null,
                            new FluidStack[] { new FluidStack(aFluids[0].getFluid(), (int) L) },
                            null,
                            256,
                            2,
                            0),
                        false,
                        false,
                        true);
                    return recipe != null ? FindRecipeResult.ofSuccess(recipe) : NOT_FOUND;
                }

                tOutput = GT_ModHandler
                    .getAllRecipeOutput(null, aInputs[0], ItemList.DYE_ONLY_ITEMS[aDye.mIndex].get(1));
                if (tOutput != null) {
                    GT_Recipe recipe = addRecipe(
                        new GT_Recipe(
                            true,
                            new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                            new ItemStack[] { tOutput },
                            null,
                            null,
                            new FluidStack[] { new FluidStack(aFluids[0].getFluid(), (int) L) },
                            null,
                            32,
                            2,
                            0),
                        false,
                        false,
                        true);
                    return recipe != null ? FindRecipeResult.ofSuccess(recipe) : NOT_FOUND;
                }
            } else {
                GT_Recipe rRecipe = result.getRecipeNonNull();
                if (aInputs[0].getItem() == Items.paper) {
                    if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return NOT_FOUND;
                    NBTTagCompound tNBT = aSpecialSlot.getTagCompound();
                    if (tNBT == null || GT_Utility.isStringInvalid(tNBT.getString("title"))
                        || GT_Utility.isStringInvalid(tNBT.getString("author"))) return NOT_FOUND;

                    rRecipe = rRecipe.copy();
                    rRecipe.mCanBeBuffered = false;
                    rRecipe.mOutputs[0].setTagCompound(tNBT);
                    return FindRecipeResult.ofSuccess(rRecipe);
                }
                if (aInputs[0].getItem() == Items.map) {
                    if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return NOT_FOUND;
                    NBTTagCompound tNBT = aSpecialSlot.getTagCompound();
                    if (tNBT == null || !tNBT.hasKey("map_id")) return NOT_FOUND;

                    rRecipe = rRecipe.copy();
                    rRecipe.mCanBeBuffered = false;
                    rRecipe.mOutputs[0].setItemDamage(tNBT.getShort("map_id"));
                    return FindRecipeResult.ofSuccess(rRecipe);
                }
                if (ItemList.Paper_Punch_Card_Empty.isStackEqual(aInputs[0], false, true)) {
                    if (!ItemList.Tool_DataStick.isStackEqual(aSpecialSlot, false, true)) return NOT_FOUND;
                    NBTTagCompound tNBT = aSpecialSlot.getTagCompound();
                    if (tNBT == null || !tNBT.hasKey("GT.PunchCardData")) return NOT_FOUND;

                    rRecipe = rRecipe.copy();
                    rRecipe.mCanBeBuffered = false;
                    rRecipe.mOutputs[0].setTagCompound(
                        GT_Utility.getNBTContainingString(
                            new NBTTagCompound(),
                            "GT.PunchCardData",
                            tNBT.getString("GT.PunchCardData")));
                    return FindRecipeResult.ofSuccess(rRecipe);
                }
            }
            return result;
        }

        @Override
        public boolean containsInput(ItemStack aStack) {
            return true;
        }

        @Override
        public boolean containsInput(FluidStack aFluid) {
            return super.containsInput(aFluid) || Dyes.isAnyFluidDye(aFluid);
        }

        @Override
        public boolean containsInput(Fluid aFluid) {
            return super.containsInput(aFluid) || Dyes.isAnyFluidDye(aFluid);
        }
    }

    public static class GT_Recipe_Map_LargeBoilerFakeFuels extends GT_Recipe_Map {

        private static final List<String> ALLOWED_SOLID_FUELS = Arrays.asList(
            GregTech_API.sMachineFile.mConfig.getStringList(
                "LargeBoiler.allowedFuels",
                ConfigCategories.machineconfig.toString(),
                new String[] { "gregtech:gt.blockreinforced:6", "gregtech:gt.blockreinforced:7" },
                "Allowed fuels for the Large Titanium Boiler and Large Tungstensteel Boiler"));

        public GT_Recipe_Map_LargeBoilerFakeFuels() {
            super(
                new HashSet<>(55),
                "gt.recipe.largeboilerfakefuels",
                "Large Boiler",
                null,
                GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
                1,
                1,
                1,
                0,
                1,
                E,
                1,
                E,
                true,
                true);
            GT_Recipe explanatoryRecipe = new GT_Recipe(
                true,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                null,
                null,
                null,
                1,
                1,
                1);
            explanatoryRecipe.setNeiDesc(
                "Not all solid fuels are listed.",
                "Any item that burns in a",
                "vanilla furnace will burn in",
                "a Large Bronze or Steel Boiler.");
            addRecipe(explanatoryRecipe);
        }

        public static boolean isAllowedSolidFuel(ItemStack stack) {
            return isAllowedSolidFuel(Item.itemRegistry.getNameForObject(stack.getItem()), stack.getItemDamage());
        }

        public static boolean isAllowedSolidFuel(String itemRegistryName, int meta) {
            return ALLOWED_SOLID_FUELS.contains(itemRegistryName + ":" + meta);
        }

        public static boolean addAllowedSolidFuel(ItemStack stack) {
            return addAllowedSolidFuel(Item.itemRegistry.getNameForObject(stack.getItem()), stack.getItemDamage());
        }

        public static boolean addAllowedSolidFuel(String itemregistryName, int meta) {
            return ALLOWED_SOLID_FUELS.add(itemregistryName + ":" + meta);
        }

        public GT_Recipe addDenseLiquidRecipe(GT_Recipe recipe) {
            return addRecipe(recipe, ((double) recipe.mSpecialValue) / 10);
        }

        public GT_Recipe addDieselRecipe(GT_Recipe recipe) {
            return addRecipe(recipe, ((double) recipe.mSpecialValue) / 40);
        }

        public void addSolidRecipes(ItemStack... itemStacks) {
            for (ItemStack itemStack : itemStacks) {
                addSolidRecipe(itemStack);
            }
        }

        public GT_Recipe addSolidRecipe(ItemStack fuelItemStack) {
            boolean allowedFuel = false;
            if (fuelItemStack != null) {
                String registryName = Item.itemRegistry.getNameForObject(fuelItemStack.getItem());
                allowedFuel = ALLOWED_SOLID_FUELS.contains(registryName + ":" + fuelItemStack.getItemDamage());
            }
            return addRecipe(
                new GT_Recipe(
                    true,
                    new ItemStack[] { fuelItemStack },
                    new ItemStack[] {},
                    null,
                    null,
                    null,
                    null,
                    1,
                    0,
                    GT_ModHandler.getFuelValue(fuelItemStack) / 1600),
                ((double) GT_ModHandler.getFuelValue(fuelItemStack)) / 1600,
                allowedFuel);
        }

        private GT_Recipe addRecipe(GT_Recipe recipe, double baseBurnTime, boolean isAllowedFuel) {
            recipe = new GT_Recipe(recipe, true);
            // Some recipes will have a burn time like 15.9999999 and % always rounds down
            double floatErrorCorrection = 0.0001;

            double bronzeBurnTime = baseBurnTime * 2 + floatErrorCorrection;
            bronzeBurnTime -= bronzeBurnTime % 0.05;
            double steelBurnTime = baseBurnTime + floatErrorCorrection;
            steelBurnTime -= steelBurnTime % 0.05;
            double titaniumBurnTime = baseBurnTime * 0.3 + floatErrorCorrection;
            titaniumBurnTime -= titaniumBurnTime % 0.05;
            double tungstensteelBurnTime = baseBurnTime * 0.15 + floatErrorCorrection;
            tungstensteelBurnTime -= tungstensteelBurnTime % 0.05;

            if (isAllowedFuel) {
                recipe.setNeiDesc(
                    "Burn time in seconds:",
                    String.format("Bronze Boiler: %.4f", bronzeBurnTime),
                    String.format("Steel Boiler: %.4f", steelBurnTime),
                    String.format("Titanium Boiler: %.4f", titaniumBurnTime),
                    String.format("Tungstensteel Boiler: %.4f", tungstensteelBurnTime));
            } else {
                recipe.setNeiDesc(
                    "Burn time in seconds:",
                    String.format("Bronze Boiler: %.4f", bronzeBurnTime),
                    String.format("Steel Boiler: %.4f", steelBurnTime),
                    "Titanium Boiler: Not allowed",
                    "Tungstenst. Boiler: Not allowed");
            }

            return super.addRecipe(recipe);
        }

        private GT_Recipe addRecipe(GT_Recipe recipe, double baseBurnTime) {
            recipe = new GT_Recipe(recipe, true);
            // Some recipes will have a burn time like 15.9999999 and % always rounds down
            double floatErrorCorrection = 0.0001;

            double bronzeBurnTime = baseBurnTime * 2 + floatErrorCorrection;
            bronzeBurnTime -= bronzeBurnTime % 0.05;
            double steelBurnTime = baseBurnTime + floatErrorCorrection;
            steelBurnTime -= steelBurnTime % 0.05;

            recipe.setNeiDesc(
                "Burn time in seconds:",
                String.format("Bronze Boiler: %.4f", bronzeBurnTime),
                String.format("Steel Boiler: %.4f", steelBurnTime),
                "Titanium Boiler: Not allowed",
                "Tungstenst. Boiler: Not allowed");

            return super.addRecipe(recipe);
        }
    }

    public static class GT_Recipe_Map_IC2NuclearFake extends GT_Recipe_Map {

        public GT_Recipe_Map_IC2NuclearFake() {
            super(
                new HashSet<>(10),
                "gt.recipe.ic2nuke",
                "Fission",
                null,
                GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
                1,
                1,
                1,
                0,
                1,
                E,
                1,
                E,
                true,
                true);
            setLogo(GT_UITextures.PICTURE_RADIATION_WARNING);
            setLogoPos(152, 24);
            setNEIBackgroundSize(172, 60);
            setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
        }

        /**
         * Add a breeder cell.
         *
         * @param input          raw stack. should be undamaged.
         * @param output         breed output
         * @param heatMultiplier bonus progress per neutron pulse per heat step
         * @param heatStep       divisor for hull heat
         * @param reflector      true if also acts as a neutron reflector, false otherwise.
         * @param requiredPulses progress required to complete breeding
         * @return added fake recipe
         */
        public GT_Recipe addBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
            int heatMultiplier, int requiredPulses) {
            return addFakeRecipe(
                input,
                output,
                reflector ? "Neutron reflecting breeder cell" : "Heat neutral Breeder Cell",
                String.format("Every %d reactor hull heat", heatStep),
                String.format("increase speed by %d00%%", heatMultiplier),
                String.format("Required pulses: %d", requiredPulses));
        }

        public GT_Recipe addFakeRecipe(ItemStack input, ItemStack output, String... neiDesc) {
            GT_Recipe r = new GT_Recipe(
                new ItemStack[] { input },
                new ItemStack[] { output },
                null,
                new int[] { 10000 },
                null,
                null,
                0,
                0,
                0);
            r.setNeiDesc(neiDesc);
            return addRecipe(r, true, true, false);
        }
    }

    public static class GT_Recipe_Map_LargeChemicalReactor extends GT_Recipe_Map_LargeNEI {

        private static final int TOTAL_INPUT_COUNT = 6;
        private static final int OUTPUT_COUNT = 6;

        public GT_Recipe_Map_LargeChemicalReactor() {
            super(
                new HashSet<>(1000),
                "gt.recipe.largechemicalreactor",
                "Large Chemical Reactor",
                null,
                GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
                TOTAL_INPUT_COUNT,
                OUTPUT_COUNT,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true);
        }

        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
            int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            aOptimize = false;
            ArrayList<ItemStack> adjustedInputs = new ArrayList<>();
            ArrayList<ItemStack> adjustedOutputs = new ArrayList<>();
            ArrayList<FluidStack> adjustedFluidInputs = new ArrayList<>();
            ArrayList<FluidStack> adjustedFluidOutputs = new ArrayList<>();

            if (aInputs == null) {
                aInputs = new ItemStack[0];
            } else {
                aInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
            }

            for (ItemStack input : aInputs) {
                FluidStack inputFluidContent = FluidContainerRegistry.getFluidForFilledItem(input);
                if (inputFluidContent != null) {
                    inputFluidContent.amount *= input.stackSize;
                    if (inputFluidContent.getFluid()
                        .getName()
                        .equals("ic2steam")) {
                        inputFluidContent = GT_ModHandler.getSteam(inputFluidContent.amount);
                    }
                    adjustedFluidInputs.add(inputFluidContent);
                } else {
                    ItemData itemData = GT_OreDictUnificator.getItemData(input);
                    if (itemData != null && itemData.hasValidPrefixMaterialData()
                        && itemData.mMaterial.mMaterial == Materials.Empty) {
                        continue;
                    } else {
                        if (itemData != null && itemData.hasValidPrefixMaterialData()
                            && itemData.mPrefix == OrePrefixes.cell) {
                            ItemStack dustStack = itemData.mMaterial.mMaterial.getDust(input.stackSize);
                            if (dustStack != null) {
                                adjustedInputs.add(dustStack);
                            } else {
                                adjustedInputs.add(input);
                            }
                        } else {
                            adjustedInputs.add(input);
                        }
                    }
                }

                if (aFluidInputs == null) {
                    aFluidInputs = new FluidStack[0];
                }
            }
            Collections.addAll(adjustedFluidInputs, aFluidInputs);
            aInputs = adjustedInputs.toArray(new ItemStack[0]);
            aFluidInputs = adjustedFluidInputs.toArray(new FluidStack[0]);

            if (aOutputs == null) {
                aOutputs = new ItemStack[0];
            } else {
                aOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
            }

            for (ItemStack output : aOutputs) {
                FluidStack outputFluidContent = FluidContainerRegistry.getFluidForFilledItem(output);
                if (outputFluidContent != null) {
                    outputFluidContent.amount *= output.stackSize;
                    if (outputFluidContent.getFluid()
                        .getName()
                        .equals("ic2steam")) {
                        outputFluidContent = GT_ModHandler.getSteam(outputFluidContent.amount);
                    }
                    adjustedFluidOutputs.add(outputFluidContent);
                } else {
                    ItemData itemData = GT_OreDictUnificator.getItemData(output);
                    if (!(itemData != null && itemData.hasValidPrefixMaterialData()
                        && itemData.mMaterial.mMaterial == Materials.Empty)) {
                        adjustedOutputs.add(output);
                    }
                }
            }
            if (aFluidOutputs == null) {
                aFluidOutputs = new FluidStack[0];
            }
            Collections.addAll(adjustedFluidOutputs, aFluidOutputs);
            aOutputs = adjustedOutputs.toArray(new ItemStack[0]);
            aFluidOutputs = adjustedFluidOutputs.toArray(new FluidStack[0]);

            return super.addRecipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
        }
    }

    public static class GT_Recipe_Map_DistillationTower extends GT_Recipe_Map {

        public GT_Recipe_Map_DistillationTower() {
            super(
                new HashSet<>(110),
                "gt.recipe.distillationtower",
                "Distillation Tower",
                null,
                GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "DistillationTower"),
                2,
                1,
                0,
                0,
                1,
                E,
                1,
                E,
                true,
                true);
            setLogoPos(80, 62);
        }

        @Override
        public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
            if (isOutput) {
                if (isFluid) {
                    return GT_UITextures.OVERLAY_SLOTS_NUMBER[index + 1];
                } else {
                    return GT_UITextures.OVERLAY_SLOTS_NUMBER[0];
                }
            }
            return super.getOverlayForSlot(isFluid, false, index, isSpecial);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(106, 62));
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            List<Pos2d> results = new ArrayList<>();
            for (int i = 1; i < fluidOutputCount + 1; i++) {
                results.add(new Pos2d(106 + (i % 3) * 18, 62 - (i / 3) * 18));
            }
            return results;
        }
    }

    public static class GT_Recipe_Map_OilCracker extends GT_Recipe_Map {

        private final Set<String> mValidCatalystFluidNames = new HashSet<>();

        public GT_Recipe_Map_OilCracker() {
            super(
                new HashSet<>(70),
                "gt.recipe.craker",
                "Oil Cracker",
                null,
                GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "OilCracker"),
                1,
                1,
                1,
                2,
                1,
                E,
                1,
                E,
                true,
                true);
        }

        @Override
        public GT_Recipe add(GT_Recipe aRecipe) {
            GT_Recipe ret = super.add(aRecipe);
            if (ret != null && ret.mFluidInputs != null && ret.mFluidInputs.length > 1 && ret.mFluidInputs[1] != null) {
                mValidCatalystFluidNames.add(
                    ret.mFluidInputs[1].getFluid()
                        .getName());
            }
            return ret;
        }

        public boolean isValidCatalystFluid(FluidStack aFluidStack) {
            return mValidCatalystFluidNames.contains(
                aFluidStack.getFluid()
                    .getName());
        }
    }

    public static class GT_Recipe_WithAlt extends GT_Recipe {

        ItemStack[][] mOreDictAlt;

        GT_Recipe_WithAlt(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs,
            FluidStack[] mFluidOutputs, int[] mChances, Object mSpecialItems, int mDuration, int mEUt,
            int mSpecialValue, boolean mEnabled, boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered,
            boolean mNeedsEmptyOutput, String[] neiDesc, ItemStack[][] mOreDictAlt) {
            super(
                mInputs,
                mOutputs,
                mFluidInputs,
                mFluidOutputs,
                mChances,
                mSpecialItems,
                mDuration,
                mEUt,
                mSpecialValue,
                mEnabled,
                mHidden,
                mFakeRecipe,
                mCanBeBuffered,
                mNeedsEmptyOutput,
                neiDesc);
            this.mOreDictAlt = mOreDictAlt;
        }

        public GT_Recipe_WithAlt(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
            int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, ItemStack[][] aAlt) {
            super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
            mOreDictAlt = aAlt;
        }

        public Object getAltRepresentativeInput(int aIndex) {
            if (aIndex < 0) return null;
            if (aIndex < mOreDictAlt.length) {
                if (mOreDictAlt[aIndex] != null && mOreDictAlt[aIndex].length > 0) {
                    ItemStack[] rStacks = new ItemStack[mOreDictAlt[aIndex].length];
                    for (int i = 0; i < mOreDictAlt[aIndex].length; i++) {
                        rStacks[i] = GT_Utility.copyOrNull(mOreDictAlt[aIndex][i]);
                    }
                    return rStacks;
                }
            }
            if (aIndex >= mInputs.length) return null;
            return GT_Utility.copyOrNull(mInputs[aIndex]);
        }
    }

    private static class ReplicatorFakeMap extends GT_Recipe_Map {

        public ReplicatorFakeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
            String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
            int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
            String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Override
        public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
            Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue) {
            AtomicInteger ai = new AtomicInteger();
            Optional.ofNullable(GT_OreDictUnificator.getAssociation(aOutputs[0]))
                .map(itemData -> itemData.mMaterial)
                .map(materialsStack -> materialsStack.mMaterial)
                .map(materials -> materials.mElement)
                .map(Element::getMass)
                .ifPresent(e -> {
                    aFluidInputs[0].amount = (int) GT_MetaTileEntity_Replicator.cubicFluidMultiplier(e);
                    ai.set(GT_Utility.safeInt(aFluidInputs[0].amount * 512L, 1));
                });
            return addFakeRecipe(
                aCheckForCollisions,
                new GT_Recipe(
                    false,
                    aInputs,
                    aOutputs,
                    aSpecial,
                    null,
                    aFluidInputs,
                    aFluidOutputs,
                    ai.get(),
                    aEUt,
                    aSpecialValue));
        }
    }

    public static class GT_Recipe_Map_ComplexFusion extends GT_Recipe_Map {

        public GT_Recipe_Map_ComplexFusion(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
            String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
            int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
            int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
            boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        @Override
        public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
            int aDuration, int aEUt, int aSpecialValue) {
            return addRecipe(
                new GT_Recipe(
                    false,
                    null,
                    null,
                    null,
                    aOutputChances,
                    aFluidInputs,
                    aFluidOutputs,
                    aDuration,
                    aEUt,
                    aSpecialValue),
                false,
                false,
                false);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 7, 9, 4);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getGridPositions(fluidOutputCount, 97, 9, 4);
        }
    }

    public static class GT_Recipe_Map_AssemblyLineFake extends GT_Recipe_Map {

        public GT_Recipe_Map_AssemblyLineFake(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
            String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
            int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
            int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
            boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
            setNEITransferRect(new Rectangle(146, 26, 10, 18));
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 16, 8, 4);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(142, 8));
        }

        @Override
        public Pos2d getSpecialItemPosition() {
            return new Pos2d(142, 44);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 106, 8, 1);
        }

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
            Pos2d windowOffset) {
            int bar1Width = 17;
            int bar2Width = 18;
            builder.widget(
                new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setProgress(() -> progressSupplier.get() * ((float) (bar1Width + bar2Width) / bar1Width))
                    .setSynced(false, false)
                    .setPos(new Pos2d(88, 8).add(windowOffset))
                    .setSize(bar1Width, 72));
            builder.widget(
                new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setProgress(
                        () -> (progressSupplier.get() - ((float) bar1Width / (bar1Width + bar2Width)))
                            * ((float) (bar1Width + bar2Width) / bar2Width))
                    .setSynced(false, false)
                    .setPos(new Pos2d(124, 8).add(windowOffset))
                    .setSize(bar2Width, 72));
            builder.widget(
                new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_3, 18)
                    .setDirection(ProgressBar.Direction.UP)
                    .setProgress(progressSupplier)
                    .setSynced(false, false)
                    .setPos(new Pos2d(146, 26).add(windowOffset))
                    .setSize(10, 18));
        }
    }
}
