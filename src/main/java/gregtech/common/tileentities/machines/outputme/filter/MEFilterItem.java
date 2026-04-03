package gregtech.common.tileentities.machines.outputme.filter;

import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTUtility;

public class MEFilterItem extends MEFilterBase<IAEItemStack, GTUtility.ItemId, ItemStack> {

    public MEFilterItem() {
        super(new HashSet<GTUtility.ItemId>());
    }

    @Override
    protected GTUtility.ItemId extractElement(ItemStack stack) {
        return GTUtility.ItemId.create(stack);
    }

    @Override
    protected boolean isCorrectType(IAEStack<?> stack) {
        return stack instanceof IAEItemStack;
    }

    @Override
    protected String getDisplayName(IAEItemStack stack) {
        return stack.getDisplayName();
    }

    @Override
    public void onLoadNBTData(NBTTagCompound aNBT) {
        NBTBase lockedItemsTag = aNBT.getTag("lockedItems");

        if (lockedItemsTag instanceof NBTTagList lockedItemsList) {
            for (int i = 0; i < lockedItemsList.tagCount(); i++) {
                NBTTagCompound stackTag = lockedItemsList.getCompoundTagAt(i);
                lockedElements.add(GTUtility.ItemId.create(GTUtility.loadItem(stackTag)));
            }
        }
    }

    @Override
    public void onSaveNBTData(NBTTagCompound aNBT) {
        NBTTagList lockedItemsTag = new NBTTagList();

        for (GTUtility.ItemId stack : lockedElements) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.getItemStack()
                .writeToNBT(stackTag);
            lockedItemsTag.appendTag(stackTag);
        }

        aNBT.setTag("lockedItems", lockedItemsTag);
    }

    @Override
    public String getEnableKey() {
        return "GT5U.hatch.fluid.filter.enable";
    }

    @Override
    public String getDisableKey() {
        return "GT5U.hatch.fluid.filter.disable";
    }

    @Override
    public ItemStack toNative(IAEItemStack aeStack) {
        return aeStack.getItemStack();
    }

    @Override
    public IAEItemStack fromNative(ItemStack stack) {
        return AEItemStack.create(stack);
    }

    public boolean isFilteredToItem(GTUtility.ItemId id) {
        if (!isFiltered()) {
            return true;
        }
        return isBlacklist ^ lockedElements.contains(id);
    }
}
