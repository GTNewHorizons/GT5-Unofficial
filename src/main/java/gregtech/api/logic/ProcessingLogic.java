package gregtech.api.logic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public abstract class ProcessingLogic {

    protected GT_Recipe_Map recipeMap;
    protected ItemStack[] inputItems;
    protected ItemStack[] outputItems;
    protected ItemStack[] currentOutputItems;
    protected FluidStack[] inputFluids;
    protected FluidStack[] outputFluids;
    protected FluidStack[] currentOutputFluids;
    protected long voltage;
    protected long ampere;
    protected long eut;
    protected long duration;
    protected boolean isCleanroom, voidProtection, perfectOverclock;
    protected Controller<?> controller;
    protected int maxParallel = 1;

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

    public ProcessingLogic setIsCleanroom(boolean isCleanroom) {
        this.isCleanroom = isCleanroom;
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

    public ProcessingLogic setAmperage(long ampere) {
        this.ampere = ampere;
        return this;
    }

    public ProcessingLogic setVoltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    public ProcessingLogic setController(Controller<?> controller) {
        this.controller = controller;
        return this;
    }

    public ProcessingLogic setVoidProtection(boolean voidProtection) {
        this.voidProtection = voidProtection;
        return this;
    }

    public ProcessingLogic setPerfectOverclock(boolean perfectOverclock) {
        this.perfectOverclock = perfectOverclock;
        return this;
    }

    public ProcessingLogic setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
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
        this.voltage = 0;
        this.ampere = 0;
        this.controller = null;
        this.maxParallel = 1;
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
