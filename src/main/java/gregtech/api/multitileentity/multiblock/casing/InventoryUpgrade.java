package gregtech.api.multitileentity.multiblock.casing;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryUpgrade extends AdvancedCasing {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private String mInventoryName = "inventory";
    private int mTier = 0;
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
        mInventorySize = aNBT.getInteger("mInventorySize");
        mTier = aNBT.getInteger("mTIer");
        mInventoryName = "inventory" + GT_Values.VN[mTier];
        mInputInventory = new ItemStackHandler(mInventorySize);
        mOutputInventory = new ItemStackHandler(mInventorySize);
    }
}
