package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.L;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Printer handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrinterBackend extends RecipeMapBackend {

    public PrinterBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public GT_Recipe modifyFoundRecipe(GT_Recipe recipe, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        if (items[0].getItem() == Items.paper) {
            assert specialSlot != null;
            if (!ItemList.Tool_DataStick.isStackEqual(specialSlot, false, true)) return null;
            NBTTagCompound nbt = specialSlot.getTagCompound();
            if (nbt == null || GT_Utility.isStringInvalid(nbt.getString("title"))
                || GT_Utility.isStringInvalid(nbt.getString("author"))) return null;

            recipe = recipe.copy();
            recipe.mCanBeBuffered = false;
            recipe.mOutputs[0].setTagCompound(nbt);
            return recipe;
        }
        if (items[0].getItem() == Items.map) {
            assert specialSlot != null;
            if (!ItemList.Tool_DataStick.isStackEqual(specialSlot, false, true)) return null;
            NBTTagCompound nbt = specialSlot.getTagCompound();
            if (nbt == null || !nbt.hasKey("map_id")) return null;

            recipe = recipe.copy();
            recipe.mCanBeBuffered = false;
            recipe.mOutputs[0].setItemDamage(nbt.getShort("map_id"));
            return recipe;
        }
        if (ItemList.Paper_Punch_Card_Empty.isStackEqual(items[0], false, true)) {
            assert specialSlot != null;
            if (!ItemList.Tool_DataStick.isStackEqual(specialSlot, false, true)) return null;
            NBTTagCompound nbt = specialSlot.getTagCompound();
            if (nbt == null || !nbt.hasKey("GT.PunchCardData")) return null;

            recipe = recipe.copy();
            recipe.mCanBeBuffered = false;
            recipe.mOutputs[0].setTagCompound(
                GT_Utility.getNBTContainingString(
                    new NBTTagCompound(),
                    "GT.PunchCardData",
                    nbt.getString("GT.PunchCardData")));
            return recipe;
        }
        return recipe;
    }

    @Override
    protected GT_Recipe findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        if (items.length == 0 || items[0] == null || fluids.length == 0 || fluids[0] == null) {
            return null;
        }
        Dyes dye = null;
        for (Dyes tDye : Dyes.VALUES) if (tDye.isFluidDye(fluids[0])) {
            dye = tDye;
            break;
        }
        if (dye == null) return null;

        ItemStack batchRecolorOutput = GT_ModHandler.getAllRecipeOutput(
            null,
            items[0],
            items[0],
            items[0],
            items[0],
            ItemList.DYE_ONLY_ITEMS[dye.mIndex].get(1),
            items[0],
            items[0],
            items[0],
            items[0]);
        if (batchRecolorOutput != null) {
            return GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(8, items[0]))
                .itemOutputs(batchRecolorOutput)
                .fluidInputs(new FluidStack(fluids[0].getFluid(), (int) L))
                .duration(256)
                .eut(2)
                .hidden()
                .build()
                .map(this::compileRecipe)
                .orElse(null);
        }

        ItemStack singleRecolorOutput = GT_ModHandler
            .getAllRecipeOutput(null, items[0], ItemList.DYE_ONLY_ITEMS[dye.mIndex].get(1));
        if (singleRecolorOutput != null) {
            return GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, items[0]))
                .itemOutputs(singleRecolorOutput)
                .fluidInputs(new FluidStack(fluids[0].getFluid(), (int) L))
                .duration(32)
                .eut(2)
                .hidden()
                .build()
                .map(this::compileRecipe)
                .orElse(null);
        }

        return null;
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return true;
    }

    @Override
    public boolean containsInput(Fluid fluid) {
        return super.containsInput(fluid) || Dyes.isAnyFluidDye(fluid);
    }
}
