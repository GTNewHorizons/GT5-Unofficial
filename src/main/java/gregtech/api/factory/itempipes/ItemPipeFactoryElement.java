package gregtech.api.factory.itempipes;

import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.factory.IFactoryElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface ItemPipeFactoryElement
    extends IFactoryElement<ItemPipeFactoryElement, ItemPipeFactoryNetwork, ItemPipeFactoryGrid> {

    int getStepSize();

    int getRemainingTransferrableStacks();

    int getStackMultiplier();

    int getMaxTransferableStacks();

    void setJumpTransferState(ItemPipeFactoryNetwork.PipeJumpTransferState transferState);

    boolean isConnectedToInventory();

    void incrementTransferCounter(int aIncrement);

    int sendItemStack(IGregTechTileEntity sender, int maxStackToTransfer, int stackMultiplier);

    boolean canTransferMoreItemsThisTick();

    void setStepCounter(int stepCounter);

    List<ItemStack> removeAllItems();
}
