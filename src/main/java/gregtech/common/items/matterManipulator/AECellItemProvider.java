package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.definitions.IItemDefinition;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.parts.automation.UpgradeInventory;
import gregtech.api.util.GTUtility;

public class AECellItemProvider implements IItemProvider {

    public PortableItemStack mCell;
    public PortableItemStack[] mUpgrades, mConfig;
    public String mOreDict;
    public byte mFuzzyMode;

    public AECellItemProvider() {}

    public static AECellItemProvider fromWorkbenchItem(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ICellWorkbenchItem item) || !item.isEditable(stack)) {
            return null;
        }

        AECellItemProvider cell = new AECellItemProvider();

        cell.mCell = new PortableItemStack(stack);
        IInventory upgrades = item.getUpgradesInventory(stack);
        cell.mUpgrades = upgrades == null ? null : MMUtils.fromInventory(upgrades);
        IInventory config = item.getConfigInventory(stack);
        cell.mConfig = config == null ? null : MMUtils.fromInventoryNoMerge(config);
        cell.mFuzzyMode = switch (item.getFuzzyMode(stack)) {
            case IGNORE_ALL -> 0;
            case PERCENT_25 -> 1;
            case PERCENT_50 -> 2;
            case PERCENT_75 -> 3;
            case PERCENT_99 -> 4;
        };

        IItemDefinition oredictCard = AEApi.instance()
            .definitions()
            .materials()
            .cardOreFilter();
        boolean hasOredictCard = upgrades == null ? false
            : GTUtility.streamInventory(upgrades)
                .anyMatch(oredictCard::isSameAs);
        if (hasOredictCard) {
            cell.mOreDict = item.getOreFilter(stack);
        }

        return cell;
    }

    @Override
    public ItemStack getStack(IPseudoInventory inv, boolean consume) {
        ItemStack cell = mCell.toStack();

        if (!(cell.getItem() instanceof ICellWorkbenchItem cellWorkbenchItem) || !cellWorkbenchItem.isEditable(cell)) {
            return null;
        }

        List<ItemStack> items = new ArrayList<>();
        items.add(cell);
        items.addAll(
            Arrays.asList(mUpgrades == null ? new PortableItemStack[0] : mUpgrades)
                .stream()
                .map(PortableItemStack::toStack)
                .collect(Collectors.toList()));

        if (consume && !inv.tryConsumeItems(items.toArray(new ItemStack[0]))) {
            return null;
        }

        UpgradeInventory upgrades = (UpgradeInventory) cellWorkbenchItem.getUpgradesInventory(cell);
        if (upgrades != null) {
            MMUtils.installUpgrades(inv, upgrades, mUpgrades, consume, false);
        }

        IInventory config = cellWorkbenchItem.getConfigInventory(cell);

        if (config != null) {
            MMUtils.clearInventory(config);

            for (int i = 0; i < mConfig.length; i++) {
                if (mConfig[i] != null) {
                    config.setInventorySlotContents(i, mConfig[i].toStack());
                }
            }

            config.markDirty();
        }

        cellWorkbenchItem.setFuzzyMode(cell, switch (mFuzzyMode) {
            case 0 -> FuzzyMode.IGNORE_ALL;
            case 1 -> FuzzyMode.PERCENT_25;
            case 2 -> FuzzyMode.PERCENT_50;
            case 3 -> FuzzyMode.PERCENT_75;
            case 4 -> FuzzyMode.PERCENT_99;
            default -> FuzzyMode.IGNORE_ALL;
        });

        IItemDefinition oredictCard = AEApi.instance()
            .definitions()
            .materials()
            .cardOreFilter();
        boolean hasOredictCard = GTUtility.streamInventory(upgrades)
            .anyMatch(oredictCard::isSameAs);
        if (hasOredictCard) {
            cellWorkbenchItem.setOreFilter(cell, mOreDict);
        }

        return cell;
    }

    @Override
    public String toString() {
        return mCell.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mCell == null) ? 0 : mCell.hashCode());
        result = prime * result + Arrays.hashCode(mUpgrades);
        result = prime * result + Arrays.hashCode(mConfig);
        result = prime * result + ((mOreDict == null) ? 0 : mOreDict.hashCode());
        result = prime * result + mFuzzyMode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AECellItemProvider other = (AECellItemProvider) obj;
        if (mCell == null) {
            if (other.mCell != null) return false;
        } else if (!mCell.equals(other.mCell)) return false;
        if (!Arrays.equals(mUpgrades, other.mUpgrades)) return false;
        if (!Arrays.equals(mConfig, other.mConfig)) return false;
        if (mOreDict == null) {
            if (other.mOreDict != null) return false;
        } else if (!mOreDict.equals(other.mOreDict)) return false;
        if (mFuzzyMode != other.mFuzzyMode) return false;
        return true;
    }
}
