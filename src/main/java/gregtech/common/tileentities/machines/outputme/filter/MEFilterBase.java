package gregtech.common.tileentities.machines.outputme.filter;

import java.util.Collection;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import appeng.api.config.Upgrades;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.data.IAEStack;
import appeng.items.contents.CellConfig;
import journeymap.shadow.org.jetbrains.annotations.NotNull;

public abstract class MEFilterBase<T extends IAEStack<T>, E, I> {

    protected boolean isBlacklist = false;
    protected final Collection<E> lockedElements;

    protected MEFilterBase(Collection<E> storage) {
        lockedElements = storage;
    }

    public boolean isAllowed(@NotNull I stack) {
        if (!isFiltered()) return true;
        boolean contains = lockedElements.contains(extractElement(stack));
        return isBlacklist != contains;
    }

    public boolean isAllowed(@NotNull T stack) {
        if (!isFiltered()) return true;
        boolean contains = lockedElements.contains(extractElement(toNative(stack)));
        return isBlacklist != contains;
    }

    public boolean isFiltered() {
        return !lockedElements.isEmpty();
    }

    public void clear() {
        lockedElements.clear();
    }

    public boolean getIsBlackList() {
        return isBlacklist;
    }

    private String updateFilterFromConfig(CellConfig cfg) {
        lockedElements.clear();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;

        for (int i = 0; i < cfg.getSizeInventory(); i++) {
            IAEStack<?> stack = cfg.getAEStackInSlot(i);
            if (stack != null && isCorrectType(stack)) {
                E element = extractElement(toNative((T) stack));
                lockedElements.add(element);

                String name = getDisplayName((T) stack);
                if (isFirst) {
                    builder.append(name);
                    isFirst = false;
                } else {
                    builder.append(", ")
                        .append(name);
                }
            }
        }

        return builder.toString();
    }

    private void updateBlacklistMode(ICellWorkbenchItem cell, ItemStack stack) {
        isBlacklist = false;
        IInventory upgrades = cell.getUpgradesInventory(stack);
        for (int i = 0; i < upgrades.getSizeInventory(); i++) {
            ItemStack is = upgrades.getStackInSlot(i);
            if (is != null) {
                Upgrades u = ((IUpgradeModule) is.getItem()).getType(is);
                if (u == Upgrades.INVERTER) {
                    isBlacklist = true;
                    return;
                }
            }
        }
    }

    public String updateFilterFromCell(ICellWorkbenchItem cell, ItemStack stack) {
        updateBlacklistMode(cell, stack);

        CellConfig cfg = (CellConfig) cell.getConfigAEInventory(stack);
        return updateFilterFromConfig(cfg);
    }

    protected abstract E extractElement(I stack);

    protected abstract boolean isCorrectType(IAEStack<?> stack);

    protected abstract String getDisplayName(T stack);

    public final void loadNBTData(NBTTagCompound tag) {
        isBlacklist = tag.getBoolean("blackList");
        onLoadNBTData(tag);
    }

    public abstract void onLoadNBTData(NBTTagCompound tag);

    public final void saveNBTData(NBTTagCompound tag) {
        tag.setBoolean("blackList", isBlacklist);
        onSaveNBTData(tag);
    }

    public abstract void onSaveNBTData(NBTTagCompound tag);

    public abstract String getEnableKey();

    public abstract String getDisableKey();

    /**
     * <b>Note:</b> This implementation returns a <b>new instance</b> on every call.
     *
     * @todo Refactor to support object reuse.
     */
    public abstract I toNative(T aeStack);

    /**
     * <b>Note:</b> This implementation returns a <b>new instance</b> on every call.
     *
     * @todo Refactor to support object reuse.
     */
    public abstract T fromNative(I stack);
}
