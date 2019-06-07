package kekztech;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GTRecipe {
	
	private int euPerTick = 0;
	private int duration = 0;
	
	private ArrayList<ItemStack> inputItems;
	private ArrayList<FluidStack> inputFluids;
	private ArrayList<ItemStack> outputItems;
	private ArrayList<FluidStack> outputFluids;
	
	public int getEuPerTick() {
		return euPerTick;
	}
	public int getDuration() {
		return duration;
	}
	public ItemStack[] getInputItems() {
		return Util.toItemStackArray(inputItems);
	}
	public FluidStack[] getInputFluids() {
		return Util.toFluidStackArray(inputFluids);
	}
	public ItemStack[] getOutputItems() {
		return Util.toItemStackArray(outputItems);
	}
	public FluidStack[] getOutputFluids() {
		return Util.toFluidStackArray(outputFluids);
	}
	public ItemStack getInputItem() {
		final ItemStack[] s = Util.toItemStackArray(inputItems);
		return s[0];
	}
	public FluidStack getInputFluid() {
		final FluidStack[] s = Util.toFluidStackArray(inputFluids);
		return s[0];
	}
	public ItemStack getOutputItem() {
		final ItemStack[] s = Util.toItemStackArray(outputItems);
		return s[0];
	}
	public FluidStack getOutputFluid() {
		final FluidStack[] s = Util.toFluidStackArray(outputFluids);
		return s[0];
	}
	
	public GTRecipe addInputItem(ItemStack inputItem) {
		if(inputItems == null) {
			inputItems = new ArrayList<ItemStack>();
			inputItems.add(inputItem);
		} else {
			inputItems.add(inputItem);
		}
		return this;
	}
	
	public GTRecipe addOutputItem(ItemStack outputItem) {
		if(outputItems == null) {
			outputItems = new ArrayList<ItemStack>();
			outputItems.add(outputItem);
		} else {
			outputItems.add(outputItem);
		}
		return this;
	}
	
	public GTRecipe addInputFluid(FluidStack inputFluid) {
		if(inputFluids == null) {
			inputFluids = new ArrayList<FluidStack>();
			inputFluids.add(inputFluid);
		} else {
			inputFluids.add(inputFluid);
		}
		return this;
	}
	
	public GTRecipe addOutputFluid(FluidStack outputFluid) {
		if(outputFluids == null) {
			outputFluids = new ArrayList<FluidStack>();
			outputFluids.add(outputFluid);
		} else {
			outputFluids.add(outputFluid);
		}
		return this;
	}
	
	public GTRecipe setEUPerTick(int euPerTick) {
		this.euPerTick = euPerTick;
		return this;
	}
	
	public GTRecipe setDuration(int duration) {
		this.duration = duration;
		return this;
	}
	
}
