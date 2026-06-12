package gregtech.api.fluid;

import static com.google.common.primitives.Ints.saturatedCast;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * FluidStack whose real amount can exceed Forge's int-backed amount field.
 */
public class GTLongFluidStack extends FluidStack {

    private long realAmount;

    public GTLongFluidStack(Fluid fluid, long amount) {
        super(fluid, saturatedCast(Math.max(0, amount)));
        this.realAmount = Math.max(0, amount);
    }

    public GTLongFluidStack(Fluid fluid, long amount, NBTTagCompound tag) {
        super(fluid, saturatedCast(Math.max(0, amount)), tag);
        this.realAmount = Math.max(0, amount);
    }

    public GTLongFluidStack(FluidStack stack, long amount) {
        super(stack, saturatedCast(Math.max(0, amount)));
        this.realAmount = Math.max(0, amount);
    }

    public long getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(long amount) {
        realAmount = Math.max(0, amount);
        this.amount = saturatedCast(realAmount);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = super.writeToNBT(nbt);
        if (realAmount > Integer.MAX_VALUE) tag.setLong("LAmount", realAmount);
        return tag;
    }

    @Override
    public GTLongFluidStack copy() {
        return new GTLongFluidStack(getFluid(), realAmount, tag == null ? null : (NBTTagCompound) tag.copy());
    }
}
