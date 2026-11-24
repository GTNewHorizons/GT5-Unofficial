package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.IFluidInputLogicComponent;
import gregtech.api.ec.component.IItemInputLogicComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.IRecipeProcessAwareComponent;
import gregtech.api.ec.component.ISlotUpdateAwareComponent;
import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidInputLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

public class FluidInputLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockFluidInputLogicComponent, IItemInputLogicComponent, ISlotUpdateAwareComponent,
    IRecipeProcessAwareComponent, IHatchConfigCopyableComponent {

    private @NotNull BooleanSupplier filterOnRecipeMap = () -> true;

    public FluidInputLogicComponent setFilterOnRecipeMap(@NotNull BooleanSupplier filterOnRecipeMap) {
        this.filterOnRecipeMap = filterOnRecipeMap;
        return this;
    }

    private final ArrayList<MTEHatchInput> mInputHatches = new ArrayList<>();
    private boolean inRecipeProcessing = false;

    @Override
    public boolean tryAttachFluidInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof IDualInputHatch) {
            return false;
        }
        if (metaTile instanceof MTEHatchInput hatch) {
            if (filterOnRecipeMap.getAsBoolean()) {
                hatch.mRecipeMap = getRecipeMap();
            }

            if (!mInputHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mInputHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mInputHatches);
        mInputHatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidFluidInputHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull List<MTEHatchInput> getFluidInputHatches() {
        return mInputHatches;
    }

    private @Nullable RecipeMap<?> getRecipeMap() {
        return recipeLogic != null ? recipeLogic.getRecipeMap() : null;
    }

    @Override
    public int getFluidInputPriority() {
        return FluidInputPriority.STANDARD_BUS.priority;
    }

    @Override
    public long depleteFluidRange(@NotNull FluidStack stack, long minAmount, long maxAmount, boolean simulate) {
        if (stack.getFluid() == null || minAmount <= 0 || maxAmount < minAmount || mInputHatches.isEmpty()) return 0;
        boolean needManageLifecycle = !inRecipeProcessing;
        if (needManageLifecycle) startRecipeProcessing();
        long amount = 0;
        try {
            List<FluidStack> stored = getStoredFluidsForColor(null);
            amount = IFluidInputLogicComponent.depleteFromFluidList(stored, stack, minAmount, maxAmount, simulate);
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
    public @Nullable FluidStack depleteFluidRange(@NotNull Predicate<FluidStack> filter, long minAmount, long maxAmount,
        boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount || mInputHatches.isEmpty()) return null;
        boolean needManageLifecycle = !inRecipeProcessing;
        if (needManageLifecycle) startRecipeProcessing();
        FluidStack result = null;
        try {
            List<FluidStack> stored = getStoredFluidsForColor(null);
            result = IFluidInputLogicComponent.depleteFromFluidList(stored, filter, minAmount, maxAmount, simulate);
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
    public void appendStoredFluidsForColor(@NotNull List<FluidStack> rList, @Nullable Byte color) {
        if (mInputHatches.isEmpty()) return;
        boolean shouldFilter = filterOnRecipeMap.getAsBoolean();
        var recipeMap = getRecipeMap();
        Map<GTUtility.FluidId, FluidStack> inputsFromME = new HashMap<>();
        for (MTEHatchInput tHatch : getValidFluidInputHatches()) {
            byte hatchColor = tHatch.getColor();
            if (color != null && hatchColor != -1 && hatchColor != color) continue;
            if (shouldFilter) {
                tHatch.mRecipeMap = recipeMap;
            }
            switch (tHatch) {
                case MTEHatchSolidifier ignored -> {
                }
                case MTEHatchInputME inputME -> {
                    for (FluidStack tFluid : inputME.getStoredFluids()) {
                        if (tFluid != null) {
                            // Prevent the same fluid from different ME hatches from being recognized
                            inputsFromME.put(GTUtility.FluidId.createNoCopy(tFluid), tFluid);
                        }
                    }
                }
                case MTEHatchMultiInput multiInputHatch -> {
                    for (FluidStack tFluid : multiInputHatch.getStoredFluid()) {
                        if (tFluid != null) {
                            rList.add(tFluid);
                        }
                    }
                }
                case MTEHatchInputDebug debugHatch -> {
                    for (FluidStack fluid : debugHatch.getFluidList()) {
                        if (fluid != null) {
                            rList.add(GTUtility.copyAmount(Long.MAX_VALUE, fluid));
                        }
                    }
                }
                default -> {
                    FluidStack fillableStack = tHatch.getFillableStack();
                    if (fillableStack != null) {
                        rList.add(fillableStack);
                    }
                }
            }
        }
        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }
    }

    @Override
    public short getHatchColors() {
        short hatchColors = 0;
        for (var bus : getValidFluidInputHatches()) hatchColors |= (short) (1 << bus.getColor());
        return hatchColors;
    }

    @Override
    public @NotNull Map<GTUtility.FluidId, FluidStack> getStoredFluidsFromME() {
        if (mInputHatches.isEmpty()) return Collections.emptyMap();
        boolean shouldFilter = filterOnRecipeMap.getAsBoolean();
        var recipeMap = getRecipeMap();
        Map<GTUtility.FluidId, FluidStack> result = new HashMap<>();
        for (MTEHatchInput tHatch : getValidFluidInputHatches()) {
            if (!(tHatch instanceof MTEHatchInputME meHatch)) continue;
            if (shouldFilter) {
                tHatch.mRecipeMap = recipeMap;
            }
            for (FluidStack fluidStack : meHatch.getStoredFluids()) {
                if (fluidStack != null) {
                    result.put(GTUtility.FluidId.createNoCopy(fluidStack), fluidStack);
                }
            }
        }
        return result;
    }

    @Override
    public void startRecipeProcessing() {
        inRecipeProcessing = true;
        for (MTEHatchInput hatch : getValidFluidInputHatches()) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
    }

    @Override
    public @NotNull CheckRecipeResult endRecipeProcessing() {
        inRecipeProcessing = false;
        CheckRecipeResult worstResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (MTEHatchInput hatch : getValidFluidInputHatches()) {
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
        for (var tHatch : getValidFluidInputHatches()) tHatch.updateSlots();
    }

    private @Nullable IRecipeLogicComponent recipeLogic;

    @Override
    public void onComponentsReady() {
        super.onComponentsReady();
        recipeLogic = tryGetComponent(IRecipeLogicComponent.KEY);
    }

    @Override
    public @NotNull Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches() {
        return Collections.singletonMap("mInputHatches", mInputHatches);
    }

    @Override
    public int getItemInputPriority() {
        return ItemInputPriority.FLUID_HATCH_SLOT.priority;
    }

    @Override
    public long depleteItemRange(@NotNull ItemStack stack, long minAmount, long maxAmount, boolean simulate) {
        if (stack.getItem() == null || minAmount <= 0 || maxAmount < minAmount || mInputHatches.isEmpty()) return 0;
        List<ItemStack> stored = getStoredItemsForColor(null);
        long amount = IItemInputLogicComponent.depleteFromItemList(stored, stack, minAmount, maxAmount, simulate);
        if (!simulate && amount > 0 && !inRecipeProcessing) {
            updateSlots();
        }
        return amount;
    }

    @Override
    public @Nullable ItemStack depleteItemRange(@NotNull Predicate<ItemStack> filter, long minAmount, long maxAmount,
        boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount || mInputHatches.isEmpty()) return null;
        List<ItemStack> stored = getStoredItemsForColor(null);
        ItemStack result = IItemInputLogicComponent.depleteFromItemList(stored, filter, minAmount, maxAmount, simulate);
        if (!simulate && result != null && !inRecipeProcessing) {
            updateSlots();
        }
        return result;
    }

    @Override
    public void appendStoredItemsForColor(@NotNull List<ItemStack> list, @Nullable Byte color) {
        if (mInputHatches.isEmpty()) return;
        boolean shouldFilter = filterOnRecipeMap.getAsBoolean();
        var recipeMap = getRecipeMap();
        for (MTEHatchInput hatch : getValidFluidInputHatches()) {
            byte hatchColor = hatch.getColor();
            if (color != null && hatchColor != -1 && hatchColor != color) continue;
            if (shouldFilter) {
                hatch.mRecipeMap = recipeMap;
            }
            IGregTechTileEntity igte = hatch.getBaseMetaTileEntity();
            ItemStack stackInFirstSlot = igte.getStackInSlot(0);
            if (stackInFirstSlot != null) {
                list.add(stackInFirstSlot);
            }
        }
    }
}
