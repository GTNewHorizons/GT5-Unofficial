package gregtech.api.metatileentity.implementations;

import gregtech.api.gui.GT_Container_SpecialFilter;
import gregtech.api.gui.GT_GUIContainer_SpecialFilter;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GT_MetaTileEntity_SpecialFilter extends GT_MetaTileEntity_Buffer {
    public static final int BUFFER_SLOT_COUNT = 9;
    public static final int SPECIAL_SLOT_INDEX = 9;
    public boolean bNBTAllowed = false;
    public boolean bInvertFilter = false;

    public GT_MetaTileEntity_SpecialFilter(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        // 9 buffer slot, 1 representation slot, 1 holo slot. last seems not needed...
        super(aID, aName, aNameRegional, aTier, 11, aDescription);
    }

    public GT_MetaTileEntity_SpecialFilter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_SpecialFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_SpecialFilter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_SpecialFilter(aPlayerInventory, aBaseMetaTileEntity);
    }

    public abstract void clickTypeIcon(boolean aRightClick, ItemStack aHandStack);

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.bInvertFilter);
        aNBT.setBoolean("bNBTAllowed", this.bNBTAllowed);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.bInvertFilter = aNBT.getBoolean("bInvertFilter");
        this.bNBTAllowed = aNBT.getBoolean("bNBTAllowed");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && ((this.bNBTAllowed) || (!aStack.hasTagCompound())) && (this.isStackAllowed(aStack) != this.bInvertFilter);
    }

    protected abstract boolean isStackAllowed(ItemStack aStack);
}
