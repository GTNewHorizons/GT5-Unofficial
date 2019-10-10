package kekztech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public class MultiFluidHandler {
	
	public static final int MAX_DISTINCT_FLUIDS = 25;
	
	private final List<FluidStack> fluids = new ArrayList<>(MAX_DISTINCT_FLUIDS);
	private final int capacityPerFluid;
	
	private boolean locked = true;
	
	public MultiFluidHandler(int capacityPerFluid) {
		this.capacityPerFluid = capacityPerFluid;
	}
	
	public MultiFluidHandler(int capacityPerFluid, List<FluidStack> fluids) {
		this.capacityPerFluid = capacityPerFluid;
		this.fluids.addAll(fluids);
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
	
	public boolean contains(FluidStack fluid) {
		return !locked && fluids.contains(fluid);
	}
	
	public int getCapacity() {
		return capacityPerFluid;
	}
	
	public List<FluidStack> getFluids(){
		return (!locked) ? fluids : new ArrayList<FluidStack>();
	}
	
	public FluidStack getFluid(int slot) {
		return (!locked && fluids.size() > 0 && slot >= 0 && slot < MAX_DISTINCT_FLUIDS) 
				? fluids.get(slot) : null;
	}
	
	public NBTTagCompound getAsNBTTag(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		int c = 0;
		for(FluidStack f : fluids) {
			nbt.setTag("" + c, f.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
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
		if(fluids.size() == MAX_DISTINCT_FLUIDS && !contains(push)) {
			return 0;
		} else if (fluids.size() < MAX_DISTINCT_FLUIDS && !contains(push)) {
			// Add new fluid
			final int remcap = getCapacity();
			final int fit = Math.min(remcap, push.amount);
			if(doPush) {
				fluids.add(new FluidStack(push.getFluid(), fit));	
			}
			return fit;
		} else {
			// Add to existing fluid
			final FluidStack fs = fluids.get(fluids.indexOf(push));
			final int remcap = getCapacity() - fs.amount;
			final int fit = Math.min(remcap, push.amount);
			if(doPush) {
				fs.amount += fit;				
			}
			return fit;
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
		if(slot < 0 || slot >= MAX_DISTINCT_FLUIDS) {
			return 0;
		}
		if(!fluids.get(slot).equals(push)) {
			return 0;
		} else {
			final FluidStack fs = fluids.get(slot);
			final int remcap = getCapacity() - fs.amount;
			final int fit = Math.min(remcap, push.amount);
			if(doPush) {
				fs.amount += fit;				
			}
			return fit;
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
		if(locked) {
			return 0;
		}
		if(!contains(pull)) {
			return 0;
		} else {
			final FluidStack src = fluids.get(fluids.indexOf(pull));
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
		if(slot < 0 || slot >= MAX_DISTINCT_FLUIDS) {
			return 0;
		}
		if(!fluids.get(slot).equals(pull)) {
			return 0;
		} else {
			final FluidStack pulled = fluids.get(slot);
			final int rec = Math.min(pull.amount, pulled.amount);
			if(doPull) {
				pulled.amount -= rec;
			}
			if(pulled.amount == 0) {
				fluids.remove(pulled);
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
		if(fluids.size() == MAX_DISTINCT_FLUIDS && !contains(push)) {
			return false;
		} else if (fluids.size() < MAX_DISTINCT_FLUIDS && !contains(push)) {
			return Math.min(getCapacity(), push.amount) > 0;
		} else {
			final int remcap = getCapacity() - fluids.get(fluids.indexOf(push)).amount;
			return Math.min(remcap, push.amount) > 0;
		}
	}
}
