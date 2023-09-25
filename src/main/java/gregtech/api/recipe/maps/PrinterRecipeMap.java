package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.L;
import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Printer handling.
 */
public class PrinterRecipeMap extends RecipeMap {

    public PrinterRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
        FindRecipeResult result = super.findRecipeWithResult(
            aRecipe,
            aIsValidRecipe,
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

            tOutput = GT_ModHandler.getAllRecipeOutput(null, aInputs[0], ItemList.DYE_ONLY_ITEMS[aDye.mIndex].get(1));
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
