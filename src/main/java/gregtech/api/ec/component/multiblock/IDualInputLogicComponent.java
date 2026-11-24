package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.uncheckedValidMTEList;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.common.tileentities.machines.IDualInputHatch;

public interface IDualInputLogicComponent extends IHatchContainerComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IDualInputLogicComponent> KEY = ComponentKey.unique();

    boolean tryAttachDualItemInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    boolean tryAttachDualFluidInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    @NotNull
    List<IDualInputHatch> getDualInputHatches();

    default @NotNull Iterable<IDualInputHatch> getValidDualInputHatches() {
        return uncheckedValidMTEList(getDualInputHatches());
    }

    void appendAllStoredItems(@NotNull List<ItemStack> list);

    void appendAllStoredFluids(@NotNull List<FluidStack> list);
}
