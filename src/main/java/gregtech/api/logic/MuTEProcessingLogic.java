package gregtech.api.logic;

import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gtnewhorizons.mutecore.MuTECore;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.registry.MultiTileContainer.FakeEntity;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Entity;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Results;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Results.With1;

/**
 * Processing logic class, dedicated for MultiTileEntities.
 */
public abstract class MuTEProcessingLogic<P extends MuTEProcessingLogic<P>> implements Runnable {

    public final void run() {
        Results<? extends With1<?>> results = MuTECore.ENGINE.findEntitiesWith(getProcessingDataClass());
        for (With1<?> result : results) {
            Entity entity = result.entity();
            if (entity.has(FakeEntity.class)) continue;
            if (!validateEntityComponents(entity)) continue;
            process(entity);
        }
    }

    protected abstract Class<?> getProcessingDataClass();

    protected boolean validateEntityComponents(@Nonnull Entity entity) {
        return true;
    }

    // #region Logic

    @Nonnull
<<<<<<< HEAD
    @Override
    public CheckRecipeResult process() {
        RecipeMap<?> recipeMap = preProcess();

        ItemInventoryLogic itemInput = null;
        FluidInventoryLogic fluidInput = null;
        if (machineHost.isInputSeparated()) {
            for (Map.Entry<UUID, ItemInventoryLogic> itemEntry : machineHost
                .getAllItemInventoryLogics(InventoryType.Input)) {
                itemOutputID = Objects.requireNonNull(itemEntry.getKey());
                itemInput = Objects.requireNonNull(itemEntry.getValue());
                fluidInput = Objects.requireNonNull(
                    machineHost.getFluidLogic(InventoryType.Input, itemInput.getConnectedFluidInventoryID()));
                fluidOutputID = itemInput.getConnectedFluidInventoryID();
            }
        } else {
            itemInput = Objects.requireNonNull(machineHost.getItemLogic(InventoryType.Input, null));
            fluidInput = Objects.requireNonNull(machineHost.getFluidLogic(InventoryType.Input, null));
        }

        CheckRecipeResult recipeValidatorResult = null;
        if (recipeValidatorResult != null) {
            return recipeValidatorResult;
        }

        return processRecipe(null, Objects.requireNonNull(itemInput), Objects.requireNonNull(fluidInput));
    }

    @Nonnull
    protected CheckRecipeResult processRecipe(@Nonnull List<GTRecipe> recipes, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        CheckRecipeResult result = CheckRecipeResultRegistry.INTERNAL_ERROR;
        for (GTRecipe recipe : recipes) {
            Objects.requireNonNull(recipe);
            ParallelHelper helper = createParallelHelper(recipe, itemInput, fluidInput);
            OverclockCalculator calculator = createOverclockCalculator(recipe);
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
    protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe, @Nonnull ItemInventoryLogic itemInput,
        @Nonnull FluidInventoryLogic fluidInput) {
        return new ParallelHelper().setRecipe(recipe)
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
        return !hasWork && machineHost.isAllowedToWork();
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
                itemOutputsNBT.appendTag(GTUtility.saveItem(item));
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
                outputItems[i] = GTUtility.loadItem(itemOutputsNBT.getCompoundTagAt(i));
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
=======
    public void process(@Nonnull Entity entity) {
>>>>>>> ddc948295b (initial setup of structures)
    }

    // #endregion
}
