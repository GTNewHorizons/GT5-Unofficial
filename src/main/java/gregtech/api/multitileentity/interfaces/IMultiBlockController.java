package gregtech.api.multitileentity.interfaces;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.InventoryType;
import gregtech.api.gui.GUIHost;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;

public interface IMultiBlockController
    extends IMultiTileEntity, FluidInventoryLogicHost, ItemInventoryLogicHost, UpgradableMuTE, PowerLogicHost, GUIHost {

    boolean checkStructure(boolean aForceReset);

    /** Set the structure as having changed, and trigger an update */
    void onStructureChange();

    void registerCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void unregisterCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void registerCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    void unregisterCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    UUID registerItemInventory(int slots, int tier, @Nonnull InventoryType type, boolean isUpgradeInventory);

    ItemInventoryLogic unregisterItemInventory(@Nonnull UUID id, @Nonnull InventoryType type);

    void changeItemInventoryDisplayName(@Nonnull UUID id, @Nullable String displayName, @Nonnull InventoryType type);

    UUID registerFluidInventory(int tanks, long capacity, int tier, @Nonnull InventoryType type,
        boolean isUpgradeInventory);

    FluidInventoryLogic unregisterFluidInventory(@Nonnull UUID id, @Nonnull InventoryType type);

    void changeFluidInventoryDisplayName(@Nonnull UUID id, @Nullable String displayName, @Nonnull InventoryType type);

    void registerSpecialCasings(@Nonnull IMultiBlockPart part);
}
