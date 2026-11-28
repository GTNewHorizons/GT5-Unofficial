package gregtech.api.ec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.ec.component.multiblock.CustomHatchKey;
import gregtech.api.ec.component.multiblock.ICustomHatchContainerComponent;
import gregtech.api.ec.component.multiblock.IDualInputLogicComponent;
import gregtech.api.ec.component.multiblock.IHatchContainerComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemInputLogicComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.ec.component.multiblock.IRecipeLockingComponent;
import gregtech.api.ec.component.multiblock.IVoidingConfigComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMultiblockController;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.tileentities.machines.IHatchWatcher;

public interface IECMultiblock
    extends IECMachine, IMultiblockController, IVoidable, IRecipeLockable, IHatchWatcher, IStructureCheckHelpers {

    default @NotNull IECMultiblock asMultiblock() {
        return this;
    }

    void explodeMultiblock();

    void stopMachine(@NotNull ShutDownReason reason);

    @Nullable
    ItemStack getControllerSlot();

    void setControllerSlot(@Nullable ItemStack item);

    long getTotalRunTime();

    boolean isFormed();

    int getStartUpCheckTimer();

    int getApproximateRadius();

    @Nullable
    Vector3f getCenter();

    @FunctionalInterface
    interface HatchAttacher<T extends IHatchContainerComponent> {

        boolean attach(T component, IMetaTileEntity metaTile, int baseCasingIndex);
    }

    default <T extends IHatchContainerComponent> boolean tryAttachHatchToComponent(@NotNull ComponentKey<T> key,
        @Nullable IGregTechTileEntity tile, int baseCasingIndex, @NotNull HatchAttacher<T> attacher) {
        if (tile == null) return false;
        IMetaTileEntity metaTile = tile.getMetaTileEntity();
        if (metaTile == null) return false;
        switch (key) {
            case ComponentKey.ListKey < T > listKey -> {
                for (T component : getComponents(listKey)) {
                    if (attacher.attach(component, metaTile, baseCasingIndex)) {
                        return true;
                    }
                }
            }
            case ComponentKey.UniqueKey < T > uniqueKey -> {
                var component = tryGetComponent(uniqueKey);
                if (component != null) return attacher.attach(component, metaTile, baseCasingIndex);
            }
        }
        return false;
    }

    default <T extends MTEHatch> boolean tryAttachCustomHatch(@NotNull CustomHatchKey<T> key,
        @Nullable IGregTechTileEntity igte, int baseCasingIndex) {
        return tryAttachHatchToComponent(
            ICustomHatchContainerComponent.KEY,
            igte,
            baseCasingIndex,
            (comp, metaTile, casing) -> comp.tryAttach(key, metaTile, casing));
    }

    default <T extends IHatchContainerComponent> int getHatchCountOf(@NotNull ComponentKey.ListKey<T> key,
        @NotNull ToIntFunction<T> counter) {
        int total = 0;
        for (var comp : getComponents(key)) {
            total += counter.applyAsInt(comp);
        }
        return total;
    }

    default <T extends IHatchContainerComponent> int getHatchCountOf(@NotNull ComponentKey.ListKey<T> key,
        Function<T, List<?>> hatchesGetter) {
        return getHatchCountOf(
            key,
            (T comp) -> hatchesGetter.apply(comp)
                .size());
    }

    default <T extends IHatchContainerComponent & IUniqueComponent> int getHatchCountOf(
        @NotNull ComponentKey.UniqueKey<T> key, @NotNull ToIntFunction<T> counter) {
        var comp = tryGetComponent(key);
        return comp != null ? counter.applyAsInt(comp) : 0;
    }

    default <T extends IHatchContainerComponent & IUniqueComponent> int getHatchCountOf(
        @NotNull ComponentKey.UniqueKey<T> key, Function<T, List<?>> hatchesGetter) {
        return getHatchCountOf(
            key,
            (T comp) -> hatchesGetter.apply(comp)
                .size());
    }

    default <T extends MTEHatch> @NotNull List<T> getCustomHatches(@NotNull CustomHatchKey<T> key) {
        var comp = tryGetComponent(ICustomHatchContainerComponent.KEY);
        return comp != null ? comp.getHatches(key) : Collections.emptyList();
    }

    default <T extends MTEHatch> @NotNull Iterable<T> getValidCustomHatches(@NotNull CustomHatchKey<T> key) {
        var comp = tryGetComponent(ICustomHatchContainerComponent.KEY);
        return comp != null ? comp.getValidHatches(key) : Collections.emptyList();
    }

    default <T extends MTEHatch> int getCustomHatchCountOf(@NotNull CustomHatchKey<T> key) {
        var comp = tryGetComponent(ICustomHatchContainerComponent.KEY);
        return comp != null ? comp.getHatchCount(key) : 0;
    }

    @Override
    default boolean supportsVoidProtection() {
        return hasComponent(IVoidingConfigComponent.KEY);
    }

    @Override
    default boolean supportsSingleRecipeLocking() {
        return hasComponent(IRecipeLockingComponent.KEY);
    }

    /**
     * Ejects a stack. Fluids are only ejected when the whole stack can be ejected into output hatches.
     *
     * @param stack The stack to eject.
     * @return True when the whole stack was ejected, false otherwise.
     */
    default boolean addOutputAtomic(FluidStack stack) {
        var comp = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        return comp != null && comp.addOutputAtomic(stack, protectsExcessFluid());
    }

    /**
     * Ejects a stack as well as it can. Fluids are ejected regardless of whether the whole stack can fit.
     *
     * @param stack The stack to eject.
     * @return The number of fluids actually ejected.
     */
    default long addOutputPartial(FluidStack stack) {
        var comp = tryGetComponent(IMultiblockFluidOutputLogicComponent.KEY);
        return comp != null ? comp.addOutputPartial(stack, protectsExcessFluid()) : 0;
    }

    /**
     * Ejects a stack. Items are only ejected when the whole stack can be ejected into output busses.
     *
     * @param stack The stack to eject.
     * @return True when the whole stack was ejected, false otherwise.
     */
    default boolean addOutputAtomic(ItemStack stack) {
        var comp = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        return comp != null && comp.addOutputAtomic(stack, protectsExcessItem());
    }

    /**
     * Ejects a stack as well as it can. Items are ejected regardless of whether the whole stack can fit.
     *
     * @param stack The stack to eject.
     * @return The number of items actually ejected.
     */
    default int addOutputPartial(ItemStack stack) {
        var comp = tryGetComponent(IMultiblockItemOutputLogicComponent.KEY);
        return comp != null ? comp.addOutputPartial(stack, protectsExcessItem()) : 0;
    }

    /**
     * Note: only be called inside a recipe processing will contain inputs from ME hatches.
     */
    default List<FluidStack> getStoredFluids() {
        return getStoredFluidsForColor(null);
    }

    /**
     * Note: only be called inside a recipe processing will contain inputs from ME hatches.
     */
    default List<FluidStack> getStoredFluidsForColor(@Nullable Byte color) {
        var comp = tryGetComponent(IMultiblockFluidInputLogicComponent.KEY);
        return comp != null ? comp.getStoredFluidsForColor(color) : Collections.emptyList();
    }

    /**
     * Note: only be called inside a recipe processing will contain inputs from ME hatches.
     */
    default List<ItemStack> getStoredItems() {
        return getStoredItemsForColor(null);
    }

    /**
     * Note: only be called inside a recipe processing will contain inputs from ME hatches.
     */
    default List<ItemStack> getStoredItemsForColor(@Nullable Byte color) {
        var comp = tryGetComponent(IMultiblockItemInputLogicComponent.KEY);
        List<ItemStack> list = comp != null ? comp.getStoredItemsForColor(color) : new ArrayList<>(1);
        ItemStack controllerSlotItem = getControllerSlot();
        if (controllerSlotItem != null && controllerSlotItem.getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) list.add(controllerSlotItem);
        return list;
    }

    /**
     * Anything that is usually separated off in {@link #getStoredItems()} (like crafting input bus/buffer) is also
     * included here.
     * Note: only be called inside a recipe processing will contain inputs from ME hatches.
     */
    default List<ItemStack> getAllStoredItems() {
        ArrayList<ItemStack> rList = new ArrayList<>();

        var dualComp = tryGetComponent(IDualInputLogicComponent.KEY);
        if (dualComp != null) {
            dualComp.appendAllStoredItems(rList);
        }

        var itemInput = tryGetComponent(IMultiblockItemInputLogicComponent.KEY);
        if (itemInput != null) {
            itemInput.appendStoredItemsForColor(rList, null);
        }

        ItemStack stackInSlot1 = getControllerSlot();
        if (stackInSlot1 != null && stackInSlot1.getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) rList.add(stackInSlot1);
        return rList;
    }

    /**
     * Only should be called inside recipe processing.
     */
    default Map<GTUtility.ItemId, ItemStack> getStoredItemsFromME() {
        var comp = tryGetComponent(IMultiblockItemInputLogicComponent.KEY);
        return comp != null ? comp.getStoredItemsFromME() : Collections.emptyMap();
    }

    /**
     * Only should be called inside recipe processing.
     */
    default Map<GTUtility.FluidId, FluidStack> getStoredFluidsFromME() {
        var comp = tryGetComponent(IMultiblockFluidInputLogicComponent.KEY);
        return comp != null ? comp.getStoredFluidsFromME() : Collections.emptyMap();
    }
}
