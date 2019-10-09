package kekztech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public class MultiFluidHandler {
	
	private static final int MAX_DISTINCT_FLUIDS = 25;
	
	private final List<FluidStack> fluids = new ArrayList<>();
	private final int capacityPerFluid;
	
	public MultiFluidHandler(int capacityPerFluid) {
		this.capacityPerFluid = capacityPerFluid;
	}
	
	public MultiFluidHandler(int capacityPerFluid, List<FluidStack> fluids) {
		this.capacityPerFluid = capacityPerFluid;
		this.fluids.addAll(fluids);
	}
	
	public boolean contains(FluidStack fluid) {
		return fluids.contains(fluid);
	}
	
	public int getCapacity() {
		return capacityPerFluid;
	}
	
	public List<FluidStack> getFluids(){
		return fluids;
	}
	
	public FluidStack getFluid(int slot) {
		return fluids.get(slot);
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
		final ArrayList<String> lines = new ArrayList<>(fluids.size() + 1);
		lines.add(EnumChatFormatting.YELLOW + "Stored Fluids:" + EnumChatFormatting.RESET);
		for(int i = 0; i < fluids.size(); i++) {
			lines.add(i + " - " + fluids.get(i).getLocalizedName() + ": " 
					+ fluids.get(i).amount + "L (" 
					+ (Math.round(100.0f * fluids.get(i).amount / getCapacity())) + "%)");
		}
		lines.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);
		
		return lines;
	}
	
	public int pushFluid(FluidStack push) {
		if(fluids.size() == MAX_DISTINCT_FLUIDS && !contains(push)) {
			return 0;
		} else if (fluids.size() < MAX_DISTINCT_FLUIDS && !contains(push)) {
			final int fit = Math.min(getCapacity(), push.amount);
			fluids.add(new FluidStack(push.getFluid(), fit));
			return fit;
		} else {
			final FluidStack fs = fluids.get(fluids.indexOf(push));
			final int remcap = getCapacity() - fs.amount;
			final int fit = Math.min(remcap, push.amount);
			fs.amount += fit;
			return fit;
		}
	}
	
	public int pushFluid(FluidStack push, int slot) {
		if(slot < 0 || slot >= MAX_DISTINCT_FLUIDS) {
			return 0;
		}
		if(!fluids.get(slot).equals(push)) {
			return 0;
		} else {
			final FluidStack fs = fluids.get(slot);
			final int remcap = getCapacity() - fs.amount;
			final int fit = Math.min(remcap, push.amount);
			fs.amount += fit;
			return fit;
		}
	}
	
	public int pullFluid(FluidStack pull) {
		if(!contains(pull)) {
			return 0;
		} else {
			final FluidStack pulled = fluids.get(fluids.indexOf(pull));
			final int rec = Math.min(pull.amount, pulled.amount);
			if(pulled.amount <= rec) {
				fluids.remove(pulled);
			} else {
				pulled.amount -= rec;
			}
			return rec;
		}
	}
	
	public int pullFluid(FluidStack pull, int slot) {
		if(slot < 0 || slot >= MAX_DISTINCT_FLUIDS) {
			return 0;
		}
		if(!fluids.get(slot).equals(pull)) {
			return 0;
		} else {
			final FluidStack pulled = fluids.get(slot);
			final int rec = Math.min(pull.amount, pulled.amount);
			if(pulled.amount <= rec) {
				fluids.remove(pulled);
			} else {
				pulled.amount -= rec;
			}
			return rec;
		}
	}
	
	public boolean couldPush(FluidStack push) {
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
