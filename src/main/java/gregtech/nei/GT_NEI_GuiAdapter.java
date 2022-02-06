package gregtech.nei;

import codechicken.nei.api.INEIGuiAdapter;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_SetLockedFluid;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_GUIContainer_OutputHatch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_NEI_GuiAdapter extends INEIGuiAdapter {
	@Override
	public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
		if (gui instanceof GT_GUIContainer_OutputHatch && ((GT_GUIContainer_OutputHatch) gui).isMouseOverSlot(3, mousex, mousey)) {
			FluidStack tFluidStack = GT_Utility.getFluidForFilledItem(draggedStack, true);
			if (tFluidStack != null) {
				GT_Values.NW.sendToServer(new GT_Packet_SetLockedFluid(((GT_GUIContainer_OutputHatch) gui).mContainer.mTileEntity, tFluidStack));
				return true;
			}
		}
		return super.handleDragNDrop(gui, mousex, mousey, draggedStack, button);
	}
}
