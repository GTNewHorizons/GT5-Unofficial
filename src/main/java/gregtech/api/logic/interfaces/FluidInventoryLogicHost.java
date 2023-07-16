package gregtech.api.logic.interfaces;

import java.util.UUID;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.FluidInventoryLogic;

public interface FluidInventoryLogicHost {

    default FluidInventoryLogic getFluidLogic() {
        return getFluidLogic(InventoryType.Input);
    }

    FluidInventoryLogic getFluidLogic(InventoryType type);

    default FluidInventoryLogic getFluidLogic(InventoryType type, UUID id) {
        return getFluidLogic(type);
    }
}
