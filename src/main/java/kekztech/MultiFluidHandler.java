package kekztech;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiFluidHandler {

	private final List<FluidStack> fluids;
	private final int maxDistinctFluids;
	private final int capacityPerFluid;
	
	private boolean locked = true;
	private boolean doVoidExcess = false;
	private byte fluidSelector = -1;
	
	private MultiFluidHandler(int maxDistinctFluids, int capacityPerFluid, List<FluidStack> fluidsToAdd) {
		this.maxDistinctFluids = maxDistinctFluids;
		this.fluids = new ArrayList<>(maxDistinctFluids);
		if(fluidsToAdd != null) {
			this.fluids.addAll(fluidsToAdd);
		}
		this.capacityPerFluid = capacityPerFluid;
	}

	/**
	 * Initialize a new MultiFluidHandler object with the given parameters
	 * @param maxDistinctFluids
	 * 				How many different fluids can be stored
	 * @param capacityPerFluid
	 * 				How much capacity each fluid should have
	 * @param fluidsToAdd
	 * 				Fluids to add immediately
	 * @return
	 * 				A new instance
	 */
	public static MultiFluidHandler newInstance(int maxDistinctFluids, int capacityPerFluid, FluidStack...fluidsToAdd) {
		return new MultiFluidHandler(maxDistinctFluids, capacityPerFluid, Arrays.asList(fluidsToAdd));
	}

	/**
	 * Deep copy a MultiFluidHandler instance with a new capacity
	 * @param toCopy
	 * 				The MultiFluidHandler that should be copied
	 * @param capacityPerFluid
	 * 				How much capacity each fluid should have
	 * @return
	 * 				A new instance
	 */
	public static MultiFluidHandler newAdjustedInstance(MultiFluidHandler toCopy, int capacityPerFluid) {
		return new MultiFluidHandler(toCopy.maxDistinctFluids, capacityPerFluid, toCopy.fluids);
	}
	
	/**
	 * Lock internal tanks in case T.F.F.T is not running.
	 * 
	 * @param state
	 * 				Lock state.
	 */
	public void setLock(boolean state) {
		locked = state;
	}

	public void setDoVoidExcess(boolean doVoidExcess) { this.doVoidExcess = doVoidExcess; }

	/**
	 * Used to tell the MFH if a fluid is selected by
	 * an Integrated Circuit in the controller.
	 * If the Integrate Circuit configuration exceeds
	 * the number of stored fluid, the configuration will be ignored.
	 * 
	 * @param fluidSelector
	 * 				Selected fluid or -1 if no fluid is selected
	 */
	public void setFluidSelector(byte fluidSelector) {
		this.fluidSelector = fluidSelector < fluids.size() ? fluidSelector : -1;
	}
	
	/**
	 * 
	 * @return
	 * 				Selected fluid or -1 if no fluid is selected
	 */
	public byte getSelectedFluid() {
		return fluidSelector;
	}
	
	public boolean contains(FluidStack fluid) {
		return !locked && fluids.contains(fluid);
	}
	
	public int getCapacity() {
		return capacityPerFluid;
	}

	/**
	 * Returns a deep copy of the the FluidStack in the requested slot
	 * @param slot
	 * 				requested slot
	 * @return
	 * 				deep copy of the requested FluidStack
	 */
	public FluidStack getFluidCopy(int slot) {
		return (!locked && fluids.size() > 0 && slot >= 0 && slot < maxDistinctFluids)
				? fluids.get(slot).copy() : null;
	}

	/**
	 * Returns the amount of different fluids currently stored.
	 * @return
	 * 				amount of different fluids currently stored (0-25)
	 */
	public int getDistinctFluids() {
		return fluids.size();
	}

	/**
	 * Helper method to save a MultiFluidHandler to NBT data
	 * @param nbt
	 * 				The NBT Tag to write to
	 * @return
	 * 				Updated NBT Tag
	 */
	public NBTTagCompound saveNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
		nbt.setInteger("capacityPerFluid", getCapacity());
		int c = 0;
		for(FluidStack f : fluids) {
			nbt.setTag("" + c, f.writeToNBT(new NBTTagCompound()));
			c++;
		}
		return nbt;
	}

	/**
	 * Helper method to initialize a MultiFluidHandler from NBT data
	 * @param nbt
	 * 				The NBT Tag to read from
	 * @return
	 * 				A new Instance
	 */
	public MultiFluidHandler loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
		final int capacityPerFluid = nbt.getInteger("capacityPerFluid");
		final NBTTagCompound fluidsTag = (NBTTagCompound) nbt.getTag("fluids");
		final ArrayList<FluidStack> loadedFluids = new ArrayList<>();
		int distinctFluids = 0;
		while(true) {
			final NBTTagCompound fluidNBT = (NBTTagCompound) fluidsTag.getTag("" + distinctFluids);
			if(fluidNBT == null) {
				break;
			}
			loadedFluids.add(FluidStack.loadFluidStackFromNBT(fluidNBT));
			distinctFluids++;
		}
		return new MultiFluidHandler(distinctFluids, capacityPerFluid, loadedFluids);
	}
	
	public ArrayList<String> getInfoData() {
		final ArrayList<String> lines = new ArrayList<>(fluids.size());
		lines.add(EnumChatFormatting.YELLOW + "Stored Fluids:" + EnumChatFormatting.RESET);
		for(int i = 0; i < fluids.size(); i++) {
			lines.add(i + " - " + fluids.get(i).getLocalizedName() + ": " 
					+ fluids.get(i).amount + "L (" 
					+ (Math.round(100.0f * fluids.get(i).amount / getCapacity())) + "%)");
		}
		
		return lines;
	}
	
	/**
	 * Fill fluid into a tank.
	 * 
	 * @param push
	 * 				Fluid type and quantity to be inserted.
	 * @param doPush
	 * 				If false, fill will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) filled.
	 */
	public int pushFluid(FluidStack push, boolean doPush) {
		if(locked) {
			return 0;
		}
		if(fluids.size() == maxDistinctFluids && !contains(push)) {
			// Already contains 25 fluids and this isn't one of them
			return 0;
		} else if (fluids.size() < maxDistinctFluids && !contains(push)) {
			// Add new fluid
			final int fit = Math.min(getCapacity(), push.amount);
			if(doPush) {
				fluids.add(new FluidStack(push.getFluid(), fit));	
			}
			// If doVoidExcess, pretend all of it fit
			return doVoidExcess ? push.amount : fit;
		} else {
			// Add to existing fluid
			final FluidStack existing = fluids.get(fluids.indexOf(push));
			final int fit = Math.min(getCapacity() - existing.amount, push.amount);
			if(doPush) {
				existing.amount += fit;
			}
			// If doVoidExcess, pretend all of it fit
			return doVoidExcess ? push.amount : fit;
		}
	}
	
	/**
	 * Fill fluid into the specified tank.
	 * 
	 * @param push
	 * 				Fluid type and quantity to be inserted.
	 * @param slot
	 * 				Tank the fluid should go into.
	 * @param doPush
	 * 				If false, fill will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) filled.
	 */
	public int pushFluid(FluidStack push, int slot, boolean doPush) {
		if(locked) {
			return 0;
		}
		if(slot < 0 || slot >= maxDistinctFluids) {
			// Invalid slot
			return 0;
		}
		if((fluids.get(slot) != null) && !fluids.get(slot).equals(push)) {
			// Selected slot is taken by a non-matching fluid
			return 0;
		} else {
			// Add to existing fluid
			final FluidStack existing = fluids.get(slot);
			final int fit = Math.min(getCapacity() - existing.amount, push.amount);
			if(doPush) {
				existing.amount += fit;
			}
			// If doVoidExcess, pretend all of it fit
			return doVoidExcess ? push.amount : fit;
		}
	}
	
	/**
	 * Drains fluid out of the internal tanks.
	 *  
	 * @param pull
	 * 				Fluid type and quantity to be pulled.
	 * @param doPull
	 * 				If false, drain will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) pulled.
	 */
	public int pullFluid(FluidStack pull, boolean doPull) {
		if (locked || !contains(pull)) {
			return 0;
		} else {
			final FluidStack src = fluids.get(fluids.indexOf(pull));
			final int rec = Math.min(pull.amount, src.amount);
			if (doPull) {
				src.amount -= rec;
			}
			if (src.amount == 0) {
				fluids.remove(src);
			}
			return rec;
		}
	}
	
	/**
	 * Drains fluid out of the specified internal tank.
	 *  
	 * @param pull
	 * 				Fluid type and quantity to be pulled.
	 * @param slot
	 * 				Tank fluid should be drained from.
	 * @param doPull
	 * 				If false, drain will only be simulated.
	 * @return Amount of fluid that was (or would have been, if simulated) pulled.
	 */
	public int pullFluid(FluidStack pull, int slot, boolean doPull) {
		if(locked) {
			return 0;
		}
		if(slot < 0 || slot >= maxDistinctFluids) {
			return 0;
		}
		if(!fluids.get(slot).equals(pull)) {
			return 0;
		} else {
			final FluidStack src = fluids.get(slot);
			final int rec = Math.min(pull.amount, src.amount);
			if(doPull) {
				src.amount -= rec;
			}
			if(src.amount == 0) {
				fluids.remove(src);
			}
			return rec;
		}
	}
	
	/**
	 * Test whether the given fluid type and quantity can be inserted into the internal tanks.
	 * @param push
	 * 				Fluid type and quantity to be tested
	 * @return True if there is sufficient space
	 */
	public boolean couldPush(FluidStack push) {
		if(locked) {
			return false;
		}
		if(fluids.size() == maxDistinctFluids && !contains(push)) {
			return false;
		} else if (fluids.size() < maxDistinctFluids && !contains(push)) {
			return Math.min(getCapacity(), push.amount) > 0;
		} else {
			final int remcap = getCapacity() - fluids.get(fluids.indexOf(push)).amount;
			return doVoidExcess || (Math.min(remcap, push.amount) > 0);
		}
	}
}
