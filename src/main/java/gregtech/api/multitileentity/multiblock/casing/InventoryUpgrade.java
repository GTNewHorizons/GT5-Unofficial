package gregtech.api.multitileentity.multiblock.casing;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
        mTier = aNBT.getInteger("mTier");
        mInventoryName = "inventory" + GT_Values.VN[mTier];
        mInputInventory = new ItemStackHandler(mInventorySize);
        mOutputInventory = new ItemStackHandler(mInventorySize);

        loadInventory(aNBT, mInputInventory, NBT.INV_INPUT_LIST);
        loadInventory(aNBT, mOutputInventory, NBT.INV_OUTPUT_LIST);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
        aNBT.setInteger("mInventorySize", mInventorySize);
        aNBT.setInteger("mTier", mTier);

        writeInventory(aNBT, mInputInventory, NBT.INV_INPUT_LIST);
        writeInventory(aNBT, mOutputInventory, NBT.INV_OUTPUT_LIST);
    }

    protected void loadInventory(NBTTagCompound aNBT, IItemHandlerModifiable inv, String invListTag) {
        if (inv != null) {
            final NBTTagList tList = aNBT.getTagList(invListTag, 10);
            for (int i = 0; i < tList.tagCount(); i++) {
                final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
                final int tSlot = tNBT.getShort("s");
                if (tSlot >= 0 && tSlot < inv.getSlots()) inv.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
            }
        }
    }

    protected void writeInventory(NBTTagCompound aNBT, IItemHandlerModifiable inv, String invListTag) {
        if (inv != null && inv.getSlots() > 0) {
            final NBTTagList tList = new NBTTagList();
            for (int tSlot = 0; tSlot < inv.getSlots(); tSlot++) {
                final ItemStack tStack = inv.getStackInSlot(tSlot);
                if (tStack != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("s", (byte) tSlot);
                    tStack.writeToNBT(tag);
                    tList.appendTag(tag);
                }
            }
            aNBT.setTag(invListTag, tList);
        }
    }
}
