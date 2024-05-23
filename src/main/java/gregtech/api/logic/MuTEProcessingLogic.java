package gregtech.api.logic;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Processing logic class, dedicated for MultiTileEntities.
 */
public class MuTEProcessingLogic<P extends MuTEProcessingLogic<P>> extends AbstractProcessingLogic<P> {

    protected boolean hasWork;
    protected int progress;
    protected ProcessingLogicHost<P> machineHost;
    @Nonnull
    protected CheckRecipeResult recipeResult = CheckRecipeResultRegistry.NONE;
    @Nullable
    protected UUID itemOutputID;
    @Nullable
    protected UUID fluidOutputID;

    public P setMachineHost(@Nonnull ProcessingLogicHost<P> machineHost) {
        this.machineHost = machineHost;
        return getThis();
    }

    // #region Logic

    @Nonnull
    @Override
    public CheckRecipeResult process() {
        return CheckRecipeResultRegistry.NO_RECIPE;/*
                                                    * RecipeMap<?> recipeMap = preProcess();
                                                    * ItemInventoryLogic itemInput = null;
                                                    * FluidInventoryLogic fluidInput = null;
                                                    * if (machineHost.isInputSeparated()) {
                                                    * for (Map.Entry<UUID, ItemInventoryLogic> itemEntry : machineHost
                                                    * .getAllItemInventoryLogics(InventoryType.Input)) {
                                                    * itemOutputID = Objects.requireNonNull(itemEntry.getKey());
                                                    * itemInput = Objects.requireNonNull(itemEntry.getValue());
                                                    * fluidInput = Objects.requireNonNull(
                                                    * machineHost.getFluidLogic(InventoryType.Input,
                                                    * itemInput.getConnectedFluidInventoryID()));
                                                    * fluidOutputID = itemInput.getConnectedFluidInventoryID();
                                                    * }
                                                    * } else {
                                                    * itemInput =
                                                    * Objects.requireNonNull(machineHost.getItemLogic(InventoryType.
                                                    * Input, null));
                                                    * fluidInput =
                                                    * Objects.requireNonNull(machineHost.getFluidLogic(InventoryType.
                                                    * Input, null));
                                                    * }
                                                    * CheckRecipeResult recipeValidatorResult = null;
                                                    * if (recipeValidatorResult != null) {
                                                    * return recipeValidatorResult;
                                                    * }
                                                    * return processRecipe(null, Objects.requireNonNull(itemInput),
                                                    * Objects.requireNonNull(fluidInput));
                                                    */
    }

    @Nonnull
    protected CheckRecipeResult processRecipe(@Nonnull List<GT_Recipe> recipes, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        CheckRecipeResult result = CheckRecipeResultRegistry.INTERNAL_ERROR;
        for (GT_Recipe recipe : recipes) {
            Objects.requireNonNull(recipe);
            GT_ParallelHelper helper = createParallelHelper(recipe, itemInput, fluidInput);
            GT_OverclockCalculator calculator = createOverclockCalculator(recipe);
            helper.setCalculator(calculator);
            helper.build();
            result = helper.getResult();
            if (result.wasSuccessful()) {
                return applyRecipe(recipe, helper, calculator, result);
            }
        }
        return result;
    }

    /**
     * Override if you don't work with regular gt recipe maps
     */
    @Nonnull
    protected Object findRecipe(@Nullable RecipeMap<?> map, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        if (map == null) {
            return false;
        }

        return true;
    }

