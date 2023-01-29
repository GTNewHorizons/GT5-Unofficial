package gregtech.api.multitileentity.multiblock.casing;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryUpgrade extends AdvancedCasing {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private String mInventoryName = "inventory";
    private int mInventorySize = 16;
    private IItemHandlerModifiable mInputInventory;
    private IItemHandlerModifiable mOutputInventory;

    @Override
    protected void customWork(IMultiBlockController aTarget) {
        aTarget.registerInventory(mInventoryName, mInputInventory, INPUT);
        aTarget.registerInventory(mInventoryName, mOutputInventory, OUTPUT);
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.inventory";
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        mInventorySize = aNBT.getInteger(NBT.UPGRADE_INVENTORY_SIZE);
        mInventoryName = "inventory" + GT_Values.VN[mTier];
        mInputInventory = new ItemStackHandler(mInventorySize);
        mOutputInventory = new ItemStackHandler(mInventorySize);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
    }

    @Override
    protected void onBaseTEDestroyed() {
        super.onBaseTEDestroyed();
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            controller.unregisterInventory(mInventoryName, mInputInventory, INPUT);
            controller.unregisterInventory(mInventoryName, mOutputInventory, OUTPUT);
        }
    }
}
