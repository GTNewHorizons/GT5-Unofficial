package gregtech.api.interfaces;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidStack;

public interface IContainerMaterialRenderer {

    void renderFluidContainer(IItemRenderer.ItemRenderType type, FluidStack tFluid, IIcon fluidIcon, IIcon overlay);
}
