package kekztech;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiFluidHandler {

	private final FluidStack[] fluids;
	private final int maxDistinctFluids;
	private final int capacityPerFluid;
	
	private boolean locked = true;
	private boolean doVoidExcess = false;
	private byte fluidSelector = -1;
	
	public MultiFluidHandler(int maxDistinctFluids, int capacityPerFluid, FluidStack[] fluidsToAdd) {
		this.maxDistinctFluids = maxDistinctFluids;
		this.fluids = new FluidStack[maxDistinctFluids];
		if(fluidsToAdd != null) {
			int tFluidLengt = (maxDistinctFluids <fluidsToAdd.length) ? maxDistinctFluids:fluidsToAdd.length;
			for (int i = 0; i < tFluidLengt; i++) {
				this.fluids[i] = fluidsToAdd[i];
			}
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
		return new MultiFluidHandler(maxDistinctFluids, capacityPerFluid, fluidsToAdd);
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
		this.fluidSelector = fluidSelector < fluids.length ? fluidSelector : -1;
	}
	
	/**
	 * 
	 * @return
	 * 				Selected fluid or -1 if no fluid is selected
	 */
	public byte getSelectedFluid() {
		return fluidSelector;
	}

	public FluidStack[] getAllFluids() {
		return fluids;
	}

	public int getFluidPosistion(FluidStack aFluid) {

		for (int i = 0; i < fluids.length; i++)
		{
			FluidStack tFluid = fluids[i];
			if (tFluid != null && tFluid.isFluidEqual(aFluid))
				return i;
		}
		return -1;
	}

	public boolean contains(FluidStack aFluid) {
		if (locked)
			return false;
		return getFluidPosistion(aFluid)>=0;
	}

	public int countFluids()
	{
		int tCount = 0;
		for (int i = 0; i < fluids.length; i++) {
			if (fluids[i] != null)
				tCount++;
		}
		return tCount;
	}

	
	public int getCapacity() {
		return capacityPerFluid;
	}
	public int getMaxDistinctFluids() {
		return maxDistinctFluids;
	}

	/**
	 * Returns a deep copy of the the FluidStack in the requested slot
	 * @param slot
	 * 				requested slot
	 * @return
	 * 				deep copy of the requested FluidStack
	 */
	public FluidStack getFluidCopy(int slot) {
		if (slot >= fluids.length)
			return null;
		if (!locked
				&& fluids.length > 0
				&& slot >= 0
				&& slot < maxDistinctFluids)
		{
			FluidStack tFluid = fluids[slot];
			if (tFluid != null)
				return tFluid.copy();
		}
		return null;
	}

	/**
	 * Returns the amount of different fluids currently stored.
	 * @return
	 * 				amount of different fluids currently stored (0-25)
	 */
	public int getDistinctFluids() {
		int distinctFluids = 0;
		for (FluidStack f : fluids) {
			if (f != null)
				distinctFluids++;
		}
		return distinctFluids;
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
		nbt.setInteger("maxDistinctFluids",this.maxDistinctFluids);
		int c = 0;
		for(FluidStack f : fluids) {
			if (f == null)
			{
				c++;
				continue;
			}
			nbt.setTag( String.valueOf(c), f.writeToNBT(new NBTTagCompound()));
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
	static public MultiFluidHandler loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
		final int capacityPerFluid = nbt.getInteger("capacityPerFluid");
		final NBTTagCompound fluidsTag = (NBTTagCompound) nbt.getTag("fluids");
		int distinctFluids = nbt.getInteger("maxDistinctFluids");
		if (!nbt.hasKey("maxDistinctFluids"))
			distinctFluids = 25;// adding it so it doesent break on upgrading
		final FluidStack[] loadedFluids = new FluidStack[distinctFluids];

		if (fluidsTag != null)
		{
			for (int i = 0; i < distinctFluids; i++) {
				final NBTTagCompound fluidNBT = (NBTTagCompound) fluidsTag.getTag("" + i);
				if(fluidNBT == null) {
					loadedFluids[i] = null;
				} else {
					loadedFluids[i] = FluidStack.loadFluidStackFromNBT(fluidNBT);
				}
			}
		}
		return new MultiFluidHandler(distinctFluids, capacityPerFluid, loadedFluids);
	}
	
	public ArrayList<String> getInfoData() {
		final ArrayList<String> lines = new ArrayList<>(fluids.length);
		lines.add(EnumChatFormatting.YELLOW + "Stored Fluids:" + EnumChatFormatting.RESET);
		for(int i = 0; i < fluids.length; i++) {
			FluidStack tFluid = fluids[i];
			if (tFluid == null) {
				lines.add(i + " - " + "null" + ": "
						+ "0" + "L ("
						+ "0" + "%)");
			} else {
				lines.add(i + " - " + tFluid.getLocalizedName() + ": "
						+ tFluid.amount + "L ("
						+ (Math.round(100.0f * tFluid.amount / getCapacity())) + "%)");
			}
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
		int empty = getNullSlot();
		int fluidCount = countFluids();
		if(fluidCount >= maxDistinctFluids && !contains(push)) {
			// Already contains 25 fluids and this isn't one of them
			return 0;
		} else if (empty < maxDistinctFluids && !contains(push)) {
			// Add new fluid
			final int fit = Math.min(getCapacity(), push.amount);
			if(doPush) {
				if (empty == -1)
					return 0;
				else
					fluids[empty] = new FluidStack(push.getFluid(), fit);
			}
			// If doVoidExcess, pretend all of it fit
			return doVoidExcess ? push.amount : fit;
		} else {
			// Add to existing fluids
			int index = getFluidPosistion(push);
			if (index < 0)
				return 0;
			final FluidStack existing = fluids[index];
			final int fit = Math.min(getCapacity() - existing.amount, push.amount);
			if(doPush) {
				existing.amount += fit;
			}
			// If doVoidExcess, pretend all of it fit
			return doVoidExcess ? push.amount : fit;
		}
	}


	public int getNullSlot()
	{
		for (int i = 0; i < fluids.length; i++) {
			if (fluids[i] == null)
				return i;
		}
		return -1;
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
		FluidStack tFluid = fluids[slot];
		if(slot < 0 || slot >= maxDistinctFluids) {
			// Invalid slot
			return 0;
		}
		if((tFluid != null) && !tFluid.equals(push)) {
			// Selected slot is taken by a non-matching fluid
			return 0;
		} else {
			int fit = 0;
			// Add to existing fluid
			if (tFluid == null) {
				fit = Math.min(getCapacity(),push.amount);
				fluids[slot] = new FluidStack(push.getFluid(), fit);
			} else {
				fit = Math.min(getCapacity() - tFluid.amount, push.amount);
				if(doPush) {
					tFluid.amount += fit;
				}
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
		if (locked) {
			return 0;
		} else {
			int tIndex = getFluidPosistion(pull);
			if (tIndex < 0)
				return 0;
			FluidStack src = fluids[tIndex];
			final int rec = Math.min(pull.amount, src.amount);
			if (doPull) {
				src.amount -= rec;
			}
			if (src.amount == 0) {
				fluids[tIndex]= null;
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
		if(locked || slot >= fluids.length) {
			return 0;
		}
		if(slot < 0 || slot >= maxDistinctFluids) {
			return 0;
		}
		FluidStack tFluid = fluids[slot];
		if(tFluid == null || !tFluid.equals(pull)) {
			return 0;
		} else {
			final int rec = Math.min(pull.amount, tFluid.amount);
			if(doPull) {
				tFluid.amount -= rec;
			}
			if(tFluid.amount == 0) {
				fluids[slot] = null;
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
		int tFluidIndex = getFluidPosistion(push);
		int fluidCount = countFluids();
		if(fluidCount >= maxDistinctFluids && !contains(push)) {
			return false;
		} else if (fluidCount < maxDistinctFluids && !contains(push)) {
			return Math.min(getCapacity(), push.amount) > 0;
		} else {
			final int remcap = getCapacity() - fluids[tFluidIndex].amount;
			return doVoidExcess || (Math.min(remcap, push.amount) > 0);
		}
	}
}
