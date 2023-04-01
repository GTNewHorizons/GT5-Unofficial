package gregtech.api.logic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public abstract class ProcessingLogic {

    protected GT_Recipe_Map recipeMap;
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected ItemStack[] currentOutputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected FluidStack[] currentOutputFluids;
    protected long eut;
    protected long duration;

    public ProcessingLogic() {}

    public ProcessingLogic setInputItems(ItemStack... itemInputs) {
        this.inputItems = itemInputs;
        return this;
    }

    public ProcessingLogic setInputFluids(FluidStack... fluidInputs) {
        this.inputFluids = fluidInputs;
        return this;
    }

    public ProcessingLogic setOutputItems(ItemStack... itemOutputs) {
        this.outputItems = itemOutputs;
        return this;
    }

    public ProcessingLogic setOutputFluids(FluidStack... fluidOutputs) {
        this.outputFluids = fluidOutputs;
        return this;
    }

    public ProcessingLogic setCurrentOutputItems(ItemStack... currentOutputItems) {
        this.currentOutputItems = currentOutputItems;
        return this;
    }

    public ProcessingLogic setCurrentOutputFluids(FluidStack... currentOutputFluids) {
        this.currentOutputFluids = currentOutputFluids;
        return this;
    }

    public ProcessingLogic setRecipeMap(GT_Recipe_Map recipeMap) {
        this.recipeMap = recipeMap;
        return this;
    }

    public ProcessingLogic setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public ProcessingLogic setEut(long eut) {
        this.eut = eut;
        return this;
    }

    /**
     * Clears everything stored in the Processing Logic other than the Recipe map used
     */
    public ProcessingLogic clear() {
        this.inputItems = null;
        this.inputFluids = null;
        this.outputItems = null;
        this.outputFluids = null;
        this.eut = 0;
        this.duration = 0;
        return this;
    }

    public abstract boolean process();

    public ItemStack[] getOutputItems() {
        return outputItems;
    }

    public FluidStack[] getOutputFluids() {
        return outputFluids;
    }

    public long getDuration() {
        return duration;
    }

    public long getEut() {
        return eut;
    }
}
