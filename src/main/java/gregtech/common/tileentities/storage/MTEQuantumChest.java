package gregtech.common.tileentities.storage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEQuantumChest extends MTEDigitalChestBase {

    public int mItemCount = 0;
    public ItemStack mItemStack = null;
    NBTTagList mInvData = null;

    public MTEQuantumChest(Args args) {
        super(args);
    }

    @Deprecated
    public MTEQuantumChest(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Deprecated
    public MTEQuantumChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEQuantumChest(getPrototype());
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        mInvData = new NBTTagList();
        boolean hasInvData = false;
        for (int i = 0; i < 3; i++) {
            if (mInventory[i] != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                tNBT.setByte("Count", (byte) mInventory[i].stackSize);
                tNBT.setShort("Damage", (short) mInventory[i].getItemDamage());
                tNBT.setShort("id", (short) Item.getIdFromItem(mInventory[i].getItem()));
                tNBT.setInteger("IntSlot", i);
                if (mInventory[i].hasTagCompound()) {
                    tNBT.setTag("tag", mInventory[i].getTagCompound());
                }
                mInvData.appendTag(tNBT);
                hasInvData = true;
            }
        }
        if (mItemStack != null) aNBT.setTag("mItemStack", getItemStack().writeToNBT(new NBTTagCompound()));
        if (hasInvData) aNBT.setTag("Inventory", mInvData);
        if (getItemCount() > 0) aNBT.setInteger("mItemCount", getItemCount());
        if (mVoidOverflow) aNBT.setBoolean("mVoidOverflow", true);

        super.setItemNBT(aNBT);
    }

    @Override
    protected String localizedChestName() {
        return StatCollector.translateToLocal("GT5U.infodata.quantum_chest.name");
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public void setItemCount(int aCount) {
        mItemCount = aCount;
    }

    @Override
    public ItemStack getItemStack() {
        return mItemStack;
    }

    @Override
    public void setItemStack(ItemStack s) {
        mItemStack = s;
    }

    @Nullable
    @Override
    public List<ItemStack> getItemsForHoloGlasses() {
        List<ItemStack> ret = new ArrayList<>();
        ret.add(getStackInSlot(0));
        ret.add(getStackInSlot(1));
        if (mItemStack != null) {
            ItemStack copy = mItemStack.copy();
            copy.stackSize = mItemCount;
            ret.add(copy);
        }
        return ret;
    }
}
