package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IItemInputLogicComponent;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.util.GTUtility;

public interface IMultiblockItemInputLogicComponent
    extends IItemInputLogicComponent, IHatchContainerComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IMultiblockItemInputLogicComponent> KEY = ComponentKey.unique();

    boolean tryAttachItemInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);

    @NotNull
    List<? extends MTEHatchInputBus> getItemInputHatches();

    @NotNull
    default Iterable<? extends MTEHatchInputBus> getValidItemInputHatches() {
        return validMTEList(getItemInputHatches());
    }

    short getHatchColors();

    @NotNull
    Map<GTUtility.ItemId, ItemStack> getStoredItemsFromME();
}
