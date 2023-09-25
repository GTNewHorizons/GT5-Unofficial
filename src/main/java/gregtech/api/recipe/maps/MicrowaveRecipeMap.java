package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.recipe.check.FindRecipeResult.*;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Microwave Recipe handling.
 */
public class MicrowaveRecipeMap extends NonGTRecipeMap {

    public MicrowaveRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
    public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, Predicate<GT_Recipe> aIsValidRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
        ItemStack aSpecialSlot, ItemStack... aInputs) {
        if (aInputs == null || aInputs.length == 0 || aInputs[0] == null) return NOT_FOUND;
        if (aRecipe != null && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
            return FindRecipeResult.ofSuccess(aRecipe);
        ItemStack tOutput = GT_ModHandler.getSmeltingOutput(aInputs[0], false, null);

        if (GT_Utility.areStacksEqual(aInputs[0], new ItemStack(Items.book, 1, W))) {
            return FindRecipeResult.ofSuccess(
                new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    new ItemStack[] { GT_Utility.getWrittenBook("Manual_Microwave", ItemList.Book_Written_03.get(1)) },
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
                    GT_Log.exp
                        .println("Microwave Explosion due to TNT || EGG || FIREWORKCHARGE || FIREWORK || FIRE CHARGE");
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
