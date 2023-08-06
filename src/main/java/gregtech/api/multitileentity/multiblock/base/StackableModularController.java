package gregtech.api.multitileentity.multiblock.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import gregtech.api.multitileentity.interfaces.UpgradableModularMuTE;
import gregtech.api.util.GT_StructureUtilityMuTE.UpgradeCasings;

public abstract class StackableModularController<T extends StackableModularController<T>> extends StackableController<T>
    implements UpgradableModularMuTE {
    protected double durationMultiplier = 1;
    protected double euTickMultiplier = 1;

    private Map<UpgradeCasings, int[]> mucMap;

    protected @NotNull Map<UpgradeCasings, int[]> getMucMap() {
        if (mucMap == null) {
            mucMap = createMucMap();
        }
        return mucMap;
    }

    protected static @NotNull Map<UpgradeCasings, int[]> createMucMap() {
        Map<UpgradeCasings, int[]> mucCount = new HashMap<>();
        mucCount.put(UpgradeCasings.Heater, new int[] { 0, 0, 0, 0, 0 });
        mucCount.put(UpgradeCasings.Insulator, new int[] { 0, 0, 0, 0, 0 });
        return mucCount;
    }

    @Override
    public void increaseMucCount(UpgradeCasings casingType, int tier) {
        Map<UpgradeCasings, int[]> mucCounters = getMucMap();
        int[] casingCount = mucCounters.get(casingType);

        switch (tier) {
            case 0, 1, 2 -> casingCount[0] += 1;
            case 3, 4, 5 -> casingCount[1] += 1;
            case 6, 7, 8 -> casingCount[2] += 1;
            case 9, 10, 11 -> casingCount[3] += 1;
            default -> casingCount[4] += 1;
        }
    }

    @Override
    public void resetMucCount() {
        Map<UpgradeCasings, int[]> mucCounters = getMucMap();
        mucCounters.forEach((type, casingCount) -> { Arrays.fill(casingCount, 0); });
    }


    protected abstract boolean calculateMucMultipliers();

    @Override
    protected boolean checkRecipe() {
        if (!(this instanceof ProcessingLogicHost)) {
            return false;
        }
        ProcessingLogic logic = ((ProcessingLogicHost) this).getProcessingLogic();
        logic.clear();
        boolean result = false;
        if (isSeparateInputs()) {
            // TODO: Add separation with fluids
            for (Pair<ItemStack[], String> inventory : getItemInputsForEachInventory()) {
                IItemHandlerModifiable outputInventory = multiBlockOutputInventory
                    .getOrDefault(inventory.getLeft(), null);
                result = logic.setInputItems(inventory.getLeft())
                    .setCurrentOutputItems(getOutputItems())
                    .process();
                if (result) {
                    inventoryName = inventory.getRight();
                    break;
                }
                logic.clear();
            }
        } else {
            result = logic.setInputItems(getInputItems())
                .setCurrentOutputItems(getOutputItems())
                .setInputFluids(getInputFluids())
                .setCurrentOutputFluids(getOutputFluids())
                .setVoltage(power.getVoltage())
                .setAmperage(amperage)
                .setPerfectOverclock(hasPerfectOverclock())
                .setIsCleanroom(isCleanroom)
                .process();
        }
        setDuration((long) (logic.getDuration() * durationMultiplier));
        setEut((long) (logic.getEut() * euTickMultiplier));
        setItemOutputs(logic.getOutputItems());
        setFluidOutputs(logic.getOutputFluids());
        return result;
    }
}
