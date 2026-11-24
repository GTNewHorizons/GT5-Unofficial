package gregtech.api.ec.component;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.common.tileentities.machines.RecipeCheckReason;

public interface IRecipeLogicComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IRecipeLogicComponent> KEY = ComponentKey.unique();

    @Nullable
    RecipeMap<?> getRecipeMap();

    long getEUt();

    void setEUt(long eut);

    int getProgressTime();

    void setProgressTime(int progressTime);

    int getMaxProgressTime();

    void setMaxProgressTime(int maxProgressTime);

    int getRuntime();

    long getLastWorkingTick();

    @NotNull
    CheckRecipeResult getCheckRecipeResult();

    void setCheckRecipeResult(@NotNull CheckRecipeResult result);

    void scheduleRecipeCheck(RecipeCheckReason reason);

    @Nullable
    ItemStack[] getOutputItems();

    void setOutputItems(@Nullable ItemStack[] outputItems);

    @Nullable
    FluidStack[] getOutputFluids();

    void setOutputFluids(@Nullable FluidStack[] outputFluids);

    boolean addPendingOutputs(@NotNull ItemStack[] outputItems);

    boolean addPendingOutputs(@NotNull FluidStack[] outputFluids);

    boolean hasPendingOutputs();

    long getMaxAllowedRecipeEUt();

    long getRecipesDone();

    boolean wasRunningOnLoad();

    void clearRunningOnLoad();
}
