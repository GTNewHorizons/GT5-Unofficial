package gregtech.common.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetLockedFluid;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

@Optional.Interface(modid = "NotEnoughItems", iface = "codechicken.nei.api.INEIGuiHandler")
public class GT_GUIContainer_OutputHatch extends GT_GUIContainerMetaTile_Machine implements INEIGuiHandler {

    private final String mName;

    public GT_GUIContainer_OutputHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_OutputHatch(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "OutputHatch.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
        fontRendererObj.drawString(mName, 8, 6, 4210752);
        if (mContainer != null) {
            fontRendererObj.drawString("Liquid Amount", 10, 20, 16448255);
            fontRendererObj.drawString(GT_Utility.parseNumberToString(((GT_Container_OutputHatch) mContainer).mContent), 10, 30, 16448255);
            fontRendererObj.drawString("Locked Fluid", 101, 20, 16448255);
            ItemStack tLockedDisplayStack = (ItemStack) mContainer.getInventory().get(3);
            String fluidName = tLockedDisplayStack == null ? "None" : tLockedDisplayStack.getDisplayName();
            fontRendererObj.drawString(fluidName, 101, 30, 16448255);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        return currentVisibility;
    }

    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return Collections.emptyList();
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return null;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        if (gui instanceof GT_GUIContainer_OutputHatch && ((GT_GUIContainer_OutputHatch) gui).isMouseOverSlot(3, mousex, mousey)) {
            // the instanceof check should be unnecessary, but we will do it regardless, just in case.
            FluidStack tFluidStack;
            tFluidStack = GT_Utility.getFluidForFilledItem(draggedStack, true);
            if (tFluidStack == null) {
                tFluidStack = GT_Utility.getFluidFromDisplayStack(draggedStack);
            }
            if (tFluidStack != null) {
                GT_Values.NW.sendToServer(new GT_Packet_SetLockedFluid(((GT_GUIContainer_OutputHatch) gui).mContainer.mTileEntity, tFluidStack));
                draggedStack.stackSize = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        return false;
    }
}