    @Nonnull
    protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        return new GT_ParallelHelper().setRecipe(recipe)
            .setItemInputInventory(itemInput)
            .setFluidInputInventory(fluidInput)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMaxParallel(maxParallel)
            .setEUtModifier(euModifier)
            .enableBatchMode(batchSize)
            .setConsumption(true)
            .setOutputCalculation(true)
            .setMuTEMode(true);
    }

    // #endregion

    // #region Getters

    @Nonnull
    public CheckRecipeResult getResult() {
        return recipeResult;
    }

    public int getProgress() {
        return progress;
    }

    // #endregion

    // #region Other

    public void startCheck() {
        recipeResult = process();
    }

    public void progress() {
        if (!hasWork) return;
        if (progress == duration) {
            progress = 0;
            duration = 0;
            calculatedEut = 0;
            output();
            return;
        }
        progress++;
    }

    protected void output() {
        ItemInventoryLogic itemOutput = machineHost.getItemLogic(InventoryType.Output, itemOutputID);
        FluidInventoryLogic fluidOutput = machineHost.getFluidLogic(InventoryType.Output, fluidOutputID);
        if (itemOutput == null || fluidOutput == null) return;
        for (ItemStack item : outputItems) {
            if (item == null) continue;
            itemOutput.insertItem(item);
        }
        for (FluidStack fluid : outputFluids) {
            if (fluid == null) continue;
            fluidOutput.fill(fluid.getFluid(), fluid.amount, false);
        }
        outputItems = new ItemStack[0];
        outputFluids = new FluidStack[0];
    }

    public boolean canWork() {
        return !hasWork;// && machineHost.isAllowedToWork();
    }

    /**
     * By how much to increase the progress?
     *
     * @param progressAmount in ticks
     */
    public void increaseProgress(int progressAmount) {
        progress += progressAmount;
    }

    public NBTTagCompound saveToNBT() {
        NBTTagCompound logicNBT = new NBTTagCompound();
        logicNBT.setLong("eutConsumption", calculatedEut);
        logicNBT.setInteger("duration", duration);
        logicNBT.setInteger("progress", progress);
        logicNBT.setBoolean("hasWork", hasWork);
        if (outputItems != null) {
            NBTTagList itemOutputsNBT = new NBTTagList();
            for (ItemStack item : outputItems) {
                itemOutputsNBT.appendTag(GT_Utility.saveItem(item));
            }
            logicNBT.setTag("itemOutputs", itemOutputsNBT);
        }
        if (outputFluids != null) {
            NBTTagList fluidOutputsNBT = new NBTTagList();
            for (FluidStack fluid : outputFluids) {
                fluidOutputsNBT.appendTag(fluid.writeToNBT(new NBTTagCompound()));
            }
            logicNBT.setTag("fluidOutputs", fluidOutputsNBT);
        }
        if (itemOutputID != null) {
            logicNBT.setString("itemOutputID", itemOutputID.toString());
        }
        if (fluidOutputID != null) {
            logicNBT.setString("fluidOutputID", fluidOutputID.toString());
        }
        return logicNBT;
    }

    public void loadFromNBT(@Nonnull NBTTagCompound logicNBT) {
        calculatedEut = logicNBT.getLong("eutConsumption");
        duration = logicNBT.getInteger("duration");
        progress = logicNBT.getInteger("progress");
        hasWork = logicNBT.getBoolean("hasWork");
        if (logicNBT.hasKey("itemOutputs")) {
            NBTTagList itemOutputsNBT = logicNBT.getTagList("itemOutputs", TAG_COMPOUND);
            outputItems = new ItemStack[itemOutputsNBT.tagCount()];
            for (int i = 0; i < itemOutputsNBT.tagCount(); i++) {
                outputItems[i] = GT_Utility.loadItem(itemOutputsNBT.getCompoundTagAt(i));
            }
        }
        if (logicNBT.hasKey("fluidOutputs")) {
            NBTTagList fluidOutputsNBT = logicNBT.getTagList("fluidOutputs", TAG_COMPOUND);
            outputFluids = new FluidStack[fluidOutputsNBT.tagCount()];
            for (int i = 0; i < fluidOutputsNBT.tagCount(); i++) {
                outputFluids[i] = FluidStack.loadFluidStackFromNBT(fluidOutputsNBT.getCompoundTagAt(i));
            }
        }
        if (logicNBT.hasKey("itemOutputID")) {
            itemOutputID = UUID.fromString(logicNBT.getString("itemOutputID"));
        }
        if (logicNBT.hasKey("fluidOutputID")) {
            fluidOutputID = UUID.fromString(logicNBT.getString("fluidOutputID"));
        }
    }

    /**
     * Returns a gui part, which will be displayed in a separate tab on the machine's gui.
     */
    @Nonnull
    public Widget getGUIPart(ModularWindow.Builder builder) {
        return new Scrollable();
    }

    // #endregion
}
