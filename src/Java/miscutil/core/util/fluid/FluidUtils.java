package miscutil.core.util.fluid;

import miscutil.core.util.Utils;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidUtils {

	public static FluidStack getFluidStack(String fluidName, int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			return FluidRegistry.getFluidStack(fluidName, amount).copy();
		} 
		catch (Throwable e){
			return null;
		}

	}
	
	public static FluidStack[] getFluidStackArray(String fluidName, int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			FluidStack[] singleFluid = {FluidRegistry.getFluidStack(fluidName, amount)};
			return singleFluid;
		} 
		catch (Throwable e){
			return null;
		}

	}
	
}
