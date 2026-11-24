package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.IItemInputLogicComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IRecipeProcessAwareComponent;
import gregtech.api.ec.component.ISlotUpdateAwareComponent;
import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemInputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;

public class ItemInputLogicComponent extends BaseHatchContainerComponent implements IMultiblockItemInputLogicComponent,
    ISlotUpdateAwareComponent, IRecipeProcessAwareComponent, IHatchConfigCopyableComponent {

    private final ArrayList<MTEHatchInputBus> mInputBusses = new ArrayList<>();
    private boolean inRecipeProcessing = false;

    @Override
    public boolean tryAttachItemInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof IDualInputHatch) {
            return false;
        }
        if (metaTile instanceof MTEHatchSteamBusInput) {
            return false;
        }
        if (metaTile instanceof MTEHatchInputBus hatch) {
            hatch.mRecipeMap = getRecipeMap();
            if (!mInputBusses.contains(hatch)) {
                onHatchAttached(metaTile, baseCasingIndex);
                mInputBusses.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mInputBusses);
        mInputBusses.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidItemInputHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull List<? extends MTEHatchInputBus> getItemInputHatches() {
        return mInputBusses;
    }

    private @Nullable RecipeMap<?> getRecipeMap() {
        return recipeLogic != null ? recipeLogic.getRecipeMap() : null;
    }

    @Override
    public int getItemInputPriority() {
        return ItemInputPriority.STANDARD_BUS.priority;
    }

    @Override
    public long depleteItemRange(@NotNull ItemStack stack, long minAmount, long maxAmount, boolean simulate) {
        if (stack.getItem() == null || minAmount <= 0 || maxAmount < minAmount || mInputBusses.isEmpty()) return 0;
        boolean needManageLifecycle = !inRecipeProcessing;
        if (needManageLifecycle) startRecipeProcessing();
        long amount = 0;
        try {
            List<ItemStack> stored = getStoredItemsForColor(null);
            amount = IItemInputLogicComponent.depleteFromItemList(stored, stack, minAmount, maxAmount, simulate);
        } finally {
            if (needManageLifecycle) {
                var checkResult = endRecipeProcessing();
                if (!simulate && amount > 0) {
                    updateSlots();
                }
                if (!checkResult.wasSuccessful()) {
                    amount = 0;
                }
            }
        }
        return amount;
    }

    @Override
    public @Nullable ItemStack depleteItemRange(@NotNull Predicate<ItemStack> filter, long minAmount, long maxAmount,
        boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount || mInputBusses.isEmpty()) return null;
        boolean needManageLifecycle = !inRecipeProcessing;
        if (needManageLifecycle) startRecipeProcessing();
        ItemStack result = null;
        try {
            List<ItemStack> stored = getStoredItemsForColor(null);
            result = IItemInputLogicComponent.depleteFromItemList(stored, filter, minAmount, maxAmount, simulate);
        } finally {
            if (needManageLifecycle) {
                var checkResult = endRecipeProcessing();
                if (!simulate && result != null) {
                    updateSlots();
                }
                if (!checkResult.wasSuccessful()) {
                    result = null;
                }
            }
        }
        return result;
    }

    @Override
    public void appendStoredItemsForColor(@NotNull List<ItemStack> list, @Nullable Byte color) {
        if (mInputBusses.isEmpty()) return;
        var recipeMap = getRecipeMap();
        Map<GTUtility.ItemId, ItemStack> inputsFromME = new HashMap<>();
        for (MTEHatchInputBus hatch : getValidItemInputHatches()) {
            if (hatch instanceof MTEHatchCraftingInputME) {
                continue;
            }
            byte busColor = hatch.getColor();
            if (color != null && busColor != -1 && busColor != color) continue;
            hatch.mRecipeMap = recipeMap;
            IGregTechTileEntity tileEntity = hatch.getBaseMetaTileEntity();
            boolean isMEBus = hatch instanceof MTEHatchInputBusME;
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        list.add(itemStack);
                    }
                }
            }
        }
        if (!inputsFromME.isEmpty()) {
            list.addAll(inputsFromME.values());
        }
    }

    @Override
    public short getHatchColors() {
        short hatchColors = 0;
        for (var bus : getValidItemInputHatches()) hatchColors |= (short) (1 << bus.getColor());
        return hatchColors;
    }

    @Override
    public @NotNull Map<GTUtility.ItemId, ItemStack> getStoredItemsFromME() {
        if (mInputBusses.isEmpty()) return Collections.emptyMap();
        Map<GTUtility.ItemId, ItemStack> result = new HashMap<>();
        var recipeMap = getRecipeMap();
        for (MTEHatchInputBus hatch : getValidItemInputHatches()) {
            if (!(hatch instanceof MTEHatchInputBusME)) continue;
            hatch.mRecipeMap = recipeMap;
            IGregTechTileEntity tileEntity = hatch.getBaseMetaTileEntity();
            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    result.put(GTUtility.ItemId.createNoCopy(itemStack), itemStack);
                }
            }

        }
        return result;
    }

    @Override
    public void startRecipeProcessing() {
        inRecipeProcessing = true;
        for (MTEHatchInputBus hatch : getValidItemInputHatches()) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
    }

    @Override
    public @NotNull CheckRecipeResult endRecipeProcessing() {
        inRecipeProcessing = false;
        CheckRecipeResult worstResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (MTEHatchInputBus hatch : getValidItemInputHatches()) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                var result = aware.endRecipeProcessing(getMachine());
                if (!result.wasSuccessful()) {
                    worstResult = result;
                }
            }
        }
        return worstResult;
    }

    @Override
    public void updateSlots() {
        for (var tHatch : getValidItemInputHatches()) tHatch.updateSlots();
    }

    private @Nullable IRecipeLogicComponent recipeLogic;

    @Override
    public void onComponentsReady() {
        super.onComponentsReady();
        recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
    }

    @Override
    public @NotNull Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches() {
        return Collections.singletonMap("mInputBusses", mInputBusses);
    }
}
