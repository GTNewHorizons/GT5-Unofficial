package gregtech.common.tileentities.machines.outputme.filter;

import static gregtech.common.covers.modes.FilterType.BLACKLIST;
import static gregtech.common.covers.modes.FilterType.WHITELIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

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

    private IChatComponent updateFilterFromConfig(CellConfig cfg) {
        lockedElements.clear();
        List<IChatComponent> list = new ArrayList<>();

        for (int i = 0; i < cfg.getSizeInventory(); i++) {
            IAEStack<?> stack = cfg.getAEStackInSlot(i);
            if (stack != null && isCorrectType(stack)) {
                E element = extractElement(toNative((T) stack));
                lockedElements.add(element);

                list.add(stack.getChatComponent());
            }
        }

        String modeKey = getIsBlackList() ? BLACKLIST.getKey() : WHITELIST.getKey();
        IChatComponent result = new ChatComponentTranslation(getEnableKey())
            .appendSibling(new ChatComponentTranslation(modeKey).appendText(": "));
        for (IChatComponent chat : list) {
            result.appendSibling(chat);
        }
        return result;
    }

    public IChatComponent clearAndGetNotify() {
        clear();
        return new ChatComponentTranslation(getDisableKey());
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

    public IChatComponent updateFilterFromCell(ICellWorkbenchItem cell, ItemStack stack) {
        updateBlacklistMode(cell, stack);

        CellConfig cfg = (CellConfig) cell.getConfigAEInventory(stack);
        return updateFilterFromConfig(cfg);
    }

    protected abstract E extractElement(I stack);

    protected abstract boolean isCorrectType(IAEStack<?> stack);

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
