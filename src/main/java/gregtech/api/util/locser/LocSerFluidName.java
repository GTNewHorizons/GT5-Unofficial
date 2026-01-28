package gregtech.api.util.locser;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.network.NetworkUtils;

/**
 * Represent localizable fluid name.
 */
public class LocSerFluidName implements ILocSer {

    public FluidStack stack = null;

    public LocSerFluidName() {}

    public LocSerFluidName(FluidStack aStack) {
        stack = aStack;
    }

    @Override
    public void encode(PacketBuffer out) {
        NetworkUtils.writeStringSafe(out, getId());
        NetworkUtils.writeFluidStack(out, stack);
    }

    @Override
    public void decode(PacketBuffer in) {
        this.stack = NetworkUtils.readFluidStack(in);
    }

    @Override
    public String localize() {
        return stack.getLocalizedName();
    }

    @Override
    public String getId() {
        return "gt:fluidName";
    }

}
